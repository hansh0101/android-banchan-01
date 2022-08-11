package co.kr.woowahan_banchan.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

object ImageLoader {
    private val cache = HashMap<String, Bitmap>()

    fun loadImage(url: String, setImage: (Bitmap?) -> Unit) {
        if (url.isEmpty()) {
            setImage(null)
            return
        }
        if (cache.containsKey(url)) {
            setImage(cache[url])
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            var urlConnection = URL(url).openConnection() as HttpURLConnection
            when (urlConnection.responseCode) {
                HttpURLConnection.HTTP_MOVED_PERM,
                HttpURLConnection.HTTP_MOVED_TEMP -> {
                    val location =
                        URLDecoder.decode(urlConnection.getHeaderField("Location"), "UTF-8")
                    val next = URL(URL(url), location)
                    urlConnection = next.openConnection() as HttpURLConnection
                }
            }
            runCatching {
                val stream = urlConnection.inputStream
                BitmapFactory.decodeStream(stream)
            }.onSuccess {
                cache[url] = it
                withContext(Dispatchers.Main) { setImage(it) }
            }.onFailure {
                Timber.e(it)
                withContext(Dispatchers.Main) { setImage(null) }
            }.also {
                urlConnection.disconnect()
            }
        }
    }
}