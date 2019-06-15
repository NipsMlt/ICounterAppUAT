package rtaservices.RMSServices.Classes;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayerRMS;
import RTANetworking.Interfaces.ButtonAnimationListener;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItemRMS;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseRMS;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAMainSubServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.ButtonAnimation;

public class RMSInvoiceNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    Fragment mFragment;
    private EditText edt_InvoiceNo;
    private Button btn_Search, btn_Back, btn_Info, btn_Home;
    private String InvoiceNo;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    String ClassName;
    private TextView tv_timer, tv_InvoiceNo, tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    private PrimeThread primeThread;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;


    // Response Final Object
    private static NPGKskInquiryResponseRMS resObject;

    private static NPGKskInquiryResponseItemRMS resObjectItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");

    }

    public static RMSInvoiceNo newInstance() {
        return new RMSInvoiceNo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtarmsservicesinvoiceno, null);

        //Id initialization
        edt_InvoiceNo = (EditText) view.findViewById(R.id.edt_invoiceno);
        btn_Search = (Button) view.findViewById(R.id.btn_rmsinvoicenosearch);
        btn_Back = (Button) view.findViewById(R.id.btnrtarmson_back);
        btn_Info = (Button) view.findViewById(R.id.btnrtarmson_info);
        btn_Home = (Button) view.findViewById(R.id.btnrtarmson_home);
        tv_InvoiceNo = (TextView) view.findViewById(R.id.tv_invoiceno);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //button animation callback method
        // this.setButtonAnimationMethodCallBack(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Write the class name in a cache to use it further
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_SALES_INVOICE);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, RMSServices.newInstance(), getFragmentManager());

        primeThread = new PrimeThread(10000, R.raw.kindlyenterdetails);
        primeThread.start();

        try {
            if (Language.equals(Constant.LanguageEnglish))
                mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenterdetails);
            else if (Language.equals(Constant.LanguageArabic))
                mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenterdetailsarabic);
            else if (Language.equals(Constant.LanguageUrdu))
                mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenterdetailsurdu);
            else if (Language.equals(Constant.LanguageChinese))
                mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenterdetailschinese);
            else if (Language.equals(Constant.LanguageMalayalam))
                mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenterdetailsmalayalam);
            mPlayer.start();
        } catch (Exception e) {

        }

        //when the view is touched
        edt_InvoiceNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                if (buttonAnimationListener != null) {
                    buttonAnimationListener.ButtonAnimationCallBackMethod();
                }
                return false;
            }
        });

        //Timer is getting reset on text changed
        edt_InvoiceNo.addTextChangedListener(Common.getTextWatcher(cTimer, edt_InvoiceNo, btn_Search));

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text from the edittext
                InvoiceNo = edt_InvoiceNo.getText().toString();//00045888
                PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMSInvoiceNo, InvoiceNo);
                //Calling the service to log this services
                ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());
                serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_Inquiry_Service_desc);

                if (InvoiceNo.equals("") || InvoiceNo.equals(null)) {
                    Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayerRMS.callInquiryByInvoiceNo(InvoiceNo, ConfigrationRTA.SERVICE_NAME_RMS_INVOICENO, ConfigrationRTA.SERVICE_ID_RMSINVOICENO, new ServiceCallback() {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                Gson gson = new Gson();

                                String reasonCode = null;
                                try {
                                    reasonCode = obj.getString("ReasonCode");
                                    if (!(reasonCode.equals("0000"))) {
                                        //Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        builder.setMessage(obj.getString("Message"))
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        //do things
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        return;
                                    }

                                    // Check the object or Array Items
                                    JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                    if (dataObject != null) {

                                        //Do things with object.
                                        resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                                    } else {

                                        //Do things with array
                                        resObject = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                                    }

                                    // This is Array of Items
                                    if (resObject != null) {

                                        for (int i = 0; i < resObject.ServiceType.getItems().size(); i++)
                                            fipdetailsItemsList.add(resObject.getServiceType().getItems().get(i));

                                        Log.d("Service Response", resObject.Message);

                                        // This is object of Items
                                    } else {
                                        fipdetailsItemsList.add(resObjectItem.getServiceType().getItems());
                                    }

                                    //Write a list in a cache
                                    try {
                                        Common.writeObject(getContext(), Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                                    } catch (IOException e) {
                                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                    }

                                    //Writing it in the cache to get it anywhere
                                    try {
                                        Common.writeObject(getContext(), Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                                        PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMSInvoiceNo, InvoiceNo);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mFragment = RMSSalesOrderListofPendingPaymentDetails.newInstance();
                                    addFragment();

                                } catch (JSONException e) {
                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                }

                            }

                            @Override
                            public void onFailure(String obj) {
                                if (cTimer != null)
                                    cTimer.start();
                                Log.d("Service Response", obj);
                            }
                        }, getContext());

                    } catch (Exception e) {
                        if (cTimer != null)
                            cTimer.start();
                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                    }
                }
            }
        });

        return view;
    }

    //Button Animation call back method
    public void setButtonAnimationMethodCallBack(ButtonAnimationListener CallBack) {
        this.buttonAnimationListener = CallBack;
    }

    //Call back for button animation
    @Override
    public void ButtonAnimationCallBackMethod() {
        if (edt_InvoiceNo.getText().toString().equals(""))
            return;
        else {
            if (animationcheck == false) {
                //Animation(Fade in/Fade out) of the button
                animatorSet = ButtonAnimation(btn_Search);
                animationcheck = true;
            } else {
            }
        }

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


    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Search.setText(resources.getString(R.string.Search));
        tv_InvoiceNo.setText(resources.getString(R.string.InvoiceNo));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.EnterInvoiceNo));
        tv_Seconds.setText(resources.getString(R.string.seconds));

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
           try {                 if (mPlayer != null) {                     mPlayer.stop();                     mPlayer.release();                 }             } catch (Exception e) {             }
            if (cTimer != null)
                cTimer.cancel();
            if (cTimer != null)
                cTimer.cancel();
            switch (v.getId()) {
                case R.id.btnrtarmson_back:
                    mFragment = RMSServices.newInstance();
                    addFragment();
                    break;
                case R.id.btnrtarmson_info:
                    break;
                case R.id.btnrtarmson_home:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }

}