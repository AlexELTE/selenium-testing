package patterns.actionbot;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

import java.util.List;
import java.util.logging.Logger;

/**
 * Thanks to http://www.programcreek.com/java-api-examples/ for the beginning
 */
public class ActionBot extends BaseAction
{
    private static final Logger LOGGER = Logger.getLogger(ActionBot.class.getName());

    public ActionBot(WebDriver driver, Wait<WebDriver> wait)
    {
        super.driver = driver;
        super.wait = wait;
    }

    /**
     * Clicks the element
     *
     * @param locator
     */
    public void click(By locator)
    {
        findControl(locator).click();
    }

    /**
     * Submits an element
     *
     * @param locator
     */
    public void submit(By locator)
    {
        findControl(locator).submit();
    }

    /**
     * Reads the text value of an element
     *
     * @param locator
     */
    public String readText(By locator)
    {
        return findControl(locator).getText();
    }

    /**
     * Reads the specified attribute of an element
     *
     * @param locator
     * @param attribute
     */
    public String readAttribute(By locator, String attribute)
    {
        return findControl(locator).getAttribute(attribute);
    }

    /**
     * Type something into an input field. WebDriver doesn't normally clear
     * these before typing, so this method does that first. It also sends a
     * return key to move the focus out of the element
     *
     * @param locator
     */
    public void type(By locator, String text)
    {
        WebElement element = findControl(locator);
        element.sendKeys(text);
    }

    public void fill(By xpath, String fullpath)
    {
        driver.findElement(xpath).sendKeys(fullpath);
    }

    public WebElement getElement(By locator)
    {
        return findControl(locator);
    }

    public List<WebElement> getElements(By locator)
    {
        return findControls(locator);
    }

    public void refreshPage()
    {
        driver.navigate().refresh();
    }

    public int countElements(By locator)
    {
        return driver.findElements(locator).size();
    }

    public void waitElementToBeVisible(By locator)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    }

    public void waitElementToBeInVisible(By locator)
    {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitTextToBeEmpty(By locator)
    {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, ""));
    }

    public void waitUrlToBe(String url)
    {
        wait.until(ExpectedConditions.urlToBe(url));
    }

    public void navigate(String url)
    {
        driver.get(url);
    }

    public String getTitle()
    {
        return driver.getTitle();
    }
}
