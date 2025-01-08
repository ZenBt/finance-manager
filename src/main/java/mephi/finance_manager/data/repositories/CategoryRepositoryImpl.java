package mephi.finance_manager.data.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import mephi.finance_manager.data.models.Category;
import mephi.finance_manager.data.models.User;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.repositories.CategoryRepository;

@Repository
public class CategoryRepositoryImpl extends CategoryRepository {
    private final EntityManager entityManager;

    public CategoryRepositoryImpl(jakarta.persistence.EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public CategoryDto createCategory(Long userId, String categoryName, BigDecimal budget,
            Category.CategoryType categoryType) {
        Category category = new Category();
        category.setName(categoryName);
        category.setBudget(budget);
        category.setCategoryType(categoryType);

        // Set the user ID directly without fetching the User entity
        category.setUser(entityManager.getReference(User.class, userId));

        entityManager.persist(category);

        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = entityManager.find(Category.class, categoryId);
        if (category == null) {
            return;
        }
        entityManager.remove(category);
    }

    @Override
    public Optional<CategoryDto> getCategoryById(Long categoryId) {
        Category category = entityManager.find(Category.class, categoryId);
        return category == null ? Optional.empty() : Optional.of(toCategoryDto(category));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = entityManager.find(Category.class, categoryDto.getId());

        if (category == null) {
            return categoryDto;
        }

        category.setName(categoryDto.getName());
        category.setBudget(categoryDto.getBudget());
        category.setCategoryType(categoryDto.getCategoryType());

        entityManager.merge(category);

        return toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> findCategoriesByUserId(Long userId) {
        String query = "SELECT c FROM Category c WHERE c.user.id = :userId";
        List<Category> categories = entityManager.createQuery(query, Category.class)
                .setParameter("userId", userId)
                .getResultList();

        return categories.stream().map(this::toCategoryDto).collect(Collectors.toList());
    }

    private CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getBudget(),
                category.getName(),

                category.getCategoryType(),
                mapToUserDto(category.getUser()));
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId());
        userDto.setLogin(user.getLogin());
        userDto.setPassword(user.getPassword());
        userDto.setAmountMoney(user.getAmountMoney());
        return userDto;
    }

}
