package com.graduation.academic.as.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.graduation.academic.as.R;

public class GroupListViewHolder extends RecyclerView.ViewHolder {


    public TextView groupName;
    public Button open;
    public ImageView groupImage;

    public GroupListViewHolder(@NonNull View itemView) {
        super(itemView);
        groupName = (TextView) itemView.findViewById(R.id.group_name);
        open = (Button) itemView.findViewById(R.id.edit);
        groupImage = (ImageView) itemView.findViewById(R.id.g_image);
    }

}
