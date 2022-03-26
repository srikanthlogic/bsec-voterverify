package io.michaelrocks.libphonenumber.android;

import io.michaelrocks.libphonenumber.android.Phonemetadata;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: classes3.dex */
final class MultiFileMetadataSourceImpl implements MetadataSource {
    private final String alternateFormatsFilePrefix;
    private final ConcurrentHashMap<String, Phonemetadata.PhoneMetadata> geographicalRegions;
    private final MetadataManager metadataManager;
    private final ConcurrentHashMap<Integer, Phonemetadata.PhoneMetadata> nonGeographicalRegions;
    private final String phoneNumberMetadataFilePrefix;
    private final String shortNumberFilePrefix;

    MultiFileMetadataSourceImpl(String phoneNumberMetadataFilePrefix, String alternateFormatsFilePrefix, String shortNumberFilePrefix, MetadataLoader metadataLoader) {
        this.geographicalRegions = new ConcurrentHashMap<>();
        this.nonGeographicalRegions = new ConcurrentHashMap<>();
        this.phoneNumberMetadataFilePrefix = phoneNumberMetadataFilePrefix;
        this.alternateFormatsFilePrefix = alternateFormatsFilePrefix;
        this.shortNumberFilePrefix = shortNumberFilePrefix;
        this.metadataManager = new MetadataManager(metadataLoader);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MultiFileMetadataSourceImpl(MetadataLoader metadataLoader) {
        this("/io/michaelrocks/libphonenumber/android/data/PhoneNumberMetadataProto", "/io/michaelrocks/libphonenumber/android/data/PhoneNumberAlternateFormatsProto", "/io/michaelrocks/libphonenumber/android/data/ShortNumberMetadataProto", metadataLoader);
    }

    @Override // io.michaelrocks.libphonenumber.android.MetadataSource
    public Phonemetadata.PhoneMetadata getMetadataForRegion(String regionCode) {
        return this.metadataManager.getMetadataFromMultiFilePrefix(regionCode, this.geographicalRegions, this.phoneNumberMetadataFilePrefix);
    }

    @Override // io.michaelrocks.libphonenumber.android.MetadataSource
    public Phonemetadata.PhoneMetadata getMetadataForNonGeographicalRegion(int countryCallingCode) {
        if (!isNonGeographical(countryCallingCode)) {
            return null;
        }
        return this.metadataManager.getMetadataFromMultiFilePrefix(Integer.valueOf(countryCallingCode), this.nonGeographicalRegions, this.phoneNumberMetadataFilePrefix);
    }

    @Override // io.michaelrocks.libphonenumber.android.MetadataSource
    public Phonemetadata.PhoneMetadata getAlternateFormatsForCountry(int countryCallingCode) {
        return this.metadataManager.getAlternateFormatsForCountry(countryCallingCode, this.alternateFormatsFilePrefix);
    }

    @Override // io.michaelrocks.libphonenumber.android.MetadataSource
    public Phonemetadata.PhoneMetadata getShortNumberMetadataForRegion(String regionCode) {
        return this.metadataManager.getShortNumberMetadataForRegion(regionCode, this.shortNumberFilePrefix);
    }

    private boolean isNonGeographical(int countryCallingCode) {
        List<String> regionCodes = CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap().get(Integer.valueOf(countryCallingCode));
        return regionCodes.size() == 1 && PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY.equals(regionCodes.get(0));
    }
}
