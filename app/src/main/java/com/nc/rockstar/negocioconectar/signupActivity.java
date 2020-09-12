package com.nc.rockstar.negocioconectar;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signupActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog mProgressDialog;

    private static final String TAG = "userDetails";

    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText nameField;
    private CheckBox tcField;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fireFB;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        emailField = findViewById(R.id.signup_email);
        passwordField = findViewById(R.id.signup_pass);
        confirmPasswordField = findViewById(R.id.signup_conf_pass);
        nameField = findViewById(R.id.signup_name);
        tcField = findViewById(R.id.signup_agree_tc);

        findViewById(R.id.signup_signup_butt).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        fireFB = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void createAccount(String email, String password, String name){

        Log.d(TAG, "create account:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            // User is signed in
                            // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                            // the user will receive another verification email.
                            //sendEmailVerification();
                            startActivity(new Intent(signupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // User is signed out

                        }
                        // ...
                    }
                };



                if (task.isSuccessful()){
                    Toast.makeText(signupActivity.this, "Successfully created user.",
                            Toast.LENGTH_SHORT).show();
                    databaseUserEntry();
                    finish();
                    startActivity(new Intent(signupActivity.this, MainActivity.class));
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(signupActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                hideProgressDialog();

            }
        });

    }


    public void getCurrentUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


    private void sendEmailVerification() {

        mAuth.getCurrentUser().sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signupActivity.this,
                                    "Verification email sent to " + mAuth.getCurrentUser().getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(signupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(signupActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        String confirmPassword = confirmPasswordField.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordField.setError("Required.");
            valid = false;
        } else {
            confirmPasswordField.setError(null);
        }

        String name = nameField.getText().toString();
        if (TextUtils.isEmpty(name)){
            nameField.setError("Required.");
            valid = false;
        } else {
            nameField.setError(null);
        }

        boolean tc = tcField.isChecked();
        if (!tc){
            tcField.setError("Required.");
            valid = false;
        } else {
            tcField.setError(null);
        }

        if(!password.equals(confirmPassword)){
            confirmPasswordField.setError("Passwords don't match.");
            valid=false;
        } else {
            confirmPasswordField.setError(null);
        }


        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signup_signup_butt) {
            createAccount(emailField.getText().toString().trim(), passwordField.getText().toString(), nameField.getText().toString().trim());
        }
    }



    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    private void databaseUserEntry(){

        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();

        CollectionReference users = fireFB.collection("users");
        userInformation usrInf = new userInformation(name, email);

        fireFB.collection("users").document(user.getUid()).set(usrInf).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(signupActivity.this, "User Created", Toast.LENGTH_LONG);
            }
        });
//
//        users.add(usrInf).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(signupActivity.this, "User Created", Toast.LENGTH_LONG);
//            }
//        });
    }

    @Exclude
    public Map<String, Object> toMap(String name, String email) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);

        return result;
    }


}
