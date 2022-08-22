package co.kr.woowahan_banchan.util

import com.google.gson.GsonBuilder
import org.json.JSONArray
import timber.log.Timber

inline fun <reified T> listToString(list: List<T>): String {
    val gson = GsonBuilder().create()
    val jsonArray = JSONArray()
    list.forEach {
        jsonArray.put(gson.toJson(it, T::class.java))
    }
    return jsonArray.toString()
}

inline fun <reified T> stringToList(str: String): List<T> {
    val gson = GsonBuilder().create()
    val list = mutableListOf<T>()
    val jsonArray = JSONArray(str)
    runCatching {
        for (idx in 0 until jsonArray.length()) {
            list.add(gson.fromJson(jsonArray[idx].toString(), T::class.java))
        }
    }.onFailure {
        Timber.e(it)
    }
    return list
}