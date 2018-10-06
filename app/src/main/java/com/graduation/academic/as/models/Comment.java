package com.graduation.academic.as.models;

/**
 * Created by Omar Wael on 10/4/2018.
 */

public class Comment {

    private String postId;
    private String owner ;
    private String ppUrl ;
    private String body ;
    private long timestamp;

    public String getPostId() {
        return postId;
    }

    public String getOwner() {
        return owner;
    }

    public String getPpUrl() {
        return ppUrl;
    }

    public String getBody() {
        return body;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
