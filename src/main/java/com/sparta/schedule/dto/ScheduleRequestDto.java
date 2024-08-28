package com.sparta.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    private Long scheduleId;
    private Long userId;
    private String todoTitle;
    private String todoContents;
}
