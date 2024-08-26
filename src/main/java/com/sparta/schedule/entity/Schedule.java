package com.sparta.schedule.entity;

import com.sparta.schedule.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor
public class Schedule extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name="todoTitle", length = 100)
    private String todoTitle;

    @Column(name = "todoContents", nullable = false, length = 500)
    private String todoContents;

    @OneToMany
    private List<Comment> commentList = new ArrayList<>();


    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.scheduleId = scheduleRequestDto.getScheduleId();
        this.username = scheduleRequestDto.getUsername();
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
    }

    public Schedule update(ScheduleRequestDto scheduleRequestDto) {
        this.username = scheduleRequestDto.getUsername();
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
        return this;
    }
}
