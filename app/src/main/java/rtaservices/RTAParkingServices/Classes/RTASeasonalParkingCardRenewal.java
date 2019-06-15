package rtaservices.RTAParkingServices.Classes;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.Utilities;

import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.PreferenceConnector;

import static utility.Constant.TAG;

public class RTASeasonalParkingCardRenewal extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_spcrenewalProceed;
    private EditText edt_entercardNumber;
    private String EnterCardNumber;
    private TextView tv_timer;
    final Calendar calendar = Calendar.getInstance();
    boolean calendarisOpened = false;
    //Declare timer
    private static CountDownTimer cTimer = null;
    InputMethodManager imm;

    public RTASeasonalParkingCardRenewal() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static RTASeasonalParkingCardRenewal newInstance() {
        return new RTASeasonalParkingCardRenewal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utilities utilities = new Utilities();
        View view = inflater.inflate(R.layout.rtaparkingservices_seasonal_parking_card_renewal, null);
        edt_entercardNumber = (EditText) view.findViewById(R.id.edt_entercardnumber);
        btn_spcrenewalProceed = (Button) view.findViewById(R.id.btn_spcrenewalproceed);
        btn_Back = (Button) view.findViewById(R.id.btn_rtaspcrenewal_back);
        btn_Info = (Button) view.findViewById(R.id.btn_rtaspcrenewal_info);
        btn_Home = (Button) view.findViewById(R.id.btn_rtaspcrenewal_home);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Automatic keyboard to be hidden on fragment start
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Driving_Damaged_Lost);

        try {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAParkingServices.newInstance(), getFragmentManager());
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

        edt_entercardNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        btn_spcrenewalProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EnterCardNumber = edt_entercardNumber.getText().toString();

                    if (EnterCardNumber.equals("")) {
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                    } else {
                        mFragment = RTASeasonalParkingCardAddVehicle.newInstance();
                        addFragment();

                    }


                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        });

        return view;
    }


    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        try {
            if (cTimer != null)
                cTimer.cancel();
            switch (v.getId()) {
                case R.id.btn_rtaspcrenewal_back:
                    mFragment = RTAParkingServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtaspcrenewal_info:
                    break;
                case R.id.btn_rtaspcrenewal_home:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }

}
