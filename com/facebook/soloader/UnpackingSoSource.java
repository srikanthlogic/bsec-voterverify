package com.facebook.soloader;

import android.content.Context;
import android.os.Parcel;
import android.os.StrictMode;
import android.util.Log;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
/* loaded from: classes.dex */
public abstract class UnpackingSoSource extends DirectorySoSource {
    private static final String DEPS_FILE_NAME;
    private static final String LOCK_FILE_NAME;
    private static final String MANIFEST_FILE_NAME;
    private static final byte MANIFEST_VERSION;
    private static final byte STATE_CLEAN;
    private static final byte STATE_DIRTY;
    private static final String STATE_FILE_NAME;
    private static final String TAG;
    @Nullable
    private String[] mAbis;
    protected final Context mContext;
    @Nullable
    protected String mCorruptedLib;
    private final Map<String, Object> mLibsBeingLoaded = new HashMap();

    protected abstract Unpacker makeUnpacker() throws IOException;

    public UnpackingSoSource(Context context, String name) {
        super(getSoStorePath(context, name), 1);
        this.mContext = context;
    }

    protected UnpackingSoSource(Context context, File storePath) {
        super(storePath, 1);
        this.mContext = context;
    }

    public static File getSoStorePath(Context context, String name) {
        return new File(context.getApplicationInfo().dataDir + "/" + name);
    }

    @Override // com.facebook.soloader.SoSource
    public String[] getSoSourceAbis() {
        String[] strArr = this.mAbis;
        if (strArr == null) {
            return super.getSoSourceAbis();
        }
        return strArr;
    }

    public void setSoSourceAbis(String[] abis) {
        this.mAbis = abis;
    }

    /* loaded from: classes.dex */
    public static class Dso {
        public final String hash;
        public final String name;

        public Dso(String name, String hash) {
            this.name = name;
            this.hash = hash;
        }
    }

    /* loaded from: classes.dex */
    public static final class DsoManifest {
        public final Dso[] dsos;

        public DsoManifest(Dso[] dsos) {
            this.dsos = dsos;
        }

        static final DsoManifest read(DataInput xdi) throws IOException {
            if (xdi.readByte() == 1) {
                int nrDso = xdi.readInt();
                if (nrDso >= 0) {
                    Dso[] dsos = new Dso[nrDso];
                    for (int i = 0; i < nrDso; i++) {
                        dsos[i] = new Dso(xdi.readUTF(), xdi.readUTF());
                    }
                    return new DsoManifest(dsos);
                }
                throw new RuntimeException("illegal number of shared libraries");
            }
            throw new RuntimeException("wrong dso manifest version");
        }

        public final void write(DataOutput xdo) throws IOException {
            xdo.writeByte(1);
            xdo.writeInt(this.dsos.length);
            int i = 0;
            while (true) {
                Dso[] dsoArr = this.dsos;
                if (i < dsoArr.length) {
                    xdo.writeUTF(dsoArr[i].name);
                    xdo.writeUTF(this.dsos[i].hash);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class InputDso implements Closeable {
        public final InputStream content;
        public final Dso dso;

        public InputDso(Dso dso, InputStream content) {
            this.dso = dso;
            this.content = content;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.content.close();
        }
    }

    /* loaded from: classes.dex */
    public static abstract class InputDsoIterator implements Closeable {
        public abstract boolean hasNext();

        public abstract InputDso next() throws IOException;

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Unpacker implements Closeable {
        protected abstract DsoManifest getDsoManifest() throws IOException;

        protected abstract InputDsoIterator openDsoIterator() throws IOException;

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }
    }

    public static void writeState(File stateFileName, byte state) throws IOException {
        RandomAccessFile stateFile = new RandomAccessFile(stateFileName, "rw");
        try {
            stateFile.seek(0);
            stateFile.write(state);
            stateFile.setLength(stateFile.getFilePointer());
            stateFile.getFD().sync();
            stateFile.close();
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                try {
                    stateFile.close();
                } catch (Throwable th3) {
                    th.addSuppressed(th3);
                }
                throw th2;
            }
        }
    }

    private void deleteUnmentionedFiles(Dso[] dsos) throws IOException {
        String[] existingFiles = this.soDirectory.list();
        if (existingFiles != null) {
            for (String fileName : existingFiles) {
                if (!fileName.equals(STATE_FILE_NAME) && !fileName.equals(LOCK_FILE_NAME) && !fileName.equals(DEPS_FILE_NAME) && !fileName.equals(MANIFEST_FILE_NAME)) {
                    boolean found = false;
                    int j = 0;
                    while (!found && j < dsos.length) {
                        if (dsos[j].name.equals(fileName)) {
                            found = true;
                        }
                        j++;
                    }
                    if (!found) {
                        File fileNameToDelete = new File(this.soDirectory, fileName);
                        Log.v(TAG, "deleting unaccounted-for file " + fileNameToDelete);
                        SysUtil.dumbDeleteRecursive(fileNameToDelete);
                    }
                }
            }
            return;
        }
        throw new IOException("unable to list directory " + this.soDirectory);
    }

    private void extractDso(InputDso iDso, byte[] ioBuffer) throws IOException {
        RandomAccessFile dsoFile;
        Log.i(TAG, "extracting DSO " + iDso.dso.name);
        if (this.soDirectory.setWritable(true, true)) {
            File dsoFileName = new File(this.soDirectory, iDso.dso.name);
            try {
                dsoFile = new RandomAccessFile(dsoFileName, "rw");
            } catch (IOException ex) {
                Log.w(TAG, "error overwriting " + dsoFileName + " trying to delete and start over", ex);
                SysUtil.dumbDeleteRecursive(dsoFileName);
                dsoFile = new RandomAccessFile(dsoFileName, "rw");
            }
            try {
                try {
                    int sizeHint = iDso.content.available();
                    if (sizeHint > 1) {
                        SysUtil.fallocateIfSupported(dsoFile.getFD(), (long) sizeHint);
                    }
                    SysUtil.copyBytes(dsoFile, iDso.content, Integer.MAX_VALUE, ioBuffer);
                    dsoFile.setLength(dsoFile.getFilePointer());
                    if (!dsoFileName.setExecutable(true, false)) {
                        throw new IOException("cannot make file executable: " + dsoFileName);
                    }
                } catch (IOException e) {
                    SysUtil.dumbDeleteRecursive(dsoFileName);
                    throw e;
                }
            } finally {
                dsoFile.close();
            }
        } else {
            throw new IOException("cannot make directory writable for us: " + this.soDirectory);
        }
    }

    private void regenerate(byte state, DsoManifest desiredManifest, InputDsoIterator dsoIterator) throws IOException {
        Log.v(TAG, "regenerating DSO store " + getClass().getName());
        RandomAccessFile manifestFile = new RandomAccessFile(new File(this.soDirectory, MANIFEST_FILE_NAME), "rw");
        DsoManifest existingManifest = null;
        if (state == 1) {
            try {
                try {
                    existingManifest = DsoManifest.read(manifestFile);
                } catch (Exception ex) {
                    Log.i(TAG, "error reading existing DSO manifest", ex);
                }
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    try {
                        manifestFile.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                    throw th2;
                }
            }
        }
        if (existingManifest == null) {
            existingManifest = new DsoManifest(new Dso[0]);
        }
        deleteUnmentionedFiles(desiredManifest.dsos);
        byte[] ioBuffer = new byte[32768];
        while (dsoIterator.hasNext()) {
            InputDso iDso = dsoIterator.next();
            boolean obsolete = true;
            int i = 0;
            while (obsolete && i < existingManifest.dsos.length) {
                if (existingManifest.dsos[i].name.equals(iDso.dso.name) && existingManifest.dsos[i].hash.equals(iDso.dso.hash)) {
                    obsolete = false;
                }
                i++;
            }
            if (obsolete) {
                extractDso(iDso, ioBuffer);
            }
            if (iDso != null) {
                iDso.close();
            }
        }
        manifestFile.close();
        Log.v(TAG, "Finished regenerating DSO store " + getClass().getName());
    }

    private boolean refreshLocked(final FileLocker lock, int flags, final byte[] deps) throws IOException {
        byte state;
        Throwable th;
        Throwable th2;
        byte state2;
        final DsoManifest desiredManifest;
        final File stateFileName = new File(this.soDirectory, STATE_FILE_NAME);
        RandomAccessFile stateFile = new RandomAccessFile(stateFileName, "rw");
        try {
            byte state3 = stateFile.readByte();
            if (state3 != 1) {
                Log.v(TAG, "dso store " + this.soDirectory + " regeneration interrupted: wiping clean");
                state3 = 0;
            }
            state = state3;
        } catch (EOFException e) {
            state = 0;
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                try {
                    stateFile.close();
                } catch (Throwable th5) {
                    th3.addSuppressed(th5);
                }
                throw th4;
            }
        }
        stateFile.close();
        final File depsFileName = new File(this.soDirectory, DEPS_FILE_NAME);
        DsoManifest desiredManifest2 = null;
        RandomAccessFile depsFile = new RandomAccessFile(depsFileName, "rw");
        try {
            byte[] existingDeps = new byte[(int) depsFile.length()];
            if (depsFile.read(existingDeps) != existingDeps.length) {
                Log.v(TAG, "short read of so store deps file: marking unclean");
                state = 0;
            }
            try {
                if (!Arrays.equals(existingDeps, deps)) {
                    Log.v(TAG, "deps mismatch on deps store: regenerating");
                    state2 = 0;
                } else {
                    state2 = state;
                }
                if (state2 == 0 || (flags & 2) != 0) {
                    try {
                        Log.v(TAG, "so store dirty: regenerating");
                        writeState(stateFileName, (byte) 0);
                        Unpacker u = makeUnpacker();
                        try {
                            desiredManifest2 = u.getDsoManifest();
                            InputDsoIterator idi = u.openDsoIterator();
                            regenerate(state2, desiredManifest2, idi);
                            if (idi != null) {
                                idi.close();
                            }
                            if (u != null) {
                                u.close();
                            }
                            desiredManifest = desiredManifest2;
                        } catch (Throwable th6) {
                            try {
                                throw th6;
                            } catch (Throwable th7) {
                                if (u != null) {
                                    try {
                                        u.close();
                                    }
                                }
                                throw th7;
                            }
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        try {
                            throw th;
                        } catch (Throwable th9) {
                            try {
                                depsFile.close();
                            } catch (Throwable th10) {
                                th.addSuppressed(th10);
                            }
                            throw th9;
                        }
                    }
                } else {
                    desiredManifest = null;
                }
                depsFile.close();
                if (desiredManifest == null) {
                    return false;
                }
                Runnable syncer = new Runnable() { // from class: com.facebook.soloader.UnpackingSoSource.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Log.v(UnpackingSoSource.TAG, "starting syncer worker");
                            RandomAccessFile depsFile2 = new RandomAccessFile(depsFileName, "rw");
                            depsFile2.write(deps);
                            depsFile2.setLength(depsFile2.getFilePointer());
                            depsFile2.close();
                            RandomAccessFile manifestFile = new RandomAccessFile(new File(UnpackingSoSource.this.soDirectory, UnpackingSoSource.MANIFEST_FILE_NAME), "rw");
                            desiredManifest.write(manifestFile);
                            manifestFile.close();
                            SysUtil.fsyncRecursive(UnpackingSoSource.this.soDirectory);
                            UnpackingSoSource.writeState(stateFileName, (byte) 1);
                            Log.v(UnpackingSoSource.TAG, "releasing dso store lock for " + UnpackingSoSource.this.soDirectory + " (from syncer thread)");
                            lock.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                };
                if ((flags & 1) != 0) {
                    new Thread(syncer, "SoSync:" + this.soDirectory.getName()).start();
                } else {
                    syncer.run();
                }
                return true;
            } catch (Throwable th11) {
                th2 = th11;
                th = th2;
                throw th;
            }
        } catch (Throwable th12) {
            th2 = th12;
        }
    }

    protected byte[] getDepsBlock() throws IOException {
        Parcel parcel = Parcel.obtain();
        Unpacker u = makeUnpacker();
        try {
            Dso[] dsos = u.getDsoManifest().dsos;
            parcel.writeByte((byte) 1);
            parcel.writeInt(dsos.length);
            for (int i = 0; i < dsos.length; i++) {
                parcel.writeString(dsos[i].name);
                parcel.writeString(dsos[i].hash);
            }
            if (u != null) {
                u.close();
            }
            byte[] depsBlock = parcel.marshall();
            parcel.recycle();
            return depsBlock;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (u != null) {
                    try {
                        u.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        }
    }

    @Override // com.facebook.soloader.SoSource
    public void prepare(int flags) throws IOException {
        SysUtil.mkdirOrThrow(this.soDirectory);
        FileLocker lock = FileLocker.lock(new File(this.soDirectory, LOCK_FILE_NAME));
        try {
            Log.v(TAG, "locked dso store " + this.soDirectory);
            if (refreshLocked(lock, flags, getDepsBlock())) {
                lock = null;
            } else {
                Log.i(TAG, "dso store is up-to-date: " + this.soDirectory);
            }
        } finally {
            if (lock != null) {
                Log.v(TAG, "releasing dso store lock for " + this.soDirectory);
                lock.close();
            } else {
                Log.v(TAG, "not releasing dso store lock for " + this.soDirectory + " (syncer thread started)");
            }
        }
    }

    private Object getLibraryLock(String soName) {
        Object lock;
        synchronized (this.mLibsBeingLoaded) {
            lock = this.mLibsBeingLoaded.get(soName);
            if (lock == null) {
                lock = new Object();
                this.mLibsBeingLoaded.put(soName, lock);
            }
        }
        return lock;
    }

    public synchronized void prepare(String soName) throws IOException {
        synchronized (getLibraryLock(soName)) {
            this.mCorruptedLib = soName;
            prepare(2);
        }
    }

    @Override // com.facebook.soloader.DirectorySoSource, com.facebook.soloader.SoSource
    public int loadLibrary(String soName, int loadFlags, StrictMode.ThreadPolicy threadPolicy) throws IOException {
        int loadLibraryFrom;
        synchronized (getLibraryLock(soName)) {
            loadLibraryFrom = loadLibraryFrom(soName, loadFlags, this.soDirectory, threadPolicy);
        }
        return loadLibraryFrom;
    }
}
