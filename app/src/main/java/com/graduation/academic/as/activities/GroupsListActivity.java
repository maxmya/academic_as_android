package com.graduation.academic.as.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.R;
import com.graduation.academic.as.adapters.GroupListAdapter;
import com.graduation.academic.as.models.Group;

import java.util.ArrayList;
import java.util.Map;

public class GroupsListActivity extends AppCompatActivity {

    private static final String TAG = GroupsListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);

        initList();
    }


    private void initList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                ArrayList<Group> groups = new ArrayList<>();
                for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                    Group currentGroup = new Group();
                    currentGroup.setGroupId(doc.getId());
                    currentGroup.setGroupName(((Map<String, String>) doc.get("metadata")).get("name"));
                    groups.add(currentGroup);
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.group_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.removeAllViews();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new GroupListAdapter(groups));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
