package com.example.newprojectmobileapp.controller;

import android.content.Context;
import android.widget.Toast;

public class ToastNotification {
    public static void setContent(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
