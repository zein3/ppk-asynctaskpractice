package com.polstat.asynctaskpractice

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var imageUrl: URL? = null
    private var inputStream: InputStream? = null
    private var bmImg: Bitmap? = null
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var p: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.asyncTask)
        imageView = findViewById(R.id.image)

        button.setOnClickListener { view ->
            downloadImage()
            //val asyncTask = AsyncTaskExample()
            //asyncTask.execute("https://stis.ac.id/media/source/up.png")
        }

    }

    private fun downloadImage() {
        val executor = Executors.newFixedThreadPool(2)

        p = ProgressDialog(imageView.context)
        p.setMessage("Downloading...")
        p.isIndeterminate = false
        p.setCancelable(false)
        p.show()

        executor.execute {
            try {
                imageUrl = URL("https://stis.ac.id/media/source/up.png")
                val conn = imageUrl!!.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()

                inputStream = conn.inputStream
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.RGB_565
                bmImg = BitmapFactory.decodeStream(inputStream, null, options)!!

                Handler(Looper.getMainLooper()).post {
                    p.hide()
                    imageView.setImageBitmap(bmImg)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncTaskExample() : AsyncTask<String, String, Bitmap>() {
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            p = ProgressDialog(imageView.context)
            p.setMessage("Downloading...")
            p.isIndeterminate = false
            p.setCancelable(false)
            p.show()
        }
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg strings: String?): Bitmap? {
            try {
                imageUrl = URL(strings[0])
                val conn = imageUrl!!.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()

                inputStream = conn.inputStream
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.RGB_565
                bmImg = BitmapFactory.decodeStream(inputStream, null, options)!!
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bmImg
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            if (imageView != null) {
                p.hide()
                imageView.setImageBitmap(result)
            } else {
                p.show()
            }
        }

    }
}