package PortfolioPro.Testing;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.BasicInfoPage;
import pages.ContactPage;
import pages.ContentPage;
import pages.DashBoardPage;
import pages.HomePage;
import pages.ImagePage;
import pages.LoginPage;
import pages.ThemePage;

public class TC038 {

	private WebDriver driver;

	@BeforeMethod
	public void setup() {
		driver = DriverSetup.getDriver();
		driver.get("http://localhost:3000");
	}

	@Test
	public void testCreatePortFolioButtonFunctionality() {
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
		ContactPage contact = new ContactPage(driver);
		contact.setEmail("rohitmishra@gmail.com");
		contact.setPhone("9876543210");
		contact.setLocation("Jamshedpur, Jharkhand, India");
		contact.setGithub("https://github.com/rohitmishra0");
		contact.setLinkedin("https://linkedin.com/in/rohitmishra0");
		contact.setTwitter("https://x.com/rohitmishra0");
		contact.setWebsite("https://rohitmishra0.edu");
		contact.next();
		ContentPage content = new ContentPage(driver);
		content.setEducation("CV Raman Global University, B.Tech(CSE), 2025");
		content.setEducation("Cognizant, Programmer Analyst, 5 months, I learned Software development and Testing");
		content.setProjects(
				"Bartr, Angular&Spring Boot, This is a educational platform teaching courses with implementation of barter system");
		content.next();
		ImagePage image = new ImagePage(driver);
		image.next();
		ThemePage theme = new ThemePage(driver);
		theme.setModern();
		theme.createPortFolio();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		boolean urlContainsDashBoard = wait.until(ExpectedConditions.urlContains("/dashboard"));
		Assert.assertTrue(urlContainsDashBoard, "Portfolio did not created successfully");
		System.out.println("Portfolio created successfully");
	}

	@AfterMethod
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}
}
