package rtaservices.RTADriverServices.Classes;

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

import RTANetworking.Common.LocaleHelper;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAMainServices;
import rtaservices.RTAMainSubServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class RTADriverServices extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btnDriverRenewal, btnDriverLossDamage;
    private TextView tv_timer, tvrtaService, tv_selectanyOption, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;


    public RTADriverServices() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
        super.onAttach(context);
    }


    public static RTADriverServices newInstance() {
        return new RTADriverServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.rtadriver_services, null);

        try {
            btnDriverRenewal = (Button) view.findViewById(R.id.btn_dl_renewal);
            btnDriverLossDamage = (Button) view.findViewById(R.id.btn_dl_loss_damage);
            btn_Back = (Button) view.findViewById(R.id.btn_rtaservicesmainback);
            btn_Info = (Button) view.findViewById(R.id.btnrtamaininfo);
            btn_Home = (Button) view.findViewById(R.id.btnrtamainhome);
            tv_selectanyOption = (TextView) view.findViewById(R.id.tv_selectanyoption);
            tvrtaService = (TextView) view.findViewById(R.id.tvrtaservice);
            tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
            tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);


            btnDriverRenewal.setOnClickListener(this);
            btnDriverLossDamage.setOnClickListener(this);
            btn_Back.setOnClickListener(this);
           //btn_Info.setOnClickListener(this);
            btn_Home.setOnClickListener(this);

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
            mPlayer.start();


            //Call the function to check which language code is it and change it accordingly
            ChangeLanguage(Language);

            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMainServices.newInstance(), getFragmentManager());
        } catch (Exception ex) {

        }


        return view;
    }


    public void onClick(View view) {
        try {
           try {                 if (mPlayer != null) {                     mPlayer.stop();                     mPlayer.release();                 }             } catch (Exception e) {             }
            if (cTimer != null)
                cTimer.cancel();
            switch (view.getId()) {
                case R.id.btn_dl_renewal:
                    mFragment = RTADriverRenewalByLicenseNo.newInstance();
                    addFragment();
                    break;
                case R.id.btn_dl_loss_damage:
                    mFragment = RTADriverLossDamageByLicenseNo.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtaservicesmainback:
                    mFragment = RTAMainServices.newInstance();
                    addFragment();
                    break;
               /* case R.id.btnrtamaininfo:
                    break;*/
                case R.id.btnrtamainhome:
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

        btnDriverRenewal.setText(resources.getString(R.string.DrivingLicenseRenewal));
        btnDriverLossDamage.setText(resources.getString(R.string.DrivingLicenseLossDamage));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaService.setText(resources.getString(R.string.DrivingLicenseServices));
        tv_selectanyOption.setText(resources.getString(R.string.Selectanyoneoption));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    private void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

}