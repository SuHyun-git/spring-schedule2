package com.sparta.schedule.dto;

import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String writer;
    private String contents;
    private Schedule schedule;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.writer = comment.getWriter();
        this.contents = comment.getContents();
        this.schedule = comment.getSchedule();
    }




}
