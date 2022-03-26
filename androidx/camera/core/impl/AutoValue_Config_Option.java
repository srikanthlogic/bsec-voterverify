package androidx.camera.core.impl;

import androidx.camera.core.impl.Config;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class AutoValue_Config_Option<T> extends Config.Option<T> {
    private final String id;
    private final Object token;
    private final Class<T> valueClass;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AutoValue_Config_Option(String id, Class<T> valueClass, Object token) {
        if (id != null) {
            this.id = id;
            if (valueClass != null) {
                this.valueClass = valueClass;
                this.token = token;
                return;
            }
            throw new NullPointerException("Null valueClass");
        }
        throw new NullPointerException("Null id");
    }

    @Override // androidx.camera.core.impl.Config.Option
    public String getId() {
        return this.id;
    }

    @Override // androidx.camera.core.impl.Config.Option
    public Class<T> getValueClass() {
        return this.valueClass;
    }

    @Override // androidx.camera.core.impl.Config.Option
    public Object getToken() {
        return this.token;
    }

    public String toString() {
        return "Option{id=" + this.id + ", valueClass=" + this.valueClass + ", token=" + this.token + "}";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Config.Option)) {
            return false;
        }
        Config.Option<?> that = (Config.Option) o;
        if (this.id.equals(that.getId()) && this.valueClass.equals(that.getValueClass())) {
            Object obj = this.token;
            if (obj == null) {
                if (that.getToken() == null) {
                    return true;
                }
            } else if (obj.equals(that.getToken())) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int h$ = ((((1 * 1000003) ^ this.id.hashCode()) * 1000003) ^ this.valueClass.hashCode()) * 1000003;
        Object obj = this.token;
        return h$ ^ (obj == null ? 0 : obj.hashCode());
    }
}
