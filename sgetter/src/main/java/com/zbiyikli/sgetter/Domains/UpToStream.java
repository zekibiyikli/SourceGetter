package com.zbiyikli.sgetter.Domains;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.zbiyikli.sgetter.Model.XModel;
import com.zbiyikli.sgetter.SourceGetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class UpToStream {
    private static String COOKIE = null;
    public static void fetch(final Context context, final String url, final SourceGetter.OnTaskCompleted onComplete){
        CookieJar cookieJar = new CookieJar() {
            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                COOKIE = cookies.toString();
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        AndroidNetworking.get("https://uptostream.com/api/streaming/source/get?token=&file_code="+getUpToStreamID(url))
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            response = new JSONObject(response).getJSONObject("data").getString("sources");
                            new UpToStream2().get(context, response, new UpToStream2.OnDone() {
                                @Override
                                public void result(String result) {
                                    Log.e("Zeki",result);
                                    if (result!=null){
                                        try {
                                            JSONArray array = new JSONArray(result);
                                            ArrayList<XModel> xModels = new ArrayList<>();
                                            for (int i=0;i<array.length();i++){
                                                String src = array.getJSONObject(i).getString("src");
                                                String label = array.getJSONObject(i).getString("label");
                                                String lang = array.getJSONObject(i).getString("lang");
                                                if (lang!=null && !lang.isEmpty()){
                                                    lang = lang.toUpperCase();
                                                }

                                                String quality=label+","+ lang;
                                                final XModel xModel = new XModel();
                                                xModel.setUrl(src);
                                                xModel.setQuality(quality);
                                                xModels.add(xModel);
                                                // putModel(src,quality,xModels);
                                            }

                                            if (xModels.size()!=0) {
                                                SourceGetter.onComplete.onTaskCompleted(xModels, true);
                                            }else SourceGetter.onComplete.onError();
                                        } catch (JSONException e) {
                                            SourceGetter.onComplete.onError();
                                        }
                                    }else {
                                        SourceGetter.onComplete.onError();
                                    }
                                }

                                @Override
                                public void retry() {
                                    fetch(context,url,onComplete);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            SourceGetter.onComplete.onError();
                        }
                    }

                    private String get(String regex,String code){
                        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                        final Matcher matcher = pattern.matcher(code);
                        code = null;
                        while (matcher.find()) {
                            for (int i = 1; i <= matcher.groupCount(); i++) {
                                code = matcher.group(i);
                            }
                        }

                        return code;
                    }

                    @Override
                    public void onError(ANError anError) {
                        SourceGetter.onComplete.onError();
                    }
                });
    }

    private static void putModel(String url, String quality, ArrayList<XModel> model){
        for (XModel x:model){
            if (x.getQuality().equalsIgnoreCase(quality)){
                return;
            }
        }
        if (url!=null && quality!=null) {
            XModel xModel = new XModel();
            xModel.setUrl(url);
            xModel.setQuality(quality);
            if (COOKIE!=null) {
                xModel.setCookie(COOKIE);
            }
            model.add(xModel);
        }
    }

    private static String getUpToStreamID(String string) {
        final String regex = "[-\\w]{12,}";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
