package com.google.firebase.installations;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.heartbeatinfo.HeartBeatInfo;
import com.google.firebase.inject.Provider;
import com.google.firebase.installations.FirebaseInstallationsException;
import com.google.firebase.installations.internal.FidListener;
import com.google.firebase.installations.internal.FidListenerHandle;
import com.google.firebase.installations.local.IidStore;
import com.google.firebase.installations.local.PersistedInstallation;
import com.google.firebase.installations.local.PersistedInstallationEntry;
import com.google.firebase.installations.remote.FirebaseInstallationServiceClient;
import com.google.firebase.installations.remote.InstallationResponse;
import com.google.firebase.installations.remote.TokenResult;
import com.google.firebase.platforminfo.UserAgentPublisher;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes3.dex */
public class FirebaseInstallations implements FirebaseInstallationsApi {
    private static final String API_KEY_VALIDATION_MSG;
    private static final String APP_ID_VALIDATION_MSG;
    private static final String AUTH_ERROR_MSG;
    private static final String CHIME_FIREBASE_APP_NAME;
    private static final int CORE_POOL_SIZE;
    private static final long KEEP_ALIVE_TIME_IN_SECONDS;
    private static final String LOCKFILE_NAME_GENERATE_FID;
    private static final int MAXIMUM_POOL_SIZE;
    private static final String PROJECT_ID_VALIDATION_MSG;
    private final ExecutorService backgroundExecutor;
    private String cachedFid;
    private final RandomFidGenerator fidGenerator;
    private Set<FidListener> fidListeners;
    private final FirebaseApp firebaseApp;
    private final IidStore iidStore;
    private final List<StateListener> listeners;
    private final Object lock;
    private final ExecutorService networkExecutor;
    private final PersistedInstallation persistedInstallation;
    private final FirebaseInstallationServiceClient serviceClient;
    private final Utils utils;
    private static final Object lockGenerateFid = new Object();
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() { // from class: com.google.firebase.installations.FirebaseInstallations.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new Thread(r, String.format("firebase-installations-executor-%d", Integer.valueOf(this.mCount.getAndIncrement())));
        }
    };

    public FirebaseInstallations(FirebaseApp firebaseApp, Provider<UserAgentPublisher> publisher, Provider<HeartBeatInfo> heartbeatInfo) {
        this(new ThreadPoolExecutor(0, 1, (long) KEEP_ALIVE_TIME_IN_SECONDS, TimeUnit.SECONDS, new LinkedBlockingQueue(), THREAD_FACTORY), firebaseApp, new FirebaseInstallationServiceClient(firebaseApp.getApplicationContext(), publisher, heartbeatInfo), new PersistedInstallation(firebaseApp), Utils.getInstance(), new IidStore(firebaseApp), new RandomFidGenerator());
    }

    FirebaseInstallations(ExecutorService backgroundExecutor, FirebaseApp firebaseApp, FirebaseInstallationServiceClient serviceClient, PersistedInstallation persistedInstallation, Utils utils, IidStore iidStore, RandomFidGenerator fidGenerator) {
        this.lock = new Object();
        this.fidListeners = new HashSet();
        this.listeners = new ArrayList();
        this.firebaseApp = firebaseApp;
        this.serviceClient = serviceClient;
        this.persistedInstallation = persistedInstallation;
        this.utils = utils;
        this.iidStore = iidStore;
        this.fidGenerator = fidGenerator;
        this.backgroundExecutor = backgroundExecutor;
        this.networkExecutor = new ThreadPoolExecutor(0, 1, (long) KEEP_ALIVE_TIME_IN_SECONDS, TimeUnit.SECONDS, new LinkedBlockingQueue(), THREAD_FACTORY);
    }

    private void preConditionChecks() {
        Preconditions.checkNotEmpty(getApplicationId(), APP_ID_VALIDATION_MSG);
        Preconditions.checkNotEmpty(getProjectIdentifier(), PROJECT_ID_VALIDATION_MSG);
        Preconditions.checkNotEmpty(getApiKey(), API_KEY_VALIDATION_MSG);
        Preconditions.checkArgument(Utils.isValidAppIdFormat(getApplicationId()), APP_ID_VALIDATION_MSG);
        Preconditions.checkArgument(Utils.isValidApiKeyFormat(getApiKey()), API_KEY_VALIDATION_MSG);
    }

    String getProjectIdentifier() {
        return this.firebaseApp.getOptions().getProjectId();
    }

    public static FirebaseInstallations getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    public static FirebaseInstallations getInstance(FirebaseApp app) {
        Preconditions.checkArgument(app != null, "Null is not a valid value of FirebaseApp.");
        return (FirebaseInstallations) app.get(FirebaseInstallationsApi.class);
    }

    String getApplicationId() {
        return this.firebaseApp.getOptions().getApplicationId();
    }

    String getApiKey() {
        return this.firebaseApp.getOptions().getApiKey();
    }

    String getName() {
        return this.firebaseApp.getName();
    }

    @Override // com.google.firebase.installations.FirebaseInstallationsApi
    public Task<String> getId() {
        preConditionChecks();
        String fid = getCacheFid();
        if (fid != null) {
            return Tasks.forResult(fid);
        }
        Task<String> task = addGetIdListener();
        this.backgroundExecutor.execute(new Runnable() { // from class: com.google.firebase.installations.-$$Lambda$FirebaseInstallations$pfXbx9QCGkMrm_6XDXgf1nv3q8c
            @Override // java.lang.Runnable
            public final void run() {
                FirebaseInstallations.this.lambda$getId$0$FirebaseInstallations();
            }
        });
        return task;
    }

    public /* synthetic */ void lambda$getId$0$FirebaseInstallations() {
        lambda$getToken$1$FirebaseInstallations(false);
    }

    @Override // com.google.firebase.installations.FirebaseInstallationsApi
    public Task<InstallationTokenResult> getToken(boolean forceRefresh) {
        preConditionChecks();
        Task<InstallationTokenResult> task = addGetAuthTokenListener();
        this.backgroundExecutor.execute(new Runnable(forceRefresh) { // from class: com.google.firebase.installations.-$$Lambda$FirebaseInstallations$o2WtyREmoY46ZHUB1BUpKOvhOaw
            private final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FirebaseInstallations.this.lambda$getToken$1$FirebaseInstallations(this.f$1);
            }
        });
        return task;
    }

    @Override // com.google.firebase.installations.FirebaseInstallationsApi
    public Task<Void> delete() {
        return Tasks.call(this.backgroundExecutor, new Callable() { // from class: com.google.firebase.installations.-$$Lambda$FirebaseInstallations$0uAknVZe1E3LLvzafpP-F998ICg
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return FirebaseInstallations.this.deleteFirebaseInstallationId();
            }
        });
    }

    @Override // com.google.firebase.installations.FirebaseInstallationsApi
    public synchronized FidListenerHandle registerFidListener(final FidListener listener) {
        this.fidListeners.add(listener);
        return new FidListenerHandle() { // from class: com.google.firebase.installations.FirebaseInstallations.2
            @Override // com.google.firebase.installations.internal.FidListenerHandle
            public void unregister() {
                synchronized (FirebaseInstallations.this) {
                    FirebaseInstallations.this.fidListeners.remove(listener);
                }
            }
        };
    }

    private Task<String> addGetIdListener() {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        addStateListeners(new GetIdListener(taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    private Task<InstallationTokenResult> addGetAuthTokenListener() {
        TaskCompletionSource<InstallationTokenResult> taskCompletionSource = new TaskCompletionSource<>();
        addStateListeners(new GetAuthTokenListener(this.utils, taskCompletionSource));
        return taskCompletionSource.getTask();
    }

    private void addStateListeners(StateListener l) {
        synchronized (this.lock) {
            this.listeners.add(l);
        }
    }

    private void triggerOnStateReached(PersistedInstallationEntry persistedInstallationEntry) {
        synchronized (this.lock) {
            Iterator<StateListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                if (it.next().onStateReached(persistedInstallationEntry)) {
                    it.remove();
                }
            }
        }
    }

    private void triggerOnException(Exception exception) {
        synchronized (this.lock) {
            Iterator<StateListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                if (it.next().onException(exception)) {
                    it.remove();
                }
            }
        }
    }

    private synchronized void updateCacheFid(String cachedFid) {
        this.cachedFid = cachedFid;
    }

    private synchronized String getCacheFid() {
        return this.cachedFid;
    }

    /* renamed from: doRegistrationOrRefresh */
    public final void lambda$getToken$1$FirebaseInstallations(boolean forceRefresh) {
        PersistedInstallationEntry prefs = getPrefsWithGeneratedIdMultiProcessSafe();
        if (forceRefresh) {
            prefs = prefs.withClearedAuthToken();
        }
        triggerOnStateReached(prefs);
        this.networkExecutor.execute(new Runnable(forceRefresh) { // from class: com.google.firebase.installations.-$$Lambda$FirebaseInstallations$SC1awUtshakVcZWKWh8TCfIkd4g
            private final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                FirebaseInstallations.this.lambda$doRegistrationOrRefresh$2$FirebaseInstallations(this.f$1);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0034  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x004c  */
    /* renamed from: doNetworkCallIfNecessary */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public void lambda$doRegistrationOrRefresh$2$FirebaseInstallations(boolean forceRefresh) {
        PersistedInstallationEntry updatedPrefs;
        PersistedInstallationEntry prefs = getMultiProcessSafePrefs();
        try {
            if (!prefs.isErrored() && !prefs.isUnregistered()) {
                if (!forceRefresh && !this.utils.isAuthTokenExpired(prefs)) {
                    return;
                }
                updatedPrefs = fetchAuthTokenFromServer(prefs);
                insertOrUpdatePrefs(updatedPrefs);
                updateFidListener(prefs, updatedPrefs);
                if (updatedPrefs.isRegistered()) {
                    updateCacheFid(updatedPrefs.getFirebaseInstallationId());
                }
                if (!updatedPrefs.isErrored()) {
                    triggerOnException(new FirebaseInstallationsException(FirebaseInstallationsException.Status.BAD_CONFIG));
                    return;
                } else if (updatedPrefs.isNotGenerated()) {
                    triggerOnException(new IOException(AUTH_ERROR_MSG));
                    return;
                } else {
                    triggerOnStateReached(updatedPrefs);
                    return;
                }
            }
            updatedPrefs = registerFidWithServer(prefs);
            insertOrUpdatePrefs(updatedPrefs);
            updateFidListener(prefs, updatedPrefs);
            if (updatedPrefs.isRegistered()) {
            }
            if (!updatedPrefs.isErrored()) {
            }
        } catch (FirebaseInstallationsException e) {
            triggerOnException(e);
        }
    }

    private synchronized void updateFidListener(PersistedInstallationEntry prefs, PersistedInstallationEntry updatedPrefs) {
        if (this.fidListeners.size() != 0 && !prefs.getFirebaseInstallationId().equals(updatedPrefs.getFirebaseInstallationId())) {
            for (FidListener listener : this.fidListeners) {
                listener.onFidChanged(updatedPrefs.getFirebaseInstallationId());
            }
        }
    }

    private void insertOrUpdatePrefs(PersistedInstallationEntry prefs) {
        synchronized (lockGenerateFid) {
            CrossProcessLock lock = CrossProcessLock.acquire(this.firebaseApp.getApplicationContext(), LOCKFILE_NAME_GENERATE_FID);
            this.persistedInstallation.insertOrUpdatePersistedInstallationEntry(prefs);
            if (lock != null) {
                lock.releaseAndClose();
            }
        }
    }

    private PersistedInstallationEntry getPrefsWithGeneratedIdMultiProcessSafe() {
        PersistedInstallationEntry prefs;
        synchronized (lockGenerateFid) {
            CrossProcessLock lock = CrossProcessLock.acquire(this.firebaseApp.getApplicationContext(), LOCKFILE_NAME_GENERATE_FID);
            prefs = this.persistedInstallation.readPersistedInstallationEntryValue();
            if (prefs.isNotGenerated()) {
                prefs = this.persistedInstallation.insertOrUpdatePersistedInstallationEntry(prefs.withUnregisteredFid(readExistingIidOrCreateFid(prefs)));
            }
            if (lock != null) {
                lock.releaseAndClose();
            }
        }
        return prefs;
    }

    private String readExistingIidOrCreateFid(PersistedInstallationEntry prefs) {
        if ((!this.firebaseApp.getName().equals(CHIME_FIREBASE_APP_NAME) && !this.firebaseApp.isDefaultApp()) || !prefs.shouldAttemptMigration()) {
            return this.fidGenerator.createRandomFid();
        }
        String fid = this.iidStore.readIid();
        if (TextUtils.isEmpty(fid)) {
            return this.fidGenerator.createRandomFid();
        }
        return fid;
    }

    private PersistedInstallationEntry registerFidWithServer(PersistedInstallationEntry prefs) throws FirebaseInstallationsException {
        String iidToken = null;
        if (prefs.getFirebaseInstallationId() != null && prefs.getFirebaseInstallationId().length() == 11) {
            iidToken = this.iidStore.readToken();
        }
        InstallationResponse response = this.serviceClient.createFirebaseInstallation(getApiKey(), prefs.getFirebaseInstallationId(), getProjectIdentifier(), getApplicationId(), iidToken);
        int i = AnonymousClass3.$SwitchMap$com$google$firebase$installations$remote$InstallationResponse$ResponseCode[response.getResponseCode().ordinal()];
        if (i == 1) {
            return prefs.withRegisteredFid(response.getFid(), response.getRefreshToken(), this.utils.currentTimeInSecs(), response.getAuthToken().getToken(), response.getAuthToken().getTokenExpirationTimestamp());
        }
        if (i == 2) {
            return prefs.withFisError("BAD CONFIG");
        }
        throw new FirebaseInstallationsException("Firebase Installations Service is unavailable. Please try again later.", FirebaseInstallationsException.Status.UNAVAILABLE);
    }

    private PersistedInstallationEntry fetchAuthTokenFromServer(PersistedInstallationEntry prefs) throws FirebaseInstallationsException {
        TokenResult tokenResult = this.serviceClient.generateAuthToken(getApiKey(), prefs.getFirebaseInstallationId(), getProjectIdentifier(), prefs.getRefreshToken());
        int i = AnonymousClass3.$SwitchMap$com$google$firebase$installations$remote$TokenResult$ResponseCode[tokenResult.getResponseCode().ordinal()];
        if (i == 1) {
            return prefs.withAuthToken(tokenResult.getToken(), tokenResult.getTokenExpirationTimestamp(), this.utils.currentTimeInSecs());
        }
        if (i == 2) {
            return prefs.withFisError("BAD CONFIG");
        }
        if (i == 3) {
            updateCacheFid(null);
            return prefs.withNoGeneratedFid();
        }
        throw new FirebaseInstallationsException("Firebase Installations Service is unavailable. Please try again later.", FirebaseInstallationsException.Status.UNAVAILABLE);
    }

    /* renamed from: com.google.firebase.installations.FirebaseInstallations$3 */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$google$firebase$installations$remote$InstallationResponse$ResponseCode;
        static final /* synthetic */ int[] $SwitchMap$com$google$firebase$installations$remote$TokenResult$ResponseCode = new int[TokenResult.ResponseCode.values().length];

        static {
            try {
                $SwitchMap$com$google$firebase$installations$remote$TokenResult$ResponseCode[TokenResult.ResponseCode.OK.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$firebase$installations$remote$TokenResult$ResponseCode[TokenResult.ResponseCode.BAD_CONFIG.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$google$firebase$installations$remote$TokenResult$ResponseCode[TokenResult.ResponseCode.AUTH_ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SwitchMap$com$google$firebase$installations$remote$InstallationResponse$ResponseCode = new int[InstallationResponse.ResponseCode.values().length];
            try {
                $SwitchMap$com$google$firebase$installations$remote$InstallationResponse$ResponseCode[InstallationResponse.ResponseCode.OK.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$google$firebase$installations$remote$InstallationResponse$ResponseCode[InstallationResponse.ResponseCode.BAD_CONFIG.ordinal()] = 2;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public Void deleteFirebaseInstallationId() throws FirebaseInstallationsException {
        updateCacheFid(null);
        PersistedInstallationEntry entry = getMultiProcessSafePrefs();
        if (entry.isRegistered()) {
            this.serviceClient.deleteFirebaseInstallation(getApiKey(), entry.getFirebaseInstallationId(), getProjectIdentifier(), entry.getRefreshToken());
        }
        insertOrUpdatePrefs(entry.withNoGeneratedFid());
        return null;
    }

    private PersistedInstallationEntry getMultiProcessSafePrefs() {
        PersistedInstallationEntry prefs;
        synchronized (lockGenerateFid) {
            CrossProcessLock lock = CrossProcessLock.acquire(this.firebaseApp.getApplicationContext(), LOCKFILE_NAME_GENERATE_FID);
            prefs = this.persistedInstallation.readPersistedInstallationEntryValue();
            if (lock != null) {
                lock.releaseAndClose();
            }
        }
        return prefs;
    }
}
