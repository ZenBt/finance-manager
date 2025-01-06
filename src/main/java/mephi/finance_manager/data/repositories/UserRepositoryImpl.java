package mephi.finance_manager.data.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import mephi.finance_manager.data.models.User;
import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.repositories.UserRepository;

@Repository
public class UserRepositoryImpl extends UserRepository {

    private final EntityManager entityManager;

    public UserRepositoryImpl(jakarta.persistence.EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserDto createUser(String login, String hashedPassword) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashedPassword);
        user.setAmountMoney(BigDecimal.ZERO); // Изначально устанавливаем нулевое количество денег

        entityManager.persist(user);

        return mapToUserDto(user);
    }

    @Override
    public Optional<UserDto> getUserByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM users u WHERE u.login = :login", User.class);
        query.setParameter("login", login);

        User user = query.getResultStream().findFirst().orElse(null);

        return Optional.ofNullable(user).map(this::mapToUserDto);
    }

    @Override
    public Optional<UserDto> getUserById(Long userId) {
        User user = entityManager.find(User.class, userId);

        return Optional.ofNullable(user).map(this::mapToUserDto);
    }

    @Override
    @Transactional
    public void decreaseMoneyAmount(Long userId, BigDecimal moneyAmount) {
        entityManager.createQuery(
                "UPDATE users u SET u.amountMoney = u.amountMoney - :moneyAmount WHERE u.id = :userId")
                .setParameter("moneyAmount", moneyAmount)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void increaseMoneyAmount(Long userId, BigDecimal moneyAmount) {
        entityManager.createQuery(
                "UPDATE users u SET u.amountMoney = u.amountMoney + :moneyAmount WHERE u.id = :userId")
                .setParameter("moneyAmount", moneyAmount)
                .setParameter("userId", userId)
                .executeUpdate();

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
