package via.com.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ViewName;


public class ExtentReportListener implements ITestListener {

	//private static final String OUTPUT_FOLDER = "./reports/";
	private static final String OUTPUT_FOLDER=System.getProperty("user.dir")+"\\reports\\";
	private static final String FILE_NAME = "TestExecutionReport.html";

	private static ExtentReports extent = init();
	public static ThreadLocal<ExtentTest> test = new ThreadLocal<ExtentTest>();
	private static ExtentReports extentReports;

	private static ExtentReports init() {

		Path path = Paths.get(OUTPUT_FOLDER);
		// if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}

		extentReports = new ExtentReports();
		ExtentSparkReporter reporter = new ExtentSparkReporter(OUTPUT_FOLDER + FILE_NAME);
		reporter.config().setReportName("Exposure Hub Automation Test Report");
		reporter.config().setDocumentTitle("Exposure Hub Automation Test Report");
		
		// change the order of extent report view. 
		reporter.viewConfigurer().viewOrder().as(new ViewName[] {ViewName.CATEGORY,ViewName.TEST,ViewName.EXCEPTION,ViewName.DASHBOARD});
		
		// add logo in extent report
		String imgPath="https://test-eh.ebixexchange.com/assets/images/exposure-hub-logo.svg";
		String jsCode1="document.getElementsByClassName('logo')[0].style.backgroundImage = 'url(" + imgPath + ")';";
		String jsCode2="document.getElementsByClassName('logo')[0].style.width= '450%';";
		String jsCode3="document.getElementsByClassName('search-box')[0].style.paddingLeft='95px';";
		String combinedJsCode = jsCode1 + jsCode2+jsCode3;
		reporter.config().setJs(combinedJsCode);
		// add logo in extent report
		
		extentReports.attachReporter(reporter);
		extentReports.setSystemInfo("System", "Window");
		extentReports.setSystemInfo("Author", "Pankaj Yadav");
		extentReports.setSystemInfo("Build", "103");
		extentReports.setSystemInfo("Team", "QA Team");
		
		//extra		
		//extentReports.setSystemInfo("Environment Name", BaseClass.environmentName());
		//extra
		
		return extentReports;
	}

	@Override
	public synchronized void onStart(ITestContext context) {
		System.out.println("Test Suite started!");
	}

	@Override
	public synchronized void onFinish(ITestContext context) {
		System.out.println(("Test Suite is ending!"));
		extent.flush();
		test.remove();
	}

	@Override
	public synchronized void onTestStart(ITestResult result) {
		//String methodName = result.getMethod().getMethodName();
		String qualifiedName = result.getMethod().getQualifiedName();
		int last = qualifiedName.lastIndexOf(".");
		int mid = qualifiedName.substring(0, last).lastIndexOf(".");
		String className = qualifiedName.substring(mid + 1, last);

		//System.out.println(methodName + " started!");
		ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),
				result.getMethod().getDescription());

		extentTest.assignCategory(result.getTestContext().getSuite().getName());
		/*
		 * methodName = StringUtils.capitalize(StringUtils.join(StringUtils.
		 * splitByCharacterTypeCamelCase(methodName), StringUtils.SPACE));
		 */
		extentTest.assignCategory(className);
		test.set(extentTest);
		test.get().getModel().setStartTime(getTime(result.getStartMillis()));
	}

	@Override
	public synchronized void onTestSuccess(ITestResult result) {
		//System.out.println((result.getMethod().getMethodName() + " passed!"));
		test.get().pass("Test passed");
		// test.get().pass(result.getThrowable(),
		// MediaEntityBuilder.createScreenCaptureFromPath(DriverFactory.getScreenshot()).build());
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	@Override
	public synchronized void onTestFailure(ITestResult result) {
		//System.out.println((result.getMethod().getMethodName() + " failed!"));
		//String methodName = result.getMethod().getMethodName();
		test.get().fail(result.getThrowable());
		test.get().fail(result.getMethod().getMethodName() + " failed");
		//test.get().fail(result.getThrowable(),
		//MediaEntityBuilder.createScreenCaptureFromPath(BaseClass.getScreenShots(methodName+"copy")).build());
//		MediaEntityBuilder.createScreenCaptureFromPath(BaseClass.getScreenShots(methodName)).build();
//		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	@Override
	public synchronized void onTestSkipped(ITestResult result) {
		//System.out.println((result.getMethod().getMethodName() + " skipped!"));
		//String methodName = result.getMethod().getMethodName();
		//test.get().skip(result.getThrowable(),
		// MediaEntityBuilder.createScreenCaptureFromPath(DriverFactory.getScreenshot()).build());
		test.get().getModel().setEndTime(getTime(result.getEndMillis()));
	}

	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println(("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName()));
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	
	public static synchronized void logExtentReport(String message) {		
		test.get().log(Status.INFO, message);	
	}
}