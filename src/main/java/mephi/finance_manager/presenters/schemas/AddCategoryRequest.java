package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

import mephi.finance_manager.data.models.Category.CategoryType;

public class AddCategoryRequest {

    private String categoryName;
    private BigDecimal budget;
    private CategoryType categoryType;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
