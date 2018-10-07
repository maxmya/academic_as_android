package com.graduation.academic.as.models;

import com.google.firebase.Timestamp;

import java.util.Map;

public class Post {

    private String postId;
    private String owner;
    private String body;
    private String ppURL;
    private String likes;
    private String groupId;
    private String postImage;
    private String commentCount;
    private Map<String, String> likers;
    private long timestamp;

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLikers(Map<String, String> likers) {
        this.likers = likers;
    }

    public Map<String, String> getLikers() {
        return likers;
    }

    public String getLikes() {
        return likes;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getOwner() {
        return owner;
    }

    public String getPpURL() {
        return ppURL;
    }

    public void setPpURL(String ppURL) {
        this.ppURL = ppURL;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
