package com.example.newprojectmobileapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newprojectmobileapp.MoviePlayerActivity;
import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.adapters.CommentItemAdapter;
import com.example.newprojectmobileapp.adapters.MovieItemAdapter;
import com.example.newprojectmobileapp.model.Comment;
import com.example.newprojectmobileapp.model.LikeMovie;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.Video;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDetailFragment extends Fragment implements MovieItemClickListener, View.OnClickListener {

    View view;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference videosDatabaseRef;
    DatabaseReference commentsDatabaseRef;
    DatabaseReference likesDatabaseRef;
    DatabaseReference wishListDatabaseRef;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ImageView movieDetailMainThumbnail, movieDetailThumbnail;
    TextView movieDetailTitle, movieDetailDirector, movieDetailMainActor, movieDetailNation,
            movieDetailYear, movieDetailLength, movieDetailDescription, movieDetailView;
    FloatingActionButton playFab;

    RecyclerView recyclerViewSimilarMovies, recyclerViewComment;
    MovieItemAdapter movieItemAdapter;
    List<Video> actionMovies, superheroMovies, scifiMovies, fantasyMovies, romanceMovies, animatedMovies;

    String currentVideoId;
    String currentVideoName;
    String currentVideoUrl;
    String currentVideoCategory;
    String currentVideoType;
    String currentVideoThumbnail;
    boolean currentVideoSlide;
    String currentVideoTitle;
    String currentVideoDirector;
    String currentVideoMainActor;
    String currentVideoNation;
    String currentVideoYear;
    String currentVideoLength;
    String currentVideoDescription;
    int currentVideoView;

    // Like, wish list
    Button buttonWish, buttonLike, buttonShare, buttonDownload;
    boolean liked;
    boolean wished;

    // Comment
    EditText editTextComment;
    Button buttonSendComment;

    CommentItemAdapter commentItemAdapter;
    List<Comment> comments;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        videosDatabaseRef = firebaseDatabase.getReference("videos");
        commentsDatabaseRef = firebaseDatabase.getReference("comments");
        likesDatabaseRef = firebaseDatabase.getReference("likes");
        wishListDatabaseRef = firebaseDatabase.getReference("wishList");
        currentUser = mAuth.getCurrentUser();


        MovieDetailFragment movieDetailFragment = this;

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.remove(movieDetailFragment);
                fragmentTransaction.commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        initView();

        getData();
        getSimilarMovies();
        getLikeData();
        getWishListData();
        getCommentData();
        initRecycleViewComment();

        buttonSendComment.setOnClickListener(this);
        playFab.setOnClickListener(this);

        return view;
    }

    private void initView() {

        playFab = view.findViewById(R.id.play_fab);
        movieDetailMainThumbnail = view.findViewById(R.id.movie_detail_main_thumbnail);
        movieDetailThumbnail = view.findViewById(R.id.movie_detail_thumbnail);
        movieDetailTitle = view.findViewById(R.id.movie_detail_title);
        movieDetailDirector = view.findViewById(R.id.movie_detail_director);
        movieDetailMainActor = view.findViewById(R.id.movie_detail_main_actor);
        movieDetailNation = view.findViewById(R.id.movie_detail_nation);
        movieDetailYear = view.findViewById(R.id.movie_detail_year);
        movieDetailLength = view.findViewById(R.id.movie_detail_length);
        movieDetailDescription = view.findViewById(R.id.movie_detail_description);
        movieDetailView = view.findViewById(R.id.movie_detail_view);

        recyclerViewSimilarMovies = view.findViewById(R.id.recycler_similar_movies);
        recyclerViewComment = view.findViewById(R.id.recycler_view_comment);

        buttonWish = view.findViewById(R.id.button_wish);
        buttonLike = view.findViewById(R.id.button_like);
        buttonShare = view.findViewById(R.id.button_share);
        buttonDownload = view.findViewById(R.id.button_download);

        buttonWish.setOnClickListener(this);
        buttonLike.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonDownload.setOnClickListener(this);

        editTextComment = view.findViewById(R.id.edit_text_comment);
        buttonSendComment = view.findViewById(R.id.button_send_comment);

        //
        Video receiveVideoDetail = (Video) getArguments().getSerializable("object_video");

        String videoId = receiveVideoDetail.getVideoId();
        String videoName = receiveVideoDetail.getVideoName();
        String videoUrl = receiveVideoDetail.getVideoUrl();
        String videoCategory = receiveVideoDetail.getVideoCategory();
        String videoType = receiveVideoDetail.getVideoType();
        String videoThumbnail = receiveVideoDetail.getVideoThumbnail();
        boolean videoSlide = receiveVideoDetail.isVideoSlide();
        String videoTitle = receiveVideoDetail.getVideoTitle();
        String videoDirector = receiveVideoDetail.getVideoDirector();
        String videoMainActor = receiveVideoDetail.getVideoMainActor();
        String videoNation = receiveVideoDetail.getVideoNation();
        String videoYear = receiveVideoDetail.getVideoYear();
        String videoLength = receiveVideoDetail.getVideoLength();
        String videoDescription = receiveVideoDetail.getVideoDescription();
        int videoView = receiveVideoDetail.getVideoView();

        movieDetailTitle.setText(videoTitle);
        movieDetailDirector.setText(videoDirector);
        movieDetailMainActor.setText(videoMainActor);
        movieDetailNation.setText(videoNation);
        movieDetailYear.setText(videoYear);
        movieDetailLength.setText(videoLength);
        movieDetailDescription.setText(videoDescription);
        movieDetailView.setText(videoView + "");

        currentVideoId = videoId;
        currentVideoName = videoName;
        currentVideoUrl = videoUrl;
        currentVideoCategory = videoCategory;
        currentVideoType = videoType;
        currentVideoThumbnail = videoThumbnail;
        currentVideoSlide = videoSlide;
        currentVideoTitle = videoTitle;
        currentVideoDirector = videoDirector;
        currentVideoMainActor = videoMainActor;
        currentVideoNation = videoNation;
        currentVideoYear = videoYear;
        currentVideoLength = videoLength;
        currentVideoDescription = videoDescription;
        currentVideoView = videoView;

        Glide.with(getActivity()).load(videoThumbnail).into(movieDetailMainThumbnail);
        Glide.with(getActivity()).load(videoThumbnail).into(movieDetailThumbnail);

    }

    private void getData() {

//        uploads = new ArrayList<>();
        actionMovies = new ArrayList<>();
        superheroMovies = new ArrayList<>();
        scifiMovies = new ArrayList<>();
        fantasyMovies = new ArrayList<>();
        romanceMovies = new ArrayList<>();
        animatedMovies = new ArrayList<>();

        videosDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Video upload = postSnapshot.getValue(Video.class);
                    if (upload.getVideoCategory().equals("Action") && !upload.getVideoId().equals(currentVideoId)) {
                        actionMovies.add(upload);
                    }
                    if (upload.getVideoCategory().equals("Superhero") && !upload.getVideoId().equals(currentVideoId)) {
                        superheroMovies.add(upload);
                    }
                    if (upload.getVideoCategory().equals("Sci-fi") && !upload.getVideoId().equals(currentVideoId)) {
                        scifiMovies.add(upload);
                    }
                    if (upload.getVideoCategory().equals("Fantasy") && !upload.getVideoId().equals(currentVideoId)) {
                        fantasyMovies.add(upload);
                    }
                    if (upload.getVideoCategory().equals("Romance") && !upload.getVideoId().equals(currentVideoId)) {
                        romanceMovies.add(upload);
                    }
                    if (upload.getVideoCategory().equals("Animated") && !upload.getVideoId().equals(currentVideoId)) {
                        animatedMovies.add(upload);
                    }
                }
                movieItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getSimilarMovies() {
        if (currentVideoCategory.equals("Action")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), actionMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }

        if (currentVideoCategory.equals("Superhero")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), superheroMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }

        if (currentVideoCategory.equals("Sci-fi")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), scifiMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }

        if (currentVideoCategory.equals("Fantasy")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), fantasyMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }

        if (currentVideoCategory.equals("Romance")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), romanceMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }

        if (currentVideoCategory.equals("Animated")) {
            movieItemAdapter = new MovieItemAdapter(getContext(), animatedMovies, this);
            recyclerViewSimilarMovies.setAdapter(movieItemAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSimilarMovies.setLayoutManager(linearLayoutManager);
            movieItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMovieClick(Video video, ImageView imageView) {
        movieDetailTitle.setText(video.getVideoTitle());
        Glide.with(this).load(video.getVideoThumbnail()).into(movieDetailMainThumbnail);
        Glide.with(this).load(video.getVideoThumbnail()).into(movieDetailThumbnail);

        movieDetailTitle.setText(video.getVideoTitle());
        movieDetailDirector.setText(video.getVideoDirector());
        movieDetailMainActor.setText(video.getVideoMainActor());
        movieDetailNation.setText(video.getVideoNation());
        movieDetailYear.setText(video.getVideoYear());
        movieDetailLength.setText(video.getVideoLength());
        movieDetailDescription.setText(video.getVideoDescription());
        movieDetailView.setText(video.getVideoView() + "");

        currentVideoUrl = video.getVideoUrl();
        currentVideoCategory = video.getVideoCategory();
    }

    private void getCommentData() {
        comments = new ArrayList<>();
        commentsDatabaseRef.child(currentVideoTitle).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                if (comment.getMovieTitle().equals(currentVideoTitle)) {
                    comments.add(comment);
                }
                commentItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                comments.clear();
                commentsDatabaseRef.child(currentVideoTitle).removeEventListener(this);
                commentsDatabaseRef.child(currentVideoTitle).addChildEventListener(this);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                comments.clear();
                commentsDatabaseRef.child(currentVideoTitle).removeEventListener(this);
                commentsDatabaseRef.child(currentVideoTitle).addChildEventListener(this);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initRecycleViewComment() {
        commentItemAdapter = new CommentItemAdapter(getContext(), comments);
        commentItemAdapter.notifyDataSetChanged();
        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewComment.setLayoutManager(llm);
        recyclerViewComment.setAdapter(commentItemAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == playFab) {
            playMovie();
        } else if (v == buttonWish) {
            addToWishList();
        } else if (v == buttonLike) {
            like();
        } else if (v == buttonSendComment) {
            sendComment();
        }
    }

    private void playMovie() {
        int view = currentVideoView + 1;
        DatabaseReference viewDBRef = videosDatabaseRef.child(currentVideoId).child("videoView");
        viewDBRef.setValue(view);

        Intent intent = new Intent(getActivity(), MoviePlayerActivity.class);
        intent.putExtra("videoUri", currentVideoUrl);
        getActivity().startActivity(intent);
    }

    private void like() {
        String userEmail = currentUser.getEmail();
        LikeMovie likeMovie;
        if (liked == false) {
            likeMovie = new LikeMovie(userEmail, true);
        } else {
            likeMovie = new LikeMovie(userEmail, false);
        }
        likesDatabaseRef.child(currentVideoTitle).child(currentUser.getUid()).setValue(likeMovie);
    }

    private void addToWishList() {
        String userUid = currentUser.getUid();
        Video video = new Video(currentVideoId, currentVideoName, currentVideoUrl, currentVideoCategory,
                currentVideoType, currentVideoThumbnail, currentVideoSlide, currentVideoTitle, currentVideoDirector,
                currentVideoMainActor, currentVideoNation, currentVideoYear, currentVideoLength,
                currentVideoDescription, currentVideoView);
        if (wished == false) {
            wishListDatabaseRef.child(userUid).child(currentVideoId).setValue(video);
        } else {
            wishListDatabaseRef.child(userUid).child(currentVideoId).removeValue();
        }
    }

    private void sendComment() {
        DatabaseReference videoTitleCommentDatabaseRef = commentsDatabaseRef.child(currentVideoTitle);
        String commentContent = editTextComment.getText().toString().trim();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date currentTime = new Date();
        Comment comment = new Comment(currentUser.getUid(), currentUser.getEmail(), currentVideoTitle, commentContent, simpleDateFormat.format(currentTime), 0);
        videoTitleCommentDatabaseRef.child(videoTitleCommentDatabaseRef.push().getKey()).setValue(comment);
    }


    private void getLikeData() {
        likesDatabaseRef.child(currentVideoTitle).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LikeMovie likeMovie = dataSnapshot.getValue(LikeMovie.class);
                    if (likeMovie.isLike() == true) {
                        liked = true;
                        buttonLike.setText("Liked");
                        buttonLike.setTextColor(R.color.hulu_green_3);
                    } else if (likeMovie.isLike() == false) {
                        liked = false;
                        buttonLike.setText("Like");
                        buttonLike.setTextColor(R.color.white);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getWishListData() {
        wishListDatabaseRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean ok = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Video video = dataSnapshot.getValue(Video.class);
                    if (video.getVideoId().equals(currentVideoId)) {
                        ok = true;
                    }
                    if (ok == true) {
                        wished = true;
                        buttonWish.setText("Added");
                        buttonWish.setTextColor(R.color.hulu_green_3);
                    } else if (ok == false) {
                        wished = false;
                        buttonWish.setText("Wish List");
                        buttonWish.setTextColor(R.color.white);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
