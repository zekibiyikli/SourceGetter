package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CloudVideo extends AsyncTask<String, Void, Void> {

    ArrayList<XModel> xModels;
    String url = "";
    static ArrayList<String> urls;
    static ArrayList<String> qualities;

    @Override
    protected Void doInBackground(String... strings) {
        urls=new ArrayList<>();
        qualities=new ArrayList<>();

        try {
            String baseUrl=strings[0];
            baseUrl=baseUrl.split("=thumb.cloudvideo.tv")[0].replace("emb.html?", "embed-");
            if (strings[0].contains("=thumb.cloudvideo.tv")){
                baseUrl=baseUrl+".html";
            }

            Document doc = Jsoup.connect(baseUrl).get();
            Elements elements=doc.getElementsByTag("script");
            String script ="";
            for(int i=0;i<elements.size();i++){
                script=elements.get(i).data();
                Log.e("Zeki",script);
                if(script.contains("eval(function(p,a,c,k,e,d)")){
                    break;
                }
            }
            JsUnpacker jsUnpacker = new JsUnpacker(script);

            String json=jsUnpacker.unpack().split("sources:")[1].split("\\}\\);player")[0];
            JSONArray array=new JSONArray(json);
            JSONObject object=new JSONObject(array.get(0).toString());
            String resultUrl=object.getString("src");
            getRequest(resultUrl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(!url.equals("") || !urls.isEmpty()){
            xModels = new ArrayList<>();
            for(int i=0;i<urls.size();i++){
                final XModel xModel = new XModel();
                xModel.setUrl(urls.get(i));
                xModel.setQuality(qualities.get(i));
                xModels.add(xModel);
            }
            SourceGetter.onComplete.onTaskCompleted(xModels, true);
        }else{
            SourceGetter.onComplete.onError();
        }
        super.onPostExecute(aVoid);
    }

    public static void getRequest(String baseurl){
        baseurl=baseurl.replace("https","http");
        try {
            URL url = new URL(baseurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-15")));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
                content.append("\n");
            }
            in.close();
            linkParse(content.toString());
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
    }

    public static void linkParse(String text) {
        String[] t= text.split("\n");
        for(int i=1;i<t.length-1;i=i+2) {
            if(!t[i].equals("")) {
                String[] split1=t[i].split("RESOLUTION=");
                String[] split2=split1[1].split(",FRAME-RATE");
                urls.add(t[i+1]);
                qualities.add(split2[0]);
            }
        }
    }

}
