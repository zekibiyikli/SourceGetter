package com.zbiyikli.sgetter.Domains;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class VidCloud extends AsyncTask<String, Void, Void> {
    ArrayList<XModel> xModels;
    String url = "";
    boolean isInside=false;
    Void v;

    @Override
    protected Void doInBackground(String... strings) {
        try {
            Document doc = Jsoup.connect(strings[0]).get();
            Element element=doc.select("div[class=vidcloud-player-embed]").first().select("script").first();
            String[] s1=element.data().split("url: '");
            String[] s2=s1[1].split("&");
            String link="https://vidcloud.co/"+s2[0];

            load(link);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(final Void aVoid) {
        v=aVoid;
    }

    public void load(String link){
        StringRequest request = new StringRequest(link, new Response.Listener<String>() {
            @Override
            public void onResponse(String string) {
                parseJsonData(string);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(SourceGetter.context);
        rQueue.add(request);
    }

    public void parseJsonData(String jsonString) {
        try {
            String object = new JSONObject(jsonString).getString("html");
            String[] split1=object.split("\"file\":\"");
            String[] split2=split1[1].split(Pattern.quote("\"}],"));
            url=split2[0].replace("\\","");
            xModels = new ArrayList<>(1);
            final XModel xModel = new XModel();
            Log.e("Link",url);
            xModel.setUrl(url);
            xModels.add(xModel);
            SourceGetter.onComplete.onTaskCompleted(xModels, false);
            isInside=true;
            onPostExecute(v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

