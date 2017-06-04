package patterns.actionbot;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thanks to http://www.programcreek.com/java-api-examples/ for the beginning
 */
public abstract class BaseAction
{
    private static final Logger LOGGER = Logger.getLogger(BaseAction.class.getName());
    protected WebDriver driver;
    protected Wait<WebDriver> wait;
    private WebElement baseWebElement;
    private long delay = Long.valueOf(System.getProperty("DELAY", "10")).longValue();
    private long timeout = Long.valueOf(System.getProperty("TIMEOUT", "10")).longValue();
    private By locator;
    private List<WebElement> baseWebElements;

    private void setLocator(By theLocator)
    {
        locator = theLocator;
    }

    /**
     * This method wraps the findElement method and returns a WebElement using
     * locator parameter. By default it will attempt to wait for an element to
     * be clickable and stationary before returning it. If this fails then after
     * 10 seconds it will simply match the element if possible and return it.
     */
    protected WebElement findControl(By theLocator)
    {
        try
        {
            setLocator(theLocator);
            waitForElementThenInitialise();

            return baseWebElement;
        }
        catch (NoSuchElementException e)
        {
            LOGGER.log(Level.SEVERE, "NoSuchElementException in findControl() method for: " + locator.toString(), e);
            throw new NoSuchElementException(locator.toString());
        }
    }

    protected List<WebElement> findControls(By theLocator)
    {
        try
        {
            setLocator(theLocator);
            waitForElementsThenInitialise();

            return baseWebElements;
        }
        catch (NoSuchElementException e)
        {
            LOGGER.log(Level.SEVERE, "NoSuchElementException in findControl() method for: " + locator.toString(), e);
            throw new NoSuchElementException(locator.toString());
        }
    }

    private void waitForElementThenInitialise()
    {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try
        {
            baseWebElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            waitForControlToStopMoving();
        }
        catch (InterruptedException e)
        {
            LOGGER.log(Level.INFO, "InterruptedException in findControl() method", e);
        }
        catch (TimeoutException e)
        {
            LOGGER.log(Level.INFO, "TimeoutException in findControl() method", e);
        }

        if (baseWebElement == null)
        {
            baseWebElement = driver.findElement(locator);
        }
    }

    private void waitForElementsThenInitialise()
    {
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try
        {
            baseWebElement = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            waitForControlToStopMoving();
        }
        catch (InterruptedException e)
        {
            LOGGER.log(Level.INFO, "InterruptedException in findControl() method", e);
        }
        catch (TimeoutException e)
        {
            LOGGER.log(Level.INFO, "TimeoutException in findControl() method", e);
        }

        baseWebElements = driver.findElements(locator);
    }

    private void waitForControlToStopMoving() throws InterruptedException
    {
        Point elementStartLocation = baseWebElement.getLocation();
        Thread.sleep(delay);
        Point elementCurrentLocation = baseWebElement.getLocation();

        while (!elementCurrentLocation.equals(elementStartLocation))
        {
            elementStartLocation = elementCurrentLocation;
            Thread.sleep(delay);
            elementCurrentLocation = baseWebElement.getLocation();
        }
    }
}