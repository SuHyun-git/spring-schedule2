package com.sparta.schedule.exception;

public class DoNotHaveAuthority extends RuntimeException {
    public DoNotHaveAuthority(){
        super("권한이 없습니다.");
    }
}
