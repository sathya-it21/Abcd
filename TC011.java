package PortfolioPro.Testing;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import pages.BasicInfoPage;
import pages.DashBoardPage;
import pages.HomePage;
import pages.LoginPage;

public class TC011 {
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
		WebElement contact = driver.findElement(By.xpath("//button[@id=\"radix-«r4»-trigger-contact\"]"));
		Assert.assertEquals(contact.getAttribute("data-state"), "active", "Form did not navigated to Contact Page");
		System.out.println("Basic Page navigated to Contact Page using next button");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
