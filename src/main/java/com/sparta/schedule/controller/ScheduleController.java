package com.sparta.schedule.controller;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
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

    @PostMapping("/schedule")
    public ScheduleResponseDto createSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto) {
        return scheduleService.createSchedule(scheduleRequestDto);
    }

    @GetMapping("/schedule/{scheduleId}")
    public ScheduleResponseDto getSchedule(@PathVariable Long scheduleId){
        return scheduleService.findOneSchedule(scheduleId);
    }

    @GetMapping("/schedule")
    public Page<ScheduleResponseDto> getAllSchedule( @RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "scheduleId") String sortBy,
                                                     @RequestParam(defaultValue = "false") boolean isAsc) {
        return scheduleService.getAllSchedules(page-1, size, sortBy, isAsc);
    }

    @PutMapping("/schedule/{scheduleId}")
    public  ScheduleResponseDto updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleRequestDto scheduleRequestDto){
        return scheduleService.updateSchedule(scheduleId, scheduleRequestDto);
    }
}
