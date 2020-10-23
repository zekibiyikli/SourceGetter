package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Vidcloud9 extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    String baseUrl="";
    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {

        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        Document doc;
        try {
            /*
            doc = Jsoup.connect(strings[0])
                    .get();

            String[] split1=doc.html().split("sources:");
            String[] split2=split1[1].split(",\n                              ");

            JSONArray jsonArray = new JSONArray(split2[0]);

            for(int i=0;i<jsonArray.length();i++) {
                JSONObject obj=new JSONObject(jsonArray.get(i).toString());
                System.out.println(obj.toString());
                String url=obj.getString("file");
                String quality=obj.getString("label");
                movieUrls.add(url);
                movieQualities.add(quality);
            }


            doc=Jsoup.connect(strings[0]).get();
            Elements elements=doc.getElementsByClass("linkserver");

            for(int i=0;i<elements.size();i++) {
                String u=elements.get(i).attr("data-video");
                if(u.contains("movcloud")){
                    baseUrl=u;
                }
            }*/
            /*
            Log.e("URL-class",strings[0]);
            String baseUrl="";
            doc=Jsoup.connect(strings[0]).get();
            String[] elements=doc.getElementsByTag("script").get(2).toString().split("'");

            for(int i=0;i<elements.length;i++) {
                String u=elements[i];
                if(u.contains(".m3u8")) {
                    Log.e("URL-class",u);
                    getRequest(u);
                    break;
                }
            }
             */
            doc=Jsoup.connect(strings[0]).get();
            String resultUrl=doc.html().split("\\{file: '")[1].split("'")[0];
            url=resultUrl;
//            Elements elements=doc.getElementsByClass("linkserver");
//            for(int i=0;i<elements.size();i++) {
//                String source=elements.get(i).attr("data-video");
//                if(source.contains("movcloud") || source.contains("gcloud")) {
//                    url=source;
//                    break;
//                }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
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

    public static void getRequest(String baseUrl){
        try{
            Log.e("Zekiii",baseUrl+"------------------- ");

            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Referer", "https://vidcloud9.com/");
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();

            linkParseVidcloud(content.toString(),baseUrl);
        }catch (Exception e){

        }
    }

    public static void linkParseVidcloud(String text,String baseUrl) {
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                if(split1.length>1) {
                    String[] split2=split1[1].split(",NAME");
                    urls.add(baseUrl.replace("sub.0.m3u8", t[i+1])+"+++https://vidcloud9.com/");
                    qualities.add(split2[0]);
                    System.out.println(baseUrl.replace("sub.0.m3u8", t[i+1])+"+++https://vidcloud9.com/");
                    System.out.println(split2[0]);
                }
            }
        }
    }

}
