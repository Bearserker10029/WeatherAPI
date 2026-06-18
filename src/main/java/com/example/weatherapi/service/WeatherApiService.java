package com.example.weatherapi.service;

import com.example.weatherapi.dto.ClimaActualDto;
import com.example.weatherapi.dto.WeatherApiDtos;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherApiService {

    @Value("${weather.api.key:88e12060abad41ab97212738250906}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Ejercicio 1: clima actual por ciudad.
     * Devuelve temp_c, condition, feelslike_c, humidity.
     */
    public ClimaActualDto obtenerClimaActual(String ciudad) {
        String url = String.format(
                "https://api.weatherapi.com/v1/current.json?key=%s&q=%s",
                apiKey, ciudad);

        WeatherApiDtos.CurrentResponse response = restTemplate.getForObject(url, WeatherApiDtos.CurrentResponse.class);

        if (response == null || response.getCurrent() == null) {
            throw new RuntimeException("No se pudo obtener datos de: " + ciudad);
        }

        WeatherApiDtos.CurrentResponse.Current current = response.getCurrent();
        return new ClimaActualDto(
                current.getTempC(),
                current.getCondition().getText(),
                current.getFeelslikeC(),
                current.getHumidity()
        );
    }

    /**
     * Ejercicio 2: pronóstico por hora del día de hoy.
     * Devuelve { city, forecast: [ { hour, temp_c, condition } ] }
     */
    public PronosticoResponse obtenerPronosticoHoras(String ciudad) {
        String url = String.format(
                "https://api.weatherapi.com/v1/forecast.json?key=%s&q=%s&days=1",
                apiKey, ciudad);

        WeatherApiDtos.ForecastResponse response = restTemplate.getForObject(url, WeatherApiDtos.ForecastResponse.class);

        List<PronosticoHora> horas = new ArrayList<>();
        if (response != null && response.getForecast() != null
                && response.getForecast().getForecastday() != null
                && !response.getForecast().getForecastday().isEmpty()) {

            String hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            for (WeatherApiDtos.ForecastResponse.Forecast.ForecastDay day
                    : response.getForecast().getForecastday()) {
                if (hoy.equals(day.getDate()) && day.getHour() != null) {
                    for (WeatherApiDtos.ForecastResponse.Forecast.ForecastDay.Hour h : day.getHour()) {
                        // El campo time viene "2025-06-08 00:00" -> nos quedamos con "00:00"
                        String hora = h.getTime();
                        if (hora != null && hora.contains(" ")) {
                            hora = hora.split(" ")[1];
                        }
                        horas.add(new PronosticoHora(hora, h.getTempC(), h.getCondition().getText()));
                    }
                }
            }
        }
        return new PronosticoResponse(ciudad, horas);
    }

    // ===== DTOs de salida mínimos, en el mismo archivo =====

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PronosticoResponse {
        @JsonProperty("city")
        private String city;
        @JsonProperty("forecast")
        private List<PronosticoHora> forecast;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PronosticoHora {
        @JsonProperty("hour")
        private String hour;
        @JsonProperty("temp_c")
        private double tempC;
        @JsonProperty("condition")
        private String condition;
    }
}
