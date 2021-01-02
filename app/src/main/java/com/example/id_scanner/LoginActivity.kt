package com.example.id_scanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val LOG : String = "LOGIN"

    var usernameText : EditText? = null
    var passwordText : EditText? = null
    var loginBtn : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameText = findViewById(R.id.username)
        passwordText = findViewById(R.id.password)

        loginBtn = findViewById(R.id.login_button)
        loginBtn?.setOnClickListener{
            val req : HTTPRequest = HTTPRequest("${Global.SERVER_URL}/api/user/login", "POST")
            req.addBody("username", usernameText?.text.toString())
            req.addBody("password", passwordText?.text.toString())

            req.setPostRequest(object : PostRequest {
                override fun postRequest(json: JSONObject) {
                    if (json.has("token")) {
                        val sp = SharedPreference(applicationContext)
                        sp.save(Global.TOKEN, json.getString("token"))
                        val user = json.getJSONObject("user")
                        sp.save(Global.USERNAME, user.getString("username"))

                        Log.i(LOG, "SAVED TOKEN")

                        val intent = Intent(applicationContext, CheckerActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {

                    }
                }
            })
            req.executeReq()
        }
    }
}