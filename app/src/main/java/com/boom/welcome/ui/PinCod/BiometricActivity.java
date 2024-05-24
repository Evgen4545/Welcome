package com.boom.welcome.ui.PinCod;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.welcome.R;
import com.boom.welcome.ui.LoginActivity;
import com.boom.welcome.ui.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class BiometricActivity extends AppCompatActivity {
    private FirebaseUser aUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        aUser = FirebaseAuth.getInstance().getCurrentUser();

        if (aUser != null) {
            TextView msgtex = findViewById(R.id.msgtext);
            final Button loginbutton = findViewById(R.id.login);

            // creating a variable for our BiometricManager
            // and lets check if our user can use biometric sensor or not
            BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {

                // this means we can use biometric sensor
                case BiometricManager.BIOMETRIC_SUCCESS:
                    msgtex.setText("Вы можете использовать датчик отпечатков пальцев для входа в систему");
                    msgtex.setTextColor(Color.parseColor("#fafafa"));
                    break;

                // this means that the device doesn't have fingerprint sensor
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    msgtex.setText("В этом устройстве нет датчика отпечатков пальцев");
                    loginbutton.setVisibility(View.GONE);
                    break;

                // this means that biometric sensor is not available
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    msgtex.setText("Биометрический датчик в настоящее время недоступен");
                    loginbutton.setVisibility(View.GONE);
                    break;

                // this means that the device doesn't contain your fingerprint
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    msgtex.setText("На вашем устройстве не сохранены отпечатки пальцев, пожалуйста, проверьте настройки безопасности");
                    loginbutton.setVisibility(View.GONE);
                    break;
            }
            // creating a variable for our Executor
            Executor executor = ContextCompat.getMainExecutor(this);
            // this will give us result of AUTHENTICATION
            final BiometricPrompt biometricPrompt = new BiometricPrompt(BiometricActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    loginbutton.setText("Login Successful");
                    Intent i = new Intent(BiometricActivity.this, SplashActivity.class);
                    startActivity(i);
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });
            // creating a variable for our promptInfo
            // BIOMETRIC DIALOG
            final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("CHECKPOINT")
                    .setDescription("Используйте свой отпечаток пальца для входа в систему").setNegativeButtonText("Отмена").build();
            biometricPrompt.authenticate(promptInfo);


            loginbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(BiometricActivity.this, PinActivity.class);
                    startActivity(i);

                }
            });
        } else {
            Intent i = new Intent(BiometricActivity.this, LoginActivity.class);
            startActivity(i);
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }


    }
}