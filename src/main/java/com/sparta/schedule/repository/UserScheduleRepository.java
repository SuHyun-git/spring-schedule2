package com.sparta.schedule.repository;

import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
}
