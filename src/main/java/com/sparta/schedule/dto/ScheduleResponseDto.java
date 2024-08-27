package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private String todoTitle;
    private String todoContents;
    private int commentCount;
    private List<Comment> commentList;


    public ScheduleResponseDto(ScheduleRequestDto scheduleRequestDto) {
        this.scheduleId = scheduleRequestDto.getScheduleId();
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.commentCount = schedule.getCommentCount();
        this.todoTitle = schedule.getTodoTitle();
        this.todoContents = schedule.getTodoContents();
        this.commentList = schedule.getCommentList();
    }
}
