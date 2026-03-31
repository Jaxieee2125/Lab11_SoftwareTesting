package tests;
import framework.base.BaseTest;
import framework.config.ConfigReader;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {
    @Test
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());

        String user = ConfigReader.getInstance().getUsername();
        String pass = ConfigReader.getInstance().getPassword();
        InventoryPage inventoryPage = loginPage.login(user, pass);
        Assert.assertTrue(inventoryPage.isLoaded(), "Login failed!");
    }
}