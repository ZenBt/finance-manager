package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;

public class ExpenseDto {
    private Long id;
    private CategoryDto category;
    private BigDecimal amountSpent;
    private UserDto user;

    public ExpenseDto() {
    }

    public ExpenseDto(Long id, CategoryDto category, BigDecimal amountSpent, UserDto user) {
        this.id = id;
        this.category = category;
        this.amountSpent = amountSpent;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public BigDecimal getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(BigDecimal amountSpent) {
        this.amountSpent = amountSpent;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}