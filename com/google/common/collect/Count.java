package com.google.common.collect;

import java.io.Serializable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes.dex */
final class Count implements Serializable {
    private int value;

    Count(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }

    public void add(int delta) {
        this.value += delta;
    }

    public int addAndGet(int delta) {
        int i = this.value + delta;
        this.value = i;
        return i;
    }

    public void set(int newValue) {
        this.value = newValue;
    }

    public int getAndSet(int newValue) {
        int result = this.value;
        this.value = newValue;
        return result;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.value;
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object obj) {
        return (obj instanceof Count) && ((Count) obj).value == this.value;
    }

    @Override // java.lang.Object
    public String toString() {
        return Integer.toString(this.value);
    }
}
