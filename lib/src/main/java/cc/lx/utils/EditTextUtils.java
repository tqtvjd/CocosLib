package cc.lx.utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.text.Editable;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.xl.view.XEditText;
import java.util.Locale;

import cc.lx.edittext.EditTextConfig;
import cc.lx.edittext.EditTextRect;
import cc.lx.edittext.XCEditText;

public class EditTextUtils {

    public static void show(String configJson, String rectJson) {
        EditTextConfig config = GsonUtils.fromJson(configJson, EditTextConfig.class);
        EditTextRect rect = GsonUtils.fromJson(rectJson, EditTextRect.class);
        float scale = ScreenUtils.getScreenHeight() / rect.gameHeight;
        AndroidUtils.runOnUiThread(() -> showInternal(
                ActivityUtils.getTopActivity(),
                config,
                (int) (rect.left * scale),
                (int) ((rect.gameHeight - rect.top) * scale),
                (int) (rect.width * scale),
                (int) (rect.height * scale),
                scale
        ));
    }

    @SuppressLint("ClickableViewAccessibility")
    private static void showInternal(
            Activity activity,
            EditTextConfig config,
            int left,
            int top,
            int width,
            int height,
            float scale
    ) {
        ViewGroup contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        XCEditText editText = new XCEditText(activity);
        editText.configure(config, scale);
        editText.setPositionAndSize(left, top, width, height);
        contentView.addView(editText);
        editText.post(() -> KeyboardUtils.showSoftInput(editText));
        setOnTextChangedListener(editText);
        KeyboardUtils.registerSoftInputChangedListener(activity, keyboardHeight -> {
            if (keyboardHeight > 0) {
                Rect rect = new Rect();
                contentView.getWindowVisibleDisplayFrame(rect);
                int[] location = new int[2];
                editText.getLocationOnScreen(location);
                int editTextBottom = location[1] + editText.getHeight();
                if (editTextBottom > rect.bottom) {
                    contentView.scrollTo(0, editTextBottom - rect.bottom + 20);
                }
            } else {
                Cocos2dxUtils.execute("onTextEnd()");
                contentView.scrollTo(0, 0);
                contentView.removeView(editText);
                editText.clearFocus();
                KeyboardUtils.unregisterSoftInputChangedListener(activity.getWindow());
            }
        });
    }

    private static void setOnTextChangedListener(XCEditText editText) {
        editText.setOnXTextChangeListener(new XEditText.OnXTextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Cocos2dxUtils.execute(String.format(Locale.ENGLISH, "onTextChanged('%s')", s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
