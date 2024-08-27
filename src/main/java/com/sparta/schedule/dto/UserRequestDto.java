package com.sparta.schedule.dto;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class UserRequestDto {
    private Long userId;
    private String password;
    private String userName;
    private String email;
    private boolean admin = false;
    private String adminToken = "";
}
