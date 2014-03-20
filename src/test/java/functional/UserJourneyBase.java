package functional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;


public class UserJourneyBase {

    private static WebDriver driver;

    @BeforeClass
    public void setUp() {
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.setEnvironmentProperty("DISPLAY", ":99");
        driver = new FirefoxDriver(firefoxBinary, null);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        driver.get(rootUrl());
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    private String rootUrl() {
        String url = "http://localhost:8080/";

        if (System.getenv().get("JENKINS_URL") != null) {
            url="http://localhost:9999/";
        }

        return url;
    }

    public WebDriver getDriver() {
        if (driver==null) setUp();
        return driver;
    }
}
