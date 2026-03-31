# Selenium Framework - Lab 11

Dự án Kiểm thử Tự động (Automation Testing) được xây dựng dựa trên Selenium WebDriver, TestNG và Maven. Dự án áp dụng mô hình Page Object Model (POM) và được thiết lập CI/CD pipeline sử dụng GitHub Actions để tự động chạy test.

## Cấu trúc công nghệ
- **Ngôn ngữ:** Java 17
- **Core Framework:** Selenium WebDriver 4.18.1
- **Test Framework:** TestNG
- **Build Tool:** Maven
- **Design Pattern:** Page Object Model (POM)
- **CI/CD:** GitHub Actions (Chạy ngầm với Headless Chrome)

## Yêu cầu hệ thống (Prerequisites)
Để chạy dự án này trên máy cá nhân (Local), bạn cần cài đặt:
1. Java Development Kit (JDK) 17 trở lên.
2. Apache Maven.
3. Trình duyệt Microsoft Edge (hoặc Google Chrome).
4. Thư mục `drivers/` ở thư mục gốc chứa file `msedgedriver.exe` (Dành cho Edge).

## Cách chạy test ở môi trường Local

1. Mở Terminal / Command Prompt tại thư mục gốc của project.
2. Để thực thi toàn bộ test script, chạy lệnh sau:
   ```bash
   mvn clean test