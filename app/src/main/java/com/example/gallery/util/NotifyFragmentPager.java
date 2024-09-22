package com.example.gallery.util;

/* loaded from: classes.dex */
public class NotifyFragmentPager {
    boolean ismute;
    String mesage;
    int mpos;

    public NotifyFragmentPager(int pos, String mesage, boolean ismute) {
        this.mesage = mesage;
        this.mpos = pos;
        this.ismute = ismute;
    }

    public boolean isIsmute() {
        return this.ismute;
    }

    public void setIsmute(boolean ismute) {
        this.ismute = ismute;
    }

    public NotifyFragmentPager(int pos, String mesage) {
        this.mesage = mesage;
        this.mpos = pos;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getMesage() {
        return this.mesage;
    }

    public int getMpos() {
        return this.mpos;
    }

    public void setMpos(int mpos) {
        this.mpos = mpos;
    }
}
