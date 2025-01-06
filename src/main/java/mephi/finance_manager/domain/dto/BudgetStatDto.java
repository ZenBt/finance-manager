package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class BudgetStatDto {
    private BigDecimal moneyLeft;
    private List<PerCategoryMoney> perCategoryBudget;

    public BudgetStatDto() {
    }

    public BudgetStatDto(BigDecimal moneyLeft, List<PerCategoryMoney> perCategoryBudget) {
        this.moneyLeft = moneyLeft;
        this.perCategoryBudget = perCategoryBudget;
    }

    public List<PerCategoryMoney> getPerCategoryBudget() {
        return perCategoryBudget;
    }

    public void setPerCategoryBudget(List<PerCategoryMoney> perCategoryBudget) {
        this.perCategoryBudget = perCategoryBudget;
    }

    public BigDecimal getMoneyLeft() {
        return moneyLeft;
    }

    public void setMoneyLeft(BigDecimal moneyLeft) {
        this.moneyLeft = moneyLeft;
    }

}
