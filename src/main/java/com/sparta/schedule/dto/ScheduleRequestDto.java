package com.sparta.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    private Long scheduleId;
    private String todoTitle;
    private String todoContents;
}
