package com.sparta.schedule.service;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.dto.UserScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserRoleEnum;
import com.sparta.schedule.entity.UserSchedule;
import com.sparta.schedule.repository.ScheduleRepository;
import com.sparta.schedule.repository.UserRepository;
import com.sparta.schedule.repository.UserScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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

    public UserScheduleResponseDto findOneSchedule(Long id) {
        Schedule schedule =scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다."));
        schedule.setCommentCount(schedule.getCommentList().size());
        List<UserSchedule> userScheduleList = new ArrayList<>();

        // schedule에 있는 list는 id만 가지고 있기 때문에 userScheduleRepository에서 id를 검색하여 찾아옵니다.
        for (UserSchedule userSchedule : schedule.getUserScheduleList()) {
            userScheduleList.add(userScheduleRepository.findById(userSchedule.getUserScheduleId()).orElseThrow(()-> new IllegalArgumentException("입력한 Id가 없습니다.")));
        }

        List<User> userList = new ArrayList<>();
        for (UserSchedule userSchedule : userScheduleList) {
            userList.add(userSchedule.getUser());
        }

        UserScheduleResponseDto userScheduleResponseDto = new UserScheduleResponseDto(schedule, userList);
        return userScheduleResponseDto;
    }

    @Transactional
    public String updateSchedule(Long id, Long userId, ScheduleRequestDto scheduleRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user id를 잘못 입력하였습니다."));
        if(user.getRole() == UserRoleEnum.ADMIN) {
            Schedule schedule = findSchedule(id);
            Schedule updateSchedule = schedule.update(scheduleRequestDto);
            ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(updateSchedule);
            return "수정 완료";
        } else {
            return "권한이 없습니다.";
        }
    }

    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("선택한 스케줄이 존재하지 않습니다."));
    }

    public String deleteSchedule(Long scheduleId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("user id를 잘못 입력하였습니다."));
        if(user.getRole() == UserRoleEnum.ADMIN) {
            Schedule schedule = findSchedule(scheduleId);
            scheduleRepository.delete(schedule);
            return "삭제 완료";
        } else {
            return "권한이 없습니다.";
        }
    }

    public UserScheduleResponseDto addUserInSchedule(Long userId, Long scheduleId, Long addUserId) {
        Schedule schedule = findSchedule(scheduleId);
        if(schedule.getUserScheduleList().get(0).getUser().getUserId() == userId) {
            // user를 userId로 찾은 뒤 user에 저장
            User user = userRepository.findById(addUserId).orElseThrow(()-> new IllegalArgumentException("찾는 id가 존재하지 않습니다."));

            // userschedule과 User, Schedule에 각각 저장하기
            UserSchedule userSchedule = new UserSchedule(user, schedule);
            userScheduleRepository.save(userSchedule);
            user.addUserScheduleList(userSchedule);

            // schedule에도 추가해주기
            schedule.addUserScheduleList(userSchedule);

            List<User> userList = new ArrayList<>();
            for (UserSchedule us : schedule.getUserScheduleList()) {
                userList.add(us.getUser());
            }

            UserScheduleResponseDto userScheduleResponseDto = new UserScheduleResponseDto(schedule, userList);
            return userScheduleResponseDto;
        } else {
            log.error("잘못된 입력입니다.");
            return null;
        }
    }
}
