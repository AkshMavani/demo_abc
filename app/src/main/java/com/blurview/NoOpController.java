package com.blurview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/* loaded from: classes.dex */
class NoOpController implements BlurController {
    @Override // com.blurview.BlurController
    public void destroy() {
    }

    @Override // com.blurview.BlurController
    public boolean draw(Canvas canvas) {
        return true;
    }

    @Override // com.blurview.BlurViewFacade
    public BlurViewFacade setBlurAutoUpdate(boolean enabled) {
        return this;
    }

    @Override // com.blurview.BlurViewFacade
    public BlurViewFacade setBlurEnabled(boolean enabled) {
        return this;
    }

    @Override // com.blurview.BlurViewFacade
    public BlurViewFacade setBlurRadius(float radius) {
        return this;
    }

    @Override // com.blurview.BlurViewFacade
    public BlurViewFacade setFrameClearDrawable(Drawable windowBackground) {
        return this;
    }

    @Override // com.blurview.BlurViewFacade
    public BlurViewFacade setOverlayColor(int overlayColor) {
        return this;
    }

    @Override // com.blurview.BlurController
    public void updateBlurViewSize() {
    }
}
