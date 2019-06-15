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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import RTANetworking.GenericServiceCall.ServiceLayerRMS;
import RTANetworking.Interfaces.ButtonAnimationListener;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.ItemES;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKInquiryResponseES;
import RTANetworking.RequestAndResponse.NPGKInquiryResponseItemES;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItemRMS;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseRMS;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAMainServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.ButtonAnimation;

public class RMSEmployeeServices extends Fragment implements View.OnClickListener, ButtonAnimationListener {

    Fragment mFragment;
    private EditText edt_enteremployeeID, edt_amountoPay;
    private Spinner sp_selectServices;
    private Button btn_Search, btn_Back, btn_Info, btn_Home;
    private String EmployeeID, AmountToPay, RMSServiceFessID, Services;
    private double amounttoPay;
    private ArrayList<KskServiceItem> createSalesOrderList; //List of items from the response of createSalesOrder
    private ArrayList<KskServiceItem> getItemsList; //getting complete items from the list from the response of GetEmployeesServicesFees
    private ArrayList<String> getItemsListSelectServices = new ArrayList<>(); //getting items from the list Field2 and Field3 for english and arabic respectively
    String ClassName;
    private TextView tv_timer, tv_txt_enteremployeeID, tv_txt_selectServices, tv_txt_amountoPay, tvrtaServiceTitle, tv_Seconds;
    //Declare timer
    CountDownTimer cTimer = null;
    private String Language;
    private MediaPlayer mPlayer;
    private PrimeThread primeThread;
    static private ButtonAnimationListener buttonAnimationListener;
    private boolean animationcheck = false;
    private AnimatorSet animatorSet;

    // Response Final Object
    private static NPGKInquiryResponseES resObject;

    private static NPGKInquiryResponseItemES resObjectItem;
    private static NPGKInquiryResponseES resObjectgetemployeesserviceFees;

    private static NPGKInquiryResponseItemES resObjectItemgetemployeesserviceFees;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RMSEmployeeServices newInstance() {
        return new RMSEmployeeServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaservices_employeeservices, null);

        //Id initialization
        edt_enteremployeeID = (EditText) view.findViewById(R.id.edt_enteremployeeid);
        edt_amountoPay = (EditText) view.findViewById(R.id.edt_amountopay);
        sp_selectServices = (Spinner) view.findViewById(R.id.sp_selectservices);
        btn_Search = (Button) view.findViewById(R.id.btn_search_employee_services);
        btn_Back = (Button) view.findViewById(R.id.btn_rtaemployeeservices_back);
        btn_Info = (Button) view.findViewById(R.id.btn_rtaemployeeservices_info);
        btn_Home = (Button) view.findViewById(R.id.btn_rtaemployeeservices_home);
        tv_txt_enteremployeeID = (TextView) view.findViewById(R.id.tv_txt_enteremployeeid);
        tv_txt_selectServices = (TextView) view.findViewById(R.id.tv_txt_selectservices);
        tv_txt_amountoPay = (TextView) view.findViewById(R.id.tv_txt_amountopay);
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
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.RMS_EMPLOYEE_SERVICES);

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
        edt_enteremployeeID.setOnTouchListener(new View.OnTouchListener() {
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
        edt_amountoPay.setOnTouchListener(new View.OnTouchListener() {
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
        edt_enteremployeeID.addTextChangedListener(Common.getTextWatcher(cTimer, edt_enteremployeeID, edt_amountoPay, btn_Search));
        edt_amountoPay.addTextChangedListener(Common.getTextWatcher(cTimer, edt_enteremployeeID, edt_amountoPay, btn_Search));

        if (cTimer != null)
            cTimer.cancel();
        //Calling the service Get RMSEMployee Services Fees
        ServiceLayerRMS.callGetRMSEmployeesServiceFees(ConfigrationRTA.SERVICE_NAME_RMS_GETEMPLOYEESERVICEFEES, ConfigrationRTA.SERVICE_ID_GETEMPLOYEESERVICEFEES, new ServiceCallback() {
            @Override
            public void onSuccess(JSONObject obj) {
                //start the timer
                if (cTimer != null)
                    cTimer.start();
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

                    //reinitialize the list object to get the updated list
                    getItemsList = new ArrayList<>();

                    try {
                        // Check the object or Array Items
                        JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                        if (dataObject != null) {

                            //Do things with object.
                            resObjectItemgetemployeesserviceFees = gson.fromJson(obj.toString(), NPGKInquiryResponseItemES.class);

                        } else {

                            //Do things with array
                            resObjectgetemployeesserviceFees = gson.fromJson(obj.toString(), NPGKInquiryResponseES.class);
                        }

                        // This is Array of Items
                        if (resObjectgetemployeesserviceFees != null) {

                            for (int i = 0; i < resObjectgetemployeesserviceFees.getServiceType().getItems().size(); i++)
                                getItemsList.add(resObjectgetemployeesserviceFees.getServiceType().getItems().get(i));

                            Log.d("Service Response", resObjectgetemployeesserviceFees.getMessage());

                            // This is object of Items
                        } else {
                            getItemsList.add(resObjectItemgetemployeesserviceFees.getServiceType().getItems());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }

                  /* Fill the spinner with the items from the list
                For english and all others Field3 and for Arabic Field2*/
                try {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_item, getItemsforSelectedServices(getItemsList));
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_selectServices.setAdapter(adapter);

                    //Select the amount from the list from where
                    sp_selectServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            amounttoPay = getItemsList.get(position).getItemPaidAmount();
                            RMSServiceFessID = getItemsList.get(position).getItemText();
                            //get the selected items
                            Services = sp_selectServices.getSelectedItem().toString();
                            if (amounttoPay > 0) {
                                edt_amountoPay.setEnabled(false);
                                AmountToPay = String.valueOf(amounttoPay);
                                //set the text of  amount as per the services are selected
                                edt_amountoPay.setText(AmountToPay);
                            } else {
                                edt_amountoPay.setEnabled(true);
                                AmountToPay = String.valueOf(amounttoPay);
                                //set the text of  amount as per the services are selected
                                edt_amountoPay.setText("");
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(String obj) {
                if (cTimer != null)
                    cTimer.start();
                Log.d("Service Response", obj);
            }
        }, getContext());


        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the text from the edittext
                EmployeeID = edt_enteremployeeID.getText().toString();//00045888
                AmountToPay = edt_amountoPay.getText().toString();//00045888

                //Calling the service to log this services
                ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());
                serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_Inquiry_Service_desc);

                //reinitialize the list object to get the updated list
                createSalesOrderList = new ArrayList<>();

                if (EmployeeID.equals("") || AmountToPay.equals("")) {
                    if (cTimer != null)
                        cTimer.start();
                    Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        amounttoPay = Double.parseDouble(AmountToPay);
                        if (amounttoPay <= 0) {
                            Toast.makeText(getContext(), "Amount cannot be zero", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (cTimer != null)
                                cTimer.cancel();
                            ServiceLayerRMS.callInquiryByCreateSalesOrder(EmployeeID, RMSServiceFessID, AmountToPay, ConfigrationRTA.SERVICE_NAME_RMS_EMPLOYEE_SERVICES, ConfigrationRTA.SERVICE_ID_CREATESALESORDER, new ServiceCallback() {
                                @Override
                                public void onSuccess(JSONObject obj) {
                                    if (cTimer != null)
                                        cTimer.start();
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
                                            resObjectItem = gson.fromJson(obj.toString(), NPGKInquiryResponseItemES.class);

                                        } else {
                                            //Do things with array
                                            resObject = gson.fromJson(obj.toString(), NPGKInquiryResponseES.class);
                                        }

                                        // This is Array of Items
                                        if (resObject != null) {

                                            for (int i = 0; i < resObject.getServiceType().getItems().size(); i++)
                                                createSalesOrderList.add(resObject.getServiceType().getItems().get(i));
                                            Log.d("Service Response", resObject.getMessage());

                                            // This is object of Items
                                        } else {
                                            for (int i = 0; i <= resObjectItem.getServiceType().getItems().size(); i++)
                                                createSalesOrderList.add(resObjectItem.getServiceType().getItems());
                                        }


                                        //Writing it in the cache to get it anywhere
                                        try {
                                            Common.writeObject(getContext(), ConfigrationRTA.RMS_SERVICES_CREATESALESORDERLIST, createSalesOrderList);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //Forcefully cancelling the timer as this is behaving abnormally
                                        if (cTimer != null)
                                            cTimer.cancel();

                                        //Assign null value to both of the objects to prevent the data merging
                                        resObject = null;
                                        resObjectItem = null;

                                        PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMS_SERVICE_EMPLOYEEID_ES, EmployeeID);
                                        PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMS_SERVICE_SERVICES_ES, Services);
                                        PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMS_SERVICE_AMOUNTTOPAY_ES, AmountToPay);
                                        mFragment = PaymentConfirmationRMSEmployeeServices.newInstance();
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
                        }

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

    //Function that get the items from the GetEmployeesServiceFees List for english and arabic accordingly
    private ArrayList<String> getItemsforSelectedServices(ArrayList<KskServiceItem> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (Language.equals(Constant.LanguageArabic)) {
                Services = String.valueOf(getItemsListSelectServices.add(arrayList.get(i).Field2));
            } else
                Services = String.valueOf(getItemsListSelectServices.add(arrayList.get(i).Field3));
        }

        return getItemsListSelectServices;
    }

    //Button Animation call back method
    public void setButtonAnimationMethodCallBack(ButtonAnimationListener CallBack) {
        this.buttonAnimationListener = CallBack;
    }

    //Call back for button animation
    @Override
    public void ButtonAnimationCallBackMethod() {
        if (edt_enteremployeeID.getText().toString().equals("") && edt_amountoPay.getText().toString().equals(""))
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
        tv_txt_enteremployeeID.setText(resources.getString(R.string.EnterEmployeeID));
        tv_txt_amountoPay.setText(resources.getString(R.string.AmounttoPay));
        tv_txt_selectServices.setText(resources.getString(R.string.SelectServices));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.EmployeeService));
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
            //Cancel the timer
            if (cTimer != null)
                cTimer.cancel();
            //Cancel the voice played
           try {                 if (mPlayer != null) {                     mPlayer.stop();                     mPlayer.release();                 }             } catch (Exception e) {             }
            switch (v.getId()) {
                case R.id.btn_rtaemployeeservices_back:
                    mFragment = RMSServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtaemployeeservices_info:
                    break;
                case R.id.btn_rtaemployeeservices_home:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }

}