package thecrims;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TheCrims {
    
    public static void waitForElement(WebElement element, int sleepTime) throws InterruptedException{
        for (int i = 0; i < sleepTime*5; i++) {
            try{
                element.isDisplayed();
            }catch(org.openqa.selenium.InvalidSelectorException e){
                Thread.sleep(200);
            }
        }
    }
    
    public static void robbery(WebDriver driver) throws InterruptedException{
        driver.findElement(By.xpath("//span[.='Zlodějina']")).click();
        driver.findElement(By.id("single-robbery-select")).click();
        Thread.sleep(500);
        //driver.findElement(By.xpath(".='Místní parchanti - 25% D: 6000 SP: 100%'")).click();
        for (int i = 0; i < 12; i++) {
            driver.findElement(By.id("single-robbery-select")).sendKeys(Keys.ARROW_DOWN);
        }
        driver.findElement(By.id("single-robbery-select")).sendKeys(Keys.ENTER);
        for (int i = 0; i < 4; i++) {
            //waitForElement(driver.findElement(By.id("single-robbery-submit")), 2);
            Thread.sleep(1500);
            driver.findElement(By.id("single-robbery-submit")).click();
        }
        
        
                
    }

    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.thecrims.com");
        driver.findElement(By.xpath("//input[@placeholder='Username']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Pivoman");
        driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("03e43b44a3dc");
        driver.findElement(By.xpath("//button[.='Přihlaš se']")).click();
        while(true){
            if(Integer.parseInt(driver.findElement(By.xpath("//span[@ng-bind='user.stamina']")).getText()) < 100){
                driver.findElement(By.xpath("//div[@id='menu-nightlife']")).click();
                driver.findElement(By.xpath("//td[.='Pivomansland']/../td[@align='right']/form/input[@value='Vstup']")).click();
                waitForElement(driver.findElement(By.xpath("//td[.='Hašiš']/../td[2][@class='ng-binding']")), 5);
                String kolikKopeString = driver.findElement(By.xpath("//td[.='Hašiš']/../td[2][@class='ng-binding']")).getText();
                kolikKopeString = kolikKopeString.substring(0,4);
                float kolikKope = Float.parseFloat(kolikKopeString);
                int doplnitVydrz = 100-(Integer.parseInt(driver.findElement(By.xpath("//span[@ng-bind='user.stamina']")).getText()));
                int koupit = (int) (doplnitVydrz/kolikKope);
                driver.findElement(By.xpath("//td[.='Hašiš']/../td[4]/form/input[@name='quantity']")).sendKeys(String.valueOf(koupit));
                driver.findElement(By.xpath("//td[.='Hašiš']/../td[4]/form/input[@type='submit']")).click();
                robbery(driver);
            }
            else{
                robbery(driver);
            }
        }
    }
    
}
