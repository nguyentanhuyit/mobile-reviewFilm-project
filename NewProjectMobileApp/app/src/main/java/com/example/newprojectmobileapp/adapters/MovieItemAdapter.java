package com.example.newprojectmobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.Video;

import java.util.List;

public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.MyViewHolder> {

    private Context mContext;
    private List<Video> uploads;
    MovieItemClickListener movieItemClickListener;

    public MovieItemAdapter(Context mContext, List<Video> uploads, MovieItemClickListener movieItemClickListener) {
        this.mContext = mContext;
        this.uploads = uploads;
        this.movieItemClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemAdapter.MyViewHolder holder, int position) {
        Video video = uploads.get(position);
        if (video == null) {
            return;
        }
        holder.textViewItemMovieTitle.setText(video.getVideoTitle());
        Glide.with(mContext).load(video.getVideoThumbnail()).into(holder.imageViewItemMovie);
    }

    @Override
    public int getItemCount() {
        if (uploads != null) {
            return uploads.size();
        }
        return 0;
    }


    /////////////////////

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemMovieTitle;
        ImageView imageViewItemMovie;
//        CardView cardViewMovieItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemMovieTitle = itemView.findViewById(R.id.text_view_item_movie_title);
            imageViewItemMovie = itemView.findViewById(R.id.image_view_item_movie);
//            cardViewMovieItem = itemView.findViewById(R.id.card_view_movie_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieItemClickListener.onMovieClick(uploads.get(getAdapterPosition()), imageViewItemMovie);
                }
            });
        }
    }
}
