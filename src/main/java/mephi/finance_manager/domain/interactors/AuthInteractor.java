package mephi.finance_manager.domain.interactors;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.exceptions.AuthFailedException;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class AuthInteractor {

    private final UserRepository userRepo;
    private final UserTokenRepository userTokenRepo;

    public AuthInteractor(UserRepository userRepo, UserTokenRepository userTokenRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
    }

    public String getAuthToken(String login, String password) throws AuthFailedException {
        Optional<UserDto> user = userRepo.getUserByLogin(login);
        if (user.isEmpty() || !isPasswordMatchesHash(password, user.get().getPassword())) {
            throw new AuthFailedException("Неверный логин или пароль");
        }

        String token = UUID.randomUUID().toString();

        userTokenRepo.saveTokenForUserId(token, user.get().getUserId());

        return token;
    }

    private boolean isPasswordMatchesHash(String rawPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, hashedPassword);
    }

    public void logOutByToken(String token) {
        userTokenRepo.logOutByToken(token);
    }
}
