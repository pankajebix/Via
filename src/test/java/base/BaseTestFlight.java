package base;

import org.openqa.selenium.WebDriver;

import via.com.baseLibrary.BaseClassFlight;

public class BaseTestFlight extends BaseClassFlight{
	
	public static WebDriver driver;

	public void setup() {
		driver = initilization();
	}

}
