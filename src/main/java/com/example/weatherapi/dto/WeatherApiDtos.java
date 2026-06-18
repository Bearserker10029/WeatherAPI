package com.example.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * DTOs de deserialización para la respuesta de WeatherAPI.
 * Se mantienen en un solo archivo para minimizar archivos.
 * Jackson mapea el JSON externo a estos objetos usando @JsonIgnoreProperties
 * para ignorar campos que no nos interesan.
 */
public final class WeatherApiDtos {

    private WeatherApiDtos() {}

    // ===== /v1/current.json =====
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentResponse {
        private Current current;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Current {
            private double tempC;          // temp_c
            private Condition condition;
            private double feelslikeC;     // feelslike_c
            private int humidity;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Condition {
                private String text;
            }
        }
    }

    // ===== /v1/forecast.json =====
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForecastResponse {
        private Forecast forecast;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Forecast {
            private List<ForecastDay> forecastday;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ForecastDay {
                private String date;
                private List<Hour> hour;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Hour {
                    private String time;     // "2025-06-08 00:00"
                    private double tempC;    // temp_c
                    private Condition condition;

                    @Data
                    @JsonIgnoreProperties(ignoreUnknown = true)
                    public static class Condition {
                        private String text;
                    }
                }
            }
        }
    }
}
