package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLDecoder;
import java.util.ArrayList;

public class Delivembed extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    public static ArrayList<String> movieUrls;
    public static ArrayList<String> movieQualities;

    @Override
    protected Void doInBackground(String... strings) {

        movieUrls=new ArrayList<>();
        movieQualities=new ArrayList<>();

        try {
            Document doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1=doc.html().split("hlsList:");
            String[] split2=split1[1].trim().substring(1).split("\\}");
            System.out.println(split2[0]);
            String[] resultSplit=split2[0].split(",");
            for (int i=0;i<resultSplit.length;i++) {
                String[] middleSplit=resultSplit[i].split("\":\"");
                String quality=middleSplit[0].substring(1);
                String url=middleSplit[1].substring(0,middleSplit[1].length()-1);
                String decodedUrl=URLDecoder.decode(url, "UTF-8");
                String[] urlArray=decodedUrl.split("video=");
                url=urlArray[1];
                movieUrls.add(url);
                movieQualities.add(quality);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        xModels = new ArrayList<>(1);
        if (!movieUrls.isEmpty()){
            for(int i=0;i<movieUrls.size();i++){
                XModel xModel = new XModel();
                xModel.setUrl(movieUrls.get(i));
                xModel.setQuality(movieQualities.get(i));
                Log.e("Link",movieUrls.get(i));
                Log.e("Quality",movieQualities.get(i));
                xModels.add(xModel);
            }
            Log.e("XModels",String.valueOf(xModels.size()));
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}

