package pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;


/**
 * @author         :Sathish
 * @since          :Nov 6, 2021
 * @filename       :PutsBox.java
 * @version		   :1.0
 * @description    :This file has all the methods and page objects related to PutsBox mail page
 */

public class PutsBox extends PageSupporter{
	WebDriver driver;
	public PutsBox(WebDriver driver) {
		super(driver);
		this.driver=driver;
	}

	@FindAll(value = {@FindBy(how=How.CSS,using="tr")})
	private List<WebElement> mails;

	@FindBy(how=How.LINK_TEXT,using="Inspect")
	private WebElement refresh;

	@FindBy(how=How.CSS,using="a[href*='clear']")
	private WebElement clear;

	@FindBy(how=How.CSS,using="a[title*='Customer Consent']")
	private WebElement reviewAndConfirm;

	@FindAll(value = {@FindBy(how=How.CSS,using="td.text-pad")})
	private List<WebElement> contents;
	
	/**
	 * 
	 * Method Name : navigateToPutsMailBox
	 * Description : 
	 * Author      : Sathish
	 * Return Types: void
	 * Paramters   : PutsBox
	 * Date        : Nov 6, 2021
	 * Version     : 1.0
	 */

	public void navigateToPutsMailBox(String url) {
		driver.navigate().to(url);
	}

	/**
	 * 
	 * Method Name : validateAccountNumberAndConfirmationNumberInMail
	 * Description : This method validates the account number and confirmation number in the oh
	 * mail - Welcome to Direct Energy!
	 * Author      : Sathish
	 * Return Types: String
	 * Parameters   : PutsBox
	 * Date        : Nov 25, 2021
	 * Version     : 1.0
	 */

	public String validateAccountNumberAndConfirmationNumberInMail(String accountNumber,String confirmationNumber) {

		int count = 0;
		String text;
		wait(30);
		while( mails.size()==0 && count<75 ) {
			wait(1);
			count++;
			refresh.click();
		}

		for(WebElement mail:mails) {
			if(mail.findElements(By.xpath("//*[contains(text(),'Welcome mail')]")).size()>0){
				wait(3);
				mail.findElement(By.xpath("//a[contains(text(),'HTML')]")).click();
				switchToNewWindow();
				waitFor(contents,15);
				for(WebElement content :contents ) {
					text = content.getText();
					if(text.contains(accountNumber) && text.contains(confirmationNumber)) {
						return "Account number and  Confirmation Number verified in the mail";
					}
				}
			}
		}
		return "Error in verifying confirmation in the mail";
	}



}
