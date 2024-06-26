package base;

import org.openqa.selenium.WebDriver;

import via.com.baseLibrary.BaseClassRail;

public class BaseTestRail extends BaseClassRail {

	public static WebDriver driver;

	public void setup() {
		driver = initilization();
	}
}
