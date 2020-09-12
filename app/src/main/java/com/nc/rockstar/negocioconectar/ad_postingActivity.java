package com.nc.rockstar.negocioconectar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;


import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ad_postingActivity extends Fragment {

    private View myView;

    public ProgressDialog mProgressDialog;

    private TextView adUserName;
    private TextView adLocation;
    private TextView adContact;
    private TextView adDesc;
    private TextView adAddDesc;
    private TextView adTitle;
    private TextView adComp;

    private Boolean posted;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireFB;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.new_ad_post, container, false);
        return myView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.new_ad_post);

        adUserName = getActivity().findViewById(R.id.adName);
        adLocation = getActivity().findViewById(R.id.adLocation);
        adContact = getActivity().findViewById(R.id.adContact);
        adDesc = getActivity().findViewById(R.id.adDesc);
        adAddDesc = getActivity().findViewById(R.id.adAddDesc);
        adTitle = getActivity().findViewById(R.id.adTitle);
        adComp = getActivity().findViewById(R.id.adComp);

        mAuth = FirebaseAuth.getInstance();
        fireFB = FirebaseFirestore.getInstance();

        Button button = (Button) getActivity().findViewById(R.id.post_ad);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = view.getId();
                if (i == R.id.post_ad) {
                    if (validateForm()) {
                        if (databaseUserEntry()){
                            getFragmentManager().popBackStackImmediate();
                        }
                    }

                }
            }
        });

    }




    private boolean validateForm() {
        boolean valid = true;

        String adUser = adUserName.getText().toString();
        if (TextUtils.isEmpty(adUser)) {
            adUserName.setError("Required.");
            valid = false;
        } else {
            adUserName.setError(null);
        }

        String title = adTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            adTitle.setError("Required.");
            valid = false;
        } else {
            adTitle.setError(null);
        }

        String description = adDesc.getText().toString();
        if (TextUtils.isEmpty(description)){
            adDesc.setError("Required.");
            valid = false;
        } else {
            adDesc.setError(null);
        }

        String location = adLocation.getText().toString();
        if (TextUtils.isEmpty(location)){
            adLocation.setError("Required.");
            valid = false;
        } else {
            adLocation.setError(null);
        }

        String contact = adContact.getText().toString();
        if (TextUtils.isEmpty(contact)){
            adContact.setError("Required.");
            valid = false;
        } else {
            adContact.setError(null);
        }

        return valid;
    }


    private Boolean databaseUserEntry(){

        posted = false;
        String userid = mAuth.getUid();
        String username = adUserName.getText().toString().trim();
        String location = adLocation.getText().toString().trim();
        String contact = adContact.getText().toString().trim();
        String description = adDesc.getText().toString().trim();
        String addDescription = adAddDesc.getText().toString().trim();
        String title = adTitle.getText().toString().trim();
        String company = adComp.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();

        CollectionReference users = fireFB.collection("ads");
        ad_new new_ad = new ad_new(userid, username, location, contact, description, addDescription, title, company);

        fireFB.collection("ads").document().set(new_ad).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(getActivity(), "Ad Posted Successfully", Toast.LENGTH_LONG);
                Log.d("userDetails", "created successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getActivity(), "Could not post ad", Toast.LENGTH_LONG);
            }
        });

        return true;
//
//        users.add(usrInf).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(signupActivity.this, "User Created", Toast.LENGTH_LONG);
//            }
//        });
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }






}
