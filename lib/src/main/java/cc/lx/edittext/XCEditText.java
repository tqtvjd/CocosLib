package cc.lx.edittext;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.xl.view.XEditText;

import java.util.regex.Pattern;

public class XCEditText extends XEditText {

    public XCEditText(Context context) {
        this(context, null);
    }

    public XCEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public XCEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setTypeface(Typeface.SANS_SERIF);
        setClearDrawableTint(ColorStateList.valueOf(Color.GRAY));
    }

    public void configure(EditTextConfig config, float scale) {
        // 设置文本
        setText(config.text);

        // 设置提示文本
        setHint(config.hint);

        // 设置文本大小
        setTextSize(TypedValue.COMPLEX_UNIT_PX, config.textSize * scale);

        // 设置文本颜色
        setTextColor(Color.parseColor(config.textColor));

        // 设置提示文本颜色
        setHintTextColor(Color.parseColor(config.hintColor));

        // 设置是否为单行模式
        if (getMaxLines() == 1 || config.inputMode == CocosInputMode.SINGLE_LINE.getValue()) {
            setSingleLine();
        }

        // 设置是否可清除
        setDisableClear(!config.cleanable);

        // 设置输入类型
        int inputType;
        switch (config.inputMode) {
            case 1:
                inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
                break;
            case 2:
                inputType = InputType.TYPE_CLASS_NUMBER;
                break;
            case 3:
                inputType = InputType.TYPE_CLASS_PHONE;
                break;
            case 4:
                inputType = InputType.TYPE_TEXT_VARIATION_URI;
                break;
            case 5:
                inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL;
                break;
            default:
                inputType = InputType.TYPE_CLASS_TEXT;
        }
        setInputType(inputType);

        // 设置密码模式
        if (config.isPassword) {
            setInputType(getInputType() | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // 设置最大长度
        if (config.maxLength > 0) {
            InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(config.maxLength)};
            setFilters(filters);
        }

        // 设置正则表达式过滤器
        setInputFilter(config.regexPattern);
    }

    public void setPositionAndSize(int left, int top, int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = left;
        params.topMargin = top;
        setLayoutParams(params);
        setMinimumHeight(height);
    }

    private void setInputFilter(String regexPattern) {
        if (regexPattern == null || regexPattern.isEmpty()) {
            return;
        }

        InputFilter[] currentFilters = getFilters();
        InputFilter[] newFilters = new InputFilter[currentFilters.length + 1];
        System.arraycopy(currentFilters, 0, newFilters, 0, currentFilters.length);

        newFilters[newFilters.length - 1] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
                if (source == null || source.length() == 0) {
                    return "";
                }

                String sourceText = source.toString();
                StringBuilder str = new StringBuilder(dest);
                str.insert(dstart, sourceText);

                if (!Pattern.matches(regexPattern, str.toString())) {
                    return "";
                }
                return sourceText;
            }
        };
        setFilters(newFilters);
    }
}
