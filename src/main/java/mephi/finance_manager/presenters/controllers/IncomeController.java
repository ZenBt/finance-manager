package mephi.finance_manager.presenters.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import mephi.finance_manager.domain.dto.IncomeDto;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody AddIncomeRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            IncomeDto income = incomeInteractor.addIncomeForUserByToken(token, request.getCategoryId(),
                    request.getAmountReceived());
            return ResponseEntity.ok(income);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header must starts with Bearer");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
