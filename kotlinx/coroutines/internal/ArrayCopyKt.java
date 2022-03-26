package kotlinx.coroutines.internal;

import androidx.exifinterface.media.ExifInterface;
import com.google.firebase.analytics.FirebaseAnalytics;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ArrayCopy.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u001aI\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\u00020\u00042\u0006\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006H\u0000¢\u0006\u0002\u0010\n¨\u0006\u000b"}, d2 = {"arraycopy", "", ExifInterface.LONGITUDE_EAST, FirebaseAnalytics.Param.SOURCE, "", "srcPos", "", FirebaseAnalytics.Param.DESTINATION, "destinationStart", "length", "([Ljava/lang/Object;I[Ljava/lang/Object;II)V", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class ArrayCopyKt {
    public static final <E> void arraycopy(E[] eArr, int srcPos, E[] eArr2, int destinationStart, int length) {
        Intrinsics.checkParameterIsNotNull(eArr, FirebaseAnalytics.Param.SOURCE);
        Intrinsics.checkParameterIsNotNull(eArr2, FirebaseAnalytics.Param.DESTINATION);
        System.arraycopy(eArr, srcPos, eArr2, destinationStart, length);
    }
}
