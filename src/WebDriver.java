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

        String expectedResults = "12,784 results,";
        String actualResults = searchResults.getText();

        Assert.assertEquals(expectedResults, actualResults);

    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();

    }

}
