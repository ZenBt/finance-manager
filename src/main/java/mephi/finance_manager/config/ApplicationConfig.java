package mephi.finance_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.persistence.EntityManager;
import mephi.finance_manager.data.repositories.CategoryRepositoryImpl;
import mephi.finance_manager.data.repositories.ExpenseRepositoryImpl;
import mephi.finance_manager.data.repositories.IncomeRepositoryImpl;
import mephi.finance_manager.data.repositories.UserRepositoryImpl;
import mephi.finance_manager.data.repositories.UserTokenRepositoryImpl;
import mephi.finance_manager.domain.interactors.AnalyticsInteractor;
import mephi.finance_manager.domain.interactors.AuthInteractor;
import mephi.finance_manager.domain.interactors.CategoryInteractor;
import mephi.finance_manager.domain.interactors.ExpenseInteractor;
import mephi.finance_manager.domain.interactors.IncomeInteractor;
import mephi.finance_manager.domain.interactors.RegistrationInteractor;
import mephi.finance_manager.domain.repositories.CategoryRepository;
import mephi.finance_manager.domain.repositories.ExpenseRepository;
import mephi.finance_manager.domain.repositories.IncomeRepository;
import mephi.finance_manager.domain.repositories.UserRepository;
import mephi.finance_manager.domain.repositories.UserTokenRepository;

@Configuration
public class ApplicationConfig {

    private final EntityManager entityManager;
    private final StringRedisTemplate stringRedisTemplate;

    public ApplicationConfig(EntityManager entityManager, StringRedisTemplate stringRedisTemplate) {
        this.entityManager = entityManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return new CategoryRepositoryImpl(entityManager);
    }

    @Bean
    public ExpenseRepository expenseRepository() {
        return new ExpenseRepositoryImpl(entityManager);
    }

    @Bean
    public IncomeRepository incomeRepository() {
        return new IncomeRepositoryImpl(entityManager);
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl(entityManager);
    }

    @Bean
    public UserTokenRepository userTokenRepository() {
        return new UserTokenRepositoryImpl(stringRedisTemplate);
    }

    @Bean
    public AuthInteractor authInteractor(UserTokenRepository userTokenRepo, UserRepository userRepo) {
        return new AuthInteractor(userRepo, userTokenRepo);
    }

    @Bean
    public RegistrationInteractor registrationInteractor(UserRepository userRepo, UserTokenRepository userTokenRepo) {
        return new RegistrationInteractor(userRepo, userTokenRepo);
    }

    @Bean
    public ExpenseInteractor expenseInteractor(
            ExpenseRepository expenseRepo,
            UserTokenRepository userTokenRepo,
            UserRepository userRepo) {
        return new ExpenseInteractor(userTokenRepo, expenseRepo, userRepo);
    }

    @Bean
    public IncomeInteractor incomeInteractor(
            IncomeRepository incomeRepo,
            UserTokenRepository userTokenRepo,
            UserRepository userRepo) {
        return new IncomeInteractor(incomeRepo, userTokenRepo, userRepo);
    }

    @Bean
    public CategoryInteractor categoryInteractor(CategoryRepository categoryRepo, UserTokenRepository userTokenRepo) {
        return new CategoryInteractor(categoryRepo, userTokenRepo);
    }

    @Bean
    public AnalyticsInteractor analyticsInteractor(
            ExpenseRepository expenseRepo,
            UserTokenRepository userTokenRepo,
            IncomeRepository incomeRepo,
            CategoryRepository categoryRepo,
            UserRepository userRepo) {
        return new AnalyticsInteractor(expenseRepo, userTokenRepo, incomeRepo, categoryRepo, userRepo);
    }
}