package com.dsa.total.datasolutionapp.DataTransferObject;

/**
 * Created by jisun on 2017-05-19.
 */

public class HelperType {

    private static String SQLITE = "1";
    private static String JSON ="2";
    private static String XML ="3";
    private static String PREFERENCE ="4";

    public static String getSQLITE() {
        return SQLITE;
    }

    public static String getJSON() {
        return JSON;
    }

    public static String getXML() {
        return XML;
    }

    public static String getPREFERENCE() {
        return PREFERENCE;
    }

}
