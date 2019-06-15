package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.ConfigurationVLDL;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.Common.Utilities;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackCyberSource;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.HTMLRequestAndRequest;
import RTANetworking.RequestAndResponse.HTMLRequestAndRequestPaymentReceiptTicketFine;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NipsiCounterInquiryResponse;
import RTANetworking.RequestAndResponse.PaymentRequest;
import RTANetworking.RequestAndResponse.PaymentResponse;
import RTANetworking.RequestAndResponse.SendEmailReceiptRequest;
import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static utility.Common.readObject;
import static utility.Constant.TAG;

public class RTAThreeDSecurePayment extends Fragment implements View.OnClickListener {

    Fragment mFragment;
    private String FirstName, LastName, CardExpiry, CardNumber, CardType, CardCVV, Amount,
            TransactionID, IPAddress, DeviceID, CustomerCountry, CustomerCity, CustomerState, TimeStampInMilli,
            CustomerAddress, CustomerEmail, Customer_Email_DP, DashDTC, GMAILCOM, PostalCode, CustomerContactNumber, CustomerContactNumber_DP,
            getEncryptedCardNumber, getEncryptedCardType, getEncryptedCardCVV, getEncryptedCardExpiry, getCipherText;
    private String TransactionDate, TransactionAmount, Ccnr, banktransctionID,
            CcHolderName, CcBrand;
    private double MinimumAmount, MaximumAmount, Dueamount, PaidAmount, ServiceCharge;
    KskServiceItem Items[];
    private WebView webView;
    // Response from 3D Secure Payment
    private static PaymentResponse paymentResponse;
    private static LinearLayout ll_Payment;
    private static RelativeLayout ll_Paymentpaid;
    static TextView tv_Seconds, tvrtaService, tv_payment_EReceiptandDpass, tv_Referenceno,
            tv_Paymentstatus, tv_Paymentthankyou, tv_paymentProcessed, tv_progressFinished, tv_visitsmartTeller;
    boolean webviewShown = true, webviewCrossCheck;
    private Handler handler;
    String UUID, CVV;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;
    private NipsiCounterInquiryResponse resObject;
    String ClassName;
    AlertDialog.Builder builder;
    KskServiceItem[] items;
    Button btn_DonePayment;
    String ServicesName, ServicesCode, LanguageCode, LanguageCodeforDigitalPass;
    private HashMap<String, String> ServiceName;
    private Button btn_Home, btn_backfromcyberSource;
    private TextView tv_timer;
    CountDownTimer cTimer;
    private String Language, ServiceChargeRMS;
    Context context;
    Resources resources;
    private MediaPlayer mPlayer;
    RelativeLayout rl_ErrorMsg, ll_Webview;
    //Calling the service to log this services
    ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());

    public RTAThreeDSecurePayment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTAThreeDSecurePayment newInstance() {
        return new RTAThreeDSecurePayment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.rtatthreedsecure, container, false);
        Bundle bundle = getArguments();
        webView = (WebView) view.findViewById(R.id.webviewrta);
        ll_Payment = (LinearLayout) view.findViewById(R.id.ll_paymentrta);
        ll_Paymentpaid = (RelativeLayout) view.findViewById(R.id.ll_paymentpaidrta);
        rl_ErrorMsg = (RelativeLayout) view.findViewById(R.id.rl_errormsg);
        ll_Webview = (RelativeLayout) view.findViewById(R.id.ll_webviewrta);
        tv_Referenceno = (TextView) view.findViewById(R.id.tv_referenceno);
        tv_progressFinished = (TextView) view.findViewById(R.id.tv_progressfinished);
        tv_paymentProcessed = (TextView) view.findViewById(R.id.tv_paymentprocessed);
        tv_Paymentstatus = (TextView) view.findViewById(R.id.tv_paymentstatus);
        tv_payment_EReceiptandDpass = (TextView) view.findViewById(R.id.tv_paymentereceiptanddpass);
        tv_Paymentthankyou = (TextView) view.findViewById(R.id.tv_paymentthankyou);
        tv_visitsmartTeller = (TextView) view.findViewById(R.id.tv_visitsmartteller);
        btn_Home = (Button) view.findViewById(R.id.btnrtapaycomhome);
        btn_backfromcyberSource = (Button) view.findViewById(R.id.btn_backfromcybersource);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tvrtaService = (TextView) view.findViewById(R.id.tvrtaservice);
        builder = new AlertDialog.Builder(getContext());
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Random UUID
        UUID = Common.getUUID();

        //get Class Name
        ClassName = getClass().getCanonicalName();
        /*Get Services Name to set the digital pass Note: this is for the Services*/
        ServicesName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
        ServicesCode = getServiceCode(ServicesName);

        //Check the language
        LanguageCode = PreferenceConnector.readString(getContext(), Constant.Language, "");
        if (LanguageCode.equals(Constant.LanguageEnglish))
            LanguageCodeforDigitalPass = Constant.LanguageCodeofEnglish;
        else if (LanguageCode.equals(Constant.LanguageArabic))
            LanguageCodeforDigitalPass = Constant.LanguageCodeofArabic;
        else if (LanguageCode.equals(Constant.LanguageUrdu))
            LanguageCodeforDigitalPass = Constant.LanguageCodeofUrdu;
        else if (LanguageCode.equals(Constant.LanguageMalayalam))
            LanguageCodeforDigitalPass = Constant.LanguageCodeofMalyalam;

        context = LocaleHelper.setLocale(getContext(), LanguageCode);
        resources = context.getResources();

        //Initialize
        try {
            createTransactionObject = (KioskPaymentResponse) readObject(getContext(), Constant.FIPCreateTransactionObject);
            TransactionID = createTransactionObject.ServiceAttributesList.CKeyValuePair[0].getValue();
            if (createTransactionObject.getServiceAttributesList().CKeyValuePair.length > 2) {
                TransactionDate = createTransactionObject.ServiceAttributesList.CKeyValuePair[1].getValue();
                TransactionAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[2].getValue() + ".00";
            } else
                TransactionAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[1].getValue() + ".00";
            MinimumAmount = createTransactionObject.getOperationType().getMinimumAmount();
            MaximumAmount = createTransactionObject.getOperationType().getMaximumAmount();
            Dueamount = createTransactionObject.getOperationType().getDueamount();
            PaidAmount = createTransactionObject.getOperationType().getMaximumAmount();
            ServiceCharge = createTransactionObject.getOperationType().getServiceCharge();

        } catch (Exception e) {

        }
        try {
            createTransactionObjectItem = (KioskPaymentResponseItem) readObject(getContext(), Constant.FIPCreateTransactionObject);
            TransactionID = createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[0].getValue();
            if (createTransactionObjectItem.getServiceAttributesList().CKeyValuePair.length > 2) {
                TransactionDate = createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[1].getValue();
                TransactionAmount = createTransactionObjectItem.getServiceAttributesList().CKeyValuePair[2].getValue() + ".00";
            } else
                TransactionAmount = createTransactionObjectItem.getServiceAttributesList().CKeyValuePair[1].getValue() + ".00";
            MinimumAmount = createTransactionObjectItem.getOperationType().getMinimumAmount();
            MaximumAmount = createTransactionObjectItem.getOperationType().getMaximumAmount();
            Dueamount = createTransactionObjectItem.getOperationType().getDueamount();
            PaidAmount = createTransactionObjectItem.getOperationType().getMaximumAmount();
            ServiceCharge = createTransactionObjectItem.getOperationType().getServiceCharge();
        } catch (Exception e) {

        }
        try {
            //get selected items from the fine list
            items = (KskServiceItem[]) readObject(getContext(), Constant.FinesListSelectedItems);
            //get Customer Email and Phone no
            Customer_Email_DP = PreferenceConnector.readString(getContext(), Constant.Customer_Email_RTA, "");
            CustomerContactNumber_DP = PreferenceConnector.readString(getContext(), Constant.Customer_Phone_RTA, "");
            //Service Charge for RMS
            ServiceChargeRMS = PreferenceConnector.readString(getContext(), Constant.ServiceChargesonCard, "");
        } catch (IOException | ClassNotFoundException e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }

        //Vehicle and Driver Transaction Date is null from RTA when calling RTAREcertifyservice
        if (ServicesName.equals(ConfigrationRTA.Vehicle_Renewal) || ServicesName.equals(ConfigrationRTA.Vehicle_Damaged_Lost) ||
                ServicesName.equals(ConfigrationRTA.Driver_Renewal) || ServicesName.equals(ConfigrationRTA.Driving_Damaged_Lost) ||
                ServicesName.equals(ConfigrationRTA.RMS_SALES_ORDER) || ServicesName.equals(ConfigrationRTA.RMS_SALES_INVOICE) ||
                ServicesName.equals(ConfigrationRTA.RMS_EMIRATESID) || ServicesName.equals(ConfigrationRTA.RMS_TRADE_LICENSE)) {
            TransactionDate = PreferenceConnector.readString(getContext(), ConfigrationRTA.TransactionDateCurrent, "");
        }
        //Set Values Production
        CVV = bundle.getString(ConfigrationRTA.CardCVV);
        FirstName = bundle.getString(ConfigrationRTA.CardFirstName);
        LastName = bundle.getString(ConfigrationRTA.CardLastName);
        CardNumber = "5200000000000007";//"4000000000000002";//bundle.getString(ConfigrationRTA.CardNumber);
        CardExpiry = bundle.getString(ConfigrationRTA.CardExpiry);
        CardExpiry = Utilities.getFormattedExpiryDateRTA(CardExpiry);
        CardCVV = CVV;
        if (CardNumber.charAt(0) == '4') {
            //if card starts from 4 its VISA, 001
            CardType = "001";
        } else if (CardNumber.charAt(0) == '5') {
            //if card starts from 5 its MASTER, 002
            CardType = "002";
        }
        IPAddress = ConfigrationRTA.IP;
        DeviceID = RTAMain.AndroidSerialNo;
        TimeStampInMilli = Common.getdateTimeInMilli();
        CustomerCountry = ConfigrationRTA.Country;
        CustomerCity = ConfigrationRTA.City;
        CustomerState = ConfigrationRTA.City;
        GMAILCOM = ConfigrationRTA.GMAILCOM;
        CustomerAddress = TimeStampInMilli + " " + FirstName + " " + LastName + " " + CustomerCity;
        CustomerEmail = FirstName + ConfigrationRTA.DOTSEP + LastName + GMAILCOM;
        PostalCode = ConfigrationRTA.PostalCode;
        CustomerContactNumber = ConfigrationRTA.ContactNumber;

        try {
            //Service Call to get the key to encrypt the card values as per the terminal IDs
            serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_Card_Values_Encryption_Service);

            ServiceLayer.callInquiryCardPayment(new ServiceCallback() {
                @Override
                public void onSuccess(JSONObject obj) throws JSONException {
                    Gson gson = new Gson();
                    resObject = gson.fromJson(obj.toString(), NipsiCounterInquiryResponse.class);
                    getCipherText = EncryptDecrpt.Decrypt(resObject.getPlainText(), ConfigrationRTA.TERMINAL_ID);

                    try {
                        getEncryptedCardNumber = Utilities.RemoveEscapeSequence(EncryptDecrpt.Encrypt(CardNumber, getCipherText));
                        getEncryptedCardExpiry = Utilities.RemoveEscapeSequence(EncryptDecrpt.Encrypt(CardExpiry, getCipherText));
                        getEncryptedCardType = Utilities.RemoveEscapeSequence(EncryptDecrpt.Encrypt(CardType, getCipherText));
                        getEncryptedCardCVV = Utilities.RemoveEscapeSequence(EncryptDecrpt.Encrypt(CardCVV, getCipherText));
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }

                    serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_ThreeD_Secure_Service_desc);

                    try {
                        threeDSecurePayment(getEncryptedCardNumber, getEncryptedCardExpiry, TransactionAmount, TransactionID,
                                TransactionID, getEncryptedCardType, getEncryptedCardCVV, IPAddress,
                                webView, FirstName, LastName, CustomerAddress, CustomerCity, PostalCode,
                                CustomerState, CustomerCountry,
                                CustomerEmail, CustomerContactNumber, new ServiceCallbackCyberSource() {
                                    @Override
                                    public void onSuccess(PaymentResponse obj) {

                                        //Work for timer
                                        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMain.newInstance(), getFragmentManager());

                                        serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_Direct_Account_Service);

                                        //Check if the transaction is paid else donot call the direct service
                                        if (obj.ReasonCode.equals("100")) {

                                            /*  Service Call for Direct Account Service for RMS*/
                                            if (ConfigrationRTA.RMS_Services.equals(ServicesName)) {
                                                //Channel Reference No will be the bank transaction ID
                                                banktransctionID = obj.ChannelReferenceNumber;
                                                ServiceLayer.callDirectAccountServiceRMS(TransactionID, TransactionAmount, banktransctionID, ConfigrationRTA.PAYMENT_TYPE_DA_RMS_CARD,
                                                        MinimumAmount, MaximumAmount, Dueamount, PaidAmount, ServiceChargeRMS, new ServiceCallbackPayment() {
                                                            @Override
                                                            public void onSuccess(JSONObject obj) throws JSONException {
                                                                ll_Paymentpaid.setVisibility(View.VISIBLE);
                                                                ll_Payment.setVisibility(View.GONE);

                                                                tv_Referenceno.setText(" " + TransactionID);
                                                                tv_Paymentthankyou.setText(resources.getString(R.string.PaymentThankyou));
                                                                tv_Paymentstatus.setText(resources.getString(R.string.PaymentisCompleted));
                                                                if (!ServicesName.equals(ConfigrationRTA.TrafficFines)) {
                                                                    tv_visitsmartTeller.setVisibility(View.VISIBLE);
                                                                    tv_visitsmartTeller.setText(resources.getString(R.string.VisitSmartTeller));
                                                                }

                                                                //Work for timer
                                                                Gson gson = new Gson();
                                                                if (Language.equals(Constant.LanguageEnglish))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpayment);
                                                                else if (Language.equals(Constant.LanguageArabic))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentarabic);
                                                                else if (Language.equals(Constant.LanguageUrdu))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymenturdu);
                                                                else if (Language.equals(Constant.LanguageChinese))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentchinese);
                                                                else if (Language.equals(Constant.LanguageMalayalam))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentmalayalam);
                                                                mPlayer.start();
                                                                String reasonCode = null;
                                                                try {
                                                                    reasonCode = obj.getString("ReasonCode");
                                                                    if (!(reasonCode.equals("0000"))) {

                                                                        return;
                                                                    } else {
                                                                        if (ServicesName.equals(ConfigrationRTA.TrafficFines)) {
                                                                            tv_payment_EReceiptandDpass.setText(resources.getString(R.string.PayemntEReceipt));
                                                                            CallRTAReceiptService();
                                                                        } else {
                                                                            tv_payment_EReceiptandDpass.setText(resources.getString(R.string.PayemntEReceiptandDPass));
                                                                            CallRTAReceiptService();
                                                                            CallDigitalPasssService();
                                                                        }

                                                                    }
                                                                } catch (JSONException e) {
                                                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(String obj) {
                                                                Log.d("Service Response", obj);
                                                            }
                                                        }, getContext());
                                            } else {
                                                ServiceLayer.callDirectAccountService(items, TransactionID, TransactionDate, TransactionAmount, MinimumAmount, MaximumAmount
                                                        , Dueamount, PaidAmount, ServiceCharge, new ServiceCallbackPayment() {
                                                            @Override
                                                            public void onSuccess(JSONObject obj) throws JSONException {
                                                                if (cTimer != null)
                                                                    cTimer.start();
                                                                ll_Paymentpaid.setVisibility(View.VISIBLE);
                                                                ll_Payment.setVisibility(View.GONE);

                                                                tv_Referenceno.setText(" " + TransactionID);
                                                                tv_Paymentthankyou.setText(resources.getString(R.string.PaymentThankyou));
                                                                tv_Paymentstatus.setText(resources.getString(R.string.PaymentisCompleted));
                                                                if (!ServicesName.equals(ConfigrationRTA.TrafficFines)) {
                                                                    tv_visitsmartTeller.setVisibility(View.VISIBLE);
                                                                    tv_visitsmartTeller.setText(resources.getString(R.string.VisitSmartTeller));
                                                                }

                                                                Gson gson = new Gson();
                                                                if (Language.equals(Constant.LanguageEnglish))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpayment);
                                                                else if (Language.equals(Constant.LanguageArabic))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentarabic);
                                                                else if (Language.equals(Constant.LanguageUrdu))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymenturdu);
                                                                else if (Language.equals(Constant.LanguageChinese))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentchinese);
                                                                else if (Language.equals(Constant.LanguageMalayalam))
                                                                    mPlayer = MediaPlayer.create(getContext(), R.raw.thankyouforpaymentmalayalam);
                                                                mPlayer.start();
                                                                String reasonCode = null;
                                                                try {
                                                                    reasonCode = obj.getString("ReasonCode");
                                                                    if (!(reasonCode.equals("0000"))) {
                                                                        //Views for payment is failed
                                                                        paymentisFailed();

                                                                        return;
                                                                    } else {
                                                                        if (ServicesName.equals(ConfigrationRTA.TrafficFines)) {
                                                                            tv_payment_EReceiptandDpass.setText(resources.getString(R.string.PayemntEReceipt));
                                                                            CallRTAReceiptService();
                                                                        } else {
                                                                            tv_payment_EReceiptandDpass.setText(resources.getString(R.string.PayemntEReceiptandDPass));
                                                                            CallDigitalPasssService();
                                                                            //Code for printing
                                                                            try {
                                                                                if (ServicesName.equals(ConfigrationRTA.Vehicle_Renewal) || ServicesName.equals(ConfigrationRTA.Vehicle_Damaged_Lost)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_VEHICLE_TID;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.Driver_Renewal) || ServicesName.equals(ConfigrationRTA.Driving_Damaged_Lost)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_DRIVING_TID;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.Experience_Cert)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_CERTIFICATES_TID + Constant.SEP_DASH + ConfigurationVLDL.SETVICE_ID_Driving_Exp_Certificate;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.No_Obligation_Cert)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_CERTIFICATES_TID + Constant.SEP_DASH + ConfigurationVLDL.SETVICE_ID_Clearance_Certificate;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.Non_Ownership_Cert)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_CERTIFICATES_TID + Constant.SEP_DASH + ConfigurationVLDL.SETVICE_ID_Vehicle_Non_Ownership_Certificate;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.Ownership_Cert)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_CERTIFICATES_TID + Constant.SEP_DASH + ConfigurationVLDL.SETVICE_ID_Vehicle_Ownership_Certificate;
                                                                                } else if (ServicesName.equals(ConfigrationRTA.Refund_Insurance_Cert)) {
                                                                                    TransactionID = TransactionID + Constant.SEP_DASH + ConfigrationRTA.CERTIFICATE_PRINTING_CERTIFICATES_TID + Constant.SEP_DASH + ConfigurationVLDL.SETVICE_ID_Insurance_Refund_Certificate;
                                                                                }
                                                                            } catch (Exception e) {
                                                                            }
                                                                            //Code for printing the license, receipts and cards
                                                                            //new Thread(new Utilities.ClientThread(getContext(), Customer_Email_DP + Constant.SEP_DASH + ServicesName + Constant.SEP_DASH + ConfigrationRTA.TERMINAL_ID + Constant.SEP_DASH + TransactionID)).start();
                                                                        }

                                                                    }
                                                                } catch (JSONException e) {
                                                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(String obj) {
                                                                //Log.d("Service Response", obj);
                                                                //Views for payment is failed
                                                                paymentisFailed();
                                                            }
                                                        }, getContext());
                                            }
                                        } else {
                                            //Views for payment is failed
                                            paymentisFailed();
                                        }
                                    }

                                    @Override
                                    public void onFailure(String obj) {
                                        //Layuot that says payment is failed
                                        paymentisFailed();
                                    }
                                });

                    } catch (Exception ex) {
                        Log.d(TAG, ex.getMessage());
                        ExceptionService.ExceptionLogService(getContext(), ex.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                    }

                }

                @Override
                public void onFailure(String obj) {
                    //Views for payment is failed
                    paymentisFailed();

                }
            }, getContext());

        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }

        return view;
    }


    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 3D secure Service For Card Payment
     * WITH OTP Processing
     *
     * @param CardNumber
     * @param CardExpiry
     * @param Amount
     * @param TransactionReferenceNo
     * @param ReferenceNumber
     * @param CardType
     * @param CVV
     * @param IpAddress
     * @param webView
     * @param FirstName
     * @param LastName
     * @param CustomerAddress
     * @param CustomerCity
     * @param PostalCode
     * @param CustomerState
     * @param CustomerCountry
     * @param CustomerEmail
     * @param CustomerContactNumber
     * @param callback
     */
    public void threeDSecurePayment(String CardNumber, String CardExpiry, String Amount, String TransactionReferenceNo,
                                    String ReferenceNumber, String CardType, String CVV, String IpAddress,
                                    WebView webView, String FirstName, String LastName, String CustomerAddress,
                                    String CustomerCity, String PostalCode, String CustomerState, String CustomerCountry,
                                    String CustomerEmail, String CustomerContactNumber,
                                    final ServiceCallbackCyberSource callback) {

        // Web View Setting Object here
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        //webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.contains("networkips.com")) {
                    webviewCrossCheck = false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!url.contains("networkips.com")) {
                    webviewCrossCheck = true;
                    if (webviewShown) {
                        handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (webviewCrossCheck) {
                                    /*boolean checkURL = false;
                                    checkURL = checkisReachable("https://testsecureacceptance.cybersource.com/silent/pay");//isServerReachable(getContext(), "https://testsecureacceptance.cybersource.com/silent/pay");
                                    if (checkURL == true) {
                                        webView.setVisibility(View.GONE);
                                        rl_ErrorMsg.setVisibility(View.VISIBLE);
                                        btn_backfromcyberSource.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mFragment = RTAMain.newInstance();
                                                addFragment();
                                            }
                                        });
                                    } else {*/
                                    if (cTimer != null)
                                        cTimer.cancel();
                                    ll_Payment.setVisibility(View.GONE);
                                    ll_Webview.setVisibility(View.VISIBLE);
                                    webviewShown = false;
                                    webviewCrossCheck = false;

                                    //}

                                }
                            }
                        }, 10000);
                    }
                } else {
                    if (cTimer != null)
                        cTimer.cancel();
                    ll_Payment.setVisibility(View.VISIBLE);
                    ll_Webview.setVisibility(View.GONE);
                    ShowWebView(view, callback);
                }
            }
        });

        // Start the Request Object from Here
        PaymentRequest request = new PaymentRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        //String RequestId = "95fea68d-22f3-471a-ab9b-17e9a9433bb2";
        String currectDateTime = Utilities.currentDateTime();

        // Set the action here
        request.setAction(ConfigrationRTA.PAYMENT_ACTION);

        // Set the request ID
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.PAYMENT_REQUEST_CATEGORY);

        // Set the Source Application here
        request.setSourceApplication(ConfigrationRTA.PAYMENT_SOURCE_APPLICATION);

        // Set the Device Finger Print
        request.setDeviceFingerPrint(ConfigrationRTA.PAYMENT_DEVICE_FINGER_PRINT);

        // set the Merchant ID
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the User ID
        request.setUserId(ConfigrationRTA.PAYMENT_USER_ID);

        // Set the Password
        request.setPassword(ConfigrationRTA.PAYMENT_PASSWORD);

        // Set the Ip ASSIGNED
        request.setIpAssigned(ConfigrationRTA.PAYMENT_IP_ASSIGNED);

        // Set the Version
        request.setVersion(ConfigrationRTA.PAYMENT_VERSION);

        // Set the Service Id
        request.setServiceId(ServicesName);
        //request.setServiceId(ConfigrationRTA.PAYMENT_SERVICE_ID);

        // Set the Service Name
        request.setServiceName(ServicesName);
        //request.setServiceName(ConfigrationRTA.PAYMENT_SERVICE_NAME);

        // Set the TimeStamp
        request.setTimeStamp(currectDateTime);
        //request.setTimeStamp("19062017 12:11:34");

        // Set the Transaction Reference Number
        request.setTransactionReferenceNumber(TransactionReferenceNo);

        // Set the Reference Number
        request.setReferenceNumber(ReferenceNumber);

        // Set the Currency
        request.setCurrency(ConfigrationRTA.PAYMENT_CURRENCY);

        // Set the amount to be passed from the service
        request.setAmount(Amount);

        // Set the First Name
        request.setFirstName(FirstName);

        // Set the Second Name
        request.setLastName(LastName);

        // Set the Customer Address
        request.setCustomerAddress(CustomerAddress);

        // Set the Customer City
        request.setCustomerCity(CustomerCity);

        // Set the Customer Postal Code
        request.setCustomerPostalCode(PostalCode);

        // Set the Customer State
        request.setCustomerState(CustomerState);

        // Set the Customer Country
        request.setCustomerCountry(CustomerCountry);

        // Set the Customer Email
        request.setCustomerEmail(CustomerEmail);

        // Set the Customer Contact Number
        request.setCustomerContactNumber(CustomerContactNumber);

        // Set the Callback Url
        request.setCallBackUrl(ConfigrationRTA.PAYMENT_CALL_BACK_URL);

        // Set the Payment Channel
        request.setPaymentChannel(ConfigrationRTA.PAYMENT_CHANNEL);

        // Set the Language
        request.setLanguage(ConfigrationRTA.PAYMENT_LANGUAGE);

        // Set the Sub Merchant Id
        request.setSubMerchantID(ConfigrationRTA.PAYMENT_SUB_MERCHANT_ID);

        // Set the Signature Fields
        request.setSignatureFields(ConfigrationRTA.PAYMENT_SIGNATURE_FIELDS);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash3DSecure(RequestId, currectDateTime, ReferenceNumber, ConfigrationRTA.MERCHANT_ID, Amount, ConfigrationRTA.PAYMENT_SECRET_KEY));
        //request.setSecureHash("E3LixUto6CuKWEn53ukQG1juvwEq5n4yE6eWIUefnOtfj84X8jZINzcEU49spLxZ/eNUDnKbmld0q5smslkUZW7IPm0iFdl6ulhQq4w23xm3qVDsXvrKQ0+8aOcirzTgUmydAPWv4zQcuMJVYxGoSuJtty6ZzfmkXGnZV3OTa1o64HuRi1CFDhIgTNABwRd6");

        // Set the Card Number
        request.setCardNumber(CardNumber);

        // Set the Card Type
        request.setCardType(CardType);

        // Set the CVV
        request.setCVV(CVV);

        // Set the Card Expiry
        request.setCardExpiry(CardExpiry);

        // Set the Customer Ip Address
        request.setCustomerIPAddress(IpAddress);

        // Pass the Object Here and Get the XML
        HTMLRequestAndRequest req = new HTMLRequestAndRequest();
        String SOAPRequestXML = req.paymentRequest(request);

        webView.loadData(SOAPRequestXML, "text/html", "UTF-8");
    }

    private void ShowWebView(WebView view, ServiceCallbackCyberSource callback) {
        // do your stuff here
        view.evaluateJavascript("(function(){return document.getElementById(\"FullResponse\").innerHTML})();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String html) {
                        try {

                            if (!TextUtils.isEmpty(html) && !html.equals("null")) {
                                html = StringEscapeUtils.unescapeJava(html);
                                Document doc = Jsoup.parse(html);
                                Map<String, Object> data = new HashMap<String, Object>();

                                for (Element input : doc.select("input")) {
                                    data.put(input.attr("id"), input.attr("value"));
                                }

                                JSONObject json = new JSONObject(data);
                                Gson gson = new Gson();
                                paymentResponse = gson.fromJson(json.toString(), PaymentResponse.class);
                                ll_Webview.setVisibility(View.GONE);
                                ll_Payment.setVisibility(View.VISIBLE);

                                System.out.println(paymentResponse);

                                callback.onSuccess(paymentResponse);
                            }

                        } catch (Exception ex) {

                            // Assign the response to result variable
                            callback.onFailure(ex.getLocalizedMessage());
                        }

                    }
                });
    }

    public String getServiceCode(String ServicesName) {

        //Define the service Codes for RTA Digital pass service
        ServiceName = new HashMap<>();
        ServiceName.put(ConfigrationRTA.Vehicle_Renewal, "204");
        ServiceName.put(ConfigrationRTA.Driver_Renewal, "103");
        ServiceName.put(ConfigrationRTA.Driving_Damaged_Lost, "104");
        ServiceName.put(ConfigrationRTA.Vehicle_Damaged_Lost, "211");
        ServiceName.put(ConfigrationRTA.Parking_Renew, "S321");
        ServiceName.put(ConfigrationRTA.Ownership_Cert, "214");
        ServiceName.put(ConfigrationRTA.Non_Ownership_Cert, "215");
        ServiceName.put(ConfigrationRTA.Refund_Insurance_Cert, "216");
        ServiceName.put(ConfigrationRTA.No_Obligation_Cert, "213");
        ServiceName.put(ConfigrationRTA.Experience_Cert, "105");
        ServiceName.put(ConfigrationRTA.Parking_New, "S322");
        ServiceName.put(ConfigrationRTA.Refund_Service, "R90");

        //return services code in return of services name
        return ServiceName.get(ServicesName);

    }

    public static String getHTMLRTAReceipt(String ReceiptNumber, String KioskNumber, String KioskLocation,
                                           String ServiceProvider, String ServiceName, String PaymentType,
                                           String TotalFineAmount, String TotalAmount, String ReceivedAmount) {
        // Start the Request Object from Here
        SendEmailReceiptRequest request = new SendEmailReceiptRequest();

        String currectDateTime = Utilities.currentDateTime();

        // Set the action here
        request.setDate(currectDateTime);

        // Set Receipt Number
        request.setReceiptNumber(ReceiptNumber);

        // Set Kiosk Number
        request.setKioskNumber(KioskNumber);

        //Set Kiosk Location
        request.setKioskLocation(KioskLocation);

        //Set Service Provider
        request.setServiceProvider(ServiceProvider);

        //Set Service Name
        request.setServiceName(ServiceName);

        //Set Payment Type
        request.setPaymentType(PaymentType);

        //Set Total FineAmount
        request.setTotalFineAmount(TotalFineAmount);

        //Set Total Amount
        request.setTotalAmount(TotalAmount);

        //Set Received Amount
        request.setReceivedAmount(ReceivedAmount);

        // Pass the Object Here and Get the XML
        HTMLRequestAndRequestPaymentReceiptTicketFine req = new HTMLRequestAndRequestPaymentReceiptTicketFine();
        String SOAPRequestXML = req.PaymentReceiptTicketFine(request);

        return SOAPRequestXML;
    }

    public void CallDigitalPasssService() {
        //Call Digital pass service
        ServiceLayer.callDigitalPassService(FirstName + LastName, TransactionID, Customer_Email_DP, CustomerContactNumber_DP
                , ServicesCode, ServicesName, LanguageCodeforDigitalPass, new ServiceCallback() {
                    @Override
                    public void onSuccess(JSONObject obj) throws JSONException {
                        Log.d("TAG", obj.getString(""));

                    }

                    @Override
                    public void onFailure(String obj) {

                    }
                }, getContext());
    }

    public void CallRTAReceiptService() {
        //Call RTA Receipt service
        ServiceLayer.callRTAReceiptService(TransactionID, Customer_Email_DP, ServicesName, new ServiceCallback() {
            @Override
            public void onSuccess(JSONObject obj) throws JSONException {
                Log.d("TAG", obj.getString(""));

            }

            @Override
            public void onFailure(String obj) {

            }
        }, getContext());
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Home.setText(resources.getString(R.string.Home));
        tvrtaService.setText(resources.getString(R.string.CompletePayment));
        tv_Seconds.setText(resources.getString(R.string.seconds));
        tv_progressFinished.setText(resources.getString(R.string.ProgressFinishedReferenceNo));

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
            case R.id.btnrtapaycomhome:
                mFragment = RTAMain.newInstance();
                addFragment();
                break;
        }
    }

    //Changes the views to Payment is failed
    public void paymentisFailed() {
        //The payment is failed
        ll_Paymentpaid.setVisibility(View.VISIBLE);
        ll_Payment.setVisibility(View.GONE);
        tv_payment_EReceiptandDpass.setText("");
        tv_Paymentstatus.setText(resources.getString(R.string.PaymentisFailed));
        tv_Paymentthankyou.setText(resources.getString(R.string.PaymentSorry));
        tv_Referenceno.setText(" " + TransactionID);
    }
}
