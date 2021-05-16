package com.example.myapplication;

public class ExampleItem {
    private int imageResource;
    private int id;
    private String header;
    private String secondary;
    private String tags;

    public ExampleItem(int imageResource, String header, String secondary) {
        this.imageResource = imageResource;
        this.header = header;
        this.secondary = secondary;
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
}