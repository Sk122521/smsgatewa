package com.api.smsgateway.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class FileLoggingTree extends Timber.DebugTree {

    private static final SimpleDateFormat LOG_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    private final File logFile;

    public FileLoggingTree() {
        logFile = new File(Environment.getExternalStorageDirectory(), "app_logs.txt");
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        try {
            String logEntry = String.format(
                    "[%s] %s/%s: %s\n",
                    LOG_DATE_FORMAT.format(new Date()),
                    getLogLevelString(priority),
                    tag,
                    message
            );
            writeLogToFile(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLogToFile(String logEntry) throws IOException {
        FileOutputStream fos = new FileOutputStream(logFile, true);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        osw.write(logEntry);
        osw.flush();
        fos.close();
    }

    private String getLogLevelString(int logLevel) {
        switch (logLevel) {
            case Log.VERBOSE:
                return "V";
            case Log.DEBUG:
                return "D";
            case Log.INFO:
                return "I";
            case Log.WARN:
                return "W";
            case Log.ERROR:
                return "E";
            case Log.ASSERT:
                return "A";
            default:
                return "?";
        }
    }
}




