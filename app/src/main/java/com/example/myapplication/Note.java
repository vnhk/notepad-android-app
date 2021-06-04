package com.example.myapplication;

import java.io.Serializable;
import java.sql.Timestamp;

public class Note implements Serializable {
    public static final String CALENDAR_NOTE = "CALENDAR_NOTE";
    public static final String QUICK_NOTE = "QUICK_NOTE";
    public static final String DOCUMENT = "DOCUMENT";
    private String content;
    private String plainContent;
    private int imageResource;
    private int id;
    private String header;
    private String secondary;
    private String tags;
    private String persistInfoDisplay;
    private Timestamp modifyDate;
    private String type;

    public Note(int imageResource, String header, String secondary) {
        this.imageResource = imageResource;
        this.header = header;
        this.secondary = secondary;
    }

    public Note() {

    }

    public String getPlainContent() {
        return plainContent;
    }

    public void setPlainContent(String plainContent) {
        this.plainContent = plainContent;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPersistInfoDisplay() {
        return persistInfoDisplay;
    }

    public void setPersistInfoDisplay(String persistInfoDisplay) {
        this.persistInfoDisplay = persistInfoDisplay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }
}