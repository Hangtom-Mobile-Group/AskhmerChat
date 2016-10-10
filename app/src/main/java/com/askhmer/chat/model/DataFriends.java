package com.askhmer.chat.model;

/**
 * Created by Thoeurn on 4/25/2016.
 */
public class DataFriends {

    private String id;
    private String name;
    private int friend_id;

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;


    public DataFriends(String id, String name,String imageUrl){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public DataFriends(String id, String name,int friend_id){

        this.id = id;
        this.name = name;
        this.friend_id = friend_id;
    }



    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }




}
