package com.example.proyectonotaspis.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView banner, registerUser, termsText;
    private EditText textFullName, textAge, textEmail, textPassword, textRepeatPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = ((TextView) findViewById(R.id.banner));
        banner.setOnClickListener(this);

        termsText = (TextView) findViewById(R.id.termsText);
        termsText.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerConfirmButton);
        registerUser.setOnClickListener(this);

        textFullName = (EditText) findViewById(R.id.registerFullName);
        textAge = (EditText) findViewById(R.id.registerAge);
        textEmail = (EditText) findViewById(R.id.registerEmail);
        textPassword = (EditText) findViewById(R.id.registerPassword);
        textRepeatPassword = (EditText) findViewById(R.id.repeatPassword);

    }
    /*
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, LoginRegister.class));
    }*/
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.banner:
                //startActivity(new Intent(this, MainActivity.class));
                startActivity(new Intent(this, LoginRegister.class));
                break;
            case R.id.registerConfirmButton:
                registerUser();
                break;
            case R.id.termsText:
                startActivity(new Intent(this, TermsAndConditions.class));
                break;
        }
    }


    private void registerUser() {
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();
        String passwordConfirmation = textRepeatPassword.getText().toString().trim();
        String fullName = textFullName.getText().toString().trim();
        String age = textAge.getText().toString().trim();

        if(fullName.isEmpty()){
            textFullName.setError("Es necessari un nom!");
            textFullName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            textAge.setError("Es necessari introduir la teva edat!");
            textAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            textEmail.setError("Es necessari un email!");
            textEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmail.setError("Aquest correu no es valid!");
            textEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            textPassword.setError("Introdueix la teva contrasenya!");
            textPassword.requestFocus();
            return;
        }
        if(passwordConfirmation.isEmpty()){
            textRepeatPassword.setError("Confirma la teva contrasenya!");
            textRepeatPassword.requestFocus();
            return;
        }
        if(!password.equals(passwordConfirmation)){
            textRepeatPassword.setError("Les contrasenyes son diferents!");
            textRepeatPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            textPassword.setError("La contrasenya te un minim de 6 caracters!");
            textPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(fullName, age, email);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterUser.this, "Usuari registrat correctament!", Toast.LENGTH_LONG).show();
                                //TODO quitar la linea?
                                startActivity(new Intent(RegisterUser.this, AppPageMainNavigation.class).putExtra("email", textEmail.getText().toString()));
                            }
                            else{
                                Toast.makeText(RegisterUser.this, "No s'ha pogut registrar l'usuari!" + task.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterUser.this, "No s'ha pogut registrar l'usuari!", Toast.LENGTH_LONG).show();
                }
            }
        });
        startActivity(new Intent(this, LoginRegister.class));
    }

}