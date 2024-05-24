package com.boom.welcome.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.boom.welcome.databinding.FragmentDashboardBinding;
import com.boom.welcome.ui.Camera.ScanBarCodeActivity;
import com.boom.welcome.ui.data.profile_info;
import com.boom.welcome.ui.data.user_info;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageView QR = binding.QR;
        TextView btn_QR = binding.btnQR;

        user_info userInfo = profile_info.getInstance().getMy_user_info();
        DateFormat df = new SimpleDateFormat("dd MM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());


        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode("name: "+userInfo.getName() +"\n"
                    +"time: " +date+"\n"
                    +"photo: "+userInfo.getPhoto()+"\n"
                    +"email: "+userInfo.getEmail(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            QR.setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

       btn_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ScanBarCodeActivity.class));


            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}