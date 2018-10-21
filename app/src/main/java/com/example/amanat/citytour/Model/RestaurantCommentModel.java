package com.example.amanat.citytour.Model;

public class RestaurantCommentModel {
    private String userName, userImage, userComment;

    public RestaurantCommentModel(String userName, String userImage, String userComment) {
        this.userName = userName;
        this.userImage = userImage;
        this.userComment = userComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }
}
