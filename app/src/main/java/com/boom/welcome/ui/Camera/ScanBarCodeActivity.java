package com.boom.welcome.ui.Camera;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.boom.welcome.MainActivity;
import com.boom.welcome.R;
import com.boom.welcome.databinding.FragmentDashboardBinding;
import com.boom.welcome.ui.SplashActivity;
import com.boom.welcome.ui.data.FireBaseLoad;
import com.boom.welcome.ui.data.profile_info;
import com.boom.welcome.ui.data.scanQr;
import com.boom.welcome.ui.data.user_info;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ScanBarCodeActivity extends AppCompatActivity {

    private FragmentDashboardBinding binding;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    String intentData = "";
    FireBaseLoad fireBaseLoad;
    private FirebaseUser aUser;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);
        aUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        initViews();
       fireBaseLoad = new FireBaseLoad(this);
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);

    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext() , "Barcode scanner started" , Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this , barcodeDetector)
                .setRequestedPreviewSize(1920 , 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanBarCodeActivity.this , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScanBarCodeActivity.this , new
                                String[]{Manifest.permission.CAMERA} , REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder , int format , int width , int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

            @Override
            public void release() {
                Toast.makeText(getApplicationContext() , "To prevent memory leaks barcode scanner has been stopped" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                DialogFragmentEx myDialogFragment = new DialogFragmentEx();
                FragmentManager manager = getSupportFragmentManager();
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                            String uid = aUser.getUid();

                            myDialogFragment.setUser(uid,intentData);
                            myDialogFragment.show(manager, "dialog");


                           // Log.d("map",fireBaseLoad.getUserBal(intentData).getBonuses());
                           // Log.d("map",fireBaseLoad.getUserBal(intentData).getEmail());


                            ;
                          //  myDialogFragment.setUser(fireBaseLoad.getUserBal(intentData));
                          //  myDialogFragment.show(manager, "dialog");
                           // myDialogFragment.dismiss();

                            //Log.d("map",myDialogFragment.getDialog().toString());


                           // onBackPressed();

                        }
                    });
                }
            }
        });
    }





        @Override
        protected void onPause () {
            super.onPause();
            cameraSource.release();
        }

        @Override
        protected void onResume () {
            super.onResume();
            initialiseDetectorsAndSources();


        }


        @Override
        public void onBackPressed () {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                this.finish();
            } else {
                super.onBackPressed(); //replaced
            }
        }


    public static class DialogFragmentEx extends DialogFragment {
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        user_info user;
        String uid;
        String intent;

        public  DialogFragmentEx(){

        }
        public void setUser (String uid, String intent){
            this.uid= uid;
            this.intent = intent;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Отправить отчет?";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());

            if (intent.equals("")){
                builder.setTitle("QR код не распознан");
                builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
            }

            else {

                builder.setTitle(title);
                builder.setMessage(getField(intent)+" "+date);

                // builder.setView(edittext);// заголовок
                builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        setUserInfo(uid,getField(intent), date);

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);




                    }
                });
                builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                    }
                });
                builder.setCancelable(true);

            }

            return builder.create();

        }
        public String createTransactionID() throws Exception{
            return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }

        public  String getField (String s){
            String text = "";
            if (s.equals("88edda4b-2bc1-4f54-8b45-e82ecfc10ede"))
            {
                text="вход";

            }
            else if (s.equals("9b66b62f-cec1-49bd-a4e4-e3e2ba89b932"))
            {
                text = "выход";
            }
            Log.d("IN", text);

            return text;
        }

        public  void setUserInfo (String uid, String field, String date)
        {


            if (field.equals("")){
                return;
            }
            else {
                try {
                    user_info userInfo = profile_info.getInstance().getMy_user_info();

                    CollectionReference cities = db.collection("log");
                    Map<String, Object> data1 = new HashMap<>();


                    data1.put("useruid", uid);
                    data1.put("turneket", field);
                    data1.put("time", date);
                    data1.put("name", userInfo.getName());
                    data1.put("photo", userInfo.getPhoto());

                    cities.document(createTransactionID()).set(data1);
                } catch (NumberFormatException e) {
                    System.out.println("Input String cannot be parsed to Integer.");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }







        }

    }


}