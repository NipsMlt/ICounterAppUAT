package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RMSServices.Classes.PaymentConfirmationRMSEmployeeServices;
import rtaservices.RMSServices.Classes.RMSEmiratesIDListofPendingPaymentDetails;
import rtaservices.RMSServices.Classes.RMSSalesOrderListofPendingPaymentDetails;
import rtaservices.RTAFineandInquiryServices.Classes.FineInqandPaymentDetails;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class FipContactDetailsOnlyEmail extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btnContinue;
    private EditText edt_customer_Email;
    String Customer_Email, Customer_No;
    boolean validate_customer_Email;
    private TextView tv_timer, tv_enterEmail, tv_emailforReceipt, tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    CountDownTimer cTimer = null;
    private String Language, TotalAmount, TransactionID, ServiceChargeonCard, ClassName, ServiceName;
    String[] TotalAmountarr = new String[2];
    NPGKskInquiryResponse npgKskInquiryResponse = new NPGKskInquiryResponse();
    Gson gson = new Gson();
    private MediaPlayer mPlayer;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static FipContactDetailsOnlyEmail newInstance() {
        return new FipContactDetailsOnlyEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaserviescontactdetailsonlyemail, null);

        btnContinue = (Button) view.findViewById(R.id.btnscontinuecontactdetails);
        edt_customer_Email = (EditText) view.findViewById(R.id.edt_customer_email);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_enterEmail = (TextView) view.findViewById(R.id.tv_enteremail);
        tv_emailforReceipt = (TextView) view.findViewById(R.id.tv_emailforreceipt);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        btn_Back = (Button) view.findViewById(R.id.btnrta_contactdetails_onlyemail_back);
        btn_Info = (Button) view.findViewById(R.id.btnrta_contactdetails_onlyemail_info);
        btn_Home = (Button) view.findViewById(R.id.btnrta_contactdetails_onlyemail_home);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Get the fragment name as per the services
        ServiceName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        //Work for timer

        if (ServiceName.equals(ConfigrationRTA.TrafficFines)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FineInqandPaymentDetails.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER) || ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSSalesOrderListofPendingPaymentDetails.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.RMS_EMIRATESID) || ServiceName.equals(ConfigrationRTA.RMS_TRADE_LICENSE)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSEmiratesIDListofPendingPaymentDetails.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.RMS_EMPLOYEE_SERVICES)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, PaymentConfirmationRMSEmployeeServices.newInstance(), getFragmentManager());
        }

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //when the view is touched
       /* edt_customer_Email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });*/

        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenteremailaddress);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenteremailaddressarabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenteremailaddressurdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenteremailaddresschinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.kindlyenteremailaddressmalayalam);
        mPlayer.start();

        //Timer is getting reset on text changed
        edt_customer_Email.addTextChangedListener(Common.getTextWatcher(cTimer));

        //This object creation is for RMS Services
        try {
            createTransactionObject = (KioskPaymentResponse) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);

        } catch (Exception e) {

        }
        try {
            createTransactionObjectItem = (KioskPaymentResponseItem) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);

        } catch (Exception e) {

        }

        try {
            if (createTransactionObject != null) {
                TransactionID = createTransactionObject.getServiceAttributesList().CKeyValuePair[0].getValue();
                TotalAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[1].getValue();
                TotalAmountarr = TotalAmount.split("\\.");
                int length = TotalAmountarr.length;
                if (length == 1)
                    TotalAmount = TotalAmount + ".00";


            } else if (createTransactionObjectItem != null) {
                TransactionID = createTransactionObject.getServiceAttributesList().CKeyValuePair[0].getValue();
                TotalAmount = createTransactionObjectItem.getServiceAttributesList().CKeyValuePair[1].getValue();
                TotalAmountarr = TotalAmount.split("\\.");
                int length = TotalAmountarr.length;
                if (length == 1)
                    TotalAmount = TotalAmount + ".00";
            }
        } catch (Exception e) {
            //ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cTimer != null)
                        cTimer.cancel();
                    validate_customer_Email = Common.validateEmail(getContext(), edt_customer_Email);

                    if (!validate_customer_Email)
                        return;

                    Customer_Email = edt_customer_Email.getText().toString();

                    PreferenceConnector.writeString(getContext(), Constant.Customer_Email_RTA, Customer_Email);
                    if (cTimer != null)
                        cTimer.cancel();
                    if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER) || ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE) ||
                            ServiceName.equals(ConfigrationRTA.RMS_EMIRATESID) || ServiceName.equals(ConfigrationRTA.RMS_TRADE_LICENSE) ||
                            ServiceName.equals(ConfigrationRTA.RMS_EMPLOYEE_SERVICES)) {
                        ServiceLayer.callInquiryServiceCharges(TotalAmount, TransactionID, new ServiceCallback() {
                            @Override
                            public void onSuccess(JSONObject obj) throws JSONException {
                                try {
                                    npgKskInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    ServiceChargeonCard = npgKskInquiryResponse.ServiceAttributesList.CKeyValuePair[0].Value;
                                    Bundle bundlepos = new Bundle();
                                    bundlepos.putString(Constant.ServiceChargesonCard, ServiceChargeonCard);
                                    //Thi below Service charge will be used in the direct account service of RMS
                                    PreferenceConnector.writeString(getContext(), Constant.ServiceChargesonCard, ServiceChargeonCard);
                                    mFragment = PaymentConfirmation.newInstance();
                                    mFragment.setArguments(bundlepos);
                                    addFragment();

                                } catch (Exception e) {
                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                }
                            }

                            @Override
                            public void onFailure(String obj) {
                                if (cTimer != null)
                                    cTimer.start();
                            }
                        }, getContext());
                    } else {
                        if (cTimer != null)
                            cTimer.cancel();
                        //if Service is not RMS
                        mFragment = FipTotalAmount.newInstance();
                        addFragment();
                    }

                } catch (Exception e) {

                }
            }
        });

        return view;
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
                case R.id.btnrta_contactdetails_onlyemail_back:
                    if (ServiceName.equals(ConfigrationRTA.TrafficFines)) {
                        mFragment = FineInqandPaymentDetails.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER) || ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {
                        mFragment = RMSSalesOrderListofPendingPaymentDetails.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.RMS_EMIRATESID) || ServiceName.equals(ConfigrationRTA.RMS_TRADE_LICENSE)) {
                        mFragment = RMSEmiratesIDListofPendingPaymentDetails.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.RMS_EMPLOYEE_SERVICES)) {
                        mFragment = PaymentConfirmationRMSEmployeeServices.newInstance();
                        addFragment();
                    }
                    break;
               /* case R.id.btnrta_contactdetails_onlyemail_info:
                    break;*/
                case R.id.btnrta_contactdetails_onlyemail_home:
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

        tv_enterEmail.setText(resources.getString(R.string.EnteryourEmailAddress));
        tv_emailforReceipt.setText(resources.getString(R.string.getreceipt));
        btnContinue.setText(resources.getString(R.string.Continue));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.ContactDetails));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}