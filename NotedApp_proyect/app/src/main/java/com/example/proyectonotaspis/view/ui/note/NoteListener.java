package com.example.proyectonotaspis.view.ui.note;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.example.proyectonotaspis.model.Note;

import java.util.List;

public interface NoteListener {
    void onNoteClicked(Note note,boolean fromLongClick,List<Note> listnotas);
    void onNoteQuitClicked();


}
