package com.example.newprojectmobileapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.adapters.MovieItemExploreAdapter;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.Video;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements MovieItemClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MovieItemExploreAdapter movieItemAdapter;
    RecyclerView recyclerViewExplore;
    private List<Video> allMovies;
    Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("videos");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerViewExplore = view.findViewById(R.id.recycle_view_explore);

        getData();
        initAllMovies();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieItemAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieItemAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getData() {
        allMovies = new ArrayList<>();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Video video = dataSnapshot.getValue(Video.class);
//                    allMovies.add(video);
//                }
//                movieItemAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Video video = snapshot.getValue(Video.class);
                allMovies.add(video);
                movieItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                allMovies.clear();
                databaseReference.removeEventListener(this);
                databaseReference.addChildEventListener(this);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                allMovies.clear();
                databaseReference.removeEventListener(this);
                databaseReference.addChildEventListener(this);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void initAllMovies() {
        movieItemAdapter = new MovieItemExploreAdapter(getContext(), allMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerViewExplore.setLayoutManager(gridLayoutManager);
        recyclerViewExplore.setAdapter(movieItemAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
