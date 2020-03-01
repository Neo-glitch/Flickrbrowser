package com.neo.flickrbrowser;


import android.icu.text.CaseMap;

import java.io.Serializable;

// class holds all data relating to a single photo # hold fields for photos
class Photo implements Serializable {
//def the version UID inorder to prevent java from changing the version mayb when java is updated
    private static final long serialVersionUID = 1L;

    private String mTitle;
    private String mAuthor;
    private String getmAuthorId;
    private String mLink;
    private String mTags;
    private String mImage;

    public Photo(String mTitle, String mAuthor, String getmAuthorId, String mLink, String mTags, String mImage) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.getmAuthorId = getmAuthorId;
        this.mLink = mLink;
        this.mTags = mTags;
        this.mImage = mImage;
    }

    String getTitle() {
        return mTitle;
    }

    String getAuthor() {
        return mAuthor;
    }

    String getGetmAuthorId() {
        return getmAuthorId;
    }

    String getLink() {
        return mLink;
    }

    String getTags() {
        return mTags;
    }

    String getImage() {
        return mImage;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", getmAuthorId='" + getmAuthorId + '\'' +
                ", mLink='" + mLink + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mImage='" + mImage + '\'' +
                '}';
    }
}
