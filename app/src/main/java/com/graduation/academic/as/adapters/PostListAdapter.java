package com.graduation.academic.as.adapters;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.App;
import com.graduation.academic.as.R;
import com.graduation.academic.as.activities.CommentsActivity;
import com.graduation.academic.as.helpers.BasicImageDownloader;
import com.graduation.academic.as.models.Comment;
import com.graduation.academic.as.models.Post;
import com.graduation.academic.as.models.User;
import com.graduation.academic.as.viewholders.PostsListViewHolder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PostListAdapter extends RecyclerView.Adapter<PostsListViewHolder> {


    ArrayList<Post> posts;
    Context mContext;

    public PostListAdapter(ArrayList<Post> posts, Context mContext) {
        this.posts = posts;
        this.mContext = mContext;
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
        String body = posts.get(i).getBody();
        postsListViewHolder.body.setText(body);
        if (body.length() > 100)
            postsListViewHolder.seeMore.setVisibility(View.VISIBLE);
        else
            postsListViewHolder.seeMore.setVisibility(View.GONE);
        postsListViewHolder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsListViewHolder.seeMore.setText(postsListViewHolder.body.isExpanded() ? R.string.read_more : R.string.read_less);
                postsListViewHolder.body.toggle();
            }
        });
        postsListViewHolder.userName.setText(posts.get(i).getOwner());
        postsListViewHolder.likes.setText(posts.get(i).getLikes());
        postsListViewHolder.time.setText(TimeAgo.using(posts.get(i).getTimestamp()));
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
        if (posts.get(i).getPostImage() != null) {
            postsListViewHolder.imageView.setVisibility(View.VISIBLE);
            Target imageTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    postsListViewHolder.imageLoader.setVisibility(View.VISIBLE);
                    postsListViewHolder.postImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    postsListViewHolder.imageLoader.setVisibility(View.GONE);
                    postsListViewHolder.postImage.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    postsListViewHolder.imageLoader.setVisibility(View.VISIBLE);
                }
            };
            Picasso.get().load(posts.get(i).getPostImage()).into(imageTarget);
            final ImagePopup imagePopup = new ImagePopup(postsListViewHolder.postImage.getContext());
            imagePopup.setBackgroundColor(Color.BLACK);
            imagePopup.setFullScreen(true);
            imagePopup.initiatePopupWithGlide(posts.get(i).getPostImage());
            postsListViewHolder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagePopup.viewPopup();
                }
            });
            postsListViewHolder.postImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setTitle("Save");
                    builder.setMessage("Do you want to save image?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            App.askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, App.WRITE_EXST, postsListViewHolder.postImage.getContext());
                            final Bitmap.CompressFormat mFormat = Bitmap.CompressFormat.JPEG;
                            String name = UUID.randomUUID().toString();
                            final File myImageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                                    File.separator + "image_test" + File.separator + name + "." + mFormat.name().toLowerCase());

                            BasicImageDownloader downloader = new BasicImageDownloader(new BasicImageDownloader.OnImageLoaderListener() {
                                @Override
                                public void onError(BasicImageDownloader.ImageError error) {
                                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onProgressChange(int percent) {
                                    Toast.makeText(mContext, percent + "%", Toast.LENGTH_SHORT).show();

                                }


                                @Override
                                public void onComplete(Bitmap result) {
                                    BasicImageDownloader.writeToDisk(myImageFile, result, new BasicImageDownloader.OnBitmapSaveListener() {
                                        @Override
                                        public void onBitmapSaved() {
                                            Toast.makeText(mContext, "Saved !", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onBitmapSaveError(BasicImageDownloader.ImageError error) {

                                        }
                                    }, Bitmap.CompressFormat.JPEG, false);

                                }
                            });
                            downloader.download(posts.get(i).getPostImage(), true);

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
                }
            });
        } else {
            postsListViewHolder.imageView.setVisibility(View.GONE);
        }
        Picasso.get().load(posts.get(i).getPpURL()).into(postsListViewHolder.profilePicture);
        postsListViewHolder.comments.setText(posts.get(i).getCommentCount());

        postsListViewHolder.commentUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsListViewHolder.commentUp.playAnimation();
                Intent commentActivity = new Intent(mContext, CommentsActivity.class);
                commentActivity.putExtra("post_id", postId);
                commentActivity.putExtra("group_id", groupId);
                mContext.startActivity(commentActivity);
            }

        });
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
