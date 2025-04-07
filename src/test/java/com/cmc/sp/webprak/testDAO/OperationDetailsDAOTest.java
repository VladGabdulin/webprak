package com.cmc.sp.webprak.testDAO;

import com.cmc.sp.webprak.DAO.*;
import com.cmc.sp.webprak.classes.*;
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
public class OperationDetailsDAOTest {

    @Autowired
    private OperationDetailsDAO operationDetailsDAO;

    @Autowired
    private OperationsDAO operationsDAO;

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private PartnersDAO partnersDAO;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private Operations testOperation;
    private Products testProduct;
    private final Date testDate1 = Date.valueOf("2023-01-01");

    @Test
    void testSimpleManipulations() {
        List<OperationDetails> allDetails = (List<OperationDetails>) operationDetailsDAO.getAll();
        assertEquals(2, allDetails.size());

        OperationDetails detailById = operationDetailsDAO.getById(1L);
        assertEquals(10, detailById.getQuantity());
        assertEquals(testOperation.getId(), detailById.getOperation().getId());

        OperationDetails nonExistingDetail = operationDetailsDAO.getById(100L);
        assertNull(nonExistingDetail);
    }

    @Test
    void testGetDetailsByOperation() {
        List<OperationDetails> operationDetails = operationDetailsDAO.getOperationsDetailsByOperation(testOperation);
        assertEquals(2, operationDetails.size());
        assertEquals(testProduct.getId(), operationDetails.getFirst().getProduct().getId());

        Operations nonExistingOperation = new Operations();
        nonExistingOperation.setId(999L);
        List<OperationDetails> nonExistingDetails = operationDetailsDAO.getOperationsDetailsByOperation(nonExistingOperation);
        assertNull(nonExistingDetails);
    }

    @Test
    void testGetDetailsByProduct() {
        List<OperationDetails> productDetails = operationDetailsDAO.getOperationsDetailsByProduct(testProduct);
        assertEquals(2, productDetails.size());
        assertEquals(testOperation.getId(), productDetails.getFirst().getOperation().getId());

        Products nonExistingProduct = new Products();
        nonExistingProduct.setId(999L);
        List<OperationDetails> nonExistingDetails = operationDetailsDAO.getOperationsDetailsByProduct(nonExistingProduct);
        assertNull(nonExistingDetails);
    }

    @Test
    void testUpdate() {
        Long newQuantity = 15L;

        OperationDetails detail = operationDetailsDAO.getById(1L);
        detail.setQuantity(newQuantity);
        operationDetailsDAO.update(detail);

        OperationDetails updatedDetail = operationDetailsDAO.getById(1L);
        assertEquals(newQuantity, updatedDetail.getQuantity());
    }

    @Test
    void testDelete() {
        OperationDetails detail = operationDetailsDAO.getById(2L);
        operationDetailsDAO.delete(detail);

        OperationDetails deletedDetail = operationDetailsDAO.getById(2L);
        assertNull(deletedDetail);
    }

    @BeforeEach
    void beforeEach() {
        // Создаем тестового партнера
        Partners testPartner = new Partners();
        testPartner.setName("Test Partner");
        testPartner.setContactInfo("partner@test.com");
        testPartner.setPartnerType(Partners.PartnerType.supplier);
        partnersDAO.save(testPartner);

        // Создаем тестового пользователя
        Users testUser = new Users();
        testUser.setName("Test User");
        testUser.setPassword("1234");
        testUser.setRole("admin");
        usersDAO.save(testUser);

        // Создаем тестовую операцию
        testOperation = new Operations();
        testOperation.setOperationDate(testDate1);
        testOperation.setOperationType(Operations.OperationType.in);
        testOperation.setPartner(testPartner);
        testOperation.setUser(testUser);
        operationsDAO.save(testOperation);

        // Создаем тестовый продукт
        testProduct = new Products();
        testProduct.setName("Smartphone");
        testProduct.setCategory("Electronics");
        testProduct.setQuantity(300L);
        productsDAO.save(testProduct);

        // Создаем тестовые детали операций
        List<OperationDetails> detailsList = new ArrayList<>();

        OperationDetails detail1 = new OperationDetails();
        detail1.setOperation(testOperation);
        detail1.setProduct(testProduct);
        detail1.setQuantity(10L);
        detailsList.add(detail1);

        OperationDetails detail2 = new OperationDetails();
        detail2.setOperation(testOperation);
        detail2.setProduct(testProduct);
        detail2.setQuantity(5L);
        detailsList.add(detail2);

        operationDetailsDAO.saveCollection(detailsList);
    }

    @BeforeAll
    @AfterEach
    void cleanup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Отключаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            // Очищаем таблицы в правильном порядке
            session.createNativeQuery("DELETE FROM operation_details").executeUpdate();
            session.createNativeQuery("DELETE FROM operations").executeUpdate();
            session.createNativeQuery("DELETE FROM products").executeUpdate();
            session.createNativeQuery("DELETE FROM partners").executeUpdate();
            session.createNativeQuery("DELETE FROM users").executeUpdate();

            // Включаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            // Сброс автоинкремента
            session.createNativeQuery("ALTER TABLE operation_details AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE operations AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE products AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE partners AUTO_INCREMENT = 1").executeUpdate();
            session.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
            session.getTransaction().commit();
        }
    }
}