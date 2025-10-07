package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final Integer MIN_AGE = 18;
    private static final Integer ZERO = 0;
    private static final Integer MIN_LOGIN_LENGTH = 6;
    private static final Integer MIN_PASSWORD_LENGTH = 6;
    private final StorageDao storageDao = new StorageDaoImpl();

    @Override
    public User register(User user) {
        if (user == null) {
            throw new UserNullException("User cannot be null");
        }

        String login = user.getLogin();
        String password = user.getPassword();
        Integer age = user.getAge();

        if (login == null) {
            throw new LoginNullException("Login cannot be null");
        }
        if (password == null) {
            throw new PasswordNullException("Password cannot be null");
        }
        if (age == null) {
            throw new AgeNullException("User's age cannot be null");
        }

        login = login.trim();
        password = password.trim();

        if (login.isEmpty()) {
            throw new LoginLengthException("Login must be at least 6 characters");
        }
        if (password.isEmpty()) {
            throw new PasswordLengthException("Password must be at least 6 characters");
        }

        if (login.length() < MIN_LOGIN_LENGTH) {
            throw new LoginLengthException("Login must have more than 6 symbols! Yours have: "
                    + login.length());
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordLengthException("Password must have more than 6 symbols! Yours have: "
                    + password.length());
        }

        if (age < ZERO) {
            throw new AgePermissionException("Age cannot be negative! Your age: "
                    + age);
        }

        if (age.equals(ZERO)) {
            throw new AgePermissionException("Age cannot be " + ZERO + " Your age: "
                    + age);
        }

        if (age < MIN_AGE) {
            throw new AgePermissionException("Your age must be greater than " + MIN_AGE
                    + " Your age: " + age);
        }

        if (storageDao.get(login) != null) {
            throw new UserAlreadyExistsException("This user already exists in data!");
        }

        return storageDao.add(user);
    }
}
