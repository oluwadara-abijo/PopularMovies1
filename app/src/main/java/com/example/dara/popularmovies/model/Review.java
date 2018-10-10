package com.example.dara.popularmovies.model;

public class Review {

    private final String mContent;

    //Constructor
    public Review(String author, String content) {
        //Declare fields
        String mAuthor = author;
        this.mContent = content;
    }

    public String getContent() {
        return mContent;
    }
}
