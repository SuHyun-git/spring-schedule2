package com.sparta.schedule.exception;

public class CanNotFindId extends RuntimeException{
    public CanNotFindId() {
        super("ID을 찾을 수 없습니다.");
    }
}
