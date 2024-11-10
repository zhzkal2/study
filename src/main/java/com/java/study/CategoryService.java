package com.java.study;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Top-level 카테고리 조회 (부모가 없는 카테고리들)
    public List<CategoryResult> getTopLevelCategories(int depth) {
        List<Category> topLevelCategories = categoryRepository.findAllByParentIsNull();
        return topLevelCategories.stream()
                .map(category -> mapCategoryWithDepth(category, depth))
                .collect(Collectors.toList());
    }



    private CategoryResult mapCategoryWithDepth(Category category, int depth) {
        CategoryResult result = CategoryResult.of(category, depth);

        // 깊이에 따라 자식 카테고리 조회
        if (depth > 1 && category.getDepth() < depth) {
            List<Category> children = categoryRepository.findByParent(category);
            result.setChildren(children.stream()
                    .map(child -> mapCategoryWithDepth(child, depth))
                    .collect(Collectors.toList()));
        }

        return result;
    }
    // Top-level 카테고리 조회 (부모가 없는 카테고리들)
    public List<CategoryResult> getAllCategories() {
        List<Category> topLevelCategories = categoryRepository.findAllByParentIsNull();
        return topLevelCategories.stream()
                .map(category -> mapCategoryWithAllDepth(category)) // 모든 깊이를 고려한 조회
                .collect(Collectors.toList());
    }


    private CategoryResult mapCategoryWithAllDepth(Category category) {
        CategoryResult result = CategoryResult.of(category, Integer.MAX_VALUE);  // 무제한 깊이로 자식 카테고리 포함

        // 자식 카테고리들을 재귀적으로 추가
        List<Category> children = categoryRepository.findByParent(category);
        result.setChildren(children.stream()
                .map(this::mapCategoryWithAllDepth)
                .collect(Collectors.toList()));

        return result;
    }
}