package com.sparta.schedule.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class UserRequestDto {
    private Long userId;
    private String userName;
    private String email;
}
