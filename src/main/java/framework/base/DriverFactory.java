package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    public static WebDriver createDriver(String browser) {
        // GitHub Actions tự đặt biến CI=true [cite: 85]
        boolean isCI = System.getenv("CI") != null; // [cite: 86]

        return switch (browser.toLowerCase()) {
            case "edge" -> createEdgeDriver(isCI);
            case "firefox" -> createFirefoxDriver(isCI); // [cite: 87]
            default -> createChromeDriver(isCI); // [cite: 87]
        };
    }

    private static WebDriver createChromeDriver(boolean headless) { // [cite: 90]
        ChromeOptions options = new ChromeOptions(); // [cite: 92]
        if (headless) { // [cite: 93]
            options.addArguments("--headless=new"); // [cite: 94]
            options.addArguments("--no-sandbox"); // [cite: 95]
            options.addArguments("--disable-dev-shm-usage"); // Tránh lỗi OOM trên Linux [cite: 98]
            options.addArguments("--window-size=1920,1080"); // [cite: 99]
        } else { // [cite: 100]
            options.addArguments("--start-maximized"); // [cite: 102]
        }
        WebDriverManager.chromedriver().setup(); // [cite: 103]
        return new ChromeDriver(options); // [cite: 104]
    }

    private static WebDriver createFirefoxDriver(boolean headless) { // [cite: 105]
        FirefoxOptions options = new FirefoxOptions(); // [cite: 106]
        if (headless) options.addArguments("-headless"); // [cite: 107]
        WebDriverManager.firefoxdriver().setup(); // [cite: 108]
        return new FirefoxDriver(options); // [cite: 113]
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        
        // Ép dùng file msedgedriver.exe offline trên máy bạn
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
        return new EdgeDriver(options);
    }
}