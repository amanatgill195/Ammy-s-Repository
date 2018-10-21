package com.example.amanat.citytour.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanat.citytour.MainMenu;
import com.example.amanat.citytour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "";
    private Button btLogin, btSignUp;
    private EditText etUsername, etPhone, etPassword, etConfirmPassword, etEmail;
    private CheckBox checkBox;
    private TextView textView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog progressDialog;
    private FirebaseUser userData;
    private String uId;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthListener);
        //updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (mAuth.getCurrentUser() != null) {

                    Intent intent = new Intent(SignUp.this, MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {

                }
            }
        };

        inItComponent();

    }

    public void inItComponent() {
        btSignUp = (Button) findViewById(R.id.btSignUp);
        btLogin = (Button) findViewById(R.id.btLogin);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        textView = (TextView) findViewById(R.id.textView);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    final String txtName = etUsername.getText().toString().trim();
                    final String txtEmail = etEmail.getText().toString().trim();
                    final String txtPassword = etPassword.getText().toString().trim();
                    final String txtConfirmPassword = etConfirmPassword.getText().toString().trim();
                    final String txtPhone = String.valueOf(etPhone.getText().toString().trim());

                    if (etUsername.getText().toString().length() == 0) {
                        etUsername.setError("Name Required");
                    } else if (etEmail.getText().toString().length() == 0) {
                        etEmail.setError("Email Required");
                    } else if (etPhone.getText().toString().length() == 0) {
                        etPhone.setError("Phone Number Required");
                    } else if (etPassword.getText().toString().length() == 0) {
                        etPassword.setError("Password Required");
                    } else if (etConfirmPassword.getText().toString().length() == 0) {
                        etConfirmPassword.setError("Confirm Password Required");
                    } else if (!txtPassword.equals(txtConfirmPassword)) {
                        etConfirmPassword.setError("Passwords does not Match");
                    } else if (etPhone.getText().toString().length() != 10) {
                        etPhone.setError("10 Digits Required");
                    } else {

                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Loading...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            userData = FirebaseAuth.getInstance().getCurrentUser();
                                            uId = userData.getUid();
                                            Map<String, Object> user = new HashMap<>();
                                            user.put("name", txtName);
                                            user.put("email", txtEmail);
                                            user.put("phone", txtPhone);
                                            user.put("uId", uId);
                                            user.put("password", txtPassword);

// Add a new document with a generated ID
                                            db.collection("users").document(uId)
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                            Toast.makeText(SignUp.this, "Document sent successfully", Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                            Toast.makeText(SignUp.this, "Error writing document", Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                                            Log.d(TAG, "createUserWithEmail:success");
                                            Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                                            //FirebaseUser user = mAuth.getCurrentUser();
                                            Intent intent = new Intent(SignUp.this, MainMenu.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            //updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(SignUp.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            //updateUI(null);
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(SignUp.this, "Please check your Internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void callSignUp(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }

    private void userProfile() {
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
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}