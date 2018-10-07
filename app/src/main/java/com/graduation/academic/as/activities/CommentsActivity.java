package com.graduation.academic.as.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.App;
import com.graduation.academic.as.R;
import com.graduation.academic.as.adapters.CommentListAdapter;
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

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> comments;
    private AVLoadingIndicatorView loadingIndicatorView;
    private LinearLayout linearLayout;
    private static final String TAG = CommentsActivity.class.getSimpleName();
    private EmojiPopup emojiPopup;
    private ImageView emojiButton;
    private EmojiEditText commentBody;
    private ViewGroup rootView;
    private LinearLayout mEmojiBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        recyclerView = findViewById(R.id.comments_list);
        loadingIndicatorView = findViewById(R.id.loading);
        linearLayout = findViewById(R.id.error);
        emojiButton = findViewById(R.id.main_activity_emoji);
        commentBody = findViewById(R.id.add_comment_body);
        rootView = (ViewGroup) findViewById(R.id.main_activity_root_view);
        mEmojiBar = (LinearLayout) findViewById(R.id.main_activity_emoji_bar);

        comments = new ArrayList<>();
        String groupId = getIntent().getStringExtra("group_id");
        String postId = getIntent().getStringExtra("post_id");
        User myUser = User.restore(App.sPrefs);
        try {
            handleComments(myUser.getName(), myUser.getPpURL(), groupId, postId);
        } catch (Exception e) {
            loadingIndicatorView.hide();
            linearLayout.setVisibility(View.VISIBLE);
        }

        setUpEmojiPopup();
        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                emojiPopup.toggle();
            }
        });
    }


    private void loadComments(String groupId, String postId) {
        comments.clear();
        FirebaseFirestore.getInstance().collection("/groups/" + groupId + "/subdata/" + postId + "/comments/")
                .orderBy("timestamp", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Comment currentComment = new Comment();
                    currentComment.setBody((String) documentSnapshot.get("body"));
                    currentComment.setOwner((String) documentSnapshot.get("owner"));
                    currentComment.setPostId((String) documentSnapshot.get("postId"));
                    currentComment.setPpUrl((String) documentSnapshot.get("ppUrl"));
                    currentComment.setTimestamp((long) documentSnapshot.get("timestamp"));
                    comments.add(currentComment);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
                recyclerView.removeAllViews();
                recyclerView.setLayoutManager(layoutManager);
                CommentListAdapter commentListAdapter = new CommentListAdapter(comments);
                recyclerView.setAdapter(commentListAdapter);
                linearLayout.setVisibility(View.GONE);
                loadingIndicatorView.hide();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                linearLayout.setVisibility(View.VISIBLE);
                loadingIndicatorView.hide();
            }
        });

    }

    private void handleComments(final String owner, final String ppUrl, final String groupId, final String postId) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        loadComments(groupId, postId);
        findViewById(R.id.add_comment_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(commentBody.getText()))
                    return;
                else if (TextUtils.isEmpty(commentBody.getText().toString().trim()))
                    return;

                Comment newComment = new Comment();
                newComment.setBody(commentBody.getText().toString());
                newComment.setOwner(owner);
                newComment.setPpUrl(ppUrl);
                newComment.setTimestamp(System.currentTimeMillis());
                newComment.setPostId(postId);

                db.collection("/groups/" + groupId + "/subdata/" + postId + "/comments/")
                        .add(newComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        commentBody.setText("");
                        loadComments(groupId, postId);
                    }
                });
            }
        });

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

}
