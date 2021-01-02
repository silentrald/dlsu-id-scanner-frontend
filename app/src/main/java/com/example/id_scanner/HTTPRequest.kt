package com.example.id_scanner

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class HTTPRequest @JvmOverloads constructor(private val urlString: String, private val method: String = "GET")  {
    private val LOG : String = "HTTP"

    private var auth : String? = null // authorization token
    private var body : HashMap<String, String> = HashMap()
    private var header : HashMap<String, String> = HashMap()
    private var postRequest : PostRequest? = null

    fun setAuth(token: String) {
        auth = token
    }

    fun setPostRequest(postRequest: PostRequest) {
        this.postRequest = postRequest
    }

    fun addBody(key: String, value: String) {
        body[key] = value
    }

    fun addHeader(key: String, value: String) {
        header[key] = value
    }

    fun executeReq() {
        if (postRequest == null) { return }

        // TODO: Change this to coroutine
        val thread = Thread {
            var connection: HttpURLConnection? = null
            try {
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection

                if (method !== "GET") {
                    connection.requestMethod = method
                    connection.doOutput = true
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                }

                if (auth !== null) {
                    connection.setRequestProperty("Authorization", "Bearer $auth")
                }

                val wr = DataOutputStream(connection.outputStream)
                if (!body.isEmpty()) {
                    var start = true
                    val bodyString = StringBuilder()
                    for (key in body.keys) {
                        if (start) {
                            start = false
                        } else {
                            bodyString.append("&")
                        }
                        bodyString.append("$key=${body[key]}")
                    }
                    wr.writeBytes(bodyString.toString())
                    wr.flush()
                }
                wr.close()

                val input = connection.inputStream
                val reader = BufferedReader(InputStreamReader(input))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) response.append(line)
                reader.close()

                postRequest?.postRequest(JSONObject(response.toString()))
            } catch (e: Exception) {
                Log.e(LOG, Log.getStackTraceString(e))
                postRequest?.postRequest(JSONObject("{}"))
            } finally {
                connection?.disconnect()
            }
        }
        thread.start()
    }
}