package org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import okhttp3.HttpUrl;
/* loaded from: classes3.dex */
public class JSONArray {
    private ArrayList myArrayList;

    public JSONArray() {
        this.myArrayList = new ArrayList();
    }

    public JSONArray(Object obj) throws JSONException {
        this();
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                put(JSONObject.wrap(Array.get(obj, i)));
            }
            return;
        }
        throw new JSONException("JSONArray initial value should be a string or collection or array.");
    }

    public JSONArray(String str) throws JSONException {
        this(new JSONTokener(str));
    }

    public JSONArray(Collection collection) {
        this.myArrayList = new ArrayList();
        if (collection != null) {
            for (Object obj : collection) {
                this.myArrayList.add(JSONObject.wrap(obj));
            }
        }
    }

    public JSONArray(JSONTokener jSONTokener) throws JSONException {
        this();
        Object obj;
        ArrayList arrayList;
        if (jSONTokener.nextClean() != '[') {
            throw jSONTokener.syntaxError("A JSONArray text must start with '['");
        } else if (jSONTokener.nextClean() != ']') {
            do {
                jSONTokener.back();
                char nextClean = jSONTokener.nextClean();
                jSONTokener.back();
                if (nextClean == ',') {
                    arrayList = this.myArrayList;
                    obj = JSONObject.NULL;
                } else {
                    arrayList = this.myArrayList;
                    obj = jSONTokener.nextValue();
                }
                arrayList.add(obj);
                char nextClean2 = jSONTokener.nextClean();
                if (nextClean2 != ',' && nextClean2 != ';') {
                    if (nextClean2 != ']') {
                        throw jSONTokener.syntaxError("Expected a ',' or ']'");
                    }
                    return;
                }
            } while (jSONTokener.nextClean() != ']');
        }
    }

    public Object get(int i) throws JSONException {
        Object opt = opt(i);
        if (opt != null) {
            return opt;
        }
        throw new JSONException("JSONArray[" + i + "] not found.");
    }

    public boolean getBoolean(int i) throws JSONException {
        Object obj = get(i);
        if (obj.equals(Boolean.FALSE)) {
            return false;
        }
        boolean z = obj instanceof String;
        if (z && ((String) obj).equalsIgnoreCase("false")) {
            return false;
        }
        if (obj.equals(Boolean.TRUE)) {
            return true;
        }
        if (z && ((String) obj).equalsIgnoreCase("true")) {
            return true;
        }
        throw new JSONException("JSONArray[" + i + "] is not a boolean.");
    }

    public double getDouble(int i) throws JSONException {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }

    public int getInt(int i) throws JSONException {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.parseInt((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }

    public JSONArray getJSONArray(int i) throws JSONException {
        Object obj = get(i);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw new JSONException("JSONArray[" + i + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(int i) throws JSONException {
        Object obj = get(i);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new JSONException("JSONArray[" + i + "] is not a JSONObject.");
    }

    public long getLong(int i) throws JSONException {
        Object obj = get(i);
        try {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.parseLong((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONArray[" + i + "] is not a number.");
        }
    }

    public String getString(int i) throws JSONException {
        Object obj = get(i);
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new JSONException("JSONArray[" + i + "] not a string.");
    }

    public boolean isNull(int i) {
        return JSONObject.NULL.equals(opt(i));
    }

    public String join(String str) throws JSONException {
        int length = length();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                stringBuffer.append(str);
            }
            stringBuffer.append(JSONObject.valueToString(this.myArrayList.get(i)));
        }
        return stringBuffer.toString();
    }

    public int length() {
        return this.myArrayList.size();
    }

    public Object opt(int i) {
        if (i < 0 || i >= length()) {
            return null;
        }
        return this.myArrayList.get(i);
    }

    public boolean optBoolean(int i) {
        return optBoolean(i, false);
    }

    public boolean optBoolean(int i, boolean z) {
        try {
            return getBoolean(i);
        } catch (Exception e) {
            return z;
        }
    }

    public double optDouble(int i) {
        return optDouble(i, Double.NaN);
    }

    public double optDouble(int i, double d) {
        try {
            return getDouble(i);
        } catch (Exception e) {
            return d;
        }
    }

    public int optInt(int i) {
        return optInt(i, 0);
    }

    public int optInt(int i, int i2) {
        try {
            return getInt(i);
        } catch (Exception e) {
            return i2;
        }
    }

    public JSONArray optJSONArray(int i) {
        Object opt = opt(i);
        if (opt instanceof JSONArray) {
            return (JSONArray) opt;
        }
        return null;
    }

    public JSONObject optJSONObject(int i) {
        Object opt = opt(i);
        if (opt instanceof JSONObject) {
            return (JSONObject) opt;
        }
        return null;
    }

    public long optLong(int i) {
        return optLong(i, 0);
    }

    public long optLong(int i, long j) {
        try {
            return getLong(i);
        } catch (Exception e) {
            return j;
        }
    }

    public String optString(int i) {
        return optString(i, "");
    }

    public String optString(int i, String str) {
        Object opt = opt(i);
        return opt != null ? opt.toString() : str;
    }

    public JSONArray put(double d) throws JSONException {
        Double d2 = new Double(d);
        JSONObject.testValidity(d2);
        put(d2);
        return this;
    }

    public JSONArray put(int i) {
        put(new Integer(i));
        return this;
    }

    public JSONArray put(int i, double d) throws JSONException {
        put(i, new Double(d));
        return this;
    }

    public JSONArray put(int i, int i2) throws JSONException {
        put(i, new Integer(i2));
        return this;
    }

    public JSONArray put(int i, long j) throws JSONException {
        put(i, new Long(j));
        return this;
    }

    public JSONArray put(int i, Object obj) throws JSONException {
        JSONObject.testValidity(obj);
        if (i >= 0) {
            if (i < length()) {
                this.myArrayList.set(i, obj);
            } else {
                while (i != length()) {
                    put(JSONObject.NULL);
                }
                put(obj);
            }
            return this;
        }
        throw new JSONException("JSONArray[" + i + "] not found.");
    }

    public JSONArray put(int i, Collection collection) throws JSONException {
        put(i, new JSONArray(collection));
        return this;
    }

    public JSONArray put(int i, Map map) throws JSONException {
        put(i, new JSONObject(map));
        return this;
    }

    public JSONArray put(int i, boolean z) throws JSONException {
        put(i, z ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONArray put(long j) {
        put(new Long(j));
        return this;
    }

    public JSONArray put(Object obj) {
        this.myArrayList.add(obj);
        return this;
    }

    public JSONArray put(Collection collection) {
        put(new JSONArray(collection));
        return this;
    }

    public JSONArray put(Map map) {
        put(new JSONObject(map));
        return this;
    }

    public JSONArray put(boolean z) {
        put(z ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public Object remove(int i) {
        Object opt = opt(i);
        this.myArrayList.remove(i);
        return opt;
    }

    public JSONObject toJSONObject(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null || jSONArray.length() == 0 || length() == 0) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        for (int i = 0; i < jSONArray.length(); i++) {
            jSONObject.put(jSONArray.getString(i), opt(i));
        }
        return jSONObject;
    }

    public String toString() {
        try {
            return '[' + join(",") + ']';
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int i) throws JSONException {
        return toString(i, 0);
    }

    public String toString(int i, int i2) throws JSONException {
        int length = length();
        if (length == 0) {
            return HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }
        StringBuffer stringBuffer = new StringBuffer("[");
        if (length == 1) {
            stringBuffer.append(JSONObject.valueToString(this.myArrayList.get(0), i, i2));
        } else {
            int i3 = i2 + i;
            stringBuffer.append('\n');
            for (int i4 = 0; i4 < length; i4++) {
                if (i4 > 0) {
                    stringBuffer.append(",\n");
                }
                for (int i5 = 0; i5 < i3; i5++) {
                    stringBuffer.append(' ');
                }
                stringBuffer.append(JSONObject.valueToString(this.myArrayList.get(i4), i, i3));
            }
            stringBuffer.append('\n');
            for (int i6 = 0; i6 < i2; i6++) {
                stringBuffer.append(' ');
            }
        }
        stringBuffer.append(']');
        return stringBuffer.toString();
    }

    public Writer write(Writer writer) throws JSONException {
        try {
            int length = length();
            writer.write(91);
            int i = 0;
            boolean z = false;
            while (i < length) {
                if (z) {
                    writer.write(44);
                }
                Object obj = this.myArrayList.get(i);
                if (obj instanceof JSONObject) {
                    ((JSONObject) obj).write(writer);
                } else if (obj instanceof JSONArray) {
                    ((JSONArray) obj).write(writer);
                } else {
                    writer.write(JSONObject.valueToString(obj));
                }
                i++;
                z = true;
            }
            writer.write(93);
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}
