package com.example.newprojectmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SlashScreenActivity extends AppCompatActivity {

    TextView textViewAppVersionName, textViewAuthorCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_screen);
        getSupportActionBar().hide();
        textViewAppVersionName = findViewById(R.id.textViewAppVersionName);
        textViewAuthorCompany = findViewById(R.id.textViewAuthorCompany);
        textViewAuthorCompany.setText(getString(R.string.author_company));
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            textViewAppVersionName.setText(getString(R.string.version_name) + " " + packageInfo.versionName);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent signIn = new Intent(SlashScreenActivity.this, SignInActivity.class);
                    startActivity(signIn);
                }
            }, 2000);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}