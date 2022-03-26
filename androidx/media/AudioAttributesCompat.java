package androidx.media;

import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.versionedparcelable.VersionedParcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class AudioAttributesCompat implements VersionedParcelable {
    static final String AUDIO_ATTRIBUTES_CONTENT_TYPE;
    static final String AUDIO_ATTRIBUTES_FLAGS;
    static final String AUDIO_ATTRIBUTES_FRAMEWORKS;
    static final String AUDIO_ATTRIBUTES_LEGACY_STREAM_TYPE;
    static final String AUDIO_ATTRIBUTES_USAGE;
    public static final int CONTENT_TYPE_MOVIE;
    public static final int CONTENT_TYPE_MUSIC;
    public static final int CONTENT_TYPE_SONIFICATION;
    public static final int CONTENT_TYPE_SPEECH;
    public static final int CONTENT_TYPE_UNKNOWN;
    static final int FLAG_ALL;
    static final int FLAG_ALL_PUBLIC;
    public static final int FLAG_AUDIBILITY_ENFORCED;
    static final int FLAG_BEACON;
    static final int FLAG_BYPASS_INTERRUPTION_POLICY;
    static final int FLAG_BYPASS_MUTE;
    static final int FLAG_DEEP_BUFFER;
    public static final int FLAG_HW_AV_SYNC;
    static final int FLAG_HW_HOTWORD;
    static final int FLAG_LOW_LATENCY;
    static final int FLAG_SCO;
    static final int FLAG_SECURE;
    static final int INVALID_STREAM_TYPE;
    private static final int SUPPRESSIBLE_CALL;
    private static final int SUPPRESSIBLE_NOTIFICATION;
    private static final String TAG;
    public static final int USAGE_ALARM;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE;
    public static final int USAGE_ASSISTANCE_SONIFICATION;
    public static final int USAGE_ASSISTANT;
    public static final int USAGE_GAME;
    public static final int USAGE_MEDIA;
    public static final int USAGE_NOTIFICATION;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST;
    public static final int USAGE_NOTIFICATION_EVENT;
    public static final int USAGE_NOTIFICATION_RINGTONE;
    public static final int USAGE_UNKNOWN;
    private static final int USAGE_VIRTUAL_SOURCE;
    public static final int USAGE_VOICE_COMMUNICATION;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING;
    static boolean sForceLegacyBehavior;
    AudioAttributesImpl mImpl;
    private static final SparseIntArray SUPPRESSIBLE_USAGES = new SparseIntArray();
    private static final int[] SDK_USAGES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AttributeContentType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AttributeUsage {
    }

    static {
        SUPPRESSIBLE_USAGES.put(5, 1);
        SUPPRESSIBLE_USAGES.put(6, 2);
        SUPPRESSIBLE_USAGES.put(7, 2);
        SUPPRESSIBLE_USAGES.put(8, 1);
        SUPPRESSIBLE_USAGES.put(9, 1);
        SUPPRESSIBLE_USAGES.put(10, 1);
    }

    public AudioAttributesCompat() {
    }

    AudioAttributesCompat(AudioAttributesImpl impl) {
        this.mImpl = impl;
    }

    public int getVolumeControlStream() {
        return this.mImpl.getVolumeControlStream();
    }

    public Object unwrap() {
        return this.mImpl.getAudioAttributes();
    }

    public int getLegacyStreamType() {
        return this.mImpl.getLegacyStreamType();
    }

    public static AudioAttributesCompat wrap(Object aa) {
        if (Build.VERSION.SDK_INT < 21 || sForceLegacyBehavior) {
            return null;
        }
        AudioAttributesImpl impl = new AudioAttributesImplApi21((AudioAttributes) aa);
        AudioAttributesCompat aac = new AudioAttributesCompat();
        aac.mImpl = impl;
        return aac;
    }

    public int getContentType() {
        return this.mImpl.getContentType();
    }

    public int getUsage() {
        return this.mImpl.getUsage();
    }

    public int getFlags() {
        return this.mImpl.getFlags();
    }

    public Bundle toBundle() {
        return this.mImpl.toBundle();
    }

    public static AudioAttributesCompat fromBundle(Bundle bundle) {
        AudioAttributesImpl impl;
        if (Build.VERSION.SDK_INT >= 21) {
            impl = AudioAttributesImplApi21.fromBundle(bundle);
        } else {
            impl = AudioAttributesImplBase.fromBundle(bundle);
        }
        if (impl == null) {
            return null;
        }
        return new AudioAttributesCompat(impl);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private int mContentType;
        private int mFlags;
        private int mLegacyStream;
        private int mUsage;

        public Builder() {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
        }

        public Builder(AudioAttributesCompat aa) {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mLegacyStream = -1;
            this.mUsage = aa.getUsage();
            this.mContentType = aa.getContentType();
            this.mFlags = aa.getFlags();
            this.mLegacyStream = aa.getRawLegacyStreamType();
        }

        public AudioAttributesCompat build() {
            AudioAttributesImpl impl;
            if (AudioAttributesCompat.sForceLegacyBehavior || Build.VERSION.SDK_INT < 21) {
                impl = new AudioAttributesImplBase(this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream);
            } else {
                AudioAttributes.Builder api21Builder = new AudioAttributes.Builder().setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
                int i = this.mLegacyStream;
                if (i != -1) {
                    api21Builder.setLegacyStreamType(i);
                }
                impl = new AudioAttributesImplApi21(api21Builder.build(), this.mLegacyStream);
            }
            return new AudioAttributesCompat(impl);
        }

        public Builder setUsage(int usage) {
            switch (usage) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    this.mUsage = usage;
                    break;
                case 16:
                    if (!AudioAttributesCompat.sForceLegacyBehavior && Build.VERSION.SDK_INT > 25) {
                        this.mUsage = usage;
                        break;
                    } else {
                        this.mUsage = 12;
                        break;
                    }
                default:
                    this.mUsage = 0;
                    break;
            }
            return this;
        }

        public Builder setContentType(int contentType) {
            if (contentType == 0 || contentType == 1 || contentType == 2 || contentType == 3 || contentType == 4) {
                this.mContentType = contentType;
            } else {
                this.mUsage = 0;
            }
            return this;
        }

        public Builder setFlags(int flags) {
            this.mFlags |= flags & AudioAttributesCompat.FLAG_ALL;
            return this;
        }

        public Builder setLegacyStreamType(int streamType) {
            if (streamType != 10) {
                this.mLegacyStream = streamType;
                if (Build.VERSION.SDK_INT >= 21) {
                    return setInternalLegacyStreamType(streamType);
                }
                return this;
            }
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        Builder setInternalLegacyStreamType(int streamType) {
            switch (streamType) {
                case 0:
                    this.mContentType = 1;
                    break;
                case 1:
                    this.mContentType = 4;
                    break;
                case 2:
                    this.mContentType = 4;
                    break;
                case 3:
                    this.mContentType = 2;
                    break;
                case 4:
                    this.mContentType = 4;
                    break;
                case 5:
                    this.mContentType = 4;
                    break;
                case 6:
                    this.mContentType = 1;
                    this.mFlags |= 4;
                    break;
                case 7:
                    this.mFlags = 1 | this.mFlags;
                    this.mContentType = 4;
                    break;
                case 8:
                    this.mContentType = 4;
                    break;
                case 9:
                    this.mContentType = 4;
                    break;
                case 10:
                    this.mContentType = 1;
                    break;
                default:
                    Log.e(AudioAttributesCompat.TAG, "Invalid stream type " + streamType + " for AudioAttributesCompat");
                    break;
            }
            this.mUsage = AudioAttributesCompat.usageForStreamType(streamType);
            return this;
        }
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public static String usageToString(int usage) {
        switch (usage) {
            case 0:
                return "USAGE_UNKNOWN";
            case 1:
                return "USAGE_MEDIA";
            case 2:
                return "USAGE_VOICE_COMMUNICATION";
            case 3:
                return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
            case 4:
                return "USAGE_ALARM";
            case 5:
                return "USAGE_NOTIFICATION";
            case 6:
                return "USAGE_NOTIFICATION_RINGTONE";
            case 7:
                return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
            case 8:
                return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
            case 9:
                return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
            case 10:
                return "USAGE_NOTIFICATION_EVENT";
            case 11:
                return "USAGE_ASSISTANCE_ACCESSIBILITY";
            case 12:
                return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
            case 13:
                return "USAGE_ASSISTANCE_SONIFICATION";
            case 14:
                return "USAGE_GAME";
            case 15:
            default:
                return "unknown usage " + usage;
            case 16:
                return "USAGE_ASSISTANT";
        }
    }

    /* loaded from: classes.dex */
    static abstract class AudioManagerHidden {
        public static final int STREAM_ACCESSIBILITY;
        public static final int STREAM_BLUETOOTH_SCO;
        public static final int STREAM_SYSTEM_ENFORCED;
        public static final int STREAM_TTS;

        private AudioManagerHidden() {
        }
    }

    static int usageForStreamType(int streamType) {
        switch (streamType) {
            case 0:
                return 2;
            case 1:
            case 7:
                return 13;
            case 2:
                return 6;
            case 3:
                return 1;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 2;
            case 8:
                return 3;
            case 9:
            default:
                return 0;
            case 10:
                return 11;
        }
    }

    public static void setForceLegacyBehavior(boolean force) {
        sForceLegacyBehavior = force;
    }

    static int toVolumeStreamType(boolean fromGetVolumeControlStream, AudioAttributesCompat aa) {
        return toVolumeStreamType(fromGetVolumeControlStream, aa.getFlags(), aa.getUsage());
    }

    int getRawLegacyStreamType() {
        return this.mImpl.getRawLegacyStreamType();
    }

    public static int toVolumeStreamType(boolean fromGetVolumeControlStream, int flags, int usage) {
        if ((flags & 1) == 1) {
            if (fromGetVolumeControlStream) {
                return 1;
            }
            return 7;
        } else if ((flags & 4) != 4) {
            switch (usage) {
                case 0:
                    if (fromGetVolumeControlStream) {
                        return Integer.MIN_VALUE;
                    }
                    return 3;
                case 1:
                case 12:
                case 14:
                case 16:
                    return 3;
                case 2:
                    return 0;
                case 3:
                    if (fromGetVolumeControlStream) {
                        return 0;
                    }
                    return 8;
                case 4:
                    return 4;
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                    return 5;
                case 6:
                    return 2;
                case 11:
                    return 10;
                case 13:
                    return 1;
                case 15:
                default:
                    if (!fromGetVolumeControlStream) {
                        return 3;
                    }
                    throw new IllegalArgumentException("Unknown usage value " + usage + " in audio attributes");
            }
        } else if (fromGetVolumeControlStream) {
            return 0;
        } else {
            return 6;
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof AudioAttributesCompat)) {
            return false;
        }
        AudioAttributesCompat that = (AudioAttributesCompat) o;
        AudioAttributesImpl audioAttributesImpl = this.mImpl;
        if (audioAttributesImpl != null) {
            return audioAttributesImpl.equals(that.mImpl);
        }
        if (that.mImpl == null) {
            return true;
        }
        return false;
    }
}
