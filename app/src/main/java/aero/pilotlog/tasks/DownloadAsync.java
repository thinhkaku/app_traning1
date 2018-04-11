package aero.pilotlog.tasks;

import android.os.AsyncTask;

import aero.pilotlog.common.MCCPilotLogConst;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuan.na on 7/31/2015.
 * Download text content from an URL
 */
public abstract class DownloadAsync extends AsyncTask<String, Void, List<String>> {

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> result = new ArrayList<>();
        InputStream in = null;
        HttpURLConnection mHttpURLConnection;
        for (String s : strings) {
            try {
                mHttpURLConnection = (HttpURLConnection) new URL(s).openConnection();
                mHttpURLConnection.setConnectTimeout(10000);
                mHttpURLConnection.setReadTimeout(5000);
                mHttpURLConnection.setRequestMethod("GET");
                mHttpURLConnection.setDoInput(true);
                mHttpURLConnection.connect();
                in = mHttpURLConnection.getInputStream();
                if (in != null) {
                    result.add(convertStreamToString(in));
                }
            } catch (Exception e) {
                e.printStackTrace();
                result.add(null);
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<String> listString) {
        super.onPostExecute(listString);
        onPostDownload(listString);
    }

    public abstract void onPostDownload(List<String> listString);

    /**
     * Get string from input stream. Stream read from url
     *
     * @param is input stream
     * @return String
     * @throws UnsupportedEncodingException
     */
    protected static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Static method of download string from URL. This method will run in called thread instead of background thread.
     *
     * @param pUrl url to call
     * @return string or null
     */
    public static String downloadString(String pUrl) {
        InputStream in;
        HttpURLConnection mHttpURLConnection;
        try {
            mHttpURLConnection = (HttpURLConnection) new URL(pUrl).openConnection();
            mHttpURLConnection.setConnectTimeout(30000);//5000->30000
            mHttpURLConnection.setReadTimeout(60000);//10000->60000
            in = mHttpURLConnection.getInputStream();
            if (in != null) {
                return convertStreamToString(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MCCPilotLogConst.STRING_EMPTY;
    }

    public static boolean downloadFile(String pUrl, String pFilePath) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(pFilePath);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                output.write(data, 0, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return true;
    }
}
