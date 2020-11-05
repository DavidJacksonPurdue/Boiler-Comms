package com.example.boiler_commslogin;

import android.util.Log;

import com.example.boiler_commslogin.comment.RecyclerViewItem;

public class Item extends RecyclerViewItem {

    String title="";
    String body = "";
    String userName = "";
    int postID = 0;
    int userID = 0;
    int commentID = 0;
    int parentID = -1;
    String date="";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getCommentID() {
        Log.d("commentID: ", Integer.toString(commentID));
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    public int getParentID() {
        Log.d("ParentID: ", Integer.toString(parentID));
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    protected Item(int level) {
        super(level);
    }
}