package com.example.newprojectmobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.model.SliderSide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context mContext;
    List<SliderSide> mList;

    public SliderPagerAdapter(Context mContext, List<SliderSide> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View slideLayout = inflater.inflate(R.layout.slide_item, null);
        ImageView slideImage = slideLayout.findViewById(R.id.slide_img);
        TextView textViewMovieName = slideLayout.findViewById(R.id.text_view_movie_name);
        TextView textViewMovieDescription = slideLayout.findViewById(R.id.text_view_movie_description);
        FloatingActionButton buttonExploreMovie = slideLayout.findViewById(R.id.button_explore_movie);

        Glide.with(mContext).load(mList.get(position).getVideoThumbnail()).into(slideImage);
        textViewMovieName.setText(mList.get(position).getVideoTitle());
        textViewMovieDescription.setText(mList.get(position).getVideoDescription());

        container.addView(slideLayout);
        return slideLayout;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
