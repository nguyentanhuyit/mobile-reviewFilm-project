package com.example.newprojectmobileapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.newprojectmobileapp.DonateActivity;
import com.example.newprojectmobileapp.GoogleMapActivity;
import com.example.newprojectmobileapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.StringTokenizer;

public class UtilsFragment extends Fragment implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    TextView textViewUserName, textViewUserEmail;
    FloatingActionButton floatingActionButtonUserAccount;

    Button buttonFindCinema, buttonWishList, buttonDonate;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_utils, container, false);

        mAuth = FirebaseAuth.getInstance();

        textViewUserName = view.findViewById(R.id.text_view_user_name);
        textViewUserEmail = view.findViewById(R.id.text_view_user_email);
        floatingActionButtonUserAccount = view.findViewById(R.id.floating_action_button_user_account);

        buttonFindCinema = view.findViewById(R.id.button_find_cinema);
        buttonWishList = view.findViewById(R.id.button_wish_list);
        buttonDonate = view.findViewById(R.id.button_donate);

        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            textViewUserEmail.setText(email);
            if (currentUser.getDisplayName().equals("")) {
                StringTokenizer emailName = new StringTokenizer(email, "@");
                String name = emailName.nextToken();
                textViewUserName.setText(name);
            } else {
                textViewUserName.setText(currentUser.getDisplayName());
            }
        } else {
            // TODO
        }

        floatingActionButtonUserAccount.setOnClickListener(this);
        buttonFindCinema.setOnClickListener(this);
        buttonWishList.setOnClickListener(this);
        buttonDonate.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButtonUserAccount) {
            redirectToAccountFragment();
        } else if (v == buttonFindCinema) {
            redirectToMap();
        } else if (v == buttonWishList) {
            redirectToWishListFragment();
        } else if (v == buttonDonate) {
            donate();
        }
    }

    private void redirectToMap() {
        Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
        getActivity().startActivity(intent);
    }

    private void donate() {
        Intent intent = new Intent(getActivity(), DonateActivity.class);
        getActivity().startActivity(intent);
    }

    private void redirectToAccountFragment() {
        AccountFragment accountFragment = new AccountFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_utils, accountFragment);
        fragmentTransaction.commit();
    }

    private void redirectToWishListFragment() {
        WishListFragment wishListFragment = new WishListFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_utils, wishListFragment);
        fragmentTransaction.commit();
    }

}
