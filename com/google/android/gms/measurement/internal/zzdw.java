package com.google.android.gms.measurement.internal;

import android.content.Context;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.facebook.common.util.UriUtil;
import com.google.android.gms.internal.measurement.zzha;
import com.google.android.gms.internal.measurement.zzhk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* compiled from: com.google.android.gms:play-services-measurement-impl@@19.0.1 */
/* loaded from: classes.dex */
public final class zzdw {
    public static final zzdv<Integer> zzE;
    public static final zzdv<Double> zzN;
    private static final List<zzdv<?>> zzaB = Collections.synchronizedList(new ArrayList());
    private static final Set<zzdv<?>> zzaC = Collections.synchronizedSet(new HashSet());
    public static final zzdv<Long> zza = zza("measurement.ad_id_cache_time", 10000L, 10000L, zzav.zza);
    public static final zzdv<Long> zzb = zza("measurement.monitoring.sample_period_millis", 86400000L, 86400000L, zzbg.zza);
    public static final zzdv<Long> zzc = zza("measurement.config.cache_time", 86400000L, 3600000L, zzay.zza);
    public static final zzdv<String> zzd = zza("measurement.config.url_scheme", UriUtil.HTTPS_SCHEME, UriUtil.HTTPS_SCHEME, zzbk.zza);
    public static final zzdv<String> zze = zza("measurement.config.url_authority", "app-measurement.com", "app-measurement.com", zzbw.zza);
    public static final zzdv<Integer> zzf = zza("measurement.upload.max_bundles", 100, 100, zzci.zza);
    public static final zzdv<Integer> zzg = zza("measurement.upload.max_batch_size", 65536, 65536, zzcu.zza);
    public static final zzdv<Integer> zzh = zza("measurement.upload.max_bundle_size", 65536, 65536, zzdg.zza);
    public static final zzdv<Integer> zzi = zza("measurement.upload.max_events_per_bundle", 1000, 1000, zzdn.zza);
    public static final zzdv<Integer> zzj = zza("measurement.upload.max_events_per_day", 100000, 100000, zzdo.zza);
    public static final zzdv<Integer> zzk = zza("measurement.upload.max_error_events_per_day", 1000, 1000, zzbr.zza);
    public static final zzdv<Integer> zzl = zza("measurement.upload.max_public_events_per_day", 50000, 50000, zzcc.zza);
    public static final zzdv<Integer> zzm = zza("measurement.upload.max_conversions_per_day", 10000, 10000, zzcn.zza);
    public static final zzdv<Integer> zzn = zza("measurement.upload.max_realtime_events_per_day", 10, 10, zzcy.zza);
    public static final zzdv<Integer> zzo = zza("measurement.store.max_stored_events_per_app", 100000, 100000, zzdj.zza);
    public static final zzdv<String> zzp = zza("measurement.upload.url", "https://app-measurement.com/a", "https://app-measurement.com/a", zzdp.zza);
    public static final zzdv<Long> zzq = zza("measurement.upload.backoff_period", 43200000L, 43200000L, zzdq.zza);
    public static final zzdv<Long> zzr = zza("measurement.upload.window_interval", 3600000L, 3600000L, zzdr.zza);
    public static final zzdv<Long> zzs = zza("measurement.upload.interval", 3600000L, 3600000L, zzaw.zza);
    public static final zzdv<Long> zzt = zza("measurement.upload.realtime_upload_interval", 10000L, 10000L, zzax.zza);
    public static final zzdv<Long> zzu = zza("measurement.upload.debug_upload_interval", 1000L, 1000L, zzaz.zza);
    public static final zzdv<Long> zzv = zza("measurement.upload.minimum_delay", 500L, 500L, zzba.zza);
    public static final zzdv<Long> zzw = zza("measurement.alarm_manager.minimum_interval", 60000L, 60000L, zzbb.zza);
    public static final zzdv<Long> zzx = zza("measurement.upload.stale_data_deletion_interval", 86400000L, 86400000L, zzbc.zza);
    public static final zzdv<Long> zzy = zza("measurement.upload.refresh_blacklisted_config_interval", 604800000L, 604800000L, zzbd.zza);
    public static final zzdv<Long> zzz = zza("measurement.upload.initial_upload_delay_time", 15000L, 15000L, zzbe.zza);
    public static final zzdv<Long> zzA = zza("measurement.upload.retry_time", 1800000L, 1800000L, zzbf.zza);
    public static final zzdv<Integer> zzB = zza("measurement.upload.retry_count", 6, 6, zzbh.zza);
    public static final zzdv<Long> zzC = zza("measurement.upload.max_queue_time", 2419200000L, 2419200000L, zzbi.zza);
    public static final zzdv<Integer> zzD = zza("measurement.lifetimevalue.max_currency_tracked", 4, 4, zzbj.zza);
    public static final zzdv<Integer> zzF = zza("measurement.upload.max_public_user_properties", 25, 25, null);
    public static final zzdv<Integer> zzG = zza("measurement.upload.max_event_name_cardinality", 500, 500, null);
    public static final zzdv<Integer> zzH = zza("measurement.upload.max_public_event_params", 25, 25, null);
    public static final zzdv<Long> zzI = zza("measurement.service_client.idle_disconnect_millis", 5000L, 5000L, zzbm.zza);
    public static final zzdv<Boolean> zzJ = zza("measurement.test.boolean_flag", false, false, zzbn.zza);
    public static final zzdv<String> zzK = zza("measurement.test.string_flag", "---", "---", zzbo.zza);
    public static final zzdv<Long> zzL = zza("measurement.test.long_flag", -1L, -1L, zzbp.zza);
    public static final zzdv<Integer> zzM = zza("measurement.test.int_flag", -2, -2, zzbq.zza);
    public static final zzdv<Integer> zzO = zza("measurement.experiment.max_ids", 50, 50, zzbt.zza);
    public static final zzdv<Integer> zzP = zza("measurement.max_bundles_per_iteration", 100, 100, zzbu.zza);
    public static final zzdv<Long> zzQ = zza("measurement.sdk.attribution.cache.ttl", 604800000L, 604800000L, zzbv.zza);
    public static final zzdv<Boolean> zzR = zza("measurement.validation.internal_limits_internal_event_params", false, false, zzbx.zza);
    public static final zzdv<Boolean> zzS = zza("measurement.collection.firebase_global_collection_flag_enabled", true, true, zzby.zza);
    public static final zzdv<Boolean> zzT = zza("measurement.collection.redundant_engagement_removal_enabled", false, false, zzbz.zza);
    public static final zzdv<Boolean> zzU = zza("measurement.collection.log_event_and_bundle_v2", true, true, zzca.zza);
    public static final zzdv<Boolean> zzV = zza("measurement.quality.checksum", false, false, null);
    public static final zzdv<Boolean> zzW = zza("measurement.audience.use_bundle_end_timestamp_for_non_sequence_property_filters", false, false, zzcb.zza);
    public static final zzdv<Boolean> zzX = zza("measurement.audience.refresh_event_count_filters_timestamp", false, false, zzcd.zza);
    public static final zzdv<Boolean> zzY = zza("measurement.audience.use_bundle_timestamp_for_event_count_filters", false, false, zzce.zza);
    public static final zzdv<Boolean> zzZ = zza("measurement.sdk.collection.retrieve_deeplink_from_bow_2", true, true, zzcf.zza);
    public static final zzdv<Boolean> zzaa = zza("measurement.sdk.collection.last_deep_link_referrer_campaign2", false, false, zzcg.zza);
    public static final zzdv<Boolean> zzab = zza("measurement.sdk.collection.enable_extend_user_property_size", true, true, zzch.zza);
    public static final zzdv<Boolean> zzac = zza("measurement.upload.file_lock_state_check", true, true, zzcj.zza);
    public static final zzdv<Boolean> zzad = zza("measurement.ga.ga_app_id", false, false, zzck.zza);
    public static final zzdv<Boolean> zzae = zza("measurement.lifecycle.app_in_background_parameter", false, false, zzcl.zza);
    public static final zzdv<Boolean> zzaf = zza("measurement.integration.disable_firebase_instance_id", false, false, zzcm.zza);
    public static final zzdv<Boolean> zzag = zza("measurement.lifecycle.app_backgrounded_engagement", false, false, zzco.zza);
    public static final zzdv<Boolean> zzah = zza("measurement.collection.service.update_with_analytics_fix", false, false, zzcp.zza);
    public static final zzdv<Boolean> zzai = zza("measurement.client.firebase_feature_rollout.v1.enable", true, true, zzcq.zza);
    public static final zzdv<Boolean> zzaj = zza("measurement.client.sessions.check_on_reset_and_enable2", true, true, zzcr.zza);
    public static final zzdv<Boolean> zzak = zza("measurement.scheduler.task_thread.cleanup_on_exit", false, false, zzcs.zza);
    public static final zzdv<Boolean> zzal = zza("measurement.upload.file_truncate_fix", false, false, zzct.zza);
    public static final zzdv<Boolean> zzam = zza("measurement.collection.synthetic_data_mitigation", false, false, zzcv.zza);
    public static final zzdv<Boolean> zzan = zza("measurement.androidId.delete_feature", true, true, zzcw.zza);
    public static final zzdv<Integer> zzao = zza("measurement.service.storage_consent_support_version", 203600, 203600, zzcx.zza);
    public static final zzdv<Boolean> zzap = zza("measurement.client.properties.non_null_origin", true, true, zzcz.zza);
    public static final zzdv<Boolean> zzaq = zza("measurement.client.click_identifier_control.dev", false, false, zzda.zza);
    public static final zzdv<Boolean> zzar = zza("measurement.service.click_identifier_control", false, false, zzdb.zza);
    public static final zzdv<Boolean> zzas = zza("measurement.client.reject_blank_user_id", true, true, zzdc.zza);
    public static final zzdv<Boolean> zzat = zza("measurement.config.persist_last_modified", true, true, zzdd.zza);
    public static final zzdv<Boolean> zzau = zza("measurement.client.consent.suppress_1p_in_ga4f_install", true, true, zzde.zza);
    public static final zzdv<Boolean> zzav = zza("measurement.module.pixie.ees", false, false, zzdf.zza);
    public static final zzdv<Boolean> zzaw = zza("measurement.euid.client.dev", false, false, zzdh.zza);
    public static final zzdv<Boolean> zzax = zza("measurement.euid.service", false, false, zzdi.zza);
    public static final zzdv<Boolean> zzay = zza("measurement.adid_zero.service", false, false, zzdk.zza);
    public static final zzdv<Boolean> zzaz = zza("measurement.adid_zero.remove_lair_if_adidzero_false", true, true, zzdl.zza);
    public static final zzdv<Boolean> zzaA = zza("measurement.service.refactor.package_side_screen", true, true, zzdm.zza);

    static {
        Integer valueOf = Integer.valueOf((int) ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        zzE = zza("measurement.audience.filter_result_max_count", valueOf, valueOf, zzbl.zza);
        Double valueOf2 = Double.valueOf(-3.0d);
        zzN = zza("measurement.test.double_flag", valueOf2, valueOf2, zzbs.zza);
    }

    static <V> zzdv<V> zza(String str, V v, V v2, zzds<V> zzds) {
        zzdv<V> zzdv = new zzdv<>(str, v, v2, zzds, null);
        zzaB.add(zzdv);
        return zzdv;
    }

    public static /* bridge */ /* synthetic */ List zzb() {
        return zzaB;
    }

    public static Map<String, String> zzc(Context context) {
        zzha zza2 = zzha.zza(context.getContentResolver(), zzhk.zza("com.google.android.gms.measurement"));
        return zza2 == null ? Collections.emptyMap() : zza2.zzc();
    }
}
