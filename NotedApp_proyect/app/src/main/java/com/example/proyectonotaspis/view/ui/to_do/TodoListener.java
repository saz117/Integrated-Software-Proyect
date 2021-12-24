package com.example.proyectonotaspis.view.ui.to_do;

import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.To_do;

import java.util.List;

public interface TodoListener {
    void onTodoChecked(To_do to_do, int position,boolean isCheked);
    void onTodoClicked(To_do todo, boolean fromLongClick, List<To_do> listaTodo);
    void onTodoQuitClicked();
}
