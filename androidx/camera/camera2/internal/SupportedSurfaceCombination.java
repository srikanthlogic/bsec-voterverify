package androidx.camera.camera2.internal;

import android.content.Context;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import android.view.WindowManager;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.camera2.internal.compat.CameraManagerCompat;
import androidx.camera.core.CameraUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.impl.ImageOutputConfig;
import androidx.camera.core.impl.SurfaceCombination;
import androidx.camera.core.impl.SurfaceConfig;
import androidx.camera.core.impl.SurfaceSizeDefinition;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SupportedSurfaceCombination {
    private static final int ALIGN16 = 16;
    private final CamcorderProfileHelper mCamcorderProfileHelper;
    private final String mCameraId;
    private final CameraCharacteristics mCharacteristics;
    private final int mHardwareLevel;
    private SurfaceSizeDefinition mSurfaceSizeDefinition;
    private static final Size MAX_PREVIEW_SIZE = new Size(1920, 1080);
    private static final Size DEFAULT_SIZE = new Size(640, 480);
    private static final Size ZERO_SIZE = new Size(0, 0);
    private static final Size QUALITY_2160P_SIZE = new Size(3840, 2160);
    private static final Size QUALITY_1080P_SIZE = new Size(1920, 1080);
    private static final Size QUALITY_720P_SIZE = new Size(1280, 720);
    private static final Size QUALITY_480P_SIZE = new Size(720, 480);
    private static final Rational ASPECT_RATIO_4_3 = new Rational(4, 3);
    private static final Rational ASPECT_RATIO_3_4 = new Rational(3, 4);
    private static final Rational ASPECT_RATIO_16_9 = new Rational(16, 9);
    private static final Rational ASPECT_RATIO_9_16 = new Rational(9, 16);
    private final List<SurfaceCombination> mSurfaceCombinations = new ArrayList();
    private final Map<Integer, Size> mMaxSizeCache = new HashMap();
    private final Map<Integer, List<Size>> mExcludedSizeListCache = new HashMap();
    private boolean mIsRawSupported = false;
    private boolean mIsBurstCaptureSupported = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SupportedSurfaceCombination(Context context, String cameraId, CamcorderProfileHelper camcorderProfileHelper) throws CameraUnavailableException {
        int i;
        this.mCameraId = (String) Preconditions.checkNotNull(cameraId);
        this.mCamcorderProfileHelper = (CamcorderProfileHelper) Preconditions.checkNotNull(camcorderProfileHelper);
        CameraManagerCompat cameraManager = CameraManagerCompat.from(context);
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        try {
            this.mCharacteristics = cameraManager.getCameraCharacteristics(this.mCameraId);
            Integer keyValue = (Integer) this.mCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            if (keyValue != null) {
                i = keyValue.intValue();
            } else {
                i = 2;
            }
            this.mHardwareLevel = i;
            generateSupportedCombinationList();
            generateSurfaceSizeDefinition(windowManager);
            checkCustomization();
        } catch (CameraAccessExceptionCompat e) {
            throw CameraUnavailableExceptionHelper.createFrom(e);
        }
    }

    String getCameraId() {
        return this.mCameraId;
    }

    boolean isRawSupported() {
        return this.mIsRawSupported;
    }

    boolean isBurstCaptureSupported() {
        return this.mIsBurstCaptureSupported;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean checkSupported(List<SurfaceConfig> surfaceConfigList) {
        boolean isSupported = false;
        Iterator<SurfaceCombination> it = this.mSurfaceCombinations.iterator();
        while (it.hasNext() && !(isSupported = it.next().isSupported(surfaceConfigList))) {
        }
        return isSupported;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceConfig transformSurfaceConfig(int imageFormat, Size size) {
        SurfaceConfig.ConfigType configType;
        SurfaceConfig.ConfigSize configSize = SurfaceConfig.ConfigSize.NOT_SUPPORT;
        if (getAllOutputSizesByFormat(imageFormat) != null) {
            if (imageFormat == 35) {
                configType = SurfaceConfig.ConfigType.YUV;
            } else if (imageFormat == 256) {
                configType = SurfaceConfig.ConfigType.JPEG;
            } else if (imageFormat == 32) {
                configType = SurfaceConfig.ConfigType.RAW;
            } else {
                configType = SurfaceConfig.ConfigType.PRIV;
            }
            Size maxSize = fetchMaxSize(imageFormat);
            if (size.getWidth() * size.getHeight() <= this.mSurfaceSizeDefinition.getAnalysisSize().getWidth() * this.mSurfaceSizeDefinition.getAnalysisSize().getHeight()) {
                configSize = SurfaceConfig.ConfigSize.ANALYSIS;
            } else if (size.getWidth() * size.getHeight() <= this.mSurfaceSizeDefinition.getPreviewSize().getWidth() * this.mSurfaceSizeDefinition.getPreviewSize().getHeight()) {
                configSize = SurfaceConfig.ConfigSize.PREVIEW;
            } else if (size.getWidth() * size.getHeight() <= this.mSurfaceSizeDefinition.getRecordSize().getWidth() * this.mSurfaceSizeDefinition.getRecordSize().getHeight()) {
                configSize = SurfaceConfig.ConfigSize.RECORD;
            } else if (size.getWidth() * size.getHeight() <= maxSize.getWidth() * maxSize.getHeight()) {
                configSize = SurfaceConfig.ConfigSize.MAXIMUM;
            }
            return SurfaceConfig.create(configType, configSize);
        }
        throw new IllegalArgumentException("Can not get supported output size for the format: " + imageFormat);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<UseCaseConfig<?>, Size> getSuggestedResolutions(List<SurfaceConfig> existingSurfaces, List<UseCaseConfig<?>> newUseCaseConfigs) {
        Map<UseCaseConfig<?>, Size> suggestedResolutionsMap = new HashMap<>();
        List<Integer> useCasesPriorityOrder = getUseCasesPriorityOrder(newUseCaseConfigs);
        List<List<Size>> supportedOutputSizesList = new ArrayList<>();
        for (Integer index : useCasesPriorityOrder) {
            supportedOutputSizesList.add(getSupportedOutputSizes(newUseCaseConfigs.get(index.intValue())));
        }
        Iterator<List<Size>> it = getAllPossibleSizeArrangements(supportedOutputSizesList).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            List<Size> possibleSizeList = it.next();
            List<SurfaceConfig> surfaceConfigList = new ArrayList<>(existingSurfaces);
            for (int i = 0; i < possibleSizeList.size(); i++) {
                surfaceConfigList.add(transformSurfaceConfig(newUseCaseConfigs.get(useCasesPriorityOrder.get(i).intValue()).getInputFormat(), possibleSizeList.get(i)));
            }
            if (checkSupported(surfaceConfigList)) {
                for (UseCaseConfig<?> useCaseConfig : newUseCaseConfigs) {
                    suggestedResolutionsMap.put(useCaseConfig, possibleSizeList.get(useCasesPriorityOrder.indexOf(Integer.valueOf(newUseCaseConfigs.indexOf(useCaseConfig)))));
                }
            }
        }
        return suggestedResolutionsMap;
    }

    private Rational getCorrectedAspectRatio() {
        if (this.mHardwareLevel != 2 || Build.VERSION.SDK_INT != 21) {
            return null;
        }
        Size maxJpegSize = fetchMaxSize(256);
        return new Rational(maxJpegSize.getWidth(), maxJpegSize.getHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceSizeDefinition getSurfaceSizeDefinition() {
        return this.mSurfaceSizeDefinition;
    }

    private Size fetchMaxSize(int imageFormat) {
        Size size = this.mMaxSizeCache.get(Integer.valueOf(imageFormat));
        if (size != null) {
            return size;
        }
        Size maxSize = getMaxOutputSizeByFormat(imageFormat);
        this.mMaxSizeCache.put(Integer.valueOf(imageFormat), maxSize);
        return maxSize;
    }

    private List<Integer> getUseCasesPriorityOrder(List<UseCaseConfig<?>> newUseCaseConfigs) {
        List<Integer> priorityOrder = new ArrayList<>();
        List<Integer> priorityValueList = new ArrayList<>();
        for (UseCaseConfig<?> config : newUseCaseConfigs) {
            int priority = config.getSurfaceOccupancyPriority(0);
            if (!priorityValueList.contains(Integer.valueOf(priority))) {
                priorityValueList.add(Integer.valueOf(priority));
            }
        }
        Collections.sort(priorityValueList);
        Collections.reverse(priorityValueList);
        for (Integer num : priorityValueList) {
            int priorityValue = num.intValue();
            for (UseCaseConfig<?> config2 : newUseCaseConfigs) {
                if (priorityValue == config2.getSurfaceOccupancyPriority(0)) {
                    priorityOrder.add(Integer.valueOf(newUseCaseConfigs.indexOf(config2)));
                }
            }
        }
        return priorityOrder;
    }

    List<Size> getSupportedOutputSizes(UseCaseConfig<?> config) {
        Size targetSize;
        Rational aspectRatio;
        int imageFormat = config.getInputFormat();
        ImageOutputConfig imageOutputConfig = (ImageOutputConfig) config;
        Size[] outputSizes = getAllOutputSizesByFormat(imageFormat, imageOutputConfig);
        List<Size> outputSizeCandidates = new ArrayList<>();
        Size maxSize = imageOutputConfig.getMaxResolution(getMaxOutputSizeByFormat(imageFormat));
        int targetRotation = imageOutputConfig.getTargetRotation(0);
        Arrays.sort(outputSizes, new CompareSizesByArea(true));
        Size targetSize2 = imageOutputConfig.getTargetResolution(ZERO_SIZE);
        if (isRotationNeeded(targetRotation)) {
            targetSize2 = new Size(targetSize2.getHeight(), targetSize2.getWidth());
        }
        Size minSize = DEFAULT_SIZE;
        int defaultSizeArea = getArea(DEFAULT_SIZE);
        if (getArea(maxSize) < defaultSizeArea) {
            minSize = new Size(0, 0);
        } else if (!targetSize2.equals(ZERO_SIZE) && getArea(targetSize2) < defaultSizeArea) {
            minSize = targetSize2;
        }
        for (Size outputSize : outputSizes) {
            if (getArea(outputSize) <= getArea(maxSize) && getArea(outputSize) >= getArea(minSize)) {
                outputSizeCandidates.add(outputSize);
            }
        }
        if (!outputSizeCandidates.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            Rational aspectRatio2 = getCorrectedAspectRatio();
            if (aspectRatio2 == null) {
                if (imageOutputConfig.hasTargetAspectRatio()) {
                    boolean isSensorLandscapeOrientation = isRotationNeeded(0);
                    int targetAspectRatio = imageOutputConfig.getTargetAspectRatio();
                    if (targetAspectRatio == 0) {
                        aspectRatio2 = isSensorLandscapeOrientation ? ASPECT_RATIO_4_3 : ASPECT_RATIO_3_4;
                    } else if (targetAspectRatio == 1) {
                        if (isSensorLandscapeOrientation) {
                            aspectRatio = ASPECT_RATIO_16_9;
                        } else {
                            aspectRatio = ASPECT_RATIO_9_16;
                        }
                        aspectRatio2 = aspectRatio;
                    }
                } else {
                    aspectRatio2 = rotateAspectRatioByRotation(imageOutputConfig.getTargetAspectRatioCustom(null), targetRotation);
                }
            }
            for (Size outputSize2 : outputSizeCandidates) {
                if (aspectRatio2 == null || hasMatchingAspectRatio(outputSize2, aspectRatio2)) {
                    if (!arrayList.contains(outputSize2)) {
                        arrayList.add(outputSize2);
                    }
                } else if (!arrayList2.contains(outputSize2)) {
                    arrayList2.add(outputSize2);
                }
            }
            if (aspectRatio2 != null) {
                Collections.sort(arrayList2, new CompareSizesByDistanceToTargetRatio(Float.valueOf(aspectRatio2.floatValue())));
            }
            if (targetSize2.equals(ZERO_SIZE)) {
                targetSize = imageOutputConfig.getDefaultResolution(ZERO_SIZE);
            } else {
                targetSize = targetSize2;
            }
            if (!targetSize.equals(ZERO_SIZE)) {
                removeSupportedSizesByTargetSize(arrayList, targetSize);
                removeSupportedSizesByTargetSizeAndAspectRatio(arrayList2, targetSize);
            }
            List<Size> supportedResolutions = new ArrayList<>();
            supportedResolutions.addAll(arrayList);
            supportedResolutions.addAll(arrayList2);
            return supportedResolutions;
        }
        throw new IllegalArgumentException("Can not get supported output size under supported maximum for the format: " + imageFormat);
    }

    private Rational rotateAspectRatioByRotation(Rational aspectRatio, int targetRotation) {
        if (aspectRatio == null || !isRotationNeeded(targetRotation)) {
            return aspectRatio;
        }
        return new Rational(aspectRatio.getDenominator(), aspectRatio.getNumerator());
    }

    private boolean isRotationNeeded(int targetRotation) {
        int sensorRotationDegrees = CameraX.getCameraInfo(this.mCameraId).getSensorRotationDegrees(targetRotation);
        return sensorRotationDegrees == 90 || sensorRotationDegrees == 270;
    }

    static boolean hasMatchingAspectRatio(Size resolution, Rational aspectRatio) {
        if (aspectRatio == null) {
            return false;
        }
        if (aspectRatio.equals(new Rational(resolution.getWidth(), resolution.getHeight()))) {
            return true;
        }
        if (getArea(resolution) >= getArea(DEFAULT_SIZE)) {
            return isPossibleMod16FromAspectRatio(resolution, aspectRatio);
        }
        return false;
    }

    private static boolean isPossibleMod16FromAspectRatio(Size resolution, Rational aspectRatio) {
        int width = resolution.getWidth();
        int height = resolution.getHeight();
        Rational invAspectRatio = new Rational(aspectRatio.getDenominator(), aspectRatio.getNumerator());
        if (width % 16 == 0 && height % 16 == 0) {
            if (ratioIntersectsMod16Segment(Math.max(0, height - 16), width, aspectRatio) || ratioIntersectsMod16Segment(Math.max(0, width - 16), height, invAspectRatio)) {
                return true;
            }
            return false;
        } else if (width % 16 == 0) {
            return ratioIntersectsMod16Segment(height, width, aspectRatio);
        } else {
            if (height % 16 == 0) {
                return ratioIntersectsMod16Segment(width, height, invAspectRatio);
            }
            return false;
        }
    }

    private static int getArea(Size size) {
        return size.getWidth() * size.getHeight();
    }

    private static boolean ratioIntersectsMod16Segment(int height, int mod16Width, Rational aspectRatio) {
        Preconditions.checkArgument(mod16Width % 16 == 0);
        double aspectRatioWidth = ((double) (aspectRatio.getNumerator() * height)) / ((double) aspectRatio.getDenominator());
        return aspectRatioWidth > ((double) Math.max(0, mod16Width + -16)) && aspectRatioWidth < ((double) (mod16Width + 16));
    }

    private void removeSupportedSizesByTargetSizeAndAspectRatio(List<Size> supportedSizesList, Size targetSize) {
        if (!(supportedSizesList == null || supportedSizesList.isEmpty())) {
            Map<Rational, Size> bigEnoughSizeMap = new HashMap<>();
            List<Size> removeSizes = new ArrayList<>();
            Rational currentRationalKey = null;
            for (int i = 0; i < supportedSizesList.size(); i++) {
                Size outputSize = supportedSizesList.get(i);
                if (outputSize.getWidth() >= targetSize.getWidth() && outputSize.getHeight() >= targetSize.getHeight()) {
                    Rational rational = new Rational(outputSize.getWidth(), outputSize.getHeight());
                    if (currentRationalKey == null || !hasMatchingAspectRatio(outputSize, currentRationalKey)) {
                        currentRationalKey = rational;
                    }
                    Size originalBigEnoughSize = bigEnoughSizeMap.get(currentRationalKey);
                    if (originalBigEnoughSize != null) {
                        removeSizes.add(originalBigEnoughSize);
                    }
                    bigEnoughSizeMap.put(currentRationalKey, outputSize);
                }
            }
            supportedSizesList.removeAll(removeSizes);
        }
    }

    private void removeSupportedSizesByTargetSize(List<Size> supportedSizesList, Size targetSize) {
        if (!(supportedSizesList == null || supportedSizesList.isEmpty())) {
            int indexBigEnough = -1;
            List<Size> removeSizes = new ArrayList<>();
            for (int i = 0; i < supportedSizesList.size(); i++) {
                Size outputSize = supportedSizesList.get(i);
                if (outputSize.getWidth() < targetSize.getWidth() || outputSize.getHeight() < targetSize.getHeight()) {
                    break;
                }
                if (indexBigEnough >= 0) {
                    removeSizes.add(supportedSizesList.get(indexBigEnough));
                }
                indexBigEnough = i;
            }
            supportedSizesList.removeAll(removeSizes);
        }
    }

    /* JADX INFO: Multiple debug info for r2v4 int: [D('currentRunCount' int), D('i' int)] */
    private List<List<Size>> getAllPossibleSizeArrangements(List<List<Size>> supportedOutputSizesList) {
        int totalArrangementsCount = 1;
        for (List<Size> supportedOutputSizes : supportedOutputSizesList) {
            totalArrangementsCount *= supportedOutputSizes.size();
        }
        if (totalArrangementsCount != 0) {
            List<List<Size>> allPossibleSizeArrangements = new ArrayList<>();
            for (int i = 0; i < totalArrangementsCount; i++) {
                allPossibleSizeArrangements.add(new ArrayList<>());
            }
            int currentRunCount = totalArrangementsCount;
            int nextRunCount = currentRunCount / supportedOutputSizesList.get(0).size();
            for (int currentIndex = 0; currentIndex < supportedOutputSizesList.size(); currentIndex++) {
                List<Size> supportedOutputSizes2 = supportedOutputSizesList.get(currentIndex);
                for (int i2 = 0; i2 < totalArrangementsCount; i2++) {
                    allPossibleSizeArrangements.get(i2).add(supportedOutputSizes2.get((i2 % currentRunCount) / nextRunCount));
                }
                if (currentIndex < supportedOutputSizesList.size() - 1) {
                    currentRunCount = nextRunCount;
                    nextRunCount = currentRunCount / supportedOutputSizesList.get(currentIndex + 1).size();
                }
            }
            return allPossibleSizeArrangements;
        }
        throw new IllegalArgumentException("Failed to find supported resolutions.");
    }

    private Size[] getAllOutputSizesByFormat(int imageFormat) {
        return getAllOutputSizesByFormat(imageFormat, null);
    }

    private Size[] getAllOutputSizesByFormat(int imageFormat, ImageOutputConfig config) {
        Size[] outputSizes = null;
        List<Pair<Integer, Size[]>> formatResolutionsPairList = null;
        if (config != null) {
            formatResolutionsPairList = config.getSupportedResolutions(null);
        }
        if (formatResolutionsPairList != null) {
            Iterator<Pair<Integer, Size[]>> it = formatResolutionsPairList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<Integer, Size[]> formatResolutionPair = it.next();
                if (((Integer) formatResolutionPair.first).intValue() == imageFormat) {
                    outputSizes = (Size[]) formatResolutionPair.second;
                    break;
                }
            }
        }
        if (outputSizes == null) {
            StreamConfigurationMap map = (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                throw new IllegalArgumentException("Can not get supported output size for the format: " + imageFormat);
            } else if (Build.VERSION.SDK_INT >= 23 || imageFormat != 34) {
                outputSizes = map.getOutputSizes(imageFormat);
            } else {
                outputSizes = map.getOutputSizes(SurfaceTexture.class);
            }
        }
        if (outputSizes != null) {
            Collection<?> excludedSizes = fetchExcludedSizes(imageFormat);
            List<Size> resultSizesList = new ArrayList<>(Arrays.asList(outputSizes));
            resultSizesList.removeAll(excludedSizes);
            Size[] outputSizes2 = (Size[]) resultSizesList.toArray(new Size[0]);
            Arrays.sort(outputSizes2, new CompareSizesByArea(true));
            return outputSizes2;
        }
        throw new IllegalArgumentException("Can not get supported output size for the format: " + imageFormat);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Size getMaxOutputSizeByFormat(int imageFormat) {
        return (Size) Collections.max(Arrays.asList(getAllOutputSizesByFormat(imageFormat)), new CompareSizesByArea());
    }

    List<SurfaceCombination> getLegacySupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination2);
        SurfaceCombination surfaceCombination3 = new SurfaceCombination();
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination3);
        SurfaceCombination surfaceCombination4 = new SurfaceCombination();
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination4);
        SurfaceCombination surfaceCombination5 = new SurfaceCombination();
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination5);
        SurfaceCombination surfaceCombination6 = new SurfaceCombination();
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        combinationList.add(surfaceCombination6);
        SurfaceCombination surfaceCombination7 = new SurfaceCombination();
        surfaceCombination7.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination7.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        combinationList.add(surfaceCombination7);
        SurfaceCombination surfaceCombination8 = new SurfaceCombination();
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination8);
        return combinationList;
    }

    List<SurfaceCombination> getLimitedSupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.RECORD));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.RECORD));
        combinationList.add(surfaceCombination2);
        SurfaceCombination surfaceCombination3 = new SurfaceCombination();
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.RECORD));
        combinationList.add(surfaceCombination3);
        SurfaceCombination surfaceCombination4 = new SurfaceCombination();
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.RECORD));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.RECORD));
        combinationList.add(surfaceCombination4);
        SurfaceCombination surfaceCombination5 = new SurfaceCombination();
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.RECORD));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.RECORD));
        combinationList.add(surfaceCombination5);
        SurfaceCombination surfaceCombination6 = new SurfaceCombination();
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination6);
        return combinationList;
    }

    List<SurfaceCombination> getFullSupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination2);
        SurfaceCombination surfaceCombination3 = new SurfaceCombination();
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination3);
        SurfaceCombination surfaceCombination4 = new SurfaceCombination();
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination4);
        SurfaceCombination surfaceCombination5 = new SurfaceCombination();
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.ANALYSIS));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination5);
        SurfaceCombination surfaceCombination6 = new SurfaceCombination();
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.ANALYSIS));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination6);
        return combinationList;
    }

    List<SurfaceCombination> getRAWSupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination2);
        SurfaceCombination surfaceCombination3 = new SurfaceCombination();
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination3);
        SurfaceCombination surfaceCombination4 = new SurfaceCombination();
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination4.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination4);
        SurfaceCombination surfaceCombination5 = new SurfaceCombination();
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination5.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination5);
        SurfaceCombination surfaceCombination6 = new SurfaceCombination();
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination6.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination6);
        SurfaceCombination surfaceCombination7 = new SurfaceCombination();
        surfaceCombination7.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination7.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        surfaceCombination7.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination7);
        SurfaceCombination surfaceCombination8 = new SurfaceCombination();
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        surfaceCombination8.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination8);
        return combinationList;
    }

    List<SurfaceCombination> getBurstSupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination2);
        SurfaceCombination surfaceCombination3 = new SurfaceCombination();
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination3.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination3);
        return combinationList;
    }

    List<SurfaceCombination> getLevel3SupportedCombinationList() {
        List<SurfaceCombination> combinationList = new ArrayList<>();
        SurfaceCombination surfaceCombination1 = new SurfaceCombination();
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.ANALYSIS));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.YUV, SurfaceConfig.ConfigSize.MAXIMUM));
        surfaceCombination1.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination1);
        SurfaceCombination surfaceCombination2 = new SurfaceCombination();
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.PREVIEW));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.PRIV, SurfaceConfig.ConfigSize.ANALYSIS));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.JPEG, SurfaceConfig.ConfigSize.MAXIMUM));
        surfaceCombination2.addSurfaceConfig(SurfaceConfig.create(SurfaceConfig.ConfigType.RAW, SurfaceConfig.ConfigSize.MAXIMUM));
        combinationList.add(surfaceCombination2);
        return combinationList;
    }

    private void generateSupportedCombinationList() {
        this.mSurfaceCombinations.addAll(getLegacySupportedCombinationList());
        int i = this.mHardwareLevel;
        if (i == 0 || i == 1 || i == 3) {
            this.mSurfaceCombinations.addAll(getLimitedSupportedCombinationList());
        }
        int i2 = this.mHardwareLevel;
        if (i2 == 1 || i2 == 3) {
            this.mSurfaceCombinations.addAll(getFullSupportedCombinationList());
        }
        int[] availableCapabilities = (int[]) this.mCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        if (availableCapabilities != null) {
            for (int capability : availableCapabilities) {
                if (capability == 3) {
                    this.mIsRawSupported = true;
                } else if (capability == 6) {
                    this.mIsBurstCaptureSupported = true;
                }
            }
        }
        if (this.mIsRawSupported) {
            this.mSurfaceCombinations.addAll(getRAWSupportedCombinationList());
        }
        if (this.mIsBurstCaptureSupported && this.mHardwareLevel == 0) {
            this.mSurfaceCombinations.addAll(getBurstSupportedCombinationList());
        }
        if (this.mHardwareLevel == 3) {
            this.mSurfaceCombinations.addAll(getLevel3SupportedCombinationList());
        }
    }

    private void checkCustomization() {
    }

    private void generateSurfaceSizeDefinition(WindowManager windowManager) {
        this.mSurfaceSizeDefinition = SurfaceSizeDefinition.create(new Size(640, 480), getPreviewSize(windowManager), getRecordSize());
    }

    private Size getPreviewSize(WindowManager windowManager) {
        Size displayViewSize;
        Point displaySize = new Point();
        windowManager.getDefaultDisplay().getRealSize(displaySize);
        if (displaySize.x > displaySize.y) {
            displayViewSize = new Size(displaySize.x, displaySize.y);
        } else {
            displayViewSize = new Size(displaySize.y, displaySize.x);
        }
        return (Size) Collections.min(Arrays.asList(new Size(displayViewSize.getWidth(), displayViewSize.getHeight()), MAX_PREVIEW_SIZE), new CompareSizesByArea());
    }

    private Size getRecordSize() {
        Size recordSize = QUALITY_480P_SIZE;
        if (this.mCamcorderProfileHelper.hasProfile(Integer.parseInt(this.mCameraId), 8)) {
            return QUALITY_2160P_SIZE;
        }
        if (this.mCamcorderProfileHelper.hasProfile(Integer.parseInt(this.mCameraId), 6)) {
            return QUALITY_1080P_SIZE;
        }
        if (this.mCamcorderProfileHelper.hasProfile(Integer.parseInt(this.mCameraId), 5)) {
            return QUALITY_720P_SIZE;
        }
        if (this.mCamcorderProfileHelper.hasProfile(Integer.parseInt(this.mCameraId), 4)) {
            return QUALITY_480P_SIZE;
        }
        return recordSize;
    }

    private List<Size> fetchExcludedSizes(int imageFormat) {
        List<Size> excludedSizes = this.mExcludedSizeListCache.get(Integer.valueOf(imageFormat));
        if (excludedSizes != null) {
            return excludedSizes;
        }
        List<Size> excludedSizes2 = SupportedSizeConstraints.getExcludedSizes(this.mCameraId, imageFormat);
        this.mExcludedSizeListCache.put(Integer.valueOf(imageFormat), excludedSizes2);
        return excludedSizes2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CompareSizesByArea implements Comparator<Size> {
        private boolean mReverse;

        CompareSizesByArea() {
            this.mReverse = false;
        }

        CompareSizesByArea(boolean reverse) {
            this.mReverse = false;
            this.mReverse = reverse;
        }

        public int compare(Size lhs, Size rhs) {
            int result = Long.signum((((long) lhs.getWidth()) * ((long) lhs.getHeight())) - (((long) rhs.getWidth()) * ((long) rhs.getHeight())));
            if (this.mReverse) {
                return result * -1;
            }
            return result;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class CompareSizesByDistanceToTargetRatio implements Comparator<Size> {
        private Float mTargetRatio;

        CompareSizesByDistanceToTargetRatio(Float targetRatio) {
            this.mTargetRatio = targetRatio;
        }

        public int compare(Size lhs, Size rhs) {
            if (SupportedSurfaceCombination.hasMatchingAspectRatio(lhs, new Rational(rhs.getWidth(), rhs.getHeight()))) {
                return 0;
            }
            Float lhsRatio = Float.valueOf((((float) lhs.getWidth()) * 1.0f) / ((float) lhs.getHeight()));
            Float rhsRatio = Float.valueOf((((float) rhs.getWidth()) * 1.0f) / ((float) rhs.getHeight()));
            return (int) Math.signum(Float.valueOf(Math.abs(lhsRatio.floatValue() - this.mTargetRatio.floatValue())).floatValue() - Float.valueOf(Math.abs(rhsRatio.floatValue() - this.mTargetRatio.floatValue())).floatValue());
        }
    }
}
