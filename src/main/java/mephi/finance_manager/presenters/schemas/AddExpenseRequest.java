package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class AddExpenseRequest {
    @NotNull
    private Long categoryId;

    @NotNull
    private BigDecimal amountSpent;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent;
    }
}