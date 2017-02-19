package com.iglin.lab3_database.model;

import android.graphics.Bitmap;

/**
 * Created by user on 19.02.2017.
 */

public class RecordPicture {
    private int id;
    private Bitmap picture;

    public RecordPicture() {
    }

    public RecordPicture(Bitmap picture) {
        this.picture = picture;
    }

    public RecordPicture(int id, Bitmap picture) {
        this.id = id;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
