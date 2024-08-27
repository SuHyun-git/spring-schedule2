package com.sparta.schedule.dto;

import lombok.Getter;

@Getter
public class ScheduleRequestDto {
    private Long scheduleId;
    private String todoTitle;
    private String todoContents;
}
