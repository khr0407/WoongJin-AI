package edu.skku.woongjin_ai;

import android.graphics.drawable.Drawable;

public class MyFriendQuizListItem {
    private String quiz;
    private String uid;
    private Drawable iconLike;
    private String like;

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Drawable getIconLikr() {
        return iconLike;
    }

    public void setIconLikr(Drawable iconLike) {
        this.iconLike = iconLike;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
