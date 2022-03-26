package com.google.common.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
public final class Predicates {
    private Predicates() {
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.withNarrowedType();
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.withNarrowedType();
    }

    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.withNarrowedType();
    }

    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return new NotPredicate(predicate);
    }

    public static <T> Predicate<T> and(Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate(defensiveCopy(components));
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<? super T>... components) {
        return new AndPredicate(defensiveCopy(components));
    }

    public static <T> Predicate<T> and(Predicate<? super T> first, Predicate<? super T> second) {
        return new AndPredicate(asList((Predicate) Preconditions.checkNotNull(first), (Predicate) Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> or(Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate(defensiveCopy(components));
    }

    @SafeVarargs
    public static <T> Predicate<T> or(Predicate<? super T>... components) {
        return new OrPredicate(defensiveCopy(components));
    }

    public static <T> Predicate<T> or(Predicate<? super T> first, Predicate<? super T> second) {
        return new OrPredicate(asList((Predicate) Preconditions.checkNotNull(first), (Predicate) Preconditions.checkNotNull(second)));
    }

    public static <T> Predicate<T> equalTo(@NullableDecl T target) {
        return target == null ? isNull() : new IsEqualToPredicate(target);
    }

    public static Predicate<Object> instanceOf(Class<?> clazz) {
        return new InstanceOfPredicate(clazz);
    }

    public static Predicate<Class<?>> subtypeOf(Class<?> clazz) {
        return new SubtypeOfPredicate(clazz);
    }

    public static <T> Predicate<T> in(Collection<? extends T> target) {
        return new InPredicate(target);
    }

    public static <A, B> Predicate<A> compose(Predicate<B> predicate, Function<A, ? extends B> function) {
        return new CompositionPredicate(predicate, function);
    }

    public static Predicate<CharSequence> containsPattern(String pattern) {
        return new ContainsPatternFromStringPredicate(pattern);
    }

    public static Predicate<CharSequence> contains(Pattern pattern) {
        return new ContainsPatternPredicate(new JdkPattern(pattern));
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes.dex */
    public static abstract class ObjectPredicate extends Enum<ObjectPredicate> implements Predicate<Object> {
        <T> Predicate<T> withNarrowedType() {
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class NotPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        final Predicate<T> predicate;

        NotPredicate(Predicate<T> predicate) {
            this.predicate = (Predicate) Preconditions.checkNotNull(predicate);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl T t) {
            return !this.predicate.apply(t);
        }

        @Override // java.lang.Object
        public int hashCode() {
            return ~this.predicate.hashCode();
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof NotPredicate) {
                return this.predicate.equals(((NotPredicate) obj).predicate);
            }
            return false;
        }

        @Override // java.lang.Object
        public String toString() {
            return "Predicates.not(" + this.predicate + ")";
        }
    }

    /* loaded from: classes.dex */
    public static class AndPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        private final List<? extends Predicate<? super T>> components;

        private AndPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl T t) {
            for (int i = 0; i < this.components.size(); i++) {
                if (!((Predicate) this.components.get(i)).apply(t)) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof AndPredicate) {
                return this.components.equals(((AndPredicate) obj).components);
            }
            return false;
        }

        @Override // java.lang.Object
        public String toString() {
            return Predicates.toStringHelper("and", this.components);
        }
    }

    /* loaded from: classes.dex */
    private static class OrPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        private final List<? extends Predicate<? super T>> components;

        private OrPredicate(List<? extends Predicate<? super T>> components) {
            this.components = components;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl T t) {
            for (int i = 0; i < this.components.size(); i++) {
                if (((Predicate) this.components.get(i)).apply(t)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof OrPredicate) {
                return this.components.equals(((OrPredicate) obj).components);
            }
            return false;
        }

        @Override // java.lang.Object
        public String toString() {
            return Predicates.toStringHelper("or", this.components);
        }
    }

    public static String toStringHelper(String methodName, Iterable<?> components) {
        StringBuilder sb = new StringBuilder("Predicates.");
        sb.append(methodName);
        StringBuilder builder = sb.append('(');
        boolean first = true;
        for (Object o : components) {
            if (!first) {
                builder.append(',');
            }
            builder.append(o);
            first = false;
        }
        builder.append(')');
        return builder.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class IsEqualToPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        private final T target;

        private IsEqualToPredicate(T target) {
            this.target = target;
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(T t) {
            return this.target.equals(t);
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.target.hashCode();
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                return this.target.equals(((IsEqualToPredicate) obj).target);
            }
            return false;
        }

        @Override // java.lang.Object
        public String toString() {
            return "Predicates.equalTo(" + this.target + ")";
        }
    }

    /* loaded from: classes.dex */
    public static class InstanceOfPredicate implements Predicate<Object>, Serializable {
        private static final long serialVersionUID;
        private final Class<?> clazz;

        private InstanceOfPredicate(Class<?> clazz) {
            this.clazz = (Class) Preconditions.checkNotNull(clazz);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl Object o) {
            return this.clazz.isInstance(o);
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (!(obj instanceof InstanceOfPredicate) || this.clazz != ((InstanceOfPredicate) obj).clazz) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public String toString() {
            return "Predicates.instanceOf(" + this.clazz.getName() + ")";
        }
    }

    /* loaded from: classes.dex */
    private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable {
        private static final long serialVersionUID;
        private final Class<?> clazz;

        private SubtypeOfPredicate(Class<?> clazz) {
            this.clazz = (Class) Preconditions.checkNotNull(clazz);
        }

        public boolean apply(Class<?> input) {
            return this.clazz.isAssignableFrom(input);
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.clazz.hashCode();
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (!(obj instanceof SubtypeOfPredicate) || this.clazz != ((SubtypeOfPredicate) obj).clazz) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public String toString() {
            return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InPredicate<T> implements Predicate<T>, Serializable {
        private static final long serialVersionUID;
        private final Collection<?> target;

        private InPredicate(Collection<?> target) {
            this.target = (Collection) Preconditions.checkNotNull(target);
        }

        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl T t) {
            try {
                return this.target.contains(t);
            } catch (ClassCastException | NullPointerException e) {
                return false;
            }
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (obj instanceof InPredicate) {
                return this.target.equals(((InPredicate) obj).target);
            }
            return false;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.target.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Predicates.in(" + this.target + ")";
        }
    }

    /* loaded from: classes.dex */
    public static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
        private static final long serialVersionUID;
        final Function<A, ? extends B> f;
        final Predicate<B> p;

        private CompositionPredicate(Predicate<B> p, Function<A, ? extends B> f) {
            this.p = (Predicate) Preconditions.checkNotNull(p);
            this.f = (Function) Preconditions.checkNotNull(f);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.common.base.Predicate
        public boolean apply(@NullableDecl A a2) {
            return this.p.apply(this.f.apply(a2));
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (!(obj instanceof CompositionPredicate)) {
                return false;
            }
            CompositionPredicate<?, ?> that = (CompositionPredicate) obj;
            if (!this.f.equals(that.f) || !this.p.equals(that.p)) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return this.p + "(" + this.f + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable {
        private static final long serialVersionUID;
        final CommonPattern pattern;

        ContainsPatternPredicate(CommonPattern pattern) {
            this.pattern = (CommonPattern) Preconditions.checkNotNull(pattern);
        }

        public boolean apply(CharSequence t) {
            return this.pattern.matcher(t).find();
        }

        @Override // java.lang.Object
        public int hashCode() {
            return Objects.hashCode(this.pattern.pattern(), Integer.valueOf(this.pattern.flags()));
        }

        @Override // com.google.common.base.Predicate, java.lang.Object
        public boolean equals(@NullableDecl Object obj) {
            if (!(obj instanceof ContainsPatternPredicate)) {
                return false;
            }
            ContainsPatternPredicate that = (ContainsPatternPredicate) obj;
            if (!Objects.equal(this.pattern.pattern(), that.pattern.pattern()) || this.pattern.flags() != that.pattern.flags()) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public String toString() {
            String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
            return "Predicates.contains(" + patternString + ")";
        }
    }

    /* loaded from: classes.dex */
    private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate {
        private static final long serialVersionUID;

        ContainsPatternFromStringPredicate(String string) {
            super(Platform.compilePattern(string));
        }

        @Override // com.google.common.base.Predicates.ContainsPatternPredicate, java.lang.Object
        public String toString() {
            return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
        }
    }

    private static <T> List<Predicate<? super T>> asList(Predicate<? super T> first, Predicate<? super T> second) {
        return Arrays.asList(first, second);
    }

    private static <T> List<T> defensiveCopy(T... array) {
        return defensiveCopy(Arrays.asList(array));
    }

    static <T> List<T> defensiveCopy(Iterable<T> iterable) {
        ArrayList arrayList = new ArrayList();
        for (T element : iterable) {
            arrayList.add(Preconditions.checkNotNull(element));
        }
        return arrayList;
    }
}
