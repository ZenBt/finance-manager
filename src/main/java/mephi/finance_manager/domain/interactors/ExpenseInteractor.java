package mephi.finance_manager.domain.interactors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.domain.dto.ExpenseDto;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.repositories.ExpenseRepository;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class ExpenseInteractor {

    private final UserTokenRepository userTokenRepo;
    private final UserRepository userRepo;
    private final ExpenseRepository expenseRepo;

    public ExpenseInteractor(UserTokenRepository userTokenRepo, ExpenseRepository expenseRepo,
            UserRepository userRepo) {
        this.userTokenRepo = userTokenRepo;
        this.expenseRepo = expenseRepo;
        this.userRepo = userRepo;
    }

    public List<ExpenseDto> getAllExpensesByUserToken(String userToken) throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        List<ExpenseDto> expenses = expenseRepo.findExpensesByUserId(userId);
        return expenses;
    }

    public List<ExpenseDto> getExpensesByUserTokenAndCategories(String userToken, Long[] categoryIds)
            throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        List<ExpenseDto> expenses = expenseRepo.findExpensesByUserIdAndCategories(userId, categoryIds);
        return expenses;
    }

    public ExpenseDto addExpenseForUserByToken(String userToken, Long categoryId, BigDecimal amountSpent)
            throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        ExpenseDto expense = expenseRepo.addExpenseForUser(userId, categoryId, amountSpent);
        userRepo.decreaseMoneyAmount(userId, amountSpent);
        return expense;
    }

    public void deleteExpenseForUserByIdAndToken(String userToken, Long expenseId)
            throws TokenNotFoundOrExpiredException, PermissionDeniedException {
        Long userId = getUserIdFromToken(userToken);
        Optional<ExpenseDto> optionalExpense = expenseRepo.getExpenseById(expenseId);
        if (optionalExpense.isEmpty()) {
            return;
        }
        if (!optionalExpense.get().getUser().getUserId().equals(userId)) {
            throw new PermissionDeniedException("Невозможно удалить чужую трату");
        }
        expenseRepo.deleteExpenseById(expenseId);
        userRepo.increaseMoneyAmount(userId, optionalExpense.get().getAmountSpent());
    }

    private Long getUserIdFromToken(String token) throws TokenNotFoundOrExpiredException {
        Optional<Long> userId = userTokenRepo.getUserIdByUserToken(token);
        if (userId.isEmpty()) {
            throw new TokenNotFoundOrExpiredException();
        }
        return userId.get();
    }
}
