package com.askhmer.chat.model;

public class Message {

	private int msgId;
	private int roomId;
	private int userId;
	private String msgDate;
	private String msgTime;
//	private boolean isSelf;
	private String stickerUrl;
	private String userProfile;
	private String userName;
	private String message;

	public Message() {

	}

	public Message(String userName, String message, int userId) {
		this.userName = userName;
		this.message = message;
		this.userId = userId;
//		this.isSelf = isSelf;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

//	public boolean isSelf() {
//		return isSelf;
//	}
//
//	public void setIsSelf(boolean isSelf) {
//		this.isSelf = isSelf;
//	}

	public String getStickerUrl() {
		return stickerUrl;
	}

	public void setStickerUrl(String stickerUrl) {
		this.stickerUrl = stickerUrl;
	}

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
}
