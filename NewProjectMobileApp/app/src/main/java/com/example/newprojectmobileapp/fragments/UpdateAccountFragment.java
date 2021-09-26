package com.example.newprojectmobileapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.controller.ToastNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.StringTokenizer;

public class UpdateAccountFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    View view;

    TextView textViewUserEmail;
    EditText editTextUserName, editTextUserAge, editTextUserGender;
    Button buttonBack, buttonUpdate;

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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AccountFragment accountFragment = new AccountFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout_utils, accountFragment);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_account, container, false);

        initView();

        buttonBack.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);

        return view;
    }

    private void initView() {
        buttonBack = view.findViewById(R.id.button_back);
        buttonUpdate = view.findViewById(R.id.button_update);

        textViewUserEmail = view.findViewById(R.id.text_view_user_email);
        editTextUserName = view.findViewById(R.id.edit_text_user_name);

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            textViewUserEmail.setText(email);
            if (currentUser.getDisplayName().equals("")) {
                StringTokenizer emailName = new StringTokenizer(email, "@");
                String name = emailName.nextToken();
                editTextUserName.setText(name);
            } else {
                editTextUserName.setText(currentUser.getDisplayName());
            }
        } else {
            // TODO
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            back();
        } else if (v == buttonUpdate) {
            updateAccount();
        }
    }

    private void updateAccount() {

    }

    private void back() {
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_utils, accountFragment);
        fragmentTransaction.commit();
    }
}
