package com.xac.demo.Presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.xac.packagemanager.OnPMObserver;

import java.io.File;

import saioapi.service.utility.EppInfo;
import saioapi.service.utility.MsgBase;
import saioapi.service.utility.UpdateReq;
import saioapi.service.utility.UpdateRsp;

import static com.xac.helper.Logger.LINE_OUT;
import static com.xac.helper.Logger.log;

public class UpdatePresenter extends BasePresenterViewWrapper implements ServiceConnection {

    private Messenger serviceMessenger;
    private Messenger replyMessenger;
    private boolean mIsBound = false;

    saioapi.pm.PackageManager pm;

    @Override
    protected void presenterImp() {

        setNaviTitle("Update Service & Install App");
        putAction("Update App Signing Key", () -> updateStart(Environment.getExternalStorageDirectory() + "/Download/xac_rk.spk", MsgBase.MSG_UPDATE, UpdateReq.ACTION_KEY));
        putAction("Update CommPort Firmware", () -> updateStart(Environment.getExternalStorageDirectory() + "/Download/mupdate.scat", MsgBase.MSG_UPDATE, UpdateReq.ACTION_EPP));
        putAction("Update Signed Data", () -> updateStart(Environment.getExternalStorageDirectory() + "/Download/dupdate.scat", MsgBase.MSG_UPDATE, UpdateReq.ACTION_DATA));
        putAction("Update Android System", () -> updateStart(Environment.getExternalStorageDirectory() + "/Download/dupdate.scat", MsgBase.MSG_UPDATE, UpdateReq.ACTION_SYSTEM));
        putAction("Native Install APK", () -> installApkNative());
        putAction("Silent Install APK", () -> silentInstall(Environment.getExternalStorageDirectory() + "/Download/test.apk"));
        putAction("Install APK Itself", () -> silentInstall(Environment.getExternalStorageDirectory() + "/Download/Demo.apk"));

        replyMessenger = new Messenger(incomingMessageHandler);

        doBindService();
    }

    //region Mapping Method

    public void installApkNative() {
        File f = new File(Environment.getExternalStorageDirectory() + "/Download/test.apk");
        if (!f.exists()) {
            addLog("Target file is not existed");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        ctx.startActivity(intent);
    }

    private void silentInstall(String apkPath) {
        File f = new File(apkPath);
        if (!f.exists()) {
            addLog("Target file is not existed");
            return;
        }

        addLog("Start to install");
        pm = new saioapi.pm.PackageManager(ctx);
        pm.setOnPMObserver(mOnPMObserver);
        pm.install(Uri.fromFile(f));
    }

    //endregion

    //region Update Service Binding

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        serviceMessenger = new Messenger(iBinder);
        addLog("Update Service Connected");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        serviceMessenger = null;
        addLog("Update Service Disconnected");
    }

    private void doBindService() {

        if (android.os.Build.VERSION.SDK_INT == 19) {
            mIsBound = ctx.bindService(new Intent(MsgBase.SERVICE_NAME), this, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);
        } else {
            Intent serviceIntent = new Intent();
            serviceIntent.setAction("com.xac.util.saioutility.SaioUtilityService");
            serviceIntent.setPackage("com.xac.util.saioutility");
            mIsBound = ctx.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE | Context.BIND_NOT_FOREGROUND);
        }

        if (!mIsBound)
            addLog("Fail to Bind Service");
    }

    private void doUnbindService() {
        if (mIsBound) {
            ctx.unbindService(this);
            mIsBound = false;
        }
    }

    @Override
    public void onDestroy() {
        doUnbindService();
    }

    //endregion

    //region Message Handling

    public void sendMessageToService(Message msg) {

        if (serviceMessenger == null) {
            addLog("Service is not available");
            return;
        }

        try {
            serviceMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateStart(String filePath, int type, int action) {

        File f = new File(filePath);
        if (!f.exists()) {
            addLog("Target file is not existed");
            return;
        }

        addLog("Start to update : " + filePath);

        UpdateReq req;
        Message msg = Message.obtain(null, type, action , 0);
        Bundle b = msg.getData();
        req = new UpdateReq(f.getParent(), f.getName(), true, false);
        UpdateReq.setBundleData(b, req);

        msg.setData(b);
        msg.replyTo = replyMessenger;

        sendMessageToService(msg);
    }

    private Handler incomingMessageHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            log(String.format("Handler Message: 0x%04X, Type 0x%04X, Ret: 0x%04X", (int) msg.what, msg.arg1, msg.arg2), LINE_OUT());

            if (msg.arg2 != MsgBase.ERR_OK) {
                addLog(MsgBase.getErrorDescription(msg.arg2));
                return;
            }

            if (msg.what == MsgBase.MSG_UPDATE_INFO) {

                UpdateRsp updateInfo = UpdateRsp.getBundleData(msg.getData());

                if (msg.arg1 == UpdateReq.ACTION_EPP) {
                    if (updateInfo.getRunFinishCount() == 0 && updateInfo.getTotalCount() == 0) {
                        addLog("Update Start...");
                    } else if (updateInfo.getRunFinishCount() == updateInfo.getTotalCount()) {
                        addLog("Update Almost Done");
                    } else {
                        appendLog(".");
                    }
                } else if (msg.arg1 == UpdateReq.ACTION_KEY) {
                    addLog("Update SPK Done");
                } else if (msg.arg1 == UpdateReq.ACTION_DATA) {
                    addLog("Update Data Done");
                } else if (msg.arg1 == UpdateReq.ACTION_SYSTEM) {
                    addLog("Update System Done. Please Reboot");
                }
            } else if (msg.what == MsgBase.MSG_EPP && msg.arg1 == EppInfo.ACTION_VERSION) {
                EppInfo.FirmwareVersion eppVer = EppInfo.getFirmwareVersion(msg.getData());
                clearLog();
                addLog("Model Name    : " + eppVer.getModelName() + "\n");
                addLog("Serial Number : " + eppVer.getSN() + "\n");
                addLog("Build Number  : " + eppVer.getBuild() + "\n");
            }

            if (msg.what == MsgBase.MSG_UPDATE)
                addLog("Update Firmware Done");
        }
    };

    //endregion

    //region Custom PackageManager

    private OnPMObserver mOnPMObserver = (int action, int returnCode, String packageName) -> {
        if (action == saioapi.pm.PackageManager.ACTION_INSTALL) {
            if (returnCode == saioapi.pm.PackageManager.INSTALL_SUCCEEDED) {
                addLog("Install package : " + packageName + " finished");
            } else {
                addLog("Install package : " + packageName + " failed, err = " + returnCode);
            }
            pm.finish();
        }
    };

    //endregion
}
