package com.google.common.net;

import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;
import com.google.thirdparty.publicsuffix.PublicSuffixPatterns;
import com.google.thirdparty.publicsuffix.PublicSuffixType;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable
/* loaded from: classes3.dex */
public final class InternetDomainName {
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_PARTS = 127;
    private static final int NO_SUFFIX_FOUND = -1;
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;
    private final int registrySuffixIndex;
    private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
    private static final Splitter DOT_SPLITTER = Splitter.on((char) FilenameUtils.EXTENSION_SEPARATOR);
    private static final Joiner DOT_JOINER = Joiner.on((char) FilenameUtils.EXTENSION_SEPARATOR);
    private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
    private static final CharMatcher DIGIT_MATCHER = CharMatcher.inRange('0', '9');
    private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('a', 'z').or(CharMatcher.inRange('A', 'Z'));
    private static final CharMatcher PART_CHAR_MATCHER = DIGIT_MATCHER.or(LETTER_MATCHER).or(DASH_MATCHER);

    InternetDomainName(String name) {
        String name2 = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, FilenameUtils.EXTENSION_SEPARATOR));
        boolean z = true;
        name2 = name2.endsWith(".") ? name2.substring(0, name2.length() - 1) : name2;
        Preconditions.checkArgument(name2.length() <= MAX_LENGTH, "Domain name too long: '%s':", name2);
        this.name = name2;
        this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name2));
        Preconditions.checkArgument(this.parts.size() > 127 ? false : z, "Domain has too many parts: '%s'", name2);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", name2);
        this.publicSuffixIndex = findSuffixOfType(Optional.absent());
        this.registrySuffixIndex = findSuffixOfType(Optional.of(PublicSuffixType.REGISTRY));
    }

    private int findSuffixOfType(Optional<PublicSuffixType> desiredType) {
        int partsSize = this.parts.size();
        for (int i = 0; i < partsSize; i++) {
            String ancestorName = DOT_JOINER.join(this.parts.subList(i, partsSize));
            if (matchesType(desiredType, Optional.fromNullable(PublicSuffixPatterns.EXACT.get(ancestorName)))) {
                return i;
            }
            if (PublicSuffixPatterns.EXCLUDED.containsKey(ancestorName)) {
                return i + 1;
            }
            if (matchesWildcardSuffixType(desiredType, ancestorName)) {
                return i;
            }
        }
        return -1;
    }

    public static InternetDomainName from(String domain) {
        return new InternetDomainName((String) Preconditions.checkNotNull(domain));
    }

    private static boolean validateSyntax(List<String> parts) {
        int lastIndex = parts.size() - 1;
        if (!validatePart(parts.get(lastIndex), true)) {
            return false;
        }
        for (int i = 0; i < lastIndex; i++) {
            if (!validatePart(parts.get(i), false)) {
                return false;
            }
        }
        return true;
    }

    private static boolean validatePart(String part, boolean isFinalPart) {
        if (part.length() < 1 || part.length() > 63) {
            return false;
        }
        if (PART_CHAR_MATCHER.matchesAllOf(CharMatcher.ascii().retainFrom(part)) && !DASH_MATCHER.matches(part.charAt(0)) && !DASH_MATCHER.matches(part.charAt(part.length() - 1))) {
            return !isFinalPart || !DIGIT_MATCHER.matches(part.charAt(0));
        }
        return false;
    }

    public ImmutableList<String> parts() {
        return this.parts;
    }

    public boolean isPublicSuffix() {
        return this.publicSuffixIndex == 0;
    }

    public boolean hasPublicSuffix() {
        return this.publicSuffixIndex != -1;
    }

    public InternetDomainName publicSuffix() {
        if (hasPublicSuffix()) {
            return ancestor(this.publicSuffixIndex);
        }
        return null;
    }

    public boolean isUnderPublicSuffix() {
        return this.publicSuffixIndex > 0;
    }

    public boolean isTopPrivateDomain() {
        return this.publicSuffixIndex == 1;
    }

    public InternetDomainName topPrivateDomain() {
        if (isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return ancestor(this.publicSuffixIndex - 1);
    }

    public boolean isRegistrySuffix() {
        return this.registrySuffixIndex == 0;
    }

    public boolean hasRegistrySuffix() {
        return this.registrySuffixIndex != -1;
    }

    public InternetDomainName registrySuffix() {
        if (hasRegistrySuffix()) {
            return ancestor(this.registrySuffixIndex);
        }
        return null;
    }

    public boolean isUnderRegistrySuffix() {
        return this.registrySuffixIndex > 0;
    }

    public boolean isTopDomainUnderRegistrySuffix() {
        return this.registrySuffixIndex == 1;
    }

    public InternetDomainName topDomainUnderRegistrySuffix() {
        if (isTopDomainUnderRegistrySuffix()) {
            return this;
        }
        Preconditions.checkState(isUnderRegistrySuffix(), "Not under a registry suffix: %s", this.name);
        return ancestor(this.registrySuffixIndex - 1);
    }

    public boolean hasParent() {
        return this.parts.size() > 1;
    }

    public InternetDomainName parent() {
        Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
        return ancestor(1);
    }

    private InternetDomainName ancestor(int levels) {
        Joiner joiner = DOT_JOINER;
        ImmutableList<String> immutableList = this.parts;
        return from(joiner.join(immutableList.subList(levels, immutableList.size())));
    }

    public InternetDomainName child(String leftParts) {
        return from(((String) Preconditions.checkNotNull(leftParts)) + "." + this.name);
    }

    public static boolean isValid(String name) {
        try {
            from(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean matchesWildcardSuffixType(Optional<PublicSuffixType> desiredType, String domain) {
        List<String> pieces = DOT_SPLITTER.limit(2).splitToList(domain);
        if (pieces.size() != 2 || !matchesType(desiredType, Optional.fromNullable(PublicSuffixPatterns.UNDER.get(pieces.get(1))))) {
            return false;
        }
        return true;
    }

    private static boolean matchesType(Optional<PublicSuffixType> desiredType, Optional<PublicSuffixType> actualType) {
        return desiredType.isPresent() ? desiredType.equals(actualType) : actualType.isPresent();
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(@NullableDecl Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof InternetDomainName) {
            return this.name.equals(((InternetDomainName) object).name);
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
