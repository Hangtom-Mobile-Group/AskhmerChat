package com.askhmer.chat.model;

/**
 * Created by Thoeurn on 4/25/2016.
 */
public class DataFriends {

    private String id;
    private String name;
    private int friend_id;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String gender;

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;


    public DataFriends(String id, String name,String imageUrl,int friend_id,String gender){
        this.setId(id);
        this.name = name;
        this.imageUrl = imageUrl;
        this.friend_id=friend_id;
        this.gender=gender;
    }

    public DataFriends(String id, String name,int friend_id){

        this.id = id;
        this.name = name;
        this.friend_id = friend_id;
    }



    public void setId(String id){
        if(id==null){
            this.id="No";
            return;
        }
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
