package rtaservices;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import RTANetworking.Common.LocaleHelper;
import example.dtc.R;
import interfaces.ResetTimerListener;
import utility.Constant;
import utility.PreferenceConnector;

public class InfoDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    Fragment mFragment;
    Button btn_info_OK;
    private String Language;
    private TextView tv_infoText;
    static private ResetTimerListener mMethodCallBack;
    String getInfoText;
    Bundle bundle = new Bundle();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        bundle = getArguments();
        AlertDialog.Builder customDialogMain = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rtaservices_infodialog, null);

        btn_info_OK = (Button) view.findViewById(R.id.btn_info_ok);
        tv_infoText = (TextView) view.findViewById(R.id.tv_infotext);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);


        //Setting view for Custom Dialog
        customDialogMain.setView(view);
        //Show dialog
        dialog = customDialogMain.show();


        btn_info_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMethodCallBack != null) {
                    mMethodCallBack.ResetTimerCallBackMethod();
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

    }

    //Reset Timer call back method
    public void setMethodCallBack(ResetTimerListener CallBack) {
        this.mMethodCallBack = CallBack;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}