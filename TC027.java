package PortfolioPro.Testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.BasicInfoPage;
import pages.ContactPage;
import pages.ContentPage;
import pages.DashBoardPage;
import pages.HomePage;
import pages.LoginPage;

public class TC027 {
	private WebDriver driver;

	@BeforeMethod
	public void setup() {
		driver = DriverSetup.getDriver();
		driver.get("http://localhost:3000");
	}

	@Test
	public void testProjectsSectionInputValidation() {
		HomePage home = new HomePage(driver);
		home.login();
		LoginPage login = new LoginPage(driver);
		login.login("sankeerth@gmail.com", "12345678");
		DashBoardPage dashBoard = new DashBoardPage(driver);
		dashBoard.createPortFolio();
		BasicInfoPage basic = new BasicInfoPage(driver);
		basic.next();
		ContactPage contact = new ContactPage(driver);
		contact.next();
		ContentPage content = new ContentPage(driver);
		content.setProjects("1.Bartr application\n\n" + "2. Assignment submission tracking system\n");
		content.next();
		WebElement imageButton = driver.findElement(By.xpath("//*[@id='radix-«r4»-trigger-images']"));
		Assert.assertEquals(imageButton.getAttribute("data-state"), "active",
				"Projects section did not accepted valid data");
		System.out.println("Projects section accepted valid data");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

}
