package com.facebook.cache.disk;

import com.facebook.cache.disk.DiskStorage;
/* loaded from: classes.dex */
public class ScoreBasedEvictionComparatorSupplier implements EntryEvictionComparatorSupplier {
    private final float mAgeWeight;
    private final float mSizeWeight;

    public ScoreBasedEvictionComparatorSupplier(float ageWeight, float sizeWeight) {
        this.mAgeWeight = ageWeight;
        this.mSizeWeight = sizeWeight;
    }

    @Override // com.facebook.cache.disk.EntryEvictionComparatorSupplier
    public EntryEvictionComparator get() {
        return new EntryEvictionComparator() { // from class: com.facebook.cache.disk.ScoreBasedEvictionComparatorSupplier.1
            long now = System.currentTimeMillis();

            public int compare(DiskStorage.Entry lhs, DiskStorage.Entry rhs) {
                float score1 = ScoreBasedEvictionComparatorSupplier.this.calculateScore(lhs, this.now);
                float score2 = ScoreBasedEvictionComparatorSupplier.this.calculateScore(rhs, this.now);
                if (score1 < score2) {
                    return 1;
                }
                return score2 == score1 ? 0 : -1;
            }
        };
    }

    float calculateScore(DiskStorage.Entry entry, long now) {
        return (this.mAgeWeight * ((float) (now - entry.getTimestamp()))) + (this.mSizeWeight * ((float) entry.getSize()));
    }
}
