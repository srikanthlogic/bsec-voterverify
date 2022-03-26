package com.google.firebase.crashlytics.internal.model;

import com.facebook.common.util.UriUtil;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.encoders.FieldDescriptor;
import com.google.firebase.encoders.ObjectEncoder;
import com.google.firebase.encoders.ObjectEncoderContext;
import com.google.firebase.encoders.config.Configurator;
import com.google.firebase.encoders.config.EncoderConfig;
import java.io.IOException;
/* loaded from: classes3.dex */
public final class AutoCrashlyticsReportEncoder implements Configurator {
    public static final int CODEGEN_VERSION = 2;
    public static final Configurator CONFIG = new AutoCrashlyticsReportEncoder();

    private AutoCrashlyticsReportEncoder() {
    }

    @Override // com.google.firebase.encoders.config.Configurator
    public void configure(EncoderConfig<?> cfg) {
        cfg.registerEncoder(CrashlyticsReport.class, CrashlyticsReportEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport.class, CrashlyticsReportEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.class, CrashlyticsReportSessionEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session.class, CrashlyticsReportSessionEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Application.class, CrashlyticsReportSessionApplicationEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Application.class, CrashlyticsReportSessionApplicationEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Application.Organization.class, CrashlyticsReportSessionApplicationOrganizationEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Application_Organization.class, CrashlyticsReportSessionApplicationOrganizationEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.User.class, CrashlyticsReportSessionUserEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_User.class, CrashlyticsReportSessionUserEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.OperatingSystem.class, CrashlyticsReportSessionOperatingSystemEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_OperatingSystem.class, CrashlyticsReportSessionOperatingSystemEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Device.class, CrashlyticsReportSessionDeviceEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Device.class, CrashlyticsReportSessionDeviceEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.class, CrashlyticsReportSessionEventEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event.class, CrashlyticsReportSessionEventEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.class, CrashlyticsReportSessionEventApplicationEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application.class, CrashlyticsReportSessionEventApplicationEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.class, CrashlyticsReportSessionEventApplicationExecutionEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution.class, CrashlyticsReportSessionEventApplicationExecutionEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.Thread.class, CrashlyticsReportSessionEventApplicationExecutionThreadEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution_Thread.class, CrashlyticsReportSessionEventApplicationExecutionThreadEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.Thread.Frame.class, CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution_Thread_Frame.class, CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.Exception.class, CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution_Exception.class, CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.ApplicationExitInfo.class, CrashlyticsReportApplicationExitInfoEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_ApplicationExitInfo.class, CrashlyticsReportApplicationExitInfoEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.Signal.class, CrashlyticsReportSessionEventApplicationExecutionSignalEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution_Signal.class, CrashlyticsReportSessionEventApplicationExecutionSignalEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Application.Execution.BinaryImage.class, CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Application_Execution_BinaryImage.class, CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.CustomAttribute.class, CrashlyticsReportCustomAttributeEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_CustomAttribute.class, CrashlyticsReportCustomAttributeEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Device.class, CrashlyticsReportSessionEventDeviceEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Device.class, CrashlyticsReportSessionEventDeviceEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.Session.Event.Log.class, CrashlyticsReportSessionEventLogEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_Session_Event_Log.class, CrashlyticsReportSessionEventLogEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.FilesPayload.class, CrashlyticsReportFilesPayloadEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_FilesPayload.class, CrashlyticsReportFilesPayloadEncoder.INSTANCE);
        cfg.registerEncoder(CrashlyticsReport.FilesPayload.File.class, CrashlyticsReportFilesPayloadFileEncoder.INSTANCE);
        cfg.registerEncoder(AutoValue_CrashlyticsReport_FilesPayload_File.class, CrashlyticsReportFilesPayloadFileEncoder.INSTANCE);
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportEncoder implements ObjectEncoder<CrashlyticsReport> {
        static final CrashlyticsReportEncoder INSTANCE = new CrashlyticsReportEncoder();
        private static final FieldDescriptor SDKVERSION_DESCRIPTOR = FieldDescriptor.of("sdkVersion");
        private static final FieldDescriptor GMPAPPID_DESCRIPTOR = FieldDescriptor.of("gmpAppId");
        private static final FieldDescriptor PLATFORM_DESCRIPTOR = FieldDescriptor.of("platform");
        private static final FieldDescriptor INSTALLATIONUUID_DESCRIPTOR = FieldDescriptor.of("installationUuid");
        private static final FieldDescriptor BUILDVERSION_DESCRIPTOR = FieldDescriptor.of("buildVersion");
        private static final FieldDescriptor DISPLAYVERSION_DESCRIPTOR = FieldDescriptor.of("displayVersion");
        private static final FieldDescriptor SESSION_DESCRIPTOR = FieldDescriptor.of("session");
        private static final FieldDescriptor NDKPAYLOAD_DESCRIPTOR = FieldDescriptor.of("ndkPayload");

        private CrashlyticsReportEncoder() {
        }

        public void encode(CrashlyticsReport value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(SDKVERSION_DESCRIPTOR, value.getSdkVersion());
            ctx.add(GMPAPPID_DESCRIPTOR, value.getGmpAppId());
            ctx.add(PLATFORM_DESCRIPTOR, value.getPlatform());
            ctx.add(INSTALLATIONUUID_DESCRIPTOR, value.getInstallationUuid());
            ctx.add(BUILDVERSION_DESCRIPTOR, value.getBuildVersion());
            ctx.add(DISPLAYVERSION_DESCRIPTOR, value.getDisplayVersion());
            ctx.add(SESSION_DESCRIPTOR, value.getSession());
            ctx.add(NDKPAYLOAD_DESCRIPTOR, value.getNdkPayload());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEncoder implements ObjectEncoder<CrashlyticsReport.Session> {
        static final CrashlyticsReportSessionEncoder INSTANCE = new CrashlyticsReportSessionEncoder();
        private static final FieldDescriptor GENERATOR_DESCRIPTOR = FieldDescriptor.of("generator");
        private static final FieldDescriptor IDENTIFIER_DESCRIPTOR = FieldDescriptor.of("identifier");
        private static final FieldDescriptor STARTEDAT_DESCRIPTOR = FieldDescriptor.of("startedAt");
        private static final FieldDescriptor ENDEDAT_DESCRIPTOR = FieldDescriptor.of("endedAt");
        private static final FieldDescriptor CRASHED_DESCRIPTOR = FieldDescriptor.of("crashed");
        private static final FieldDescriptor APP_DESCRIPTOR = FieldDescriptor.of("app");
        private static final FieldDescriptor USER_DESCRIPTOR = FieldDescriptor.of("user");
        private static final FieldDescriptor OS_DESCRIPTOR = FieldDescriptor.of("os");
        private static final FieldDescriptor DEVICE_DESCRIPTOR = FieldDescriptor.of("device");
        private static final FieldDescriptor EVENTS_DESCRIPTOR = FieldDescriptor.of("events");
        private static final FieldDescriptor GENERATORTYPE_DESCRIPTOR = FieldDescriptor.of("generatorType");

        private CrashlyticsReportSessionEncoder() {
        }

        public void encode(CrashlyticsReport.Session value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(GENERATOR_DESCRIPTOR, value.getGenerator());
            ctx.add(IDENTIFIER_DESCRIPTOR, value.getIdentifierUtf8Bytes());
            ctx.add(STARTEDAT_DESCRIPTOR, value.getStartedAt());
            ctx.add(ENDEDAT_DESCRIPTOR, value.getEndedAt());
            ctx.add(CRASHED_DESCRIPTOR, value.isCrashed());
            ctx.add(APP_DESCRIPTOR, value.getApp());
            ctx.add(USER_DESCRIPTOR, value.getUser());
            ctx.add(OS_DESCRIPTOR, value.getOs());
            ctx.add(DEVICE_DESCRIPTOR, value.getDevice());
            ctx.add(EVENTS_DESCRIPTOR, value.getEvents());
            ctx.add(GENERATORTYPE_DESCRIPTOR, value.getGeneratorType());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionApplicationEncoder implements ObjectEncoder<CrashlyticsReport.Session.Application> {
        static final CrashlyticsReportSessionApplicationEncoder INSTANCE = new CrashlyticsReportSessionApplicationEncoder();
        private static final FieldDescriptor IDENTIFIER_DESCRIPTOR = FieldDescriptor.of("identifier");
        private static final FieldDescriptor VERSION_DESCRIPTOR = FieldDescriptor.of("version");
        private static final FieldDescriptor DISPLAYVERSION_DESCRIPTOR = FieldDescriptor.of("displayVersion");
        private static final FieldDescriptor ORGANIZATION_DESCRIPTOR = FieldDescriptor.of("organization");
        private static final FieldDescriptor INSTALLATIONUUID_DESCRIPTOR = FieldDescriptor.of("installationUuid");
        private static final FieldDescriptor DEVELOPMENTPLATFORM_DESCRIPTOR = FieldDescriptor.of("developmentPlatform");
        private static final FieldDescriptor DEVELOPMENTPLATFORMVERSION_DESCRIPTOR = FieldDescriptor.of("developmentPlatformVersion");

        private CrashlyticsReportSessionApplicationEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Application value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(IDENTIFIER_DESCRIPTOR, value.getIdentifier());
            ctx.add(VERSION_DESCRIPTOR, value.getVersion());
            ctx.add(DISPLAYVERSION_DESCRIPTOR, value.getDisplayVersion());
            ctx.add(ORGANIZATION_DESCRIPTOR, value.getOrganization());
            ctx.add(INSTALLATIONUUID_DESCRIPTOR, value.getInstallationUuid());
            ctx.add(DEVELOPMENTPLATFORM_DESCRIPTOR, value.getDevelopmentPlatform());
            ctx.add(DEVELOPMENTPLATFORMVERSION_DESCRIPTOR, value.getDevelopmentPlatformVersion());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionApplicationOrganizationEncoder implements ObjectEncoder<CrashlyticsReport.Session.Application.Organization> {
        static final CrashlyticsReportSessionApplicationOrganizationEncoder INSTANCE = new CrashlyticsReportSessionApplicationOrganizationEncoder();
        private static final FieldDescriptor CLSID_DESCRIPTOR = FieldDescriptor.of("clsId");

        private CrashlyticsReportSessionApplicationOrganizationEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Application.Organization value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(CLSID_DESCRIPTOR, value.getClsId());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionUserEncoder implements ObjectEncoder<CrashlyticsReport.Session.User> {
        static final CrashlyticsReportSessionUserEncoder INSTANCE = new CrashlyticsReportSessionUserEncoder();
        private static final FieldDescriptor IDENTIFIER_DESCRIPTOR = FieldDescriptor.of("identifier");

        private CrashlyticsReportSessionUserEncoder() {
        }

        public void encode(CrashlyticsReport.Session.User value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(IDENTIFIER_DESCRIPTOR, value.getIdentifier());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionOperatingSystemEncoder implements ObjectEncoder<CrashlyticsReport.Session.OperatingSystem> {
        static final CrashlyticsReportSessionOperatingSystemEncoder INSTANCE = new CrashlyticsReportSessionOperatingSystemEncoder();
        private static final FieldDescriptor PLATFORM_DESCRIPTOR = FieldDescriptor.of("platform");
        private static final FieldDescriptor VERSION_DESCRIPTOR = FieldDescriptor.of("version");
        private static final FieldDescriptor BUILDVERSION_DESCRIPTOR = FieldDescriptor.of("buildVersion");
        private static final FieldDescriptor JAILBROKEN_DESCRIPTOR = FieldDescriptor.of("jailbroken");

        private CrashlyticsReportSessionOperatingSystemEncoder() {
        }

        public void encode(CrashlyticsReport.Session.OperatingSystem value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(PLATFORM_DESCRIPTOR, value.getPlatform());
            ctx.add(VERSION_DESCRIPTOR, value.getVersion());
            ctx.add(BUILDVERSION_DESCRIPTOR, value.getBuildVersion());
            ctx.add(JAILBROKEN_DESCRIPTOR, value.isJailbroken());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionDeviceEncoder implements ObjectEncoder<CrashlyticsReport.Session.Device> {
        static final CrashlyticsReportSessionDeviceEncoder INSTANCE = new CrashlyticsReportSessionDeviceEncoder();
        private static final FieldDescriptor ARCH_DESCRIPTOR = FieldDescriptor.of("arch");
        private static final FieldDescriptor MODEL_DESCRIPTOR = FieldDescriptor.of("model");
        private static final FieldDescriptor CORES_DESCRIPTOR = FieldDescriptor.of("cores");
        private static final FieldDescriptor RAM_DESCRIPTOR = FieldDescriptor.of("ram");
        private static final FieldDescriptor DISKSPACE_DESCRIPTOR = FieldDescriptor.of("diskSpace");
        private static final FieldDescriptor SIMULATOR_DESCRIPTOR = FieldDescriptor.of("simulator");
        private static final FieldDescriptor STATE_DESCRIPTOR = FieldDescriptor.of("state");
        private static final FieldDescriptor MANUFACTURER_DESCRIPTOR = FieldDescriptor.of("manufacturer");
        private static final FieldDescriptor MODELCLASS_DESCRIPTOR = FieldDescriptor.of("modelClass");

        private CrashlyticsReportSessionDeviceEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Device value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(ARCH_DESCRIPTOR, value.getArch());
            ctx.add(MODEL_DESCRIPTOR, value.getModel());
            ctx.add(CORES_DESCRIPTOR, value.getCores());
            ctx.add(RAM_DESCRIPTOR, value.getRam());
            ctx.add(DISKSPACE_DESCRIPTOR, value.getDiskSpace());
            ctx.add(SIMULATOR_DESCRIPTOR, value.isSimulator());
            ctx.add(STATE_DESCRIPTOR, value.getState());
            ctx.add(MANUFACTURER_DESCRIPTOR, value.getManufacturer());
            ctx.add(MODELCLASS_DESCRIPTOR, value.getModelClass());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event> {
        static final CrashlyticsReportSessionEventEncoder INSTANCE = new CrashlyticsReportSessionEventEncoder();
        private static final FieldDescriptor TIMESTAMP_DESCRIPTOR = FieldDescriptor.of("timestamp");
        private static final FieldDescriptor TYPE_DESCRIPTOR = FieldDescriptor.of("type");
        private static final FieldDescriptor APP_DESCRIPTOR = FieldDescriptor.of("app");
        private static final FieldDescriptor DEVICE_DESCRIPTOR = FieldDescriptor.of("device");
        private static final FieldDescriptor LOG_DESCRIPTOR = FieldDescriptor.of("log");

        private CrashlyticsReportSessionEventEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(TIMESTAMP_DESCRIPTOR, value.getTimestamp());
            ctx.add(TYPE_DESCRIPTOR, value.getType());
            ctx.add(APP_DESCRIPTOR, value.getApp());
            ctx.add(DEVICE_DESCRIPTOR, value.getDevice());
            ctx.add(LOG_DESCRIPTOR, value.getLog());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application> {
        static final CrashlyticsReportSessionEventApplicationEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationEncoder();
        private static final FieldDescriptor EXECUTION_DESCRIPTOR = FieldDescriptor.of("execution");
        private static final FieldDescriptor CUSTOMATTRIBUTES_DESCRIPTOR = FieldDescriptor.of("customAttributes");
        private static final FieldDescriptor INTERNALKEYS_DESCRIPTOR = FieldDescriptor.of("internalKeys");
        private static final FieldDescriptor BACKGROUND_DESCRIPTOR = FieldDescriptor.of("background");
        private static final FieldDescriptor UIORIENTATION_DESCRIPTOR = FieldDescriptor.of("uiOrientation");

        private CrashlyticsReportSessionEventApplicationEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(EXECUTION_DESCRIPTOR, value.getExecution());
            ctx.add(CUSTOMATTRIBUTES_DESCRIPTOR, value.getCustomAttributes());
            ctx.add(INTERNALKEYS_DESCRIPTOR, value.getInternalKeys());
            ctx.add(BACKGROUND_DESCRIPTOR, value.getBackground());
            ctx.add(UIORIENTATION_DESCRIPTOR, value.getUiOrientation());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution> {
        static final CrashlyticsReportSessionEventApplicationExecutionEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionEncoder();
        private static final FieldDescriptor THREADS_DESCRIPTOR = FieldDescriptor.of("threads");
        private static final FieldDescriptor EXCEPTION_DESCRIPTOR = FieldDescriptor.of("exception");
        private static final FieldDescriptor APPEXITINFO_DESCRIPTOR = FieldDescriptor.of("appExitInfo");
        private static final FieldDescriptor SIGNAL_DESCRIPTOR = FieldDescriptor.of("signal");
        private static final FieldDescriptor BINARIES_DESCRIPTOR = FieldDescriptor.of("binaries");

        private CrashlyticsReportSessionEventApplicationExecutionEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(THREADS_DESCRIPTOR, value.getThreads());
            ctx.add(EXCEPTION_DESCRIPTOR, value.getException());
            ctx.add(APPEXITINFO_DESCRIPTOR, value.getAppExitInfo());
            ctx.add(SIGNAL_DESCRIPTOR, value.getSignal());
            ctx.add(BINARIES_DESCRIPTOR, value.getBinaries());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionThreadEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution.Thread> {
        static final CrashlyticsReportSessionEventApplicationExecutionThreadEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionThreadEncoder();
        private static final FieldDescriptor NAME_DESCRIPTOR = FieldDescriptor.of(AppMeasurementSdk.ConditionalUserProperty.NAME);
        private static final FieldDescriptor IMPORTANCE_DESCRIPTOR = FieldDescriptor.of("importance");
        private static final FieldDescriptor FRAMES_DESCRIPTOR = FieldDescriptor.of("frames");

        private CrashlyticsReportSessionEventApplicationExecutionThreadEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution.Thread value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(NAME_DESCRIPTOR, value.getName());
            ctx.add(IMPORTANCE_DESCRIPTOR, value.getImportance());
            ctx.add(FRAMES_DESCRIPTOR, value.getFrames());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution.Thread.Frame> {
        static final CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder();
        private static final FieldDescriptor PC_DESCRIPTOR = FieldDescriptor.of("pc");
        private static final FieldDescriptor SYMBOL_DESCRIPTOR = FieldDescriptor.of("symbol");
        private static final FieldDescriptor FILE_DESCRIPTOR = FieldDescriptor.of(UriUtil.LOCAL_FILE_SCHEME);
        private static final FieldDescriptor OFFSET_DESCRIPTOR = FieldDescriptor.of("offset");
        private static final FieldDescriptor IMPORTANCE_DESCRIPTOR = FieldDescriptor.of("importance");

        private CrashlyticsReportSessionEventApplicationExecutionThreadFrameEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution.Thread.Frame value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(PC_DESCRIPTOR, value.getPc());
            ctx.add(SYMBOL_DESCRIPTOR, value.getSymbol());
            ctx.add(FILE_DESCRIPTOR, value.getFile());
            ctx.add(OFFSET_DESCRIPTOR, value.getOffset());
            ctx.add(IMPORTANCE_DESCRIPTOR, value.getImportance());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution.Exception> {
        static final CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder();
        private static final FieldDescriptor TYPE_DESCRIPTOR = FieldDescriptor.of("type");
        private static final FieldDescriptor REASON_DESCRIPTOR = FieldDescriptor.of("reason");
        private static final FieldDescriptor FRAMES_DESCRIPTOR = FieldDescriptor.of("frames");
        private static final FieldDescriptor CAUSEDBY_DESCRIPTOR = FieldDescriptor.of("causedBy");
        private static final FieldDescriptor OVERFLOWCOUNT_DESCRIPTOR = FieldDescriptor.of("overflowCount");

        private CrashlyticsReportSessionEventApplicationExecutionExceptionEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution.Exception value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(TYPE_DESCRIPTOR, value.getType());
            ctx.add(REASON_DESCRIPTOR, value.getReason());
            ctx.add(FRAMES_DESCRIPTOR, value.getFrames());
            ctx.add(CAUSEDBY_DESCRIPTOR, value.getCausedBy());
            ctx.add(OVERFLOWCOUNT_DESCRIPTOR, value.getOverflowCount());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportApplicationExitInfoEncoder implements ObjectEncoder<CrashlyticsReport.ApplicationExitInfo> {
        static final CrashlyticsReportApplicationExitInfoEncoder INSTANCE = new CrashlyticsReportApplicationExitInfoEncoder();
        private static final FieldDescriptor PID_DESCRIPTOR = FieldDescriptor.of("pid");
        private static final FieldDescriptor PROCESSNAME_DESCRIPTOR = FieldDescriptor.of("processName");
        private static final FieldDescriptor REASONCODE_DESCRIPTOR = FieldDescriptor.of("reasonCode");
        private static final FieldDescriptor IMPORTANCE_DESCRIPTOR = FieldDescriptor.of("importance");
        private static final FieldDescriptor PSS_DESCRIPTOR = FieldDescriptor.of("pss");
        private static final FieldDescriptor RSS_DESCRIPTOR = FieldDescriptor.of("rss");
        private static final FieldDescriptor TIMESTAMP_DESCRIPTOR = FieldDescriptor.of("timestamp");
        private static final FieldDescriptor TRACEFILE_DESCRIPTOR = FieldDescriptor.of("traceFile");

        private CrashlyticsReportApplicationExitInfoEncoder() {
        }

        public void encode(CrashlyticsReport.ApplicationExitInfo value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(PID_DESCRIPTOR, value.getPid());
            ctx.add(PROCESSNAME_DESCRIPTOR, value.getProcessName());
            ctx.add(REASONCODE_DESCRIPTOR, value.getReasonCode());
            ctx.add(IMPORTANCE_DESCRIPTOR, value.getImportance());
            ctx.add(PSS_DESCRIPTOR, value.getPss());
            ctx.add(RSS_DESCRIPTOR, value.getRss());
            ctx.add(TIMESTAMP_DESCRIPTOR, value.getTimestamp());
            ctx.add(TRACEFILE_DESCRIPTOR, value.getTraceFile());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionSignalEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution.Signal> {
        static final CrashlyticsReportSessionEventApplicationExecutionSignalEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionSignalEncoder();
        private static final FieldDescriptor NAME_DESCRIPTOR = FieldDescriptor.of(AppMeasurementSdk.ConditionalUserProperty.NAME);
        private static final FieldDescriptor CODE_DESCRIPTOR = FieldDescriptor.of("code");
        private static final FieldDescriptor ADDRESS_DESCRIPTOR = FieldDescriptor.of("address");

        private CrashlyticsReportSessionEventApplicationExecutionSignalEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution.Signal value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(NAME_DESCRIPTOR, value.getName());
            ctx.add(CODE_DESCRIPTOR, value.getCode());
            ctx.add(ADDRESS_DESCRIPTOR, value.getAddress());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Application.Execution.BinaryImage> {
        static final CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder INSTANCE = new CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder();
        private static final FieldDescriptor BASEADDRESS_DESCRIPTOR = FieldDescriptor.of("baseAddress");
        private static final FieldDescriptor SIZE_DESCRIPTOR = FieldDescriptor.of("size");
        private static final FieldDescriptor NAME_DESCRIPTOR = FieldDescriptor.of(AppMeasurementSdk.ConditionalUserProperty.NAME);
        private static final FieldDescriptor UUID_DESCRIPTOR = FieldDescriptor.of("uuid");

        private CrashlyticsReportSessionEventApplicationExecutionBinaryImageEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Application.Execution.BinaryImage value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(BASEADDRESS_DESCRIPTOR, value.getBaseAddress());
            ctx.add(SIZE_DESCRIPTOR, value.getSize());
            ctx.add(NAME_DESCRIPTOR, value.getName());
            ctx.add(UUID_DESCRIPTOR, value.getUuidUtf8Bytes());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportCustomAttributeEncoder implements ObjectEncoder<CrashlyticsReport.CustomAttribute> {
        static final CrashlyticsReportCustomAttributeEncoder INSTANCE = new CrashlyticsReportCustomAttributeEncoder();
        private static final FieldDescriptor KEY_DESCRIPTOR = FieldDescriptor.of("key");
        private static final FieldDescriptor VALUE_DESCRIPTOR = FieldDescriptor.of("value");

        private CrashlyticsReportCustomAttributeEncoder() {
        }

        public void encode(CrashlyticsReport.CustomAttribute value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(KEY_DESCRIPTOR, value.getKey());
            ctx.add(VALUE_DESCRIPTOR, value.getValue());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventDeviceEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Device> {
        static final CrashlyticsReportSessionEventDeviceEncoder INSTANCE = new CrashlyticsReportSessionEventDeviceEncoder();
        private static final FieldDescriptor BATTERYLEVEL_DESCRIPTOR = FieldDescriptor.of("batteryLevel");
        private static final FieldDescriptor BATTERYVELOCITY_DESCRIPTOR = FieldDescriptor.of("batteryVelocity");
        private static final FieldDescriptor PROXIMITYON_DESCRIPTOR = FieldDescriptor.of("proximityOn");
        private static final FieldDescriptor ORIENTATION_DESCRIPTOR = FieldDescriptor.of("orientation");
        private static final FieldDescriptor RAMUSED_DESCRIPTOR = FieldDescriptor.of("ramUsed");
        private static final FieldDescriptor DISKUSED_DESCRIPTOR = FieldDescriptor.of("diskUsed");

        private CrashlyticsReportSessionEventDeviceEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Device value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(BATTERYLEVEL_DESCRIPTOR, value.getBatteryLevel());
            ctx.add(BATTERYVELOCITY_DESCRIPTOR, value.getBatteryVelocity());
            ctx.add(PROXIMITYON_DESCRIPTOR, value.isProximityOn());
            ctx.add(ORIENTATION_DESCRIPTOR, value.getOrientation());
            ctx.add(RAMUSED_DESCRIPTOR, value.getRamUsed());
            ctx.add(DISKUSED_DESCRIPTOR, value.getDiskUsed());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportSessionEventLogEncoder implements ObjectEncoder<CrashlyticsReport.Session.Event.Log> {
        static final CrashlyticsReportSessionEventLogEncoder INSTANCE = new CrashlyticsReportSessionEventLogEncoder();
        private static final FieldDescriptor CONTENT_DESCRIPTOR = FieldDescriptor.of("content");

        private CrashlyticsReportSessionEventLogEncoder() {
        }

        public void encode(CrashlyticsReport.Session.Event.Log value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(CONTENT_DESCRIPTOR, value.getContent());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportFilesPayloadEncoder implements ObjectEncoder<CrashlyticsReport.FilesPayload> {
        static final CrashlyticsReportFilesPayloadEncoder INSTANCE = new CrashlyticsReportFilesPayloadEncoder();
        private static final FieldDescriptor FILES_DESCRIPTOR = FieldDescriptor.of("files");
        private static final FieldDescriptor ORGID_DESCRIPTOR = FieldDescriptor.of("orgId");

        private CrashlyticsReportFilesPayloadEncoder() {
        }

        public void encode(CrashlyticsReport.FilesPayload value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(FILES_DESCRIPTOR, value.getFiles());
            ctx.add(ORGID_DESCRIPTOR, value.getOrgId());
        }
    }

    /* loaded from: classes3.dex */
    private static final class CrashlyticsReportFilesPayloadFileEncoder implements ObjectEncoder<CrashlyticsReport.FilesPayload.File> {
        static final CrashlyticsReportFilesPayloadFileEncoder INSTANCE = new CrashlyticsReportFilesPayloadFileEncoder();
        private static final FieldDescriptor FILENAME_DESCRIPTOR = FieldDescriptor.of("filename");
        private static final FieldDescriptor CONTENTS_DESCRIPTOR = FieldDescriptor.of("contents");

        private CrashlyticsReportFilesPayloadFileEncoder() {
        }

        public void encode(CrashlyticsReport.FilesPayload.File value, ObjectEncoderContext ctx) throws IOException {
            ctx.add(FILENAME_DESCRIPTOR, value.getFilename());
            ctx.add(CONTENTS_DESCRIPTOR, value.getContents());
        }
    }
}
