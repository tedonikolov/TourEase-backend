package com.tourease.hotel.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiTypeVO{
    private String name;
    private int count;
    private int people;
}
