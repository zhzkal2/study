package com.java.study;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // depth에 따라 카테고리 조회
    @GetMapping("/categorys/{depth}")
    public ResponseEntity<?> getCategoriesByDepth(@PathVariable("depth") int depth) {
        return ResponseEntity.ok(categoryService.getTopLevelCategories(depth));
    }

    // 전체 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}