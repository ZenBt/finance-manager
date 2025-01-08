package mephi.finance_manager.data.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import mephi.finance_manager.data.models.Category;
import mephi.finance_manager.data.models.Income;
import mephi.finance_manager.data.models.User;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.dto.IncomeDto;
import mephi.finance_manager.domain.dto.PerCategoryMoney;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.repositories.IncomeRepository;

@Repository
public class IncomeRepositoryImpl extends IncomeRepository {
    private final EntityManager entityManager;

    public IncomeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<IncomeDto> findIncomesByUserId(Long userId) {
        TypedQuery<Income> query = entityManager.createQuery(
                "SELECT i FROM Income i WHERE i.user.id = :userId", Income.class);
        query.setParameter("userId", userId);

        List<Income> incomes = query.getResultList();
        return mapToIncomeDtos(incomes);
    }

    @Override
    public List<IncomeDto> findIncomesByUserIdAndCategories(Long userId, Long[] categoryIds) {
        TypedQuery<Income> query = entityManager.createQuery(
                "SELECT i FROM Income i WHERE i.user.id = :userId AND i.category.id IN :categoryIds", Income.class);
        query.setParameter("userId", userId);
        query.setParameter("categoryIds", List.of(categoryIds));

        List<Income> incomes = query.getResultList();
        return mapToIncomeDtos(incomes);
    }

    @Override
    @Transactional
    public IncomeDto addIncomeForUser(Long userId, Long categoryId, BigDecimal amountReceived) {
        Income income = new Income();
        income.setUser(entityManager.getReference(User.class, userId));
        income.setCategory(entityManager.getReference(Category.class, categoryId));
        income.setAmountReceived(amountReceived);
        income.setCreatedAt(java.time.LocalDateTime.now());

        entityManager.persist(income);
        entityManager.flush(); // Ensure the entity is synchronized and an ID is generated.

        return mapToIncomeDto(income);
    }

    @Override
    public Optional<IncomeDto> getIncomeById(Long incomeId) {
        Income income = entityManager.find(Income.class, incomeId);
        return income != null ? Optional.of(mapToIncomeDto(income)) : Optional.empty();
    }

    @Override
    @Transactional
    public void deleteIncomeById(Long incomeId) {
        Income income = entityManager.find(Income.class, incomeId);
        if (income != null) {
            entityManager.remove(income);
        }
    }

    @Override
    public BigDecimal getOverallIncome(Long userId) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(i.amountReceived), 0) FROM Income i WHERE i.user.id = :userId", BigDecimal.class);
        query.setParameter("userId", userId);

        return query.getSingleResult();
    }

    @Override
    public List<PerCategoryMoney> getOverallIncomePerCategory(Long userId) {
        TypedQuery<PerCategoryMoney> query = entityManager.createQuery(
                "SELECT new mephi.finance_manager.domain.dto.PerCategoryMoney(i.category.id, i.category.name, SUM(i.amountReceived)) "
                        +
                        "FROM Income i WHERE i.user.id = :userId GROUP BY i.category.id, i.category.name",
                PerCategoryMoney.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    private List<IncomeDto> mapToIncomeDtos(List<Income> incomes) {
        List<IncomeDto> incomeDtos = new ArrayList<>();
        for (Income income : incomes) {
            incomeDtos.add(mapToIncomeDto(income));
        }
        return incomeDtos;
    }

    private IncomeDto mapToIncomeDto(Income income) {
        UserDto user = new UserDto(income.getUser().getId(), income.getUser().getLogin(),
                income.getUser().getPassword(), income.getUser().getAmountMoney());
        CategoryDto category = new CategoryDto(
                income.getCategory().getId(),
                income.getCategory().getBudget(),
                income.getCategory().getName(),
                income.getCategory().getCategoryType(),
                user);
        return new IncomeDto(
                income.getId(),

                category,

                income.getAmountReceived(),
                user);
    }
}
