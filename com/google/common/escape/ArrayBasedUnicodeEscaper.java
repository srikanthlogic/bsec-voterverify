package com.google.common.escape;

import com.google.common.base.Preconditions;
import java.util.Map;
import kotlin.jvm.internal.CharCompanionObject;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public abstract class ArrayBasedUnicodeEscaper extends UnicodeEscaper {
    private final char[][] replacements;
    private final int replacementsLength;
    private final int safeMax;
    private final char safeMaxChar;
    private final int safeMin;
    private final char safeMinChar;

    protected abstract char[] escapeUnsafe(int i);

    protected ArrayBasedUnicodeEscaper(Map<Character, String> replacementMap, int safeMin, int safeMax, @NullableDecl String unsafeReplacement) {
        this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax, unsafeReplacement);
    }

    protected ArrayBasedUnicodeEscaper(ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @NullableDecl String unsafeReplacement) {
        Preconditions.checkNotNull(escaperMap);
        this.replacements = escaperMap.getReplacementArray();
        this.replacementsLength = this.replacements.length;
        if (safeMax < safeMin) {
            safeMax = -1;
            safeMin = Integer.MAX_VALUE;
        }
        this.safeMin = safeMin;
        this.safeMax = safeMax;
        if (safeMin >= 55296) {
            this.safeMinChar = CharCompanionObject.MAX_VALUE;
            this.safeMaxChar = 0;
            return;
        }
        this.safeMinChar = (char) safeMin;
        this.safeMaxChar = (char) Math.min(safeMax, 55295);
    }

    @Override // com.google.common.escape.UnicodeEscaper, com.google.common.escape.Escaper
    public final String escape(String s) {
        Preconditions.checkNotNull(s);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar) {
                return escapeSlow(s, i);
            }
        }
        return s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.common.escape.UnicodeEscaper
    public final char[] escape(int cp) {
        char[] chars;
        if (cp < this.replacementsLength && (chars = this.replacements[cp]) != null) {
            return chars;
        }
        if (cp < this.safeMin || cp > this.safeMax) {
            return escapeUnsafe(cp);
        }
        return null;
    }

    @Override // com.google.common.escape.UnicodeEscaper
    protected final int nextEscapeIndex(CharSequence csq, int index, int end) {
        while (index < end) {
            char c = csq.charAt(index);
            if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMaxChar || c < this.safeMinChar) {
                break;
            }
            index++;
        }
        return index;
    }
}
