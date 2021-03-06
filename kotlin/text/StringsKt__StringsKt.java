package kotlin.text;

import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
/* compiled from: Strings.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000|\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u001b\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u000e\u001a\u00020\n*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u001f\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\rH\u0086\u0002\u001a\u0015\u0010\u000f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010\u0014\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a:\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001aE\u0010\u0016\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\rH\u0002??\u0006\u0002\b\u001c\u001a:\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n\u0018\u00010\u0017*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010\u001e\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u0006\u001a4\u0010 \u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0087\b??\u0006\u0002\u0010%\u001a4\u0010&\u001a\u0002H!\"\f\b\u0000\u0010\"*\u00020\u0002*\u0002H!\"\u0004\b\u0001\u0010!*\u0002H\"2\f\u0010#\u001a\b\u0012\u0004\u0012\u0002H!0$H\u0087\b??\u0006\u0002\u0010%\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a;\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u001b\u001a\u00020\rH\u0002??\u0006\u0002\b)\u001a&\u0010'\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u0010+\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u0010+\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\r\u0010.\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u0010/\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a\r\u00100\u001a\u00020\r*\u00020\u0002H\u0087\b\u001a \u00101\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00102\u001a\u00020\r*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00103\u001a\u000204*\u00020\u0002H\u0086\u0002\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00105\u001a\u00020\u0006*\u00020\u00022\u0006\u0010*\u001a\u00020\n2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a&\u00106\u001a\u00020\u0006*\u00020\u00022\u0006\u0010,\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a,\u00106\u001a\u00020\u0006*\u00020\u00022\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\n0\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0010\u00107\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u0002\u001a\u0010\u00109\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u0002\u001a\u0015\u0010;\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\f\u001a\u000f\u0010<\u001a\u00020\n*\u0004\u0018\u00010\nH\u0087\b\u001a\u001c\u0010=\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010=\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\u0002*\u00020\u00022\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001a\u001c\u0010@\u001a\u00020\n*\u00020\n2\u0006\u0010>\u001a\u00020\u00062\b\b\u0002\u0010?\u001a\u00020\u0011\u001aG\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u000e\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002??\u0006\u0004\bE\u0010F\u001a=\u0010A\u001a\b\u0012\u0004\u0012\u00020\u000108*\u00020\u00022\u0006\u0010B\u001a\u00020-2\b\b\u0002\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006H\u0002??\u0006\u0002\bE\u001a4\u0010G\u001a\u00020\r*\u00020\u00022\u0006\u0010H\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00022\u0006\u0010I\u001a\u00020\u00062\u0006\u0010>\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u0012\u0010J\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u0002\u001a\u0012\u0010J\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u0002\u001a\u001a\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006\u001a\u0012\u0010L\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010L\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\n*\u00020\n2\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a\u0012\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u0002\u001a\u001a\u0010O\u001a\u00020\n*\u00020\n2\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0002\u001a+\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0014\b\b\u0010R\u001a\u000e\u0012\u0004\u0012\u00020T\u0012\u0004\u0012\u00020\u00020SH\u0087\b\u001a\u001d\u0010Q\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\u0087\b\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010V\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010X\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Y\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a$\u0010Z\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\u0006\u0010U\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001d\u0010[\u001a\u00020\n*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010U\u001a\u00020\nH\u0087\b\u001a\"\u0010\\\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002\u001a\u001a\u0010\\\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002\u001a%\u0010\\\u001a\u00020\n*\u00020\n2\u0006\u0010\u001a\u001a\u00020\u00062\u0006\u0010(\u001a\u00020\u00062\u0006\u0010U\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010\\\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u00012\u0006\u0010U\u001a\u00020\u0002H\u0087\b\u001a=\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006??\u0006\u0002\u0010^\u001a0\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a/\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010P\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010D\u001a\u00020\u0006H\u0002??\u0006\u0002\b_\u001a%\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0:*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010D\u001a\u00020\u0006H\u0087\b\u001a=\u0010`\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\u0012\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0C\"\u00020\n2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006??\u0006\u0002\u0010a\u001a0\u0010`\u001a\b\u0012\u0004\u0012\u00020\n08*\u00020\u00022\n\u0010B\u001a\u00020-\"\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\u0006\u001a\u001c\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u001c\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\b\b\u0002\u0010\f\u001a\u00020\r\u001a$\u0010b\u001a\u00020\r*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010\f\u001a\u00020\r\u001a\u0012\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u001d\u0010c\u001a\u00020\u0002*\u00020\n2\u0006\u0010d\u001a\u00020\u00062\u0006\u0010e\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010f\u001a\u00020\n*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00062\b\b\u0002\u0010(\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010f\u001a\u00020\n*\u00020\u00022\u0006\u0010M\u001a\u00020\u0001\u001a\u0012\u0010f\u001a\u00020\n*\u00020\n2\u0006\u0010M\u001a\u00020\u0001\u001a\u001c\u0010g\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010g\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010h\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010h\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010i\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010i\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\u00112\b\b\u0002\u0010W\u001a\u00020\n\u001a\u001c\u0010j\u001a\u00020\n*\u00020\n2\u0006\u0010P\u001a\u00020\n2\b\b\u0002\u0010W\u001a\u00020\n\u001a\n\u0010k\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010k\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010k\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010k\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010k\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010k\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010m\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010m\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010m\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010m\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010m\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010m\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\n\u0010n\u001a\u00020\u0002*\u00020\u0002\u001a!\u0010n\u001a\u00020\u0002*\u00020\u00022\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010n\u001a\u00020\u0002*\u00020\u00022\n\u0010,\u001a\u00020-\"\u00020\u0011\u001a\r\u0010n\u001a\u00020\n*\u00020\nH\u0087\b\u001a!\u0010n\u001a\u00020\n*\u00020\n2\u0012\u0010l\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\r0SH\u0086\b\u001a\u0016\u0010n\u001a\u00020\n*\u00020\n2\n\u0010,\u001a\u00020-\"\u00020\u0011\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F??\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F??\u0006\u0006\u001a\u0004\b\u0007\u0010\b??\u0006o"}, d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", FirebaseAnalytics.Param.INDEX, "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "limit", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: classes3.dex */
public class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    public static final CharSequence trim(CharSequence $this$trim, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int startIndex = 0;
        int endIndex = $this$trim.length() - 1;
        boolean startFound = false;
        while (startIndex <= endIndex) {
            boolean match = function1.invoke(Character.valueOf($this$trim.charAt(!startFound ? startIndex : endIndex))).booleanValue();
            if (!startFound) {
                if (!match) {
                    startFound = true;
                } else {
                    startIndex++;
                }
            } else if (!match) {
                break;
            } else {
                endIndex--;
            }
        }
        return $this$trim.subSequence(startIndex, endIndex + 1);
    }

    public static final String trim(String $this$trim, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        String $this$trim$iv = $this$trim;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            boolean match$iv = function1.invoke(Character.valueOf($this$trim$iv.charAt(!startFound$iv ? startIndex$iv : endIndex$iv))).booleanValue();
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1).toString();
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$trimStart.length();
        for (int index = 0; index < length; index++) {
            if (!function1.invoke(Character.valueOf($this$trimStart.charAt(index))).booleanValue()) {
                return $this$trimStart.subSequence(index, $this$trimStart.length());
            }
        }
        return "";
    }

    public static final String trimStart(String $this$trimStart, Function1<? super Character, Boolean> function1) {
        String str;
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        String $this$trimStart$iv = $this$trimStart;
        int length = $this$trimStart$iv.length();
        int index$iv = 0;
        while (true) {
            if (index$iv >= length) {
                break;
            } else if (!function1.invoke(Character.valueOf($this$trimStart$iv.charAt(index$iv))).booleanValue()) {
                str = $this$trimStart$iv.subSequence(index$iv, $this$trimStart$iv.length());
                break;
            } else {
                index$iv++;
            }
        }
        return str.toString();
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int index = $this$trimEnd.length();
        do {
            index--;
            if (index < 0) {
                return "";
            }
        } while (function1.invoke(Character.valueOf($this$trimEnd.charAt(index))).booleanValue());
        return $this$trimEnd.subSequence(0, index + 1);
    }

    public static final String trimEnd(String $this$trimEnd, Function1<? super Character, Boolean> function1) {
        String str;
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        String $this$trimEnd$iv = $this$trimEnd;
        int index$iv = $this$trimEnd$iv.length();
        while (true) {
            index$iv--;
            if (index$iv >= 0) {
                if (!function1.invoke(Character.valueOf($this$trimEnd$iv.charAt(index$iv))).booleanValue()) {
                    str = $this$trimEnd$iv.subSequence(0, index$iv + 1);
                    break;
                }
            } else {
                break;
            }
        }
        return str.toString();
    }

    /* JADX INFO: Multiple debug info for r6v1 boolean: [D('it' char), D('match$iv' boolean)] */
    public static final CharSequence trim(CharSequence $this$trim, char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            boolean match$iv = ArraysKt.contains(chars, $this$trim.charAt(!startFound$iv ? startIndex$iv : endIndex$iv));
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    /* JADX INFO: Multiple debug info for r8v1 boolean: [D('it' char), D('match$iv$iv' boolean)] */
    public static final String trim(String $this$trim, char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        String $this$trim$iv$iv = $this$trim;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            boolean match$iv$iv = ArraysKt.contains(chars, $this$trim$iv$iv.charAt(!startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv));
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                } else {
                    startIndex$iv$iv++;
                }
            } else if (!match$iv$iv) {
                break;
            } else {
                endIndex$iv$iv--;
            }
        }
        return $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart, char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        int length = $this$trimStart.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            if (!ArraysKt.contains(chars, $this$trimStart.charAt(index$iv))) {
                return $this$trimStart.subSequence(index$iv, $this$trimStart.length());
            }
        }
        return "";
    }

    public static final String trimStart(String $this$trimStart, char... chars) {
        String str;
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        String $this$trimStart$iv$iv = $this$trimStart;
        int length = $this$trimStart$iv$iv.length();
        int index$iv$iv = 0;
        while (true) {
            if (index$iv$iv >= length) {
                break;
            } else if (!ArraysKt.contains(chars, $this$trimStart$iv$iv.charAt(index$iv$iv))) {
                str = $this$trimStart$iv$iv.subSequence(index$iv$iv, $this$trimStart$iv$iv.length());
                break;
            } else {
                index$iv$iv++;
            }
        }
        return str.toString();
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd, char... chars) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        int index$iv = $this$trimEnd.length();
        do {
            index$iv--;
            if (index$iv < 0) {
                return "";
            }
        } while (ArraysKt.contains(chars, $this$trimEnd.charAt(index$iv)));
        return $this$trimEnd.subSequence(0, index$iv + 1);
    }

    public static final String trimEnd(String $this$trimEnd, char... chars) {
        String str;
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        String $this$trimEnd$iv$iv = $this$trimEnd;
        int index$iv$iv = $this$trimEnd$iv$iv.length();
        while (true) {
            index$iv$iv--;
            if (index$iv$iv >= 0) {
                if (!ArraysKt.contains(chars, $this$trimEnd$iv$iv.charAt(index$iv$iv))) {
                    str = $this$trimEnd$iv$iv.subSequence(0, index$iv$iv + 1);
                    break;
                }
            } else {
                break;
            }
        }
        return str.toString();
    }

    /* JADX INFO: Multiple debug info for r6v1 boolean: [D('p1' char), D('match$iv' boolean)] */
    public static final CharSequence trim(CharSequence $this$trim) {
        Intrinsics.checkParameterIsNotNull($this$trim, "$this$trim");
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            boolean match$iv = CharsKt.isWhitespace($this$trim.charAt(!startFound$iv ? startIndex$iv : endIndex$iv));
            if (!startFound$iv) {
                if (!match$iv) {
                    startFound$iv = true;
                } else {
                    startIndex$iv++;
                }
            } else if (!match$iv) {
                break;
            } else {
                endIndex$iv--;
            }
        }
        return $this$trim.subSequence(startIndex$iv, endIndex$iv + 1);
    }

    private static final String trim(String $this$trim) {
        if ($this$trim != null) {
            return StringsKt.trim((CharSequence) $this$trim).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence trimStart(CharSequence $this$trimStart) {
        Intrinsics.checkParameterIsNotNull($this$trimStart, "$this$trimStart");
        int length = $this$trimStart.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            if (!CharsKt.isWhitespace($this$trimStart.charAt(index$iv))) {
                return $this$trimStart.subSequence(index$iv, $this$trimStart.length());
            }
        }
        return "";
    }

    private static final String trimStart(String $this$trimStart) {
        if ($this$trimStart != null) {
            return StringsKt.trimStart((CharSequence) $this$trimStart).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence trimEnd(CharSequence $this$trimEnd) {
        Intrinsics.checkParameterIsNotNull($this$trimEnd, "$this$trimEnd");
        int index$iv = $this$trimEnd.length();
        do {
            index$iv--;
            if (index$iv < 0) {
                return "";
            }
        } while (CharsKt.isWhitespace($this$trimEnd.charAt(index$iv)));
        return $this$trimEnd.subSequence(0, index$iv + 1);
    }

    private static final String trimEnd(String $this$trimEnd) {
        if ($this$trimEnd != null) {
            return StringsKt.trimEnd((CharSequence) $this$trimEnd).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padStart(charSequence, i, c);
    }

    public static final CharSequence padStart(CharSequence $this$padStart, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padStart, "$this$padStart");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padStart.length()) {
            return $this$padStart.subSequence(0, $this$padStart.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            int length2 = length - $this$padStart.length();
            int i = 1;
            if (1 <= length2) {
                while (true) {
                    sb.append(padChar);
                    if (i == length2) {
                        break;
                    }
                    i++;
                }
            }
            sb.append($this$padStart);
            return sb;
        }
    }

    public static /* synthetic */ String padStart$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padStart(str, i, c);
    }

    public static final String padStart(String $this$padStart, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padStart, "$this$padStart");
        return StringsKt.padStart((CharSequence) $this$padStart, length, padChar).toString();
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence charSequence, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padEnd(charSequence, i, c);
    }

    public static final CharSequence padEnd(CharSequence $this$padEnd, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padEnd, "$this$padEnd");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        } else if (length <= $this$padEnd.length()) {
            return $this$padEnd.subSequence(0, $this$padEnd.length());
        } else {
            StringBuilder sb = new StringBuilder(length);
            sb.append($this$padEnd);
            int length2 = length - $this$padEnd.length();
            int i = 1;
            if (1 <= length2) {
                while (true) {
                    sb.append(padChar);
                    if (i == length2) {
                        break;
                    }
                    i++;
                }
            }
            return sb;
        }
    }

    public static /* synthetic */ String padEnd$default(String str, int i, char c, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return StringsKt.padEnd(str, i, c);
    }

    public static final String padEnd(String $this$padEnd, int length, char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padEnd, "$this$padEnd");
        return StringsKt.padEnd((CharSequence) $this$padEnd, length, padChar).toString();
    }

    private static final boolean isNullOrEmpty(CharSequence $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.length() == 0;
    }

    private static final boolean isEmpty(CharSequence $this$isEmpty) {
        return $this$isEmpty.length() == 0;
    }

    private static final boolean isNotEmpty(CharSequence $this$isNotEmpty) {
        return $this$isNotEmpty.length() > 0;
    }

    private static final boolean isNotBlank(CharSequence $this$isNotBlank) {
        return !StringsKt.isBlank($this$isNotBlank);
    }

    private static final boolean isNullOrBlank(CharSequence $this$isNullOrBlank) {
        return $this$isNullOrBlank == null || StringsKt.isBlank($this$isNullOrBlank);
    }

    public static final CharIterator iterator(CharSequence $this$iterator) {
        Intrinsics.checkParameterIsNotNull($this$iterator, "$this$iterator");
        return new CharIterator($this$iterator) { // from class: kotlin.text.StringsKt__StringsKt$iterator$1
            final /* synthetic */ CharSequence $this_iterator;
            private int index;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_iterator = $receiver;
            }

            @Override // kotlin.collections.CharIterator
            public char nextChar() {
                CharSequence charSequence = this.$this_iterator;
                int i = this.index;
                this.index = i + 1;
                return charSequence.charAt(i);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < this.$this_iterator.length();
            }
        };
    }

    private static final String orEmpty(String $this$orEmpty) {
        return $this$orEmpty != null ? $this$orEmpty : "";
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends CharSequence & R, R> R ifEmpty(C c, Function0<? extends R> function0) {
        return c.length() == 0 ? (R) function0.invoke() : c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends CharSequence & R, R> R ifBlank(C c, Function0<? extends R> function0) {
        return StringsKt.isBlank(c) ? (R) function0.invoke() : c;
    }

    public static final IntRange getIndices(CharSequence $this$indices) {
        Intrinsics.checkParameterIsNotNull($this$indices, "$this$indices");
        return new IntRange(0, $this$indices.length() - 1);
    }

    public static final int getLastIndex(CharSequence $this$lastIndex) {
        Intrinsics.checkParameterIsNotNull($this$lastIndex, "$this$lastIndex");
        return $this$lastIndex.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(CharSequence $this$hasSurrogatePairAt, int index) {
        Intrinsics.checkParameterIsNotNull($this$hasSurrogatePairAt, "$this$hasSurrogatePairAt");
        return index >= 0 && $this$hasSurrogatePairAt.length() + -2 >= index && Character.isHighSurrogate($this$hasSurrogatePairAt.charAt(index)) && Character.isLowSurrogate($this$hasSurrogatePairAt.charAt(index + 1));
    }

    public static final String substring(String $this$substring, IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$substring, "$this$substring");
        Intrinsics.checkParameterIsNotNull(range, "range");
        String substring = $this$substring.substring(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static final CharSequence subSequence(CharSequence $this$subSequence, IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$subSequence, "$this$subSequence");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return $this$subSequence.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    @Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    private static final CharSequence subSequence(String $this$subSequence, int start, int end) {
        return $this$subSequence.subSequence(start, end);
    }

    private static final String substring(CharSequence $this$substring, int startIndex, int endIndex) {
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    static /* synthetic */ String substring$default(CharSequence $this$substring, int startIndex, int endIndex, int i, Object obj) {
        if ((i & 2) != 0) {
            endIndex = $this$substring.length();
        }
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    public static final String substring(CharSequence $this$substring, IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$substring, "$this$substring");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return $this$substring.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1).toString();
    }

    public static /* synthetic */ String substringBefore$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringBefore(str, c, str2);
    }

    public static final String substringBefore(String $this$substringBefore, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBefore, "$this$substringBefore");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBefore.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringBefore$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringBefore(str, str2, str3);
    }

    public static final String substringBefore(String $this$substringBefore, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBefore, "$this$substringBefore");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringBefore, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBefore.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringAfter$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringAfter(str, c, str2);
    }

    public static final String substringAfter(String $this$substringAfter, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfter, "$this$substringAfter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfter.substring(index + 1, $this$substringAfter.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringAfter$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringAfter(str, str2, str3);
    }

    public static final String substringAfter(String $this$substringAfter, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfter, "$this$substringAfter");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$substringAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfter.substring(delimiter.length() + index, $this$substringAfter.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringBeforeLast(str, c, str2);
    }

    public static final String substringBeforeLast(String $this$substringBeforeLast, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBeforeLast, "$this$substringBeforeLast");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBeforeLast.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringBeforeLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringBeforeLast(str, str2, str3);
    }

    public static final String substringBeforeLast(String $this$substringBeforeLast, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringBeforeLast, "$this$substringBeforeLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringBeforeLast.substring(0, index);
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, char c, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return StringsKt.substringAfterLast(str, c, str2);
    }

    public static final String substringAfterLast(String $this$substringAfterLast, char delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfterLast, "$this$substringAfterLast");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfterLast.substring(index + 1, $this$substringAfterLast.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static /* synthetic */ String substringAfterLast$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return StringsKt.substringAfterLast(str, str2, str3);
    }

    public static final String substringAfterLast(String $this$substringAfterLast, String delimiter, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$substringAfterLast, "$this$substringAfterLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        String substring = $this$substringAfterLast.substring(delimiter.length() + index, $this$substringAfterLast.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static final CharSequence replaceRange(CharSequence $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        Intrinsics.checkParameterIsNotNull($this$replaceRange, "$this$replaceRange");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        if (endIndex >= startIndex) {
            StringBuilder sb = new StringBuilder();
            sb.append($this$replaceRange, 0, startIndex);
            Intrinsics.checkExpressionValueIsNotNull(sb, "this.append(value, startIndex, endIndex)");
            sb.append(replacement);
            sb.append($this$replaceRange, endIndex, $this$replaceRange.length());
            Intrinsics.checkExpressionValueIsNotNull(sb, "this.append(value, startIndex, endIndex)");
            return sb;
        }
        throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
    }

    private static final String replaceRange(String $this$replaceRange, int startIndex, int endIndex, CharSequence replacement) {
        if ($this$replaceRange != null) {
            return StringsKt.replaceRange((CharSequence) $this$replaceRange, startIndex, endIndex, replacement).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence replaceRange(CharSequence $this$replaceRange, IntRange range, CharSequence replacement) {
        Intrinsics.checkParameterIsNotNull($this$replaceRange, "$this$replaceRange");
        Intrinsics.checkParameterIsNotNull(range, "range");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        return StringsKt.replaceRange($this$replaceRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1, replacement);
    }

    private static final String replaceRange(String $this$replaceRange, IntRange range, CharSequence replacement) {
        if ($this$replaceRange != null) {
            return StringsKt.replaceRange((CharSequence) $this$replaceRange, range, replacement).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence removeRange(CharSequence $this$removeRange, int startIndex, int endIndex) {
        Intrinsics.checkParameterIsNotNull($this$removeRange, "$this$removeRange");
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        } else if (endIndex == startIndex) {
            return $this$removeRange.subSequence(0, $this$removeRange.length());
        } else {
            StringBuilder sb = new StringBuilder($this$removeRange.length() - (endIndex - startIndex));
            sb.append($this$removeRange, 0, startIndex);
            Intrinsics.checkExpressionValueIsNotNull(sb, "this.append(value, startIndex, endIndex)");
            sb.append($this$removeRange, endIndex, $this$removeRange.length());
            Intrinsics.checkExpressionValueIsNotNull(sb, "this.append(value, startIndex, endIndex)");
            return sb;
        }
    }

    private static final String removeRange(String $this$removeRange, int startIndex, int endIndex) {
        if ($this$removeRange != null) {
            return StringsKt.removeRange((CharSequence) $this$removeRange, startIndex, endIndex).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence removeRange(CharSequence $this$removeRange, IntRange range) {
        Intrinsics.checkParameterIsNotNull($this$removeRange, "$this$removeRange");
        Intrinsics.checkParameterIsNotNull(range, "range");
        return StringsKt.removeRange($this$removeRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    private static final String removeRange(String $this$removeRange, IntRange range) {
        if ($this$removeRange != null) {
            return StringsKt.removeRange((CharSequence) $this$removeRange, range).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence removePrefix(CharSequence $this$removePrefix, CharSequence prefix) {
        Intrinsics.checkParameterIsNotNull($this$removePrefix, "$this$removePrefix");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (StringsKt.startsWith$default($this$removePrefix, prefix, false, 2, (Object) null)) {
            return $this$removePrefix.subSequence(prefix.length(), $this$removePrefix.length());
        }
        return $this$removePrefix.subSequence(0, $this$removePrefix.length());
    }

    public static final String removePrefix(String $this$removePrefix, CharSequence prefix) {
        Intrinsics.checkParameterIsNotNull($this$removePrefix, "$this$removePrefix");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (!StringsKt.startsWith$default((CharSequence) $this$removePrefix, prefix, false, 2, (Object) null)) {
            return $this$removePrefix;
        }
        String substring = $this$removePrefix.substring(prefix.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }

    public static final CharSequence removeSuffix(CharSequence $this$removeSuffix, CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSuffix, "$this$removeSuffix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (StringsKt.endsWith$default($this$removeSuffix, suffix, false, 2, (Object) null)) {
            return $this$removeSuffix.subSequence(0, $this$removeSuffix.length() - suffix.length());
        }
        return $this$removeSuffix.subSequence(0, $this$removeSuffix.length());
    }

    public static final String removeSuffix(String $this$removeSuffix, CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSuffix, "$this$removeSuffix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (!StringsKt.endsWith$default((CharSequence) $this$removeSuffix, suffix, false, 2, (Object) null)) {
            return $this$removeSuffix;
        }
        String substring = $this$removeSuffix.substring(0, $this$removeSuffix.length() - suffix.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static final CharSequence removeSurrounding(CharSequence $this$removeSurrounding, CharSequence prefix, CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default($this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default($this$removeSurrounding, suffix, false, 2, (Object) null)) {
            return $this$removeSurrounding.subSequence(0, $this$removeSurrounding.length());
        }
        return $this$removeSurrounding.subSequence(prefix.length(), $this$removeSurrounding.length() - suffix.length());
    }

    public static final String removeSurrounding(String $this$removeSurrounding, CharSequence prefix, CharSequence suffix) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if ($this$removeSurrounding.length() < prefix.length() + suffix.length() || !StringsKt.startsWith$default((CharSequence) $this$removeSurrounding, prefix, false, 2, (Object) null) || !StringsKt.endsWith$default((CharSequence) $this$removeSurrounding, suffix, false, 2, (Object) null)) {
            return $this$removeSurrounding;
        }
        String substring = $this$removeSurrounding.substring(prefix.length(), $this$removeSurrounding.length() - suffix.length());
        Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin???ing(startIndex, endIndex)");
        return substring;
    }

    public static final CharSequence removeSurrounding(CharSequence $this$removeSurrounding, CharSequence delimiter) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static final String removeSurrounding(String $this$removeSurrounding, CharSequence delimiter) {
        Intrinsics.checkParameterIsNotNull($this$removeSurrounding, "$this$removeSurrounding");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        return StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static /* synthetic */ String replaceBefore$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceBefore(str, c, str2, str3);
    }

    public static final String replaceBefore(String $this$replaceBefore, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBefore, "$this$replaceBefore");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBefore$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceBefore(str, str2, str3, str4);
    }

    public static final String replaceBefore(String $this$replaceBefore, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBefore, "$this$replaceBefore");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBefore, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceAfter(str, c, str2, str3);
    }

    public static final String replaceAfter(String $this$replaceAfter, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfter, "$this$replaceAfter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, index + 1, $this$replaceAfter.length(), (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfter$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceAfter(str, str2, str3, str4);
    }

    public static final String replaceAfter(String $this$replaceAfter, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfter, "$this$replaceAfter");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.indexOf$default((CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfter, delimiter.length() + index, $this$replaceAfter.length(), (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceAfterLast(str, str2, str3, str4);
    }

    public static final String replaceAfterLast(String $this$replaceAfterLast, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfterLast, "$this$replaceAfterLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, delimiter.length() + index, $this$replaceAfterLast.length(), (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceAfterLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceAfterLast(str, c, str2, str3);
    }

    public static final String replaceAfterLast(String $this$replaceAfterLast, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceAfterLast, "$this$replaceAfterLast");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return StringsKt.replaceRange((CharSequence) $this$replaceAfterLast, index + 1, $this$replaceAfterLast.length(), (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, char c, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return StringsKt.replaceBeforeLast(str, c, str2, str3);
    }

    public static final String replaceBeforeLast(String $this$replaceBeforeLast, char delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBeforeLast, "$this$replaceBeforeLast");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, index, (CharSequence) replacement).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String str, String str2, String str3, String str4, int i, Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return StringsKt.replaceBeforeLast(str, str2, str3, str4);
    }

    public static final String replaceBeforeLast(String $this$replaceBeforeLast, String delimiter, String replacement, String missingDelimiterValue) {
        Intrinsics.checkParameterIsNotNull($this$replaceBeforeLast, "$this$replaceBeforeLast");
        Intrinsics.checkParameterIsNotNull(delimiter, "delimiter");
        Intrinsics.checkParameterIsNotNull(replacement, "replacement");
        Intrinsics.checkParameterIsNotNull(missingDelimiterValue, "missingDelimiterValue");
        int index = StringsKt.lastIndexOf$default((CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (Object) null);
        return index == -1 ? missingDelimiterValue : StringsKt.replaceRange((CharSequence) $this$replaceBeforeLast, 0, index, (CharSequence) replacement).toString();
    }

    private static final String replace(CharSequence $this$replace, Regex regex, String replacement) {
        return regex.replace($this$replace, replacement);
    }

    private static final String replace(CharSequence $this$replace, Regex regex, Function1<? super MatchResult, ? extends CharSequence> function1) {
        return regex.replace($this$replace, function1);
    }

    private static final String replaceFirst(CharSequence $this$replaceFirst, Regex regex, String replacement) {
        return regex.replaceFirst($this$replaceFirst, replacement);
    }

    private static final boolean matches(CharSequence $this$matches, Regex regex) {
        return regex.matches($this$matches);
    }

    public static final boolean regionMatchesImpl(CharSequence $this$regionMatchesImpl, int thisOffset, CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$regionMatchesImpl, "$this$regionMatchesImpl");
        Intrinsics.checkParameterIsNotNull(other, "other");
        if (otherOffset < 0 || thisOffset < 0 || thisOffset > $this$regionMatchesImpl.length() - length || otherOffset > other.length() - length) {
            return false;
        }
        for (int index = 0; index < length; index++) {
            if (!CharsKt.equals($this$regionMatchesImpl.charAt(thisOffset + index), other.charAt(otherOffset + index), ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, c, z);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        return $this$startsWith.length() > 0 && CharsKt.equals($this$startsWith.charAt(0), c, ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, c, z);
    }

    public static final boolean endsWith(CharSequence $this$endsWith, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        return $this$endsWith.length() > 0 && CharsKt.equals($this$endsWith.charAt(StringsKt.getLastIndex($this$endsWith)), c, ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, z);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, CharSequence prefix, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (ignoreCase || !($this$startsWith instanceof String) || !(prefix instanceof String)) {
            return StringsKt.regionMatchesImpl($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
        }
        return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, false, 2, (Object) null);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence charSequence, CharSequence charSequence2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.startsWith(charSequence, charSequence2, i, z);
    }

    public static final boolean startsWith(CharSequence $this$startsWith, CharSequence prefix, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$startsWith, "$this$startsWith");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        if (ignoreCase || !($this$startsWith instanceof String) || !(prefix instanceof String)) {
            return StringsKt.regionMatchesImpl($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
        }
        return StringsKt.startsWith$default((String) $this$startsWith, (String) prefix, startIndex, false, 4, (Object) null);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.endsWith(charSequence, charSequence2, z);
    }

    public static final boolean endsWith(CharSequence $this$endsWith, CharSequence suffix, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$endsWith, "$this$endsWith");
        Intrinsics.checkParameterIsNotNull(suffix, "suffix");
        if (ignoreCase || !($this$endsWith instanceof String) || !(suffix instanceof String)) {
            return StringsKt.regionMatchesImpl($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), ignoreCase);
        }
        return StringsKt.endsWith$default((String) $this$endsWith, (String) suffix, false, 2, (Object) null);
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.commonPrefixWith(charSequence, charSequence2, z);
    }

    public static final String commonPrefixWith(CharSequence $this$commonPrefixWith, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$commonPrefixWith, "$this$commonPrefixWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int shortestLength = Math.min($this$commonPrefixWith.length(), other.length());
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonPrefixWith.charAt(i), other.charAt(i), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonPrefixWith, i - 1) || StringsKt.hasSurrogatePairAt(other, i - 1)) {
            i--;
        }
        return $this$commonPrefixWith.subSequence(0, i).toString();
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.commonSuffixWith(charSequence, charSequence2, z);
    }

    public static final String commonSuffixWith(CharSequence $this$commonSuffixWith, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$commonSuffixWith, "$this$commonSuffixWith");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int thisLength = $this$commonSuffixWith.length();
        int otherLength = other.length();
        int shortestLength = Math.min(thisLength, otherLength);
        int i = 0;
        while (i < shortestLength && CharsKt.equals($this$commonSuffixWith.charAt((thisLength - i) - 1), other.charAt((otherLength - i) - 1), ignoreCase)) {
            i++;
        }
        if (StringsKt.hasSurrogatePairAt($this$commonSuffixWith, (thisLength - i) - 1) || StringsKt.hasSurrogatePairAt(other, (otherLength - i) - 1)) {
            i--;
        }
        return $this$commonSuffixWith.subSequence(thisLength - i, thisLength).toString();
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, cArr, i, z);
    }

    public static final int indexOfAny(CharSequence $this$indexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        char[] $this$any$iv;
        Intrinsics.checkParameterIsNotNull($this$indexOfAny, "$this$indexOfAny");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$indexOfAny instanceof String)) {
            int index = RangesKt.coerceAtLeast(startIndex, 0);
            int lastIndex = StringsKt.getLastIndex($this$indexOfAny);
            if (index > lastIndex) {
                return -1;
            }
            while (true) {
                char charAtIndex = $this$indexOfAny.charAt(index);
                int length = chars.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        $this$any$iv = null;
                        break;
                    } else if (CharsKt.equals(chars[i], charAtIndex, ignoreCase)) {
                        $this$any$iv = 1;
                        break;
                    } else {
                        i++;
                    }
                }
                if ($this$any$iv != null) {
                    return index;
                }
                if (index == lastIndex) {
                    return -1;
                }
                index++;
            }
        } else {
            return ((String) $this$indexOfAny).indexOf(ArraysKt.single(chars), startIndex);
        }
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, cArr, i, z);
    }

    public static final int lastIndexOfAny(CharSequence $this$lastIndexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOfAny, "$this$lastIndexOfAny");
        Intrinsics.checkParameterIsNotNull(chars, "chars");
        if (ignoreCase || chars.length != 1 || !($this$lastIndexOfAny instanceof String)) {
            for (int index = RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$lastIndexOfAny)); index >= 0; index--) {
                char charAtIndex = $this$lastIndexOfAny.charAt(index);
                int length = chars.length;
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (CharsKt.equals(chars[i], charAtIndex, ignoreCase)) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    return index;
                }
            }
            return -1;
        }
        return ((String) $this$lastIndexOfAny).lastIndexOf(ArraysKt.single(chars), startIndex);
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2, int i3, Object obj) {
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, i, i2, z, (i3 & 16) != 0 ? false : z2);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence $this$indexOf, CharSequence other, int startIndex, int endIndex, boolean ignoreCase, boolean last) {
        IntRange indices;
        if (!last) {
            indices = new IntRange(RangesKt.coerceAtLeast(startIndex, 0), RangesKt.coerceAtMost(endIndex, $this$indexOf.length()));
        } else {
            indices = RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$indexOf)), RangesKt.coerceAtLeast(endIndex, 0));
        }
        if (!($this$indexOf instanceof String) || !(other instanceof String)) {
            int index = indices.getFirst();
            int last2 = indices.getLast();
            int step = indices.getStep();
            if (step >= 0) {
                if (index > last2) {
                    return -1;
                }
            } else if (index < last2) {
                return -1;
            }
            while (!StringsKt.regionMatchesImpl(other, 0, $this$indexOf, index, other.length(), ignoreCase)) {
                if (index == last2) {
                    return -1;
                }
                index += step;
            }
            return index;
        }
        int index2 = indices.getFirst();
        int last3 = indices.getLast();
        int step2 = indices.getStep();
        if (step2 >= 0) {
            if (index2 > last3) {
                return -1;
            }
        } else if (index2 < last3) {
            return -1;
        }
        while (!StringsKt.regionMatches((String) other, 0, (String) $this$indexOf, index2, other.length(), ignoreCase)) {
            if (index2 == last3) {
                return -1;
            }
            index2 += step2;
        }
        return index2;
    }

    public static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence $this$findAnyOf, Collection<String> $this$firstOrNull$iv, int startIndex, boolean ignoreCase, boolean last) {
        Object element$iv;
        Object element$iv2;
        if (ignoreCase || $this$firstOrNull$iv.size() != 1) {
            IntRange indices = !last ? new IntRange(RangesKt.coerceAtLeast(startIndex, 0), $this$findAnyOf.length()) : RangesKt.downTo(RangesKt.coerceAtMost(startIndex, StringsKt.getLastIndex($this$findAnyOf)), 0);
            if ($this$findAnyOf instanceof String) {
                int first = indices.getFirst();
                int last2 = indices.getLast();
                int step = indices.getStep();
                if (step < 0 ? first >= last2 : first <= last2) {
                    int index = first;
                    while (true) {
                        Iterator<T> it = $this$firstOrNull$iv.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                element$iv2 = null;
                                break;
                            }
                            element$iv2 = it.next();
                            String it2 = (String) element$iv2;
                            if (StringsKt.regionMatches(it2, 0, (String) $this$findAnyOf, index, it2.length(), ignoreCase)) {
                                break;
                            }
                        }
                        String matchingString = (String) element$iv2;
                        if (matchingString == null) {
                            if (index == last2) {
                                break;
                            }
                            index += step;
                        } else {
                            return TuplesKt.to(Integer.valueOf(index), matchingString);
                        }
                    }
                }
            } else {
                int first2 = indices.getFirst();
                int last3 = indices.getLast();
                int step2 = indices.getStep();
                if (step2 < 0 ? first2 >= last3 : first2 <= last3) {
                    int index2 = first2;
                    while (true) {
                        Iterator<T> it3 = $this$firstOrNull$iv.iterator();
                        while (true) {
                            if (!it3.hasNext()) {
                                element$iv = null;
                                break;
                            }
                            element$iv = it3.next();
                            String it4 = (String) element$iv;
                            if (StringsKt.regionMatchesImpl(it4, 0, $this$findAnyOf, index2, it4.length(), ignoreCase)) {
                                break;
                            }
                        }
                        String matchingString2 = (String) element$iv;
                        if (matchingString2 == null) {
                            if (index2 == last3) {
                                break;
                            }
                            index2 += step2;
                        } else {
                            return TuplesKt.to(Integer.valueOf(index2), matchingString2);
                        }
                    }
                }
            }
            return null;
        }
        String string = (String) CollectionsKt.single($this$firstOrNull$iv);
        int index3 = !last ? StringsKt.indexOf$default($this$findAnyOf, string, startIndex, false, 4, (Object) null) : StringsKt.lastIndexOf$default($this$findAnyOf, string, startIndex, false, 4, (Object) null);
        if (index3 < 0) {
            return null;
        }
        return TuplesKt.to(Integer.valueOf(index3), string);
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findAnyOf(charSequence, collection, i, z);
    }

    public static final Pair<Integer, String> findAnyOf(CharSequence $this$findAnyOf, Collection<String> collection, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$findAnyOf, "$this$findAnyOf");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findAnyOf, collection, startIndex, ignoreCase, false);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.findLastAnyOf(charSequence, collection, i, z);
    }

    public static final Pair<Integer, String> findLastAnyOf(CharSequence $this$findLastAnyOf, Collection<String> collection, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$findLastAnyOf, "$this$findLastAnyOf");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findLastAnyOf, collection, startIndex, ignoreCase, true);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOfAny(charSequence, collection, i, z);
    }

    public static final int indexOfAny(CharSequence $this$indexOfAny, Collection<String> collection, int startIndex, boolean ignoreCase) {
        Integer first;
        Intrinsics.checkParameterIsNotNull($this$indexOfAny, "$this$indexOfAny");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$indexOfAny, collection, startIndex, ignoreCase, false);
        if (findAnyOf$StringsKt__StringsKt == null || (first = findAnyOf$StringsKt__StringsKt.getFirst()) == null) {
            return -1;
        }
        return first.intValue();
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence charSequence, Collection collection, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOfAny(charSequence, collection, i, z);
    }

    public static final int lastIndexOfAny(CharSequence $this$lastIndexOfAny, Collection<String> collection, int startIndex, boolean ignoreCase) {
        Integer first;
        Intrinsics.checkParameterIsNotNull($this$lastIndexOfAny, "$this$lastIndexOfAny");
        Intrinsics.checkParameterIsNotNull(collection, "strings");
        Pair<Integer, String> findAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$lastIndexOfAny, collection, startIndex, ignoreCase, true);
        if (findAnyOf$StringsKt__StringsKt == null || (first = findAnyOf$StringsKt__StringsKt.getFirst()) == null) {
            return -1;
        }
        return first.intValue();
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, c, i, z);
    }

    public static final int indexOf(CharSequence $this$indexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$indexOf, "$this$indexOf");
        if (ignoreCase || !($this$indexOf instanceof String)) {
            return StringsKt.indexOfAny($this$indexOf, new char[]{c}, startIndex, ignoreCase);
        }
        return ((String) $this$indexOf).indexOf(c, startIndex);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.indexOf(charSequence, str, i, z);
    }

    public static final int indexOf(CharSequence $this$indexOf, String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$indexOf, "$this$indexOf");
        Intrinsics.checkParameterIsNotNull(string, "string");
        if (ignoreCase || !($this$indexOf instanceof String)) {
            return indexOf$StringsKt__StringsKt$default($this$indexOf, string, startIndex, $this$indexOf.length(), ignoreCase, false, 16, null);
        }
        return ((String) $this$indexOf).indexOf(string, startIndex);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, char c, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, c, i, z);
    }

    public static final int lastIndexOf(CharSequence $this$lastIndexOf, char c, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOf, "$this$lastIndexOf");
        if (ignoreCase || !($this$lastIndexOf instanceof String)) {
            return StringsKt.lastIndexOfAny($this$lastIndexOf, new char[]{c}, startIndex, ignoreCase);
        }
        return ((String) $this$lastIndexOf).lastIndexOf(c, startIndex);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            i = StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return StringsKt.lastIndexOf(charSequence, str, i, z);
    }

    public static final int lastIndexOf(CharSequence $this$lastIndexOf, String string, int startIndex, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOf, "$this$lastIndexOf");
        Intrinsics.checkParameterIsNotNull(string, "string");
        if (ignoreCase || !($this$lastIndexOf instanceof String)) {
            return indexOf$StringsKt__StringsKt($this$lastIndexOf, string, startIndex, 0, ignoreCase, true);
        }
        return ((String) $this$lastIndexOf).lastIndexOf(string, startIndex);
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, CharSequence charSequence2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, charSequence2, z);
    }

    public static final boolean contains(CharSequence $this$contains, CharSequence other, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        Intrinsics.checkParameterIsNotNull(other, "other");
        return other instanceof String ? StringsKt.indexOf$default($this$contains, (String) other, 0, ignoreCase, 2, (Object) null) >= 0 : indexOf$StringsKt__StringsKt$default($this$contains, other, 0, $this$contains.length(), ignoreCase, false, 16, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(CharSequence charSequence, char c, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return StringsKt.contains(charSequence, c, z);
    }

    public static final boolean contains(CharSequence $this$contains, char c, boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return StringsKt.indexOf$default($this$contains, c, 0, ignoreCase, 2, (Object) null) >= 0;
    }

    private static final boolean contains(CharSequence $this$contains, Regex regex) {
        Intrinsics.checkParameterIsNotNull($this$contains, "$this$contains");
        return regex.containsMatchIn($this$contains);
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, char[] cArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArr, i, z, i2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, char[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        if (limit >= 0) {
            return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(delimiters, ignoreCase) { // from class: kotlin.text.StringsKt__StringsKt$rangesDelimitedBy$2
                final /* synthetic */ char[] $delimiters;
                final /* synthetic */ boolean $ignoreCase;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.$delimiters = r1;
                    this.$ignoreCase = r2;
                }

                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Pair<? extends Integer, ? extends Integer> invoke(CharSequence charSequence, Integer num) {
                    return invoke(charSequence, num.intValue());
                }

                public final Pair<Integer, Integer> invoke(CharSequence $receiver, int currentIndex) {
                    Intrinsics.checkParameterIsNotNull($receiver, "$receiver");
                    int it = StringsKt.indexOfAny($receiver, this.$delimiters, currentIndex, this.$ignoreCase);
                    if (it < 0) {
                        return null;
                    }
                    return TuplesKt.to(Integer.valueOf(it), 1);
                }
            });
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + FilenameUtils.EXTENSION_SEPARATOR).toString());
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] strArr, int i, boolean z, int i2, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, strArr, i, z, i2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence $this$rangesDelimitedBy, String[] delimiters, int startIndex, boolean ignoreCase, int limit) {
        if (limit >= 0) {
            return new DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new Function2<CharSequence, Integer, Pair<? extends Integer, ? extends Integer>>(ArraysKt.asList(delimiters), ignoreCase) { // from class: kotlin.text.StringsKt__StringsKt$rangesDelimitedBy$4
                final /* synthetic */ List $delimitersList;
                final /* synthetic */ boolean $ignoreCase;

                /* JADX INFO: Access modifiers changed from: package-private */
                {
                    this.$delimitersList = r1;
                    this.$ignoreCase = r2;
                }

                @Override // kotlin.jvm.functions.Function2
                public /* bridge */ /* synthetic */ Pair<? extends Integer, ? extends Integer> invoke(CharSequence charSequence, Integer num) {
                    return invoke(charSequence, num.intValue());
                }

                public final Pair<Integer, Integer> invoke(CharSequence $receiver, int currentIndex) {
                    Intrinsics.checkParameterIsNotNull($receiver, "$receiver");
                    Pair it = StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($receiver, this.$delimitersList, currentIndex, this.$ignoreCase, false);
                    if (it != null) {
                        return TuplesKt.to(it.getFirst(), Integer.valueOf(((String) it.getSecond()).length()));
                    }
                    return null;
                }
            });
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + FilenameUtils.EXTENSION_SEPARATOR).toString());
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, strArr, z, i);
    }

    public static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$splitToSequence, "$this$splitToSequence");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new Function1<IntRange, String>($this$splitToSequence) { // from class: kotlin.text.StringsKt__StringsKt$splitToSequence$1
            final /* synthetic */ CharSequence $this_splitToSequence;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_splitToSequence = r1;
            }

            public final String invoke(IntRange it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return StringsKt.substring(this.$this_splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, String[] strArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, strArr, z, i);
    }

    public static final List<String> split(CharSequence $this$split, String[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$split, "$this$split");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        if (delimiters.length == 1) {
            boolean z = false;
            String delimiter = delimiters[0];
            if (delimiter.length() == 0) {
                z = true;
            }
            if (!z) {
                return split$StringsKt__StringsKt($this$split, delimiter, ignoreCase, limit);
            }
        }
        Iterable<IntRange> $this$map$iv = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (IntRange it : $this$map$iv) {
            destination$iv$iv.add(StringsKt.substring($this$split, it));
        }
        return (List) destination$iv$iv;
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.splitToSequence(charSequence, cArr, z, i);
    }

    public static final Sequence<String> splitToSequence(CharSequence $this$splitToSequence, char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$splitToSequence, "$this$splitToSequence");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (Object) null), new Function1<IntRange, String>($this$splitToSequence) { // from class: kotlin.text.StringsKt__StringsKt$splitToSequence$2
            final /* synthetic */ CharSequence $this_splitToSequence;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_splitToSequence = r1;
            }

            public final String invoke(IntRange it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                return StringsKt.substring(this.$this_splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ List split$default(CharSequence charSequence, char[] cArr, boolean z, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return StringsKt.split(charSequence, cArr, z, i);
    }

    public static final List<String> split(CharSequence $this$split, char[] delimiters, boolean ignoreCase, int limit) {
        Intrinsics.checkParameterIsNotNull($this$split, "$this$split");
        Intrinsics.checkParameterIsNotNull(delimiters, "delimiters");
        if (delimiters.length == 1) {
            return split$StringsKt__StringsKt($this$split, String.valueOf(delimiters[0]), ignoreCase, limit);
        }
        Iterable<IntRange> $this$map$iv = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (Object) null));
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (IntRange it : $this$map$iv) {
            destination$iv$iv.add(StringsKt.substring($this$split, it));
        }
        return (List) destination$iv$iv;
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence $this$split, String delimiter, boolean ignoreCase, int limit) {
        boolean isLimited = false;
        if (limit >= 0) {
            int currentOffset = 0;
            int nextIndex = StringsKt.indexOf($this$split, delimiter, 0, ignoreCase);
            if (nextIndex == -1 || limit == 1) {
                return CollectionsKt.listOf($this$split.toString());
            }
            if (limit > 0) {
                isLimited = true;
            }
            int i = 10;
            if (isLimited) {
                i = RangesKt.coerceAtMost(limit, 10);
            }
            ArrayList result = new ArrayList(i);
            do {
                result.add($this$split.subSequence(currentOffset, nextIndex).toString());
                currentOffset = nextIndex + delimiter.length();
                if (isLimited && result.size() == limit - 1) {
                    break;
                }
                nextIndex = StringsKt.indexOf($this$split, delimiter, currentOffset, ignoreCase);
            } while (nextIndex != -1);
            result.add($this$split.subSequence(currentOffset, $this$split.length()).toString());
            return result;
        }
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + limit + FilenameUtils.EXTENSION_SEPARATOR).toString());
    }

    private static final List<String> split(CharSequence $this$split, Regex regex, int limit) {
        return regex.split($this$split, limit);
    }

    static /* synthetic */ List split$default(CharSequence $this$split, Regex regex, int limit, int i, Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        return regex.split($this$split, limit);
    }

    public static final Sequence<String> lineSequence(CharSequence $this$lineSequence) {
        Intrinsics.checkParameterIsNotNull($this$lineSequence, "$this$lineSequence");
        return StringsKt.splitToSequence$default($this$lineSequence, new String[]{"\r\n", IOUtils.LINE_SEPARATOR_UNIX, "\r"}, false, 0, 6, (Object) null);
    }

    public static final List<String> lines(CharSequence $this$lines) {
        Intrinsics.checkParameterIsNotNull($this$lines, "$this$lines");
        return SequencesKt.toList(StringsKt.lineSequence($this$lines));
    }
}
