package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zaj;
import java.util.ArrayList;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
public final class zaag extends zaap {
    final /* synthetic */ zaaf zaa;
    private final Map<Api.Client, zaah> zab;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public zaag(zaaf zaaf, Map<Api.Client, zaah> map) {
        super(zaaf, null);
        this.zaa = zaaf;
        this.zab = map;
    }

    @Override // com.google.android.gms.common.api.internal.zaap
    public final void zaa() {
        zaj zaj = new zaj(this.zaa.zad);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Api.Client client : this.zab.keySet()) {
            if (!client.requiresGooglePlayServices() || (this.zab.get(client).zac)) {
                arrayList2.add(client);
            } else {
                arrayList.add(client);
            }
        }
        int i = -1;
        int i2 = 0;
        if (arrayList.isEmpty()) {
            ArrayList arrayList3 = arrayList2;
            int size = arrayList3.size();
            while (i2 < size) {
                Object obj = arrayList3.get(i2);
                i2++;
                i = zaj.zaa(this.zaa.zac, (Api.Client) obj);
                if (i == 0) {
                    break;
                }
            }
        } else {
            ArrayList arrayList4 = arrayList;
            int size2 = arrayList4.size();
            while (i2 < size2) {
                Object obj2 = arrayList4.get(i2);
                i2++;
                i = zaj.zaa(this.zaa.zac, (Api.Client) obj2);
                if (i != 0) {
                    break;
                }
            }
        }
        if (i != 0) {
            this.zaa.zaa.zaa(new zaaj(this, this.zaa, new ConnectionResult(i, null)));
            return;
        }
        if ((this.zaa.zam) && this.zaa.zak != null) {
            this.zaa.zak.zab();
        }
        for (Api.Client client2 : this.zab.keySet()) {
            zaah zaah = this.zab.get(client2);
            if (!client2.requiresGooglePlayServices() || zaj.zaa(this.zaa.zac, client2) == 0) {
                client2.connect(zaah);
            } else {
                this.zaa.zaa.zaa(new zaai(this, this.zaa, zaah));
            }
        }
    }
}
