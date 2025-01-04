package mephi.finance_manager.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.domain.dto.IncomeDto;

public abstract class IncomeRepository {
    public abstract List<IncomeDto> findIncomesByUserId(Long userId);

    public abstract List<IncomeDto> findIncomesByUserIdAndCategories(Long userId, Long[] categoryIds);

    public abstract IncomeDto addIncomeForUser(Long userId, Long categoryId, BigDecimal amountSpent);

    public abstract Optional<IncomeDto> getIncomeById(Long incomeId);

    public abstract void deleteIncomeById(Long incomeId);
}
