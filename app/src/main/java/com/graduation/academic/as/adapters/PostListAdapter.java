package com.graduation.academic.as.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.Map;

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
        postsListViewHolder.body.append(posts.get(i).getBody());
        postsListViewHolder.userName.setText(posts.get(i).getOwner());
        postsListViewHolder.likes.setText(posts.get(i).getLikes());
        final String groupId = posts.get(i).getGroupId();
        final String postId = posts.get(i).getPostId();
        postsListViewHolder.likeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    int likes = Integer.parseInt(posts.get(i).getLikes());
//                    posts.get(i).setLikes((likes + 1) + "");
//                    postsListViewHolder.likes.setText(posts.get(i).getLikes());
//                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    final DocumentReference ref = db.collection("/groups/" + groupId + "/posts/").document(postId);
//                    ref.set(posts.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            Log.i("Adapter ", task.isSuccessful() + " at " + ref.getPath());
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.i("Adapter ", e.getMessage() + "");
//
//                            e.printStackTrace();
//                        }
//                    });

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("/groups/" + groupId + "/posts/").document(postId)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Map<String, String> likers = (Map<String, String>) documentSnapshot.get("likers");
                            String likes = (String) documentSnapshot.get("likes");
                            // todo get uid and check if he's a liker !
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Picasso.get().load(posts.get(i).getPpURL()).into(postsListViewHolder.profilePicture);
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
