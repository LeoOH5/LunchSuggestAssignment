package com.sparta.lunchsuggestassignment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @Min(0)
    private int price;
}

