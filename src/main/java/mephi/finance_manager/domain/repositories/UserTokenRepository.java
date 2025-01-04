package mephi.finance_manager.domain.repositories;

import java.util.Optional;

public abstract class UserTokenRepository {
    public abstract void saveTokenForUserId(String token, Long userId);

    public abstract Optional<Long> getUserIdByUserToken(String token);

    public abstract void logOutByToken(String token);
}
