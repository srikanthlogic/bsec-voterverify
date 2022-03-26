package com.google.common.reflect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.text.Typography;
import okhttp3.HttpUrl;
import org.apache.commons.io.FilenameUtils;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class Types {
    private static final Function<Type, String> TYPE_NAME = new Function<Type, String>() { // from class: com.google.common.reflect.Types.1
        public String apply(Type from) {
            return JavaVersion.CURRENT.typeName(from);
        }
    };
    private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");

    public static Type newArrayType(Type componentType) {
        if (!(componentType instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(componentType);
        }
        WildcardType wildcard = (WildcardType) componentType;
        Type[] lowerBounds = wildcard.getLowerBounds();
        boolean z = true;
        Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        Type[] upperBounds = wildcard.getUpperBounds();
        if (upperBounds.length != 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }

    public static ParameterizedType newParameterizedTypeWithOwner(@NullableDecl Type ownerType, Class<?> rawType, Type... arguments) {
        if (ownerType == null) {
            return newParameterizedType(rawType, arguments);
        }
        Preconditions.checkNotNull(arguments);
        Preconditions.checkArgument(rawType.getEnclosingClass() != null, "Owner type for unenclosed %s", rawType);
        return new ParameterizedTypeImpl(ownerType, rawType, arguments);
    }

    public static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(rawType), rawType, arguments);
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes3.dex */
    public static abstract class ClassOwnership extends Enum<ClassOwnership> {
        static final ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();

        @NullableDecl
        abstract Class<?> getOwnerType(Class<?> cls);

        private static ClassOwnership detectJvmBehavior() {
            ParameterizedType parameterizedType = (ParameterizedType) new AnonymousClass1LocalClass<String>() { // from class: com.google.common.reflect.Types.ClassOwnership.3
            }.getClass().getGenericSuperclass();
            ClassOwnership[] values = values();
            for (ClassOwnership behavior : values) {
                if (behavior.getOwnerType(AnonymousClass1LocalClass.class) == parameterizedType.getOwnerType()) {
                    return behavior;
                }
            }
            throw new AssertionError();
        }
    }

    public static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) {
        return newTypeVariableImpl(declaration, name, bounds.length == 0 ? new Type[]{Object.class} : bounds);
    }

    static WildcardType subtypeOf(Type upperBound) {
        return new WildcardTypeImpl(new Type[0], new Type[]{upperBound});
    }

    static WildcardType supertypeOf(Type lowerBound) {
        return new WildcardTypeImpl(new Type[]{lowerBound}, new Type[]{Object.class});
    }

    public static String toString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }

    @NullableDecl
    public static Type getComponentType(Type type) {
        Preconditions.checkNotNull(type);
        final AtomicReference<Type> result = new AtomicReference<>();
        new TypeVisitor() { // from class: com.google.common.reflect.Types.2
            @Override // com.google.common.reflect.TypeVisitor
            void visitTypeVariable(TypeVariable<?> t) {
                result.set(Types.subtypeOfComponentType(t.getBounds()));
            }

            @Override // com.google.common.reflect.TypeVisitor
            void visitWildcardType(WildcardType t) {
                result.set(Types.subtypeOfComponentType(t.getUpperBounds()));
            }

            @Override // com.google.common.reflect.TypeVisitor
            void visitGenericArrayType(GenericArrayType t) {
                result.set(t.getGenericComponentType());
            }

            @Override // com.google.common.reflect.TypeVisitor
            void visitClass(Class<?> t) {
                result.set(t.getComponentType());
            }
        }.visit(type);
        return result.get();
    }

    @NullableDecl
    public static Type subtypeOfComponentType(Type[] bounds) {
        for (Type bound : bounds) {
            Type componentType = getComponentType(bound);
            if (componentType != null) {
                if (componentType instanceof Class) {
                    Class<?> componentClass = (Class) componentType;
                    if (componentClass.isPrimitive()) {
                        return componentClass;
                    }
                }
                return subtypeOf(componentType);
            }
        }
        return null;
    }

    /* loaded from: classes3.dex */
    public static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID;
        private final Type componentType;

        GenericArrayTypeImpl(Type componentType) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(componentType);
        }

        @Override // java.lang.reflect.GenericArrayType
        public Type getGenericComponentType() {
            return this.componentType;
        }

        @Override // java.lang.Object
        public String toString() {
            return Types.toString(this.componentType) + HttpUrl.PATH_SEGMENT_ENCODE_SET_URI;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.componentType.hashCode();
        }

        @Override // java.lang.Object
        public boolean equals(Object obj) {
            if (obj instanceof GenericArrayType) {
                return Objects.equal(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
            }
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID;
        private final ImmutableList<Type> argumentsList;
        @NullableDecl
        private final Type ownerType;
        private final Class<?> rawType;

        ParameterizedTypeImpl(@NullableDecl Type ownerType, Class<?> rawType, Type[] typeArguments) {
            Preconditions.checkNotNull(rawType);
            Preconditions.checkArgument(typeArguments.length == rawType.getTypeParameters().length);
            Types.disallowPrimitiveType(typeArguments, "type parameter");
            this.ownerType = ownerType;
            this.rawType = rawType;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArguments);
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type[] getActualTypeArguments() {
            return Types.toArray(this.argumentsList);
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getRawType() {
            return this.rawType;
        }

        @Override // java.lang.reflect.ParameterizedType
        public Type getOwnerType() {
            return this.ownerType;
        }

        @Override // java.lang.Object
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (this.ownerType != null && JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName()) {
                builder.append(JavaVersion.CURRENT.typeName(this.ownerType));
                builder.append(FilenameUtils.EXTENSION_SEPARATOR);
            }
            builder.append(this.rawType.getName());
            builder.append(Typography.less);
            builder.append(Types.COMMA_JOINER.join(Iterables.transform(this.argumentsList, Types.TYPE_NAME)));
            builder.append(Typography.greater);
            return builder.toString();
        }

        @Override // java.lang.Object
        public int hashCode() {
            Type type = this.ownerType;
            return ((type == null ? 0 : type.hashCode()) ^ this.argumentsList.hashCode()) ^ this.rawType.hashCode();
        }

        @Override // java.lang.Object
        public boolean equals(Object other) {
            if (!(other instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType that = (ParameterizedType) other;
            if (!getRawType().equals(that.getRawType()) || !Objects.equal(getOwnerType(), that.getOwnerType()) || !Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments())) {
                return false;
            }
            return true;
        }
    }

    private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
        return (TypeVariable) Reflection.newProxy(TypeVariable.class, new TypeVariableInvocationHandler(new TypeVariableImpl<>(genericDeclaration, name, bounds)));
    }

    /* loaded from: classes3.dex */
    public static final class TypeVariableInvocationHandler implements InvocationHandler {
        private static final ImmutableMap<String, Method> typeVariableMethods;
        private final TypeVariableImpl<?> typeVariableImpl;

        static {
            ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
            Method[] methods = TypeVariableImpl.class.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass().equals(TypeVariableImpl.class)) {
                    try {
                        method.setAccessible(true);
                    } catch (AccessControlException e) {
                    }
                    builder.put(method.getName(), method);
                }
            }
            typeVariableMethods = builder.build();
        }

        TypeVariableInvocationHandler(TypeVariableImpl<?> typeVariableImpl) {
            this.typeVariableImpl = typeVariableImpl;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Method typeVariableMethod = typeVariableMethods.get(methodName);
            if (typeVariableMethod != null) {
                try {
                    return typeVariableMethod.invoke(this.typeVariableImpl, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            } else {
                throw new UnsupportedOperationException(methodName);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class TypeVariableImpl<D extends GenericDeclaration> {
        private final ImmutableList<Type> bounds;
        private final D genericDeclaration;
        private final String name;

        TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
            Types.disallowPrimitiveType(bounds, "bound for type variable");
            this.genericDeclaration = (D) ((GenericDeclaration) Preconditions.checkNotNull(genericDeclaration));
            this.name = (String) Preconditions.checkNotNull(name);
            this.bounds = ImmutableList.copyOf(bounds);
        }

        public Type[] getBounds() {
            return Types.toArray(this.bounds);
        }

        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }

        public String getName() {
            return this.name;
        }

        public String getTypeName() {
            return this.name;
        }

        public String toString() {
            return this.name;
        }

        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }

        public boolean equals(Object obj) {
            if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
                if (obj == null || !Proxy.isProxyClass(obj.getClass()) || !(Proxy.getInvocationHandler(obj) instanceof TypeVariableInvocationHandler)) {
                    return false;
                }
                TypeVariableImpl<?> that = ((TypeVariableInvocationHandler) Proxy.getInvocationHandler(obj)).typeVariableImpl;
                return this.name.equals(that.getName()) && this.genericDeclaration.equals(that.getGenericDeclaration()) && this.bounds.equals(that.bounds);
            } else if (!(obj instanceof TypeVariable)) {
                return false;
            } else {
                TypeVariable<?> that2 = (TypeVariable) obj;
                return this.name.equals(that2.getName()) && this.genericDeclaration.equals(that2.getGenericDeclaration());
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID;
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;

        public WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
            Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(lowerBounds);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(upperBounds);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getLowerBounds() {
            return Types.toArray(this.lowerBounds);
        }

        @Override // java.lang.reflect.WildcardType
        public Type[] getUpperBounds() {
            return Types.toArray(this.upperBounds);
        }

        @Override // java.lang.Object
        public boolean equals(Object obj) {
            if (!(obj instanceof WildcardType)) {
                return false;
            }
            WildcardType that = (WildcardType) obj;
            if (!this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) || !this.upperBounds.equals(Arrays.asList(that.getUpperBounds()))) {
                return false;
            }
            return true;
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            StringBuilder builder = new StringBuilder("?");
            UnmodifiableIterator<Type> it = this.lowerBounds.iterator();
            while (it.hasNext()) {
                builder.append(" super ");
                builder.append(JavaVersion.CURRENT.typeName(it.next()));
            }
            for (Type upperBound : Types.filterUpperBounds(this.upperBounds)) {
                builder.append(" extends ");
                builder.append(JavaVersion.CURRENT.typeName(upperBound));
            }
            return builder.toString();
        }
    }

    public static Type[] toArray(Collection<Type> types) {
        return (Type[]) types.toArray(new Type[0]);
    }

    public static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
        return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
    }

    public static void disallowPrimitiveType(Type[] types, String usedAs) {
        for (Type type : types) {
            if (type instanceof Class) {
                Class<?> cls = (Class) type;
                Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
            }
        }
    }

    public static Class<?> getArrayClass(Class<?> componentType) {
        return Array.newInstance(componentType, 0).getClass();
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* loaded from: classes3.dex */
    public static abstract class JavaVersion extends Enum<JavaVersion> {
        static final JavaVersion CURRENT;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Type newArrayType(Type type);

        abstract Type usedInGenericType(Type type);

        static {
            if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
                if (new TypeCapture<Map.Entry<String, int[][]>>() { // from class: com.google.common.reflect.Types.JavaVersion.5
                }.capture().toString().contains("java.util.Map.java.util.Map")) {
                    CURRENT = JAVA8;
                } else {
                    CURRENT = JAVA9;
                }
            } else if (new TypeCapture<int[]>() { // from class: com.google.common.reflect.Types.JavaVersion.6
            }.capture() instanceof Class) {
                CURRENT = JAVA7;
            } else {
                CURRENT = JAVA6;
            }
        }

        final ImmutableList<Type> usedInGenericType(Type[] types) {
            ImmutableList.Builder<Type> builder = ImmutableList.builder();
            for (Type type : types) {
                builder.add((ImmutableList.Builder<Type>) usedInGenericType(type));
            }
            return builder.build();
        }

        String typeName(Type type) {
            return Types.toString(type);
        }

        boolean jdkTypeDuplicatesOwnerName() {
            return true;
        }
    }

    /* loaded from: classes3.dex */
    public static final class NativeTypeVariableEquals<X> {
        static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));

        NativeTypeVariableEquals() {
        }
    }

    private Types() {
    }
}
