package rtamain;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.GenericServiceCall.ServiceLayer;
import example.dtc.R;
import saioapi.service.SystemUIService.SystemUIService;
import utility.Common;
import utility.Constant;

public class RTAMain extends Fragment {


    private TextView tv_timer;
    //Declare timer
    CountDownTimer cTimer = null;
    public static String AndroidSerialNo;

    public RTAMain() {
        // Required empty public constructor
    }

    @NonNull
    public static RTAMain newInstance() {
        return new RTAMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.rtamain, container, false);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 2000, 1000, RTAlanguages.newInstance(), getFragmentManager());

        //Getting Unique Device ID
        AndroidSerialNo = android.os.Build.SERIAL;

        return view;
    }

    public void HideNavigationBar() {
        services.SystemUIService.setNaviButtonVisibility(getContext(), services.SystemUIService.NAVIBUTTON_NAVIBAR, View.GONE);
        services.SystemUIService.setStatusBarVisibility(getContext(), View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        HideNavigationBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        HideNavigationBar();
    }
}
