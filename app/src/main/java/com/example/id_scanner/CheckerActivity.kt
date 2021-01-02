package com.example.id_scanner

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CheckerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checker)

        val sp = SharedPreference(applicationContext)

        val checkerID : TextView = findViewById(R.id.checker_id)
        checkerID.text = sp.readString(Global.USERNAME)

        val logoutButton : Button = findViewById(R.id.checker_logout_button)
        logoutButton.setOnClickListener {
            val sp = SharedPreference(applicationContext)
            sp.remove(Global.TOKEN)
            sp.remove(Global.USERNAME)

            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        var tapRipple : Array<ImageView>? = arrayOf<ImageView>(
                findViewById(R.id.tag_ripple_1),
                findViewById(R.id.tag_ripple_2),
                findViewById(R.id.tag_ripple_3)
        )

        val animatorSet = AnimatorSet()
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        val animatorList : ArrayList<Animator> = ArrayList()

        val size = 3
        val duration : Long = (size * 1000).toLong()

        for (i in 0 until 3) {
            val scaleXAnimator = ObjectAnimator.ofFloat(tapRipple?.get(i), "ScaleX", 1.0f, 2.0f)
            scaleXAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleXAnimator.startDelay = (i * duration / size).toLong()
            scaleXAnimator.duration = duration
            animatorList.add(scaleXAnimator)
            val scaleYAnimator = ObjectAnimator.ofFloat(tapRipple?.get(i), "ScaleY", 1.0f, 2.0f)
            scaleYAnimator.repeatCount = ObjectAnimator.INFINITE
            scaleYAnimator.startDelay = (i * duration / size).toLong()
            scaleYAnimator.duration = duration
            animatorList.add(scaleYAnimator)
            val alphaAnimator = ObjectAnimator.ofFloat(tapRipple?.get(i), "Alpha", 0.75f, 0f)
            alphaAnimator.repeatCount = ObjectAnimator.INFINITE
            alphaAnimator.startDelay = (i * duration / size).toLong()
            alphaAnimator.duration = duration
            animatorList.add(alphaAnimator)
        }
        animatorSet.playTogether(animatorList)
        animatorSet.start()
    }
}