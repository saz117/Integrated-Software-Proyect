package com.example.proyectonotaspis.model;

import android.graphics.Color;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

//TODO esto es un copy paste de lo que hay en NOTES
public class To_do {
    /*
    ATTRIBUTES
     */
    private String idUser;
    private String title;
    private String bodyText;
    private int color;
    private String email;
    private boolean done =false;
    private PRIORITY priority;



    private long day;
    private long month;
    private long year;
    private long hour;
    private long minute;
    private Date creationDate;

    public enum PRIORITY {
        NONE,
        LOW,
        MEDIUM,
        HIGH;

    }

    //hacer atributo statico para saber el dia en el que esta
    //private AudioNote audio;

    /*
    CONSTRUCTORS
     */

    public To_do(String title, String bodyText,PRIORITY priority,long day,long month,long year,long minute,long hour){
        this.email=email;
        this.setTitle(title);
        this.bodyText=bodyText;
        this.setPriority(priority);
        this.setDone(done);
        this.day= day;
        this.month=month;
        this.year=year;
        this.minute=minute;
        this.hour=hour;
        this.creationDate = new Date();
    }

    static public PRIORITY getPriority(String pr){
        PRIORITY priority;
        switch(pr){
            case "LOW":
                priority = PRIORITY.LOW;
                break;
            case "MEDIUM":
                priority = PRIORITY.MEDIUM;
                break;
            case "HIGH":
                priority = PRIORITY.HIGH;
                break;
            default:
                priority= PRIORITY.NONE;
        }
        return priority;
    }

    /*
    GETTERS y SETTERS
     */

    public PRIORITY getPriority(){
        return priority;
    }

    public int getColor(){
        return this.color;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getBodyText() {
        return bodyText;
    }

    public String getEmail() {
        return email;
    }

    public String getTitle(){
        return this.title;
    }

    public boolean getDone(){
        return done;
    }

    public String getFinicio() {return "finicio";}



    public long getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
    public void setDone(boolean done) {
        this.done = done;
        if (!this.done){
            this.setPriority(this.priority);
        }else
            this.color=Color.BLUE;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {this.title = title;}

    public void setDate(int day, int month, int year) {this.day = day;this.month=month;this.year=year;}


    public void setPriority(@NotNull PRIORITY priority){
        this.priority = priority;
        if (this.done)
            this.color=Color.BLUE;
        else {
            switch (priority) {
                case LOW:
                    this.setColor(Color.GREEN);
                    break;
                case MEDIUM:
                    this.setColor(Color.YELLOW);
                    break;
                case HIGH:
                    this.setColor(Color.RED);
                    break;
                case NONE:
                    this.setColor(Color.TRANSPARENT);
                    break;
            }
        }
    }

}
