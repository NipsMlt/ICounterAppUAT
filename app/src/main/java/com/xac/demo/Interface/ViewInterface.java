package com.xac.demo.Interface;

public interface ViewInterface {

    void setNaviTitle(String title);
    void setActionTable(String[] actionList, PresenterOnAction onAction);
    void addLog(String msg);
    void addLog(String msg, boolean clear);
    void appendLog(String msg);
    void clearLog();
    void saveLog();
    void checkToRunOnUiThread(Runnable codeInUiThread);
}

