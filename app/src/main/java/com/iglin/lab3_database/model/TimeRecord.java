package com.iglin.lab3_database.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by user on 19.02.2017.
 */

public class TimeRecord {
    private int id;
    private String description;
    private long startTime;
    private long endTime;
    private int duration;
    private TimeCategory timeCategory;
    private List<RecordPicture> pics;

    public TimeRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NonNull
    public TimeCategory getTimeCategory() {
        return timeCategory;
    }

    public void setTimeCategory(TimeCategory timeCategory) {
        this.timeCategory = timeCategory;
    }

    @Nullable
    public List<RecordPicture> getPics() {
        return pics;
    }

    public void setPics(List<RecordPicture> pics) {
        this.pics = pics;
    }
}
