package kotlin.text;

import androidx.exifinterface.media.ExifInterface;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
/* compiled from: Regex.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\u001a-\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0014\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0082\b\u001a\u001e\u0010\u0007\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\u0016\u0010\r\u001a\u0004\u0018\u00010\b*\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u001a\f\u0010\u000e\u001a\u00020\u000f*\u00020\u0010H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0002\u001a\u0012\u0010\u0012\u001a\u00020\u0006*\b\u0012\u0004\u0012\u00020\u00030\u0013H\u0002¨\u0006\u0014"}, d2 = {"fromInt", "", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/text/FlagEnum;", "", "value", "", "findNext", "Lkotlin/text/MatchResult;", "Ljava/util/regex/Matcher;", "from", "input", "", "matchEntire", "range", "Lkotlin/ranges/IntRange;", "Ljava/util/regex/MatchResult;", "groupIndex", "toInt", "", "kotlin-stdlib"}, k = 2, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class RegexKt {
    public static final /* synthetic */ MatchResult access$findNext(Matcher $this$access_u24findNext, int from, CharSequence input) {
        return findNext($this$access_u24findNext, from, input);
    }

    public static final /* synthetic */ MatchResult access$matchEntire(Matcher $this$access_u24matchEntire, CharSequence input) {
        return matchEntire($this$access_u24matchEntire, input);
    }

    public static final /* synthetic */ int access$toInt(Iterable $this$access_u24toInt) {
        return toInt($this$access_u24toInt);
    }

    public static final int toInt(Iterable<? extends FlagEnum> iterable) {
        int accumulator$iv = 0;
        for (FlagEnum option : iterable) {
            accumulator$iv |= option.getValue();
        }
        return accumulator$iv;
    }

    public static final /* synthetic */ <T extends Enum<T> & FlagEnum> Set<T> fromInt(int value) {
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_DIRECTION_TRUE);
        EnumSet $this$apply = EnumSet.allOf(Enum.class);
        CollectionsKt.retainAll($this$apply, new Function1<T, Boolean>(value) { // from class: kotlin.text.RegexKt$fromInt$$inlined$apply$lambda$1
            final /* synthetic */ int $value$inlined;

            {
                this.$value$inlined = r1;
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(Object obj) {
                return Boolean.valueOf(invoke((Enum) obj));
            }

            /* JADX WARN: Incorrect types in method signature: (TT;)Z */
            public final boolean invoke(Enum it) {
                return (this.$value$inlined & ((FlagEnum) it).getMask()) == ((FlagEnum) it).getValue();
            }
        });
        Set<T> unmodifiableSet = Collections.unmodifiableSet($this$apply);
        Intrinsics.checkExpressionValueIsNotNull(unmodifiableSet, "Collections.unmodifiable…mask == it.value }\n    })");
        return unmodifiableSet;
    }

    public static final MatchResult findNext(Matcher $this$findNext, int from, CharSequence input) {
        if (!$this$findNext.find(from)) {
            return null;
        }
        return new MatcherMatchResult($this$findNext, input);
    }

    public static final MatchResult matchEntire(Matcher $this$matchEntire, CharSequence input) {
        if (!$this$matchEntire.matches()) {
            return null;
        }
        return new MatcherMatchResult($this$matchEntire, input);
    }

    public static final IntRange range(MatchResult $this$range) {
        return RangesKt.until($this$range.start(), $this$range.end());
    }

    public static final IntRange range(MatchResult $this$range, int groupIndex) {
        return RangesKt.until($this$range.start(groupIndex), $this$range.end(groupIndex));
    }
}
