package com.developer.myapplication.jsoneditor

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.developer.myapplication.jsoneditor.ui.theme.JsonEditorTheme
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JsonEditorTheme {
                Column(modifier = Modifier.fillMaxSize()) {
                    val recipeList =
                        getArrayFromJson(loadJsonFromAssets("recipes.json", applicationContext))
                    convertJsonToFile(array = recipeList, applicationContext = applicationContext)

                }
            }
        }
    }
}


private fun loadJsonFromAssets(fileName: String, context: Context): String {
    var json: String = ""
    try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        json = String(buffer, charset = charset("UTF-8"))

    } catch (ex: Exception) {
        json = ""
    }
    return json
}

private fun convertJsonToFile(
    array: ArrayList<HashMap<String, String>>,
    applicationContext: Context
) {
    val file = File(applicationContext.filesDir, "recipes_edited.json")
    if(!file.exists()){
        file.createNewFile()
    }
    try {
        val jsonArray = JSONArray()

        for (it in array){
            val jsonObject =JSONObject()
            jsonObject.put("id", it.getValue("id"))
            jsonObject.put("img",it.getValue("img"))
            jsonArray.put(jsonObject)
        }
        file.writeText(jsonArray.toString(),charset = charset("UTF-8"))

    }catch (ex:Exception){
        ex.printStackTrace()
    }
}

private fun getArrayFromJson(loadJSONFromAsset: String): ArrayList<HashMap<String, String>> {
    val wallpapersList: ArrayList<HashMap<String, String>> = arrayListOf()
    try {
        val obj = JSONObject(loadJSONFromAsset)
        val m_jArry: JSONArray = obj.getJSONArray("recipieList")
        var m_li: HashMap<String, String>
        for (i in 0 until m_jArry.length()) {
            val jo_inside = m_jArry.getJSONObject(i)
            val img_value = jo_inside.getString("name")
            //Add your values in your `ArrayList` as below:
            m_li = HashMap()
            m_li["id"] = UUID.randomUUID().toString()
            m_li["name"] = img_value
            wallpapersList.add(m_li)
        }
        return wallpapersList
    } catch (ex: Exception) {
        ex.printStackTrace()
        return arrayListOf()

    }
}
