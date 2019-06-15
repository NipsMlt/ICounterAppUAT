package rtaservices.RTAVehicleServices.Classes;

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


public class RTAVehicleRenewalByPlateNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Search_Plateno;
    private EditText edt_plateNo, edt_birthYear;
    private Spinner sp_licensesourcePlateNo, sp_platecode_PlateNo, sp_platecaegory_PalteNo;
    private String Plateno, Birthyear, PlateSource, PlateCode, PlateCategory, TrafficFileNo, ServiceId, ServiceName;
    private static NPGKskInquiryResponse vsInquiryResponse;
    private static KskServiceTypeResponse vsServiceTypeDetail;
    private ArrayList<KskServiceTypeResponse> vsServiceTypeDetails = new ArrayList<>();
    private static NPGKskInquiryResponseItem resObjectItem;
    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    CountDownTimer cTimer;
    private String Language;
    private TextView tv_timer, tvrtaServiceTitle, tv_Seconds, tv_vehiclerenewalplateNo,
            tv_vehiclerenewallicenseSource, tv_vehiclerenewalbirthYear, tv_vehiclerenewalplateCategory,
            tv_vehiclerenewalplateCode;
    private MediaPlayer mPlayer;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    public RTAVehicleRenewalByPlateNo() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTAVehicleRenewalByPlateNo newInstance() {
        return new RTAVehicleRenewalByPlateNo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtavehicle_renewal_by_plate_no, container, false);

        edt_plateNo = (EditText) view.findViewById(R.id.edt_vs_vr_plateno);
        edt_birthYear = (EditText) view.findViewById(R.id.edt_vs_vr_birthyear);
        sp_licensesourcePlateNo = (Spinner) view.findViewById(R.id.sp_vs_vr_licensesourceplateno);
        sp_platecode_PlateNo = (Spinner) view.findViewById(R.id.sp_vs_vr_platecode_plateno);
        sp_platecaegory_PalteNo = (Spinner) view.findViewById(R.id.sp_vs_vr_platecaegory);
        btn_Search_Plateno = (Button) view.findViewById(R.id.btn_vs_vr_search_plateno);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_vehiclerenewalplateNo = (TextView) view.findViewById(R.id.tv_vehiclerenewalplateno);
        tv_vehiclerenewallicenseSource = (TextView) view.findViewById(R.id.tv_vehiclerenewallicensesource);
        tv_vehiclerenewalbirthYear = (TextView) view.findViewById(R.id.tv_vehiclerenewalbirthyear);
        tv_vehiclerenewalplateCategory = (TextView) view.findViewById(R.id.tv_vehiclerenewalplatecategory);
        tv_vehiclerenewalplateCode = (TextView) view.findViewById(R.id.tv_vehiclerenewalplatecode);
        btn_Back = (Button) view.findViewById(R.id.btn_rtavehiclerenewalback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtavehiclerenewalinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtavehiclerenewalhome);

        btn_Back.setOnClickListener(this);
       // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //button animation callback method
        //this.setButtonAnimationMethodCallBack(this);


        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAVehicleServices.newInstance(), getFragmentManager());

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Vehicle_Renewal);


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

        edt_plateNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
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
        edt_plateNo.addTextChangedListener(Common.getTextWatcher(cTimer, edt_plateNo, edt_birthYear, btn_Search_Plateno));
        edt_birthYear.addTextChangedListener(Common.getTextWatcher(cTimer, edt_plateNo, edt_birthYear, btn_Search_Plateno));

        sp_licensesourcePlateNo.setOnTouchListener(new View.OnTouchListener() {
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
        sp_platecode_PlateNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });
        sp_platecaegory_PalteNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        /*edt_plateNo.setText("54349");
        edt_birthYear.setText("1957");*/


        btn_Search_Plateno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cTimer != null)
                        cTimer.cancel();
                    Utilities utilities = new Utilities();
                    PlateSource = utilities.getPlateSourceName(sp_licensesourcePlateNo.getSelectedItem().toString());
                    PlateCode = sp_platecode_PlateNo.getSelectedItem().toString();
                    PlateCategory = sp_platecaegory_PalteNo.getSelectedItem().toString();
                    Plateno = edt_plateNo.getText().toString();
                    Birthyear = edt_birthYear.getText().toString();
                    ServiceId = ConfigurationVLDL.SERVICE_ID_VEHICLE;
                    ServiceName = ConfigurationVLDL.SNInquiryRTAVehicleDetailByPlateInfo;

                    if (Plateno.equals("") || Birthyear.equals("")) {
                        if (cTimer != null)
                            cTimer.start();
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                    } else {
                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayer.callInquiryByPlateNo(ServiceId, ServiceName, Plateno, PlateCode, PlateCategory, PlateSource, Birthyear, new ServiceCallback() {
                            @Override
                            public void onSuccess(JSONObject obj) {

                                Gson gson = new Gson();

                                String reasonCode = null;
                                try {
                                    reasonCode = obj.getString("ReasonCode");
                                    if (!(reasonCode.equals("0000"))) {
                                        if (cTimer != null)
                                            cTimer.start();
                                        Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    if (cTimer != null)
                                        cTimer.start();
                                    e.printStackTrace();
                                }

                                // Check the object or Array Items
                                JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                if (dataObject != null) {

                                    //Do things with object.
                                    vsServiceTypeDetail = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                                    TrafficFileNo = resObjectItem.ServiceType.getItems().getItemText();

                                } else {

                                    //Do things with array
                                    vsServiceTypeDetail = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    vsInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    TrafficFileNo = resObjectItem.ServiceType.getItems().getItemText();
                                }

                                // This is Array of Items
                                if (vsInquiryResponse != null) {

                                    for (int i = 0; i < vsInquiryResponse.ServiceType.getItems().size(); i++)
                                        ServiceItems.add(vsInquiryResponse.getServiceType().getItems().get(i));

                                    Log.d("Service Response", vsInquiryResponse.Message);
                                } else {
                                    vsServiceTypeDetails.add(vsServiceTypeDetail);
                                    ServiceItems.add(resObjectItem.getServiceType().getItems());
                                    CustomerUniqueNo.add(resObjectItem.CustomerUniqueNo.CKeyValuePair[0]);
                                    CustomerUniqueNo.add(resObjectItem.CustomerUniqueNo.CKeyValuePair[1]);
                                    CustomerUniqueNo.add(resObjectItem.CustomerUniqueNo.CKeyValuePair[2]);
                                    CustomerUniqueNo.add(resObjectItem.CustomerUniqueNo.CKeyValuePair[3]);
                                    CustomerUniqueNo.add(resObjectItem.CustomerUniqueNo.CKeyValuePair[4]);
                                }

                                try {
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceTypeDetails, vsServiceTypeDetails);
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceItems, ServiceItems);
                                    Common.writeObject(getContext(), ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                PreferenceConnector.writeString(getContext(), Constant.TrafficFileNo, TrafficFileNo);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_VEHICLE_RENEWAL);
                                //PreferenceConnector.writeString(getContext(), ConfigurationVLDL.IsLost,ReplacementReason);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.VehicleTitle, ConfigurationVLDL.VehicleLostRenewal);

                                Bundle bundlepos = new Bundle();
                                mFragment = RTAVehicleRenewalInquiryAndPaymentDetails.newInstance();
                                addFragment();
                               /* bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceTypeDetails, vsServiceTypeDetails);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceItems, ServiceItems);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);
                                bundlepos.putString(Constant.TrafficFileNo, TrafficFileNo);
                                bundlepos.putString(ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_VEHICLE_RENEWAL); //204
                                bundlepos.putString("VehicleTitle", "Vehicle Renewal");*/

                                // mFragment.setArguments(bundlepos);


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
        if (edt_plateNo.getText().toString().equals("") && edt_birthYear.getText().toString().equals(""))
            return;
        else {
            if (animationcheck == false) {
                //Animation(Fade in/Fade out) of the button
                animatorSet = ButtonAnimation(btn_Search_Plateno);
                animationcheck = true;
            } else {
            }
        }

    }


    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Search_Plateno.setText(resources.getString(R.string.Search));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_vehiclerenewalplateNo.setText(resources.getString(R.string.PlateNumber));
        tv_vehiclerenewallicenseSource.setText(resources.getString(R.string.LicenseSource));
        tv_vehiclerenewalbirthYear.setText(resources.getString(R.string.BirthYear));
        tv_vehiclerenewalplateCategory.setText(resources.getString(R.string.Platecategory));
        tv_vehiclerenewalplateCode.setText(resources.getString(R.string.Platecode));
        tvrtaServiceTitle.setText(resources.getString(R.string.VehicleRegistrationRenewal));
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
                case R.id.btn_rtavehiclerenewalback:
                    mFragment = RTAVehicleServices.newInstance();
                    addFragment();
                    break;
               /* case R.id.btn_rtavehiclerenewalinfo:
                    break;*/
                case R.id.btn_rtavehiclerenewalhome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }
}
