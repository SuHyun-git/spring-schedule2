package com.sparta.schedule.entity;

import com.sparta.schedule.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(name = "writer", length = 100)
    private String writer;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "schedule")
    private Schedule schedule;

    public Comment(CommentRequestDto commentRequestDto){
        this.commentId = commentRequestDto.getCommentId();
        this.writer = commentRequestDto.getWriter();
        this.contents = commentRequestDto.getContents();
        this.schedule = commentRequestDto.getSchedule();
    }

    public Comment update(CommentRequestDto commentRequestDto) {
        this.writer = commentRequestDto.getWriter();
        this.contents = commentRequestDto.getContents();
        return this;
    }
}
