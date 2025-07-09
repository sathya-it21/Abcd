package PortfolioPro.Testing;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import pages.BasicInfoPage;
import pages.ContactPage;
import pages.DashBoardPage;
import pages.HomePage;
import pages.LoginPage;

public class TC019 {
	private WebDriver driver;

	@BeforeMethod
	public void setup() {
		driver = DriverSetup.getDriver();
		driver.get("http://localhost:3000");
	}

	@Test
	public void testNavigationToBasicInfoPage() {
		HomePage home = new HomePage(driver);
		home.login();
		LoginPage login = new LoginPage(driver);
		login.login("sankeerth@gmail.com", "12345678");
		DashBoardPage dashBoard = new DashBoardPage(driver);
		dashBoard.createPortFolio();
		BasicInfoPage basic = new BasicInfoPage(driver);
		basic.next();
		ContactPage contact = new ContactPage(driver);
		contact.previous();
		WebElement basicButton = driver.findElement(By.xpath("//*[@id='radix-«r4»-trigger-basic']"));
		Assert.assertEquals(basicButton.getAttribute("data-state"), "active",
				"Form did not navigated to Basic Info Page");
		System.out.println("Contact Page navigated to Basic Info Page using previous button");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
