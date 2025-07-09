
package PortfolioPro.Testing;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App 
{
	public static WebDriver driver;

	
	public static void login(String email,String password)
    {
    	driver.findElement(By.xpath("//a[@href='/login']")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.id("email")).sendKeys("sankeerth@gmail.com");
        driver.findElement(By.id("password")).sendKeys("12345678");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
	
    public static void createPortfolio()
    {
    	WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id='3']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@href='/create-portfolio']"))).click();
    }
    
    public static void basicInfoPage(String title,String fullName,String description,String jobTitle,String bio,String skills)
    {
    	driver.findElement(By.xpath("//*[@id='title']")).sendKeys(title);
        driver.findElement(By.xpath("//*[@id='fullName']")).sendKeys(fullName);
        driver.findElement(By.xpath("//*[@id='description']")).sendKeys(description);
        driver.findElement(By.xpath("//*[@id='jobTitle']")).sendKeys(jobTitle);
        driver.findElement(By.xpath("//*[@id='bio']")).sendKeys(bio);
        driver.findElement(By.xpath("//*[@id='skills']")).sendKeys(skills);
		WebElement contact = driver.findElement(By.xpath("//*[@id=\"radix-«r4»-trigger-contact\"]/div/span"));
		System.out.println(contact.getText());
        next();
    }
    
    public static void contactPage(String email,String phone,String location,String github,String linkedin,String twitter,String website)
    {
    	driver.findElement(By.xpath("//*[@id='email']")).sendKeys(email);
        driver.findElement(By.xpath("//*[@id='phone']")).sendKeys(phone);
        driver.findElement(By.xpath("//*[@id='location']")).sendKeys(location);
        driver.findElement(By.xpath("//*[@id='github']")).sendKeys(github);
        driver.findElement(By.xpath("//*[@id='linkedin']")).sendKeys(linkedin);
        driver.findElement(By.xpath("//*[@id='twitter']")).sendKeys(twitter);
        driver.findElement(By.xpath("//*[@id='website']")).sendKeys(website);
        next();
}
    public static void contentPage(String education,String experience,String projects)
    {
    	 driver.findElement(By.xpath("//*[@id='education']")).sendKeys(education);
         driver.findElement(By.xpath("//*[@id='experience']")).sendKeys(experience);
         driver.findElement(By.xpath("//*[@id='projects']")).sendKeys(projects);
         next();
    }
    public static void imagePage()
    {
    	next();
    }
    public static void themePage(String theme)
    {
    	if(theme.equalsIgnoreCase("creative"))
    		selectCreative();
    	else if(theme.equalsIgnoreCase("minimal"))
    		selectMinimal();
    	else
    		selectModern();
    	create();
    }
    public static void next()
    {
        driver.findElement(By.xpath("/html/body/div/div/div[4]/form/div[2]/div[8]/button[2]")).click();
    }
    public static void prev()
    {
        driver.findElement(By.xpath("/html/body/div/div/div[4]/form/div[2]/div[8]/button[1]")).click();
    }
    public static void create()
    {
        driver.findElement(By.xpath("/html/body/div/div/div[4]/form/div[2]/div[8]/div/button")).click();
    }
    public static void selectCreative()
    {
      driver.findElement(By.xpath("//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[1]/label")).click();
    }
    public static void selectMinimal()
    {
      driver.findElement(By.xpath("//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[2]/label")).click();
    }
    public static void selectModern()
    {
      driver.findElement(By.xpath("//*[@id='radix-«r4»-content-theme']/div/div[1]/div/div[3]/label")).click();
    }
    public static void main( String[] args )
    {
        driver=DriverSetup.getDriver();
        driver.get("http://localhost:3000");
        login("sankeerth@gmail.com","12345678");
        createPortfolio();
        
        
        basicInfoPage("My Portfolio","Rohit Mishra","This is my first portfolio","Backend Developer","Hi this is rohit i am a backend developer","Spring, Spring Boot, My SQL");
        
        
        contactPage("rohitmishra@gmail.com","9876543210","Jamshedpur, Jharkhand, India","https://github.com/rohitmishra0","https://linkedin.com/in/rohitmishra0","https://x.com/rohitmishra0","https://rohitmishra0.edu");
        contentPage("CV Raman Global University, B.Tech(CSE), 2025","Cognizant, Programmer Analyst, 5 months, I learned Software development and Testing","Bartr, Angular&Spring Boot, This is a educational platform teaching courses with implementation of barter system");
        imagePage();
        themePage("minimal");
    }
}

