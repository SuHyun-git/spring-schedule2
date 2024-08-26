package com.sparta.schedule.controller;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.dto.CommentResponseDto;
import com.sparta.schedule.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/{scheduleId}")
    public CommentResponseDto createComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(scheduleId, commentRequestDto);
    }

    @GetMapping("/comment/{commentId}")
    public CommentResponseDto getOneComment(@PathVariable Long commentId){
        return commentService.findOneComment(commentId);
    }

    @GetMapping("/comment")
    public List<CommentResponseDto> getAllComments() {
        return commentService.findAllComments();
    }

    @PutMapping("/comment/{commentId}")
    public CommentResponseDto updateSchedule(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.updateComment(commentId, commentRequestDto);
    }

    @DeleteMapping("/comment/{commentId}")
    public Long deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }

}
