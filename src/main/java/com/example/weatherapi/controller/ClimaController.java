package com.example.weatherapi.controller;

import com.example.weatherapi.dto.ApiResponse;
import com.example.weatherapi.dto.ClimaActualDto;
import com.example.weatherapi.dto.MonitoreoClimaticoRequest;
import com.example.weatherapi.entity.MonitoreoClimatico;
import com.example.weatherapi.service.MonitoreoClimaticoService;
import com.example.weatherapi.service.WeatherApiService;
import com.example.weatherapi.service.WeatherApiService.PronosticoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clima")
public class ClimaController {

    private final WeatherApiService weatherApiService;
    private final MonitoreoClimaticoService monitoreoService;

    public ClimaController(WeatherApiService weatherApiService,
                           MonitoreoClimaticoService monitoreoService) {
        this.weatherApiService = weatherApiService;
        this.monitoreoService = monitoreoService;
    }

    // Ejercicio 1: clima actual
    @GetMapping("/actual/{ciudad}")
    public ResponseEntity<ApiResponse<ClimaActualDto>> obtenerClimaActual(@PathVariable String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("La ciudad es obligatoria"));
        }
        try {
            ClimaActualDto clima = weatherApiService.obtenerClimaActual(ciudad);
            return ResponseEntity.ok(ApiResponse.success("OK - Clima actual obtenido correctamente", clima));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener el clima actual: " + e.getMessage()));
        }
    }

    // Ejercicio 2: pronóstico por hora del día actual
    @GetMapping("/pronostico/{ciudad}")
    public ResponseEntity<ApiResponse<PronosticoResponse>> obtenerPronosticoPorHora(@PathVariable String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("La ciudad es obligatoria"));
        }
        try {
            PronosticoResponse pronostico = weatherApiService.obtenerPronosticoHoras(ciudad);
            return ResponseEntity.ok(ApiResponse.success("OK - Pronóstico obtenido correctamente", pronostico));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener el pronóstico: " + e.getMessage()));
        }
    }

    // Ejercicio 3: guardar monitoreo
    @PostMapping("/monitoreo")
    public ResponseEntity<ApiResponse<MonitoreoClimatico>> guardarMonitoreo(
            @Valid @RequestBody MonitoreoClimaticoRequest request) {
        try {
            MonitoreoClimatico saved = monitoreoService.guardarMonitoreo(request);
            return ResponseEntity.ok(
                    ApiResponse.success("OK - Monitoreo climático guardado correctamente", saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al guardar el monitoreo: " + e.getMessage()));
        }
    }
}
