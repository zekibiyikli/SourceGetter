package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class VidTodo extends AsyncTask<String, Void, Void> {

    static ArrayList<XModel> xModels;
    static String url = "";
    @Override
    protected Void doInBackground(String... strings) {
        try {
            String resultUrl="";
            String baseUrl=strings[0];

            if(baseUrl.contains("vidtobo")) {
                baseUrl=baseUrl.replace("vidtobo", "vidtodo");
            }

            String[] urlArray=baseUrl.split("/");
            if(urlArray.length==5){
                baseUrl="https://vidtodo.com/"+urlArray[3];
            }

            System.out.println(baseUrl);

            if(baseUrl.contains("embed")){
                resultUrl=baseUrl.replace("vidtodo","vidtobo");
            }else{
                resultUrl=baseUrl.replace("vidtodo.com/","vidtobo.com/embed-");
            }

            System.out.println(baseUrl);
            System.out.println(resultUrl);

            Document doc=Jsoup.connect(resultUrl)
                    .header("Referer", resultUrl)
                    .get();

            Element element=doc.getElementById("vplayer");

            System.out.println(element.child(0).attr("src").split("/")[2]);

            String[] split1=doc.html().split("mp4\\|");
            String[] split2=split1[1].split("\\|sources");
            String[] resultSplit=split2[0].split("\\|");

            for(int i=0;i<resultSplit.length;i++) {
                System.out.println(resultSplit[i]);
            }

            String base=element.child(0).attr("src").split("/")[2];

            url="https://"+base+"/"+resultSplit[resultSplit.length-1]+"/v.mp4";


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    protected void onPostExecute(final Void aVoid) {
        if(!url.equals("")){
            xModels = new ArrayList<>(1);
            final XModel xModel = new XModel();
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }

/*
    public void metaData(String metaDataUrl) {
        try {
            Document doc = Jsoup
                    .connect(metaDataUrl)
                    .header("cookie", cookie)
                    .userAgent(user_agent)
                    .header("accept", accept)
                    .header("accept-language", accept_language)
                    .header("cache-control", cache_control)
                    .header("sec-fetch-mode", sec_fetch_mode)
                    .header("sec-fetch-site", sec_fetch_site)
                    .header("sec-fetch-user", sec_fetch_user)
                    .header("upgrade-insecure-requests", upgrade_insecure_requests)
                    .header("referer", metaDataUrl)
                    .get();

            Elements elements=doc.select("video[id=hola]");
            String[] baseUrl=elements.get(0).attr("poster").split("/i");

            String[] split1=doc.html().split("\\|sources");
            String[] split2=split1[0].split("\\|");

            String url2=baseUrl[0]+"/"+split2[split2.length-1]+"/v.mp4";
            url=url2;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
