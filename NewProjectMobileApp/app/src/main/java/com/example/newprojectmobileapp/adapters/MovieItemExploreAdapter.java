package com.example.newprojectmobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.model.MovieItemClickListener;
import com.example.newprojectmobileapp.model.Video;

import java.util.ArrayList;
import java.util.List;

public class MovieItemExploreAdapter extends RecyclerView.Adapter<MovieItemExploreAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Video> uploads;
    private List<Video> uploadsOld;
    MovieItemClickListener movieItemClickListener;

    public MovieItemExploreAdapter(Context mContext, List<Video> uploads, MovieItemClickListener movieItemClickListener) {
        this.mContext = mContext;
        this.uploads = uploads;
        this.uploadsOld = uploads;
        this.movieItemClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_grid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemExploreAdapter.MyViewHolder holder, int position) {
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


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty()) {
                    uploads = uploadsOld;
                } else {
                    List<Video> list = new ArrayList<>();
                    for (Video video : uploadsOld) {
                        if (video.getVideoTitle().toLowerCase().contains(search.toLowerCase())) {
                            list.add(video);
                        }
                    }
                    uploads = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = uploads;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                uploads = (List<Video>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
