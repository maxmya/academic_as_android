package com.graduation.academic.as.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.academic.as.R;
import com.varunest.sparkbutton.SparkButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListViewHolder extends RecyclerView.ViewHolder {

    public TextView body;
    public TextView userName;
    public CircleImageView profilePicture;
    public TextView likes;
    public SparkButton likeUp;

    public PostsListViewHolder(@NonNull View itemView) {
        super(itemView);
        body = itemView.findViewById(R.id.post_body);
        userName = itemView.findViewById(R.id.user_name);
        profilePicture = itemView.findViewById(R.id.user_pp);
        likes = itemView.findViewById(R.id.likes);
        likeUp = itemView.findViewById(R.id.like_up);
    }
}
