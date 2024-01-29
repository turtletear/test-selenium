package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;

public class HomePageTest {
    WebDriver driver;

    @BeforeTest
    public void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/");
    }

    @Test (priority = 0)
    private void checkElement(){
        // check H1 element
        Assert.assertEquals(driver.findElement(By.cssSelector("header h1")).getText(), "CURA Healthcare Service");
        Assert.assertEquals(Color.fromString(driver.findElement(By.cssSelector("header h1")).getCssValue("color")).asHex(), "#ffffff");

        // check button
        Assert.assertEquals(driver.findElement(By.id("btn-make-appointment")).getText(), "Make Appointment");
        Assert.assertEquals(driver.findElement(By.id("btn-make-appointment")).getCssValue("background-color"), "rgba(115, 112, 181, 0.8)");

        // check footer
        Assert.assertEquals(driver.findElement(By.cssSelector("footer h4 strong")).getText(), "CURA Healthcare Service");
    }

    @Test (priority = 1)
    private  void clickToggleMenu(){
        driver.findElement(By.id("menu-toggle")).click();
        Assert.assertEquals(driver.findElement(By.id("sidebar-wrapper")).getAttribute("class"), "active");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[1]")).getText(), "CURA Healthcare");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[2]")).getText(), "Home");
        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]")).getText(), "Login");
        driver.findElement(By.id("menu-toggle")).click();
        Assert.assertEquals(driver.findElement(By.id("sidebar-wrapper")).getAttribute("class"), "");
    }

    @Test (priority = 2)
    private void clickButtonMakeAppointment(){
        driver.findElement(By.id("btn-make-appointment")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/profile.php#login");
    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }
}
