package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import com.zbiyikli.sgetter.SourceGetter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Bitly extends AsyncTask<String, Void, Void> {
    String url = "";

    @Override
    protected Void doInBackground(String... strings) {
        try{
            final URL urll = new URL(strings[0]);
            final HttpURLConnection urlConnection =(HttpURLConnection) urll.openConnection();
            urlConnection.setInstanceFollowRedirects(false);

            url= urlConnection.getHeaderField("location");
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        if(!url.equals("")){
            SourceGetter.onComplete.goBitLY(url);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}
