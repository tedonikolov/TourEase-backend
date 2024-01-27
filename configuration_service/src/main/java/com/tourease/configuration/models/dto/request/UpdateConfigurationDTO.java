package com.tourease.configuration.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateConfigurationDTO {
    private String name;
    private String value;
}
