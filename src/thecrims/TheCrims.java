package thecrims;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TheCrims {
    
    public static void waitForElement(WebDriver driver, WebElement element, int sleepTime) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, sleepTime);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    public static void waitForElement(WebDriver driver, By by, int sleepTime) throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, sleepTime);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }
    
    public static void detoxicate(WebDriver driver) throws InterruptedException{
        if(driver.findElement(By.xpath("//div[@id='user-profile-addiction']/span")).getText().equals("7")){
            Thread.sleep(1500);
            driver.findElement(By.id("menu-sprite-hospital")).click();
            waitForElement(driver, By.xpath("//input[@value='Detoxikuj']"), 5);
            driver.findElement(By.xpath("//input[@value='Detoxikuj']")).click();
            Thread.sleep(2000);
        }
    }
    
    public static void robbery(WebDriver driver, Properties prop) throws InterruptedException{
        driver.findElement(By.xpath("//span[.='Zlodějina']")).click();
        waitForElement(driver, By.id("single-robbery-select"), 5);
        driver.findElement(By.id("single-robbery-select")).click();
        waitForElement(driver, By.xpath("//option[.='" + prop.getProperty("stole") + "']"), 5);
        driver.findElement(By.xpath("//option[.='" + prop.getProperty("stole") + "']")).click();
        for (int i = 0; i < 4; i++) {
            waitForElement(driver, By.id("single-robbery-submit"), 5);
            driver.findElement(By.id("single-robbery-submit")).click();
        }
        detoxicate(driver);
    }
    
    public static void increaseClubRespect(WebDriver driver, Properties prop, int value) throws InterruptedException{
        waitForElement(driver, By.xpath("//div[@id='menu-sprite-businesses']"), 5);
        driver.findElement(By.xpath("//div[@id='menu-sprite-businesses']")).click();
        waitForElement(driver, By.linkText(prop.getProperty("club")), 5);
        driver.findElement(By.linkText(prop.getProperty("club"))).click();
        waitForElement(driver, By.xpath("//td[.='Maximální respekt']/../td[2]/input"), 5);
        int actual = Integer.parseInt(driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).getAttribute("value"));
        actual += value;
        driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).clear();
        driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).sendKeys(String.valueOf(actual));
        waitForElement(driver, By.xpath("//input[@value='OK']"), 5);
        driver.findElement(By.xpath("//input[@value='OK']")).click();
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, IOException {
        Properties prop = new Properties();
	InputStream input = new FileInputStream("config.properties");
        prop.load(input);
        
        System.setProperty("webdriver.chrome.driver", prop.getProperty("chromeDriverPath"));
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.thecrims.com");
        driver.findElement(By.xpath("//input[@placeholder='Username']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(prop.getProperty("username"));
        driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(prop.getProperty("password"));
        driver.findElement(By.xpath("//button[.='Přihlaš se']")).click();
        int i=0;
        while(true){
            if(Integer.parseInt(driver.findElement(By.xpath("//span[@ng-bind='user.stamina']")).getText()) < 100){
                Thread.sleep(1500);
                detoxicate(driver);
                if(i % 2 == 0){
                    Thread.sleep(1000);
                    increaseClubRespect(driver, prop, 190);
                }
                Thread.sleep(1000);
                driver.findElement(By.xpath("//div[@id='menu-nightlife']")).click();
                Thread.sleep(1000);
                try{
                    driver.findElement(By.xpath("//td[.='" + prop.getProperty("club") + "']/../td[@align='right']/form/input[@value='Vstup']")).click();
                }catch(org.openqa.selenium.NoSuchElementException e){
                    increaseClubRespect(driver, prop, 190);
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//div[@id='menu-nightlife']")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//td[.='" + prop.getProperty("club") + "']/../td[@align='right']/form/input[@value='Vstup']")).click();
                }
                Thread.sleep(1000);
                String kolikKopeString = driver.findElement(By.xpath("//td[.='Hašiš']/../td[2][@class='ng-binding']")).getText();
                int kolikkolik = kolikKopeString.length();
                kolikKopeString = kolikKopeString.substring(0, kolikkolik-1);
                float kolikKope = Float.parseFloat(kolikKopeString);
                int doplnitVydrz = 100-(Integer.parseInt(driver.findElement(By.xpath("//span[@ng-bind='user.stamina']")).getText()));
                int koupit = (int) ((doplnitVydrz/kolikKope)+1);
                Thread.sleep(1000);
                driver.findElement(By.xpath("//td[.='Hašiš']/../td[4]/form/input[@name='quantity']")).sendKeys(String.valueOf(koupit));
                Thread.sleep(200);
                driver.findElement(By.xpath("//td[.='Hašiš']/../td[4]/form/input[@type='submit']")).click();
                Thread.sleep(500);
                robbery(driver, prop);
                i++;
            }
            else{
                robbery(driver, prop);
            }
        }
    }
    
}
