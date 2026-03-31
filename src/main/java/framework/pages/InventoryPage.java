package framework.pages;
import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InventoryPage extends BasePage {
    @FindBy(css = ".inventory_list") private WebElement inventoryList;
    public InventoryPage(WebDriver driver) { super(driver); }
    
    public boolean isLoaded() {
        try { return driver.findElement(By.cssSelector(".inventory_list")).isDisplayed(); }
        catch (Exception e) { return false; }
    }
}