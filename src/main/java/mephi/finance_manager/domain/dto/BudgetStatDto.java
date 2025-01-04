package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;

public class BudgetStatDto {
    private BigDecimal moneyLeft;
    private PerCategoryMoney perCategoryBudget;

    public BudgetStatDto() {
    }

    public BudgetStatDto(BigDecimal moneyLeft, PerCategoryMoney perCategoryBudget) {
        this.moneyLeft = moneyLeft;
        this.perCategoryBudget = perCategoryBudget;
    }

    public PerCategoryMoney getPerCategoryBudget() {
        return perCategoryBudget;
    }

    public void setPerCategoryBudget(PerCategoryMoney perCategoryBudget) {
        this.perCategoryBudget = perCategoryBudget;
    }

    public BigDecimal getMoneyLeft() {
        return moneyLeft;
    }

    public void setMoneyLeft(BigDecimal moneyLeft) {
        this.moneyLeft = moneyLeft;
    }

}
