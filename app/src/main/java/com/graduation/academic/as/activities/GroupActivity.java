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
import com.graduation.academic.as.adapters.PostListAdapter;
import com.graduation.academic.as.models.Group;
import com.graduation.academic.as.models.Post;

import java.util.ArrayList;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {

    private static final String TAG = GroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        String groupId = getIntent().getStringExtra("group_id");
        initList(groupId);
    }

    private void initList(String groupId) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("/groups/" + groupId + "/posts")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                ArrayList<Post> posts = new ArrayList<>();
                for (DocumentSnapshot doc : documentSnapshots.getDocuments()) {
                    Post currentPost = new Post();
                    currentPost.setPostId(doc.getId());
                    currentPost.setOwner((String) doc.get("owner"));
                    currentPost.setBody((String) doc.get("body"));
                    currentPost.setPpURL((String) doc.get("ppURL"));
                    currentPost.setLikes((String) doc.get("likes"));
                    currentPost.setGroupId((String) doc.get("groupId"));
                    posts.add(currentPost);
                }
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.posts_list);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.removeAllViews();
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new PostListAdapter(posts));
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