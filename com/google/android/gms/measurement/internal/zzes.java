package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzes {
    final /* synthetic */ zzex zza;
    private final String zzb = "default_event_parameters";
    private final Bundle zzc = new Bundle();
    private Bundle zzd;

    public zzes(zzex zzex, String str, Bundle bundle) {
        this.zza = zzex;
        Preconditions.checkNotEmpty("default_event_parameters");
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x009f A[Catch: JSONException -> 0x00aa, NumberFormatException | JSONException -> 0x00a8, TRY_LEAVE, TryCatch #3 {NumberFormatException | JSONException -> 0x00a8, blocks: (B:9:0x0027, B:30:0x0073, B:31:0x0085, B:32:0x0092, B:33:0x009f), top: B:47:0x0027 }] */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final Bundle zza() {
        JSONObject jSONObject;
        String string;
        String string2;
        int hashCode;
        char c;
        if (this.zzd == null) {
            String string3 = this.zza.zza().getString(this.zzb, null);
            if (string3 != null) {
                try {
                    Bundle bundle = new Bundle();
                    JSONArray jSONArray = new JSONArray(string3);
                    for (int i = 0; i < jSONArray.length(); i++) {
                        try {
                            jSONObject = jSONArray.getJSONObject(i);
                            string = jSONObject.getString("n");
                            string2 = jSONObject.getString("t");
                            hashCode = string2.hashCode();
                        } catch (NumberFormatException | JSONException e) {
                            this.zza.zzs.zzay().zzd().zza("Error reading value from SharedPreferences. Value dropped");
                        }
                        if (hashCode == 100) {
                            if (string2.equals("d")) {
                                c = 1;
                                if (c != 0) {
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        } else if (hashCode != 108) {
                            if (hashCode == 115 && string2.equals("s")) {
                                c = 0;
                                if (c != 0) {
                                    bundle.putString(string, jSONObject.getString("v"));
                                } else if (c == 1) {
                                    bundle.putDouble(string, Double.parseDouble(jSONObject.getString("v")));
                                } else if (c != 2) {
                                    this.zza.zzs.zzay().zzd().zzb("Unrecognized persisted bundle type. Type", string2);
                                } else {
                                    bundle.putLong(string, Long.parseLong(jSONObject.getString("v")));
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        } else {
                            if (string2.equals("l")) {
                                c = 2;
                                if (c != 0) {
                                }
                            }
                            c = 65535;
                            if (c != 0) {
                            }
                        }
                    }
                    this.zzd = bundle;
                } catch (JSONException e2) {
                    this.zza.zzs.zzay().zzd().zza("Error loading bundle from SharedPreferences. Values will be lost");
                }
            }
            if (this.zzd == null) {
                this.zzd = this.zzc;
            }
        }
        return this.zzd;
    }

    public final void zzb(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        SharedPreferences.Editor edit = this.zza.zza().edit();
        if (bundle.size() == 0) {
            edit.remove(this.zzb);
        } else {
            String str = this.zzb;
            JSONArray jSONArray = new JSONArray();
            for (String str2 : bundle.keySet()) {
                Object obj = bundle.get(str2);
                if (obj != null) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("n", str2);
                        jSONObject.put("v", String.valueOf(obj));
                        if (obj instanceof String) {
                            jSONObject.put("t", "s");
                        } else if (obj instanceof Long) {
                            jSONObject.put("t", "l");
                        } else if (obj instanceof Double) {
                            jSONObject.put("t", "d");
                        } else {
                            this.zza.zzs.zzay().zzd().zzb("Cannot serialize bundle value to SharedPreferences. Type", obj.getClass());
                        }
                        jSONArray.put(jSONObject);
                    } catch (JSONException e) {
                        this.zza.zzs.zzay().zzd().zzb("Cannot serialize bundle value to SharedPreferences", e);
                    }
                }
            }
            edit.putString(str, jSONArray.toString());
        }
        edit.apply();
        this.zzd = bundle;
    }
}
