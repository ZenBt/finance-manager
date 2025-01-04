package mephi.finance_manager.domain.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import mephi.finance_manager.data.models.Category.CategoryType;
import mephi.finance_manager.domain.dto.CategoryDto;

public abstract class CategoryRepository {
    public abstract CategoryDto createCategory(Long userId, String categoryName, BigDecimal budget,
            CategoryType categoryType);

    public abstract void deleteCategory(Long categoryId);

    public abstract Optional<CategoryDto> getCategoryById(Long categoryId);

    public abstract CategoryDto updateCategory(CategoryDto category);

    public abstract List<CategoryDto> findCategoriesByUserId(Long UserId);
}
