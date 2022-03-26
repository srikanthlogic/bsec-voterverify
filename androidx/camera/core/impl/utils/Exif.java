package androidx.camera.core.impl.utils;

import android.location.Location;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface;
import com.facebook.imagepipeline.common.RotationOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/* loaded from: classes.dex */
public final class Exif {
    public static final long INVALID_TIMESTAMP = -1;
    private static final String KILOMETERS_PER_HOUR = "K";
    private static final String KNOTS = "N";
    private static final String MILES_PER_HOUR = "M";
    private final ExifInterface mExifInterface;
    private boolean mRemoveTimestamp = false;
    private static final String TAG = Exif.class.getSimpleName();
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() { // from class: androidx.camera.core.impl.utils.Exif.1
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy:MM:dd", Locale.US);
        }
    };
    private static final ThreadLocal<SimpleDateFormat> TIME_FORMAT = new ThreadLocal<SimpleDateFormat>() { // from class: androidx.camera.core.impl.utils.Exif.2
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss", Locale.US);
        }
    };
    private static final ThreadLocal<SimpleDateFormat> DATETIME_FORMAT = new ThreadLocal<SimpleDateFormat>() { // from class: androidx.camera.core.impl.utils.Exif.3
        @Override // java.lang.ThreadLocal
        public SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        }
    };

    private Exif(ExifInterface exifInterface) {
        this.mExifInterface = exifInterface;
    }

    public static Exif createFromFile(File file) throws IOException {
        return createFromFileString(file.toString());
    }

    public static Exif createFromFileString(String filePath) throws IOException {
        return new Exif(new ExifInterface(filePath));
    }

    public static Exif createFromInputStream(InputStream is) throws IOException {
        return new Exif(new ExifInterface(is));
    }

    private static String convertToExifDateTime(long timestamp) {
        return DATETIME_FORMAT.get().format(new Date(timestamp));
    }

    private static Date convertFromExifDateTime(String dateTime) throws ParseException {
        return DATETIME_FORMAT.get().parse(dateTime);
    }

    private static Date convertFromExifDate(String date) throws ParseException {
        return DATE_FORMAT.get().parse(date);
    }

    private static Date convertFromExifTime(String time) throws ParseException {
        return TIME_FORMAT.get().parse(time);
    }

    public void save() throws IOException {
        if (!this.mRemoveTimestamp) {
            attachLastModifiedTimestamp();
        }
        this.mExifInterface.saveAttributes();
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "Exif{width=%s, height=%s, rotation=%d, isFlippedVertically=%s, isFlippedHorizontally=%s, location=%s, timestamp=%s, description=%s}", Integer.valueOf(getWidth()), Integer.valueOf(getHeight()), Integer.valueOf(getRotation()), Boolean.valueOf(isFlippedVertically()), Boolean.valueOf(isFlippedHorizontally()), getLocation(), Long.valueOf(getTimestamp()), getDescription());
    }

    public int getOrientation() {
        return this.mExifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
    }

    public void setOrientation(int orientation) {
        this.mExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
    }

    public int getWidth() {
        return this.mExifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
    }

    public int getHeight() {
        return this.mExifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
    }

    public String getDescription() {
        return this.mExifInterface.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
    }

    public void setDescription(String description) {
        this.mExifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, description);
    }

    public int getRotation() {
        switch (getOrientation()) {
            case 2:
                return 0;
            case 3:
                return RotationOptions.ROTATE_180;
            case 4:
                return RotationOptions.ROTATE_180;
            case 5:
                return 270;
            case 6:
                return 90;
            case 7:
                return 90;
            case 8:
                return 270;
            default:
                return 0;
        }
    }

    public boolean isFlippedVertically() {
        switch (getOrientation()) {
            case 2:
                return false;
            case 3:
                return false;
            case 4:
                return true;
            case 5:
                return true;
            case 6:
                return false;
            case 7:
                return true;
            case 8:
                return false;
            default:
                return false;
        }
    }

    public boolean isFlippedHorizontally() {
        switch (getOrientation()) {
            case 2:
                return true;
            case 3:
                return false;
            case 4:
                return false;
            case 5:
                return false;
            case 6:
                return false;
            case 7:
                return false;
            case 8:
                return false;
            default:
                return false;
        }
    }

    private void attachLastModifiedTimestamp() {
        long now = System.currentTimeMillis();
        String datetime = convertToExifDateTime(now);
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME, datetime);
        try {
            this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME, Long.toString(now - convertFromExifDateTime(datetime).getTime()));
        } catch (ParseException e) {
        }
    }

    public long getLastModifiedTimestamp() {
        long timestamp = parseTimestamp(this.mExifInterface.getAttribute(ExifInterface.TAG_DATETIME));
        if (timestamp == -1) {
            return -1;
        }
        String subSecs = this.mExifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
        if (subSecs == null) {
            return timestamp;
        }
        try {
            long sub = Long.parseLong(subSecs);
            while (sub > 1000) {
                sub /= 10;
            }
            return timestamp + sub;
        } catch (NumberFormatException e) {
            return timestamp;
        }
    }

    public long getTimestamp() {
        long timestamp = parseTimestamp(this.mExifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL));
        if (timestamp == -1) {
            return -1;
        }
        String subSecs = this.mExifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIGINAL);
        if (subSecs == null) {
            return timestamp;
        }
        try {
            long sub = Long.parseLong(subSecs);
            while (sub > 1000) {
                sub /= 10;
            }
            return timestamp + sub;
        } catch (NumberFormatException e) {
            return timestamp;
        }
    }

    public Location getLocation() {
        double speed;
        String provider = this.mExifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);
        double[] latlng = this.mExifInterface.getLatLong();
        double altitude = this.mExifInterface.getAltitude(0.0d);
        double speed2 = this.mExifInterface.getAttributeDouble(ExifInterface.TAG_GPS_SPEED, 0.0d);
        String speedRef = this.mExifInterface.getAttribute(ExifInterface.TAG_GPS_SPEED_REF);
        String speedRef2 = speedRef == null ? "K" : speedRef;
        long timestamp = parseTimestamp(this.mExifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP), this.mExifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP));
        if (latlng == null) {
            return null;
        }
        if (provider == null) {
            provider = TAG;
        }
        Location location = new Location(provider);
        location.setLatitude(latlng[0]);
        location.setLongitude(latlng[1]);
        if (altitude != 0.0d) {
            location.setAltitude(altitude);
        }
        if (speed2 != 0.0d) {
            char c = 65535;
            int hashCode = speedRef2.hashCode();
            if (hashCode != 75) {
                if (hashCode != 77) {
                    if (hashCode == 78 && speedRef2.equals("N")) {
                        c = 1;
                    }
                } else if (speedRef2.equals("M")) {
                    c = 0;
                }
            } else if (speedRef2.equals("K")) {
                c = 2;
            }
            if (c == 0) {
                speed = Speed.fromMilesPerHour(speed2).toMetersPerSecond();
            } else if (c != 1) {
                speed = Speed.fromKilometersPerHour(speed2).toMetersPerSecond();
            } else {
                speed = Speed.fromKnots(speed2).toMetersPerSecond();
            }
            location.setSpeed((float) speed);
        }
        if (timestamp != -1) {
            location.setTime(timestamp);
        }
        return location;
    }

    public void rotate(int degrees) {
        if (degrees % 90 != 0) {
            Log.w(TAG, String.format("Can only rotate in right angles (eg. 0, 90, 180, 270). %d is unsupported.", Integer.valueOf(degrees)));
            this.mExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(0));
            return;
        }
        int degrees2 = degrees % 360;
        int orientation = getOrientation();
        while (degrees2 < 0) {
            degrees2 += 90;
            switch (orientation) {
                case 2:
                    orientation = 5;
                    break;
                case 3:
                    orientation = 6;
                    break;
                case 4:
                    orientation = 7;
                    break;
                case 5:
                    orientation = 4;
                    break;
                case 6:
                    orientation = 1;
                    break;
                case 7:
                    orientation = 2;
                    break;
                case 8:
                    orientation = 6;
                    break;
                default:
                    orientation = 8;
                    break;
            }
        }
        while (degrees2 > 0) {
            degrees2 -= 90;
            switch (orientation) {
                case 2:
                    orientation = 7;
                    break;
                case 3:
                    orientation = 8;
                    break;
                case 4:
                    orientation = 5;
                    break;
                case 5:
                    orientation = 2;
                    break;
                case 6:
                    orientation = 3;
                    break;
                case 7:
                    orientation = 4;
                    break;
                case 8:
                    orientation = 1;
                    break;
                default:
                    orientation = 6;
                    break;
            }
        }
        this.mExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
    }

    public void flipVertically() {
        int orientation;
        switch (getOrientation()) {
            case 2:
                orientation = 3;
                break;
            case 3:
                orientation = 2;
                break;
            case 4:
                orientation = 1;
                break;
            case 5:
                orientation = 8;
                break;
            case 6:
                orientation = 7;
                break;
            case 7:
                orientation = 6;
                break;
            case 8:
                orientation = 5;
                break;
            default:
                orientation = 4;
                break;
        }
        this.mExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
    }

    public void flipHorizontally() {
        int orientation;
        switch (getOrientation()) {
            case 2:
                orientation = 1;
                break;
            case 3:
                orientation = 4;
                break;
            case 4:
                orientation = 3;
                break;
            case 5:
                orientation = 6;
                break;
            case 6:
                orientation = 5;
                break;
            case 7:
                orientation = 8;
                break;
            case 8:
                orientation = 7;
                break;
            default:
                orientation = 2;
                break;
        }
        this.mExifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
    }

    public void attachTimestamp() {
        long now = System.currentTimeMillis();
        String datetime = convertToExifDateTime(now);
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, datetime);
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, datetime);
        try {
            String subsec = Long.toString(now - convertFromExifDateTime(datetime).getTime());
            this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIGINAL, subsec);
            this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, subsec);
        } catch (ParseException e) {
        }
        this.mRemoveTimestamp = false;
    }

    public void removeTimestamp() {
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME_ORIGINAL, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_DATETIME_DIGITIZED, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIGINAL, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED, null);
        this.mRemoveTimestamp = true;
    }

    public void attachLocation(Location location) {
        this.mExifInterface.setGpsInfo(location);
    }

    public void removeLocation() {
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_SPEED, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_SPEED_REF, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_DATESTAMP, null);
        this.mExifInterface.setAttribute(ExifInterface.TAG_GPS_TIMESTAMP, null);
    }

    private long parseTimestamp(String date, String time) {
        if (date == null && time == null) {
            return -1;
        }
        if (time == null) {
            try {
                return convertFromExifDate(date).getTime();
            } catch (ParseException e) {
                return -1;
            }
        } else if (date == null) {
            try {
                return convertFromExifTime(time).getTime();
            } catch (ParseException e2) {
                return -1;
            }
        } else {
            return parseTimestamp(date + " " + time);
        }
    }

    private long parseTimestamp(String datetime) {
        if (datetime == null) {
            return -1;
        }
        try {
            return convertFromExifDateTime(datetime).getTime();
        } catch (ParseException e) {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Speed {
        private Speed() {
        }

        static Converter fromKilometersPerHour(double kph) {
            return new Converter(0.621371d * kph);
        }

        static Converter fromMetersPerSecond(double mps) {
            return new Converter(2.23694d * mps);
        }

        static Converter fromMilesPerHour(double mph) {
            return new Converter(mph);
        }

        static Converter fromKnots(double knots) {
            return new Converter(1.15078d * knots);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static final class Converter {
            final double mMph;

            Converter(double mph) {
                this.mMph = mph;
            }

            double toKilometersPerHour() {
                return this.mMph / 0.621371d;
            }

            double toMilesPerHour() {
                return this.mMph;
            }

            double toKnots() {
                return this.mMph / 1.15078d;
            }

            double toMetersPerSecond() {
                return this.mMph / 2.23694d;
            }
        }
    }
}
