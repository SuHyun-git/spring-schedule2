package com.sparta.schedule.controller;

import com.sparta.schedule.dto.LoginRequestDto;
import com.sparta.schedule.dto.UserRequestDto;
import com.sparta.schedule.dto.UserResponseDto;
import com.sparta.schedule.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody LoginRequestDto requestDto, HttpServletResponse res) {
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return "로그인 실패";
        }
        return "로그인 성공";
    }

    @GetMapping("/user/{userId}")
    public UserResponseDto findOneUser(@PathVariable Long userId){
        return userService.findOneUser(userId);
    }

    @GetMapping("/user")
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUsers();
    }

    @DeleteMapping("/user/{userId}")
    public Long deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }

}
