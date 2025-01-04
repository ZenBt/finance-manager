package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;

public class IncomeDto {
    private Long id;
    private CategoryDto category;
    private BigDecimal amountReceived;
    private UserDto user;

    public IncomeDto() {
    }

    public IncomeDto(Long id, CategoryDto category, BigDecimal amountReceived, UserDto user) {
        this.id = id;
        this.category = category;
        this.amountReceived = amountReceived;
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

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}