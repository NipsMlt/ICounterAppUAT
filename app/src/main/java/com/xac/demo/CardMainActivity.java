package com.xac.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xac.demo.Presenter.ContactlessPresenter;
import com.xac.demo.Presenter.EmvTransactionPresenter;
import com.xac.demo.Presenter.InfoPresenter;
import com.xac.demo.Presenter.MiscPresenter;
import com.xac.demo.Presenter.PinpadPresenter;
import com.xac.demo.Presenter.PrinterPresenter;
import com.xac.demo.Presenter.UpdatePresenter;
import com.xac.demo.View.StandardActivity;
import com.xac.helper.Utility;

import java.util.LinkedHashMap;
import java.util.Map;

import example.dtc.R;
import saioapi.base.Misc;


public class CardMainActivity extends AppCompatActivity {

    ListView pageList;

    public static Map<String, String> presenterList = new LinkedHashMap<>();
    static
    {
        presenterList.put("Information" , InfoPresenter.class.getCanonicalName());
        presenterList.put("Update Service", UpdatePresenter.class.getCanonicalName());
        presenterList.put("Printer", PrinterPresenter.class.getCanonicalName());
        presenterList.put("EMV Transaction", EmvTransactionPresenter.class.getCanonicalName());
        presenterList.put("NFC and Soft LED", ContactlessPresenter.class.getCanonicalName());
        presenterList.put("Misc Functions", MiscPresenter.class.getCanonicalName());
        presenterList.put("Pinpad", PinpadPresenter.class.getCanonicalName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        pageList = findViewById(R.id.page_list);
        pageList.setAdapter(new ArrayAdapter<>(this, R.layout.textview_page_list, presenterList.keySet().toArray(new String[presenterList.size()])));
        pageList.setOnItemClickListener((parent, view, position, id) -> {

            String pageName = ((TextView)view) .getText().toString();

            Intent intent = new Intent(CardMainActivity.this, StandardActivity.class);
            intent.putExtra("presenter", presenterList.get(pageName));
            startActivity(intent);
        });

        requestPermission();
        getProductName();

        this.setTitle(AppData.getInstance().product);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void requestPermission() {

        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void getProductName() {

        byte [] buf = new byte[32];

        if (new Misc().getSystemInfo(Misc.INFO_PRODUCT, buf) == 0)
            AppData.getInstance().product = Utility.byte2String(buf);
    }

}
