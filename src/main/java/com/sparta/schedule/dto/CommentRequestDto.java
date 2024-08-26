package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long commentId;
    private String writer;
    private String contents;
    private Schedule schedule;

}
