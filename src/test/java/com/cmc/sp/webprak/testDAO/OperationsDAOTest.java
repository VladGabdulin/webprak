package com.cmc.sp.webprak.testDAO;

import com.cmc.sp.webprak.DAO.OperationsDAO;
import com.cmc.sp.webprak.DAO.PartnersDAO;
import com.cmc.sp.webprak.DAO.UsersDAO;
import com.cmc.sp.webprak.classes.Operations;
import com.cmc.sp.webprak.classes.Partners;
import com.cmc.sp.webprak.classes.Users;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class OperationsDAOTest {

    @Autowired
    private OperationsDAO operationsDAO;

    @Autowired
    private PartnersDAO partnersDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Partners testPartner;
    private Users testUser;
    private final Date testDate1 = Date.valueOf("2023-01-01");
    private final Date testDate2 = Date.valueOf("2023-02-01");

    @Test
    void testSimpleManipulations() {
        List<Operations> allOperations = (List<Operations>) operationsDAO.getAll();
        assertEquals(2, allOperations.size());

        Operations operationById = operationsDAO.getById(1L);
        assertEquals(testDate1, operationById.getOperationDate());
        assertEquals(Operations.OperationType.in, operationById.getOperationType());

        Operations nonExistingOperation = operationsDAO.getById(100L);
        assertNull(nonExistingOperation);
    }

    @Test
    void testGetOperationsByType() {
        List<Operations> inOperations = operationsDAO.getOperationsByType(Operations.OperationType.in);
        assertEquals(1, inOperations.size());
        assertEquals(testPartner.getId(), inOperations.getFirst().getPartner().getId());

        List<Operations> outOperations = operationsDAO.getOperationsByType(Operations.OperationType.out);
        assertEquals(1, outOperations.size());
        assertEquals(testUser.getId(), outOperations.getFirst().getUser().getId());

        operationsDAO.delete(outOperations.getFirst());
        List<Operations> deletedOuts = operationsDAO.getOperationsByType(Operations.OperationType.out);
        assertNull(deletedOuts);
    }

    @Test
    void testGetOperationsByPartner() {
        List<Operations> partnerOperations = operationsDAO.getOperationsByPartner(testPartner);
        assertEquals(2, partnerOperations.size());
        assertEquals(Operations.OperationType.in, partnerOperations.getFirst().getOperationType());

        operationsDAO.delete(partnerOperations.get(0));
        operationsDAO.delete(partnerOperations.get(1));
        List<Operations> nonExistingPartnerOperations = operationsDAO.getOperationsByPartner(testPartner);
        assertNull(nonExistingPartnerOperations);
    }

    @Test
    void testGetOperationsByUser() {
        List<Operations> userOperations = operationsDAO.getOperationsByUser(testUser);
        assertEquals(2, userOperations.size());
        assertEquals(Operations.OperationType.in, userOperations.getFirst().getOperationType());

        operationsDAO.delete(userOperations.get(0));
        operationsDAO.delete(userOperations.get(1));
        List<Operations> nonExistingUserOperations = operationsDAO.getOperationsByUser(testUser);
        assertNull(nonExistingUserOperations);
    }

    @Test
    void testUpdate() {
        Date newDate = Date.valueOf("2023-03-01");

        Operations operation = operationsDAO.getById(1L);
        operation.setOperationDate(newDate);
        operationsDAO.update(operation);

        Operations updatedOperation = operationsDAO.getById(1L);
        assertEquals(newDate, updatedOperation.getOperationDate());
    }

    @Test
    void testDelete() {
        Operations operation = operationsDAO.getById(2L);
        operationsDAO.delete(operation);

        Operations deletedOperation = operationsDAO.getById(2L);
        assertNull(deletedOperation);
    }

    @BeforeEach
    void beforeEach() {
        // Создаем тестового партнера
        testPartner = new Partners();
        testPartner.setName("Test Partner");
        testPartner.setContactInfo("partner@test.com");
        testPartner.setPartnerType(Partners.PartnerType.supplier);
        partnersDAO.save(testPartner);

        // Создаем тестового пользователя
        testUser = new Users();
        testUser.setName("Test User");
        testUser.setPassword("1234");
        testUser.setRole("admin");
        usersDAO.save(testUser);

        // Создаем тестовые операции
        List<Operations> operationsList = new ArrayList<>();

        Operations inOperation = new Operations();
        inOperation.setOperationDate(testDate1);
        inOperation.setOperationType(Operations.OperationType.in);
        inOperation.setPartner(testPartner);
        inOperation.setUser(testUser);
        operationsList.add(inOperation);

        Operations outOperation = new Operations();
        outOperation.setOperationDate(testDate2);
        outOperation.setOperationType(Operations.OperationType.out);
        outOperation.setPartner(testPartner);
        outOperation.setUser(testUser);
        operationsList.add(outOperation);

        operationsDAO.saveCollection(operationsList);
    }

    @BeforeAll
    @AfterEach
    void cleanup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Отключаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            // Очищаем таблицы в правильном порядке
            session.createNativeQuery("DELETE FROM operations").executeUpdate();
            session.createNativeQuery("DELETE FROM partners").executeUpdate();
            session.createNativeQuery("DELETE FROM users").executeUpdate();

            // Включаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            // Сброс автоинкремента
            session.createNativeQuery("ALTER TABLE operations AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE partners AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            session.getTransaction().commit();
        }
    }
}