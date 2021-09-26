package com.example.newprojectmobileapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.SignInActivity;
import com.example.newprojectmobileapp.controller.ToastNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment implements View.OnClickListener {

    View view;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Button buttonBack;
    Button buttonUpdateAccountInfo;
    Button buttonSignOut;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
//            ToastNotification.setContent(getContext(), currentUser.getEmail());
        } else {
            ToastNotification.setContent(getContext(), "user null");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        AccountFragment accountFragment = this;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(accountFragment);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);

        initView();

        buttonBack.setOnClickListener(this);
        buttonUpdateAccountInfo.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);

        return view;
    }

    private void initView() {
        buttonBack = view.findViewById(R.id.button_back);
        buttonUpdateAccountInfo = view.findViewById(R.id.button_update_account_info);
        buttonSignOut = view.findViewById(R.id.button_sign_out);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            back();
        } else if (v == buttonUpdateAccountInfo) {
            redirectToUpdateAccountActivity();
        } else if (v == buttonSignOut) {
            signOut();
        }
    }

    private void back() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    private void redirectToUpdateAccountActivity() {
        UpdateAccountFragment updateAccountFragment = new UpdateAccountFragment();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_utils, updateAccountFragment);
        fragmentTransaction.commit();
    }

    private void signOut() {
        if (currentUser != null) {
            mAuth.signOut();
            redirectToSignInActivity();
            ToastNotification.setContent(getContext(), "Signed out!!");
        } else {
            ToastNotification.setContent(getContext(), "user = null, can't sign out");
        }
    }

    private void redirectToSignInActivity() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }
}
