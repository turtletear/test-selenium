package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginPageTest {
    WebDriver driver;

    @BeforeTest
    public void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/profile.php#login");
    }

    @Test (priority = 0)
    private void checkElement(){
        Assert.assertEquals(driver.findElement(By.cssSelector("section h2")).getText(), "Login");
        Assert.assertEquals(driver.findElement(By.cssSelector("section p")).getText(), "Please login to make appointment.");

        Assert.assertEquals(driver.findElement(By.cssSelector("#txt-username")).getAttribute("placeholder"), "Username");
        Assert.assertEquals(driver.findElement(By.id("txt-password")).getAttribute("placeholder"), "Password");
        Assert.assertEquals(driver.findElement(By.id("btn-login")).getText(), "Login");
    }

    @Test (priority = 1)
    private void loginNullValue(){
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.className("text-danger")).getText(),
                "Login failed! Please ensure the username and password are valid.");
    }

    @Test (priority = 2)
    private void loginUnregisteredUser(){
        driver.findElement(By.id("txt-username")).sendKeys("invalidUsername");
        driver.findElement(By.id("txt-password")).sendKeys("invalidPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.findElement(By.className("text-danger")).getText(),
                "Login failed! Please ensure the username and password are valid.");
    }

    @Test (priority = 3)
    private void loginRegisteredUser(){
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }
}
