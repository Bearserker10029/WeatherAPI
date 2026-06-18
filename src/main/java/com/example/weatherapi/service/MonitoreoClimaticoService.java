package com.example.weatherapi.service;

import com.example.weatherapi.dto.MonitoreoClimaticoRequest;
import com.example.weatherapi.entity.MonitoreoClimatico;
import com.example.weatherapi.repository.MonitoreoClimaticoRepository;
import org.springframework.stereotype.Service;

@Service
public class MonitoreoClimaticoService {

    private final MonitoreoClimaticoRepository repository;

    public MonitoreoClimaticoService(MonitoreoClimaticoRepository repository) {
        this.repository = repository;
    }

    public MonitoreoClimatico guardarMonitoreo(MonitoreoClimaticoRequest request) {
        validarDatosClimaticos(request);

        MonitoreoClimatico entity = MonitoreoClimatico.builder()
                .ciudad(request.getCiudad())
                .fecha(request.getFecha())
                .tempPromedio(request.getTempPromedio())
                .condicionFrecuente(request.getCondicionMasFrecuente())
                .tempMax(request.getTempMax())
                .tempMin(request.getTempMin())
                .build();

        return repository.save(entity);
    }

    private void validarDatosClimaticos(MonitoreoClimaticoRequest request) {
        if (request.getTempMin() > request.getTempMax()) {
            throw new IllegalArgumentException("La temperatura mínima no puede ser mayor que la máxima");
        }
        if (request.getTempMin() > request.getTempPromedio()) {
            throw new IllegalArgumentException("La temperatura mínima no puede ser mayor que la promedio");
        }
        if (request.getTempPromedio() > request.getTempMax()) {
            throw new IllegalArgumentException("La temperatura promedio no puede ser mayor que la máxima");
        }
    }
}
