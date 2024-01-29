package saucedemo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PurchaseTest {

    WebDriver driver;
    String usernameId = "user-name";
    String passwordId = "password";
    //String username = "performance_glitch_user";
    String username = "standard_user";
    String password = "secret_sauce";
    String loginButtonId = "login-button";

    @BeforeTest
    public void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://www.saucedemo.com/");
        driver.findElement(By.id(usernameId)).sendKeys(username);
        driver.findElement(By.id(passwordId)).sendKeys(password);

        driver.findElement(By.id(loginButtonId)).click();
    }

    @Test (priority = 0)
    private void purchaseHappyFlow(){
        //add item
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.id("add-to-cart-sauce-labs-onesie")).click();

        //check item count icon
        Assert.assertEquals(driver.findElement(By.className("shopping_cart_badge")).getText(), "2");
        //click shopping cart
        driver.findElement(By.className("shopping_cart_link")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/cart.html");

        //verify bought item
        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div[3]/div[2]/a/div")).getText(), "Sauce Labs Backpack");
        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[1]/div[4]/div[2]/a/div")).getText(), "Sauce Labs Onesie");

        // go to checkout step-one page
        driver.findElement(By.id("checkout")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-one.html");

        //fill in shipment form
        driver.findElement(By.id("first-name")).sendKeys("test-name");
        driver.findElement(By.id("last-name")).sendKeys("test-lastname");
        driver.findElement(By.id("postal-code")).sendKeys("99999");
        driver.findElement(By.id("continue")).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-step-two.html");

        //verify bought item fee (step two)
        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[2]/div[6]")).getText(), "Item total: $37.98");
        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[2]/div[7]")).getText(), "Tax: $3.04");
        Assert.assertEquals(driver.findElement(By.xpath("/html/body/div/div/div/div[2]/div/div[2]/div[8]")).getText(), "Total: $41.02");

        //finish
        driver.findElement(By.id("finish")).click();

        //verify success message
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/h2")).getText(), "Thank you for your order!");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/div")).getText(), "Your order has been dispatched, and will arrive just as fast as the pony can get there!");
    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }
}
