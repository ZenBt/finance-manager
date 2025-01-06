package mephi.finance_manager.presenters.controllers;

import java.util.List;

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

import mephi.finance_manager.domain.dto.CategoryDto;
import mephi.finance_manager.domain.exceptions.CategoryNotFoundException;
import mephi.finance_manager.domain.exceptions.PermissionDeniedException;
import mephi.finance_manager.domain.exceptions.TokenNotFoundOrExpiredException;
import mephi.finance_manager.domain.interactors.CategoryInteractor;
import mephi.finance_manager.presenters.schemas.AddCategoryRequest;
import mephi.finance_manager.presenters.schemas.UpdateCategoryRequest;

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
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AddCategoryRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            CategoryDto category = categoryInteractor.createCategoryForUserByToken(token, request.getCategoryName(),
                    request.getBudget(), request.getCategoryType());
            return ResponseEntity.ok(category);
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).body(null);
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
            return ResponseEntity.status(401).build();
        } catch (PermissionDeniedException e) {
            return ResponseEntity.status(403).build();
        }
    }

    @PatchMapping("/{categoryId}/budget")
    public ResponseEntity<Void> changeCategoryBudget(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId, @RequestBody UpdateCategoryRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            categoryInteractor.changeCategoryBudgetByIdAndToken(token, categoryId, request.getBudget());
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).build();
        } catch (PermissionDeniedException e) {
            return ResponseEntity.status(403).build();
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PatchMapping("/{categoryId}/name")
    public ResponseEntity<Void> changeCategoryName(@RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long categoryId, @RequestBody UpdateCategoryRequest request) {
        try {
            String token = extractTokenFromHeader(authorizationHeader);
            categoryInteractor.changeCategoryNameByIdAndToken(token, categoryId, request.getCategoryName());
            return ResponseEntity.noContent().build();
        } catch (TokenNotFoundOrExpiredException e) {
            return ResponseEntity.status(401).build();
        } catch (PermissionDeniedException e) {
            return ResponseEntity.status(403).build();
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}
