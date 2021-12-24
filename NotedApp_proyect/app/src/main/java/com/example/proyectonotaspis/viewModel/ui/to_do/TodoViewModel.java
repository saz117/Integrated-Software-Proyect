package com.example.proyectonotaspis.viewModel.ui.to_do;

import android.graphics.Bitmap;
import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectonotaspis.model.AudioNote;
import com.example.proyectonotaspis.model.DatabaseAdapter;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.AppPageMainNavigation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoViewModel extends ViewModel implements DatabaseAdapter.vmInterface{

    private MutableLiveData<ArrayList<To_do>> mTodo_row;
    private MutableLiveData<String> mToast;
    private String email = null;
    DatabaseAdapter da;

    public TodoViewModel() throws Exception {
        mToast = new MutableLiveData<String>();
        mTodo_row = new MutableLiveData<ArrayList<To_do>>();
        da = DatabaseAdapter.getInstance(this);
        this.email= AppPageMainNavigation.email;
        String coleccion="todo";
        String key="email";
        this.da.getQueryResults(coleccion,key,this.email);
    }

    public LiveData<String> getToast(){
        return mToast;
    }
    public LiveData<ArrayList<To_do>> getTodo() {return mTodo_row;}



    public void modificaChecked(To_do to_do, int pos){
        da.actualizaTodoChecked(to_do);
    }

    @Override
    public void setCollection(ArrayList checkbox) {mTodo_row.setValue(checkbox);}

    @Override
    public void setToast(String s)  {
        mToast.setValue(s);
    }

    public void editTodo(String title, String text, String prio,String idUser,long day,long month,long year,long minute,long hour){
        To_do todo = new To_do(title,text,To_do.getPriority(prio),day,month,year,minute,hour);
        todo.setEmail(email);
        System.out.println(todo.getPriority());
        todo.setIdUser(idUser);
        da.editTodo(todo,email);
    }

    public void deleteTodo(List<To_do> deleteTodo) {
        for(To_do todo :deleteTodo){
            da.deleteTodo(todo);
        }
    }

    public void createTodo(String title) {
        To_do todo = new To_do(title,null, To_do.PRIORITY.NONE,-1,-1,-1,-1,-1);
        todo.setEmail(email);
        da.saveTodo(todo,email);

    }
}