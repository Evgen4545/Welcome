package com.boom.welcome.ui.PinCod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.boom.welcome.R;
import com.boom.welcome.ui.LoginActivity;
import com.boom.welcome.ui.SplashActivity;

public class PinActivity extends AppCompatActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_PASS = "pass";
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin2);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        final PinEntryEditText pinEntry = (PinEntryEditText) findViewById(R.id.pass);
        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.toString().equals("1234")) {
                        Intent i = new Intent(PinActivity.this, SplashActivity.class);
                        startActivity(i);
                        Toast.makeText(PinActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PinActivity.this, "FAIL", Toast.LENGTH_SHORT).show();
                        pinEntry.setText(null);
                    }
                }
            });
        }
    }
}