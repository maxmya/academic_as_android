package com.graduation.academic.as.models;

import java.util.Map;

public class Post {

    private String postId;
    private String owner;
    private String body;
    private String ppURL;
    private String likes;
    private String groupId;

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