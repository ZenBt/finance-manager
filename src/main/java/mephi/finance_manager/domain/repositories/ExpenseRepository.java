package mephi.finance_manager.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.domain.dto.ExpenseDto;

public abstract class ExpenseRepository {
    public abstract List<ExpenseDto> findExpensesByUserId(Long userId);

    public abstract List<ExpenseDto> findExpensesByUserIdAndCategories(Long userId, Long[] categoryIds);

    public abstract ExpenseDto addExpenseForUser(Long userId, Long categoryId, BigDecimal amountSpent);

    public abstract Optional<ExpenseDto> getExpenseById(Long expenseId);

    public abstract void deleteExpenseById(Long expenseId);
}
