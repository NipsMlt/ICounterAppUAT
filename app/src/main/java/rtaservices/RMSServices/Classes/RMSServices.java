package rtaservices.RMSServices.Classes;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByDrivingLicense;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByFineNumber;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByPlateNo;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByTrafficFileNo;
import rtaservices.RTAMainServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;


public class RMSServices extends Fragment implements View.OnClickListener {

    private Button btn_salesOrder, btn_salesInvoice,
            btn_rmsemiratesID, btn_tradeLicense, btn_Back, btn_Info, btn_Home;
    private Fragment mFragment;
    private TextView tv_timer, tvrtaService, tv_selectanyOption, btn_employeeServices, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    private PrimeThread primeThread;

    @Override
    public void onAttach(Context context) {
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
        super.onAttach(context);
    }

    public static RMSServices newInstance() {
        return new RMSServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.rtarmsservices, null);


        btn_salesOrder = (Button) view.findViewById(R.id.btn_salesorder);
        btn_salesInvoice = (Button) view.findViewById(R.id.btn_salesinvoice);
        btn_rmsemiratesID = (Button) view.findViewById(R.id.btn_rmsemiratesid);
        btn_tradeLicense = (Button) view.findViewById(R.id.btn_tradelicense);
        btn_employeeServices = (Button) view.findViewById(R.id.btn_employeeservices);
        btn_Back = (Button) view.findViewById(R.id.btn_rtarmsservicesback);
        btn_Info = (Button) view.findViewById(R.id.btnrtarmsservicesinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtarmsserviceshome);
        tv_selectanyOption = (TextView) view.findViewById(R.id.tv_selectanyoption);
        tvrtaService = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);


        btn_salesOrder.setOnClickListener(this);
        btn_salesInvoice.setOnClickListener(this);
        btn_rmsemiratesID.setOnClickListener(this);
        btn_tradeLicense.setOnClickListener(this);
        btn_employeeServices.setOnClickListener(this);
        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMainServices.newInstance(), getFragmentManager());

        primeThread = new PrimeThread(10000, R.raw.pleaseselectoption);
        primeThread.start();

        return view;
    }

    class PrimeThread extends Thread {
        long minPrime;
        int Audio;

        PrimeThread(long minPrime, int audio) {
            this.minPrime = minPrime;
            this.Audio = audio;
        }

        public void run() {
            mPlayer = MediaPlayer.create(getContext(), this.Audio);
            mPlayer.start();
        }
    }

    private void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        try {
           try {                 if (mPlayer != null) {                     mPlayer.stop();                     mPlayer.release();                 }             } catch (Exception e) {             }
            if (cTimer != null)
                cTimer.cancel();
            PreferenceConnector.writeString(getContext(), ConfigrationRTA.Services_Name, ConfigrationRTA.RMS_Services);
            switch (v.getId()) {
                case R.id.btn_salesorder:
                    PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_SALES_ORDER);
                    mFragment = RMSOrderNo.newInstance();
                    addFragment();
                    break;
                case R.id.btn_salesinvoice:
                    mFragment = RMSInvoiceNo.newInstance();
                    addFragment();
                    PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_SALES_INVOICE);
                    break;
                case R.id.btn_rmsemiratesid:
                    mFragment = RMSEmiratesID.newInstance();
                    addFragment();
                    PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_EMIRATESID);
                    break;
                case R.id.btn_tradelicense:
                    mFragment = RMSTradeLicense.newInstance();
                    addFragment();
                    PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_TRADE_LICENSE);
                    break;
                case R.id.btn_employeeservices:
                    mFragment = RMSEmployeeServices.newInstance();
                    addFragment();
                    PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_EMPLOYEE_SERVICES);
                    break;
                case R.id.btn_rtarmsservicesback:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content, RTAMainServices.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.btnrtarmsservicesinfo:
               /* mFragment = RTAMainSubServices.newInstance();
                addFragment();
                    break;*/
                case R.id.btnrtarmsserviceshome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_salesOrder.setText(resources.getString(R.string.SaleOrder));
        btn_salesInvoice.setText(resources.getString(R.string.SalesInvoice));
        btn_rmsemiratesID.setText(resources.getString(R.string.RMSEmiratesId));
        btn_tradeLicense.setText(resources.getString(R.string.TradeLicense));
        btn_employeeServices.setText(resources.getString(R.string.EmployeeService));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaService.setText(resources.getString(R.string.RMSServices));
        tv_selectanyOption.setText(resources.getString(R.string.Selectanyoneoption));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }
}
