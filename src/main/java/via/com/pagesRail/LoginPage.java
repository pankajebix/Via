package via.com.pagesRail;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import via.com.constants.AppConstants;
import via.com.utils.ElementUtil;

public class LoginPage {
	//public WebDriver driver;
	ElementUtil eleUtil;

	public LoginPage(WebDriver driver) {
		//this.driver = driver;
		PageFactory.initElements(driver, this);
		eleUtil=new ElementUtil(driver);
	}

	@FindBy(xpath = "//input[@placeholder='Username']")
	WebElement username;

	@FindBy(xpath = "//input[@placeholder='Password']")
	WebElement password;

	@FindBy(xpath = "//input[@value='Masuk']")
	WebElement loginbutton;

	public void userlogin(){
		try {
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, username);
			username.clear();
			username.sendKeys("amit.balyan@via.com");
			
			password.clear();
			password.sendKeys("Ebix@2045");
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_SHORT_TIME_OUT, loginbutton);
			loginbutton.click();
			
		} catch (Exception e) {
			System.out.println("Issue in Login.userlogin "+e);
		}
	}
}
