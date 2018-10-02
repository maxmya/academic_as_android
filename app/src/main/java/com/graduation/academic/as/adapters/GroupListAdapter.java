package com.graduation.academic.as.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.graduation.academic.as.R;
import com.graduation.academic.as.activities.GroupActivity;
import com.graduation.academic.as.models.Group;
import com.graduation.academic.as.viewholders.GroupListViewHolder;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListViewHolder> {


    ArrayList<Group> groups;

    public GroupListAdapter(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_list, viewGroup, false);
        GroupListViewHolder viewHolder = new GroupListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroupListViewHolder groupListViewHolder, int i) {
        groupListViewHolder.groupName.append(groups.get(i).getGroupName());
        final Context context = groupListViewHolder.open.getContext().getApplicationContext();
        final int groupPosition = i;
        groupListViewHolder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupActivity = new Intent(context, GroupActivity.class);
                groupActivity.putExtra("group_id", groups.get(groupPosition).getGroupId());
                context.startActivity(groupActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}