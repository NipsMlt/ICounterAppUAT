package rtaservices;

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

import RTANetworking.Common.LocaleHelper;
import example.dtc.R;
import rtamain.RTAMain;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;


public class PaymentCategory extends Fragment implements View.OnClickListener {

    Button btn_Card, btn_Directdebit,
            btn_Easypaymentplan, btn_Paymentvoucher, btn_Back, btn_Info, btn_Home;
    Fragment mFragment;
    private TextView tv_timer, tvrtaServiceTitle, tv_selectanyOption, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static PaymentCategory newInstance() {
        return new PaymentCategory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.rtaserviespaymentcategory, null);


        btn_Card = (Button) view.findViewById(R.id.btn_card);
       /* btn_Directdebit = (Button) view.findViewById(R.id.btn_directdebit);
        btn_Easypaymentplan = (Button) view.findViewById(R.id.btn_easypaymentplan);
        btn_Paymentvoucher = (Button) view.findViewById(R.id.btn_paymentvoucher);*/
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_selectanyOption = (TextView) view.findViewById(R.id.tv_selectanyoption);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        btn_Back = (Button) view.findViewById(R.id.btnrtapaycategoryback);
        btn_Info = (Button) view.findViewById(R.id.btnrtapaycategoryinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtapaycategoryhome);


        btn_Back.setOnClickListener(this);
        // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        btn_Card.setOnClickListener(this);
        /*btn_Directdebit.setOnClickListener(this);
        btn_Easypaymentplan.setOnClickListener(this);
        btn_Paymentvoucher.setOnClickListener(this);
*/

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, RTAMainSubServices.newInstance(), getFragmentManager());


        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoption);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.pleaseselectoptionarabic);
        mPlayer.start();


        return view;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Card.setText(resources.getString(R.string.PCCard));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.PaymentCategory));
        tv_selectanyOption.setText(resources.getString(R.string.Selectanyoneoption));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    @Override
    public void onClick(View v) {
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

            case R.id.btn_card:
                mFragment = CardPaymentSwipe.newInstance();
                addFragment();
                break;
            case R.id.btn_directdebit:
            /* mFragment = PaymentCategory.newInstance();
                addFragment();*/
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_easypaymentplan:
                /*mFragment = PaymentCategory.newInstance();
                addFragment();*/
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_paymentvoucher:
                /*mFragment = PaymentCategory.newInstance();
                addFragment();*/
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnrtapaycategoryback:
                /*mFragment = FipContactDetails.newInstance();
                addFragment();*/
                break;
           /* case R.id.btnrtapaycategoryinfo:
                break;*/
            case R.id.btnrtapaycategoryhome:
                mFragment = RTAMain.newInstance();
                addFragment();
        }

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}
