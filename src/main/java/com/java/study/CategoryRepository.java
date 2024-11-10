package com.java.study;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentIsNull(); // 최상위 카테고리 조회

    List<Category> findByParent(Category parent); // 주어진 부모 카테고리에 대한 자식 카테고리 조회

    // 특정 깊이에 해당하는 카테고리들을 조회하는 메서드 추가
    List<Category> findAllByDepth(Long depth);
}