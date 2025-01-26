//Copyright (c) 2011 Kenneth Evans
//
//Permission is hereby granted, free of charge, to any person obtaining
//a copy of this software and associated documentation files (the
//"Software"), to deal in the Software without restriction, including
//without limitation the rights to use, copy, modify, merge, publish,
//distribute, sublicense, and/or sell copies of the Software, and to
//permit persons to whom the Software is furnished to do so, subject to
//the following conditions:
//
//The above copyright notice and this permission notice shall be included
//in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
//IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
//CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
//TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
//SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

package com.net.kenevans.android.duplicateimagehandler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import com.example.demo_full.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class Utils implements IConstants {
    /**
     * General alert dialog.
     *
     * @param context The context.
     * @param title   The dialog title.
     * @param msg     The dialog message.
     */
    @SuppressWarnings("unused")
    private static void alert(Context context, String title, String msg) {
        try {
            AlertDialog alertDialog =
                    new AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(msg)
                            .setPositiveButton(context.getText(R.string.ok),
                                    (dialog, which) -> dialog.cancel()).create();
            alertDialog.show();
        } catch (Throwable t) {
            Log.e(getContextTag(context), "Error using " + title
                    + "AlertDialog\n" + t + "\n" + t.getMessage());
        }
    }

    /**
     * Error message dialog.
     *
     * @param context The context.
     * @param msg     The dialog message.
     */
    @SuppressWarnings("unused")
    static void errMsg(Context context, String msg) {
        Log.e(TAG, getContextTag(context) + msg);
        alert(context, context.getText(R.string.error).toString(), msg);
    }

    /**
     * Error message dialog.
     *
     * @param context The context.
     * @param msg     The dialog message.
     */
    @SuppressWarnings("unused")
    public static void warnMsg(Context context, String msg) {
        Log.w(TAG, getContextTag(context) + msg);
        alert(context, context.getText(R.string.warning).toString(), msg);
    }

    /**
     * Info message dialog.
     *
     * @param context The context.
     * @param msg     The dialog message.
     */
    @SuppressWarnings("unused")
    static void infoMsg(Context context, String msg) {
        Log.i(TAG, getContextTag(context) + msg);
        alert(context, context.getText(R.string.info).toString(), msg);
    }

    /**
     * Exception message dialog. Displays message plus the exception and
     * exception message.
     *
     * @param context The context.
     * @param msg     The dialog message.
     * @param t       The throwable.
     */
    @SuppressWarnings("unused")
    static void excMsg(Context context, String msg, Throwable t) {
        String fullMsg = msg += "\n"
                + context.getText(R.string.exception) + ": " + t
                + "\n" + t.getMessage();
        Log.e(TAG, getContextTag(context) + msg);
        alert(context, context.getText(R.string.exception).toString(), fullMsg);
    }

    /**
     * Utility method to get a tag representing the Context to associate with a
     * log message.
     *
     * @param context The context.
     * @return The context tag.
     */
    @SuppressWarnings("unused")
    private static String getContextTag(Context context) {
        if (context == null) {
            return "<???>: ";
        }
        return "Utils: " + context.getClass().getSimpleName() + ": ";
    }

    /**
     * Get the stack trace for a throwable as a String.
     *
     * @param t The throwable.
     * @return The stack trace as a String.
     */
    @SuppressWarnings("unused")
    public static String getStackTraceString(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        t.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }

    /**
     * Get the extension of a file.
     *
     * @param file The file.
     * @return The extension without the dot.
     */
    @SuppressWarnings("unused")
    public static String getExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Utility method for printing a hash code in hex.
     *
     * @param obj The object whose hash code is desired.
     * @return The hex-formatted hash code.
     */
    @SuppressWarnings("unused")
    public static String getHashCode(Object obj) {
        if (obj == null) {
            return "null";
        }
        return String.format("%08X", obj.hashCode());
    }

    /**
     * Get the version name for the application with the specified context.
     *
     * @param ctx The context.
     * @return The package name.
     */
    @SuppressWarnings("unused")
    public static String getVersion(Context ctx) {
        String versionName = "NA";
        try {
            versionName = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0).versionName;
        } catch (Exception ex) {
            // Do nothing
        }
        return versionName;
    }

    /**
     * Get the orientation of the device.
     *
     * @param ctx The Context.
     * @return Either "Portrait" or "Landscape".
     */
    @SuppressWarnings("unused")
    public static String getOrientation(Context ctx) {
        int orientation = ctx.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return "Portrait";
        } else {
            return "Landscape";
        }
    }

    /**
     * Utility method to get an info string listing all the keys,value pairs
     * in the given SharedPreferences.
     *
     * @param prefix String with text to prepend to each line, e.g. "    ".
     * @param prefs  The given Preferences.
     * @return The info/
     */
    @SuppressWarnings("unused")
    public static String getSharedPreferencesInfo(String prefix,
                                                  SharedPreferences prefs) {
        Map<String, ?> map = prefs.getAll();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(prefix).append("key=").append(key)
                    .append(" value=").append(value).append("\n");
        }
        return sb.toString();
    }

    /**
     * Gets a string of the form Days:HH:MM:SS for the difference of the
     * given start and end dates.
     *
     * @param start The start date;
     * @param end   The end date.
     * @return The formatted String.
     */
    @SuppressWarnings("unused")
    public static String getDurationString(Date start, Date end) {
        return getDurationString(start.getTime(), end.getTime());
    }

    /**
     * Gets a string of the form Days:HH:MM:SS for the difference of the
     * given start and end times.
     *
     * @param start The start time;
     * @param end   The end time.
     * @return The formatted String.
     */
    @SuppressWarnings("unused")
    public static String getDurationString(long start, long end) {
        // d1, d2 are dates
        long diff = end - start;

        long seconds = diff / 1000 % 60;
        long minutes = diff / (60 * 1000) % 60;
        long hours = diff / (60 * 60 * 1000) % 24;
        long days = diff / (24 * 60 * 60 * 1000);
        if (days > 0) {
            return String.format(Locale.US, "%d:%02d:%02d:%02d", days, hours,
                    minutes,
                    seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Convert bytes to human readable shorter string using SI (1 k = 1,000).
     *
     * @param bytes Size in bytes.
     * @return Human readable String.
     */
    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

    /**
     * Convert bytes to human readable shorter string using Binary (1 Ki = 1,
     * 024).
     *
     * @param bytes Size in bytes.
     * @return Human readable String.
     */
    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }
}
