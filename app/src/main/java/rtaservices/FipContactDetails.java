package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

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
import rtaservices.RTACertificates.Classes.RTACertificateClearanceByPlateNo;
import rtaservices.RTACertificates.Classes.RTACertificateDrivingExperienceByLicenseNo;
import rtaservices.RTACertificates.Classes.RTACertificateInsuranceRefundByPlateNo;
import rtaservices.RTACertificates.Classes.RTACertificateNonOwnershipByTrfNo;
import rtaservices.RTACertificates.Classes.RTACertificateOwnershipByPlateNo;
import rtaservices.RTADriverServices.Classes.RTADriverLicenseInquiryAndPaymentDetails;
import rtaservices.RTAVehicleServices.Classes.RTAVehicleRenewalInquiryAndPaymentDetails;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class FipContactDetails extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btnContinue;
    private EditText edt_customer_Email, edt_customer_No;
    String Customer_Email, Customer_No, FragmentName, ServiceName;
    boolean validate_customer_Email, validate_customer_phone_No;
    private TextView tv_timer, tvrtaServiceTitle, tv_emailAddress, tv_mobileNo, tv_txtentervalidEmail, tv_Seconds;
    //Declare timer
    CountDownTimer cTimer = null;
    private String Language, ClassName, TotalAmount, TransactionID;
    String[] TotalAmountarr = new String[2];
    private MediaPlayer mPlayer;
    Gson gson = new Gson();
    NPGKskInquiryResponse npgKskInquiryResponse = new NPGKskInquiryResponse();
    private String ServiceChargeonCard;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static FipContactDetails newInstance() {
        return new FipContactDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaserviescontactdetails, null);

        btnContinue = (Button) view.findViewById(R.id.btnscontinuecontactdetails);
        edt_customer_Email = (EditText) view.findViewById(R.id.edt_customer_email);
        edt_customer_No = (EditText) view.findViewById(R.id.edt_customer_no);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_emailAddress = (TextView) view.findViewById(R.id.tv_emailaddress);
        tv_mobileNo = (TextView) view.findViewById(R.id.tv_mobileno);
        tv_txtentervalidEmail = (TextView) view.findViewById(R.id.tv_txtentervalidemail);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        btn_Back = (Button) view.findViewById(R.id.btnrtacontactdetailsback);
        btn_Info = (Button) view.findViewById(R.id.btnrtacontactdetailinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtacontactdetailhome);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Get the fragment name as per the services
        ServiceName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, ""); //Traffic Fines or others

        //Work for timer
        if (ServiceName.equals(ConfigrationRTA.Vehicle_Renewal) || ServiceName.equals(ConfigrationRTA.Vehicle_Damaged_Lost)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTAVehicleRenewalInquiryAndPaymentDetails.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.Driver_Renewal) || ServiceName.equals(ConfigrationRTA.Driving_Damaged_Lost)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTADriverLicenseInquiryAndPaymentDetails.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.Ownership_Cert)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTACertificateOwnershipByPlateNo.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.Non_Ownership_Cert)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTACertificateNonOwnershipByTrfNo.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.Experience_Cert)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTACertificateDrivingExperienceByLicenseNo.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.No_Obligation_Cert)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTACertificateClearanceByPlateNo.newInstance(), getFragmentManager());
        } else if (ServiceName.equals(ConfigrationRTA.Refund_Insurance_Cert)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RTACertificateInsuranceRefundByPlateNo.newInstance(), getFragmentManager());
        }

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //when the view is touched
        edt_customer_Email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });
        edt_customer_No.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        //Timer is getting reset on text changed
        edt_customer_Email.addTextChangedListener(Common.getTextWatcher(cTimer));
        edt_customer_No.addTextChangedListener(Common.getTextWatcher(cTimer));


        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.enteremailandmobile);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.enteremailandmobilearabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.enteremailandmobileurdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.enteremailandmobilechinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.enteremailandmobilemalayalam);
        mPlayer.start();


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
                    validate_customer_phone_No = Common.validatePhoneNo(getContext(), edt_customer_No);

                    if (!validate_customer_Email)
                        return;
                    else if (!validate_customer_phone_No)
                        return;

                    Customer_Email = edt_customer_Email.getText().toString();
                    Customer_No = edt_customer_No.getText().toString();

                    PreferenceConnector.writeString(getContext(), Constant.Customer_Email_RTA, Customer_Email);
                    PreferenceConnector.writeString(getContext(), Constant.Customer_Phone_RTA, Customer_No);
                    if (cTimer != null)
                        cTimer.cancel();
                   /* if (ServicesName.equals(ConfigrationRTA.RMS_Services)) {
                        ServiceLayer.callInquiryServiceCharges(TotalAmount, TransactionID, new ServiceCallback() {
                            @Override
                            public void onSuccess(JSONObject obj) throws JSONException {
                                try {
                                    npgKskInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    ServiceChargeonCard = npgKskInquiryResponse.ServiceAttributesList.CKeyValuePair[0].Value;
                                    if (cTimer != null)
                                        cTimer.cancel();
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
                    } else {*/
                    //if Service is not RMS
                    mFragment = FipTotalAmount.newInstance();
                    addFragment();
                    //}

                } catch (Exception e) {

                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        try {
            if (cTimer != null)
                cTimer.cancel();
           try {                 if (mPlayer != null) {                     mPlayer.stop();                     mPlayer.release();                 }             } catch (Exception e) {             }
            switch (v.getId()) {
                case R.id.btnrtacontactdetailsback:
                    if (ServiceName.equals(ConfigrationRTA.Vehicle_Renewal) || ServiceName.equals(ConfigrationRTA.Vehicle_Damaged_Lost)) {
                        mFragment = RTAVehicleRenewalInquiryAndPaymentDetails.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.Driver_Renewal) || ServiceName.equals(ConfigrationRTA.Driving_Damaged_Lost)) {
                        mFragment = RTADriverLicenseInquiryAndPaymentDetails.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.Ownership_Cert)) {
                        mFragment = RTACertificateOwnershipByPlateNo.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.Non_Ownership_Cert)) {
                        mFragment = RTACertificateNonOwnershipByTrfNo.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.Experience_Cert)) {
                        mFragment = RTACertificateDrivingExperienceByLicenseNo.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.No_Obligation_Cert)) {
                        mFragment = RTACertificateClearanceByPlateNo.newInstance();
                        addFragment();
                    } else if (ServiceName.equals(ConfigrationRTA.Refund_Insurance_Cert)) {
                        mFragment = RTACertificateInsuranceRefundByPlateNo.newInstance();
                        addFragment();
                    }
                    break;
                /*case R.id.btnrtacontactdetailinfo:
                    break;*/
                case R.id.btnrtacontactdetailhome:
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

        tv_emailAddress.setText(resources.getString(R.string.EnteryourEmailAddressor));
        tv_mobileNo.setText(resources.getString(R.string.EnteryourMobileNumber));
        tv_txtentervalidEmail.setText(resources.getString(R.string.getdigitalpass));
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