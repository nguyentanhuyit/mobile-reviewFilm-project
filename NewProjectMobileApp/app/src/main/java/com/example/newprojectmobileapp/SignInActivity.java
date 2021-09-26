package com.example.newprojectmobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.newprojectmobileapp.controller.ToastNotification;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "CHECK_LOG_IN";
    private static final String SUCCESS = ":SUCCESS";
    private static final String FAILURE = ":FAILURE";
    private static final String NORMAL_SIGN_IN = "Normal sign in:";
    private static final String SIGN_IN_WITH_GOOGLE = "Sign in with Google:";
    private static final String SIGN_IN_WITH_FACEBOOK= "Sign in with Facebook:";
    private static final int RC_SIGN_IN = 9001;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    EditText emailEditText, passwordEditText; // Edit Text
    Button normalSignInButton, resetPasswordButton, createAccountButton; // Button
    SignInButton signInButton; // google button
    FloatingActionButton signInButtonGoogleCustom; // google button custom
    LoginButton loginButton; // facebook button
    FloatingActionButton loginButtonFaceBookCustom; // facebook button custom
    CallbackManager callbackManager;
    Profile profile;

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//        LoginManager.getInstance().logOut();
//        FirebaseAuth.getInstance().signOut();

        // Normal task
        emailEditText = findViewById(R.id.edit_text_email);
        passwordEditText = findViewById(R.id.edit_text_password);
        normalSignInButton = findViewById(R.id.button_normal_sign_in);
        resetPasswordButton = findViewById(R.id.button_reset_password);
        createAccountButton = (Button) findViewById(R.id.button_create_account);

        normalSignInButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
        createAccountButton.setOnClickListener(this);


        // Google Sign In
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        signInButtonGoogleCustom = (FloatingActionButton) findViewById(R.id.sign_in_button_gg_custom);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        // Facebook Log In
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButtonFaceBookCustom = (FloatingActionButton) findViewById(R.id.login_button_fb_custom);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            // GOOGLE
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "onActivityResult|" + SIGN_IN_WITH_GOOGLE + SUCCESS + "|" + account.getEmail());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    Log.w(TAG, "onActivityResult|" + SIGN_IN_WITH_GOOGLE + FAILURE, e);
                }
            }
            // FACEBOOK
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            redirectToMainActivity();
        } else {
//            ToastNotification.setContent(SignInActivity.this, "onStart-SignInActivity\nNo user signed in");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void normalSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            redirectToMainActivity();
                        } else {
                            ToastNotification.setContent(SignInActivity.this, "Sign in failure!\nWrong email or password");
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, SIGN_IN_WITH_GOOGLE + SUCCESS + "|" + user.getEmail());
                            redirectToMainActivity();
                        } else {
                            Log.d(TAG, SIGN_IN_WITH_GOOGLE + FAILURE);
                            ToastNotification.setContent(SignInActivity.this, SIGN_IN_WITH_GOOGLE + FAILURE);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//    private void signOut() {
//        mAuth.signOut();
//        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                // TODO
//            }
//        });
//    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, SIGN_IN_WITH_FACEBOOK + SUCCESS);
                            redirectToMainActivity();
                        } else {
                            Log.w(TAG, SIGN_IN_WITH_FACEBOOK + FAILURE, task.getException());
                            ToastNotification.setContent(SignInActivity.this, SIGN_IN_WITH_FACEBOOK + FAILURE);
                        }
                    }
                });
    }


    /// reset password
    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ToastNotification.setContent(SignInActivity.this, "Email sent");
                } else {
                    ToastNotification.setContent(SignInActivity.this, "Reset password require failure");
                }
            }
        });
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void redirectToSignUpActivity() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == normalSignInButton) {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            normalSignIn(email, password);

        } else if (v == loginButtonFaceBookCustom) {
            loginButton.performClick();

        } else if (v == signInButtonGoogleCustom) {
            signInButton.performClick();
            signIn();

        } else if (v == createAccountButton) {
            redirectToSignUpActivity();

        } else if (v == resetPasswordButton) {
            String email = emailEditText.getText().toString().trim();
            resetPassword(email);
        }
    }
}

//profile = Profile.getCurrentProfile().getCurrentProfile();
//        if (profile != null) {
//        // user has logged in
//        } else {
//        // user has not logged in
//        }