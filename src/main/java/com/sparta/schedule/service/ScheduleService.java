package com.sparta.schedule.service;

import com.sparta.schedule.dto.ScheduleRequestDto;
import com.sparta.schedule.dto.ScheduleResponseDto;
import com.sparta.schedule.dto.UserScheduleResponseDto;
import com.sparta.schedule.entity.Schedule;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserRoleEnum;
import com.sparta.schedule.entity.UserSchedule;
import com.sparta.schedule.exception.CanNotFindId;
import com.sparta.schedule.exception.CanNotFindSchedule;
import com.sparta.schedule.exception.DoNotHaveAuthority;
import com.sparta.schedule.repository.ScheduleRepository;
import com.sparta.schedule.repository.UserRepository;
import com.sparta.schedule.repository.UserScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final RestTemplate restTemplate;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, UserScheduleRepository userScheduleRepository, RestTemplateBuilder restTemplateBuilder) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.userScheduleRepository = userScheduleRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    public ScheduleResponseDto createSchedule(Long userId, ScheduleRequestDto scheduleRequestDto) {
        // 현재 날짜에 맞춰서 날씨를 구합니다.
        String weather = Weather();

        // schedule 생성하고 Dto 정보를 저장
        Schedule schedule = new Schedule(scheduleRequestDto, weather);

        // 생성자 넣기
        schedule.setUserId(userId);
        Schedule saveSchedule = scheduleRepository.save(schedule);

        // user를 userId로 찾은 뒤 user에 저장
        User user = userRepository.findById(userId).orElseThrow(()-> new CanNotFindId());

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
        Schedule schedule =scheduleRepository.findById(id).orElseThrow(() -> new CanNotFindSchedule());
        schedule.setCommentCount(schedule.getCommentList().size());
        List<UserSchedule> userScheduleList = new ArrayList<>();

        // schedule에 있는 list는 id만 가지고 있기 때문에 userScheduleRepository에서 id를 검색하여 찾아옵니다.
        for (UserSchedule userSchedule : schedule.getUserScheduleList()) {
            userScheduleList.add(userScheduleRepository.findById(userSchedule.getUserScheduleId()).orElseThrow(()-> new CanNotFindId()));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindId());
        if(user.getRole() == UserRoleEnum.ADMIN) {
            Schedule schedule = findSchedule(id);
            Schedule updateSchedule = schedule.update(scheduleRequestDto);
            ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(updateSchedule);
            return "수정 완료";
        } else {
            throw new DoNotHaveAuthority();
        }
    }

    public Schedule findSchedule(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new CanNotFindId());
    }

    public String deleteSchedule(Long scheduleId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CanNotFindId());
        if(user.getRole() == UserRoleEnum.ADMIN) {
            Schedule schedule = findSchedule(scheduleId);
            scheduleRepository.delete(schedule);
            return "삭제 완료";
        } else {
            throw new DoNotHaveAuthority();
        }
    }

    public UserScheduleResponseDto addUserInSchedule(Long userId, Long scheduleId, Long addUserId) {
        Schedule schedule = findSchedule(scheduleId);
        if(schedule.getUserId() == userId) {
            // user를 userId로 찾은 뒤 user에 저장
            User user = userRepository.findById(addUserId).orElseThrow(()-> new CanNotFindId());

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

    public String Weather() {
        // 작성하는 날짜를 원하는 형식으로 변경하여 구합니다.
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        String today = now.format(formatter);

        // 날씨 정보 데이터에 접근하여 정보를 가져옵니다.
        URI uri = UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);

        // 정보를 String에 저장합니다.
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonString = response.getBody();

        // JSON을 파싱하여 원하는 날씨 정보를 가져옵니다.
        JSONArray weathers = new JSONArray(jsonString);
        String weather = "";
        for(int i = 0; i<weathers.length(); i++) {
            JSONObject jsonObject = weathers.getJSONObject(i);
            // 날짜가 일치하는 weather값을 반환한다.
            if(jsonObject.get("date").equals(today)) {
                weather = (jsonObject.getString("weather"));
            }
        }

        return weather;
    }
}
