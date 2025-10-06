package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
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
            throw new LoginNullException("Login must be at least 6 characters");
        }
        if (password == null) {
            throw new PasswordNullException("Password must be at least 6 characters");
        }
        if (age == null) {
            throw new AgeNullException("User must be at least 18");
        }

        if (login.length() < 6) {
            throw new LoginLengthException("Login must have more than 6 symbols! Yours have: "
                    + login.length());
        }
        if (password.length() < 6) {
            throw new PasswordLengthException("Password must have more than 6 symbols! Yours have: "
                    + password.length());
        }
        if (age < 18) {
            throw new AgePermissionException("Your age must be greater that 18! Your age: "
                    + age);
        }

        if (storageDao.get(login) != null) {
            throw new UserAlreadyExistsException("This user already exists in data!");
        }

        storageDao.add(user);
        return user;
    }
}
