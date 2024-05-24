package com.boom.welcome.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.boom.welcome.databinding.FragmentHomeBinding;
import com.boom.welcome.ui.data.Adapter.AdapterList;
import com.boom.welcome.ui.data.FireBaseLoad;
import com.boom.welcome.ui.data.ListInfo;
import com.boom.welcome.ui.data.profile_info;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Log.d("ACTIVITY","home");
         FirebaseUser aUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = aUser.getUid();
        FireBaseLoad data = new FireBaseLoad(getContext());
        Log.d("USERS",uid);
        data.getMyInfo();




        ArrayList<ListInfo> Infos = profile_info.getInstance().getMyvoice();
        RecyclerView recyclerView = binding.List;


        AdapterList adapter = new AdapterList(getActivity(),Infos);

        if (Infos.size()!=0) {
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else Log.d("RECYCLER", "0");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}