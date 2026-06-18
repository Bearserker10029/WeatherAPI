package com.example.weatherapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "monitoreo_climatico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoreoClimatico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "temp_promedio", nullable = false)
    private double tempPromedio;

    @Column(name = "condicion_frecuente", nullable = false)
    private String condicionFrecuente;

    @Column(name = "temp_max", nullable = false)
    private double tempMax;

    @Column(name = "temp_min", nullable = false)
    private double tempMin;
}
