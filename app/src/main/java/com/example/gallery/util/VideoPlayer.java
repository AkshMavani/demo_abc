package com.example.gallery.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

/* loaded from: classes2.dex */
public class VideoPlayer extends VideoView implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {
    public static AudioManager f5492b;
    public boolean f5493a;

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) {
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    public VideoPlayer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5493a = true;
        setOnPreparedListener(this);
        setOnCompletionListener(this);
        setOnErrorListener(this);
    }

    public void streamMute() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService("audio");
        f5492b = audioManager;
        audioManager.setStreamMute(3, true);
    }

    public void streamMute1() {
        AudioManager audioManager = (AudioManager) getContext().getSystemService("audio");
        f5492b = audioManager;
        audioManager.setStreamMute(3, false);
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (this.f5493a) {
            streamMute();
        } else {
            streamMute1();
        }
    }

    public void setIsMuteWhenStart(boolean z) {
        this.f5493a = z;
    }
}
