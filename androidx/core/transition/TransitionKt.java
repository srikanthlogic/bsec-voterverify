package androidx.core.transition;

import android.transition.Transition;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Transition.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\u001aÆ\u0001\u0010\u0000\u001a\u00020\u0001*\u00020\u00022#\b\u0006\u0010\u0003\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\t\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\n\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\u000b\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u00042#\b\u0006\u0010\f\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b\u001a2\u0010\r\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b\u001a2\u0010\u000f\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b\u001a2\u0010\u0010\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b\u001a2\u0010\u0011\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b\u001a2\u0010\u0012\u001a\u00020\u0001*\u00020\u00022#\b\u0004\u0010\u000e\u001a\u001d\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0005\u0012\b\b\u0006\u0012\u0004\b\b(\u0007\u0012\u0004\u0012\u00020\b0\u0004H\u0087\b¨\u0006\u0013"}, d2 = {"addListener", "Landroid/transition/Transition$TransitionListener;", "Landroid/transition/Transition;", "onEnd", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "transition", "", "onStart", "onCancel", "onResume", "onPause", "doOnCancel", "action", "doOnEnd", "doOnPause", "doOnResume", "doOnStart", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class TransitionKt {
    public static final Transition.TransitionListener doOnEnd(Transition $this$doOnEnd, Function1<? super Transition, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doOnEnd, "$this$doOnEnd");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TransitionKt$doOnEnd$$inlined$addListener$1 listener$iv = new Transition.TransitionListener() { // from class: androidx.core.transition.TransitionKt$doOnEnd$$inlined$addListener$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
                Function1.this.invoke(transition);
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }
        };
        $this$doOnEnd.addListener(listener$iv);
        return listener$iv;
    }

    public static final Transition.TransitionListener doOnStart(Transition $this$doOnStart, Function1<? super Transition, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doOnStart, "$this$doOnStart");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TransitionKt$doOnStart$$inlined$addListener$1 listener$iv = new Transition.TransitionListener() { // from class: androidx.core.transition.TransitionKt$doOnStart$$inlined$addListener$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
                Function1.this.invoke(transition);
            }
        };
        $this$doOnStart.addListener(listener$iv);
        return listener$iv;
    }

    public static final Transition.TransitionListener doOnCancel(Transition $this$doOnCancel, Function1<? super Transition, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doOnCancel, "$this$doOnCancel");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TransitionKt$doOnCancel$$inlined$addListener$1 listener$iv = new Transition.TransitionListener() { // from class: androidx.core.transition.TransitionKt$doOnCancel$$inlined$addListener$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
                Function1.this.invoke(transition);
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }
        };
        $this$doOnCancel.addListener(listener$iv);
        return listener$iv;
    }

    public static final Transition.TransitionListener doOnResume(Transition $this$doOnResume, Function1<? super Transition, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doOnResume, "$this$doOnResume");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TransitionKt$doOnResume$$inlined$addListener$1 listener$iv = new Transition.TransitionListener() { // from class: androidx.core.transition.TransitionKt$doOnResume$$inlined$addListener$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
                Function1.this.invoke(transition);
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }
        };
        $this$doOnResume.addListener(listener$iv);
        return listener$iv;
    }

    public static final Transition.TransitionListener doOnPause(Transition $this$doOnPause, Function1<? super Transition, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doOnPause, "$this$doOnPause");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TransitionKt$doOnPause$$inlined$addListener$1 listener$iv = new Transition.TransitionListener() { // from class: androidx.core.transition.TransitionKt$doOnPause$$inlined$addListener$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
                Function1.this.invoke(transition);
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Intrinsics.checkParameterIsNotNull(transition, "transition");
            }
        };
        $this$doOnPause.addListener(listener$iv);
        return listener$iv;
    }

    public static /* synthetic */ Transition.TransitionListener addListener$default(Transition $this$addListener, Function1 onEnd, Function1 onStart, Function1 onCancel, Function1 onResume, Function1 onPause, int i, Object obj) {
        Function1 onStart2;
        Function1 onCancel2;
        Function1 onResume2;
        Function1 onPause2;
        if ((i & 1) != 0) {
            onEnd = TransitionKt$addListener$1.INSTANCE;
        }
        if ((i & 2) != 0) {
            onStart2 = TransitionKt$addListener$2.INSTANCE;
        } else {
            onStart2 = onStart;
        }
        if ((i & 4) != 0) {
            onCancel2 = TransitionKt$addListener$3.INSTANCE;
        } else {
            onCancel2 = onCancel;
        }
        if ((i & 8) != 0) {
            onResume2 = TransitionKt$addListener$4.INSTANCE;
        } else {
            onResume2 = onResume;
        }
        if ((i & 16) != 0) {
            onPause2 = TransitionKt$addListener$5.INSTANCE;
        } else {
            onPause2 = onPause;
        }
        Intrinsics.checkParameterIsNotNull($this$addListener, "$this$addListener");
        Intrinsics.checkParameterIsNotNull(onEnd, "onEnd");
        Intrinsics.checkParameterIsNotNull(onStart2, "onStart");
        Intrinsics.checkParameterIsNotNull(onCancel2, "onCancel");
        Intrinsics.checkParameterIsNotNull(onResume2, "onResume");
        Intrinsics.checkParameterIsNotNull(onPause2, "onPause");
        TransitionKt$addListener$listener$1 listener = new TransitionKt$addListener$listener$1(onEnd, onResume2, onPause2, onCancel2, onStart2);
        $this$addListener.addListener(listener);
        return listener;
    }

    public static final Transition.TransitionListener addListener(Transition $this$addListener, Function1<? super Transition, Unit> function1, Function1<? super Transition, Unit> function12, Function1<? super Transition, Unit> function13, Function1<? super Transition, Unit> function14, Function1<? super Transition, Unit> function15) {
        Intrinsics.checkParameterIsNotNull($this$addListener, "$this$addListener");
        Intrinsics.checkParameterIsNotNull(function1, "onEnd");
        Intrinsics.checkParameterIsNotNull(function12, "onStart");
        Intrinsics.checkParameterIsNotNull(function13, "onCancel");
        Intrinsics.checkParameterIsNotNull(function14, "onResume");
        Intrinsics.checkParameterIsNotNull(function15, "onPause");
        TransitionKt$addListener$listener$1 listener = new TransitionKt$addListener$listener$1(function1, function14, function15, function13, function12);
        $this$addListener.addListener(listener);
        return listener;
    }
}
