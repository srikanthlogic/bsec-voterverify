package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
/* loaded from: classes.dex */
public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_INTEROP_ACTION = "androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_LINK_URI_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_INTEROP_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_OPTS_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_INTEROP_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER_KEY = "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    /* loaded from: classes.dex */
    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    static boolean handlePerformPrivateCommand(String action, Bundle data, OnCommitContentListener onCommitContentListener) {
        boolean interop;
        int i = 0;
        if (data == null) {
            return false;
        }
        if (TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", action)) {
            interop = false;
        } else if (!TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", action)) {
            return false;
        } else {
            interop = true;
        }
        ResultReceiver resultReceiver = null;
        boolean result = false;
        if (interop) {
        }
        try {
            resultReceiver = (ResultReceiver) data.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            if (interop) {
            }
            Uri contentUri = (Uri) data.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI");
            if (interop) {
            }
            ClipDescription description = (ClipDescription) data.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
            if (interop) {
            }
            Uri linkUri = (Uri) data.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
            if (interop) {
            }
            int flags = data.getInt("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
            if (interop) {
            }
            Bundle opts = (Bundle) data.getParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
            if (!(contentUri == null || description == null)) {
                result = onCommitContentListener.onCommitContent(new InputContentInfoCompat(contentUri, description, linkUri), flags, opts);
            }
            if (resultReceiver != null) {
                if (result) {
                    i = 1;
                }
                resultReceiver.send(i, null);
            }
            return result;
        } catch (Throwable th) {
            if (resultReceiver != null) {
                resultReceiver.send(0, null);
            }
            throw th;
        }
    }

    public static boolean commitContent(InputConnection inputConnection, EditorInfo editorInfo, InputContentInfoCompat inputContentInfo, int flags, Bundle opts) {
        boolean interop;
        ClipDescription description = inputContentInfo.getDescription();
        boolean supported = false;
        String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        int length = contentMimeTypes.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (description.hasMimeType(contentMimeTypes[i])) {
                supported = true;
                break;
            } else {
                i++;
            }
        }
        if (!supported) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo) inputContentInfo.unwrap(), flags, opts);
        }
        int protocol = EditorInfoCompat.getProtocol(editorInfo);
        if (protocol == 2) {
            interop = true;
        } else if (protocol != 3 && protocol != 4) {
            return false;
        } else {
            interop = false;
        }
        Bundle params = new Bundle();
        if (interop) {
        }
        params.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI", inputContentInfo.getContentUri());
        if (interop) {
        }
        params.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", inputContentInfo.getDescription());
        if (interop) {
        }
        params.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", inputContentInfo.getLinkUri());
        if (interop) {
        }
        params.putInt("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", flags);
        if (interop) {
        }
        params.putParcelable("androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", opts);
        if (interop) {
        }
        return inputConnection.performPrivateCommand("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", params);
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        } else if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        } else if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        } else if (Build.VERSION.SDK_INT >= 25) {
            return new InputConnectionWrapper(inputConnection, false) { // from class: androidx.core.view.inputmethod.InputConnectionCompat.1
                @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                    if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, flags, opts);
                }
            };
        } else {
            if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
                return inputConnection;
            }
            return new InputConnectionWrapper(inputConnection, false) { // from class: androidx.core.view.inputmethod.InputConnectionCompat.2
                @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                public boolean performPrivateCommand(String action, Bundle data) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(action, data, onCommitContentListener)) {
                        return true;
                    }
                    return super.performPrivateCommand(action, data);
                }
            };
        }
    }
}
