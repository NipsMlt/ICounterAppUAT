package rtaservices.RTAParkingServices.Classes;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAMainServices;
import rtaservices.RTAMainSubServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;

public class RTAParkingServices extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_NewseaSonalCard, btn_SeasonalCardRenewal;
    private TextView tv_timer;
    //Declare timer
    private static CountDownTimer cTimer = null;

    public RTAParkingServices() {
        // Required empty public constructor
    }

    public static RTAParkingServices newInstance() {
        return new RTAParkingServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.rtaparking_services, null);

        try {
            btn_NewseaSonalCard = (Button) view.findViewById(R.id.btn_newseasonalcard);
            btn_SeasonalCardRenewal = (Button) view.findViewById(R.id.btn_seasonalcardrenewal);
            btn_Back = (Button) view.findViewById(R.id.btn_rtaservicesmainback);
            btn_Info = (Button) view.findViewById(R.id.btnrtamaininfo);
            btn_Home = (Button) view.findViewById(R.id.btnrtamainhome);
            tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

            btn_NewseaSonalCard.setOnClickListener(this);
            btn_SeasonalCardRenewal.setOnClickListener(this);
            btn_Back.setOnClickListener(this);
            btn_Info.setOnClickListener(this);
            btn_Home.setOnClickListener(this);

            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMainSubServices.newInstance(), getFragmentManager());
        } catch (Exception ex) {

        }


        return view;
    }

    public void onClick(View view) {
        try {
            if (cTimer != null)
                cTimer.cancel();
            switch (view.getId()) {
                case R.id.btn_newseasonalcard:
                    mFragment = RTASeasonalParkingCard.newInstance();
                    addFragment();
                    break;
                case R.id.btn_seasonalcardrenewal:
                    mFragment = RTASeasonalParkingCardRenewal.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtaserviceparkingservicesback:
                    mFragment = RTAMainServices.newInstance();
                    addFragment();
                    break;
                case R.id.btnrtaparkingservicesinfo:
                    break;
                case R.id.btnrtaparkingserviceshome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }

    private void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}