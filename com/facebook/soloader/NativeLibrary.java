package com.facebook.soloader;

import android.util.Log;
import java.util.List;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class NativeLibrary {
    private static final String TAG = NativeLibrary.class.getName();
    @Nullable
    private List<String> mLibraryNames;
    private final Object mLock = new Object();
    private Boolean mLoadLibraries = true;
    private boolean mLibrariesLoaded = false;
    @Nullable
    private volatile UnsatisfiedLinkError mLinkError = null;

    protected NativeLibrary(List<String> libraryNames) {
        this.mLibraryNames = libraryNames;
    }

    @Nullable
    public boolean loadLibraries() {
        synchronized (this.mLock) {
            if (!this.mLoadLibraries.booleanValue()) {
                return this.mLibrariesLoaded;
            }
            try {
                if (this.mLibraryNames != null) {
                    for (String name : this.mLibraryNames) {
                        SoLoader.loadLibrary(name);
                    }
                }
                initialNativeCheck();
                this.mLibrariesLoaded = true;
                this.mLibraryNames = null;
            } catch (UnsatisfiedLinkError error) {
                Log.e(TAG, "Failed to load native lib (initial check): ", error);
                this.mLinkError = error;
                this.mLibrariesLoaded = false;
            } catch (Throwable other) {
                Log.e(TAG, "Failed to load native lib (other error): ", other);
                this.mLinkError = new UnsatisfiedLinkError("Failed loading libraries");
                this.mLinkError.initCause(other);
                this.mLibrariesLoaded = false;
            }
            this.mLoadLibraries = false;
            return this.mLibrariesLoaded;
        }
    }

    public void ensureLoaded() throws UnsatisfiedLinkError {
        if (!loadLibraries()) {
            throw this.mLinkError;
        }
    }

    protected void initialNativeCheck() throws UnsatisfiedLinkError {
    }

    @Nullable
    public UnsatisfiedLinkError getError() {
        return this.mLinkError;
    }
}
