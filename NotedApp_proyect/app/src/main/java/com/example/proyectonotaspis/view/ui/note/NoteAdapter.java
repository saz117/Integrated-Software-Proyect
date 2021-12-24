package com.example.proyectonotaspis.view.ui.note;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.view.LoginRegister;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.ArrayDeque;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notas;
    private NoteListener noteListener;
    private List<Note> notesSelected= new ArrayList<>();
    private boolean eliminando= false;

    public NoteAdapter(List<Note> notas,NoteListener notaListener) {
        this.notas = notas;
        this.noteListener= notaListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!eliminando){
                        noteListener.onNoteClicked(notas.get(position), false,notesSelected);

                }else{

                    if(!isSelected(notas.get(position))){
                        notesSelected.add(notas.get(position));
                        holder.itemView.setBackgroundResource(R.drawable.fondo_item_nota_selected);


                    }else{
                        notesSelected.remove(notas.get(position));
                        holder.itemView.setBackgroundResource(R.drawable.fondo_item_nota);
                        if(notesSelected.isEmpty()){
                            eliminando=false;
                            noteListener.onNoteQuitClicked();
                        }

                    }
                }
            }

        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(notesSelected.isEmpty()) {
                    holder.itemView.setBackgroundResource(R.drawable.fondo_item_nota_selected);
                    notesSelected.add(notas.get(position));
                    eliminando = true;
                    noteListener.onNoteClicked(notas.get(position),true,notesSelected);
                }else if(!isSelected(notas.get(position))){
                    holder.itemView.setBackgroundResource(R.drawable.fondo_item_nota_selected);
                    notesSelected.add(notas.get(position));
                    noteListener.onNoteClicked(notas.get(position),false,notesSelected);
                }else{
                    notesSelected.remove(notas.get(position));
                    holder.itemView.setBackgroundResource(R.drawable.fondo_item_nota);
                    if(notesSelected.isEmpty()) {
                        eliminando = false;
                        noteListener.onNoteQuitClicked();
                    }
                }
                    return true;

            }
        });
        holder.setNota(notas.get(position));

    }

    public void clear(){
        notas.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private boolean isSelected(Note note){
        return notesSelected.contains(note);
    }
    public void setEliminando(Boolean eliminando){
        this.eliminando=eliminando;
    }

    public void clearView() {
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, texto, color;
        ImageView imagen;
        LinearLayout layout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_item);
            texto = itemView.findViewById(R.id.cuerpo_item);
            imagen = itemView.findViewById(R.id.image_item_nota);
            color = itemView.findViewById(R.id.color_item);
            layout = itemView.findViewById(R.id.nota_vista);

        }
        void setNota(Note nota){
            titulo.setText(nota.getTitle());
            texto.setText(nota.getBodyText());
            if (nota.getImg()!=null){
                imagen.setImageBitmap(nota.getImg());
                imagen.setAdjustViewBounds(true);
                imagen.setMaxHeight(500);

            }
            if (nota.getColor()!=null) {
                switch (nota.getColor()) {
                    case "blue":
                        //color.setBackgroundColor(0xFF0000FF);  // color BLUE ->  FF opacidad 00 Rojo 00 Verde FF azul
                        //color.setBackgroundColor(0xFFE53935);  //  0x(opacidad)(Rojo)(verde)(azul)
                        color.setBackgroundColor(Color.BLUE);
                        break;
                    case "red":
                        color.setBackgroundColor(Color.RED);
                        //color.setBackgroundColor(Color.parseColor("0xe53935"));
                        break;
                    case "yellow":
                        color.setBackgroundColor(Color.YELLOW);
                        //color.setBackgroundColor(Color.parseColor("0xe53935"));
                        break;
                    case "purple":
                        color.setBackgroundColor(0xFF8E24AA); // opac FF + md_purple_600 -> 8e24aa
                        //color.setBackgroundColor(Color.CYAN);
                        //color.setBackgroundColor(Color.parseColor("0xe53935"));
                        break;
                    case "orange":
                        color.setBackgroundColor(0xFFfb8c00);  // opac FF + color md_orange_600 -> fb8c00
                        //color.setBackgroundColor(Color.parseColor("0xe53935"));
                        break;
                }
            }
        }
    }
}
