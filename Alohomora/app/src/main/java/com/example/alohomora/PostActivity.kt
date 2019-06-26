package com.example.alohomora

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

operator fun JSONArray.iterator(): Iterator<JSONObject> =
    (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

const val TIMEOUT = 10*1000

class PostActivity {

    override fun onCreate(savedInstanceState: Bundle?) {

        val id = "ecf3401e2758c87ff713242dc1ea53db"
        val rev = "1-16f3ffbafd857d530c4da43d03cebbd9"
        val date = "hh"
        val user_id = "hh"
        val hour = "hh"
        val padlock = "hh"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        val buttonPost = findViewById<Button>(R.id.button)
        buttonPost.setOnClickListener {

            val json = JSONObject("{\n\"_id\": \"$id\",\n  \"" +
                    "_rev\": \"$rev\",\n  \"" +
                    "reserve\": {\n    \"" +
                    "date\": \"$date\",\n    \"" +
                    "user_id\": \"$user_id\",\n    \"" +
                    "hour\": \"$hour\",\n    \"" +
                    "padlock\": \"$padlock\"\n  }\n}")

            progressBar.visibility = View.VISIBLE

            HttpTask {
                progressBar.visibility = View.INVISIBLE
                if (it == null) {
                    println("ERRO DE CONEXAO MIZERA!")
                    return@HttpTask
                }
                println(it)
            }.execute("POST", "https://alohomorabeta1.mybluemix.net/app/request/reserve", json.toString())
        }
    }

    class HttpTask(var callback: (String?) -> Unit) : AsyncTask<String, Unit, String>()  {

        override fun doInBackground(vararg params: String): String? {
            val url = URL(params[1])
            val httpClient = url.openConnection() as HttpURLConnection
            httpClient.readTimeout = TIMEOUT
            httpClient.connectTimeout = TIMEOUT
            httpClient.requestMethod = params[0]

            if (params[0] == "POST") {
                httpClient.instanceFollowRedirects = false
                httpClient.doOutput = true
                httpClient.doInput = true
                httpClient.useCaches = false
                httpClient.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            }
            try {
                if (params[0] == "POST") {
                    httpClient.connect()
                    val os = httpClient.outputStream
                    val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                    writer.write(params[2])
                    writer.flush()
                    writer.close()
                    os.close()
                }
                if (httpClient.responseCode == HttpURLConnection.HTTP_OK) {
                    val stream = BufferedInputStream(httpClient.inputStream)
                    return readStream(inputStream = stream)
                } else {
                    println("ERROR ${httpClient.responseCode}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }

            return null
        }

        private fun readStream(inputStream: BufferedInputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            callback(result)
        }
    }
}