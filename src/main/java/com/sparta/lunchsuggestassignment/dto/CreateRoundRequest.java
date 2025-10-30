package com.sparta.lunchsuggestassignment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoundRequest {

    @NotNull
    private LocalDate date;

    @NotNull
    @Size(min = 1)
    private List<@Valid MenuCreateRequest> menus;
}
