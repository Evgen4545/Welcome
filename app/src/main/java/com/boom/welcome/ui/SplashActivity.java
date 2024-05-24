package com.boom.welcome.ui;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.boom.welcome.MainActivity;
import com.boom.welcome.R;
import com.boom.welcome.ui.data.FireBaseLoad;
import com.boom.welcome.ui.data.profile_info;
import com.boom.welcome.ui.data.user_info;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {



    private FirebaseUser aUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseApp.initializeApp(this);

        aUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("ACTIVITY","splash");


        if (aUser != null) {
            // User is signed in
            String uid = aUser.getUid();
            FireBaseLoad data = new FireBaseLoad(this);
            Log.d("USERS",uid);
            data.getUserInfo(uid);
            data.getMyInfo();




            if (isNetworkAvailable(this)) {

                new Handler().postDelayed(new Runnable() {

                    @Override

                    public void run() {




                        user_info userInfo = profile_info.getInstance().getMy_user_info();
                        // Log.d("USERS", userInfo.getName());
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);


                    }




                }, 4000);




                //  Log.d("map", String.valueOf(data.getMarkersInfo()));




            }







        } else {
            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

    }



    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


}




