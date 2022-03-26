package io.michaelrocks.libphonenumber.android;

import io.michaelrocks.libphonenumber.android.Phonemetadata;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes3.dex */
final class MetadataManager {
    static final String ALTERNATE_FORMATS_FILE_PREFIX = "/io/michaelrocks/libphonenumber/android/data/PhoneNumberAlternateFormatsProto";
    static final String MULTI_FILE_PHONE_NUMBER_METADATA_FILE_PREFIX = "/io/michaelrocks/libphonenumber/android/data/PhoneNumberMetadataProto";
    static final String SHORT_NUMBER_METADATA_FILE_PREFIX = "/io/michaelrocks/libphonenumber/android/data/ShortNumberMetadataProto";
    private static final Logger logger = Logger.getLogger(MetadataManager.class.getName());
    private final MetadataLoader metadataLoader;
    private final ConcurrentHashMap<Integer, Phonemetadata.PhoneMetadata> alternateFormatsMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Phonemetadata.PhoneMetadata> shortNumberMetadataMap = new ConcurrentHashMap<>();
    private final Set<Integer> alternateFormatsCountryCodes = AlternateFormatsCountryCodeSet.getCountryCodeSet();
    private final Set<String> shortNumberMetadataRegionCodes = ShortNumbersRegionCodeSet.getRegionCodeSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    public MetadataManager(MetadataLoader metadataLoader) {
        this.metadataLoader = metadataLoader;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Phonemetadata.PhoneMetadata getAlternateFormatsForCountry(int countryCallingCode, String filePrefix) {
        if (!this.alternateFormatsCountryCodes.contains(Integer.valueOf(countryCallingCode))) {
            return null;
        }
        return getMetadataFromMultiFilePrefix(Integer.valueOf(countryCallingCode), this.alternateFormatsMap, filePrefix);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Phonemetadata.PhoneMetadata getShortNumberMetadataForRegion(String regionCode, String filePrefix) {
        if (!this.shortNumberMetadataRegionCodes.contains(regionCode)) {
            return null;
        }
        return getMetadataFromMultiFilePrefix(regionCode, this.shortNumberMetadataMap, filePrefix);
    }

    Set<String> getSupportedShortNumberRegions() {
        return Collections.unmodifiableSet(this.shortNumberMetadataRegionCodes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <T> Phonemetadata.PhoneMetadata getMetadataFromMultiFilePrefix(T key, ConcurrentHashMap<T, Phonemetadata.PhoneMetadata> map, String filePrefix) {
        Phonemetadata.PhoneMetadata metadata = map.get(key);
        if (metadata != null) {
            return metadata;
        }
        String fileName = filePrefix + "_" + key;
        List<Phonemetadata.PhoneMetadata> metadataList = getMetadataFromSingleFileName(fileName, this.metadataLoader);
        if (metadataList.size() > 1) {
            logger.log(Level.WARNING, "more than one metadata in file " + fileName);
        }
        Phonemetadata.PhoneMetadata metadata2 = metadataList.get(0);
        Phonemetadata.PhoneMetadata oldValue = map.putIfAbsent(key, metadata2);
        return oldValue != null ? oldValue : metadata2;
    }

    private static List<Phonemetadata.PhoneMetadata> getMetadataFromSingleFileName(String fileName, MetadataLoader metadataLoader) {
        InputStream source = metadataLoader.loadMetadata(fileName);
        if (source != null) {
            List<Phonemetadata.PhoneMetadata> metadataList = loadMetadataAndCloseInput(source).getMetadataList();
            if (metadataList.size() != 0) {
                return metadataList;
            }
            throw new IllegalStateException("empty metadata: " + fileName);
        }
        throw new IllegalStateException("missing metadata: " + fileName);
    }

    private static Phonemetadata.PhoneMetadataCollection loadMetadataAndCloseInput(InputStream source) {
        ObjectInputStream ois;
        try {
            ois = null;
            try {
                ois = new ObjectInputStream(source);
                Phonemetadata.PhoneMetadataCollection metadataCollection = new Phonemetadata.PhoneMetadataCollection();
                try {
                    metadataCollection.readExternal(ois);
                    try {
                        ois.close();
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "error closing input stream (ignored)", (Throwable) e);
                    }
                    return metadataCollection;
                } catch (IOException e2) {
                    throw new RuntimeException("cannot load/parse metadata", e2);
                }
            } catch (IOException e3) {
                throw new RuntimeException("cannot load/parse metadata", e3);
            }
        } catch (Throwable th) {
            try {
                if (ois != null) {
                    ois.close();
                } else {
                    source.close();
                }
            } catch (IOException e4) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", (Throwable) e4);
            }
            throw th;
        }
    }
}
