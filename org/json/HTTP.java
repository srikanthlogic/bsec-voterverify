package org.json;

import java.util.Iterator;
import kotlin.text.Typography;
/* loaded from: classes3.dex */
public class HTTP {
    public static final String CRLF = "\r\n";

    /* JADX WARN: Removed duplicated region for block: B:10:0x005f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0051  */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static JSONObject toJSONObject(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        HTTPTokener hTTPTokener = new HTTPTokener(str);
        String nextToken = hTTPTokener.nextToken();
        if (nextToken.toUpperCase().startsWith("HTTP")) {
            jSONObject.put("HTTP-Version", nextToken);
            jSONObject.put("Status-Code", hTTPTokener.nextToken());
            String nextTo = hTTPTokener.nextTo((char) 0);
            String nextTo2 = "Reason-Phrase";
            jSONObject.put(nextTo2, nextTo);
            hTTPTokener.next();
            if (hTTPTokener.more()) {
                nextTo2 = hTTPTokener.nextTo(':');
                hTTPTokener.next(':');
                nextTo = hTTPTokener.nextTo((char) 0);
                jSONObject.put(nextTo2, nextTo);
                hTTPTokener.next();
                if (hTTPTokener.more()) {
                    return jSONObject;
                }
            }
        } else {
            jSONObject.put("Method", nextToken);
            jSONObject.put("Request-URI", hTTPTokener.nextToken());
            jSONObject.put("HTTP-Version", hTTPTokener.nextToken());
            if (hTTPTokener.more()) {
            }
        }
    }

    public static String toString(JSONObject jSONObject) throws JSONException {
        String string;
        String obj;
        Iterator keys = jSONObject.keys();
        StringBuffer stringBuffer = new StringBuffer();
        if (jSONObject.has("Status-Code") && jSONObject.has("Reason-Phrase")) {
            stringBuffer.append(jSONObject.getString("HTTP-Version"));
            stringBuffer.append(' ');
            stringBuffer.append(jSONObject.getString("Status-Code"));
            stringBuffer.append(' ');
            string = jSONObject.getString("Reason-Phrase");
        } else if (!jSONObject.has("Method") || !jSONObject.has("Request-URI")) {
            throw new JSONException("Not enough material for an HTTP header.");
        } else {
            stringBuffer.append(jSONObject.getString("Method"));
            stringBuffer.append(' ');
            stringBuffer.append(Typography.quote);
            stringBuffer.append(jSONObject.getString("Request-URI"));
            stringBuffer.append(Typography.quote);
            stringBuffer.append(' ');
            string = jSONObject.getString("HTTP-Version");
        }
        stringBuffer.append(string);
        while (true) {
            stringBuffer.append("\r\n");
            while (keys.hasNext()) {
                obj = keys.next().toString();
                if (obj.equals("HTTP-Version") || obj.equals("Status-Code") || obj.equals("Reason-Phrase") || obj.equals("Method") || obj.equals("Request-URI") || jSONObject.isNull(obj)) {
                }
            }
            stringBuffer.append("\r\n");
            return stringBuffer.toString();
            stringBuffer.append(obj);
            stringBuffer.append(": ");
            stringBuffer.append(jSONObject.getString(obj));
        }
    }
}
