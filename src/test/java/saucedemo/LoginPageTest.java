package saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginPageTest {
    WebDriver driver;

    String usernameId = "user-name";
    String passwordId = "password";
    String password = "secret_sauce";
    String loginButtonId = "login-button";

    @DataProvider(name = "invalid-credentials-data-provider")
    public Object[][] dpMethod(){
        return new Object[][] {
                {"username123"}, {"123username"}
        };
    }

    @BeforeTest
    public void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://www.saucedemo.com/");
    }

    @Test (priority = 0,  invocationCount = 5)
    private void checkElement(){
        Assert.assertEquals(driver.findElement(By.className("login_logo")).getText(), "Swag Labs");
        Assert.assertEquals(driver.findElement(By.id(usernameId)).getAttribute("placeholder"), "Username");
        Assert.assertEquals(driver.findElement(By.id(passwordId)).getAttribute("placeholder"), "Password");
    }

    @Test (priority = 1, timeOut = 2000)
    private void loginNullValue(){
        driver.findElement(By.id(loginButtonId)).click();
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3")).getText(),
                "Epic sadface: Username is required");
    }


    @Test (priority = 2, groups = {"Sanity"})
    private void loginLockedUser(){
        driver.findElement(By.id(usernameId)).sendKeys("locked_out_user");
        driver.findElement(By.id(passwordId)).sendKeys(password);

        driver.findElement(By.id(loginButtonId)).click();

        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3")).getText(),
                "Epic sadface: Sorry, this user has been locked out.");
    }

    @Test (priority = 3,dataProvider = "invalid-credentials-data-provider", enabled = false)
    private void loginInvalidUser(String username){
        driver.findElement(By.id(usernameId)).sendKeys(username);
        driver.findElement(By.id(passwordId)).sendKeys(password);

        driver.findElement(By.id(loginButtonId)).click();

        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"login_button_container\"]/div/form/div[3]/h3")).getText(),
                "Epic sadface: Username and password do not match any user in this service");
    }

    @Test (priority = 4)
    private void loginValidUser(){
        driver.findElement(By.id(usernameId)).sendKeys("standard_user");
        driver.findElement(By.id(passwordId)).sendKeys(password);

        driver.findElement(By.id(loginButtonId)).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/inventory.html");
    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }
}
