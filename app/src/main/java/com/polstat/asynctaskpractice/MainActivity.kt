package com.polstat.asynctaskpractice

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
            val asyncTask = AsyncTaskExample()
            asyncTask.execute("https://stis.ac.id/media/source/up.png")
        }
    }

    inner class AsyncTaskExample() : AsyncTask<String, String, Bitmap>() {
        override fun onPreExecute() {
            super.onPreExecute()
            p = ProgressDialog(imageView.context)
            p.setMessage("Downloading...")
            p.isIndeterminate = false
            p.setCancelable(false)
            p.show()
        }
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