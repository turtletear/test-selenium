package cura;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateAppointment {
    WebDriver driver;
    String selectedFacility = "Seoul CURA Healthcare Center";
    String commentInput = "Test comment comment comment";
    String selectedProgram = "Medicaid";


    @BeforeTest
    private void init(){
        System.setProperty("webdriver.chrome.driver", "/Users/segari/Downloads/chromedriver-mac-arm64/chromedriver");
        driver = new ChromeDriver();

        driver.navigate().to("https://katalon-demo-cura.herokuapp.com/profile.php#login");
        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("btn-login")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/#appointment");
    }

    @Test (priority = 0)
    private void checkElement(){
        List<String> expectedDropdownValue = new ArrayList<>();
        expectedDropdownValue.add("Tokyo CURA Healthcare Center");
        expectedDropdownValue.add("Hongkong CURA Healthcare Center");
        expectedDropdownValue.add("Seoul CURA Healthcare Center");


        Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"appointment\"]/div/div/div/h2")).getText(), "Make Appointment");

        //check dropdown items
        Select select = new Select(driver.findElement(By.id("combo_facility")));
        List<WebElement> options = select.getOptions();
        for ( int i = 0; i < options.size(); i++){
            Assert.assertEquals(options.get(i).getAttribute("value"), expectedDropdownValue.get(i));
        }

        Assert.assertEquals(driver.findElement(By.id("txt_comment")).getAttribute("placeholder"), "Comment");
        Assert.assertEquals(driver.findElement(By.id("btn-book-appointment")).getText(), "Book Appointment");
    }

    @Test (priority = 1)
    private void makeAppointmentCorrectValue(){
        Select select = new Select(driver.findElement(By.id("combo_facility")));
        select.selectByIndex(2);

        driver.findElement(By.id("chk_hospotal_readmission")).click();
        driver.findElement(By.id("radio_program_medicaid")).click();
        driver.findElement(By.id("txt_visit_date")).sendKeys("11/01/2024");
        driver.findElement(By.id("txt_comment")).sendKeys("Test comment comment comment");
        driver.findElement(By.id("btn-book-appointment")).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/appointment.php#summary");
    }

    @Test (priority = 2, dependsOnMethods = {"makeAppointmentCorrectValue"})
    private void checkSummary(){
        Assert.assertEquals(driver.findElement(By.cssSelector("section h2")).getText(), "Appointment Confirmation");
        Assert.assertEquals(driver.findElement(By.cssSelector("section p")).getText(), "Please be informed that your appointment has been booked as following:");
        Assert.assertEquals(driver.findElement(By.id("facility")).getText(), selectedFacility);
        Assert.assertEquals(driver.findElement(By.id("hospital_readmission")).getText(), "Yes");
        Assert.assertEquals(driver.findElement(By.id("program")).getText(), selectedProgram);
        Assert.assertEquals(driver.findElement(By.id("visit_date")).getText(), "11/01/2024");
        Assert.assertEquals(driver.findElement(By.id("comment")).getText(), commentInput);

        driver.findElement(By.className("btn-default")).click();
        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/");
    }

    @Test(priority = 3, dependsOnMethods = {"checkSummary", "makeAppointmentCorrectValue"})
    private void checkHistory(){
        driver.findElement(By.id("menu-toggle")).click();
        driver.findElement(By.xpath("//*[@id=\"sidebar-wrapper\"]/ul/li[3]/a")).click();

        Assert.assertEquals(driver.getCurrentUrl(), "https://katalon-demo-cura.herokuapp.com/history.php#history");

        Assert.assertEquals(driver.findElement(By.id("facility")).getText(), selectedFacility);
        Assert.assertEquals(driver.findElement(By.id("hospital_readmission")).getText(), "Yes");
        Assert.assertEquals(driver.findElement(By.id("program")).getText(), selectedProgram);
        Assert.assertEquals(driver.findElement(By.id("comment")).getText(), commentInput);

    }

    @AfterTest
    private void closeBrowser(){
        driver.close();
    }
}
