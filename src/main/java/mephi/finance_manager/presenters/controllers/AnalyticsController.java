package mephi.finance_manager.presenters.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mephi.finance_manager.domain.dto.BudgetExceededAlertDto;
import mephi.finance_manager.domain.dto.BudgetStatDto;
import mephi.finance_manager.domain.dto.MoneyStatDto;
import mephi.finance_manager.domain.exceptions.NoSuchUserException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.interactors.AnalyticsInteractor;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsInteractor analyticsInteractor;

    public AnalyticsController(AnalyticsInteractor analyticsInteractor) {
        this.analyticsInteractor = analyticsInteractor;
    }

    @GetMapping("/money-stats")
    public ResponseEntity<MoneyStatDto> getOverallMoneyStats(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            MoneyStatDto stats = analyticsInteractor.getOverallMoneyStatisticForUserByToken(token, 0);
            return ResponseEntity.ok(stats);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/budget-stats")
    public ResponseEntity<BudgetStatDto> getBudgetStats(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            BudgetStatDto stats = analyticsInteractor.getBudgetStatForUserByToken(token);
            return ResponseEntity.ok(stats);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        } catch (NoSuchUserException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/budget-alerts")
    public ResponseEntity<BudgetExceededAlertDto> getBudgetAlerts(
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            BudgetExceededAlertDto alert = analyticsInteractor.getAlertForBudgetByUserToken(token);
            return ResponseEntity.ok(alert);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}