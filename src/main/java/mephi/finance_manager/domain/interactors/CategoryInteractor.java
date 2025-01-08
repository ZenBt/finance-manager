package mephi.finance_manager.domain.interactors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.data.models.Category.CategoryType;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.exceptions.CategoryDeletionFailedException;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.repositories.CategoryRepository;
import mephi.finance_manager.domain.repositories.ExpenseRepository;
import mephi.finance_manager.domain.repositories.IncomeRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class CategoryInteractor {
    private final CategoryRepository categoryRepo;
    private final UserTokenRepository userTokenRepo;
    private final ExpenseRepository expenseRepo;
    private final IncomeRepository incomeRepo;

    public CategoryInteractor(CategoryRepository categoryRepo, UserTokenRepository userTokenRepo,
            ExpenseRepository expenseRepo, IncomeRepository incomeRepo) {
        this.categoryRepo = categoryRepo;
        this.userTokenRepo = userTokenRepo;
        this.expenseRepo = expenseRepo;
        this.incomeRepo = incomeRepo;
    }

    public List<CategoryDto> getCategoriesByUserToken(String userToken) throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        List<CategoryDto> categories = categoryRepo.findCategoriesByUserId(userId);
        return categories;
    }

    public CategoryDto createCategoryForUserByToken(String userToken, String categoryName, BigDecimal budget,
            CategoryType categoryType) throws TokenNotFoundOrExpiredException {
        Long userId = getUserIdFromToken(userToken);
        CategoryDto category = categoryRepo.createCategory(userId, categoryName, budget, categoryType);
        return category;
    }

    public void deleteCategoryByIdAndToken(String userToken, Long categoryId)
            throws TokenNotFoundOrExpiredException, PermissionDeniedException, CategoryDeletionFailedException {
        Long userId = getUserIdFromToken(userToken);
        Optional<CategoryDto> optionalCategory = categoryRepo.getCategoryById(categoryId);
        if (optionalCategory.isEmpty()) {
            return;
        }
        if (!optionalCategory.get().getUser().getUserId().equals(userId)) {
            throw new PermissionDeniedException("Невозможно удалить чужую категорию");
        }
        if (optionalCategory.get().getCategoryType() == CategoryType.EXPENSE
                && !expenseRepo.findExpensesByUserIdAndCategories(userId, new Long[] { categoryId }).isEmpty()) {
            throw new CategoryDeletionFailedException(
                    "Невозможно удалить категорию, т.к. существуют неудаленные расходы");
        }
        if (optionalCategory.get().getCategoryType() == CategoryType.INCOME
                && !incomeRepo.findIncomesByUserIdAndCategories(userId, new Long[] { categoryId }).isEmpty()) {
            throw new CategoryDeletionFailedException(
                    "Невозможно удалить категорию, т.к. существуют неудаленные доходы");
        }
        categoryRepo.deleteCategory(categoryId);

    }

    public void changeCategoryBudgetByIdAndToken(String userToken, Long categoryId, BigDecimal budget)
            throws TokenNotFoundOrExpiredException, PermissionDeniedException, CategoryNotFoundException {
        Long userId = getUserIdFromToken(userToken);
        Optional<CategoryDto> optionalCategory = categoryRepo.getCategoryById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Категория не найдена");
        }
        if (!optionalCategory.get().getUser().getUserId().equals(userId)) {
            throw new PermissionDeniedException("Невозможно изменить чужую категорию");
        }

        CategoryDto category = optionalCategory.get();

        category.setBudget(budget);
        categoryRepo.updateCategory(category);

    }

    public void changeCategoryNameByIdAndToken(String userToken, Long categoryId, String name)
            throws TokenNotFoundOrExpiredException, PermissionDeniedException, CategoryNotFoundException {
        Long userId = getUserIdFromToken(userToken);

        Optional<CategoryDto> optionalCategory = categoryRepo.getCategoryById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException("Категория не найдена");
        }
        if (!optionalCategory.get().getUser().getUserId().equals(userId)) {
            throw new PermissionDeniedException("Невозможно изменить чужую категорию");
        }

        CategoryDto category = optionalCategory.get();

        category.setName(name);
        categoryRepo.updateCategory(category);

    }

    private Long getUserIdFromToken(String token) throws TokenNotFoundOrExpiredException {
        Optional<Long> userId = userTokenRepo.getUserIdByUserToken(token);
        if (userId.isEmpty()) {
            throw new TokenNotFoundOrExpiredException();
        }
        return userId.get();
    }
}
