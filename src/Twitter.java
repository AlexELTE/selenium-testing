import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import patterns.actionbot.ActionBot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Twitter
{
    private ActionBot action;
    private WebDriver driver;
    private Wait<WebDriver> wait;
    private String url = "https://twitter.com/";
    private final int maxTweetLength = 140;
    private boolean isLogged = false;

    public Twitter()
    {
        Utils.initChromeDriver();
        driver = Utils.initDriver();
        wait = Utils.initWait(driver, 30);

        // Action Bot
        action = new ActionBot(driver, wait);
        action.navigate(url);
    }

    public void login(String email, String password)
    {
        action.type(By.id("signin-email"), email);
        action.type(By.id("signin-password"), password);
        action.click(By.xpath("//button[@type='submit' and contains(@class, 'js-submit')]"));

        isLogged = true;
    }

    public void logout()
    {
        action.click(By.id("user-dropdown-toggle"));
        action.click(By.id("signout-button"));

        isLogged = false;
    }

    public int countTweets()
    {
        assertTrue(isLogged);

        action.refreshPage();

        return Integer.parseInt(action.readText(By.xpath("//span[@class='ProfileCardStats-statValue']")));
    }

    public void deleteAllTweets()
    {
        assertTrue(isLogged);

        while (action.countElements(By.xpath("//button[@type='button' and contains(@class, 'ProfileTweet-actionButton')]")) > 0) {
            deleteLastTweet();
        }
    }

    public void deleteLastTweet()
    {
        assertTrue(isLogged);

        action.click(By.xpath("//button[@type='button' and contains(@class, 'ProfileTweet-actionButton')]"));
        action.click(By.xpath("//li[@class='js-actionDelete']"));
        action.click(By.xpath("//button[contains(@class, 'delete-action')]"));

        // we wait for the alert-message
        action.waitElementToBeVisible(By.className("alert-messages"));
        action.waitElementToBeInVisible(By.className("alert-messages"));
    }

    public void postTweets(int howMany)
    {
        assertTrue(isLogged);

        for (int i = 0; i < howMany; ++i) {
            postTweet(Helper.getFunnyQuote(140));
        }
    }

    public void postTweet(String tweet)
    {
        assertTrue(isLogged);
        assertTrue(maxTweetLength > tweet.length());

        action.type(By.id("tweet-box-home-timeline"), tweet);
        action.click(By.xpath("//button[@type='button' and contains(@class, 'js-tweet-btn')]"));

        // we wait until Twitter empty the field and hide the button by itself
        action.waitTextToBeEmpty(By.id("tweet-box-home-timeline"));
        action.waitElementToBeInVisible(By.xpath("//button[@type='button' and contains(@class, 'js-tweet-btn')]"));
    }

    public void moveToHomePage()
    {
        assertTrue(isLogged);

        action.navigate(url);
    }

    public void moveToProfilePage()
    {
        assertTrue(isLogged);

        String profile = getProfileURL();

        assertTrue(action.countElements(By.id("user-dropdown-toggle")) > 0);
        action.click(By.id("user-dropdown-toggle"));

        assertTrue(action.countElements(By.className("account-summary")) > 0);
        action.click(By.className("account-summary"));

        // we need to wait this way because Twitter's pages has the same structure (id, class, ...)
        action.waitUrlToBe(profile);
    }

    public String getLastTweet()
    {
        assertTrue(isLogged);

        By locator = By.xpath("//p[contains(@class, 'tweet-text')]");

        // find it
        action.waitElementToBeVisible(locator);
        return action.readText(locator);
    }

    public String getProfileURL()
    {
        assertTrue(isLogged);

        By wanted = By.xpath("//a[contains(@class,'DashboardProfileCard-bg')]");

        assertTrue(action.countElements(wanted) > 0);

        return action.readAttribute(wanted, "href");
    }

    public void changeAvatar(String fullpath)
    {
        assertTrue(isLogged);

        accessToProfile();

        action.waitElementToBeVisible(By.id("photo-choose-existing"));

        action.fill(By.xpath("//input[@name='media[]' and contains(@class, 'file-input')]"), fullpath);

        action.waitElementToBeVisible(By.id("profile_image_upload_dialog-dialog"));

        action.click(By.xpath("//div[@id='profile_image_upload_dialog-dialog']//button[@type='button' and contains(@class, 'profile-image-save')]"));

        waitForEditingProfile();
    }

    public void deleteAvatar()
    {
        assertTrue(isLogged);

        By condition = By.xpath("//div[@id='avatar_confirm_remove_dialog-dialog']//button[contains(@class, 'ok-btn')]");

        accessToProfile();

        action.click(By.xpath("//li[@id='photo-delete-image']//button[@type='button' and @class='dropdown-link']"));

        // we need an avatar to be deleted
        assertTrue(action.countElements(condition) > 0);

        action.click(condition);

        waitForEditingProfile();
    }

    public void createList(String name, String resume, boolean isPrivate)
    {
        assertTrue(isLogged);

        String type = isPrivate ? "private" : "public";

        moveToLists();

        // name must starts with a letter
        assertTrue(Character.isLetter(name.charAt(0)));

        action.click(By.xpath("//div[@class='ListCreationModule-action']//button[@type='button' and @data-element-term='create_list_button']"));

        action.waitElementToBeVisible(By.id("list-operations-dialog-dialog"));

        action.type(By.id("list-name"), name);
        action.type(By.id("list-description"), resume);
        action.click(By.id("list-" + type + "-radio"));

        action.click(By.xpath("//button[@type='button' and contains(@class, 'update-list-button')]"));
    }

    public void deleteAllLists()
    {
        assertTrue(isLogged);

        By condition = By.xpath("//div[contains(@class, 'GridTimeline-items')]//div[@class='Grid']//a[contains(@class, 'ProfileListItem-name')]");

        moveToLists();

        List<String> urls = new ArrayList<String>();

        if (action.countElements(condition) > 0) {
            List<WebElement> lists = action.getElements(condition);

            for (WebElement el : lists) {
                urls.add(el.getAttribute("href"));
            }

            for (String u : urls) {
                action.navigate(u);

                // waiting for the page to be loaded and then waiting for the required class
                action.waitUrlToBe(u);
                action.waitElementToBeVisible(By.className("js-delete-list-action"));

                // delete this list
                action.click(By.className("js-delete-list-action"));
                action.click(By.id("confirm_dialog_submit_button"));

                // one way to do that
                action.waitElementToBeVisible(By.xpath("//button[@data-scribe-element='profile_edit_button']"));
            }
        }
    }

    public String getTitle()
    {
        return action.getTitle();
    }

    public int getMaxTweetLength()
    {
        return maxTweetLength;
    }

    private void moveToLists()
    {
        assertTrue(isLogged);

        action.click(By.id("user-dropdown-toggle"));
        action.click(By.xpath("//li[@data-name='lists']"));

        action.waitElementToBeVisible(By.xpath("//div[@class='ListCreationModule-action']"));
    }

    private void accessToProfile()
    {
        assertTrue(isLogged);

        action.click(By.xpath("//button[@type='button' and contains(@class, 'UserActions-editButton')]"));
        action.click(By.xpath("//button[@type='button' and contains(@class, 'ProfileAvatarEditing-button')]"));
    }

    private void waitForEditingProfile()
    {
        assertTrue(isLogged);

        action.waitElementToBeVisible(By.id("message-drawer"));
        action.waitElementToBeInVisible(By.id("message-drawer"));

        action.click(By.xpath("//div[@class='ProfilePage-editingButtons']//button[contains(@class, 'ProfilePage-saveButton')]"));
    }

    public void dispose()
    {
        driver.close();
    }
}
