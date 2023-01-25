package com.github.chuettenrauch.mixifyapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @GetMapping
    public String test() {
        return "OK";
    }

    @GetMapping("/bla")
    public String bla() {
        return "blubb";
    }
}
