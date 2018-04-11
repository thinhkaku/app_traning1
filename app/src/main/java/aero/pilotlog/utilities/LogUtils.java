package aero.pilotlog.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by tuan.pv on 2015/07/03.
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    private static boolean LOG_ENABLE = true;
    private static final boolean DETAIL_ENABLE = true;
    private static final String LOG_FORMAT = "%1$s\n%2$s";

    private LogUtils() {
    }

    private static String buildMsg(String msg) {
        final StringBuilder buffer = new StringBuilder();
        if (DETAIL_ENABLE) {
            final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
            buffer.append("[ ");
            buffer.append(Thread.currentThread().getName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getFileName());
            buffer.append(": ");
            buffer.append(stackTraceElement.getLineNumber());
            buffer.append(": ");
            buffer.append(stackTraceElement.getMethodName());
            buffer.append("() ] _____ ");
        }
        buffer.append(msg);

        return buffer.toString();
    }

    public static void d(String msg) {
        log(Log.DEBUG, TAG, buildMsg(msg), false, null);
    }

    public static void i(String msg) {
        log(Log.INFO, TAG, buildMsg(msg), false, null);
    }

    public static void e(String msg) {
        log(Log.ERROR, TAG, buildMsg(msg), false, null);
    }

    public static void d(final String pTag, final String pLog, final boolean isWriteToLogFile) {
        log(Log.DEBUG, pTag, buildMsg(pLog), isWriteToLogFile, null);
    }

    public static void d(final String pTag, final String pLog) {
        log(Log.DEBUG, pTag, buildMsg(pLog), false, null);
    }

    public static void e(final String pTag, final String pLog, final boolean isWriteToLogFile) {
        log(Log.ERROR, pTag, buildMsg(pLog), isWriteToLogFile, null);
    }

    public static void e(final String pTag, final String pLog) {
        log(Log.ERROR, pTag, buildMsg(pLog), false, null);
    }

    public static void e(final String pTag, final Throwable pThrowable) {
        String log = "message is null";
        if (pThrowable != null) {
            final String logMessage = pThrowable.getMessage();
            final String logBody = Log.getStackTraceString(pThrowable);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }
        log(Log.ERROR, pTag, log, false, null);
    }

    public static void i(final String pTag, final String pLog, final boolean isWriteToLogFile) {
        log(Log.INFO, pTag, buildMsg(pLog), isWriteToLogFile, null);
    }

    public static void i(final String pTag, final String pLog) {
        log(Log.INFO, pTag, buildMsg(pLog), false, null);
    }

    /**
     * Enables logger (if {@link #disableLogging()} was called before)
     */
    public static void enableLogging() {
        LOG_ENABLE = true;
    }

    /**
     * Disables logger, no logs will be passed to LogCat, all log methods will do nothing
     */
    public static void disableLogging() {
        LOG_ENABLE = false;
    }

    private static void log(int priority, String pTag, String message, boolean isWriteToLogFile, Object... args) {
        if (!LOG_ENABLE) return;
        if (args != null && args.length > 0) {
            message = String.format(message, args);
        }
        if (pTag == null) {
            pTag = TAG;
        }
        String log = message;
        if (log == null) {
            log = "log message is null!";
        }
        Log.println(priority, pTag, log);
        if (isWriteToLogFile) {
            writeLogToFile(log);
        }
    }

    public static final String LOG_FILE_NAME = "mccPILOTLOG_SyncLog.txt";
    public static final File LOG_FILE_PATH = Environment.getExternalStorageDirectory();

    public static synchronized void writeLogToFile(final String log) {
        final File logFile = new File(StorageUtils.getStorageRootFolder(MCCApplication.getInstance()), LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ignored) {
            }
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile, true)));
            pw.println(log);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }

    public static void deleteLogFile() {
        try {
            File logFile = new File(StorageUtils.getStorageRootFolder(MCCApplication.getInstance()), LOG_FILE_NAME);
            logFile.delete();
        } catch (Exception ignored) {
        }
    }

}
