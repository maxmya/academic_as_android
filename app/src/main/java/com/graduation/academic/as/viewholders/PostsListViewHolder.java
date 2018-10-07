package com.graduation.academic.as.viewholders;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.academic.as.R;
import com.varunest.sparkbutton.SparkButton;

import at.blogc.android.views.ExpandableTextView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListViewHolder extends RecyclerView.ViewHolder {

    public ExpandableTextView body;

    public TextView userName;
    public CircleImageView profilePicture;
    public TextView likes;
    public SparkButton likeUp;
    public TextView time;
    public ImageView postImage;
    public SparkButton commentUp;
    public TextView comments;
    public TextView seeMore;

    public PostsListViewHolder(@NonNull View itemView) {
        super(itemView);
        body = itemView.findViewById(R.id.post_body);
        userName = itemView.findViewById(R.id.user_name);
        profilePicture = itemView.findViewById(R.id.user_pp);
        likes = itemView.findViewById(R.id.likes);
        likeUp = itemView.findViewById(R.id.like_up);
        commentUp = itemView.findViewById(R.id.comment);
        comments = itemView.findViewById(R.id.comment_up);
        time = itemView.findViewById(R.id.time);
        postImage = itemView.findViewById(R.id.post_image_item);
        seeMore = itemView.findViewById(R.id.expand_see_more);
    }
}
