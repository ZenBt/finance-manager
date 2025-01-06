package mephi.finance_manager.data.repositories;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import mephi.finance_manager.domain.repositories.UserTokenRepository;

@Repository
public class UserTokenRepositoryImpl extends UserTokenRepository {
    private final StringRedisTemplate redisTemplate;
    private final Duration TOKEN_LIFETIME = Duration.ofHours(10);

    public UserTokenRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveTokenForUserId(String token, Long userId) {
        redisTemplate.opsForValue().set(token, userId.toString(), TOKEN_LIFETIME);
    }

    @Override
    public Optional<Long> getUserIdByUserToken(String token) {
        String userId = redisTemplate.opsForValue().get(token);
        if (userId != null) {
            return Optional.of(Long.valueOf(userId));
        }
        return Optional.empty();
    }

    @Override
    public void logOutByToken(String token) {
        redisTemplate.opsForValue().getOperations().delete(token);
    }

}
