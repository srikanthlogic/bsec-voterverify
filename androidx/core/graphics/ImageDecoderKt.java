package androidx.core.graphics;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ImageDecoder.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aR\u0010\u0000\u001a\u00020\u0001*\u00020\u00022C\b\u0004\u0010\u0003\u001a=\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0004¢\u0006\u0002\b\fH\u0087\b\u001aR\u0010\r\u001a\u00020\u000e*\u00020\u00022C\b\u0004\u0010\u0003\u001a=\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0006¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0004¢\u0006\u0002\b\fH\u0087\b¨\u0006\u000f"}, d2 = {"decodeBitmap", "Landroid/graphics/Bitmap;", "Landroid/graphics/ImageDecoder$Source;", "action", "Lkotlin/Function3;", "Landroid/graphics/ImageDecoder;", "Landroid/graphics/ImageDecoder$ImageInfo;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "info", FirebaseAnalytics.Param.SOURCE, "", "Lkotlin/ExtensionFunctionType;", "decodeDrawable", "Landroid/graphics/drawable/Drawable;", "core-ktx_release"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes.dex */
public final class ImageDecoderKt {
    public static final Bitmap decodeBitmap(ImageDecoder.Source $this$decodeBitmap, Function3<? super ImageDecoder, ? super ImageDecoder.ImageInfo, ? super ImageDecoder.Source, Unit> function3) {
        Intrinsics.checkParameterIsNotNull($this$decodeBitmap, "$this$decodeBitmap");
        Intrinsics.checkParameterIsNotNull(function3, "action");
        Bitmap decodeBitmap = ImageDecoder.decodeBitmap($this$decodeBitmap, new ImageDecoder.OnHeaderDecodedListener() { // from class: androidx.core.graphics.ImageDecoderKt$decodeBitmap$1
            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
            public final void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
                Intrinsics.checkParameterIsNotNull(decoder, "decoder");
                Intrinsics.checkParameterIsNotNull(info, "info");
                Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
                Function3.this.invoke(decoder, info, source);
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(decodeBitmap, "ImageDecoder.decodeBitma…ction(info, source)\n    }");
        return decodeBitmap;
    }

    public static final Drawable decodeDrawable(ImageDecoder.Source $this$decodeDrawable, Function3<? super ImageDecoder, ? super ImageDecoder.ImageInfo, ? super ImageDecoder.Source, Unit> function3) {
        Intrinsics.checkParameterIsNotNull($this$decodeDrawable, "$this$decodeDrawable");
        Intrinsics.checkParameterIsNotNull(function3, "action");
        Drawable decodeDrawable = ImageDecoder.decodeDrawable($this$decodeDrawable, new ImageDecoder.OnHeaderDecodedListener() { // from class: androidx.core.graphics.ImageDecoderKt$decodeDrawable$1
            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
            public final void onHeaderDecoded(ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source source) {
                Intrinsics.checkParameterIsNotNull(decoder, "decoder");
                Intrinsics.checkParameterIsNotNull(info, "info");
                Intrinsics.checkParameterIsNotNull(source, FirebaseAnalytics.Param.SOURCE);
                Function3.this.invoke(decoder, info, source);
            }
        });
        Intrinsics.checkExpressionValueIsNotNull(decodeDrawable, "ImageDecoder.decodeDrawa…ction(info, source)\n    }");
        return decodeDrawable;
    }
}
