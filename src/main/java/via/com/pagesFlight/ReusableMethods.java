package via.com.pagesFlight;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import via.com.constants.AppConstants;
import via.com.utils.ElementUtil;
import via.com.utils.JavaScriptUtil;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ReusableMethods {
	public WebDriver driver;
	public String year;
	public String Month;
	public String day;
	public String Date;

	ElementUtil eleUtil;
	JavaScriptUtil jsUtil;

	public ReusableMethods(WebDriver driver, String Date) {
		this.driver = driver;
		this.Date = Date;
		PageFactory.initElements(driver, this);
		eleUtil = new ElementUtil(driver);
		jsUtil = new JavaScriptUtil(driver);
	}
	
	@FindBy(xpath = "//span[@class='vc-month-box-head-cell vc-month-controls icon-leftarrowthin vc-month-control-active js-prev-month']")
	private WebElement previousMonthArrow;
	
	@FindBy(xpath = "//span[@class='vc-month-box-head-cell vc-month-controls icon-Rightarrowthin vc-month-control-active js-next-month']")
	private WebElement nextMonthArrow;

	public void selectdate() {
		try {
			String Dateinsheet = Date;
			String Dateinsheet1[] = Dateinsheet.split("/");
			day = Dateinsheet1[0];
			Month = Dateinsheet1[1];
			year = Dateinsheet1[2];

			String monthyear;
			String monthyear2;
			
			for(int i=1;i<=11;i++) {
				Thread.sleep(1000);
				monthyear = driver.findElement(By.xpath("(//span[@class='vc-month-box-head-cell '])[1]")).getText();
				monthyear2 = driver.findElement(By.xpath("(//span[@class='vc-month-box-head-cell '])[2]")).getText();

				String arr[] = monthyear.split(" ");
				String mon = arr[0];
				String yr = arr[1];
				
				String arr2[] = monthyear2.split(" ");
				String mon2 = arr2[0];
				String yr2 = arr2[1];

				if (mon.equalsIgnoreCase(Month) && yr.equals(year)) {
					break;
				} else {
					try {
						Thread.sleep(2000);
						eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, nextMonthArrow);
						jsUtil.clickElementByJS(nextMonthArrow);
						System.out.println("clicked on next arrow");
						
					} catch (Exception e) {
						for(int j=1;j<=11;j++) {
							Thread.sleep(2000);
							eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, previousMonthArrow);
							jsUtil.clickElementByJS(previousMonthArrow);
							System.out.println("clicked on previous arrow");
							if (mon.equalsIgnoreCase(Month) && yr.equals(year)) {
								break;
							}							
						}						
					}				
				}
			}

			Thread.sleep(1000);
			//(//div[@class='vc-month-box-container'])[1]//div[not(@class='vc-cell vc-disabled-cell') and @data-date='"+ day + "'] 
			WebElement date = driver.findElement(By.xpath("((//div[@class='vc-month-box-container'])[1]/div)[2]/div/div[not(@class='vc-cell vc-disabled-cell') and @data-date='"+day+"']"));

			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, date);
			jsUtil.scrollPageDown("40");
			Thread.sleep(2000);
			jsUtil.clickElementByJS(date);

			// =====================
//			Thread.sleep(1000);
//			WebElement dateSecond = driver.findElement(By.xpath(
//					"(//div[@class='vc-month-box-container'])[3]//div[not(@class='vc-cell vc-disabled-cell') and @data-date='"
//							+ day + "'] "));
//
//			eleUtil.waitForElementToBeClickable(AppConstants.DEFAULT_MEDIUM_TIME_OUT, dateSecond);
//			jsUtil.scrollPageDown("40");
//			jsUtil.clickElementByJS(dateSecond);

		} catch (Exception e) {
			System.out.println("Issue in ReusableMethods.selectdate " + e);
		}
	}

}