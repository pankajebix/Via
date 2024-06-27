package via.com.pagesFlight;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.DataProvider;

import via.com.constants.AppConstants;
import via.com.utils.ElementUtil;
import via.com.utils.ExcelUtil;
import via.com.utils.JavaScriptUtil;

public class FlightBookPage extends ReusableMethods {

	public WebDriver driver;
	public static String date;
	String successMessage = null;
	String soldOutMessage = null;
	String fareChangedMessage = null;

	ExcelUtil excUtil = new ExcelUtil(System.getProperty("user.dir") + "\\src\\test\\resources\\testDataFlight\\viadata.xlsx");
	ExcelUtil excUtilLastRun = new ExcelUtil(System.getProperty("user.dir") + "\\src\\test\\resources\\testDataFlight\\lastRunData.xlsx");
	ElementUtil eleUtil;
	JavaScriptUtil jsUtil;

	public FlightBookPage(WebDriver driver) {
		super(driver, date);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		eleUtil = new ElementUtil(driver);
		jsUtil = new JavaScriptUtil(driver);
	}

	@FindBy(xpath = "//input[@id='source']")
	WebElement source;

	@FindBy(xpath = "//input[@id='destination']")
	WebElement destination;

	@FindBys(@FindBy(xpath = "(//div[@id='round-trip-panel']/div)[1]/ul//li[@class='ui-menu-item']/span[@class='code']"))
	List<WebElement> Sourcelist;
	
	@FindBys(@FindBy(xpath = "(//div[@id='round-trip-panel']/div)[1]/ul//li[@class='ui-menu-item']/span[@class='name']"))
	List<WebElement> SourcelistName;

	@FindBys(@FindBy(xpath = "(//div[@id='round-trip-panel']/div)[3]/ul//li[@class='ui-menu-item']/span[@class='code']"))
	List<WebElement> departurelist;
	
	@FindBys(@FindBy(xpath = "(//div[@id='round-trip-panel']/div)[3]/ul//li[@class='ui-menu-item']/span[@class='name']"))
	List<WebElement> departurelistName;

	@FindBy(xpath = "//div[@id='search-flight-btn']")
	WebElement searchbutton;

	@FindBys(@FindBy(xpath = "//div[@class='optsDiv']/preceding-sibling::div[@class='dealDiv'][1]"))
	List<WebElement> flightcompany;

	@FindBys(@FindBy(xpath = "//div[@id='resultFilter']/div/div[@class='filt_typ airlines']/div[@class='filtDataCont']/div/label"))
	List<WebElement> flightName;

	@FindBy(xpath = "//div[text()='100%']")
	WebElement hundred;

	@FindBy(xpath = "(//div[@class='productNavItemCont']/a/span)[2]")
	private WebElement modifybutton;

	@FindBy(xpath = "//div[text()='100%']")
	private WebElement ele100;

	@FindBy(xpath = "//div[@class='one-way']/label")
	private WebElement oneWayEle;

	@FindBy(xpath = "(//label[@class='labl selAll'])[1]")
	private WebElement flightFilterSelectAll;

	@FindBy(xpath = "//div[@class='modalPanelWaitMessage']")
	private WebElement soldOutMessageEle;

	@FindBy(xpath = "(//div[@class='fltNum'])[1]")
	private WebElement flightNumberFirstEle;

	@FindBy(xpath = "(//button[@class='bookCTA'])[1]")
	private WebElement bookNowFirstEle;

	@FindBy(xpath = "//div[@class='page-title']")
	private WebElement pageTittleForSuccessEle;

	@FindBy(xpath = "//div[@class='modalPanelWaitMessage']")
	private WebElement upsEle;

	@FindBy(xpath = "//button[@id='modalAlertCTA']")
	private WebElement confirmationOK;

	@FindBy(xpath = "//button[@id='modalConfirmCTA']")
	private WebElement fareChangeConfirmationMessageEle;

	@FindBy(xpath = "//div[@class='resCount']/span")
	private WebElement totalFlightCountEle;

	@FindBy(xpath = "(//div[@class='optsDiv'])[1]/preceding-sibling::div[1]")
	private WebElement suppilerNameFirstEle;

	
	
	static DataFormatter formatter = new DataFormatter();

	@DataProvider(name = "Data")
	public Object[][] getData(){
		Object data[][] = null;
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\testDataFlight\\viadata.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();// total row
			XSSFRow row = sheet.getRow(0);// all cells in row 0
			int colCount = row.getLastCellNum();// total column
			data= new Object[rowCount - 1][colCount];
			for (int i = 0; i < rowCount - 1; i++) {
				row = sheet.getRow(i + 1);
				for (int j = 0; j < colCount; j++) {
					// data[i][j]=row.getCell(j);//but here data might be of string,int,char so we
					// use DataFormatter to convert all into string.
					XSSFCell cell = row.getCell(j);
					data[i][j] = formatter.formatCellValue(cell);
				}
			}
			
		} catch (Exception e) {
		}		
		return data;
	}

	public void enterSource(String SourceKey, String Sourcestation) {
		try {
			System.out.println("==========================================================");
			
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, oneWayEle);
			jsUtil.clickElementByJS(oneWayEle);

			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, source);
			jsUtil.clickElementByJS(source);
			source.clear();
			source.sendKeys(SourceKey);
			System.out.println("Given Source Key : "+SourceKey);
			System.out.println("Given Source Station Name : "+Sourcestation);
			Thread.sleep(1000);
			try {
				eleUtil.waitForElementsVisible(Sourcelist, 2);
				
			} catch (Exception e) {
			}			
			
			for (WebElement selectsource : Sourcelist) {
				String sourceKey = selectsource.getText();
				int len=sourceKey.length();
				String sourceKeyFound=sourceKey.substring(1, len-1);
				if (sourceKeyFound.equalsIgnoreCase(SourceKey)) {
					System.out.println("Found Source Key : " + sourceKeyFound);

					for (WebElement selectsourceName : SourcelistName) {
						String sourceName = selectsourceName.getText().trim();

						if (sourceName.equalsIgnoreCase(Sourcestation)) {
							System.out.println("Found Source Station Name : " + sourceName);
							selectsource.click();
							break;
						}
					}
					break;
				}
			}

		} catch (Exception e) {
			System.out.println("Issue in BookFlight.enterSource " + e);
		}
	}

	public void enterDestination(String key, String Destinationstation) {
		try {
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, destination);
			destination.click();
			destination.clear();
			destination.sendKeys(key);
			System.out.println("----------------------------------");
			System.out.println("Given Departure Key : "+key);
			System.out.println("Given Departure Station Name : "+Destinationstation);

			Thread.sleep(1000);
			try {
				eleUtil.waitForElementsVisible(departurelist, 2);				
			} catch (Exception e) {
				
			}
			for (WebElement departureKey : departurelist) {
				String departureSKey = departureKey.getText();
				int len = departureSKey.length();
				String departureKeyFound = departureSKey.substring(1, len - 1);
				
				if (departureKeyFound.equalsIgnoreCase(key)) {
					System.out.println("Found Departure Key : " + departureKeyFound);

					for (WebElement selectDepartureName : departurelistName) {
						String departureName = selectDepartureName.getText().trim();

						if (departureName.equalsIgnoreCase(Destinationstation)) {
							System.out.println("Found Departure Station Name : " + departureName);
							departureKey.click();
							break;
						}
					}
					break;
				}
			}

		} catch (Exception e) {
			System.out.println("Issue in BookFlight.enterDestination " + e);
		}
	}

	public void enterdate(String date) {
		try {
			System.out.println("Date : "+date);
			ReusableMethods rm = new ReusableMethods(driver, date);
			rm.selectdate();
		} catch (Exception e) {
			System.out.println("Issue in BookFlight.enterdate " + e);
		}
	}

	public void clicksearch() {
		try {
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, searchbutton);
			jsUtil.clickElementByJS(searchbutton);
		} catch (Exception e) {
			System.out.println("Issue in BookFlight.clicksearch " + e);
		}		
	}

	public int searchFlightStatus(String FlightName, String rowNumber) throws InterruptedException {
		List<String> flightNameArrayList = new ArrayList<String>();
		String flightNumberActual = null;
		String supplierNameActual = null;
		int flightFound = 0;
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		excUtil.setCellData("TestData", "Search Status", rowNumberDataUpdate, "");
		excUtil.fillBackgroundCellColorWhite("TestData", rowNumberDataUpdate, 8);
		excUtil.setCellData("TestData", "Supplier Name", rowNumberDataUpdate, "");
		excUtil.setCellData("TestData", "Flight No.", rowNumberDataUpdate, "");
		excUtil.setCellData("TestData", "Reprice", rowNumberDataUpdate, "");
		excUtil.setCellData("TestData", "Total Flight Count", rowNumberDataUpdate, "");
		excUtil.setCellData("TestData", "Flight Found", rowNumberDataUpdate, "");
		try {
			eleUtil.waitForElementVisible(ele100, AppConstants.DEFAULT_VERY_LONG_TIME_OUT);

		} catch (Exception e) {
			//System.out.println("Issue in BookFlight.searchFlightStatus " + e);
		} finally {
			try {			
			try {
				String totalFlightCount = totalFlightCountEle.getText().trim();
				excUtil.setCellData("TestData", "Total Flight Count", rowNumberDataUpdate, totalFlightCount);

			} catch (Exception e) {
				excUtil.setCellData("TestData", "Total Flight Count", rowNumberDataUpdate, "NA");
			}

			int j = 0;
			flightFound = 0;

			int flightSize = flightName.size();
			System.out.println("Total Flight Found : " + flightSize);

			for (WebElement ele : flightName) {
				String flightNameText = ele.getText();
				System.out.println("Flight Name found on UI : " + flightNameText);

				flightNameArrayList.add(j, flightNameText);
				j++;
			}
			String tempFlight=FlightName;
			for (int i = 0; i < flightSize; i++) {
				if (flightNameArrayList.get(i).toLowerCase().contains(tempFlight.toLowerCase())) {
					System.out.println("Given flight name " + FlightName + " is found.");
					flightFound = flightFound + 1;

					jsUtil.scrollIntoViewTrue(flightFilterSelectAll);
					jsUtil.scrollPageUp("100");

					eleUtil.waitForElementVisibleAndToBeClickable(flightFilterSelectAll,
							AppConstants.DEFAULT_SHORT_TIME_OUT);
					jsUtil.clickElementByJS(flightFilterSelectAll);

					WebElement filterData = driver
							.findElement(By.xpath("//label[contains(text(),'" + FlightName + "')]"));
					jsUtil.scrollIntoViewTrue(filterData);
					jsUtil.scrollPageUp("100");
					jsUtil.clickElementByJS(filterData);

					eleUtil.waitForElementPresenceWithFluentWait(AppConstants.DEFAULT_MEDIUM_TIME_OUT, 1,
							flightNumberFirstEle);
					jsUtil.scrollIntoViewTrue(flightNumberFirstEle);
					jsUtil.scrollPageUp("100");
					flightNumberActual = flightNumberFirstEle.getText().trim();
					supplierNameActual = suppilerNameFirstEle.getText().trim();
					break;
				}
			}
			} catch (Exception e2) {
				//System.out.println("Issue in BookFlight.searchFlightStatus");
			}
			if (flightFound > 0) {
				jsUtil.clickElementByJS(bookNowFirstEle);
				Thread.sleep(15000);
				excUtil.setCellData("TestData", "Search Status", rowNumberDataUpdate, "Pass");
				excUtil.fillBackgroundCellColorGreen("TestData", rowNumberDataUpdate, 8);
				excUtil.setCellData("TestData", "Flight Found", rowNumberDataUpdate, flightNameArrayList.toString());
				excUtil.setCellData("TestData", "Flight No.", rowNumberDataUpdate, flightNumberActual);
				excUtil.setCellData("TestData", "Supplier Name", rowNumberDataUpdate, supplierNameActual);
				System.out.println("Search Status : "+"Pass");
				System.out.println("Flight No. : "+flightNumberActual);
				System.out.println("Supplier Name : "+supplierNameActual);
				System.out.println("Flight Found : " + flightNameArrayList.toString());
				
			} else {
				excUtil.setCellData("TestData", "Search Status", rowNumberDataUpdate, "Fail");
				excUtil.fillBackgroundCellColorRed("TestData", rowNumberDataUpdate, 8);
				System.out.println("Given flight name " + FlightName + " is not found.");
				excUtil.setCellData("TestData", "Flight Found", rowNumberDataUpdate, flightNameArrayList.toString());
				excUtil.setCellData("TestData", "Flight No.", rowNumberDataUpdate, "NA");
				excUtil.setCellData("TestData", "Supplier Name", rowNumberDataUpdate, "NA");
				excUtil.setCellData("TestData", "Reprice", rowNumberDataUpdate, "NA");
				System.out.println("Search Status : "+"Fail");
				System.out.println("Flight No. : "+"NA");
				System.out.println("Supplier Name : "+"NA");
				System.out.println("Flight Found : " + flightNameArrayList.toString());
			}
			// Assert.assertTrue(flightFound > 0);
			
		}
		return flightFound;
	}

	public void successStatus(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {

			String successTittle = pageTittleForSuccessEle.getText();
			if (successTittle != null) {
				// System.out.println("Success Started.");
				if (successTittle.equalsIgnoreCase("Complete Your Booking in 3 simple steps")) {
					successMessage = "Success";
					excUtil.setCellData("TestData", "Reprice", rowNumberDataUpdate, successMessage);
					System.out.println("Reprice Status : "+successMessage);
				}
			}
		} catch (Exception e) {
			// System.out.println("Issue in BookFlight.successStatus "+e);
		}
	}

	public void soldOut(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			String upsSoldOut = upsEle.getText().trim();
			if (upsSoldOut != null) {
				// System.out.println("Ups Started");
				if (upsSoldOut.equalsIgnoreCase("Ups!")) {
					soldOutMessage = "Sold Out";
					eleUtil.doActionsCick(confirmationOK);
					excUtil.setCellData("TestData", "Reprice", rowNumberDataUpdate, soldOutMessage);
					System.out.println("Reprice Status : "+soldOutMessage);
				}
			}
		} catch (Exception e) {
			// System.out.println("Issue in BookFlight.soldOut "+e);
		}
	}

	public void fareChanged(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			String fareChangesMessage = fareChangeConfirmationMessageEle.getText();
			if (fareChangesMessage != null) {
				//System.out.println("Fare Change Started");
				fareChangedMessage = "Fare Changed";
				eleUtil.doActionsCick(fareChangeConfirmationMessageEle);
				excUtil.setCellData("TestData", "Reprice", rowNumberDataUpdate, fareChangedMessage);
				System.out.println("Reprice Status : "+fareChangedMessage);
			}
		} catch (Exception e) {
			// System.out.println("Issue in BookFlight.fareChanged "+e);
		}
	}

	public void modifySearchingData() {
		try {
			jsUtil.scrollPageUp();
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, modifybutton);
			modifybutton.click();

		} catch (Exception e) {
			System.out.println("Issue in BookFlight.modifySearchingData " + e);
		}
	}
	
	public String systemDate() {
		String sdate = null;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate now = LocalDate.now();
			sdate = dtf.format(now);
		} catch (Exception e) {
			System.out.println("Issue in BookFlight.systemDate "+e);
		}
		return sdate;
	}
	
	public String dateManage() {
		String date = null;
		try {
			date = excUtil.getCellData("TestData", "Date", 2);
		} catch (Exception e) {
		}
		return date;
	}
	
	public String excelMonth(String dateExcel) {
		String excelMonth=null;
		try {
			String excelDateMonthYear[] = dateExcel.split("/");
			excelMonth = excelDateMonthYear[1];
			
		} catch (Exception e) {
		}
		return excelMonth;
	}
	
	public String excelYear(String dateExcel) {
		String excelYear=null;
		try {
			String excelDateMonthYear[] = dateExcel.split("/");
			excelYear = excelDateMonthYear[2];
			
		} catch (Exception e) {
		}
		return excelYear;
	}
	
	public void dateLogic(String rowNumber, String dateExcel) {
		String systemdate;
		try {
			int rown = Integer.parseInt(rowNumber);
			if (rown == 2) {
				FlightBookPage ob = new FlightBookPage(driver);
				String systemdateWithMonthYear = ob.systemDate();
				String seprateDate[] = systemdateWithMonthYear.split("/");
				systemdate = seprateDate[0];
				
				String lastRunFullDate=excUtilLastRun.getCellData("LastRun", "LastRun", 2);
				String lastRunArray[]=lastRunFullDate.split("/");
				String lastRunDate=lastRunArray[0].trim();
				
				excUtilLastRun.setCellData("LastRun", "LastRun", 2, systemdateWithMonthYear);
				
				if (!systemdate.equalsIgnoreCase(lastRunDate)) {
					ob.dateIncreaseLogic(rowNumber, systemdateWithMonthYear);
				}				
			}

		} catch (Exception e) {
		}
	}
	
	public void dateIncreaseLogic(String rowNumber, String systemdateWithMonthYear) {
		String excelMonth;
		String excelYear;		
		String finalDate = null;
		String dateExcelFullDate;
		int dateOnlyFromExcel;			
		
		try {
			int count=excUtil.getRowCount("TestData");
			for (int i = 2; i <= count; i++) {
				dateExcelFullDate=excUtil.getCellData("TestData", "Date", i);
				String excelDateMonthYear[] = dateExcelFullDate.split("/");
				
				String dateOnlyFromExcelString = excelDateMonthYear[0];
				dateOnlyFromExcel=Integer.parseInt(dateOnlyFromExcelString);
				
				excelMonth = excelDateMonthYear[1];
				excelYear = excelDateMonthYear[2];
				
				
				int YearInteger=Integer.parseInt(excelYear);
				if (dateOnlyFromExcel <= 31) {
					dateOnlyFromExcel = dateOnlyFromExcel + 1;
				}

				if (dateOnlyFromExcel > 30 && excelMonth.equalsIgnoreCase("Apr")
						|| dateOnlyFromExcel > 30 && excelMonth.equalsIgnoreCase("Jun")
						|| dateOnlyFromExcel > 30 && excelMonth.equalsIgnoreCase("Sept")
						|| dateOnlyFromExcel > 30 && excelMonth.equalsIgnoreCase("Nov")) {
					dateOnlyFromExcel = 1;

					if (excelMonth.equals("Jan")) {
						excelMonth = "Feb";
					} else if (excelMonth.equals("Feb")) {
						excelMonth = "Mar";
					} else if (excelMonth.equals("Mar")) {
						excelMonth = "Apr";
					} else if (excelMonth.equals("Apr")) {
						excelMonth = "Mei";
					} else if (excelMonth.equals("Mei")) {
						excelMonth = "Jun";
					} else if (excelMonth.equals("Jun")) {
						excelMonth = "Jul";
					} else if (excelMonth.equals("Jul")) {
						excelMonth = "Agu";
					} else if (excelMonth.equals("Agu")) {
						excelMonth = "Sept";
					} else if (excelMonth.equals("Sept")) {
						excelMonth = "Okt";
					} else if (excelMonth.equals("Okt")) {
						excelMonth = "Nov";
					} else if (excelMonth.equals("Nov")) {
						excelMonth = "Des";
					} else if (excelMonth.equals("Des")) {
						excelMonth = "Jan";
					}
				}
				
				if (dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Jan")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Mar")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Mei")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Jul")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Agu")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Okt")
						|| dateOnlyFromExcel > 31 && excelMonth.equalsIgnoreCase("Des")) {
					dateOnlyFromExcel = 1;

					if (excelMonth.equals("Jan")) {
						excelMonth = "Feb";
					} else if (excelMonth.equals("Feb")) {
						excelMonth = "Mar";
					} else if (excelMonth.equals("Mar")) {
						excelMonth = "Apr";
					} else if (excelMonth.equals("Apr")) {
						excelMonth = "Mei";
					} else if (excelMonth.equals("Mei")) {
						excelMonth = "Jun";
					} else if (excelMonth.equals("Jun")) {
						excelMonth = "Jul";
					} else if (excelMonth.equals("Jul")) {
						excelMonth = "Agu";
					} else if (excelMonth.equals("Agu")) {
						excelMonth = "Sept";
					} else if (excelMonth.equals("Sept")) {
						excelMonth = "Okt";
					} else if (excelMonth.equals("Okt")) {
						excelMonth = "Nov";
					} else if (excelMonth.equals("Nov")) {
						excelMonth = "Des";
					} else if (excelMonth.equals("Des")) {
						excelMonth = "Jan";
					}
				}

				if (dateOnlyFromExcel > 28 && excelMonth.equalsIgnoreCase("Feb")) {
					dateOnlyFromExcel = 1;

					if (excelMonth.equals("Jan")) {
						excelMonth = "Feb";
					} else if (excelMonth.equals("Feb")) {
						excelMonth = "Mar";
					} else if (excelMonth.equals("Mar")) {
						excelMonth = "Apr";
					} else if (excelMonth.equals("Apr")) {
						excelMonth = "Mei";
					} else if (excelMonth.equals("Mei")) {
						excelMonth = "Jun";
					} else if (excelMonth.equals("Jun")) {
						excelMonth = "Jul";
					} else if (excelMonth.equals("Jul")) {
						excelMonth = "Agu";
					} else if (excelMonth.equals("Agu")) {
						excelMonth = "Sept";
					} else if (excelMonth.equals("Sept")) {
						excelMonth = "Okt";
					} else if (excelMonth.equals("Okt")) {
						excelMonth = "Nov";
					} else if (excelMonth.equals("Nov")) {
						excelMonth = "Des";
					} else if (excelMonth.equals("Des")) {
						excelMonth = "Jan";
					}
				}

				if (dateOnlyFromExcel == 1 & excelMonth.equalsIgnoreCase("Jan")) {
					YearInteger = YearInteger + 1;
				}

				finalDate = dateOnlyFromExcel + "/" + excelMonth + "/" + YearInteger;
				excUtil.setCellData("TestData", "Date", i, finalDate);
			}
			//excUtil.setCellData("TestData", "RowNumber", count+1, "Last Run : "+systemdateWithMonthYear);
			
		} catch (Exception e) {
		}
	}
}
