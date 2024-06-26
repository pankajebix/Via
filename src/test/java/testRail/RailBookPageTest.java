package testRail;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTestRail;
import via.com.pagesRail.RailBookPage;


public class RailBookPageTest extends BaseTestRail {
	// public WebDriver driver;
	public Object[][] data;
	RailBookPage booRailPage;
	
	public RailBookPageTest() {
		booRailPage = new RailBookPage(driver);		
	}

	@Test(dataProvider = "Data", priority = 1)
	public void flightvalidate(String rowNumber, String SourceKey, String Sourcestation, String DestinationKey,
			String Destinationstation, String Date, String searchStatus, String totalCount,
			String trainList, String trainClassName, String reprice, String totalTrainFound)
			throws InterruptedException, IOException {

		booRailPage.enterSource(SourceKey, Sourcestation);
		booRailPage.enterDestination(DestinationKey, Destinationstation);		
		booRailPage.enterdate(Date);
		booRailPage.clicksearch();

		int a = booRailPage.searchRailStatus(rowNumber);
		booRailPage.totalRailFound(rowNumber);	
		booRailPage.railList(rowNumber);

		booRailPage.modifySearchingData();
		Assert.assertTrue(a > 0);
	}

	@DataProvider(name = "Data")
	public Object[][] runData() throws IOException {
		data = booRailPage.getData();
		return data;
	}
}