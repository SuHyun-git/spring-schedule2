package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class UserScheduleResponseDto {
    private Long scheduleId;
    private String todoTitle;
    private String todoContents;
    private int commentCount;
    private List<User> user;

    public UserScheduleResponseDto(Schedule schedule, List<User> userScheduleList) {
        this.scheduleId = schedule.getScheduleId();
        this.todoTitle = schedule.getTodoTitle();
        this.todoContents = schedule.getTodoContents();
        this.commentCount = schedule.getCommentCount();
        this.user = userScheduleList;
    }
}
