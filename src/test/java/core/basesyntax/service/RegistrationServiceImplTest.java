package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void register_nullUser_notOk() {
        assertThrows(UserNullException.class, () -> registrationService.register(null));
    }

    @Test
    void register_existingLogin_notOk() {
        User anna = new User(2L, "anna_smith", "qwerasd", 30);
        assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(anna));
    }

    @Test
    void register_newUser_ok() {
        User jack = new User(3L, "jack_McKartney", "HolleyWooD", 19);
        User actual = registrationService.register(jack);
        assertNotNull(actual);
    }

    @Test
    void register_ageBelow18_notOk() {
        User kai = new User(3L, "kai_lepksi", "kardasda", 12);
        assertThrows(AgePermissionException.class, () -> registrationService.register(kai));
    }

    @Test
    void register_loginLengthLessThan6_notOk() {
        User alex = new User(5L, "alex", "securePass1", 32);
        assertThrows(LoginLengthException.class, () -> registrationService.register(alex));
    }

    @Test
    void register_passwordLengthLessThan6_notOk() {
        User may = new User(6L, "may_turtle", "asbc", 21);
        assertThrows(PasswordLengthException.class, () -> registrationService.register(may));
    }

    @Test
    void register_ageExactly18_ok() {
        User mike = new User(3L, "mikeasd1", "helloWorld!", 18);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void register_loginLengthExactly6_ok() {
        User mike = new User(5L, "mike88", "helload2!", 21);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void register_passwordLengthExactly6_ok() {
        User mike = new User(7L, "mike89", "helloo", 29);
        User actual = assertDoesNotThrow(() -> registrationService.register(mike));
        assertNotNull(actual);
    }

    @Test
    void register_nullLogin_notOk() {
        User u = new User(1L, null, "password123", 20);
        assertThrows(LoginNullException.class, () -> registrationService.register(u));
    }

    @Test
    void register_nullPassword_notOk() {
        User u = new User(1L, "null123123", null, 20);
        assertThrows(PasswordNullException.class, () -> registrationService.register(u));
    }

    @Test
    void register_nullAge_notOk() {
        User u = new User(1L, "null123123", "asdasdasd", null);
        assertThrows(AgeNullException.class, () -> registrationService.register(u));
    }

    @Test
    void register_validUser_increasesStorageSizeByOne_ok() {
        int sizeBefore = Storage.people.size();
        User u = new User(99L, "unique_aaa", "secret9", 22);
        registrationService.register(u);
        assertEquals(sizeBefore + 1, Storage.people.size());
    }

    @Test
    void register_loginLength5_notOk() {
        User u = new User(107L, "aaaaa", "password", 20);
        assertThrows(LoginLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void register_passwordLength5_notOk() {
        User u = new User(108L, "validL", "aaaaa", 20);
        assertThrows(PasswordLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void register_validUser_returnsSameInstance_ok() {
        User u = new User(300L, "same_instance", "password", 25);
        User returned = registrationService.register(u);
        assertSame(u, returned);
    }

    @Test
    void register_negativeAge_notOk() {
        User u = new User(104L, "neg_age", "password", -5);
        assertThrows(AgePermissionException.class, () -> registrationService.register(u));
    }

    @Test
    void register_zeroAge_notOk() {
        User u = new User(10L, "Zero_age", "password12", 0);
        assertThrows(AgePermissionException.class, () -> registrationService.register(u));
    }

    @Test
    void register_blankLogin_notOk() {
        User u = new User(105L, "  ", "password", 20);
        assertThrows(LoginLengthException.class, () -> registrationService.register(u));
    }

    @Test
    void register_blankPassword_notOk() {
        User u = new User(106L, "blank_pwd_user", "     ", 20);
        assertThrows(PasswordLengthException.class, () -> registrationService.register(u));
    }
}
