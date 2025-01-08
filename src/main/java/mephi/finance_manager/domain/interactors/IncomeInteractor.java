package mephi.finance_manager.domain.interactors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.data.models.Category.CategoryType;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.dto.IncomeDto;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.repositories.CategoryRepository;
import mephi.finance_manager.domain.repositories.IncomeRepository;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class IncomeInteractor {
    private final UserTokenRepository userTokenRepo;
    private final UserRepository userRepo;
    private final IncomeRepository incomeRepo;
    private final CategoryRepository categoryRepo;

    public IncomeInteractor(IncomeRepository incomeRepo, UserTokenRepository userTokenRepo, UserRepository userRepo,
            CategoryRepository categoryRepo) {
        this.incomeRepo = incomeRepo;
        this.userTokenRepo = userTokenRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<IncomeDto> getAllIncomesByUserToken(String userToken) throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        List<IncomeDto> incomes = incomeRepo.findIncomesByUserId(userId);
        return incomes;
    }

    public List<IncomeDto> getIncomesByUserTokenAndCategories(String userToken, Long[] categoryIds)
            throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        List<IncomeDto> incomes = incomeRepo.findIncomesByUserIdAndCategories(userId, categoryIds);
        return incomes;
    }

    public IncomeDto addIncomeForUserByToken(String userToken, Long categoryId, BigDecimal amountReceived)
            throws TokenNotFoundOrExpiredException, CategoryNotFoundException {
        Long userId = getUserIdFromToken(userToken);
        isCategoryExists(categoryId);
        IncomeDto income = incomeRepo.addIncomeForUser(userId, categoryId, amountReceived);
        userRepo.increaseMoneyAmount(userId, amountReceived);
        return income;
    }

    public void deleteIncomeForUserByIdAndToken(String userToken, Long IncomeId)
            throws TokenNotFoundOrExpiredException, PermissionDeniedException {
        Long userId = getUserIdFromToken(userToken);
        Optional<IncomeDto> optionalIncome = incomeRepo.getIncomeById(IncomeId);
        if (optionalIncome.isEmpty()) {
            return;
        }
        if (!optionalIncome.get().getUser().getUserId().equals(userId)) {
            throw new PermissionDeniedException("Невозможно удалить чужое поступление");
        }
        incomeRepo.deleteIncomeById(IncomeId);
        userRepo.decreaseMoneyAmount(userId, optionalIncome.get().getAmountReceived());
    }

    private Long getUserIdFromToken(String token) throws TokenNotFoundOrExpiredException {
        Optional<Long> userId = userTokenRepo.getUserIdByUserToken(token);
        if (userId.isEmpty()) {
            throw new TokenNotFoundOrExpiredException();
        }
        return userId.get();
    }

    private void isCategoryExists(Long categoryId) throws CategoryNotFoundException {
        Optional<CategoryDto> cat = categoryRepo.getCategoryById(categoryId);
        if (cat.isEmpty() || cat.get().getCategoryType() == CategoryType.EXPENSE) {
            throw new CategoryNotFoundException("Категория не найдена");
        }
        System.out.println(cat.get().getCategoryType());
    }
}
