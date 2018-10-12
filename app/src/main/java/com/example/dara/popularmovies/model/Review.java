package com.example.dara.popularmovies.model;

public class Review {

    private final String mContent;

    //Constructor
    public Review(String content) {
        //Declare fields
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }
}
