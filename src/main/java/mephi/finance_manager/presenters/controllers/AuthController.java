package mephi.finance_manager.presenters.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import mephi.finance_manager.domain.exceptions.AuthFailedException;
import mephi.finance_manager.domain.interactors.AuthInteractor;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthInteractor authInteractor;

    public AuthController(AuthInteractor authInteractor) {
        this.authInteractor = authInteractor;
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestParam String login, @RequestParam String password) {
        try {
            String token = authInteractor.getAuthToken(login, password);
            return ResponseEntity.ok(token);
        } catch (AuthFailedException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logOut(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractTokenFromHeader(authorizationHeader);
        authInteractor.logOutByToken(token);
        return ResponseEntity.noContent().build();
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header must starts with Bearer");
        }
        return authorizationHeader.substring(7); // Remove "Bearer " prefix
    }
}