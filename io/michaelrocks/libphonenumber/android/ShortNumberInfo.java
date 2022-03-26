package io.michaelrocks.libphonenumber.android;

import io.michaelrocks.libphonenumber.android.Phonemetadata;
import io.michaelrocks.libphonenumber.android.Phonenumber;
import io.michaelrocks.libphonenumber.android.internal.MatcherApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes3.dex */
public class ShortNumberInfo {
    private final Map<Integer, List<String>> countryCallingCodeToRegionCodeMap = CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap();
    private final MatcherApi matcherApi;
    private final MetadataSource metadataSource;
    private static final Logger logger = Logger.getLogger(ShortNumberInfo.class.getName());
    private static final Set<String> REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT = new HashSet();

    /* loaded from: classes3.dex */
    public enum ShortNumberCost {
        TOLL_FREE,
        STANDARD_RATE,
        PREMIUM_RATE,
        UNKNOWN_COST
    }

    static {
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("BR");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("CL");
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.add("NI");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShortNumberInfo(MetadataSource metadataSource, MatcherApi matcherApi) {
        this.metadataSource = metadataSource;
        this.matcherApi = matcherApi;
    }

    private List<String> getRegionCodesForCountryCode(int countryCallingCode) {
        List<String> regionCodes = this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(countryCallingCode));
        return Collections.unmodifiableList(regionCodes == null ? new ArrayList<>(0) : regionCodes);
    }

    private boolean regionDialingFromMatchesNumber(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        return getRegionCodesForCountryCode(number.getCountryCode()).contains(regionDialingFrom);
    }

    public boolean isPossibleShortNumberForRegion(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        Phonemetadata.PhoneMetadata phoneMetadata;
        if (regionDialingFromMatchesNumber(number, regionDialingFrom) && (phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionDialingFrom)) != null) {
            return phoneMetadata.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(getNationalSignificantNumber(number).length()));
        }
        return false;
    }

    public boolean isPossibleShortNumber(Phonenumber.PhoneNumber number) {
        List<String> regionCodes = getRegionCodesForCountryCode(number.getCountryCode());
        int shortNumberLength = getNationalSignificantNumber(number).length();
        for (String region : regionCodes) {
            Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(region);
            if (phoneMetadata != null && phoneMetadata.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(shortNumberLength))) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidShortNumberForRegion(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        Phonemetadata.PhoneMetadata phoneMetadata;
        if (!regionDialingFromMatchesNumber(number, regionDialingFrom) || (phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionDialingFrom)) == null) {
            return false;
        }
        String shortNumber = getNationalSignificantNumber(number);
        if (!matchesPossibleNumberAndNationalNumber(shortNumber, phoneMetadata.getGeneralDesc())) {
            return false;
        }
        return matchesPossibleNumberAndNationalNumber(shortNumber, phoneMetadata.getShortCode());
    }

    public boolean isValidShortNumber(Phonenumber.PhoneNumber number) {
        List<String> regionCodes = getRegionCodesForCountryCode(number.getCountryCode());
        String regionCode = getRegionCodeForShortNumberFromRegionList(number, regionCodes);
        if (regionCodes.size() <= 1 || regionCode == null) {
            return isValidShortNumberForRegion(number, regionCode);
        }
        return true;
    }

    public ShortNumberCost getExpectedCostForRegion(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        if (!regionDialingFromMatchesNumber(number, regionDialingFrom)) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionDialingFrom);
        if (phoneMetadata == null) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        String shortNumber = getNationalSignificantNumber(number);
        if (!phoneMetadata.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(shortNumber.length()))) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (matchesPossibleNumberAndNationalNumber(shortNumber, phoneMetadata.getPremiumRate())) {
            return ShortNumberCost.PREMIUM_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(shortNumber, phoneMetadata.getStandardRate())) {
            return ShortNumberCost.STANDARD_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(shortNumber, phoneMetadata.getTollFree())) {
            return ShortNumberCost.TOLL_FREE;
        }
        if (isEmergencyNumber(shortNumber, regionDialingFrom)) {
            return ShortNumberCost.TOLL_FREE;
        }
        return ShortNumberCost.UNKNOWN_COST;
    }

    public ShortNumberCost getExpectedCost(Phonenumber.PhoneNumber number) {
        List<String> regionCodes = getRegionCodesForCountryCode(number.getCountryCode());
        if (regionCodes.size() == 0) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (regionCodes.size() == 1) {
            return getExpectedCostForRegion(number, regionCodes.get(0));
        }
        ShortNumberCost cost = ShortNumberCost.TOLL_FREE;
        for (String regionCode : regionCodes) {
            ShortNumberCost costForRegion = getExpectedCostForRegion(number, regionCode);
            int i = AnonymousClass1.$SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[costForRegion.ordinal()];
            if (i == 1) {
                return ShortNumberCost.PREMIUM_RATE;
            }
            if (i == 2) {
                cost = ShortNumberCost.UNKNOWN_COST;
            } else if (i != 3) {
                if (i != 4) {
                    Logger logger2 = logger;
                    Level level = Level.SEVERE;
                    logger2.log(level, "Unrecognised cost for region: " + costForRegion);
                }
            } else if (cost != ShortNumberCost.UNKNOWN_COST) {
                cost = ShortNumberCost.STANDARD_RATE;
            }
        }
        return cost;
    }

    /* renamed from: io.michaelrocks.libphonenumber.android.ShortNumberInfo$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost = new int[ShortNumberCost.values().length];

        static {
            try {
                $SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[ShortNumberCost.PREMIUM_RATE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[ShortNumberCost.UNKNOWN_COST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[ShortNumberCost.STANDARD_RATE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[ShortNumberCost.TOLL_FREE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private String getRegionCodeForShortNumberFromRegionList(Phonenumber.PhoneNumber number, List<String> regionCodes) {
        if (regionCodes.size() == 0) {
            return null;
        }
        if (regionCodes.size() == 1) {
            return regionCodes.get(0);
        }
        String nationalNumber = getNationalSignificantNumber(number);
        for (String regionCode : regionCodes) {
            Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionCode);
            if (phoneMetadata != null && matchesPossibleNumberAndNationalNumber(nationalNumber, phoneMetadata.getShortCode())) {
                return regionCode;
            }
        }
        return null;
    }

    Set<String> getSupportedRegions() {
        return Collections.emptySet();
    }

    String getExampleShortNumber(String regionCode) {
        Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionCode);
        if (phoneMetadata == null) {
            return "";
        }
        Phonemetadata.PhoneNumberDesc desc = phoneMetadata.getShortCode();
        if (desc.hasExampleNumber()) {
            return desc.getExampleNumber();
        }
        return "";
    }

    String getExampleShortNumberForCost(String regionCode, ShortNumberCost cost) {
        Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionCode);
        if (phoneMetadata == null) {
            return "";
        }
        Phonemetadata.PhoneNumberDesc desc = null;
        int i = AnonymousClass1.$SwitchMap$io$michaelrocks$libphonenumber$android$ShortNumberInfo$ShortNumberCost[cost.ordinal()];
        if (i == 1) {
            desc = phoneMetadata.getPremiumRate();
        } else if (i == 3) {
            desc = phoneMetadata.getStandardRate();
        } else if (i == 4) {
            desc = phoneMetadata.getTollFree();
        }
        if (desc == null || !desc.hasExampleNumber()) {
            return "";
        }
        return desc.getExampleNumber();
    }

    public boolean connectsToEmergencyNumber(String number, String regionCode) {
        return matchesEmergencyNumberHelper(number, regionCode, true);
    }

    public boolean isEmergencyNumber(CharSequence number, String regionCode) {
        return matchesEmergencyNumberHelper(number, regionCode, false);
    }

    private boolean matchesEmergencyNumberHelper(CharSequence number, String regionCode, boolean allowPrefixMatch) {
        Phonemetadata.PhoneMetadata metadata;
        CharSequence possibleNumber = PhoneNumberUtil.extractPossibleNumber(number);
        boolean allowPrefixMatchForRegion = false;
        if (PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(possibleNumber).lookingAt() || (metadata = this.metadataSource.getShortNumberMetadataForRegion(regionCode)) == null || !metadata.hasEmergency()) {
            return false;
        }
        String normalizedNumber = PhoneNumberUtil.normalizeDigitsOnly(possibleNumber);
        if (allowPrefixMatch && !REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.contains(regionCode)) {
            allowPrefixMatchForRegion = true;
        }
        return this.matcherApi.matchNationalNumber(normalizedNumber, metadata.getEmergency(), allowPrefixMatchForRegion);
    }

    public boolean isCarrierSpecific(Phonenumber.PhoneNumber number) {
        String regionCode = getRegionCodeForShortNumberFromRegionList(number, getRegionCodesForCountryCode(number.getCountryCode()));
        String nationalNumber = getNationalSignificantNumber(number);
        Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionCode);
        return phoneMetadata != null && matchesPossibleNumberAndNationalNumber(nationalNumber, phoneMetadata.getCarrierSpecific());
    }

    public boolean isCarrierSpecificForRegion(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        if (!regionDialingFromMatchesNumber(number, regionDialingFrom)) {
            return false;
        }
        String nationalNumber = getNationalSignificantNumber(number);
        Phonemetadata.PhoneMetadata phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionDialingFrom);
        if (phoneMetadata == null || !matchesPossibleNumberAndNationalNumber(nationalNumber, phoneMetadata.getCarrierSpecific())) {
            return false;
        }
        return true;
    }

    public boolean isSmsServiceForRegion(Phonenumber.PhoneNumber number, String regionDialingFrom) {
        Phonemetadata.PhoneMetadata phoneMetadata;
        if (regionDialingFromMatchesNumber(number, regionDialingFrom) && (phoneMetadata = this.metadataSource.getShortNumberMetadataForRegion(regionDialingFrom)) != null && matchesPossibleNumberAndNationalNumber(getNationalSignificantNumber(number), phoneMetadata.getSmsServices())) {
            return true;
        }
        return false;
    }

    private static String getNationalSignificantNumber(Phonenumber.PhoneNumber number) {
        StringBuilder nationalNumber = new StringBuilder();
        if (number.isItalianLeadingZero()) {
            char[] zeros = new char[number.getNumberOfLeadingZeros()];
            Arrays.fill(zeros, '0');
            nationalNumber.append(new String(zeros));
        }
        nationalNumber.append(number.getNationalNumber());
        return nationalNumber.toString();
    }

    private boolean matchesPossibleNumberAndNationalNumber(String number, Phonemetadata.PhoneNumberDesc numberDesc) {
        if (numberDesc.getPossibleLengthCount() <= 0 || numberDesc.getPossibleLengthList().contains(Integer.valueOf(number.length()))) {
            return this.matcherApi.matchNationalNumber(number, numberDesc, false);
        }
        return false;
    }
}
