package com.google.common.graph;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.errorprone.annotations.Immutable;
import java.util.Comparator;
import java.util.Map;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable
/* loaded from: classes.dex */
public final class ElementOrder<T> {
    @NullableDecl
    private final Comparator<T> comparator;
    private final Type type;

    /* loaded from: classes.dex */
    public enum Type {
        UNORDERED,
        STABLE,
        INSERTION,
        SORTED
    }

    private ElementOrder(Type type, @NullableDecl Comparator<T> comparator) {
        this.type = (Type) Preconditions.checkNotNull(type);
        this.comparator = comparator;
        Preconditions.checkState((type == Type.SORTED) != (comparator != null) ? false : true);
    }

    public static <S> ElementOrder<S> unordered() {
        return new ElementOrder<>(Type.UNORDERED, null);
    }

    public static <S> ElementOrder<S> stable() {
        return new ElementOrder<>(Type.STABLE, null);
    }

    public static <S> ElementOrder<S> insertion() {
        return new ElementOrder<>(Type.INSERTION, null);
    }

    public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
        return new ElementOrder<>(Type.SORTED, Ordering.natural());
    }

    public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
        return new ElementOrder<>(Type.SORTED, (Comparator) Preconditions.checkNotNull(comparator));
    }

    public Type type() {
        return this.type;
    }

    public Comparator<T> comparator() {
        Comparator<T> comparator = this.comparator;
        if (comparator != null) {
            return comparator;
        }
        throw new UnsupportedOperationException("This ordering does not define a comparator.");
    }

    public boolean equals(@NullableDecl Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ElementOrder)) {
            return false;
        }
        ElementOrder<?> other = (ElementOrder) obj;
        if (this.type != other.type || !Objects.equal(this.comparator, other.comparator)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hashCode(this.type, this.comparator);
    }

    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
        Comparator<T> comparator = this.comparator;
        if (comparator != null) {
            helper.add("comparator", comparator);
        }
        return helper.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.common.graph.ElementOrder$1 */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$graph$ElementOrder$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[Type.UNORDERED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[Type.INSERTION.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[Type.STABLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$google$common$graph$ElementOrder$Type[Type.SORTED.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public <K extends T, V> Map<K, V> createMap(int expectedSize) {
        int i = AnonymousClass1.$SwitchMap$com$google$common$graph$ElementOrder$Type[this.type.ordinal()];
        if (i == 1) {
            return Maps.newHashMapWithExpectedSize(expectedSize);
        }
        if (i == 2 || i == 3) {
            return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
        }
        if (i == 4) {
            return Maps.newTreeMap(comparator());
        }
        throw new AssertionError();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T1 extends T> ElementOrder<T1> cast() {
        return this;
    }
}
