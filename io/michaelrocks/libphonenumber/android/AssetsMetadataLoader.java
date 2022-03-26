package io.michaelrocks.libphonenumber.android;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes3.dex */
public class AssetsMetadataLoader implements MetadataLoader {
    private final AssetManager assetManager;

    public AssetsMetadataLoader(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    @Override // io.michaelrocks.libphonenumber.android.MetadataLoader
    public InputStream loadMetadata(String metadataFileName) {
        try {
            return this.assetManager.open(metadataFileName.substring(1));
        } catch (IOException e) {
            return null;
        }
    }
}
