package com.example.proyectonotaspis.view.ui.note;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.viewModel.ui.note.NoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class NoteOpenActivity extends AppCompatActivity {
    TextView titol,text;
    ImageView buttatras,img, saveFoto;
    NoteViewModel noteViewModel;
    View botonRojo,botonAzul,botonAmarillo,botonMorado,botonNaranja;
    ImageView checkRojo,checkAzul,checkAmarillo,checkMorado,checkNaranja;
    Bitmap imagen=null;
    ScrollView scrollView;
    int existeix;
    String color =  "blue";
    String titulo;
    String idUser;

    private static final int ACCESO_FOTO_PERMITDO=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noteview_layout);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        saveFoto = findViewById(R.id.botonGuardarFoto);
        img = findViewById(R.id.image_nota);
        scrollView =findViewById(R.id.scroll);
        text = findViewById(R.id.text_nota);
        titol = findViewById(R.id.Titol);
        titulo = getIntent().getStringExtra("Titol");
        titol.setText(titulo);
        existeix= getIntent().getIntExtra("Existeix",0);
        idUser= getIntent().getStringExtra("idUser");


        iniciaMenu();





    }
    private void iniciaMenu(){
        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setSelected(true);
            }
        });
        buttatras= findViewById(R.id.bottAtrasNota);
        buttatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(existeix==0)
                        noteViewModel.createNote(titol.getText().toString(),text.getText().toString(),imagen,color);
                    else{

                        noteViewModel.editNote(titol.getText().toString(),text.getText().toString(),imagen,color,idUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                onBackPressed();
            }
        });

        saveFoto = findViewById(R.id.botonGuardarFoto);
        checkRojo=findViewById(R.id.checkColorRojo);
        checkAzul=findViewById(R.id.checkColorAzul);
        checkAmarillo=findViewById(R.id.checkColorAmarillo);
        checkMorado=findViewById(R.id.checkColorMorado);
        checkNaranja=findViewById(R.id.checkColorNaranja);
        botonRojo=findViewById(R.id.botonColorRojo);
        botonAzul=findViewById(R.id.botonColorAzul);
        botonAmarillo=findViewById(R.id.botonColorAmarillo);
        botonMorado=findViewById(R.id.botonColorMorado);
        botonNaranja=findViewById(R.id.botonColorNaranja);

        TextView menu;
        menu = findViewById(R.id.texto_menu_notas);
        //text.setText(menu.getText());                                               
        LinearLayout lin = findViewById(R.id.noteview2);

        BottomSheetBehavior<LinearLayout> bsb = BottomSheetBehavior.from(lin);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bsb.getState()!=BottomSheetBehavior.STATE_EXPANDED){
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);}
                else{
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });


        botonAzul.setOnClickListener(v -> {
            color= "blue";
            resetcolor();
            checkAzul.setImageResource(R.drawable.ic_check);
        });
        botonRojo.setOnClickListener(v -> {
            color= "red";
            resetcolor();
            checkRojo.setImageResource(R.drawable.ic_check);
        });
        botonAmarillo.setOnClickListener(v -> {
            color= "yellow";
            resetcolor();
            checkAmarillo.setImageResource(R.drawable.ic_check);
        });
        botonNaranja.setOnClickListener(v -> {
            color= "orange";
            resetcolor();
            checkNaranja.setImageResource(R.drawable.ic_check);
        });
        botonMorado.setOnClickListener(v -> {
            color= "purple";
            resetcolor();
            checkMorado.setImageResource(R.drawable.ic_check);
        });

        saveFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NoteOpenActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},ACCESO_FOTO_PERMITDO);
                }else{
                    selectImage();
                }
            }
        });
        if(existeix==1) {
            text.setText(getIntent().getStringExtra("cuerpo"));
            Bitmap bitmap = DataHolder.getInstance().getData();
            if(bitmap!=null) {
                DataHolder.getInstance().setData(null);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
            }
            color = getIntent().getStringExtra("color");
            resetcolor();
            switch (color) {
                case "blue":
                    checkAzul.setImageResource(R.drawable.ic_check);
                    break;
                case "red":
                    checkRojo.setImageResource(R.drawable.ic_check);
                    break;
                case "yellow":
                    checkAmarillo.setImageResource(R.drawable.ic_check);
                    break;
                case "purple":
                    checkMorado.setImageResource(R.drawable.ic_check);
                    break;
                case "orange":
                    checkNaranja.setImageResource(R.drawable.ic_check);
                    break;


            }
        }
    }

    private void resetcolor(){
        checkAzul.setImageResource(0);
        checkRojo.setImageResource(0);
        checkAmarillo.setImageResource(0);
        checkNaranja.setImageResource(0);
        checkMorado.setImageResource(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==ACCESO_FOTO_PERMITDO && grantResults.length>0){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{

            }
        }
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK){
            if(data!=null){
                Uri imagen_seleccionada = data.getData();

                if(imagen_seleccionada!=null){
                    try{
                        InputStream is = getContentResolver().openInputStream(imagen_seleccionada);
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        img.setImageBitmap(bm);
                        imagen=bm;
                        img.setVisibility(View.VISIBLE);
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}