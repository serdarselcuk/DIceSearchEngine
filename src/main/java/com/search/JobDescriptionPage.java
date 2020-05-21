package com.search;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class JobDescriptionPage extends BaseSteps {

    XlsxConfigurator avoidFile ;
    String LocatorOfPositionId ;
    List<String>list ;

    {
        if(avoidFile==null) avoidFile = new XlsxConfigurator("src/main/resources/JobId.xlsx","dontApplie");
        if(LocatorOfPositionId==null) LocatorOfPositionId = ".company-header-info";//*[contains(text(),'Position Id :')]";
        if(list==null) list = avoidFile.getColumnList(1);
    }

    @FindBy(id="applyTxt")
    private WebElement applyButtonTextElement;

    @FindBy(id="jobdescSec")
    private WebElement jobDescElement;

    public void getPositionId(int row,int page)throws NoSuchElementException, TimeoutException {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(LocatorOfPositionId)));

        String buttonValue = applyButtonTextElement.getAttribute("value");
        WebElement id = driver.findElement(By.cssSelector(LocatorOfPositionId));
        String text = jobDescElement.getText();
        String[] arr = setElements(id);

        int numberOfRow = ((page-1)*20)+row;
        if(buttonValue.equalsIgnoreCase("Apply Now")||buttonValue.equalsIgnoreCase("View")){

            if(available(arr[1],text)){

                file.setCellData(numberOfRow+" ",numberOfRow,0);
                file.setCellData(arr[0]+"",numberOfRow,1);
                file.setCellData(arr[1]+"",numberOfRow,2);
                file.setCellData(arr[2]+"",numberOfRow,3);
                file.setCellData("page: "+page+" num: "+row,numberOfRow,4);
                file.setCellData(text,numberOfRow,5);
            }else{

                System.out.println("there is citizen only or the id is in the list: "+page+" " +row);
                avoidFile.setCellData(arr[1],avoidFile.rowCount()+1,1);
            }
        }else{
            System.out.println("already applied");
            avoidFile.setCellData(arr[1],avoidFile.rowCount()+1,1);
        }
    }

    private String[] setElements(WebElement id) {
        String[] result = id.getText().split("\n");
        for (String each :
                result) {
            String textDiceId ="Dice Id : " ,textId= "Position Id : ",textPostDate="Originally Posted : " ;
            if(each.contains(textDiceId)){
                result[0] = each.substring(textDiceId.length()+1);
            }else if(each.contains(textId)){
                result[1] = each.substring(textId.length()+1);
            }else if(each.contains(textPostDate)){
                result[2] = each.substring(textPostDate.length()+1);
            }
        }
        return result;
    }

    public boolean available(String id,String text){
        List<String>list = avoidFile.getColumnList(1);
        for (String each :
                list) {
            if(id.equalsIgnoreCase(each)){
                return false;
            }
        }
        if(text.replaceAll("\\W","")
                .toLowerCase()
                .contains("uscitizenonly")){
            return false;
        }
        return true;
    }


}
