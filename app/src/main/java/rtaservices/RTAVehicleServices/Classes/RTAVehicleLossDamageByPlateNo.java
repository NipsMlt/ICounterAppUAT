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

public class RTAVehicleLossDamageByPlateNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Search_Plateno;
    private EditText edt_plateNo, edt_birthYear;
    private Spinner sp_licensesourcePlateNo, sp_platecode_PlateNo, sp_platecaegory_PalteNo, sp_replacementreason;
    private String Plateno, Birthyear, PlateSource, PlateCode, PlateCategory, TrafficFileNo, ServiceId, ServiceName, ReplacementReason;

    private static NPGKskInquiryResponse vsInquiryResponse;
    private static NPGKskInquiryResponseItem vsInquiryResponseItem;
    private static KskServiceTypeResponse vsServiceType;
    private ArrayList<KskServiceItem> vsServiceItems = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    private ArrayList<KskServiceTypeResponse> vsServiceTypes = new ArrayList<>();
    CountDownTimer cTimer;
    private TextView tv_timer, tv_Seconds, tvrtaService, tv_vehiclelossplateNo, tv_vehiclelosslicenseSource,
            tv_vehiclelossbirthYear, tv_vehiclelossplateCategory, tv_vehiclelossplateCode,
            tv_vehiclelossreplacementReason;
    private String Language;
    private MediaPlayer mPlayer;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    public static RTAVehicleLossDamageByPlateNo newInstance() {
        return new RTAVehicleLossDamageByPlateNo();
    }

    public RTAVehicleLossDamageByPlateNo() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtavehicle_loss_damage_by_plate_no, container, false);

        edt_plateNo = (EditText) view.findViewById(R.id.edt_vehicleloss_plateno);
        edt_birthYear = (EditText) view.findViewById(R.id.edt_vehicleloss_birthyear);
        sp_licensesourcePlateNo = (Spinner) view.findViewById(R.id.sp_licensesourceplateno);
        sp_platecode_PlateNo = (Spinner) view.findViewById(R.id.sp_platecode_plateno);
        sp_platecaegory_PalteNo = (Spinner) view.findViewById(R.id.sp_platecaegory_palteno);
        sp_replacementreason = (Spinner) view.findViewById(R.id.sp_replacement_reason);
        btn_Search_Plateno = (Button) view.findViewById(R.id.btnsearch_plateno);
        btn_Back = (Button) view.findViewById(R.id.btn_rtavehiclelossback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtavehiclelossinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtavehiclelosshome);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tvrtaService = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_vehiclelossplateNo = (TextView) view.findViewById(R.id.tv_vehiclelossplateno);
        tv_vehiclelosslicenseSource = (TextView) view.findViewById(R.id.tv_vehiclelosslicensesource);
        tv_vehiclelossbirthYear = (TextView) view.findViewById(R.id.tv_vehiclelossbirthyear);
        tv_vehiclelossplateCategory = (TextView) view.findViewById(R.id.tv_vehiclelossplatecategory);
        tv_vehiclelossplateCode = (TextView) view.findViewById(R.id.tv_vehiclelossplatecode);
        tv_vehiclelossreplacementReason = (TextView) view.findViewById(R.id.tv_vehiclelossreplacementreason);

        btn_Back.setOnClickListener(this);
       // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //button animation callback method
        //this.setButtonAnimationMethodCallBack(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        /*edt_plateNo.setText("68997");
        edt_birthYear.setText("1964");*/

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAVehicleServices.newInstance(), getFragmentManager());

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Vehicle_Damaged_Lost);


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
        edt_plateNo.addTextChangedListener(Common.getTextWatcher(cTimer, edt_plateNo, edt_birthYear, btn_Search_Plateno));
        edt_birthYear.addTextChangedListener(Common.getTextWatcher(cTimer, edt_plateNo, edt_birthYear, btn_Search_Plateno));

        sp_licensesourcePlateNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
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
        sp_replacementreason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });


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
                    ReplacementReason = sp_replacementreason.getSelectedItem().toString();
                    Plateno = edt_plateNo.getText().toString();
                    Birthyear = edt_birthYear.getText().toString();
                    ServiceId = ConfigurationVLDL.SERVICE_ID_VEHICLE; //106
                    ServiceName = ConfigurationVLDL.SNInquiryRTAVehicleDetailByPlateInfo; //InquiryRTAVehicleDetailByPlateInfo

                    //Write Plate code
                    PreferenceConnector.writeString(getContext(), Constant.PlateCode, PlateCode);

                    if (Plateno.equals("") || Birthyear.equals("")) {
                        if (cTimer != null)
                            cTimer.start();
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_SHORT).show();
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
                                    vsServiceType = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    vsInquiryResponseItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                                    TrafficFileNo = vsInquiryResponseItem.ServiceType.getItems().getItemText();

                                } else {

                                    //Do things with array
                                    vsServiceType = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                                    vsInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    TrafficFileNo = vsInquiryResponseItem.ServiceType.getItems().getItemText();
                                }

                                // This is Array of Items
                                if (vsInquiryResponse != null) {

                                    for (int i = 0; i < vsInquiryResponse.ServiceType.getItems().size(); i++)
                                        vsServiceItems.add(vsInquiryResponse.getServiceType().getItems().get(i));

                                    Log.d("Service Response", vsInquiryResponse.Message);

                                    // This is object of Items
                                } else {
                                    vsServiceTypes.add(vsServiceType);
                                    vsServiceItems.add(vsInquiryResponseItem.getServiceType().getItems());
                                    CustomerUniqueNo.add(vsInquiryResponseItem.CustomerUniqueNo.CKeyValuePair[0]);
                                    CustomerUniqueNo.add(vsInquiryResponseItem.CustomerUniqueNo.CKeyValuePair[1]);
                                    CustomerUniqueNo.add(vsInquiryResponseItem.CustomerUniqueNo.CKeyValuePair[2]);
                                    CustomerUniqueNo.add(vsInquiryResponseItem.CustomerUniqueNo.CKeyValuePair[3]);
                                    CustomerUniqueNo.add(vsInquiryResponseItem.CustomerUniqueNo.CKeyValuePair[4]);
                                }
                                try {
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceTypeDetails, vsServiceTypes);
                                    Common.writeObject(getContext(), ConfigurationVLDL.ServiceItems, vsServiceItems);
                                    Common.writeObject(getContext(), ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                PreferenceConnector.writeString(getContext(), Constant.TrafficFileNo, TrafficFileNo);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_VEHICLE_LOSS_DAMAGE);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.IsLost, ReplacementReason);
                                PreferenceConnector.writeString(getContext(), ConfigurationVLDL.VehicleTitle, ConfigurationVLDL.VehicleLostDamage);

                                Bundle bundlepos = new Bundle();
                                mFragment = RTAVehicleRenewalInquiryAndPaymentDetails.newInstance();
                                addFragment();
                               /* bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceTypeDetails, vsServiceTypes);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.ServiceItems, vsServiceItems);
                                bundlepos.putParcelableArrayList(ConfigurationVLDL.CustomerUniqueNo, CustomerUniqueNo);*/
                                //bundlepos.putString(Constant.TrafficFileNo, TrafficFileNo);
                               /* bundlepos.putString(ConfigurationVLDL.SetviceCode, ConfigurationVLDL.SETVICE_ID_VEHICLE_LOSS_DAMAGE); //211
                                bundlepos.putString(ConfigurationVLDL.IsLost, ReplacementReason);
                                bundlepos.putString(ConfigurationVLDL.VehicleTitle,ConfigurationVLDL.VehicleLostDamage);*/
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
        tv_vehiclelossplateNo.setText(resources.getString(R.string.PlateNumber));
        tv_vehiclelosslicenseSource.setText(resources.getString(R.string.LicenseSource));
        tv_vehiclelossbirthYear.setText(resources.getString(R.string.BirthYear));
        tv_vehiclelossplateCategory.setText(resources.getString(R.string.Platecategory));
        tv_vehiclelossplateCode.setText(resources.getString(R.string.Platecode));
        tv_vehiclelossreplacementReason.setText(resources.getString(R.string.ReplacementReason));
        tvrtaService.setText(resources.getString(R.string.VehicleRegistrationLossDamage));
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
                case R.id.btn_rtavehiclelossback:
                    mFragment = RTAVehicleServices.newInstance();
                    addFragment();
                    break;
                /*case R.id.btn_rtavehiclelossinfo:
                    break;*/
                case R.id.btn_rtavehiclelosshome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }
}
