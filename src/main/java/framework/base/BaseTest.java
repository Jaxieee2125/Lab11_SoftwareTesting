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
        
        // 1. Ưu tiên 1: Lấy từ lệnh Maven (-Dbrowser=...) mà GitHub Actions truyền vào
        String targetBrowser = System.getProperty("browser");
        
        // 2. Ưu tiên 2: Lấy từ TestNG XML (nếu Maven không có)
        if (targetBrowser == null || targetBrowser.isBlank() || targetBrowser.equals("${browser}")) {
            targetBrowser = xmlBrowser;
        }
        
        // 3. Ưu tiên 3: Lấy từ file config-dev.properties (chỉ dùng cho Local máy bạn)
        if (targetBrowser == null || targetBrowser.isBlank()) {
            targetBrowser = ConfigReader.getInstance().getBrowser();
        }

        System.out.println(">>> TRÌNH DUYỆT ĐANG CHẠY: " + targetBrowser + " <<<");

        // Khởi tạo trình duyệt qua DriverFactory
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