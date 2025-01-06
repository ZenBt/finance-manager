package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

public class UpdateCategoryRequest {

    private String categoryName;
    private BigDecimal budget;

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
}
