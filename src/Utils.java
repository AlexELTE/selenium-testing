import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

public class Utils
{
    public static void initChromeDriver()
    {
        // get the absolute path of the Chrome Driver
        File chrome = new File("chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", chrome.getAbsolutePath());
    }

    public static WebDriver initDriver()
    {
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    public static Wait<WebDriver> initWait(WebDriver driver, int secondsTimeOut)
    {
        Wait<WebDriver> wait = new WebDriverWait(driver, secondsTimeOut);
        return wait;
    }
}
