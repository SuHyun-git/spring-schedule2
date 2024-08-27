package com.sparta.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "userschedule")
@Setter
@Getter
@NoArgsConstructor
public class UserSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userScheduleId;

    @ManyToOne
    @JoinColumn(name = "user_Id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_Id")
    @JsonIgnore
    private Schedule schedule;

    public UserSchedule(User user, Schedule schedule) {
        this.user = user;
        this.schedule = schedule;
    }
}
