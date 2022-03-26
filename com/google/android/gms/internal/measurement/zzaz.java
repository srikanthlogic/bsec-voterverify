package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.List;
/* compiled from: com.google.android.gms:play-services-measurement@@19.0.1 */
/* loaded from: classes.dex */
public final class zzaz extends zzaw {
    /* JADX INFO: Access modifiers changed from: protected */
    public zzaz() {
        this.zza.add(zzbl.APPLY);
        this.zza.add(zzbl.BLOCK);
        this.zza.add(zzbl.BREAK);
        this.zza.add(zzbl.CASE);
        this.zza.add(zzbl.DEFAULT);
        this.zza.add(zzbl.CONTINUE);
        this.zza.add(zzbl.DEFINE_FUNCTION);
        this.zza.add(zzbl.FN);
        this.zza.add(zzbl.IF);
        this.zza.add(zzbl.QUOTE);
        this.zza.add(zzbl.RETURN);
        this.zza.add(zzbl.SWITCH);
        this.zza.add(zzbl.TERNARY);
    }

    private static zzap zzc(zzg zzg, List<zzap> list) {
        zzh.zzi(zzbl.FN.name(), 2, list);
        zzap zzb = zzg.zzb(list.get(0));
        zzap zzb2 = zzg.zzb(list.get(1));
        if (zzb2 instanceof zzae) {
            List<zzap> zzm = ((zzae) zzb2).zzm();
            List<zzap> arrayList = new ArrayList<>();
            if (list.size() > 2) {
                arrayList = list.subList(2, list.size());
            }
            return new zzao(zzb.zzi(), zzm, arrayList, zzg);
        }
        throw new IllegalArgumentException(String.format("FN requires an ArrayValue of parameter names found %s", zzb2.getClass().getCanonicalName()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:60:0x012a, code lost:
        if (r8.equals("continue") == false) goto L_0x012e;
     */
    @Override // com.google.android.gms.internal.measurement.zzaw
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final zzap zza(String str, zzg zzg, List<zzap> list) {
        zzap zzap;
        zzap zzb;
        zzbl zzbl = zzbl.ADD;
        int ordinal = zzh.zze(str).ordinal();
        if (ordinal == 2) {
            zzh.zzh(zzbl.APPLY.name(), 3, list);
            zzap zzb2 = zzg.zzb(list.get(0));
            String zzi = zzg.zzb(list.get(1)).zzi();
            zzap zzb3 = zzg.zzb(list.get(2));
            if (!(zzb3 instanceof zzae)) {
                throw new IllegalArgumentException(String.format("Function arguments for Apply are not a list found %s", zzb3.getClass().getCanonicalName()));
            } else if (!zzi.isEmpty()) {
                return zzb2.zzbK(zzi, zzg, ((zzae) zzb3).zzm());
            } else {
                throw new IllegalArgumentException("Function name for apply is undefined");
            }
        } else if (ordinal == 15) {
            zzh.zzh(zzbl.BREAK.name(), 0, list);
            return zzap.zzh;
        } else if (ordinal == 25) {
            return zzc(zzg, list);
        } else {
            if (ordinal == 41) {
                zzh.zzi(zzbl.IF.name(), 2, list);
                zzap zzb4 = zzg.zzb(list.get(0));
                zzap zzb5 = zzg.zzb(list.get(1));
                if (list.size() > 2) {
                    zzap = zzg.zzb(list.get(2));
                } else {
                    zzap = null;
                }
                zzap zzap2 = zzap.zzf;
                if (zzb4.zzg().booleanValue()) {
                    zzap2 = zzg.zzc((zzae) zzb5);
                } else if (zzap != null) {
                    zzap2 = zzg.zzc((zzae) zzap);
                }
                return zzap2 instanceof zzag ? zzap2 : zzap.zzf;
            } else if (ordinal == 54) {
                return new zzae(list);
            } else {
                if (ordinal != 57) {
                    if (ordinal != 19) {
                        if (ordinal == 20) {
                            zzh.zzi(zzbl.DEFINE_FUNCTION.name(), 2, list);
                            zzap zzc = zzc(zzg, list);
                            zzai zzai = (zzai) zzc;
                            if (zzai.zzc() == null) {
                                zzg.zzg("", zzc);
                            } else {
                                zzg.zzg(zzai.zzc(), zzc);
                            }
                            return zzc;
                        } else if (ordinal == 60) {
                            zzh.zzh(zzbl.SWITCH.name(), 3, list);
                            zzap zzb6 = zzg.zzb(list.get(0));
                            zzap zzb7 = zzg.zzb(list.get(1));
                            zzap zzb8 = zzg.zzb(list.get(2));
                            if (!(zzb7 instanceof zzae)) {
                                throw new IllegalArgumentException("Malformed SWITCH statement, cases are not a list");
                            } else if (zzb8 instanceof zzae) {
                                zzae zzae = (zzae) zzb7;
                                zzae zzae2 = (zzae) zzb8;
                                int i = 0;
                                boolean z = false;
                                while (true) {
                                    if (i < zzae.zzc()) {
                                        if (z || zzb6.equals(zzg.zzb(zzae.zze(i)))) {
                                            zzb = zzg.zzb(zzae2.zze(i));
                                            if (!(zzb instanceof zzag)) {
                                                z = true;
                                            } else if (((zzag) zzb).zzc().equals("break")) {
                                                return zzap.zzf;
                                            }
                                        } else {
                                            z = false;
                                        }
                                        i++;
                                    } else {
                                        if (zzae.zzc() + 1 == zzae2.zzc()) {
                                            zzb = zzg.zzb(zzae2.zze(zzae.zzc()));
                                            if (zzb instanceof zzag) {
                                                String zzc2 = ((zzag) zzb).zzc();
                                                if (!zzc2.equals("return")) {
                                                }
                                            }
                                        }
                                        return zzap.zzf;
                                    }
                                }
                                return zzb;
                            } else {
                                throw new IllegalArgumentException("Malformed SWITCH statement, case statements are not a list");
                            }
                        } else if (ordinal != 61) {
                            switch (ordinal) {
                                case 11:
                                    return zzg.zza().zzc(new zzae(list));
                                case 12:
                                    zzh.zzh(zzbl.BREAK.name(), 0, list);
                                    return zzap.zzi;
                                case 13:
                                    break;
                                default:
                                    return super.zzb(str);
                            }
                        } else {
                            zzh.zzh(zzbl.TERNARY.name(), 3, list);
                            if (zzg.zzb(list.get(0)).zzg().booleanValue()) {
                                return zzg.zzb(list.get(1));
                            }
                            return zzg.zzb(list.get(2));
                        }
                    }
                    if (list.isEmpty()) {
                        return zzap.zzf;
                    }
                    zzap zzb9 = zzg.zzb(list.get(0));
                    if (zzb9 instanceof zzae) {
                        return zzg.zzc((zzae) zzb9);
                    }
                    return zzap.zzf;
                } else if (list.isEmpty()) {
                    return zzap.zzj;
                } else {
                    zzh.zzh(zzbl.RETURN.name(), 1, list);
                    return new zzag("return", zzg.zzb(list.get(0)));
                }
            }
        }
    }
}
