package com.java;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // 삭제되지 않은 사용자만 조회
    List<User> findByIsDeletedFalse();
}