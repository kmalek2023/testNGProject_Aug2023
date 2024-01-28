package variousConcepts;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CrmTest {
	// Element list
	WebDriver driver;

	// Element lists
	By USER_NAME_FIELD = By.xpath("//input[@name='user_name']");
	By PASSWORD_FIELD = By.xpath("//input[@type='password']");
	By SIGNIN_BUTTON_FIELD = By.xpath("//button[@type='submit']");

	// Dashboard
	By DASHBOARD_HEADER_FIELD = By.xpath("//strong[contains(text(), 'Dashboard')]");
	By CUSTOMERS_MENU_FIELD = By.xpath("//div[@class= 'aside-inner']/nav/ul[2]/li[2]/a/span");
	By SIDEBAR_FIELD = By.xpath("//nav[@class= 'sidebar']");
	By ADD_CUSTOMER_MENU_FIELD = By.xpath("//a[ @title='Add Customer']");
	By ADD_CUSTOMER_VALIDATION_FIELD = By.xpath("//strong[contains(text(), 'New Customer')]");

	// List of elements for Add Customer
	By FULL_NAME_FIELD = By.xpath("//div[@id='general_compnay']/descendant::input[1]");
	By COMPANY_DROPDOWN_FIELD = By.xpath("//select[@name = 'company_name']");
	By EMAIL_FILED = By.xpath("//div[@id='general_compnay']/descendant::input[2]");
	By PHONE_FILED = By.xpath("//div[@id='general_compnay']/descendant::input[3]");
	By ADDRESS_FILED = By.xpath("//div[@id='general_compnay']/descendant::input[4]");
	By CITY_FILED = By.xpath("//div[@id='general_compnay']/descendant::input[5]");
	By ZIPCODE_FILED = By.xpath("//div[@id='general_compnay']/descendant::input[6]");
	By COUNTRY_DROPDOWN_FILED = By.xpath("//select[@name='country']");
	By GROUP_DROPDOWN_FILED = By.xpath("//select[@id='customer_group']");
	By SAVE_BUTTON_FILED = By.xpath("//button[@id='save_btn']");

	// test data or mock data
	String browser;
	String url;
	String userName;
	String password;

	String dashboardValidationText = "Dashboard";
	String userAlertValidationText = "Please enter your user name";
	String addCustomerValidationText = "New Customer";
	String fullName = "Selenium";
	String email = "demo@techfios.com";
	String company = "Techfios";
	String phone = "952000";
	String country = "Bangladesh";

	@BeforeClass
	public void readConfig() {
		try {
			InputStream input = new FileInputStream("src\\main\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			System.out.println("Browser used: " + browser);
			url = prop.getProperty("url");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@BeforeMethod
	public void init() {

		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
			driver = new EdgeDriver();
		} else {
			System.out.println("Please define a valid browser");
		}

		driver.manage().deleteAllCookies();
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterMethod
	public void tearDown() throws InterruptedException {
		Thread.sleep(10000);
		driver.quit();
	}

	 @Test(priority=1)
	public void TestLogin() {
		driver.findElement(USER_NAME_FIELD).sendKeys(userName);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), dashboardValidationText,
				"Dashboard not found");

	}

	 @Test (priority=2)
	public void testAlert() {
		driver.findElement(SIGNIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.switchTo().alert().getText(), userAlertValidationText, "Alert is not available");
		driver.switchTo().alert().accept();
		

	}

	@Test (priority=3)
	public void testAddCustomer() {
		TestLogin();
		driver.findElement(CUSTOMERS_MENU_FIELD).click();
		driver.findElement(ADD_CUSTOMER_MENU_FIELD).click();
		 Assert.assertEquals(driver.findElement(ADD_CUSTOMER_VALIDATION_FIELD).getText(), addCustomerValidationText, "Add Customer page not found");

		int randomNum = generateRandomNum(9999);
		selectFromDropdown(driver.findElement(COMPANY_DROPDOWN_FIELD), company); //using WebElement

		// enter Full Name
		driver.findElement(FULL_NAME_FIELD).sendKeys(fullName + randomNum);
		Select sel = new Select(driver.findElement(COMPANY_DROPDOWN_FIELD));
		sel.selectByVisibleText(company);

		driver.findElement(EMAIL_FILED).sendKeys(randomNum + email);
		driver.findElement(PHONE_FILED).sendKeys(phone + randomNum);
		
		selectFromDropdown(COUNTRY_DROPDOWN_FILED, country); // using By class
		


	}

	private void selectFromDropdown(WebElement element, String visbleText) {
		Select sel = new Select(element);
		sel.selectByVisibleText(visbleText);
	}
	private void selectFromDropdown(By element, String visbleText) {
		Select sel = new Select(driver.findElement(element));
		sel.selectByVisibleText(visbleText);
	}

	public int generateRandomNum(int boundNum) {
		Random rnd = new Random();
		return rnd.nextInt(boundNum);
	}

}
