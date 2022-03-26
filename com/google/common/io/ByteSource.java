package com.google.common.io;

import com.google.common.base.Ascii;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: classes3.dex */
public abstract class ByteSource {
    public abstract InputStream openStream() throws IOException;

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public InputStream openBufferedStream() throws IOException {
        InputStream in = openStream();
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public ByteSource slice(long offset, long length) {
        return new SlicedByteSource(offset, length);
    }

    public boolean isEmpty() throws IOException {
        Optional<Long> sizeIfKnown = sizeIfKnown();
        boolean z = true;
        if (sizeIfKnown.isPresent()) {
            return sizeIfKnown.get().longValue() == 0;
        }
        Closer closer = Closer.create();
        try {
            if (((InputStream) closer.register(openStream())).read() != -1) {
                z = false;
            }
            return z;
        } catch (Throwable e) {
            try {
                throw closer.rethrow(e);
            } finally {
                closer.close();
            }
        }
    }

    public Optional<Long> sizeIfKnown() {
        return Optional.absent();
    }

    /* JADX WARN: Finally extract failed */
    public long size() throws IOException {
        Optional<Long> sizeIfKnown = sizeIfKnown();
        if (sizeIfKnown.isPresent()) {
            return sizeIfKnown.get().longValue();
        }
        Closer closer = Closer.create();
        try {
            return countBySkipping((InputStream) closer.register(openStream()));
        } catch (IOException e) {
            closer.close();
            closer = Closer.create();
            try {
                return ByteStreams.exhaust((InputStream) closer.register(openStream()));
            } finally {
                try {
                    throw closer.rethrow(e);
                } finally {
                }
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private long countBySkipping(InputStream in) throws IOException {
        long count = 0;
        while (true) {
            long skipped = ByteStreams.skipUpTo(in, 2147483647L);
            if (skipped <= 0) {
                return count;
            }
            count += skipped;
        }
    }

    public long copyTo(OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        Closer closer = Closer.create();
        try {
            return ByteStreams.copy((InputStream) closer.register(openStream()), output);
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    public long copyTo(ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            return ByteStreams.copy((InputStream) closer.register(openStream()), (OutputStream) closer.register(sink.openStream()));
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    public byte[] read() throws IOException {
        byte[] bArr;
        Closer closer = Closer.create();
        try {
            InputStream in = (InputStream) closer.register(openStream());
            Optional<Long> size = sizeIfKnown();
            if (size.isPresent()) {
                bArr = ByteStreams.toByteArray(in, size.get().longValue());
            } else {
                bArr = ByteStreams.toByteArray(in);
            }
            return bArr;
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    public <T> T read(ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            return (T) ByteStreams.readBytes((InputStream) closer.register(openStream()), processor);
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    public HashCode hash(HashFunction hashFunction) throws IOException {
        Hasher hasher = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }

    public boolean contentEquals(ByteSource other) throws IOException {
        int read1;
        Preconditions.checkNotNull(other);
        byte[] buf1 = ByteStreams.createBuffer();
        byte[] buf2 = ByteStreams.createBuffer();
        Closer closer = Closer.create();
        try {
            InputStream in1 = (InputStream) closer.register(openStream());
            InputStream in2 = (InputStream) closer.register(other.openStream());
            do {
                read1 = ByteStreams.read(in1, buf1, 0, buf1.length);
                if (read1 == ByteStreams.read(in2, buf2, 0, buf2.length) && Arrays.equals(buf1, buf2)) {
                }
                return false;
            } while (read1 == buf1.length);
            return true;
        } finally {
            try {
                throw closer.rethrow(e);
            } finally {
            }
        }
    }

    public static ByteSource concat(Iterable<? extends ByteSource> sources) {
        return new ConcatenatedByteSource(sources);
    }

    public static ByteSource concat(Iterator<? extends ByteSource> sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource concat(ByteSource... sources) {
        return concat(ImmutableList.copyOf(sources));
    }

    public static ByteSource wrap(byte[] b) {
        return new ByteArrayByteSource(b);
    }

    public static ByteSource empty() {
        return EmptyByteSource.INSTANCE;
    }

    /* loaded from: classes3.dex */
    public class AsCharSource extends CharSource {
        final Charset charset;

        AsCharSource(Charset charset) {
            ByteSource.this = this$0;
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        @Override // com.google.common.io.CharSource
        public ByteSource asByteSource(Charset charset) {
            if (charset.equals(this.charset)) {
                return ByteSource.this;
            }
            return super.asByteSource(charset);
        }

        @Override // com.google.common.io.CharSource
        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        @Override // com.google.common.io.CharSource
        public String read() throws IOException {
            return new String(ByteSource.this.read(), this.charset);
        }

        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }

    /* loaded from: classes3.dex */
    public final class SlicedByteSource extends ByteSource {
        final long length;
        final long offset;

        SlicedByteSource(long offset, long length) {
            ByteSource.this = r6;
            boolean z = true;
            Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
            Preconditions.checkArgument(length < 0 ? false : z, "length (%s) may not be negative", length);
            this.offset = offset;
            this.length = length;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return sliceStream(ByteSource.this.openStream());
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return sliceStream(ByteSource.this.openBufferedStream());
        }

        private InputStream sliceStream(InputStream in) throws IOException {
            Closer closer;
            long j = this.offset;
            if (j > 0) {
                try {
                    if (ByteStreams.skipUpTo(in, j) < this.offset) {
                        in.close();
                        return new ByteArrayInputStream(new byte[0]);
                    }
                } finally {
                    try {
                        throw closer.rethrow(e);
                    } catch (Throwable th) {
                    }
                }
            }
            return ByteStreams.limit(in, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public ByteSource slice(long offset, long length) {
            boolean z = true;
            Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
            if (length < 0) {
                z = false;
            }
            Preconditions.checkArgument(z, "length (%s) may not be negative", length);
            long maxLength = this.length - offset;
            if (maxLength <= 0) {
                return ByteSource.empty();
            }
            return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            return this.length == 0 || ByteSource.super.isEmpty();
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
            if (!optionalUnslicedSize.isPresent()) {
                return Optional.absent();
            }
            long unslicedSize = optionalUnslicedSize.get().longValue();
            return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - Math.min(this.offset, unslicedSize))));
        }

        public String toString() {
            return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ByteArrayByteSource extends ByteSource {
        final byte[] bytes;
        final int length;
        final int offset;

        ByteArrayByteSource(byte[] bytes) {
            this(bytes, 0, bytes.length);
        }

        ByteArrayByteSource(byte[] bytes, int offset, int length) {
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() {
            return new ByteArrayInputStream(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openBufferedStream() throws IOException {
            return openStream();
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() {
            return this.length == 0;
        }

        @Override // com.google.common.io.ByteSource
        public long size() {
            return (long) this.length;
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            return Optional.of(Long.valueOf((long) this.length));
        }

        @Override // com.google.common.io.ByteSource
        public byte[] read() {
            byte[] bArr = this.bytes;
            int i = this.offset;
            return Arrays.copyOfRange(bArr, i, this.length + i);
        }

        @Override // com.google.common.io.ByteSource
        public <T> T read(ByteProcessor<T> processor) throws IOException {
            processor.processBytes(this.bytes, this.offset, this.length);
            return processor.getResult();
        }

        @Override // com.google.common.io.ByteSource
        public long copyTo(OutputStream output) throws IOException {
            output.write(this.bytes, this.offset, this.length);
            return (long) this.length;
        }

        @Override // com.google.common.io.ByteSource
        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes, this.offset, this.length);
        }

        @Override // com.google.common.io.ByteSource
        public ByteSource slice(long offset, long length) {
            boolean z = true;
            Preconditions.checkArgument(offset >= 0, "offset (%s) may not be negative", offset);
            if (length < 0) {
                z = false;
            }
            Preconditions.checkArgument(z, "length (%s) may not be negative", length);
            long offset2 = Math.min(offset, (long) this.length);
            return new ByteArrayByteSource(this.bytes, this.offset + ((int) offset2), (int) Math.min(length, ((long) this.length) - offset2));
        }

        public String toString() {
            return "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
        }
    }

    /* loaded from: classes3.dex */
    public static final class EmptyByteSource extends ByteArrayByteSource {
        static final EmptyByteSource INSTANCE = new EmptyByteSource();

        EmptyByteSource() {
            super(new byte[0]);
        }

        @Override // com.google.common.io.ByteSource
        public CharSource asCharSource(Charset charset) {
            Preconditions.checkNotNull(charset);
            return CharSource.empty();
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource, com.google.common.io.ByteSource
        public byte[] read() {
            return this.bytes;
        }

        @Override // com.google.common.io.ByteSource.ByteArrayByteSource
        public String toString() {
            return "ByteSource.empty()";
        }
    }

    /* loaded from: classes3.dex */
    public static final class ConcatenatedByteSource extends ByteSource {
        final Iterable<? extends ByteSource> sources;

        ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
            this.sources = (Iterable) Preconditions.checkNotNull(sources);
        }

        @Override // com.google.common.io.ByteSource
        public InputStream openStream() throws IOException {
            return new MultiInputStream(this.sources.iterator());
        }

        @Override // com.google.common.io.ByteSource
        public boolean isEmpty() throws IOException {
            for (ByteSource source : this.sources) {
                if (!source.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.google.common.io.ByteSource
        public Optional<Long> sizeIfKnown() {
            Iterable<? extends ByteSource> iterable = this.sources;
            if (!(iterable instanceof Collection)) {
                return Optional.absent();
            }
            long result = 0;
            for (ByteSource source : iterable) {
                Optional<Long> sizeIfKnown = source.sizeIfKnown();
                if (!sizeIfKnown.isPresent()) {
                    return Optional.absent();
                }
                result += sizeIfKnown.get().longValue();
                if (result < 0) {
                    return Optional.of(Long.MAX_VALUE);
                }
            }
            return Optional.of(Long.valueOf(result));
        }

        @Override // com.google.common.io.ByteSource
        public long size() throws IOException {
            long result = 0;
            for (ByteSource source : this.sources) {
                result += source.size();
                if (result < 0) {
                    return Long.MAX_VALUE;
                }
            }
            return result;
        }

        public String toString() {
            return "ByteSource.concat(" + this.sources + ")";
        }
    }
}
