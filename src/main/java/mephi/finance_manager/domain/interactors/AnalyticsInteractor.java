package mephi.finance_manager.domain.interactors;

import mephi.finance_manager.domain.dto.BudgetExceededAlertDto;
import mephi.finance_manager.domain.dto.BudgetStatDto;
import mephi.finance_manager.domain.dto.MoneyStatDto;

public class AnalyticsInteractor {
    public MoneyStatDto getOverallMoneyStatisticForUserByToken(String userToken, int expenseId) {
        // Реализовать возможность отображения общей суммы доходов и расходов,
        // а также данных по каждой категории.
        return new MoneyStatDto();
    }

    public BudgetStatDto getBudgetStatForUserByToken(String userToken) {
        // Выводить информацию о текущем состоянии бюджета для каждой категории,
        // а также оставшийся лимит.
        return new BudgetStatDto();
    }

    public BudgetExceededAlertDto getAlertForBudgetByUserToken(String userToken) {
        // Оповещать пользователя, если превышен лимит бюджета по категории или расходы
        // превысили доходы.
        return new BudgetExceededAlertDto();
    }

}
