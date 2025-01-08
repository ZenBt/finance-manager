package mephi.finance_manager.presenters.schemas;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class AddIncomeRequest {

    @NotNull
    private Long categoryId;

    @NotNull
    private BigDecimal amountReceived;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }
}
