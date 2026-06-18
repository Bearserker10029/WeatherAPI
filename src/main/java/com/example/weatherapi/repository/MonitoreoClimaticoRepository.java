package com.example.weatherapi.repository;

import com.example.weatherapi.entity.MonitoreoClimatico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoreoClimaticoRepository  extends JpaRepository<MonitoreoClimatico, Long> {
}
