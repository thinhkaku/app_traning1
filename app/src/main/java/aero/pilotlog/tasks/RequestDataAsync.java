package aero.pilotlog.tasks;

import android.content.Context;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import aero.pilotlog.interfaces.OnloadRequest;

public class RequestDataAsync extends AsyncTask<String,Void,String>{
    private OnloadRequest onloadRequest;
    private Context context;

    public RequestDataAsync(OnloadRequest onloadRequest, Context context) {
        this.onloadRequest = onloadRequest;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String link = params[0];
        URL url = null;
        try {
            url = new URL(link);
            URLConnection connection =url.openConnection();
            InputStream inputStream = connection.getInputStream();
            byte[]b = new byte[1024];
            int count = inputStream.read(b);
            String s ="";
            while (count!=-1){
                s+=new String(b,0,count,"utf-8");
                count =inputStream.read(b);

            }
            inputStream.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s==null ||s.isEmpty()){
            onloadRequest.onErorr();
        }else {
            onloadRequest.onSucess(s);
        }
    }
}
