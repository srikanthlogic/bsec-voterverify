package org.json;

import java.util.Iterator;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
/* loaded from: classes3.dex */
public class JSONML {
    /* JADX WARN: Code restructure failed: missing block: B:107:0x016c, code lost:
        throw r9.syntaxError("Reserved attribute.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0175, code lost:
        r7 = r9.nextToken();
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x017b, code lost:
        if ((r7 instanceof java.lang.String) == false) goto L_0x0188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x018e, code lost:
        throw r9.syntaxError("Missing value");
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0089, code lost:
        throw r9.syntaxError("Expected 'CDATA['");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private static Object parse(XMLTokener xMLTokener, boolean z, JSONArray jSONArray) throws JSONException {
        String str;
        Object nextToken;
        loop0: while (true) {
            Object nextContent = xMLTokener.nextContent();
            if (nextContent == XML.LT) {
                Object nextToken2 = xMLTokener.nextToken();
                if (nextToken2 instanceof Character) {
                    if (nextToken2 == XML.SLASH) {
                        Object nextToken3 = xMLTokener.nextToken();
                        if (!(nextToken3 instanceof String)) {
                            throw new JSONException("Expected a closing name instead of '" + nextToken3 + "'.");
                        } else if (xMLTokener.nextToken() == XML.GT) {
                            return nextToken3;
                        } else {
                            throw xMLTokener.syntaxError("Misshaped close tag");
                        }
                    } else if (nextToken2 == XML.BANG) {
                        char next = xMLTokener.next();
                        if (next == '-') {
                            if (xMLTokener.next() == '-') {
                                xMLTokener.skipPast("-->");
                            }
                            xMLTokener.back();
                        } else if (next != '[') {
                            int i = 1;
                            do {
                                Object nextMeta = xMLTokener.nextMeta();
                                if (nextMeta == null) {
                                    throw xMLTokener.syntaxError("Missing '>' after '<!'.");
                                } else if (nextMeta == XML.LT) {
                                    i++;
                                    continue;
                                } else if (nextMeta == XML.GT) {
                                    i--;
                                    continue;
                                } else {
                                    continue;
                                }
                            } while (i > 0);
                        } else if (!xMLTokener.nextToken().equals("CDATA") || xMLTokener.next() != '[') {
                            break;
                        } else if (jSONArray != null) {
                            nextContent = xMLTokener.nextCDATA();
                            jSONArray.put(nextContent);
                        }
                    } else if (nextToken2 == XML.QUEST) {
                        xMLTokener.skipPast("?>");
                    } else {
                        throw xMLTokener.syntaxError("Misshaped tag");
                    }
                } else if (nextToken2 instanceof String) {
                    String str2 = (String) nextToken2;
                    JSONArray jSONArray2 = new JSONArray();
                    JSONObject jSONObject = new JSONObject();
                    if (z) {
                        jSONArray2.put(str2);
                        if (jSONArray != null) {
                            jSONArray.put(jSONArray2);
                        }
                    } else {
                        jSONObject.put("tagName", str2);
                        if (jSONArray != null) {
                            jSONArray.put(jSONObject);
                        }
                    }
                    Object obj = null;
                    while (true) {
                        while (true) {
                            if (obj == null) {
                                obj = xMLTokener.nextToken();
                            }
                            if (obj == null) {
                                throw xMLTokener.syntaxError("Misshaped tag");
                            } else if (!(obj instanceof String)) {
                                if (z && jSONObject.length() > 0) {
                                    jSONArray2.put(jSONObject);
                                }
                                if (obj == XML.SLASH) {
                                    if (xMLTokener.nextToken() != XML.GT) {
                                        throw xMLTokener.syntaxError("Misshaped tag");
                                    } else if (jSONArray == null) {
                                        return z ? jSONArray2 : jSONObject;
                                    }
                                } else if (obj == XML.GT) {
                                    String str3 = (String) parse(xMLTokener, z, jSONArray2);
                                    if (str3 == null) {
                                        continue;
                                    } else if (str3.equals(str2)) {
                                        if (!z && jSONArray2.length() > 0) {
                                            jSONObject.put("childNodes", jSONArray2);
                                        }
                                        if (jSONArray == null) {
                                            return z ? jSONArray2 : jSONObject;
                                        }
                                    } else {
                                        throw xMLTokener.syntaxError("Mismatched '" + str2 + "' and '" + str3 + "'");
                                    }
                                } else {
                                    throw xMLTokener.syntaxError("Misshaped tag");
                                }
                            } else {
                                str = (String) obj;
                                if (z || !(str == "tagName" || str == "childNode")) {
                                    Object nextToken4 = xMLTokener.nextToken();
                                    if (nextToken4 == XML.EQ) {
                                        break;
                                    }
                                    jSONObject.accumulate(str, "");
                                    obj = nextToken4;
                                }
                            }
                        }
                        jSONObject.accumulate(str, XML.stringToValue((String) nextToken));
                    }
                } else {
                    throw xMLTokener.syntaxError("Bad tagName '" + nextToken2 + "'.");
                }
            } else if (jSONArray != null) {
                if (nextContent instanceof String) {
                    nextContent = XML.stringToValue((String) nextContent);
                }
                jSONArray.put(nextContent);
            }
        }
    }

    public static JSONArray toJSONArray(String str) throws JSONException {
        return toJSONArray(new XMLTokener(str));
    }

    public static JSONArray toJSONArray(XMLTokener xMLTokener) throws JSONException {
        return (JSONArray) parse(xMLTokener, true, null);
    }

    public static JSONObject toJSONObject(String str) throws JSONException {
        return toJSONObject(new XMLTokener(str));
    }

    public static JSONObject toJSONObject(XMLTokener xMLTokener) throws JSONException {
        return (JSONObject) parse(xMLTokener, false, null);
    }

    public static String toString(JSONArray jSONArray) throws JSONException {
        int i;
        String jsonml;
        StringBuffer stringBuffer = new StringBuffer();
        String string = jSONArray.getString(0);
        XML.noSpace(string);
        String escape = XML.escape(string);
        stringBuffer.append(Typography.less);
        stringBuffer.append(escape);
        Object opt = jSONArray.opt(1);
        if (opt instanceof JSONObject) {
            i = 2;
            JSONObject jSONObject = (JSONObject) opt;
            Iterator keys = jSONObject.keys();
            while (keys.hasNext()) {
                String obj = keys.next().toString();
                XML.noSpace(obj);
                String optString = jSONObject.optString(obj);
                if (optString != null) {
                    stringBuffer.append(' ');
                    stringBuffer.append(XML.escape(obj));
                    stringBuffer.append('=');
                    stringBuffer.append(Typography.quote);
                    stringBuffer.append(XML.escape(optString));
                    stringBuffer.append(Typography.quote);
                }
            }
        } else {
            i = 1;
        }
        int length = jSONArray.length();
        if (i >= length) {
            stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
        } else {
            stringBuffer.append(Typography.greater);
            do {
                Object obj2 = jSONArray.get(i);
                i++;
                if (obj2 != null) {
                    if (obj2 instanceof String) {
                        jsonml = XML.escape(obj2.toString());
                    } else if (obj2 instanceof JSONObject) {
                        jsonml = toString((JSONObject) obj2);
                    } else if (obj2 instanceof JSONArray) {
                        jsonml = toString((JSONArray) obj2);
                    } else {
                        continue;
                    }
                    stringBuffer.append(jsonml);
                    continue;
                }
            } while (i < length);
            stringBuffer.append(Typography.less);
            stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
            stringBuffer.append(escape);
        }
        stringBuffer.append(Typography.greater);
        return stringBuffer.toString();
    }

    public static String toString(JSONObject jSONObject) throws JSONException {
        String jsonml;
        StringBuffer stringBuffer = new StringBuffer();
        String optString = jSONObject.optString("tagName");
        if (optString == null) {
            return XML.escape(jSONObject.toString());
        }
        XML.noSpace(optString);
        String escape = XML.escape(optString);
        stringBuffer.append(Typography.less);
        stringBuffer.append(escape);
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String obj = keys.next().toString();
            if (!obj.equals("tagName") && !obj.equals("childNodes")) {
                XML.noSpace(obj);
                String optString2 = jSONObject.optString(obj);
                if (optString2 != null) {
                    stringBuffer.append(' ');
                    stringBuffer.append(XML.escape(obj));
                    stringBuffer.append('=');
                    stringBuffer.append(Typography.quote);
                    stringBuffer.append(XML.escape(optString2));
                    stringBuffer.append(Typography.quote);
                }
            }
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("childNodes");
        if (optJSONArray == null) {
            stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
        } else {
            stringBuffer.append(Typography.greater);
            int length = optJSONArray.length();
            for (int i = 0; i < length; i++) {
                Object obj2 = optJSONArray.get(i);
                if (obj2 != null) {
                    if (obj2 instanceof String) {
                        jsonml = XML.escape(obj2.toString());
                    } else if (obj2 instanceof JSONObject) {
                        jsonml = toString((JSONObject) obj2);
                    } else if (obj2 instanceof JSONArray) {
                        jsonml = toString((JSONArray) obj2);
                    }
                    stringBuffer.append(jsonml);
                }
            }
            stringBuffer.append(Typography.less);
            stringBuffer.append(IOUtils.DIR_SEPARATOR_UNIX);
            stringBuffer.append(escape);
        }
        stringBuffer.append(Typography.greater);
        return stringBuffer.toString();
    }
}
