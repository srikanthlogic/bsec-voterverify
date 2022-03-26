package org.json;

import kotlin.text.Typography;
/* loaded from: classes3.dex */
public class CDL {
    /* JADX WARN: Code restructure failed: missing block: B:28:0x005d, code lost:
        throw r4.syntaxError("Missing close quote '" + r0 + "'.");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static String getValue(JSONTokener jSONTokener) throws JSONException {
        char next;
        while (true) {
            next = jSONTokener.next();
            if (next != ' ' && next != '\t') {
                break;
            }
        }
        if (next == 0) {
            return null;
        }
        if (next == '\"' || next == '\'') {
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                char next2 = jSONTokener.next();
                if (next2 == next) {
                    return stringBuffer.toString();
                }
                if (next2 == 0 || next2 == '\n' || next2 == '\r') {
                    break;
                }
                stringBuffer.append(next2);
            }
        } else {
            jSONTokener.back();
            return next != ',' ? jSONTokener.nextTo(',') : "";
        }
    }

    public static JSONArray rowToJSONArray(JSONTokener jSONTokener) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        while (true) {
            String value = getValue(jSONTokener);
            char next = jSONTokener.next();
            if (value == null) {
                return null;
            }
            if (jSONArray.length() == 0 && value.length() == 0 && next != ',') {
                return null;
            }
            jSONArray.put(value);
            while (next != ',') {
                if (next == ' ') {
                    next = jSONTokener.next();
                } else if (next == '\n' || next == '\r' || next == 0) {
                    return jSONArray;
                } else {
                    throw jSONTokener.syntaxError("Bad character '" + next + "' (" + ((int) next) + ").");
                }
            }
        }
    }

    public static JSONObject rowToJSONObject(JSONArray jSONArray, JSONTokener jSONTokener) throws JSONException {
        JSONArray rowToJSONArray = rowToJSONArray(jSONTokener);
        if (rowToJSONArray != null) {
            return rowToJSONArray.toJSONObject(jSONArray);
        }
        return null;
    }

    public static String rowToString(JSONArray jSONArray) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < jSONArray.length(); i++) {
            if (i > 0) {
                stringBuffer.append(',');
            }
            Object opt = jSONArray.opt(i);
            if (opt != null) {
                String obj = opt.toString();
                if (obj.length() <= 0 || (obj.indexOf(44) < 0 && obj.indexOf(10) < 0 && obj.indexOf(13) < 0 && obj.indexOf(0) < 0 && obj.charAt(0) != '\"')) {
                    stringBuffer.append(obj);
                } else {
                    stringBuffer.append(Typography.quote);
                    int length = obj.length();
                    for (int i2 = 0; i2 < length; i2++) {
                        char charAt = obj.charAt(i2);
                        if (charAt >= ' ' && charAt != '\"') {
                            stringBuffer.append(charAt);
                        }
                    }
                    stringBuffer.append(Typography.quote);
                }
            }
        }
        stringBuffer.append('\n');
        return stringBuffer.toString();
    }

    public static JSONArray toJSONArray(String str) throws JSONException {
        return toJSONArray(new JSONTokener(str));
    }

    public static JSONArray toJSONArray(JSONArray jSONArray, String str) throws JSONException {
        return toJSONArray(jSONArray, new JSONTokener(str));
    }

    public static JSONArray toJSONArray(JSONArray jSONArray, JSONTokener jSONTokener) throws JSONException {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        JSONArray jSONArray2 = new JSONArray();
        while (true) {
            JSONObject rowToJSONObject = rowToJSONObject(jSONArray, jSONTokener);
            if (rowToJSONObject == null) {
                break;
            }
            jSONArray2.put(rowToJSONObject);
        }
        if (jSONArray2.length() == 0) {
            return null;
        }
        return jSONArray2;
    }

    public static JSONArray toJSONArray(JSONTokener jSONTokener) throws JSONException {
        return toJSONArray(rowToJSONArray(jSONTokener), jSONTokener);
    }

    public static String toString(JSONArray jSONArray) throws JSONException {
        JSONArray names;
        JSONObject optJSONObject = jSONArray.optJSONObject(0);
        if (optJSONObject == null || (names = optJSONObject.names()) == null) {
            return null;
        }
        return rowToString(names) + toString(names, jSONArray);
    }

    public static String toString(JSONArray jSONArray, JSONArray jSONArray2) throws JSONException {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < jSONArray2.length(); i++) {
            JSONObject optJSONObject = jSONArray2.optJSONObject(i);
            if (optJSONObject != null) {
                stringBuffer.append(rowToString(optJSONObject.toJSONArray(jSONArray)));
            }
        }
        return stringBuffer.toString();
    }
}
