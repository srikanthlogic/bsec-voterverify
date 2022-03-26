package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Person;
import android.app.RemoteInput;
import android.content.Context;
import android.content.LocusId;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.SparseArray;
import android.widget.RemoteViews;
import androidx.core.R;
import androidx.core.app.Person;
import androidx.core.content.LocusIdCompat;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.text.BidiFormatter;
import androidx.core.view.ViewCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.IOUtils;
/* loaded from: classes.dex */
public class NotificationCompat {
    public static final int BADGE_ICON_LARGE;
    public static final int BADGE_ICON_NONE;
    public static final int BADGE_ICON_SMALL;
    public static final String CATEGORY_ALARM;
    public static final String CATEGORY_CALL;
    public static final String CATEGORY_EMAIL;
    public static final String CATEGORY_ERROR;
    public static final String CATEGORY_EVENT;
    public static final String CATEGORY_LOCATION_SHARING;
    public static final String CATEGORY_MESSAGE;
    public static final String CATEGORY_MISSED_CALL;
    public static final String CATEGORY_NAVIGATION;
    public static final String CATEGORY_PROGRESS;
    public static final String CATEGORY_PROMO;
    public static final String CATEGORY_RECOMMENDATION;
    public static final String CATEGORY_REMINDER;
    public static final String CATEGORY_SERVICE;
    public static final String CATEGORY_SOCIAL;
    public static final String CATEGORY_STATUS;
    public static final String CATEGORY_STOPWATCH;
    public static final String CATEGORY_SYSTEM;
    public static final String CATEGORY_TRANSPORT;
    public static final String CATEGORY_WORKOUT;
    public static final int COLOR_DEFAULT;
    public static final int DEFAULT_ALL;
    public static final int DEFAULT_LIGHTS;
    public static final int DEFAULT_SOUND;
    public static final int DEFAULT_VIBRATE;
    public static final String EXTRA_AUDIO_CONTENTS_URI;
    public static final String EXTRA_BACKGROUND_IMAGE_URI;
    public static final String EXTRA_BIG_TEXT;
    public static final String EXTRA_CHANNEL_GROUP_ID;
    public static final String EXTRA_CHANNEL_ID;
    public static final String EXTRA_CHRONOMETER_COUNT_DOWN;
    public static final String EXTRA_COLORIZED;
    public static final String EXTRA_COMPACT_ACTIONS;
    public static final String EXTRA_COMPAT_TEMPLATE;
    public static final String EXTRA_CONVERSATION_TITLE;
    public static final String EXTRA_HIDDEN_CONVERSATION_TITLE;
    public static final String EXTRA_HISTORIC_MESSAGES;
    public static final String EXTRA_INFO_TEXT;
    public static final String EXTRA_IS_GROUP_CONVERSATION;
    public static final String EXTRA_LARGE_ICON;
    public static final String EXTRA_LARGE_ICON_BIG;
    public static final String EXTRA_MEDIA_SESSION;
    public static final String EXTRA_MESSAGES;
    public static final String EXTRA_MESSAGING_STYLE_USER;
    public static final String EXTRA_NOTIFICATION_ID;
    public static final String EXTRA_NOTIFICATION_TAG;
    @Deprecated
    public static final String EXTRA_PEOPLE;
    public static final String EXTRA_PEOPLE_LIST;
    public static final String EXTRA_PICTURE;
    public static final String EXTRA_PROGRESS;
    public static final String EXTRA_PROGRESS_INDETERMINATE;
    public static final String EXTRA_PROGRESS_MAX;
    public static final String EXTRA_REMOTE_INPUT_HISTORY;
    public static final String EXTRA_SELF_DISPLAY_NAME;
    public static final String EXTRA_SHOW_CHRONOMETER;
    public static final String EXTRA_SHOW_WHEN;
    public static final String EXTRA_SMALL_ICON;
    public static final String EXTRA_SUB_TEXT;
    public static final String EXTRA_SUMMARY_TEXT;
    public static final String EXTRA_TEMPLATE;
    public static final String EXTRA_TEXT;
    public static final String EXTRA_TEXT_LINES;
    public static final String EXTRA_TITLE;
    public static final String EXTRA_TITLE_BIG;
    public static final int FLAG_AUTO_CANCEL;
    public static final int FLAG_BUBBLE;
    public static final int FLAG_FOREGROUND_SERVICE;
    public static final int FLAG_GROUP_SUMMARY;
    @Deprecated
    public static final int FLAG_HIGH_PRIORITY;
    public static final int FLAG_INSISTENT;
    public static final int FLAG_LOCAL_ONLY;
    public static final int FLAG_NO_CLEAR;
    public static final int FLAG_ONGOING_EVENT;
    public static final int FLAG_ONLY_ALERT_ONCE;
    public static final int FLAG_SHOW_LIGHTS;
    public static final int GROUP_ALERT_ALL;
    public static final int GROUP_ALERT_CHILDREN;
    public static final int GROUP_ALERT_SUMMARY;
    public static final String GROUP_KEY_SILENT;
    public static final String INTENT_CATEGORY_NOTIFICATION_PREFERENCES;
    public static final int PRIORITY_DEFAULT;
    public static final int PRIORITY_HIGH;
    public static final int PRIORITY_LOW;
    public static final int PRIORITY_MAX;
    public static final int PRIORITY_MIN;
    public static final int STREAM_DEFAULT;
    public static final int VISIBILITY_PRIVATE;
    public static final int VISIBILITY_PUBLIC;
    public static final int VISIBILITY_SECRET;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BadgeIconType {
    }

    /* loaded from: classes.dex */
    public interface Extender {
        Builder extend(Builder builder);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface GroupAlertBehavior {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NotificationVisibility {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StreamType {
    }

    /* loaded from: classes.dex */
    public static class Builder {
        private static final int MAX_CHARSEQUENCE_LENGTH;
        public ArrayList<Action> mActions;
        boolean mAllowSystemGeneratedContextualActions;
        int mBadgeIcon;
        RemoteViews mBigContentView;
        BubbleMetadata mBubbleMetadata;
        String mCategory;
        String mChannelId;
        boolean mChronometerCountDown;
        int mColor;
        boolean mColorized;
        boolean mColorizedSet;
        CharSequence mContentInfo;
        PendingIntent mContentIntent;
        CharSequence mContentText;
        CharSequence mContentTitle;
        RemoteViews mContentView;
        public Context mContext;
        Bundle mExtras;
        PendingIntent mFullScreenIntent;
        int mGroupAlertBehavior;
        String mGroupKey;
        boolean mGroupSummary;
        RemoteViews mHeadsUpContentView;
        ArrayList<Action> mInvisibleActions;
        Bitmap mLargeIcon;
        boolean mLocalOnly;
        LocusIdCompat mLocusId;
        Notification mNotification;
        int mNumber;
        @Deprecated
        public ArrayList<String> mPeople;
        public ArrayList<Person> mPersonList;
        int mPriority;
        int mProgress;
        boolean mProgressIndeterminate;
        int mProgressMax;
        Notification mPublicVersion;
        CharSequence[] mRemoteInputHistory;
        CharSequence mSettingsText;
        String mShortcutId;
        boolean mShowWhen;
        boolean mSilent;
        Icon mSmallIcon;
        String mSortKey;
        Style mStyle;
        CharSequence mSubText;
        RemoteViews mTickerView;
        long mTimeout;
        boolean mUseChronometer;
        int mVisibility;

        public Builder(Context context, Notification notification) {
            this(context, NotificationCompat.getChannelId(notification));
            ArrayList<Person> peopleList;
            Bundle extras = notification.extras;
            Style style = Style.extractStyleFromNotification(notification);
            setContentTitle(NotificationCompat.getContentTitle(notification)).setContentText(NotificationCompat.getContentText(notification)).setContentInfo(NotificationCompat.getContentInfo(notification)).setSubText(NotificationCompat.getSubText(notification)).setSettingsText(NotificationCompat.getSettingsText(notification)).setStyle(style).setContentIntent(notification.contentIntent).setGroup(NotificationCompat.getGroup(notification)).setGroupSummary(NotificationCompat.isGroupSummary(notification)).setLocusId(NotificationCompat.getLocusId(notification)).setWhen(notification.when).setShowWhen(NotificationCompat.getShowWhen(notification)).setUsesChronometer(NotificationCompat.getUsesChronometer(notification)).setAutoCancel(NotificationCompat.getAutoCancel(notification)).setOnlyAlertOnce(NotificationCompat.getOnlyAlertOnce(notification)).setOngoing(NotificationCompat.getOngoing(notification)).setLocalOnly(NotificationCompat.getLocalOnly(notification)).setLargeIcon(notification.largeIcon).setBadgeIconType(NotificationCompat.getBadgeIconType(notification)).setCategory(NotificationCompat.getCategory(notification)).setBubbleMetadata(NotificationCompat.getBubbleMetadata(notification)).setNumber(notification.number).setTicker(notification.tickerText).setContentIntent(notification.contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(notification.fullScreenIntent, NotificationCompat.getHighPriority(notification)).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setDefaults(notification.defaults).setPriority(notification.priority).setColor(NotificationCompat.getColor(notification)).setVisibility(NotificationCompat.getVisibility(notification)).setPublicVersion(NotificationCompat.getPublicVersion(notification)).setSortKey(NotificationCompat.getSortKey(notification)).setTimeoutAfter(NotificationCompat.getTimeoutAfter(notification)).setShortcutId(NotificationCompat.getShortcutId(notification)).setProgress(extras.getInt(NotificationCompat.EXTRA_PROGRESS_MAX), extras.getInt(NotificationCompat.EXTRA_PROGRESS), extras.getBoolean(NotificationCompat.EXTRA_PROGRESS_INDETERMINATE)).setAllowSystemGeneratedContextualActions(NotificationCompat.getAllowSystemGeneratedContextualActions(notification)).setSmallIcon(notification.icon, notification.iconLevel).addExtras(getExtrasWithoutDuplicateData(notification, style));
            if (Build.VERSION.SDK_INT >= 23) {
                this.mSmallIcon = notification.getSmallIcon();
            }
            if (!(notification.actions == null || notification.actions.length == 0)) {
                for (Notification.Action action : notification.actions) {
                    addAction(Action.Builder.fromAndroidAction(action).build());
                }
            }
            if (Build.VERSION.SDK_INT >= 21) {
                List<Action> invisibleActions = NotificationCompat.getInvisibleActions(notification);
                if (!invisibleActions.isEmpty()) {
                    for (Action invisibleAction : invisibleActions) {
                        addInvisibleAction(invisibleAction);
                    }
                }
            }
            String[] people = notification.extras.getStringArray(NotificationCompat.EXTRA_PEOPLE);
            if (!(people == null || people.length == 0)) {
                for (String person : people) {
                    addPerson(person);
                }
            }
            if (Build.VERSION.SDK_INT >= 28 && (peopleList = notification.extras.getParcelableArrayList(NotificationCompat.EXTRA_PEOPLE_LIST)) != null && !peopleList.isEmpty()) {
                Iterator<Person> it = peopleList.iterator();
                while (it.hasNext()) {
                    addPerson(Person.fromAndroidPerson(it.next()));
                }
            }
            if (Build.VERSION.SDK_INT >= 24 && extras.containsKey(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN)) {
                setChronometerCountDown(extras.getBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN));
            }
            if (Build.VERSION.SDK_INT >= 26 && extras.containsKey(NotificationCompat.EXTRA_COLORIZED)) {
                setColorized(extras.getBoolean(NotificationCompat.EXTRA_COLORIZED));
            }
        }

        private static Bundle getExtrasWithoutDuplicateData(Notification notification, Style style) {
            if (notification.extras == null) {
                return null;
            }
            Bundle newExtras = new Bundle(notification.extras);
            newExtras.remove(NotificationCompat.EXTRA_TITLE);
            newExtras.remove(NotificationCompat.EXTRA_TEXT);
            newExtras.remove(NotificationCompat.EXTRA_INFO_TEXT);
            newExtras.remove(NotificationCompat.EXTRA_SUB_TEXT);
            newExtras.remove(NotificationCompat.EXTRA_CHANNEL_ID);
            newExtras.remove(NotificationCompat.EXTRA_CHANNEL_GROUP_ID);
            newExtras.remove(NotificationCompat.EXTRA_SHOW_WHEN);
            newExtras.remove(NotificationCompat.EXTRA_PROGRESS);
            newExtras.remove(NotificationCompat.EXTRA_PROGRESS_MAX);
            newExtras.remove(NotificationCompat.EXTRA_PROGRESS_INDETERMINATE);
            newExtras.remove(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN);
            newExtras.remove(NotificationCompat.EXTRA_COLORIZED);
            newExtras.remove(NotificationCompat.EXTRA_PEOPLE_LIST);
            newExtras.remove(NotificationCompat.EXTRA_PEOPLE);
            newExtras.remove(NotificationCompatExtras.EXTRA_SORT_KEY);
            newExtras.remove(NotificationCompatExtras.EXTRA_GROUP_KEY);
            newExtras.remove(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
            newExtras.remove(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
            newExtras.remove(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
            Bundle carExtenderExtras = newExtras.getBundle("android.car.EXTENSIONS");
            if (carExtenderExtras != null) {
                Bundle carExtenderExtras2 = new Bundle(carExtenderExtras);
                carExtenderExtras2.remove("invisible_actions");
                newExtras.putBundle("android.car.EXTENSIONS", carExtenderExtras2);
            }
            if (style != null) {
                style.clearCompatExtraKeys(newExtras);
            }
            return newExtras;
        }

        public Builder(Context context, String channelId) {
            this.mActions = new ArrayList<>();
            this.mPersonList = new ArrayList<>();
            this.mInvisibleActions = new ArrayList<>();
            this.mShowWhen = true;
            this.mLocalOnly = false;
            this.mColor = 0;
            this.mVisibility = 0;
            this.mBadgeIcon = 0;
            this.mGroupAlertBehavior = 0;
            this.mNotification = new Notification();
            this.mContext = context;
            this.mChannelId = channelId;
            this.mNotification.when = System.currentTimeMillis();
            this.mNotification.audioStreamType = -1;
            this.mPriority = 0;
            this.mPeople = new ArrayList<>();
            this.mAllowSystemGeneratedContextualActions = true;
        }

        @Deprecated
        public Builder(Context context) {
            this(context, (String) null);
        }

        public Builder setWhen(long when) {
            this.mNotification.when = when;
            return this;
        }

        public Builder setShowWhen(boolean show) {
            this.mShowWhen = show;
            return this;
        }

        public Builder setSmallIcon(IconCompat icon) {
            this.mSmallIcon = icon.toIcon(this.mContext);
            return this;
        }

        public Builder setUsesChronometer(boolean b) {
            this.mUseChronometer = b;
            return this;
        }

        public Builder setChronometerCountDown(boolean countsDown) {
            this.mChronometerCountDown = countsDown;
            getExtras().putBoolean(NotificationCompat.EXTRA_CHRONOMETER_COUNT_DOWN, countsDown);
            return this;
        }

        public Builder setSmallIcon(int icon) {
            this.mNotification.icon = icon;
            return this;
        }

        public Builder setSmallIcon(int icon, int level) {
            Notification notification = this.mNotification;
            notification.icon = icon;
            notification.iconLevel = level;
            return this;
        }

        @Deprecated
        public Builder setNotificationSilent() {
            this.mSilent = true;
            return this;
        }

        public Builder setSilent(boolean silent) {
            this.mSilent = silent;
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            this.mContentTitle = limitCharSequenceLength(title);
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.mContentText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setSubText(CharSequence text) {
            this.mSubText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setSettingsText(CharSequence text) {
            this.mSettingsText = limitCharSequenceLength(text);
            return this;
        }

        public Builder setRemoteInputHistory(CharSequence[] text) {
            this.mRemoteInputHistory = text;
            return this;
        }

        public Builder setNumber(int number) {
            this.mNumber = number;
            return this;
        }

        public Builder setContentInfo(CharSequence info) {
            this.mContentInfo = limitCharSequenceLength(info);
            return this;
        }

        public Builder setProgress(int max, int progress, boolean indeterminate) {
            this.mProgressMax = max;
            this.mProgress = progress;
            this.mProgressIndeterminate = indeterminate;
            return this;
        }

        public Builder setContent(RemoteViews views) {
            this.mNotification.contentView = views;
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            this.mContentIntent = intent;
            return this;
        }

        public Builder setDeleteIntent(PendingIntent intent) {
            this.mNotification.deleteIntent = intent;
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent intent, boolean highPriority) {
            this.mFullScreenIntent = intent;
            setFlag(128, highPriority);
            return this;
        }

        public Builder setTicker(CharSequence tickerText) {
            this.mNotification.tickerText = limitCharSequenceLength(tickerText);
            return this;
        }

        @Deprecated
        public Builder setTicker(CharSequence tickerText, RemoteViews views) {
            this.mNotification.tickerText = limitCharSequenceLength(tickerText);
            this.mTickerView = views;
            return this;
        }

        public Builder setLargeIcon(Bitmap icon) {
            this.mLargeIcon = reduceLargeIconSize(icon);
            return this;
        }

        private Bitmap reduceLargeIconSize(Bitmap icon) {
            if (icon == null || Build.VERSION.SDK_INT >= 27) {
                return icon;
            }
            Resources res = this.mContext.getResources();
            int maxWidth = res.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_width);
            int maxHeight = res.getDimensionPixelSize(R.dimen.compat_notification_large_icon_max_height);
            if (icon.getWidth() <= maxWidth && icon.getHeight() <= maxHeight) {
                return icon;
            }
            double scale = Math.min(((double) maxWidth) / ((double) Math.max(1, icon.getWidth())), ((double) maxHeight) / ((double) Math.max(1, icon.getHeight())));
            return Bitmap.createScaledBitmap(icon, (int) Math.ceil(((double) icon.getWidth()) * scale), (int) Math.ceil(((double) icon.getHeight()) * scale), true);
        }

        public Builder setSound(Uri sound) {
            Notification notification = this.mNotification;
            notification.sound = sound;
            notification.audioStreamType = -1;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setUsage(5).build();
            }
            return this;
        }

        public Builder setSound(Uri sound, int streamType) {
            Notification notification = this.mNotification;
            notification.sound = sound;
            notification.audioStreamType = streamType;
            if (Build.VERSION.SDK_INT >= 21) {
                this.mNotification.audioAttributes = new AudioAttributes.Builder().setContentType(4).setLegacyStreamType(streamType).build();
            }
            return this;
        }

        public Builder setVibrate(long[] pattern) {
            this.mNotification.vibrate = pattern;
            return this;
        }

        public Builder setLights(int argb, int onMs, int offMs) {
            Notification notification = this.mNotification;
            notification.ledARGB = argb;
            notification.ledOnMS = onMs;
            notification.ledOffMS = offMs;
            int i = 1;
            boolean showLights = (notification.ledOnMS == 0 || this.mNotification.ledOffMS == 0) ? false : true;
            Notification notification2 = this.mNotification;
            int i2 = notification2.flags & -2;
            if (!showLights) {
                i = 0;
            }
            notification2.flags = i | i2;
            return this;
        }

        public Builder setOngoing(boolean ongoing) {
            setFlag(2, ongoing);
            return this;
        }

        public Builder setColorized(boolean colorize) {
            this.mColorized = colorize;
            this.mColorizedSet = true;
            return this;
        }

        public Builder setOnlyAlertOnce(boolean onlyAlertOnce) {
            setFlag(8, onlyAlertOnce);
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            setFlag(16, autoCancel);
            return this;
        }

        public Builder setLocalOnly(boolean b) {
            this.mLocalOnly = b;
            return this;
        }

        public Builder setCategory(String category) {
            this.mCategory = category;
            return this;
        }

        public Builder setDefaults(int defaults) {
            Notification notification = this.mNotification;
            notification.defaults = defaults;
            if ((defaults & 4) != 0) {
                notification.flags |= 1;
            }
            return this;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mNotification.flags |= mask;
                return;
            }
            this.mNotification.flags &= ~mask;
        }

        public Builder setPriority(int pri) {
            this.mPriority = pri;
            return this;
        }

        @Deprecated
        public Builder addPerson(String uri) {
            if (uri != null && !uri.isEmpty()) {
                this.mPeople.add(uri);
            }
            return this;
        }

        public Builder addPerson(Person person) {
            if (person != null) {
                this.mPersonList.add(person);
            }
            return this;
        }

        public Builder clearPeople() {
            this.mPersonList.clear();
            this.mPeople.clear();
            return this;
        }

        public Builder setGroup(String groupKey) {
            this.mGroupKey = groupKey;
            return this;
        }

        public Builder setGroupSummary(boolean isGroupSummary) {
            this.mGroupSummary = isGroupSummary;
            return this;
        }

        public Builder setSortKey(String sortKey) {
            this.mSortKey = sortKey;
            return this;
        }

        public Builder addExtras(Bundle extras) {
            if (extras != null) {
                Bundle bundle = this.mExtras;
                if (bundle == null) {
                    this.mExtras = new Bundle(extras);
                } else {
                    bundle.putAll(extras);
                }
            }
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public Bundle getExtras() {
            if (this.mExtras == null) {
                this.mExtras = new Bundle();
            }
            return this.mExtras;
        }

        public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
            this.mActions.add(new Action(icon, title, intent));
            return this;
        }

        public Builder addAction(Action action) {
            if (action != null) {
                this.mActions.add(action);
            }
            return this;
        }

        public Builder clearActions() {
            this.mActions.clear();
            return this;
        }

        public Builder addInvisibleAction(int icon, CharSequence title, PendingIntent intent) {
            this.mInvisibleActions.add(new Action(icon, title, intent));
            return this;
        }

        public Builder addInvisibleAction(Action action) {
            if (action != null) {
                this.mInvisibleActions.add(action);
            }
            return this;
        }

        public Builder clearInvisibleActions() {
            this.mInvisibleActions.clear();
            Bundle carExtenderBundle = this.mExtras.getBundle("android.car.EXTENSIONS");
            if (carExtenderBundle != null) {
                Bundle carExtenderBundle2 = new Bundle(carExtenderBundle);
                carExtenderBundle2.remove("invisible_actions");
                this.mExtras.putBundle("android.car.EXTENSIONS", carExtenderBundle2);
            }
            return this;
        }

        public Builder setStyle(Style style) {
            if (this.mStyle != style) {
                this.mStyle = style;
                Style style2 = this.mStyle;
                if (style2 != null) {
                    style2.setBuilder(this);
                }
            }
            return this;
        }

        public Builder setColor(int argb) {
            this.mColor = argb;
            return this;
        }

        public Builder setVisibility(int visibility) {
            this.mVisibility = visibility;
            return this;
        }

        public Builder setPublicVersion(Notification n) {
            this.mPublicVersion = n;
            return this;
        }

        private boolean useExistingRemoteView() {
            Style style = this.mStyle;
            return style == null || !style.displayCustomViewInline();
        }

        public RemoteViews createContentView() {
            RemoteViews styleView;
            if (this.mContentView != null && useExistingRemoteView()) {
                return this.mContentView;
            }
            NotificationCompatBuilder compatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (styleView = style.makeContentView(compatBuilder)) != null) {
                return styleView;
            }
            Notification notification = compatBuilder.build();
            if (Build.VERSION.SDK_INT >= 24) {
                return Notification.Builder.recoverBuilder(this.mContext, notification).createContentView();
            }
            return notification.contentView;
        }

        public RemoteViews createBigContentView() {
            RemoteViews styleView;
            if (Build.VERSION.SDK_INT < 16) {
                return null;
            }
            if (this.mBigContentView != null && useExistingRemoteView()) {
                return this.mBigContentView;
            }
            NotificationCompatBuilder compatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (styleView = style.makeBigContentView(compatBuilder)) != null) {
                return styleView;
            }
            Notification notification = compatBuilder.build();
            if (Build.VERSION.SDK_INT >= 24) {
                return Notification.Builder.recoverBuilder(this.mContext, notification).createBigContentView();
            }
            return notification.bigContentView;
        }

        public RemoteViews createHeadsUpContentView() {
            RemoteViews styleView;
            if (Build.VERSION.SDK_INT < 21) {
                return null;
            }
            if (this.mHeadsUpContentView != null && useExistingRemoteView()) {
                return this.mHeadsUpContentView;
            }
            NotificationCompatBuilder compatBuilder = new NotificationCompatBuilder(this);
            Style style = this.mStyle;
            if (style != null && (styleView = style.makeHeadsUpContentView(compatBuilder)) != null) {
                return styleView;
            }
            Notification notification = compatBuilder.build();
            if (Build.VERSION.SDK_INT >= 24) {
                return Notification.Builder.recoverBuilder(this.mContext, notification).createHeadsUpContentView();
            }
            return notification.headsUpContentView;
        }

        public Builder setCustomContentView(RemoteViews contentView) {
            this.mContentView = contentView;
            return this;
        }

        public Builder setCustomBigContentView(RemoteViews contentView) {
            this.mBigContentView = contentView;
            return this;
        }

        public Builder setCustomHeadsUpContentView(RemoteViews contentView) {
            this.mHeadsUpContentView = contentView;
            return this;
        }

        public Builder setChannelId(String channelId) {
            this.mChannelId = channelId;
            return this;
        }

        public Builder setTimeoutAfter(long durationMs) {
            this.mTimeout = durationMs;
            return this;
        }

        public Builder setShortcutId(String shortcutId) {
            this.mShortcutId = shortcutId;
            return this;
        }

        public Builder setShortcutInfo(ShortcutInfoCompat shortcutInfo) {
            if (shortcutInfo == null) {
                return this;
            }
            this.mShortcutId = shortcutInfo.getId();
            if (this.mLocusId == null) {
                if (shortcutInfo.getLocusId() != null) {
                    this.mLocusId = shortcutInfo.getLocusId();
                } else if (shortcutInfo.getId() != null) {
                    this.mLocusId = new LocusIdCompat(shortcutInfo.getId());
                }
            }
            if (this.mContentTitle == null) {
                setContentTitle(shortcutInfo.getShortLabel());
            }
            return this;
        }

        public Builder setLocusId(LocusIdCompat locusId) {
            this.mLocusId = locusId;
            return this;
        }

        public Builder setBadgeIconType(int icon) {
            this.mBadgeIcon = icon;
            return this;
        }

        public Builder setGroupAlertBehavior(int groupAlertBehavior) {
            this.mGroupAlertBehavior = groupAlertBehavior;
            return this;
        }

        public Builder setBubbleMetadata(BubbleMetadata data) {
            this.mBubbleMetadata = data;
            return this;
        }

        public Builder extend(Extender extender) {
            extender.extend(this);
            return this;
        }

        public Builder setAllowSystemGeneratedContextualActions(boolean allowed) {
            this.mAllowSystemGeneratedContextualActions = allowed;
            return this;
        }

        @Deprecated
        public Notification getNotification() {
            return build();
        }

        public Notification build() {
            return new NotificationCompatBuilder(this).build();
        }

        protected static CharSequence limitCharSequenceLength(CharSequence cs) {
            if (cs == null) {
                return cs;
            }
            if (cs.length() > MAX_CHARSEQUENCE_LENGTH) {
                return cs.subSequence(0, MAX_CHARSEQUENCE_LENGTH);
            }
            return cs;
        }

        public RemoteViews getContentView() {
            return this.mContentView;
        }

        public RemoteViews getBigContentView() {
            return this.mBigContentView;
        }

        public RemoteViews getHeadsUpContentView() {
            return this.mHeadsUpContentView;
        }

        public long getWhenIfShowing() {
            if (this.mShowWhen) {
                return this.mNotification.when;
            }
            return 0;
        }

        public int getPriority() {
            return this.mPriority;
        }

        public int getColor() {
            return this.mColor;
        }

        public BubbleMetadata getBubbleMetadata() {
            return this.mBubbleMetadata;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Style {
        CharSequence mBigContentTitle;
        protected Builder mBuilder;
        CharSequence mSummaryText;
        boolean mSummaryTextSet = false;

        public void setBuilder(Builder builder) {
            if (this.mBuilder != builder) {
                this.mBuilder = builder;
                Builder builder2 = this.mBuilder;
                if (builder2 != null) {
                    builder2.setStyle(this);
                }
            }
        }

        public Notification build() {
            Builder builder = this.mBuilder;
            if (builder != null) {
                return builder.build();
            }
            return null;
        }

        protected String getClassName() {
            return null;
        }

        public void apply(NotificationBuilderWithBuilderAccessor builder) {
        }

        public boolean displayCustomViewInline() {
            return false;
        }

        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor builder) {
            return null;
        }

        public void addCompatExtras(Bundle extras) {
            if (this.mSummaryTextSet) {
                extras.putCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT, this.mSummaryText);
            }
            CharSequence charSequence = this.mBigContentTitle;
            if (charSequence != null) {
                extras.putCharSequence(NotificationCompat.EXTRA_TITLE_BIG, charSequence);
            }
            String className = getClassName();
            if (className != null) {
                extras.putString(NotificationCompat.EXTRA_COMPAT_TEMPLATE, className);
            }
        }

        protected void restoreFromCompatExtras(Bundle extras) {
            if (extras.containsKey(NotificationCompat.EXTRA_SUMMARY_TEXT)) {
                this.mSummaryText = extras.getCharSequence(NotificationCompat.EXTRA_SUMMARY_TEXT);
                this.mSummaryTextSet = true;
            }
            this.mBigContentTitle = extras.getCharSequence(NotificationCompat.EXTRA_TITLE_BIG);
        }

        protected void clearCompatExtraKeys(Bundle extras) {
            extras.remove(NotificationCompat.EXTRA_SUMMARY_TEXT);
            extras.remove(NotificationCompat.EXTRA_TITLE_BIG);
            extras.remove(NotificationCompat.EXTRA_COMPAT_TEMPLATE);
        }

        public static Style extractStyleFromNotification(Notification notification) {
            Bundle extras = NotificationCompat.getExtras(notification);
            if (extras == null) {
                return null;
            }
            return constructStyleForExtras(extras);
        }

        private static Style constructCompatStyleByPlatformName(String platformTemplateClass) {
            if (platformTemplateClass != null && Build.VERSION.SDK_INT >= 16) {
                if (platformTemplateClass.equals(Notification.BigPictureStyle.class.getName())) {
                    return new BigPictureStyle();
                }
                if (platformTemplateClass.equals(Notification.BigTextStyle.class.getName())) {
                    return new BigTextStyle();
                }
                if (platformTemplateClass.equals(Notification.InboxStyle.class.getName())) {
                    return new InboxStyle();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    if (platformTemplateClass.equals(Notification.MessagingStyle.class.getName())) {
                        return new MessagingStyle();
                    }
                    if (platformTemplateClass.equals(Notification.DecoratedCustomViewStyle.class.getName())) {
                        return new DecoratedCustomViewStyle();
                    }
                }
            }
            return null;
        }

        static Style constructCompatStyleByName(String templateClass) {
            if (templateClass == null) {
                return null;
            }
            char c = 65535;
            switch (templateClass.hashCode()) {
                case -716705180:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$DecoratedCustomViewStyle")) {
                        c = 3;
                        break;
                    }
                    break;
                case -171946061:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$BigPictureStyle")) {
                        c = 1;
                        break;
                    }
                    break;
                case 912942987:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$InboxStyle")) {
                        c = 2;
                        break;
                    }
                    break;
                case 919595044:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$BigTextStyle")) {
                        c = 0;
                        break;
                    }
                    break;
                case 2090799565:
                    if (templateClass.equals("androidx.core.app.NotificationCompat$MessagingStyle")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                return new BigTextStyle();
            }
            if (c == 1) {
                return new BigPictureStyle();
            }
            if (c == 2) {
                return new InboxStyle();
            }
            if (c == 3) {
                return new DecoratedCustomViewStyle();
            }
            if (c != 4) {
                return null;
            }
            return new MessagingStyle();
        }

        static Style constructCompatStyleForBundle(Bundle extras) {
            Style style = constructCompatStyleByName(extras.getString(NotificationCompat.EXTRA_COMPAT_TEMPLATE));
            if (style != null) {
                return style;
            }
            if (extras.containsKey(NotificationCompat.EXTRA_SELF_DISPLAY_NAME) || extras.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) {
                return new MessagingStyle();
            }
            if (extras.containsKey(NotificationCompat.EXTRA_PICTURE)) {
                return new BigPictureStyle();
            }
            if (extras.containsKey(NotificationCompat.EXTRA_BIG_TEXT)) {
                return new BigTextStyle();
            }
            if (extras.containsKey(NotificationCompat.EXTRA_TEXT_LINES)) {
                return new InboxStyle();
            }
            return constructCompatStyleByPlatformName(extras.getString(NotificationCompat.EXTRA_TEMPLATE));
        }

        static Style constructStyleForExtras(Bundle extras) {
            Style style = constructCompatStyleForBundle(extras);
            if (style == null) {
                return null;
            }
            try {
                style.restoreFromCompatExtras(extras);
                return style;
            } catch (ClassCastException e) {
                return null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:71:0x01c7  */
        /* JADX WARN: Removed duplicated region for block: B:75:0x01e9  */
        /* JADX WARN: Removed duplicated region for block: B:88:0x0245  */
        /* JADX WARN: Removed duplicated region for block: B:89:0x0247  */
        /* JADX WARN: Removed duplicated region for block: B:92:0x0250  */
        /* Code decompiled incorrectly, please refer to instructions dump */
        public RemoteViews applyStandardTemplate(boolean showSmallIcon, int resId, boolean fitIn1U) {
            boolean hasRightSide;
            boolean showLine2;
            Resources res = this.mBuilder.mContext.getResources();
            RemoteViews contentView = new RemoteViews(this.mBuilder.mContext.getPackageName(), resId);
            boolean showLine3 = false;
            int i = 0;
            boolean minPriority = this.mBuilder.getPriority() < -1;
            if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21) {
                if (minPriority) {
                    contentView.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                    contentView.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
                } else {
                    contentView.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                    contentView.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
                }
            }
            if (this.mBuilder.mLargeIcon != null) {
                if (Build.VERSION.SDK_INT >= 16) {
                    contentView.setViewVisibility(R.id.icon, 0);
                    contentView.setImageViewBitmap(R.id.icon, this.mBuilder.mLargeIcon);
                } else {
                    contentView.setViewVisibility(R.id.icon, 8);
                }
                if (showSmallIcon && this.mBuilder.mNotification.icon != 0) {
                    int backgroundSize = res.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                    int iconSize = backgroundSize - (res.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding) * 2);
                    if (Build.VERSION.SDK_INT >= 21) {
                        contentView.setImageViewBitmap(R.id.right_icon, createIconWithBackground(this.mBuilder.mNotification.icon, backgroundSize, iconSize, this.mBuilder.getColor()));
                    } else {
                        contentView.setImageViewBitmap(R.id.right_icon, createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                    }
                    contentView.setViewVisibility(R.id.right_icon, 0);
                }
            } else if (showSmallIcon && this.mBuilder.mNotification.icon != 0) {
                contentView.setViewVisibility(R.id.icon, 0);
                if (Build.VERSION.SDK_INT >= 21) {
                    contentView.setImageViewBitmap(R.id.icon, createIconWithBackground(this.mBuilder.mNotification.icon, res.getDimensionPixelSize(R.dimen.notification_large_icon_width) - res.getDimensionPixelSize(R.dimen.notification_big_circle_margin), res.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large), this.mBuilder.getColor()));
                } else {
                    contentView.setImageViewBitmap(R.id.icon, createColoredBitmap(this.mBuilder.mNotification.icon, -1));
                }
            }
            if (this.mBuilder.mContentTitle != null) {
                contentView.setTextViewText(R.id.title, this.mBuilder.mContentTitle);
            }
            if (this.mBuilder.mContentText != null) {
                contentView.setTextViewText(R.id.text, this.mBuilder.mContentText);
                showLine3 = true;
            }
            boolean hasRightSide2 = Build.VERSION.SDK_INT < 21 && this.mBuilder.mLargeIcon != null;
            if (this.mBuilder.mContentInfo != null) {
                contentView.setTextViewText(R.id.info, this.mBuilder.mContentInfo);
                contentView.setViewVisibility(R.id.info, 0);
                showLine3 = true;
                hasRightSide = true;
            } else if (this.mBuilder.mNumber > 0) {
                if (this.mBuilder.mNumber > res.getInteger(R.integer.status_bar_notification_info_maxnum)) {
                    contentView.setTextViewText(R.id.info, res.getString(R.string.status_bar_notification_info_overflow));
                } else {
                    contentView.setTextViewText(R.id.info, NumberFormat.getIntegerInstance().format((long) this.mBuilder.mNumber));
                }
                contentView.setViewVisibility(R.id.info, 0);
                showLine3 = true;
                hasRightSide = true;
            } else {
                contentView.setViewVisibility(R.id.info, 8);
                hasRightSide = hasRightSide2;
            }
            if (this.mBuilder.mSubText != null && Build.VERSION.SDK_INT >= 16) {
                contentView.setTextViewText(R.id.text, this.mBuilder.mSubText);
                if (this.mBuilder.mContentText != null) {
                    contentView.setTextViewText(R.id.text2, this.mBuilder.mContentText);
                    contentView.setViewVisibility(R.id.text2, 0);
                    showLine2 = true;
                    if (showLine2 && Build.VERSION.SDK_INT >= 16) {
                        if (fitIn1U) {
                            contentView.setTextViewTextSize(R.id.text, 0, (float) res.getDimensionPixelSize(R.dimen.notification_subtext_size));
                        }
                        contentView.setViewPadding(R.id.line1, 0, 0, 0, 0);
                    }
                    if (this.mBuilder.getWhenIfShowing() != 0) {
                        if (!this.mBuilder.mUseChronometer || Build.VERSION.SDK_INT < 16) {
                            contentView.setViewVisibility(R.id.time, 0);
                            contentView.setLong(R.id.time, "setTime", this.mBuilder.getWhenIfShowing());
                        } else {
                            contentView.setViewVisibility(R.id.chronometer, 0);
                            contentView.setLong(R.id.chronometer, "setBase", this.mBuilder.getWhenIfShowing() + (SystemClock.elapsedRealtime() - System.currentTimeMillis()));
                            contentView.setBoolean(R.id.chronometer, "setStarted", true);
                            if (this.mBuilder.mChronometerCountDown && Build.VERSION.SDK_INT >= 24) {
                                contentView.setChronometerCountDown(R.id.chronometer, this.mBuilder.mChronometerCountDown);
                            }
                        }
                        hasRightSide = true;
                    }
                    contentView.setViewVisibility(R.id.right_side, !hasRightSide ? 0 : 8);
                    int i2 = R.id.line3;
                    if (!showLine3) {
                        i = 8;
                    }
                    contentView.setViewVisibility(i2, i);
                    return contentView;
                }
                contentView.setViewVisibility(R.id.text2, 8);
            }
            showLine2 = false;
            if (showLine2) {
                if (fitIn1U) {
                }
                contentView.setViewPadding(R.id.line1, 0, 0, 0, 0);
            }
            if (this.mBuilder.getWhenIfShowing() != 0) {
            }
            contentView.setViewVisibility(R.id.right_side, !hasRightSide ? 0 : 8);
            int i22 = R.id.line3;
            if (!showLine3) {
            }
            contentView.setViewVisibility(i22, i);
            return contentView;
        }

        public Bitmap createColoredBitmap(int iconId, int color) {
            return createColoredBitmap(iconId, color, 0);
        }

        Bitmap createColoredBitmap(IconCompat icon, int color) {
            return createColoredBitmap(icon, color, 0);
        }

        private Bitmap createColoredBitmap(int iconId, int color, int size) {
            return createColoredBitmap(IconCompat.createWithResource(this.mBuilder.mContext, iconId), color, size);
        }

        private Bitmap createColoredBitmap(IconCompat icon, int color, int size) {
            Drawable drawable = icon.loadDrawable(this.mBuilder.mContext);
            int width = size == 0 ? drawable.getIntrinsicWidth() : size;
            int height = size == 0 ? drawable.getIntrinsicHeight() : size;
            Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            drawable.setBounds(0, 0, width, height);
            if (color != 0) {
                drawable.mutate().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
            drawable.draw(new Canvas(resultBitmap));
            return resultBitmap;
        }

        private Bitmap createIconWithBackground(int iconId, int size, int iconSize, int color) {
            Bitmap coloredBitmap = createColoredBitmap(R.drawable.notification_icon_background, color == 0 ? 0 : color, size);
            Canvas canvas = new Canvas(coloredBitmap);
            Drawable icon = this.mBuilder.mContext.getResources().getDrawable(iconId).mutate();
            icon.setFilterBitmap(true);
            int inset = (size - iconSize) / 2;
            icon.setBounds(inset, inset, iconSize + inset, iconSize + inset);
            icon.setColorFilter(new PorterDuffColorFilter(-1, PorterDuff.Mode.SRC_ATOP));
            icon.draw(canvas);
            return coloredBitmap;
        }

        public void buildIntoRemoteViews(RemoteViews outerView, RemoteViews innerView) {
            hideNormalContent(outerView);
            outerView.removeAllViews(R.id.notification_main_column);
            outerView.addView(R.id.notification_main_column, innerView.clone());
            outerView.setViewVisibility(R.id.notification_main_column, 0);
            if (Build.VERSION.SDK_INT >= 21) {
                outerView.setViewPadding(R.id.notification_main_column_container, 0, calculateTopPadding(), 0, 0);
            }
        }

        private void hideNormalContent(RemoteViews outerView) {
            outerView.setViewVisibility(R.id.title, 8);
            outerView.setViewVisibility(R.id.text2, 8);
            outerView.setViewVisibility(R.id.text, 8);
        }

        private int calculateTopPadding() {
            Resources resources = this.mBuilder.mContext.getResources();
            int padding = resources.getDimensionPixelSize(R.dimen.notification_top_pad);
            int largePadding = resources.getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
            float largeFactor = (constrain(resources.getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
            return Math.round(((1.0f - largeFactor) * ((float) padding)) + (((float) largePadding) * largeFactor));
        }

        private static float constrain(float amount, float low, float high) {
            if (amount < low) {
                return low;
            }
            return amount > high ? high : amount;
        }
    }

    /* loaded from: classes.dex */
    public static class BigPictureStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME;
        private IconCompat mBigLargeIcon;
        private boolean mBigLargeIconSet;
        private Bitmap mPicture;

        public BigPictureStyle() {
        }

        public BigPictureStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigPictureStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public BigPictureStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }

        public BigPictureStyle bigPicture(Bitmap b) {
            this.mPicture = b;
            return this;
        }

        public BigPictureStyle bigLargeIcon(Bitmap b) {
            this.mBigLargeIcon = b == null ? null : IconCompat.createWithBitmap(b);
            this.mBigLargeIconSet = true;
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigPictureStyle style = new Notification.BigPictureStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigPicture(this.mPicture);
                if (this.mBigLargeIconSet) {
                    if (this.mBigLargeIcon == null) {
                        Api16Impl.setBigLargeIcon(style, null);
                    } else if (Build.VERSION.SDK_INT >= 23) {
                        Context context = null;
                        if (builder instanceof NotificationCompatBuilder) {
                            context = ((NotificationCompatBuilder) builder).getContext();
                        }
                        Api23Impl.setBigLargeIcon(style, this.mBigLargeIcon.toIcon(context));
                    } else if (this.mBigLargeIcon.getType() == 1) {
                        Api16Impl.setBigLargeIcon(style, this.mBigLargeIcon.getBitmap());
                    } else {
                        Api16Impl.setBigLargeIcon(style, null);
                    }
                }
                if (this.mSummaryTextSet) {
                    Api16Impl.setSummaryText(style, this.mSummaryText);
                }
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            if (extras.containsKey(NotificationCompat.EXTRA_LARGE_ICON_BIG)) {
                this.mBigLargeIcon = asIconCompat(extras.getParcelable(NotificationCompat.EXTRA_LARGE_ICON_BIG));
                this.mBigLargeIconSet = true;
            }
            this.mPicture = (Bitmap) extras.getParcelable(NotificationCompat.EXTRA_PICTURE);
        }

        private static IconCompat asIconCompat(Parcelable bitmapOrIcon) {
            if (bitmapOrIcon == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 23 && (bitmapOrIcon instanceof Icon)) {
                return IconCompat.createFromIcon((Icon) bitmapOrIcon);
            }
            if (bitmapOrIcon instanceof Bitmap) {
                return IconCompat.createWithBitmap((Bitmap) bitmapOrIcon);
            }
            return null;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_LARGE_ICON_BIG);
            extras.remove(NotificationCompat.EXTRA_PICTURE);
        }

        /* loaded from: classes.dex */
        private static class Api16Impl {
            private Api16Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle style, Bitmap icon) {
                style.bigLargeIcon(icon);
            }

            static void setSummaryText(Notification.BigPictureStyle style, CharSequence text) {
                style.setSummaryText(text);
            }
        }

        /* loaded from: classes.dex */
        private static class Api23Impl {
            private Api23Impl() {
            }

            static void setBigLargeIcon(Notification.BigPictureStyle style, Icon icon) {
                style.bigLargeIcon(icon);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class BigTextStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME;
        private CharSequence mBigText;

        public BigTextStyle() {
        }

        public BigTextStyle(Builder builder) {
            setBuilder(builder);
        }

        public BigTextStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public BigTextStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }

        public BigTextStyle bigText(CharSequence cs) {
            this.mBigText = Builder.limitCharSequenceLength(cs);
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.BigTextStyle style = new Notification.BigTextStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle).bigText(this.mBigText);
                if (this.mSummaryTextSet) {
                    style.setSummaryText(this.mSummaryText);
                }
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mBigText = extras.getCharSequence(NotificationCompat.EXTRA_BIG_TEXT);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void addCompatExtras(Bundle extras) {
            super.addCompatExtras(extras);
            if (Build.VERSION.SDK_INT < 21) {
                extras.putCharSequence(NotificationCompat.EXTRA_BIG_TEXT, this.mBigText);
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_BIG_TEXT);
        }
    }

    /* loaded from: classes.dex */
    public static class MessagingStyle extends Style {
        public static final int MAXIMUM_RETAINED_MESSAGES;
        private static final String TEMPLATE_CLASS_NAME;
        private CharSequence mConversationTitle;
        private Boolean mIsGroupConversation;
        private Person mUser;
        private final List<Message> mMessages = new ArrayList();
        private final List<Message> mHistoricMessages = new ArrayList();

        MessagingStyle() {
        }

        @Deprecated
        public MessagingStyle(CharSequence userDisplayName) {
            this.mUser = new Person.Builder().setName(userDisplayName).build();
        }

        public MessagingStyle(Person user) {
            if (!TextUtils.isEmpty(user.getName())) {
                this.mUser = user;
                return;
            }
            throw new IllegalArgumentException("User's name must not be empty.");
        }

        @Deprecated
        public CharSequence getUserDisplayName() {
            return this.mUser.getName();
        }

        public Person getUser() {
            return this.mUser;
        }

        public MessagingStyle setConversationTitle(CharSequence conversationTitle) {
            this.mConversationTitle = conversationTitle;
            return this;
        }

        public CharSequence getConversationTitle() {
            return this.mConversationTitle;
        }

        @Deprecated
        public MessagingStyle addMessage(CharSequence text, long timestamp, CharSequence sender) {
            this.mMessages.add(new Message(text, timestamp, new Person.Builder().setName(sender).build()));
            if (this.mMessages.size() > 25) {
                this.mMessages.remove(0);
            }
            return this;
        }

        public MessagingStyle addMessage(CharSequence text, long timestamp, Person person) {
            addMessage(new Message(text, timestamp, person));
            return this;
        }

        public MessagingStyle addMessage(Message message) {
            if (message != null) {
                this.mMessages.add(message);
                if (this.mMessages.size() > 25) {
                    this.mMessages.remove(0);
                }
            }
            return this;
        }

        public MessagingStyle addHistoricMessage(Message message) {
            if (message != null) {
                this.mHistoricMessages.add(message);
                if (this.mHistoricMessages.size() > 25) {
                    this.mHistoricMessages.remove(0);
                }
            }
            return this;
        }

        public List<Message> getMessages() {
            return this.mMessages;
        }

        public List<Message> getHistoricMessages() {
            return this.mHistoricMessages;
        }

        public MessagingStyle setGroupConversation(boolean isGroupConversation) {
            this.mIsGroupConversation = Boolean.valueOf(isGroupConversation);
            return this;
        }

        public boolean isGroupConversation() {
            if (this.mBuilder == null || this.mBuilder.mContext.getApplicationInfo().targetSdkVersion >= 28 || this.mIsGroupConversation != null) {
                Boolean bool = this.mIsGroupConversation;
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } else if (this.mConversationTitle != null) {
                return true;
            } else {
                return false;
            }
        }

        public static MessagingStyle extractMessagingStyleFromNotification(Notification notification) {
            Style style = Style.extractStyleFromNotification(notification);
            if (style instanceof MessagingStyle) {
                return (MessagingStyle) style;
            }
            return null;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            CharSequence charSequence;
            Notification.MessagingStyle frameworkStyle;
            setGroupConversation(isGroupConversation());
            if (Build.VERSION.SDK_INT >= 24) {
                if (Build.VERSION.SDK_INT >= 28) {
                    frameworkStyle = new Notification.MessagingStyle(this.mUser.toAndroidPerson());
                } else {
                    frameworkStyle = new Notification.MessagingStyle(this.mUser.getName());
                }
                for (Message message : this.mMessages) {
                    frameworkStyle.addMessage(message.toAndroidMessage());
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    for (Message historicMessage : this.mHistoricMessages) {
                        frameworkStyle.addHistoricMessage(historicMessage.toAndroidMessage());
                    }
                }
                if (this.mIsGroupConversation.booleanValue() || Build.VERSION.SDK_INT >= 28) {
                    frameworkStyle.setConversationTitle(this.mConversationTitle);
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    frameworkStyle.setGroupConversation(this.mIsGroupConversation.booleanValue());
                }
                frameworkStyle.setBuilder(builder.getBuilder());
                return;
            }
            Message latestIncomingMessage = findLatestIncomingMessage();
            if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                builder.getBuilder().setContentTitle(this.mConversationTitle);
            } else if (latestIncomingMessage != null) {
                builder.getBuilder().setContentTitle("");
                if (latestIncomingMessage.getPerson() != null) {
                    builder.getBuilder().setContentTitle(latestIncomingMessage.getPerson().getName());
                }
            }
            if (latestIncomingMessage != null) {
                Notification.Builder builder2 = builder.getBuilder();
                if (this.mConversationTitle != null) {
                    charSequence = makeMessageLine(latestIncomingMessage);
                } else {
                    charSequence = latestIncomingMessage.getText();
                }
                builder2.setContentText(charSequence);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                SpannableStringBuilder completeMessage = new SpannableStringBuilder();
                boolean showNames = this.mConversationTitle != null || hasMessagesWithoutSender();
                for (int i = this.mMessages.size() - 1; i >= 0; i--) {
                    Message message2 = this.mMessages.get(i);
                    CharSequence line = showNames ? makeMessageLine(message2) : message2.getText();
                    if (i != this.mMessages.size() - 1) {
                        completeMessage.insert(0, (CharSequence) IOUtils.LINE_SEPARATOR_UNIX);
                    }
                    completeMessage.insert(0, line);
                }
                new Notification.BigTextStyle(builder.getBuilder()).setBigContentTitle(null).bigText(completeMessage);
            }
        }

        private Message findLatestIncomingMessage() {
            for (int i = this.mMessages.size() - 1; i >= 0; i--) {
                Message message = this.mMessages.get(i);
                if (!(message.getPerson() == null || TextUtils.isEmpty(message.getPerson().getName()))) {
                    return message;
                }
            }
            if (this.mMessages.isEmpty()) {
                return null;
            }
            List<Message> list = this.mMessages;
            return list.get(list.size() - 1);
        }

        private boolean hasMessagesWithoutSender() {
            for (int i = this.mMessages.size() - 1; i >= 0; i--) {
                Message message = this.mMessages.get(i);
                if (message.getPerson() != null && message.getPerson().getName() == null) {
                    return true;
                }
            }
            return false;
        }

        private CharSequence makeMessageLine(Message message) {
            int i;
            BidiFormatter bidi = BidiFormatter.getInstance();
            SpannableStringBuilder sb = new SpannableStringBuilder();
            boolean afterLollipop = Build.VERSION.SDK_INT >= 21;
            int color = afterLollipop ? ViewCompat.MEASURED_STATE_MASK : -1;
            CharSequence text = "";
            CharSequence replyName = message.getPerson() == null ? text : message.getPerson().getName();
            if (TextUtils.isEmpty(replyName)) {
                replyName = this.mUser.getName();
                if (!afterLollipop || this.mBuilder.getColor() == 0) {
                    i = color;
                } else {
                    i = this.mBuilder.getColor();
                }
                color = i;
            }
            CharSequence senderText = bidi.unicodeWrap(replyName);
            sb.append(senderText);
            sb.setSpan(makeFontColorSpan(color), sb.length() - senderText.length(), sb.length(), 33);
            if (message.getText() != null) {
                text = message.getText();
            }
            sb.append((CharSequence) "  ").append(bidi.unicodeWrap(text));
            return sb;
        }

        private TextAppearanceSpan makeFontColorSpan(int color) {
            return new TextAppearanceSpan(null, 0, 0, ColorStateList.valueOf(color), null);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void addCompatExtras(Bundle extras) {
            super.addCompatExtras(extras);
            extras.putCharSequence(NotificationCompat.EXTRA_SELF_DISPLAY_NAME, this.mUser.getName());
            extras.putBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER, this.mUser.toBundle());
            extras.putCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE, this.mConversationTitle);
            if (this.mConversationTitle != null && this.mIsGroupConversation.booleanValue()) {
                extras.putCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE, this.mConversationTitle);
            }
            if (!this.mMessages.isEmpty()) {
                extras.putParcelableArray(NotificationCompat.EXTRA_MESSAGES, Message.getBundleArrayForMessages(this.mMessages));
            }
            if (!this.mHistoricMessages.isEmpty()) {
                extras.putParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES, Message.getBundleArrayForMessages(this.mHistoricMessages));
            }
            Boolean bool = this.mIsGroupConversation;
            if (bool != null) {
                extras.putBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION, bool.booleanValue());
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mMessages.clear();
            if (extras.containsKey(NotificationCompat.EXTRA_MESSAGING_STYLE_USER)) {
                this.mUser = Person.fromBundle(extras.getBundle(NotificationCompat.EXTRA_MESSAGING_STYLE_USER));
            } else {
                this.mUser = new Person.Builder().setName(extras.getString(NotificationCompat.EXTRA_SELF_DISPLAY_NAME)).build();
            }
            this.mConversationTitle = extras.getCharSequence(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            if (this.mConversationTitle == null) {
                this.mConversationTitle = extras.getCharSequence(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            }
            Parcelable[] messages = extras.getParcelableArray(NotificationCompat.EXTRA_MESSAGES);
            if (messages != null) {
                this.mMessages.addAll(Message.getMessagesFromBundleArray(messages));
            }
            Parcelable[] historicMessages = extras.getParcelableArray(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            if (historicMessages != null) {
                this.mHistoricMessages.addAll(Message.getMessagesFromBundleArray(historicMessages));
            }
            if (extras.containsKey(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION)) {
                this.mIsGroupConversation = Boolean.valueOf(extras.getBoolean(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION));
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_MESSAGING_STYLE_USER);
            extras.remove(NotificationCompat.EXTRA_SELF_DISPLAY_NAME);
            extras.remove(NotificationCompat.EXTRA_CONVERSATION_TITLE);
            extras.remove(NotificationCompat.EXTRA_HIDDEN_CONVERSATION_TITLE);
            extras.remove(NotificationCompat.EXTRA_MESSAGES);
            extras.remove(NotificationCompat.EXTRA_HISTORIC_MESSAGES);
            extras.remove(NotificationCompat.EXTRA_IS_GROUP_CONVERSATION);
        }

        /* loaded from: classes.dex */
        public static final class Message {
            static final String KEY_DATA_MIME_TYPE;
            static final String KEY_DATA_URI;
            static final String KEY_EXTRAS_BUNDLE;
            static final String KEY_NOTIFICATION_PERSON;
            static final String KEY_PERSON;
            static final String KEY_SENDER;
            static final String KEY_TEXT;
            static final String KEY_TIMESTAMP;
            private String mDataMimeType;
            private Uri mDataUri;
            private Bundle mExtras;
            private final Person mPerson;
            private final CharSequence mText;
            private final long mTimestamp;

            public Message(CharSequence text, long timestamp, Person person) {
                this.mExtras = new Bundle();
                this.mText = text;
                this.mTimestamp = timestamp;
                this.mPerson = person;
            }

            @Deprecated
            public Message(CharSequence text, long timestamp, CharSequence sender) {
                this(text, timestamp, new Person.Builder().setName(sender).build());
            }

            public Message setData(String dataMimeType, Uri dataUri) {
                this.mDataMimeType = dataMimeType;
                this.mDataUri = dataUri;
                return this;
            }

            public CharSequence getText() {
                return this.mText;
            }

            public long getTimestamp() {
                return this.mTimestamp;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            @Deprecated
            public CharSequence getSender() {
                Person person = this.mPerson;
                if (person == null) {
                    return null;
                }
                return person.getName();
            }

            public Person getPerson() {
                return this.mPerson;
            }

            public String getDataMimeType() {
                return this.mDataMimeType;
            }

            public Uri getDataUri() {
                return this.mDataUri;
            }

            private Bundle toBundle() {
                Bundle bundle = new Bundle();
                CharSequence charSequence = this.mText;
                if (charSequence != null) {
                    bundle.putCharSequence(KEY_TEXT, charSequence);
                }
                bundle.putLong(KEY_TIMESTAMP, this.mTimestamp);
                Person person = this.mPerson;
                if (person != null) {
                    bundle.putCharSequence(KEY_SENDER, person.getName());
                    if (Build.VERSION.SDK_INT >= 28) {
                        bundle.putParcelable(KEY_NOTIFICATION_PERSON, this.mPerson.toAndroidPerson());
                    } else {
                        bundle.putBundle(KEY_PERSON, this.mPerson.toBundle());
                    }
                }
                String str = this.mDataMimeType;
                if (str != null) {
                    bundle.putString(KEY_DATA_MIME_TYPE, str);
                }
                Uri uri = this.mDataUri;
                if (uri != null) {
                    bundle.putParcelable(KEY_DATA_URI, uri);
                }
                Bundle bundle2 = this.mExtras;
                if (bundle2 != null) {
                    bundle.putBundle(KEY_EXTRAS_BUNDLE, bundle2);
                }
                return bundle;
            }

            static Bundle[] getBundleArrayForMessages(List<Message> messages) {
                Bundle[] bundles = new Bundle[messages.size()];
                int N = messages.size();
                for (int i = 0; i < N; i++) {
                    bundles[i] = messages.get(i).toBundle();
                }
                return bundles;
            }

            static List<Message> getMessagesFromBundleArray(Parcelable[] bundles) {
                Message message;
                List<Message> messages = new ArrayList<>(bundles.length);
                for (int i = 0; i < bundles.length; i++) {
                    if ((bundles[i] instanceof Bundle) && (message = getMessageFromBundle((Bundle) bundles[i])) != null) {
                        messages.add(message);
                    }
                }
                return messages;
            }

            static Message getMessageFromBundle(Bundle bundle) {
                try {
                    if (bundle.containsKey(KEY_TEXT) && bundle.containsKey(KEY_TIMESTAMP)) {
                        Person person = null;
                        if (bundle.containsKey(KEY_PERSON)) {
                            person = Person.fromBundle(bundle.getBundle(KEY_PERSON));
                        } else if (bundle.containsKey(KEY_NOTIFICATION_PERSON) && Build.VERSION.SDK_INT >= 28) {
                            person = Person.fromAndroidPerson((android.app.Person) bundle.getParcelable(KEY_NOTIFICATION_PERSON));
                        } else if (bundle.containsKey(KEY_SENDER)) {
                            person = new Person.Builder().setName(bundle.getCharSequence(KEY_SENDER)).build();
                        }
                        Message message = new Message(bundle.getCharSequence(KEY_TEXT), bundle.getLong(KEY_TIMESTAMP), person);
                        if (bundle.containsKey(KEY_DATA_MIME_TYPE) && bundle.containsKey(KEY_DATA_URI)) {
                            message.setData(bundle.getString(KEY_DATA_MIME_TYPE), (Uri) bundle.getParcelable(KEY_DATA_URI));
                        }
                        if (bundle.containsKey(KEY_EXTRAS_BUNDLE)) {
                            message.getExtras().putAll(bundle.getBundle(KEY_EXTRAS_BUNDLE));
                        }
                        return message;
                    }
                    return null;
                } catch (ClassCastException e) {
                    return null;
                }
            }

            Notification.MessagingStyle.Message toAndroidMessage() {
                Notification.MessagingStyle.Message frameworkMessage;
                Person person = getPerson();
                CharSequence charSequence = null;
                android.app.Person person2 = null;
                if (Build.VERSION.SDK_INT >= 28) {
                    CharSequence text = getText();
                    long timestamp = getTimestamp();
                    if (person != null) {
                        person2 = person.toAndroidPerson();
                    }
                    frameworkMessage = new Notification.MessagingStyle.Message(text, timestamp, person2);
                } else {
                    CharSequence text2 = getText();
                    long timestamp2 = getTimestamp();
                    if (person != null) {
                        charSequence = person.getName();
                    }
                    frameworkMessage = new Notification.MessagingStyle.Message(text2, timestamp2, charSequence);
                }
                if (getDataMimeType() != null) {
                    frameworkMessage.setData(getDataMimeType(), getDataUri());
                }
                return frameworkMessage;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class InboxStyle extends Style {
        private static final String TEMPLATE_CLASS_NAME;
        private ArrayList<CharSequence> mTexts = new ArrayList<>();

        public InboxStyle() {
        }

        public InboxStyle(Builder builder) {
            setBuilder(builder);
        }

        public InboxStyle setBigContentTitle(CharSequence title) {
            this.mBigContentTitle = Builder.limitCharSequenceLength(title);
            return this;
        }

        public InboxStyle setSummaryText(CharSequence cs) {
            this.mSummaryText = Builder.limitCharSequenceLength(cs);
            this.mSummaryTextSet = true;
            return this;
        }

        public InboxStyle addLine(CharSequence cs) {
            if (cs != null) {
                this.mTexts.add(Builder.limitCharSequenceLength(cs));
            }
            return this;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 16) {
                Notification.InboxStyle style = new Notification.InboxStyle(builder.getBuilder()).setBigContentTitle(this.mBigContentTitle);
                if (this.mSummaryTextSet) {
                    style.setSummaryText(this.mSummaryText);
                }
                Iterator<CharSequence> it = this.mTexts.iterator();
                while (it.hasNext()) {
                    style.addLine(it.next());
                }
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void restoreFromCompatExtras(Bundle extras) {
            super.restoreFromCompatExtras(extras);
            this.mTexts.clear();
            if (extras.containsKey(NotificationCompat.EXTRA_TEXT_LINES)) {
                Collections.addAll(this.mTexts, extras.getCharSequenceArray(NotificationCompat.EXTRA_TEXT_LINES));
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        protected void clearCompatExtraKeys(Bundle extras) {
            super.clearCompatExtraKeys(extras);
            extras.remove(NotificationCompat.EXTRA_TEXT_LINES);
        }
    }

    /* loaded from: classes.dex */
    public static class DecoratedCustomViewStyle extends Style {
        private static final int MAX_ACTION_BUTTONS;
        private static final String TEMPLATE_CLASS_NAME;

        @Override // androidx.core.app.NotificationCompat.Style
        protected String getClassName() {
            return TEMPLATE_CLASS_NAME;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public boolean displayCustomViewInline() {
            return true;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public void apply(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 24) {
                builder.getBuilder().setStyle(new Notification.DecoratedCustomViewStyle());
            }
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT < 24 && this.mBuilder.getContentView() != null) {
                return createRemoteViews(this.mBuilder.getContentView(), false);
            }
            return null;
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor builder) {
            RemoteViews innerView;
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews bigContentView = this.mBuilder.getBigContentView();
            if (bigContentView != null) {
                innerView = bigContentView;
            } else {
                innerView = this.mBuilder.getContentView();
            }
            if (innerView == null) {
                return null;
            }
            return createRemoteViews(innerView, true);
        }

        @Override // androidx.core.app.NotificationCompat.Style
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor builder) {
            if (Build.VERSION.SDK_INT >= 24) {
                return null;
            }
            RemoteViews headsUp = this.mBuilder.getHeadsUpContentView();
            RemoteViews innerView = headsUp != null ? headsUp : this.mBuilder.getContentView();
            if (headsUp == null) {
                return null;
            }
            return createRemoteViews(innerView, true);
        }

        private RemoteViews createRemoteViews(RemoteViews innerView, boolean showActions) {
            int numActions;
            int actionVisibility = 0;
            RemoteViews remoteViews = applyStandardTemplate(true, R.layout.notification_template_custom_big, false);
            remoteViews.removeAllViews(R.id.actions);
            boolean actionsVisible = false;
            List<Action> nonContextualActions = getNonContextualActions(this.mBuilder.mActions);
            if (showActions && nonContextualActions != null && (numActions = Math.min(nonContextualActions.size(), 3)) > 0) {
                actionsVisible = true;
                for (int i = 0; i < numActions; i++) {
                    remoteViews.addView(R.id.actions, generateActionButton(nonContextualActions.get(i)));
                }
            }
            if (!actionsVisible) {
                actionVisibility = 8;
            }
            remoteViews.setViewVisibility(R.id.actions, actionVisibility);
            remoteViews.setViewVisibility(R.id.action_divider, actionVisibility);
            buildIntoRemoteViews(remoteViews, innerView);
            return remoteViews;
        }

        private static List<Action> getNonContextualActions(List<Action> actions) {
            if (actions == null) {
                return null;
            }
            List<Action> nonContextualActions = new ArrayList<>();
            for (Action action : actions) {
                if (!action.isContextual()) {
                    nonContextualActions.add(action);
                }
            }
            return nonContextualActions;
        }

        private RemoteViews generateActionButton(Action action) {
            int i;
            boolean tombstone = action.actionIntent == null;
            String packageName = this.mBuilder.mContext.getPackageName();
            if (tombstone) {
                i = R.layout.notification_action_tombstone;
            } else {
                i = R.layout.notification_action;
            }
            RemoteViews button = new RemoteViews(packageName, i);
            IconCompat icon = action.getIconCompat();
            if (icon != null) {
                button.setImageViewBitmap(R.id.action_image, createColoredBitmap(icon, this.mBuilder.mContext.getResources().getColor(R.color.notification_action_color_filter)));
            }
            button.setTextViewText(R.id.action_text, action.title);
            if (!tombstone) {
                button.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
            }
            if (Build.VERSION.SDK_INT >= 15) {
                button.setContentDescription(R.id.action_container, action.title);
            }
            return button;
        }
    }

    /* loaded from: classes.dex */
    public static class Action {
        static final String EXTRA_SEMANTIC_ACTION;
        static final String EXTRA_SHOWS_USER_INTERFACE;
        public static final int SEMANTIC_ACTION_ARCHIVE;
        public static final int SEMANTIC_ACTION_CALL;
        public static final int SEMANTIC_ACTION_DELETE;
        public static final int SEMANTIC_ACTION_MARK_AS_READ;
        public static final int SEMANTIC_ACTION_MARK_AS_UNREAD;
        public static final int SEMANTIC_ACTION_MUTE;
        public static final int SEMANTIC_ACTION_NONE;
        public static final int SEMANTIC_ACTION_REPLY;
        public static final int SEMANTIC_ACTION_THUMBS_DOWN;
        public static final int SEMANTIC_ACTION_THUMBS_UP;
        public static final int SEMANTIC_ACTION_UNMUTE;
        public PendingIntent actionIntent;
        @Deprecated
        public int icon;
        private boolean mAllowGeneratedReplies;
        private final RemoteInput[] mDataOnlyRemoteInputs;
        final Bundle mExtras;
        private IconCompat mIcon;
        private final boolean mIsContextual;
        private final RemoteInput[] mRemoteInputs;
        private final int mSemanticAction;
        boolean mShowsUserInterface;
        public CharSequence title;

        /* loaded from: classes.dex */
        public interface Extender {
            Builder extend(Builder builder);
        }

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface SemanticAction {
        }

        public Action(int icon, CharSequence title, PendingIntent intent) {
            this(icon != 0 ? IconCompat.createWithResource(null, "", icon) : null, title, intent);
        }

        public Action(IconCompat icon, CharSequence title, PendingIntent intent) {
            this(icon, title, intent, new Bundle(), (RemoteInput[]) null, (RemoteInput[]) null, true, 0, true, false);
        }

        public Action(int icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, RemoteInput[] dataOnlyRemoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual) {
            this(icon != 0 ? IconCompat.createWithResource(null, "", icon) : null, title, intent, extras, remoteInputs, dataOnlyRemoteInputs, allowGeneratedReplies, semanticAction, showsUserInterface, isContextual);
        }

        Action(IconCompat icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, RemoteInput[] dataOnlyRemoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual) {
            this.mShowsUserInterface = true;
            this.mIcon = icon;
            if (icon != null && icon.getType() == 2) {
                this.icon = icon.getResId();
            }
            this.title = Builder.limitCharSequenceLength(title);
            this.actionIntent = intent;
            this.mExtras = extras != null ? extras : new Bundle();
            this.mRemoteInputs = remoteInputs;
            this.mDataOnlyRemoteInputs = dataOnlyRemoteInputs;
            this.mAllowGeneratedReplies = allowGeneratedReplies;
            this.mSemanticAction = semanticAction;
            this.mShowsUserInterface = showsUserInterface;
            this.mIsContextual = isContextual;
        }

        @Deprecated
        public int getIcon() {
            return this.icon;
        }

        public IconCompat getIconCompat() {
            int i;
            if (this.mIcon == null && (i = this.icon) != 0) {
                this.mIcon = IconCompat.createWithResource(null, "", i);
            }
            return this.mIcon;
        }

        public CharSequence getTitle() {
            return this.title;
        }

        public PendingIntent getActionIntent() {
            return this.actionIntent;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }

        public boolean getAllowGeneratedReplies() {
            return this.mAllowGeneratedReplies;
        }

        public RemoteInput[] getRemoteInputs() {
            return this.mRemoteInputs;
        }

        public int getSemanticAction() {
            return this.mSemanticAction;
        }

        public boolean isContextual() {
            return this.mIsContextual;
        }

        public RemoteInput[] getDataOnlyRemoteInputs() {
            return this.mDataOnlyRemoteInputs;
        }

        public boolean getShowsUserInterface() {
            return this.mShowsUserInterface;
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            private boolean mAllowGeneratedReplies;
            private final Bundle mExtras;
            private final IconCompat mIcon;
            private final PendingIntent mIntent;
            private boolean mIsContextual;
            private ArrayList<RemoteInput> mRemoteInputs;
            private int mSemanticAction;
            private boolean mShowsUserInterface;
            private final CharSequence mTitle;

            public static Builder fromAndroidAction(Notification.Action action) {
                Builder builder;
                RemoteInput[] remoteInputs;
                if (Build.VERSION.SDK_INT < 23 || action.getIcon() == null) {
                    builder = new Builder(action.icon, action.title, action.actionIntent);
                } else {
                    builder = new Builder(IconCompat.createFromIcon(action.getIcon()), action.title, action.actionIntent);
                }
                if (!(Build.VERSION.SDK_INT < 20 || (remoteInputs = action.getRemoteInputs()) == null || remoteInputs.length == 0)) {
                    for (RemoteInput remoteInput : remoteInputs) {
                        builder.addRemoteInput(RemoteInput.fromPlatform(remoteInput));
                    }
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    builder.mAllowGeneratedReplies = action.getAllowGeneratedReplies();
                }
                if (Build.VERSION.SDK_INT >= 28) {
                    builder.setSemanticAction(action.getSemanticAction());
                }
                if (Build.VERSION.SDK_INT >= 29) {
                    builder.setContextual(action.isContextual());
                }
                return builder;
            }

            public Builder(IconCompat icon, CharSequence title, PendingIntent intent) {
                this(icon, title, intent, new Bundle(), null, true, 0, true, false);
            }

            public Builder(int icon, CharSequence title, PendingIntent intent) {
                this(icon != 0 ? IconCompat.createWithResource(null, "", icon) : null, title, intent, new Bundle(), null, true, 0, true, false);
            }

            public Builder(Action action) {
                this(action.getIconCompat(), action.title, action.actionIntent, new Bundle(action.mExtras), action.getRemoteInputs(), action.getAllowGeneratedReplies(), action.getSemanticAction(), action.mShowsUserInterface, action.isContextual());
            }

            private Builder(IconCompat icon, CharSequence title, PendingIntent intent, Bundle extras, RemoteInput[] remoteInputs, boolean allowGeneratedReplies, int semanticAction, boolean showsUserInterface, boolean isContextual) {
                ArrayList<RemoteInput> arrayList;
                this.mAllowGeneratedReplies = true;
                this.mShowsUserInterface = true;
                this.mIcon = icon;
                this.mTitle = Builder.limitCharSequenceLength(title);
                this.mIntent = intent;
                this.mExtras = extras;
                if (remoteInputs == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList<>(Arrays.asList(remoteInputs));
                }
                this.mRemoteInputs = arrayList;
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                this.mSemanticAction = semanticAction;
                this.mShowsUserInterface = showsUserInterface;
                this.mIsContextual = isContextual;
            }

            public Builder addExtras(Bundle extras) {
                if (extras != null) {
                    this.mExtras.putAll(extras);
                }
                return this;
            }

            public Bundle getExtras() {
                return this.mExtras;
            }

            public Builder addRemoteInput(RemoteInput remoteInput) {
                if (this.mRemoteInputs == null) {
                    this.mRemoteInputs = new ArrayList<>();
                }
                if (remoteInput != null) {
                    this.mRemoteInputs.add(remoteInput);
                }
                return this;
            }

            public Builder setAllowGeneratedReplies(boolean allowGeneratedReplies) {
                this.mAllowGeneratedReplies = allowGeneratedReplies;
                return this;
            }

            public Builder setSemanticAction(int semanticAction) {
                this.mSemanticAction = semanticAction;
                return this;
            }

            public Builder setContextual(boolean isContextual) {
                this.mIsContextual = isContextual;
                return this;
            }

            public Builder setShowsUserInterface(boolean showsUserInterface) {
                this.mShowsUserInterface = showsUserInterface;
                return this;
            }

            public Builder extend(Extender extender) {
                extender.extend(this);
                return this;
            }

            private void checkContextualActionNullFields() {
                if (this.mIsContextual && this.mIntent == null) {
                    throw new NullPointerException("Contextual Actions must contain a valid PendingIntent");
                }
            }

            public Action build() {
                checkContextualActionNullFields();
                List<RemoteInput> dataOnlyInputs = new ArrayList<>();
                List<RemoteInput> textInputs = new ArrayList<>();
                ArrayList<RemoteInput> arrayList = this.mRemoteInputs;
                if (arrayList != null) {
                    Iterator<RemoteInput> it = arrayList.iterator();
                    while (it.hasNext()) {
                        RemoteInput input = it.next();
                        if (input.isDataOnly()) {
                            dataOnlyInputs.add(input);
                        } else {
                            textInputs.add(input);
                        }
                    }
                }
                RemoteInput[] textInputsArr = null;
                RemoteInput[] dataOnlyInputsArr = dataOnlyInputs.isEmpty() ? null : (RemoteInput[]) dataOnlyInputs.toArray(new RemoteInput[dataOnlyInputs.size()]);
                if (!textInputs.isEmpty()) {
                    textInputsArr = (RemoteInput[]) textInputs.toArray(new RemoteInput[textInputs.size()]);
                }
                return new Action(this.mIcon, this.mTitle, this.mIntent, this.mExtras, textInputsArr, dataOnlyInputsArr, this.mAllowGeneratedReplies, this.mSemanticAction, this.mShowsUserInterface, this.mIsContextual);
            }
        }

        /* loaded from: classes.dex */
        public static final class WearableExtender implements Extender {
            private static final int DEFAULT_FLAGS;
            private static final String EXTRA_WEARABLE_EXTENSIONS;
            private static final int FLAG_AVAILABLE_OFFLINE;
            private static final int FLAG_HINT_DISPLAY_INLINE;
            private static final int FLAG_HINT_LAUNCHES_ACTIVITY;
            private static final String KEY_CANCEL_LABEL;
            private static final String KEY_CONFIRM_LABEL;
            private static final String KEY_FLAGS;
            private static final String KEY_IN_PROGRESS_LABEL;
            private CharSequence mCancelLabel;
            private CharSequence mConfirmLabel;
            private int mFlags;
            private CharSequence mInProgressLabel;

            public WearableExtender() {
                this.mFlags = 1;
            }

            public WearableExtender(Action action) {
                this.mFlags = 1;
                Bundle wearableBundle = action.getExtras().getBundle(EXTRA_WEARABLE_EXTENSIONS);
                if (wearableBundle != null) {
                    this.mFlags = wearableBundle.getInt(KEY_FLAGS, 1);
                    this.mInProgressLabel = wearableBundle.getCharSequence(KEY_IN_PROGRESS_LABEL);
                    this.mConfirmLabel = wearableBundle.getCharSequence(KEY_CONFIRM_LABEL);
                    this.mCancelLabel = wearableBundle.getCharSequence(KEY_CANCEL_LABEL);
                }
            }

            @Override // androidx.core.app.NotificationCompat.Action.Extender
            public Builder extend(Builder builder) {
                Bundle wearableBundle = new Bundle();
                int i = this.mFlags;
                if (i != 1) {
                    wearableBundle.putInt(KEY_FLAGS, i);
                }
                CharSequence charSequence = this.mInProgressLabel;
                if (charSequence != null) {
                    wearableBundle.putCharSequence(KEY_IN_PROGRESS_LABEL, charSequence);
                }
                CharSequence charSequence2 = this.mConfirmLabel;
                if (charSequence2 != null) {
                    wearableBundle.putCharSequence(KEY_CONFIRM_LABEL, charSequence2);
                }
                CharSequence charSequence3 = this.mCancelLabel;
                if (charSequence3 != null) {
                    wearableBundle.putCharSequence(KEY_CANCEL_LABEL, charSequence3);
                }
                builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
                return builder;
            }

            public WearableExtender clone() {
                WearableExtender that = new WearableExtender();
                that.mFlags = this.mFlags;
                that.mInProgressLabel = this.mInProgressLabel;
                that.mConfirmLabel = this.mConfirmLabel;
                that.mCancelLabel = this.mCancelLabel;
                return that;
            }

            public WearableExtender setAvailableOffline(boolean availableOffline) {
                setFlag(1, availableOffline);
                return this;
            }

            public boolean isAvailableOffline() {
                return (this.mFlags & 1) != 0;
            }

            private void setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
            }

            @Deprecated
            public WearableExtender setInProgressLabel(CharSequence label) {
                this.mInProgressLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getInProgressLabel() {
                return this.mInProgressLabel;
            }

            @Deprecated
            public WearableExtender setConfirmLabel(CharSequence label) {
                this.mConfirmLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getConfirmLabel() {
                return this.mConfirmLabel;
            }

            @Deprecated
            public WearableExtender setCancelLabel(CharSequence label) {
                this.mCancelLabel = label;
                return this;
            }

            @Deprecated
            public CharSequence getCancelLabel() {
                return this.mCancelLabel;
            }

            public WearableExtender setHintLaunchesActivity(boolean hintLaunchesActivity) {
                setFlag(2, hintLaunchesActivity);
                return this;
            }

            public boolean getHintLaunchesActivity() {
                return (this.mFlags & 2) != 0;
            }

            public WearableExtender setHintDisplayActionInline(boolean hintDisplayInline) {
                setFlag(4, hintDisplayInline);
                return this;
            }

            public boolean getHintDisplayActionInline() {
                return (this.mFlags & 4) != 0;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class WearableExtender implements Extender {
        private static final int DEFAULT_CONTENT_ICON_GRAVITY;
        private static final int DEFAULT_FLAGS;
        private static final int DEFAULT_GRAVITY;
        private static final String EXTRA_WEARABLE_EXTENSIONS;
        private static final int FLAG_BIG_PICTURE_AMBIENT;
        private static final int FLAG_CONTENT_INTENT_AVAILABLE_OFFLINE;
        private static final int FLAG_HINT_AVOID_BACKGROUND_CLIPPING;
        private static final int FLAG_HINT_CONTENT_INTENT_LAUNCHES_ACTIVITY;
        private static final int FLAG_HINT_HIDE_ICON;
        private static final int FLAG_HINT_SHOW_BACKGROUND_ONLY;
        private static final int FLAG_START_SCROLL_BOTTOM;
        private static final String KEY_ACTIONS;
        private static final String KEY_BACKGROUND;
        private static final String KEY_BRIDGE_TAG;
        private static final String KEY_CONTENT_ACTION_INDEX;
        private static final String KEY_CONTENT_ICON;
        private static final String KEY_CONTENT_ICON_GRAVITY;
        private static final String KEY_CUSTOM_CONTENT_HEIGHT;
        private static final String KEY_CUSTOM_SIZE_PRESET;
        private static final String KEY_DISMISSAL_ID;
        private static final String KEY_DISPLAY_INTENT;
        private static final String KEY_FLAGS;
        private static final String KEY_GRAVITY;
        private static final String KEY_HINT_SCREEN_TIMEOUT;
        private static final String KEY_PAGES;
        @Deprecated
        public static final int SCREEN_TIMEOUT_LONG;
        @Deprecated
        public static final int SCREEN_TIMEOUT_SHORT;
        @Deprecated
        public static final int SIZE_DEFAULT;
        @Deprecated
        public static final int SIZE_FULL_SCREEN;
        @Deprecated
        public static final int SIZE_LARGE;
        @Deprecated
        public static final int SIZE_MEDIUM;
        @Deprecated
        public static final int SIZE_SMALL;
        @Deprecated
        public static final int SIZE_XSMALL;
        public static final int UNSET_ACTION_INDEX;
        private ArrayList<Action> mActions;
        private Bitmap mBackground;
        private String mBridgeTag;
        private int mContentActionIndex;
        private int mContentIcon;
        private int mContentIconGravity;
        private int mCustomContentHeight;
        private int mCustomSizePreset;
        private String mDismissalId;
        private PendingIntent mDisplayIntent;
        private int mFlags;
        private int mGravity;
        private int mHintScreenTimeout;
        private ArrayList<Notification> mPages;

        public WearableExtender() {
            this.mActions = new ArrayList<>();
            this.mFlags = 1;
            this.mPages = new ArrayList<>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
        }

        public WearableExtender(Notification notification) {
            Bundle wearableBundle;
            this.mActions = new ArrayList<>();
            this.mFlags = 1;
            this.mPages = new ArrayList<>();
            this.mContentIconGravity = 8388613;
            this.mContentActionIndex = -1;
            this.mCustomSizePreset = 0;
            this.mGravity = 80;
            Bundle extras = NotificationCompat.getExtras(notification);
            if (extras != null) {
                wearableBundle = extras.getBundle(EXTRA_WEARABLE_EXTENSIONS);
            } else {
                wearableBundle = null;
            }
            if (wearableBundle != null) {
                ArrayList<Parcelable> parcelables = wearableBundle.getParcelableArrayList(KEY_ACTIONS);
                if (Build.VERSION.SDK_INT >= 16 && parcelables != null) {
                    Action[] actions = new Action[parcelables.size()];
                    for (int i = 0; i < actions.length; i++) {
                        if (Build.VERSION.SDK_INT >= 20) {
                            actions[i] = NotificationCompat.getActionCompatFromAction((Notification.Action) parcelables.get(i));
                        } else if (Build.VERSION.SDK_INT >= 16) {
                            actions[i] = NotificationCompatJellybean.getActionFromBundle((Bundle) parcelables.get(i));
                        }
                    }
                    Collections.addAll(this.mActions, actions);
                }
                this.mFlags = wearableBundle.getInt(KEY_FLAGS, 1);
                this.mDisplayIntent = (PendingIntent) wearableBundle.getParcelable(KEY_DISPLAY_INTENT);
                Notification[] pages = NotificationCompat.getNotificationArrayFromBundle(wearableBundle, KEY_PAGES);
                if (pages != null) {
                    Collections.addAll(this.mPages, pages);
                }
                this.mBackground = (Bitmap) wearableBundle.getParcelable(KEY_BACKGROUND);
                this.mContentIcon = wearableBundle.getInt(KEY_CONTENT_ICON);
                this.mContentIconGravity = wearableBundle.getInt(KEY_CONTENT_ICON_GRAVITY, 8388613);
                this.mContentActionIndex = wearableBundle.getInt(KEY_CONTENT_ACTION_INDEX, -1);
                this.mCustomSizePreset = wearableBundle.getInt(KEY_CUSTOM_SIZE_PRESET, 0);
                this.mCustomContentHeight = wearableBundle.getInt(KEY_CUSTOM_CONTENT_HEIGHT);
                this.mGravity = wearableBundle.getInt(KEY_GRAVITY, 80);
                this.mHintScreenTimeout = wearableBundle.getInt(KEY_HINT_SCREEN_TIMEOUT);
                this.mDismissalId = wearableBundle.getString(KEY_DISMISSAL_ID);
                this.mBridgeTag = wearableBundle.getString(KEY_BRIDGE_TAG);
            }
        }

        @Override // androidx.core.app.NotificationCompat.Extender
        public Builder extend(Builder builder) {
            Bundle wearableBundle = new Bundle();
            if (!this.mActions.isEmpty()) {
                if (Build.VERSION.SDK_INT >= 16) {
                    ArrayList<Parcelable> parcelables = new ArrayList<>(this.mActions.size());
                    Iterator<Action> it = this.mActions.iterator();
                    while (it.hasNext()) {
                        Action action = it.next();
                        if (Build.VERSION.SDK_INT >= 20) {
                            parcelables.add(getActionFromActionCompat(action));
                        } else if (Build.VERSION.SDK_INT >= 16) {
                            parcelables.add(NotificationCompatJellybean.getBundleForAction(action));
                        }
                    }
                    wearableBundle.putParcelableArrayList(KEY_ACTIONS, parcelables);
                } else {
                    wearableBundle.putParcelableArrayList(KEY_ACTIONS, null);
                }
            }
            int i = this.mFlags;
            if (i != 1) {
                wearableBundle.putInt(KEY_FLAGS, i);
            }
            PendingIntent pendingIntent = this.mDisplayIntent;
            if (pendingIntent != null) {
                wearableBundle.putParcelable(KEY_DISPLAY_INTENT, pendingIntent);
            }
            if (!this.mPages.isEmpty()) {
                ArrayList<Notification> arrayList = this.mPages;
                wearableBundle.putParcelableArray(KEY_PAGES, (Parcelable[]) arrayList.toArray(new Notification[arrayList.size()]));
            }
            Bitmap bitmap = this.mBackground;
            if (bitmap != null) {
                wearableBundle.putParcelable(KEY_BACKGROUND, bitmap);
            }
            int i2 = this.mContentIcon;
            if (i2 != 0) {
                wearableBundle.putInt(KEY_CONTENT_ICON, i2);
            }
            int i3 = this.mContentIconGravity;
            if (i3 != 8388613) {
                wearableBundle.putInt(KEY_CONTENT_ICON_GRAVITY, i3);
            }
            int i4 = this.mContentActionIndex;
            if (i4 != -1) {
                wearableBundle.putInt(KEY_CONTENT_ACTION_INDEX, i4);
            }
            int i5 = this.mCustomSizePreset;
            if (i5 != 0) {
                wearableBundle.putInt(KEY_CUSTOM_SIZE_PRESET, i5);
            }
            int i6 = this.mCustomContentHeight;
            if (i6 != 0) {
                wearableBundle.putInt(KEY_CUSTOM_CONTENT_HEIGHT, i6);
            }
            int i7 = this.mGravity;
            if (i7 != 80) {
                wearableBundle.putInt(KEY_GRAVITY, i7);
            }
            int i8 = this.mHintScreenTimeout;
            if (i8 != 0) {
                wearableBundle.putInt(KEY_HINT_SCREEN_TIMEOUT, i8);
            }
            String str = this.mDismissalId;
            if (str != null) {
                wearableBundle.putString(KEY_DISMISSAL_ID, str);
            }
            String str2 = this.mBridgeTag;
            if (str2 != null) {
                wearableBundle.putString(KEY_BRIDGE_TAG, str2);
            }
            builder.getExtras().putBundle(EXTRA_WEARABLE_EXTENSIONS, wearableBundle);
            return builder;
        }

        private static Notification.Action getActionFromActionCompat(Action actionCompat) {
            Notification.Action.Builder actionBuilder;
            Bundle actionExtras;
            if (Build.VERSION.SDK_INT >= 23) {
                IconCompat iconCompat = actionCompat.getIconCompat();
                actionBuilder = new Notification.Action.Builder(iconCompat == null ? null : iconCompat.toIcon(), actionCompat.getTitle(), actionCompat.getActionIntent());
            } else {
                IconCompat icon = actionCompat.getIconCompat();
                int iconResId = 0;
                if (icon != null && icon.getType() == 2) {
                    iconResId = icon.getResId();
                }
                actionBuilder = new Notification.Action.Builder(iconResId, actionCompat.getTitle(), actionCompat.getActionIntent());
            }
            if (actionCompat.getExtras() != null) {
                actionExtras = new Bundle(actionCompat.getExtras());
            } else {
                actionExtras = new Bundle();
            }
            actionExtras.putBoolean("android.support.allowGeneratedReplies", actionCompat.getAllowGeneratedReplies());
            if (Build.VERSION.SDK_INT >= 24) {
                actionBuilder.setAllowGeneratedReplies(actionCompat.getAllowGeneratedReplies());
            }
            actionBuilder.addExtras(actionExtras);
            RemoteInput[] remoteInputCompats = actionCompat.getRemoteInputs();
            if (remoteInputCompats != null) {
                for (RemoteInput remoteInput : RemoteInput.fromCompat(remoteInputCompats)) {
                    actionBuilder.addRemoteInput(remoteInput);
                }
            }
            return actionBuilder.build();
        }

        public WearableExtender clone() {
            WearableExtender that = new WearableExtender();
            that.mActions = new ArrayList<>(this.mActions);
            that.mFlags = this.mFlags;
            that.mDisplayIntent = this.mDisplayIntent;
            that.mPages = new ArrayList<>(this.mPages);
            that.mBackground = this.mBackground;
            that.mContentIcon = this.mContentIcon;
            that.mContentIconGravity = this.mContentIconGravity;
            that.mContentActionIndex = this.mContentActionIndex;
            that.mCustomSizePreset = this.mCustomSizePreset;
            that.mCustomContentHeight = this.mCustomContentHeight;
            that.mGravity = this.mGravity;
            that.mHintScreenTimeout = this.mHintScreenTimeout;
            that.mDismissalId = this.mDismissalId;
            that.mBridgeTag = this.mBridgeTag;
            return that;
        }

        public WearableExtender addAction(Action action) {
            this.mActions.add(action);
            return this;
        }

        public WearableExtender addActions(List<Action> actions) {
            this.mActions.addAll(actions);
            return this;
        }

        public WearableExtender clearActions() {
            this.mActions.clear();
            return this;
        }

        public List<Action> getActions() {
            return this.mActions;
        }

        @Deprecated
        public WearableExtender setDisplayIntent(PendingIntent intent) {
            this.mDisplayIntent = intent;
            return this;
        }

        @Deprecated
        public PendingIntent getDisplayIntent() {
            return this.mDisplayIntent;
        }

        @Deprecated
        public WearableExtender addPage(Notification page) {
            this.mPages.add(page);
            return this;
        }

        @Deprecated
        public WearableExtender addPages(List<Notification> pages) {
            this.mPages.addAll(pages);
            return this;
        }

        @Deprecated
        public WearableExtender clearPages() {
            this.mPages.clear();
            return this;
        }

        @Deprecated
        public List<Notification> getPages() {
            return this.mPages;
        }

        @Deprecated
        public WearableExtender setBackground(Bitmap background) {
            this.mBackground = background;
            return this;
        }

        @Deprecated
        public Bitmap getBackground() {
            return this.mBackground;
        }

        @Deprecated
        public WearableExtender setContentIcon(int icon) {
            this.mContentIcon = icon;
            return this;
        }

        @Deprecated
        public int getContentIcon() {
            return this.mContentIcon;
        }

        @Deprecated
        public WearableExtender setContentIconGravity(int contentIconGravity) {
            this.mContentIconGravity = contentIconGravity;
            return this;
        }

        @Deprecated
        public int getContentIconGravity() {
            return this.mContentIconGravity;
        }

        public WearableExtender setContentAction(int actionIndex) {
            this.mContentActionIndex = actionIndex;
            return this;
        }

        public int getContentAction() {
            return this.mContentActionIndex;
        }

        @Deprecated
        public WearableExtender setGravity(int gravity) {
            this.mGravity = gravity;
            return this;
        }

        @Deprecated
        public int getGravity() {
            return this.mGravity;
        }

        @Deprecated
        public WearableExtender setCustomSizePreset(int sizePreset) {
            this.mCustomSizePreset = sizePreset;
            return this;
        }

        @Deprecated
        public int getCustomSizePreset() {
            return this.mCustomSizePreset;
        }

        @Deprecated
        public WearableExtender setCustomContentHeight(int height) {
            this.mCustomContentHeight = height;
            return this;
        }

        @Deprecated
        public int getCustomContentHeight() {
            return this.mCustomContentHeight;
        }

        public WearableExtender setStartScrollBottom(boolean startScrollBottom) {
            setFlag(8, startScrollBottom);
            return this;
        }

        public boolean getStartScrollBottom() {
            return (this.mFlags & 8) != 0;
        }

        public WearableExtender setContentIntentAvailableOffline(boolean contentIntentAvailableOffline) {
            setFlag(1, contentIntentAvailableOffline);
            return this;
        }

        public boolean getContentIntentAvailableOffline() {
            return (this.mFlags & 1) != 0;
        }

        @Deprecated
        public WearableExtender setHintHideIcon(boolean hintHideIcon) {
            setFlag(2, hintHideIcon);
            return this;
        }

        @Deprecated
        public boolean getHintHideIcon() {
            return (this.mFlags & 2) != 0;
        }

        @Deprecated
        public WearableExtender setHintShowBackgroundOnly(boolean hintShowBackgroundOnly) {
            setFlag(4, hintShowBackgroundOnly);
            return this;
        }

        @Deprecated
        public boolean getHintShowBackgroundOnly() {
            return (this.mFlags & 4) != 0;
        }

        @Deprecated
        public WearableExtender setHintAvoidBackgroundClipping(boolean hintAvoidBackgroundClipping) {
            setFlag(16, hintAvoidBackgroundClipping);
            return this;
        }

        @Deprecated
        public boolean getHintAvoidBackgroundClipping() {
            return (this.mFlags & 16) != 0;
        }

        @Deprecated
        public WearableExtender setHintScreenTimeout(int timeout) {
            this.mHintScreenTimeout = timeout;
            return this;
        }

        @Deprecated
        public int getHintScreenTimeout() {
            return this.mHintScreenTimeout;
        }

        @Deprecated
        public WearableExtender setHintAmbientBigPicture(boolean hintAmbientBigPicture) {
            setFlag(32, hintAmbientBigPicture);
            return this;
        }

        @Deprecated
        public boolean getHintAmbientBigPicture() {
            return (this.mFlags & 32) != 0;
        }

        public WearableExtender setHintContentIntentLaunchesActivity(boolean hintContentIntentLaunchesActivity) {
            setFlag(64, hintContentIntentLaunchesActivity);
            return this;
        }

        public boolean getHintContentIntentLaunchesActivity() {
            return (this.mFlags & 64) != 0;
        }

        public WearableExtender setDismissalId(String dismissalId) {
            this.mDismissalId = dismissalId;
            return this;
        }

        public String getDismissalId() {
            return this.mDismissalId;
        }

        public WearableExtender setBridgeTag(String bridgeTag) {
            this.mBridgeTag = bridgeTag;
            return this;
        }

        public String getBridgeTag() {
            return this.mBridgeTag;
        }

        private void setFlag(int mask, boolean value) {
            if (value) {
                this.mFlags |= mask;
            } else {
                this.mFlags &= ~mask;
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class CarExtender implements Extender {
        static final String EXTRA_CAR_EXTENDER;
        private static final String EXTRA_COLOR;
        private static final String EXTRA_CONVERSATION;
        static final String EXTRA_INVISIBLE_ACTIONS;
        private static final String EXTRA_LARGE_ICON;
        private static final String KEY_AUTHOR;
        private static final String KEY_MESSAGES;
        private static final String KEY_ON_READ;
        private static final String KEY_ON_REPLY;
        private static final String KEY_PARTICIPANTS;
        private static final String KEY_REMOTE_INPUT;
        private static final String KEY_TEXT;
        private static final String KEY_TIMESTAMP;
        private int mColor;
        private Bitmap mLargeIcon;
        private UnreadConversation mUnreadConversation;

        public CarExtender() {
            this.mColor = 0;
        }

        public CarExtender(Notification notification) {
            this.mColor = 0;
            if (Build.VERSION.SDK_INT >= 21) {
                Bundle carBundle = NotificationCompat.getExtras(notification) == null ? null : NotificationCompat.getExtras(notification).getBundle(EXTRA_CAR_EXTENDER);
                if (carBundle != null) {
                    this.mLargeIcon = (Bitmap) carBundle.getParcelable(EXTRA_LARGE_ICON);
                    this.mColor = carBundle.getInt(EXTRA_COLOR, 0);
                    this.mUnreadConversation = getUnreadConversationFromBundle(carBundle.getBundle(EXTRA_CONVERSATION));
                }
            }
        }

        private static UnreadConversation getUnreadConversationFromBundle(Bundle b) {
            RemoteInput remoteInputCompat;
            int i;
            if (b == null) {
                return null;
            }
            Parcelable[] parcelableMessages = b.getParcelableArray(KEY_MESSAGES);
            String[] messages = null;
            if (parcelableMessages != null) {
                String[] tmp = new String[parcelableMessages.length];
                boolean success = true;
                int i2 = 0;
                while (true) {
                    if (i2 >= tmp.length) {
                        break;
                    } else if (!(parcelableMessages[i2] instanceof Bundle)) {
                        success = false;
                        break;
                    } else {
                        tmp[i2] = ((Bundle) parcelableMessages[i2]).getString(KEY_TEXT);
                        if (tmp[i2] == null) {
                            success = false;
                            break;
                        }
                        i2++;
                    }
                }
                if (!success) {
                    return null;
                }
                messages = tmp;
            }
            PendingIntent onRead = (PendingIntent) b.getParcelable(KEY_ON_READ);
            PendingIntent onReply = (PendingIntent) b.getParcelable(KEY_ON_REPLY);
            RemoteInput remoteInput = (RemoteInput) b.getParcelable(KEY_REMOTE_INPUT);
            String[] participants = b.getStringArray(KEY_PARTICIPANTS);
            if (participants == null || participants.length != 1) {
                return null;
            }
            if (remoteInput != null) {
                String resultKey = remoteInput.getResultKey();
                CharSequence label = remoteInput.getLabel();
                CharSequence[] choices = remoteInput.getChoices();
                boolean allowFreeFormInput = remoteInput.getAllowFreeFormInput();
                if (Build.VERSION.SDK_INT >= 29) {
                    i = remoteInput.getEditChoicesBeforeSending();
                } else {
                    i = 0;
                }
                remoteInputCompat = new RemoteInput(resultKey, label, choices, allowFreeFormInput, i, remoteInput.getExtras(), null);
            } else {
                remoteInputCompat = null;
            }
            return new UnreadConversation(messages, remoteInputCompat, onReply, onRead, participants, b.getLong(KEY_TIMESTAMP));
        }

        private static Bundle getBundleForUnreadConversation(UnreadConversation uc) {
            Bundle b = new Bundle();
            String author = null;
            if (uc.getParticipants() != null && uc.getParticipants().length > 1) {
                author = uc.getParticipants()[0];
            }
            Parcelable[] messages = new Parcelable[uc.getMessages().length];
            for (int i = 0; i < messages.length; i++) {
                Bundle m = new Bundle();
                m.putString(KEY_TEXT, uc.getMessages()[i]);
                m.putString(KEY_AUTHOR, author);
                messages[i] = m;
            }
            b.putParcelableArray(KEY_MESSAGES, messages);
            RemoteInput remoteInputCompat = uc.getRemoteInput();
            if (remoteInputCompat != null) {
                b.putParcelable(KEY_REMOTE_INPUT, new RemoteInput.Builder(remoteInputCompat.getResultKey()).setLabel(remoteInputCompat.getLabel()).setChoices(remoteInputCompat.getChoices()).setAllowFreeFormInput(remoteInputCompat.getAllowFreeFormInput()).addExtras(remoteInputCompat.getExtras()).build());
            }
            b.putParcelable(KEY_ON_REPLY, uc.getReplyPendingIntent());
            b.putParcelable(KEY_ON_READ, uc.getReadPendingIntent());
            b.putStringArray(KEY_PARTICIPANTS, uc.getParticipants());
            b.putLong(KEY_TIMESTAMP, uc.getLatestTimestamp());
            return b;
        }

        @Override // androidx.core.app.NotificationCompat.Extender
        public Builder extend(Builder builder) {
            if (Build.VERSION.SDK_INT < 21) {
                return builder;
            }
            Bundle carExtensions = new Bundle();
            Bitmap bitmap = this.mLargeIcon;
            if (bitmap != null) {
                carExtensions.putParcelable(EXTRA_LARGE_ICON, bitmap);
            }
            int i = this.mColor;
            if (i != 0) {
                carExtensions.putInt(EXTRA_COLOR, i);
            }
            UnreadConversation unreadConversation = this.mUnreadConversation;
            if (unreadConversation != null) {
                carExtensions.putBundle(EXTRA_CONVERSATION, getBundleForUnreadConversation(unreadConversation));
            }
            builder.getExtras().putBundle(EXTRA_CAR_EXTENDER, carExtensions);
            return builder;
        }

        public CarExtender setColor(int color) {
            this.mColor = color;
            return this;
        }

        public int getColor() {
            return this.mColor;
        }

        public CarExtender setLargeIcon(Bitmap largeIcon) {
            this.mLargeIcon = largeIcon;
            return this;
        }

        public Bitmap getLargeIcon() {
            return this.mLargeIcon;
        }

        @Deprecated
        public CarExtender setUnreadConversation(UnreadConversation unreadConversation) {
            this.mUnreadConversation = unreadConversation;
            return this;
        }

        @Deprecated
        public UnreadConversation getUnreadConversation() {
            return this.mUnreadConversation;
        }

        @Deprecated
        /* loaded from: classes.dex */
        public static class UnreadConversation {
            private final long mLatestTimestamp;
            private final String[] mMessages;
            private final String[] mParticipants;
            private final PendingIntent mReadPendingIntent;
            private final RemoteInput mRemoteInput;
            private final PendingIntent mReplyPendingIntent;

            UnreadConversation(String[] messages, RemoteInput remoteInput, PendingIntent replyPendingIntent, PendingIntent readPendingIntent, String[] participants, long latestTimestamp) {
                this.mMessages = messages;
                this.mRemoteInput = remoteInput;
                this.mReadPendingIntent = readPendingIntent;
                this.mReplyPendingIntent = replyPendingIntent;
                this.mParticipants = participants;
                this.mLatestTimestamp = latestTimestamp;
            }

            public String[] getMessages() {
                return this.mMessages;
            }

            public RemoteInput getRemoteInput() {
                return this.mRemoteInput;
            }

            public PendingIntent getReplyPendingIntent() {
                return this.mReplyPendingIntent;
            }

            public PendingIntent getReadPendingIntent() {
                return this.mReadPendingIntent;
            }

            public String[] getParticipants() {
                return this.mParticipants;
            }

            public String getParticipant() {
                String[] strArr = this.mParticipants;
                if (strArr.length > 0) {
                    return strArr[0];
                }
                return null;
            }

            public long getLatestTimestamp() {
                return this.mLatestTimestamp;
            }

            /* loaded from: classes.dex */
            public static class Builder {
                private long mLatestTimestamp;
                private final List<String> mMessages = new ArrayList();
                private final String mParticipant;
                private PendingIntent mReadPendingIntent;
                private RemoteInput mRemoteInput;
                private PendingIntent mReplyPendingIntent;

                public Builder(String name) {
                    this.mParticipant = name;
                }

                public Builder addMessage(String message) {
                    if (message != null) {
                        this.mMessages.add(message);
                    }
                    return this;
                }

                public Builder setReplyAction(PendingIntent pendingIntent, RemoteInput remoteInput) {
                    this.mRemoteInput = remoteInput;
                    this.mReplyPendingIntent = pendingIntent;
                    return this;
                }

                public Builder setReadPendingIntent(PendingIntent pendingIntent) {
                    this.mReadPendingIntent = pendingIntent;
                    return this;
                }

                public Builder setLatestTimestamp(long timestamp) {
                    this.mLatestTimestamp = timestamp;
                    return this;
                }

                public UnreadConversation build() {
                    List<String> list = this.mMessages;
                    return new UnreadConversation((String[]) list.toArray(new String[list.size()]), this.mRemoteInput, this.mReplyPendingIntent, this.mReadPendingIntent, new String[]{this.mParticipant}, this.mLatestTimestamp);
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class BubbleMetadata {
        private static final int FLAG_AUTO_EXPAND_BUBBLE;
        private static final int FLAG_SUPPRESS_NOTIFICATION;
        private PendingIntent mDeleteIntent;
        private int mDesiredHeight;
        private int mDesiredHeightResId;
        private int mFlags;
        private IconCompat mIcon;
        private PendingIntent mPendingIntent;
        private String mShortcutId;

        private BubbleMetadata(PendingIntent expandIntent, PendingIntent deleteIntent, IconCompat icon, int height, int heightResId, int flags, String shortcutId) {
            this.mPendingIntent = expandIntent;
            this.mIcon = icon;
            this.mDesiredHeight = height;
            this.mDesiredHeightResId = heightResId;
            this.mDeleteIntent = deleteIntent;
            this.mFlags = flags;
            this.mShortcutId = shortcutId;
        }

        public PendingIntent getIntent() {
            return this.mPendingIntent;
        }

        public String getShortcutId() {
            return this.mShortcutId;
        }

        public PendingIntent getDeleteIntent() {
            return this.mDeleteIntent;
        }

        public IconCompat getIcon() {
            return this.mIcon;
        }

        public int getDesiredHeight() {
            return this.mDesiredHeight;
        }

        public int getDesiredHeightResId() {
            return this.mDesiredHeightResId;
        }

        public boolean getAutoExpandBubble() {
            return (this.mFlags & 1) != 0;
        }

        public boolean isNotificationSuppressed() {
            return (this.mFlags & 2) != 0;
        }

        public void setFlags(int flags) {
            this.mFlags = flags;
        }

        public static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
            if (compatMetadata == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 30) {
                return Api30Impl.toPlatform(compatMetadata);
            }
            if (Build.VERSION.SDK_INT == 29) {
                return Api29Impl.toPlatform(compatMetadata);
            }
            return null;
        }

        public static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
            if (platformMetadata == null) {
                return null;
            }
            if (Build.VERSION.SDK_INT >= 30) {
                return Api30Impl.fromPlatform(platformMetadata);
            }
            if (Build.VERSION.SDK_INT == 29) {
                return Api29Impl.fromPlatform(platformMetadata);
            }
            return null;
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            private PendingIntent mDeleteIntent;
            private int mDesiredHeight;
            private int mDesiredHeightResId;
            private int mFlags;
            private IconCompat mIcon;
            private PendingIntent mPendingIntent;
            private String mShortcutId;

            @Deprecated
            public Builder() {
            }

            public Builder(String shortcutId) {
                if (!TextUtils.isEmpty(shortcutId)) {
                    this.mShortcutId = shortcutId;
                    return;
                }
                throw new NullPointerException("Bubble requires a non-null shortcut id");
            }

            public Builder(PendingIntent intent, IconCompat icon) {
                if (intent == null) {
                    throw new NullPointerException("Bubble requires non-null pending intent");
                } else if (icon != null) {
                    this.mPendingIntent = intent;
                    this.mIcon = icon;
                } else {
                    throw new NullPointerException("Bubbles require non-null icon");
                }
            }

            public Builder setIntent(PendingIntent intent) {
                if (this.mShortcutId != null) {
                    throw new IllegalStateException("Created as a shortcut bubble, cannot set a PendingIntent. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
                } else if (intent != null) {
                    this.mPendingIntent = intent;
                    return this;
                } else {
                    throw new NullPointerException("Bubble requires non-null pending intent");
                }
            }

            public Builder setIcon(IconCompat icon) {
                if (this.mShortcutId != null) {
                    throw new IllegalStateException("Created as a shortcut bubble, cannot set an Icon. Consider using BubbleMetadata.Builder(PendingIntent,Icon) instead.");
                } else if (icon != null) {
                    this.mIcon = icon;
                    return this;
                } else {
                    throw new NullPointerException("Bubbles require non-null icon");
                }
            }

            public Builder setDesiredHeight(int height) {
                this.mDesiredHeight = Math.max(height, 0);
                this.mDesiredHeightResId = 0;
                return this;
            }

            public Builder setDesiredHeightResId(int heightResId) {
                this.mDesiredHeightResId = heightResId;
                this.mDesiredHeight = 0;
                return this;
            }

            public Builder setAutoExpandBubble(boolean shouldExpand) {
                setFlag(1, shouldExpand);
                return this;
            }

            public Builder setSuppressNotification(boolean shouldSuppressNotif) {
                setFlag(2, shouldSuppressNotif);
                return this;
            }

            public Builder setDeleteIntent(PendingIntent deleteIntent) {
                this.mDeleteIntent = deleteIntent;
                return this;
            }

            public BubbleMetadata build() {
                if (this.mShortcutId == null && this.mPendingIntent == null) {
                    throw new NullPointerException("Must supply pending intent or shortcut to bubble");
                } else if (this.mShortcutId == null && this.mIcon == null) {
                    throw new NullPointerException("Must supply an icon or shortcut for the bubble");
                } else {
                    BubbleMetadata data = new BubbleMetadata(this.mPendingIntent, this.mDeleteIntent, this.mIcon, this.mDesiredHeight, this.mDesiredHeightResId, this.mFlags, this.mShortcutId);
                    data.setFlags(this.mFlags);
                    return data;
                }
            }

            private Builder setFlag(int mask, boolean value) {
                if (value) {
                    this.mFlags |= mask;
                } else {
                    this.mFlags &= ~mask;
                }
                return this;
            }
        }

        /* loaded from: classes.dex */
        public static class Api29Impl {
            private Api29Impl() {
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
                if (compatMetadata == null || compatMetadata.getIntent() == null) {
                    return null;
                }
                Notification.BubbleMetadata.Builder platformMetadataBuilder = new Notification.BubbleMetadata.Builder().setIcon(compatMetadata.getIcon().toIcon()).setIntent(compatMetadata.getIntent()).setDeleteIntent(compatMetadata.getDeleteIntent()).setAutoExpandBubble(compatMetadata.getAutoExpandBubble()).setSuppressNotification(compatMetadata.isNotificationSuppressed());
                if (compatMetadata.getDesiredHeight() != 0) {
                    platformMetadataBuilder.setDesiredHeight(compatMetadata.getDesiredHeight());
                }
                if (compatMetadata.getDesiredHeightResId() != 0) {
                    platformMetadataBuilder.setDesiredHeightResId(compatMetadata.getDesiredHeightResId());
                }
                return platformMetadataBuilder.build();
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
                if (platformMetadata == null || platformMetadata.getIntent() == null) {
                    return null;
                }
                Builder compatBuilder = new Builder(platformMetadata.getIntent(), IconCompat.createFromIcon(platformMetadata.getIcon())).setAutoExpandBubble(platformMetadata.getAutoExpandBubble()).setDeleteIntent(platformMetadata.getDeleteIntent()).setSuppressNotification(platformMetadata.isNotificationSuppressed());
                if (platformMetadata.getDesiredHeight() != 0) {
                    compatBuilder.setDesiredHeight(platformMetadata.getDesiredHeight());
                }
                if (platformMetadata.getDesiredHeightResId() != 0) {
                    compatBuilder.setDesiredHeightResId(platformMetadata.getDesiredHeightResId());
                }
                return compatBuilder.build();
            }
        }

        /* loaded from: classes.dex */
        public static class Api30Impl {
            private Api30Impl() {
            }

            static Notification.BubbleMetadata toPlatform(BubbleMetadata compatMetadata) {
                Notification.BubbleMetadata.Builder platformMetadataBuilder;
                if (compatMetadata == null) {
                    return null;
                }
                if (compatMetadata.getShortcutId() != null) {
                    platformMetadataBuilder = new Notification.BubbleMetadata.Builder(compatMetadata.getShortcutId());
                } else {
                    platformMetadataBuilder = new Notification.BubbleMetadata.Builder(compatMetadata.getIntent(), compatMetadata.getIcon().toIcon());
                }
                platformMetadataBuilder.setDeleteIntent(compatMetadata.getDeleteIntent()).setAutoExpandBubble(compatMetadata.getAutoExpandBubble()).setSuppressNotification(compatMetadata.isNotificationSuppressed());
                if (compatMetadata.getDesiredHeight() != 0) {
                    platformMetadataBuilder.setDesiredHeight(compatMetadata.getDesiredHeight());
                }
                if (compatMetadata.getDesiredHeightResId() != 0) {
                    platformMetadataBuilder.setDesiredHeightResId(compatMetadata.getDesiredHeightResId());
                }
                return platformMetadataBuilder.build();
            }

            static BubbleMetadata fromPlatform(Notification.BubbleMetadata platformMetadata) {
                Builder compatBuilder;
                if (platformMetadata == null) {
                    return null;
                }
                if (platformMetadata.getShortcutId() != null) {
                    compatBuilder = new Builder(platformMetadata.getShortcutId());
                } else {
                    compatBuilder = new Builder(platformMetadata.getIntent(), IconCompat.createFromIcon(platformMetadata.getIcon()));
                }
                compatBuilder.setAutoExpandBubble(platformMetadata.getAutoExpandBubble()).setDeleteIntent(platformMetadata.getDeleteIntent()).setSuppressNotification(platformMetadata.isNotificationSuppressed());
                if (platformMetadata.getDesiredHeight() != 0) {
                    compatBuilder.setDesiredHeight(platformMetadata.getDesiredHeight());
                }
                if (platformMetadata.getDesiredHeightResId() != 0) {
                    compatBuilder.setDesiredHeightResId(platformMetadata.getDesiredHeightResId());
                }
                return compatBuilder.build();
            }
        }
    }

    static Notification[] getNotificationArrayFromBundle(Bundle bundle, String key) {
        Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof Notification[]) || array == null) {
            return (Notification[]) array;
        }
        Notification[] typedArray = new Notification[array.length];
        for (int i = 0; i < array.length; i++) {
            typedArray[i] = (Notification) array[i];
        }
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }

    public static Bundle getExtras(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification);
        }
        return null;
    }

    public static int getActionCount(Notification notification) {
        if (Build.VERSION.SDK_INT >= 19) {
            if (notification.actions != null) {
                return notification.actions.length;
            }
            return 0;
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getActionCount(notification);
        } else {
            return 0;
        }
    }

    public static Action getAction(Notification notification, int actionIndex) {
        if (Build.VERSION.SDK_INT >= 20) {
            return getActionCompatFromAction(notification.actions[actionIndex]);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            Notification.Action action = notification.actions[actionIndex];
            Bundle actionExtras = null;
            SparseArray<Bundle> actionExtrasMap = notification.extras.getSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
            if (actionExtrasMap != null) {
                actionExtras = actionExtrasMap.get(actionIndex);
            }
            return NotificationCompatJellybean.readAction(action.icon, action.title, action.actionIntent, actionExtras);
        } else if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getAction(notification, actionIndex);
        } else {
            return null;
        }
    }

    public static BubbleMetadata getBubbleMetadata(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return BubbleMetadata.fromPlatform(notification.getBubbleMetadata());
        }
        return null;
    }

    static Action getActionCompatFromAction(Notification.Action action) {
        RemoteInput[] remoteInputs;
        boolean allowGeneratedReplies;
        int semanticAction;
        int i;
        RemoteInput[] srcArray = action.getRemoteInputs();
        boolean isContextual = false;
        if (srcArray == null) {
            remoteInputs = null;
        } else {
            remoteInputs = new RemoteInput[srcArray.length];
            for (int i2 = 0; i2 < srcArray.length; i2++) {
                RemoteInput src = srcArray[i2];
                String resultKey = src.getResultKey();
                CharSequence label = src.getLabel();
                CharSequence[] choices = src.getChoices();
                boolean allowFreeFormInput = src.getAllowFreeFormInput();
                if (Build.VERSION.SDK_INT >= 29) {
                    i = src.getEditChoicesBeforeSending();
                } else {
                    i = 0;
                }
                remoteInputs[i2] = new RemoteInput(resultKey, label, choices, allowFreeFormInput, i, src.getExtras(), null);
            }
        }
        if (Build.VERSION.SDK_INT >= 24) {
            allowGeneratedReplies = action.getExtras().getBoolean("android.support.allowGeneratedReplies") || action.getAllowGeneratedReplies();
        } else {
            allowGeneratedReplies = action.getExtras().getBoolean("android.support.allowGeneratedReplies");
        }
        boolean showsUserInterface = action.getExtras().getBoolean("android.support.action.showsUserInterface", true);
        if (Build.VERSION.SDK_INT >= 28) {
            semanticAction = action.getSemanticAction();
        } else {
            semanticAction = action.getExtras().getInt("android.support.action.semanticAction", 0);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            isContextual = action.isContextual();
        }
        if (Build.VERSION.SDK_INT < 23) {
            return new Action(action.icon, action.title, action.actionIntent, action.getExtras(), remoteInputs, (RemoteInput[]) null, allowGeneratedReplies, semanticAction, showsUserInterface, isContextual);
        }
        if (action.getIcon() == null && action.icon != 0) {
            return new Action(action.icon, action.title, action.actionIntent, action.getExtras(), remoteInputs, (RemoteInput[]) null, allowGeneratedReplies, semanticAction, showsUserInterface, isContextual);
        }
        return new Action(action.getIcon() == null ? null : IconCompat.createFromIconOrNullIfZeroResId(action.getIcon()), action.title, action.actionIntent, action.getExtras(), remoteInputs, (RemoteInput[]) null, allowGeneratedReplies, semanticAction, showsUserInterface, isContextual);
    }

    public static List<Action> getInvisibleActions(Notification notification) {
        ArrayList<Action> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 19) {
            Bundle carExtenderBundle = notification.extras.getBundle("android.car.EXTENSIONS");
            if (carExtenderBundle == null) {
                return result;
            }
            Bundle listBundle = carExtenderBundle.getBundle("invisible_actions");
            if (listBundle != null) {
                for (int i = 0; i < listBundle.size(); i++) {
                    result.add(NotificationCompatJellybean.getActionFromBundle(listBundle.getBundle(Integer.toString(i))));
                }
            }
        }
        return result;
    }

    public static List<Person> getPeople(Notification notification) {
        String[] peopleArray;
        ArrayList<Person> result = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 28) {
            ArrayList<android.app.Person> peopleList = notification.extras.getParcelableArrayList(EXTRA_PEOPLE_LIST);
            if (peopleList != null && !peopleList.isEmpty()) {
                Iterator<android.app.Person> it = peopleList.iterator();
                while (it.hasNext()) {
                    result.add(Person.fromAndroidPerson(it.next()));
                }
            }
        } else if (!(Build.VERSION.SDK_INT < 19 || (peopleArray = notification.extras.getStringArray(EXTRA_PEOPLE)) == null || peopleArray.length == 0)) {
            for (String personUri : peopleArray) {
                result.add(new Person.Builder().setUri(personUri).build());
            }
        }
        return result;
    }

    public static CharSequence getContentTitle(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_TITLE);
    }

    public static CharSequence getContentText(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_TEXT);
    }

    public static CharSequence getContentInfo(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_INFO_TEXT);
    }

    public static CharSequence getSubText(Notification notification) {
        return notification.extras.getCharSequence(EXTRA_SUB_TEXT);
    }

    public static String getCategory(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.category;
        }
        return null;
    }

    public static boolean getLocalOnly(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            if ((notification.flags & 256) != 0) {
                return true;
            }
            return false;
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
        } else {
            if (Build.VERSION.SDK_INT >= 16) {
                return NotificationCompatJellybean.getExtras(notification).getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
            }
            return false;
        }
    }

    public static String getGroup(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getGroup();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
        }
        return null;
    }

    public static boolean getShowWhen(Notification notification) {
        return notification.extras.getBoolean(EXTRA_SHOW_WHEN);
    }

    public static boolean getUsesChronometer(Notification notification) {
        return notification.extras.getBoolean(EXTRA_SHOW_CHRONOMETER);
    }

    public static boolean getOnlyAlertOnce(Notification notification) {
        return (notification.flags & 8) != 0;
    }

    public static boolean getAutoCancel(Notification notification) {
        return (notification.flags & 16) != 0;
    }

    public static boolean getOngoing(Notification notification) {
        return (notification.flags & 2) != 0;
    }

    public static int getColor(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.color;
        }
        return 0;
    }

    public static int getVisibility(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.visibility;
        }
        return 0;
    }

    public static Notification getPublicVersion(Notification notification) {
        if (Build.VERSION.SDK_INT >= 21) {
            return notification.publicVersion;
        }
        return null;
    }

    static boolean getHighPriority(Notification notification) {
        return (notification.flags & 128) != 0;
    }

    public static boolean isGroupSummary(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            if ((notification.flags & 512) != 0) {
                return true;
            }
            return false;
        } else if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
        } else {
            if (Build.VERSION.SDK_INT >= 16) {
                return NotificationCompatJellybean.getExtras(notification).getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
            }
            return false;
        }
    }

    public static String getSortKey(Notification notification) {
        if (Build.VERSION.SDK_INT >= 20) {
            return notification.getSortKey();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            return notification.extras.getString(NotificationCompatExtras.EXTRA_SORT_KEY);
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return NotificationCompatJellybean.getExtras(notification).getString(NotificationCompatExtras.EXTRA_SORT_KEY);
        }
        return null;
    }

    public static String getChannelId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getChannelId();
        }
        return null;
    }

    public static long getTimeoutAfter(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getTimeoutAfter();
        }
        return 0;
    }

    public static int getBadgeIconType(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getBadgeIconType();
        }
        return 0;
    }

    public static String getShortcutId(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getShortcutId();
        }
        return null;
    }

    public static CharSequence getSettingsText(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getSettingsText();
        }
        return null;
    }

    public static LocusIdCompat getLocusId(Notification notification) {
        LocusId locusId;
        if (Build.VERSION.SDK_INT < 29 || (locusId = notification.getLocusId()) == null) {
            return null;
        }
        return LocusIdCompat.toLocusIdCompat(locusId);
    }

    public static int getGroupAlertBehavior(Notification notification) {
        if (Build.VERSION.SDK_INT >= 26) {
            return notification.getGroupAlertBehavior();
        }
        return 0;
    }

    public static boolean getAllowSystemGeneratedContextualActions(Notification notification) {
        if (Build.VERSION.SDK_INT >= 29) {
            return notification.getAllowSystemGeneratedContextualActions();
        }
        return false;
    }
}
