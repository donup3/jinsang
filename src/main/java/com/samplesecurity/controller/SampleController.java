package com.samplesecurity.controller;

import com.samplesecurity.exception.SampleException;
import com.samplesecurity.exception.SampleException2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {
    @GetMapping("/sampleex")
    public String sample(){
        throw new SampleException();
    }

    @GetMapping("/sampleex2")
    public String sample2(){
        throw new SampleException2();
    }

    //컨트롤러 에러처리
    @ExceptionHandler({SampleException.class, SampleException2.class})
    @ResponseBody
    public String sampleHandler(){
        return "/sampleEx";
    }
}
