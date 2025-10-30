package com.sparta.lunchsuggestassignment.service;

import com.sparta.lunchsuggestassignment.dto.CreateRoundRequest;
import com.sparta.lunchsuggestassignment.dto.RoundResponse;
import com.sparta.lunchsuggestassignment.entity.User;
import com.sparta.lunchsuggestassignment.repository.LunchRoundRepository;
import com.sparta.lunchsuggestassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final LunchRoundRepository roundRepository;
    private final UserRepository userRepository;




}
