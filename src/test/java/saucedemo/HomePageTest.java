package saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePageTest {
    WebDriver driver;
    String usernameId = "user-name";
    String passwordId = "password";
    String password = "secret_sauce";
    String loginButtonId = "login-button";

    @BeforeTest
    public void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://www.saucedemo.com/");
        driver.findElement(By.id(usernameId)).sendKeys("standard_user");
        driver.findElement(By.id(passwordId)).sendKeys(password);

        driver.findElement(By.id(loginButtonId)).click();

    }

    @Test (priority = 0)
    private void checkElement(){
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/span")).getText(), "Products");
    }

    @Test (priority = 1)
    private void checkToggleMenuBar(){
        driver.findElement(By.id("react-burger-menu-btn")).click();
        Assert.assertEquals(driver.findElement(By.id("inventory_sidebar_link")).getAttribute("innerText"), "All Items");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"about_sidebar_link\"]")).getAttribute("innerText"), "About");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"logout_sidebar_link\"]")).getAttribute("innerText"), "Logout");
        driver.findElement(By.id("react-burger-cross-btn")).click();
    }

    @Test (priority = 2)
    private void checkShortDropdownOptions(){
        List<String> expectedDropdownValue = new ArrayList<>();
        expectedDropdownValue.add("Name (A to Z)");
        expectedDropdownValue.add("Name (Z to A)");
        expectedDropdownValue.add("Price (low to high)");
        expectedDropdownValue.add("Price (high to low)");
        Select select = new Select(driver.findElement(By.xpath("//*[@id=\"header_container\"]/div[2]/div/span/select")));
        List<WebElement> options = select.getOptions();
        for (int i = 0; i < options.size(); i++){
            Assert.assertEquals(options.get(i).getText(), expectedDropdownValue.get(i));
        }
    }

    @Test (priority = 3)
    private void checkSortingFunctionByNameAtoZ(){
        List<String> expectedOrder = new ArrayList<>();
        expectedOrder.add("Sauce Labs Backpack");
        expectedOrder.add("Sauce Labs Bike Light");
        expectedOrder.add("Sauce Labs Bolt T-Shirt");
        expectedOrder.add("Sauce Labs Fleece Jacket");
        expectedOrder.add("Sauce Labs Onesie");
        expectedOrder.add("Test.allTheThings() T-Shirt (Red)");

        List<String> actualOrder = new ArrayList<>();

        List<WebElement> productData = driver.findElements(By.className("inventory_item_name"));
        for (WebElement item : productData){
            actualOrder.add(item.getText());
        }

        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test (priority = 4)
    private void checkSortingFunctionByNameZtoA(){
        List<String> expectedOrder = new ArrayList<>();
        expectedOrder.add("Test.allTheThings() T-Shirt (Red)");
        expectedOrder.add("Sauce Labs Onesie");
        expectedOrder.add("Sauce Labs Fleece Jacket");
        expectedOrder.add("Sauce Labs Bolt T-Shirt");
        expectedOrder.add("Sauce Labs Bike Light");
        expectedOrder.add("Sauce Labs Backpack");

        List<String> actualOrder = new ArrayList<>();

        Select select= new Select(driver.findElement(By.className("product_sort_container")));
        select.selectByIndex(1);

        List<WebElement> productData = driver.findElements(By.className("inventory_item_name"));
        for (WebElement item : productData){
            actualOrder.add(item.getText());
        }

        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test (priority = 5)
    private void checkSortingFunctionByPriceAscending(){
        List<String> expectedOrder = new ArrayList<>();
        expectedOrder.add("$7.99");
        expectedOrder.add("$9.99");
        expectedOrder.add("$15.99");
        expectedOrder.add("$15.99");
        expectedOrder.add("$29.99");
        expectedOrder.add("$49.99");

        List<String> actualOrder = new ArrayList<>();

        Select select= new Select(driver.findElement(By.className("product_sort_container")));
        select.selectByIndex(2);

        List<WebElement> productData = driver.findElements(By.className("inventory_item_price"));

        for (WebElement item : productData){
            actualOrder.add(item.getText());
        }

        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test (priority = 6)
    private void checkSortingFunctionByPriceDescending(){
        List<String> expectedOrder = new ArrayList<>();
        expectedOrder.add("$49.99");
        expectedOrder.add("$29.99");
        expectedOrder.add("$15.99");
        expectedOrder.add("$15.99");
        expectedOrder.add("$9.99");
        expectedOrder.add("$7.99");

        List<String> actualOrder = new ArrayList<>();

        Select select= new Select(driver.findElement(By.className("product_sort_container")));
        select.selectByIndex(3);

        List<WebElement> productData = driver.findElements(By.className("inventory_item_price"));

        for (WebElement item : productData){
            actualOrder.add(item.getText());
        }

        Assert.assertEquals(expectedOrder, actualOrder);
    }

    @Test (priority = 7)
    private void checkLogout(){
        driver.findElement(By.id("react-burger-menu-btn")).click();
        new WebDriverWait(driver, Duration.ofMillis(500)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"logout_sidebar_link\"]"))).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }

}
