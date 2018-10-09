package com.graduation.academic.as.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.graduation.academic.as.models.Comment;
import com.graduation.academic.as.viewholders.CommentListViewHolder;
import com.graduation.academic.as.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Omar Wael on 10/5/2018.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListViewHolder> {

    public List<Comment> comments;


    public CommentListAdapter(List<Comment> comments) {
        this.comments = comments;
    }

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
        commentListViewHolder.time.setText(TimeAgo.using(comments.get(i).getTimestamp()));
        commentListViewHolder.userName.setText(c.getOwner());
        Picasso.get().load(comments.get(i).getPpUrl()).into(commentListViewHolder.profilePicture);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addItem(Comment comment) {
        comments.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
