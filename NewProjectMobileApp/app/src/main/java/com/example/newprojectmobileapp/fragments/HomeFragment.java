package com.example.newprojectmobileapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.adapters.MovieItemAdapter;
import com.example.newprojectmobileapp.adapters.SliderPagerAdapter;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.SliderSide;
import com.example.newprojectmobileapp.model.Video;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment implements MovieItemClickListener, View.OnClickListener {

    private Context mContext;

    Button buttonExplore;

    MovieItemAdapter movieItemAdapter;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    private ViewPager sliderPager;
    private List<Video> uploads, uploadsListLatest, uploadsListPopular, actionMovies, superheroMovies, scifiMovies, fantasyMovies, romanceMovies, animatedMovies;
    private List<SliderSide> uploadsSlider;
    private TabLayout indicator, tabActionMovies;
    private RecyclerView recycleViewLatestMovies, recyclerViewPopularMovies, tab;
    ProgressDialog progressDialog;

    boolean init = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(mContext);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonExplore = view.findViewById(R.id.button_explore);
        buttonExplore.setOnClickListener(this);

        tabActionMovies = view.findViewById(R.id.tab_action_movies);
        sliderPager = view.findViewById(R.id.slider_pager);
        indicator = view.findViewById(R.id.indicator);
        recyclerViewPopularMovies = view.findViewById(R.id.movie_recycler_view);
        recycleViewLatestMovies = view.findViewById(R.id.movie_week_recycler_view);
        tab = view.findViewById(R.id.tab_recycler);

        getData();
        moviesViewTab();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void getData() {
        uploads = new ArrayList<>();
        uploadsListLatest = new ArrayList<>();
        uploadsListPopular = new ArrayList<>();
        actionMovies = new ArrayList<>();
        superheroMovies = new ArrayList<>();
        scifiMovies = new ArrayList<>();
        fantasyMovies = new ArrayList<>();
        romanceMovies = new ArrayList<>();
        animatedMovies = new ArrayList<>();
        uploadsSlider = new ArrayList<>();

        mDatabaseReference = mFirebaseDatabase.getReference("videos");
        progressDialog.setMessage("Loading movies...");
        progressDialog.show();

//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                    Video upload = postSnapshot.getValue(Video.class);
//                    SliderSide slide = postSnapshot.getValue(SliderSide.class);
//                    if (upload.getVideoType().equals("Latest Movie")) {
//                        uploadsListLatest.add(upload);
//                    }
//
//                    if (upload.getVideoType().equals("Popular Movie")) {
//                        uploadsListPopular.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Action")) {
//                        actionMovies.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Superhero")) {
//                        superheroMovies.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Sci-fi")) {
//                        scifiMovies.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Fantasy")) {
//                        fantasyMovies.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Romance")) {
//                        romanceMovies.add(upload);
//                    }
//
//                    if (upload.getVideoCategory().equals("Animated")) {
//                        animatedMovies.add(upload);
//                    }
//
//                    if (upload.isVideoSlide() == true) {
//                        uploadsSlider.add(slide);
//                    }
//                    uploads.add(upload);
//                }
//                movieItemAdapter.notifyDataSetChanged();
//                initSlider();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Video upload = snapshot.getValue(Video.class);
                SliderSide slide = snapshot.getValue(SliderSide.class);
                if (upload.getVideoType().equals("Latest Movie")) {
                    uploadsListLatest.add(upload);
                }

                if (upload.getVideoType().equals("Popular Movie")) {
                    uploadsListPopular.add(upload);
                }

                if (upload.getVideoCategory().equals("Action")) {
                    actionMovies.add(upload);
                }

                if (upload.getVideoCategory().equals("Superhero")) {
                    superheroMovies.add(upload);
                }

                if (upload.getVideoCategory().equals("Sci-fi")) {
                    scifiMovies.add(upload);
                }

                if (upload.getVideoCategory().equals("Fantasy")) {
                    fantasyMovies.add(upload);
                }

                if (upload.getVideoCategory().equals("Romance")) {
                    romanceMovies.add(upload);
                }

                if (upload.getVideoCategory().equals("Animated")) {
                    animatedMovies.add(upload);
                }

                if (upload.isVideoSlide() == true) {
                    uploadsSlider.add(slide);
                }

                uploads.add(upload);
                if (uploads.size() == snapshot.getChildrenCount()) {
                    progressDialog.dismiss();
                }
                initSlider();
                initPopularMovies();
                initWeekMovies();
                getActionMovies();
                movieItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uploadsListLatest.clear();
                uploadsListPopular.clear();
                actionMovies.clear();
                superheroMovies.clear();
                scifiMovies.clear();
                fantasyMovies.clear();
                romanceMovies.clear();
                animatedMovies.clear();
                uploadsSlider.clear();
                mDatabaseReference.removeEventListener(this);
                mDatabaseReference.addChildEventListener(this);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                uploadsListLatest.clear();
                uploadsListPopular.clear();
                actionMovies.clear();
                superheroMovies.clear();
                scifiMovies.clear();
                fantasyMovies.clear();
                romanceMovies.clear();
                animatedMovies.clear();
                uploadsSlider.clear();
                mDatabaseReference.removeEventListener(this);
                mDatabaseReference.addChildEventListener(this);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initSlider() {
        SliderPagerAdapter adapterNew = new SliderPagerAdapter(mContext, uploadsSlider);
        sliderPager.setAdapter(adapterNew);
        adapterNew.notifyDataSetChanged();

        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new HomeFragment().getActivity().SliderTimer(), 4000, 6000);
        indicator.setupWithViewPager(sliderPager, true);
    }

    private void initWeekMovies() {
        if (init == true) {
            movieItemAdapter = new MovieItemAdapter(getContext(), uploadsListLatest, this);
            recycleViewLatestMovies.setAdapter(movieItemAdapter);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recycleViewLatestMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }
    }

    private void initPopularMovies() {
        if (init == true) {
            movieItemAdapter = new MovieItemAdapter(getContext(), uploadsListPopular, this);
            recyclerViewPopularMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewPopularMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }
    }

    private void moviesViewTab() {
        getActionMovies();
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Action"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("SuperHero"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Sci-fi"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Fantasy"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Romance"));
        tabActionMovies.addTab(tabActionMovies.newTab().setText("Animated"));
        tabActionMovies.setTabGravity(TabLayout.GRAVITY_START);
        tabActionMovies.setTabTextColors(ColorStateList.valueOf(Color.WHITE));

        tabActionMovies.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getActionMovies();
                        break;
                    case 1:
                        getSuperheroMovies();
                        break;
                    case 2:
                        getScifiMovies();
                        break;
                    case 3:
                        getFantasyMovies();
                        break;
                    case 4:
                        getRomanceMovies();
                        break;
                    case 5:
                        getAnimatedMovies();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        getActionMovies();
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getActionMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), actionMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
    }

    private void getSuperheroMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), superheroMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
    }

    private void getScifiMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), scifiMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
    }

    private void getFantasyMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), fantasyMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
    }

    private void getRomanceMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), romanceMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
    }

    private void getAnimatedMovies() {
        movieItemAdapter = new MovieItemAdapter(getContext(), animatedMovies, this);
        movieItemAdapter.notifyDataSetChanged();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tab.setLayoutManager(gridLayoutManager);
        tab.setAdapter(movieItemAdapter);
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

    @Override
    public void onClick(View v) {
        if (v == buttonExplore) {

        }
    }


    private class SliderTimer extends TimerTask {
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderPager.getCurrentItem() < uploadsSlider.size() - 1) {
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
                    } else {
                        sliderPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

}
