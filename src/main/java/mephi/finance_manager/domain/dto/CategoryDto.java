package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;

import mephi.finance_manager.data.models.Category.CategoryType;

public class CategoryDto {
    private Long id;
    private BigDecimal budget;
    private String name;
    private CategoryType categoryType;
    private UserDto user;

    public CategoryDto() {
    }

    public CategoryDto(Long id, BigDecimal budget, String name, CategoryType categoryType, UserDto user) {
        this.id = id;
        this.budget = budget;
        this.name = name;
        this.user = user;
        this.categoryType = categoryType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
