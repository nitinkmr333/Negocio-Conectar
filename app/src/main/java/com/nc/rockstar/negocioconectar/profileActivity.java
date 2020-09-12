package com.nc.rockstar.negocioconectar;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class profileActivity extends Fragment {
    View myView;

    Button mFollow;

    FirebaseUser user;
    FirebaseAuth mAuth;
    FirebaseFirestore FB;

    String Username;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_profile, container, false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        FB = FirebaseFirestore.getInstance();
        ImageView mIcon = getActivity().findViewById(R.id.ivProfile);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boss_128);
        RoundedBitmapDrawable mDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        mDrawable.setCircular(true);
        mDrawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.main_gold), PorterDuff.Mode.DST_OVER);
        mIcon.setImageDrawable(mDrawable);

        TextView userName = getActivity().findViewById(R.id.info);
        TextView userEmail = getActivity().findViewById(R.id.tvDescription);

        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());


    }


//    private userInformation getData(){
//        FB.collection("users").document(user.getUid()).get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                        if (documentSnapshots.isEmpty()) {
//                            Log.d("userDetails", "onSuccess: LIST EMPTY");
//
//                            return;
//                        } else {
//                            List<userInformation> types = documentSnapshots.toObjects(userInformation.class);
//
//
//                            userInformation usr = types.get(0);
//
//                            Username = usr.userName.toString().
//
//                            }
//                        }
//                    });
//    }


}
