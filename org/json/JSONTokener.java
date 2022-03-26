package org.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
/* loaded from: classes3.dex */
public class JSONTokener {
    private int character;
    private boolean eof;
    private int index;
    private int line;
    private char previous;
    private Reader reader;
    private boolean usePrevious;

    public JSONTokener(InputStream inputStream) throws JSONException {
        this(new InputStreamReader(inputStream));
    }

    public JSONTokener(Reader reader) {
        this.reader = !reader.markSupported() ? new BufferedReader(reader) : reader;
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.line = 1;
    }

    public JSONTokener(String str) {
        this(new StringReader(str));
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - '7';
        }
        if (c < 'a' || c > 'f') {
            return -1;
        }
        return c - 'W';
    }

    public void back() throws JSONException {
        int i;
        if (this.usePrevious || (i = this.index) <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        this.index = i - 1;
        this.character--;
        this.usePrevious = true;
        this.eof = false;
    }

    public boolean end() {
        return this.eof && !this.usePrevious;
    }

    public boolean more() throws JSONException {
        next();
        if (end()) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        int read;
        int i = 0;
        if (this.usePrevious) {
            this.usePrevious = false;
            read = this.previous;
        } else {
            try {
                read = this.reader.read();
                if (read <= 0) {
                    this.eof = true;
                    read = 0;
                }
            } catch (IOException e) {
                throw new JSONException(e);
            }
        }
        this.index++;
        if (this.previous == '\r') {
            this.line++;
            if (read != 10) {
                i = 1;
            }
        } else if (read == 10) {
            this.line++;
        } else {
            i = this.character + 1;
        }
        this.character = i;
        this.previous = (char) read;
        return this.previous;
    }

    public char next(char c) throws JSONException {
        char next = next();
        if (next == c) {
            return next;
        }
        throw syntaxError("Expected '" + c + "' and instead saw '" + next + "'");
    }

    public String next(int i) throws JSONException {
        if (i == 0) {
            return "";
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < i; i2++) {
            cArr[i2] = next();
            if (end()) {
                throw syntaxError("Substring bounds error");
            }
        }
        return new String(cArr);
    }

    public char nextClean() throws JSONException {
        char next;
        do {
            next = next();
            if (next == 0) {
                break;
            }
        } while (next <= ' ');
        return next;
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x0077, code lost:
        throw syntaxError("Unterminated string");
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public String nextString(char c) throws JSONException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            if (next == 0 || next == '\n' || next == '\r') {
                break;
            }
            if (next == '\\') {
                next = next();
                if (!(next == '\"' || next == '\'' || next == '/' || next == '\\')) {
                    if (next == 'b') {
                        next = '\b';
                    } else if (next == 'f') {
                        next = '\f';
                    } else if (next == 'n') {
                        stringBuffer.append('\n');
                    } else if (next == 'r') {
                        stringBuffer.append('\r');
                    } else if (next == 't') {
                        next = '\t';
                    } else if (next == 'u') {
                        next = (char) Integer.parseInt(next(4), 16);
                    } else {
                        throw syntaxError("Illegal escape.");
                    }
                }
            } else if (next == c) {
                return stringBuffer.toString();
            }
            stringBuffer.append(next);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x001c, code lost:
        back();
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public String nextTo(char c) throws JSONException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            if (next == c || next == 0 || next == '\n' || next == '\r') {
                break;
            }
            stringBuffer.append(next);
        }
        return stringBuffer.toString().trim();
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0020, code lost:
        back();
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public String nextTo(String str) throws JSONException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            char next = next();
            if (str.indexOf(next) >= 0 || next == 0 || next == '\n' || next == '\r') {
                break;
            }
            stringBuffer.append(next);
        }
        return stringBuffer.toString().trim();
    }

    public Object nextValue() throws JSONException {
        char nextClean = nextClean();
        if (nextClean == '\"' || nextClean == '\'') {
            return nextString(nextClean);
        }
        if (nextClean == '[') {
            back();
            return new JSONArray(this);
        } else if (nextClean != '{') {
            StringBuffer stringBuffer = new StringBuffer();
            while (nextClean >= ' ' && ",:]}/\\\"[{;=#".indexOf(nextClean) < 0) {
                stringBuffer.append(nextClean);
                nextClean = next();
            }
            back();
            String trim = stringBuffer.toString().trim();
            if (!trim.equals("")) {
                return JSONObject.stringToValue(trim);
            }
            throw syntaxError("Missing value");
        } else {
            back();
            return new JSONObject(this);
        }
    }

    public char skipTo(char c) throws JSONException {
        char next;
        try {
            int i = this.index;
            int i2 = this.character;
            int i3 = this.line;
            this.reader.mark(Integer.MAX_VALUE);
            do {
                next = next();
                if (next == 0) {
                    this.reader.reset();
                    this.index = i;
                    this.character = i2;
                    this.line = i3;
                    return next;
                }
            } while (next != c);
            back();
            return next;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public JSONException syntaxError(String str) {
        return new JSONException(str + toString());
    }

    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " + this.line + "]";
    }
}
