package com.example.gallery;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.demo_full.R;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.IVideoUpdate;
import com.example.gallery.util.NotifyFragmentPager;
import com.example.gallery.util.VideoPlayer;

import com.github.chrisbanes.photoview.PhotoView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import java.util.List;

/* loaded from: classes2.dex */
public class FragmentMediaPager extends Fragment {
    Fragment fragment;
    public GalleryModel galleryModel;
    int currentPosition=0;
    IVideoUpdate iVideoUpdate;
    boolean isComplete;
    boolean isFragmentLoaded;
    int position;
    VideoPlayer videoPlayer;

    public static FragmentMediaPager newInstance(List<GalleryModel> galleryModels, int pos, Fragment fragment, IVideoUpdate iVideoUpdate) {
        FragmentMediaPager fragmentMediaPager = new FragmentMediaPager();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        fragmentMediaPager.galleryModel = galleryModels.get(pos);
        fragmentMediaPager.fragment = fragment;
        fragmentMediaPager.iVideoUpdate = iVideoUpdate;
        fragmentMediaPager.setArguments(bundle);
        return fragmentMediaPager;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.item, container, false);
        Log.e("CREDRT", "onCreateView: >>>>>>>      "+galleryModel.getPath() );
        this.videoPlayer = (VideoPlayer) inflate.findViewById(R.id.video2);
        final PhotoView photoView = (PhotoView) inflate.findViewById(R.id.imageViewMain);
        if (this.galleryModel == null) {
            return inflate;
        }
        if (getArguments() != null) {
            this.position = getArguments().getInt("pos");
        }
        if (this.galleryModel.getPath() != null && this.galleryModel.getPath().contains(".mp4")) {
            photoView.setVisibility(View.INVISIBLE);
            this.videoPlayer.setVisibility(View.VISIBLE);
            if (this.galleryModel.getUri() != null) {
                this.videoPlayer.setVideoURI(Uri.parse(this.galleryModel.getUri()));
            } else {
                this.videoPlayer.setVideoURI(Uri.parse(this.galleryModel.getPath()));
            }
            this.videoPlayer.seekTo(0);
            this.videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager$$ExternalSyntheticLambda1
                @Override // android.media.MediaPlayer.OnPreparedListener
                public final void onPrepared(MediaPlayer mediaPlayer) {
                    FragmentMediaPager.this.m248x78d13929(mediaPlayer);
                }
            });
            this.videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager$$ExternalSyntheticLambda0
                @Override // android.media.MediaPlayer.OnCompletionListener
                public final void onCompletion(MediaPlayer mediaPlayer) {
                    FragmentMediaPager.this.m249x6a7adf48(mediaPlayer);
                }
            });
            this.videoPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager.1
                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    return true;
                }
            });
            this.videoPlayer.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    FragmentMediaPager.this.iVideoUpdate.updateClickItem();
                }
            });
        } else {
            photoView.setVisibility(View.VISIBLE);
            photoView.setTransitionName("anyString" + this.position);
            if (getContext() != null) {
                if (this.galleryModel.getPath() != null && this.galleryModel.getUri() != null) {
                    Glide.with(getContext()).load(Uri.parse(this.galleryModel.getUri())).listener(new RequestListener<Drawable>() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager.3
                        @Override // com.bumptech.glide.request.RequestListener
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }

                        @Override // com.bumptech.glide.request.RequestListener
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager.3.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (FragmentMediaPager.this.getContext() != null) {
                                        Glide.with(FragmentMediaPager.this.getContext()).load(FragmentMediaPager.this.galleryModel.getPath()).into(photoView);
                                    }
                                }
                            });
                            return false;
                        }
                    }).into(photoView);
                } else {
                    Glide.with(this.fragment.getContext()).load(this.galleryModel.getPath()).into(photoView);
                }
            }
            this.videoPlayer.setVisibility(View.GONE);
            photoView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.ui.gallery.FragmentMediaPager.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    FragmentMediaPager.this.iVideoUpdate.updateClickItem();
                }
            });
        }
        return inflate;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreateView$0$com-example-iphoto-ui-gallery-FragmentMediaPager, reason: not valid java name */
    public /* synthetic */ void m248x78d13929(MediaPlayer mediaPlayer) {
        if (this.position ==currentPosition) {
            this.videoPlayer.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onCreateView$1$com-example-iphoto-ui-gallery-FragmentMediaPager, reason: not valid java name */
    public /* synthetic */ void m249x6a7adf48(MediaPlayer mediaPlayer) {
        this.videoPlayer.resume();
        this.isComplete = true;
        this.videoPlayer.stopPlayback();
        this.iVideoUpdate.resumePlay();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyFragmentPager fragmentPager) {
        if (fragmentPager != null ) {
            if (this.isComplete) {
                this.videoPlayer.resume();
                this.isComplete = false;
            }
            this.videoPlayer.start();
            return;
        }
        if (fragmentPager != null && fragmentPager.getMesage().equals("start")) {
            if (fragmentPager.getMpos() == this.position) {
                this.videoPlayer.seekTo(0);
                this.videoPlayer.setVideoURI(Uri.parse(this.galleryModel.getUri()));
                this.videoPlayer.start();
            } else {
                this.videoPlayer.stopPlayback();
            }
            if (fragmentPager.isIsmute()) {
                this.videoPlayer.streamMute();
                return;
            } else {
                this.videoPlayer.streamMute1();
                return;
            }
        }
        if (fragmentPager != null && fragmentPager.getMesage().equals("pause")) {
            this.videoPlayer.pause();
            return;
        }
        if (fragmentPager != null && fragmentPager.getMesage().equals("mute")) {
            this.videoPlayer.streamMute();
            return;
        }
        if (fragmentPager != null && fragmentPager.getMesage().equals("nomute")) {
            this.videoPlayer.streamMute1();
        } else {
            if (fragmentPager == null || !fragmentPager.getMesage().equals("exit")) {
                return;
            }
            this.videoPlayer.stopPlayback();
            this.videoPlayer.streamMute();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        VideoPlayer videoPlayer = this.videoPlayer;
        if (videoPlayer != null) {
            videoPlayer.stopPlayback();
            this.videoPlayer = null;
        }
        super.onDestroyView();
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
