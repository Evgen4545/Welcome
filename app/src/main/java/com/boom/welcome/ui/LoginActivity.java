package com.boom.welcome.ui;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.boom.welcome.MainActivity;
import com.boom.welcome.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText;
    TextView nextImageView;
    TextView registrTextView, fogotPassTextView;
    private FirebaseAuth mAuth;
    private FirebaseUser aUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nextImageView = findViewById(R.id.textView2);
        registrTextView = findViewById(R.id.registrTextView);
       // fogotPassTextView = findViewById(R.id.fogotPassTextView);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LayoutInflater inflater = this.getLayoutInflater();

        Log.d("ACTIVITY","login");






        mAuth = FirebaseAuth.getInstance();
        aUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        // getMarkersInfo ();



        if (aUser != null) {
            // User is signed in
            String uid = aUser.getUid();


            if(isNetworkAvailable(this)) {

              //  getUserInfo(uid);


                try {
                    Thread.sleep(2000);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }


        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }








        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (!email.equals("") || !password.equals("")) {
                    signIn(email , password);
                }
            }
        });


        registrTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });





    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            String rr = mAuth.getUid();
                          //  getUserInfo(uid);


                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            startActivity(intent);
                            finish();
                            //updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
        // [END sign_in_with_email]
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent

                    }
                });
        // [END send_email_verification]
    }


/*


    public user_info getUserInfo (String UserToken)
    {
        user_info userInfo = new user_info() ;
        profile_info profileInfo = new profile_info();
        DocumentReference docRef = db.collection("users").document(UserToken);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                        String email= notNullStrForUsers (document , "email" );
                        String name= notNullStrForUsers (document , "name" );




                        userInfo.setEmail(email);
                        userInfo.setName(name);

                        profileInfo.getInstance().setMy_user_info(userInfo);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
        return userInfo;

    }


    public String notNullStr (QueryDocumentSnapshot document , String tokenName)
    {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        }
        catch (Exception e)
        {

        }
        return dd;
    }
    public String notNullStrForUsers (DocumentSnapshot document ,String tokenName)
    {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        }
        catch (Exception e)
        {

        }
        return dd;
    }
*/
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}



