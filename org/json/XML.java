package org.json;

import java.util.Iterator;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public class XML {
    public static final Character AMP = new Character(Typography.amp);
    public static final Character APOS = new Character('\'');
    public static final Character BANG = new Character('!');
    public static final Character EQ = new Character('=');
    public static final Character GT = new Character(Typography.greater);
    public static final Character LT = new Character(Typography.less);
    public static final Character QUEST = new Character('?');
    public static final Character QUOT = new Character(Typography.quote);
    public static final Character SLASH = new Character(IOUtils.DIR_SEPARATOR_UNIX);

    public static String escape(String str) {
        String str2;
        StringBuffer stringBuffer = new StringBuffer();
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (charAt == '\"') {
                str2 = "&quot;";
            } else if (charAt == '<') {
                str2 = "&lt;";
            } else if (charAt == '>') {
                str2 = "&gt;";
            } else if (charAt == '&') {
                str2 = "&amp;";
            } else if (charAt != '\'') {
                stringBuffer.append(charAt);
            } else {
                str2 = "&apos;";
            }
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    public static void noSpace(String str) throws JSONException {
        int length = str.length();
        if (length != 0) {
            for (int i = 0; i < length; i++) {
                if (Character.isWhitespace(str.charAt(i))) {
                    throw new JSONException("'" + str + "' contains a space character.");
                }
            }
            return;
        }
        throw new JSONException("Empty string.");
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x00ec, code lost:
        r7 = r10.nextToken();
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00f2, code lost:
        if ((r7 instanceof java.lang.String) == false) goto L_0x00fe;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0104, code lost:
        throw r10.syntaxError("Missing value");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static boolean parse(XMLTokener xMLTokener, JSONObject jSONObject, String str) throws JSONException {
        String str2;
        Object nextToken;
        String str3;
        Object nextToken2 = xMLTokener.nextToken();
        int i = 1;
        if (nextToken2 == BANG) {
            char next = xMLTokener.next();
            if (next == '-') {
                if (xMLTokener.next() == '-') {
                    str3 = "-->";
                } else {
                    xMLTokener.back();
                }
            } else if (next == '[') {
                if (!xMLTokener.nextToken().equals("CDATA") || xMLTokener.next() != '[') {
                    throw xMLTokener.syntaxError("Expected 'CDATA['");
                }
                String nextCDATA = xMLTokener.nextCDATA();
                if (nextCDATA.length() > 0) {
                    jSONObject.accumulate("content", nextCDATA);
                }
                return false;
            }
            do {
                Object nextMeta = xMLTokener.nextMeta();
                if (nextMeta == null) {
                    throw xMLTokener.syntaxError("Missing '>' after '<!'.");
                } else if (nextMeta == LT) {
                    i++;
                    continue;
                } else if (nextMeta == GT) {
                    i--;
                    continue;
                } else {
                    continue;
                }
            } while (i > 0);
            return false;
        } else if (nextToken2 == QUEST) {
            str3 = "?>";
        } else if (nextToken2 == SLASH) {
            Object nextToken3 = xMLTokener.nextToken();
            if (str == null) {
                throw xMLTokener.syntaxError("Mismatched close tag " + nextToken3);
            } else if (!nextToken3.equals(str)) {
                throw xMLTokener.syntaxError("Mismatched " + str + " and " + nextToken3);
            } else if (xMLTokener.nextToken() == GT) {
                return true;
            } else {
                throw xMLTokener.syntaxError("Misshaped close tag");
            }
        } else if (!(nextToken2 instanceof Character)) {
            String str4 = (String) nextToken2;
            JSONObject jSONObject2 = new JSONObject();
            Object obj = null;
            while (true) {
                while (true) {
                    if (obj == null) {
                        obj = xMLTokener.nextToken();
                    }
                    if (obj instanceof String) {
                        str2 = (String) obj;
                        Object nextToken4 = xMLTokener.nextToken();
                        if (nextToken4 == EQ) {
                            break;
                        }
                        jSONObject2.accumulate(str2, "");
                        obj = nextToken4;
                    } else if (obj == SLASH) {
                        if (xMLTokener.nextToken() == GT) {
                            if (jSONObject2.length() > 0) {
                                jSONObject.accumulate(str4, jSONObject2);
                            } else {
                                jSONObject.accumulate(str4, "");
                            }
                            return false;
                        }
                        throw xMLTokener.syntaxError("Misshaped tag");
                    } else if (obj == GT) {
                        while (true) {
                            Object nextContent = xMLTokener.nextContent();
                            if (nextContent == null) {
                                if (str4 == null) {
                                    return false;
                                }
                                throw xMLTokener.syntaxError("Unclosed tag " + str4);
                            } else if (nextContent instanceof String) {
                                String str5 = (String) nextContent;
                                if (str5.length() > 0) {
                                    jSONObject2.accumulate("content", stringToValue(str5));
                                }
                            } else if (nextContent == LT && parse(xMLTokener, jSONObject2, str4)) {
                                if (jSONObject2.length() == 0) {
                                    jSONObject.accumulate(str4, "");
                                } else if (jSONObject2.length() != 1 || jSONObject2.opt("content") == null) {
                                    jSONObject.accumulate(str4, jSONObject2);
                                } else {
                                    jSONObject.accumulate(str4, jSONObject2.opt("content"));
                                }
                                return false;
                            }
                        }
                    } else {
                        throw xMLTokener.syntaxError("Misshaped tag");
                    }
                }
                jSONObject2.accumulate(str2, stringToValue((String) nextToken));
            }
        } else {
            throw xMLTokener.syntaxError("Misshaped tag");
        }
        xMLTokener.skipPast(str3);
        return false;
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
            return JSONObject.NULL;
        }
        boolean z = false;
        if (str.equals("0")) {
            return new Integer(0);
        }
        try {
            char charAt = str.charAt(0);
            int i = 1;
            if (charAt == '-') {
                charAt = str.charAt(1);
                z = true;
            }
            if (charAt == '0') {
                if (z) {
                    i = 2;
                }
                if (str.charAt(i) == '0') {
                    return str;
                }
            }
            if (charAt >= '0' && charAt <= '9') {
                if (str.indexOf(46) >= 0) {
                    return Double.valueOf(str);
                }
                if (str.indexOf(101) < 0 && str.indexOf(69) < 0) {
                    Long l = new Long(str);
                    return l.longValue() == ((long) l.intValue()) ? new Integer(l.intValue()) : l;
                }
            }
        } catch (Exception e) {
        }
        return str;
    }

    public static JSONObject toJSONObject(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        XMLTokener xMLTokener = new XMLTokener(str);
        while (xMLTokener.more() && xMLTokener.skipPast("<")) {
            parse(xMLTokener, jSONObject, null);
        }
        return jSONObject;
    }

    public static String toString(Object obj) throws JSONException {
        return toString(obj, null);
    }

    public static String toString(Object obj, String str) throws JSONException {
        String escape;
        StringBuffer stringBuffer = new StringBuffer();
        if (obj instanceof JSONObject) {
            if (str != null) {
                stringBuffer.append(Typography.less);
                stringBuffer.append(str);
                stringBuffer.append(Typography.greater);
            }
            JSONObject jSONObject = (JSONObject) obj;
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String obj2 = keys.next().toString();
                Object opt = jSONObject.opt(obj2);
                if (opt == null) {
                    opt = "";
                }
                if (opt instanceof String) {
                    String str2 = (String) opt;
                }
                if (obj2.equals("content")) {
                    if (opt instanceof JSONArray) {
                        JSONArray jSONArray = (JSONArray) opt;
                        int length = jSONArray.length();
                        for (int i = 0; i < length; i++) {
                            if (i > 0) {
                                stringBuffer.append('\n');
                            }
                            stringBuffer.append(escape(jSONArray.get(i).toString()));
                        }
                    } else {
                        escape = escape(opt.toString());
                        stringBuffer.append(escape);
                    }
                } else if (opt instanceof JSONArray) {
                    JSONArray jSONArray2 = (JSONArray) opt;
                    int length2 = jSONArray2.length();
                    for (int i2 = 0; i2 < length2; i2++) {
                        Object obj3 = jSONArray2.get(i2);
                        if (obj3 instanceof JSONArray) {
                            stringBuffer.append(Typography.less);
                            stringBuffer.append(obj2);
                            stringBuffer.append(Typography.greater);
                            stringBuffer.append(toString(obj3));
                            stringBuffer.append("</");
                            stringBuffer.append(obj2);
                            stringBuffer.append(Typography.greater);
                        } else {
                            stringBuffer.append(toString(obj3, obj2));
                        }
                    }
                } else if (opt.equals("")) {
                    stringBuffer.append(Typography.less);
                    stringBuffer.append(obj2);
                    stringBuffer.append("/>");
                } else {
                    escape = toString(opt, obj2);
                    stringBuffer.append(escape);
                }
            }
            if (str != null) {
                stringBuffer.append("</");
                stringBuffer.append(str);
                stringBuffer.append(Typography.greater);
            }
            return stringBuffer.toString();
        }
        if (obj.getClass().isArray()) {
            obj = new JSONArray(obj);
        }
        if (obj instanceof JSONArray) {
            JSONArray jSONArray3 = (JSONArray) obj;
            int length3 = jSONArray3.length();
            for (int i3 = 0; i3 < length3; i3++) {
                stringBuffer.append(toString(jSONArray3.opt(i3), str == null ? "array" : str));
            }
            return stringBuffer.toString();
        }
        String escape2 = obj == null ? "null" : escape(obj.toString());
        if (str == null) {
            return "\"" + escape2 + "\"";
        } else if (escape2.length() == 0) {
            return "<" + str + "/>";
        } else {
            return "<" + str + ">" + escape2 + "</" + str + ">";
        }
    }
}
