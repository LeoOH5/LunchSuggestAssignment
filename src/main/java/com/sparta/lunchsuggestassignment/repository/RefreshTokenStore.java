package com.sparta.lunchsuggestassignment.repository;

public interface RefreshTokenStore {
    void save(String email, String refreshToken);
    String get(String email);
}
