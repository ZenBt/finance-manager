package mephi.finance_manager.domain.dto;

import java.math.BigDecimal;

public class UserDto {
    private Long userId;
    private String login;
    private String password;
    private BigDecimal amountMoney;

    public UserDto() {
    }

    public UserDto(Long userId, String login, String password, BigDecimal amountMoney) {
        this.userId = userId;
        this.login = login;
        this.password = password;
        this.amountMoney = amountMoney;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(BigDecimal amountMoney) {
        this.amountMoney = amountMoney;
    }
}
