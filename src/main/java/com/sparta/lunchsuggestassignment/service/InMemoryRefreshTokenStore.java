package com.sparta.lunchsuggestassignment.service;

import com.sparta.lunchsuggestassignment.repository.RefreshTokenStore;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenStore implements RefreshTokenStore {
    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    @Override
    public void save(String email, String refreshToken) {
        store.put(email, refreshToken);
    }

    @Override
    public String get(String email) {
        return store.get(email);
    }
}
