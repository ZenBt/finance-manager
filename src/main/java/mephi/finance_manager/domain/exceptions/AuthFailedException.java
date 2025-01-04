package mephi.finance_manager.domain.exceptions;

public class AuthFailedException extends Exception {
    public AuthFailedException(String message) {
        super(message);
    }
}
