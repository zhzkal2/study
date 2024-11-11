package com.java.category;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    private Long depth;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryName> categoryNames = new ArrayList<>(); // 다국어 이름들

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
}
