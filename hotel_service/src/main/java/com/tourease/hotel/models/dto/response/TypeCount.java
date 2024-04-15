package com.tourease.hotel.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TypeCount {
    private Long id;
    private String name;
    private int count;
}
