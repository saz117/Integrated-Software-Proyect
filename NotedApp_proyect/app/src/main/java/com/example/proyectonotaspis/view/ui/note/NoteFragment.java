package com.example.proyectonotaspis.view.ui.note;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.AppPageMainNavigation;
import com.example.proyectonotaspis.view.LoginRegister;
import com.example.proyectonotaspis.view.MainActivity;
import com.example.proyectonotaspis.viewModel.MainActivityViewModel;
import com.example.proyectonotaspis.viewModel.ui.note.NoteViewModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//Note fragment
public class NoteFragment extends Fragment implements NoteListener {

    private NoteViewModel noteViewModel;
    private FloatingActionButton fab_add,fab_delete;
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private RecyclerView notesRec;
    NoteAdapter newAdapter;
    private MaterialCardView materialCardView;
    private NoteAdapter noteAdapter=null;
    private boolean eliminado=true;

    Context context;
    public ArrayList<Note> notes = new ArrayList<>();
    private List<Note> deleteNotes = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_note, container, false);
        context = root.getContext(); // Assign your rootView to context

        notesRec = root.findViewById(R.id.ListView_note);
        notesRec.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        notesRec = root.findViewById(R.id.ListView_note);
        notesRec.setAdapter(newAdapter);

        //setLiveDataObservers();
/*
        noteAdapter = new NoteAdapter(notes);
        notesRec.setAdapter(noteAdapter);

        Note nota = new Note ("titulo","cuerpo",null,"red");
        //mirar ejemplo audioCard
        /*
        noteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<arraylistNote>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //Boton flotante Añadir
        fab_add = root.findViewById(R.id.fab_add);
        //Lista de notas

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog();
                }
        });
        fab_delete = root.findViewById(R.id.fab_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAdapter.setEliminando(true);
                eliminado=false;
                fab_delete.hide();
                fab_add.hide();
                extendedFloatingActionButton.show();
                Toast.makeText(context, "Selecciona las notas a eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        extendedFloatingActionButton = root.findViewById(R.id.extended_fab);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminado=true;
                fab_delete.show();
                fab_add.show();
                extendedFloatingActionButton.hide();
                if(!deleteNotes.isEmpty()){
                    noteViewModel.deleteNotes(deleteNotes);
                    deleteNotes.clear();
                }else{
                    newAdapter.setEliminando(false);
                }
            }
        });
        extendedFloatingActionButton.hide();


        return root;
    }

    protected void refresh_list(ArrayList<Note> ac) {
        newAdapter = new NoteAdapter(ac,this);
        notesRec.swapAdapter(newAdapter, false);
        newAdapter.clearView();
        newAdapter.notifyDataSetChanged();

    }

    protected void refresh_list(){
        notesRec.swapAdapter(newAdapter, false);
        newAdapter.notifyDataSetChanged();

    }

    private void AddDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final Context context = builder.getContext();
        final LayoutInflater inf = LayoutInflater.from(context);
        final View view = inf.inflate(R.layout.dialog_layout, null, false);
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //hola
            }
        };
        builder.setView(view)
                .setNegativeButton("Cancelar", listener)
                .setPositiveButton("crear",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText text = (EditText) view.findViewById(R.id.username);
                                if (text.getText().toString().equals("")){
                                    Toast.makeText(getContext(), "Has de posar un nom!", Toast.LENGTH_LONG).show();
                                    AddDialog();
                                }
                                else{
                                    Intent intent = new Intent(getActivity(), NoteOpenActivity.class);
                                    intent.putExtra("Titol", text.getText().toString());
                                    intent.putExtra("Existeix",0);
                                    startActivity(intent);
                                }
                            }
                        });

        AlertDialog ale=builder.create();
        ale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ale.show();


    }


    public void setLiveDataObservers() {
        //Subscribe the activity to the observable
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        final Observer<ArrayList<Note>> observer = new Observer<ArrayList<Note>>() {
            @Override
            public void onChanged(ArrayList<Note> ac) {
                refresh_list(ac);
            }
        };

        final Observer<String> observerToast = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
            }
        };

        noteViewModel.getNotes().observe(getViewLifecycleOwner(), observer);
        noteViewModel.getToast().observe(getViewLifecycleOwner(), observerToast);

    }

    @Override
    public void onResume() {
        super.onResume();
        setLiveDataObservers();
    }
    public void sincronizar(){
        if (newAdapter!=null)
            newAdapter.notifyDataSetChanged();
    }                            

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onNoteClicked(Note note, boolean fromLongClick,List<Note> listanotas) {
        if(fromLongClick){
            eliminado = false;
            deleteNotes=listanotas;
            extendedFloatingActionButton.show();
            fab_delete.hide();
            fab_add.hide();
        }
        if(eliminado){
            Intent intent = new Intent(getContext(),NoteOpenActivity.class);
            intent.putExtra("Titol",note.getTitle());
            intent.putExtra("cuerpo",note.getBodyText());
            intent.putExtra("color",note.getColor());
            intent.putExtra("Existeix",1);
            intent.putExtra("idUser",note.getIdUser());
            DataHolder.getInstance().setData(note.getImg());
            startActivity(intent);


         }
        deleteNotes=listanotas;
    }

    @Override
    public void onNoteQuitClicked() {
        eliminado=true;
        deleteNotes.clear();
        extendedFloatingActionButton.hide();
        fab_delete.show();
        fab_add.show();
    }

}