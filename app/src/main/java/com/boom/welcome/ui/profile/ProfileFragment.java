package com.boom.welcome.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.boom.welcome.R;
import com.boom.welcome.databinding.FragmentProfileBinding;
import com.boom.welcome.ui.LoginActivity;
import com.boom.welcome.ui.data.MaskTransformation;
import com.boom.welcome.ui.data.RoundedCornersTransformation;
import com.boom.welcome.ui.data.profile_info;
import com.boom.welcome.ui.data.user_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private FirebaseUser aUser;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView nameText = binding.nameText;
        TextView emailText = binding.emailText;
        TextView exit = binding.exit;
        ImageView photo = binding.photo;

        mAuth = FirebaseAuth.getInstance();
        aUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        user_info userInfo = profile_info.getInstance().getMy_user_info();
        System.out.println(userInfo.getName());
        nameText.setText(userInfo.getName());
        emailText.setText(userInfo.getEmail());

        final Transformation transformation = new MaskTransformation(getContext(), R.drawable.rounded_convers_transformation);
        Picasso.get()
                .load(userInfo.getPhoto())
                .resize(200, 200) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .transform(transformation)
                .into(photo);
       // Picasso.get().load(userInfo.getPhoto()).into(photo);



        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentExit myDialogFragment = new DialogFragmentExit();
                FragmentManager manager = getFragmentManager();
                //myDialogFragment.show(manager, "dialog");

                FragmentTransaction transaction = manager.beginTransaction();
                myDialogFragment.show(transaction, "dialog");


            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class DialogFragmentExit extends DialogFragment {




        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String title = "Вы уверены, что хотите завершить сеанс? ";
            String button1String = "Да, я уверен(а)";
            String button2String = "Отмена";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);  // заголовок
            builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {


                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
            });
            builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.setCancelable(true);

            return builder.create();
        }


    }
}