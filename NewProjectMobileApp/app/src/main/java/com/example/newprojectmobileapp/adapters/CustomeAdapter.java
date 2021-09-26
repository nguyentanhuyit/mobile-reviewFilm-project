package com.example.newprojectmobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.model.DocBao;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomeAdapter extends ArrayAdapter<DocBao> {

    public CustomeAdapter(Context context, int resource, List<DocBao> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.dong_layout_listview, null);
        }
        DocBao p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txttitle = (TextView) view.findViewById(R.id.textviewtitle);
            txttitle.setText(p.title);
            ImageView imageView = view.findViewById(R.id.ImageView);
            Picasso.with(getContext()).load(p.image).into(imageView);


        }
        return view;
    }

}