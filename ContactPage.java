package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContactPage {
	@FindBy(xpath = "//*[@id='email']")
	private WebElement email;

	@FindBy(xpath = "//*[@id='phone']")
	private WebElement phone;

	@FindBy(xpath = "//*[@id='location']")
	private WebElement location;

	@FindBy(xpath = "//*[@id='github']")
	private WebElement github;

	@FindBy(xpath = "//*[@id='linkedin']")
	private WebElement linkedin;

	@FindBy(xpath = "//*[@id='twitter']")
	private WebElement twitter;

	@FindBy(xpath = "//*[@id='website']")
	private WebElement website;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[2]")
	private WebElement next;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")
	private WebElement previous;

	private WebDriver driver;

	public ContactPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void setEmail(String email) {
		this.email.sendKeys(email);
	}

	public void setPhone(String phone) {
		this.phone.sendKeys(phone);
	}

	public void setLocation(String location) {
		this.location.sendKeys(location);
	}

	public void setGithub(String github) {
		this.github.sendKeys(github);
	}

	public void setLinkedin(String linkedin) {
		this.linkedin.sendKeys(linkedin);
	}

	public void setTwitter(String twitter) {
		this.twitter.sendKeys(twitter);
	}

	public void setWebsite(String website) {
		this.website.sendKeys(website);
	}

	public void next() {
		this.next.click();
	}

	public void previous() {
		this.previous.click();
	}
}
