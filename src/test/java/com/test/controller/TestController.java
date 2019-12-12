package com.test.controller;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Test
    public void helloMethod() {

        double d = 114.144;
        String test = String.format("%.2f", d);
        System.out.println(test);
    }
}
