package com.example.id_scanner

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class TapActivity : AppCompatActivity() {
//    val LOG : String = "TAP"
    private var nfcAdapter : NfcAdapter? = null
    private var text : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tap)

        text = findViewById(R.id.text)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (nfcAdapter?.isEnabled() == false) {
            Toast.makeText(this, "NFC is not enabled", Toast.LENGTH_LONG).show()
            return
        }

        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val intent = intent
        val action = intent?.action

        if (NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val tagId : ByteArray? = tag?.id
            val tagString = tagId?.toHexString()

            val sp = SharedPreference(applicationContext)

            val token = sp.readString(Global.TOKEN)!!
            // TODO: Change enter
            // TODO: Change event to var PC8ywoFF9Ne
            val req : HTTPRequest = HTTPRequest("${Global.SERVER_URL}/api/attendance/enter/PC8ywoFF9Ne/$tagString", "POST")
            req.setAuth(token)
            req.setPostRequest(object: PostRequest {
                override fun postRequest(json: JSONObject) {
                    if (json.has("msg")) {
                        val msg = json.getString("msg")
                        text?.text = msg
                    } else {

                    }
                }
            })
            req.executeReq()
        }
    }

    @ExperimentalUnsignedTypes // just to make it clear that the experimental unsigned types are used
    fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }
}