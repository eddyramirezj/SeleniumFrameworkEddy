package etsy.com;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import subSystems.ConnectToSqlDB;
import subSystems.InputData;

import java.util.concurrent.TimeUnit;

public class WebDriver {

    String absolutePath = System.getProperty("user.dir");
    String relativePath = "/resources/WebDrivers/Windows/chromedriver.exe";
    String windowsChromeDriverPath = absolutePath + relativePath;
    String website = "https://www.etsy.com/";
    static ChromeDriver driver;
    static WebDriverWait webDriverWait;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", windowsChromeDriverPath);

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get(website);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();

    }

    @Test
    public void testSearch() {
        webDriverWait = new WebDriverWait(driver, 7);

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("global-enhancements-search-query")));

        WebElement searchInputBox = driver.findElement(By.id("global-enhancements-search-query"));

        searchInputBox.sendKeys("mattress");
        searchInputBox.sendKeys(Keys.ENTER);

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/div[4]/div[5]/div[3]/div[6]/div[2]/div[1]/span/span/span[1]"))));

        WebElement searchResults = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/div[4]/div[5]/div[3]/div[6]/div[2]/div[1]/span/span/span[1]"));

        String expectedResults = "12,792 results,";
        String actualResults = searchResults.getText();

        Assert.assertEquals(expectedResults, actualResults);
        System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);
    }

    @Test
    public void testRegister() throws InterruptedException {
        webDriverWait = new WebDriverWait(driver, 7);

        webDriverWait.until(ExpectedConditions.elementToBeClickable(driver.findElementByCssSelector("#gnav-header-inner > div.wt-flex-shrink-xs-0 > nav > ul > li:nth-child(1) > button"))).click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("#join_neu_email_field")));

        WebElement registerEmailAddress = driver.findElementByCssSelector("#join_neu_email_field");

        String testEmail = InputData.randomString(8) + "@gmail.com";
        registerEmailAddress.sendKeys(testEmail);

        WebElement continueButton = driver.findElementByCssSelector("#join-neu-form > div.wt-grid.wt-grid--block > div > div:nth-child(9) > div > button");
        continueButton.sendKeys(Keys.ENTER);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#join_neu_first_name_field")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#join_neu_password_field")));

        WebElement registerFirstName = driver.findElementByCssSelector("#join_neu_first_name_field");

        registerFirstName.sendKeys("Eddy");

        WebElement registerPassword = driver.findElementByCssSelector("#join_neu_password_field");

        String testPassword = InputData.randomString(8);
        registerPassword.sendKeys(testPassword);

        WebElement registerButton = driver.findElementByCssSelector("#join-neu-form > div:nth-child(9) > div > div:nth-child(9) > div > button");
        registerButton.sendKeys(Keys.ENTER);

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//*[@id=\"content\"]/div/section/div/div[1]/div/div/div/h1[1]")));

        WebElement loginConfirmation = driver.findElementByXPath("//*[@id=\"content\"]/div/section/div/div[1]/div/div/div/h1[1]");

        String expectedResults = "Welcome to Etsy, Eddy!";
        String actualResults = loginConfirmation.getText();

        if (expectedResults.equals(actualResults)) {
            ConnectToSqlDB connectToSqlDB = new ConnectToSqlDB();

            connectToSqlDB.insertCredentialsIntoDB("random_accounts", testEmail, "Eddy", testPassword, "Email", "First_Name", "Password");

        }

        Assert.assertEquals(expectedResults, actualResults);
        System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);



    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();

    }

}
