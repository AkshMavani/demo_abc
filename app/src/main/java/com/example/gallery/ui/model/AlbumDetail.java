package com.example.gallery.ui.model;

/* loaded from: classes.dex */
public class AlbumDetail {
    long bugetId;
    String buget_name;
    int count;
    private int id;
    boolean isFolder;
    String path;

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isFolder() {
        return this.isFolder;
    }

    public void setFolder(boolean folder) {
        this.isFolder = folder;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return this.id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBuget_name() {
        return this.buget_name;
    }

    public void setBuget_name(String buget_name) {
        this.buget_name = buget_name;
    }

    public long getBugetId() {
        return this.bugetId;
    }

    public void setBugetId(long bugetId) {
        this.bugetId = bugetId;
    }
}
