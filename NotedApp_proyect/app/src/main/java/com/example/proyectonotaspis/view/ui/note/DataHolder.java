package com.example.proyectonotaspis.view.ui.note;

import android.graphics.Bitmap;

public class DataHolder {
    private Bitmap data=null;

    public Bitmap getData() {
        return data;
    }

    public void setData(Bitmap data) {
        this.data = data;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }

}
