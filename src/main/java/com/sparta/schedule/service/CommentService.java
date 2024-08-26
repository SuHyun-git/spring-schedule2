package com.sparta.schedule.service;

import com.sparta.schedule.dto.CommentRequestDto;
import com.sparta.schedule.dto.CommentResponseDto;
import com.sparta.schedule.entity.Comment;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.repository.CommentRepository;
import com.sparta.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }


    public CommentResponseDto createComment(Long scheduleId,CommentRequestDto commentRequestDto) {
        Comment comment = new Comment(commentRequestDto);
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다."));
        comment.setSchedule(schedule);
        Comment saveComment = commentRepository.save(comment);

        schedule.addCommentList(saveComment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

    public List<CommentResponseDto> findAllComments() {
        return commentRepository.findAll().stream().map(CommentResponseDto::new).toList();
    }

    public CommentResponseDto findOneComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("선택한 id가 존재하지 않습니다."));
        CommentResponseDto commentRequestDto = new CommentResponseDto(comment);
        return commentRequestDto;
    }


    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("선택한 id가 존재하지 않습니다."));
    }


    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = findComment(commentId);
        Comment updateComment = comment.update(commentRequestDto);
        CommentResponseDto commentResponseDto = new CommentResponseDto(updateComment);
        return commentResponseDto;
    }
}
