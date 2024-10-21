package com.xl.cocos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.xl.view.XEditText
import java.util.Locale
import java.util.regex.Pattern

object EditTextUtils {

    /**
     *  {
     *      "text": "",
     *      "hint": "Enter Phone Number",
     *      "textSize": 42,
     *      "isPassword": false,
     *      "maxLength": 12,
     *      "regexPattern": "[0-9]{0,12}",
     *      "maxLines": 1,
     *      "textColor": "#333333",
     *      "hintColor": "#999999",
     *      "rawInputType": 1
     *  }
     */
    data class EditTextConfig(
        val hint: String,
        val text: String,
        val textSize: Int,
        val textColor: String,
        val hintColor: String,
        val inputMode: Int,
        val maxLines: Int,
        val isPassword: Boolean,
        val maxLength: Int,
        val regexPattern: String
    )

    /**
     *  {
     *      "left": 310,
     *      "top": 1811.6,
     *      "width": 664,
     *      "height": 130.20000000000002,
     *      "gameWidth": 1080,
     *      "gameHeight": 2340
     *  }
     */
    data class EditTextRect(
        val left: Float,
        val top: Float,
        val width: Float,
        val height: Float,
        val gameWidth: Float,
        val gameHeight: Float
    )

    /**
     * Cocos 中的输入模式
     */
    enum class CocosInputMode(val value: Int) { ANY(0), EMAIL_ADDR(1), NUMERIC(2), PHONE_NUMBER(3), URL(4), DECIMAL(5), SINGLE_LINE(6) }

    @JvmStatic
    fun show(configJson: String, rectJson: String) {
        val config = GsonUtils.fromJson(configJson, EditTextConfig::class.java)
        val rect = GsonUtils.fromJson(rectJson, EditTextRect::class.java)
        val scale = ScreenUtils.getScreenHeight() / rect.gameHeight
        show(ActivityUtils.getTopActivity(), config, (rect.left * scale).toInt(), ((rect.gameHeight - rect.top) * scale).toInt(), (rect.width * scale).toInt(), (rect.height * scale).toInt())
    }

    @SuppressLint("ClickableViewAccessibility")
    fun show(activity: Activity, config: EditTextConfig, left: Int, top: Int, width: Int, height: Int) {

        LogUtils.d("show: $left, $top, $width, $height")
        // 获取 DecorView
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        hide(activity)

        // 创建一个新的 EditText
        val editText = XEditText(activity)
        setEditTextConfig(editText, config)
        val mLayoutParams = FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = left
            topMargin = top
        }
        editText.layoutParams = mLayoutParams
        editText.minimumHeight = height

        // 将 EditText 添加到 DecorView 中
        contentView.addView(editText)
        editText.requestFocus()

        editText.post {
            KeyboardUtils.showSoftInput(editText)
        }

        setOnTextChangedListener(editText)

        KeyboardUtils.registerSoftInputChangedListener(activity) { keyboardHeight ->
            if (keyboardHeight > 0) {
                val rect = Rect()
                contentView.getWindowVisibleDisplayFrame(rect)
                val location = IntArray(2)
                editText.getLocationOnScreen(location)
                val editTextBottom = location[1] + editText.height
                if (editTextBottom > rect.bottom) {
                    contentView.scrollTo(0, editTextBottom - rect.bottom + 20)
                }
            } else {
                contentView.scrollTo(0, 0)
                contentView.removeView(editText)
                editText.clearFocus()
                KeyboardUtils.unregisterSoftInputChangedListener(activity.window)
            }
        }
    }

    private fun setEditTextConfig(editText: XEditText, config: EditTextConfig) {
        // 创建一个新的 EditText
        editText.apply {
            hint = config.hint
            textSize = SizeUtils.px2sp(config.textSize.toFloat()).toFloat()
            setTextColor(Color.parseColor(config.textColor))
            setHintTextColor(Color.parseColor(config.hintColor))
            maxLines = config.maxLines
            if(maxLines == 1 || config.inputMode == CocosInputMode.SINGLE_LINE.value) {
                setSingleLine()
            }
            inputType = when(config.inputMode) {
                CocosInputMode.ANY.value, CocosInputMode.SINGLE_LINE.value -> InputType.TYPE_CLASS_TEXT
                CocosInputMode.EMAIL_ADDR.value -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                CocosInputMode.NUMERIC.value -> InputType.TYPE_CLASS_NUMBER
                CocosInputMode.PHONE_NUMBER.value -> InputType.TYPE_CLASS_PHONE
                CocosInputMode.URL.value -> InputType.TYPE_TEXT_VARIATION_URI
                CocosInputMode.DECIMAL.value -> InputType.TYPE_NUMBER_FLAG_DECIMAL
                else -> InputType.TYPE_CLASS_TEXT
            }

            if (config.isPassword) {
                inputType = inputType or InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
            }

            //数字键盘
            if(inputType == InputType.TYPE_CLASS_NUMBER || inputType == InputType.TYPE_CLASS_PHONE || inputType == InputType.TYPE_NUMBER_FLAG_DECIMAL) {
                setRawInputType(Configuration.KEYBOARD_QWERTY)
            }

            if (config.maxLength > 0) {
                val filters = arrayOf<InputFilter>(InputFilter.LengthFilter(config.maxLength))
                editText.filters = filters
            }
            setInputFilter(editText, config.regexPattern)
        }
    }

    private fun setInputFilter(editText: EditText, regexPattern: String?) {
        if (regexPattern.isNullOrEmpty()) {
            return
        }

        val filters = editText.filters
        val newFilters = filters.copyOf(filters.size + 1)

        newFilters[newFilters.size - 1] = InputFilter { source, start, end, dest, dstart, _ ->
            if (source.isNullOrEmpty()) {
                return@InputFilter ""
            }

            val sourceText = source.toString()
            val str = StringBuilder(dest).apply {
                insert(dstart, sourceText)
            }

            if (!Pattern.matches(regexPattern, str)) {
                return@InputFilter ""
            }

            if (source is Spanned) {
                val sp = SpannableString(sourceText)
                TextUtils.copySpansFrom(source, start, end, null, sp, 0)
                sp
            } else {
                sourceText
            }
        }

        editText.filters = newFilters
    }

    private fun setOnTextChangedListener(editText: XEditText) {
        editText.setOnXTextChangeListener(object: XEditText.OnXTextChangeListener {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Cocos2dxUtils.execute(
                    String.format(
                        Locale.ENGLISH,
                        "onTextChanged('%s')",
                        s.toString()
                    )
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun hide(activity: Activity) {
        val contentView = activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)
        for(i in 0 until contentView.childCount) {
            val child = contentView.getChildAt(i)
            if(child is XEditText) {
                child.clearFocus()
                contentView.removeView(child)
            }
        }
    }
}
