package android.util;

public class Log {
    public static int e(String tag, String msg) {
        System.out.println(tag + ", " + msg);
        return 1;
    }
}
