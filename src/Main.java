import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Properties;

public class Main
{
    private Twitter twitter;
    private String login;
    private String password;

    @Before
    public void init()
    {
        Properties prop = new Properties();
        InputStream input = null;

        try
        {
			// reading the configuration file
            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the login and password properties
            login = prop.getProperty("login");
            password = prop.getProperty("password");

            // everything is okay
            assertNotEquals(login, "");
            assertNotEquals(password, "");

            // instantiate Twitter's testing class
            twitter = new Twitter();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @After
    public void after()
    {
        // cleaning the memory
        if (twitter != null)
        {
            twitter.dispose();
        }
    }

    @Test
    public void test1()
    {
        // log in to Twitter
        twitter.login(login, password);

        // we check the title of the home page
        assertEquals(twitter.getTitle(), "Twitter");

        // we get a funny quote to tweet
        String tweet = Helper.getFunnyQuote(twitter.getMaxTweetLength());

        // we check if it worked
        assertNotEquals(tweet, "");
        assertTrue(tweet.length() <= twitter.getMaxTweetLength());

        // send Tweet with the funny quote we got earlier
        twitter.postTweet(tweet);

        // we go to our profile in order to see our Tweets
        twitter.moveToProfilePage();

        // we check if our last Tweet is the same as the one we sent
        assertEquals(tweet, twitter.getLastTweet());

        // we log out from Twitter
        twitter.logout();
    }

    @Test
    public void test2()
    {
        // log in to Twitter
        twitter.login(login, password);

        // we delete all our Tweets
        if (twitter.countTweets() != 0) {
            twitter.moveToProfilePage();
            twitter.deleteAllTweets();
            twitter.moveToHomePage();
        }

        // we expect 0 Tweet
        assertEquals(twitter.countTweets(), 0);

        // we post 3 Tweets
        twitter.postTweets(3);

        // we expect 3 Tweets
        assertEquals(twitter.countTweets(), 3);

        // we log out from Twitter
        twitter.logout();
    }

    @Test
    public void test3()
    {
        // get the absolute path of our avatar
        File file = new File("selenium.png");
        String avatar = file.getAbsolutePath();

        // log in to Twitter
        twitter.login(login, password);

        twitter.moveToProfilePage();

        // upload our avatar
        twitter.changeAvatar(avatar);
    }

    @Test
    public void test4()
    {
        // log in to Twitter
        twitter.login(login, password);

        twitter.moveToProfilePage();

        // delete our avatar
        twitter.deleteAvatar();
    }

    @Test
    public void test5()
    {
        // log in to Twitter
        twitter.login(login, password);

        // create a new list
        twitter.createList("A new list", "Here is the description", true);
    }

    @Test
    public void test6()
    {
        // log in to Twitter
        twitter.login(login, password);

        // we delete all the created lists
        twitter.deleteAllLists();
    }
}