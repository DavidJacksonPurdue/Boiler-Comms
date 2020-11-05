package com.example.boiler_commslogin;

public abstract class Constants {
    public static final String ROOT_URL = "http://10.0.2.2:8000/";
    public static final String CREATE_POST = ROOT_URL + "createPost.php?q=";
    public static final String DELETEUSER = ROOT_URL + "deleteUser.php?q=";
    public static final String EDITUSER = ROOT_URL + "editUser.php?q=";
    public static final String GETPOST = ROOT_URL + "getPost.php?q=";
    public static final String GETUSERCREDENTIALS = ROOT_URL + "getUserCredentials.php?q=";
    public static final String INSERTOPIC = ROOT_URL + "insertTopic.php?q=";
    public static final String INSERTUSER = ROOT_URL + "insertUser.php?q=1_";
    public static final String LOGIN = ROOT_URL + "Login.php?q=";
    public static final String POSTUSERCREDENTIALS = ROOT_URL + "postUserCredentials.php";
    public static final String UPLOADFILE2 = ROOT_URL + "uploadFile2.php?q=";
    public static final String VERIFYUSER = ROOT_URL + "verifyUser.php?q=";
    public static final String GETCOMMENTS = ROOT_URL + "getComments.php?q=";
    public static final String SENDCOMMENT = ROOT_URL + "insertComment.php?q=";
    public static final String GETMYCOMMENTS = ROOT_URL + "getUserCommentsByDate.php?q=";
    public static final String GETUSERID = ROOT_URL + "getIDFromUsername.php?q=";
    public static final String GETPOSTTOPIC = ROOT_URL + "getPostByTopic.php?q=";
    public static final String UPVOTE = ROOT_URL + "upvotePost.php?q=";
    public static final String DOWNVOTE = ROOT_URL + "downvotePost.php?q=";
    public static final String GET_TOPICS = ROOT_URL + "getTopics.php";
    public static final String GET_UPVOTED = ROOT_URL + "getUpvoteList.php?q=";
    public static final String GET_DOWNVOTED = ROOT_URL + "getDownvoteList.php?q=";
    public static final String GETSAVED = ROOT_URL + "getSavedPost.php?q=";
    public static final String GETLIKED = ROOT_URL + "getUpvoteUser.php?q=";
    public static final String GETSTARTTIMELINE = ROOT_URL + "getStartTimeline.php?q=";
    public static final String GET_POST_BY_POSTID = ROOT_URL + "getPostByPostID.php?q=";
    public static final String SAVE_POST = ROOT_URL + "insertSavedPost.php?q=";
    public static final String CHECK_FOLLOW = ROOT_URL + "checkvalidFollow.php?q=";
    public static final String FOLLOW_USER = ROOT_URL + "followUser.php?q=";
    public static final String GETFOLLOWTOPIC = ROOT_URL + "followTopic.php?q=";

}
