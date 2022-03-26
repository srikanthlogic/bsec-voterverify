package kotlin.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;
import org.apache.commons.io.IOUtils;
/* compiled from: Indent.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\n\u0010\u0013\u001a\u00020\u0002*\u00020\u0002\u001a\u0014\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002¨\u0006\u0015"}, d2 = {"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, k = 5, mv = {1, 1, 16}, xi = 1, xs = "kotlin/text/StringsKt")
/* loaded from: classes3.dex */
class StringsKt__IndentKt extends StringsKt__AppendableKt {
    public static /* synthetic */ String trimMargin$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "|";
        }
        return StringsKt.trimMargin(str, str2);
    }

    public static final String trimMargin(String $this$trimMargin, String marginPrefix) {
        Intrinsics.checkParameterIsNotNull($this$trimMargin, "$this$trimMargin");
        Intrinsics.checkParameterIsNotNull(marginPrefix, "marginPrefix");
        return StringsKt.replaceIndentByMargin($this$trimMargin, "", marginPrefix);
    }

    public static /* synthetic */ String replaceIndentByMargin$default(String str, String str2, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "";
        }
        if ((i & 2) != 0) {
            str3 = "|";
        }
        return StringsKt.replaceIndentByMargin(str, str2, str3);
    }

    /* JADX INFO: Multiple debug info for r1v16 'index$iv$iv$iv$iv'  int: [D('index$iv$iv$iv$iv' int), D('index$iv$iv$iv' int)] */
    public static final String replaceIndentByMargin(String $this$replaceIndentByMargin, String newIndent, String marginPrefix) {
        Collection destination$iv$iv$iv;
        String str;
        String invoke;
        Intrinsics.checkParameterIsNotNull($this$replaceIndentByMargin, "$this$replaceIndentByMargin");
        Intrinsics.checkParameterIsNotNull(newIndent, "newIndent");
        Intrinsics.checkParameterIsNotNull(marginPrefix, "marginPrefix");
        if (!StringsKt.isBlank(marginPrefix)) {
            List lines = StringsKt.lines($this$replaceIndentByMargin);
            int resultSizeEstimate$iv = $this$replaceIndentByMargin.length() + (newIndent.length() * lines.size());
            Function1 indentAddFunction$iv = getIndentFunction$StringsKt__IndentKt(newIndent);
            int lastIndex$iv = CollectionsKt.getLastIndex(lines);
            Collection destination$iv$iv$iv2 = new ArrayList();
            int index$iv$iv$iv$iv = 0;
            for (Object item$iv$iv$iv$iv : lines) {
                int index$iv$iv$iv$iv2 = index$iv$iv$iv$iv + 1;
                if (index$iv$iv$iv$iv < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                String value$iv = (String) item$iv$iv$iv$iv;
                String str2 = null;
                if ((index$iv$iv$iv$iv == 0 || index$iv$iv$iv$iv == lastIndex$iv) && StringsKt.isBlank(value$iv)) {
                    destination$iv$iv$iv = destination$iv$iv$iv2;
                } else {
                    String $this$indexOfFirst$iv = value$iv;
                    int $i$f$indexOfFirst = 0;
                    int length = $this$indexOfFirst$iv.length();
                    int index$iv = 0;
                    while (true) {
                        if (index$iv >= length) {
                            index$iv = -1;
                            break;
                        } else if (!CharsKt.isWhitespace($this$indexOfFirst$iv.charAt(index$iv))) {
                            break;
                        } else {
                            index$iv++;
                            $i$f$indexOfFirst = $i$f$indexOfFirst;
                        }
                    }
                    if (index$iv == -1) {
                        destination$iv$iv$iv = destination$iv$iv$iv2;
                        str = null;
                    } else {
                        destination$iv$iv$iv = destination$iv$iv$iv2;
                        if (StringsKt.startsWith$default(value$iv, marginPrefix, index$iv, false, 4, (Object) null)) {
                            int length2 = marginPrefix.length() + index$iv;
                            if (value$iv != null) {
                                str = value$iv.substring(length2);
                                Intrinsics.checkExpressionValueIsNotNull(str, "(this as java.lang.String).substring(startIndex)");
                            } else {
                                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                            }
                        } else {
                            str = null;
                        }
                    }
                    str2 = (str == null || (invoke = indentAddFunction$iv.invoke(str)) == null) ? value$iv : invoke;
                }
                if (str2 != null) {
                    destination$iv$iv$iv.add(str2);
                }
                destination$iv$iv$iv2 = destination$iv$iv$iv;
                index$iv$iv$iv$iv = index$iv$iv$iv$iv2;
            }
            String sb = ((StringBuilder) CollectionsKt.joinTo$default((List) destination$iv$iv$iv2, new StringBuilder(resultSizeEstimate$iv), IOUtils.LINE_SEPARATOR_UNIX, null, null, 0, null, null, 124, null)).toString();
            Intrinsics.checkExpressionValueIsNotNull(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
            return sb;
        }
        throw new IllegalArgumentException("marginPrefix must be non-blank string.".toString());
    }

    public static final String trimIndent(String $this$trimIndent) {
        Intrinsics.checkParameterIsNotNull($this$trimIndent, "$this$trimIndent");
        return StringsKt.replaceIndent($this$trimIndent, "");
    }

    public static /* synthetic */ String replaceIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "";
        }
        return StringsKt.replaceIndent(str, str2);
    }

    /* JADX INFO: Multiple debug info for r16v1 'index$iv$iv$iv$iv'  int: [D('index$iv$iv$iv$iv' int), D('index$iv$iv$iv' int)] */
    /* JADX INFO: Multiple debug info for r3v4 java.util.ArrayList: [D('$this$filter$iv' java.lang.Iterable), D('$this$map$iv' java.lang.Iterable)] */
    public static final String replaceIndent(String $this$replaceIndent, String newIndent) {
        String str;
        Intrinsics.checkParameterIsNotNull($this$replaceIndent, "$this$replaceIndent");
        Intrinsics.checkParameterIsNotNull(newIndent, "newIndent");
        List lines = StringsKt.lines($this$replaceIndent);
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : lines) {
            if (!StringsKt.isBlank((String) element$iv$iv)) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        Iterable<String> $this$map$iv = (List) destination$iv$iv;
        Collection destination$iv$iv2 = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (String p1 : $this$map$iv) {
            destination$iv$iv2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(p1)));
        }
        Integer num = (Integer) CollectionsKt.min((Iterable<? extends Comparable>) ((List) destination$iv$iv2));
        int minCommonIndent = num != null ? num.intValue() : 0;
        int resultSizeEstimate$iv = $this$replaceIndent.length() + (newIndent.length() * lines.size());
        Function1 indentAddFunction$iv = getIndentFunction$StringsKt__IndentKt(newIndent);
        int lastIndex$iv = CollectionsKt.getLastIndex(lines);
        Collection destination$iv$iv$iv = new ArrayList();
        int index$iv$iv$iv$iv = 0;
        for (Object item$iv$iv$iv$iv : lines) {
            int index$iv$iv$iv$iv2 = index$iv$iv$iv$iv + 1;
            if (index$iv$iv$iv$iv < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String value$iv = (String) item$iv$iv$iv$iv;
            if ((index$iv$iv$iv$iv == 0 || index$iv$iv$iv$iv == lastIndex$iv) && StringsKt.isBlank(value$iv)) {
                str = null;
            } else {
                String line = StringsKt.drop(value$iv, minCommonIndent);
                if (line == null || (str = indentAddFunction$iv.invoke(line)) == null) {
                    str = value$iv;
                }
            }
            if (str != null) {
                destination$iv$iv$iv.add(str);
            }
            index$iv$iv$iv$iv = index$iv$iv$iv$iv2;
        }
        String sb = ((StringBuilder) CollectionsKt.joinTo$default((List) destination$iv$iv$iv, new StringBuilder(resultSizeEstimate$iv), IOUtils.LINE_SEPARATOR_UNIX, null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb;
    }

    public static /* synthetic */ String prependIndent$default(String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str2 = "    ";
        }
        return StringsKt.prependIndent(str, str2);
    }

    public static final String prependIndent(String $this$prependIndent, String indent) {
        Intrinsics.checkParameterIsNotNull($this$prependIndent, "$this$prependIndent");
        Intrinsics.checkParameterIsNotNull(indent, "indent");
        return SequencesKt.joinToString$default(SequencesKt.map(StringsKt.lineSequence($this$prependIndent), new Function1<String, String>(indent) { // from class: kotlin.text.StringsKt__IndentKt$prependIndent$1
            final /* synthetic */ String $indent;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$indent = r1;
            }

            public final String invoke(String it) {
                Intrinsics.checkParameterIsNotNull(it, "it");
                if (!StringsKt.isBlank(it)) {
                    return this.$indent + it;
                } else if (it.length() < this.$indent.length()) {
                    return this.$indent;
                } else {
                    return it;
                }
            }
        }), IOUtils.LINE_SEPARATOR_UNIX, null, null, 0, null, null, 62, null);
    }

    private static final int indentWidth$StringsKt__IndentKt(String $this$indentWidth) {
        String $this$indexOfFirst$iv = $this$indentWidth;
        int length = $this$indexOfFirst$iv.length();
        int index$iv = 0;
        while (true) {
            if (index$iv >= length) {
                index$iv = -1;
                break;
            } else if (!CharsKt.isWhitespace($this$indexOfFirst$iv.charAt(index$iv))) {
                break;
            } else {
                index$iv++;
            }
        }
        return index$iv == -1 ? $this$indentWidth.length() : index$iv;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String indent) {
        if (indent.length() == 0) {
            return StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        }
        return new Function1<String, String>(indent) { // from class: kotlin.text.StringsKt__IndentKt$getIndentFunction$2
            final /* synthetic */ String $indent;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.$indent = r1;
            }

            public final String invoke(String line) {
                Intrinsics.checkParameterIsNotNull(line, "line");
                return this.$indent + line;
            }
        };
    }

    /* JADX INFO: Multiple debug info for r9v1 'index$iv$iv$iv'  int: [D('index$iv$iv$iv' int), D('index$iv$iv' int)] */
    private static final String reindent$StringsKt__IndentKt(List<String> $this$mapIndexedNotNull$iv, int resultSizeEstimate, Function1<? super String, String> function1, Function1<? super String, String> function12) {
        int lastIndex;
        int $i$f$reindent$StringsKt__IndentKt = 0;
        int lastIndex2 = CollectionsKt.getLastIndex($this$mapIndexedNotNull$iv);
        Collection destination$iv$iv = new ArrayList();
        int index$iv$iv = 0;
        for (Object item$iv$iv$iv : $this$mapIndexedNotNull$iv) {
            int index$iv$iv$iv = index$iv$iv + 1;
            if (index$iv$iv < 0) {
                if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                    CollectionsKt.throwIndexOverflow();
                } else {
                    throw new ArithmeticException("Index overflow has happened.");
                }
            }
            String value = (String) item$iv$iv$iv;
            if ((index$iv$iv == 0 || index$iv$iv == lastIndex2) && StringsKt.isBlank(value)) {
                value = null;
                lastIndex = lastIndex2;
            } else {
                String invoke = function12.invoke(value);
                if (invoke != null) {
                    lastIndex = lastIndex2;
                    String invoke2 = function1.invoke(invoke);
                    if (invoke2 != null) {
                        value = invoke2;
                    }
                } else {
                    lastIndex = lastIndex2;
                }
            }
            if (value != null) {
                destination$iv$iv.add(value);
            }
            index$iv$iv = index$iv$iv$iv;
            $i$f$reindent$StringsKt__IndentKt = $i$f$reindent$StringsKt__IndentKt;
            lastIndex2 = lastIndex;
        }
        String sb = ((StringBuilder) CollectionsKt.joinTo$default((List) destination$iv$iv, new StringBuilder(resultSizeEstimate), IOUtils.LINE_SEPARATOR_UNIX, null, null, 0, null, null, 124, null)).toString();
        Intrinsics.checkExpressionValueIsNotNull(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb;
    }
}
