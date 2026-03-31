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

    public String getUsername() {
        // Ưu tiên đọc từ biến môi trường (khi chạy trên CI/CD)
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            // Fallback: đọc từ file config (khi chạy local)
            username = props.getProperty("app.username", "standard_user"); 
        }
        return username;
    }

    public String getPassword() {
        // Ưu tiên đọc từ biến môi trường (khi chạy trên CI/CD)
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            // Fallback: đọc từ file config (khi chạy local)
            password = props.getProperty("app.password", "secret_sauce");
        }
        return password;
    }

    public String getBaseUrl() { return props.getProperty("base.url"); }
    public String getBrowser() { return props.getProperty("browser", "chrome"); }
}