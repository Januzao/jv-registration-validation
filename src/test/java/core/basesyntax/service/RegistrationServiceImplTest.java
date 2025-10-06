package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import core.basesyntax.db.Storage;
import core.basesyntax.model.User;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private final RegistrationService registrationService = new RegistrationServiceImpl();

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        List<User> sampleUsers = Arrays.asList(
                new User(1L, "john_doe", "password123", 25),
                new User(2L, "anna_smith", "qwerty456", 30),
                new User(4L, "kate_miller", "passKate99", 27),
                new User(6L, "lisa_white", "mySecret!", 19),
                new User(7L, "tom_jackson", "jackson777", 40),
                new User(8L, "sophie_brown", "sophPass2024", 28)
        );
        Storage.people.addAll(sampleUsers);

    }

    @AfterEach
    void tearDown() {
        Storage.people.clear();
    }

    @Test
    void registerNullUserThrowsUserNullException() {
        try {
            registrationService.register(null);
        } catch (UserNullException e) {
            return;
        }
        fail("UserNullException should be thrown if User value is null");
    }

    @Test
    void userExistsInData_ShouldThrow() {
        User anna = new User(2L, "anna_smith", "qwerasd", 30);
        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.register(anna));
    }

    @Test
    void userDontExistInData_Ok() {
        User jack = new User(3L, "jack_McKartney", "HolleyWooD", 19);
        User actual = registrationService.register(jack);
        assertNotNull(actual);
    }

    @Test
    void checkIfAgeIsOk_ShouldThrow() {
        User kai = new User(3L, "kai_lepksi", "kardasda", 12);
        assertThrows(AgePermissionException.class, () -> registrationService.register(kai));
    }

    @Test
    void checkLoginLength_ShouldThrow() {
        User alex = new User(5L, "alex", "securePass1", 32);
        assertThrows(LoginLengthException.class, () -> registrationService.register(alex));
    }

    @Test
    void checkPasswordLength_ShouldThrow() {
        User may = new User(6L, "may_turtle", "asbc", 21);
        assertThrows(PasswordLengthException.class, () -> registrationService.register(may));
    }

    @Test
    void ageExactly18_Ok() {
        User mike = new User(3L, "mikeasd1", "helloWorld!", 18);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void loginLengthHaveExactly6_Ok() {
        User mike = new User(5L, "mike88", "helload2!", 21);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void passwordLengthHaveExactly6_Ok() {
        User mike = new User(7L, "mike89", "helloo", 29);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void registerNullLogin_notOk() {
        User u = new User(1L, null, "password123", 20);
        assertThrows(LoginNullException.class, () -> registrationService.register(u));
    }

    @Test
    void registerNullPassword_notOk() {
        User u = new User(1L, "null123123", null, 20);
        assertThrows(PasswordNullException.class, () -> registrationService.register(u));
    }

    @Test
    void registerNullAge_notOk() {
        User u = new User(1L, "null123123", "asdasdasd", null);
        assertThrows(AgeNullException.class, () -> registrationService.register(u));
    }

    @Test
    void registerValidUserStorageSizeIncreasedByOne_ok() {
        int sizeBefore = Storage.people.size();
        User u = new User(99L, "unique_aaa", "secret9", 22);
        registrationService.register(u);

        assertEquals(sizeBefore + 1, Storage.people.size());
    }

    @Test
    void registerLoginLen5_notOk() {
        User u = new User(107L, "aaaaa", "password", 20); // 5
        assertThrows(LoginLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void registerPasswordLen5_notOk() {
        User u = new User(108L, "validL", "aaaaa", 20); // 5
        assertThrows(PasswordLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void registerReturnTheSameUser_Ok() {
        User u = new User(300L, "same_instance", "password", 25);
        User returned = registrationService.register(u);
        assertSame(u, returned);
    }

    @Test
    void registerNegativeAge_notOk() {
        User u = new User(104L, "neg_age", "password", -5);
        assertThrows(AgePermissionException.class, () -> registrationService.register(u));
    }

    @Test
    void registerZeroAge_notOk() {
        User u = new User(10L, "Zero_age", "password12", 0);
        assertThrows(AgePermissionException.class, () -> registrationService.register(u));
    }

    @Test
    void registerBlankLogin_notOk() {
        User u = new User(105L, "  ", "password", 20);
        assertThrows(LoginLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void register_blankPassword_notOk() {
        User u = new User(106L, "blank_pwd_user", "     ", 20);
        assertThrows(PasswordLengthException.class, () -> registrationService.register(u));
    }
}
