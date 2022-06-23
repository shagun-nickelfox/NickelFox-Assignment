package com.example.nickelfoxassignment.assignment0

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser {

    private fun parseJsonObject(json: JSONObject): HashMap<String, String> {

        val dataList: HashMap<String, String> = HashMap()
        try {

            val name = json.getString("name")
            val latitude = json.getJSONObject("geometry").getJSONObject("location").getString("lat")
            val longitude =
                json.getJSONObject("geometry").getJSONObject("location").getString("lng")
            dataList["name"] = name
            dataList["lat"] = latitude
            dataList["lng"] = longitude
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return dataList
    }

    private fun parseJsonArray(jsonArray: JSONArray?): List<HashMap<String, String>> {

        val dataList: MutableList<HashMap<String, String>> = mutableListOf()
        for (i in 0 until jsonArray!!.length()) {
            try {
                val data: HashMap<String, String> = parseJsonObject(jsonArray.get(i) as JSONObject)
                dataList.add(data)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return dataList
    }

    fun parseResult(json: JSONObject): List<HashMap<String, String>> {
        var jsonArray: JSONArray? = null
        try {
            jsonArray = json.getJSONArray("results")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return parseJsonArray(jsonArray)
    }
}
