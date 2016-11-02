package com.askhmer.chat.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by Lach Phalleak on 11/2/2016.
 */
public class MessageConvertor {

    public static String emojisDecode(String emojiStr){
        if(emojiStr.contains("#kbalhongnew#")){
            return emojiStr.replace("#kbalhongnew#","");
        }
        try {
            byte[] data = Base64.decode(emojiStr, Base64.DEFAULT);
            String newStringWithEmojis = new String(data, "UTF-8");
            return newStringWithEmojis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emojiStr;
    }

    public static String emojisEncode(String emojiStr){
        try {
            byte[] data  = emojiStr.getBytes("UTF-8");
            String  base64String = Base64.encodeToString(data, Base64.DEFAULT);
            return base64String;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return emojiStr;
    }
}
