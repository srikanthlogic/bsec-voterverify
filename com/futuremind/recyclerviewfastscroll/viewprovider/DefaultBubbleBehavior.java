package com.futuremind.recyclerviewfastscroll.viewprovider;
/* loaded from: classes.dex */
public class DefaultBubbleBehavior implements ViewBehavior {
    private final VisibilityAnimationManager animationManager;

    public DefaultBubbleBehavior(VisibilityAnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ViewBehavior
    public void onHandleGrabbed() {
        this.animationManager.show();
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ViewBehavior
    public void onHandleReleased() {
        this.animationManager.hide();
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ViewBehavior
    public void onScrollStarted() {
    }

    @Override // com.futuremind.recyclerviewfastscroll.viewprovider.ViewBehavior
    public void onScrollFinished() {
    }
}
