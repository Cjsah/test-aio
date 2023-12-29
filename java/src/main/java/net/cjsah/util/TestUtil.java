package net.cjsah.util;

@SuppressWarnings("unused")
public class TestUtil {
    public static void printStack() {
        StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        for (StackTraceElement trace : traces) {
            System.out.println(trace);
        }
    }
}
