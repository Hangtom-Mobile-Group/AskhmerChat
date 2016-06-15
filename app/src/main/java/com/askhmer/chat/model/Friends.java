package com.askhmer.chat.model;

/**
 * Created by Longdy on 3/26/2016.
 */
public class Friends {

    private String friName;


    private int friId;
    private String chatId;
    private String img;
    private boolean isSelected;
    private boolean isOnline;
    private int roomId;
    private String roomName;


    public int getFriId() {
        return friId;
    }

    public void setFriId(int friId) {
        this.friId = friId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFriName() {
        return friName;
    }

    public void setFriName(String friName) {
        this.friName = friName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }



    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }



    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
