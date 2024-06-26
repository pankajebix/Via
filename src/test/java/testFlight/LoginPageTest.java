package testFlight;

import org.testng.annotations.Test;

import base.BaseTestFlight;
import via.com.pagesFlight.LoginPage;

public class LoginPageTest extends BaseTestFlight{
	
	LoginPage loginPage;
	
	public LoginPageTest() {
		setup();
		loginPage=new LoginPage(driver);		
	}
	
	@Test(priority = 1, enabled = true)
	public void userLogin() {
		loginPage.userlogin();
	}

}
