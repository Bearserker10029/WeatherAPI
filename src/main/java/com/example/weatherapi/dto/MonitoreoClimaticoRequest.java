package com.example.weatherapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MonitoreoClimaticoRequest {
    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La temperatura promedio es obligatoria")
    private Double tempPromedio;

    @NotBlank(message = "La condición más frecuente es obligatoria")
    private String condicionMasFrecuente;

    @NotNull(message = "La temperatura máxima es obligatoria")
    private Double tempMax;

    @NotNull(message = "La temperatura mínima es obligatoria")
    private Double tempMin;
}
