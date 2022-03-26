package org.json;

import java.util.HashMap;
/* loaded from: classes3.dex */
public class XMLTokener extends JSONTokener {
    public static final HashMap entity = new HashMap(8);

    static {
        entity.put("amp", XML.AMP);
        entity.put("apos", XML.APOS);
        entity.put("gt", XML.GT);
        entity.put("lt", XML.LT);
        entity.put("quot", XML.QUOT);
    }

    public XMLTokener(String str) {
        super(str);
    }

    public String nextCDATA() throws JSONException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            if (!end()) {
                stringBuffer.append(next);
                int length = stringBuffer.length() - 3;
                if (length >= 0 && stringBuffer.charAt(length) == ']' && stringBuffer.charAt(length + 1) == ']' && stringBuffer.charAt(length + 2) == '>') {
                    stringBuffer.setLength(length);
                    return stringBuffer.toString();
                }
            } else {
                throw syntaxError("Unclosed CDATA");
            }
        }
    }

    public Object nextContent() throws JSONException {
        char next;
        do {
            next = next();
        } while (Character.isWhitespace(next));
        if (next == 0) {
            return null;
        }
        if (next == '<') {
            return XML.LT;
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (next != '<' && next != 0) {
            if (next == '&') {
                stringBuffer.append(nextEntity(next));
            } else {
                stringBuffer.append(next);
            }
            next = next();
        }
        back();
        return stringBuffer.toString().trim();
    }

    public Object nextEntity(char c) throws JSONException {
        char next;
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            next = next();
            if (!Character.isLetterOrDigit(next) && next != '#') {
                break;
            }
            stringBuffer.append(Character.toLowerCase(next));
        }
        if (next == ';') {
            String stringBuffer2 = stringBuffer.toString();
            Object obj = entity.get(stringBuffer2);
            if (obj != null) {
                return obj;
            }
            return c + stringBuffer2 + ";";
        }
        throw syntaxError("Missing ';' in XML entity: &" + ((Object) stringBuffer));
    }

    public Object nextMeta() throws JSONException {
        char next;
        char next2;
        do {
            next = next();
        } while (Character.isWhitespace(next));
        if (next != 0) {
            if (next != '\'') {
                if (next == '/') {
                    return XML.SLASH;
                }
                if (next == '!') {
                    return XML.BANG;
                }
                if (next != '\"') {
                    switch (next) {
                        case '<':
                            return XML.LT;
                        case '=':
                            return XML.EQ;
                        case '>':
                            return XML.GT;
                        case '?':
                            return XML.QUEST;
                    }
                    while (true) {
                        char next3 = next();
                        if (Character.isWhitespace(next3)) {
                            return Boolean.TRUE;
                        }
                        if (next3 != 0 && next3 != '\'' && next3 != '/' && next3 != '!' && next3 != '\"') {
                            switch (next3) {
                            }
                        }
                    }
                    back();
                    return Boolean.TRUE;
                }
            }
            do {
                next2 = next();
                if (next2 == 0) {
                    throw syntaxError("Unterminated string");
                }
            } while (next2 != next);
            return Boolean.TRUE;
        }
        throw syntaxError("Misshaped meta tag");
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0053, code lost:
        return r5.toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x005a, code lost:
        throw syntaxError("Bad character in a name");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public Object nextToken() throws JSONException {
        char next;
        do {
            next = next();
        } while (Character.isWhitespace(next));
        if (next != 0) {
            if (next != '\'') {
                if (next == '/') {
                    return XML.SLASH;
                }
                if (next == '!') {
                    return XML.BANG;
                }
                if (next != '\"') {
                    switch (next) {
                        case '<':
                            throw syntaxError("Misplaced '<'");
                        case '=':
                            return XML.EQ;
                        case '>':
                            return XML.GT;
                        case '?':
                            return XML.QUEST;
                        default:
                            StringBuffer stringBuffer = new StringBuffer();
                            while (true) {
                                stringBuffer.append(next);
                                next = next();
                                if (!Character.isWhitespace(next) && next != 0) {
                                    if (next != '\'') {
                                        if (next != '/' && next != '[' && next != ']' && next != '!') {
                                            if (next == '\"') {
                                                break;
                                            } else {
                                                switch (next) {
                                                }
                                            }
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                return stringBuffer.toString();
                            }
                    }
                }
            }
            StringBuffer stringBuffer2 = new StringBuffer();
            while (true) {
                char next2 = next();
                if (next2 == 0) {
                    throw syntaxError("Unterminated string");
                } else if (next2 == next) {
                    return stringBuffer2.toString();
                } else {
                    if (next2 == '&') {
                        stringBuffer2.append(nextEntity(next2));
                    } else {
                        stringBuffer2.append(next2);
                    }
                }
            }
        } else {
            throw syntaxError("Misshaped element");
        }
    }

    public boolean skipPast(String str) throws JSONException {
        boolean z;
        int length = str.length();
        char[] cArr = new char[length];
        for (int i = 0; i < length; i++) {
            char next = next();
            if (next == 0) {
                return false;
            }
            cArr[i] = next;
        }
        int i2 = 0;
        while (true) {
            int i3 = 0;
            int i4 = i2;
            while (true) {
                if (i3 >= length) {
                    z = true;
                    break;
                } else if (cArr[i4] != str.charAt(i3)) {
                    z = false;
                    break;
                } else {
                    i4++;
                    if (i4 >= length) {
                        i4 -= length;
                    }
                    i3++;
                }
            }
            if (z) {
                return true;
            }
            char next2 = next();
            if (next2 == 0) {
                return false;
            }
            cArr[i2] = next2;
            i2++;
            if (i2 >= length) {
                i2 -= length;
            }
        }
    }
}
