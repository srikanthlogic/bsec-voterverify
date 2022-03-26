package org.json;

import java.io.IOException;
import java.io.Writer;
/* loaded from: classes3.dex */
public class JSONWriter {
    private static final int maxdepth = 20;
    private boolean comma = false;
    protected char mode = 'i';
    private JSONObject[] stack = new JSONObject[20];
    private int top = 0;
    protected Writer writer;

    public JSONWriter(Writer writer) {
        this.writer = writer;
    }

    private JSONWriter append(String str) throws JSONException {
        if (str != null) {
            char c = this.mode;
            if (c == 'o' || c == 'a') {
                try {
                    if (this.comma && this.mode == 'a') {
                        this.writer.write(44);
                    }
                    this.writer.write(str);
                    if (this.mode == 'o') {
                        this.mode = 'k';
                    }
                    this.comma = true;
                    return this;
                } catch (IOException e) {
                    throw new JSONException(e);
                }
            } else {
                throw new JSONException("Value out of sequence.");
            }
        } else {
            throw new JSONException("Null pointer");
        }
    }

    private JSONWriter end(char c, char c2) throws JSONException {
        if (this.mode != c) {
            throw new JSONException(c == 'a' ? "Misplaced endArray." : "Misplaced endObject.");
        }
        pop(c);
        try {
            this.writer.write(c2);
            this.comma = true;
            return this;
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    private void pop(char c) throws JSONException {
        int i = this.top;
        if (i > 0) {
            char c2 = 'a';
            if ((this.stack[i + -1] == null ? 'a' : 'k') == c) {
                this.top--;
                int i2 = this.top;
                if (i2 == 0) {
                    c2 = 'd';
                } else if (this.stack[i2 - 1] != null) {
                    c2 = 'k';
                }
                this.mode = c2;
                return;
            }
            throw new JSONException("Nesting error.");
        }
        throw new JSONException("Nesting error.");
    }

    private void push(JSONObject jSONObject) throws JSONException {
        int i = this.top;
        if (i < 20) {
            this.stack[i] = jSONObject;
            this.mode = jSONObject == null ? 'a' : 'k';
            this.top++;
            return;
        }
        throw new JSONException("Nesting too deep.");
    }

    public JSONWriter array() throws JSONException {
        char c = this.mode;
        if (c == 'i' || c == 'o' || c == 'a') {
            push(null);
            append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }

    public JSONWriter endArray() throws JSONException {
        return end('a', ']');
    }

    public JSONWriter endObject() throws JSONException {
        return end('k', '}');
    }

    public JSONWriter key(String str) throws JSONException {
        if (str == null) {
            throw new JSONException("Null key.");
        } else if (this.mode == 'k') {
            try {
                this.stack[this.top - 1].putOnce(str, Boolean.TRUE);
                if (this.comma) {
                    this.writer.write(44);
                }
                this.writer.write(JSONObject.quote(str));
                this.writer.write(58);
                this.comma = false;
                this.mode = 'o';
                return this;
            } catch (IOException e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Misplaced key.");
        }
    }

    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        char c = this.mode;
        if (c == 'o' || c == 'a') {
            append("{");
            push(new JSONObject());
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");
    }

    public JSONWriter value(double d) throws JSONException {
        return value(new Double(d));
    }

    public JSONWriter value(long j) throws JSONException {
        return append(Long.toString(j));
    }

    public JSONWriter value(Object obj) throws JSONException {
        return append(JSONObject.valueToString(obj));
    }

    public JSONWriter value(boolean z) throws JSONException {
        return append(z ? "true" : "false");
    }
}
