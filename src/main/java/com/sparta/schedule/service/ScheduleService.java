package com.sparta.schedule.service;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserSchedule;
import com.sparta.schedule.repository.ScheduleRepository;
import com.sparta.schedule.repository.UserRepository;
import com.sparta.schedule.repository.UserScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final UserScheduleRepository userScheduleRepository;


    public ScheduleResponseDto createSchedule(Long userId, ScheduleRequestDto scheduleRequestDto) {
        // schedule 생성하고 Dto 정보를 저장
        Schedule schedule = new Schedule(scheduleRequestDto);
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // user를 userId로 찾은 뒤 user에 저장
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("찾는 id가 존재하지 않습니다."));

        // userschedule과 User, Schedule에 각각 저장하기
        UserSchedule userSchedule = new UserSchedule(user, schedule);
        userScheduleRepository.save(userSchedule);
        user.addUserScheduleList(userSchedule);
        saveSchedule.addUserScheduleList(userSchedule);

        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(saveSchedule);
        return scheduleResponseDto;
    }

    public Page<ScheduleResponseDto> getAllSchedules(int page,int size,String sortBy,boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> schedulesList;
        schedulesList = scheduleRepository.findAll(pageable);
        for (Schedule schedule : schedulesList) {
            schedule.setCommentCount(schedule.getCommentList().size());
        }
        return schedulesList.map(ScheduleResponseDto::new);
    }

    public ScheduleResponseDto findOneSchedule(Long id) {
        Schedule schedule =scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다."));
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule);
        return scheduleResponseDto;
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule = findSchedule(id);
        Schedule updateSchedule = schedule.update(scheduleRequestDto);
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(updateSchedule);
        return scheduleResponseDto;
    }

    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다."));
    }

    public Long deleteSchedule(Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        scheduleRepository.delete(schedule);
        return scheduleId;
    }
}
