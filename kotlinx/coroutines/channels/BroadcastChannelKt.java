package kotlinx.coroutines.channels;

import androidx.exifinterface.media.ExifInterface;
import kotlin.Metadata;
/* compiled from: BroadcastChannel.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006\u0005"}, d2 = {"BroadcastChannel", "Lkotlinx/coroutines/channels/BroadcastChannel;", ExifInterface.LONGITUDE_EAST, "capacity", "", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public final class BroadcastChannelKt {
    public static final <E> BroadcastChannel<E> BroadcastChannel(int capacity) {
        if (capacity == -1) {
            return new ConflatedBroadcastChannel();
        }
        if (capacity == 0) {
            throw new IllegalArgumentException("Unsupported 0 capacity for BroadcastChannel");
        } else if (capacity != Integer.MAX_VALUE) {
            return new ArrayBroadcastChannel(capacity);
        } else {
            throw new IllegalArgumentException("Unsupported UNLIMITED capacity for BroadcastChannel");
        }
    }
}
