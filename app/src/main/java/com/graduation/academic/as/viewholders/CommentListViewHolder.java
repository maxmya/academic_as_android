package com.graduation.academic.as.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.graduation.academic.as.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Omar Wael on 10/5/2018.
 */

public class CommentListViewHolder extends RecyclerView.ViewHolder {


    public TextView body;
    public TextView userName;
    public CircleImageView profilePicture;
    public TextView time;

    public CommentListViewHolder(@NonNull View itemView) {
        super(itemView);
        body = itemView.findViewById(R.id.comment_body);
        userName = itemView.findViewById(R.id.comment_user_name);
        profilePicture = itemView.findViewById(R.id.comment_user_pp);
        time = itemView.findViewById(R.id.comment_time);

    }
}
