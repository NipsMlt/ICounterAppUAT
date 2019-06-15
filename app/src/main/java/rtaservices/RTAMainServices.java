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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import MerchantDetailsService.MerchantMultipleDetail;
import MerchantDetailsService.NipsMerchantDetailsResponse;
import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.MerchantDetailsCallback;
import RTANetworking.Interfaces.ServiceCallback;
import example.dtc.R;
import interfaces.ResetTimerListener;
import rtamain.RTAMain;
import rtaservices.RTACertificates.Classes.RTACertificateServices;
import rtaservices.RTADriverServices.Classes.RTADriverServices;
import rtaservices.RTAVehicleServices.Classes.RTAVehicleServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.FetchSerialNumber;
import utility.PreferenceConnector;

import static rtamain.RTAMain.AndroidSerialNo;

//import RTANetworking.RequestAndResponse.NipsMerchantDetailsResponse;

public class RTAMainServices extends Fragment implements View.OnClickListener, ResetTimerListener {
    private Fragment mFragment;
    private TextView tv_timer, tvrtaServiceTitle, tv_selectanyOption, tv_Seconds;

    private Button btn_Fineinquirypayment, btn_Vehicleregservices,
            btn_Drivinglicenseservices, btn_Parkingservices, btn_Printingservices, btn_otherrtaServices, btn_Back, btn_Info, btn_Home;
    CountDownTimer cTimer;
    String ClassName;
    private String Language;
    private MediaPlayer mPlayer;
    ImageView iv_mlt_Logo;
    int opendialog = 0;
    RTAMLTDialogFragment rtamltDialogFragment = new RTAMLTDialogFragment();
    FetchSerialNumber fetchSerialNumber = new FetchSerialNumber();
    Context context;
    Resources resources;
    private String BankID, TerminalId, MerchantId, LoginId, Password, SecretKey,
            PaymentUserId, PaymentSecretKey, PaymentPassword, DeviceSerialNumber, SourceApplication, PaymentAction, CallBackURL, DeviceFingerPrint,
            IPAssign, PaymentVersion, Currency, Channel, Language_Merchant, RequestCategory, RequestTypePayment, Is3DSecure, SilentOrderAPIURL;
    private NipsMerchantDetailsResponse resObjectMerchantdetails;
    private static MerchantDetailsCallback merchantDetailsCallback;
    private List<MerchantMultipleDetail> merchantDetails = new ArrayList<>();
    private String Merchant;
    WindowManager.LayoutParams layoutpars;
    //Window object, that will store a reference to the current window
    private Window window;
    Integer int_brightness;

    public RTAMainServices() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTAMainServices newInstance() {
        return new RTAMainServices();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtaserviesmain, container, false);
        try {
            //Ids declaration
            btn_Fineinquirypayment = (Button) view.findViewById(R.id.btn_fineinquirypayment);
            btn_Vehicleregservices = (Button) view.findViewById(R.id.btn_vehicleregservices);
            btn_Drivinglicenseservices = (Button) view.findViewById(R.id.btn_drivinglicenseservices);
            //btn_Parkingservices = (Button) view.findViewById(R.id.btn_parkingservices);
            btn_Printingservices = (Button) view.findViewById(R.id.btn_printingservices);
            //btn_otherrtaServices = (Button) view.findViewById(R.id.btn_otherrtaservices);
            btn_Info = (Button) view.findViewById(R.id.btnrtamaininfo);
            btn_Home = (Button) view.findViewById(R.id.btnrtamainhome);
            tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
            tv_selectanyOption = (TextView) view.findViewById(R.id.tv_selectanyoption);
            tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
            tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
            iv_mlt_Logo = (ImageView) view.findViewById(R.id.iv_mlt_logo);

            //Set the language resources
            context = LocaleHelper.setLocale(getContext(), Language);
            resources = context.getResources();

            //Setting click listeners for button
            btn_Fineinquirypayment.setOnClickListener(this);
            btn_Vehicleregservices.setOnClickListener(this);
            btn_Drivinglicenseservices.setOnClickListener(this);
            //btn_Parkingservices.setOnClickListener(this);
            btn_Printingservices.setOnClickListener(this);
            //btn_otherrtaServices.setOnClickListener(this);
            //btn_Back.setOnClickListener(this);
            //btn_Info.setOnClickListener(this);
            btn_Home.setOnClickListener(this);
            rtamltDialogFragment.setMethodCallBack(this);
            iv_mlt_Logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opendialog == 0) {
                        rtamltDialogFragment.show(getFragmentManager(), "");
                        opendialog = 1;
                    } else if (opendialog == 1)
                        opendialog = 0;

                }
            });

            //Call the function to check which language code is it and change it accordingly
            ChangeLanguage(Language);
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAMain.newInstance(), getFragmentManager());

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

        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), AndroidSerialNo);
        }

        try {
            //MerchantConfiguration.getMerchantDetails(getContext(), Constant.RTA);
            //Get the device Serial Number
            //DeviceSerialNumber = fetchSerialNumber.getSerialNumber();
            DeviceSerialNumber = "E2C1000590";//E2C1000954 /<-**Prod**/ //"E2C1000590";  /<-**UAT**/

            //Merchant details service to get the configuration values like Merchant_id etc
            ServiceLayer.callServiceByDeviceSerialNumber(new ServiceCallback() {
                @Override
                public void onSuccess(JSONObject obj) throws JSONException {
                    try {
                        Gson gson = new Gson();
                        resObjectMerchantdetails = gson.fromJson(obj.toString(), NipsMerchantDetailsResponse.class);
                        merchantDetails = resObjectMerchantdetails.getMerchantDetail().getMerchantMultipleDetails();
                        for (int i = 0; i < merchantDetails.size(); i++) {
                            Merchant = merchantDetails.get(i).getMerchantServiceType();
                            if (Constant.RTA.equalsIgnoreCase(Merchant)) {
                                PreferenceConnector.writeString(getContext(), Constant.RTA, Merchant);
                                DeviceSerialNumber = merchantDetails.get(i).getItemDetails().getField1();
                                MerchantId = merchantDetails.get(i).getItemDetails().getField2();
                                TerminalId = merchantDetails.get(i).getItemDetails().getField3();
                                SecretKey = merchantDetails.get(i).getItemDetails().getField4();// From the service it comes as a secret key
                                BankID = merchantDetails.get(i).getItemDetails().getField5();
                                PaymentPassword = merchantDetails.get(i).getItemDetails().getField6();
                                PaymentUserId = merchantDetails.get(i).getItemDetails().getField7();
                                PaymentAction = merchantDetails.get(i).getItemDetails().getField8();
                                Channel = merchantDetails.get(i).getItemDetails().getField9();
                                RequestTypePayment = merchantDetails.get(i).getItemDetails().getField10();
                                RequestCategory = merchantDetails.get(i).getItemDetails().getField11();
                                CallBackURL = merchantDetails.get(i).getItemDetails().getField12();
                                Language_Merchant = merchantDetails.get(i).getItemDetails().getField13();
                                SourceApplication = merchantDetails.get(i).getItemDetails().getField14();
                                Currency = merchantDetails.get(i).getItemDetails().getField15();
                                DeviceFingerPrint = merchantDetails.get(i).getItemDetails().getField16();
                                Is3DSecure = merchantDetails.get(i).getItemDetails().getField17();
                                SilentOrderAPIURL = merchantDetails.get(i).getItemDetails().getField18();
                                LoginId = merchantDetails.get(i).getItemDetails().getField28();
                                Password = merchantDetails.get(i).getItemDetails().getField29();
                                PaymentSecretKey = merchantDetails.get(i).getItemDetails().getField30(); //From the service it comes as a payment secret key

                                // write the values in the cache to get in different classes
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.DEVICE_SERIAL_NUMBER_MERCHANT_DETAILS_KEY, DeviceSerialNumber);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.BANK_ID_MERCHANT_DETAILS_KEY, BankID);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.TERMINAL_ID_MERCHANT_DETAILS_KEY, TerminalId);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.MERCHANT_ID_MERCHANT_DETAILS_KEY, MerchantId);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.LOGIN_ID_MERCHANT_DETAILS_KEY, LoginId);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.PASSWORD_MERCHANT_DETAILS_KEY, Password);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.SECRETKEY_MERCHANT_DETAILS_KEY, SecretKey);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.PAYMENT_USER_ID_MERCHANT_DETAILS_KEY, PaymentUserId);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.PAYMENT_PASSWORD_MERCHANT_DETAILS_KEY, PaymentPassword);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.PAYMENT_SECRETKEY_MERCHANT_DETAILS_KEY, PaymentSecretKey);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.SOURCE_APPLICATION_MERCHANT_DETAILS_KEY, SourceApplication);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.PAYMENT_ACTION_MERCHANT_DETAILS_KEY, PaymentAction);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.CALLBACK_URL_MERCHANT_DETAILS_KEY, CallBackURL);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.DEVICE_FINGER_PRINT_MERCHANT_DETAILS_KEY, DeviceFingerPrint);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.CURRENCY_MERCHANT_DETAILS_KEY, Currency);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.CHANNEL_MERCHANT_DETAILS_KEY, Channel);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.LANGUAGE_MERCHANT_DETAILS_KEY, Language_Merchant);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.REQUEST_CATEGORY_MERCHANT_DETAILS_KEY, RequestCategory);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.REQUEST_TYPE_PAYMENT_MERCHANT_DETAILS_KEY, RequestTypePayment);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.IS_THREED_SECURE_MERCHANT_DETAILS_KEY, Is3DSecure);
                                PreferenceConnector.writeString(getContext(), ConfigrationRTA.SILENT_ORDER_API_URL_MERCHANT_DETAILS_KEY, SilentOrderAPIURL);
                            }
                        }
                        //Initialization of an object to call the callback method in the constructor
                        ConfigrationRTA configrationRTA = new ConfigrationRTA(getContext());

                        if (merchantDetailsCallback != null) {
                            merchantDetailsCallback.MerchantDetails(getContext());
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }


                }

                @Override
                public void onFailure(String obj) {
                }

            }, getContext(), DeviceSerialNumber);
        } catch (Exception ex) {
            ex.getStackTrace();

        }

        return view;
    }

    //Set Call back method to get the merchant details values from the service
    public static void setMerchantDetailsCallBackMethod(MerchantDetailsCallback CallBack) {
        merchantDetailsCallback = CallBack;
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
                case R.id.btn_fineinquirypayment:
                    mFragment = RTAMainSubServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_vehicleregservices:
                    mFragment = RTAVehicleServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_drivinglicenseservices:
                    mFragment = RTADriverServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_printingservices:
                    mFragment = RTACertificateServices.newInstance();
                    addFragment();
                    break;
               /* case R.id.btn_parkingservices:
                    mFragment = RTAParkingServices.newInstance();
                    addFragment();
                    break;*/
                /*case R.id.btn_otherrtaservices:
                    mFragment = RMSServices.newInstance();
                    addFragment();
                    //Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                    break;*/
                /*case R.id.btnrtamaininfo:
                    Bundle bundlepos = new Bundle();
                    InfoDialogFragment infoDialogFragment = new InfoDialogFragment();
                    bundlepos.putString(Constant.INFO_TEXT, resources.getString(R.string.infotext_RTAMainServices));
                    infoDialogFragment.show(getFragmentManager(), "");
                    break;*/
                case R.id.btnrtamainhome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), AndroidSerialNo);
        }

    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {

        btn_Fineinquirypayment.setText(resources.getString(R.string.FineInquiryandPayments));
        btn_Vehicleregservices.setText(resources.getString(R.string.VehicleRegistrationServices));
        btn_Drivinglicenseservices.setText(resources.getString(R.string.DrivingLicenseServices));
        btn_Printingservices.setText(resources.getString(R.string.PrintingCertificate));
        //btn_otherrtaServices.setText(resources.getString(R.string.OtherRTAServices));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        tvrtaServiceTitle.setText(resources.getString(R.string.RTAService));
        tv_selectanyOption.setText(resources.getString(R.string.Selectanyoneoption));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    private void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void ResetTimerCallBackMethod() {
        if (cTimer != null)
            cTimer.start();

    }
}

