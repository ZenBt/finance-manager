package mephi.finance_manager.domain.interactors;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mephi.finance_manager.domain.dto.UserDto;
import mephi.finance_manager.domain.exceptions.RegistrationFailedException;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

public class RegistrationInteractor {

    private final UserRepository userRepo;
    private final UserTokenRepository userTokenRepo;

    public RegistrationInteractor(UserRepository userRepo, UserTokenRepository userTokenRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
    }

    public String registerUser(String login, String password) throws RegistrationFailedException {
        Optional<UserDto> user = userRepo.getUserByLogin(login);
        if (user.isPresent()) {
            throw new RegistrationFailedException("Пользователь уже существует");
        }
        String pwdHash = makePasswordHash(password);

        UserDto userDto = userRepo.createUser(login, pwdHash);

        String token = UUID.randomUUID().toString();

        userTokenRepo.saveTokenForUserId(token, userDto.getUserId());

        return token;

    }

    private String makePasswordHash(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }
}
