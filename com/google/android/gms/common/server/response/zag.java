package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
/* compiled from: com.google.android.gms:play-services-base@@17.5.0 */
/* loaded from: classes.dex */
final class zag implements FastParser.zaa<BigDecimal> {
    @Override // com.google.android.gms.common.server.response.FastParser.zaa
    public final /* synthetic */ BigDecimal zaa(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        return fastParser.zai(bufferedReader);
    }
}
