package aero.pilotlog.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.Settings;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import aero.pilotlog.common.MCCPilotLogConst;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tuan.pv on 2015/07/08.
 * Utils
 */
public class Utils {
    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final String KEY_SPLIT = "#$";
    public static final String DEFAULT = "a1b2c3d4e5f6g7h8";

    public static String[] settingSplit(String value) {
        return value.split(KEY_SPLIT);
    }

    /**
     * show a toast
     *
     * @param context context
     * @param message message
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * set color and style for each line of text view
     *
     * @param textView Text View
     * @param text     Text
     * @param color    Color
     * @param isBold   Style
     */
    public static void appendColoredText(TextView textView, String text, int color, boolean isBold) {
        int start = textView.getText().length();
        Typeface bold = Typeface.DEFAULT_BOLD;
        Typeface normal = Typeface.DEFAULT;
        textView.append(text);
        int end = textView.getText().length();
        Spannable spannableText = (Spannable) textView.getText();
        spannableText.setSpan(new ForegroundColorSpan(color), start, end, 0);
        if (isBold) {
            spannableText.setSpan(new CustomTypefaceSpan("", bold), start, end, 0);
        } else {
            spannableText.setSpan(new CustomTypefaceSpan("", normal), start, end, 0);

        }
    }

    /**
     * Copy file to another destination
     *
     * @param src file source
     * @param dst file destination
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Get byte array from input stream
     *
     * @throws IOException
     */
    public static byte[] getBytes(InputStream is, String pFileName, Context pContext) throws IOException {
//        int len;
//        int size = 1024;
//        byte[] buf;
//
//        if (is instanceof ByteArrayInputStream) {
//            size = is.available();
//            buf = new byte[size];
//            len = is.read(buf, 0, size);
//        } else {
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            buf = new byte[size];
//            while ((len = is.read(buf, 0, size)) != -1)
//                bos.write(buf, 0, len);
//            buf = bos.toByteArray();
//        }

        //TuanPV add new
        StringBuilder stringBuilder = new StringBuilder("");
        try {
            String pathFolder = StorageUtils.getStorageRootFolder(pContext) + File.separator + MCCPilotLogConst.XMLPC_FOLDER;
            File xmlPCFolder = new File(pathFolder);
            if (!xmlPCFolder.exists()) {
                xmlPCFolder.mkdir();
            }
            //number of file max is 10
            try {
                if (xmlPCFolder.exists()) {
                    File[] listOfFiles = xmlPCFolder.listFiles();
                    if (listOfFiles != null && listOfFiles.length > 10) {
                        int sizeDelete = listOfFiles.length - 9;
                        //delete files
                        for (int i = 0; i < sizeDelete; i++) {
                            File fileDelete = new File(listOfFiles[i].getAbsolutePath());
                            fileDelete.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            String UTF8 = "utf8";
            String UTF8 = "windows-1252";
            int BUFFER_SIZE = 8192;

            BufferedReader br = new BufferedReader(new InputStreamReader(is,
                    UTF8), BUFFER_SIZE);
            String str;
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
                stringBuilder.append(System.getProperty("line.separator"));
            }

            //save file from PC to local mobile
            File outputFile = new File(pathFolder, pFileName);
            Writer writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(stringBuilder.toString());
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString().getBytes();
//        return buf;
    }

    /**
     * Convert to Hours or Decimal from Minutes
     *
     * @param pDecimal = "1": convert to decimal, = "0": convert to hours
     * @param pMinutes minutes input
     * @return result strTime
     */
    public static String getHoursFromMinutes(String pDecimal, String pMinutes) {
        String strTime = "";
        int total = 0;
        try {
            if (!TextUtils.isEmpty(pMinutes)) {
                total = Integer.parseInt(pMinutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (total > 0) {
            if (pDecimal.equalsIgnoreCase("1")) {
                float fTime = Math.round(total * 10 / 60.0f) / 10.0f;//+ 0.4f
                strTime = "" + fTime;
            } else {
                int h = total / 60;
                int m = total % 60;
                strTime = (h < 10 ? "0" : "") + h + (m < 10 ? ":0" : ":") + m;
            }
        }

        return strTime;
    }

    /**
     * Convert to Hours or Decimal from Minutes
     *
     * @param pDecimal = "1": convert to decimal, = "0": convert to hours
     * @param pMinutes minutes input
     * @return result strTime
     */
    public static String getHoursFromMinutes(String pDecimal, Integer pMinutes) {
        String strTime = "";
        if (pMinutes != null && pMinutes > 0) {
            if (pDecimal.equalsIgnoreCase("1")) {
                float fTime = Math.round(pMinutes * 10 / 60.0f) / 10.0f;//+ 0.4f
                strTime = "" + fTime;
            } else {
                int h = pMinutes / 60;
                int m = pMinutes % 60;
                strTime = (h < 10 ? "0" : "") + h + (m < 10 ? ":0" : ":") + m;
            }
        }

        return strTime;
    }

    /**
     * parse money to transferred x 100
     * (e.g. 2200 must be displayed as 22.00 or 22,00 according to Mobile system settings).
     *
     * @param pMoney input
     */
    public static String getCostString(int pMoney) {
        if (pMoney > 0 && IsValidNumber(String.valueOf(pMoney))) {
            char symbols = new DecimalFormatSymbols(Locale.getDefault()).getDecimalSeparator();
            int first, second;
            if (pMoney >= 100) {
                first = pMoney / 100;
                second = pMoney % 100;
                return "" + first + symbols + String.format("%02d", second);
            }
            return "" + pMoney;
        }

        return "";
    }

    public static String getHourTotals(String pDecimal, Integer pMinutes) {
        String strNumber;
        String strTime = getHoursFromMinutes(pDecimal, pMinutes);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        try {
            if (pDecimal.equalsIgnoreCase("1")) {
                if (strTime.equals("")) strTime = "0";
                strTime = strTime.replace(",", ".");
                decimalFormat.applyPattern("###,###.#");
                strNumber = decimalFormat.format(Double.parseDouble(strTime));
                if (strTime.equals("0")) strNumber = "0";
            } else {
                if (strTime.equals("")) strTime = "00:00";
                decimalFormat.applyPattern("###,###.#");
                String hours = strTime.substring(0, strTime.indexOf(":"));
                String minutes = strTime.substring(strTime.indexOf(":"));
                strNumber = decimalFormat.format(Integer.parseInt(hours)) + minutes;
            }
        } catch (Exception e) {
            e.printStackTrace();
            strNumber = "";
        }

        return strNumber;
    }

    /**
     * validate number contain digits 0-9, min length is 1, not need max length
     *
     * @param pNumber number need validate
     * @return number after validate
     */
    public static boolean IsValidNumber(String pNumber) {
//        Pattern pattern = Pattern.compile("^[0-9]{1,4}$");
        Pattern pattern = Pattern.compile("^[0-9]{1,}$");
        Matcher matcher = pattern.matcher(pNumber);

        return matcher.matches();
    }

    public static boolean checkStringNotEmptyNull(String pText) {
        return !TextUtils.isEmpty(pText) && !"null".equalsIgnoreCase(pText);
    }


    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    //PL-394 reminder currency
    public static boolean getRemiderCurrency(Context context) {
        try {
            Calendar calendarToDay = Calendar.getInstance(TimeZone.getDefault());
            //calendarToDay.add(Calendar.DATE, 3);
            //calendarToDay.set(Calendar.HOUR, 0);
            //calendarToDay.set(Calendar.MINUTE, 0);
            //calendarToDay.set(Calendar.SECOND, 0);
            long toDay = calendarToDay.getTimeInMillis();
            long dateFlagCurrency = getDateFlagCurrencyFromMemory(context);
            long diff = dateFlagCurrency - toDay;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            //long diffDays = diff / (60 * 1000);
            if (diffDays <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void saveDateFlagCurrencyToMemory(Context context, boolean isAddThreeDay) {
        try {
            Calendar addThreeDay = Calendar.getInstance(TimeZone.getDefault());
            if (isAddThreeDay) {
                addThreeDay.add(Calendar.DATE, 4);
                //addThreeDay.add(Calendar.MINUTE, 4);
            }
            //addThreeDay.set(Calendar.HOUR, 0);
            //addThreeDay.set(Calendar.MINUTE, 0);
            //addThreeDay.set(Calendar.SECOND, 0);
            StorageUtils.writeLongToSharedPref(context, "dateFlagCurrency", addThreeDay.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getDateFlagCurrencyFromMemory(Context context) {
        try {
            long longDayFlagCurrency = StorageUtils.getLongFromSharedPref(context, "dateFlagCurrency", 0);
            if (longDayFlagCurrency != 0) {
                return longDayFlagCurrency;
            } else {
                return new Date().getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Date().getTime();
        }
    }

    public static byte[] getByteArrayFromGUID(String str) {
        UUID uuid = UUID.fromString(str);
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static String getGUIDFromByteArray(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        UUID uuid = new UUID(high, low);
        return uuid.toString();
    }

    public static String getJsonStringFromByteArray(byte[] bytes) {
        String response = "[";
        for (int i = 0; i < bytes.length; i++) {
            response += bytes[i];
            if (i < bytes.length - 1)
                response += ",";
        }
        response += "]";

        return response;
    }

    public static String generateStringGUID() {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        return randomUUIDString;
    }

    public static int signedToUnsigned(byte signed) {
        return signed & 0xff;
    }

    public static byte[] convertSignedToUnSigned(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            int byte2 = bytes[i] & 0x0F;
            bytes[i] = (byte) (byte2);
        }
        return bytes;
    }

    public static String escapeBlobArgument(byte[] bytes) {
        return "X'" + toHex(bytes) + '\'';
    }

    public static String toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int byteValue = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[byteValue >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[byteValue & 0x0F];
        }
        return new String(hexChars);
    }

    public static String getDeviceid(Context context) {
        String deviceId;
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceId.length() > 22) {
            deviceId = deviceId.substring(0, 22);
        } else if (deviceId.length() < 16) {
            deviceId += DEFAULT;
            deviceId = deviceId.substring(0, 16);
        }
        return deviceId;
    }

    public static String getStringJSON(JsonObject jsonObject, String param) {
        return jsonObject.get(param).toString().replace("\"", "");
    }

    public static long getTimeOffset(long currentTimeOffset, int timeZoneCode) {
        Date specialDate;
        switch (timeZoneCode) {
          /*  case 50:
                Date date = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.set(2017, Calendar.JULY, 7);
                specialDate = calendar.getTime();
                if (date.before(specialDate))
                    currentTimeOffset = 150;
                break;*/
        }
        return currentTimeOffset;
    }

    public static long getTimeOffset(long currentTimeOffset, int timeZoneCode, Date currentDate) {
        Date specialDate;
        if (currentDate == null) currentDate = new Date();
        switch (timeZoneCode) {
        /*    case 50:
                Calendar calendar = Calendar.getInstance();
                calendar.set(2017, Calendar.JULY, 7);
                specialDate = calendar.getTime();
                if (currentDate.before(specialDate))
                    currentTimeOffset = 150;
                break;*/
        }
        return currentTimeOffset;
    }

    public static double parseDouble(String value) {
        value = value.replace(",", ".");
        return Double.parseDouble(value);
    }

}
