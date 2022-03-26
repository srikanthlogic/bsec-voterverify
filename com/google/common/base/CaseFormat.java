package com.google.common.base;

import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public enum CaseFormat {
    LOWER_HYPHEN(CharMatcher.is('-'), "-") {
        @Override // com.google.common.base.CaseFormat
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }

        @Override // com.google.common.base.CaseFormat
        String convert(CaseFormat format, String s) {
            if (format == LOWER_UNDERSCORE) {
                return s.replace('-', '_');
            }
            if (format == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s.replace('-', '_'));
            }
            return CaseFormat.super.convert(format, s);
        }
    },
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override // com.google.common.base.CaseFormat
        String normalizeWord(String word) {
            return Ascii.toLowerCase(word);
        }

        @Override // com.google.common.base.CaseFormat
        String convert(CaseFormat format, String s) {
            if (format == LOWER_HYPHEN) {
                return s.replace('_', '-');
            }
            if (format == UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s);
            }
            return CaseFormat.super.convert(format, s);
        }
    },
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override // com.google.common.base.CaseFormat
        String normalizeWord(String word) {
            return CaseFormat.firstCharOnlyToUpper(word);
        }

        @Override // com.google.common.base.CaseFormat
        String normalizeFirstWord(String word) {
            return Ascii.toLowerCase(word);
        }
    },
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override // com.google.common.base.CaseFormat
        String normalizeWord(String word) {
            return CaseFormat.firstCharOnlyToUpper(word);
        }
    },
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override // com.google.common.base.CaseFormat
        String normalizeWord(String word) {
            return Ascii.toUpperCase(word);
        }

        @Override // com.google.common.base.CaseFormat
        String convert(CaseFormat format, String s) {
            if (format == LOWER_HYPHEN) {
                return Ascii.toLowerCase(s.replace('_', '-'));
            }
            if (format == LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(s);
            }
            return CaseFormat.super.convert(format, s);
        }
    };
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    abstract String normalizeWord(String str);

    CaseFormat(CharMatcher wordBoundary, String wordSeparator) {
        this.wordBoundary = wordBoundary;
        this.wordSeparator = wordSeparator;
    }

    public final String to(CaseFormat format, String str) {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(str);
        return format == this ? str : convert(format, str);
    }

    String convert(CaseFormat format, String s) {
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while (true) {
            int indexIn = this.wordBoundary.indexIn(s, j + 1);
            j = indexIn;
            if (indexIn == -1) {
                break;
            }
            if (i == 0) {
                out = new StringBuilder(s.length() + (format.wordSeparator.length() * 4));
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            } else {
                out.append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + this.wordSeparator.length();
        }
        if (i == 0) {
            return format.normalizeFirstWord(s);
        }
        out.append(format.normalizeWord(s.substring(i)));
        return out.toString();
    }

    public Converter<String, String> converterTo(CaseFormat targetFormat) {
        return new StringConverter(this, targetFormat);
    }

    /* loaded from: classes.dex */
    private static final class StringConverter extends Converter<String, String> implements Serializable {
        private static final long serialVersionUID = 0;
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;

        StringConverter(CaseFormat sourceFormat, CaseFormat targetFormat) {
            this.sourceFormat = (CaseFormat) Preconditions.checkNotNull(sourceFormat);
            this.targetFormat = (CaseFormat) Preconditions.checkNotNull(targetFormat);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String doForward(String s) {
            return this.sourceFormat.to(this.targetFormat, s);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public String doBackward(String s) {
            return this.targetFormat.to(this.sourceFormat, s);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function, java.lang.Object
        public boolean equals(@NullableDecl Object object) {
            if (!(object instanceof StringConverter)) {
                return false;
            }
            StringConverter that = (StringConverter) object;
            if (!this.sourceFormat.equals(that.sourceFormat) || !this.targetFormat.equals(that.targetFormat)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
        }
    }

    String normalizeFirstWord(String word) {
        return normalizeWord(word);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String firstCharOnlyToUpper(String word) {
        if (word.isEmpty()) {
            return word;
        }
        return Ascii.toUpperCase(word.charAt(0)) + Ascii.toLowerCase(word.substring(1));
    }
}
