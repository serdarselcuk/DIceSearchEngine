package com.search;

import com.search.XlsxConfigurator;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class BaseSteps {
    static WebDriver driver;
    static XlsxConfigurator file;

    String diceToken;
    WebDriverWait wait;

public BaseSteps(){
    PageFactory.initElements(driver,this);
    wait = new WebDriverWait(driver,10);
    file = new XlsxConfigurator("src/main/resources/JobId.xlsx","canApplie");


}
   {
        if(driver==null) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(new ChromeOptions().setHeadless(true));// new ChromeOptions().setHeadless(true)
            driver.manage().window().setSize(new Dimension(1920,1080));
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
//        if(diceToken==null){
//            getToken();
//        }
    }

    public static void getPage(){

        driver.get("https://www.dice.com/dashboard/login");
    }

    public static void getToken() throws UnsupportedEncodingException {

        RestAssured.baseURI = " https://secure.dice.com/oauth/token";

        String encodedUser= DatatypeConverter.printBase64Binary("serdarselcukus@outlook.com".getBytes("UTF-8"));
        String Password = DatatypeConverter.printBase64Binary("HS3a&Tb28riZhtu".getBytes("UTF-8"));

        given().
                auth().basic(encodedUser, Password).
                when().
                put().prettyPeek().
                then().assertThat().statusCode(200);
    }
    boolean syncronizedClickElement(WebElement element)throws RuntimeException {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        return true;
    }


    public static boolean avoidToApplie(XlsxConfigurator file){
        List<Map<String, String>> list = file.getDataList();
        System.out.println(list);
        return true;
    }

//    public static void main(String[] args) {
////        BaseSteps obj = new BaseSteps();
//        XlsxConfigurator avoidAplie = new XlsxConfigurator("src/main/resources/JobId.xlsx","dontApplie");
//        List<String> list = avoidAplie.getColumnList(1);
//        System.out.println(list);
//        List<String>arr=new ArrayList<>();
//        for (String each: list  ) {
//
//            if( each.isEmpty()||
//                each.isBlank()||
//                each.equalsIgnoreCase("Dice")||
//                each.equalsIgnoreCase("monster")||
//                each.equalsIgnoreCase("glassdoor")||
//                each.equalsIgnoreCase("indeed") ){
//            }else{
//                arr.add(each);
//            }
//        }
//        int i=0;
//        for (String each:arr
//             ) {
//            each = each.substring(each.length()-2).equals(".0")?each.substring(0,each.indexOf(".")):each;
//            avoidAplie.setCellData(each,i,1);
//            i++;
//        }
//    }
}
