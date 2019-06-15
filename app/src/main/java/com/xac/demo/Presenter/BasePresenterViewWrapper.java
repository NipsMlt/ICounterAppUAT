package com.xac.demo.Presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Window;

import com.xac.demo.Interface.PresenterInterface;
import com.xac.demo.Interface.PresenterOnAction;
import com.xac.demo.Interface.ViewInterface;
import com.xac.demo.View.DispatchWindowCallback;
import com.xac.helper.CommPort;
import com.xac.helper.DataSync;
import com.xac.helper.Utility;
import com.xac.helper.VNG;

import java.util.LinkedHashMap;

import rtaservices.RTAFineandInquiryServices.Interfaces.CardValuesListener;

/**
 * Created by simon_chen on 2018/2/14.
 */

public class BasePresenterViewWrapper implements PresenterInterface, ViewInterface {

    protected ViewInterface vi;
    CardValuesListener cvl;
    protected Context ctx;

    public BasePresenterViewWrapper() {}

    @Override
    public void initPresenter(Context context, ViewInterface viewInterface, CardValuesListener cardValuesListener) {
        ctx = context;
        vi = viewInterface;
        cvl = cardValuesListener;
        presenterImp();
        refreshActionTable();
    }

    @Override
    public void onDestroy() {
    }

    protected void presenterImp() {
        vi.setNaviTitle("Base Empty Presenter");
        putAction("Empty", () -> {});
    }

    //region CommPort R/W IO

    protected CommPort commPort;
    protected DataSync mDataSync = new DataSync();
    boolean logCmd = true;

    protected void initCommPort() {

        commPort = new CommPort(ctx);
        commPort.setOnEvent((err, data, len) -> {
            switch(err){
                case CONNECTED:
                    vi.addLog("Connected");
                    break;
                case DISCONNECTED:
                    vi.addLog("Disconnected !!!");
                    mDataSync.setErr("Device Disconnected");
                    break;
                case DATA_READY:
                    mDataSync.dataIn(data, len);
                    break;
                default:
                    break;
            }
        });
    }

    public byte [] waitData(int timeout,CardValuesListener cardValuesListener) {

        byte [] data = mDataSync.waitData(timeout);

        if (logCmd && data != null) {
            String log = "RSP-> " + Utility.bytes2Hex(data);
            Log.v(this.getClass().getName(), log);
            vi.addLog(log);
        }
        return data;
    }

    public byte [] waitData() {
        return waitData(commPort.READ_TIMEOUT,cvl);
    }

    public boolean sendData(byte [] data, int length) {

        if (logCmd) {
            String log = "CMD-> " + Utility.bytes2Hex(data, length);
            vi.addLog(log);
            Log.v(this.getClass().getName(), log);
        }

        return commPort.sendData(data, length);
    }

    public boolean sendData(byte [] data) {
        return sendData(data, data.length);
    }

    public byte [] exchangeData(byte [] data) { return exchangeData(data, data.length, commPort.READ_TIMEOUT); }

    public byte [] exchangeData(byte [] data, int length) { return exchangeData(data, length, commPort.READ_TIMEOUT); }

    public byte [] exchangeData(byte [] data, int length, int timeout) {
        if (sendData(data, length)) {
            return waitData(timeout,cvl);
        }
        return null;
    }

    public VNG exchangeData(VNG req) { return exchangeData(req, commPort.READ_TIMEOUT); }

    public VNG exchangeData(VNG req, int timeout) {
        if (sendData(req.getCmdBuffer())) {
            byte [] rspData = waitData(timeout,cvl);
            if (rspData != null)
                return new VNG(rspData);
        }
        return null;
    }

    //endregion

    //region bypass to ViewInterface

    @Override
    public void setNaviTitle(String title) {
        vi.setNaviTitle(title);
    }

    @Override
    public void setActionTable(String [] actionList, PresenterOnAction presenterOnAction) {
        vi.setActionTable(actionList, presenterOnAction);
    }

    @Override
    public void addLog(String msg) {
        vi.addLog(msg);
    }

    @Override
    public void addLog(String msg, boolean clear) {
        vi.addLog(msg, clear);
    }

    @Override
    public void appendLog(String msg) {
        vi.appendLog(msg);
    }

    @Override
    public void clearLog() {
        vi.clearLog();
    }

    @Override
    public void saveLog() {
        vi.saveLog();
    }

    @Override
    public void checkToRunOnUiThread(Runnable codeInUiThread) {
        vi.checkToRunOnUiThread(codeInUiThread);
    }

    //endregion

    //region action table

    private LinkedHashMap<String, Runnable> actionTable = new LinkedHashMap<>();

    protected void beforeInvoke(String methodName) {}

    protected void afterInvoke(String methodName) {}

    protected void putAction(String action, Runnable runnable) {
        actionTable.put(action, runnable);
    }

    private void refreshActionTable() {
        setActionTable(actionTable.keySet().toArray(new String[actionTable.size()]), action -> onAction(action));
    }

    Thread procTaskThread = new Thread();

    protected void onAction(String action) {

        if (!procTaskThread.isAlive()) {
            procTaskThread = new Thread(() ->{
                beforeInvoke(action);
                actionTable.get(action).run();
                afterInvoke(action);
            });
            procTaskThread.start();
        }
        else
            addLog("Action [" + action + "] is in processing");
    }

    //endregion

    //region Activity Event Dispath

    protected void runOnWindowFocusChanged(DispatchWindowCallback.RunnableOnWindowFocusChanged runnable) {
        final Window win = ((Activity)ctx) .getWindow();
        DispatchWindowCallback dispatch = new DispatchWindowCallback(win.getCallback());
        dispatch.registerOnWindowFocusChanged(runnable);
        if (runnable != null)
            win.setCallback(dispatch);
    }

    //endregion
}
