package cc.lx.utils;


public enum CocosInputMode {
    ANY(0),
    EMAIL_ADDR(1),
    NUMERIC(2),
    PHONE_NUMBER(3),
    URL(4),
    DECIMAL(5),
    SINGLE_LINE(6);

    private final int value;

    CocosInputMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}