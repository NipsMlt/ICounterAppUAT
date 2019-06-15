package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.spec.InvalidKeySpecException;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.GifView;
import utility.PreferenceConnector;

public class EnterCVV extends Fragment implements View.OnClickListener {

    Fragment mFragment;
    EditText edt_CVV;
    Button btn_submitEnterCVV, btn_Back, btn_Info, btn_Home;
    private WebView webView;
    private String FirstName, LastName, CardExpiry, CardNumber, CardType, CardCVV, Amount,
            TransactionID, IPAddress, DeviceID, CustomerCountry, CustomerCity, CustomerState, TimeStampInMilli,
            CustomerAddress, CustomerEmail, DashDTC, GMAILCOM, PostalCode, CustomerContactNumber,
            getEncryptedCardNumber, getEncryptedCardType, getEncryptedCardCVV, getEncryptedCardExpiry;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;
    private LinearLayout ll_EnterCVV;
    private String getCipherText;
    private TextView tv_timer, tv_enterCVV, tvrtaServiceTitle, tv_Seconds;
    CountDownTimer cTimer;
    String ClassName;
    private String Language;
    private GifView iv_entercvv_Animation;
    private MediaPlayer mPlayer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static EnterCVV newInstance() {
        return new EnterCVV();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.rtaserviesentercvv, null);

        btn_submitEnterCVV = (Button) view.findViewById(R.id.btn_submitentercvv);
        edt_CVV = (EditText) view.findViewById(R.id.edt_cvv);
        ll_EnterCVV = (LinearLayout) view.findViewById(R.id.ll_entercvv);
        tv_enterCVV = (TextView) view.findViewById(R.id.tv_entercvv);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        btn_Back = (Button) view.findViewById(R.id.btnrtaentercvvback);
        btn_Info = (Button) view.findViewById(R.id.btnrtaentercvvinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtaentercvvhome);
        iv_entercvv_Animation = (GifView) view.findViewById(R.id.iv_entercvv_animation);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);

        //Play GIF Swipe card animation
        iv_entercvv_Animation.play();

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, CardPaymentSwipe.newInstance(), getFragmentManager());


        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.entercvv);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.entercvvarabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.entercvvurdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.entercvvchinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.entercvvmalayalam);
        mPlayer.start();

        //webView = (WebView) view.findViewById(R.id.webview);
        //webView.setVisibility(View.GONE);
        try {
            createTransactionObject = (KioskPaymentResponse) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);
            Amount = createTransactionObject.ServiceAttributesList.CKeyValuePair[2].getValue();
            TransactionID = createTransactionObject.ServiceAttributesList.CKeyValuePair[0].getValue();
        } catch (Exception e) {
            if (cTimer != null)
                cTimer.start();

        }
        try {
            createTransactionObjectItem = (KioskPaymentResponseItem) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);
            Amount = createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[2].getValue();
            TransactionID = createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[0].getValue();
        } catch (Exception e) {
            if (cTimer != null)
                cTimer.start();

        }
        try {
            //Set Values for Service Call
            FirstName = bundle.getString(ConfigrationRTA.CardFirstName);
            LastName = bundle.getString(ConfigrationRTA.CardLastName);
            CardNumber = bundle.getString(ConfigrationRTA.CardNumber);
            CardExpiry = bundle.getString(ConfigrationRTA.CardExpiry);
            if (CardNumber.charAt(0) == '4') {
                CardType = "001"; //VISA
            } else if (CardNumber.charAt(0) == '5') {
                CardType = "002";  //Master
            }
            IPAddress = ConfigrationRTA.IP;
            DeviceID = RTAMain.AndroidSerialNo;
            TimeStampInMilli = utility.Common.getdateTimeInMilli();
            CustomerCountry = ConfigrationRTA.Country;
            CustomerCity = ConfigrationRTA.City;
            CustomerState = ConfigrationRTA.City;
            //DashDTC = ConfigrationRTA.DashDTC;
            GMAILCOM = ConfigrationRTA.GMAILCOM;
            CustomerAddress = DeviceID + " " + TimeStampInMilli + " " + FirstName + " " + LastName + " " + CustomerCity;
            CustomerEmail = FirstName + ConfigrationRTA.DOTSEP + LastName + GMAILCOM;
            PostalCode = ConfigrationRTA.PostalCode;
            CustomerContactNumber = ConfigrationRTA.ContactNumber;
            //getCipherText = com.security.CommonCryptography.Decrypt(ConfigrationRTA.POSKey);

            try {
                getEncryptedCardNumber = EncryptDecrpt.Encrypt("4111111111111111", getCipherText); //for UAT CardNumber = 4111111111111111
                getEncryptedCardExpiry = EncryptDecrpt.Encrypt(CardExpiry, getCipherText);
                getEncryptedCardType = EncryptDecrpt.Encrypt(CardType, getCipherText);
            } catch (InvalidKeySpecException e) {
                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }

        btn_submitEnterCVV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cTimer != null)
                    cTimer.cancel();
                CardCVV = edt_CVV.getText().toString();

                if (CardCVV.length() == 3) {
                    mFragment = RTAThreeDSecurePayment.newInstance();
                    bundle.putString(ConfigrationRTA.CardCVV, CardCVV);
                    bundle.putString(ConfigrationRTA.CardFirstName, FirstName);
                    bundle.putString(ConfigrationRTA.CardLastName, LastName);
                    bundle.putString(ConfigrationRTA.CardNumber, CardNumber);
                    bundle.putString(ConfigrationRTA.CardExpiry, CardExpiry);
                    mFragment.setArguments(bundle);
                    addFragment();
                } else {
                    if (cTimer != null)
                        cTimer.start();
                    Toast.makeText(getContext(), "Please enter correct CVV", Toast.LENGTH_LONG).show();
                }

            }
        });

        try {
            //Keeping the CVV masked
            edt_CVV.setTransformationMethod(new Common.MyPasswordTransformationMethod());
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        if (cTimer != null)
            cTimer.cancel();
        switch (v.getId()) {
            case R.id.btnrtaentercvvback:
                mFragment = CardPaymentSwipe.newInstance();
                addFragment();
                break;
           /* case R.id.btnrtaentercvvinfo:
                break;*/
            case R.id.btnrtaentercvvhome:
                mFragment = RTAMain.newInstance();
                addFragment();
        }
    }


    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_submitEnterCVV.setText(resources.getString(R.string.Submit));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.CardPayment));
        tv_enterCVV.setText(resources.getString(R.string.PleaseEnteryourCVV));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}