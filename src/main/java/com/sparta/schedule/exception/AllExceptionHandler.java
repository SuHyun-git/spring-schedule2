package com.sparta.schedule.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AllExceptionHandler {

    // 로그인 id 틀리면 401
    @ExceptionHandler(CanNotFindEmail.class)
    public ResponseEntity<String> CanNotFindEmail(CanNotFindEmail ex) {
        log.error(ex + ": " +ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 비밀번호 틀리면 401
    @ExceptionHandler(CanNotFindPassword.class)
    public ResponseEntity<String> CanNotFindPassword(CanNotFindPassword ex) {
        log.error(ex + ": " +ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 권한이 없으면 403 반환
    @ExceptionHandler(DoNotHaveAuthority.class)
    public ResponseEntity<String> DoNotHaveAuthority(DoNotHaveAuthority ex) {
        log.error(ex + ": " +ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CanNotFindSchedule.class)
    public ResponseEntity<String> CanNotFindSchedule(CanNotFindSchedule ex) {
        log.error(ex + ": " +ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CanNotFindId.class)
    public ResponseEntity<String> CanNotFindId(CanNotFindId ex) {
        log.error(ex + ": " +ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception ex) {
        log.error(ex + ": " +ex.getMessage());
        // e.printStackTrace()
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
