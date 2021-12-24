package com.example.proyectonotaspis.viewModel.ui.note;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.AudioNote;
import com.example.proyectonotaspis.model.DatabaseAdapter;
import com.example.proyectonotaspis.view.AppPageMainNavigation;
import com.example.proyectonotaspis.view.ui.note.NoteFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NoteViewModel extends ViewModel implements DatabaseAdapter.vmInterface {

    //agregar array de current date notes, si no hay dia seleccionado, las recupera todas

    //Implementar
    private MutableLiveData<String> mToast;
    private MutableLiveData<ArrayList<Note>> mNotes;
    private String email = null;
    DatabaseAdapter da;

    public NoteViewModel() throws Exception {
        mToast = new MutableLiveData<String>();
        mNotes = new MutableLiveData<ArrayList<Note>>();
        da = DatabaseAdapter.getInstance(this);
        this.email = AppPageMainNavigation.email;
        String coleccion = "notes";
        String key = "email";
        System.out.println("Hola2");
        this.da.getQueryResults(coleccion, key, this.email);
    }

    /*
    protected NoteViewModel(Parcel in) {
        }

        public static final Creator<NoteViewModel> CREATOR = new Creator<NoteViewModel>() {
            @Override
            public NoteViewModel createFromParcel(Parcel in) {
                return new NoteViewModel(in);
            }

            @Override
            public NoteViewModel[] newArray(int size) {
                return new NoteViewModel[size];
            }
        };
    */
    public LiveData<String> getToast() {
        return mToast;
    }

    public LiveData<ArrayList<Note>> getNotes() {
        return mNotes;
    }

    /*
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

     */
//TO-DO
    public void createNote(String title, String text, Bitmap imagen, String color) throws Exception {
        Note note = new Note(title, text, imagen, color);
        da.saveNote(note, email);
        /*
        ArrayList<Note> notas=  mNotes.getValue();
        notas.add(note);
        mNotes.postValue(notas);
        boolean obs=mNotes.hasActiveObservers(); */
    }

    @Override
    public void setCollection(ArrayList ac) {
        mNotes.setValue(ac);
    }

    @Override
    public void setToast(String t) {
        mToast.setValue(t);
    }


    public void deleteNotes(List<Note> deleteNotes) {
        for (Note nota : deleteNotes) {
            da.deleteNote(nota);
        }
    }

    public void editNote(String title, String text, Bitmap imagen, String color, String idUser) throws Exception {
        Note note = new Note(title, text, imagen, color, idUser);
        da.editNote(note, email);
    }
}