package com.boom.welcome.ui.data;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseLoad {

    Context context;

    user_info user = new user_info();

    ArrayList<ListInfo> myInfos;

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser userDB= mAuth.getCurrentUser();




    FirebaseFirestore db = FirebaseFirestore.getInstance();
    profile_info profileInfo = new profile_info();

      public FireBaseLoad(Context context){
          this.context=context;
      }





    public user_info getUserInfo(String UserToken) {
        user_info userInfo = new user_info();

        DocumentReference docRef = db.collection("users").document(UserToken);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String email = notNullStrForUsers(document, "email");
                        String name = notNullStrForUsers(document, "name");
                        String post = notNullStrForUsers(document, "post");
                        String photo = notNullStrForUsers(document, "photo");




                        userInfo.setEmail(email);
                        userInfo.setName(name);
                        userInfo.setPost(post);
                        userInfo.setPhoto(photo);
                        userInfo.setId(document.getId());
                        profileInfo.getInstance().setMy_user_info(userInfo);
                        Log.d("map", "setMy_user_info");

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


    public void getMyInfo() {
        myInfos = new ArrayList<>();
        String uid = userDB.getUid();

        db.collection("log")
                .whereEqualTo("useruid", uid)
                .get()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                //5ADD4A60-236B-471A-9CCF-CD7325E53462 => {address=Yanvarskaya, Bishkek, Kyrgyzstan, chat=отлль, photo=https://firebasestorage.googleapis.com:443/v0/b/clean-299910.appspot.com/o/images%2F5ADD4A60-236B-471A-9CCF-CD7325E53462?alt=media&token=3196b541-fd18-4d57-9ec6-4ea5e4e2aea4, lon=74.55639065219918, details=Друзья обнаружена свалка на территории, давайте уберемся, id=5ADD4A60-236B-471A-9CCF-CD7325E53462, lat=42.874141240957535}

                                ListInfo pInfo = new ListInfo();
                                // pInfo.setId(document.getId());  // 5ADD4A60-236B-471A-9CCF-CD7325E53462
                                Map<String, Object> responseObj = new HashMap<String, Object>();

                                String time = notNullStr(document, "time");
                                String turneket = notNullStr(document, "turneket");
                                String photo = notNullStr(document, "photo");

                                Log.d("PART", time);


                                pInfo.setTime(time);
                                pInfo.setTurneket(turneket);
                                pInfo.setPhoto(photo);


                                myInfos.add(pInfo);
                                Log.d(TAG, document.getId() + " => " + document.getData());


                            }

                            profileInfo.getInstance().setMyvoice(myInfos);
                            Log.d("map", "setInfos");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Log.d("map", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }




    public List<String> stringToArrList(String s){
        s = s.replaceAll("[\\[\\]]", "");
        List<String> arr = new ArrayList<String>(Arrays.asList(s.split(",")));
          return arr;
    }


    public String notNullStr(QueryDocumentSnapshot document, String tokenName) {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        } catch (Exception e) {

        }
        return dd;
    }


    public String notNullStrForUsers(DocumentSnapshot document, String tokenName) {
        String dd = "";
        try {
            dd = document.getData().get(tokenName).toString();
        } catch (Exception e) {

        }
        return dd;
    }

    public void removeVoice (String s) {

        db.collection("voice").document(s).delete();
    }


}
