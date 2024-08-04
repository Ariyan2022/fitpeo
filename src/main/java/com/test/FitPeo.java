package com.test;
import static org.testng.Assert.assertEquals;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.PointerInput.Origin;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FitPeo {
	
	WebDriver driver = null;
	WebDriverWait wait;
    public WebDriverWait getWait() {
        return wait;
    }

    public void sleep(int timeinsec){
        try{
            Thread.sleep(timeinsec*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void scrollPage( int horizontal,int vertical) {
    	String passObject = "window.scroll("+ horizontal  + "," + vertical + ")";
    	((JavascriptExecutor)driver).executeScript(passObject);
    }
    
    public void moveToElement(WebElement element) {
    	Actions act =  new Actions(driver);
    	act.moveToElement(element).perform();
    	sleep(1);
    }
    
    public void dragAndDropElement(WebElement element,int fromPos, int toPos) {
    	Actions act =  new Actions(driver);
    	act.clickAndHold(element).moveByOffset(fromPos, toPos).release().perform();
    }
    
    public static double calculatePercentage(int value, int minValue, int maxValue) {
        return ((double) (value - minValue) / (maxValue - minValue)) * 100;
    }
    

	public void setSlider(WebElement slider, int pos) {
        // Sleep for UI stability if needed
        sleep(3);
        // Move to the slider element
        moveToElement(slider);
        dragAndDropElement(slider, -30, 0);
        
        // Drag and drop to the calculated target position
        dragAndDropElement(slider, pos, 0);
    }

	public void clickCheckBox(WebElement element) {
		if(!element.getAttribute("class").contains("Mui-checked")) {
			moveToElement(element);
			element.click();
			sleep(2);
		}
	}
    @Test
    public void verifyRecurringReimbursement() {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://www.fitpeo.com/");
        driver.manage().window().maximize();
        System.out.println("Page title "+ driver.getTitle());
        
        //click on Revenue calculator
        WebElement R_Cal = driver.findElement(By.xpath("//a[@href='/revenue-calculator']"));
        R_Cal.click();
        sleep(5);
        WebElement slider = driver.findElement(By.xpath("//span[contains(@class,'thumbSizeMedium MuiSlider-thumbColorPrimary MuiSlider')]"));
        setSlider(slider, 124);
        sleep(5);
        WebElement valueField = driver.findElement(By.xpath("//input[@type='number']"));
        moveToElement(valueField);
        valueField.clear();
        valueField.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        valueField.sendKeys("560");
        sleep(5);
        valueField.clear();
        valueField.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
        valueField.sendKeys("820");
        assertEquals(true, slider.getAttribute("style").contains("41"));
        WebElement CPT99091 = driver.findElement(By.xpath("//p[text()='CPT-99091']/following-sibling::label/span"));
        WebElement CPT99453 = driver.findElement(By.xpath("//p[text()='CPT-99453']/following-sibling::label/span"));
        WebElement CPT99454 = driver.findElement(By.xpath("//p[text()='CPT-99454']/following-sibling::label/span"));
        WebElement CPT99474 = driver.findElement(By.xpath("//p[text()='CPT-99474']/following-sibling::label/span"));
        clickCheckBox(CPT99091);
        clickCheckBox(CPT99453);
        clickCheckBox(CPT99454);
        clickCheckBox(CPT99474);
        WebElement verify = driver.findElement(By.xpath("//p[contains(text(),'Total Recurring Reimbursement for all Patients Per Month:')]"));
        assertEquals(true, verify.getText().contains("$110700"));
        System.out.println("Verified Total Recurring Reimbursement having value $110700 as " + verify.getText().contains("$110700"));
    }

}
