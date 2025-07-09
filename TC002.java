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

public class TC002 {

	private WebDriver driver;

	@BeforeMethod
	public void setup() {
		driver = DriverSetup.getDriver();
		driver.get("http://localhost:3000");
	}

	@Test
	public void testBasicInfoPageValidation() {
		HomePage home = new HomePage(driver);
		home.login();
		LoginPage login = new LoginPage(driver);
		login.login("sankeerth@gmail.com", "12345678");
		DashBoardPage dashBoard = new DashBoardPage(driver);
		dashBoard.createPortFolio();
		BasicInfoPage basic = new BasicInfoPage(driver);
		basic.setTitle("My Portfolio");
		basic.setFullName("Rohit Mishra");
		basic.setDescription("This is my first portfolio");
		basic.setJobTitle("Backend Developer");
		basic.setBio("Hi this is rohit i am a backend developer");
		basic.setSkills("Spring, Spring Boot, My SQL");
		basic.next();
		WebElement contact = driver.findElement(By.xpath("//button[@id=\"radix-«r4»-trigger-contact\"]"));
		Assert.assertEquals(contact.getAttribute("data-state"), "active", "Form did not accepted valid data");
		System.out.println("Basic Page accepted valid data and navigated to Contact Page");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
