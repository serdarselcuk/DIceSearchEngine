package com.search;


import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.io.PrintStream;
import java.util.List;


public class MainPage extends BaseSteps {


    @FindBy(id = "email")
    private WebElement emailBox;

    @FindBy(id = "password")
    private WebElement passwordBox;

    @FindBy(css="navbar-toggle.nbt.navbar-magnifier.search")
    private WebElement searchIcon;

    @FindBy(css=" [placeholder$='Search Term']")
    private WebElement searchTerm;

    @FindBy(id="google-location-search")
    private WebElement searchLocationBox;

    @FindBy(xpath="//span[contains(text(),'radius from')]")
    private WebElement searchLocationText;

    @FindBy(css = "iframe[title='Usabilla Feedback Form']")
    private WebElement iframe;

    @FindBy(css = " span[data-cy='search-count']")
    private WebElement searchCountelement;

    @FindBy(id = "submitSearch-button")
    private WebElement searchButton;
    @FindBy(css = ".pagination-next.page-item.ng-star-inserted")
    private WebElement nextPageButton;


    public MainPage(){

    }
    static  int searchCount;
    public MainPage sendCredentials(){
        emailBox.sendKeys("serdarselcukus@outlook.com");
        passwordBox.sendKeys("HS3a&Tb28riZhtu", Keys.ENTER);
        return new MainPage();
    }
    public MainPage clickSearchIcon(){
        wait.until(ExpectedConditions.elementToBeClickable(searchTerm));
        searchIcon.click();
        return new MainPage();
    }
    public MainPage sendSearchTerm() {

        if(!searchTerm.isEnabled()){
            clickSearchIcon();
            wait.until(ExpectedConditions.elementToBeClickable(searchTerm));
        }

        searchTerm.sendKeys("SDET",Keys.ENTER);

        cancelLocation();

        searchButton.click();

        return new MainPage();

    }
    public MainPage sendSearchTerm(String term) {

        if(!searchTerm.isEnabled()){
            clickSearchIcon();
            wait.until(ExpectedConditions.elementToBeClickable(searchTerm));
        }

        searchTerm.sendKeys("term",Keys.ENTER);

        cancelLocation();

        searchButton.click();

        return new MainPage();

    }

    private void cancelLocation(){
        if(!searchLocationText.getText().equals("radius from")){
            Actions act = new Actions(driver);
            searchLocationBox.click();
            act.keyDown(Keys.CONTROL).perform();
            act.sendKeys("a").perform();
            act.keyUp(Keys.CONTROL).perform();
            searchLocationBox.sendKeys(Keys.DELETE);
        }
    }

    private void searchElementCount(){
        searchCount =  Integer.parseInt(searchCountelement.getText());
    }

    public MainPage searchCardsText(){

        try {
            wait.until(ExpectedConditions.textToBe(By.xpath("//span[contains(text(),'radius from')]"),"radius from"));
        }catch(TimeoutException e){
            System.out.println(searchLocationText.getText()+" is the text in the search box, but I was waiting for \"radius from\"");;
        }
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(" dhi-search-card[class=ng-star-inserted]"),20));
        WebElement searchElements = null;
        int num = 1;
        int page = 1;
        searchElementCount();
        while(num<=20||(page*20)+num<=searchCount){

            System.out.println(page +"th page num: "+num+ ": ================================================================================");

            searchElements = driver.findElement(By.cssSelector("dhi-search-card[class=ng-star-inserted]:nth-child("+num+") a"));
            try{
                syncronizedClickElement(searchElements);
                new JobDescriptionPage().getPositionId(num,page);
            }catch (NoSuchElementException e){
                e.printStackTrace();
                num--;
            }catch(StaleElementReferenceException e){
                e.printStackTrace();
                continue;
            }catch(TimeoutException e) {
                e.printStackTrace();
                continue;
            }catch(RuntimeException e){
                e.printStackTrace();
                System.out.println("couldn't click the Search element, continue... ");
                num--;
                continue;
            }
            driver.navigate().back();

            if(num ==20){
                if(clickNextPage()) {
                    num = 0;
                    page++;
                }else{
                    System.out.println("Couldn't click next: \n clickNextPage(): "+ clickNextPage()+" num: "+num+" page: "+page);

                }
            }
            num++;
        }

        return new MainPage();
    }
    private boolean clickNextPage(){
        System.out.println("nextPageButton.isEnabled(): "+nextPageButton.isEnabled());
        if(nextPageButton.isEnabled()){
            syncronizedClickElement(nextPageButton);
            return true;
        }
        return false;
    }


}
