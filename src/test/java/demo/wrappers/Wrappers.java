package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */

     public static void scrollNClickWrapper(WebElement e, WebDriver d){
        Actions act = new Actions(d);
        act.scrollToElement(e).click(e).build().perform();

     }

     public static void testHome(WebDriver d){
        d.get("https://www.youtube.com/");
     }

     public static void nextClickWrapper(WebDriver driver, By nextButton){
        WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(5L), Duration.ofMillis(40L));
                do{
                        Wait.until(ExpectedConditions.presenceOfElementLocated(nextButton));
                        driver.findElement(nextButton).click();
                } while(driver.findElement(nextButton).isDisplayed());
     }

     public static void nextClickWrapper(WebDriver driver, By nextButton, WebElement parent){
        WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(5L), Duration.ofMillis(40L));
                do{
                        Wait.until(ExpectedConditions.visibilityOf(parent.findElement(nextButton)));
                        parent.findElement(nextButton).click();
                } while(parent.findElement(nextButton).isDisplayed());
     }

     public static void ScrollWrapper(WebDriver driver, WebElement e){
        Actions ac = new Actions(driver);
        ac.scrollToElement(e).perform();
     }

     public static void waitAndClickWrapper(By xpath, WebDriver driver){
        new WebDriverWait(driver, Duration.ofSeconds(5L)).until(
                ExpectedConditions.visibilityOfElementLocated(xpath)
        );
        driver.findElement(xpath).click();
     }

     public static void searchInputWraper(WebDriver driver, String text){
        WebElement searchElement = driver.findElement(By.xpath("//input[@id='search']"));

        Actions ac = new Actions(driver);
        // ac.moveToElement(searchElement).click().sendKeys(text).sendKeys(Keys.RETURN).build().perform();
        searchElement.sendKeys(text);
        ac.sendKeys(Keys.RETURN).perform();


     }
}
