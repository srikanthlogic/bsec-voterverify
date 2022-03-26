package kotlin.jvm.internal;

import com.facebook.imagepipeline.producers.DecodeProducer;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Function;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmClassMappingKt;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function9;
import kotlin.reflect.KCallable;
import kotlin.reflect.KClass;
import kotlin.reflect.KFunction;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KVisibility;
import kotlin.text.StringsKt;
import kotlin.text.Typography;
import org.apache.commons.io.FilenameUtils;
/* compiled from: ClassReference.kt */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 K2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001KB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010B\u001a\u00020\u00122\b\u0010C\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010D\u001a\u00020EH\u0002J\b\u0010F\u001a\u00020GH\u0016J\u0012\u0010H\u001a\u00020\u00122\b\u0010I\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010J\u001a\u00020-H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u001e\u0010$\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030%0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b&\u0010\u0010R\u001e\u0010'\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b(\u0010\u0010R\u0016\u0010)\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b*\u0010+R\u0016\u0010,\u001a\u0004\u0018\u00010-8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/R(\u00100\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b1\u0010\u0014\u001a\u0004\b2\u0010\u000bR\u0016\u00103\u001a\u0004\u0018\u00010-8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b4\u0010/R \u00105\u001a\b\u0012\u0004\u0012\u0002060\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b7\u0010\u0014\u001a\u0004\b8\u0010\u000bR \u00109\u001a\b\u0012\u0004\u0012\u00020:0\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b;\u0010\u0014\u001a\u0004\b<\u0010\u000bR\u001c\u0010=\u001a\u0004\u0018\u00010>8VX\u0097\u0004¢\u0006\f\u0012\u0004\b?\u0010\u0014\u001a\u0004\b@\u0010A¨\u0006L"}, d2 = {"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", DecodeProducer.EXTRA_IS_FINAL, "isFinal$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "sealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "supertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "typeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "visibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", "hashCode", "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
/* loaded from: classes3.dex */
public final class ClassReference implements KClass<Object>, ClassBasedDeclarationContainer {
    public static final Companion Companion = new Companion(null);
    private static final Map<Class<? extends Function<?>>, Integer> FUNCTION_CLASSES;
    private static final HashMap<String, String> classFqNames;
    private static final HashMap<String, String> primitiveFqNames;
    private static final HashMap<String, String> primitiveWrapperFqNames;
    private static final Map<String, String> simpleNames;
    private final Class<?> jClass;

    public static /* synthetic */ void isAbstract$annotations() {
    }

    public static /* synthetic */ void isCompanion$annotations() {
    }

    public static /* synthetic */ void isData$annotations() {
    }

    public static /* synthetic */ void isFinal$annotations() {
    }

    public static /* synthetic */ void isInner$annotations() {
    }

    public static /* synthetic */ void isOpen$annotations() {
    }

    public static /* synthetic */ void isSealed$annotations() {
    }

    public static /* synthetic */ void sealedSubclasses$annotations() {
    }

    public static /* synthetic */ void supertypes$annotations() {
    }

    public static /* synthetic */ void typeParameters$annotations() {
    }

    public static /* synthetic */ void visibility$annotations() {
    }

    public ClassReference(Class<?> cls) {
        Intrinsics.checkParameterIsNotNull(cls, "jClass");
        this.jClass = cls;
    }

    @Override // kotlin.jvm.internal.ClassBasedDeclarationContainer
    public Class<?> getJClass() {
        return this.jClass;
    }

    @Override // kotlin.reflect.KClass
    public String getSimpleName() {
        return Companion.getClassSimpleName(getJClass());
    }

    @Override // kotlin.reflect.KClass
    public String getQualifiedName() {
        return Companion.getClassQualifiedName(getJClass());
    }

    @Override // kotlin.reflect.KClass, kotlin.reflect.KDeclarationContainer
    public Collection<KCallable<?>> getMembers() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public Collection<KFunction<Object>> getConstructors() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public Collection<KClass<?>> getNestedClasses() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KAnnotatedElement
    public List<Annotation> getAnnotations() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public Object getObjectInstance() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isInstance(Object value) {
        return Companion.isInstance(value, getJClass());
    }

    @Override // kotlin.reflect.KClass
    public List<KTypeParameter> getTypeParameters() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public List<KType> getSupertypes() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public List<KClass<? extends Object>> getSealedSubclasses() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public KVisibility getVisibility() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isFinal() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isOpen() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isAbstract() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isSealed() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isData() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isInner() {
        error();
        throw null;
    }

    @Override // kotlin.reflect.KClass
    public boolean isCompanion() {
        error();
        throw null;
    }

    private final Void error() {
        throw new KotlinReflectionNotSupportedError();
    }

    @Override // kotlin.reflect.KClass
    public boolean equals(Object other) {
        return (other instanceof ClassReference) && Intrinsics.areEqual(JvmClassMappingKt.getJavaObjectType(this), JvmClassMappingKt.getJavaObjectType((KClass) other));
    }

    @Override // kotlin.reflect.KClass
    public int hashCode() {
        return JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    public String toString() {
        return getJClass().toString() + " (Kotlin reflection is not available)";
    }

    /* compiled from: ClassReference.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"}, k = 1, mv = {1, 1, 16})
    /* loaded from: classes3.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0042, code lost:
            if (r1 != null) goto L_0x0068;
         */
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final String getClassSimpleName(Class<?> cls) {
            String str;
            String str2;
            Intrinsics.checkParameterIsNotNull(cls, "jClass");
            String str3 = null;
            if (cls.isAnonymousClass()) {
                return null;
            }
            if (cls.isLocalClass()) {
                String name = cls.getSimpleName();
                Method method = cls.getEnclosingMethod();
                if (method != null) {
                    Intrinsics.checkExpressionValueIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
                    str2 = StringsKt.substringAfter$default(name, method.getName() + "$", (String) null, 2, (Object) null);
                }
                Constructor constructor = cls.getEnclosingConstructor();
                if (constructor != null) {
                    Intrinsics.checkExpressionValueIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
                    str2 = StringsKt.substringAfter$default(name, constructor.getName() + "$", (String) null, 2, (Object) null);
                } else {
                    str2 = null;
                }
                if (str2 != null) {
                    return str2;
                }
                Intrinsics.checkExpressionValueIsNotNull(name, AppMeasurementSdk.ConditionalUserProperty.NAME);
                return StringsKt.substringAfter$default(name, (char) Typography.dollar, (String) null, 2, (Object) null);
            } else if (cls.isArray()) {
                Class componentType = cls.getComponentType();
                Intrinsics.checkExpressionValueIsNotNull(componentType, "componentType");
                if (componentType.isPrimitive() && (str = (String) ClassReference.simpleNames.get(componentType.getName())) != null) {
                    str3 = str + "Array";
                }
                if (str3 != null) {
                    return str3;
                }
                return "Array";
            } else {
                String str4 = (String) ClassReference.simpleNames.get(cls.getName());
                if (str4 != null) {
                    return str4;
                }
                return cls.getSimpleName();
            }
        }

        public final String getClassQualifiedName(Class<?> cls) {
            String str;
            Intrinsics.checkParameterIsNotNull(cls, "jClass");
            String str2 = null;
            if (cls.isAnonymousClass() || cls.isLocalClass()) {
                return null;
            }
            if (cls.isArray()) {
                Class componentType = cls.getComponentType();
                Intrinsics.checkExpressionValueIsNotNull(componentType, "componentType");
                if (componentType.isPrimitive() && (str = (String) ClassReference.classFqNames.get(componentType.getName())) != null) {
                    str2 = str + "Array";
                }
                if (str2 != null) {
                    return str2;
                }
                return "kotlin.Array";
            }
            String str3 = (String) ClassReference.classFqNames.get(cls.getName());
            if (str3 != null) {
                return str3;
            }
            return cls.getCanonicalName();
        }

        public final boolean isInstance(Object value, Class<?> cls) {
            Intrinsics.checkParameterIsNotNull(cls, "jClass");
            Map map = ClassReference.FUNCTION_CLASSES;
            if (map != null) {
                Integer num = (Integer) map.get(cls);
                if (num != null) {
                    return TypeIntrinsics.isFunctionOfArity(value, num.intValue());
                }
                return (cls.isPrimitive() ? JvmClassMappingKt.getJavaObjectType(JvmClassMappingKt.getKotlinClass(cls)) : cls).isInstance(value);
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
    }

    /* JADX INFO: Multiple debug info for r7v1 'index$iv$iv'  int: [D('i' int), D('index$iv$iv' int)] */
    static {
        Iterable $this$mapIndexed$iv = CollectionsKt.listOf((Object[]) new Class[]{Function0.class, Function1.class, Function2.class, Function3.class, Function4.class, Function5.class, Function6.class, Function7.class, Function8.class, Function9.class, Function10.class, Function11.class, Function12.class, Function13.class, Function14.class, Function15.class, Function16.class, Function17.class, Function18.class, Function19.class, Function20.class, Function21.class, Function22.class});
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$mapIndexed$iv, 10));
        int index$iv$iv = 0;
        for (Object item$iv$iv : $this$mapIndexed$iv) {
            int index$iv$iv2 = index$iv$iv + 1;
            if (index$iv$iv < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            destination$iv$iv.add(TuplesKt.to((Class) item$iv$iv, Integer.valueOf(index$iv$iv)));
            index$iv$iv = index$iv$iv2;
        }
        FUNCTION_CLASSES = MapsKt.toMap((List) destination$iv$iv);
        HashMap $this$apply = new HashMap();
        $this$apply.put("boolean", "kotlin.Boolean");
        $this$apply.put("char", "kotlin.Char");
        $this$apply.put("byte", "kotlin.Byte");
        $this$apply.put("short", "kotlin.Short");
        $this$apply.put("int", "kotlin.Int");
        $this$apply.put("float", "kotlin.Float");
        $this$apply.put("long", "kotlin.Long");
        $this$apply.put("double", "kotlin.Double");
        primitiveFqNames = $this$apply;
        HashMap $this$apply2 = new HashMap();
        $this$apply2.put("java.lang.Boolean", "kotlin.Boolean");
        $this$apply2.put("java.lang.Character", "kotlin.Char");
        $this$apply2.put("java.lang.Byte", "kotlin.Byte");
        $this$apply2.put("java.lang.Short", "kotlin.Short");
        $this$apply2.put("java.lang.Integer", "kotlin.Int");
        $this$apply2.put("java.lang.Float", "kotlin.Float");
        $this$apply2.put("java.lang.Long", "kotlin.Long");
        $this$apply2.put("java.lang.Double", "kotlin.Double");
        primitiveWrapperFqNames = $this$apply2;
        HashMap $this$apply3 = new HashMap();
        $this$apply3.put("java.lang.Object", "kotlin.Any");
        $this$apply3.put("java.lang.String", "kotlin.String");
        $this$apply3.put("java.lang.CharSequence", "kotlin.CharSequence");
        $this$apply3.put("java.lang.Throwable", "kotlin.Throwable");
        $this$apply3.put("java.lang.Cloneable", "kotlin.Cloneable");
        $this$apply3.put("java.lang.Number", "kotlin.Number");
        $this$apply3.put("java.lang.Comparable", "kotlin.Comparable");
        $this$apply3.put("java.lang.Enum", "kotlin.Enum");
        $this$apply3.put("java.lang.annotation.Annotation", "kotlin.Annotation");
        $this$apply3.put("java.lang.Iterable", "kotlin.collections.Iterable");
        $this$apply3.put("java.util.Iterator", "kotlin.collections.Iterator");
        $this$apply3.put("java.util.Collection", "kotlin.collections.Collection");
        $this$apply3.put("java.util.List", "kotlin.collections.List");
        $this$apply3.put("java.util.Set", "kotlin.collections.Set");
        $this$apply3.put("java.util.ListIterator", "kotlin.collections.ListIterator");
        $this$apply3.put("java.util.Map", "kotlin.collections.Map");
        $this$apply3.put("java.util.Map$Entry", "kotlin.collections.Map.Entry");
        $this$apply3.put("kotlin.jvm.internal.StringCompanionObject", "kotlin.String.Companion");
        $this$apply3.put("kotlin.jvm.internal.EnumCompanionObject", "kotlin.Enum.Companion");
        $this$apply3.putAll(primitiveFqNames);
        $this$apply3.putAll(primitiveWrapperFqNames);
        Iterable<String> $this$associateTo$iv = primitiveFqNames.values();
        Intrinsics.checkExpressionValueIsNotNull($this$associateTo$iv, "primitiveFqNames.values");
        for (String kotlinName : $this$associateTo$iv) {
            StringBuilder sb = new StringBuilder();
            sb.append("kotlin.jvm.internal.");
            Intrinsics.checkExpressionValueIsNotNull(kotlinName, "kotlinName");
            sb.append(StringsKt.substringAfterLast$default(kotlinName, (char) FilenameUtils.EXTENSION_SEPARATOR, (String) null, 2, (Object) null));
            sb.append("CompanionObject");
            Pair pair = TuplesKt.to(sb.toString(), kotlinName + ".Companion");
            $this$apply3.put(pair.getFirst(), pair.getSecond());
        }
        for (Map.Entry<Class<? extends Function<?>>, Integer> entry : FUNCTION_CLASSES.entrySet()) {
            int arity = entry.getValue().intValue();
            $this$apply3.put(entry.getKey().getName(), "kotlin.Function" + arity);
        }
        classFqNames = $this$apply3;
        Map $this$mapValues$iv = classFqNames;
        Map destination$iv$iv2 = new LinkedHashMap(MapsKt.mapCapacity($this$mapValues$iv.size()));
        for (Object element$iv$iv$iv : $this$mapValues$iv.entrySet()) {
            destination$iv$iv2.put(((Map.Entry) element$iv$iv$iv).getKey(), StringsKt.substringAfterLast$default((String) ((Map.Entry) element$iv$iv$iv).getValue(), (char) FilenameUtils.EXTENSION_SEPARATOR, (String) null, 2, (Object) null));
        }
        simpleNames = destination$iv$iv2;
    }
}
