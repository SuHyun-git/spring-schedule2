package com.sparta.schedule.exception;

public class CanNotFindPassword extends RuntimeException {
    public CanNotFindPassword() {
        super("비밀번호를 찾을 수 없습니다.");
    }
}