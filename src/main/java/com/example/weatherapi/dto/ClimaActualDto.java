package com.example.weatherapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClimaActualDto {
    @JsonProperty("temp_c")
    private double tempC;

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("feelslike_c")
    private double feelslikeC;

    @JsonProperty("humidity")
    private int humidity;
}
