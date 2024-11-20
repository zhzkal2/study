package com.java;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 전체 사용자 조회 (isDeleted가 false인 사용자만)
    public List<User> getActiveUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    // 특정 사용자 삭제 (논리 삭제)
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsDeleted(true); // 논리 삭제
        userRepository.save(user);
    }

    // 사용자 추가
    @Transactional
    public User createUser(String name) {
        User user = new User();
        user.setName(name);
        return userRepository.save(user);
    }
}