package rtaservices.RTADriverServices.Classes;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.ConfigurationVLDL;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.Common.Utilities;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ButtonAnimationListener;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.KskServiceTypeResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.ButtonAnimation;
import static utility.Constant.TAG;

public class RTADriverRenewalByLicenseNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    private static KskServiceTypeResponse ServiceType;
    private ArrayList<KskServiceTypeResponse> ServiceTypes = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_SearchDrivingLicense;
    private EditText edt_licenseNo, edt_birthYear;
    private Spinner sp_licenseSource;
    private String LicenseNo, BirthYear, LicenseSource, TrafficFileNo, ServiceId, ExpiryDate;
    // Response Final Object
    private static NPGKskInquiryResponse InquiryResponse;
    private static NPGKskInquiryResponseItem InquiryResponseItem;
    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private TextView tv_timer, tv_drrivingrenewlicenseNo, tv_drrivingrenewlicenseSource, tv_drrivingrenewbirthYear,
            tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    public RTADriverRenewalByLicenseNo() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTADriverRenewalByLicenseNo newInstance() {
        return new RTADriverRenewalByLicenseNo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utilities utilities = new Utilities();
        View view = inflater.inflate(R.layout.rtadriver_renewal_by_license_no, null);
        edt_licenseNo = (EditText) view.findViewById(R.id.edt_licenseno);
        edt_birthYear = (EditText) view.findViewById(R.id.edt_birthyear);
        sp_licenseSource = (Spinner) view.findViewById(R.id.sp_licensesource);
        btn_SearchDrivingLicense = (Button) view.findViewById(R.id.btn_searchdrivinglicense);
        btn_Back = (Button) view.findViewById(R.id.btn_rtadriverrenewalback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtadriverrenewalinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtadriverrenewalhome);
        tv_drrivingrenewlicenseNo = (TextView) view.findViewById(R.id.tv_drrivingrenewlicenseno);
        tv_drrivingrenewlicenseSource = (TextView) view.findViewById(R.id.tv_drrivingrenewlicensesource);
        tv_drrivingrenewbirthYear = (TextView) view.findViewById(R.id.tv_drrivingrenewbirthyear);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);


        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //button animation callback method
        // this.setButtonAnimationMethodCallBack(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Driver_Renewal);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTADriverServices.newInstance(), getFragmentManager());

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

        edt_licenseNo.setOnTouchListener(new View.OnTouchListener() {
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

        edt_birthYear.setOnTouchListener(new View.OnTouchListener() {
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
        edt_licenseNo.addTextChangedListener(Common.getTextWatcher(cTimer, edt_birthYear, edt_licenseNo, btn_SearchDrivingLicense));
        edt_birthYear.addTextChangedListener(Common.getTextWatcher(cTimer, edt_birthYear, edt_licenseNo, btn_SearchDrivingLicense));

        sp_licenseSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        /*edt_licenseNo.setText("76381");
        edt_birthYear.setText("1961");*/


        btn_SearchDrivingLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LicenseNo = edt_licenseNo.getText().toString();
                    BirthYear = edt_birthYear.getText().toString();
                    LicenseSource = utilities.getPlateSourceName(sp_licenseSource.getSelectedItem().toString());

                    ServiceId = ConfigurationVLDL.SERVICE_ID_DRIVER; //107

                    if (LicenseNo.equals("") || BirthYear.equals("")) {
                        if (cTimer != null)
                            cTimer.start();
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayer.callDriverInquiryByLicenseNo(ServiceId, LicenseSource, LicenseNo, BirthYear, new ServiceCallback() {
                            @Override
                            public void onSuccess(JSONObject obj) {

                                Gson gson = new Gson();

                                String reasonCode = null;
                                try {
                                    reasonCode = obj.getString("ReasonCode");
                                    if (!(reasonCode.equals("0000"))) {
                                        if (cTimer != null)
                                            cTimer.start();
                                        Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    if (cTimer != null)
                                        cTimer.start();
                                    e.printStackTrace();
                                }

                                JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                if (dataObject != null) {

                                    //Do things with object.
                                    ServiceType = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    InquiryResponseItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                                    TrafficFileNo = InquiryResponseItem.ServiceType.getItems().getItemText();
                                    ExpiryDate = InquiryResponseItem.ServiceType.getItems().getServiceDate();

                                } else {

                                    //Do things with array
                                    ServiceType = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    InquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    TrafficFileNo = InquiryResponseItem.ServiceType.getItems().getItemText();
                                    ExpiryDate = InquiryResponseItem.ServiceType.getItems().getServiceDate();
                                }

                                // This is Array of Items
                                if (InquiryResponse != null) {

                                    for (int i = 0; i < InquiryResponse.ServiceType.getItems().size(); i++)
                                        ServiceItems.add(InquiryResponse.getServiceType().getItems().get(i));

                                    Log.d("Service Response", InquiryResponse.Message);

                                    // This is object of Items
                                } else {
                                    ServiceTypes.add(ServiceType);
                                    ServiceItems.add(InquiryResponseItem.getServiceType().getItems());
                                    CustomerUniqueNo.add(InquiryResponseItem.CustomerUniqueNo.CKeyValuePair[0]);
                                    CustomerUniqueNo.add(InquiryResponseItem.CustomerUniqueNo.CKeyValuePair[1]);
                                    CustomerUniqueNo.add(InquiryResponseItem.CustomerUniqueNo.CKeyValuePair[2]);
                                    CustomerUniqueNo.add(new CKeyValuePair("TrfNo", TrafficFileNo));
                                    CustomerUniqueNo.add(new CKeyValuePair("ExpiryDate", ExpiryDate));
                                }

                                try {
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceTypeDetails, ServiceTypes);
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceItems, ServiceItems);
                                    Common.writeObject(getContext(), ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                PreferenceConnector.writeString(getContext(), Constant.TrafficFileNo, TrafficFileNo);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_DRIVER_RENEWAL);
                                //PreferenceConnector.writeString(getContext(), ConfigurationVLDL.IsLost,ReplacementReason);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.DriverTitle, ConfigurationVLDL.DriverRenewal);

                                Bundle bundlepos = new Bundle();
                                mFragment = RTADriverLicenseInquiryAndPaymentDetails.newInstance();
                                addFragment();
                              /*  bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceTypeDetails, ServiceTypes);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceItems, ServiceItems);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);
                                bundlepos.putString(Constant.TrafficFileNo, TrafficFileNo);
                                bundlepos.putString(ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_DRIVER_RENEWAL); //103
                                bundlepos.putString("DriverLicenseTitle", "Driving License Renewal");*/

                                //mFragment.setArguments(bundlepos);


                                Log.d("Service Response", obj.toString());

                            }

                            @Override
                            public void onFailure(String obj) {
                                if (cTimer != null)
                                    cTimer.start();
                                Log.d("Service Response", obj);
                            }
                        }, getContext());
                    }


                } catch (Exception ex) {
                    if (cTimer != null)
                        cTimer.start();
                    Log.d(TAG, ex.getMessage());
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
        if (edt_licenseNo.getText().toString().equals("") && edt_birthYear.getText().toString().equals(""))
            return;
        else {
            if (animationcheck == false) {
                //Animation(Fade in/Fade out) of the button
                animatorSet = ButtonAnimation(btn_SearchDrivingLicense);
                animationcheck = true;
            } else {
            }
        }

    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_SearchDrivingLicense.setText(resources.getString(R.string.Search));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_drrivingrenewlicenseNo.setText(resources.getString(R.string.LicenseNo));
        tv_drrivingrenewlicenseSource.setText(resources.getString(R.string.LicenseSource));
        tv_drrivingrenewbirthYear.setText(resources.getString(R.string.BirthYear));
        tvrtaServiceTitle.setText(resources.getString(R.string.DrivingLicenseRenewal));
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
            switch (v.getId()) {
                case R.id.btn_rtadriverrenewalback:
                    mFragment = RTADriverServices.newInstance();
                    addFragment();
                    break;
                /*case R.id.btn_rtadriverrenewalinfo:
                    break;*/
                case R.id.btn_rtadriverrenewalhome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }
}
