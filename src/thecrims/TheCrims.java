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
        Thread.sleep(1000);
        driver.findElement(By.id("single-robbery-select")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//option[.='Klubík - 25% D: 10000 SP: 100%']")).click();
        Thread.sleep(500);
        driver.findElement(By.id("single-robbery-select")).sendKeys(Keys.ENTER);
        for (int i = 0; i < 4; i++) {
            Thread.sleep(1000);
            driver.findElement(By.id("single-robbery-submit")).click();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Marek\\Documents\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.thecrims.com");
        driver.findElement(By.xpath("//input[@placeholder='Username']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys("Pivoman");
        driver.findElement(By.xpath("//input[@placeholder='Password']")).clear();
        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys("03e43b44a3dc");
        driver.findElement(By.xpath("//button[.='Přihlaš se']")).click();
        int i=0;
        while(true){
            if(Integer.parseInt(driver.findElement(By.xpath("//span[@ng-bind='user.stamina']")).getText()) < 100){
                if(i % 2 == 0){
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//div[@id='menu-sprite-businesses']")).click();
                    Thread.sleep(1000);
                    driver.findElement(By.linkText("Pivomansland")).click();
                    Thread.sleep(1000);
                    int value = Integer.parseInt(driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).getAttribute("value"));
                    value += 190;
                    driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).clear();
                    driver.findElement(By.xpath("//td[.='Maximální respekt']/../td[2]/input")).sendKeys(String.valueOf(value));
                    Thread.sleep(1000);
                    driver.findElement(By.xpath("//input[@value='OK']")).click();
                }
                Thread.sleep(1000);
                driver.findElement(By.xpath("//div[@id='menu-nightlife']")).click();
                Thread.sleep(1000);
                driver.findElement(By.xpath("//td[.='Pivomansland']/../td[@align='right']/form/input[@value='Vstup']")).click();
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
                robbery(driver);
                i++;
            }
            else{
                robbery(driver);
            }
        }
    }
    
}
