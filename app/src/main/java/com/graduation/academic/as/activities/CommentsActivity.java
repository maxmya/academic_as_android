package com.graduation.academic.as.activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.App;
import com.graduation.academic.as.R;
import com.graduation.academic.as.adapters.CommentListAdapter;
import com.graduation.academic.as.handlers.CommentsHandler;
import com.graduation.academic.as.models.Comment;
import com.graduation.academic.as.models.User;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiImageView;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiClickListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;
import com.wang.avi.AVLoadingIndicatorView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class CommentsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Comment> comments;
    private AVLoadingIndicatorView loadingIndicatorView;
    private AVLoadingIndicatorView commentLoadingIndicatorView;

    private static final String TAG = CommentsActivity.class.getSimpleName();
    private EmojiPopup emojiPopup;
    private ImageView emojiButton;
    private EmojiEditText commentBody;
    private ViewGroup rootView;
    private LinearLayoutManager layoutManager;
    private CommentListAdapter commentListAdapter;
    private ImageView addComment;
    private CommentsHandler commentsHandler;

    private String groupId;
    private String postId;
    private User myUser;

    Map<String, String> realtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        comments = new ArrayList<>();
        groupId = getIntent().getStringExtra("group_id");
        postId = getIntent().getStringExtra("post_id");
        myUser = User.restore(App.sPrefs);

        initViews();
        initRecyclerView();
        commenting();
    }


    private void initViews() {
        recyclerView = findViewById(R.id.comments_list);
        loadingIndicatorView = findViewById(R.id.loading);
        emojiButton = findViewById(R.id.main_activity_emoji);
        commentBody = findViewById(R.id.add_comment_body);
        rootView = findViewById(R.id.main_activity_root_view);
        addComment = findViewById(R.id.add_comment_button);
        addComment.setOnClickListener(this);
        commentLoadingIndicatorView = findViewById(R.id.comment_loading);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPositionWithOffset(commentListAdapter.getItemCount() - 1, 0);
                        }
                    }, 50);
                }
            }
        });
        setUpEmojiPopup();
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emojiPopup.toggle();
            }
        });
    }


    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.removeAllViews();
        recyclerView.setLayoutManager(layoutManager);
        commentsHandler = new CommentsHandler(groupId, postId);
        //commentListAdapter = new CommentListAdapter(commentsHandler.getComments());
        // commentsHandler.connect();
        comments = new ArrayList<>();
        String collectionPath = "/groups/" + groupId + "/subdata/" + postId + "/comments";
        commentListAdapter = new CommentListAdapter(comments);
        FirebaseFirestore.getInstance().collection(collectionPath).orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                loadingIndicatorView.hide();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED: {
                            Comment currentComment = convertSnapDocumentToComment(dc.getDocument());

                            if (!comments.contains(currentComment)) {
                                comments.add(currentComment);
                            } else {
                                int index = comments.indexOf(currentComment);
                                comments.set(index, currentComment);
                            }
                            commentListAdapter.notifyDataSetChanged();
                            layoutManager.scrollToPositionWithOffset(commentListAdapter.getItemCount() - 1, 0);
                        }
                    }

                }

            }
        });
        recyclerView.setAdapter(commentListAdapter);
    }

    private Comment convertSnapDocumentToComment(DocumentSnapshot snapshot) {
        Comment comment = new Comment();

        comment.setBody((String) snapshot.get("body"));
        comment.setOwner((String) snapshot.get("owner"));
        comment.setPostId((String) snapshot.get("postId"));
        comment.setPpUrl((String) snapshot.get("ppUrl"));
        comment.setTimestamp((Long) snapshot.get("timestamp"));
        comment.setUid((String) snapshot.get("uid"));

        return comment;
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override
                    public void onEmojiBackspaceClick(final View v) {
                        Log.d(TAG, "Clicked on Backspace");
                    }
                })
                .setOnEmojiClickListener(new OnEmojiClickListener() {
                    @Override
                    public void onEmojiClick(@NonNull final EmojiImageView imageView, @NonNull final Emoji emoji) {
                        Log.d(TAG, "Clicked on emoji");
                    }
                })
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override
                    public void onEmojiPopupShown() {
                        emojiButton.setImageResource(org.chat21.android.R.drawable.ic_keyboard_24dp);
                    }
                })
                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
                    @Override
                    public void onKeyboardOpen(@Px final int keyBoardHeight) {
                        Log.d(TAG, "Opened soft keyboard");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override
                    public void onEmojiPopupDismiss() {
                        emojiButton.setImageResource(org.chat21.android.R.drawable.emoji_ios_category_people);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override
                    public void onKeyboardClose() {
                        Log.d(TAG, "Closed soft keyboard");
                    }
                })
                .build(commentBody);
    }

    private void commenting() {
        String path = "/groups/" + groupId + "/subdata/" + postId + "/comments";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference cr = db.collection(path);
        cr.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case MODIFIED: {
                            Log.i(TAG, dc.getDocument().getId());
                            if (dc.getDocument().getId().equals("commenters")) {
                                realtime = (Map<String, String>) dc.getDocument().get("realtime");
                                if (realtime != null && realtime.size() == 1)
                                    if (realtime.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                        return;
                                if (realtime != null && realtime.size() >= 1) {
                                    findViewById(R.id.commenting).setVisibility(View.VISIBLE);
                                } else {
                                    findViewById(R.id.commenting).setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }

            }
        });

        final DocumentReference dr = cr.document("commenters");

        commentBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (realtime != null) {
                    realtime.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), "1");
                    Map<String, Object> realtimeCommenters = new HashMap<>();
                    realtimeCommenters.put("realtime", realtime);
                    dr.set(realtimeCommenters);
                } else {
                    realtime = new HashMap<>();
                    realtime.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), "1");
                    Map<String, Object> realtimeCommenters = new HashMap<>();
                    realtimeCommenters.put("realtime", realtime);
                    dr.set(realtimeCommenters);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (realtime != null) {
                            realtime.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Map<String, Object> realtimeCommenters = new HashMap<>();
                            realtimeCommenters.put("realtime", realtime);
                            dr.set(realtimeCommenters);
                        }
                    }
                }, 3000);

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_comment_button) {
            String comment = commentBody.getText().toString().trim();
            if (TextUtils.isEmpty(comment))
                return;
            commentLoadingIndicatorView.setVisibility(View.VISIBLE);
            String collectionPath = "/groups/" + groupId + "/subdata/" + postId + "/comments";
            Comment newComment = new Comment();
            newComment.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            newComment.setTimestamp(System.currentTimeMillis());
            newComment.setPpUrl(myUser.getPpURL());
            newComment.setOwner(myUser.getName());
            newComment.setPostId(postId);
            newComment.setBody(comment);
            commentBody.setText("");
            FirebaseFirestore.getInstance().collection(collectionPath).add(newComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    commentLoadingIndicatorView.setVisibility(View.GONE);
                }
            });
        }
    }
}
