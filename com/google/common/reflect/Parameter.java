package com.google.common.reflect;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Parameter implements AnnotatedElement {
    private final ImmutableList<Annotation> annotations;
    private final Invokable<?, ?> declaration;
    private final int position;
    private final TypeToken<?> type;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations) {
        this.declaration = declaration;
        this.position = position;
        this.type = type;
        this.annotations = ImmutableList.copyOf(annotations);
    }

    public TypeToken<?> getType() {
        return this.type;
    }

    public Invokable<?, ?> getDeclaringInvokable() {
        return this.declaration;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
        return getAnnotation(annotationType) != null;
    }

    @Override // java.lang.reflect.AnnotatedElement
    @NullableDecl
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        Preconditions.checkNotNull(annotationType);
        UnmodifiableIterator<Annotation> it = this.annotations.iterator();
        while (it.hasNext()) {
            Annotation annotation = it.next();
            if (annotationType.isInstance(annotation)) {
                return annotationType.cast(annotation);
            }
        }
        return null;
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
        return (A[]) getDeclaredAnnotationsByType(annotationType);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return (Annotation[]) this.annotations.toArray(new Annotation[0]);
    }

    @Override // java.lang.reflect.AnnotatedElement
    @NullableDecl
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
        Preconditions.checkNotNull(annotationType);
        return (A) ((Annotation) FluentIterable.from(this.annotations).filter(annotationType).first().orNull());
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
        return (A[]) ((Annotation[]) FluentIterable.from(this.annotations).filter(annotationType).toArray(annotationType));
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object obj) {
        if (!(obj instanceof Parameter)) {
            return false;
        }
        Parameter that = (Parameter) obj;
        if (this.position != that.position || !this.declaration.equals(that.declaration)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.position;
    }

    @Override // java.lang.Object
    public String toString() {
        return this.type + " arg" + this.position;
    }
}
