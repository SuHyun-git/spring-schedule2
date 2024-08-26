package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class ScheduleResponseDto {
    private Long scheduleId;
    private String username;
    private String todoTitle;
    private String todoContents;


    public ScheduleResponseDto(ScheduleRequestDto scheduleRequestDto) {
        this.scheduleId = scheduleRequestDto.getScheduleId();
        this.username = scheduleRequestDto.getUsername();
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
    }

    public ScheduleResponseDto(Schedule schedule) {
        this.scheduleId = schedule.getScheduleId();
        this.username = schedule.getUsername();
        this.todoTitle = schedule.getTodoTitle();
        this.todoContents = schedule.getTodoContents();
    }
}
