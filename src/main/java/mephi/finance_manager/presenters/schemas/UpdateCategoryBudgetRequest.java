package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class UpdateCategoryBudgetRequest {

    @NotNull
    private BigDecimal budget;

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
