package com.sparkwaker.beam.models;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class MediaStoreAudio {

    private long mId;
    private Uri mContentUri;
    private String mTitle;
    private String mSize;
    private String mFormat;
    private String mDateModified;


    public MediaStoreAudio(long id, Uri contentUri, String title, String size, String format, String dateModified) {
        this.mId = id;
        this.mContentUri = contentUri;
        this.mTitle = title;
        this.mSize = size;
        this.mFormat = format;
        this.mDateModified = dateModified;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public Uri getContentUri() {
        return mContentUri;
    }

    public void setContentUri(Uri mContentUri) {
        this.mContentUri = mContentUri;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getSize() {
        return mSize;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String mFormat) {
        this.mFormat = mFormat;
    }

    public String getDateModified() {
        return mDateModified;
    }

    public void setDateModified(String DateModified) {
        this.mDateModified = mDateModified;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
