package com.graduation.academic.as.viewholders;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devs.readmoreoption.ReadMoreOption;
import com.graduation.academic.as.R;
import com.varunest.sparkbutton.SparkButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListViewHolder extends RecyclerView.ViewHolder {

    public TextView body;

    public TextView userName;
    public CircleImageView profilePicture;
    public TextView likes;
    public SparkButton likeUp;
    public TextView time;
    public ReadMoreOption readMoreOption;
    public ImageView postImage;
    public SparkButton commentUp;
    public TextView comments;

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
        readMoreOption = new ReadMoreOption.Builder(itemView.getContext())
                .textLength(3, ReadMoreOption.TYPE_LINE)
                .moreLabel("see more")
                .lessLabel("see less")
                .moreLabelColor(Color.RED)
                .lessLabelColor(Color.BLUE)
                .labelUnderLine(true)
                .build();
    }
}
