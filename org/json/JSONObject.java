package org.json;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public class JSONObject {
    public static final Object NULL = new Null();
    private Map map;

    /* loaded from: classes3.dex */
    private static final class Null {
        private Null() {
        }

        protected final Object clone() {
            return this;
        }

        public boolean equals(Object obj) {
            return obj == null || obj == this;
        }

        public String toString() {
            return "null";
        }
    }

    public JSONObject() {
        this.map = new HashMap();
    }

    public JSONObject(Object obj) {
        this();
        populateMap(obj);
    }

    public JSONObject(Object obj, String[] strArr) {
        this();
        Class<?> cls = obj.getClass();
        for (String str : strArr) {
            try {
                putOpt(str, cls.getField(str).get(obj));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(String str) throws JSONException {
        this(new JSONTokener(str));
    }

    public JSONObject(String str, Locale locale) throws JSONException {
        this();
        ResourceBundle bundle = ResourceBundle.getBundle(str, locale, Thread.currentThread().getContextClassLoader());
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String nextElement = keys.nextElement();
            if (nextElement instanceof String) {
                String str2 = nextElement;
                String[] split = str2.split("\\.");
                int length = split.length - 1;
                JSONObject jSONObject = this;
                for (int i = 0; i < length; i++) {
                    String str3 = split[i];
                    jSONObject = jSONObject.optJSONObject(str3);
                    if (jSONObject == null) {
                        jSONObject = new JSONObject();
                        jSONObject.put(str3, jSONObject);
                    }
                }
                jSONObject.put(split[length], bundle.getString(str2));
            }
        }
    }

    public JSONObject(Map map) {
        this.map = new HashMap();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    this.map.put(entry.getKey(), wrap(value));
                }
            }
        }
    }

    public JSONObject(JSONObject jSONObject, String[] strArr) {
        this();
        for (int i = 0; i < strArr.length; i++) {
            try {
                putOnce(strArr[i], jSONObject.opt(strArr[i]));
            } catch (Exception e) {
            }
        }
    }

    public JSONObject(JSONTokener jSONTokener) throws JSONException {
        this();
        if (jSONTokener.nextClean() == '{') {
            while (true) {
                char nextClean = jSONTokener.nextClean();
                if (nextClean == 0) {
                    throw jSONTokener.syntaxError("A JSONObject text must end with '}'");
                } else if (nextClean != '}') {
                    jSONTokener.back();
                    String obj = jSONTokener.nextValue().toString();
                    char nextClean2 = jSONTokener.nextClean();
                    if (nextClean2 == '=') {
                        if (jSONTokener.next() != '>') {
                            jSONTokener.back();
                        }
                    } else if (nextClean2 != ':') {
                        throw jSONTokener.syntaxError("Expected a ':' after a key");
                    }
                    putOnce(obj, jSONTokener.nextValue());
                    char nextClean3 = jSONTokener.nextClean();
                    if (nextClean3 == ',' || nextClean3 == ';') {
                        if (jSONTokener.nextClean() != '}') {
                            jSONTokener.back();
                        } else {
                            return;
                        }
                    } else if (nextClean3 != '}') {
                        throw jSONTokener.syntaxError("Expected a ',' or '}'");
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            throw jSONTokener.syntaxError("A JSONObject text must begin with '{'");
        }
    }

    public static String doubleToString(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            return "null";
        }
        String d2 = Double.toString(d);
        if (d2.indexOf(46) <= 0 || d2.indexOf(101) >= 0 || d2.indexOf(69) >= 0) {
            return d2;
        }
        while (d2.endsWith("0")) {
            d2 = d2.substring(0, d2.length() - 1);
        }
        return d2.endsWith(".") ? d2.substring(0, d2.length() - 1) : d2;
    }

    public static String[] getNames(Object obj) {
        Field[] fields;
        int length;
        if (obj == null || (length = (fields = obj.getClass().getFields()).length) == 0) {
            return null;
        }
        String[] strArr = new String[length];
        for (int i = 0; i < length; i++) {
            strArr[i] = fields[i].getName();
        }
        return strArr;
    }

    public static String[] getNames(JSONObject jSONObject) {
        int length = jSONObject.length();
        if (length == 0) {
            return null;
        }
        Iterator keys = jSONObject.keys();
        String[] strArr = new String[length];
        int i = 0;
        while (keys.hasNext()) {
            strArr[i] = (String) keys.next();
            i++;
        }
        return strArr;
    }

    public static String numberToString(Number number) throws JSONException {
        if (number != null) {
            testValidity(number);
            String obj = number.toString();
            if (obj.indexOf(46) <= 0 || obj.indexOf(101) >= 0 || obj.indexOf(69) >= 0) {
                return obj;
            }
            while (obj.endsWith("0")) {
                obj = obj.substring(0, obj.length() - 1);
            }
            return obj.endsWith(".") ? obj.substring(0, obj.length() - 1) : obj;
        }
        throw new JSONException("Null pointer");
    }

    private void populateMap(Object obj) {
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getClassLoader() != null ? cls.getMethods() : cls.getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (Modifier.isPublic(method.getModifiers())) {
                    String name = method.getName();
                    String str = "";
                    if (name.startsWith("get")) {
                        if (!name.equals("getClass") && !name.equals("getDeclaringClass")) {
                            str = name.substring(3);
                        }
                    } else if (name.startsWith("is")) {
                        str = name.substring(2);
                    }
                    if (str.length() > 0 && Character.isUpperCase(str.charAt(0)) && method.getParameterTypes().length == 0) {
                        if (str.length() == 1) {
                            str = str.toLowerCase();
                        } else if (!Character.isUpperCase(str.charAt(1))) {
                            str = str.substring(0, 1).toLowerCase() + str.substring(1);
                        }
                        Object invoke = method.invoke(obj, null);
                        if (invoke != null) {
                            this.map.put(str, wrap(invoke));
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x008a, code lost:
        if (r4 == '<') goto L_0x008c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static String quote(String str) {
        String str2;
        if (str == null || str.length() == 0) {
            return "\"\"";
        }
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length + 4);
        stringBuffer.append(Typography.quote);
        int i = 0;
        char c = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt == '\f') {
                str2 = "\\f";
            } else if (charAt != '\r') {
                if (charAt != '\"') {
                    if (charAt != '/') {
                        if (charAt != '\\') {
                            switch (charAt) {
                                case '\b':
                                    str2 = "\\b";
                                    break;
                                case '\t':
                                    str2 = "\\t";
                                    break;
                                case '\n':
                                    str2 = "\\n";
                                    break;
                                default:
                                    if (charAt < ' ' || ((charAt >= 128 && charAt < 160) || (charAt >= 8192 && charAt < 8448))) {
                                        String str3 = "000" + Integer.toHexString(charAt);
                                        str2 = "\\u" + str3.substring(str3.length() - 4);
                                        break;
                                    }
                                    stringBuffer.append(charAt);
                                    break;
                            }
                        }
                    }
                    i++;
                    c = charAt;
                }
                stringBuffer.append(IOUtils.DIR_SEPARATOR_WINDOWS);
                stringBuffer.append(charAt);
                i++;
                c = charAt;
            } else {
                str2 = "\\r";
            }
            stringBuffer.append(str2);
            i++;
            c = charAt;
        }
        stringBuffer.append(Typography.quote);
        return stringBuffer.toString();
    }

    public static Object stringToValue(String str) {
        if (str.equals("")) {
            return str;
        }
        if (str.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (str.equalsIgnoreCase("null")) {
            return NULL;
        }
        char charAt = str.charAt(0);
        if ((charAt >= '0' && charAt <= '9') || charAt == '.' || charAt == '-' || charAt == '+') {
            if (charAt == '0' && str.length() > 2 && (str.charAt(1) == 'x' || str.charAt(1) == 'X')) {
                try {
                    return new Integer(Integer.parseInt(str.substring(2), 16));
                } catch (Exception e) {
                }
            }
            try {
                if (str.indexOf(46) <= -1 && str.indexOf(101) <= -1 && str.indexOf(69) <= -1) {
                    Long l = new Long(str);
                    return l.longValue() == ((long) l.intValue()) ? new Integer(l.intValue()) : l;
                }
                return Double.valueOf(str);
            } catch (Exception e2) {
            }
        }
        return str;
    }

    public static void testValidity(Object obj) throws JSONException {
        if (obj == null) {
            return;
        }
        if (obj instanceof Double) {
            Double d = (Double) obj;
            if (d.isInfinite() || d.isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        } else if (obj instanceof Float) {
            Float f = (Float) obj;
            if (f.isInfinite() || f.isNaN()) {
                throw new JSONException("JSON does not allow non-finite numbers.");
            }
        }
    }

    public static String valueToString(Object obj) throws JSONException {
        if (obj == null || obj.equals(null)) {
            return "null";
        }
        if (!(obj instanceof JSONString)) {
            return obj instanceof Number ? numberToString((Number) obj) : ((obj instanceof Boolean) || (obj instanceof JSONObject) || (obj instanceof JSONArray)) ? obj.toString() : obj instanceof Map ? new JSONObject((Map) obj).toString() : obj instanceof Collection ? new JSONArray((Collection) obj).toString() : obj.getClass().isArray() ? new JSONArray(obj).toString() : quote(obj.toString());
        }
        try {
            String jSONString = ((JSONString) obj).toJSONString();
            if (jSONString instanceof String) {
                return jSONString;
            }
            throw new JSONException("Bad value from toJSONString: " + ((Object) jSONString));
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static String valueToString(Object obj, int i, int i2) throws JSONException {
        if (obj == null || obj.equals(null)) {
            return "null";
        }
        try {
            if (obj instanceof JSONString) {
                String jSONString = ((JSONString) obj).toJSONString();
                if (jSONString instanceof String) {
                    return jSONString;
                }
            }
        } catch (Exception e) {
        }
        return obj instanceof Number ? numberToString((Number) obj) : obj instanceof Boolean ? obj.toString() : obj instanceof JSONObject ? ((JSONObject) obj).toString(i, i2) : obj instanceof JSONArray ? ((JSONArray) obj).toString(i, i2) : obj instanceof Map ? new JSONObject((Map) obj).toString(i, i2) : obj instanceof Collection ? new JSONArray((Collection) obj).toString(i, i2) : obj.getClass().isArray() ? new JSONArray(obj).toString(i, i2) : quote(obj.toString());
    }

    public static Object wrap(Object obj) {
        try {
            if (obj == null) {
                return NULL;
            }
            if (!(obj instanceof JSONObject) && !(obj instanceof JSONArray) && !NULL.equals(obj) && !(obj instanceof JSONString) && !(obj instanceof Byte) && !(obj instanceof Character) && !(obj instanceof Short) && !(obj instanceof Integer) && !(obj instanceof Long) && !(obj instanceof Boolean) && !(obj instanceof Float) && !(obj instanceof Double) && !(obj instanceof String)) {
                if (obj instanceof Collection) {
                    return new JSONArray((Collection) obj);
                }
                if (obj.getClass().isArray()) {
                    return new JSONArray(obj);
                }
                if (obj instanceof Map) {
                    return new JSONObject((Map) obj);
                }
                Package r0 = obj.getClass().getPackage();
                String name = r0 != null ? r0.getName() : "";
                if (!name.startsWith("java.") && !name.startsWith("javax.") && obj.getClass().getClassLoader() != null) {
                    return new JSONObject(obj);
                }
                return obj.toString();
            }
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject accumulate(String str, Object obj) throws JSONException {
        JSONArray jSONArray;
        testValidity(obj);
        Object opt = opt(str);
        if (opt == null) {
            if (obj instanceof JSONArray) {
                jSONArray = new JSONArray();
            }
            put(str, obj);
            return this;
        } else if (opt instanceof JSONArray) {
            ((JSONArray) opt).put(obj);
            return this;
        } else {
            jSONArray = new JSONArray().put(opt);
        }
        obj = jSONArray.put(obj);
        put(str, obj);
        return this;
    }

    public JSONObject append(String str, Object obj) throws JSONException {
        JSONArray jSONArray;
        testValidity(obj);
        Object opt = opt(str);
        if (opt == null) {
            jSONArray = new JSONArray();
        } else if (opt instanceof JSONArray) {
            jSONArray = (JSONArray) opt;
        } else {
            throw new JSONException("JSONObject[" + str + "] is not a JSONArray.");
        }
        put(str, jSONArray.put(obj));
        return this;
    }

    public Object get(String str) throws JSONException {
        if (str != null) {
            Object opt = opt(str);
            if (opt != null) {
                return opt;
            }
            throw new JSONException("JSONObject[" + quote(str) + "] not found.");
        }
        throw new JSONException("Null key.");
    }

    public boolean getBoolean(String str) throws JSONException {
        Object obj = get(str);
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
        throw new JSONException("JSONObject[" + quote(str) + "] is not a Boolean.");
    }

    public double getDouble(String str) throws JSONException {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).doubleValue() : Double.parseDouble((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(str) + "] is not a number.");
        }
    }

    public int getInt(String str) throws JSONException {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).intValue() : Integer.parseInt((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(str) + "] is not an int.");
        }
    }

    public JSONArray getJSONArray(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof JSONArray) {
            return (JSONArray) obj;
        }
        throw new JSONException("JSONObject[" + quote(str) + "] is not a JSONArray.");
    }

    public JSONObject getJSONObject(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new JSONException("JSONObject[" + quote(str) + "] is not a JSONObject.");
    }

    public long getLong(String str) throws JSONException {
        Object obj = get(str);
        try {
            return obj instanceof Number ? ((Number) obj).longValue() : Long.parseLong((String) obj);
        } catch (Exception e) {
            throw new JSONException("JSONObject[" + quote(str) + "] is not a long.");
        }
    }

    public String getString(String str) throws JSONException {
        Object obj = get(str);
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new JSONException("JSONObject[" + quote(str) + "] not a string.");
    }

    public boolean has(String str) {
        return this.map.containsKey(str);
    }

    public JSONObject increment(String str) throws JSONException {
        double floatValue;
        Object opt = opt(str);
        if (opt == null) {
            put(str, 1);
        } else if (opt instanceof Integer) {
            put(str, ((Integer) opt).intValue() + 1);
        } else if (opt instanceof Long) {
            put(str, ((Long) opt).longValue() + 1);
        } else {
            if (opt instanceof Double) {
                floatValue = ((Double) opt).doubleValue() + 1.0d;
            } else if (opt instanceof Float) {
                floatValue = (double) (((Float) opt).floatValue() + 1.0f);
            } else {
                throw new JSONException("Unable to increment [" + quote(str) + "].");
            }
            put(str, floatValue);
        }
        return this;
    }

    public boolean isNull(String str) {
        return NULL.equals(opt(str));
    }

    public Iterator keys() {
        return this.map.keySet().iterator();
    }

    public int length() {
        return this.map.size();
    }

    public JSONArray names() {
        JSONArray jSONArray = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()) {
            jSONArray.put(keys.next());
        }
        if (jSONArray.length() == 0) {
            return null;
        }
        return jSONArray;
    }

    public Object opt(String str) {
        if (str == null) {
            return null;
        }
        return this.map.get(str);
    }

    public boolean optBoolean(String str) {
        return optBoolean(str, false);
    }

    public boolean optBoolean(String str, boolean z) {
        try {
            return getBoolean(str);
        } catch (Exception e) {
            return z;
        }
    }

    public double optDouble(String str) {
        return optDouble(str, Double.NaN);
    }

    public double optDouble(String str, double d) {
        try {
            return getDouble(str);
        } catch (Exception e) {
            return d;
        }
    }

    public int optInt(String str) {
        return optInt(str, 0);
    }

    public int optInt(String str, int i) {
        try {
            return getInt(str);
        } catch (Exception e) {
            return i;
        }
    }

    public JSONArray optJSONArray(String str) {
        Object opt = opt(str);
        if (opt instanceof JSONArray) {
            return (JSONArray) opt;
        }
        return null;
    }

    public JSONObject optJSONObject(String str) {
        Object opt = opt(str);
        if (opt instanceof JSONObject) {
            return (JSONObject) opt;
        }
        return null;
    }

    public long optLong(String str) {
        return optLong(str, 0);
    }

    public long optLong(String str, long j) {
        try {
            return getLong(str);
        } catch (Exception e) {
            return j;
        }
    }

    public String optString(String str) {
        return optString(str, "");
    }

    public String optString(String str, String str2) {
        Object opt = opt(str);
        return NULL.equals(opt) ? str2 : opt.toString();
    }

    public JSONObject put(String str, double d) throws JSONException {
        put(str, new Double(d));
        return this;
    }

    public JSONObject put(String str, int i) throws JSONException {
        put(str, new Integer(i));
        return this;
    }

    public JSONObject put(String str, long j) throws JSONException {
        put(str, new Long(j));
        return this;
    }

    public JSONObject put(String str, Object obj) throws JSONException {
        if (str != null) {
            if (obj != null) {
                testValidity(obj);
                this.map.put(str, obj);
            } else {
                remove(str);
            }
            return this;
        }
        throw new JSONException("Null key.");
    }

    public JSONObject put(String str, Collection collection) throws JSONException {
        put(str, new JSONArray(collection));
        return this;
    }

    public JSONObject put(String str, Map map) throws JSONException {
        put(str, new JSONObject(map));
        return this;
    }

    public JSONObject put(String str, boolean z) throws JSONException {
        put(str, z ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public JSONObject putOnce(String str, Object obj) throws JSONException {
        if (!(str == null || obj == null)) {
            if (opt(str) == null) {
                put(str, obj);
            } else {
                throw new JSONException("Duplicate key \"" + str + "\"");
            }
        }
        return this;
    }

    public JSONObject putOpt(String str, Object obj) throws JSONException {
        if (!(str == null || obj == null)) {
            put(str, obj);
        }
        return this;
    }

    public Object remove(String str) {
        return this.map.remove(str);
    }

    public JSONArray toJSONArray(JSONArray jSONArray) throws JSONException {
        if (jSONArray == null || jSONArray.length() == 0) {
            return null;
        }
        JSONArray jSONArray2 = new JSONArray();
        for (int i = 0; i < jSONArray.length(); i++) {
            jSONArray2.put(opt(jSONArray.getString(i)));
        }
        return jSONArray2;
    }

    public String toString() {
        try {
            Iterator keys = keys();
            StringBuffer stringBuffer = new StringBuffer("{");
            while (keys.hasNext()) {
                if (stringBuffer.length() > 1) {
                    stringBuffer.append(',');
                }
                Object next = keys.next();
                stringBuffer.append(quote(next.toString()));
                stringBuffer.append(':');
                stringBuffer.append(valueToString(this.map.get(next)));
            }
            stringBuffer.append('}');
            return stringBuffer.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String toString(int i) throws JSONException {
        return toString(i, 0);
    }

    String toString(int i, int i2) throws JSONException {
        int i3;
        int length = length();
        if (length == 0) {
            return "{}";
        }
        Iterator keys = keys();
        int i4 = i2 + i;
        StringBuffer stringBuffer = new StringBuffer("{");
        if (length == 1) {
            Object next = keys.next();
            stringBuffer.append(quote(next.toString()));
            stringBuffer.append(": ");
            stringBuffer.append(valueToString(this.map.get(next), i, i2));
        } else {
            while (true) {
                i3 = 0;
                if (!keys.hasNext()) {
                    break;
                }
                Object next2 = keys.next();
                if (stringBuffer.length() > 1) {
                    stringBuffer.append(",\n");
                } else {
                    stringBuffer.append('\n');
                }
                while (i3 < i4) {
                    stringBuffer.append(' ');
                    i3++;
                }
                stringBuffer.append(quote(next2.toString()));
                stringBuffer.append(": ");
                stringBuffer.append(valueToString(this.map.get(next2), i, i4));
            }
            if (stringBuffer.length() > 1) {
                stringBuffer.append('\n');
                while (i3 < i2) {
                    stringBuffer.append(' ');
                    i3++;
                }
            }
        }
        stringBuffer.append('}');
        return stringBuffer.toString();
    }

    public Writer write(Writer writer) throws JSONException {
        boolean z = false;
        try {
            Iterator keys = keys();
            writer.write(123);
            while (keys.hasNext()) {
                if (z) {
                    writer.write(44);
                }
                Object next = keys.next();
                writer.write(quote(next.toString()));
                writer.write(58);
                Object obj = this.map.get(next);
                if (obj instanceof JSONObject) {
                    ((JSONObject) obj).write(writer);
                } else if (obj instanceof JSONArray) {
                    ((JSONArray) obj).write(writer);
                } else {
                    writer.write(valueToString(obj));
                }
                z = true;
            }
            writer.write(125);
            return writer;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}
