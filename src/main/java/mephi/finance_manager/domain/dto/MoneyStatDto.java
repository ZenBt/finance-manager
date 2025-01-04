package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;
import java.util.List;

public class MoneyStatDto {
    private BigDecimal overallIncome;
    private BigDecimal overallExpense;

    private List<PerCategoryMoney> perCategoryExpenses;
    private List<PerCategoryMoney> perCategoryIncomes;

    public MoneyStatDto() {
    }

    public MoneyStatDto(BigDecimal overallIncome, BigDecimal overallExpense, List<PerCategoryMoney> perCategoryExpenses,
            List<PerCategoryMoney> perCategoryIncomes) {
        this.overallIncome = overallIncome;
        this.overallExpense = overallExpense;
        this.perCategoryExpenses = perCategoryExpenses;
        this.perCategoryIncomes = perCategoryIncomes;
    }

    public BigDecimal getOverallExpense() {
        return overallExpense;
    }

    public void setOverallExpense(BigDecimal overallExpense) {
        this.overallExpense = overallExpense;
    }

    public BigDecimal getOverallIncome() {
        return overallIncome;
    }

    public void setOverallIncome(BigDecimal overallIncome) {
        this.overallIncome = overallIncome;
    }

    public List<PerCategoryMoney> getPerCategoryExpenses() {
        return perCategoryExpenses;
    }

    public void setPerCategoryExpenses(List<PerCategoryMoney> perCategoryExpenses) {
        this.perCategoryExpenses = perCategoryExpenses;
    }

    public List<PerCategoryMoney> getPerCategoryIncomes() {
        return perCategoryIncomes;
    }

    public void setPerCategoryIncomes(List<PerCategoryMoney> perCategoryIncomes) {
        this.perCategoryIncomes = perCategoryIncomes;
    }

}
