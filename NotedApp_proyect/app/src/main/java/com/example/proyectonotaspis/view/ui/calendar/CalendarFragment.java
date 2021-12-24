package com.example.proyectonotaspis.view.ui.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.ui.to_do.TodoAdapter;
import com.example.proyectonotaspis.viewModel.ui.calendar.CalendarViewModel;
import com.example.proyectonotaspis.viewModel.ui.to_do.TodoViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Clase calendario
public class CalendarFragment extends Fragment {
    //ImageButton backBtn, forwardBtn;
    ImageView backBtn, forwardBtn;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    TextView currentDate;
    GridView gridView;
    private TodoCalendarAdapter todoCalendarAdapter;
    private CalendarViewModel calendarViewModel;
    private static final int MAX_CALENDAR_DAYS = 37;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    private int month,year;
    Context parentContext;

    CalendarGridAdapter myGridAdapter;

    List<Date> dates = new ArrayList<>();
    List<To_do> to_dos = new ArrayList<>();

    //todo si tengo que tomar algo de la Database, hago view.model y delego
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.calendar_layout, container, false);
        parentContext = root.getContext();
        recyclerView = root.findViewById(R.id.listaCalendario);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
            }
        });

        backBtn = root.findViewById(R.id.calendar_move_back);
        forwardBtn = root.findViewById(R.id.calendar_move_forward);
        currentDate = root.findViewById(R.id.currentDate);
        gridView = root.findViewById(R.id.grid_view);

        //final TextView textView = root.findViewById(R.id.text_calendar);
        /*
        CalendarViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        */
        refresh_calendar();
        //para que tenga una fecha al iniciar

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                refresh_calendar();
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                refresh_calendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Integer> data = myGridAdapter.getDay(position, view, gridView);
                calendarViewModel.getDayResults(data.get(0), data.get(1), data.get(2));
                refresh_calendar();

            }

///calendarViewModel.getDayResults(myGridAdapter.getDay(position,view,gridView).get(0),myGridAdapter.getDay(position,view,gridView).get(1),myGridAdapter.getDay(position,view,gridView).get(2));


        });

        refresh_list();
        return root;
    }
    private void cargaAdapteer(ArrayList<To_do> to_do){
        todoCalendarAdapter = new TodoCalendarAdapter(parentContext,to_do);
        recyclerView.swapAdapter(todoCalendarAdapter, true);
        todoCalendarAdapter.notifyDataSetChanged();
    }
    protected void refresh_list() {
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        final Observer<ArrayList<To_do>> observer = new Observer<ArrayList<To_do>>() {
            @Override
            public void onChanged(ArrayList<To_do> to_do) {
                cargaAdapteer(to_do);
            }
        };
        calendarViewModel.getTodo().observe(getViewLifecycleOwner(), observer);
    }

    private void refresh_calendar(){
        String auxiliar = dateFormat.format(calendar.getTime());
        currentDate.setText(auxiliar);

        //rellenamos el mes
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);
        int firstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayofMonth);

        getTo_dosOfMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while(dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        myGridAdapter = new CalendarGridAdapter(getContext(), dates,calendar,to_dos);

    }

    private void getTo_dosOfMonth(String month, String year){
        to_dos.clear();
        //calendarViewModel = new calendarViewModel();
        //Database database = Database.get();


    }

    private void save_event(String event, String time, String month, String year, String date){

    }
}