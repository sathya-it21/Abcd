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

public class TC020 {
	private WebDriver driver;

	@BeforeMethod
	public void setup() {
		driver = DriverSetup.getDriver();
		driver.get("http://localhost:3000");
	}

	@Test
	public void testNavigationToContactPage() {
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
		content.previous();
		WebElement contactButton = driver.findElement(By.xpath("//*[@id='radix-«r4»-trigger-contact']"));
		Assert.assertEquals(contactButton.getAttribute("data-state"), "active",
				"Form did not navigated to Contact Page");
		System.out.println("Content Page navigated to Contact Page using previous button");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
