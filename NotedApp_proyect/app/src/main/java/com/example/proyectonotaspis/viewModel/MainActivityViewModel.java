package com.example.proyectonotaspis.viewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectonotaspis.model.AudioNote;
import com.example.proyectonotaspis.model.DatabaseAdapter;
import com.example.proyectonotaspis.model.Note;
import com.google.firebase.firestore.DocumentSnapshot;


public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Note> arrayNotes;
    private ArrayList<Note> getArraynotes;
    private final MutableLiveData<ArrayList<Note>> mNotes;
    private MutableLiveData<ArrayList<Note>> mNotes2;
    private final MutableLiveData<String> mToast;
    private String email = null;
    private List<DocumentSnapshot> documentSnapshotsList;

    public static final String TAG = "ViewModel";

    //Constructor
    private MainActivityViewModel(){
        documentSnapshotsList = new ArrayList<>();
        mNotes = new MutableLiveData<>();
        mNotes2 = new MutableLiveData<>();
        mToast = new MutableLiveData<>();
    }
    /*
    //public getter. Not mutable , read-only
    public LiveData<ArrayList<Note>> getAudioCards(){
        return mNotes;
    }
    public Note getAudioCard(int idx){
        return mNotes.getValue().get(idx);
    }
    public void addAudioCard(String owner,String description, String localPath){
        Note ac = new Note(owner, description, localPath);
        mNotes.getValue().add(ac);
        // Inform observer.
        mNotes.setValue(mNotes.getValue());
        ac.saveCard();

    }*/

    public void setEmail(String email) {
        this.email=email;
    }



}

