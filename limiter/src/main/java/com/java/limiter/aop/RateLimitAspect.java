package com.java.limiter.aop;

import com.java.limiter.BucketConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {


    private final BucketConfig bucketConfig;
    private final HttpServletRequest request;


    // @RateLimited 어노테이션이 붙은 메서드를 처리
    @Around("@annotation(com.java.limiter.annotation.RateLimited)")
    public Object applyRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            System.out.println(request);
            String ip = request.getRemoteAddr();
            Bucket bucket = bucketConfig.getBucketForIp(ip);

            // rate limiting 처리
            if (bucket.tryConsume(1)) {  // 요청을 1개 토큰만 소비
                return joinPoint.proceed(); // 메서드 실행
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many requests");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }


}
