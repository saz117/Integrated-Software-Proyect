package com.example.proyectonotaspis.view.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.ui.to_do.TodoAdapter;
import com.example.proyectonotaspis.view.ui.to_do.TodoListener;
import com.google.firebase.firestore.util.Listener;

import java.util.ArrayList;
import java.util.List;


public class TodoCalendarAdapter extends RecyclerView.Adapter<TodoCalendarAdapter.TodoViewHolder> {
    private LayoutInflater inflater;
    protected List<To_do> to_doVector;
    private TodoListener todoListener;
    private List<To_do> todoSelected= new ArrayList<>();
    private boolean eliminando= false;
    private Context context;

    public  TodoCalendarAdapter(Context context, List<To_do> to_doVector){
        this.to_doVector=to_doVector;
        //this.todoListener
    }

    @NonNull
    @Override
    public TodoCalendarAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoCalendarAdapter.TodoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoCalendarAdapter.TodoViewHolder holder, int position) {
        holder.setTodo(to_doVector.get(position));

    }
    private boolean isSelected(To_do note){
        return todoSelected.contains(note);
    }
    public void setEliminandoTodo(Boolean eliminando){
        this.eliminando=eliminando;
    }

    @Override
    public int getItemCount() {
        return to_doVector.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public CheckBox checkBox;
        public TextView textView, date;
        public ImageView imageView;
        public View color;
        LinearLayout layout;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=(LinearLayout)itemView.findViewById(R.id.todo_row_layout);
            checkBox=(CheckBox)itemView.findViewById(R.id.checkBox);
            textView=(TextView)itemView.findViewById(R.id.textView4);
            imageView = itemView.findViewById(R.id.calendar_path);
            color = itemView.findViewById(R.id.color_todo);
            date=itemView.findViewById(R.id.editTextDate2);

        }
        public void setTodo(To_do todo){
            textView.setText(todo.getTitle());
            checkBox.setChecked(todo.getDone());
            checkBox.setVisibility(View.INVISIBLE);
            if(todo.getDay()!=-1) {
                if (todo.getMonth() != -1){
                    if (todo.getYear()!=-1){
                        final String selectedDate = todo.getDay() + " / " + todo.getMonth() + " / " + todo.getYear();
                        date.setText(selectedDate);
                    }else{
                        date.setText("");}
                }else{
                    date.setText("");}

            }else{
                date.setText("");}
            switch(todo.getPriority()){
                case LOW:
                    color.setBackgroundResource(R.drawable.ic_baseline_priority_low_24);
                    break;
                case HIGH:
                    color.setBackgroundResource(R.drawable.ic_baseline_priority_high_24);
                    break;
                case MEDIUM:
                    color.setBackgroundResource(R.drawable.ic_baseline_priority_medium_24);
                    break;
                case NONE:
                    color.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    }
}
