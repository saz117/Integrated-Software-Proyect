package com.example.proyectonotaspis.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Note {

    /*
    ATTRIBUTES
     */
    private String idUser;
    private String email;
    private String title;
    private String bodyText;
    private String priority;
    private String color;
    private Bitmap img;
    private String uri_fire;
    private final DatabaseAdapter adapter = DatabaseAdapter.getInstance(DatabaseAdapter.listener);




    public Note(String title){
        this.setIdUser("default idUser");
        this.setTitle(title);
        this.setBodyText("default body text");
        this.setColor("00000000");
    }
    public Note(String Title, String text ,Bitmap foto,String color,String idUser){
        this.setTitle(Title);
        this.setBodyText(text);
        this.setImg(foto);
        this.setColor(color);
        this.setIdUser(idUser);
    }


    public Note(String Title, String text ,Bitmap foto,String color){
        this.setTitle(Title);
        this.setBodyText(text);
        this.setImg(foto);
        this.setColor(color);
    }

    public String getTitle(){
        return this.title;
    }
    public String getColor(){
        return this.color;
    }
    public Bitmap getImg(){
        return this.img;
    }
    public void setEmail(String email){this.email=email;}
    public String getEmail(){return  this.email;}


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUri_fire() {
        return uri_fire;
    }

    public void setUri_fire(String uri_fire) {
        this.uri_fire = uri_fire;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}