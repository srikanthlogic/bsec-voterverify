package com.google.firebase.installations.local;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.stats.CodePackage;
import com.google.common.base.Ascii;
import com.google.firebase.FirebaseApp;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class IidStore {
    private static final String[] ALLOWABLE_SCOPES = {"*", "FCM", CodePackage.GCM, ""};
    private static final String IID_SHARED_PREFS_NAME = "com.google.android.gms.appid";
    private static final String JSON_ENCODED_PREFIX = "{";
    private static final String JSON_TOKEN_KEY = "token";
    private static final String STORE_KEY_ID = "|S|id";
    private static final String STORE_KEY_PUB = "|S||P|";
    private static final String STORE_KEY_SEPARATOR = "|";
    private static final String STORE_KEY_TOKEN = "|T|";
    private final String defaultSenderId;
    private final SharedPreferences iidPrefs;

    public IidStore(FirebaseApp firebaseApp) {
        this.iidPrefs = firebaseApp.getApplicationContext().getSharedPreferences(IID_SHARED_PREFS_NAME, 0);
        this.defaultSenderId = getDefaultSenderId(firebaseApp);
    }

    public IidStore(SharedPreferences iidPrefs, String defaultSenderId) {
        this.iidPrefs = iidPrefs;
        this.defaultSenderId = defaultSenderId;
    }

    private static String getDefaultSenderId(FirebaseApp app) {
        String senderId = app.getOptions().getGcmSenderId();
        if (senderId != null) {
            return senderId;
        }
        String appId = app.getOptions().getApplicationId();
        if (!appId.startsWith("1:") && !appId.startsWith("2:")) {
            return appId;
        }
        String[] parts = appId.split(":");
        if (parts.length != 4) {
            return null;
        }
        String projectNumber = parts[1];
        if (projectNumber.isEmpty()) {
            return null;
        }
        return projectNumber;
    }

    private String createTokenKey(String senderId, String scope) {
        return STORE_KEY_TOKEN + senderId + STORE_KEY_SEPARATOR + scope;
    }

    public String readToken() {
        synchronized (this.iidPrefs) {
            for (String scope : ALLOWABLE_SCOPES) {
                String token = this.iidPrefs.getString(createTokenKey(this.defaultSenderId, scope), null);
                if (token != null && !token.isEmpty()) {
                    return token.startsWith(JSON_ENCODED_PREFIX) ? parseIidTokenFromJson(token) : token;
                }
            }
            return null;
        }
    }

    private String parseIidTokenFromJson(String token) {
        try {
            return new JSONObject(token).getString(JSON_TOKEN_KEY);
        } catch (JSONException e) {
            return null;
        }
    }

    public String readIid() {
        synchronized (this.iidPrefs) {
            String id = readInstanceIdFromLocalStorage();
            if (id != null) {
                return id;
            }
            return readPublicKeyFromLocalStorageAndCalculateInstanceId();
        }
    }

    private String readInstanceIdFromLocalStorage() {
        String string;
        synchronized (this.iidPrefs) {
            string = this.iidPrefs.getString(STORE_KEY_ID, null);
        }
        return string;
    }

    private String readPublicKeyFromLocalStorageAndCalculateInstanceId() {
        synchronized (this.iidPrefs) {
            String base64PublicKey = this.iidPrefs.getString(STORE_KEY_PUB, null);
            if (base64PublicKey == null) {
                return null;
            }
            PublicKey publicKey = parseKey(base64PublicKey);
            if (publicKey == null) {
                return null;
            }
            return getIdFromPublicKey(publicKey);
        }
    }

    private static String getIdFromPublicKey(PublicKey publicKey) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(publicKey.getEncoded());
            digest[0] = (byte) (((digest[0] & Ascii.SI) + 112) & 255);
            return Base64.encodeToString(digest, 0, 8, 11);
        } catch (NoSuchAlgorithmException e) {
            Log.w("ContentValues", "Unexpected error, device missing required algorithms");
            return null;
        }
    }

    private PublicKey parseKey(String base64PublicKey) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(base64PublicKey, 8)));
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            Log.w("ContentValues", "Invalid key stored " + e);
            return null;
        }
    }
}
