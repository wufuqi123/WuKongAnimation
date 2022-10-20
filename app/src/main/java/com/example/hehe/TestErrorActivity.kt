package com.example.hehe

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wukonganimation.action.extend.createAction
import com.wukonganimation.action.extend.createSequenceAction
import com.wukonganimation.action.extend.stopAction

class TestErrorActivity : AppCompatActivity() {

    val time = 500L

    var isShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_error)

        findViewById<View>(R.id.btn).setOnClickListener {
            if (isShow) {
                hide()
            } else {
                show()
            }

        }
    }

    fun hide() {
        if (!isShow) return
        isShow = false
        findViewById<View>(R.id.tv_move).stopAction()
        findViewById<View>(R.id.tv_hide).stopAction()
        findViewById<View>(R.id.tv_hide).createSequenceAction()
            .fadeOut(time)
            .callFunc {
                findViewById<View>(R.id.tv_move).createAction()
                    .moveTo(time, y = 1800f)
                    .start()
            }
            .start()
    }

    fun show() {
        if (isShow) return
        isShow = true
        findViewById<View>(R.id.tv_move).stopAction()
        findViewById<View>(R.id.tv_hide).stopAction()
        findViewById<View>(R.id.tv_move).createAction()
            .moveTo(time, y = 180f)
            .callFunc {
                findViewById<View>(R.id.tv_hide).createSequenceAction()
                    .fadeIn(time)
                    .start()
            }
            .start()
    }
}