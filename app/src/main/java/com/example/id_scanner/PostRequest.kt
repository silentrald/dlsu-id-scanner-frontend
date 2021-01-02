package com.example.id_scanner

import org.json.JSONObject

interface PostRequest {
    fun postRequest(json : JSONObject)
}