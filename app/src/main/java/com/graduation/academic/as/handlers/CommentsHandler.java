package com.graduation.academic.as.handlers;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.models.Comment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CommentsHandler {

    private List<Comment> comments;
    private CollectionReference commentsNode;
    private ListenerRegistration listenerRegistration;


    public CommentsHandler(String groupId, String postId) {
        String collectionPath = "/groups/" + groupId + "/subdata/" + postId + "/comments";
        commentsNode = FirebaseFirestore.getInstance().collection(collectionPath);
        comments = new ArrayList<>();
    }

    public void connect() {
        if (listenerRegistration == null) {
            listenerRegistration = commentsNode.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

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

                            }
                        }

                    }

                }
            });
        }

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

    public List<Comment> getComments() {
        return comments;
    }
}
