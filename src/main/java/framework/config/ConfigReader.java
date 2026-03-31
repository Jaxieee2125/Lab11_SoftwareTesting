package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties props = new Properties();
    private static ConfigReader instance;

    private ConfigReader() {
        // Lấy biến env từ hệ thống
        String env = System.getProperty("env");
        
        // Chống đạn: Nếu Maven truyền biến rỗng hoặc lỗi literal, ép về "dev"
        if (env == null || env.trim().isEmpty() || env.equals("${env}")) {
            env = "dev";
        }

        String file = "src/test/resources/config-" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy config: " + file);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }

    public String getBaseUrl() { return props.getProperty("base.url"); }
    public String getBrowser() { return props.getProperty("browser", "chrome"); }
}