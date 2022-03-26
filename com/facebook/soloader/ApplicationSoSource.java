package com.facebook.soloader;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public class ApplicationSoSource extends SoSource {
    private Context applicationContext;
    private int flags;
    private DirectorySoSource soSource;

    public ApplicationSoSource(Context context, int flags) {
        this.applicationContext = context.getApplicationContext();
        if (this.applicationContext == null) {
            Log.w("SoLoader", "context.getApplicationContext returned null, holding reference to original context.");
            this.applicationContext = context;
        }
        this.flags = flags;
        this.soSource = new DirectorySoSource(new File(this.applicationContext.getApplicationInfo().nativeLibraryDir), flags);
    }

    public boolean checkAndMaybeUpdate() throws IOException {
        try {
            File nativeLibDir = this.soSource.soDirectory;
            Context updatedContext = this.applicationContext.createPackageContext(this.applicationContext.getPackageName(), 0);
            File updatedNativeLibDir = new File(updatedContext.getApplicationInfo().nativeLibraryDir);
            if (nativeLibDir.equals(updatedNativeLibDir)) {
                return false;
            }
            Log.d("SoLoader", "Native library directory updated from " + nativeLibDir + " to " + updatedNativeLibDir);
            this.flags = this.flags | 1;
            this.soSource = new DirectorySoSource(updatedNativeLibDir, this.flags);
            this.soSource.prepare(this.flags);
            this.applicationContext = updatedContext;
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // com.facebook.soloader.SoSource
    public int loadLibrary(String soName, int loadFlags, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        return this.soSource.loadLibrary(soName, loadFlags, threadPolicy);
    }

    @Override // com.facebook.soloader.SoSource
    @Nullable
    public File unpackLibrary(String soName) throws IOException {
        return this.soSource.unpackLibrary(soName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.facebook.soloader.SoSource
    public void prepare(int flags) throws IOException {
        this.soSource.prepare(flags);
    }

    @Override // com.facebook.soloader.SoSource
    public void addToLdLibraryPath(Collection<String> paths) {
        this.soSource.addToLdLibraryPath(paths);
    }

    @Override // com.facebook.soloader.SoSource
    public String toString() {
        return this.soSource.toString();
    }
}
