package androidx.core.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import androidx.core.util.Preconditions;
import androidx.core.util.Predicate;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class ContentInfoCompat {
    public static final int FLAG_CONVERT_TO_PLAIN_TEXT;
    public static final int SOURCE_APP;
    public static final int SOURCE_CLIPBOARD;
    public static final int SOURCE_DRAG_AND_DROP;
    public static final int SOURCE_INPUT_METHOD;
    final ClipData mClip;
    final Bundle mExtras;
    final int mFlags;
    final Uri mLinkUri;
    final int mSource;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Flags {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Source {
    }

    static String sourceToString(int source) {
        if (source == 0) {
            return "SOURCE_APP";
        }
        if (source == 1) {
            return "SOURCE_CLIPBOARD";
        }
        if (source == 2) {
            return "SOURCE_INPUT_METHOD";
        }
        if (source != 3) {
            return String.valueOf(source);
        }
        return "SOURCE_DRAG_AND_DROP";
    }

    static String flagsToString(int flags) {
        if ((flags & 1) != 0) {
            return "FLAG_CONVERT_TO_PLAIN_TEXT";
        }
        return String.valueOf(flags);
    }

    ContentInfoCompat(Builder b) {
        this.mClip = (ClipData) Preconditions.checkNotNull(b.mClip);
        this.mSource = Preconditions.checkArgumentInRange(b.mSource, 0, 3, FirebaseAnalytics.Param.SOURCE);
        this.mFlags = Preconditions.checkFlagsArgument(b.mFlags, 1);
        this.mLinkUri = b.mLinkUri;
        this.mExtras = b.mExtras;
    }

    public String toString() {
        return "ContentInfoCompat{clip=" + this.mClip + ", source=" + sourceToString(this.mSource) + ", flags=" + flagsToString(this.mFlags) + ", linkUri=" + this.mLinkUri + ", extras=" + this.mExtras + "}";
    }

    public ClipData getClip() {
        return this.mClip;
    }

    public int getSource() {
        return this.mSource;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public Uri getLinkUri() {
        return this.mLinkUri;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public Pair<ContentInfoCompat, ContentInfoCompat> partition(Predicate<ClipData.Item> itemPredicate) {
        ContentInfoCompat contentInfoCompat = null;
        if (this.mClip.getItemCount() == 1) {
            boolean matched = itemPredicate.test(this.mClip.getItemAt(0));
            ContentInfoCompat contentInfoCompat2 = matched ? this : null;
            if (!matched) {
                contentInfoCompat = this;
            }
            return Pair.create(contentInfoCompat2, contentInfoCompat);
        }
        ArrayList<ClipData.Item> acceptedItems = new ArrayList<>();
        ArrayList<ClipData.Item> remainingItems = new ArrayList<>();
        for (int i = 0; i < this.mClip.getItemCount(); i++) {
            ClipData.Item item = this.mClip.getItemAt(i);
            if (itemPredicate.test(item)) {
                acceptedItems.add(item);
            } else {
                remainingItems.add(item);
            }
        }
        if (acceptedItems.isEmpty()) {
            return Pair.create(null, this);
        }
        if (remainingItems.isEmpty()) {
            return Pair.create(this, null);
        }
        return Pair.create(new Builder(this).setClip(buildClipData(this.mClip.getDescription(), acceptedItems)).build(), new Builder(this).setClip(buildClipData(this.mClip.getDescription(), remainingItems)).build());
    }

    private static ClipData buildClipData(ClipDescription description, List<ClipData.Item> items) {
        ClipData clip = new ClipData(new ClipDescription(description), items.get(0));
        for (int i = 1; i < items.size(); i++) {
            clip.addItem(items.get(i));
        }
        return clip;
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        ClipData mClip;
        Bundle mExtras;
        int mFlags;
        Uri mLinkUri;
        int mSource;

        public Builder(ContentInfoCompat other) {
            this.mClip = other.mClip;
            this.mSource = other.mSource;
            this.mFlags = other.mFlags;
            this.mLinkUri = other.mLinkUri;
            this.mExtras = other.mExtras;
        }

        public Builder(ClipData clip, int source) {
            this.mClip = clip;
            this.mSource = source;
        }

        public Builder setClip(ClipData clip) {
            this.mClip = clip;
            return this;
        }

        public Builder setSource(int source) {
            this.mSource = source;
            return this;
        }

        public Builder setFlags(int flags) {
            this.mFlags = flags;
            return this;
        }

        public Builder setLinkUri(Uri linkUri) {
            this.mLinkUri = linkUri;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public ContentInfoCompat build() {
            return new ContentInfoCompat(this);
        }
    }
}
