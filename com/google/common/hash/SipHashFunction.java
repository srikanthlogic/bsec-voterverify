package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;
import java.nio.ByteBuffer;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@Immutable
/* loaded from: classes.dex */
final class SipHashFunction extends AbstractHashFunction implements Serializable {
    static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
    private static final long serialVersionUID = 0;
    private final int c;
    private final int d;
    private final long k0;
    private final long k1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SipHashFunction(int c, int d, long k0, long k1) {
        boolean z = true;
        Preconditions.checkArgument(c > 0, "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
        Preconditions.checkArgument(d <= 0 ? false : z, "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
        this.c = c;
        this.d = d;
        this.k0 = k0;
        this.k1 = k1;
    }

    @Override // com.google.common.hash.HashFunction
    public int bits() {
        return 64;
    }

    @Override // com.google.common.hash.HashFunction
    public Hasher newHasher() {
        return new SipHasher(this.c, this.d, this.k0, this.k1);
    }

    @Override // java.lang.Object
    public String toString() {
        return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
    }

    @Override // java.lang.Object
    public boolean equals(@NullableDecl Object object) {
        if (!(object instanceof SipHashFunction)) {
            return false;
        }
        SipHashFunction other = (SipHashFunction) object;
        if (this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1) {
            return true;
        }
        return false;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return (int) ((((long) ((getClass().hashCode() ^ this.c) ^ this.d)) ^ this.k0) ^ this.k1);
    }

    /* loaded from: classes.dex */
    private static final class SipHasher extends AbstractStreamingHasher {
        private static final int CHUNK_SIZE = 8;
        private final int c;
        private final int d;
        private long v0 = 8317987319222330741L;
        private long v1 = 7237128888997146477L;
        private long v2 = 7816392313619706465L;
        private long v3 = 8387220255154660723L;
        private long b = 0;
        private long finalM = 0;

        SipHasher(int c, int d, long k0, long k1) {
            super(8);
            this.c = c;
            this.d = d;
            this.v0 ^= k0;
            this.v1 ^= k1;
            this.v2 ^= k0;
            this.v3 ^= k1;
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void process(ByteBuffer buffer) {
            this.b += 8;
            processM(buffer.getLong());
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected void processRemaining(ByteBuffer buffer) {
            this.b += (long) buffer.remaining();
            int i = 0;
            while (buffer.hasRemaining()) {
                this.finalM ^= (((long) buffer.get()) & 255) << i;
                i += 8;
            }
        }

        @Override // com.google.common.hash.AbstractStreamingHasher
        protected HashCode makeHash() {
            this.finalM ^= this.b << 56;
            processM(this.finalM);
            this.v2 ^= 255;
            sipRound(this.d);
            return HashCode.fromLong(((this.v0 ^ this.v1) ^ this.v2) ^ this.v3);
        }

        private void processM(long m) {
            this.v3 ^= m;
            sipRound(this.c);
            this.v0 ^= m;
        }

        private void sipRound(int iterations) {
            for (int i = 0; i < iterations; i++) {
                long j = this.v0;
                long j2 = this.v1;
                this.v0 = j + j2;
                this.v2 += this.v3;
                this.v1 = Long.rotateLeft(j2, 13);
                this.v3 = Long.rotateLeft(this.v3, 16);
                long j3 = this.v1;
                long j4 = this.v0;
                this.v1 = j3 ^ j4;
                this.v3 ^= this.v2;
                this.v0 = Long.rotateLeft(j4, 32);
                long j5 = this.v2;
                long j6 = this.v1;
                this.v2 = j5 + j6;
                this.v0 += this.v3;
                this.v1 = Long.rotateLeft(j6, 17);
                this.v3 = Long.rotateLeft(this.v3, 21);
                long j7 = this.v1;
                long j8 = this.v2;
                this.v1 = j7 ^ j8;
                this.v3 ^= this.v0;
                this.v2 = Long.rotateLeft(j8, 32);
            }
        }
    }
}
