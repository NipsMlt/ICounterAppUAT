package rtaservices.RTAFineandInquiryServices.Classes;

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
import RTANetworking.Common.LocaleHelper;
import RTANetworking.Common.Utilities;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ButtonAnimationListener;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAMainSubServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.ButtonAnimation;

public class FinesByPlateNo extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Search_Plateno;
    private EditText edt_plateNo, edt_birthYear;
    private Spinner sp_licensesourcePlateNo, sp_platecode_PlateNo, sp_platecaegory_PalteNo;
    private String Plateno, Birthyear, PlateSource, PlateCode, PlateCategory, TrafficFileNo;
    // Response Final Object
    private static NPGKskInquiryResponse resObject;
    private static NPGKskInquiryResponseItem resObjectItem;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    String ClassName;
    private TextView tv_timer, tv_plateNo, tv_licensesourceplateNo, tv_birthyear_plateNo, tv_platecaegory_palteNo, tv_platecode_plateNo,
            tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static FinesByPlateNo newInstance() {
        return new FinesByPlateNo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaserviesfinesbyplateno, null);

        edt_plateNo = (EditText) view.findViewById(R.id.edt_plateno);
        edt_birthYear = (EditText) view.findViewById(R.id.birthyear_plateno);
        sp_licensesourcePlateNo = (Spinner) view.findViewById(R.id.sp_licensesourceplateno);
        sp_platecode_PlateNo = (Spinner) view.findViewById(R.id.sp_platecode_plateno);
        sp_platecaegory_PalteNo = (Spinner) view.findViewById(R.id.sp_platecaegory_palteno);
        btn_Search_Plateno = (Button) view.findViewById(R.id.btnsearch_plateno);
        btn_Back = (Button) view.findViewById(R.id.btn_rtaplatenoback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtaplatenoinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtaplatenohome);
        tv_plateNo = (TextView) view.findViewById(R.id.tv_plateno);
        tv_licensesourceplateNo = (TextView) view.findViewById(R.id.tv_licensesourceplateno);
        tv_birthyear_plateNo = (TextView) view.findViewById(R.id.tv_birthyear_plateno);
        tv_platecaegory_palteNo = (TextView) view.findViewById(R.id.tv_platecaegory_palteno);
        tv_platecode_plateNo = (TextView) view.findViewById(R.id.tv_platecode_plateno);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Back.setOnClickListener(this);
        // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //button animation callback method
        //this.setButtonAnimationMethodCallBack(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, RTAMainSubServices.newInstance(), getFragmentManager());

        //when the view is touched
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

        edt_plateNo.setText("");
        edt_birthYear.setText("");

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Write the class name in a cache to use it further
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.TrafficFines);

        btn_Search_Plateno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cTimer != null)
                    cTimer.cancel();
                Utilities utilities = new Utilities();
                PlateSource = sp_licensesourcePlateNo.getSelectedItem().toString();
                PlateCode = sp_platecode_PlateNo.getSelectedItem().toString();
                PlateCategory = sp_platecaegory_PalteNo.getSelectedItem().toString();
                Plateno = edt_plateNo.getText().toString();
                Birthyear = edt_birthYear.getText().toString();

                //Calling the service to log this services
                ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());
                serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_Inquiry_Service_desc);

                if (Birthyear.equals("")) {
                    if (cTimer != null)
                        cTimer.start();
                    Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayer.callInquiryByPlateNo(ConfigrationRTA.SERVICE_ID_PLATE, ConfigrationRTA.SERVICE_NAME_PLATE, Plateno, PlateCode, PlateCategory, PlateSource, Birthyear, new ServiceCallback() {
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
                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                }

                                // Check the object or Array Items
                                JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                if (dataObject != null) {

                                    //Do things with object.
                                    resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                                    TrafficFileNo = resObjectItem.ServiceAttributesList.CKeyValuePair[1].getValue();

                                } else {

                                    //Do things with array
                                    resObject = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                    TrafficFileNo = resObject.ServiceAttributesList.CKeyValuePair[1].getValue();
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
                                //If the list is filled set null to the objects
                                resObject = null;
                                resObjectItem = null;

                                if (cTimer != null)
                                    cTimer.cancel();
                                Bundle bundlepos = new Bundle();
                                mFragment = FineInqandPaymentDetails.newInstance();
                                /*bundlepos.putParcelableArrayList(Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                                bundlepos.putString(Constant.TrafficFileNo, TrafficFileNo);
                                bundlepos.putString(Constant.FinesPage, Constant.PlateNo);*/
                                //Writing it in the cache to get it anywhere
                                try {
                                    Common.writeObject(getContext(), Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                                    PreferenceConnector.writeString(getContext(), Constant.TrafficFileNo, TrafficFileNo);
                                    PreferenceConnector.writeString(getContext(), Constant.FinesPage, Constant.PlateNo);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                mFragment.setArguments(bundlepos);
                                addFragment();

                                Log.d("Service Response", obj.toString());

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
        tv_plateNo.setText(resources.getString(R.string.PlateNumber));
        tv_licensesourceplateNo.setText(resources.getString(R.string.LicenseSource));
        tv_birthyear_plateNo.setText(resources.getString(R.string.BirthYear));
        tv_platecaegory_palteNo.setText(resources.getString(R.string.Platecategory));
        tv_platecode_plateNo.setText(resources.getString(R.string.Platecode));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.FinesByPlateNo));
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
                case R.id.btn_rtaplatenoback:
                    mFragment = RTAMainSubServices.newInstance();
                    addFragment();
                    break;
               /* case R.id.btn_rtaplatenoinfo:
                    break;*/
                case R.id.btn_rtaplatenohome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }
}