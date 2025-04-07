package com.cmc.sp.webprak.testDAO;

import com.cmc.sp.webprak.DAO.PartnersDAO;
import com.cmc.sp.webprak.classes.Partners;
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
public class PartnersDAOTest {

    @Autowired
    private PartnersDAO partnersDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testSimpleManipulations() {
        List<Partners> allPartners = (List<Partners>) partnersDAO.getAll();
        assertEquals(3, allPartners.size()); // Ожидаем 3 партнёра из @BeforeEach

        List<Partners> applePartners = partnersDAO.getPartnersByName("Apple");
        assertEquals(1, applePartners.size());
        assertEquals("Apple Inc.", applePartners.getFirst().getName());

        Partners partnerId1 = partnersDAO.getById(1L);
        assertEquals(1, partnerId1.getId());
        assertEquals("Apple Inc.", partnerId1.getName());

        Partners notExistPartner = partnersDAO.getById(100L);
        assertNull(notExistPartner);

        List<Partners> notExistPartner2 = partnersDAO.getPartnersByName("Yandex");
        assertNull(notExistPartner2);
    }

    @Test
    void testGetPartnersByType() {
        List<Partners> suppliers = partnersDAO.getPartnersByType(Partners.PartnerType.supplier);
        assertEquals(1, suppliers.size());
        assertEquals("Microsoft", suppliers.getFirst().getName());

        partnersDAO.delete(suppliers.getFirst());
        List<Partners> deleted_suppliers = partnersDAO.getPartnersByType(Partners.PartnerType.supplier);
        assertNull(deleted_suppliers);
    }

    @Test
    void testUpdate() {
        String newContactInfo = "new.contact@microsoft.com";

        Partners microsoft = partnersDAO.getPartnersByName("Microsoft").getFirst();
        microsoft.setContactInfo(newContactInfo);
        partnersDAO.update(microsoft);

        Partners updatedMicrosoft = partnersDAO.getPartnersByName("Microsoft").getFirst();
        assertEquals(newContactInfo, updatedMicrosoft.getContactInfo());
    }

    @Test
    void testDelete() {
        Partners google = partnersDAO.getPartnersByName("Google").getFirst();
        partnersDAO.delete(google);

        List<Partners> googleList = partnersDAO.getPartnersByName("Google");
        assertNull(googleList);
    }

    @BeforeEach
    void beforeEach() {
        List<Partners> partnersList = new ArrayList<>();

        Partners apple = new Partners();
        apple.setName("Apple Inc.");
        apple.setContactInfo("contact@apple.com");
        apple.setPartnerType(Partners.PartnerType.consumer);
        partnersList.add(apple);

        Partners microsoft = new Partners();
        microsoft.setName("Microsoft");
        microsoft.setContactInfo("contact@microsoft.com");
        microsoft.setPartnerType(Partners.PartnerType.supplier);
        partnersList.add(microsoft);

        Partners google = new Partners();
        google.setName("Google");
        google.setContactInfo("contact@google.com");
        google.setPartnerType(Partners.PartnerType.both);
        partnersList.add(google);

        partnersDAO.saveCollection(partnersList);
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

            // Включаем проверку внешних ключей
            session.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();

            // Сброс автоинкремента
            session.createNativeQuery("ALTER TABLE partners AUTO_INCREMENT = 1").executeUpdate();
            session.getTransaction().commit();
        }
    }
}