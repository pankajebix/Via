package testRail;

import org.testng.annotations.Test;

import base.BaseTestRail;
import via.com.pagesRail.LoginPage;

public class LoginPageTest extends BaseTestRail{
	
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
