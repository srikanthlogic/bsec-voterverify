package com.google.firebase.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.measurement.zzee;
import com.google.android.gms.measurement.internal.zzhw;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.installations.FirebaseInstallations;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
/* loaded from: classes3.dex */
public final class FirebaseAnalytics {
    private static volatile FirebaseAnalytics zza;
    private final zzee zzb;
    private ExecutorService zzc;

    /* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
    /* loaded from: classes3.dex */
    public enum ConsentStatus {
        GRANTED,
        DENIED
    }

    /* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
    /* loaded from: classes3.dex */
    public enum ConsentType {
        AD_STORAGE,
        ANALYTICS_STORAGE
    }

    /* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
    /* loaded from: classes3.dex */
    public static class Event {
        public static final String ADD_PAYMENT_INFO;
        public static final String ADD_SHIPPING_INFO;
        public static final String ADD_TO_CART;
        public static final String ADD_TO_WISHLIST;
        public static final String AD_IMPRESSION;
        public static final String APP_OPEN;
        public static final String BEGIN_CHECKOUT;
        public static final String CAMPAIGN_DETAILS;
        @Deprecated
        public static final String CHECKOUT_PROGRESS;
        public static final String EARN_VIRTUAL_CURRENCY;
        @Deprecated
        public static final String ECOMMERCE_PURCHASE;
        public static final String GENERATE_LEAD;
        public static final String JOIN_GROUP;
        public static final String LEVEL_END;
        public static final String LEVEL_START;
        public static final String LEVEL_UP;
        public static final String LOGIN;
        public static final String POST_SCORE;
        @Deprecated
        public static final String PRESENT_OFFER;
        public static final String PURCHASE;
        @Deprecated
        public static final String PURCHASE_REFUND;
        public static final String REFUND;
        public static final String REMOVE_FROM_CART;
        public static final String SCREEN_VIEW;
        public static final String SEARCH;
        public static final String SELECT_CONTENT;
        public static final String SELECT_ITEM;
        public static final String SELECT_PROMOTION;
        @Deprecated
        public static final String SET_CHECKOUT_OPTION;
        public static final String SHARE;
        public static final String SIGN_UP;
        public static final String SPEND_VIRTUAL_CURRENCY;
        public static final String TUTORIAL_BEGIN;
        public static final String TUTORIAL_COMPLETE;
        public static final String UNLOCK_ACHIEVEMENT;
        public static final String VIEW_CART;
        public static final String VIEW_ITEM;
        public static final String VIEW_ITEM_LIST;
        public static final String VIEW_PROMOTION;
        public static final String VIEW_SEARCH_RESULTS;

        protected Event() {
        }
    }

    /* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
    /* loaded from: classes3.dex */
    public static class Param {
        public static final String ACHIEVEMENT_ID;
        public static final String ACLID;
        public static final String AD_FORMAT;
        public static final String AD_PLATFORM;
        public static final String AD_SOURCE;
        public static final String AD_UNIT_NAME;
        public static final String AFFILIATION;
        public static final String CAMPAIGN;
        public static final String CHARACTER;
        @Deprecated
        public static final String CHECKOUT_OPTION;
        @Deprecated
        public static final String CHECKOUT_STEP;
        public static final String CONTENT;
        public static final String CONTENT_TYPE;
        public static final String COUPON;
        public static final String CP1;
        public static final String CREATIVE_NAME;
        public static final String CREATIVE_SLOT;
        public static final String CURRENCY;
        public static final String DESTINATION;
        public static final String DISCOUNT;
        public static final String END_DATE;
        public static final String EXTEND_SESSION;
        public static final String FLIGHT_NUMBER;
        public static final String GROUP_ID;
        public static final String INDEX;
        public static final String ITEMS;
        public static final String ITEM_BRAND;
        public static final String ITEM_CATEGORY;
        public static final String ITEM_CATEGORY2;
        public static final String ITEM_CATEGORY3;
        public static final String ITEM_CATEGORY4;
        public static final String ITEM_CATEGORY5;
        public static final String ITEM_ID;
        @Deprecated
        public static final String ITEM_LIST;
        public static final String ITEM_LIST_ID;
        public static final String ITEM_LIST_NAME;
        @Deprecated
        public static final String ITEM_LOCATION_ID;
        public static final String ITEM_NAME;
        public static final String ITEM_VARIANT;
        public static final String LEVEL;
        public static final String LEVEL_NAME;
        public static final String LOCATION;
        public static final String LOCATION_ID;
        public static final String MEDIUM;
        public static final String METHOD;
        public static final String NUMBER_OF_NIGHTS;
        public static final String NUMBER_OF_PASSENGERS;
        public static final String NUMBER_OF_ROOMS;
        public static final String ORIGIN;
        public static final String PAYMENT_TYPE;
        public static final String PRICE;
        public static final String PROMOTION_ID;
        public static final String PROMOTION_NAME;
        public static final String QUANTITY;
        public static final String SCORE;
        public static final String SCREEN_CLASS;
        public static final String SCREEN_NAME;
        public static final String SEARCH_TERM;
        public static final String SHIPPING;
        public static final String SHIPPING_TIER;
        @Deprecated
        public static final String SIGN_UP_METHOD;
        public static final String SOURCE;
        public static final String START_DATE;
        public static final String SUCCESS;
        public static final String TAX;
        public static final String TERM;
        public static final String TRANSACTION_ID;
        public static final String TRAVEL_CLASS;
        public static final String VALUE;
        public static final String VIRTUAL_CURRENCY_NAME;

        protected Param() {
        }
    }

    /* compiled from: com.google.android.gms:play-services-measurement-api@@19.0.1 */
    /* loaded from: classes3.dex */
    public static class UserProperty {
        public static final String ALLOW_AD_PERSONALIZATION_SIGNALS;
        public static final String SIGN_UP_METHOD;

        protected UserProperty() {
        }
    }

    public FirebaseAnalytics(zzee zzee) {
        Preconditions.checkNotNull(zzee);
        this.zzb = zzee;
    }

    public static FirebaseAnalytics getInstance(Context context) {
        if (zza == null) {
            synchronized (FirebaseAnalytics.class) {
                if (zza == null) {
                    zza = new FirebaseAnalytics(zzee.zzg(context, null, null, null, null));
                }
            }
        }
        return zza;
    }

    public static zzhw getScionFrontendApiImplementation(Context context, Bundle extraParams) {
        zzee zzg = zzee.zzg(context, null, null, null, extraParams);
        if (zzg == null) {
            return null;
        }
        return new zzc(zzg);
    }

    public Task<String> getAppInstanceId() {
        ExecutorService executorService;
        try {
            synchronized (FirebaseAnalytics.class) {
                if (this.zzc == null) {
                    this.zzc = new zza(this, 0, 1, 30, TimeUnit.SECONDS, new ArrayBlockingQueue(100));
                }
                executorService = this.zzc;
            }
            return Tasks.call(executorService, new zzb(this));
        } catch (RuntimeException e) {
            this.zzb.zzA(5, "Failed to schedule task for getAppInstanceId", null, null, null);
            return Tasks.forException(e);
        }
    }

    public String getFirebaseInstanceId() {
        try {
            return (String) Tasks.await(FirebaseInstallations.getInstance().getId(), 30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e2) {
            throw new IllegalStateException(e2.getCause());
        } catch (TimeoutException e3) {
            throw new IllegalThreadStateException("Firebase Installations getId Task has timed out.");
        }
    }

    public void logEvent(String name, Bundle params) {
        this.zzb.zzx(name, params);
    }

    public void resetAnalyticsData() {
        this.zzb.zzC();
    }

    public void setAnalyticsCollectionEnabled(boolean enabled) {
        this.zzb.zzK(Boolean.valueOf(enabled));
    }

    public void setConsent(Map<ConsentType, ConsentStatus> map) {
        Bundle bundle = new Bundle();
        ConsentStatus consentStatus = map.get(ConsentType.AD_STORAGE);
        if (consentStatus != null) {
            int ordinal = consentStatus.ordinal();
            if (ordinal == 0) {
                bundle.putString("ad_storage", "granted");
            } else if (ordinal == 1) {
                bundle.putString("ad_storage", "denied");
            }
        }
        ConsentStatus consentStatus2 = map.get(ConsentType.ANALYTICS_STORAGE);
        if (consentStatus2 != null) {
            int ordinal2 = consentStatus2.ordinal();
            if (ordinal2 == 0) {
                bundle.putString("analytics_storage", "granted");
            } else if (ordinal2 == 1) {
                bundle.putString("analytics_storage", "denied");
            }
        }
        this.zzb.zzF(bundle);
    }

    @Deprecated
    public void setCurrentScreen(Activity activity, String screenName, String screenClassOverride) {
        this.zzb.zzG(activity, screenName, screenClassOverride);
    }

    public void setDefaultEventParameters(Bundle parameters) {
        this.zzb.zzI(parameters);
    }

    public void setSessionTimeoutDuration(long milliseconds) {
        this.zzb.zzL(milliseconds);
    }

    public void setUserId(String id) {
        this.zzb.zzM(id);
    }

    public void setUserProperty(String name, String value) {
        this.zzb.zzN(null, name, value, false);
    }
}
