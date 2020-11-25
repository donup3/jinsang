package com.samplesecurity.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//전역 에러처리
@ControllerAdvice
public class SampleControllerAdvice {

    @ExceptionHandler(SampleException.class)
    public String notFound(){
        return "sampleEx";
    }
}
