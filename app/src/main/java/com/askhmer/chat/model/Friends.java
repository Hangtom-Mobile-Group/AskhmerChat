package com.askhmer.chat.model;

/**
 * Created by Longdy on 3/26/2016.
 */
public class Friends {
    private int friId;
    private String chatId;
    private String friName;
    private int img;
    private boolean isSelected;

//Friend Longdy
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
