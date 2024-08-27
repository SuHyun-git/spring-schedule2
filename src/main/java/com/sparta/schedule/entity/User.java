package com.sparta.schedule.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.schedule.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "userName", length = 100)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UserSchedule> userScheduleList = new ArrayList<>();

    public void addUserScheduleList(UserSchedule userSchedule) {
        this.userScheduleList.add(userSchedule);
        userSchedule.setUser(this);
    }
    public User(UserRequestDto userRequestDto) {
        this.userName = userRequestDto.getUserName();
        this.email = userRequestDto.getEmail();
    }
}
