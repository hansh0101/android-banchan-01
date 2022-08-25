package co.kr.woowahan_banchan.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder

class ImageLoader(
    private val imageView: ImageView,
    private val context: Context? = null,
) {
    private var placeholder: Bitmap? = null
    private var errorImage: Bitmap? = null

    fun setPlaceHolder(id: Int): ImageLoader {
        placeholder = context?.let {
            (ContextCompat.getDrawable(it, id) as BitmapDrawable).bitmap
        }
        setImage(placeholder)
        return this
    }

    fun setErrorImage(id: Int): ImageLoader {
        errorImage = context?.let {
            (ContextCompat.getDrawable(it, id) as BitmapDrawable).bitmap
        }
        return this
    }

    fun loadImage(url: String) {
        if (url.isEmpty()) return
        if (cache.containsKey(url)) {
            setImage(cache[url])
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            var urlConnection: HttpURLConnection? = null
            runCatching {
                urlConnection = getUrlConnection(url)
                val stream = urlConnection?.inputStream
                BitmapFactory.decodeStream(stream)
            }.onSuccess {
                cache[url] = it
                withContext(Dispatchers.Main) { setImage(it) }
            }.onFailure {
                Timber.e(it)
                withContext(Dispatchers.Main) { setImage(errorImage) }
            }.also {
                urlConnection?.disconnect()
            }
            this.cancel()
        }
    }

    private fun getUrlConnection(url: String): HttpURLConnection {
        var conn = URL(url).openConnection() as HttpURLConnection
        when (conn.responseCode) {
            HttpURLConnection.HTTP_MOVED_PERM,
            HttpURLConnection.HTTP_MOVED_TEMP -> {
                val location = URLDecoder.decode(conn.getHeaderField("Location"), "UTF-8")
                val next = URL(URL(url), location)
                conn = next.openConnection() as HttpURLConnection
            }
        }
        return conn
    }

    private fun setImage(bitmap: Bitmap?) {
        imageView.setImageBitmap(bitmap)
    }

    companion object {
        private val cache = HashMap<String, Bitmap>()
    }
}