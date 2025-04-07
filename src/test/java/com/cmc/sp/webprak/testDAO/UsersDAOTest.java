package com.cmc.sp.webprak.testDAO;

import com.cmc.sp.webprak.DAO.UsersDAO;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class UsersDAOTest {

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        List<Users> allUsers = (List<Users>) usersDAO.getAll();
        assertEquals(3, allUsers.size());

        List<Users> johnUsers = usersDAO.getAllUsersByName("John");
        assertEquals(1, johnUsers.size());
        assertEquals("John Doe", johnUsers.getFirst().getName());
        assertEquals("admin", johnUsers.getFirst().getRole());

        Users userById = usersDAO.getById(2L);
        assertEquals("Jane Smith", userById.getName());
        assertEquals("manager", userById.getRole());

        Users nonExistingUser = usersDAO.getById(100L);
        assertNull(nonExistingUser);

        List<Users> nonExistingUsers = usersDAO.getAllUsersByName("NonExisting");
        assertNull(nonExistingUsers);
    }

    @Test
    void testGetUsersByRole() {
        List<Users> admins = usersDAO.getAllUsersSameRole("admin");
        assertEquals(1, admins.size());
        assertEquals("John Doe", admins.getFirst().getName());

        List<Users> managers = usersDAO.getAllUsersSameRole("manager");
        assertEquals(1, managers.size());
        assertEquals("Jane Smith", managers.getFirst().getName());

        List<Users> nonExistingRole = usersDAO.getAllUsersSameRole("non-existing-role");
        assertNull(nonExistingRole);

        // Проверка после удаления
        usersDAO.delete(admins.getFirst());
        List<Users> deletedAdmins = usersDAO.getAllUsersSameRole("admin");
        assertNull(deletedAdmins);
    }

    @Test
    void testUpdate() {
        String newRole = "tester";

        Users jane = usersDAO.getAllUsersByName("Jane").getFirst();
        jane.setRole(newRole);
        usersDAO.update(jane);

        Users updatedJane = usersDAO.getAllUsersByName("Jane").getFirst();
        assertEquals(newRole, updatedJane.getRole());
    }

    @Test
    void testDelete() {
        Users bob = usersDAO.getAllUsersByName("Bob").getFirst();
        usersDAO.delete(bob);

        List<Users> deletedBob = usersDAO.getAllUsersByName("Bob");
        assertNull(deletedBob);
    }

    @BeforeEach
    void beforeEach() throws NoSuchAlgorithmException {
        List<Users> usersList = new ArrayList<>();

        MessageDigest md = MessageDigest.getInstance("MD5");

        Users john = new Users();
        john.setName("John Doe");
        byte[] password_hash = md.digest("1234".getBytes());
        john.setPassword(Arrays.toString(password_hash));
        john.setRole("admin");
        usersList.add(john);

        Users jane = new Users();
        jane.setName("Jane Smith");
        password_hash = md.digest("0000".getBytes());
        jane.setPassword(Arrays.toString(password_hash));
        jane.setRole("manager");
        usersList.add(jane);

        Users bob = new Users();
        bob.setName("Bob Jackson");
        password_hash = md.digest("28052005".getBytes());
        bob.setPassword(Arrays.toString(password_hash));
        bob.setRole("user");
        usersList.add(bob);

        usersDAO.saveCollection(usersList);
    }

    @BeforeAll
    @AfterEach
    void cleanup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Отключаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            // Очищаем таблицы в правильном порядке (если есть связанные таблицы)
            session.createNativeQuery("DELETE FROM operations").executeUpdate();
            session.createNativeQuery("DELETE FROM users").executeUpdate();

            // Включаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            // Сброс автоинкремента
            session.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            session.getTransaction().commit();
        }
    }
}