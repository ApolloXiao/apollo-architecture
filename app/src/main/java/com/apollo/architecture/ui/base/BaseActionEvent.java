package com.apollo.architecture.ui.base;

public class BaseActionEvent {
    public static final int SHOW_LOADING_DIALOG = 1;

    public static final int SHOW_LOADING_DIALOG_RES_ID = 2;

    public static final int DISMISS_LOADING_DIALOG = 3;

    public static final int SHOW_TOAST = 4;

    public static final int SHOW_TOAST_FOR_RES_ID = 5;

    public static final int FINISH = 6;

    public static final int FINISH_WITH_RESULT = 7;

    private int action;
    private String message;
    private int resId;

    public BaseActionEvent(int action){
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }
}
