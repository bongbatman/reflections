package com.serpare.serpare_beta.database;

import java.util.ArrayList;

/**
 * Created by Nishant on 14/09/2015.
 */
public class UserInputSuggestions {
    public static ArrayList<String> SUGGESTION_LIST = new ArrayList<>();

    public static ArrayList<String> getSuggestionList() {
        return SUGGESTION_LIST;
    }

    public static void setSuggestionList(ArrayList<String> suggestionList) {
        SUGGESTION_LIST = suggestionList;
    }


}
