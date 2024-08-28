package com.sparta.schedule.exception;

public class CanNotFindEmail extends RuntimeException{
    public CanNotFindEmail() {
        super("Email을 찾을 수 없습니다.");
    }
}
