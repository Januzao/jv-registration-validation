package core.basesyntax.service;

public class LoginLengthException extends IllegalArgumentException {
    public LoginLengthException(String message) {
        super(message);
    }
}
