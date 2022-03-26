package com.facebook.imagepipeline.decoder;

import android.graphics.Bitmap;
import android.os.Build;
import com.facebook.common.references.CloseableReference;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.platform.PlatformDecoder;
import com.facebook.imagepipeline.transformation.BitmapTransformation;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class DefaultImageDecoder implements ImageDecoder {
    private final ImageDecoder mAnimatedGifDecoder;
    private final ImageDecoder mAnimatedWebPDecoder;
    @Nullable
    private final Map<ImageFormat, ImageDecoder> mCustomDecoders;
    private final ImageDecoder mDefaultDecoder;
    private final PlatformDecoder mPlatformDecoder;

    public DefaultImageDecoder(ImageDecoder animatedGifDecoder, ImageDecoder animatedWebPDecoder, PlatformDecoder platformDecoder) {
        this(animatedGifDecoder, animatedWebPDecoder, platformDecoder, null);
    }

    public DefaultImageDecoder(ImageDecoder animatedGifDecoder, ImageDecoder animatedWebPDecoder, PlatformDecoder platformDecoder, @Nullable Map<ImageFormat, ImageDecoder> customDecoders) {
        this.mDefaultDecoder = new ImageDecoder() { // from class: com.facebook.imagepipeline.decoder.DefaultImageDecoder.1
            @Override // com.facebook.imagepipeline.decoder.ImageDecoder
            public CloseableImage decode(EncodedImage encodedImage, int length, QualityInfo qualityInfo, ImageDecodeOptions options) {
                ImageFormat imageFormat = encodedImage.getImageFormat();
                if (imageFormat == DefaultImageFormats.JPEG) {
                    return DefaultImageDecoder.this.decodeJpeg(encodedImage, length, qualityInfo, options);
                }
                if (imageFormat == DefaultImageFormats.GIF) {
                    return DefaultImageDecoder.this.decodeGif(encodedImage, length, qualityInfo, options);
                }
                if (imageFormat == DefaultImageFormats.WEBP_ANIMATED) {
                    return DefaultImageDecoder.this.decodeAnimatedWebp(encodedImage, length, qualityInfo, options);
                }
                if (imageFormat != ImageFormat.UNKNOWN) {
                    return DefaultImageDecoder.this.decodeStaticImage(encodedImage, options);
                }
                throw new DecodeException("unknown image format", encodedImage);
            }
        };
        this.mAnimatedGifDecoder = animatedGifDecoder;
        this.mAnimatedWebPDecoder = animatedWebPDecoder;
        this.mPlatformDecoder = platformDecoder;
        this.mCustomDecoders = customDecoders;
    }

    @Override // com.facebook.imagepipeline.decoder.ImageDecoder
    public CloseableImage decode(EncodedImage encodedImage, int length, QualityInfo qualityInfo, ImageDecodeOptions options) {
        ImageDecoder decoder;
        if (options.customImageDecoder != null) {
            return options.customImageDecoder.decode(encodedImage, length, qualityInfo, options);
        }
        ImageFormat imageFormat = encodedImage.getImageFormat();
        if (imageFormat == null || imageFormat == ImageFormat.UNKNOWN) {
            imageFormat = ImageFormatChecker.getImageFormat_WrapIOException(encodedImage.getInputStream());
            encodedImage.setImageFormat(imageFormat);
        }
        Map<ImageFormat, ImageDecoder> map = this.mCustomDecoders;
        if (map == null || (decoder = map.get(imageFormat)) == null) {
            return this.mDefaultDecoder.decode(encodedImage, length, qualityInfo, options);
        }
        return decoder.decode(encodedImage, length, qualityInfo, options);
    }

    public CloseableImage decodeGif(EncodedImage encodedImage, int length, QualityInfo qualityInfo, ImageDecodeOptions options) {
        ImageDecoder imageDecoder;
        if (options.forceStaticImage || (imageDecoder = this.mAnimatedGifDecoder) == null) {
            return decodeStaticImage(encodedImage, options);
        }
        return imageDecoder.decode(encodedImage, length, qualityInfo, options);
    }

    public CloseableStaticBitmap decodeStaticImage(EncodedImage encodedImage, ImageDecodeOptions options) {
        CloseableReference<Bitmap> bitmapReference = this.mPlatformDecoder.decodeFromEncodedImageWithColorSpace(encodedImage, options.bitmapConfig, null, options.transformToSRGB);
        try {
            maybeApplyTransformation(options.bitmapTransformation, bitmapReference);
            return new CloseableStaticBitmap(bitmapReference, ImmutableQualityInfo.FULL_QUALITY, encodedImage.getRotationAngle(), encodedImage.getExifOrientation());
        } finally {
            bitmapReference.close();
        }
    }

    public CloseableStaticBitmap decodeJpeg(EncodedImage encodedImage, int length, QualityInfo qualityInfo, ImageDecodeOptions options) {
        CloseableReference<Bitmap> bitmapReference = this.mPlatformDecoder.decodeJPEGFromEncodedImageWithColorSpace(encodedImage, options.bitmapConfig, null, length, options.transformToSRGB);
        try {
            maybeApplyTransformation(options.bitmapTransformation, bitmapReference);
            return new CloseableStaticBitmap(bitmapReference, qualityInfo, encodedImage.getRotationAngle(), encodedImage.getExifOrientation());
        } finally {
            bitmapReference.close();
        }
    }

    public CloseableImage decodeAnimatedWebp(EncodedImage encodedImage, int length, QualityInfo qualityInfo, ImageDecodeOptions options) {
        return this.mAnimatedWebPDecoder.decode(encodedImage, length, qualityInfo, options);
    }

    private void maybeApplyTransformation(@Nullable BitmapTransformation transformation, CloseableReference<Bitmap> bitmapReference) {
        if (transformation != null) {
            Bitmap bitmap = bitmapReference.get();
            if (Build.VERSION.SDK_INT >= 12 && transformation.modifiesTransparency()) {
                bitmap.setHasAlpha(true);
            }
            transformation.transform(bitmap);
        }
    }
}
