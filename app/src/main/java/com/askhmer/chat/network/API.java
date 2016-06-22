package com.askhmer.chat.network;

/**
 * Created by soklundy on 5/23/2016.
 */
public class API {

    public static final String BASEURL = "http://10.0.3.2:8081/ChatAskhmer/api/";

    public static final String key = "Basic YWRtaW46MTIz";

    /**
     * API url
     */
    public static final String FBSIGNUP = BASEURL + "user/adduserwithfb";
    public static final String CHECKUSERBYFBORNUMP = BASEURL + "user/checkUser?valuesSearch=";
    public static final String SEARCHUSER = BASEURL + "user/searchby_userno_name/";

    /**
     * ravy url
     */
    public static final String VIEWFRIEND =  BASEURL + "friend/viewfriendById/";
    public static final String VIEWUSERPROFILE = BASEURL + "user/viewUserById/";
    public static final String UPDATEUSER = BASEURL + "user/updateuser";
    public static final String LISTFRIEND = BASEURL + "friend/listfriendById/";
    public static final String LISTCHATROOM = BASEURL + "chathistory/listchatroom/";
    public static final String UPLOADFILE = "http://10.0.3.2:8080/ChatAskhmer/resources/upload/file/";
    public static final String UPLOAD = "http://10.0.3.2:8080/ChatAskhmer/api/uploadfile/upload?url=user";


    //http://localhost:8081/ChatAskhmer/api/message/list_message_by_roomId/1/2
    public static final String LISTMESSAGE = BASEURL + "message/list_message_by_roomId/";
}
