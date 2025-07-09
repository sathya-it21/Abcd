package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
	@FindBy(xpath = "//a[@href='/login']")
	private WebElement loginButton;

	@FindBy(xpath = "//a[@href='/register']")
	private WebElement registerButton;

	private WebDriver driver;

	public void login() {
		loginButton.click();
	}

	public void register() {
		registerButton.click();
	}

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}
}