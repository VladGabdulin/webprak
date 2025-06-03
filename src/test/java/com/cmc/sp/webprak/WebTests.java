package com.cmc.sp.webprak;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class WebTests {

    private final String rootTitle = "Главная страница";
    private final String OperationsTitle = "Операции";
    private final String PartnersTitle = "Партнёры";
    private final String ProductsTitle = "Товары";

    @Test
    void MainPage() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        assertEquals(rootTitle, driver.getTitle());
        driver.quit();
    }

    @Test
    void HeaderTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(0,0));
        driver.manage().window().setSize(new Dimension(1024,768));
        driver.get("http://localhost:8080/");

        WebElement operationsButton = driver.findElement(By.id("MainOperationsLink"));
        operationsButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(OperationsTitle, driver.getTitle());

        WebElement rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        WebElement partnersButton = driver.findElement(By.id("MainPartnersLink"));
        partnersButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(PartnersTitle, driver.getTitle());

        rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        WebElement productsButton = driver.findElement(By.id("MainProductsLink"));
        productsButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(ProductsTitle, driver.getTitle());

        rootButton = driver.findElement(By.id("rootLink"));
        rootButton.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals(rootTitle, driver.getTitle());

        driver.quit();
    }

    @Test
    void addPartner() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/partners");
        assertEquals(PartnersTitle, driver.getTitle());
        WebElement addPartner = driver.findElement(By.id("AddPartnerButton"));
        addPartner.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        String editPartnerTitle = "Добавление партнёра";
        assertEquals(editPartnerTitle, driver.getTitle());

        driver.findElement(By.id("NamePartner")).sendKeys("Тестовое имя");
        driver.findElement(By.id("ContactsPartner")).sendKeys("Тестовые контакты");
        WebElement typeDropdown = driver.findElement(By.id("TypePartner"));
        Select select = new Select(typeDropdown);
        select.selectByIndex(1);
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        assertEquals(PartnersTitle, driver.getTitle());
        WebElement partnersTable = driver.findElement(By.cssSelector("table.table"));
        List<WebElement> rows = partnersTable.findElements(By.cssSelector("tbody tr"));

        boolean isPartnerFound = false;
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 3 && cells.get(0).getText().equals("Тестовое имя")) {
                assertEquals("Тестовые контакты", cells.get(1).getText());
                assertEquals("supplier", cells.get(2).getText());
                isPartnerFound = true;
                break;
            }
        }

        assertTrue("Добавленный партнёр не найден в таблице", isPartnerFound);

        driver.quit();
    }

    @Test
    void editPartnerTest() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/partners");

        String originalWindow = driver.getWindowHandle();
        // Find and click edit button for first partner
        WebElement firstPartnerEditBtn = new WebDriverWait(driver, Duration.ofSeconds(6))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("table.table tbody tr:first-child .btn-warning")));
        firstPartnerEditBtn.click();

        // Verify edit page
        assertEquals("Редактирование партнёра", driver.getTitle());

        // Update partner info
        driver.findElement(By.id("NamePartner")).clear();
        driver.findElement(By.id("NamePartner")).sendKeys("Обновленное имя");

        driver.findElement(By.id("ContactsPartner")).clear();
        driver.findElement(By.id("ContactsPartner")).sendKeys("Новые контакты");

        Select typeDropdown = new Select(driver.findElement(By.id("TypePartner")));
        typeDropdown.selectByVisibleText("Consumer");

        driver.findElement(By.id("submitButton")).click();
        // Verify updated partner in table
        assertTrue("Ошибка обновления информации", isPartnerInTable(driver,"Обновленное имя", "Новые контакты", "consumer"));
        driver.quit();
    }

    private void fillPartnerForm(ChromeDriver driver, String name, String contacts, String type) {
        driver.get("http://localhost:8080/partners");
        driver.findElement(By.id("NamePartner")).clear();
        driver.findElement(By.id("NamePartner")).sendKeys(name);

        driver.findElement(By.id("ContactsPartner")).clear();
        driver.findElement(By.id("ContactsPartner")).sendKeys(contacts);

        Select typeDropdown = new Select(driver.findElement(By.id("TypePartner")));
        typeDropdown.selectByValue(type);

        driver.findElement(By.id("submitButton")).click();
        driver.quit();
    }

    private boolean isPartnerInTable(ChromeDriver driver, String name, String contacts, String type) {
        driver.get("http://localhost:8080/partners");
        List<WebElement> rows = driver.findElements(By.cssSelector("table.table tbody tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(0).getText().equals(name)) {
                if (contacts != null && !cells.get(1).getText().equals(contacts)) {
                    return false;
                }
                if (type != null && !cells.get(2).getText().equals(type)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Test
    void testProductDetailsPage() {
        ChromeDriver driver = new ChromeDriver();
        try {
            // Переходим на страницу товаров
            driver.get("http://localhost:8080/products");

            // Находим первую ссылку на товар и кликаем
            WebElement firstProductLink = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(
                            By.cssSelector("table.table tbody tr:first-child a")));
            String productName = firstProductLink.getText();
            firstProductLink.click();

            // Проверяем заголовок страницы
            assertEquals("Информация о товаре", driver.getTitle());

            // Проверяем основные данные товара
            WebElement nameElement = driver.findElement(By.cssSelector(".product-details h2"));
            assertEquals(productName, nameElement.getText());

            assertTrue("Ошибка отображения категории", driver.findElement(By.xpath("//p[contains(.,'Категория:')]")).isDisplayed());
            assertTrue("Ошибка отображения срока годности", driver.findElement(By.xpath("//p[contains(.,'Срок годности:')]")).isDisplayed());
            assertTrue("Ошибка отображения количества", driver.findElement(By.xpath("//p[contains(.,'Количество:')]")).isDisplayed());

            // Проверяем кнопку возврата
            WebElement backButton = driver.findElement(By.linkText("Вернуться к списку"));
            assertTrue("Ошибка отображения кнопки", backButton.isDisplayed());
            assertEquals("http://localhost:8080/products", backButton.getAttribute("href"));
        } finally {
            driver.quit();
        }
    }
}