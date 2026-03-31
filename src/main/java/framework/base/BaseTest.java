package framework.base;

import framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
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
    public void setUp(@Optional("") String xmlBrowser, @Optional("dev") String env) {
        System.setProperty("env", env);
        
        // 1. Lấy từ biến môi trường của GitHub
        String targetBrowser = System.getenv("BROWSER");
        
        // 2. Lấy từ lệnh Maven
        if (targetBrowser == null || targetBrowser.isBlank()) {
            targetBrowser = System.getProperty("browser");
        }
        
        // 3. Lấy từ file config local
        if (targetBrowser == null || targetBrowser.isBlank() || targetBrowser.equals("${browser}")) {
            targetBrowser = ConfigReader.getInstance().getBrowser();
        }

        // BẢO HIỂM NHÂN THỌ: Cấm chạy Edge offline trên Linux CI
        if (System.getenv("CI") != null && targetBrowser.toLowerCase().contains("edge")) {
            System.out.println(">>> CI DETECTED: ÉP CHẠY CHROME BỎ QUA EDGE KHỎI CRASH <<<");
            targetBrowser = "chrome"; 
        }

        System.out.println("=========================================");
        System.out.println("CI MODE: " + (System.getenv("CI") != null));
        System.out.println("TARGET BROWSER: " + targetBrowser);
        System.out.println("=========================================");

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