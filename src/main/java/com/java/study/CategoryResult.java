package com.java.study;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResult {
    private Long id;
    private String name;
    private Long depth;
    private Long parent;
    private List<CategoryResult> children;

    public static CategoryResult of(Category category) {
        CategoryResult categoryResult = new CategoryResult();
        categoryResult.setId(category.getId());
        categoryResult.setDepth(category.getDepth());
        categoryResult.setParent(category.getParent() != null ? category.getParent().getId() : null);
        return categoryResult;
    }

    // 다국어 지원을 위해 이름을 설정할 수 있도록 오버로딩된 메서드 추가
    public static CategoryResult of(Category category, String languageName) {
        CategoryResult categoryResult = of(category);
        categoryResult.setName(languageName);
        return categoryResult;
    }
}