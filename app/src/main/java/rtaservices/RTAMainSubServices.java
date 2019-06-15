package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.InvalidKeySpecException;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.NipsiCounterInquiryResponse;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByDrivingLicense;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByFineNumber;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByPlateNo;
import rtaservices.RTAFineandInquiryServices.Classes.FinesByTrafficFileNo;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;


public class RTAMainSubServices extends Fragment implements View.OnClickListener {

    private Button btn_TrafficfileNo, btn_Drivinglicense,
            btn_PlateNo, btn_FineNo, btn_Back, btn_Info, btn_Home;
    private Fragment mFragment;
    private TextView tv_timer, tvrtaService, tv_selectanyOption, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    // private PrimeThread primeThread;

    @Override
    public void onAttach(Context context) {
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
        super.onAttach(context);
    }

    public static RTAMainSubServices newInstance() {
        return new RTAMainSubServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.rtaserviesfineinquiry, null);


        btn_TrafficfileNo = (Button) view.findViewById(R.id.btn_trafficfileno);
        btn_Drivinglicense = (Button) view.findViewById(R.id.btn_drivinglicense);
        btn_PlateNo = (Button) view.findViewById(R.id.btn_plateno);
        btn_FineNo = (Button) view.findViewById(R.id.btn_fineno);
        btn_Back = (Button) view.findViewById(R.id.btn_rtasubservicesback);
        btn_Info = (Button) view.findViewById(R.id.btnrtasubservicesinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtasubserviceshome);
        tv_selectanyOption = (TextView) view.findViewById(R.id.tv_selectanyoption);
        tvrtaService = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);


        btn_TrafficfileNo.setOnClickListener(this);
        btn_Drivinglicense.setOnClickListener(this);
        btn_PlateNo.setOnClickListener(this);
        btn_FineNo.setOnClickListener(this);
        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMainServices.newInstance(), getFragmentManager());

        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoption);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoptionarabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoptionurdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoptionchinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoptionmalayalam);
        //mp3 will be started after completion of preparing...
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer player) {
                player.start();
            }

        });

        return view;
    }

   /* class PrimeThread extends Thread {
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
    }*/

    private void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        try {
            try {
                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                }
            } catch (Exception e) {
            }
            if (cTimer != null)
                cTimer.cancel();
            switch (v.getId()) {
                case R.id.btn_trafficfileno:
                    mFragment = FinesByTrafficFileNo.newInstance();
                    addFragment();
                    break;
                case R.id.btn_drivinglicense:
                    mFragment = FinesByDrivingLicense.newInstance();
                    addFragment();
                    break;
                case R.id.btn_plateno:
                    mFragment = FinesByPlateNo.newInstance();
                    addFragment();
                    break;
                case R.id.btn_fineno:
                    mFragment = FinesByFineNumber.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtasubservicesback:
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content, RTAMainServices.newInstance())
                            .addToBackStack(null)
                            .commit();
                    break;
                /* case R.id.btnrtasubservicesinfo:
                 *//* mFragment = RTAMainSubServices.newInstance();
                addFragment();*//*
                    break;*/
                case R.id.btnrtasubserviceshome:
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

        btn_TrafficfileNo.setText(resources.getString(R.string.TrafficFileNumber));
        btn_Drivinglicense.setText(resources.getString(R.string.DrivingLicense));
        btn_PlateNo.setText(resources.getString(R.string.Plateno));
        btn_FineNo.setText(resources.getString(R.string.Fineno));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaService.setText(resources.getString(R.string.FineInquiryandPayment));
        tv_selectanyOption.setText(resources.getString(R.string.Selectanyoneoption));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }
}
