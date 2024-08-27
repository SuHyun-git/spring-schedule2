package com.sparta.schedule.service;

import com.sparta.schedule.dto.UserRequestDto;
import com.sparta.schedule.dto.UserResponseDto;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = new User(userRequestDto);
        User saveUser = userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(saveUser);
        return userResponseDto;
    }

    public UserResponseDto findOneUser(Long userId) {
        User user = findUser(userId);
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }

    public User findUser(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("선택한 id는 존재하지 않습니다."));
    }

    public List<UserResponseDto> findAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseDto::new).toList();
    }

    public Long deleteUser(Long userId) {
        User user = findUser(userId);
        userRepository.delete(user);
        return userId;
    }
}
