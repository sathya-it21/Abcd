package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ImagePage {
	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[2]")
	private WebElement next;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")
	private WebElement previous;

	private WebDriver driver;

	public ImagePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void next() {
		this.next.click();
	}

	public void previous() {
		this.previous.click();
	}
}
