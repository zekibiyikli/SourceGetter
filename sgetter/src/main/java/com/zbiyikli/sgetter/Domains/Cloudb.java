package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.ArrayList;

public class Cloudb extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            String baseUrl=strings[0].replace("https","http");
            Document doc = Jsoup.connect(baseUrl)
                    .get();

            String[] split1=doc.html().split("mp4\\|");
            String[] split2=split1[1].split("\\|sources");

            url="http://cloudb.me/"+split2[0]+"/v.mp4";


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        xModels = new ArrayList<>(1);
        XModel xModel = new XModel();
        if (!url.equals("")){
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }
}
