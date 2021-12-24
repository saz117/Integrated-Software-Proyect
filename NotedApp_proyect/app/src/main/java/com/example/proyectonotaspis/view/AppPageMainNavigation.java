package com.example.proyectonotaspis.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.Note;
import com.example.proyectonotaspis.model.To_do;
import com.example.proyectonotaspis.view.ui.note.NoteFragment;
import com.example.proyectonotaspis.view.ui.to_do.TodoFragment;
import com.example.proyectonotaspis.viewModel.MainActivityViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AppPageMainNavigation extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private Context parentContext;
    private AppCompatActivity mActivity;
    private boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    //ArrayList<String> notes = new ArrayList<>(); //declaramos una lista de notas

    private RecyclerView mRecyclerView;
    public static String email=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Cogemos del bundle el email dada su key
        email = getIntent().getStringExtra("email");
        /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        this.email=sharedPref.getString("email","no existe este valor");
        */

        setContentView(R.layout.activity_main4);


        //NAVEGACION en activity_main3
        /*
        parentContext = this.getBaseContext();
        mActivity = this;
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        //DrawerLayout drawer = findViewById(R.id.container);
        //NavigationView navView = findViewById(R.id.nav_view);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_notes, R.id.navigation_to_do, R.id.navigation_calendar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // TODO -> no entiendo que hace la linea de abajo. Si la pongo, me peta. Si la comento, funciona.
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        NavigationUI.setupWithNavController(navView, navController);
        

        //Lista de notas
        //ListView listView_notes = (ListView)  findViewById(R.id.ListView_note);
        //notes.add("Nota Ejemplo");
        //ArrayAdapter arrayManager = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        //listView_notes.setAdapter(arrayManager);
        */
        ////NAVEGACION en activity_main4
        parentContext = this.getBaseContext();
        mActivity = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_notes, R.id.navigation_to_do, R.id.navigation_calendar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // TODO -> no entiendo que hace la linea de abajo. Si la pongo, me peta. Si la comento, funciona.
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mRecyclerView = findViewById(R.id.recyclerview);
    }


    //Menu de la derecha (opciones del menu settings...)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    //Menu de la izquierda (opciones del menu navegacion...)
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_logout:
                Toast.makeText(AppPageMainNavigation.this, "Logout", Toast.LENGTH_SHORT).show();
                startActivity((new Intent(this, LoginRegister.class)));
                return true;

            case R.id.navigation_sync:
                Toast.makeText(AppPageMainNavigation.this, "Sync", Toast.LENGTH_SHORT).show();
                NavHostFragment fragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                assert fragment != null;
                final FragmentManager fragmentManager=fragment.getChildFragmentManager();
                Fragment notfragment=fragmentManager.getPrimaryNavigationFragment();
                if (notfragment instanceof NoteFragment){
                    ((NoteFragment) notfragment).sincronizar();
                }else if (notfragment instanceof TodoFragment){
                    ((TodoFragment) notfragment).sincronizar();
                }
                return true;

            case R.id.navigation_settings:
                Toast.makeText(AppPageMainNavigation.this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}