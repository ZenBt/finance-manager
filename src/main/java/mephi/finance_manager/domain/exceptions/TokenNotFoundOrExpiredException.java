package mephi.finance_manager.domain.exceptions;

public class TokenNotFoundOrExpiredException extends Exception {
    public TokenNotFoundOrExpiredException() {
        super("Время жизни токена истекло, либо токен неверен/незапрашивался");
    }
}
