package com.zbiyikli.sourcegetter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zbiyikli.sgetter.Model.XModel
import com.zbiyikli.sgetter.SourceGetter
import pl.droidsonroids.gif.GifImageView

class ListActivity : AppCompatActivity() {

    var urlText:TextView?=null
    var sourceLV:ListView?=null
    var loading: GifImageView?=null
    lateinit var sGetter: SourceGetter
    lateinit var org:String
    var resultList:ArrayList<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        urlText=findViewById(R.id.urlText)
        sourceLV=findViewById(R.id.sourceLV)
        loading=findViewById(R.id.loading)

        sGetter = SourceGetter(baseContext)
        org= intent.getStringExtra("url").toString()

        urlText!!.text=org

        urlText!!.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(org))
            startActivity(i)
        }

        org=org.trim()

        if (org.equals("")){
            urlText!!.text="URL Not Found"
        }

        sourceLV!!.setOnItemClickListener { adapterView, view, i, l ->
            var url:String=resultList!!.get(i).split("=> ")[1]
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
        }
        initXPlayer(org);

    }

    private fun initXPlayer(url:String){
        letGo(url)
        sGetter.onFinish(object: SourceGetter.OnTaskCompleted {
            override fun onTaskCompleted(vidURL:ArrayList<XModel>?, multiple_quality:Boolean) {
                if(vidURL != null || vidURL!!.size!=0){
                    if (multiple_quality)
                    {
                        Log.e("Zeki","Multiple Url")
                        for (model in vidURL)
                        {
                            Log.e("Zeki",model.quality)
                            Log.e("Zeki",model.url)
                        }
                    }
                    else
                    {
                        Log.e("Zeki","Single Url")
                        Log.e("Zeki",vidURL.get(0).url)
                    }
                    loading!!.visibility= View.GONE
                    loadListview(vidURL,multiple_quality)
                }else{
                    loading!!.visibility= View.GONE
                    Log.e("Zeki","NO")
                }
            }
            override fun onError() {
                loading!!.visibility= View.GONE
                Log.e("Zeki","NO")
            }

            override fun goBitLY(url: String?) {
                if (url != null) {
                    letGo(url)
                }
            }
        })
    }

    private fun letGo(url:String){
        try{
            org = url
            if(sGetter.find(url)){
                Log.e("Zeki","YES")
            }else{
                loading!!.visibility= View.GONE
                Log.e("Zeki","NO")
            }
        }catch (ex:Exception){
            loading!!.visibility= View.GONE
            Log.e("Zeki","ERROR_LETGO: "+ex.toString())
        }
    }

    fun loadListview(model:ArrayList<XModel>,multipleQuality:Boolean){
        resultList = ArrayList();
        if (multipleQuality){
            for (mod in model)
            {
                var res=mod.quality+" => "+mod.url
                resultList!!.add(res)
            }
        }else{
            var res="Normal"+" => "+model.get(0).url
            resultList!!.add(res)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultList!!)
        sourceLV!!.adapter = adapter
    }

}