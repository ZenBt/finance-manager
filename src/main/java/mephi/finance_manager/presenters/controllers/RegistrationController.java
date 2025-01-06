package mephi.finance_manager.presenters.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mephi.finance_manager.domain.exceptions.RegistrationFailedException;
import mephi.finance_manager.domain.interactors.RegistrationInteractor;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationInteractor registrationInteractor;

    public RegistrationController(RegistrationInteractor registrationInteractor) {
        this.registrationInteractor = registrationInteractor;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestParam String login, @RequestParam String password) {
        try {
            String token = registrationInteractor.registerUser(login, password);
            return ResponseEntity.ok(token);
        } catch (RegistrationFailedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}