import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Helper
{
    /**
     * Get a funny quote of the http://www.litquotes.com/random-funny-quote.php
     * The length has to be less than the limit
     *
     * @param limit
     * @return
     */
    public static String getFunnyQuote(int limit)
    {
        String quote = "";

        // using HtmlUnitDriver instead of WebDriver
        HtmlUnitDriver hud = new HtmlUnitDriver(BrowserVersion.CHROME);
        Wait<WebDriver> pause = new WebDriverWait(hud, 30);
        hud.get("http://www.litquotes.com/random-funny-quote.php");

        By wanted = By.xpath("//div[@class='purple']//p//b");

        // we try to get a quote under the Twitter's limit
        while (quote.length() == 0 || quote.length() > limit)
        {
            hud.navigate().refresh();
            pause.until(ExpectedConditions.visibilityOfElementLocated(wanted));

            quote = hud.findElement(wanted).getText().replace("\"", "").trim();
        }

        return quote;
    }
}
