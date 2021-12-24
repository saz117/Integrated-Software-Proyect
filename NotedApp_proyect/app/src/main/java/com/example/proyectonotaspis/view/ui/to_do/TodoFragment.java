package com.example.proyectonotaspis.view.ui.to_do;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.To_do;
//import com.example.proyectonotaspis.view.CustomAdapter;
import com.example.proyectonotaspis.view.ui.note.NoteAdapter;
import com.example.proyectonotaspis.view.ui.note.NoteListener;
import com.example.proyectonotaspis.view.ui.note.NoteOpenActivity;
import com.example.proyectonotaspis.viewModel.ui.note.NoteViewModel;
import com.example.proyectonotaspis.viewModel.ui.to_do.TodoViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TodoFragment extends Fragment implements TodoListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TodoAdapter todoAdapter=null;
    private TodoViewModel todoViewModel;
    private FloatingActionButton fab_add,fab_delete;
    private ExtendedFloatingActionButton extendedFloatingActionButton;
    private Context parentContext;
    private List<To_do> deleteTodo = new ArrayList<>();
    private boolean eliminado=true;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todo, container, false);
        parentContext = root.getContext();
        ///////////////
        recyclerView = root.findViewById(R.id.RecyclerView_todo);
        //todoAdapter = new TodoAdapter(parentContext,listatodo);
        //recyclerView.setAdapter(todoAdapter);
        layoutManager = new LinearLayoutManager(parentContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
            }
        });
        //////////////
        //Boton flotante Añadir
        fab_add=root.findViewById(R.id.fab_add2);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog();
            }
        });
        fab_delete = root.findViewById(R.id.fab_delete2);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoAdapter.setEliminandoTodo(true);
                eliminado=false;
                fab_delete.hide();
                fab_add.hide();
                extendedFloatingActionButton.show();
                Toast.makeText(root.getContext(), "Selecciona las notas a eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        extendedFloatingActionButton = root.findViewById(R.id.extended_fab2);
        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminado=true;
                fab_delete.show();
                fab_add.show();
                extendedFloatingActionButton.hide();
                if(!deleteTodo.isEmpty()){
                    todoViewModel.deleteTodo(deleteTodo);
                    deleteTodo.clear();
                }else{
                    todoAdapter.setEliminandoTodo(false);
                }
            }
        });
        extendedFloatingActionButton.hide();
        refresh_list();
        return root;
    }

    private void cargaAdapteer(ArrayList<To_do> to_do){
        todoAdapter = new TodoAdapter(parentContext,to_do,this);
        recyclerView.swapAdapter(todoAdapter, false);
        todoAdapter.notifyDataSetChanged();
    }

    protected void refresh_list() {
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        final Observer<ArrayList<To_do>> observer = new Observer<ArrayList<To_do>>() {
            @Override
            public void onChanged(ArrayList<To_do> to_do) {
                cargaAdapteer(to_do);
            }
        };
        final Observer<String> observerToast = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                Toast.makeText(parentContext, t, Toast.LENGTH_SHORT).show();
            }
        };
        todoViewModel.getTodo().observe(getViewLifecycleOwner(), observer);
        todoViewModel.getToast().observe(getViewLifecycleOwner(), observerToast);
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
                                else if("hola".equals(text.getText().toString())){
                                    Toast.makeText(getContext(), "Ja existeix aquest nom!", Toast.LENGTH_LONG).show();
                                    AddDialog();
                                }
                                else{
                                    /*   No funciona con fragment se ve el fragmento que hay debajo
                                    TodoOpenFragment fragment=TodoOpenFragment.newInstance(titol);
                                    FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
                                    fragmentTransaction.add(R.id.nav_host_fragment,fragment);
                                    fragmentTransaction.commit(); */
                                    todoViewModel.createTodo(text.getText().toString());
                                    //Intent intent = new Intent(getActivity(), TodoOpenActivity.class);
                                    //intent.putExtra("Titol", text.getText().toString());
                                    // startActivity(intent);
                                }


                            }
                        });

        AlertDialog ale=builder.create();
        ale.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ale.show();
    }

    public void sincronizar(){
        if (todoAdapter!=null)
            todoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTodoChecked(To_do to_do, int pos,boolean isCheked) {
        if (todoViewModel!=null)
            todoViewModel.modificaChecked(to_do,pos);

    }

    @Override
    public void onTodoClicked(To_do todo, boolean fromLongClick, List<To_do> listaTodo) {
        if(fromLongClick){
            eliminado = false;
            deleteTodo=listaTodo;
            extendedFloatingActionButton.show();
            fab_delete.hide();
            fab_add.hide();
        }
        if(eliminado){
            Intent intent = new Intent(getContext(),TodoOpenActivity.class);
            intent.putExtra("Titol",todo.getTitle());
            intent.putExtra("texto",todo.getBodyText());
            intent.putExtra("priority",todo.getPriority().toString());
            intent.putExtra("Existeix",1);
            intent.putExtra("idUser",todo.getIdUser());
            intent.putExtra("day",todo.getDay());
            intent.putExtra("month",todo.getMonth());
            intent.putExtra("year",todo.getYear());
            intent.putExtra("minute",todo.getMinute());
            intent.putExtra("hour",todo.getHour());
            startActivity(intent);


        }
        deleteTodo=listaTodo;
    }

    @Override
    public void onTodoQuitClicked() {
        eliminado=true;
        deleteTodo.clear();
        extendedFloatingActionButton.hide();
        fab_delete.show();
        fab_add.show();
    }
}