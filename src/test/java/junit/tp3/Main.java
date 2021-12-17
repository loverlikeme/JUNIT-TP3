package junit.tp3;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.github.bonigarcia.wdm.WebDriverManager;
public class Main {
    WebDriver driver;
    JavascriptExecutor jse;

    @BeforeAll
    public static void initialissation() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void preparation(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(7));

        jse = (JavascriptExecutor) driver;
    }

    public static void takeScreenshot(WebDriver webdriver,String filePath) throws Exception{
        TakesScreenshot screenShot =((TakesScreenshot)webdriver);
        File SourceFile=screenShot.getScreenshotAs(OutputType.FILE);
        File DestinationFile=new File(filePath);
        FileUtils.copyFile(SourceFile, DestinationFile);
    }
    @Test
    public void todoTestCase() throws Exception {
        driver.get("https://todomvc.com");
        choosePlatform("Backbone.js");
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo(1);
        removeTodo(3);
        Thread.sleep(1000);
        assertLeft(1);
        takeScreenshot(driver, "D:\\GL4\\2021-2022\\Qualite\\TP\\TP3 - JUnit\\tp3.selenium\\screenshots\\firstOne.png"); ;
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Backbone.js",
            "AngularJS",
            "Dojo",
            "React"
        })
    public void todosTestCase(String platform) throws Exception {
        driver.get("https://todomvc.com");
        choosePlatform(platform);
        addTodo("Meet a Friend");
        addTodo("Buy Meat");
        addTodo("clean the car");
        removeTodo(1);
        removeTodo(2);
        assertLeft(1);
        takeScreenshot(driver, "D:\\GL4\\2021-2022\\Qualite\\TP\\TP3 - JUnit\\tp3.selenium\\screenshots\\screenshots_" + platform +".png");
    }

    private void choosePlatform(String platform) {
        WebElement platforme = driver.findElement(By.linkText(platform));
        platforme.click();
    }
    private void addTodo(String todo) throws InterruptedException {
        WebElement addTodo = driver.findElement(By.className("new-todo"));
        addTodo.sendKeys(todo);
        addTodo.sendKeys(Keys.RETURN);
        Thread.sleep(2000);
    }
    private void removeTodo(int num) throws InterruptedException {
        WebElement rmvTodo = driver.findElement(By.cssSelector("li:nth-child("+num+") .toggle"));
        rmvTodo.click();
        Thread.sleep(2000);
    }
    private void assertLeft(int expectedLeft) throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
            String expectedTest = String.format("$d item left", expectedLeft);
            validateInnerText(element,expectedTest);
        Thread.sleep(2000);
    }

    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }

    @AfterEach
    public void quitter() throws InterruptedException {
        driver.quit();
    }
}
