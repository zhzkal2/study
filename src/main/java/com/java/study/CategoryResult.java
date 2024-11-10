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

    public static CategoryResult of(Category category,int requestedDepth) {
        CategoryResult categoryResult = new CategoryResult();
        categoryResult.setId(category.getId());
        categoryResult.setName(category.getName());
        categoryResult.setDepth(category.getDepth());

        // 부모 정보는 ID만 설정 (무한 참조 방지)
        categoryResult.setParent(category.getParent() != null ? category.getParent().getId() : null);

        if (category.getDepth() < requestedDepth) {
            List<CategoryResult> children = category.getChildren().stream()
                    .map(child -> of(child, requestedDepth))  // 재귀적으로 자식 카테고리 생성
                    .collect(Collectors.toList());
            categoryResult.setChildren(children);
        } else {
            categoryResult.setChildren(null);  // 요청된 depth에 도달하면 자식 카테고리 비워둠
        }

        return categoryResult;
    }
}