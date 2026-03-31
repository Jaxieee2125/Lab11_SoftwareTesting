package framework.base;

import framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import java.time.Duration;

public abstract class BaseTest {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    protected WebDriver getDriver() { return tlDriver.get(); }

    @Parameters({"browser", "env"})
    @BeforeMethod(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        System.setProperty("env", env);
        
        // Ưu tiên lấy browser từ command line (-Dbrowser=...), nếu không có thì lấy từ file config
        String sysBrowser = System.getProperty("browser");
        String targetBrowser = (sysBrowser != null && !sysBrowser.isBlank()) ? sysBrowser : ConfigReader.getInstance().getBrowser();
        
        WebDriver driver = DriverFactory.createDriver(targetBrowser);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(ConfigReader.getInstance().getBaseUrl());
        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove();
        }
    }
}