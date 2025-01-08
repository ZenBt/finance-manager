package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import mephi.finance_manager.data.models.Category.CategoryType;

public class AddCategoryRequest {

    @NotNull
    @NotBlank
    private String categoryName;

    @NotNull
    private BigDecimal budget;

    @NotNull
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
