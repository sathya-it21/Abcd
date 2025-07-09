package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	@FindBy(xpath = "//*[@id='email']")
	private WebElement email;

	@FindBy(xpath = "//*[@id='password']")
	private WebElement password;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement login;

	private WebDriver driver;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void login(String email, String password) {
		this.email.sendKeys(email);
		this.password.sendKeys(password);
		this.login.click();
	}
}
