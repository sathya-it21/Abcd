package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ContentPage {

	@FindBy(xpath = "//*[@id=\"education\"]")
	private WebElement education;

	@FindBy(xpath = "//*[@id='experience']")
	private WebElement experience;

	@FindBy(xpath = "//*[@id='projects']")
	private WebElement projects;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[2]")
	private WebElement next;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")
	private WebElement previous;

	private WebDriver driver;

	public ContentPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void setEducation(String education) {
		this.education.sendKeys(education);
	}

	public void setExperience(String experience) {
		this.experience.sendKeys(experience);
	}

	public void setProjects(String projects) {
		this.projects.sendKeys(projects);
	}

	public void next() {
		this.next.click();
	}

	public void previous() {
		this.previous.click();
	}
}
