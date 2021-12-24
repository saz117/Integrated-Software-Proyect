package com.example.proyectonotaspis.model;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;


public class AudioNote {
    /*TODO descomentar*/
    private String noteId;
    private final String audioDesc;
    private final String address;
    private final String owner;
    private final DatabaseAdapter adapter =DatabaseAdapter.getInstance(DatabaseAdapter.listener);

    // Description, uri, duration, format, owner
    public AudioNote(String description, String localPath, String owner) {
        //this.noteId = id;
        this.audioDesc = description;
        this.address = localPath;
        this.owner = owner;
        UUID uuid = UUID.randomUUID();
        this.noteId = uuid.toString();
    }
    public String getAddress () {
        return this.address;
    }
    public String getDescription () {
        return this.audioDesc;
    }

    private void setNoteId (String id) {
        this.noteId = id;
    }

    public void saveCard() {

        Log.d("saveCard", "saveCard-> saveDocument");
        adapter.saveDocumentWithFile(this.noteId, this.audioDesc, this.owner,this.address);
    }

    public AudioNote getCard() {
        // ask database and if true, return audioCard
        HashMap<String, String> hm = adapter.getDocuments();
        Boolean answer = false;
        if (hm != null) {
            AudioNote ac = new AudioNote(hm.get("description"), "", hm.get("owner"));
            ac.setNoteId(hm.get("noteid"));
            return ac;
        } else {
            return null;
        }
    }
    /*TODO descomentar*/
}
