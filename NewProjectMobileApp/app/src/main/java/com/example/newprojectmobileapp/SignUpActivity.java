package com.example.newprojectmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.newprojectmobileapp.controller.ToastNotification;
import com.example.newprojectmobileapp.model.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CHECK_LOG_IN";
    private static final String SUCCESS = ":SUCCESS";
    private static final String FAILURE = ":FAILURE";

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseRef;
    DatabaseReference usersDatabaseRef;

    FirebaseAuth mAuth;

    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextRePassword;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mFirebaseDatabase.getReference();
        usersDatabaseRef = mDatabaseRef.child("users");

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextRePassword = findViewById(R.id.edit_text_re_password);
        buttonSignUp = findViewById(R.id.button_sign_up);

        buttonSignUp.setOnClickListener(this);

    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    saveAccountInfoToDatabase(user, password); // luu
                    Log.d(TAG, "CREATE_ACCOUNT:" + SUCCESS + "|" + user.getEmail());
                    ToastNotification.setContent(SignUpActivity.this, "Sign up success!\nWelcome " + user.getEmail());
                    redirectToSignInActivity(email);
                } else {
                    Log.d(TAG, "CREATE_ACCOUNT:" + FAILURE);
                    ToastNotification.setContent(SignUpActivity.this, "Sign up failure!\nEmail already exists");
                }
            }
        });
    }

    private void saveAccountInfoToDatabase(FirebaseUser user, String userPassword) {
        DatabaseReference userUidDatabaseRef = usersDatabaseRef.child(user.getUid());
        UserAccount userAccount = new UserAccount(user.getUid(), user.getEmail(), userPassword,
                "", "", 0, true, false, "normal", 0, 1);
        userUidDatabaseRef.setValue(userAccount);
    }

    private void redirectToSignInActivity(String email) {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignUp) {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString();
            createAccount(email, password);
        }
    }
}