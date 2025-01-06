package mephi.finance_manager.presenters.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mephi.finance_manager.domain.dto.IncomeDto;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.interactors.IncomeInteractor;
import mephi.finance_manager.presenters.schemas.AddIncomeRequest;

@RestController
@RequestMapping("/api/incomes")
public class IncomeController {

    private final IncomeInteractor incomeInteractor;

    public IncomeController(IncomeInteractor incomeInteractor) {
        this.incomeInteractor = incomeInteractor;
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getAllIncomes(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            List<IncomeDto> incomes = incomeInteractor.getAllIncomesByUserToken(token);
            return ResponseEntity.ok(incomes);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<IncomeDto>> getIncomesByCategories(
            @RequestHeader("Authorization") String authorizationHeader, @RequestParam Long[] categoryIds) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            List<IncomeDto> incomes = incomeInteractor.getIncomesByUserTokenAndCategories(token, categoryIds);
            return ResponseEntity.ok(incomes);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AddIncomeRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            IncomeDto income = incomeInteractor.addIncomeForUserByToken(token, request.getCategoryId(),
                    request.getAmountReceived());
            return ResponseEntity.ok(income);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @DeleteMapping("/{incomeId}")
    public ResponseEntity<Void> deleteIncome(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long incomeId) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            incomeInteractor.deleteIncomeForUserByIdAndToken(token, incomeId);
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).build();
        } catch (PermissionDeniedException e) {
            return ResponseEntity.status(403).build();
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
