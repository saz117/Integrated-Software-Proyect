package com.example.proyectonotaspis.viewModel.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectonotaspis.model.DatabaseAdapter;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.AppPageMainNavigation;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewModel extends ViewModel implements DatabaseAdapter.vmInterface{

    private MutableLiveData<ArrayList<To_do>> mTodo_row;
    private MutableLiveData<String> mToast;
    private String email = null;
    DatabaseAdapter da;

    public CalendarViewModel() throws Exception {
        mToast = new MutableLiveData<String>();
        mTodo_row = new MutableLiveData<ArrayList<To_do>>();
        da = DatabaseAdapter.getInstance(this);
        this.email= AppPageMainNavigation.email;
        String coleccion="todo";
        String key="email";
        da.SortCalendarItems("todo","email", this.email,0,0,0);
    }

    public LiveData<String> getToast(){
        return mToast;
    }
    public void getDayResults(long day,long month,long year)  {
        da.SortCalendarItems("todo","email", this.email,day,month,year);
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

}