package com.sparta.schedule.dto;

import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserSchedule;
import lombok.Getter;

import java.util.List;

@Getter
public class UserResponseDto {
    private Long userId;
    private String userName;
    private String email;
    private List<UserSchedule> UserSchedules;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.UserSchedules = user.getUserScheduleList();
    }
}
