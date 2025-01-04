package mephi.finance_manager.domain.dto;

import java.util.List;

public class BudgetExceededAlertDto {
    private boolean isExpensesMoreThanIncomes;
    private List<CategoryWithBudgetDeficit> categoriesWithBudgetDeficit;

    public BudgetExceededAlertDto() {
    }

    public BudgetExceededAlertDto(boolean isExpensesMoreThanIncomes,
            List<CategoryWithBudgetDeficit> categoriesWithBudgetDeficit) {
        this.isExpensesMoreThanIncomes = isExpensesMoreThanIncomes;
        this.categoriesWithBudgetDeficit = categoriesWithBudgetDeficit;
    }

    public boolean isIsExpensesMoreThanIncomes() {
        return isExpensesMoreThanIncomes;
    }

    public void setIsExpensesMoreThanIncomes(boolean isExpensesMoreThanIncomes) {
        this.isExpensesMoreThanIncomes = isExpensesMoreThanIncomes;
    }

    public List<CategoryWithBudgetDeficit> getCategoriesWithBudgetDeficit() {
        return categoriesWithBudgetDeficit;
    }

    public void setCategoriesWithBudgetDeficit(List<CategoryWithBudgetDeficit> categoriesWithBudgetDeficit) {
        this.categoriesWithBudgetDeficit = categoriesWithBudgetDeficit;
    }

    public class CategoryWithBudgetDeficit {
        private Long categoryId;
        private String categoryName;

        public CategoryWithBudgetDeficit(Long categoryId, String categoryName) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
        }

        public CategoryWithBudgetDeficit() {
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
