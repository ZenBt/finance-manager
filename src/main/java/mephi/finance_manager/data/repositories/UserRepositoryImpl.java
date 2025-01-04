package mephi.finance_manager.data.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import mephi.finance_manager.data.models.User;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.repositories.UserRepository;

@Repository
public class UserRepositoryImpl extends UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDto createUser(String login, String hashedPassword) {
        // Создаем объект User
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashedPassword);
        user.setAmountMoney(BigDecimal.ZERO); // Изначально устанавливаем нулевое количество денег

        // Сохраняем пользователя в базе данных
        entityManager.persist(user);

        // Возвращаем UserDto после сохранения
        return mapToUserDto(user);
    }

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        // Пытаемся найти пользователя по логину
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.login = :login", User.class);
        query.setParameter("login", login);

        // Получаем результат
        User user = query.getResultStream().findFirst().orElse(null);

        // Если пользователя нет, возвращаем пустой Optional
        return Optional.ofNullable(user).map(this::mapToUserDto);
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
