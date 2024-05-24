package com.boom.welcome.ui;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.boom.welcome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegistrationActivity extends AppCompatActivity { EditText emailEditText , nameEditText , passwordEditText, postEditText, pinEditText;
    TextView  btnRegistration ;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    private String uploadedImageUrl;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap galleryPic = null;
    ImageView trash_image;
    private static final int SELECT_PICTURE = 1;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PASS = "pass";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailEditText = findViewById(R.id.emailEditText);
        pinEditText = findViewById(R.id.pinEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        postEditText = findViewById(R.id.postEditText);
        trash_image = findViewById(R.id.listImage);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnRegistration = findViewById(R.id.btnRegistration);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();





        trash_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PICTURE);

            }
        });


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String post = postEditText.getText().toString();
                String pin = pinEditText.getText().toString();

                if (!email.equals("") || !name.equals("") || !password.equals("")|| !post.equals("")||!uploadedImageUrl.equals("")||!pin.equals(""))
                {
                    createAccount(email , password ,name, post);
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putString(APP_PREFERENCES_PASS, pin);
                    editor.apply();



                }
            }
        });


    }


    private void createAccount(String email, String password, String name, String post) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            user = mAuth.getCurrentUser();
                            String uid = user.getUid();

                            Log.d("UID", uid);
                            setUserInfo( uid , email , name, post);





                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
        // [END create_user_with_email]
    }



    public  void setUserInfo (String uid , String email ,String name, String post )
    {
        CollectionReference cities = db.collection("users");

        Map<String, Object> data1 = new HashMap<>();

        System.out.println(name+ email);
        Log.d("USERS", name+email);

        data1.put("email", email);
        data1.put("name",  name);
        data1.put("post",  post);
        data1.put("photo",  uploadedImageUrl);


        cities.document(uid).set(data1);

        Intent intent = new Intent(RegistrationActivity.this, SplashActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        galleryPic = MediaStore.Images.Media.getBitmap(RegistrationActivity.this.getContentResolver(), selectedImage);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    uploadImage(selectedImage);
                    trash_image.setImageBitmap(galleryPic);
                }
        }
    }


    private void uploadImage(Uri selectedImage) {



        if(selectedImage != null) {
            final ProgressDialog progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());


            ref.putFile(selectedImage)

                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();

                        }

                    })

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override

                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot

                                    .getTotalByteCount());

                            progressDialog.setMessage("Uploaded " + (int) progress + "%");

                        }

                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    uploadedImageUrl = task.getResult().toString();
                                    Log.d("map", uploadedImageUrl);
                                }
                            });
                        }
                    });

        }
    }





}