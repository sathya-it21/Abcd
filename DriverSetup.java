package PortfolioPro.Testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverSetup {

    private static final String CHROME_DRIVER_PATH = "C:\\Users\\2401687\\Downloads\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe";

    public static WebDriver getDriver() {

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        System.out.println("Set webdriver.chrome.driver property to: " + CHROME_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Maximize browser window on startup

        WebDriver driver = new ChromeDriver(options);
        System.out.println("ChromeDriver instance created.");


//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        System.out.println("Implicit wait set to 10 seconds.");

        return driver;
    }
}