package kotlinx.coroutines;

import androidx.exifinterface.media.ExifInterface;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.intrinsics.CancellableKt;
import kotlinx.coroutines.intrinsics.UndispatchedKt;
import kotlinx.coroutines.selects.SelectClause0;
import kotlinx.coroutines.selects.SelectInstance;
/* compiled from: JobSupport.kt */
@Deprecated(level = DeprecationLevel.ERROR, message = "This is internal API and may be removed in the future releases")
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000è\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0001\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0017\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u0004:\u0006¤\u0001¥\u0001¦\u0001B\r\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J$\u0010-\u001a\u00020\u00062\u0006\u0010.\u001a\u00020\n2\u0006\u0010/\u001a\u0002002\n\u00101\u001a\u0006\u0012\u0002\b\u000302H\u0002J\u000e\u00103\u001a\u00020\"2\u0006\u00104\u001a\u00020\u0002J\u0015\u00105\u001a\u0004\u0018\u00010\nH\u0080@ø\u0001\u0000¢\u0006\u0004\b6\u00107J\u0013\u00108\u001a\u0004\u0018\u00010\nH\u0082@ø\u0001\u0000¢\u0006\u0002\u00107J\b\u00109\u001a\u00020:H\u0016J\u0012\u00109\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010'H\u0016J\u0012\u0010<\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010=\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0010\u0010>\u001a\u00020\u00062\u0006\u0010;\u001a\u00020'H\u0002J\u0010\u0010?\u001a\u00020\u00062\u0006\u0010;\u001a\u00020'H\u0016J*\u0010@\u001a\u00020:2\u0006\u0010#\u001a\u00020+2\b\u0010A\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020\u0006H\u0002J\"\u0010E\u001a\u00020:2\u0006\u0010#\u001a\u00020F2\u0006\u0010G\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010\nH\u0002J\u0012\u0010J\u001a\u00020'2\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\b\u0010K\u001a\u00020LH\u0002J\u0012\u0010M\u001a\u0004\u0018\u00010H2\u0006\u0010#\u001a\u00020+H\u0002J\n\u0010N\u001a\u00060Oj\u0002`PJ\b\u0010Q\u001a\u00020'H\u0016J\u000f\u0010R\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\bSJ\n\u0010T\u001a\u0004\u0018\u00010'H\u0004J\b\u0010U\u001a\u0004\u0018\u00010'J \u0010V\u001a\u0004\u0018\u00010'2\u0006\u0010#\u001a\u00020F2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020'0XH\u0002J\u0012\u0010Y\u001a\u0004\u0018\u0001002\u0006\u0010#\u001a\u00020+H\u0002J\u0010\u0010Z\u001a\u00020:2\u0006\u0010[\u001a\u00020'H\u0014J\u0015\u0010\\\u001a\u00020:2\u0006\u0010[\u001a\u00020'H\u0010¢\u0006\u0002\b]J\u0017\u0010^\u001a\u00020:2\b\u0010_\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b`J?\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020\u00062\u0006\u0010d\u001a\u00020\u00062'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`iJ/\u0010a\u001a\u00020b2'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`iJ\u0011\u0010j\u001a\u00020:H\u0086@ø\u0001\u0000¢\u0006\u0002\u00107J\b\u0010k\u001a\u00020\u0006H\u0002J\u0011\u0010l\u001a\u00020:H\u0082@ø\u0001\u0000¢\u0006\u0002\u00107J\u001f\u0010m\u001a\u00020n2\u0014\u0010o\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\n\u0012\u0004\u0012\u00020:0fH\u0082\bJ\u0012\u0010p\u001a\u00020\u00062\b\u0010;\u001a\u0004\u0018\u00010\nH\u0002J\u0017\u0010q\u001a\u00020\u00062\b\u0010I\u001a\u0004\u0018\u00010\nH\u0000¢\u0006\u0002\brJ\u001f\u0010s\u001a\u00020\u00062\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0000¢\u0006\u0002\btJ=\u0010u\u001a\u0006\u0012\u0002\b\u0003022'\u0010e\u001a#\u0012\u0015\u0012\u0013\u0018\u00010'¢\u0006\f\bg\u0012\b\bh\u0012\u0004\b\b(;\u0012\u0004\u0012\u00020:0fj\u0002`i2\u0006\u0010c\u001a\u00020\u0006H\u0002J\r\u0010v\u001a\u00020wH\u0010¢\u0006\u0002\bxJ\u0018\u0010y\u001a\u00020:2\u0006\u0010/\u001a\u0002002\u0006\u0010;\u001a\u00020'H\u0002J+\u0010z\u001a\u00020:\"\u000e\b\u0000\u0010{\u0018\u0001*\u0006\u0012\u0002\b\u0003022\u0006\u0010/\u001a\u0002002\b\u0010;\u001a\u0004\u0018\u00010'H\u0082\bJ\u0012\u0010|\u001a\u00020:2\b\u0010;\u001a\u0004\u0018\u00010'H\u0014J'\u0010}\u001a\u00020:2\b\u0010#\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020\u0006H\u0010¢\u0006\u0002\b~J\u000e\u0010\u007f\u001a\u00020:H\u0010¢\u0006\u0003\b\u0080\u0001J\u0010\u0010\u0081\u0001\u001a\u00020:2\u0007\u0010\u0082\u0001\u001a\u00020\u0003J\u0012\u0010\u0083\u0001\u001a\u00020:2\u0007\u0010#\u001a\u00030\u0084\u0001H\u0002J\u0015\u0010\u0085\u0001\u001a\u00020:2\n\u0010#\u001a\u0006\u0012\u0002\b\u000302H\u0002JH\u0010\u0086\u0001\u001a\u00020:\"\u0005\b\u0000\u0010\u0087\u00012\u000f\u0010\u0088\u0001\u001a\n\u0012\u0005\u0012\u0003H\u0087\u00010\u0089\u00012\u001e\u0010o\u001a\u001a\b\u0001\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u0087\u00010\u008a\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0fø\u0001\u0000¢\u0006\u0003\u0010\u008b\u0001JZ\u0010\u008c\u0001\u001a\u00020:\"\u0004\b\u0000\u0010{\"\u0005\b\u0001\u0010\u0087\u00012\u000f\u0010\u0088\u0001\u001a\n\u0012\u0005\u0012\u0003H\u0087\u00010\u0089\u00012%\u0010o\u001a!\b\u0001\u0012\u0004\u0012\u0002H{\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u0087\u00010\u008a\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u008d\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u008e\u0001\u0010\u008f\u0001J\u001b\u0010\u0090\u0001\u001a\u00020:2\n\u00101\u001a\u0006\u0012\u0002\b\u000302H\u0000¢\u0006\u0003\b\u0091\u0001JZ\u0010\u0092\u0001\u001a\u00020:\"\u0004\b\u0000\u0010{\"\u0005\b\u0001\u0010\u0087\u00012\u000f\u0010\u0088\u0001\u001a\n\u0012\u0005\u0012\u0003H\u0087\u00010\u0089\u00012%\u0010o\u001a!\b\u0001\u0012\u0004\u0012\u0002H{\u0012\f\u0012\n\u0012\u0005\u0012\u0003H\u0087\u00010\u008a\u0001\u0012\u0006\u0012\u0004\u0018\u00010\n0\u008d\u0001H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0093\u0001\u0010\u008f\u0001J\u0007\u0010\u0094\u0001\u001a\u00020\u0006J\u0013\u0010\u0095\u0001\u001a\u00020C2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J\u0013\u0010\u0096\u0001\u001a\u00020w2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J \u0010\u0097\u0001\u001a\u00020\u00062\u0007\u0010\u0098\u0001\u001a\u00020'2\f\u0010W\u001a\b\u0012\u0004\u0012\u00020'0XH\u0002J\t\u0010\u0099\u0001\u001a\u00020wH\u0016J#\u0010\u009a\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020F2\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J#\u0010\u009b\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020+2\b\u0010A\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J\u001a\u0010\u009c\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020+2\u0007\u0010\u0098\u0001\u001a\u00020'H\u0002J%\u0010\u009d\u0001\u001a\u00020C2\b\u0010#\u001a\u0004\u0018\u00010\n2\b\u0010I\u001a\u0004\u0018\u00010\n2\u0006\u0010B\u001a\u00020CH\u0002J$\u0010\u009e\u0001\u001a\u00020\u00062\u0006\u0010#\u001a\u00020F2\u0006\u00104\u001a\u00020H2\b\u0010I\u001a\u0004\u0018\u00010\nH\u0082\u0010J\u0010\u0010\u009f\u0001\u001a\u0004\u0018\u00010H*\u00030 \u0001H\u0002J\u0017\u0010¡\u0001\u001a\u00020:*\u0002002\b\u0010;\u001a\u0004\u0018\u00010'H\u0002J\u001a\u0010¢\u0001\u001a\u00060Oj\u0002`P*\u00020'2\u0007\u0010£\u0001\u001a\u00020wH\u0002R\u0016\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f8F¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00068TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\u0013\u0010\rR\u0014\u0010\u0014\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\rR\u0011\u0010\u0015\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0015\u0010\rR\u0011\u0010\u0016\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\rR\u0011\u0010\u0017\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\rR\u0015\u0010\u0018\u001a\u0006\u0012\u0002\b\u00030\u00198F¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0014\u0010\u001c\u001a\u00020\u00068PX\u0090\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\rR\u0011\u0010\u001e\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u001f\u0010 R\u0010\u0010!\u001a\u0004\u0018\u00010\"X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010#\u001a\u0004\u0018\u00010\n8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%R\u001c\u0010&\u001a\u0004\u0018\u00010'*\u0004\u0018\u00010\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b(\u0010)R\u0018\u0010*\u001a\u00020\u0006*\u00020+8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b*\u0010,\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006§\u0001"}, d2 = {"Lkotlinx/coroutines/JobSupport;", "Lkotlinx/coroutines/Job;", "Lkotlinx/coroutines/ChildJob;", "Lkotlinx/coroutines/ParentJob;", "Lkotlinx/coroutines/selects/SelectClause0;", AppMeasurementSdk.ConditionalUserProperty.ACTIVE, "", "(Z)V", "_state", "Lkotlinx/atomicfu/AtomicRef;", "", "cancelsParent", "getCancelsParent", "()Z", "children", "Lkotlin/sequences/Sequence;", "getChildren", "()Lkotlin/sequences/Sequence;", "handlesException", "getHandlesException", "isActive", "isCancelled", "isCompleted", "isCompletedExceptionally", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "getKey", "()Lkotlin/coroutines/CoroutineContext$Key;", "onCancelComplete", "getOnCancelComplete$kotlinx_coroutines_core", "onJoin", "getOnJoin", "()Lkotlinx/coroutines/selects/SelectClause0;", "parentHandle", "Lkotlinx/coroutines/ChildHandle;", "state", "getState$kotlinx_coroutines_core", "()Ljava/lang/Object;", "exceptionOrNull", "", "getExceptionOrNull", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "isCancelling", "Lkotlinx/coroutines/Incomplete;", "(Lkotlinx/coroutines/Incomplete;)Z", "addLastAtomic", "expect", "list", "Lkotlinx/coroutines/NodeList;", "node", "Lkotlinx/coroutines/JobNode;", "attachChild", "child", "awaitInternal", "awaitInternal$kotlinx_coroutines_core", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "awaitSuspend", "cancel", "", "cause", "cancelImpl", "cancelMakeCompleting", "cancelParent", "childCancelled", "completeStateFinalization", "update", "mode", "", "suppressed", "continueCompleting", "Lkotlinx/coroutines/JobSupport$Finishing;", "lastChild", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "createCauseException", "createJobCancellationException", "Lkotlinx/coroutines/JobCancellationException;", "firstChild", "getCancellationException", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "getChildJobCancellationCause", "getCompletedInternal", "getCompletedInternal$kotlinx_coroutines_core", "getCompletionCause", "getCompletionExceptionOrNull", "getFinalRootCause", "exceptions", "", "getOrPromoteCancellingList", "handleJobException", "exception", "handleOnCompletionException", "handleOnCompletionException$kotlinx_coroutines_core", "initParentJobInternal", "parent", "initParentJobInternal$kotlinx_coroutines_core", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "onCancelling", "invokeImmediately", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", AppMeasurementSdk.ConditionalUserProperty.NAME, "Lkotlinx/coroutines/CompletionHandler;", "join", "joinInternal", "joinSuspend", "loopOnState", "", "block", "makeCancelling", "makeCompleting", "makeCompleting$kotlinx_coroutines_core", "makeCompletingOnce", "makeCompletingOnce$kotlinx_coroutines_core", "makeNode", "nameString", "", "nameString$kotlinx_coroutines_core", "notifyCancelling", "notifyHandlers", ExifInterface.GPS_DIRECTION_TRUE, "onCancellation", "onCompletionInternal", "onCompletionInternal$kotlinx_coroutines_core", "onStartInternal", "onStartInternal$kotlinx_coroutines_core", "parentCancelled", "parentJob", "promoteEmptyToNodeList", "Lkotlinx/coroutines/Empty;", "promoteSingleToNodeList", "registerSelectClause0", "R", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/coroutines/Continuation;", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function1;)V", "registerSelectClause1Internal", "Lkotlin/Function2;", "registerSelectClause1Internal$kotlinx_coroutines_core", "(Lkotlinx/coroutines/selects/SelectInstance;Lkotlin/jvm/functions/Function2;)V", "removeNode", "removeNode$kotlinx_coroutines_core", "selectAwaitCompletion", "selectAwaitCompletion$kotlinx_coroutines_core", "start", "startInternal", "stateString", "suppressExceptions", "rootCause", "toString", "tryFinalizeFinishingState", "tryFinalizeSimpleState", "tryMakeCancelling", "tryMakeCompleting", "tryWaitForChild", "nextChild", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "notifyCompletion", "toCancellationException", "message", "AwaitContinuation", "ChildCompletion", "Finishing", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
/* loaded from: classes3.dex */
public class JobSupport implements Job, ChildJob, ParentJob, SelectClause0 {
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile Object _state;
    private volatile ChildHandle parentHandle;

    public JobSupport(boolean active) {
        this._state = active ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW;
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return (R) Job.DefaultImpls.fold(this, r, function2);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return (E) Job.DefaultImpls.get(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return Job.DefaultImpls.minusKey(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    public CoroutineContext plus(CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return Job.DefaultImpls.plus(this, context);
    }

    @Override // kotlinx.coroutines.Job
    @Deprecated(level = DeprecationLevel.ERROR, message = "Operator '+' on two Job objects is meaningless. Job is a coroutine context element and `+` is a set-sum operator for coroutine contexts. The job to the right of `+` just replaces the job the left of `+`.")
    public Job plus(Job other) {
        Intrinsics.checkParameterIsNotNull(other, "other");
        return Job.DefaultImpls.plus((Job) this, other);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(Job parent) {
        if (!(this.parentHandle == null)) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (parent == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
        } else {
            parent.start();
            ChildHandle handle = parent.attachChild(this);
            this.parentHandle = handle;
            if (isCompleted()) {
                handle.dispose();
                this.parentHandle = NonDisposableHandle.INSTANCE;
            }
        }
    }

    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object state = this._state;
            if (!(state instanceof OpDescriptor)) {
                return state;
            }
            ((OpDescriptor) state).perform(this);
        }
    }

    private final Void loopOnState(Function1<Object, Unit> function1) {
        while (true) {
            function1.invoke(getState$kotlinx_coroutines_core());
        }
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        Object state = getState$kotlinx_coroutines_core();
        return (state instanceof Incomplete) && ((Incomplete) state).isActive();
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    @Override // kotlinx.coroutines.Job
    public final boolean isCancelled() {
        Object state = getState$kotlinx_coroutines_core();
        return (state instanceof CompletedExceptionally) || ((state instanceof Finishing) && ((Finishing) state).isCancelling());
    }

    /* JADX INFO: Multiple debug info for r7v0 java.lang.Throwable: [D('finalCause' java.lang.Throwable), D('finalException' java.lang.Throwable)] */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0042, code lost:
        if (r7 != r10.rootCause) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final boolean tryFinalizeFinishingState(Finishing state, Object proposedUpdate, int mode) {
        Throwable finalException;
        if (!(proposedUpdate instanceof Incomplete)) {
            boolean z = false;
            if (!(getState$kotlinx_coroutines_core() == state)) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            } else if (!(!state.isSealed())) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            } else if (state.isCompleting) {
                Throwable proposedException = null;
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(proposedUpdate instanceof CompletedExceptionally) ? null : proposedUpdate);
                if (completedExceptionally != null) {
                    proposedException = completedExceptionally.cause;
                }
                boolean suppressed = false;
                synchronized (state) {
                    List exceptions = state.sealLocked(proposedException);
                    finalException = getFinalRootCause(state, exceptions);
                    if (finalException != null) {
                        if (!suppressExceptions(finalException, exceptions)) {
                        }
                        z = true;
                        suppressed = z;
                    }
                }
                Object finalState = (finalException == null || finalException == proposedException) ? proposedUpdate : new CompletedExceptionally(finalException);
                if (finalException != null && !cancelParent(finalException)) {
                    handleJobException(finalException);
                }
                if (_state$FU.compareAndSet(this, state, finalState)) {
                    completeStateFinalization(state, finalState, mode, suppressed);
                    return true;
                }
                throw new IllegalArgumentException(("Unexpected state: " + this._state + ", expected: " + state + ", update: " + finalState).toString());
            } else {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
        } else {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private final Throwable getFinalRootCause(Finishing state, List<? extends Throwable> $receiver$iv) {
        Object obj = null;
        if (!$receiver$iv.isEmpty()) {
            Iterator<T> it = $receiver$iv.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object element$iv = it.next();
                if (!(((Throwable) element$iv) instanceof CancellationException)) {
                    obj = element$iv;
                    break;
                }
            }
            Throwable th = (Throwable) obj;
            return th != null ? th : (Throwable) $receiver$iv.get(0);
        } else if (state.isCancelling()) {
            return createJobCancellationException();
        } else {
            return null;
        }
    }

    private final boolean suppressExceptions(Throwable rootCause, List<? extends Throwable> list) {
        if (list.size() <= 1) {
            return false;
        }
        Set seenExceptions = ConcurrentKt.identitySet(list.size());
        boolean suppressed = false;
        for (Throwable exception : list) {
            if (exception != rootCause && !(exception instanceof CancellationException) && seenExceptions.add(exception)) {
                ExceptionsKt.addSuppressed(rootCause, exception);
                suppressed = true;
            }
        }
        return suppressed;
    }

    private final boolean tryFinalizeSimpleState(Incomplete state, Object update, int mode) {
        if (!((state instanceof Empty) || (state instanceof JobNode))) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (!(!(update instanceof CompletedExceptionally))) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (!_state$FU.compareAndSet(this, state, update)) {
            return false;
        } else {
            completeStateFinalization(state, update, mode, false);
            return true;
        }
    }

    private final void completeStateFinalization(Incomplete state, Object update, int mode, boolean suppressed) {
        ChildHandle it = this.parentHandle;
        if (it != null) {
            it.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        Throwable cause = null;
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(update instanceof CompletedExceptionally) ? null : update);
        if (completedExceptionally != null) {
            cause = completedExceptionally.cause;
        }
        if (!isCancelling(state)) {
            onCancellation(cause);
        }
        if (state instanceof JobNode) {
            try {
                ((JobNode) state).invoke(cause);
            } catch (Throwable ex) {
                handleOnCompletionException$kotlinx_coroutines_core(new CompletionHandlerException("Exception in completion handler " + state + " for " + this, ex));
            }
        } else {
            NodeList list = state.getList();
            if (list != null) {
                notifyCompletion(list, cause);
            }
        }
        onCompletionInternal$kotlinx_coroutines_core(update, mode, suppressed);
    }

    /* JADX INFO: Multiple debug info for r4v7 kotlinx.coroutines.CompletionHandlerException: [D('$receiver$iv' kotlinx.coroutines.JobSupport), D('exception$iv' java.lang.Object)] */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004e, code lost:
        if (r9 != null) goto L_0x0080;
     */
    /* Code decompiled incorrectly, please refer to instructions dump */
    private final void notifyCancelling(NodeList list, Throwable cause) {
        int $i$f$notifyHandlers;
        onCancellation(cause);
        int $i$f$notifyHandlers2 = 0;
        Throwable th = null;
        Object next = list.getNext();
        if (next != null) {
            LockFreeLinkedListNode cur$iv$iv = (LockFreeLinkedListNode) next;
            CompletionHandlerException completionHandlerException = th;
            Object exception$iv = null;
            int $i$a$2$run = 0;
            while (!Intrinsics.areEqual(cur$iv$iv, list)) {
                if (cur$iv$iv instanceof JobCancellingNode) {
                    JobNode node$iv = (JobNode) cur$iv$iv;
                    try {
                        node$iv.invoke(cause);
                        $i$f$notifyHandlers = $i$f$notifyHandlers2;
                    } catch (Throwable ex$iv) {
                        if (completionHandlerException != null) {
                            $i$f$notifyHandlers = $i$f$notifyHandlers2;
                            ExceptionsKt.addSuppressed(completionHandlerException, ex$iv);
                        } else {
                            $i$f$notifyHandlers = $i$f$notifyHandlers2;
                        }
                        Unit unit = Unit.INSTANCE;
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + node$iv + " for " + this, ex$iv);
                        $i$a$2$run = $i$a$2$run;
                    }
                    exception$iv = exception$iv;
                } else {
                    $i$f$notifyHandlers = $i$f$notifyHandlers2;
                }
                cur$iv$iv = cur$iv$iv.getNextNode();
                $i$f$notifyHandlers2 = $i$f$notifyHandlers;
            }
            if (completionHandlerException != null) {
                handleOnCompletionException$kotlinx_coroutines_core(completionHandlerException);
            }
            cancelParent(cause);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    /* JADX INFO: Multiple debug info for r2v7 kotlinx.coroutines.CompletionHandlerException: [D('$receiver$iv' kotlinx.coroutines.JobSupport), D('exception$iv' java.lang.Object)] */
    private final void notifyCompletion(NodeList $receiver, Throwable cause) {
        int $i$f$notifyHandlers;
        int $i$f$notifyHandlers2 = 0;
        Throwable th = null;
        Object next = $receiver.getNext();
        if (next != null) {
            LockFreeLinkedListNode cur$iv$iv = (LockFreeLinkedListNode) next;
            CompletionHandlerException completionHandlerException = th;
            Object exception$iv = null;
            while (!Intrinsics.areEqual(cur$iv$iv, $receiver)) {
                if (cur$iv$iv instanceof JobNode) {
                    JobNode node$iv = (JobNode) cur$iv$iv;
                    try {
                        node$iv.invoke(cause);
                        $i$f$notifyHandlers = $i$f$notifyHandlers2;
                    } catch (Throwable ex$iv) {
                        if (completionHandlerException != null) {
                            ExceptionsKt.addSuppressed(completionHandlerException, ex$iv);
                            if (completionHandlerException != null) {
                                $i$f$notifyHandlers = $i$f$notifyHandlers2;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        $i$f$notifyHandlers = $i$f$notifyHandlers2;
                        sb.append("Exception in completion handler ");
                        sb.append(node$iv);
                        sb.append(" for ");
                        sb.append(this);
                        Unit unit = Unit.INSTANCE;
                        completionHandlerException = new CompletionHandlerException(sb.toString(), ex$iv);
                    }
                    exception$iv = exception$iv;
                } else {
                    $i$f$notifyHandlers = $i$f$notifyHandlers2;
                }
                cur$iv$iv = cur$iv$iv.getNextNode();
                $i$f$notifyHandlers2 = $i$f$notifyHandlers;
            }
            if (completionHandlerException != null) {
                handleOnCompletionException$kotlinx_coroutines_core(completionHandlerException);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    private final <T extends JobNode<?>> void notifyHandlers(NodeList list, Throwable cause) {
        int $i$f$notifyHandlers;
        int $i$f$notifyHandlers2 = 0;
        Throwable th = null;
        Object next = list.getNext();
        if (next != null) {
            LockFreeLinkedListNode cur$iv = (LockFreeLinkedListNode) next;
            CompletionHandlerException completionHandlerException = th;
            Object exception = null;
            while (!Intrinsics.areEqual(cur$iv, list)) {
                Intrinsics.reifiedOperationMarker(3, ExifInterface.GPS_DIRECTION_TRUE);
                if (cur$iv instanceof LockFreeLinkedListNode) {
                    JobNode node = (JobNode) cur$iv;
                    try {
                        node.invoke(cause);
                        $i$f$notifyHandlers = $i$f$notifyHandlers2;
                    } catch (Throwable ex) {
                        if (completionHandlerException != null) {
                            ExceptionsKt.addSuppressed(completionHandlerException, ex);
                            if (completionHandlerException != null) {
                                $i$f$notifyHandlers = $i$f$notifyHandlers2;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        $i$f$notifyHandlers = $i$f$notifyHandlers2;
                        sb.append("Exception in completion handler ");
                        sb.append(node);
                        sb.append(" for ");
                        sb.append(this);
                        Unit unit = Unit.INSTANCE;
                        completionHandlerException = new CompletionHandlerException(sb.toString(), ex);
                    }
                    exception = exception;
                } else {
                    $i$f$notifyHandlers = $i$f$notifyHandlers2;
                }
                cur$iv = cur$iv.getNextNode();
                $i$f$notifyHandlers2 = $i$f$notifyHandlers;
            }
            if (completionHandlerException != null) {
                handleOnCompletionException$kotlinx_coroutines_core(completionHandlerException);
                return;
            }
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        int startInternal;
        do {
            startInternal = startInternal(getState$kotlinx_coroutines_core());
            if (startInternal == 0) {
                return false;
            }
        } while (startInternal != 1);
        return true;
    }

    private final int startInternal(Object state) {
        if (state instanceof Empty) {
            if (((Empty) state).isActive()) {
                return 0;
            }
            if (!_state$FU.compareAndSet(this, state, JobSupportKt.EMPTY_ACTIVE)) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        } else if (!(state instanceof InactiveNodeList)) {
            return 0;
        } else {
            if (!_state$FU.compareAndSet(this, state, ((InactiveNodeList) state).getList())) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        }
    }

    public void onStartInternal$kotlinx_coroutines_core() {
    }

    @Override // kotlinx.coroutines.Job
    public final CancellationException getCancellationException() {
        CancellationException cancellationException;
        Object state = getState$kotlinx_coroutines_core();
        if (state instanceof Finishing) {
            Throwable th = ((Finishing) state).rootCause;
            if (th != null && (cancellationException = toCancellationException(th, "Job is cancelling")) != null) {
                return cancellationException;
            }
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state instanceof Incomplete) {
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state instanceof CompletedExceptionally) {
            return toCancellationException(((CompletedExceptionally) state).cause, "Job was cancelled");
        } else {
            return new JobCancellationException("Job has completed normally", null, this);
        }
    }

    private final CancellationException toCancellationException(Throwable $receiver, String message) {
        CancellationException cancellationException = (CancellationException) (!($receiver instanceof CancellationException) ? null : $receiver);
        return cancellationException != null ? cancellationException : new JobCancellationException(message, $receiver, this);
    }

    protected final Throwable getCompletionCause() {
        Object state = getState$kotlinx_coroutines_core();
        if (state instanceof Finishing) {
            Throwable th = ((Finishing) state).rootCause;
            if (th != null) {
                return th;
            }
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state instanceof Incomplete) {
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state instanceof CompletedExceptionally) {
            return ((CompletedExceptionally) state).cause;
        } else {
            return null;
        }
    }

    @Override // kotlinx.coroutines.Job
    public final DisposableHandle invokeOnCompletion(Function1<? super Throwable, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        return invokeOnCompletion(false, true, function1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0095, code lost:
        if (((kotlinx.coroutines.JobSupport.Finishing) r14).isCompleting == false) goto L_0x009c;
     */
    @Override // kotlinx.coroutines.Job
    /* Code decompiled incorrectly, please refer to instructions dump */
    public final DisposableHandle invokeOnCompletion(boolean onCancelling, boolean invokeImmediately, Function1<? super Throwable, Unit> function1) {
        int $i$a$1$loopOnState;
        JobNode node;
        Throwable th;
        JobNode node2;
        JobNode node3;
        Intrinsics.checkParameterIsNotNull(function1, "handler");
        Object obj = null;
        JobNode jobNode = null;
        int nodeCache = 0;
        while (true) {
            Object state = getState$kotlinx_coroutines_core();
            if (state instanceof Empty) {
                if (((Empty) state).isActive()) {
                    if (jobNode != null) {
                        node3 = jobNode;
                    } else {
                        node3 = makeNode(function1, onCancelling);
                        jobNode = node3;
                    }
                    if (_state$FU.compareAndSet(this, state, node3)) {
                        return node3;
                    }
                    $i$a$1$loopOnState = nodeCache;
                    nodeCache = $i$a$1$loopOnState;
                    obj = null;
                } else {
                    promoteEmptyToNodeList((Empty) state);
                    $i$a$1$loopOnState = nodeCache;
                    nodeCache = $i$a$1$loopOnState;
                    obj = null;
                }
            } else if (state instanceof Incomplete) {
                NodeList list = ((Incomplete) state).getList();
                if (list != null) {
                    Object rootCause = (Throwable) obj;
                    JobNode jobNode2 = NonDisposableHandle.INSTANCE;
                    if (!onCancelling || !(state instanceof Finishing)) {
                        $i$a$1$loopOnState = nodeCache;
                    } else {
                        synchronized (state) {
                            try {
                                rootCause = ((Finishing) state).rootCause;
                                if (rootCause != null) {
                                    $i$a$1$loopOnState = nodeCache;
                                    try {
                                        if (function1 instanceof ChildHandleNode) {
                                        }
                                        Unit unit = Unit.INSTANCE;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        throw th;
                                    }
                                } else {
                                    $i$a$1$loopOnState = nodeCache;
                                }
                                if (jobNode != null) {
                                    node2 = jobNode;
                                } else {
                                    try {
                                        node2 = makeNode(function1, onCancelling);
                                        jobNode = node2;
                                    } catch (Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                }
                                if (addLastAtomic(state, list, node2)) {
                                    if (rootCause == null) {
                                        return node2;
                                    }
                                    jobNode2 = node2;
                                    Unit unit2 = Unit.INSTANCE;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                        nodeCache = $i$a$1$loopOnState;
                        obj = null;
                    }
                    if (rootCause != null) {
                        if (invokeImmediately) {
                            function1.invoke(rootCause);
                        }
                        return jobNode2;
                    }
                    if (jobNode != null) {
                        node = jobNode;
                    } else {
                        node = makeNode(function1, onCancelling);
                        jobNode = node;
                    }
                    if (addLastAtomic(state, list, node)) {
                        return node;
                    }
                    nodeCache = $i$a$1$loopOnState;
                    obj = null;
                } else if (state != null) {
                    promoteSingleToNodeList((JobNode) state);
                    $i$a$1$loopOnState = nodeCache;
                    nodeCache = $i$a$1$loopOnState;
                    obj = null;
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobNode<*>");
                }
            } else {
                if (invokeImmediately) {
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(state instanceof CompletedExceptionally) ? null : state);
                    function1.invoke(completedExceptionally != null ? completedExceptionally.cause : null);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> function1, boolean onCancelling) {
        boolean z = true;
        JobNode jobNode = null;
        if (onCancelling) {
            if (function1 instanceof JobCancellingNode) {
                jobNode = function1;
            }
            JobCancellingNode it = jobNode;
            if (it != null) {
                if (it.job != this) {
                    z = false;
                }
                if (!z) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                } else if (it != null) {
                    return it;
                }
            }
            return new InvokeOnCancelling(this, function1);
        }
        if (function1 instanceof JobNode) {
            jobNode = function1;
        }
        JobNode it2 = jobNode;
        if (it2 != null) {
            if (it2.job != this || (it2 instanceof JobCancellingNode)) {
                z = false;
            }
            if (!z) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            } else if (it2 != null) {
                return it2;
            }
        }
        return new InvokeOnCompletion(this, function1);
    }

    private final boolean addLastAtomic(Object expect, NodeList list, JobNode<?> jobNode) {
        int tryCondAddNext;
        LockFreeLinkedListNode.CondAddOp condAdd$iv = new LockFreeLinkedListNode.CondAddOp(jobNode, this, expect) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
            final /* synthetic */ Object $expect$inlined;
            final /* synthetic */ JobSupport this$0;

            {
                this.this$0 = r3;
                this.$expect$inlined = r4;
            }

            public Object prepare(LockFreeLinkedListNode affected) {
                Intrinsics.checkParameterIsNotNull(affected, "affected");
                boolean z = false;
                if (this.this$0.getState$kotlinx_coroutines_core() == this.$expect$inlined) {
                    z = true;
                }
                if (z) {
                    return null;
                }
                return LockFreeLinkedListKt.getCONDITION_FALSE();
            }
        };
        do {
            Object prev = list.getPrev();
            if (prev != null) {
                tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(jobNode, list, condAdd$iv);
                if (tryCondAddNext == 1) {
                    return true;
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    private final void promoteEmptyToNodeList(Empty state) {
        NodeList list = new NodeList();
        _state$FU.compareAndSet(this, state, state.isActive() ? list : new InactiveNodeList(list));
    }

    private final void promoteSingleToNodeList(JobNode<?> jobNode) {
        jobNode.addOneIfEmpty(new NodeList());
        _state$FU.compareAndSet(this, jobNode, jobNode.getNextNode());
    }

    @Override // kotlinx.coroutines.Job
    public final Object join(Continuation<? super Unit> continuation) {
        if (joinInternal()) {
            return joinSuspend(continuation);
        }
        YieldKt.checkCompletion(continuation.getContext());
        return Unit.INSTANCE;
    }

    private final boolean joinInternal() {
        Object state;
        do {
            state = getState$kotlinx_coroutines_core();
            if (!(state instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state) < 0);
        return true;
    }

    final /* synthetic */ Object joinSuspend(Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        CancellableContinuationImpl cont = cancellable$iv;
        CancellableContinuationKt.disposeOnCancellation(cont, invokeOnCompletion(new ResumeOnCompletion(this, cont)));
        Object result = cancellable$iv.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    @Override // kotlinx.coroutines.Job
    public final SelectClause0 getOnJoin() {
        return this;
    }

    @Override // kotlinx.coroutines.selects.SelectClause0
    public final <R> void registerSelectClause0(SelectInstance<? super R> selectInstance, Function1<? super Continuation<? super R>, ? extends Object> function1) {
        Object state;
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function1, "block");
        do {
            state = getState$kotlinx_coroutines_core();
            if (!selectInstance.isSelected()) {
                if (!(state instanceof Incomplete)) {
                    if (selectInstance.trySelect(null)) {
                        YieldKt.checkCompletion(selectInstance.getCompletion().getContext());
                        UndispatchedKt.startCoroutineUnintercepted(function1, selectInstance.getCompletion());
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
        } while (startInternal(state) != 0);
        selectInstance.disposeOnSelect(invokeOnCompletion(new SelectJoinOnCompletion(this, selectInstance, function1)));
    }

    public final void removeNode$kotlinx_coroutines_core(JobNode<?> jobNode) {
        Object state;
        Intrinsics.checkParameterIsNotNull(jobNode, "node");
        do {
            state = getState$kotlinx_coroutines_core();
            if (state instanceof JobNode) {
                if (state != jobNode) {
                    return;
                }
            } else if ((state instanceof Incomplete) && ((Incomplete) state).getList() != null) {
                jobNode.remove();
                return;
            } else {
                return;
            }
        } while (!_state$FU.compareAndSet(this, state, JobSupportKt.EMPTY_ACTIVE));
    }

    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return false;
    }

    @Override // kotlinx.coroutines.Job
    public void cancel() {
        cancel(null);
    }

    @Override // kotlinx.coroutines.Job
    public boolean cancel(Throwable cause) {
        return cancelImpl(cause) && getHandlesException();
    }

    @Override // kotlinx.coroutines.ChildJob
    public final void parentCancelled(ParentJob parentJob) {
        Intrinsics.checkParameterIsNotNull(parentJob, "parentJob");
        cancelImpl(parentJob);
    }

    public boolean childCancelled(Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        return cancelImpl(cause) && getHandlesException();
    }

    private final boolean cancelImpl(Object cause) {
        if (!getOnCancelComplete$kotlinx_coroutines_core() || !cancelMakeCompleting(cause)) {
            return makeCancelling(cause);
        }
        return true;
    }

    private final boolean cancelMakeCompleting(Object cause) {
        int tryMakeCompleting;
        do {
            Object state = getState$kotlinx_coroutines_core();
            if (!(state instanceof Incomplete) || (((state instanceof Finishing) && ((Finishing) state).isCompleting) || (tryMakeCompleting = tryMakeCompleting(state, new CompletedExceptionally(createCauseException(cause)), 0)) == 0)) {
                return false;
            }
            if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                return true;
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    private final JobCancellationException createJobCancellationException() {
        return new JobCancellationException("Job was cancelled", null, this);
    }

    @Override // kotlinx.coroutines.ParentJob
    public Throwable getChildJobCancellationCause() {
        Throwable rootCause;
        Object state = getState$kotlinx_coroutines_core();
        if (state instanceof Finishing) {
            rootCause = ((Finishing) state).rootCause;
        } else if (state instanceof Incomplete) {
            throw new IllegalStateException(("Cannot be cancelling child in this state: " + state).toString());
        } else if (state instanceof CompletedExceptionally) {
            rootCause = ((CompletedExceptionally) state).cause;
        } else {
            rootCause = null;
        }
        if (rootCause != null && (!getHandlesException() || (rootCause instanceof CancellationException))) {
            return rootCause;
        }
        return new JobCancellationException("Parent job is " + stateString(state), rootCause, this);
    }

    private final Throwable createCauseException(Object cause) {
        if (cause != null ? cause instanceof Throwable : true) {
            return cause != null ? (Throwable) cause : createJobCancellationException();
        }
        if (cause != null) {
            return ((ParentJob) cause).getChildJobCancellationCause();
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
    }

    private final boolean makeCancelling(Object cause) {
        Throwable notifyRootCause;
        Object causeExceptionCache;
        Object obj;
        Throwable th;
        Throwable notifyRootCause2 = null;
        boolean z = false;
        Throwable causeException = null;
        Object causeExceptionCache2 = null;
        while (true) {
            Object state = getState$kotlinx_coroutines_core();
            if (state instanceof Finishing) {
                synchronized (state) {
                    try {
                        if (((Finishing) state).isSealed()) {
                            return false;
                        }
                        boolean wasCancelling = ((Finishing) state).isCancelling();
                        if (cause != null || !wasCancelling) {
                            if (causeException != null) {
                                causeExceptionCache = causeException;
                            } else {
                                Object it = createCauseException(cause);
                                causeExceptionCache = it;
                                causeException = it;
                            }
                            try {
                                ((Finishing) state).addExceptionLocked(causeException);
                            } catch (Throwable th2) {
                                notifyRootCause = th2;
                                throw notifyRootCause;
                            }
                        }
                        notifyRootCause2 = ((Finishing) state).rootCause;
                        if (!wasCancelling) {
                            z = true;
                        }
                        if (z) {
                        }
                        if (notifyRootCause2 != null) {
                            notifyCancelling(((Finishing) state).getList(), notifyRootCause2);
                        }
                        return true;
                    } catch (Throwable th3) {
                        notifyRootCause = th3;
                    }
                }
            } else if (!(state instanceof Incomplete)) {
                return false;
            } else {
                if (causeException != null) {
                    obj = causeExceptionCache2;
                    th = causeException;
                } else {
                    Throwable it2 = createCauseException(cause);
                    th = it2;
                    causeException = it2;
                    obj = causeExceptionCache2;
                }
                if (!((Incomplete) state).isActive()) {
                    int tryMakeCompleting = tryMakeCompleting(state, new CompletedExceptionally(causeException), 0);
                    if (tryMakeCompleting == 0) {
                        throw new IllegalStateException(("Cannot happen in " + state).toString());
                    } else if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                        break;
                    } else if (tryMakeCompleting != 3) {
                        throw new IllegalStateException("unexpected result".toString());
                    }
                } else if (tryMakeCancelling((Incomplete) state, causeException)) {
                    return true;
                }
                causeException = th;
                causeExceptionCache2 = obj;
            }
        }
        return true;
    }

    private final NodeList getOrPromoteCancellingList(Incomplete state) {
        NodeList list = state.getList();
        if (list != null) {
            return list;
        }
        if (state instanceof Empty) {
            return new NodeList();
        }
        if (state instanceof JobNode) {
            promoteSingleToNodeList((JobNode) state);
            return null;
        }
        throw new IllegalStateException(("State should have list: " + state).toString());
    }

    private final boolean tryMakeCancelling(Incomplete state, Throwable rootCause) {
        if (!(!(state instanceof Finishing))) {
            throw new IllegalStateException("Check failed.".toString());
        } else if (state.isActive()) {
            NodeList list = getOrPromoteCancellingList(state);
            if (list == null) {
                return false;
            }
            if (!_state$FU.compareAndSet(this, state, new Finishing(list, false, rootCause))) {
                return false;
            }
            notifyCancelling(list, rootCause);
            return true;
        } else {
            throw new IllegalStateException("Check failed.".toString());
        }
    }

    public final boolean makeCompleting$kotlinx_coroutines_core(Object proposedUpdate) {
        int tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate, 0);
            if (tryMakeCompleting == 0) {
                return false;
            }
            if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                return true;
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    public final boolean makeCompletingOnce$kotlinx_coroutines_core(Object proposedUpdate, int mode) {
        int tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), proposedUpdate, mode);
            if (tryMakeCompleting == 0) {
                throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + proposedUpdate, getExceptionOrNull(proposedUpdate));
            } else if (tryMakeCompleting == 1) {
                return true;
            } else {
                if (tryMakeCompleting == 2) {
                    return false;
                }
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    private final int tryMakeCompleting(Object state, Object proposedUpdate, int mode) {
        if (!(state instanceof Incomplete)) {
            return 0;
        }
        if (((state instanceof Empty) || (state instanceof JobNode)) && !(state instanceof ChildHandleNode) && !(proposedUpdate instanceof CompletedExceptionally)) {
            return !tryFinalizeSimpleState((Incomplete) state, proposedUpdate, mode) ? 3 : 1;
        }
        NodeList list = getOrPromoteCancellingList((Incomplete) state);
        if (list == null) {
            return 3;
        }
        Finishing finishing = (Finishing) (!(state instanceof Finishing) ? null : state);
        if (finishing == null) {
            finishing = new Finishing(list, false, null);
        }
        synchronized (finishing) {
            if (finishing.isCompleting) {
                return 0;
            }
            finishing.isCompleting = true;
            if (finishing != state && !_state$FU.compareAndSet(this, state, finishing)) {
                return 3;
            }
            if (!finishing.isSealed()) {
                boolean wasCancelling = finishing.isCancelling();
                CompletedExceptionally it = (CompletedExceptionally) (!(proposedUpdate instanceof CompletedExceptionally) ? null : proposedUpdate);
                if (it != null) {
                    finishing.addExceptionLocked(it.cause);
                }
                Throwable th = finishing.rootCause;
                if ((!wasCancelling ? 1 : null) == null) {
                    th = null;
                }
                Unit unit = Unit.INSTANCE;
                if (th != null) {
                    notifyCancelling(list, th);
                }
                ChildHandleNode child = firstChild((Incomplete) state);
                if (child == null || !tryWaitForChild(finishing, child, proposedUpdate)) {
                    return tryFinalizeFinishingState(finishing, proposedUpdate, mode) ? 1 : 3;
                }
                return 2;
            }
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private final Throwable getExceptionOrNull(Object $receiver) {
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!($receiver instanceof CompletedExceptionally) ? null : $receiver);
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    private final ChildHandleNode firstChild(Incomplete state) {
        ChildHandleNode childHandleNode = (ChildHandleNode) (!(state instanceof ChildHandleNode) ? null : state);
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = state.getList();
        if (list != null) {
            return nextChild(list);
        }
        return null;
    }

    private final boolean tryWaitForChild(Finishing state, ChildHandleNode child, Object proposedUpdate) {
        while (Job.DefaultImpls.invokeOnCompletion$default(child.childJob, false, false, new ChildCompletion(this, state, child, proposedUpdate), 1, null) == NonDisposableHandle.INSTANCE) {
            ChildHandleNode nextChild = nextChild(child);
            if (nextChild == null) {
                return false;
            }
            child = nextChild;
        }
        return true;
    }

    public final void continueCompleting(Finishing state, ChildHandleNode lastChild, Object proposedUpdate) {
        if (getState$kotlinx_coroutines_core() == state) {
            ChildHandleNode waitChild = nextChild(lastChild);
            if ((waitChild != null && tryWaitForChild(state, waitChild, proposedUpdate)) || !tryFinalizeFinishingState(state, proposedUpdate, 0)) {
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }

    private final ChildHandleNode nextChild(LockFreeLinkedListNode $receiver) {
        LockFreeLinkedListNode cur = $receiver;
        while (cur.isRemoved()) {
            cur = cur.getPrevNode();
        }
        while (true) {
            cur = cur.getNextNode();
            if (!cur.isRemoved()) {
                if (cur instanceof ChildHandleNode) {
                    return (ChildHandleNode) cur;
                }
                if (cur instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.Job
    public final Sequence<Job> getChildren() {
        return SequencesKt.sequence(new JobSupport$children$1(this, null));
    }

    @Override // kotlinx.coroutines.Job
    public final ChildHandle attachChild(ChildJob child) {
        Intrinsics.checkParameterIsNotNull(child, "child");
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(this, child), 2, null);
        if (invokeOnCompletion$default != null) {
            return (ChildHandle) invokeOnCompletion$default;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
    }

    public void handleOnCompletionException$kotlinx_coroutines_core(Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        throw exception;
    }

    protected void onCancellation(Throwable cause) {
    }

    protected boolean getCancelsParent() {
        return false;
    }

    protected boolean getHandlesException() {
        return true;
    }

    protected void handleJobException(Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
    }

    private final boolean cancelParent(Throwable cause) {
        if (cause instanceof CancellationException) {
            return true;
        }
        if (!getCancelsParent()) {
            return false;
        }
        ChildHandle childHandle = this.parentHandle;
        if (childHandle == null || !childHandle.childCancelled(cause)) {
            return false;
        }
        return true;
    }

    public void onCompletionInternal$kotlinx_coroutines_core(Object state, int mode, boolean suppressed) {
    }

    public String toString() {
        return nameString$kotlinx_coroutines_core() + '{' + stateString(getState$kotlinx_coroutines_core()) + "}@" + DebugKt.getHexAddress(this);
    }

    public String nameString$kotlinx_coroutines_core() {
        return DebugKt.getClassSimpleName(this);
    }

    private final String stateString(Object state) {
        if (state instanceof Finishing) {
            if (((Finishing) state).isCancelling()) {
                return "Cancelling";
            }
            if (((Finishing) state).isCompleting) {
                return "Completing";
            }
            return "Active";
        } else if (state instanceof Incomplete) {
            if (((Incomplete) state).isActive()) {
                return "Active";
            }
            return "New";
        } else if (state instanceof CompletedExceptionally) {
            return "Cancelled";
        } else {
            return "Completed";
        }
    }

    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0003\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u00022\u00020\u0003B\u001f\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\tJ\u0018\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\t0\u0016j\b\u0012\u0004\u0012\u00020\t`\u0017H\u0002J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\t0\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\tJ\b\u0010\u001b\u001a\u00020\u001cH\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u0001X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u00078VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000e\u0010\rR\u0012\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u00078F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\rR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/JobSupport$Finishing;", "", "Lkotlinx/coroutines/internal/SynchronizedObject;", "Lkotlinx/coroutines/Incomplete;", "list", "Lkotlinx/coroutines/NodeList;", "isCompleting", "", "rootCause", "", "(Lkotlinx/coroutines/NodeList;ZLjava/lang/Throwable;)V", "_exceptionsHolder", "isActive", "()Z", "isCancelling", "isSealed", "getList", "()Lkotlinx/coroutines/NodeList;", "addExceptionLocked", "", "exception", "allocateList", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "sealLocked", "", "proposedException", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class Finishing implements Incomplete {
        private volatile Object _exceptionsHolder;
        public volatile boolean isCompleting;
        private final NodeList list;
        public volatile Throwable rootCause;

        public Finishing(NodeList list, boolean isCompleting, Throwable rootCause) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = list;
            this.isCompleting = isCompleting;
            this.rootCause = rootCause;
        }

        @Override // kotlinx.coroutines.Incomplete
        public NodeList getList() {
            return this.list;
        }

        public final boolean isSealed() {
            return this._exceptionsHolder == JobSupportKt.SEALED;
        }

        public final boolean isCancelling() {
            return this.rootCause != null;
        }

        @Override // kotlinx.coroutines.Incomplete
        public boolean isActive() {
            return this.rootCause == null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object] */
        /* JADX WARN: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final List<Throwable> sealLocked(Throwable proposedException) {
            ArrayList it;
            ?? r0 = this._exceptionsHolder;
            if (r0 == 0) {
                it = allocateList();
            } else if (r0 instanceof Throwable) {
                it = allocateList();
                it.add(r0);
            } else if (!(r0 instanceof ArrayList)) {
                throw new IllegalStateException(("State is " + ((Object) r0)).toString());
            } else if (r0 != 0) {
                it = (ArrayList) r0;
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<kotlin.Throwable> /* = java.util.ArrayList<kotlin.Throwable> */");
            }
            Throwable rootCause = this.rootCause;
            if (rootCause != null) {
                it.add(0, rootCause);
            }
            if (proposedException != null && (!Intrinsics.areEqual(proposedException, rootCause))) {
                it.add(proposedException);
            }
            this._exceptionsHolder = JobSupportKt.SEALED;
            return it;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.Object] */
        /* JADX WARN: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump */
        public final void addExceptionLocked(Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            Throwable rootCause = this.rootCause;
            if (rootCause == null) {
                this.rootCause = exception;
            } else if (exception != rootCause) {
                ?? r1 = this._exceptionsHolder;
                if (r1 == 0) {
                    this._exceptionsHolder = exception;
                } else if (r1 instanceof Throwable) {
                    if (exception != r1) {
                        ArrayList $receiver = allocateList();
                        $receiver.add(r1);
                        $receiver.add(exception);
                        this._exceptionsHolder = $receiver;
                    }
                } else if (!(r1 instanceof ArrayList)) {
                    throw new IllegalStateException(("State is " + ((Object) r1)).toString());
                } else if (r1 != 0) {
                    ((ArrayList) r1).add(exception);
                } else {
                    throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.ArrayList<kotlin.Throwable> /* = java.util.ArrayList<kotlin.Throwable> */");
                }
            }
        }

        private final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        public String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + this.isCompleting + ", rootCause=" + this.rootCause + ", exceptions=" + this._exceptionsHolder + ", list=" + getList() + ']';
        }
    }

    private final boolean isCancelling(Incomplete $receiver) {
        return ($receiver instanceof Finishing) && ((Finishing) $receiver).isCancelling();
    }

    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B'\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0096\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlinx/coroutines/JobSupport$ChildCompletion;", "Lkotlinx/coroutines/JobNode;", "Lkotlinx/coroutines/Job;", "parent", "Lkotlinx/coroutines/JobSupport;", "state", "Lkotlinx/coroutines/JobSupport$Finishing;", "child", "Lkotlinx/coroutines/ChildHandleNode;", "proposedUpdate", "", "(Lkotlinx/coroutines/JobSupport;Lkotlinx/coroutines/JobSupport$Finishing;Lkotlinx/coroutines/ChildHandleNode;Ljava/lang/Object;)V", "invoke", "", "cause", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class ChildCompletion extends JobNode<Job> {
        private final ChildHandleNode child;
        private final JobSupport parent;
        private final Object proposedUpdate;
        private final Finishing state;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChildCompletion(JobSupport parent, Finishing state, ChildHandleNode child, Object proposedUpdate) {
            super(child.childJob);
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Intrinsics.checkParameterIsNotNull(state, "state");
            Intrinsics.checkParameterIsNotNull(child, "child");
            this.parent = parent;
            this.state = state;
            this.child = child;
            this.proposedUpdate = proposedUpdate;
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* renamed from: invoke */
        public void invoke2(Throwable cause) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        public String toString() {
            return "ChildCompletion[" + this.child + ", " + this.proposedUpdate + ']';
        }
    }

    /* compiled from: JobSupport.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/JobSupport$AwaitContinuation;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlinx/coroutines/CancellableContinuationImpl;", "delegate", "Lkotlin/coroutines/Continuation;", "job", "Lkotlinx/coroutines/JobSupport;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/JobSupport;)V", "getContinuationCancellationCause", "", "parent", "Lkotlinx/coroutines/Job;", "nameString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 13})
    /* loaded from: classes3.dex */
    public static final class AwaitContinuation<T> extends CancellableContinuationImpl<T> {
        private final JobSupport job;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AwaitContinuation(Continuation<? super T> continuation, JobSupport job) {
            super(continuation, 1);
            Intrinsics.checkParameterIsNotNull(continuation, "delegate");
            Intrinsics.checkParameterIsNotNull(job, "job");
            this.job = job;
        }

        @Override // kotlinx.coroutines.AbstractContinuation
        public Throwable getContinuationCancellationCause(Job parent) {
            Throwable it;
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Object state = this.job.getState$kotlinx_coroutines_core();
            if ((state instanceof Finishing) && (it = ((Finishing) state).rootCause) != null) {
                return it;
            }
            if (state instanceof CompletedExceptionally) {
                return ((CompletedExceptionally) state).cause;
            }
            return parent.getCancellationException();
        }

        @Override // kotlinx.coroutines.CancellableContinuationImpl, kotlinx.coroutines.AbstractContinuation
        protected String nameString() {
            return "AwaitContinuation(" + DebugKt.toDebugString(getDelegate()) + ')';
        }
    }

    public final boolean isCompletedExceptionally() {
        return getState$kotlinx_coroutines_core() instanceof CompletedExceptionally;
    }

    public final Throwable getCompletionExceptionOrNull() {
        Object state = getState$kotlinx_coroutines_core();
        if (!(state instanceof Incomplete)) {
            return getExceptionOrNull(state);
        }
        throw new IllegalStateException("This job has not completed yet".toString());
    }

    public final Object getCompletedInternal$kotlinx_coroutines_core() {
        Object state = getState$kotlinx_coroutines_core();
        if (!(!(state instanceof Incomplete))) {
            throw new IllegalStateException("This job has not completed yet".toString());
        } else if (!(state instanceof CompletedExceptionally)) {
            return state;
        } else {
            throw ((CompletedExceptionally) state).cause;
        }
    }

    public final Object awaitInternal$kotlinx_coroutines_core(Continuation<Object> continuation) {
        Object state;
        do {
            state = getState$kotlinx_coroutines_core();
            if (!(state instanceof Incomplete)) {
                if (!(state instanceof CompletedExceptionally)) {
                    return state;
                }
                throw ((CompletedExceptionally) state).cause;
            }
        } while (startInternal(state) < 0);
        return awaitSuspend(continuation);
    }

    final /* synthetic */ Object awaitSuspend(Continuation<Object> continuation) {
        AwaitContinuation cont = new AwaitContinuation(IntrinsicsKt.intercepted(continuation), this);
        cont.initCancellability();
        invokeOnCompletion(new ResumeAwaitOnCompletion(this, cont));
        Object result = cont.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public final <T, R> void registerSelectClause1Internal$kotlinx_coroutines_core(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Object state;
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        do {
            state = getState$kotlinx_coroutines_core();
            if (!selectInstance.isSelected()) {
                if (!(state instanceof Incomplete)) {
                    if (!selectInstance.trySelect(null)) {
                        return;
                    }
                    if (state instanceof CompletedExceptionally) {
                        selectInstance.resumeSelectCancellableWithException(((CompletedExceptionally) state).cause);
                        return;
                    } else {
                        UndispatchedKt.startCoroutineUnintercepted(function2, state, selectInstance.getCompletion());
                        return;
                    }
                }
            } else {
                return;
            }
        } while (startInternal(state) != 0);
        selectInstance.disposeOnSelect(invokeOnCompletion(new SelectAwaitOnCompletion(this, selectInstance, function2)));
    }

    public final <T, R> void selectAwaitCompletion$kotlinx_coroutines_core(SelectInstance<? super R> selectInstance, Function2<? super T, ? super Continuation<? super R>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(selectInstance, "select");
        Intrinsics.checkParameterIsNotNull(function2, "block");
        Object state = getState$kotlinx_coroutines_core();
        if (state instanceof CompletedExceptionally) {
            selectInstance.resumeSelectCancellableWithException(((CompletedExceptionally) state).cause);
        } else {
            CancellableKt.startCoroutineCancellable(function2, state, selectInstance.getCompletion());
        }
    }
}
