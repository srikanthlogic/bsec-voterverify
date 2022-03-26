package com.facebook.imagepipeline.memory;

import com.facebook.common.references.OOMSoftReference;
import java.util.LinkedList;
/* loaded from: classes.dex */
class OOMSoftReferenceBucket<V> extends Bucket<V> {
    private LinkedList<OOMSoftReference<V>> mSpareReferences = new LinkedList<>();

    public OOMSoftReferenceBucket(int itemSize, int maxLength, int inUseLength) {
        super(itemSize, maxLength, inUseLength, false);
    }

    @Override // com.facebook.imagepipeline.memory.Bucket
    public V pop() {
        OOMSoftReference<V> ref = (OOMSoftReference) this.mFreeList.poll();
        V value = ref.get();
        ref.clear();
        this.mSpareReferences.add(ref);
        return value;
    }

    @Override // com.facebook.imagepipeline.memory.Bucket
    void addToFreeList(V value) {
        OOMSoftReference<V> ref = this.mSpareReferences.poll();
        if (ref == null) {
            ref = new OOMSoftReference<>();
        }
        ref.set(value);
        this.mFreeList.add(ref);
    }
}
