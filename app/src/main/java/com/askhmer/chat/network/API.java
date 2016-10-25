package com.askhmer.chat.network;

/**
 * Created by soklundy on 5/23/2016.
 */
public class API {

   // public static final String BASEURL = "http://10.0.3.2:8080/ChatAskhmer/api/";
   public static final String BASEURL = "http://chat.askhmer.com/api/";

    public static final String key = "Basic YWRtaW46MTIz";

    /**
     * API url
     */
    public static final String LISTSTRICKER = "http://chat.askhmer.com/api/sticker/liststicker";
    public static final String LISTSTRICKERWITHID = "http://chat.askhmer.com/api/sticker/liststicker";
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
    public static final String UPLOADFILE = "http://chat.askhmer.com/resources/upload/file/";
    public static final String UPLOAD = BASEURL+"uploadfile/image?folder=user";
    public static final String UPLOADIMAGE = BASEURL+"uploadfile/image?folder=images";

    public static final String LISTMESSAGE = BASEURL + "message/list_message_by_roomId/";


    public static final String SEARCHFRIEND = BASEURL+"friend/searchfriend/";
    public static final String CONFIRM = BASEURL+"friend/confirm/";
    public static final String LISTFREINDBYID = BASEURL+"friend/listfriendById/";

    public static final String DELETEMESSAGE = BASEURL+"message/deletemessage/";
 public static final String DELETEMCONVERSATION = BASEURL+"message/deletecoversation";


    public static final String ADDFIRSTMSGPERSONALCHAT = BASEURL+"message/addfirstmsgpersonalchat/";
    public static final String ADDMESSAGE = BASEURL+"message/add_message";
    public static final String CHECKCHATROOM = BASEURL+"chathistory/checkChatRoom/";


    public static final String VERIFYPHONENUMBER = BASEURL+"verify/phone_number/";
    public static final String LOGINEMAILPWD = BASEURL+"authentication/mobilelogin";

    public static final String ADDUSER = BASEURL+"user/add";

    public static final String LISTHISTORYCHATROOM = BASEURL+"room/listroommessage?userid=";
    public static final String LISTCHATROOMNOT = BASEURL+"message/notseenmsginroom/";
    public static String logInUrl ="http://10.0.3.2:8080/ChatAskhmer/api#!/api%2Fauthentication%2Fmobilelogin/mobileLogin";
}
