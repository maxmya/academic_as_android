package com.graduation.academic.as.adapters;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.R;
import com.graduation.academic.as.models.Post;
import com.graduation.academic.as.viewholders.GroupListViewHolder;
import com.graduation.academic.as.viewholders.PostsListViewHolder;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PostListAdapter extends RecyclerView.Adapter<PostsListViewHolder> {


    ArrayList<Post> posts;

    public PostListAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        PostsListViewHolder viewHolder = new PostsListViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PostsListViewHolder postsListViewHolder, final int i) {
        postsListViewHolder.body.setText(posts.get(i).getBody());
        postsListViewHolder.userName.setText(posts.get(i).getOwner());
        // Todo :load name and image url from users node
        postsListViewHolder.likes.setText(posts.get(i).getLikes());
        postsListViewHolder.time.setText(DateFormat.getInstance().format(posts.get(i).getTimestamp()));
        final String groupId = posts.get(i).getGroupId();
        final String postId = posts.get(i).getPostId();
        postsListViewHolder.likeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final MediaPlayer mp = MediaPlayer.create(postsListViewHolder.likeUp.getContext(), R.raw.pop);
                    mp.start();
                    postsListViewHolder.likeUp.playAnimation();
                    handleLikes(postsListViewHolder, groupId, postId, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        postsListViewHolder.commentUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   handleComments(postsListViewHolder , groupId ,postId , i);
            }

        });

        Picasso.get().load(posts.get(i).getPpURL()).into(postsListViewHolder.profilePicture);
    }

    private void handleComments(final PostsListViewHolder postsListViewHolder, final String groupId, final String postId, final int i) {
        // connect to firebase
        //if there is a comments for this posts
        // load them in a recycle view wiht a dialog

    }


    private void handleLikes(final PostsListViewHolder postsListViewHolder, final String groupId, final String postId, final int i) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("/groups/" + groupId + "/posts/").document(postId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (documentSnapshot.contains("likers")) {
                    Map<String, String> likers = (Map<String, String>) documentSnapshot.get("likers");
                    String likes = (String) documentSnapshot.get("likes");
                    if (likers.containsKey(uid)) {
                        // delete like
                        likers.remove(uid);
                        likes = (Integer.parseInt(likes) - 1) + "";
                        posts.get(i).setLikes(likes);
                        posts.get(i).setLikers(likers);
                        postsListViewHolder.likes.setText(likes);
                        db.collection("/groups/" + groupId + "/posts/").document(postId).set(posts.get(i));
                    } else {
                        // do like
                        likers.put(uid, "1");
                        likes = (Integer.parseInt(likes) + 1) + "";
                        posts.get(i).setLikes(likes);
                        posts.get(i).setLikers(likers);
                        postsListViewHolder.likes.setText(likes);
                        db.collection("/groups/" + groupId + "/posts/").document(postId).set(posts.get(i));
                    }
                } else {
                    Map<String, String> likers = new HashMap<>();
                    likers.put(uid, "1");
                    String likes = "1";
                    posts.get(i).setLikes(likes);
                    posts.get(i).setLikers(likers);
                    postsListViewHolder.likes.setText(likes);
                    db.collection("/groups/" + groupId + "/posts/").document(postId).set(posts.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
