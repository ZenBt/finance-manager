package mephi.finance_manager.domain.interactors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mephi.finance_manager.domain.dto.BudgetExceededAlertDto;
import mephi.finance_manager.domain.dto.BudgetExceededAlertDto.CategoryWithBudgetDeficit;
import mephi.finance_manager.domain.dto.BudgetStatDto;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.dto.MoneyStatDto;
import mephi.finance_manager.domain.dto.PerCategoryMoney;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.exceptions.NoSuchUserException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.repositories.CategoryRepository;
import mephi.finance_manager.domain.repositories.ExpenseRepository;
import mephi.finance_manager.domain.repositories.IncomeRepository;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class AnalyticsInteractor {

    private final ExpenseRepository expenseRepo;
    private final UserTokenRepository userTokenRepo;
    private final UserRepository userRepo;
    private final IncomeRepository incomeRepo;
    private final CategoryRepository categoryRepo;

    public AnalyticsInteractor(ExpenseRepository expenseRepo, UserTokenRepository userTokenRepo,
            IncomeRepository incomeRepo, CategoryRepository categoryRepo,
            UserRepository userRepo) {
        this.expenseRepo = expenseRepo;
        this.userTokenRepo = userTokenRepo;
        this.incomeRepo = incomeRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    public MoneyStatDto getOverallMoneyStatisticForUserByToken(String userToken, int expenseId)
            throws TokenNotFoundOrExpiredException {
        // Реализовать возможность отображения общей суммы доходов и расходов,
        // а также данных по каждой категории.
        Long userId = getUserIdFromToken(userToken);

        BigDecimal overallExpense = expenseRepo.getOverallExpense(userId);
        BigDecimal overallIncome = incomeRepo.getOverallIncome(userId);

        List<PerCategoryMoney> perCategoryExpenses = expenseRepo.getOverallExpensePerCategory(userId);
        List<PerCategoryMoney> perCategoryIncomes = incomeRepo.getOverallIncomePerCategory(userId);

        return new MoneyStatDto(overallIncome, overallExpense, perCategoryExpenses, perCategoryIncomes);
    }

    public BudgetStatDto getBudgetStatForUserByToken(String userToken)
            throws TokenNotFoundOrExpiredException, NoSuchUserException {
        // Выводить информацию о текущем состоянии бюджета для каждой категории,
        // а также оставшийся лимит.
        Long userId = getUserIdFromToken(userToken);
        Optional<UserDto> optionalUser = userRepo.getUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        BigDecimal currentBalance = optionalUser.get().getAmountMoney();

        List<CategoryDto> userCategories = categoryRepo.findCategoriesByUserId(userId);

        Map<Long, PerCategoryMoney> expensesMap = toHashMap(expenseRepo.getOverallExpensePerCategory(userId));
        Map<Long, PerCategoryMoney> incomesMap = toHashMap(incomeRepo.getOverallIncomePerCategory(userId));

        List<PerCategoryMoney> perCategoryBudget = getBudgetLeftPerCategory(userCategories, expensesMap, incomesMap);

        return new BudgetStatDto(currentBalance, perCategoryBudget);
    }

    public BudgetExceededAlertDto getAlertForBudgetByUserToken(String userToken)
            throws TokenNotFoundOrExpiredException {
        // Оповещать пользователя, если превышен лимит бюджета по категории или расходы
        // превысили доходы.
        Long userId = getUserIdFromToken(userToken);

        BigDecimal overallExpense = expenseRepo.getOverallExpense(userId);
        BigDecimal overallIncome = incomeRepo.getOverallIncome(userId);

        List<CategoryDto> userCategories = categoryRepo.findCategoriesByUserId(userId);

        Map<Long, PerCategoryMoney> expensesMap = toHashMap(expenseRepo.getOverallExpensePerCategory(userId));
        Map<Long, PerCategoryMoney> incomesMap = toHashMap(incomeRepo.getOverallIncomePerCategory(userId));

        BudgetExceededAlertDto alertDto = new BudgetExceededAlertDto();

        alertDto.setIsExpensesMoreThanIncomes(overallIncome.compareTo(overallExpense) == -1);
        setCategoriesWithBudgetDeficit(alertDto, userCategories, expensesMap, incomesMap);

        return alertDto;
    }

    private Long getUserIdFromToken(String token) throws TokenNotFoundOrExpiredException {
        Optional<Long> userId = userTokenRepo.getUserIdByUserToken(token);
        if (userId.isEmpty()) {
            throw new TokenNotFoundOrExpiredException();
        }
        return userId.get();
    }

    private List<PerCategoryMoney> getBudgetLeftPerCategory(List<CategoryDto> userCategories,
            Map<Long, PerCategoryMoney> expensesMap, Map<Long, PerCategoryMoney> incomesMap) {
        List<PerCategoryMoney> lst = new ArrayList<>();

        for (CategoryDto cat : userCategories) {
            BigDecimal amountSpent = expensesMap
                    .getOrDefault(cat, new PerCategoryMoney(cat.getId(), cat.getName(), BigDecimal.ZERO)).getAmount();
            BigDecimal amountReceived = incomesMap
                    .getOrDefault(cat, new PerCategoryMoney(cat.getId(), cat.getName(), BigDecimal.ZERO)).getAmount();
            BigDecimal amountLeft = amountReceived.subtract(amountSpent);
            lst.add(new PerCategoryMoney(cat.getId(), cat.getName(), amountLeft));
        }

        return lst;
    }

    private void setCategoriesWithBudgetDeficit(BudgetExceededAlertDto alertDto, List<CategoryDto> userCategories,
            Map<Long, PerCategoryMoney> expensesMap, Map<Long, PerCategoryMoney> incomesMap) {
        List<CategoryWithBudgetDeficit> lst = new ArrayList<>();

        for (CategoryDto cat : userCategories) {
            BigDecimal amountSpent = expensesMap
                    .getOrDefault(cat, new PerCategoryMoney(cat.getId(), cat.getName(), BigDecimal.ZERO)).getAmount();
            BigDecimal amountReceived = incomesMap
                    .getOrDefault(cat, new PerCategoryMoney(cat.getId(), cat.getName(), BigDecimal.ZERO)).getAmount();
            BigDecimal amountLeft = amountReceived.subtract(amountSpent);
            if (amountLeft.compareTo(BigDecimal.ZERO) == -1) {
                lst.add(alertDto.new CategoryWithBudgetDeficit(cat.getId(), cat.getName()));
            }

        }

        alertDto.setCategoriesWithBudgetDeficit(lst);
    }

    private Map<Long, PerCategoryMoney> toHashMap(List<PerCategoryMoney> lst) {
        Map<Long, PerCategoryMoney> resultMap = new HashMap<>();
        for (PerCategoryMoney item : lst) {
            resultMap.put(item.getCategoryId(), item);
        }
        return resultMap;
    }

}
