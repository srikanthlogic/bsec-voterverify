package com.facebook.soloader;

import android.content.Context;
import com.facebook.soloader.UnpackingSoSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
/* loaded from: classes.dex */
public final class ExoSoSource extends UnpackingSoSource {
    public ExoSoSource(Context context, String name) {
        super(context, name);
    }

    @Override // com.facebook.soloader.UnpackingSoSource
    protected UnpackingSoSource.Unpacker makeUnpacker() throws IOException {
        return new ExoUnpacker(this);
    }

    /* loaded from: classes.dex */
    private final class ExoUnpacker extends UnpackingSoSource.Unpacker {
        private final FileDso[] mDsos;

        /* JADX WARN: Code restructure failed: missing block: B:36:0x011f, code lost:
            throw new java.lang.RuntimeException("illegal line in exopackage metadata: [" + r14 + "]");
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x0123, code lost:
            r16 = r0;
            r21 = r2;
            r19 = r3;
            r18 = r6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:40:0x012b, code lost:
            r13.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x012e, code lost:
            r12.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x013d, code lost:
            r2 = th;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x0159, code lost:
            throw r2;
         */
        /* JADX WARN: Code restructure failed: missing block: B:51:0x015a, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:53:0x015c, code lost:
            r12.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:54:0x0160, code lost:
            r0 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x0161, code lost:
            r2.addSuppressed(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:56:0x0165, code lost:
            throw r0;
         */
        /* Code decompiled incorrectly, please refer to instructions dump */
        ExoUnpacker(UnpackingSoSource soSource) throws IOException {
            Context context;
            File exoDir;
            int i;
            String[] strArr;
            Throwable th;
            String line;
            File exoDir2;
            Context context2 = ExoSoSource.this.mContext;
            File exoDir3 = new File("/data/local/tmp/exopackage/" + context2.getPackageName() + "/native-libs/");
            ArrayList<FileDso> providedLibraries = new ArrayList<>();
            Set<String> librariesAbiSet = new LinkedHashSet<>();
            String[] supportedAbis = SysUtil.getSupportedAbis();
            int length = supportedAbis.length;
            int i2 = 0;
            loop0: while (i2 < length) {
                String abi = supportedAbis[i2];
                File abiDir = new File(exoDir3, abi);
                if (!abiDir.isDirectory()) {
                    strArr = supportedAbis;
                    context = context2;
                    exoDir = exoDir3;
                    i = length;
                } else {
                    librariesAbiSet.add(abi);
                    File metadataFileName = new File(abiDir, "metadata.txt");
                    if (!metadataFileName.isFile()) {
                        strArr = supportedAbis;
                        context = context2;
                        exoDir = exoDir3;
                        i = length;
                    } else {
                        FileReader fr = new FileReader(metadataFileName);
                        try {
                            BufferedReader br = new BufferedReader(fr);
                            while (true) {
                                try {
                                    line = br.readLine();
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                if (line == null) {
                                    break;
                                } else if (line.length() != 0) {
                                    int sep = line.indexOf(32);
                                    if (sep == -1) {
                                        break loop0;
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    try {
                                        sb.append(line.substring(0, sep));
                                        sb.append(".so");
                                        String soName = sb.toString();
                                        int nrAlreadyProvided = providedLibraries.size();
                                        boolean found = false;
                                        int i3 = 0;
                                        while (true) {
                                            if (i3 >= nrAlreadyProvided) {
                                                exoDir2 = exoDir3;
                                                break;
                                            }
                                            exoDir2 = exoDir3;
                                            try {
                                                if (providedLibraries.get(i3).name.equals(soName)) {
                                                    found = true;
                                                    break;
                                                } else {
                                                    i3++;
                                                    exoDir3 = exoDir2;
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                            }
                                        }
                                        if (found) {
                                            context2 = context2;
                                            supportedAbis = supportedAbis;
                                            exoDir3 = exoDir2;
                                        } else {
                                            String backingFileBaseName = line.substring(sep + 1);
                                            providedLibraries.add(new FileDso(soName, backingFileBaseName, new File(abiDir, backingFileBaseName)));
                                            context2 = context2;
                                            supportedAbis = supportedAbis;
                                            length = length;
                                            exoDir3 = exoDir2;
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                    }
                                    th = th3;
                                    throw th;
                                }
                            }
                        } catch (Throwable th5) {
                            Throwable th6 = th5;
                        }
                    }
                }
                i2++;
                context2 = context;
                supportedAbis = strArr;
                length = i;
                exoDir3 = exoDir;
            }
            soSource.setSoSourceAbis((String[]) librariesAbiSet.toArray(new String[librariesAbiSet.size()]));
            this.mDsos = (FileDso[]) providedLibraries.toArray(new FileDso[providedLibraries.size()]);
        }

        @Override // com.facebook.soloader.UnpackingSoSource.Unpacker
        protected UnpackingSoSource.DsoManifest getDsoManifest() throws IOException {
            return new UnpackingSoSource.DsoManifest(this.mDsos);
        }

        @Override // com.facebook.soloader.UnpackingSoSource.Unpacker
        protected UnpackingSoSource.InputDsoIterator openDsoIterator() throws IOException {
            return new FileBackedInputDsoIterator();
        }

        /* loaded from: classes.dex */
        private final class FileBackedInputDsoIterator extends UnpackingSoSource.InputDsoIterator {
            private int mCurrentDso;

            private FileBackedInputDsoIterator() {
            }

            @Override // com.facebook.soloader.UnpackingSoSource.InputDsoIterator
            public boolean hasNext() {
                return this.mCurrentDso < ExoUnpacker.this.mDsos.length;
            }

            @Override // com.facebook.soloader.UnpackingSoSource.InputDsoIterator
            public UnpackingSoSource.InputDso next() throws IOException {
                FileDso[] fileDsoArr = ExoUnpacker.this.mDsos;
                int i = this.mCurrentDso;
                this.mCurrentDso = i + 1;
                FileDso fileDso = fileDsoArr[i];
                FileInputStream dsoFile = new FileInputStream(fileDso.backingFile);
                try {
                    UnpackingSoSource.InputDso ret = new UnpackingSoSource.InputDso(fileDso, dsoFile);
                    dsoFile = null;
                    if (dsoFile != null) {
                    }
                    return ret;
                } finally {
                    dsoFile.close();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static final class FileDso extends UnpackingSoSource.Dso {
        final File backingFile;

        FileDso(String name, String hash, File backingFile) {
            super(name, hash);
            this.backingFile = backingFile;
        }
    }
}
