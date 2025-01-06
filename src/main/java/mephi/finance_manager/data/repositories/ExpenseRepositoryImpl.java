package mephi.finance_manager.data.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import mephi.finance_manager.data.models.Category;
import mephi.finance_manager.data.models.Expense;
import mephi.finance_manager.data.models.User;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.dto.ExpenseDto;
import mephi.finance_manager.domain.dto.PerCategoryMoney;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.repositories.ExpenseRepository;

public class ExpenseRepositoryImpl extends ExpenseRepository {
    private final EntityManager entityManager;

    public ExpenseRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ExpenseDto> findExpensesByUserId(Long userId) {
        TypedQuery<Expense> query = entityManager.createQuery(
                "SELECT * FROM expense e WHERE e.userId = :userId", Expense.class);
        query.setParameter("userId", userId);

        List<Expense> expenses = query.getResultList();
        return mapToExpenseDtos(expenses);
    }

    @Override
    public List<ExpenseDto> findExpensesByUserIdAndCategories(Long userId, Long[] categoryIds) {
        TypedQuery<Expense> query = entityManager.createQuery(
                "SELECT i FROM expense i WHERE i.user.id = :userId AND i.category.id IN :categoryIds", Expense.class);
        query.setParameter("userId", userId);
        query.setParameter("categoryIds", List.of(categoryIds));

        List<Expense> expenses = query.getResultList();
        return mapToExpenseDtos(expenses);
    }

    @Override
    public ExpenseDto addExpenseForUser(Long userId, Long categoryId, BigDecimal amountSpent) {
        Expense expense = new Expense();
        expense.setUser(entityManager.getReference(User.class, userId));
        expense.setCategory(entityManager.getReference(Category.class, categoryId));
        expense.setAmountSpent(amountSpent);
        expense.setCreatedAt(java.time.LocalDateTime.now());

        entityManager.persist(expense);
        entityManager.flush(); // Ensure the entity is synchronized and an ID is generated.

        return mapToExpenseDto(expense);
    }

    @Override
    public Optional<ExpenseDto> getExpenseById(Long expenseId) {
        Expense expense = entityManager.find(Expense.class, expenseId);
        return expense != null ? Optional.of(mapToExpenseDto(expense)) : Optional.empty();
    }

    @Override
    public void deleteExpenseById(Long expenseId) {
        Expense expense = entityManager.find(Expense.class, expenseId);
        if (expense != null) {
            entityManager.remove(expense);
        }
    }

    @Override
    public BigDecimal getOverallExpense(Long userId) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(i.amountReceived), 0) FROM expense i WHERE i.user.id = :userId", BigDecimal.class);
        query.setParameter("userId", userId);

        return query.getSingleResult();
    }

    @Override
    public List<PerCategoryMoney> getOverallExpensePerCategory(Long userId) {
        TypedQuery<PerCategoryMoney> query = entityManager.createQuery(
                "SELECT new mephi.finance_manager.domain.dto.PerCategoryMoney(i.category.id, i.category.name, SUM(i.amountReceived)) "
                        +
                        "FROM expense i WHERE i.user.id = :userId GROUP BY i.category.id, i.category.name",
                PerCategoryMoney.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    private List<ExpenseDto> mapToExpenseDtos(List<Expense> expenses) {
        List<ExpenseDto> expenseDtos = new ArrayList<>();
        for (Expense expense : expenses) {
            expenseDtos.add(mapToExpenseDto(expense));
        }
        return expenseDtos;
    }

    private ExpenseDto mapToExpenseDto(Expense expense) {
        UserDto user = new UserDto(expense.getUser().getId(), expense.getUser().getLogin(),
                expense.getUser().getPassword(), expense.getUser().getAmountMoney());
        CategoryDto category = new CategoryDto(
                expense.getCategory().getId(),
                expense.getCategory().getBudget(),
                expense.getCategory().getName(),
                expense.getCategory().getCategoryType(),
                user);
        return new ExpenseDto(
                expense.getId(),
                category,
                expense.getAmountSpent(),
                user);
    }
}
