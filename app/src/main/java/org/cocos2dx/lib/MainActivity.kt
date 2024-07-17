package org.cocos2dx.lib

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils

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

        DeviceUtils.getUniqueDeviceId()
        DeviceUtils.getModel()


        val configJson = """ 
            {
                "text": "",
                "hint": "Enter Phone Number",
                "textSize": 42,
                "isPassword": false,
                "maxLength": 12,
                "regexPattern": "[0-9]{0,12}",
                "maxLines": 1,
                "textColor": "#333333",
                "hintColor": "#999999",
                "rawInputType": 1
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
    }
}