package kotlin.text;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.collections.Grouping;
import kotlin.collections.IndexedValue;
import kotlin.collections.IndexingIterable;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.collections.SlidingWindowKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.apache.commons.codec.language.bm.Languages;
/* compiled from: _Strings.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Ü\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\b\b\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u001f\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010\u000f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0010\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\b*\u00020\u0002\u001a\u0010\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00050\n*\u00020\u0002\u001aE\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b\u001a3\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u00020\u00050\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aM\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aN\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u00020\u00050\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001ah\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a`\u0010\u001a\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0018\b\u0002\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u001e\u0010\u000f\u001a\u001a\u0012\u0004\u0012\u00020\u0005\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e0\u00100\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001a3\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b\u001aN\u0010\u001d\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\u000e\"\u0018\b\u0001\u0010\u0015*\u0012\u0012\u0006\b\u0000\u0012\u00020\u0005\u0012\u0006\b\u0000\u0012\u0002H\u000e0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0087\b¢\u0006\u0002\u0010\u0018\u001a\u001a\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010\u001e\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u001a\u0010$\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"H\u0007\u001a4\u0010$\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\r\u0010%\u001a\u00020\"*\u00020\u0002H\u0087\b\u001a!\u0010%\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010&\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010&\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0012\u0010(\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a!\u0010)\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010)\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010*\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a)\u0010+\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u001c\u0010.\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"H\u0087\b¢\u0006\u0002\u0010/\u001a!\u00100\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u00100\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a6\u00101\u001a\u00020\u0002*\u00020\u00022'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b\u001a6\u00101\u001a\u00020 *\u00020 2'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b\u001aQ\u00105\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u0003\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000102H\u0086\b¢\u0006\u0002\u00109\u001a!\u0010:\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010:\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a<\u0010;\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010<\u001a<\u0010=\u001a\u0002H6\"\f\b\u0000\u00106*\u000607j\u0002`8*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010<\u001a(\u0010>\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010?\u001a(\u0010@\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0087\b¢\u0006\u0002\u0010?\u001a\n\u0010A\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010A\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a(\u0010B\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010?\u001a3\u0010D\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b\u001aL\u0010E\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0018\u0010\u000f\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H#0\b0\u0004H\u0086\b¢\u0006\u0002\u0010G\u001aI\u0010H\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b¢\u0006\u0002\u0010L\u001a^\u0010M\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0NH\u0086\b¢\u0006\u0002\u0010O\u001aI\u0010P\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#02H\u0086\b¢\u0006\u0002\u0010L\u001a^\u0010Q\u001a\u0002H#\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u0002H#0NH\u0086\b¢\u0006\u0002\u0010O\u001a!\u0010R\u001a\u00020S*\u00020\u00022\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\u0086\b\u001a6\u0010U\u001a\u00020S*\u00020\u00022'\u0010T\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S02H\u0086\b\u001a)\u0010V\u001a\u00020\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"2\u0012\u0010-\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00050\u0004H\u0087\b\u001a\u0019\u0010W\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010,\u001a\u00020\"¢\u0006\u0002\u0010/\u001a9\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u001f0\f\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b\u001aS\u0010X\u001a\u0014\u0012\u0004\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0\u001f0\f\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e*\u00020\u00022\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b\u001aR\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u001c\b\u0001\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0086\b¢\u0006\u0002\u0010\u0018\u001al\u0010Y\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u001c\b\u0002\u0010\u0015*\u0016\u0012\u0006\b\u0000\u0012\u0002H\r\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0Z0\u0016*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H\u00152\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u00042\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\u000e0\u0004H\u0086\b¢\u0006\u0002\u0010\u0019\u001a5\u0010[\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\\\"\u0004\b\u0000\u0010\r*\u00020\u00022\u0014\b\u0004\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H\r0\u0004H\u0087\b\u001a!\u0010]\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010^\u001a\u00020\"*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\n\u0010_\u001a\u00020\u0005*\u00020\u0002\u001a!\u0010_\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0011\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a(\u0010`\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010?\u001a-\u0010a\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b\u001aB\u0010b\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b\u001aH\u0010c\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\u0086\b\u001aa\u0010e\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62)\u0010\u000f\u001a%\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#02H\u0086\b¢\u0006\u0002\u0010f\u001a[\u0010g\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62'\u0010\u000f\u001a#\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0086\b¢\u0006\u0002\u0010f\u001a3\u0010h\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\b\b\u0000\u0010#*\u00020d*\u00020\u00022\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b\u001aL\u0010i\u001a\u0002H6\"\b\b\u0000\u0010#*\u00020d\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0014\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u0001H#0\u0004H\u0086\b¢\u0006\u0002\u0010G\u001aF\u0010j\u001a\u0002H6\"\u0004\b\u0000\u0010#\"\u0010\b\u0001\u00106*\n\u0012\u0006\b\u0000\u0012\u0002H#0F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H62\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010G\u001a\u0011\u0010k\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a8\u0010l\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010?\u001a-\u0010o\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r¢\u0006\u0002\u0010s\u001a\u0011\u0010t\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a8\u0010u\u001a\u0004\u0018\u00010\u0005\"\u000e\b\u0000\u0010#*\b\u0012\u0004\u0012\u0002H#0m*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0\u0004H\u0086\b¢\u0006\u0002\u0010?\u001a-\u0010v\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u001a\u0010p\u001a\u0016\u0012\u0006\b\u0000\u0012\u00020\u00050qj\n\u0012\u0006\b\u0000\u0012\u00020\u0005`r¢\u0006\u0002\u0010s\u001a\n\u0010w\u001a\u00020\u0001*\u00020\u0002\u001a!\u0010w\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a0\u0010x\u001a\u0002Hy\"\b\b\u0000\u0010y*\u00020\u0002*\u0002Hy2\u0012\u0010T\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020S0\u0004H\u0087\b¢\u0006\u0002\u0010z\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u0010*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a-\u0010{\u001a\u000e\u0012\u0004\u0012\u00020 \u0012\u0004\u0012\u00020 0\u0010*\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\r\u0010|\u001a\u00020\u0005*\u00020\u0002H\u0087\b\u001a\u0014\u0010|\u001a\u00020\u0005*\u00020\u00022\u0006\u0010|\u001a\u00020}H\u0007\u001a\u0014\u0010~\u001a\u0004\u0018\u00010\u0005*\u00020\u0002H\u0087\b¢\u0006\u0002\u0010C\u001a\u001b\u0010~\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0006\u0010|\u001a\u00020}H\u0007¢\u0006\u0002\u0010\u007f\u001a7\u0010\u0080\u0001\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000502H\u0086\b\u001aL\u0010\u0081\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050NH\u0086\b\u001a?\u0010\u0082\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000502H\u0087\b¢\u0006\u0003\u0010\u0083\u0001\u001a7\u0010\u0084\u0001\u001a\u00020\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u000502H\u0086\b\u001aL\u0010\u0085\u0001\u001a\u00020\u0005*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u00050NH\u0086\b\u001a?\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022'\u0010J\u001a#\u0012\u0004\u0012\u00020\u0005\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u000502H\u0087\b¢\u0006\u0003\u0010\u0083\u0001\u001a\u000b\u0010\u0087\u0001\u001a\u00020\u0002*\u00020\u0002\u001a\u000e\u0010\u0087\u0001\u001a\u00020 *\u00020 H\u0087\b\u001aQ\u0010\u0088\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2'\u0010J\u001a#\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#02H\u0087\b¢\u0006\u0003\u0010\u0089\u0001\u001af\u0010\u008a\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010I\u001a\u0002H#2<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u0011H#¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u0002H#0NH\u0087\b¢\u0006\u0003\u0010\u008b\u0001\u001a=\u0010\u008c\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u00022'\u0010J\u001a#\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u000502H\u0087\b\u001aR\u0010\u008d\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u00022<\u0010J\u001a8\u0012\u0013\u0012\u00110\"¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0005¢\u0006\f\b3\u0012\b\b4\u0012\u0004\b\b(K\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050NH\u0087\b\u001a\u000b\u0010\u008e\u0001\u001a\u00020\u0005*\u00020\u0002\u001a\"\u0010\u008e\u0001\u001a\u00020\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\u0012\u0010\u008f\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u0002¢\u0006\u0002\u0010C\u001a)\u0010\u008f\u0001\u001a\u0004\u0018\u00010\u0005*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¢\u0006\u0002\u0010?\u001a\u001a\u0010\u0090\u0001\u001a\u00020\u0002*\u00020\u00022\r\u0010\u0091\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\b\u001a\u0015\u0010\u0090\u0001\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0091\u0001\u001a\u00030\u0092\u0001\u001a\u001d\u0010\u0090\u0001\u001a\u00020 *\u00020 2\r\u0010\u0091\u0001\u001a\b\u0012\u0004\u0012\u00020\"0\bH\u0087\b\u001a\u0015\u0010\u0090\u0001\u001a\u00020 *\u00020 2\b\u0010\u0091\u0001\u001a\u00030\u0092\u0001\u001a\"\u0010\u0093\u0001\u001a\u00020\"*\u00020\u00022\u0012\u0010n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004H\u0086\b\u001a$\u0010\u0094\u0001\u001a\u00030\u0095\u0001*\u00020\u00022\u0013\u0010n\u001a\u000f\u0012\u0004\u0012\u00020\u0005\u0012\u0005\u0012\u00030\u0095\u00010\u0004H\u0086\b\u001a\u0013\u0010\u0096\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0096\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0097\u0001\u001a\u00020\u0002*\u00020\u00022\u0006\u0010'\u001a\u00020\"\u001a\u0013\u0010\u0097\u0001\u001a\u00020 *\u00020 2\u0006\u0010'\u001a\u00020\"\u001a\"\u0010\u0098\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u0098\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u0099\u0001\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a\"\u0010\u0099\u0001\u001a\u00020 *\u00020 2\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a+\u0010\u009a\u0001\u001a\u0002H6\"\u0010\b\u0000\u00106*\n\u0012\u0006\b\u0000\u0012\u00020\u00050F*\u00020\u00022\u0006\u0010\u0017\u001a\u0002H6¢\u0006\u0003\u0010\u009b\u0001\u001a\u001d\u0010\u009c\u0001\u001a\u0014\u0012\u0004\u0012\u00020\u00050\u009d\u0001j\t\u0012\u0004\u0012\u00020\u0005`\u009e\u0001*\u00020\u0002\u001a\u0011\u0010\u009f\u0001\u001a\b\u0012\u0004\u0012\u00020\u00050\u001f*\u00020\u0002\u001a\u0011\u0010 \u0001\u001a\b\u0012\u0004\u0012\u00020\u00050Z*\u00020\u0002\u001a\u0012\u0010¡\u0001\u001a\t\u0012\u0004\u0012\u00020\u00050¢\u0001*\u00020\u0002\u001a1\u0010£\u0001\u001a\b\u0012\u0004\u0012\u00020 0\u001f*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010¤\u0001\u001a\u00020\"2\t\b\u0002\u0010¥\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010£\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010¤\u0001\u001a\u00020\"2\t\b\u0002\u0010¥\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a1\u0010¦\u0001\u001a\b\u0012\u0004\u0012\u00020 0\n*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010¤\u0001\u001a\u00020\"2\t\b\u0002\u0010¥\u0001\u001a\u00020\u0001H\u0007\u001aK\u0010¦\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\n\"\u0004\b\u0000\u0010#*\u00020\u00022\u0006\u0010!\u001a\u00020\"2\t\b\u0002\u0010¤\u0001\u001a\u00020\"2\t\b\u0002\u0010¥\u0001\u001a\u00020\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002H#0\u0004H\u0007\u001a\u0018\u0010§\u0001\u001a\u000f\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00050¨\u00010\b*\u00020\u0002\u001a)\u0010©\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u00022\u0007\u0010ª\u0001\u001a\u00020\u0002H\u0086\u0004\u001a]\u0010©\u0001\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u001f\"\u0004\b\u0000\u0010\u000e*\u00020\u00022\u0007\u0010ª\u0001\u001a\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(«\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(¬\u0001\u0012\u0004\u0012\u0002H\u000e02H\u0086\b\u001a\u001f\u0010\u00ad\u0001\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00100\u001f*\u00020\u0002H\u0007\u001aT\u0010\u00ad\u0001\u001a\b\u0012\u0004\u0012\u0002H#0\u001f\"\u0004\b\u0000\u0010#*\u00020\u000228\u0010\u000f\u001a4\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(«\u0001\u0012\u0014\u0012\u00120\u0005¢\u0006\r\b3\u0012\t\b4\u0012\u0005\b\b(¬\u0001\u0012\u0004\u0012\u0002H#02H\u0087\b¨\u0006®\u0001"}, d2 = {"all", "", "", "predicate", "Lkotlin/Function1;", "", Languages.ANY, "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", "associate", "", "K", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "transform", "Lkotlin/Pair;", "associateBy", "keySelector", "valueTransform", "associateByTo", "M", "", FirebaseAnalytics.Param.DESTINATION, "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "(Ljava/lang/CharSequence;Ljava/util/Map;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "associateTo", "associateWith", "valueSelector", "associateWithTo", "chunked", "", "", "size", "", "R", "chunkedSequence", "count", "drop", "n", "dropLast", "dropLastWhile", "dropWhile", "elementAtOrElse", FirebaseAnalytics.Param.INDEX, "defaultValue", "elementAtOrNull", "(Ljava/lang/CharSequence;I)Ljava/lang/Character;", "filter", "filterIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "filterIndexedTo", "C", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function2;)Ljava/lang/Appendable;", "filterNot", "filterNotTo", "(Ljava/lang/CharSequence;Ljava/lang/Appendable;Lkotlin/jvm/functions/Function1;)Ljava/lang/Appendable;", "filterTo", "find", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/Character;", "findLast", "first", "firstOrNull", "(Ljava/lang/CharSequence;)Ljava/lang/Character;", "flatMap", "flatMapTo", "", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "fold", "initial", "operation", "acc", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "foldIndexed", "Lkotlin/Function3;", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "foldRight", "foldRightIndexed", "forEach", "", "action", "forEachIndexed", "getOrElse", "getOrNull", "groupBy", "groupByTo", "", "groupingBy", "Lkotlin/collections/Grouping;", "indexOfFirst", "indexOfLast", "last", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "", "mapIndexedNotNullTo", "(Ljava/lang/CharSequence;Ljava/util/Collection;Lkotlin/jvm/functions/Function2;)Ljava/util/Collection;", "mapIndexedTo", "mapNotNull", "mapNotNullTo", "mapTo", "max", "maxBy", "", "selector", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/lang/CharSequence;Ljava/util/Comparator;)Ljava/lang/Character;", "min", "minBy", "minWith", "none", "onEach", ExifInterface.LATITUDE_SOUTH, "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function1;)Ljava/lang/CharSequence;", "partition", "random", "Lkotlin/random/Random;", "randomOrNull", "(Ljava/lang/CharSequence;Lkotlin/random/Random;)Ljava/lang/Character;", "reduce", "reduceIndexed", "reduceOrNull", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function2;)Ljava/lang/Character;", "reduceRight", "reduceRightIndexed", "reduceRightOrNull", "reversed", "scan", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/util/List;", "scanIndexed", "(Ljava/lang/CharSequence;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Ljava/util/List;", "scanReduce", "scanReduceIndexed", "single", "singleOrNull", "slice", "indices", "Lkotlin/ranges/IntRange;", "sumBy", "sumByDouble", "", "take", "takeLast", "takeLastWhile", "takeWhile", "toCollection", "(Ljava/lang/CharSequence;Ljava/util/Collection;)Ljava/util/Collection;", "toHashSet", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "toList", "toMutableList", "toSet", "", "windowed", "step", "partialWindows", "windowedSequence", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "zipWithNext", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: classes3.dex */
class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    private static final char elementAtOrElse(CharSequence $this$elementAtOrElse, int index, Function1<? super Integer, Character> function1) {
        return (index < 0 || index > StringsKt.getLastIndex($this$elementAtOrElse)) ? function1.invoke(Integer.valueOf(index)).charValue() : $this$elementAtOrElse.charAt(index);
    }

    private static final Character elementAtOrNull(CharSequence $this$elementAtOrNull, int index) {
        return StringsKt.getOrNull($this$elementAtOrNull, index);
    }

    private static final Character find(CharSequence $this$find, Function1<? super Character, Boolean> function1) {
        for (int i = 0; i < $this$find.length(); i++) {
            char element$iv = $this$find.charAt(i);
            if (function1.invoke(Character.valueOf(element$iv)).booleanValue()) {
                return Character.valueOf(element$iv);
            }
        }
        return null;
    }

    private static final Character findLast(CharSequence $this$findLast, Function1<? super Character, Boolean> function1) {
        char element$iv;
        int index$iv = $this$findLast.length();
        do {
            index$iv--;
            if (index$iv < 0) {
                return null;
            }
            element$iv = $this$findLast.charAt(index$iv);
        } while (!function1.invoke(Character.valueOf(element$iv)).booleanValue());
        return Character.valueOf(element$iv);
    }

    public static final char first(CharSequence $this$first) {
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        if (!($this$first.length() == 0)) {
            return $this$first.charAt(0);
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static final char first(CharSequence $this$first, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$first, "$this$first");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$first.length(); i++) {
            char element = $this$first.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                return element;
            }
        }
        throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
    }

    public static final Character firstOrNull(CharSequence $this$firstOrNull) {
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        if ($this$firstOrNull.length() == 0) {
            return null;
        }
        return Character.valueOf($this$firstOrNull.charAt(0));
    }

    public static final Character firstOrNull(CharSequence $this$firstOrNull, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$firstOrNull, "$this$firstOrNull");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$firstOrNull.length(); i++) {
            char element = $this$firstOrNull.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                return Character.valueOf(element);
            }
        }
        return null;
    }

    private static final char getOrElse(CharSequence $this$getOrElse, int index, Function1<? super Integer, Character> function1) {
        return (index < 0 || index > StringsKt.getLastIndex($this$getOrElse)) ? function1.invoke(Integer.valueOf(index)).charValue() : $this$getOrElse.charAt(index);
    }

    public static final Character getOrNull(CharSequence $this$getOrNull, int index) {
        Intrinsics.checkParameterIsNotNull($this$getOrNull, "$this$getOrNull");
        if (index < 0 || index > StringsKt.getLastIndex($this$getOrNull)) {
            return null;
        }
        return Character.valueOf($this$getOrNull.charAt(index));
    }

    public static final int indexOfFirst(CharSequence $this$indexOfFirst, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$indexOfFirst, "$this$indexOfFirst");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$indexOfFirst.length();
        for (int index = 0; index < length; index++) {
            if (function1.invoke(Character.valueOf($this$indexOfFirst.charAt(index))).booleanValue()) {
                return index;
            }
        }
        return -1;
    }

    public static final int indexOfLast(CharSequence $this$indexOfLast, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$indexOfLast, "$this$indexOfLast");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int index = $this$indexOfLast.length() - 1; index >= 0; index--) {
            if (function1.invoke(Character.valueOf($this$indexOfLast.charAt(index))).booleanValue()) {
                return index;
            }
        }
        return -1;
    }

    public static final char last(CharSequence $this$last) {
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        if (!($this$last.length() == 0)) {
            return $this$last.charAt(StringsKt.getLastIndex($this$last));
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    public static final char last(CharSequence $this$last, Function1<? super Character, Boolean> function1) {
        char element;
        Intrinsics.checkParameterIsNotNull($this$last, "$this$last");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int index = $this$last.length();
        do {
            index--;
            if (index >= 0) {
                element = $this$last.charAt(index);
            } else {
                throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
            }
        } while (!function1.invoke(Character.valueOf(element)).booleanValue());
        return element;
    }

    public static final Character lastOrNull(CharSequence $this$lastOrNull) {
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        if ($this$lastOrNull.length() == 0) {
            return null;
        }
        return Character.valueOf($this$lastOrNull.charAt($this$lastOrNull.length() - 1));
    }

    public static final Character lastOrNull(CharSequence $this$lastOrNull, Function1<? super Character, Boolean> function1) {
        char element;
        Intrinsics.checkParameterIsNotNull($this$lastOrNull, "$this$lastOrNull");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int index = $this$lastOrNull.length();
        do {
            index--;
            if (index < 0) {
                return null;
            }
            element = $this$lastOrNull.charAt(index);
        } while (!function1.invoke(Character.valueOf(element)).booleanValue());
        return Character.valueOf(element);
    }

    private static final char random(CharSequence $this$random) {
        return StringsKt.random($this$random, Random.Default);
    }

    public static final char random(CharSequence $this$random, Random random) {
        Intrinsics.checkParameterIsNotNull($this$random, "$this$random");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if (!($this$random.length() == 0)) {
            return $this$random.charAt(random.nextInt($this$random.length()));
        }
        throw new NoSuchElementException("Char sequence is empty.");
    }

    private static final Character randomOrNull(CharSequence $this$randomOrNull) {
        return StringsKt.randomOrNull($this$randomOrNull, Random.Default);
    }

    public static final Character randomOrNull(CharSequence $this$randomOrNull, Random random) {
        Intrinsics.checkParameterIsNotNull($this$randomOrNull, "$this$randomOrNull");
        Intrinsics.checkParameterIsNotNull(random, "random");
        if ($this$randomOrNull.length() == 0) {
            return null;
        }
        return Character.valueOf($this$randomOrNull.charAt(random.nextInt($this$randomOrNull.length())));
    }

    public static final char single(CharSequence $this$single) {
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        int length = $this$single.length();
        if (length == 0) {
            throw new NoSuchElementException("Char sequence is empty.");
        } else if (length == 1) {
            return $this$single.charAt(0);
        } else {
            throw new IllegalArgumentException("Char sequence has more than one element.");
        }
    }

    public static final char single(CharSequence $this$single, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$single, "$this$single");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Character single = null;
        boolean found = false;
        for (int i = 0; i < $this$single.length(); i++) {
            char element = $this$single.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                if (!found) {
                    single = Character.valueOf(element);
                    found = true;
                } else {
                    throw new IllegalArgumentException("Char sequence contains more than one matching element.");
                }
            }
        }
        if (!found) {
            throw new NoSuchElementException("Char sequence contains no character matching the predicate.");
        } else if (single != null) {
            return single.charValue();
        } else {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Char");
        }
    }

    public static final Character singleOrNull(CharSequence $this$singleOrNull) {
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        if ($this$singleOrNull.length() == 1) {
            return Character.valueOf($this$singleOrNull.charAt(0));
        }
        return null;
    }

    public static final Character singleOrNull(CharSequence $this$singleOrNull, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$singleOrNull, "$this$singleOrNull");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Character single = null;
        boolean found = false;
        for (int i = 0; i < $this$singleOrNull.length(); i++) {
            char element = $this$singleOrNull.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                if (found) {
                    return null;
                }
                single = Character.valueOf(element);
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        return single;
    }

    public static final CharSequence drop(CharSequence $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        if (n >= 0) {
            return $this$drop.subSequence(RangesKt.coerceAtMost(n, $this$drop.length()), $this$drop.length());
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final String drop(String $this$drop, int n) {
        Intrinsics.checkParameterIsNotNull($this$drop, "$this$drop");
        if (n >= 0) {
            String substring = $this$drop.substring(RangesKt.coerceAtMost(n, $this$drop.length()));
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final CharSequence dropLast(CharSequence $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        if (n >= 0) {
            return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final String dropLast(String $this$dropLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$dropLast, "$this$dropLast");
        if (n >= 0) {
            return StringsKt.take($this$dropLast, RangesKt.coerceAtLeast($this$dropLast.length() - n, 0));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final CharSequence dropLastWhile(CharSequence $this$dropLastWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int index = StringsKt.getLastIndex($this$dropLastWhile); index >= 0; index--) {
            if (!function1.invoke(Character.valueOf($this$dropLastWhile.charAt(index))).booleanValue()) {
                return $this$dropLastWhile.subSequence(0, index + 1);
            }
        }
        return "";
    }

    public static final String dropLastWhile(String $this$dropLastWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$dropLastWhile, "$this$dropLastWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int index = StringsKt.getLastIndex($this$dropLastWhile); index >= 0; index--) {
            if (!function1.invoke(Character.valueOf($this$dropLastWhile.charAt(index))).booleanValue()) {
                String substring = $this$dropLastWhile.substring(0, index + 1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return substring;
            }
        }
        return "";
    }

    public static final CharSequence dropWhile(CharSequence $this$dropWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$dropWhile.length();
        for (int index = 0; index < length; index++) {
            if (!function1.invoke(Character.valueOf($this$dropWhile.charAt(index))).booleanValue()) {
                return $this$dropWhile.subSequence(index, $this$dropWhile.length());
            }
        }
        return "";
    }

    public static final String dropWhile(String $this$dropWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$dropWhile, "$this$dropWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$dropWhile.length();
        for (int index = 0; index < length; index++) {
            if (!function1.invoke(Character.valueOf($this$dropWhile.charAt(index))).booleanValue()) {
                String substring = $this$dropWhile.substring(index);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                return substring;
            }
        }
        return "";
    }

    public static final CharSequence filter(CharSequence $this$filter, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Appendable destination$iv = new StringBuilder();
        int length = $this$filter.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            char element$iv = $this$filter.charAt(index$iv);
            if (function1.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        return (CharSequence) destination$iv;
    }

    public static final String filter(String $this$filter, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filter, "$this$filter");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        String $this$filterTo$iv = $this$filter;
        Appendable destination$iv = new StringBuilder();
        int length = $this$filterTo$iv.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            char element$iv = $this$filterTo$iv.charAt(index$iv);
            if (function1.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        String sb = ((StringBuilder) destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "filterTo(StringBuilder(), predicate).toString()");
        return sb;
    }

    /* JADX INFO: Multiple debug info for r6v1 'index$iv$iv'  int: [D('index$iv' int), D('index$iv$iv' int)] */
    public static final CharSequence filterIndexed(CharSequence $this$filterIndexed, Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        Appendable destination$iv = new StringBuilder();
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$filterIndexed.length()) {
            char item$iv$iv = $this$filterIndexed.charAt(i);
            int index$iv$iv2 = index$iv$iv + 1;
            if (function2.invoke(Integer.valueOf(index$iv$iv), Character.valueOf(item$iv$iv)).booleanValue()) {
                destination$iv.append(item$iv$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        return (CharSequence) destination$iv;
    }

    /* JADX INFO: Multiple debug info for r6v1 'index$iv$iv'  int: [D('index$iv' int), D('index$iv$iv' int)] */
    public static final String filterIndexed(String $this$filterIndexed, Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexed, "$this$filterIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        String $this$filterIndexedTo$iv = $this$filterIndexed;
        Appendable destination$iv = new StringBuilder();
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$filterIndexedTo$iv.length()) {
            char item$iv$iv = $this$filterIndexedTo$iv.charAt(i);
            int index$iv$iv2 = index$iv$iv + 1;
            if (function2.invoke(Integer.valueOf(index$iv$iv), Character.valueOf(item$iv$iv)).booleanValue()) {
                destination$iv.append(item$iv$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        String sb = ((StringBuilder) destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "filterIndexedTo(StringBu…(), predicate).toString()");
        return sb;
    }

    /* JADX INFO: Multiple debug info for r3v1 'index$iv'  int: [D('index' int), D('index$iv' int)] */
    public static final <C extends Appendable> C filterIndexedTo(CharSequence $this$filterIndexedTo, C c, Function2<? super Integer, ? super Character, Boolean> function2) {
        Intrinsics.checkParameterIsNotNull($this$filterIndexedTo, "$this$filterIndexedTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function2, "predicate");
        int index$iv = 0;
        int i = 0;
        while (i < $this$filterIndexedTo.length()) {
            char item$iv = $this$filterIndexedTo.charAt(i);
            int index$iv2 = index$iv + 1;
            if (function2.invoke(Integer.valueOf(index$iv), Character.valueOf(item$iv)).booleanValue()) {
                c.append(item$iv);
            }
            i++;
            index$iv = index$iv2;
        }
        return c;
    }

    public static final CharSequence filterNot(CharSequence $this$filterNot, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        Appendable destination$iv = new StringBuilder();
        for (int i = 0; i < $this$filterNot.length(); i++) {
            char element$iv = $this$filterNot.charAt(i);
            if (!function1.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        return (CharSequence) destination$iv;
    }

    public static final String filterNot(String $this$filterNot, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filterNot, "$this$filterNot");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        String $this$filterNotTo$iv = $this$filterNot;
        Appendable destination$iv = new StringBuilder();
        for (int i = 0; i < $this$filterNotTo$iv.length(); i++) {
            char element$iv = $this$filterNotTo$iv.charAt(i);
            if (!function1.invoke(Character.valueOf(element$iv)).booleanValue()) {
                destination$iv.append(element$iv);
            }
        }
        String sb = ((StringBuilder) destination$iv).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "filterNotTo(StringBuilder(), predicate).toString()");
        return sb;
    }

    public static final <C extends Appendable> C filterNotTo(CharSequence $this$filterNotTo, C c, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filterNotTo, "$this$filterNotTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$filterNotTo.length(); i++) {
            char element = $this$filterNotTo.charAt(i);
            if (!function1.invoke(Character.valueOf(element)).booleanValue()) {
                c.append(element);
            }
        }
        return c;
    }

    public static final <C extends Appendable> C filterTo(CharSequence $this$filterTo, C c, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$filterTo, "$this$filterTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$filterTo.length();
        for (int index = 0; index < length; index++) {
            char element = $this$filterTo.charAt(index);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                c.append(element);
            }
        }
        return c;
    }

    public static final CharSequence slice(CharSequence $this$slice, IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.subSequence($this$slice, indices);
    }

    public static final String slice(String $this$slice, IntRange indices) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(indices, "indices");
        if (indices.isEmpty()) {
            return "";
        }
        return StringsKt.substring($this$slice, indices);
    }

    public static final CharSequence slice(CharSequence $this$slice, Iterable<Integer> iterable) {
        Intrinsics.checkParameterIsNotNull($this$slice, "$this$slice");
        Intrinsics.checkParameterIsNotNull(iterable, "indices");
        int size = CollectionsKt.collectionSizeOrDefault(iterable, 10);
        if (size == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder(size);
        for (Integer num : iterable) {
            result.append($this$slice.charAt(num.intValue()));
        }
        return result;
    }

    private static final String slice(String $this$slice, Iterable<Integer> iterable) {
        if ($this$slice != null) {
            return StringsKt.slice((CharSequence) $this$slice, iterable).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    public static final CharSequence take(CharSequence $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        if (n >= 0) {
            return $this$take.subSequence(0, RangesKt.coerceAtMost(n, $this$take.length()));
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final String take(String $this$take, int n) {
        Intrinsics.checkParameterIsNotNull($this$take, "$this$take");
        if (n >= 0) {
            String substring = $this$take.substring(0, RangesKt.coerceAtMost(n, $this$take.length()));
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final CharSequence takeLast(CharSequence $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        if (n >= 0) {
            int length = $this$takeLast.length();
            return $this$takeLast.subSequence(length - RangesKt.coerceAtMost(n, length), length);
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final String takeLast(String $this$takeLast, int n) {
        Intrinsics.checkParameterIsNotNull($this$takeLast, "$this$takeLast");
        if (n >= 0) {
            int length = $this$takeLast.length();
            String substring = $this$takeLast.substring(length - RangesKt.coerceAtMost(n, length));
            Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + n + " is less than zero.").toString());
    }

    public static final CharSequence takeLastWhile(CharSequence $this$takeLastWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int index = StringsKt.getLastIndex($this$takeLastWhile); index >= 0; index--) {
            if (!function1.invoke(Character.valueOf($this$takeLastWhile.charAt(index))).booleanValue()) {
                return $this$takeLastWhile.subSequence(index + 1, $this$takeLastWhile.length());
            }
        }
        return $this$takeLastWhile.subSequence(0, $this$takeLastWhile.length());
    }

    public static final String takeLastWhile(String $this$takeLastWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$takeLastWhile, "$this$takeLastWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int index = StringsKt.getLastIndex($this$takeLastWhile); index >= 0; index--) {
            if (!function1.invoke(Character.valueOf($this$takeLastWhile.charAt(index))).booleanValue()) {
                String substring = $this$takeLastWhile.substring(index + 1);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.String).substring(startIndex)");
                return substring;
            }
        }
        return $this$takeLastWhile;
    }

    public static final CharSequence takeWhile(CharSequence $this$takeWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$takeWhile.length();
        for (int index = 0; index < length; index++) {
            if (!function1.invoke(Character.valueOf($this$takeWhile.charAt(index))).booleanValue()) {
                return $this$takeWhile.subSequence(0, index);
            }
        }
        return $this$takeWhile.subSequence(0, $this$takeWhile.length());
    }

    public static final String takeWhile(String $this$takeWhile, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$takeWhile, "$this$takeWhile");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int length = $this$takeWhile.length();
        for (int index = 0; index < length; index++) {
            if (!function1.invoke(Character.valueOf($this$takeWhile.charAt(index))).booleanValue()) {
                String substring = $this$takeWhile.substring(0, index);
                Intrinsics.checkExpressionValueIsNotNull(substring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                return substring;
            }
        }
        return $this$takeWhile;
    }

    public static final CharSequence reversed(CharSequence $this$reversed) {
        Intrinsics.checkParameterIsNotNull($this$reversed, "$this$reversed");
        StringBuilder reverse = new StringBuilder($this$reversed).reverse();
        Intrinsics.checkExpressionValueIsNotNull(reverse, "StringBuilder(this).reverse()");
        return reverse;
    }

    private static final String reversed(String $this$reversed) {
        if ($this$reversed != null) {
            return StringsKt.reversed((CharSequence) $this$reversed).toString();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> Map<K, V> associate(CharSequence $this$associate, Function1<? super Character, ? extends Pair<? extends K, ? extends V>> function1) {
        Intrinsics.checkParameterIsNotNull($this$associate, "$this$associate");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associate.length()), 16));
        for (int i = 0; i < $this$associate.length(); i++) {
            Pair pair = (Pair) function1.invoke(Character.valueOf($this$associate.charAt(i)));
            linkedHashMap.put(pair.getFirst(), pair.getSecond());
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K> Map<K, Character> associateBy(CharSequence $this$associateBy, Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16));
        for (int i = 0; i < $this$associateBy.length(); i++) {
            char element$iv = $this$associateBy.charAt(i);
            linkedHashMap.put(function1.invoke(Character.valueOf(element$iv)), Character.valueOf(element$iv));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> Map<K, V> associateBy(CharSequence $this$associateBy, Function1<? super Character, ? extends K> function1, Function1<? super Character, ? extends V> function12) {
        Intrinsics.checkParameterIsNotNull($this$associateBy, "$this$associateBy");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function12, "valueTransform");
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateBy.length()), 16));
        for (int i = 0; i < $this$associateBy.length(); i++) {
            char element$iv = $this$associateBy.charAt(i);
            linkedHashMap.put(function1.invoke(Character.valueOf(element$iv)), function12.invoke(Character.valueOf(element$iv)));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, M extends Map<? super K, ? super Character>> M associateByTo(CharSequence $this$associateByTo, M m, Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        for (int i = 0; i < $this$associateByTo.length(); i++) {
            char element = $this$associateByTo.charAt(i);
            m.put(function1.invoke(Character.valueOf(element)), Character.valueOf(element));
        }
        return m;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, M extends Map<? super K, ? super V>> M associateByTo(CharSequence $this$associateByTo, M m, Function1<? super Character, ? extends K> function1, Function1<? super Character, ? extends V> function12) {
        Intrinsics.checkParameterIsNotNull($this$associateByTo, "$this$associateByTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function12, "valueTransform");
        for (int i = 0; i < $this$associateByTo.length(); i++) {
            char element = $this$associateByTo.charAt(i);
            m.put(function1.invoke(Character.valueOf(element)), function12.invoke(Character.valueOf(element)));
        }
        return m;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, M extends Map<? super K, ? super V>> M associateTo(CharSequence $this$associateTo, M m, Function1<? super Character, ? extends Pair<? extends K, ? extends V>> function1) {
        Intrinsics.checkParameterIsNotNull($this$associateTo, "$this$associateTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < $this$associateTo.length(); i++) {
            Pair pair = (Pair) function1.invoke(Character.valueOf($this$associateTo.charAt(i)));
            m.put(pair.getFirst(), pair.getSecond());
        }
        return m;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <V> Map<Character, V> associateWith(CharSequence $this$associateWith, Function1<? super Character, ? extends V> function1) {
        Intrinsics.checkParameterIsNotNull($this$associateWith, "$this$associateWith");
        Intrinsics.checkParameterIsNotNull(function1, "valueSelector");
        LinkedHashMap result = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity($this$associateWith.length()), 16));
        for (int i = 0; i < $this$associateWith.length(); i++) {
            char element$iv = $this$associateWith.charAt(i);
            result.put(Character.valueOf(element$iv), function1.invoke(Character.valueOf(element$iv)));
        }
        return result;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <V, M extends Map<? super Character, ? super V>> M associateWithTo(CharSequence $this$associateWithTo, M m, Function1<? super Character, ? extends V> function1) {
        Intrinsics.checkParameterIsNotNull($this$associateWithTo, "$this$associateWithTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "valueSelector");
        for (int i = 0; i < $this$associateWithTo.length(); i++) {
            char element = $this$associateWithTo.charAt(i);
            m.put(Character.valueOf(element), function1.invoke(Character.valueOf(element)));
        }
        return m;
    }

    public static final <C extends Collection<? super Character>> C toCollection(CharSequence $this$toCollection, C c) {
        Intrinsics.checkParameterIsNotNull($this$toCollection, "$this$toCollection");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        for (int i = 0; i < $this$toCollection.length(); i++) {
            c.add(Character.valueOf($this$toCollection.charAt(i)));
        }
        return c;
    }

    public static final HashSet<Character> toHashSet(CharSequence $this$toHashSet) {
        Intrinsics.checkParameterIsNotNull($this$toHashSet, "$this$toHashSet");
        return (HashSet) StringsKt.toCollection($this$toHashSet, new HashSet(MapsKt.mapCapacity($this$toHashSet.length())));
    }

    public static final List<Character> toList(CharSequence $this$toList) {
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        int length = $this$toList.length();
        if (length == 0) {
            return CollectionsKt.emptyList();
        }
        if (length != 1) {
            return StringsKt.toMutableList($this$toList);
        }
        return CollectionsKt.listOf(Character.valueOf($this$toList.charAt(0)));
    }

    public static final List<Character> toMutableList(CharSequence $this$toMutableList) {
        Intrinsics.checkParameterIsNotNull($this$toMutableList, "$this$toMutableList");
        return (List) StringsKt.toCollection($this$toMutableList, new ArrayList($this$toMutableList.length()));
    }

    public static final Set<Character> toSet(CharSequence $this$toSet) {
        Intrinsics.checkParameterIsNotNull($this$toSet, "$this$toSet");
        int length = $this$toSet.length();
        if (length == 0) {
            return SetsKt.emptySet();
        }
        if (length != 1) {
            return (Set) StringsKt.toCollection($this$toSet, new LinkedHashSet(MapsKt.mapCapacity($this$toSet.length())));
        }
        return SetsKt.setOf(Character.valueOf($this$toSet.charAt(0)));
    }

    public static final <R> List<R> flatMap(CharSequence $this$flatMap, Function1<? super Character, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkParameterIsNotNull($this$flatMap, "$this$flatMap");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        Collection destination$iv = new ArrayList();
        for (int i = 0; i < $this$flatMap.length(); i++) {
            CollectionsKt.addAll(destination$iv, (Iterable) function1.invoke(Character.valueOf($this$flatMap.charAt(i))));
        }
        return (List) destination$iv;
    }

    public static final <R, C extends Collection<? super R>> C flatMapTo(CharSequence $this$flatMapTo, C c, Function1<? super Character, ? extends Iterable<? extends R>> function1) {
        Intrinsics.checkParameterIsNotNull($this$flatMapTo, "$this$flatMapTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < $this$flatMapTo.length(); i++) {
            CollectionsKt.addAll(c, (Iterable) function1.invoke(Character.valueOf($this$flatMapTo.charAt(i))));
        }
        return c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K> Map<K, List<Character>> groupBy(CharSequence $this$groupBy, Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < $this$groupBy.length(); i++) {
            char element$iv = $this$groupBy.charAt(i);
            Object key$iv = function1.invoke(Character.valueOf(element$iv));
            Object value$iv$iv = linkedHashMap.get(key$iv);
            if (value$iv$iv == null) {
                ArrayList arrayList = new ArrayList();
                linkedHashMap.put(key$iv, arrayList);
                value$iv$iv = arrayList;
            }
            ((List) value$iv$iv).add(Character.valueOf(element$iv));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> Map<K, List<V>> groupBy(CharSequence $this$groupBy, Function1<? super Character, ? extends K> function1, Function1<? super Character, ? extends V> function12) {
        Intrinsics.checkParameterIsNotNull($this$groupBy, "$this$groupBy");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function12, "valueTransform");
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (int i = 0; i < $this$groupBy.length(); i++) {
            char element$iv = $this$groupBy.charAt(i);
            Object key$iv = function1.invoke(Character.valueOf(element$iv));
            Object value$iv$iv = linkedHashMap.get(key$iv);
            if (value$iv$iv == null) {
                ArrayList arrayList = new ArrayList();
                linkedHashMap.put(key$iv, arrayList);
                value$iv$iv = arrayList;
            }
            ((List) value$iv$iv).add(function12.invoke(Character.valueOf(element$iv)));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, M extends Map<? super K, List<Character>>> M groupByTo(CharSequence $this$groupByTo, M m, Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        for (int i = 0; i < $this$groupByTo.length(); i++) {
            char element = $this$groupByTo.charAt(i);
            Object key = function1.invoke(Character.valueOf(element));
            Object value$iv = m.get(key);
            if (value$iv == null) {
                ArrayList arrayList = new ArrayList();
                m.put(key, arrayList);
                value$iv = arrayList;
            }
            ((List) value$iv).add(Character.valueOf(element));
        }
        return m;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, M extends Map<? super K, List<V>>> M groupByTo(CharSequence $this$groupByTo, M m, Function1<? super Character, ? extends K> function1, Function1<? super Character, ? extends V> function12) {
        Intrinsics.checkParameterIsNotNull($this$groupByTo, "$this$groupByTo");
        Intrinsics.checkParameterIsNotNull(m, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        Intrinsics.checkParameterIsNotNull(function12, "valueTransform");
        for (int i = 0; i < $this$groupByTo.length(); i++) {
            char element = $this$groupByTo.charAt(i);
            Object key = function1.invoke(Character.valueOf(element));
            Object value$iv = m.get(key);
            if (value$iv == null) {
                ArrayList arrayList = new ArrayList();
                m.put(key, arrayList);
                value$iv = arrayList;
            }
            ((List) value$iv).add(function12.invoke(Character.valueOf(element)));
        }
        return m;
    }

    public static final <K> Grouping<Character, K> groupingBy(CharSequence $this$groupingBy, Function1<? super Character, ? extends K> function1) {
        Intrinsics.checkParameterIsNotNull($this$groupingBy, "$this$groupingBy");
        Intrinsics.checkParameterIsNotNull(function1, "keySelector");
        return new Grouping<Character, K>($this$groupingBy, function1) { // from class: kotlin.text.StringsKt___StringsKt$groupingBy$1
            final /* synthetic */ Function1 $keySelector;
            final /* synthetic */ CharSequence $this_groupingBy;

            {
                this.$this_groupingBy = $receiver;
                this.$keySelector = $captured_local_variable$1;
            }

            @Override // kotlin.collections.Grouping
            public /* bridge */ /* synthetic */ Object keyOf(Character ch) {
                return keyOf(ch.charValue());
            }

            @Override // kotlin.collections.Grouping
            public Iterator<Character> sourceIterator() {
                return StringsKt.iterator(this.$this_groupingBy);
            }

            /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, K] */
            public K keyOf(char element) {
                return this.$keySelector.invoke(Character.valueOf(element));
            }
        };
    }

    public static final <R> List<R> map(CharSequence $this$map, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$map, "$this$map");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        Collection destination$iv = new ArrayList($this$map.length());
        for (int i = 0; i < $this$map.length(); i++) {
            destination$iv.add(function1.invoke(Character.valueOf($this$map.charAt(i))));
        }
        return (List) destination$iv;
    }

    public static final <R> List<R> mapIndexed(CharSequence $this$mapIndexed, Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexed, "$this$mapIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        Collection destination$iv = new ArrayList($this$mapIndexed.length());
        int index$iv = 0;
        for (int i = 0; i < $this$mapIndexed.length(); i++) {
            char item$iv = $this$mapIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index$iv);
            index$iv++;
            destination$iv.add(function2.invoke(valueOf, Character.valueOf(item$iv)));
        }
        return (List) destination$iv;
    }

    /* JADX INFO: Multiple debug info for r6v1 'index$iv$iv'  int: [D('index$iv' int), D('index$iv$iv' int)] */
    public static final <R> List<R> mapIndexedNotNull(CharSequence $this$mapIndexedNotNull, Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNull, "$this$mapIndexedNotNull");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        Collection destination$iv = new ArrayList();
        int index$iv$iv = 0;
        int i = 0;
        while (i < $this$mapIndexedNotNull.length()) {
            int index$iv$iv2 = index$iv$iv + 1;
            Object it$iv = function2.invoke(Integer.valueOf(index$iv$iv), Character.valueOf($this$mapIndexedNotNull.charAt(i)));
            if (it$iv != null) {
                destination$iv.add(it$iv);
            }
            i++;
            index$iv$iv = index$iv$iv2;
        }
        return (List) destination$iv;
    }

    /* JADX INFO: Multiple debug info for r3v1 'index$iv'  int: [D('index' int), D('index$iv' int)] */
    public static final <R, C extends Collection<? super R>> C mapIndexedNotNullTo(CharSequence $this$mapIndexedNotNullTo, C c, Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedNotNullTo, "$this$mapIndexedNotNullTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        int index$iv = 0;
        int i = 0;
        while (i < $this$mapIndexedNotNullTo.length()) {
            int index$iv2 = index$iv + 1;
            Object it = function2.invoke(Integer.valueOf(index$iv), Character.valueOf($this$mapIndexedNotNullTo.charAt(i)));
            if (it != null) {
                c.add(it);
            }
            i++;
            index$iv = index$iv2;
        }
        return c;
    }

    public static final <R, C extends Collection<? super R>> C mapIndexedTo(CharSequence $this$mapIndexedTo, C c, Function2<? super Integer, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$mapIndexedTo, "$this$mapIndexedTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        int index = 0;
        for (int i = 0; i < $this$mapIndexedTo.length(); i++) {
            char item = $this$mapIndexedTo.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            c.add(function2.invoke(valueOf, Character.valueOf(item)));
        }
        return c;
    }

    public static final <R> List<R> mapNotNull(CharSequence $this$mapNotNull, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$mapNotNull, "$this$mapNotNull");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        Collection destination$iv = new ArrayList();
        for (int i = 0; i < $this$mapNotNull.length(); i++) {
            Object it$iv = function1.invoke(Character.valueOf($this$mapNotNull.charAt(i)));
            if (it$iv != null) {
                destination$iv.add(it$iv);
            }
        }
        return (List) destination$iv;
    }

    public static final <R, C extends Collection<? super R>> C mapNotNullTo(CharSequence $this$mapNotNullTo, C c, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$mapNotNullTo, "$this$mapNotNullTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < $this$mapNotNullTo.length(); i++) {
            Object it = function1.invoke(Character.valueOf($this$mapNotNullTo.charAt(i)));
            if (it != null) {
                c.add(it);
            }
        }
        return c;
    }

    public static final <R, C extends Collection<? super R>> C mapTo(CharSequence $this$mapTo, C c, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$mapTo, "$this$mapTo");
        Intrinsics.checkParameterIsNotNull(c, FirebaseAnalytics.Param.DESTINATION);
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        for (int i = 0; i < $this$mapTo.length(); i++) {
            c.add(function1.invoke(Character.valueOf($this$mapTo.charAt(i))));
        }
        return c;
    }

    public static final Iterable<IndexedValue<Character>> withIndex(CharSequence $this$withIndex) {
        Intrinsics.checkParameterIsNotNull($this$withIndex, "$this$withIndex");
        return new IndexingIterable(new Function0<CharIterator>($this$withIndex) { // from class: kotlin.text.StringsKt___StringsKt$withIndex$1
            final /* synthetic */ CharSequence $this_withIndex;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_withIndex = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final CharIterator invoke() {
                return StringsKt.iterator(this.$this_withIndex);
            }
        });
    }

    public static final boolean all(CharSequence $this$all, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$all, "$this$all");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$all.length(); i++) {
            if (!function1.invoke(Character.valueOf($this$all.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final boolean any(CharSequence $this$any) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        return !($this$any.length() == 0);
    }

    public static final boolean any(CharSequence $this$any, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$any, "$this$any");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$any.length(); i++) {
            if (function1.invoke(Character.valueOf($this$any.charAt(i))).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    private static final int count(CharSequence $this$count) {
        return $this$count.length();
    }

    public static final int count(CharSequence $this$count, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$count, "$this$count");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        int count = 0;
        for (int i = 0; i < $this$count.length(); i++) {
            if (function1.invoke(Character.valueOf($this$count.charAt(i))).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    public static final <R> R fold(CharSequence $this$fold, R r, Function2<? super R, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$fold, "$this$fold");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        Object accumulator = r;
        for (int i = 0; i < $this$fold.length(); i++) {
            accumulator = (R) function2.invoke(accumulator, Character.valueOf($this$fold.charAt(i)));
        }
        return (R) accumulator;
    }

    public static final <R> R foldIndexed(CharSequence $this$foldIndexed, R r, Function3<? super Integer, ? super R, ? super Character, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull($this$foldIndexed, "$this$foldIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        int index = 0;
        Object accumulator = r;
        for (int i = 0; i < $this$foldIndexed.length(); i++) {
            char element = $this$foldIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            accumulator = (R) function3.invoke(valueOf, accumulator, Character.valueOf(element));
        }
        return (R) accumulator;
    }

    public static final <R> R foldRight(CharSequence $this$foldRight, R r, Function2<? super Character, ? super R, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$foldRight, "$this$foldRight");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        Object accumulator = r;
        for (int index = StringsKt.getLastIndex($this$foldRight); index >= 0; index--) {
            accumulator = (R) function2.invoke(Character.valueOf($this$foldRight.charAt(index)), accumulator);
        }
        return (R) accumulator;
    }

    public static final <R> R foldRightIndexed(CharSequence $this$foldRightIndexed, R r, Function3<? super Integer, ? super Character, ? super R, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull($this$foldRightIndexed, "$this$foldRightIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        Object accumulator = r;
        for (int index = StringsKt.getLastIndex($this$foldRightIndexed); index >= 0; index--) {
            accumulator = (R) function3.invoke(Integer.valueOf(index), Character.valueOf($this$foldRightIndexed.charAt(index)), accumulator);
        }
        return (R) accumulator;
    }

    public static final void forEach(CharSequence $this$forEach, Function1<? super Character, Unit> function1) {
        Intrinsics.checkParameterIsNotNull($this$forEach, "$this$forEach");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        for (int i = 0; i < $this$forEach.length(); i++) {
            function1.invoke(Character.valueOf($this$forEach.charAt(i)));
        }
    }

    public static final void forEachIndexed(CharSequence $this$forEachIndexed, Function2<? super Integer, ? super Character, Unit> function2) {
        Intrinsics.checkParameterIsNotNull($this$forEachIndexed, "$this$forEachIndexed");
        Intrinsics.checkParameterIsNotNull(function2, "action");
        int index = 0;
        for (int i = 0; i < $this$forEachIndexed.length(); i++) {
            char item = $this$forEachIndexed.charAt(i);
            Integer valueOf = Integer.valueOf(index);
            index++;
            function2.invoke(valueOf, Character.valueOf(item));
        }
    }

    public static final Character max(CharSequence $this$max) {
        Intrinsics.checkParameterIsNotNull($this$max, "$this$max");
        int i = 1;
        if ($this$max.length() == 0) {
            return null;
        }
        char max = $this$max.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$max);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$max.charAt(i);
                if (max < e) {
                    max = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(max);
    }

    public static final <R extends Comparable<? super R>> Character maxBy(CharSequence $this$maxBy, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$maxBy, "$this$maxBy");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        int i = 1;
        if ($this$maxBy.length() == 0) {
            return null;
        }
        char maxElem = $this$maxBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$maxBy);
        if (lastIndex == 0) {
            return Character.valueOf(maxElem);
        }
        Comparable maxValue = (Comparable) function1.invoke(Character.valueOf(maxElem));
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$maxBy.charAt(i);
                Comparable v = (Comparable) function1.invoke(Character.valueOf(e));
                if (maxValue.compareTo(v) < 0) {
                    maxElem = e;
                    maxValue = v;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(maxElem);
    }

    public static final Character maxWith(CharSequence $this$maxWith, Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull($this$maxWith, "$this$maxWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        int i = 1;
        if ($this$maxWith.length() == 0) {
            return null;
        }
        char max = $this$maxWith.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$maxWith);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$maxWith.charAt(i);
                if (comparator.compare(Character.valueOf(max), Character.valueOf(e)) < 0) {
                    max = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(max);
    }

    public static final Character min(CharSequence $this$min) {
        Intrinsics.checkParameterIsNotNull($this$min, "$this$min");
        int i = 1;
        if ($this$min.length() == 0) {
            return null;
        }
        char min = $this$min.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$min);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$min.charAt(i);
                if (min > e) {
                    min = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(min);
    }

    public static final <R extends Comparable<? super R>> Character minBy(CharSequence $this$minBy, Function1<? super Character, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$minBy, "$this$minBy");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        int i = 1;
        if ($this$minBy.length() == 0) {
            return null;
        }
        char minElem = $this$minBy.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$minBy);
        if (lastIndex == 0) {
            return Character.valueOf(minElem);
        }
        Comparable minValue = (Comparable) function1.invoke(Character.valueOf(minElem));
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$minBy.charAt(i);
                Comparable v = (Comparable) function1.invoke(Character.valueOf(e));
                if (minValue.compareTo(v) > 0) {
                    minElem = e;
                    minValue = v;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(minElem);
    }

    public static final Character minWith(CharSequence $this$minWith, Comparator<? super Character> comparator) {
        Intrinsics.checkParameterIsNotNull($this$minWith, "$this$minWith");
        Intrinsics.checkParameterIsNotNull(comparator, "comparator");
        int i = 1;
        if ($this$minWith.length() == 0) {
            return null;
        }
        char min = $this$minWith.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$minWith);
        if (1 <= lastIndex) {
            while (true) {
                char e = $this$minWith.charAt(i);
                if (comparator.compare(Character.valueOf(min), Character.valueOf(e)) > 0) {
                    min = e;
                }
                if (i == lastIndex) {
                    break;
                }
                i++;
            }
        }
        return Character.valueOf(min);
    }

    public static final boolean none(CharSequence $this$none) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        return $this$none.length() == 0;
    }

    public static final boolean none(CharSequence $this$none, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$none, "$this$none");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        for (int i = 0; i < $this$none.length(); i++) {
            if (function1.invoke(Character.valueOf($this$none.charAt(i))).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <S extends CharSequence> S onEach(S s, Function1<? super Character, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(s, "$this$onEach");
        Intrinsics.checkParameterIsNotNull(function1, "action");
        for (int i = 0; i < s.length(); i++) {
            function1.invoke(Character.valueOf(s.charAt(i)));
        }
        return s;
    }

    public static final char reduce(CharSequence $this$reduce, Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull($this$reduce, "$this$reduce");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        int index = 1;
        if (!($this$reduce.length() == 0)) {
            char accumulator = $this$reduce.charAt(0);
            int lastIndex = StringsKt.getLastIndex($this$reduce);
            if (1 <= lastIndex) {
                while (true) {
                    accumulator = function2.invoke(Character.valueOf(accumulator), Character.valueOf($this$reduce.charAt(index))).charValue();
                    if (index == lastIndex) {
                        break;
                    }
                    index++;
                }
            }
            return accumulator;
        }
        throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
    }

    public static final char reduceIndexed(CharSequence $this$reduceIndexed, Function3<? super Integer, ? super Character, ? super Character, Character> function3) {
        Intrinsics.checkParameterIsNotNull($this$reduceIndexed, "$this$reduceIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        int index = 1;
        if (!($this$reduceIndexed.length() == 0)) {
            char accumulator = $this$reduceIndexed.charAt(0);
            int lastIndex = StringsKt.getLastIndex($this$reduceIndexed);
            if (1 <= lastIndex) {
                while (true) {
                    accumulator = function3.invoke(Integer.valueOf(index), Character.valueOf(accumulator), Character.valueOf($this$reduceIndexed.charAt(index))).charValue();
                    if (index == lastIndex) {
                        break;
                    }
                    index++;
                }
            }
            return accumulator;
        }
        throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
    }

    public static final Character reduceOrNull(CharSequence $this$reduceOrNull, Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull($this$reduceOrNull, "$this$reduceOrNull");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        int index = 1;
        if ($this$reduceOrNull.length() == 0) {
            return null;
        }
        char accumulator = $this$reduceOrNull.charAt(0);
        int lastIndex = StringsKt.getLastIndex($this$reduceOrNull);
        if (1 <= lastIndex) {
            while (true) {
                accumulator = function2.invoke(Character.valueOf(accumulator), Character.valueOf($this$reduceOrNull.charAt(index))).charValue();
                if (index == lastIndex) {
                    break;
                }
                index++;
            }
        }
        return Character.valueOf(accumulator);
    }

    public static final char reduceRight(CharSequence $this$reduceRight, Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull($this$reduceRight, "$this$reduceRight");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        int index = StringsKt.getLastIndex($this$reduceRight);
        if (index >= 0) {
            char accumulator = $this$reduceRight.charAt(index);
            for (int index2 = index - 1; index2 >= 0; index2--) {
                accumulator = function2.invoke(Character.valueOf($this$reduceRight.charAt(index2)), Character.valueOf(accumulator)).charValue();
            }
            return accumulator;
        }
        throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
    }

    public static final char reduceRightIndexed(CharSequence $this$reduceRightIndexed, Function3<? super Integer, ? super Character, ? super Character, Character> function3) {
        Intrinsics.checkParameterIsNotNull($this$reduceRightIndexed, "$this$reduceRightIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        int index = StringsKt.getLastIndex($this$reduceRightIndexed);
        if (index >= 0) {
            char accumulator = $this$reduceRightIndexed.charAt(index);
            for (int index2 = index - 1; index2 >= 0; index2--) {
                accumulator = function3.invoke(Integer.valueOf(index2), Character.valueOf($this$reduceRightIndexed.charAt(index2)), Character.valueOf(accumulator)).charValue();
            }
            return accumulator;
        }
        throw new UnsupportedOperationException("Empty char sequence can't be reduced.");
    }

    public static final Character reduceRightOrNull(CharSequence $this$reduceRightOrNull, Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull($this$reduceRightOrNull, "$this$reduceRightOrNull");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        int index = StringsKt.getLastIndex($this$reduceRightOrNull);
        if (index < 0) {
            return null;
        }
        char accumulator = $this$reduceRightOrNull.charAt(index);
        for (int index2 = index - 1; index2 >= 0; index2--) {
            accumulator = function2.invoke(Character.valueOf($this$reduceRightOrNull.charAt(index2)), Character.valueOf(accumulator)).charValue();
        }
        return Character.valueOf(accumulator);
    }

    /* JADX INFO: Multiple debug info for r1v4 java.util.ArrayList: [D('result' java.util.ArrayList), D('$this$apply' java.util.ArrayList)] */
    public static final <R> List<R> scan(CharSequence $this$scan, R r, Function2<? super R, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$scan, "$this$scan");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        if ($this$scan.length() == 0) {
            return CollectionsKt.listOf(r);
        }
        ArrayList $this$apply = new ArrayList($this$scan.length() + 1);
        $this$apply.add(r);
        Object accumulator = (Object) r;
        for (int i = 0; i < $this$scan.length(); i++) {
            accumulator = (Object) function2.invoke(accumulator, Character.valueOf($this$scan.charAt(i)));
            $this$apply.add(accumulator);
        }
        return $this$apply;
    }

    /* JADX INFO: Multiple debug info for r1v4 java.util.ArrayList: [D('result' java.util.ArrayList), D('$this$apply' java.util.ArrayList)] */
    public static final <R> List<R> scanIndexed(CharSequence $this$scanIndexed, R r, Function3<? super Integer, ? super R, ? super Character, ? extends R> function3) {
        Intrinsics.checkParameterIsNotNull($this$scanIndexed, "$this$scanIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        if ($this$scanIndexed.length() == 0) {
            return CollectionsKt.listOf(r);
        }
        ArrayList $this$apply = new ArrayList($this$scanIndexed.length() + 1);
        $this$apply.add(r);
        Object accumulator = (Object) r;
        int length = $this$scanIndexed.length();
        for (int index = 0; index < length; index++) {
            accumulator = (Object) function3.invoke(Integer.valueOf(index), accumulator, Character.valueOf($this$scanIndexed.charAt(index)));
            $this$apply.add(accumulator);
        }
        return $this$apply;
    }

    /* JADX INFO: Multiple debug info for r1v6 int: [D('index' int), D('accumulator' char)] */
    /* JADX INFO: Multiple debug info for r2v1 java.util.ArrayList: [D('result' java.util.ArrayList), D('$this$apply' java.util.ArrayList)] */
    public static final List<Character> scanReduce(CharSequence $this$scanReduce, Function2<? super Character, ? super Character, Character> function2) {
        Intrinsics.checkParameterIsNotNull($this$scanReduce, "$this$scanReduce");
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        if ($this$scanReduce.length() == 0) {
            return CollectionsKt.emptyList();
        }
        char accumulator = $this$scanReduce.charAt(0);
        ArrayList $this$apply = new ArrayList($this$scanReduce.length());
        $this$apply.add(Character.valueOf(accumulator));
        int length = $this$scanReduce.length();
        char accumulator2 = accumulator;
        for (int index = 1; index < length; index++) {
            accumulator2 = function2.invoke(Character.valueOf(accumulator2), Character.valueOf($this$scanReduce.charAt(index))).charValue();
            $this$apply.add(Character.valueOf(accumulator2));
        }
        return $this$apply;
    }

    /* JADX INFO: Multiple debug info for r1v6 int: [D('index' int), D('accumulator' char)] */
    /* JADX INFO: Multiple debug info for r2v1 java.util.ArrayList: [D('result' java.util.ArrayList), D('$this$apply' java.util.ArrayList)] */
    public static final List<Character> scanReduceIndexed(CharSequence $this$scanReduceIndexed, Function3<? super Integer, ? super Character, ? super Character, Character> function3) {
        Intrinsics.checkParameterIsNotNull($this$scanReduceIndexed, "$this$scanReduceIndexed");
        Intrinsics.checkParameterIsNotNull(function3, "operation");
        if ($this$scanReduceIndexed.length() == 0) {
            return CollectionsKt.emptyList();
        }
        char accumulator = $this$scanReduceIndexed.charAt(0);
        ArrayList $this$apply = new ArrayList($this$scanReduceIndexed.length());
        $this$apply.add(Character.valueOf(accumulator));
        int length = $this$scanReduceIndexed.length();
        char accumulator2 = accumulator;
        for (int index = 1; index < length; index++) {
            accumulator2 = function3.invoke(Integer.valueOf(index), Character.valueOf(accumulator2), Character.valueOf($this$scanReduceIndexed.charAt(index))).charValue();
            $this$apply.add(Character.valueOf(accumulator2));
        }
        return $this$apply;
    }

    public static final int sumBy(CharSequence $this$sumBy, Function1<? super Character, Integer> function1) {
        Intrinsics.checkParameterIsNotNull($this$sumBy, "$this$sumBy");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        int sum = 0;
        for (int i = 0; i < $this$sumBy.length(); i++) {
            sum += function1.invoke(Character.valueOf($this$sumBy.charAt(i))).intValue();
        }
        return sum;
    }

    public static final double sumByDouble(CharSequence $this$sumByDouble, Function1<? super Character, Double> function1) {
        Intrinsics.checkParameterIsNotNull($this$sumByDouble, "$this$sumByDouble");
        Intrinsics.checkParameterIsNotNull(function1, "selector");
        double sum = 0.0d;
        for (int i = 0; i < $this$sumByDouble.length(); i++) {
            sum += function1.invoke(Character.valueOf($this$sumByDouble.charAt(i))).doubleValue();
        }
        return sum;
    }

    public static final List<String> chunked(CharSequence $this$chunked, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        return StringsKt.windowed($this$chunked, size, size, true);
    }

    public static final <R> List<R> chunked(CharSequence $this$chunked, int size, Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$chunked, "$this$chunked");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return StringsKt.windowed($this$chunked, size, size, true, function1);
    }

    public static final Sequence<String> chunkedSequence(CharSequence $this$chunkedSequence, int size) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        return StringsKt.chunkedSequence($this$chunkedSequence, size, StringsKt___StringsKt$chunkedSequence$1.INSTANCE);
    }

    public static final <R> Sequence<R> chunkedSequence(CharSequence $this$chunkedSequence, int size, Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$chunkedSequence, "$this$chunkedSequence");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        return StringsKt.windowedSequence($this$chunkedSequence, size, size, true, function1);
    }

    public static final Pair<CharSequence, CharSequence> partition(CharSequence $this$partition, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        for (int i = 0; i < $this$partition.length(); i++) {
            char element = $this$partition.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
            } else {
                second.append(element);
            }
        }
        return new Pair<>(first, second);
    }

    public static final Pair<String, String> partition(String $this$partition, Function1<? super Character, Boolean> function1) {
        Intrinsics.checkParameterIsNotNull($this$partition, "$this$partition");
        Intrinsics.checkParameterIsNotNull(function1, "predicate");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        int length = $this$partition.length();
        for (int i = 0; i < length; i++) {
            char element = $this$partition.charAt(i);
            if (function1.invoke(Character.valueOf(element)).booleanValue()) {
                first.append(element);
            } else {
                second.append(element);
            }
        }
        return new Pair<>(first.toString(), second.toString());
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z);
    }

    public static final List<String> windowed(CharSequence $this$windowed, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        return StringsKt.windowed($this$windowed, size, step, partialWindows, StringsKt___StringsKt$windowed$1.INSTANCE);
    }

    public static /* synthetic */ List windowed$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowed(charSequence, i, i2, z, function1);
    }

    public static final <R> List<R> windowed(CharSequence $this$windowed, int size, int step, boolean partialWindows, Function1<? super CharSequence, ? extends R> function1) {
        int coercedEnd;
        Intrinsics.checkParameterIsNotNull($this$windowed, "$this$windowed");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        int thisSize = $this$windowed.length();
        ArrayList result = new ArrayList((thisSize / step) + (thisSize % step == 0 ? 0 : 1));
        int index = 0;
        while (index >= 0 && thisSize > index) {
            int end = index + size;
            if (end < 0 || end > thisSize) {
                if (!partialWindows) {
                    break;
                }
                coercedEnd = thisSize;
            } else {
                coercedEnd = end;
            }
            result.add(function1.invoke($this$windowed.subSequence(index, coercedEnd)));
            index += step;
        }
        return result;
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z);
    }

    public static final Sequence<String> windowedSequence(CharSequence $this$windowedSequence, int size, int step, boolean partialWindows) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        return StringsKt.windowedSequence($this$windowedSequence, size, step, partialWindows, StringsKt___StringsKt$windowedSequence$1.INSTANCE);
    }

    public static /* synthetic */ Sequence windowedSequence$default(CharSequence charSequence, int i, int i2, boolean z, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return StringsKt.windowedSequence(charSequence, i, i2, z, function1);
    }

    public static final <R> Sequence<R> windowedSequence(CharSequence $this$windowedSequence, int size, int step, boolean partialWindows, Function1<? super CharSequence, ? extends R> function1) {
        Intrinsics.checkParameterIsNotNull($this$windowedSequence, "$this$windowedSequence");
        Intrinsics.checkParameterIsNotNull(function1, "transform");
        SlidingWindowKt.checkWindowSizeStep(size, step);
        return SequencesKt.map(CollectionsKt.asSequence(RangesKt.step(partialWindows ? StringsKt.getIndices($this$windowedSequence) : RangesKt.until(0, ($this$windowedSequence.length() - size) + 1), step)), new Function1<Integer, R>($this$windowedSequence, size, function1) { // from class: kotlin.text.StringsKt___StringsKt$windowedSequence$2
            final /* synthetic */ int $size;
            final /* synthetic */ CharSequence $this_windowedSequence;
            final /* synthetic */ Function1 $transform;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$this_windowedSequence = r1;
                this.$size = r2;
                this.$transform = r3;
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Integer num) {
                return invoke(num.intValue());
            }

            /* JADX WARN: Type inference failed for: r0v2, types: [R, java.lang.Object] */
            public final R invoke(int index) {
                int end = this.$size + index;
                return this.$transform.invoke(this.$this_windowedSequence.subSequence(index, (end < 0 || end > this.$this_windowedSequence.length()) ? this.$this_windowedSequence.length() : end));
            }
        });
    }

    public static final List<Pair<Character, Character>> zip(CharSequence $this$zip, CharSequence other) {
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        int length$iv = Math.min($this$zip.length(), other.length());
        ArrayList list$iv = new ArrayList(length$iv);
        for (int i$iv = 0; i$iv < length$iv; i$iv++) {
            list$iv.add(TuplesKt.to(Character.valueOf($this$zip.charAt(i$iv)), Character.valueOf(other.charAt(i$iv))));
        }
        return list$iv;
    }

    public static final <V> List<V> zip(CharSequence $this$zip, CharSequence other, Function2<? super Character, ? super Character, ? extends V> function2) {
        Intrinsics.checkParameterIsNotNull($this$zip, "$this$zip");
        Intrinsics.checkParameterIsNotNull(other, "other");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        int length = Math.min($this$zip.length(), other.length());
        ArrayList list = new ArrayList(length);
        for (int i = 0; i < length; i++) {
            list.add(function2.invoke(Character.valueOf($this$zip.charAt(i)), Character.valueOf(other.charAt(i))));
        }
        return list;
    }

    public static final List<Pair<Character, Character>> zipWithNext(CharSequence $this$zipWithNext) {
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        int size$iv = $this$zipWithNext.length() - 1;
        if (size$iv < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList result$iv = new ArrayList(size$iv);
        for (int index$iv = 0; index$iv < size$iv; index$iv++) {
            result$iv.add(TuplesKt.to(Character.valueOf($this$zipWithNext.charAt(index$iv)), Character.valueOf($this$zipWithNext.charAt(index$iv + 1))));
        }
        return result$iv;
    }

    public static final <R> List<R> zipWithNext(CharSequence $this$zipWithNext, Function2<? super Character, ? super Character, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull($this$zipWithNext, "$this$zipWithNext");
        Intrinsics.checkParameterIsNotNull(function2, "transform");
        int size = $this$zipWithNext.length() - 1;
        if (size < 1) {
            return CollectionsKt.emptyList();
        }
        ArrayList result = new ArrayList(size);
        for (int index = 0; index < size; index++) {
            result.add(function2.invoke(Character.valueOf($this$zipWithNext.charAt(index)), Character.valueOf($this$zipWithNext.charAt(index + 1))));
        }
        return result;
    }

    public static final Iterable<Character> asIterable(CharSequence $this$asIterable) {
        Intrinsics.checkParameterIsNotNull($this$asIterable, "$this$asIterable");
        if ($this$asIterable instanceof String) {
            if ($this$asIterable.length() == 0) {
                return CollectionsKt.emptyList();
            }
        }
        return new Object($this$asIterable) { // from class: kotlin.text.StringsKt___StringsKt$asIterable$$inlined$Iterable$1
            final /* synthetic */ CharSequence $this_asIterable$inlined;

            {
                this.$this_asIterable$inlined = r1;
            }

            @Override // java.lang.Iterable
            public Iterator<Character> iterator() {
                return StringsKt.iterator(this.$this_asIterable$inlined);
            }
        };
    }

    public static final Sequence<Character> asSequence(CharSequence $this$asSequence) {
        Intrinsics.checkParameterIsNotNull($this$asSequence, "$this$asSequence");
        if ($this$asSequence instanceof String) {
            if ($this$asSequence.length() == 0) {
                return SequencesKt.emptySequence();
            }
        }
        return new Sequence<Character>($this$asSequence) { // from class: kotlin.text.StringsKt___StringsKt$asSequence$$inlined$Sequence$1
            final /* synthetic */ CharSequence $this_asSequence$inlined;

            {
                this.$this_asSequence$inlined = r1;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<Character> iterator() {
                return StringsKt.iterator(this.$this_asSequence$inlined);
            }
        };
    }
}
