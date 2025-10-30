package com.sparta.lunchsuggestassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoundResponse {
    private Long id;
    private Long userId;
    private LocalDate date;
    private List<MenuResponse> menus;
}
