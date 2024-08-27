package com.sparta.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.schedule.dto.ScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
public class Schedule extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @Column(name="todoTitle", length = 100)
    private String todoTitle;

    @Column(name = "todoContents", nullable = false, length = 500)
    private String todoContents;

    @Column(name = "commentCount")
    private int commentCount;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserSchedule> userScheduleList = new ArrayList<>();


    public void addCommentList(Comment comment){
        this.commentList.add(comment);
        comment.setSchedule(this);
    }

    public void addUserScheduleList(UserSchedule userSchedule){
        this.userScheduleList.add(userSchedule);
        userSchedule.setSchedule(this);
    }

    public Schedule(ScheduleRequestDto scheduleRequestDto) {
        this.scheduleId = scheduleRequestDto.getScheduleId();
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
    }

    public Schedule update(ScheduleRequestDto scheduleRequestDto) {
        this.todoTitle = scheduleRequestDto.getTodoTitle();
        this.todoContents = scheduleRequestDto.getTodoContents();
        return this;
    }
}
