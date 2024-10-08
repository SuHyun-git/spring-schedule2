package com.sparta.schedule.controller;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.dto.UserScheduleResponseDto;
import com.sparta.schedule.service.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedule/{userId}")
    public ScheduleResponseDto createSchedule(@PathVariable Long userId, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.createSchedule(userId, scheduleRequestDto);
    }

    @GetMapping("/schedule/{scheduleId}")
    public UserScheduleResponseDto getSchedule(@PathVariable Long scheduleId){
        return scheduleService.findOneSchedule(scheduleId);
    }

    @GetMapping("/schedule")
    public Page<ScheduleResponseDto> getAllSchedule( @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "scheduleId") String sortBy,
                                                     @RequestParam(defaultValue = "false") boolean isAsc) {
        return scheduleService.getAllSchedules(page-1, size, sortBy, isAsc);
    }

    @PutMapping("/schedule/{scheduleId}/user/{userId}")
    public String updateSchedule(@PathVariable Long scheduleId, @PathVariable Long userId, @RequestBody ScheduleRequestDto scheduleRequestDto){
        return scheduleService.updateSchedule(scheduleId, userId, scheduleRequestDto);
    }

    // 일정을 작성한 유저는 추가로 일정 담당 유저들을 배치 할 수 있음으로
    // scheduleId와 userId(생성자인지 확인하기 위함), addUserId(user를 추가하기 위함)
    @PutMapping("/schedule/{scheduleId}/{userId}/{addUserId}")
    public UserScheduleResponseDto addUserInSchedule(@PathVariable Long userId, @PathVariable Long scheduleId, @PathVariable Long addUserId){
        return scheduleService.addUserInSchedule(userId, scheduleId, addUserId);
    }


    @DeleteMapping("/schedule/{scheduleId}/user/{userId}")
    public String delete(@PathVariable Long scheduleId, @PathVariable Long userId){
        return scheduleService.deleteSchedule(scheduleId, userId);
    }
}
