package mephi.finance_manager.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.domain.dto.IncomeDto;
import mephi.finance_manager.domain.dto.PerCategoryMoney;

public abstract class IncomeRepository {
    public abstract List<IncomeDto> findIncomesByUserId(Long userId);

    public abstract List<IncomeDto> findIncomesByUserIdAndCategories(Long userId, Long[] categoryIds);

    public abstract IncomeDto addIncomeForUser(Long userId, Long categoryId, BigDecimal amountSpent);

    public abstract Optional<IncomeDto> getIncomeById(Long incomeId);

    public abstract void deleteIncomeById(Long incomeId);

    public abstract BigDecimal getOverallIncome(Long userId);

    public abstract List<PerCategoryMoney> getOverallIncomePerCategory(Long userId);

}
