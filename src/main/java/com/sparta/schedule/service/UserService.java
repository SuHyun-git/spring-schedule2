package com.sparta.schedule.service;

import com.sparta.schedule.config.PasswordEncoder;
import com.sparta.schedule.dto.UserRequestDto;
import com.sparta.schedule.dto.UserResponseDto;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class  UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    // ADMIN_TOKEN
//    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // email 중복확인
        String email = userRequestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        User user = new User(userRequestDto);

        // password를 암호화 해서 넣기
        String password = passwordEncoder.encode(userRequestDto.getPassword());

        user.setPassword(password);
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
