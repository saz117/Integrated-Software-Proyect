package com.example.proyectonotaspis.view.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.To_do;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarGridAdapter extends ArrayAdapter {

    List<Date> dates;
    Calendar currentDate;
    List<To_do> to_dos = new ArrayList<>();
    LayoutInflater inflater;

    public CalendarGridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<To_do> to_dos) {
        super(context, R.layout.single_cell_layout);

        this.dates = dates;
        this.currentDate = currentDate;
        this.to_dos = to_dos;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int dayNumb = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) +1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);


        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);

        }

        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundResource(R.drawable.fondo_calendario);

        }else view.setBackgroundResource(R.drawable.fondo_calendario2);

        TextView dayNumber = view.findViewById(R.id.calendar_day);
        dayNumber.setText(String.valueOf(dayNumb));

        return view;
    }
    public List<Integer> getDay(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int dayNumb = dateCalendar.get(Calendar.DAY_OF_MONTH);

        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        List<Integer> lista = new ArrayList<>();
        lista.add(dayNumb);
        lista.add(dateCalendar.get(Calendar.MONTH) +1);
        lista.add(dateCalendar.get(Calendar.YEAR));


        return lista;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }
}
