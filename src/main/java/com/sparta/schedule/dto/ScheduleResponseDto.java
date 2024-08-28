package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private Long userId;
    private String todoTitle;
    private String todoContents;
    private String weather;
    private int commentCount;
    private List<Comment> commentList;


    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.userId = schedule.getUserId();
        this.commentCount = schedule.getCommentCount();
        this.todoTitle = schedule.getTodoTitle();
        this.todoContents = schedule.getTodoContents();
        this.weather = schedule.getWeather();
        this.commentList = schedule.getCommentList();
    }
}
