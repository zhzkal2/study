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
    @GetMapping("/categories/{depth}")
    public ResponseEntity<List<CategoryResult>> getCategoriesByDepth(@PathVariable("depth") int depth,
                                                                     @RequestParam(value = "lang", defaultValue = "ko") String languageCode) {
        List<CategoryResult> categories = categoryService.getCategoriesByDepthAndLanguage(depth, languageCode);
        return ResponseEntity.ok(categories);
    }

    // 전체 카테고리 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResult>> getAllCategories(@RequestParam(value = "lang", defaultValue = "ko") String languageCode) {
        List<CategoryResult> categories = categoryService.getAllCategories(languageCode);
        return ResponseEntity.ok(categories);
    }
}