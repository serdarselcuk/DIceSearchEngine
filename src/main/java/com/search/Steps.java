package com.search;

import com.search.MainPage;

public class Steps extends BaseSteps {
    static MainPage main = new MainPage();

    public static void main(String[] args){
        getPage();


        main.sendCredentials().sendSearchTerm();

        main.searchCardsText();

    }
}
