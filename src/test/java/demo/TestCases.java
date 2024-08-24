package demo;

import org.checkerframework.checker.units.qual.t;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*","--headless");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
        }

        static final boolean flag = true;

        @Test (enabled = flag)
        public void testCase01() throws InterruptedException{
                Wrappers.testHome(driver);
                Assert.assertTrue(driver.getCurrentUrl().contains("youtube.com"),"We didn't reach youtube");
                
                WebElement about = driver.findElement(By.xpath("//a[text()='About']"));
                Wrappers.scrollNClickWrapper(about,driver);
                
                WebElement contentElement = driver.findElement(By.className("ytabout__content"));
                String text = contentElement.getText();
                System.out.println(text);
                Thread.sleep(1000);
        }

        @Test (enabled = flag)
        public void testCase02() throws InterruptedException {
                Wrappers.testHome(driver);
                // Thread.sleep(3000);
                // WebElement filmsElement = driver.findElement(By.xpath("//a[@title = 'Movies' or @title='Films']"));
                // Wrappers.scrollNClickWrapper(filmsElement, driver);
                By films = By.xpath("//a[@title = 'Movies' or @title='Films']");
                Wrappers.waitAndClickWrapper(films, driver);
                
                By nextButton = By.xpath("//ytd-browse[@role='main']//div[@id='right-arrow']//div[@class='yt-spec-touch-feedback-shape__fill']");
                
                Wrappers.nextClickWrapper(driver, nextButton);

                WebElement lastCard = driver.findElement(By.xpath("//*[@class='style-scope yt-horizontal-list-renderer'][parent::*[@id='items']][last()]"));
                WebElement maturity = lastCard.findElements(By.xpath(".//p")).get(1);
                System.out.println(maturity.getText());
                SoftAssert SA = new SoftAssert();
                SA.assertTrue(maturity.getText().equalsIgnoreCase("U"));

                WebElement genre = lastCard.findElement(By.xpath(".//a/h3/following-sibling::span"));
                System.out.println(genre.getText());

                SA.assertTrue(genre.isDisplayed());

                Thread.sleep(1000);
                SA.assertAll();

        }
        
        @Test (enabled = flag)
        public void testCase03() throws InterruptedException{
                Wrappers.testHome(driver);

                // WebElement musicElement = driver.findElement(By.xpath("//a[@title = 'Music']"));
                // Wrappers.scrollNClickWrapper(musicElement, driver);
                By music = By.xpath("//a[@title = 'Music']");
                Wrappers.waitAndClickWrapper(music, driver);

                WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(5L), Duration.ofMillis(40L));
                Wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='scroll-outer-container']")));

                WebElement firstCarousel = driver.findElements(By.xpath("//*[@id='scroll-outer-container']")).get(0);
                // WebElement nextElement = firstCarousel.findElement(By.xpath("./following-sibling::*[@id='right-arrow']/.//button"));
                By nextElement = By.xpath("./following-sibling::*[@id='right-arrow']/.//button");
                Wrappers.nextClickWrapper(driver, nextElement, firstCarousel);

                List<WebElement> Card = firstCarousel.findElements(By.xpath(".//div[@class='flex-container style-scope ytd-compact-station-renderer']"));
                WebElement lastCard = Card.get(Card.size()-1);

                System.out.println("title: " + lastCard.findElement(By.xpath(".//h3")).getText());

                SoftAssert SA = new SoftAssert();

                String trackNum = lastCard.findElement(By.xpath(".//p[@id='video-count-text']")).getText().substring(0,2);
                // System.out.println(trackNum);
                int track = Integer.parseInt(trackNum);

                System.out.println(track);

                SA.assertTrue(track<=50);

                Thread.sleep(1000);
                SA.assertAll();
        }

        @Test (enabled = flag)
        public void testCase04() throws InterruptedException{

                Wrappers.testHome(driver);

                // WebElement newsElement = driver.findElement(By.xpath("//a[@title = 'News']"));
                // Wrappers.scrollNClickWrapper(newsElement, driver);
                By news = By.xpath("//a[@title = 'News']");
                Wrappers.waitAndClickWrapper(news, driver);

                new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='dismissible'][@class='style-scope ytd-rich-grid-media']"))
                );

                List<WebElement> newsCards = driver.findElements(By.xpath("//span[normalize-space()='Latest news posts']//ancestor::div[@id='dismissible']/.//div[@class='style-scope ytd-rich-item-renderer']"));
                Wrappers.ScrollWrapper(driver, newsCards.get(0));

                //span[normalize-space()='Latest news posts']//ancestor::div[@id='dismissible']/.//div[@class='style-scope ytd-rich-item-renderer']//*[@id='home-content-text']/span

                int totalLikeCount = 0;
                for(int i = 0; i < 3 ; i++){
                        WebElement newsCard = newsCards.get(i);
                        String title = newsCard.findElement(By.xpath(".//*[@id='home-content-text']")).getText();
                        System.out.println("title: " + title);
                        String likeString = newsCard.findElement(By.id("vote-count-middle")).getText();
                        int likeInt = Integer.parseInt(likeString);
                        totalLikeCount += likeInt;
                }

                System.out.println("Total Likes: " + totalLikeCount);

                Thread.sleep(1000);
        }

        @Test(enabled = true, dataProvider = "excelData")
        public void testCase05(String search) throws InterruptedException{

                Wrappers.testHome(driver);

                Wrappers.searchInputWraper(driver, search);
                Thread.sleep(4000);

        }

        @AfterTest
        public void endTest() {
                // driver.close();
                driver.quit();

        }
}