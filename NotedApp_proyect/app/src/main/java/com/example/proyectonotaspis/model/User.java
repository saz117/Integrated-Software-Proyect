package com.example.proyectonotaspis.model;

import java.util.ArrayList;

public class User {
    public static User currentUser;
    private String fullName, age, email;
    private ArrayList<Note> userNotes;

    public User(){
        this.fullName = "default name";
        this.age = "default age";
        this.email = "default email";
        this.userNotes = new ArrayList<>();
    }

    public User(String fullName, String age, String email){
        //coger id unica del user -> puede ser el email
        this.age = age;
        this.fullName = fullName;
        this.email = email;
        this.currentUser=this;
        this.userNotes = new ArrayList<>();
    }

    /*
    SETTERS
     */
    public void addNote(Note note){
        this.userNotes.add(note);
    }

    /*
    GETTERS
     */
    public ArrayList<Note> getUserNotes(){
        return this.userNotes;
    }

    public Note getNoteByTitle(String searchTitle){
        for(Note note : this.userNotes){
            if(note.getTitle().equals(searchTitle)){
                return note;
            }
        }
        return null;
    }

}
