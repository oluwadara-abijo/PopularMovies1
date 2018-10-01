package com.example.dara.popularmovies.model;

public class Review {

    //Declare fields
    private String mAuthor;
    private String mContent;

    //Constructor
    public Review(String author, String content) {
        this.mAuthor = author;
        this.mContent = content;
    }

    //Getter methods
    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
