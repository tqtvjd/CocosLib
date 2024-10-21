package cc.lx.edittext;

import java.io.Serializable;

public class EditTextConfig implements Serializable {
    public String hint;
    public String text;
    public int textSize;
    public String textColor;
    public String hintColor;
    public int inputMode;
    public boolean isPassword;
    public int maxLength;
    public String regexPattern;
}
