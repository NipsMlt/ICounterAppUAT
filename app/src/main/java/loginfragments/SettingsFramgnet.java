package loginfragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.UUID;


import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.UserLoginService.RequestandResponse.UserLoginDetailsResponse;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;


public class SettingsFramgnet extends DialogFragment {

    private AlertDialog dialog;
    private ImageView iv_close;
    EditText edtIP;
    Button btn_Save;
    Context context;
    Fragment mFragment;

    BufferedInputStream is;
    String PrinterIP;
    String ClassName;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static SettingsFramgnet newInstance() {
        return new SettingsFramgnet();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder customDialogMain = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_settingpop, null);

        iv_close = (ImageView) view.findViewById(R.id.iv_closeemailpop);
        edtIP = (EditText) view.findViewById(R.id.edt_ip);
        btn_Save = (Button) view.findViewById(R.id.btn_save);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        customDialogMain.setView(view);

        dialog = customDialogMain.show();


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PrinterIP = edtIP.getText().toString();
                    PreferenceConnector.writeString(getContext(), Constant.PRINTER_IP, PrinterIP);
                    Toast.makeText(getContext(), "IP is saved successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } catch (Exception e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}