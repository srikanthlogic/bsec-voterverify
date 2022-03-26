package com.google.firebase.components;
/* loaded from: classes3.dex */
public final class Dependency {
    private final Class<?> anInterface;
    private final int injection;
    private final int type;

    private Dependency(Class<?> anInterface, int type, int injection) {
        this.anInterface = (Class) Preconditions.checkNotNull(anInterface, "Null dependency anInterface.");
        this.type = type;
        this.injection = injection;
    }

    @Deprecated
    public static Dependency optional(Class<?> anInterface) {
        return new Dependency(anInterface, 0, 0);
    }

    public static Dependency deferred(Class<?> anInterface) {
        return new Dependency(anInterface, 0, 2);
    }

    public static Dependency required(Class<?> anInterface) {
        return new Dependency(anInterface, 1, 0);
    }

    public static Dependency setOf(Class<?> anInterface) {
        return new Dependency(anInterface, 2, 0);
    }

    public static Dependency optionalProvider(Class<?> anInterface) {
        return new Dependency(anInterface, 0, 1);
    }

    public static Dependency requiredProvider(Class<?> anInterface) {
        return new Dependency(anInterface, 1, 1);
    }

    public static Dependency setOfProvider(Class<?> anInterface) {
        return new Dependency(anInterface, 2, 1);
    }

    public Class<?> getInterface() {
        return this.anInterface;
    }

    public boolean isRequired() {
        return this.type == 1;
    }

    public boolean isSet() {
        return this.type == 2;
    }

    public boolean isDirectInjection() {
        return this.injection == 0;
    }

    public boolean isDeferred() {
        return this.injection == 2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Dependency)) {
            return false;
        }
        Dependency other = (Dependency) o;
        if (this.anInterface == other.anInterface && this.type == other.type && this.injection == other.injection) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((1000003 ^ this.anInterface.hashCode()) * 1000003) ^ this.type) * 1000003) ^ this.injection;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Dependency{anInterface=");
        sb.append(this.anInterface);
        sb.append(", type=");
        int i = this.type;
        sb.append(i == 1 ? "required" : i == 0 ? "optional" : "set");
        sb.append(", injection=");
        sb.append(describeInjection(this.injection));
        return sb.append("}").toString();
    }

    private static String describeInjection(int injection) {
        if (injection == 0) {
            return "direct";
        }
        if (injection == 1) {
            return "provider";
        }
        if (injection == 2) {
            return "deferred";
        }
        throw new AssertionError("Unsupported injection: " + injection);
    }
}
