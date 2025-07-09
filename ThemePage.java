package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ThemePage {
	@FindBy(xpath = "//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[1]/label")
	private WebElement creative;

	@FindBy(xpath = "//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[2]/label")
	private WebElement minimal;

	@FindBy(xpath = "//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[3]/label")
	private WebElement modern;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/div/button")
	private WebElement createPortFolio;

	@FindBy(xpath = "/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")
	private WebElement previous;

	private WebDriver driver;

	public ThemePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(this.driver, this);
	}

	public void setCreative() {
		this.creative.click();
	}

	public void setMinimal() {
		this.minimal.click();
	}

	public void setModern() {
		this.modern.click();
	}

	public void createPortFolio() {
		this.createPortFolio.click();
	}

	public void previous() {
		this.previous.click();
	}
}
