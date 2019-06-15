package com.xac.demo.View;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xac.demo.Interface.PresenterInterface;
import com.xac.demo.Interface.PresenterOnAction;
import com.xac.demo.Interface.ViewInterface;
import com.xac.helper.Utility;

import example.dtc.R;

public class StandardActivity extends AppCompatActivity implements ViewInterface {

    ListView cmdList;
    TextView cmdLog;

    PresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.standard_page);

        cmdList = findViewById(R.id.cmdList);

        cmdLog = findViewById(R.id.cmdLog);
        cmdLog.setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.btnClearLog).setOnClickListener(v -> clearLog());

        findViewById(R.id.btnSaveLog).setOnClickListener(v -> saveLog());

        initPresenter(getIntent().getExtras().getString("presenter"));

    }

    private void initPresenter(String presenterName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(presenterName);
            Log.i(getLocalClassName(), clazz.getCanonicalName());
            presenter = (PresenterInterface)clazz.newInstance();
            presenter.initPresenter(this, this,null);

        } catch (Exception e) {
            e.printStackTrace();
            this.finish();
        }
    }

    //region Log

    @Override
    public void checkToRunOnUiThread(Runnable codeInUiThread) {
        if (Looper.myLooper() == Looper.getMainLooper()){
            codeInUiThread.run();
        } else {
            this.runOnUiThread(codeInUiThread);
        }
    }

    @Override
    public void setNaviTitle(String title) {
        setTitle(title);
    }

    @Override
    public void setActionTable(String[] actionList, final PresenterOnAction onAction) {

        cmdList.setAdapter(new ArrayAdapter<>(this, R.layout.textview_cmd_list, actionList));
        cmdList.setOnItemClickListener((parent, view, position, id) -> {
            String action = ((TextView)view).getText().toString();
            onAction.run(action);
        });

    }

    @Override
    public void addLog(final String msg) {
        addLog(msg, false);
    }

    @Override
    public void addLog(final String msg, final boolean clear) {
        checkToRunOnUiThread(() -> {
            if (clear) cmdLog.setText("");
            cmdLog.append(msg+"\r\n");
            cmdLog.setScaleY(cmdLog.getTextScaleX());
            final Layout layout = cmdLog.getLayout();
            if(layout != null){
                int scrollDelta = layout.getLineBottom(cmdLog.getLineCount() - 1)
                        - cmdLog.getScrollY() - cmdLog.getHeight();
                if(scrollDelta > 0)
                    cmdLog.scrollBy(0, scrollDelta);
            }
        });
    }

    @Override
    public void appendLog(String msg) {
        checkToRunOnUiThread(() -> {
            cmdLog.append(msg);
        });
    }

    @Override
    public void clearLog() {
        checkToRunOnUiThread(() -> {
            cmdLog.setText("");
        });
    }

    @Override
    public void saveLog() {
        String logPath = Utility.saveLogToFile(getApplicationContext(), "cmdLog", cmdLog.getText().toString());
        if (logPath != null)
            addLog("Save Cmd Log to -\r\n" + logPath, true);
        else
            addLog("Save Cmd Log Failed");
    }

    //endregion

}
