package androidx.camera.core;

import android.util.Size;
import androidx.camera.core.ImageProxy;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
final class ImageProxyDownsampler {

    /* loaded from: classes.dex */
    enum DownsamplingMethod {
        NEAREST_NEIGHBOR,
        AVERAGING
    }

    private ImageProxyDownsampler() {
    }

    static ForwardingImageProxy downsample(ImageProxy image, int downsampledWidth, int downsampledHeight, DownsamplingMethod downsamplingMethod) {
        byte[] output;
        if (image.getFormat() != 35) {
            throw new UnsupportedOperationException("Only YUV_420_888 format is currently supported.");
        } else if (image.getWidth() < downsampledWidth || image.getHeight() < downsampledHeight) {
            throw new IllegalArgumentException("Downsampled dimension " + new Size(downsampledWidth, downsampledHeight) + " is not <= original dimension " + new Size(image.getWidth(), image.getHeight()) + ".");
        } else if (image.getWidth() == downsampledWidth && image.getHeight() == downsampledHeight) {
            return new ForwardingImageProxyImpl(image, image.getPlanes(), downsampledWidth, downsampledHeight);
        } else {
            int[] inputWidths = {image.getWidth(), image.getWidth() / 2, image.getWidth() / 2};
            int[] inputHeights = {image.getHeight(), image.getHeight() / 2, image.getHeight() / 2};
            int[] outputWidths = {downsampledWidth, downsampledWidth / 2, downsampledWidth / 2};
            int[] outputHeights = {downsampledHeight, downsampledHeight / 2, downsampledHeight / 2};
            ImageProxy.PlaneProxy[] outputPlanes = new ImageProxy.PlaneProxy[3];
            for (int i = 0; i < 3; i++) {
                ImageProxy.PlaneProxy inputPlane = image.getPlanes()[i];
                ByteBuffer inputBuffer = inputPlane.getBuffer();
                byte[] output2 = new byte[outputWidths[i] * outputHeights[i]];
                int i2 = AnonymousClass2.$SwitchMap$androidx$camera$core$ImageProxyDownsampler$DownsamplingMethod[downsamplingMethod.ordinal()];
                if (i2 == 1) {
                    output = output2;
                    resizeNearestNeighbor(inputBuffer, inputWidths[i], inputPlane.getPixelStride(), inputPlane.getRowStride(), inputHeights[i], output, outputWidths[i], outputHeights[i]);
                } else if (i2 != 2) {
                    output = output2;
                } else {
                    output = output2;
                    resizeAveraging(inputBuffer, inputWidths[i], inputPlane.getPixelStride(), inputPlane.getRowStride(), inputHeights[i], output, outputWidths[i], outputHeights[i]);
                }
                outputPlanes[i] = createPlaneProxy(outputWidths[i], 1, output);
            }
            return new ForwardingImageProxyImpl(image, outputPlanes, downsampledWidth, downsampledHeight);
        }
    }

    /* renamed from: androidx.camera.core.ImageProxyDownsampler$2  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$androidx$camera$core$ImageProxyDownsampler$DownsamplingMethod = new int[DownsamplingMethod.values().length];

        static {
            try {
                $SwitchMap$androidx$camera$core$ImageProxyDownsampler$DownsamplingMethod[DownsamplingMethod.NEAREST_NEIGHBOR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$androidx$camera$core$ImageProxyDownsampler$DownsamplingMethod[DownsamplingMethod.AVERAGING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private static void resizeNearestNeighbor(ByteBuffer input, int inputWidth, int inputPixelStride, int inputRowStride, int inputHeight, byte[] output, int outputWidth, int outputHeight) {
        int i = inputRowStride;
        int i2 = inputHeight;
        float scaleX = ((float) inputWidth) / ((float) outputWidth);
        float scaleY = ((float) i2) / ((float) outputHeight);
        byte[] row = new byte[i];
        int[] sourceIndices = new int[outputWidth];
        for (int ix = 0; ix < outputWidth; ix++) {
            sourceIndices[ix] = ((int) (((float) ix) * scaleX)) * inputPixelStride;
        }
        synchronized (input) {
            input.rewind();
            int iy = 0;
            while (iy < outputHeight) {
                int rowOffsetTarget = iy * outputWidth;
                input.position(Math.min((int) (((float) iy) * scaleY), i2 - 1) * i);
                input.get(row, 0, Math.min(i, input.remaining()));
                for (int ix2 = 0; ix2 < outputWidth; ix2++) {
                    output[rowOffsetTarget + ix2] = row[sourceIndices[ix2]];
                }
                iy++;
                i = inputRowStride;
                i2 = inputHeight;
            }
        }
    }

    private static void resizeAveraging(ByteBuffer input, int inputWidth, int inputPixelStride, int inputRowStride, int inputHeight, byte[] output, int outputWidth, int outputHeight) {
        int i = inputRowStride;
        int i2 = inputHeight;
        int i3 = outputWidth;
        int i4 = outputHeight;
        float scaleX = ((float) inputWidth) / ((float) i3);
        float scaleY = ((float) i2) / ((float) i4);
        byte[] row0 = new byte[i];
        byte[] row1 = new byte[i];
        int[] sourceIndices = new int[i3];
        for (int ix = 0; ix < i3; ix++) {
            sourceIndices[ix] = ((int) (((float) ix) * scaleX)) * inputPixelStride;
        }
        try {
            synchronized (input) {
                try {
                    input.rewind();
                    int iy = 0;
                    while (iy < i4) {
                        int floorSourceY = (int) (((float) iy) * scaleY);
                        int rowOffsetSource1 = Math.min(floorSourceY + 1, i2 - 1) * i;
                        int rowOffsetTarget = iy * outputWidth;
                        input.position(Math.min(floorSourceY, i2 - 1) * i);
                        input.get(row0, 0, Math.min(i, input.remaining()));
                        input.position(rowOffsetSource1);
                        input.get(row1, 0, Math.min(i, input.remaining()));
                        int ix2 = 0;
                        while (ix2 < i3) {
                            output[rowOffsetTarget + ix2] = (byte) ((((((row0[sourceIndices[ix2]] & 255) + (row0[sourceIndices[ix2] + inputPixelStride] & 255)) + (row1[sourceIndices[ix2]] & 255)) + (row1[sourceIndices[ix2] + inputPixelStride] & 255)) / 4) & 255);
                            ix2++;
                            i3 = outputWidth;
                            rowOffsetSource1 = rowOffsetSource1;
                        }
                        iy++;
                        i = inputRowStride;
                        i2 = inputHeight;
                        i3 = outputWidth;
                        i4 = outputHeight;
                        scaleX = scaleX;
                    }
                } catch (Throwable th) {
                    th = th;
                    throw th;
                }
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static ImageProxy.PlaneProxy createPlaneProxy(final int rowStride, final int pixelStride, final byte[] data) {
        return new ImageProxy.PlaneProxy() { // from class: androidx.camera.core.ImageProxyDownsampler.1
            final ByteBuffer mBuffer;

            {
                this.mBuffer = ByteBuffer.wrap(data);
            }

            @Override // androidx.camera.core.ImageProxy.PlaneProxy
            public int getRowStride() {
                return rowStride;
            }

            @Override // androidx.camera.core.ImageProxy.PlaneProxy
            public int getPixelStride() {
                return pixelStride;
            }

            @Override // androidx.camera.core.ImageProxy.PlaneProxy
            public ByteBuffer getBuffer() {
                return this.mBuffer;
            }
        };
    }

    /* loaded from: classes.dex */
    private static final class ForwardingImageProxyImpl extends ForwardingImageProxy {
        private final int mDownsampledHeight;
        private final ImageProxy.PlaneProxy[] mDownsampledPlanes;
        private final int mDownsampledWidth;

        ForwardingImageProxyImpl(ImageProxy originalImage, ImageProxy.PlaneProxy[] downsampledPlanes, int downsampledWidth, int downsampledHeight) {
            super(originalImage);
            this.mDownsampledPlanes = downsampledPlanes;
            this.mDownsampledWidth = downsampledWidth;
            this.mDownsampledHeight = downsampledHeight;
        }

        @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
        public synchronized int getWidth() {
            return this.mDownsampledWidth;
        }

        @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
        public synchronized int getHeight() {
            return this.mDownsampledHeight;
        }

        @Override // androidx.camera.core.ForwardingImageProxy, androidx.camera.core.ImageProxy
        public synchronized ImageProxy.PlaneProxy[] getPlanes() {
            return this.mDownsampledPlanes;
        }
    }
}
