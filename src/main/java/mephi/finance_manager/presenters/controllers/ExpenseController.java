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
import mephi.finance_manager.domain.dto.ExpenseDto;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.interactors.ExpenseInteractor;
import mephi.finance_manager.presenters.schemas.AddExpenseRequest;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseInteractor expenseInteractor;

    public ExpenseController(ExpenseInteractor expenseInteractor) {
        this.expenseInteractor = expenseInteractor;
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpenses(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            List<ExpenseDto> expenses = expenseInteractor.getAllExpensesByUserToken(token);
            return ResponseEntity.ok(expenses);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ExpenseDto>> getExpensesByCategories(
            @RequestHeader("Authorization") String authorizationHeader, @RequestParam Long[] categoryIds) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            List<ExpenseDto> expenses = expenseInteractor.getExpensesByUserTokenAndCategories(token, categoryIds);
            return ResponseEntity.ok(expenses);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> addExpense(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody @Valid AddExpenseRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            ExpenseDto expense = expenseInteractor.addExpenseForUserByToken(token, request.getCategoryId(),
                    request.getAmountSpent());
            return ResponseEntity.ok(expense);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long expenseId) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            expenseInteractor.deleteExpenseForUserByIdAndToken(token, expenseId);
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
