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
        
        // CÚ CHỐT: Đọc trực tiếp từ Biến môi trường của GitHub Actions (Không qua Maven)
        String targetBrowser = System.getenv("BROWSER");
        
        // Nếu chạy ở máy bạn (không có biến BROWSER), thử lấy từ dòng lệnh Maven
        if (targetBrowser == null || targetBrowser.isBlank()) {
            targetBrowser = System.getProperty("browser");
        }
        
        // Nếu vẫn không có, lôi từ file config-dev.properties ra
        if (targetBrowser == null || targetBrowser.isBlank() || targetBrowser.equals("${browser}")) {
            targetBrowser = ConfigReader.getInstance().getBrowser();
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