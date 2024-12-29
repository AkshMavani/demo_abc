package com.example.gallery.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NavigationView extends View {
    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public NavigationView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        updateLayout();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(NotifyUpdateNavigationView notifyUpdateNavigationView) {
        updateLayout();
    }

    private void updateLayout() {
        if (getTag() != null || BaseSettings.getInstance().insetBottom() <= 0) {
            return;
        }
        post(new Runnable() { // from class: com.example.iphoto.view.NavigationView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NavigationView.this.m265lambda$updateLayout$0$comexampleiphotoviewNavigationView();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$updateLayout$0$com-example-iphoto-view-NavigationView, reason: not valid java name */
    public /* synthetic */ void m265lambda$updateLayout$0$comexampleiphotoviewNavigationView() {
        if (getLayoutParams() != null) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = BaseSettings.getInstance().insetBottom();
            setLayoutParams(layoutParams);
            setVisibility(View.VISIBLE);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (getTag() != null && getTag().equals("tag_home")) {
            BaseSettings.getInstance().insetBottom(insets.getSystemWindowInsetBottom());
        //    ((HomeActivity) getContext()).updateInset(insets.getSystemWindowInsetBottom());
            EventBus.getDefault().post(new NotifyUpdateNavigationView());
        }
        return insets;
    }
}
