package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.text.Typography;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
/* loaded from: classes3.dex */
public final class TypeResolver {
    private final TypeTable typeTable;

    public TypeResolver() {
        this.typeTable = new TypeTable();
    }

    private TypeResolver(TypeTable typeTable) {
        this.typeTable = typeTable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TypeResolver covariantly(Type contextType) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(contextType));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static TypeResolver invariantly(Type contextType) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(WildcardCapturer.INSTANCE.capture(contextType)));
    }

    public TypeResolver where(Type formal, Type actual) {
        Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
        populateTypeMappings(mappings, (Type) Preconditions.checkNotNull(formal), (Type) Preconditions.checkNotNull(actual));
        return where(mappings);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
        return new TypeResolver(this.typeTable.where(mappings));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
        if (!from.equals(to)) {
            new TypeVisitor() { // from class: com.google.common.reflect.TypeResolver.1
                @Override // com.google.common.reflect.TypeVisitor
                void visitTypeVariable(TypeVariable<?> typeVariable) {
                    mappings.put(new TypeVariableKey(typeVariable), to);
                }

                @Override // com.google.common.reflect.TypeVisitor
                void visitWildcardType(WildcardType fromWildcardType) {
                    Type type = to;
                    if (type instanceof WildcardType) {
                        WildcardType toWildcardType = (WildcardType) type;
                        Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                        Type[] toUpperBounds = toWildcardType.getUpperBounds();
                        Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                        Type[] toLowerBounds = toWildcardType.getLowerBounds();
                        Preconditions.checkArgument(fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length, "Incompatible type: %s vs. %s", fromWildcardType, to);
                        for (int i = 0; i < fromUpperBounds.length; i++) {
                            TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                        }
                        for (int i2 = 0; i2 < fromLowerBounds.length; i2++) {
                            TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i2], toLowerBounds[i2]);
                        }
                    }
                }

                @Override // com.google.common.reflect.TypeVisitor
                void visitParameterizedType(ParameterizedType fromParameterizedType) {
                    Type type = to;
                    if (!(type instanceof WildcardType)) {
                        ParameterizedType toParameterizedType = (ParameterizedType) TypeResolver.expectArgument(ParameterizedType.class, type);
                        if (!(fromParameterizedType.getOwnerType() == null || toParameterizedType.getOwnerType() == null)) {
                            TypeResolver.populateTypeMappings(mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
                        }
                        Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
                        Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                        Type[] toArgs = toParameterizedType.getActualTypeArguments();
                        Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
                        for (int i = 0; i < fromArgs.length; i++) {
                            TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                        }
                    }
                }

                @Override // com.google.common.reflect.TypeVisitor
                void visitGenericArrayType(GenericArrayType fromArrayType) {
                    Type type = to;
                    if (!(type instanceof WildcardType)) {
                        Type componentType = Types.getComponentType(type);
                        Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
                        TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
                    }
                }

                @Override // com.google.common.reflect.TypeVisitor
                void visitClass(Class<?> fromClass) {
                    if (!(to instanceof WildcardType)) {
                        throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to);
                    }
                }
            }.visit(from);
        }
    }

    public Type resolveType(Type type) {
        Preconditions.checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        if (type instanceof WildcardType) {
            return resolveWildcardType((WildcardType) type);
        }
        return type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Type[] resolveTypesInPlace(Type[] types) {
        for (int i = 0; i < types.length; i++) {
            types[i] = resolveType(types[i]);
        }
        return types;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Type[] resolveTypes(Type[] types) {
        Type[] result = new Type[types.length];
        for (int i = 0; i < types.length; i++) {
            result[i] = resolveType(types[i]);
        }
        return result;
    }

    private WildcardType resolveWildcardType(WildcardType type) {
        return new Types.WildcardTypeImpl(resolveTypes(type.getLowerBounds()), resolveTypes(type.getUpperBounds()));
    }

    private Type resolveGenericArrayType(GenericArrayType type) {
        return Types.newArrayType(resolveType(type.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType type) {
        Type owner = type.getOwnerType();
        return Types.newParameterizedTypeWithOwner(owner == null ? null : resolveType(owner), (Class) resolveType(type.getRawType()), resolveTypes(type.getActualTypeArguments()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T expectArgument(Class<T> type, Object arg) {
        try {
            return type.cast(arg);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;

        TypeTable() {
            this.map = ImmutableMap.of();
        }

        private TypeTable(ImmutableMap<TypeVariableKey, Type> map) {
            this.map = map;
        }

        final TypeTable where(Map<TypeVariableKey, ? extends Type> mappings) {
            ImmutableMap.Builder<TypeVariableKey, Type> builder = ImmutableMap.builder();
            builder.putAll(this.map);
            for (Map.Entry<TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
                TypeVariableKey variable = mapping.getKey();
                Type type = (Type) mapping.getValue();
                Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
                builder.put(variable, type);
            }
            return new TypeTable(builder.build());
        }

        final Type resolve(final TypeVariable<?> var) {
            return resolveInternal(var, new TypeTable() { // from class: com.google.common.reflect.TypeResolver.TypeTable.1
                @Override // com.google.common.reflect.TypeResolver.TypeTable
                public Type resolveInternal(TypeVariable<?> intermediateVar, TypeTable forDependent) {
                    if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
                        return intermediateVar;
                    }
                    return this.resolveInternal(intermediateVar, forDependent);
                }
            });
        }

        Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
            Type type = this.map.get(new TypeVariableKey(var));
            if (type != null) {
                return new TypeResolver(forDependants).resolveType(type);
            }
            Type[] bounds = var.getBounds();
            if (bounds.length == 0) {
                return var;
            }
            Type[] resolvedBounds = new TypeResolver(forDependants).resolveTypes(bounds);
            if (!Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY || !Arrays.equals(bounds, resolvedBounds)) {
                return Types.newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
            }
            return var;
        }
    }

    /* loaded from: classes3.dex */
    private static final class TypeMappingIntrospector extends TypeVisitor {
        private final Map<TypeVariableKey, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(Type contextType) {
            Preconditions.checkNotNull(contextType);
            TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.visit(contextType);
            return ImmutableMap.copyOf(introspector.mappings);
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitClass(Class<?> clazz) {
            visit(clazz.getGenericSuperclass());
            visit(clazz.getGenericInterfaces());
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitParameterizedType(ParameterizedType parameterizedType) {
            Class<?> rawClass = (Class) parameterizedType.getRawType();
            TypeVariable<?>[] vars = rawClass.getTypeParameters();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(vars.length == typeArgs.length);
            for (int i = 0; i < vars.length; i++) {
                map(new TypeVariableKey(vars[i]), typeArgs[i]);
            }
            visit(rawClass);
            visit(parameterizedType.getOwnerType());
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitTypeVariable(TypeVariable<?> t) {
            visit(t.getBounds());
        }

        @Override // com.google.common.reflect.TypeVisitor
        void visitWildcardType(WildcardType t) {
            visit(t.getUpperBounds());
        }

        private void map(TypeVariableKey var, Type arg) {
            if (!this.mappings.containsKey(var)) {
                Type t = arg;
                while (t != null) {
                    if (var.equalsType(t)) {
                        Type x = arg;
                        while (x != null) {
                            x = this.mappings.remove(TypeVariableKey.forLookup(x));
                        }
                        return;
                    }
                    t = this.mappings.get(TypeVariableKey.forLookup(t));
                }
                this.mappings.put(var, arg);
            }
        }
    }

    /* loaded from: classes3.dex */
    private static class WildcardCapturer {
        static final WildcardCapturer INSTANCE = new WildcardCapturer();
        private final AtomicInteger id;

        private WildcardCapturer() {
            this(new AtomicInteger());
        }

        private WildcardCapturer(AtomicInteger id) {
            this.id = id;
        }

        final Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if (type instanceof Class) {
                return type;
            }
            if (type instanceof TypeVariable) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                return Types.newArrayType(notForTypeVariable().capture(((GenericArrayType) type).getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?> rawType = (Class) parameterizedType.getRawType();
                TypeVariable<?>[] typeVars = rawType.getTypeParameters();
                Type[] typeArgs = parameterizedType.getActualTypeArguments();
                for (int i = 0; i < typeArgs.length; i++) {
                    typeArgs[i] = forTypeVariable(typeVars[i]).capture(typeArgs[i]);
                }
                return Types.newParameterizedTypeWithOwner(notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                if (wildcardType.getLowerBounds().length == 0) {
                    return captureAsTypeVariable(wildcardType.getUpperBounds());
                }
                return type;
            } else {
                throw new AssertionError("must have been one of the known types");
            }
        }

        TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
            return Types.newArtificialTypeVariable(WildcardCapturer.class, "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on((char) Typography.amp).join(upperBounds), upperBounds);
        }

        private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
            return new WildcardCapturer(this.id) { // from class: com.google.common.reflect.TypeResolver.WildcardCapturer.1
                @Override // com.google.common.reflect.TypeResolver.WildcardCapturer
                TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
                    Set<Type> combined = new LinkedHashSet<>(Arrays.asList(upperBounds));
                    combined.addAll(Arrays.asList(typeParam.getBounds()));
                    if (combined.size() > 1) {
                        combined.remove(Object.class);
                    }
                    return super.captureAsTypeVariable((Type[]) combined.toArray(new Type[0]));
                }
            };
        }

        private WildcardCapturer notForTypeVariable() {
            return new WildcardCapturer(this.id);
        }

        private Type captureNullable(@NullableDecl Type type) {
            if (type == null) {
                return null;
            }
            return capture(type);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class TypeVariableKey {
        private final TypeVariable<?> var;

        /* JADX INFO: Access modifiers changed from: package-private */
        public TypeVariableKey(TypeVariable<?> var) {
            this.var = (TypeVariable) Preconditions.checkNotNull(var);
        }

        public int hashCode() {
            return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
        }

        public boolean equals(Object obj) {
            if (obj instanceof TypeVariableKey) {
                return equalsTypeVariable(((TypeVariableKey) obj).var);
            }
            return false;
        }

        public String toString() {
            return this.var.toString();
        }

        static TypeVariableKey forLookup(Type t) {
            if (t instanceof TypeVariable) {
                return new TypeVariableKey((TypeVariable) t);
            }
            return null;
        }

        boolean equalsType(Type type) {
            if (type instanceof TypeVariable) {
                return equalsTypeVariable((TypeVariable) type);
            }
            return false;
        }

        private boolean equalsTypeVariable(TypeVariable<?> that) {
            return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
        }
    }
}
