

package com.java.limiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class BucketConfig {


    private final ConcurrentHashMap<String, Bucket> loginBuckets = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Bucket> queryBuckets = new ConcurrentHashMap<>();



    public Bucket getBucketForLogin(String ip) {
        System.out.println("로그인 관련 요청 IP : " + ip);
        return loginBuckets.computeIfAbsent(ip, k -> createBucket(5,Duration.ofHours(1)));
    }

    public Bucket getBucketForQuery(String ip) {
        System.out.println("조회 관련 요청 IP : " + ip);
        return queryBuckets.computeIfAbsent(ip, k -> createBucket(5,Duration.ofSeconds(5)));
    }




    // 공통된 Bucket 생성 메서드
    private Bucket createBucket(int capacity, Duration refillDuration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, refillDuration));
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
