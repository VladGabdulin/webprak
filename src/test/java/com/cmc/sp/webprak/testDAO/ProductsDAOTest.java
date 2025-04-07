package com.cmc.sp.webprak.testDAO;

import com.cmc.sp.webprak.DAO.ProductsDAO;
import com.cmc.sp.webprak.classes.Products;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations="classpath:application.properties")
public class ProductsDAOTest {

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        List<Products> allProducts = (List<Products>) productsDAO.getAll();
        assertEquals(3, allProducts.size());

        List<Products> electronicsProducts = productsDAO.getProductsByCategory("Electronics");
        assertEquals(2, electronicsProducts.size());
        assertEquals("Smartphone", electronicsProducts.getFirst().getName());

        Products product = productsDAO.getById(2L);
        assertEquals("Laptop", product.getName());
        assertEquals("Electronics", product.getCategory());

        Products nonExistingProduct = productsDAO.getById(100L);
        assertNull(nonExistingProduct);
    }

    @Test
    void testGetProductsByCategory() {
        List<Products> electronics = productsDAO.getProductsByCategory("Electronics");
        assertEquals(2, electronics.size());

        List<Products> literature = productsDAO.getProductsByCategory("Literature");
        assertEquals(1, literature.size());
        assertEquals("Book", literature.getFirst().getName());

        List<Products> toys = productsDAO.getProductsByCategory("Toys");
        assertNull(toys);
    }

    @Test
    void testUpdate() {
        Long newQuantity = 500L;

        Products smartphone = productsDAO.getById(1L);
        smartphone.setQuantity(newQuantity);
        productsDAO.update(smartphone);

        Products updatedSmartphone = productsDAO.getById(1L);
        assertEquals(newQuantity, updatedSmartphone.getQuantity());
    }

    @Test
    void testDelete() {
        Products book = productsDAO.getById(3L);
        productsDAO.delete(book);

        Products deletedBook = productsDAO.getById(3L);
        assertNull(deletedBook);
    }

    @BeforeEach
    void beforeEach() {
        List<Products> productsList = new ArrayList<>();

        Products smartphone = new Products();
        smartphone.setName("Smartphone");
        smartphone.setCategory("Electronics");
        smartphone.setQuantity(300L);
        productsList.add(smartphone);

        Products laptop = new Products();
        laptop.setName("Laptop");
        laptop.setCategory("Electronics");
        laptop.setQuantity(700L);
        productsList.add(laptop);

        Products book = new Products();
        book.setName("Book");
        book.setCategory("Literature");
        book.setQuantity(30L);
        productsList.add(book);

        productsDAO.saveCollection(productsList);
    }

    @BeforeAll
    @AfterEach
    void cleanup() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Отключаем проверку внешних ключей для MySQL
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            // Очищаем таблицы (если есть связанные таблицы, добавьте их здесь)
            session.createNativeQuery("DELETE FROM operation_details").executeUpdate();
            session.createNativeQuery("DELETE FROM products").executeUpdate();

            // Включаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            // Сброс автоинкремента для MySQL
            session.createNativeQuery("ALTER TABLE products AUTO_INCREMENT = 1").executeUpdate();
            session.getTransaction().commit();
        }
    }
}