package com.graduation.academic.as.activities;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.R;
import com.graduation.academic.as.adapters.GroupListAdapter;
import com.graduation.academic.as.models.Group;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Map;

public class GroupsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = GroupsListActivity.class.getSimpleName();
    private AVLoadingIndicatorView loadingIndicatorView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);
        initViews();
        initList();
    }

    private void initViews() {
        loadingIndicatorView = findViewById(R.id.loading);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        errorLayout = findViewById(R.id.error);
    }

    private void initList() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                ArrayList<Group> groups = new ArrayList<>();
                for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                    Group currentGroup = new Group();
                    currentGroup.setGroupId(doc.getId());
                    currentGroup.setGroupName(((Map<String, String>) doc.get("metadata")).get("name"));
                    currentGroup.setGroupImage(((Map<String, String>) doc.get("metadata")).get("groupImage"));
                    groups.add(currentGroup);
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.removeAllViews();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new GroupListAdapter(groups));
                loadingIndicatorView.hide();
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingIndicatorView.hide();
                errorLayout.setVisibility(View.VISIBLE);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onRefresh() {
        initList();
    }
}
