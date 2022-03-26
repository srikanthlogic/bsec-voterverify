package org.zz.tool;

import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
/* loaded from: classes3.dex */
public class FileUnit {
    public static String getSDCardPath() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public static Boolean isExist(String path) {
        if (!new File(path).exists()) {
            return false;
        }
        return true;
    }

    public static Boolean AddDirectory(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return true;
    }

    public static void GetSubFolders(String strMainFolder, List<String> strSubFolders) {
        File[] files = new File(strMainFolder).listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                strSubFolders.add(f.getPath());
            }
        }
    }

    public static void GetSubFiles(String Path, String Extension, List<String> strSubFiles) {
        File[] files = new File(Path).listFiles();
        for (File f : files) {
            if (f.isFile() && f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                strSubFiles.add(f.getPath());
            }
        }
    }

    public static int SaveData(String filepath, byte[] buffer, int size) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filepath));
            try {
                fos.write(buffer, 0, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
                return 0;
            } catch (IOException e2) {
                e2.printStackTrace();
                return -2;
            }
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
            return -1;
        }
    }

    public static long getFileSizes(String filepath) {
        File f = new File(filepath);
        if (!f.exists()) {
            return -1;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return (long) fis.available();
        } catch (IOException e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump */
    public static byte[] ReadData(String filepath) {
        BufferedInputStream in;
        File f = new File(filepath);
        ?? exists = f.exists();
        if (exists == 0) {
            return null;
        }
        try {
            exists = new ByteArrayOutputStream((int) f.length());
            in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(f));
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = in.read(buffer, 0, 1024);
                    if (-1 == len) {
                        break;
                    }
                    exists.write(buffer, 0, len);
                }
                byte[] byteArray = exists.toByteArray();
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    exists.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                return byteArray;
            } catch (IOException e3) {
                e3.printStackTrace();
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
                try {
                    exists.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
                return null;
            }
        } catch (Throwable th) {
            try {
                in.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
            try {
                exists.close();
            } catch (IOException e7) {
                e7.printStackTrace();
            }
            throw th;
        }
    }

    public static boolean deleteFile(String sPath) {
        File file = new File(sPath);
        if (!file.isFile() || !file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

    public static void AppendFile(String fileName, String content, int iPos) {
        RandomAccessFile randomFile = null;
        try {
            try {
                randomFile = new RandomAccessFile(fileName, "rw");
                long fileLength = randomFile.length();
                if (iPos == 0) {
                    fileLength = 0;
                } else if (iPos > 0) {
                    fileLength = (long) iPos;
                }
                randomFile.seek(fileLength);
                randomFile.writeBytes(content);
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        } catch (Throwable e4) {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw e4;
        }
    }

    public static void AppendFile_bk(String fileName, String content, int iPos) {
        RandomAccessFile randomFile;
        try {
            randomFile = null;
            try {
                randomFile = new RandomAccessFile(fileName, "rw");
                long fileLength = randomFile.length();
                if (iPos == 0) {
                    fileLength = 0;
                } else if (iPos > 0) {
                    fileLength = (long) iPos;
                }
                randomFile.seek(fileLength);
                byte[] bArr = new byte[content.length()];
                randomFile.write(content.getBytes());
                try {
                    randomFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                if (randomFile != null) {
                    try {
                        randomFile.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        } catch (Throwable e4) {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
            throw e4;
        }
    }
}
