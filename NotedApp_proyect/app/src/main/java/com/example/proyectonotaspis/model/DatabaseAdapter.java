package com.example.proyectonotaspis.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.LocaleData;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.common.util.ScopeUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


public class DatabaseAdapter {

    public static final String TAG = "DatabaseAdapter";

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private ArrayList<Object> ac_pr;

    public static vmInterface listener;

    //Singleton
    private static DatabaseAdapter databaseAdapter = null;
    public static DatabaseAdapter getInstance(vmInterface listener1) {
        if (databaseAdapter==null) {
            databaseAdapter=new DatabaseAdapter(listener1);
        }else listener=listener1;
        return databaseAdapter;
    }

    public DatabaseAdapter(vmInterface listener){
        this.listener = listener;
        databaseAdapter = this;
        //FirebaseFirestore.setLoggingEnabled(true);
        //initFirebase();
    }


    public Bitmap getBitmap(String idUser) {
            db.collection("notes").document(idUser)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            System.out.println("Borrado");
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("No  Borrado");
                            Log.w(TAG, "Error writing document", e);
                        }});

        return null;
    }

    public interface vmInterface{
        void setCollection(ArrayList<Object> ac);
        void setToast(String s);
    }


    public void getQueryResults(String coleccion, String key, String email) {
        // [START fs_get_multiple_docs]
        // [START firestore_data_query]
        //asynchronously retrieve multiple documents

        Task<QuerySnapshot> future = db.collection(coleccion).whereEqualTo(key, email).get();
        // future.get() blocks on response

        future.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Lectura de datos finalizada erroneamente ", task.getException());
                    return;
                }
                ac_pr=new ArrayList<Object>();
                List<DocumentSnapshot> documents = future.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    System.out.println(document.getId() + " => " + document.getData());
                    if (coleccion.equals("notes")){
                        Note note = new Note (document.getString("title"),
                                (String)document.get("bodyText"),
                                (Bitmap)document.get("img"),
                                (String)document.get("color"));
                        note.setEmail(email);
                        note.setIdUser(document.getId());
                        note.setUri_fire((String)document.get("uri_fire"));
                        if (note.getUri_fire()!=null)
                            recupera_imagen(note);
                        else
                            ac_pr.add(note);
                    }else if (coleccion.equals("todo")){
                        Boolean done=(Boolean) document.get("done");
                        document.get("title");
                        document.get("bodyText");
                        document.get("color");

                        To_do todo = new To_do(document.getString("title"),
                                (String)document.get("bodyText"),
                                To_do.getPriority((String)document.get("priority")),(long)document.get("day"),
                                (long)document.get("month"),
                                (long)document.get("year"),
                                (long)document.get("minute"),
                                (long)document.get("hour")
                                );
                        todo.setIdUser(document.getId());
                        todo.setDone(done);
                        todo.setEmail(email);
                        System.out.println(todo.getDay());
                        ac_pr.add(todo);
                    }

                }
                listener.setCollection(ac_pr);
                // [END firestore_data_query]
                // [END fs_get_multiple_docs]

            }
        });
    }

    private void recupera_imagen(Note note) {
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child(note.getUri_fire());
        final long ONE_MEGABYTE = 1024 * 1024;
        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                note.setImg(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                ac_pr.add(note);
                // Data for "images/island.jpg" is returns, use this as needed
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.w(TAG, "no existe la imagen: "+note.getUri_fire(), null);
                listener.setToast("no existe la imagen: "+note.getUri_fire());
            }
        });

    }
    public void SortCalendarItems(String coleccion, String key, String email,long day,long month,long year)  {
        // [START fs_get_multiple_docs]
        // [START firestore_data_query]
        //asynchronously retrieve multiple documents

        Task<QuerySnapshot> future = db.collection(coleccion).whereEqualTo(key, email).get();
        ArrayList<To_do> lista=new ArrayList<>();
        lista.clear();
        future.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Lectura de datos finalizada erroneamente ", task.getException());
                    return;
                }
                ac_pr=new ArrayList<Object>();
                List<DocumentSnapshot> documents = future.getResult().getDocuments();
                for (DocumentSnapshot document : documents) {
                    System.out.println(document.getId() + " => " + document.getData());


                    Boolean done = (Boolean) document.get("done");

                    System.out.println((long) document.get("day") == (long)day);

                    if ((long) document.get("day") == (long)day) {
                        System.out.println(((long) document.get("month") == (long)month)+"1");
                        if ((long) document.get("month") == (long)month) {
                            System.out.println((long) document.get("year") == (long)year);
                            if ((long) document.get("year") == (long)year) {
                                To_do todo = new To_do(document.getString("title"),
                                        (String) document.get("bodyText"),
                                        To_do.getPriority((String) document.get("priority")), (long) document.get("day"),
                                        (long) document.get("month"),
                                        (long) document.get("year"),
                                        (long) document.get("minute"),
                                        (long) document.get("hour")
                                );
                                todo.setIdUser(document.getId());
                                todo.setDone(done);
                                todo.setEmail(email);

                                ac_pr.add(todo);
                            }
                        }
                    }
                } listener.setCollection(ac_pr);
            }
        });
    }

    public List<DocumentSnapshot> getAllDocuments() throws Exception {
        // [START fs_get_all_docs]
        // [START firestore_data_get_all_documents]
        //asynchronously retrieve all documents
        Task<QuerySnapshot> future = db.collection("notes").get();
        // future.get() blocks on response
        List<DocumentSnapshot> documents = future.getResult().getDocuments();
        for (DocumentSnapshot document : documents) {
            System.out.println(document.getId() + " => " + document.getData());
        }
        // [END firestore_data_get_all_documents]
        // [END fs_get_all_docs]
        return documents;
    }






    public void initFirebase(){

        user = mAuth.getCurrentUser();

        if (user == null) {
            listener.setToast("Authentication with null user. TODO-DataBase Adapter");

            mAuth.signInAnonymously()
                    .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                listener.setToast("Authentication successful.");
                                user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
                                listener.setToast("Authentication failed.");

                            }
                        }
                    });
        }
        else{
            listener.setToast("Authentication with current user.");

        }
    }


    public void getCollection(){
        //falta hacer un Query para hacer los mismo pero con los dias
        //este metodo recibe una fecha, y recuperamos todas las nochas por esa fecha.
        /*
        Log.d(TAG,"updateaudioCards");
        DatabaseAdapter.db.collection("notasPruebas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Note> retrieved_ac = new ArrayList<Note>() ;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                retrieved_ac.add(new Note( document.getString("email"), document.getString("titulo"), document.getString("priority")));
                            }
                            listener.setCollection(retrieved_ac); //lama a interfaz

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        */
    }




    public void saveDocument (String id, String description, String userid, String url) {

        // Create a new user with a first and last name
        Map<String, Object> note = new HashMap<>();
        note.put("id", id);
        note.put("description", description);
        note.put("userid", userid);
        note.put("url", url);

        Log.d(TAG, "saveDocument");
        // Add a new document with a generated ID
        db.collection("audioCards")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }








    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveNote (Note nota, String email) {

        Map<String, Object> note = new HashMap<>();
        note.put("email", email);
        note.put("title",nota.getTitle());
        note.put("bodyText", nota.getBodyText());
        note.put("color", nota.getColor());
        note.put("f_creacion", LocalDateTime.now());

        if (nota.getImg()!=null) {
            String tit=nota.getTitle();
            String tit_sin_esp=tit.replace(' ','_');
            String uri_fire="images/"+tit_sin_esp+".jpg";
            note.put("uri_fire",uri_fire);
            StorageReference storageRef = storage.getReference();
            StorageReference imagenRef = storageRef.child(uri_fire);
            Bitmap bitmap = nota.getImg();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imagenRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle unsuccessful uploads
                    Log.w(TAG, "Error añadiendo imagen al document", e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Log.d(TAG, "DocumentSnapshot añadido ");
                    //Uri downloadUri = taskSnapshot.getUploadSessionUri();
                    //saveDocument("foto", "acabo de añadir una foto", email, downloadUri.toString());
                }
            });
        }

        Log.d(TAG, "saveDocument");
        // Add a new document with a generated ID
        db.collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot añadido con ID: " + documentReference.getId());
                        nota.setIdUser(documentReference.getId());
                        ac_pr.add(nota);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error añadiendo document", e);
                    }
                });


    }


    public void saveTodo (To_do todo, String email) {

        Map<String, Object> wtodo = new HashMap<>();
        wtodo.put("email", email);
        wtodo.put("title",todo.getTitle());
        wtodo.put("bodyText", todo.getBodyText());
        wtodo.put("color", todo.getColor());
        wtodo.put("f_creacion", new Timestamp(new Date()));
        wtodo.put("done",todo.getDone());
        wtodo.put("priority",todo.getPriority());
        wtodo.put("day",todo.getDay());
        wtodo.put("month",todo.getMonth());
        wtodo.put("year",todo.getYear());
        wtodo.put("minute",todo.getMinute());
        wtodo.put("hour",todo.getHour());

        Log.d(TAG, "saveDocument");
        // Add a new document with a generated ID
        db.collection("todo")
                .add(wtodo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot añadido con ID: " + documentReference.getId());
                        todo.setIdUser(documentReference.getId());
                        ac_pr.add(todo);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error añadiendo document", e);
                    }
                });


    }

    public void actualizaTodoChecked(To_do to_do){
        db.collection("todo").document(to_do.getIdUser())
                .update("done",to_do.getDone())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
    public void deleteNote(Note note){
        db.collection("notes").document(note.getIdUser())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Borrado");
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("No  Borrado");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void deleteTodo(To_do todo){
        db.collection("todo").document(todo.getIdUser())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Borrado");
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("No  Borrado");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void editNote(Note nota, String email) {
        Map<String, Object> note = new HashMap<>();
        note.put("email", email);
        note.put("title",nota.getTitle());
        note.put("bodyText", nota.getBodyText());
        note.put("color", nota.getColor());
        db.collection("notes").document(nota.getIdUser())
                .set(note, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Guardado");
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("No  guardado");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void editTodo(To_do todo, String email) {
        Map<String, Object> wtodo = new HashMap<>();
        wtodo.put("email", email);
        wtodo.put("title",todo.getTitle());
        wtodo.put("bodyText", todo.getBodyText());
        wtodo.put("color", todo.getColor());
        wtodo.put("f_creacion", new Timestamp(new Date()));
        wtodo.put("done",todo.getDone());
        wtodo.put("priority",todo.getPriority());
        wtodo.put("day",todo.getDay());
        wtodo.put("month",todo.getMonth());
        wtodo.put("year",todo.getYear());
        wtodo.put("minute",todo.getMinute());
        wtodo.put("hour",todo.getHour());
        db.collection("todo").document(todo.getIdUser())
                .set(todo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Guardado");
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("No  guardado");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void saveDocumentWithFile (String id, String description, String userid, String path) {

        Uri file = Uri.fromFile(new File(path));
        StorageReference storageRef = storage.getReference();
        StorageReference audioRef = storageRef.child("audio"+File.separator+file.getLastPathSegment());
        UploadTask uploadTask = audioRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return audioRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    saveDocument(id, description, userid, downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });


        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Upload is " + progress + "% done");
            }
        });
    }

    public HashMap<String, String> getDocuments () {
        db.collection("audioCards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        return new HashMap<>();
    }
}



