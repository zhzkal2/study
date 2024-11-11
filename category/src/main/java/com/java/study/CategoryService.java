package com.java.study;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 특정 깊이를 기반으로 카테고리 가져오기
    public List<CategoryResult> getCategoriesByDepthAndLanguage(int depth, String languageCode) {
        List<Category> topLevelCategories = categoryRepository.findAllByParentIsNull();
        return topLevelCategories.stream()
                .map(category -> mapCategoryWithDepthAndLanguage(category, depth, languageCode))
                .collect(Collectors.toList());
    }

    // 무제한 깊이로 모든 카테고리 가져오기
    public List<CategoryResult> getAllCategories(String languageCode) {
        List<Category> topLevelCategories = categoryRepository.findAllByParentIsNull();
        return topLevelCategories.stream()
                .map(category -> mapCategoryWithDepthAndLanguage(category, Integer.MAX_VALUE, languageCode))
                .collect(Collectors.toList());
    }


    // 이름과 깊이에 맞춰 카테고리를 변환
    private CategoryResult mapCategoryWithDepthAndLanguage(Category category, int requestedDepth, String languageCode) {
        List<CategoryResult> children = mapChildren(category, requestedDepth, languageCode);

        // CategoryName에서 언어 코드에 맞는 이름을 찾음 (fallback은 기본 언어인 "ko")
        String name = category.getCategoryNames().stream()
                .filter(nameEntity -> nameEntity.getLanguageCode().equals(languageCode))
                .map(CategoryName::getName)
                .findFirst()
                .orElseGet(() -> category.getCategoryNames().stream()
                        .filter(nameEntity -> nameEntity.getLanguageCode().equals("ko"))
                        .map(CategoryName::getName)
                        .findFirst()
                        .orElse("이름 없음"));

        CategoryResult result = CategoryResult.of(category, name);
        result.setChildren(children);
        return result;
    }

    // 깊이와 언어 코드로 자식 카테고리들 변환
    private List<CategoryResult> mapChildren(Category category, int requestedDepth, String languageCode) {
        if (category.getDepth() < requestedDepth) {
            return category.getChildren().stream()
                    .map(child -> mapCategoryWithDepthAndLanguage(child, requestedDepth, languageCode))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}