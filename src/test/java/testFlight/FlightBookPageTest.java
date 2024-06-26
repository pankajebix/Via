package testFlight;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import base.BaseTestFlight;
import via.com.pagesFlight.FlightBookPage;

public class FlightBookPageTest extends BaseTestFlight{
	// public WebDriver driver;
	public Object[][] data;

	FlightBookPage flightBookPage;

	public FlightBookPageTest() {	
		flightBookPage = new FlightBookPage(driver);		
	}

	@Test(priority = 0)
	public void dateManage() {		
		String Date = flightBookPage.dateManage();
		flightBookPage.dateLogic("2", Date);
	}

	@Test(dataProvider = "Data", priority = 1)
	public void flightvalidate(String rowNumber, String SourceKey, String Sourcestation, String DestinationKey,
			String Destinationstation, String Date, String FlightName, String searchStatus, String supplierName,
			String flightNo, String reprice, String totalFlightFound, String flightFoundList)
			throws InterruptedException, IOException {

		flightBookPage.enterSource(SourceKey, Sourcestation);

		flightBookPage.enterDestination(DestinationKey, Destinationstation);

		// bookFlight.dateLogic(rowNumber, Date);

		flightBookPage.enterdate(Date);
		flightBookPage.clicksearch();

		int a = flightBookPage.searchFlightStatus(FlightName, rowNumber);

		flightBookPage.successStatus(rowNumber);
		flightBookPage.soldOut(rowNumber);
		flightBookPage.fareChanged(rowNumber);
		flightBookPage.modifySearchingData();
		Assert.assertTrue(a > 0);
	}

	@DataProvider(name = "Data")
	public Object[][] runData() throws IOException {
		data = flightBookPage.getData();
		return data;
	}
}