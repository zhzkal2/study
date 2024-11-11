package com.java.limiter;


import com.java.limiter.annotation.RateLimited;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RateLimited
    @GetMapping(value = "/api/test")
    public ResponseEntity<String> test() {

        // API 호출시 토큰 1개를 소비
        return ResponseEntity.ok("Request successful!");
    }
}