package com.example.proyectonotaspis.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectonotaspis.R;
import com.example.proyectonotaspis.viewModel.MainActivityViewModel;
/*
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
*/

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class LoginRegister extends AppCompatActivity implements View.OnClickListener {

    /*
    private static final int GOOGLE_SING_IN = 100;
    public static final String TAG = "LoginRegister";

    private Context parentContext;
    private AppCompatActivity mActivity;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private MediaRecorder recorder;
    private boolean isRecording = false;
    String fileName;

 */
    private MainActivityViewModel viewModel;
    //private RecyclerView mRecyclerView;


    private TextView registre;
    private EditText textEmail, textPassword;
    private Button logIn;
    private ImageView img;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registre = findViewById(R.id.button_register);
        registre.setOnClickListener(this);

        logIn = findViewById(R.id.button_login);
        logIn.setOnClickListener(this);

        textEmail = findViewById(R.id.login_email);
        textPassword = findViewById(R.id.login_password);

        mAuth = FirebaseAuth.getInstance();

        img = findViewById(R.id.imageView);
        img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            case R.id.button_login:
                userLogin();
                break;

            case R.id.imageView:
                textEmail.setText("p@gmail.com");
                textPassword.setText("123456");
                userLogin();
                break;

        }

    }

    private void userLogin() {
        String email = textEmail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if (email.isEmpty()) {
            textEmail.setError("Es necessari un email!");
            textEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textEmail.setError("Aquest correu no es valid!");
            textEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            textPassword.setError("Introdueix la teva contrasenya!");
            textPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            textPassword.setError("La contrasenya te un minim de 6 caracters!");
            textPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    try {
                        saveDataDB();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Creamos el bundle para pasarle el key email con su valor
                    startActivity(new Intent(LoginRegister.this, AppPageMainNavigation.class).putExtra("email", textEmail.getText().toString()));

                    Toast.makeText(LoginRegister.this, "Sesio iniciada!", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginRegister.this, "No s'ha pogut loguejar l'usuari!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void saveDataDB() throws Exception {
        /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email",textEmail.getText().toString());
        editor.commit();
        */
        /*
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("pasword",textPassword.getText().toString());
        //db.collection("users").document(textEmail.getText().toString()).set(map);
        Query q = db.collection("notes").whereEqualTo("email",textEmail.getText().toString());
        q.get();
*/

        //asynchronously retrieve multiple documents
        /*ApiFuture<QuerySnapshot> future = db.collection("notes").whereEqualTo("email", textEmail.getText().toString()).get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (DocumentSnapshot document : documents) {
            System.out.println(document.getId() + " => " + document.toObject(City.class));
        }*/
    }




    /*
    private void googleLogin(){
        GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleClient = GoogleSignIn.getClient(this, googleConf);
        startActivityForResult(googleClient.getSignInIntent(), GOOGLE_SING_IN);
    }


    //Verificación con google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SING_IN){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account.getIdToken());
                    //AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    //FirebaseAuth.getInstance().signInWithCredential(credential);
                    //TODO
                }
            } catch (ApiException e) {
                e.printStackTrace();
                // Google Sign In failed, update UI appropriately
                Log.w(TAG,"Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //TODO
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
*/



      /*

      public void setLiveDataObservers() {
        //Subscribe the activity to the observable
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        final Observer<ArrayList<AudioNote>> observer = new Observer<ArrayList<AudioNote>>() {
            @Override
            public void onChanged(ArrayList<AudioNote> ac) {
                CustomAdapter newAdapter = new CustomAdapter(parentContext, ac, (CustomAdapter.playerInterface) mActivity);
                mRecyclerView.swapAdapter(newAdapter, false);
                newAdapter.notifyDataSetChanged();

            }
        };

        final Observer<String> observerToast = new Observer<String>() {
            @Override
            public void onChanged(String t) {
                Toast.makeText(parentContext, t, Toast.LENGTH_SHORT).show();
            }
        };

        viewModel.getAudioCards().observe(this, observer);
        viewModel.getToast().observe(this, observerToast);

    }

    private void startRecording() {
        Log.d("startRecording", "startRecording");

        recorder = new MediaRecorder();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss", Locale.GERMANY);
        String date = df.format(Calendar.getInstance().getTime());
        fileName =  getExternalCacheDir().getAbsolutePath()+ File.separator +date+".3gp";
        Log.d("startRecording", fileName);

        recorder.setOutputFile(fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.d("startRecording", "prepare() failed");
        }

        recorder.start();
        isRecording = true;
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;

    }

    public void startPlaying(int recyclerItem) {

        try {
            MediaPlayer player = new MediaPlayer();

            fileName = viewModel.getAudioCard(recyclerItem).getAddress();
            Log.d("startPlaying", fileName);
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.d("startPlaying", "prepare() failed");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    public void showPopup(View anchorView) {

        View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
        PopupWindow popupWindow = new PopupWindow(popupView, 800, 600);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

        // Initialize objects from layout
        TextInputLayout saveDescr = popupView.findViewById(R.id.note_description);
        Button saveButton = popupView.findViewById(R.id.save_button);
        saveButton.setOnClickListener((v) -> {
            String text = saveDescr.getEditText().getText().toString();
            viewModel.addAudioCard(text, fileName, "");
            popupWindow.dismiss();
        });
    }
*/

}
