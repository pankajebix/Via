package via.com.baseLibrary;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import org.testng.annotations.AfterSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import via.com.utils.SendEmailFlight;


public class BaseClassFlight {

	public static ExtentTest test;
	//private WebDriver driver;
	public static ExtentReports extent;

//	public static ExcelUtil excUtil = new ExcelUtil(
//			System.getProperty("user.dir") + "");

	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	public WebDriver initilization() {
		String browserName = "chrome";
		// String browserName = PropertyUtility.getProperty("browserName").trim();
//		ChromeOptions options = new ChromeOptions();
//		FirefoxOptions optionsFirefox = new FirefoxOptions();
//		EdgeOptions optionsEdge = new EdgeOptions();
//		options.addArguments("--remote-allow-origins=*");
//		optionsFirefox.addArguments("--remote-allow-origins=*");
//		optionsEdge.addArguments("--remote-allow-origins=*");

		if (browserName.equalsIgnoreCase("chrome")) {
			// driver = new ChromeDriver();
			tlDriver.set(new ChromeDriver());

		} else if (browserName.equalsIgnoreCase("firefox")) {
			// driver = new FirefoxDriver();
			tlDriver.set(new FirefoxDriver());

		} else if (browserName.equalsIgnoreCase("ie")) {
			// driver = new InternetExplorerDriver();
			tlDriver.set(new InternetExplorerDriver());

		} else if (browserName.equalsIgnoreCase("edge")) {
			// driver = new EdgeDriver();
			tlDriver.set(new EdgeDriver());			
		} else {
			System.out.println("Kindly pass the right browser name.");			
		}

		getDriver().manage().deleteAllCookies();	
		getDriver().manage().window().maximize();
		String url = "https://id.via.com/";
		getDriver().get(url);
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return getDriver();
	}

	/*
	 * Get the local thread copy of the driver
	 */
	public synchronized static WebDriver getDriver() {
		return tlDriver.get();
	}

	@AfterSuite
	public void tearDown() {
		try {
			Thread.sleep(1000);
			getDriver().quit();
			SendEmailFlight.main(null);

		} catch (Exception e) {
			System.out.println("Issue in BaseTest.tearDown " + e);
		}
	}
}
