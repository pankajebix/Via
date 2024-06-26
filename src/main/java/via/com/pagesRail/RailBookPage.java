package via.com.pagesRail;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

public class RailBookPage{

	public WebDriver driver;
	public static String date;
	String successMessage = null;
	String soldOutMessage = null;
	String fareChangedMessage = null;
	int randomNum;

	ExcelUtil excUtil = new ExcelUtil(System.getProperty("user.dir") + "\\src\\test\\resources\\testDataRail\\viaDataRail.xlsx");
	//ExcelUtil excUtilLastRun = new ExcelUtil(System.getProperty("user.dir") + "\\src\\test\\java\\resource\\lastRunData.xlsx");
	ElementUtil eleUtil;
	JavaScriptUtil jsUtil;

	public RailBookPage(WebDriver driver) {
		//super(driver, date);
		this.driver = driver;
		PageFactory.initElements(driver, this);
		eleUtil = new ElementUtil(driver);
		jsUtil = new JavaScriptUtil(driver);
	}

	@FindBy(xpath = "//input[@id='source']")
	WebElement source;

	@FindBy(xpath = "//input[@id='destination']")
	WebElement destination;

	@FindBys(@FindBy(xpath = "//ul[@id='ui-id-1']/li/span[@class='code']"))
	List<WebElement> Sourcelist;

	@FindBys(@FindBy(xpath = "//ul[@id='ui-id-2']/li/span[@class='code']"))
	List<WebElement> departurelist;

	@FindBy(xpath = "//div[@id='search-rail-btn']")
	WebElement searchbutton;

	@FindBys(@FindBy(xpath = "//div[@class='optsDiv']/preceding-sibling::div[@class='dealDiv'][1]"))
	List<WebElement> flightcompany;

	@FindBys(@FindBy(xpath = "//div[@id='resultFilter']/div/div[@class='filt_typ airlines']/div[@class='filtDataCont']/div/label"))
	List<WebElement> flightName;

	@FindBy(xpath = "//div[text()='100%']")
	WebElement hundred;

	@FindBy(xpath = "(//div[@class='productNavItemCont']/a/span)[4]")
	private WebElement modifybutton;

	@FindBy(xpath = "//div[text()='100%']")
	private WebElement ele100;

	@FindBy(xpath = "//label[@for='one-way']")
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
	
	//Rail
	
	@FindBy(xpath = "//span[@class='icon  railProductNav']")
	private WebElement railEle;
	
	@FindBy(xpath = "//div[@class='resCount onward']/span")
	private WebElement totalRailCount;
	
	@FindBys(@FindBy(xpath = "//div[@class='trainInfo']/div[@class='name js-toolTip jsName']"))
	private List<WebElement> railListEle;
	
	@FindBys(@FindBy(xpath = "//button[@class='priBtn jsSelBtn']"))
	private List<WebElement> bookNowCount;
	
	@FindBy(xpath = "//label[@id='step1Lbl']")
	private WebElement passengerDetailsPage;

	List<String> flightNameArrayList = new ArrayList<String>();
	
	static DataFormatter formatter = new DataFormatter();

	@DataProvider(name = "Data")
	public Object[][] getData(){
		Object data[][] = null;
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\test\\resources\\testDataRail\\viaDataRail.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();// total row
			XSSFRow row = sheet.getRow(0);// all cells in row 0
			int colCount = row.getLastCellNum();// total column
			data = new Object[rowCount - 1][colCount];
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
			System.out.println("====================================================");
			
			eleUtil.waitForElementVisibleAndToBeClickable(railEle, AppConstants.DEFAULT_MEDIUM_TIME_OUT);
			jsUtil.clickElementByJS(railEle);

			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, oneWayEle);
			jsUtil.clickElementByJS(oneWayEle);

			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, source);
			jsUtil.clickElementByJS(source);
			source.clear();
			source.sendKeys(SourceKey);
			System.out.println("Source Key : "+SourceKey);
			Thread.sleep(1000);
			try {
				eleUtil.waitForElementsVisible(Sourcelist, 2);				
			} catch (Exception e) {
				System.out.println("Issue in RailBookPage.enterSource "+e);
			}			
			
			for (WebElement selectsource : Sourcelist) {
				String name = selectsource.getText();
				if (name.contains(SourceKey)) {
					selectsource.click();
					break;				}
			}

		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.enterSource " + e);
		}
	}

	public void enterDestination(String key, String Destinationstation) {
		try {
			System.out.println("Destination Key : "+key);
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, destination);
			destination.click();
			destination.clear();
			destination.sendKeys(key);

			Thread.sleep(1000);
			try {
				eleUtil.waitForElementsVisible(departurelist, 2);				
			} catch (Exception e) {
				System.out.println("Issue in RailBookPage.enterDestination "+e);
			}	
			for (WebElement selectdeparture : departurelist) {
				if (selectdeparture.getText().contains(key)) {
					selectdeparture.click();
					break;
				}
			}

		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.enterDestination " + e);
		}
	}

	public void enterdate(String date) {
		try {
			System.out.println("Date : "+date);
			ReusableMethods rm = new ReusableMethods(driver, date);
			rm.selectdate();
		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.enterdate " + e);
		}
	}

	public void clicksearch() {
		try {
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_LONG_TIME_OUT, searchbutton);
			jsUtil.clickElementByJS(searchbutton);
		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.clicksearch " + e);
		}		
	}
	
	public int searchRailStatus(String rowNumber) {
		int railFound = 0;
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		excUtil.setCellData("viaDataRails", "Search Status", rowNumberDataUpdate, "");
		excUtil.setCellData("viaDataRails", "Total Count", rowNumberDataUpdate, "");
		excUtil.setCellData("viaDataRails", "Train Name", rowNumberDataUpdate, "");
		excUtil.setCellData("viaDataRails", "Class Name", rowNumberDataUpdate, "");
		excUtil.setCellData("viaDataRails", "Reprice", rowNumberDataUpdate, "");
		excUtil.setCellData("viaDataRails", "Total Train Found", rowNumberDataUpdate, "");
		
		String railSearchStatus;
		try {
			eleUtil.waitForElementVisible(totalRailCount, AppConstants.DEFAULT_MEDIUM_TIME_OUT);
			String totalRailCounts = totalRailCount.getText();
			if (totalRailCounts != null) {
				railFound = 1;
				railSearchStatus = "Pass";
				excUtil.setCellData("viaDataRails", "Search Status", rowNumberDataUpdate, railSearchStatus);
				excUtil.fillBackgroundCellColorGreen("viaDataRails", rowNumberDataUpdate, 7);
				System.out.println("Search Status : "+railSearchStatus);
			}
		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.searchRailStatus " + e);
			railSearchStatus = "Fail";
			excUtil.setCellData("viaDataRails", "Search Status", rowNumberDataUpdate, railSearchStatus);
			excUtil.fillBackgroundCellColorRed("viaDataRails", rowNumberDataUpdate, 7);
			System.out.println("Search Status : "+railSearchStatus);
		}
		return railFound;
	}
	
	public void totalRailFound(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			eleUtil.waitForElementVisible(totalRailCount, AppConstants.DEFAULT_SHORT_TIME_OUT);
			String totalCountRail=totalRailCount.getText().trim();
			excUtil.setCellData("viaDataRails", "Total Count", rowNumberDataUpdate, totalCountRail);
			System.out.println("Total Rail Count : "+totalCountRail);
		} catch (Exception e) {
			System.out.println("Issue in RailBookPage.totalRailFound");
			excUtil.setCellData("viaDataRails", "Total Count", rowNumberDataUpdate, "NA");
			System.out.println("Total Rail Count : "+"NA");
		}
	}
	
	public void railList(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			LinkedHashSet<String> list=new LinkedHashSet<String>();			
			for (WebElement e : railListEle) {
				String text = e.getText().trim();
				list.add(text);
			}
			excUtil.setCellData("viaDataRails", "Total Train Found", rowNumberDataUpdate, list.toString());
			System.out.println("Rail List : "+list.toString());
		} catch (Exception e) {
			System.out.println("Issue in RailBookPage.railList "+e);
			excUtil.setCellData("viaDataRails", "Total Train Found", rowNumberDataUpdate, "NA");
			System.out.println("Rail List : "+"NA");
		}
	}
	
	public void railNameAndClassName(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			int count = bookNowCount.size();
			randomNum = ThreadLocalRandom.current().nextInt(1, count);
			System.out.println("Random Number Before Clicking on Book Now is : "+randomNum);
			WebElement bookNow=driver.findElement(By.xpath("(//button[@class='priBtn jsSelBtn'])["+randomNum+"]"));
			jsUtil.scrollIntoViewTrue(bookNow);
			jsUtil.scrollPageUp("70");
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_SHORT_TIME_OUT, bookNow);
			
			WebElement railNameEle=driver.findElement(By.xpath("(//div[@class='trainInfo'])["+randomNum+"]/div[@class='name js-toolTip jsName']"));
			String railName=railNameEle.getText().trim();
			excUtil.setCellData("viaDataRails", "Train Name", rowNumberDataUpdate, railName);
			System.out.println("Rail Name : "+railName);
			
			WebElement classNameEle=driver.findElement(By.xpath("(//div[@class='trainInfo'])["+randomNum+"]/div[@class='cls jsCls font-light']/span"));
			String className=classNameEle.getText().trim();
			excUtil.setCellData("viaDataRails", "Class Name", rowNumberDataUpdate, className);
			System.out.println("Class Name : "+className);
			
		} catch (Exception e) {
			System.out.println("Issue in RailBookPage.railNameAndClassName "+e);
			excUtil.setCellData("viaDataRails", "Train Name", rowNumberDataUpdate, "NA");
			excUtil.setCellData("viaDataRails", "Class Name", rowNumberDataUpdate, "NA");
			System.out.println("Rail Name : "+"NA");
			System.out.println("Class Name : "+"NA");			
		}
	}
	
	public void reprice(String rowNumber) {
		int rowNumberDataUpdate = Integer.parseInt(rowNumber);
		try {
			WebElement bookNow2=driver.findElement(By.xpath("(//button[@class='priBtn jsSelBtn'])["+randomNum+"]"));
			jsUtil.clickElementByJS(bookNow2);
			
			eleUtil.waitForElementVisible(passengerDetailsPage, AppConstants.DEFAULT_LONG_TIME_OUT);
			String passengerDetailPage=passengerDetailsPage.getText();
			
			if(passengerDetailPage !=null) {
				excUtil.setCellData("viaDataRails", "Reprice", rowNumberDataUpdate, "Success");	
				System.out.println("Reprice Status : "+"Success");
			}
			
		} catch (Exception e) {
			System.out.println("Issue in RailBookPage.reprice "+e);
			excUtil.setCellData("viaDataRails", "Reprice", rowNumberDataUpdate, "NA");
			System.out.println("Reprice Status : "+"NA");
		}
	}
	
	public void modifySearchingData() {
		try {
			jsUtil.scrollPageUp();
			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, modifybutton);
			modifybutton.click();
			
		} catch (Exception e) {
			System.out.println("Issue in BookRailPage.modifySearchingData " + e);
		}
	}
	
	public String systemDate() {
		String sdate = null;
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate now = LocalDate.now();
			sdate = dtf.format(now);

		} catch (Exception e) {
		}
		return sdate;
	}
	
	public String dateManage() {
		String date = null;
		try {
			date=excUtil.getCellData("TestData", "Date", 2);
			
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
				RailBookPage ob = new RailBookPage(driver);
				String systemdateWithMonthYear = ob.systemDate();
				String seprateDate[] = systemdateWithMonthYear.split("/");
				systemdate = seprateDate[0];
				
				//String lastRunFullDate=excUtilLastRun.getCellData("LastRun", "LastRun", 2);
				//String lastRunArray[]=lastRunFullDate.split("/");
				//String lastRunDate=lastRunArray[0].trim();
				
				//excUtilLastRun.setCellData("LastRun", "LastRun", 2, systemdateWithMonthYear);
				
				String excelDateMonthYear[] = dateExcel.split("/");
				String excelDate1 = excelDateMonthYear[0];
				
//				if (!systemdate.equalsIgnoreCase(lastRunDate)) {
//					int dateExcelOnly = Integer.parseInt(excelDate1);
//					ob.dateIncreaseLogic(dateExcelOnly, dateExcel, rowNumber, systemdateWithMonthYear);
//				}				
			}

		} catch (Exception e) {
		}
	}
	
	public void dateIncreaseLogic(int dateOnlyFromExcel, String dateExcelFullDate, String rowNumber, String systemdateWithMonthYear) {
		String excelMonth;
		String excelYear;		
		String finalDate = null;
		String excelMonthCheckForYearUpdate = null;
		
		RailBookPage ob=new RailBookPage(driver);
		excelMonth=ob.excelMonth(dateExcelFullDate);
		excelYear=ob.excelYear(dateExcelFullDate);
		int YearInteger=Integer.parseInt(excelYear);	
		
		try {
			int count=excUtil.getRowCount("TestData");
			for(int i=2;i<=count;i++) {
				if (dateOnlyFromExcel <= 28) {
					dateOnlyFromExcel = dateOnlyFromExcel + 1;
				}			
				if (dateOnlyFromExcel > 28) {
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
					}else if (excelMonth.equals("Des")) {
						excelMonth = "Jan";
					}
					//excelMonthCheckForYearUpdate=excelMonth;
				}
				
				if(dateOnlyFromExcel == 1 & excelMonth.equalsIgnoreCase("Jan")) {
					YearInteger=YearInteger+1;
				}
				
				finalDate=dateOnlyFromExcel+"/"+excelMonth+"/"+YearInteger;
				excUtil.setCellData("TestData", "Date", i, finalDate);
			}
			//excUtil.setCellData("TestData", "RowNumber", count+1, "Last Run : "+systemdateWithMonthYear);
			
		} catch (Exception e) {
		}
	}
}
