package com.xl.cocos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cc.lx.utils.AndroidUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import cc.lx.utils.EditTextUtils
import cc.lx.utils.SysRecorderWrapper
import org.cocos2dx.lib.R

class MainActivity : CocosActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        BarUtils.setNavBarVisibility(this, false)
        BarUtils.setStatusBarVisibility(this, false)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val configJson = """ 
            {
                "text": "",
                "hint": "Enter Phone Number",
                "textSize": 42,
                "isPassword": true,
                "maxLength": 12,
                "regexPattern": "[0-9]{0,12}",
                "maxLines": 1,
                "textColor": "#ffffff",
                "hintColor": "#aaaaaa",
                "inputMode": 2
            }
        """.trimIndent()

        val rectJson = """
            {
                "left": 110,
                "top": 1650.15,
                "width": 864,
                "height": 130.2,
                "gameWidth": 1080,
                "gameHeight": 2400
            }
        """.trimIndent()

        findViewById<View>(R.id.tvHello).setOnClickListener {
            LogUtils.e("屏幕宽高: ${ScreenUtils.getScreenWidth()}, ${ScreenUtils.getScreenHeight()}")
            EditTextUtils.show(configJson, rectJson)
        }

        findViewById<View>(R.id.btnStart).setOnClickListener {
            AndroidUtils.startRecord()
        }

        findViewById<View>(R.id.btnStop).setOnClickListener {
            AndroidUtils.stopRecord()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SysRecorderWrapper.getInstance().onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        SysRecorderWrapper.getInstance().onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        SysRecorderWrapper.getInstance().onDestroy()
    }
}