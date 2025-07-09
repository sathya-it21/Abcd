package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BasicInfoPage {

	@FindBy(xpath = "//*[@id='title']")
	private WebElement title;

	@FindBy(xpath = "//*[@id='fullName']")
	private WebElement fullName;

	@FindBy(xpath = "//*[@id='description']")
	private WebElement description;

	@FindBy(xpath = "//*[@id='jobTitle']")
	private WebElement jobTitle;

	@FindBy(xpath = "//*[@id='bio']")
	private WebElement bio;

	@FindBy(xpath = "//*[@id='skills']")
	private WebElement skills;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[2]")
	private WebElement next;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")
	private WebElement previous;

	private WebDriver driver;

	public BasicInfoPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void setTitle(String title) {
		this.title.sendKeys(title);
	}

	public void setFullName(String fullName) {
		this.fullName.sendKeys(fullName);
	}

	public void setDescription(String description) {
		this.description.sendKeys(description);
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle.sendKeys(jobTitle);
	}

	public void setBio(String bio) {
		this.bio.sendKeys(bio);
	}

	public void setSkills(String skills) {
		this.skills.sendKeys(skills);
	}

	public void next() {
		this.next.click();
	}

	public void previous() {
		this.previous.click();
	}
}
