package a.a;

import android.os.Environment;
import com.alcorlink.camera.g;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/* loaded from: classes5.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static a f3a;
    private FileWriter b;
    private boolean c = g.f20a.booleanValue();
    private int d;

    private a() {
        if (this.c) {
            this.d = 0;
            String format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            a("=================" + format + "=================\n");
        }
    }

    public static synchronized a a() {
        a aVar;
        synchronized (a.class) {
            if (f3a == null) {
                f3a = new a();
            }
            aVar = f3a;
        }
        return aVar;
    }

    private void a(String str) {
        if (b()) {
            BufferedWriter bufferedWriter = new BufferedWriter(this.b);
            try {
                bufferedWriter.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bufferedWriter.close();
            } catch (Exception e2) {
            }
        }
    }

    private boolean b() {
        String file = Environment.getExternalStorageDirectory().toString();
        try {
            this.b = new FileWriter(file + "/alCamera.txt", true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
