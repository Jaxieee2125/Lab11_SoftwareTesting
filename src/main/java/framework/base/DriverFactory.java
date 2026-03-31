package framework.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverFactory {
    public static WebDriver createDriver(String browser) {
        // GitHub Actions tự đặt biến CI=true
        boolean isCI = System.getenv("CI") != null; 

        return switch (browser.toLowerCase()) {
            case "edge" -> createEdgeDriver(isCI);
            default -> createChromeDriver(isCI); 
        };
    }

    private static WebDriver createChromeDriver(boolean headless) { 
        ChromeOptions options = new ChromeOptions(); 
        if (headless) { 
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox"); 
            options.addArguments("--disable-dev-shm-usage"); // Tránh lỗi OOM trên Linux CI 
            options.addArguments("--window-size=1920,1080"); 
        } else {
            options.addArguments("--start-maximized");
        }
        WebDriverManager.chromedriver().setup(); 
        return new ChromeDriver(options); 
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