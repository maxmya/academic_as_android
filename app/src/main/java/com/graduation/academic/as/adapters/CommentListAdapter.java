package com.graduation.academic.as.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.academic.as.models.Comment;
import com.graduation.academic.as.viewholders.CommentListViewHolder;
import com.graduation.academic.as.viewholders.GroupListViewHolder;
import com.graduation.academic.as.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;

/**
 * Created by Omar Wael on 10/5/2018.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListViewHolder> {

    ArrayList<Comment> comments;

    @Override
    public CommentListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        CommentListViewHolder viewHolder = new CommentListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListViewHolder commentListViewHolder, int i) {

        Comment c = comments.get(i);
        commentListViewHolder.body.setText(c.getBody());
        commentListViewHolder.time.setText(DateFormat.getInstance().format(comments.get(i).getTimestamp()));
        commentListViewHolder.userName.setText(c.getOwner());
        Picasso.get().load(comments.get(i).getPpUrl()).into(commentListViewHolder.profilePicture);
        // Todo :attach comment to a post

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
