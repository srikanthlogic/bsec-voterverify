package com.google.common.reflect;

import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class Element extends AccessibleObject implements Member {
    private final AccessibleObject accessibleObject;
    private final Member member;

    /* JADX INFO: Access modifiers changed from: package-private */
    public <M extends AccessibleObject & Member> Element(M member) {
        Preconditions.checkNotNull(member);
        this.accessibleObject = member;
        this.member = member;
    }

    public TypeToken<?> getOwnerType() {
        return TypeToken.of((Class) getDeclaringClass());
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return this.accessibleObject.isAnnotationPresent(annotationClass);
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        return (A) this.accessibleObject.getAnnotation(annotationClass);
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public final Annotation[] getAnnotations() {
        return this.accessibleObject.getAnnotations();
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public final Annotation[] getDeclaredAnnotations() {
        return this.accessibleObject.getDeclaredAnnotations();
    }

    @Override // java.lang.reflect.AccessibleObject
    public final void setAccessible(boolean flag) throws SecurityException {
        this.accessibleObject.setAccessible(flag);
    }

    @Override // java.lang.reflect.AccessibleObject
    public final boolean isAccessible() {
        return this.accessibleObject.isAccessible();
    }

    @Override // java.lang.reflect.Member
    public Class<?> getDeclaringClass() {
        return this.member.getDeclaringClass();
    }

    @Override // java.lang.reflect.Member
    public final String getName() {
        return this.member.getName();
    }

    @Override // java.lang.reflect.Member
    public final int getModifiers() {
        return this.member.getModifiers();
    }

    @Override // java.lang.reflect.Member
    public final boolean isSynthetic() {
        return this.member.isSynthetic();
    }

    public final boolean isPublic() {
        return Modifier.isPublic(getModifiers());
    }

    public final boolean isProtected() {
        return Modifier.isProtected(getModifiers());
    }

    public final boolean isPackagePrivate() {
        return !isPrivate() && !isPublic() && !isProtected();
    }

    public final boolean isPrivate() {
        return Modifier.isPrivate(getModifiers());
    }

    public final boolean isStatic() {
        return Modifier.isStatic(getModifiers());
    }

    public final boolean isFinal() {
        return Modifier.isFinal(getModifiers());
    }

    public final boolean isAbstract() {
        return Modifier.isAbstract(getModifiers());
    }

    public final boolean isNative() {
        return Modifier.isNative(getModifiers());
    }

    public final boolean isSynchronized() {
        return Modifier.isSynchronized(getModifiers());
    }

    final boolean isVolatile() {
        return Modifier.isVolatile(getModifiers());
    }

    final boolean isTransient() {
        return Modifier.isTransient(getModifiers());
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object obj) {
        if (!(obj instanceof Element)) {
            return false;
        }
        Element that = (Element) obj;
        if (!getOwnerType().equals(that.getOwnerType()) || !this.member.equals(that.member)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.member.hashCode();
    }

    @Override // java.lang.Object
    public String toString() {
        return this.member.toString();
    }
}
