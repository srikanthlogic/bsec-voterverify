package com.facebook.imagepipeline.memory;

import android.util.SparseArray;
import java.util.LinkedList;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class BucketMap<T> {
    @Nullable
    LinkedEntry<T> mHead;
    protected final SparseArray<LinkedEntry<T>> mMap = new SparseArray<>();
    @Nullable
    LinkedEntry<T> mTail;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LinkedEntry<I> {
        int key;
        @Nullable
        LinkedEntry<I> next;
        @Nullable
        LinkedEntry<I> prev;
        LinkedList<I> value;

        private LinkedEntry(@Nullable LinkedEntry<I> prev, int key, LinkedList<I> value, @Nullable LinkedEntry<I> next) {
            this.prev = prev;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public String toString() {
            return "LinkedEntry(key: " + this.key + ")";
        }
    }

    @Nullable
    public synchronized T acquire(int key) {
        LinkedEntry<T> bucket = this.mMap.get(key);
        if (bucket == null) {
            return null;
        }
        T result = bucket.value.pollFirst();
        moveToFront(bucket);
        return result;
    }

    public synchronized void release(int key, T value) {
        LinkedEntry<T> bucket = this.mMap.get(key);
        if (bucket == null) {
            bucket = new LinkedEntry<>(null, key, new LinkedList(), null);
            this.mMap.put(key, bucket);
        }
        bucket.value.addLast(value);
        moveToFront(bucket);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int valueCount() {
        int count;
        count = 0;
        LinkedEntry entry = this.mHead;
        while (entry != null) {
            if (entry.value != null) {
                count += entry.value.size();
            }
            entry = entry.next;
        }
        return count;
    }

    private synchronized void prune(LinkedEntry<T> bucket) {
        LinkedEntry linkedEntry = (LinkedEntry<T>) bucket.prev;
        LinkedEntry linkedEntry2 = (LinkedEntry<T>) bucket.next;
        if (linkedEntry != null) {
            linkedEntry.next = linkedEntry2;
        }
        if (linkedEntry2 != null) {
            linkedEntry2.prev = linkedEntry;
        }
        bucket.prev = null;
        bucket.next = null;
        if (bucket == this.mHead) {
            this.mHead = linkedEntry2;
        }
        if (bucket == this.mTail) {
            this.mTail = linkedEntry;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void moveToFront(LinkedEntry<T> bucket) {
        if (this.mHead != bucket) {
            prune(bucket);
            LinkedEntry linkedEntry = (LinkedEntry<T>) this.mHead;
            if (linkedEntry == null) {
                this.mHead = bucket;
                this.mTail = bucket;
                return;
            }
            bucket.next = linkedEntry;
            linkedEntry.prev = bucket;
            this.mHead = bucket;
        }
    }

    @Nullable
    public synchronized T removeFromEnd() {
        LinkedEntry<T> last = this.mTail;
        if (last == null) {
            return null;
        }
        T value = last.value.pollLast();
        maybePrune(last);
        return value;
    }

    private void maybePrune(LinkedEntry<T> bucket) {
        if (bucket != null && bucket.value.isEmpty()) {
            prune(bucket);
            this.mMap.remove(bucket.key);
        }
    }
}
