package com.sparta.schedule.exception;

public class CanNotFindSchedule extends RuntimeException{
    public CanNotFindSchedule(){
        super("선택한 스케줄을 찾을 수 없습니다.");
    }
}


