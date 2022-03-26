package androidx.core.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TextView.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0080\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u00022d\b\u0006\u0010\u0003\u001a^\u0012\u0015\u0012\u0013\u0018\u00010\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\r0\u00042d\b\u0006\u0010\u000e\u001a^\u0012\u0015\u0012\u0013\u0018\u00010\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\r0\u00042%\b\u0006\u0010\u0010\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u0012¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\r0\u0011H\u0086\b\u001a4\u0010\u0013\u001a\u00020\u0001*\u00020\u00022%\b\u0004\u0010\u0014\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u0012¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\r0\u0011H\u0086\b\u001as\u0010\u0015\u001a\u00020\u0001*\u00020\u00022d\b\u0004\u0010\u0014\u001a^\u0012\u0015\u0012\u0013\u0018\u00010\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\f\u0012\u0004\u0012\u00020\r0\u0004H\u0086\b\u001as\u0010\u0016\u001a\u00020\u0001*\u00020\u00022d\b\u0004\u0010\u0014\u001a^\u0012\u0015\u0012\u0013\u0018\u00010\u0005¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000f\u0012\u0013\u0012\u00110\t¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\r0\u0004H\u0086\b¨\u0006\u0017"}, d2 = {"addTextChangedListener", "Landroid/text/TextWatcher;", "Landroid/widget/TextView;", "beforeTextChanged", "Lkotlin/Function4;", "", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "text", "", "start", "count", "after", "", "onTextChanged", "before", "afterTextChanged", "Lkotlin/Function1;", "Landroid/text/Editable;", "doAfterTextChanged", "action", "doBeforeTextChanged", "doOnTextChanged", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class TextViewKt {
    public static final TextWatcher doBeforeTextChanged(TextView $this$doBeforeTextChanged, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> function4) {
        Intrinsics.checkParameterIsNotNull($this$doBeforeTextChanged, "$this$doBeforeTextChanged");
        Intrinsics.checkParameterIsNotNull(function4, "action");
        TextViewKt$doBeforeTextChanged$$inlined$addTextChangedListener$1 textWatcher$iv = new TextWatcher() { // from class: androidx.core.widget.TextViewKt$doBeforeTextChanged$$inlined$addTextChangedListener$1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                Function4.this.invoke(text, Integer.valueOf(start), Integer.valueOf(count), Integer.valueOf(after));
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }
        };
        $this$doBeforeTextChanged.addTextChangedListener(textWatcher$iv);
        return textWatcher$iv;
    }

    public static final TextWatcher doOnTextChanged(TextView $this$doOnTextChanged, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> function4) {
        Intrinsics.checkParameterIsNotNull($this$doOnTextChanged, "$this$doOnTextChanged");
        Intrinsics.checkParameterIsNotNull(function4, "action");
        TextViewKt$doOnTextChanged$$inlined$addTextChangedListener$1 textWatcher$iv = new TextWatcher() { // from class: androidx.core.widget.TextViewKt$doOnTextChanged$$inlined$addTextChangedListener$1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                Function4.this.invoke(text, Integer.valueOf(start), Integer.valueOf(before), Integer.valueOf(count));
            }
        };
        $this$doOnTextChanged.addTextChangedListener(textWatcher$iv);
        return textWatcher$iv;
    }

    public static final TextWatcher doAfterTextChanged(TextView $this$doAfterTextChanged, Function1<? super Editable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$doAfterTextChanged, "$this$doAfterTextChanged");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        TextViewKt$doAfterTextChanged$$inlined$addTextChangedListener$1 textWatcher$iv = new TextWatcher() { // from class: androidx.core.widget.TextViewKt$doAfterTextChanged$$inlined$addTextChangedListener$1
            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                Function1.this.invoke(s);
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence text, int start, int before, int count) {
            }
        };
        $this$doAfterTextChanged.addTextChangedListener(textWatcher$iv);
        return textWatcher$iv;
    }

    public static /* synthetic */ TextWatcher addTextChangedListener$default(TextView $this$addTextChangedListener, Function4 beforeTextChanged, Function4 onTextChanged, Function1 afterTextChanged, int i, Object obj) {
        if ((i & 1) != 0) {
            beforeTextChanged = TextViewKt$addTextChangedListener$1.INSTANCE;
        }
        if ((i & 2) != 0) {
            onTextChanged = TextViewKt$addTextChangedListener$2.INSTANCE;
        }
        if ((i & 4) != 0) {
            afterTextChanged = TextViewKt$addTextChangedListener$3.INSTANCE;
        }
        Intrinsics.checkParameterIsNotNull($this$addTextChangedListener, "$this$addTextChangedListener");
        Intrinsics.checkParameterIsNotNull(beforeTextChanged, "beforeTextChanged");
        Intrinsics.checkParameterIsNotNull(onTextChanged, "onTextChanged");
        Intrinsics.checkParameterIsNotNull(afterTextChanged, "afterTextChanged");
        TextViewKt$addTextChangedListener$textWatcher$1 textWatcher = new TextViewKt$addTextChangedListener$textWatcher$1(afterTextChanged, beforeTextChanged, onTextChanged);
        $this$addTextChangedListener.addTextChangedListener(textWatcher);
        return textWatcher;
    }

    public static final TextWatcher addTextChangedListener(TextView $this$addTextChangedListener, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> function4, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> function42, Function1<? super Editable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$addTextChangedListener, "$this$addTextChangedListener");
        Intrinsics.checkParameterIsNotNull(function4, "beforeTextChanged");
        Intrinsics.checkParameterIsNotNull(function42, "onTextChanged");
        Intrinsics.checkParameterIsNotNull(function1, "afterTextChanged");
        TextViewKt$addTextChangedListener$textWatcher$1 textWatcher = new TextViewKt$addTextChangedListener$textWatcher$1(function1, function4, function42);
        $this$addTextChangedListener.addTextChangedListener(textWatcher);
        return textWatcher;
    }
}
