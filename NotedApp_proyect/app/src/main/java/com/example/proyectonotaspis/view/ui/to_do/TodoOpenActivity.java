package com.example.proyectonotaspis.view.ui.to_do;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.viewModel.ui.to_do.TodoViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TodoOpenActivity extends AppCompatActivity {

    ImageView buttatras;
    TextView titol,calendarText,hourText;
    View botonHigh,botonMedium,botonLow;
    ImageView checkHigh,checkMedium,checkLow,checkNone,calendarButton,horaButton;
    EditText subtitle;
    private TodoViewModel todoViewModel;


    LocalDateTime f_inicio;
    private String titulo;
    private String prioridadString;
    private String texto;
    private String idUser;
    private long day;
    private long month;
    private long year;
    private long hour;
    private long minute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_edit);
        titulo = getIntent().getStringExtra("Titol");
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        buttatras=findViewById(R.id.bottAtrasTodo);
        titol=findViewById(R.id.todoTitle);
        titol.setText(titulo);
        checkNone = findViewById(R.id.priorityNone);
        subtitle = findViewById(R.id.todoSubtitle);
        checkHigh=findViewById(R.id.checkHigh);
        checkMedium=findViewById(R.id.checkMedium);
        checkLow=findViewById(R.id.checkLow);
        botonHigh=findViewById(R.id.priorityHigh);
        botonMedium=findViewById(R.id.priorityMedium);
        botonLow=findViewById(R.id.priorityLow);
        calendarButton=findViewById(R.id.calendarButton);
        horaButton = findViewById(R.id.hourButton);
        calendarText=findViewById(R.id.addCalendarset);
        hourText=findViewById(R.id.addCalendarset2);

        idUser=getIntent().getStringExtra("idUser");
        prioridadString= getIntent().getStringExtra("priority");
        texto=getIntent().getStringExtra("texto");
        subtitle.setText(texto);
        day=getIntent().getLongExtra("day",-1);
        month=getIntent().getLongExtra("month",-1);
        year=getIntent().getLongExtra("year",-1);
        hour=getIntent().getLongExtra("hour",-1);
        minute=getIntent().getLongExtra("minute",-1);
        if(day!=-1) {
            if (month != -1){
                if (year!=-1){
                    final String selectedDate = day + " / " + month + " / " + year;
                    calendarText.setText(selectedDate);
                }
            }
        }
        if (hour != -1) {
            if(minute!=-1){
                if(minute<10){
                    final String selectedHour = hour + " : 0" + minute;
                    hourText.setText(selectedHour);
                }else{
                    final String selectedHour = hour + " : " + minute;
                    hourText.setText(selectedHour);
                }
            }
        }



        buttatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoViewModel.editTodo(titol.getText().toString(),subtitle.getText().toString(),prioridadString,idUser,day,month,year,minute,hour);
                onBackPressed();
            }
        });
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        horaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        botonHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prioridadString="HIGH";
                checkHigh.setImageResource(R.drawable.ic_check);
                checkLow.setImageResource(0);
                checkMedium.setImageResource(0);
            }
        });
        botonLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prioridadString="LOW";
                checkLow.setImageResource(R.drawable.ic_check);
                checkMedium.setImageResource(0);
                checkHigh.setImageResource(0);
            }
        });
        botonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prioridadString="MEDIUM";
                checkMedium.setImageResource(R.drawable.ic_check);
                checkHigh.setImageResource(0);
                checkLow.setImageResource(0);
            }
        });
        checkNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prioridadString="NONE";
                checkLow.setImageResource(0);
                checkHigh.setImageResource(0);
                checkMedium.setImageResource(0);

            }
        });
        switch(prioridadString){
            case "LOW":
                botonLow.callOnClick();
                break;
            case "MEDIUM":
                botonMedium.callOnClick();
                break;
            case "HIGH":
                botonHigh.callOnClick();
                break;
            default:
                Toast.makeText(this, "F", Toast.LENGTH_SHORT).show();

        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int any, int mes, int dia) {
                // +1 because January is zero
                final String selectedDate = dia + " / " + (mes+1) + " / " + any;
                day = dia;
                month=mes+1;
                year=any;
                calendarText.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog() {
        TimePickerFragment newFragment= TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int min) {
            String minu="";
            if(min<=0){
                minu= "0";
            }
            final String selectedHour = hourOfDay + " : " + minu+min;
            hour=hourOfDay;
            minute=min;
            hourText.setText(selectedHour);
        }
    });
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

}
