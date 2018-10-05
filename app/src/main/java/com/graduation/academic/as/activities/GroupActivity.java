package com.graduation.academic.as.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.R;
import com.graduation.academic.as.adapters.PostListAdapter;
import com.graduation.academic.as.listners.OnVerticalScrollListener;
import com.graduation.academic.as.models.Post;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = GroupActivity.class.getSimpleName();
    // ui
    private CircleImageView userPP;
    private TextView userName;
    private EditText newPost;
    private LoadingButton post;
    private FloatingActionButton floatingActionButton;
    private ExpandableLayout expandableLayout;
    private ExpandableLayout appbarExpandable;
    private TextView groupNameTextView;
    private AVLoadingIndicatorView loadingIndicatorView;
    private LinearLayout errorLayout;

    // firebase
    private FirebaseFirestore db;

    // shared fields
    private String groupId;
    private String uid;
    private String ppUrl;
    private String fullName;
    private String groupName;

    // Adapters
    private PostListAdapter myPostsAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        db = FirebaseFirestore.getInstance();
        groupId = getIntent().getStringExtra("group_id");
        groupName = getIntent().getStringExtra("group_name");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initViews();
        initList(groupId);
        groupNameTextView.setText(groupName);
    }

    private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrolledUp() {
                super.onScrolledUp();
                appbarExpandable.expand();
            }

            @Override
            public void onScrolledDown() {
                super.onScrolledDown();
                appbarExpandable.collapse();
            }
        });
    }

    private void initList(String groupId) {
        try {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("/groups/" + groupId + "/posts").orderBy("timestamp", Query.Direction.DESCENDING)
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
                        currentPost.setTimestamp((long) doc.get("timestamp"));
                        posts.add(currentPost);
                    }
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                    recyclerView.removeAllViews();
                    recyclerView.setLayoutManager(layoutManager);
                    myPostsAdapter = new PostListAdapter(posts);
                    recyclerView.setAdapter(myPostsAdapter);
                    loadingIndicatorView.hide();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    loadingIndicatorView.hide();
                }
            });

        } catch (Exception e) {
            loadingIndicatorView.hide();
            e.printStackTrace();
            errorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        userPP = (CircleImageView) findViewById(R.id.my_user_pp);
        userName = (TextView) findViewById(R.id.my_user_name);
        newPost = (EditText) findViewById(R.id.my_post_body);
        post = (LoadingButton) findViewById(R.id.post);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_post);
        expandableLayout = (ExpandableLayout) findViewById(R.id.expandable_layout);
        appbarExpandable = (ExpandableLayout) findViewById(R.id.appbar_expandable);
        recyclerView = (RecyclerView) findViewById(R.id.posts_list);
        groupNameTextView = (TextView) findViewById(R.id.group_title);
        loadingIndicatorView = findViewById(R.id.loading);
        errorLayout = findViewById(R.id.error);
        post.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        db.collection("users").document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                fullName = (String) documentSnapshot.get("fullname");
                ppUrl = (String) documentSnapshot.get("ppURL");
                userName.setText(fullName);
                Picasso.get().load(ppUrl).placeholder(R.drawable.user).into(userPP);
            }
        });
        setRecyclerViewScrollListener();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.post: {
                if (TextUtils.isEmpty(newPost.getText().toString()))
                    return;
                try {
                    post.startLoading();
                    Post myPost = new Post();
                    myPost.setPostId("--");
                    myPost.setOwner(fullName);
                    myPost.setPpURL(ppUrl);
                    myPost.setBody(newPost.getText().toString());
                    myPost.setGroupId(groupId);
                    myPost.setLikers(new HashMap<String, String>());
                    myPost.setLikes("0");
                    myPost.setTimestamp(System.currentTimeMillis());
                    db.collection("/groups/" + groupId + "/posts").add(myPost)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    post.loadingSuccessful();
                                    initList(groupId);
                                    newPost.setText("");
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            post.reset();
                                            expandableLayout.collapse();
                                        }
                                    }, 2000);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            post.cancelLoading();
                            Toast.makeText(GroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.floating_post: {
                if (expandableLayout.isExpanded()) expandableLayout.collapse();
                else expandableLayout.expand();
            }
        }
    }


}