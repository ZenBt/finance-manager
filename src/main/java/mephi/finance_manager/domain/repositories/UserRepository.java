package mephi.finance_manager.domain.repositories;

import java.util.Optional;

import mephi.finance_manager.domain.dto.UserDto;

public abstract class UserRepository {
    public abstract UserDto createUser(String login, String hashedPassword);

    public abstract Optional<UserDto> getUserByLogin(String login);

}
