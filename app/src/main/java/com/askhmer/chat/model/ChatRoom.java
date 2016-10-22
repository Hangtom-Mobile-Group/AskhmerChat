package com.askhmer.chat.model;

/**
 * Created by Longdy on 10/10/2016.
 */
public class ChatRoom {

    private String currentMsg;
    private String msgDate;
    private String msgTime;

    private String chatId;
    private String imgUrl;
    private String roomName;
    private String friendName;
    private String friendProfileUrl;
    private String memberID;

    private int roomId;
    private int totalRow;
    private int totalPage;
    private int friId;
    private int counterMember;
    private int counterMsgNotSeen;

    private boolean isSelected;
    private boolean isOnline;
    private boolean isSeen;
    private boolean isGroup;

    public String getCurrentMsg() {
        return currentMsg;
    }

    public void setCurrentMsg(String currentMsg) {
        this.currentMsg = currentMsg;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendProfileUrl() {
        return friendProfileUrl;
    }

    public void setFriendProfileUrl(String friendProfileUrl) {
        this.friendProfileUrl = friendProfileUrl;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getFriId() {
        return friId;
    }

    public void setFriId(int friId) {
        this.friId = friId;
    }

    public int getCounterMember() {
        return counterMember;
    }

    public void setCounterMember(int counterMember) {
        this.counterMember = counterMember;
    }

    public int getCounterMsgNotSeen() {
        return counterMsgNotSeen;
    }

    public void setCounterMsgNotSeen(int counterMsgNotSeen) {
        this.counterMsgNotSeen = counterMsgNotSeen;
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

    public boolean isSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
