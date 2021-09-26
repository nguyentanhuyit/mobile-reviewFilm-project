package com.example.newprojectmobileapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newprojectmobileapp.R;
import com.example.newprojectmobileapp.model.Comment;

import java.util.List;

public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.CommentItemViewHolder> {

    Context mContext;
    List<Comment> comments;

    public CommentItemAdapter(Context mContext, List<Comment> comments) {
        this.mContext = mContext;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.textViewUserName.setText(comment.getUserName());
        holder.textViewCommentTime.setText(comment.getCommentTime());
        holder.textViewCommentContent.setText(comment.getCommentContent());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewAvatar;
        TextView textViewUserName, textViewCommentTime, textViewCommentContent,  textViewCommentCountLike;
        RadioButton radioButtonLike;

        public CommentItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.image_view_avatar);
            textViewUserName = itemView.findViewById(R.id.text_view_user_name);
            textViewCommentTime = itemView.findViewById(R.id.text_view_commnet_time);
            textViewCommentContent = itemView.findViewById(R.id.text_view_comment_content);
            textViewCommentCountLike = itemView.findViewById(R.id.text_view_comment_count_like);
            radioButtonLike = itemView.findViewById(R.id.radio_like);
        }
    }
}
