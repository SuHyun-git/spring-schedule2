package com.sparta.schedule.service;

import com.sparta.schedule.config.PasswordEncoder;
import com.sparta.schedule.dto.LoginRequestDto;
import com.sparta.schedule.dto.UserRequestDto;
import com.sparta.schedule.dto.UserResponseDto;
import com.sparta.schedule.entity.User;
import com.sparta.schedule.entity.UserRoleEnum;
import com.sparta.schedule.exception.CanNotFindEmail;
import com.sparta.schedule.exception.CanNotFindId;
import com.sparta.schedule.exception.CanNotFindPassword;
import com.sparta.schedule.jwt.JwtUtil;
import com.sparta.schedule.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class  UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // email 중복확인
        String email = userRequestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (userRequestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(userRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(userRequestDto);

        // password를 암호화 해서 넣기
        String password = passwordEncoder.encode(userRequestDto.getPassword());
        // role(권한) 넣기
        user.setRole(role);

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
        return userRepository.findById(userId).orElseThrow(()->new CanNotFindId());
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

    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(()->new CanNotFindEmail());

        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new CanNotFindPassword();
        }

        // JWT 생성 및 쿠키에 저장 후 쿠키에 추가
        String token = jwtUtil.createToken(user.getEmail(), user.getRole());
        jwtUtil.addJwtToCookie(token, res);
    }
}
