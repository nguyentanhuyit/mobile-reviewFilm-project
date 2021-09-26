package com.example.newprojectmobileapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.adapters.MovieItemExploreAdapter;
import com.example.newprojectmobileapp.controller.ToastNotification;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.Video;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment implements MovieItemClickListener, View.OnClickListener {

    View view;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference wishListDatabaseRef;
    MovieItemExploreAdapter movieItemAdapter;
    RecyclerView recyclerViewWishList;
    private List<Video> allMovies;

    Button buttonBack;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishList");

        WishListFragment wishListFragment = this;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(wishListFragment);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wish_list, container, false);

        buttonBack = view.findViewById(R.id.button_back);
        recyclerViewWishList = view.findViewById(R.id.recycle_view_wish_list);


        getData();
        initWishList();

        buttonBack.setOnClickListener(this);

        return view;
    }

    private void getData() {
        currentUser = mAuth.getCurrentUser();
        allMovies = new ArrayList<>();
        String userUid = currentUser.getUid();
        Log.d("wish", userUid);
        databaseReference.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Video video = dataSnapshot.getValue(Video.class);
                    allMovies.add(video);
                }
                movieItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initWishList() {
        movieItemAdapter = new MovieItemExploreAdapter(getContext(), allMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewWishList.setLayoutManager(gridLayoutManager);
        recyclerViewWishList.setAdapter(movieItemAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonBack) {
            back();
        }
    }

    private void back() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    @Override
    public void onMovieClick(Video video, ImageView imageView) {
        String videoId = video.getVideoId();
        String videoName = video.getVideoName();
        String videoUrl = video.getVideoUrl();
        String videoCategory = video.getVideoCategory();
        String videoType = video.getVideoType();
        String videoThumbnail = video.getVideoThumbnail();
        boolean videoSlide = video.isVideoSlide();
        String videoTitle = video.getVideoTitle();
        String videoDirector = video.getVideoDirector();
        String videoMainActor = video.getVideoMainActor();
        String videoNation = video.getVideoNation();
        String videoYear = video.getVideoYear();
        String videoLength = video.getVideoLength();
        String videoDescription = video.getVideoDescription();
        int videoView = video.getVideoView();

        Video sendVideoDetail = new Video(videoId, videoName, videoUrl, videoCategory, videoType, videoThumbnail,
                videoSlide, videoTitle, videoDirector, videoMainActor, videoNation, videoYear, videoLength,
                videoDescription, videoView);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_video", sendVideoDetail);
        movieDetailFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_movie_detail, movieDetailFragment);
        fragmentTransaction.commit();
    }
}
