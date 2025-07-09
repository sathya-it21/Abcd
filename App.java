package PortfolioPro.Testing;

import org.openqa.selenium.WebDriver;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        WebDriver driver=DriverSetup.getDriver();
        driver.get("http://www.google.com");
    }
}
