package mephi.finance_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            UserRepository userRepo,
            CategoryRepository categoryRepo) {
        return new ExpenseInteractor(userTokenRepo, expenseRepo, userRepo, categoryRepo);
    }

    @Bean
    public IncomeInteractor incomeInteractor(
            IncomeRepository incomeRepo,
            UserTokenRepository userTokenRepo,
            UserRepository userRepo,
            CategoryRepository categoryRepo) {
        return new IncomeInteractor(incomeRepo, userTokenRepo, userRepo, categoryRepo);
    }

    @Bean
    public CategoryInteractor categoryInteractor(CategoryRepository categoryRepo, UserTokenRepository userTokenRepo,
            ExpenseRepository expenseRepo, IncomeRepository incomeRepo) {
        return new CategoryInteractor(categoryRepo, userTokenRepo, expenseRepo, incomeRepo);
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