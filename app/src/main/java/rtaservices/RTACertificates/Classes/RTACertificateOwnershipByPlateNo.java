package rtaservices.RTACertificates.Classes;

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
import RTANetworking.GenericServiceCall.ServiceLayerVLDL;
import RTANetworking.Interfaces.ButtonAnimationListener;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.KskServiceTypeResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.FipContactDetails;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.ButtonAnimation;
import static RTANetworking.Common.Utilities.setAddressedDeptKeyValue;
import static RTANetworking.Common.Utilities.setAddressedDeptKeyValueArabic;
import static utility.Common.writeObject;
import static utility.Constant.TAG;

public class RTACertificateOwnershipByPlateNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Search_Plateno;
    private EditText edt_plateNo, edt_birthYear;
    private Spinner sp_licensesourcePlateNo, sp_platecode_PlateNo, sp_platecaegory_PalteNo, sp_vehicles;
    private String Plateno, Birthyear, PlateSource, PlateCode, PlateCategory, TrafficFileNo, ServiceId, ServiceName, Vehicle;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    private String serviceId, trfNo, setviceCode, chassisNo, isMortgage, cauCode;

    private static KioskPaymentResponseItem resObjectItem2;
    private static NPGKskInquiryResponse vsInquiryResponse;
    private static KskServiceTypeResponse vsServiceTypeDetail;
    private ArrayList<KskServiceTypeResponse> vsServiceTypeDetails = new ArrayList<>();
    private static NPGKskInquiryResponseItem resObjectItem;
    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();

    private static NPGKskInquiryResponse SLUvsInquiryResponse;
    private ArrayList<KskServiceItem> SLUServiceItems = new ArrayList<>();
    private static KskServiceTypeResponse SLUvsServiceTypeDetail;
    private static NPGKskInquiryResponseItem SLUresObjectItem;
    private ArrayList<KskServiceTypeResponse> SLUvsServiceTypeDetails = new ArrayList<>();
    private TextView tv_timer, tv_ownercertplateNo, tv_ownercertlicenseSource, tv_ownercertbirthYear,
            tv_ownercertplateCategory, tv_ownercertaddDept, tv_ownercertplateCode, tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    public RTACertificateOwnershipByPlateNo() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTACertificateOwnershipByPlateNo newInstance() {
        return new RTACertificateOwnershipByPlateNo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtacertificate_ownership_by_plate_no, container, false);

        edt_plateNo = (EditText) view.findViewById(R.id.edt_plateno);
        edt_birthYear = (EditText) view.findViewById(R.id.birthyear_plateno);
        sp_licensesourcePlateNo = (Spinner) view.findViewById(R.id.sp_licensesourceplateno);
        sp_platecode_PlateNo = (Spinner) view.findViewById(R.id.sp_platecode_plateno);
        sp_platecaegory_PalteNo = (Spinner) view.findViewById(R.id.sp_platecaegory_palteno);
        btn_Search_Plateno = (Button) view.findViewById(R.id.btnsearch_plateno);
        sp_vehicles = (Spinner) view.findViewById(R.id.sp_vehicles);
        btn_Back = (Button) view.findViewById(R.id.btn_rtaownershipback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtaownershipinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtaownershiphome);
        tv_ownercertplateNo = (TextView) view.findViewById(R.id.tv_ownercertplateno);
        tv_ownercertlicenseSource = (TextView) view.findViewById(R.id.tv_ownercertlicensesource);
        tv_ownercertbirthYear = (TextView) view.findViewById(R.id.tv_ownercertbirthyear);
        tv_ownercertplateCategory = (TextView) view.findViewById(R.id.tv_ownercertplatecategory);
        tv_ownercertaddDept = (TextView) view.findViewById(R.id.tv_ownercertadddept);
        tv_ownercertplateCode = (TextView) view.findViewById(R.id.tv_ownercertplatecode);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);


        btn_Back.setOnClickListener(this);
        // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //button animation callback method
        // this.setButtonAnimationMethodCallBack(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Ownership_Cert);

        try {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTACertificateServices.newInstance(), getFragmentManager());
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }


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
        edt_plateNo.addTextChangedListener(Common.getTextWatcher(cTimer, edt_birthYear, edt_plateNo, btn_Search_Plateno));
        edt_birthYear.addTextChangedListener(Common.getTextWatcher(cTimer, edt_birthYear, edt_plateNo, btn_Search_Plateno));

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
        btn_Search_Plateno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

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

        /*edt_plateNo.setText("65785");
        edt_birthYear.setText("1970");*/
       /* if (cTimer != null)
            cTimer.cancel();
        ServiceLayerVLDL.CallRTACertificatesLookupVehicle(new ServiceCallback() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (cTimer != null)
                    cTimer.start();
                Gson gson = new Gson();

                String reasonCode = null;
                try {
                    reasonCode = obj.getString("ReasonCode");
                    if (!(reasonCode.equals("0000"))) {
                        Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject SLUdataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                if (SLUdataObject != null) {

                    //Do things with object.
                    SLUvsServiceTypeDetail = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                    SLUresObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                    SLUvsInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);

                } else {
                    //Do things with array
                    SLUvsServiceTypeDetail = gson.fromJson(obj.toString(), KskServiceTypeResponse.class);
                    SLUvsInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                }

                // This is Array of Items
                if (SLUvsInquiryResponse != null) {

                    for (int i = 0; i < SLUvsInquiryResponse.ServiceType.getItems().size(); i++)
                        SLUServiceItems.add(SLUvsInquiryResponse.getServiceType().getItems().get(i));

                    Log.d("Service Response", SLUvsInquiryResponse.Message);
                } else {
                    SLUvsServiceTypeDetails.add(SLUvsServiceTypeDetail);
                    SLUServiceItems.add(SLUresObjectItem.getServiceType().getItems());
                }

                List<String> list = new ArrayList<String>();
                for (KskServiceItem kskServiceItem : SLUServiceItems) {
                    list.add(kskServiceItem.Field2);
                }

                map = new HashMap<>();
                for (KskServiceItem kskServiceItem : SLUServiceItems) {
                    map.put(kskServiceItem.Field3, kskServiceItem.Field2);
                }

                ArrayAdapter<String> adataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                adataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_vehicles.setAdapter(adataAdapter);
            }

            @Override
            public void onFailure(String obj) {
                if (cTimer != null)
                    cTimer.start();
                Log.d("Service Response", obj);
            }
        }, getContext());*/

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
                    Vehicle = sp_vehicles.getSelectedItem().toString();
                    ServiceId = ConfigurationVLDL.SERVICE_ID_VEHICLE;
                    ServiceName = ConfigurationVLDL.SNInquiryRTAVehicleDetailByPlateInfo;

                    if (Plateno.equals("") || Birthyear.equals("")) {
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_SHORT).show();
                    } else {
                        //animatorSet.cancel();
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
                                }

                                for (int i = 0; i <= ServiceItems.size() - 1; i++) {
                                    MaximumAmount += ServiceItems.get(i).getMaximumAmount();
                                    MinimumAmount += vsServiceTypeDetails.get(i).getMinimumAmount();
                                    DueAmount += vsServiceTypeDetails.get(i).getDueamount();
                                    PaidAmount += ServiceItems.get(i).getItemPaidAmount();
                                    ServiceCharge += vsServiceTypeDetails.get(i).getServiceCharge();
                                    DiscountRate += ServiceItems.get(i).getDiscountRate();
                                }
                                TotalAmount = ServiceCharge + PaidAmount + DiscountRate;
                                KskServiceItem[] items = ServiceItems.toArray(new KskServiceItem[ServiceItems.size()]);
                                try {
                                    writeObject(getContext(), Constant.FinesListSelectedItems, items);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                               /* for (Map.Entry<String, String> entry : map.entrySet()) {
                                    if (entry.getValue().equals(Vehicle)) {
                                        cauCode = entry.getKey();
                                        break;
                                    }
                                }*/

                                serviceId = ConfigurationVLDL.SIDCreateTransactionInquiry; //108
                                trfNo = TrafficFileNo;
                                setviceCode = ConfigurationVLDL.SETVICE_ID_Vehicle_Ownership_Certificate; //"214";
                                chassisNo = ServiceItems.get(0).getField6();
                                isMortgage = ServiceItems.get(0).getField1();
                                if (Language.equals(Constant.LanguageArabic))
                                    cauCode = setAddressedDeptKeyValueArabic(Vehicle);
                                else
                                    cauCode = setAddressedDeptKeyValue(Vehicle);

                                ServiceLayerVLDL.CallCreateTransactionInquiry(serviceId, trfNo, setviceCode, chassisNo, isMortgage, cauCode, MinimumAmount, MaximumAmount, DueAmount,
                                        PaidAmount, ServiceCharge, items, String.valueOf(TotalAmount), new ServiceCallbackPayment() {
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
                                                    } else {
                                                        try {
                                                            resObjectItem2 = gson.fromJson(obj.toString(), KioskPaymentResponseItem.class);
                                                            writeObject(getContext(), Constant.FIPCreateTransactionObject, resObjectItem2);
                                                        } catch (IOException e) {
                                                            if (cTimer != null)
                                                                cTimer.start();
                                                            e.printStackTrace();
                                                        }
                                                        //mFragment = FipTotalAmount.newInstance();
                                                        //addFragment();
                                                        if (cTimer != null)
                                                            cTimer.cancel();
                                                        mFragment = FipContactDetails.newInstance();
                                                        addFragment();
                                                    }
                                                } catch (JSONException e) {
                                                    if (cTimer != null)
                                                        cTimer.start();
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(String obj) {
                                                if (cTimer != null)
                                                    cTimer.start();
                                                Log.d("Service Response", obj);
                                            }
                                        }, getContext());


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

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Search_Plateno.setText(resources.getString(R.string.Search));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_ownercertplateNo.setText(resources.getString(R.string.PlateNumber));
        tv_ownercertlicenseSource.setText(resources.getString(R.string.LicenseSource));
        tv_ownercertbirthYear.setText(resources.getString(R.string.BirthYear));
        tv_ownercertplateCategory.setText(resources.getString(R.string.Platecategory));
        tv_ownercertaddDept.setText(resources.getString(R.string.AddressedDepartment));
        tv_ownercertplateCode.setText(resources.getString(R.string.Platecode));
        tvrtaServiceTitle.setText(resources.getString(R.string.CertificateTitleOwnership));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
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
                case R.id.btn_rtaownershipback:
                    mFragment = RTACertificateServices.newInstance();
                    addFragment();
                    break;
                /*case R.id.btn_rtaownershipinfo:
                    break;*/
                case R.id.btn_rtaownershiphome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }
}