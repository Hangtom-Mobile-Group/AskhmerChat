package com.askhmer.chat.model;

/**
 * Created by Thoeurn on 4/25/2016.
 */
public class DataFriends {

    private String id;
    private String name;

    public DataFriends(String id, String name){

        this.id = id;
        this.name = name;
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
}
