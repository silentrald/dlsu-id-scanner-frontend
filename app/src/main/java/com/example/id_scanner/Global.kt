package com.example.id_scanner

class Global {
    companion object {
        private val HOST : String = "192.168.254.120"
        private val PORT = 5000
        val SERVER_URL = "http://$HOST:$PORT"

        val TOKEN = "token"
        val USERNAME = "username"
    }
}