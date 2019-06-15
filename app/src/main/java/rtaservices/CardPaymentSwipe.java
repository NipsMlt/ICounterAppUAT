package rtaservices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xac.demo.Interface.PresenterInterface;
import com.xac.demo.Interface.PresenterOnAction;
import com.xac.demo.Interface.ViewInterface;
import com.xac.demo.Presenter.EmvTransactionPresenter;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAFineandInquiryServices.Interfaces.CardValuesListener;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.GifView;
import utility.PreferenceConnector;

public class CardPaymentSwipe extends Fragment implements ViewInterface, CardValuesListener, View.OnClickListener {

    Fragment mFragment;
    TextView tv_Swipecard, tv_paymentisprocessed, tvrtaServiceTitle;
    Button btnBackSwipeCard, btn_Back, btn_Info, btn_Home;
    PresenterInterface presenter;
    private String Track1Data, Track2Data, namecharatfirstIndex, namecharatlastIndex;
    String[] Track1DataArr, Track2DataArr, GetFullNameArr, getFirstLastName;
    String expirydateStart, expirydateEnd, FirstName, LastName, CardExpiry, CardNumber;
    private EmvTransactionPresenter emvTransactionPresenter = new EmvTransactionPresenter();
    String ClassName;
    private TextView tv_timer, tv_Seconds;
    CountDownTimer cTimer;
    private String Language;
    private GifView iv_swpiecard_Animation;
    private MediaPlayer mPlayer;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }


    public static CardPaymentSwipe newInstance() {
        return new CardPaymentSwipe();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaserviescardpayment, null);
        tv_Swipecard = (TextView) view.findViewById(R.id.tv_swipecard);
        tv_paymentisprocessed = (TextView) view.findViewById(R.id.tv_paymentisprocessed);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        btn_Back = (Button) view.findViewById(R.id.btnswipecardback);
        btn_Home = (Button) view.findViewById(R.id.btnrtaswipecardhome);
        btn_Info = (Button) view.findViewById(R.id.btnrtaswipecardinfo);
        iv_swpiecard_Animation = (GifView) view.findViewById(R.id.iv_swpiecard_animation);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        //Play GIF Swipe card animation
        iv_swpiecard_Animation.play();

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 20000, 1000,
                tv_timer, PaymentConfirmation.newInstance(), getFragmentManager());

        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.swipeyourcard);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.swipeyourcardarabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.swipeyourcardurdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.swipeyourcardchinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.swipeyourcardmalayalam);
        mPlayer.start();

        //Setting call back method to get the card values from this class
        emvTransactionPresenter.setcardValuesCallBackMethod(this);

        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.xac.demo.Presenter.EmvTransactionPresenter");
            presenter = (PresenterInterface) clazz.newInstance();
            //Calling CardValueListener interface to get the card values in this class
            presenter.initPresenter(getContext(), this, this);

        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }

        return view;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_Swipecard.setText(resources.getString(R.string.Pleaseswipeyourcard));
        tv_paymentisprocessed.setText(resources.getString(R.string.YourPaymentisbeingprocessed));
        tvrtaServiceTitle.setText(resources.getString(R.string.CardPayment));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    public String[] getNameOnCard(String name) {
        name = name.trim();
        namecharatfirstIndex = name.substring(0, 1);
        namecharatlastIndex = name.substring(name.length() - 1);
        if (namecharatfirstIndex.equals("/") || namecharatlastIndex.equals("/"))
            name = name.replace("/", "");
        else if (name.contains("/")) {
            GetFullNameArr = Common.MySplit(name, "/");
            name = name.replace("/", " ");
            GetFullNameArr = Common.MySplit(name, " ");
            FirstName = GetFullNameArr[1];
            LastName = GetFullNameArr[0];
            name = FirstName + " " + LastName;
        }
        getFirstLastName = Common.MySplit(name, " ");
        return getFirstLastName;
    }

    public String getCardNumberOnCard(String cardno) {
        cardno = cardno.trim();
        cardno = cardno.substring(1, cardno.length());
        return cardno;
    }

    public String getExpiryDateOnCard(String expirydate) {
        expirydate = expirydate.trim();
        expirydate = expirydate.substring(0, 4);
        expirydateStart = expirydate.substring(0, 2);
        expirydateEnd = expirydate.substring(2, 4);
        expirydate = expirydateEnd + expirydateStart;
        return expirydate;
    }

    @Override
    public void setNaviTitle(String title) {

    }

    @Override
    public void setActionTable(String[] actionList, PresenterOnAction onAction) {
        onAction.run("Run Transaction");

    }

    @Override
    public void addLog(String msg) {

    }

    @Override
    public void addLog(String msg, boolean clear) {

    }

    @Override
    public void appendLog(String msg) {

    }

    @Override
    public void clearLog() {

    }

    @Override
    public void saveLog() {

    }

    @Override
    public void checkToRunOnUiThread(Runnable codeInUiThread) {

    }


    @Override
    public void CardValuesCallBackMethod(String track1, String track2) {

        try {
            if (!track1.equals("")) {
                if (track1.contains("^")) {
                    Track1DataArr = new String[]{};
                    Track2DataArr = new String[]{};
                    Track1Data = track1;
                    //Track2Data = track2;
                    //Track2Data = "B4806660101035938^/TALAL ABSAR SHAIKH AB^20122211185700163000000";
                    //Track2Data = "B4266110035080009^ABSAR/TALAL^21092011145900160000000";
                    //Track2Data = "B4582097101179194^ALI/NAAFII FAHAR^21032011964600682000000";
                    Track1DataArr = Common.MySplit(Track1Data, "^");
                    CardNumber = getCardNumberOnCard(Track1DataArr[0]);
                    CardExpiry = getExpiryDateOnCard(Track1DataArr[2]);
                    getFirstLastName = getNameOnCard(Track1DataArr[1]);
                    if (getFirstLastName.equals(null) || getFirstLastName.equals("")) {
                        FirstName = getFirstLastName[0];
                        LastName = getFirstLastName[1];
                    }

                    //if (CardNumber.substring(0, 1).equals("4") || CardNumber.substring(0, 1).equals("5")) {
                    FirstName = getFirstLastName[0];
                    LastName = getFirstLastName[1];
                    Bundle bundle = new Bundle();
                    bundle.putString(ConfigrationRTA.CardFirstName, FirstName);
                    bundle.putString(ConfigrationRTA.CardLastName, LastName);
                    bundle.putString(ConfigrationRTA.CardExpiry, CardExpiry);
                    bundle.putString(ConfigrationRTA.CardNumber, CardNumber);
                    if (cTimer != null)
                        cTimer.cancel();
                    mFragment = EnterCVV.newInstance();
                    mFragment.setArguments(bundle);
                    addFragment();
                    /*} else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (cTimer != null)
                                    cTimer.start();
                                Utilities.AlertDialog(getContext(), "Only Master and Visa cards are accepted");
                                Class<?> clazz = null;
                                try {
                                    clazz = Class.forName("com.xac.demo.Presenter.EmvTransactionPresenter");
                                    presenter = (PresenterInterface) clazz.newInstance();
                                    //Calling CardValueListener interface to get the card values in this class
                                    presenter.initPresenter(getContext(), CardPaymentSwipe.this, CardPaymentSwipe.this);

                                } catch (Exception e) {
                                    if (cTimer != null)
                                        cTimer.start();
                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                }
                            }
                        });*/
                }
            } else {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (cTimer != null)
                            cTimer.cancel();
                        // Utilities utilities = new Utilities();
                        //utilities.AlertDialog(getContext(), "Card is not swiped properly,kindly swipe it again", mFragment);
                        //Class<?> clazz = null;
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Card is not swiped properly,kindly swipe it again")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mFragment = PaymentConfirmation.newInstance();
                                            addFragment();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } catch (Exception e) {
                            if (cTimer != null)
                                cTimer.start();
                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (cTimer != null)
                cTimer.start();
        }
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
            case R.id.btnswipecardback:
                mFragment = PaymentConfirmation.newInstance();
                addFragment();
                break;
            /*case R.id.btnrtaswipecardinfo:
                break;*/
            case R.id.btnrtaswipecardhome:
                mFragment = RTAMain.newInstance();
                addFragment();
        }
    }


}