package com.google.android.gms.internal.measurement;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import kotlin.text.Typography;
import org.apache.commons.io.IOUtils;
/* compiled from: com.google.android.gms:play-services-measurement-base@@19.0.1 */
/* loaded from: classes.dex */
public final class zzli {
    public static String zza(zzlg zzlg, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        zzd(zzlg, sb, 0);
        return sb.toString();
    }

    public static final void zzb(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object obj2 : (List) obj) {
                zzb(sb, i, str, obj2);
            }
        } else if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                zzb(sb, i, str, entry);
            }
        } else {
            sb.append('\n');
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                sb.append(' ');
            }
            sb.append(str);
            if (obj instanceof String) {
                sb.append(": \"");
                sb.append(zzmg.zza(zziy.zzm((String) obj)));
                sb.append(Typography.quote);
            } else if (obj instanceof zziy) {
                sb.append(": \"");
                sb.append(zzmg.zza((zziy) obj));
                sb.append(Typography.quote);
            } else if (obj instanceof zzjz) {
                sb.append(" {");
                zzd((zzjz) obj, sb, i + 2);
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                while (i2 < i) {
                    sb.append(' ');
                    i2++;
                }
                sb.append("}");
            } else if (obj instanceof Map.Entry) {
                sb.append(" {");
                Map.Entry entry2 = (Map.Entry) obj;
                int i4 = i + 2;
                zzb(sb, i4, "key", entry2.getKey());
                zzb(sb, i4, "value", entry2.getValue());
                sb.append(IOUtils.LINE_SEPARATOR_UNIX);
                while (i2 < i) {
                    sb.append(' ');
                    i2++;
                }
                sb.append("}");
            } else {
                sb.append(": ");
                sb.append(obj.toString());
            }
        }
    }

    private static final String zzc(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(charAt));
        }
        return sb.toString();
    }

    private static void zzd(zzlg zzlg, StringBuilder sb, int i) {
        String str;
        boolean equals;
        String str2;
        String str3;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        TreeSet<String> treeSet = new TreeSet();
        Method[] declaredMethods = zzlg.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            hashMap2.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                hashMap.put(method.getName(), method);
                if (method.getName().startsWith("get")) {
                    treeSet.add(method.getName());
                }
            }
        }
        for (String str4 : treeSet) {
            String substring = str4.startsWith("get") ? str4.substring(3) : str4;
            if (substring.endsWith("List") && !substring.endsWith("OrBuilderList") && !substring.equals("List")) {
                String valueOf = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf2 = String.valueOf(substring.substring(1, substring.length() - 4));
                if (valueOf2.length() != 0) {
                    str3 = valueOf.concat(valueOf2);
                } else {
                    str3 = new String(valueOf);
                }
                Method method2 = (Method) hashMap.get(str4);
                if (method2 != null && method2.getReturnType().equals(List.class)) {
                    zzb(sb, i, zzc(str3), zzjz.zzbE(method2, zzlg, new Object[0]));
                }
            }
            if (substring.endsWith("Map") && !substring.equals("Map")) {
                String valueOf3 = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf4 = String.valueOf(substring.substring(1, substring.length() - 3));
                if (valueOf4.length() != 0) {
                    str2 = valueOf3.concat(valueOf4);
                } else {
                    str2 = new String(valueOf3);
                }
                Method method3 = (Method) hashMap.get(str4);
                if (method3 != null && method3.getReturnType().equals(Map.class) && !method3.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(method3.getModifiers())) {
                    zzb(sb, i, zzc(str2), zzjz.zzbE(method3, zzlg, new Object[0]));
                }
            }
            String valueOf5 = String.valueOf(substring);
            if (((Method) hashMap2.get(valueOf5.length() != 0 ? "set".concat(valueOf5) : new String("set"))) != null) {
                if (substring.endsWith("Bytes")) {
                    String valueOf6 = String.valueOf(substring.substring(0, substring.length() - 5));
                    if (!hashMap.containsKey(valueOf6.length() != 0 ? "get".concat(valueOf6) : new String("get"))) {
                    }
                }
                String valueOf7 = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf8 = String.valueOf(substring.substring(1));
                String concat = valueOf8.length() != 0 ? valueOf7.concat(valueOf8) : new String(valueOf7);
                String valueOf9 = String.valueOf(substring);
                Method method4 = (Method) hashMap.get(valueOf9.length() != 0 ? "get".concat(valueOf9) : new String("get"));
                String valueOf10 = String.valueOf(substring);
                if (valueOf10.length() != 0) {
                    str = "has".concat(valueOf10);
                } else {
                    str = new String("has");
                }
                Method method5 = (Method) hashMap.get(str);
                if (method4 != null) {
                    Object zzbE = zzjz.zzbE(method4, zzlg, new Object[0]);
                    if (method5 == null) {
                        if (zzbE instanceof Boolean) {
                            if (((Boolean) zzbE).booleanValue()) {
                                zzb(sb, i, zzc(concat), zzbE);
                            }
                        } else if (zzbE instanceof Integer) {
                            if (((Integer) zzbE).intValue() != 0) {
                                zzb(sb, i, zzc(concat), zzbE);
                            }
                        } else if (zzbE instanceof Float) {
                            if (((Float) zzbE).floatValue() != 0.0f) {
                                zzb(sb, i, zzc(concat), zzbE);
                            }
                        } else if (!(zzbE instanceof Double)) {
                            if (zzbE instanceof String) {
                                equals = zzbE.equals("");
                            } else if (zzbE instanceof zziy) {
                                equals = zzbE.equals(zziy.zzb);
                            } else if (!(zzbE instanceof zzlg)) {
                                if ((zzbE instanceof Enum) && ((Enum) zzbE).ordinal() == 0) {
                                }
                                zzb(sb, i, zzc(concat), zzbE);
                            } else if (zzbE != ((zzlg) zzbE).zzbL()) {
                                zzb(sb, i, zzc(concat), zzbE);
                            }
                            if (!equals) {
                                zzb(sb, i, zzc(concat), zzbE);
                            }
                        } else if (((Double) zzbE).doubleValue() != 0.0d) {
                            zzb(sb, i, zzc(concat), zzbE);
                        }
                    } else if (((Boolean) zzjz.zzbE(method5, zzlg, new Object[0])).booleanValue()) {
                        zzb(sb, i, zzc(concat), zzbE);
                    }
                }
            }
        }
        if (!(zzlg instanceof zzjw)) {
            zzmj zzmj = ((zzjz) zzlg).zzc;
            if (zzmj != null) {
                zzmj.zzg(sb, i);
                return;
            }
            return;
        }
        zzjq zzjq = ((zzjw) zzlg).zza;
        throw null;
    }
}
