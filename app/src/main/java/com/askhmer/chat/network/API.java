package com.askhmer.chat.network;

/**
 * Created by soklundy on 5/23/2016.
 */
public class API {

    public static final String BASEURL = "http://10.0.3.2:8080/ChatAskhmer/api/";

    public static final String key = "Basic YWRtaW46MTIz";

    /**
     * API url
     */
    public static final String FBSIGNUP = BASEURL + "user/adduserwithfb";
    public static final String CHECKUSERBYFBORNUMP = BASEURL + "user/checkUser?valuesSearch=";
    public static final String SEARCHUSER = BASEURL + "user/searchby_userno_name/";
}
