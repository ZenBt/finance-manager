package mephi.finance_manager.domain.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import mephi.finance_manager.domain.dto.UserDto;

public abstract class UserRepository {
    public abstract UserDto createUser(String login, String hashedPassword);

    public abstract Optional<UserDto> getUserByLogin(String login);

    public abstract Optional<UserDto> getUserById(Long userId);

    public abstract void decreaseMoneyAmount(Long userId, BigDecimal moneyAmount);
    
    public abstract void increaseMoneyAmount(Long userId, BigDecimal moneyAmount);
}
