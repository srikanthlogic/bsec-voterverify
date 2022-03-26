package com.squareup.picasso;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.squareup.picasso.NetworkRequestHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
/* loaded from: classes3.dex */
public class Dispatcher {
    static final int AIRPLANE_MODE_CHANGE;
    private static final int AIRPLANE_MODE_OFF;
    private static final int AIRPLANE_MODE_ON;
    private static final int BATCH_DELAY;
    private static final String DISPATCHER_THREAD_NAME;
    static final int HUNTER_BATCH_COMPLETE;
    static final int HUNTER_COMPLETE;
    static final int HUNTER_DECODE_FAILED;
    static final int HUNTER_DELAY_NEXT_BATCH;
    static final int HUNTER_RETRY;
    static final int NETWORK_STATE_CHANGE;
    static final int REQUEST_BATCH_RESUME;
    static final int REQUEST_CANCEL;
    static final int REQUEST_GCED;
    static final int REQUEST_SUBMIT;
    private static final int RETRY_DELAY;
    static final int TAG_PAUSE;
    static final int TAG_RESUME;
    boolean airplaneMode;
    final Cache cache;
    final Context context;
    final Downloader downloader;
    final Handler mainThreadHandler;
    final boolean scansNetworkChanges;
    final ExecutorService service;
    final Stats stats;
    final DispatcherThread dispatcherThread = new DispatcherThread();
    final Map<String, BitmapHunter> hunterMap = new LinkedHashMap();
    final Map<Object, Action> failedActions = new WeakHashMap();
    final Map<Object, Action> pausedActions = new WeakHashMap();
    final Set<Object> pausedTags = new LinkedHashSet();
    final Handler handler = new DispatcherHandler(this.dispatcherThread.getLooper(), this);
    final List<BitmapHunter> batch = new ArrayList(4);
    final NetworkBroadcastReceiver receiver = new NetworkBroadcastReceiver(this);

    public Dispatcher(Context context, ExecutorService service, Handler mainThreadHandler, Downloader downloader, Cache cache, Stats stats) {
        this.dispatcherThread.start();
        Utils.flushStackLocalLeaks(this.dispatcherThread.getLooper());
        this.context = context;
        this.service = service;
        this.downloader = downloader;
        this.mainThreadHandler = mainThreadHandler;
        this.cache = cache;
        this.stats = stats;
        this.airplaneMode = Utils.isAirplaneModeOn(this.context);
        this.scansNetworkChanges = Utils.hasPermission(context, "android.permission.ACCESS_NETWORK_STATE");
        this.receiver.register();
    }

    public void shutdown() {
        ExecutorService executorService = this.service;
        if (executorService instanceof PicassoExecutorService) {
            executorService.shutdown();
        }
        this.downloader.shutdown();
        this.dispatcherThread.quit();
        Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.Dispatcher.1
            @Override // java.lang.Runnable
            public void run() {
                Dispatcher.this.receiver.unregister();
            }
        });
    }

    public void dispatchSubmit(Action action) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(1, action));
    }

    public void dispatchCancel(Action action) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(2, action));
    }

    public void dispatchPauseTag(Object tag) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(11, tag));
    }

    public void dispatchResumeTag(Object tag) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(12, tag));
    }

    public void dispatchComplete(BitmapHunter hunter) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(4, hunter));
    }

    public void dispatchRetry(BitmapHunter hunter) {
        Handler handler = this.handler;
        handler.sendMessageDelayed(handler.obtainMessage(5, hunter), 500);
    }

    public void dispatchFailed(BitmapHunter hunter) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(6, hunter));
    }

    void dispatchNetworkStateChange(NetworkInfo info) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(9, info));
    }

    void dispatchAirplaneModeChange(boolean airplaneMode) {
        Handler handler = this.handler;
        handler.sendMessage(handler.obtainMessage(10, airplaneMode ? 1 : 0, 0));
    }

    void performSubmit(Action action) {
        performSubmit(action, true);
    }

    void performSubmit(Action action, boolean dismissFailed) {
        if (this.pausedTags.contains(action.getTag())) {
            this.pausedActions.put(action.getTarget(), action);
            if (action.getPicasso().loggingEnabled) {
                String logId = action.request.logId();
                Utils.log(DISPATCHER_THREAD_NAME, "paused", logId, "because tag '" + action.getTag() + "' is paused");
                return;
            }
            return;
        }
        BitmapHunter hunter = this.hunterMap.get(action.getKey());
        if (hunter != null) {
            hunter.attach(action);
        } else if (!this.service.isShutdown()) {
            BitmapHunter hunter2 = BitmapHunter.forRequest(action.getPicasso(), this, this.cache, this.stats, action);
            hunter2.future = this.service.submit(hunter2);
            this.hunterMap.put(action.getKey(), hunter2);
            if (dismissFailed) {
                this.failedActions.remove(action.getTarget());
            }
            if (action.getPicasso().loggingEnabled) {
                Utils.log(DISPATCHER_THREAD_NAME, "enqueued", action.request.logId());
            }
        } else if (action.getPicasso().loggingEnabled) {
            Utils.log(DISPATCHER_THREAD_NAME, "ignored", action.request.logId(), "because shut down");
        }
    }

    void performCancel(Action action) {
        String key = action.getKey();
        BitmapHunter hunter = this.hunterMap.get(key);
        if (hunter != null) {
            hunter.detach(action);
            if (hunter.cancel()) {
                this.hunterMap.remove(key);
                if (action.getPicasso().loggingEnabled) {
                    Utils.log(DISPATCHER_THREAD_NAME, "canceled", action.getRequest().logId());
                }
            }
        }
        if (this.pausedTags.contains(action.getTag())) {
            this.pausedActions.remove(action.getTarget());
            if (action.getPicasso().loggingEnabled) {
                Utils.log(DISPATCHER_THREAD_NAME, "canceled", action.getRequest().logId(), "because paused request got canceled");
            }
        }
        Action remove = this.failedActions.remove(action.getTarget());
        if (remove != null && remove.getPicasso().loggingEnabled) {
            Utils.log(DISPATCHER_THREAD_NAME, "canceled", remove.getRequest().logId(), "from replaying");
        }
    }

    void performPauseTag(Object tag) {
        if (this.pausedTags.add(tag)) {
            Iterator<BitmapHunter> it = this.hunterMap.values().iterator();
            while (it.hasNext()) {
                BitmapHunter hunter = it.next();
                boolean loggingEnabled = hunter.getPicasso().loggingEnabled;
                Action single = hunter.getAction();
                List<Action> joined = hunter.getActions();
                boolean hasMultiple = joined != null && !joined.isEmpty();
                if (single != null || hasMultiple) {
                    if (single != null && single.getTag().equals(tag)) {
                        hunter.detach(single);
                        this.pausedActions.put(single.getTarget(), single);
                        if (loggingEnabled) {
                            Utils.log(DISPATCHER_THREAD_NAME, "paused", single.request.logId(), "because tag '" + tag + "' was paused");
                        }
                    }
                    if (hasMultiple) {
                        for (int i = joined.size() - 1; i >= 0; i--) {
                            Action action = joined.get(i);
                            if (action.getTag().equals(tag)) {
                                hunter.detach(action);
                                this.pausedActions.put(action.getTarget(), action);
                                if (loggingEnabled) {
                                    Utils.log(DISPATCHER_THREAD_NAME, "paused", action.request.logId(), "because tag '" + tag + "' was paused");
                                }
                            }
                        }
                    }
                    if (hunter.cancel()) {
                        it.remove();
                        if (loggingEnabled) {
                            Utils.log(DISPATCHER_THREAD_NAME, "canceled", Utils.getLogIdsForHunter(hunter), "all actions paused");
                        }
                    }
                }
            }
        }
    }

    void performResumeTag(Object tag) {
        if (this.pausedTags.remove(tag)) {
            List<Action> batch = null;
            Iterator<Action> i = this.pausedActions.values().iterator();
            while (i.hasNext()) {
                Action action = i.next();
                if (action.getTag().equals(tag)) {
                    if (batch == null) {
                        batch = new ArrayList<>();
                    }
                    batch.add(action);
                    i.remove();
                }
            }
            if (batch != null) {
                Handler handler = this.mainThreadHandler;
                handler.sendMessage(handler.obtainMessage(13, batch));
            }
        }
    }

    void performRetry(BitmapHunter hunter) {
        if (!hunter.isCancelled()) {
            boolean willReplay = false;
            if (this.service.isShutdown()) {
                performError(hunter, false);
                return;
            }
            NetworkInfo networkInfo = null;
            if (this.scansNetworkChanges) {
                networkInfo = ((ConnectivityManager) Utils.getService(this.context, "connectivity")).getActiveNetworkInfo();
            }
            if (hunter.shouldRetry(this.airplaneMode, networkInfo)) {
                if (hunter.getPicasso().loggingEnabled) {
                    Utils.log(DISPATCHER_THREAD_NAME, "retrying", Utils.getLogIdsForHunter(hunter));
                }
                if (hunter.getException() instanceof NetworkRequestHandler.ContentLengthException) {
                    hunter.networkPolicy |= NetworkPolicy.NO_CACHE.index;
                }
                hunter.future = this.service.submit(hunter);
                return;
            }
            if (this.scansNetworkChanges && hunter.supportsReplay()) {
                willReplay = true;
            }
            performError(hunter, willReplay);
            if (willReplay) {
                markForReplay(hunter);
            }
        }
    }

    void performComplete(BitmapHunter hunter) {
        if (MemoryPolicy.shouldWriteToMemoryCache(hunter.getMemoryPolicy())) {
            this.cache.set(hunter.getKey(), hunter.getResult());
        }
        this.hunterMap.remove(hunter.getKey());
        batch(hunter);
        if (hunter.getPicasso().loggingEnabled) {
            Utils.log(DISPATCHER_THREAD_NAME, "batched", Utils.getLogIdsForHunter(hunter), "for completion");
        }
    }

    void performBatchComplete() {
        List<BitmapHunter> copy = new ArrayList<>(this.batch);
        this.batch.clear();
        Handler handler = this.mainThreadHandler;
        handler.sendMessage(handler.obtainMessage(8, copy));
        logBatch(copy);
    }

    void performError(BitmapHunter hunter, boolean willReplay) {
        if (hunter.getPicasso().loggingEnabled) {
            String logIdsForHunter = Utils.getLogIdsForHunter(hunter);
            StringBuilder sb = new StringBuilder();
            sb.append("for error");
            sb.append(willReplay ? " (will replay)" : "");
            Utils.log(DISPATCHER_THREAD_NAME, "batched", logIdsForHunter, sb.toString());
        }
        this.hunterMap.remove(hunter.getKey());
        batch(hunter);
    }

    void performAirplaneModeChange(boolean airplaneMode) {
        this.airplaneMode = airplaneMode;
    }

    void performNetworkStateChange(NetworkInfo info) {
        ExecutorService executorService = this.service;
        if (executorService instanceof PicassoExecutorService) {
            ((PicassoExecutorService) executorService).adjustThreadCount(info);
        }
        if (info != null && info.isConnected()) {
            flushFailedActions();
        }
    }

    private void flushFailedActions() {
        if (!this.failedActions.isEmpty()) {
            Iterator<Action> iterator = this.failedActions.values().iterator();
            while (iterator.hasNext()) {
                Action action = iterator.next();
                iterator.remove();
                if (action.getPicasso().loggingEnabled) {
                    Utils.log(DISPATCHER_THREAD_NAME, "replaying", action.getRequest().logId());
                }
                performSubmit(action, false);
            }
        }
    }

    private void markForReplay(BitmapHunter hunter) {
        Action action = hunter.getAction();
        if (action != null) {
            markForReplay(action);
        }
        List<Action> joined = hunter.getActions();
        if (joined != null) {
            int n = joined.size();
            for (int i = 0; i < n; i++) {
                markForReplay(joined.get(i));
            }
        }
    }

    private void markForReplay(Action action) {
        Object target = action.getTarget();
        if (target != null) {
            action.willReplay = true;
            this.failedActions.put(target, action);
        }
    }

    private void batch(BitmapHunter hunter) {
        if (!hunter.isCancelled()) {
            if (hunter.result != null) {
                hunter.result.prepareToDraw();
            }
            this.batch.add(hunter);
            if (!this.handler.hasMessages(7)) {
                this.handler.sendEmptyMessageDelayed(7, 200);
            }
        }
    }

    private void logBatch(List<BitmapHunter> copy) {
        if (!(copy == null || copy.isEmpty() || !copy.get(0).getPicasso().loggingEnabled)) {
            StringBuilder builder = new StringBuilder();
            for (BitmapHunter bitmapHunter : copy) {
                if (builder.length() > 0) {
                    builder.append(", ");
                }
                builder.append(Utils.getLogIdsForHunter(bitmapHunter));
            }
            Utils.log(DISPATCHER_THREAD_NAME, "delivered", builder.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DispatcherHandler extends Handler {
        private final Dispatcher dispatcher;

        DispatcherHandler(Looper looper, Dispatcher dispatcher) {
            super(looper);
            this.dispatcher = dispatcher;
        }

        @Override // android.os.Handler
        public void handleMessage(final Message msg) {
            boolean z = false;
            switch (msg.what) {
                case 1:
                    this.dispatcher.performSubmit((Action) msg.obj);
                    return;
                case 2:
                    this.dispatcher.performCancel((Action) msg.obj);
                    return;
                case 3:
                case 8:
                default:
                    Picasso.HANDLER.post(new Runnable() { // from class: com.squareup.picasso.Dispatcher.DispatcherHandler.1
                        @Override // java.lang.Runnable
                        public void run() {
                            throw new AssertionError("Unknown handler message received: " + msg.what);
                        }
                    });
                    return;
                case 4:
                    this.dispatcher.performComplete((BitmapHunter) msg.obj);
                    return;
                case 5:
                    this.dispatcher.performRetry((BitmapHunter) msg.obj);
                    return;
                case 6:
                    this.dispatcher.performError((BitmapHunter) msg.obj, false);
                    return;
                case 7:
                    this.dispatcher.performBatchComplete();
                    return;
                case 9:
                    this.dispatcher.performNetworkStateChange((NetworkInfo) msg.obj);
                    return;
                case 10:
                    Dispatcher dispatcher = this.dispatcher;
                    if (msg.arg1 == 1) {
                        z = true;
                    }
                    dispatcher.performAirplaneModeChange(z);
                    return;
                case 11:
                    this.dispatcher.performPauseTag(msg.obj);
                    return;
                case 12:
                    this.dispatcher.performResumeTag(msg.obj);
                    return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DispatcherThread extends HandlerThread {
        DispatcherThread() {
            super("Picasso-Dispatcher", 10);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class NetworkBroadcastReceiver extends BroadcastReceiver {
        static final String EXTRA_AIRPLANE_STATE;
        private final Dispatcher dispatcher;

        NetworkBroadcastReceiver(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        void register() {
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.AIRPLANE_MODE");
            if (this.dispatcher.scansNetworkChanges) {
                filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            }
            this.dispatcher.context.registerReceiver(this, filter);
        }

        void unregister() {
            this.dispatcher.context.unregisterReceiver(this);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    if (intent.hasExtra(EXTRA_AIRPLANE_STATE)) {
                        this.dispatcher.dispatchAirplaneModeChange(intent.getBooleanExtra(EXTRA_AIRPLANE_STATE, false));
                    }
                } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
                    this.dispatcher.dispatchNetworkStateChange(((ConnectivityManager) Utils.getService(context, "connectivity")).getActiveNetworkInfo());
                }
            }
        }
    }
}
