package mephi.finance_manager.presenters.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.exceptions.CategoryDeletionFailedException;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.interactors.CategoryInteractor;
import mephi.finance_manager.presenters.schemas.AddCategoryRequest;
import mephi.finance_manager.presenters.schemas.UpdateCategoryBudgetRequest;
import mephi.finance_manager.presenters.schemas.UpdateCategoryNameRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryInteractor categoryInteractor;

    public CategoryController(CategoryInteractor categoryInteractor) {
        this.categoryInteractor = categoryInteractor;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            List<CategoryDto> categories = categoryInteractor.getCategoriesByUserToken(token);
            return ResponseEntity.ok(categories);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody AddCategoryRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            CategoryDto category = categoryInteractor.createCategoryForUserByToken(token, request.getCategoryName(),
                    request.getBudget(), request.getCategoryType());
            return ResponseEntity.ok(category);
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            categoryInteractor.deleteCategoryByIdAndToken(token, categoryId);
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (CategoryDeletionFailedException e) {
            String m = e.getMessage();
            System.out.println(m);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{categoryId}/budget")
    public ResponseEntity<Void> changeCategoryBudget(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryBudgetRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            categoryInteractor.changeCategoryBudgetByIdAndToken(token, categoryId, request.getBudget());
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PatchMapping("/{categoryId}/name")
    public ResponseEntity<Void> changeCategoryName(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId, @Valid @RequestBody UpdateCategoryNameRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            categoryInteractor.changeCategoryNameByIdAndToken(token, categoryId, request.getCategoryName());
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (PermissionDeniedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header must starts with Bearer");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
