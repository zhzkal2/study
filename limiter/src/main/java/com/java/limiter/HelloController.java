package com.java.limiter;


import com.java.limiter.annotation.RateLimited;
import com.java.limiter.annotation.RequestType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RateLimited(type = RequestType.LOGIN)
    @GetMapping(value = "/api/test")
    public ResponseEntity<String> test() {

        // API 호출시 토큰 1개를 소비
        return ResponseEntity.ok("Request successful!1");
    }

    @RateLimited(type = RequestType.QUERY)
    @GetMapping(value = "/api/test2")
    public ResponseEntity<String> test2() {

        // API 호출시 토큰 1개를 소비
        return ResponseEntity.ok("Request successful!2");
    }
}