package etsy.com;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import subSystems.ConnectToSqlDB;
import subSystems.InputData;

import java.time.Duration;
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
        webDriverWait = new WebDriverWait(driver, 5);

        try {

            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("global-enhancements-search-query")));

            WebElement searchInputBox = driver.findElement(By.id("global-enhancements-search-query"));

            searchInputBox.sendKeys("mattress");
            searchInputBox.sendKeys(Keys.ENTER);

            webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(text(), 'results,')] "))));

            WebElement searchResults = driver.findElement(By.xpath("//span[contains(text(), 'results,')] "));

            String expectedResults = "12,798 results,";
            String actualResults = searchResults.getText();

            Assert.assertEquals(expectedResults, actualResults);
            System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        }

    @Test
    public void testRegister() throws InterruptedException {
        webDriverWait = new WebDriverWait(driver, 5);

        webDriverWait.until(ExpectedConditions.elementToBeClickable(driver.findElementByCssSelector("button.wt-btn.wt-btn--small.wt-btn--transparent.wt-mr-xs-1"))).click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("#join_neu_email_field")));

        WebElement registerEmailAddress = driver.findElementByCssSelector("#join_neu_email_field");

        String testEmail = InputData.randomString(8) + "@gmail.com";
        registerEmailAddress.sendKeys(testEmail);

        WebElement continueButton = driver.findElementByCssSelector("button.wt-btn.wt-btn--primary.wt-width-full");
        continueButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#join_neu_first_name_field")));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#join_neu_password_field")));

        WebElement registerFirstName = driver.findElementByCssSelector("#join_neu_first_name_field");

        registerFirstName.sendKeys("Eddy");

        WebElement registerPassword = driver.findElementByCssSelector("#join_neu_password_field");

        String testPassword = InputData.randomString(8);
        registerPassword.sendKeys(testPassword);

        WebElement registerButton = driver.findElementByCssSelector("button.wt-btn.wt-btn--primary.wt-width-full");
        registerButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("h1.welcome-message-text.wt-text-display-01")));

        WebElement loginConfirmation = driver.findElementByCssSelector("h1.welcome-message-text.wt-text-display-01");

        String expectedResults = "Welcome to Etsy, Eddy!";
        String actualResults = loginConfirmation.getText();

        if (expectedResults.equals(actualResults)) {
            ConnectToSqlDB connectToSqlDB = new ConnectToSqlDB();

            connectToSqlDB.insertCredentialsIntoDB("random_accounts", testEmail, "Eddy", testPassword, "Email", "First_Name", "Password");

        }

        Assert.assertEquals(expectedResults, actualResults);
        System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);

    }

    @Test
    public void testLogin() {
        webDriverWait = new WebDriverWait(driver, 7);

            webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.wt-btn.wt-btn--small.wt-btn--transparent.wt-mr-xs-1"))).click();

            webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementById("join_neu_email_field")));

            WebElement emailAddress = driver.findElementById("join_neu_email_field");
            emailAddress.sendKeys("#wuXLIPq@gmail.com");

            WebElement continueButton = driver.findElementByCssSelector("button.wt-btn.wt-btn--primary.wt-width-full");
            continueButton.click();

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("join_neu_password_field")));

            WebElement password = driver.findElementById("join_neu_password_field");
            password.sendKeys("ag5olHjt");

            WebElement checkBox = driver.findElementByXPath("//input[@id='persisent']");
            if (checkBox.isSelected()) {
                driver.findElementByXPath("//label[@for='persisent']").click();
            }

            WebElement signIn = driver.findElement(By.cssSelector("button[class='wt-btn wt-btn--primary wt-width-full'][value='sign-in']"));
            signIn.click();

            webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("h1.welcome-message-text.wt-text-display-01")));

            WebElement loginConfirmation = driver.findElementByCssSelector("h1.welcome-message-text.wt-text-display-01");

            String expectedResults = "Welcome to Etsy, Eddy!";
            String actualResults = loginConfirmation.getText();

            Assert.assertEquals(expectedResults, actualResults);
            System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);


    }

    @Test
    public void testAddToCart() {

            Wait<ChromeDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(StaleElementReferenceException.class);

//            webDriverWait = new WebDriverWait(driver, 5);
            Actions action = new Actions(driver);

            fluentWait.until(ExpectedConditions.elementToBeClickable(By.id("catnav-primary-link--10")));
            WebElement giftCardsMenu = driver.findElementById("catnav-primary-link--10");

            action.moveToElement(giftCardsMenu).build().perform();

            WebElement shopGiftCards = driver.findElementById("catnav-l4--1234");
            shopGiftCards.click();

            fluentWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='radio_giftcards_editor_amount_USD_10000'][class='wt-btn wt-action-group__item']")));

            WebElement amount100 = driver.findElementByCssSelector("label[for='radio_giftcards_editor_amount_USD_10000'][class='wt-btn wt-action-group__item']");

            if (!amount100.isSelected()) {
                amount100.click();
            }

            WebElement emailToYouRadioButton = driver.findElementByCssSelector("label[for='medium-print']");

            if (!emailToYouRadioButton.isSelected()) {
                emailToYouRadioButton.click();

            }

            WebElement recipientsName = driver.findElementById("recipient_name");
            recipientsName.sendKeys("Sami");

            WebElement giftMessage = driver.findElementById("message");
            giftMessage.sendKeys("This is a test message.");

            WebElement sendersName = driver.findElementById("sender_name");
            sendersName.sendKeys("Eddy");

            WebElement addToCart = driver.findElementByCssSelector("button.wt-btn.wt-btn--primary.wt-width-full");
            addToCart.click();

            fluentWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("div.wt-display-flex-xs.wt-justify-content-space-between.wt-align-items-center>div>h1.wt-text-heading-01")));

            String expectedResults = "1 item in your cart";
            String actualResults = driver.findElementByCssSelector("div.wt-display-flex-xs.wt-justify-content-space-between.wt-align-items-center>div>h1.wt-text-heading-01").getText();

            Assert.assertEquals(expectedResults, actualResults);
            System.out.println("The expected Results were: " + expectedResults + "\n" + "...and the actual Results were: " + actualResults);

        }

    @Test
    public void testLogOut() {

        webDriverWait = new WebDriverWait(driver, 7);
        Actions action = new Actions(driver);

        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.wt-btn.wt-btn--small.wt-btn--transparent.wt-mr-xs-1"))).click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementById("join_neu_email_field")));

        WebElement emailAddress = driver.findElementById("join_neu_email_field");
        emailAddress.sendKeys("#wuXLIPq@gmail.com");

        WebElement continueButton = driver.findElementByCssSelector("button.wt-btn.wt-btn--primary.wt-width-full");
        continueButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("join_neu_password_field")));

        WebElement password = driver.findElementById("join_neu_password_field");
        password.sendKeys("ag5olHjt");

        WebElement checkBox = driver.findElementByXPath("//input[@id='persisent']");
        if (checkBox.isSelected()) {
            driver.findElementByXPath("//label[@for='persisent']").click();
        }

        WebElement signIn = driver.findElement(By.cssSelector("button[class='wt-btn wt-btn--primary wt-width-full'][value='sign-in']"));
        signIn.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByXPath("//button[@type='button' and @aria-describedby='ge-tooltip-label-you-menu']")));
        WebElement accountDropDownMenu = driver.findElementByXPath("//button[@type='button' and @aria-describedby='ge-tooltip-label-you-menu']");

        accountDropDownMenu.click();

        WebElement signOutButton = driver.findElementByXPath("//p[contains(text(), 'Sign out')]");
        signOutButton.click();

        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElementByCssSelector("button.wt-btn.wt-btn--small.wt-btn--transparent.wt-mr-xs-1")));

        WebElement signInButton = driver.findElementByCssSelector("button.wt-btn.wt-btn--small.wt-btn--transparent.wt-mr-xs-1");

        String expectedResults = "Sign in";
        String actualResults = signInButton.getText();

        Assert.assertEquals("The 'Sign in' button should be displayed... ", expectedResults, actualResults);

        if (expectedResults.equals(actualResults)) {
            System.out.println("User has been successfully logged out");
        }


    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();

    }

}
