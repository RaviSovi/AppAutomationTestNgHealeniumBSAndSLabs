package com.company.testscripts;

import com.project.base.DriverFactory;
import com.project.config.ER;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class BasicTest extends DriverFactory {

	@Test
	public static void testCase1() throws Exception {
		try {
			ER.Info("Test case 1 started");
//			getDriver().get("https://www.google.com");
//			String title = getDriver().getTitle();
//			System.out.println("current tile is " + title);
//			Thread.sleep(5000);
//			ER.Info("Clicked on button successfully....");
//			getDriver().findElement(By.xpath("//a[@aria-label='Gmail ']")).click();
//			String gmailtitle = getDriver().getTitle();
//			ER.Pass("Title of gmail page is ::::" + gmailtitle);

		} catch (Exception e) {
			ER.Fail("Failed test case with reason:\n"+e.getMessage());
			e.getMessage();

		}
	}

	@Test
	public static void testCase2() throws Exception {
		try {
			ER.Info("Test case 2 started");
//			getDriver().get("https://www.facebook.com");
//			String title = getDriver().getTitle();
//			System.out.println("current tile is " + title);
//			Thread.sleep(5000);
//			ER.Info("Clicked on button successfully....");
//			getDriver().findElement(By.xpath("//a[contains(text(), 'Forgot')]")).click();
//			String instaTitle = getDriver().getTitle();
//			ER.Pass("Title of gmail page is ::::" + instaTitle);
		} catch (Exception e) {
			ER.Fail("Failed test case with reason:\n"+e.getMessage());
			e.getMessage();

		}

	}
}
