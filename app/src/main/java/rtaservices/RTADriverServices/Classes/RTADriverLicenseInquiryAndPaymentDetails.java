package rtaservices.RTADriverServices.Classes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.ConfigurationVLDL;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayerVLDL;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.KskServiceTypeResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import interfaces.ResetTimerListener;
import rtaservices.RTADriverServices.Adapters.DriverDetailsViewAdapter;
import rtaservices.FipContactDetails;
import rtaservices.RTAMainServices;
import rtaservices.RTAVehicleServices.Interfaces.VehicleDetailsClickListener;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;
import static android.content.ContentValues.TAG;
import static utility.Common.writeObject;

public class RTADriverLicenseInquiryAndPaymentDetails extends Fragment implements View.OnClickListener, VehicleDetailsClickListener, ResetTimerListener {

    Fragment mFragment;
    private ListView mListView;
    private DriverDetailsViewAdapter mAdapter;
    private String LicenseNo, Plate_Source, TicketNo, BirthYear, Expiry_Date, ReplacementReason, isLostCode, Title;
    String serviceId, trfNo, setviceCode, chassisNo, isMortgage;
    private static KioskPaymentResponse resObject2;
    private static NPGKskInquiryResponseItem resObjectItem;
    private static KioskPaymentResponseItem resObjectItem2;
    private static String TransactionId, KioskStatus;

    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    private ArrayList<KskServiceTypeResponse> ServiceTypeDetails = new ArrayList<>();
    private ArrayList<KskServiceItem> getSelectedListItems = new ArrayList<>();
    private String TrafficFileno, FragmentName;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    private Button btn_Fipdetailscontinue;
    //private RecyclerView recyclerView, recyclerViewMenus;
    DrivingLicenseDetailsDialogFragment drivingLicenseDetailsDialogFragment = new DrivingLicenseDetailsDialogFragment();
    private Button btn_Back, btn_Info, btn_Home;
    private TextView tv_title_rtadriverServices, tv_driverdetailslicenseNo, tv_driverdetailsexpiryDate,
            tv_driverdetailsPlateSource, tv_driverdetailsticketNo, tv_driverdetailsbirthYear, tv_driverDetails, tv_txtdriverDetails,
            tvrtaServiceTitle, tv_Seconds;
    AlertDialog.Builder builder;
    private TextView tv_timer;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;

    private LinkedHashMap<String, String> hashMap = new LinkedHashMap();

    public RTADriverLicenseInquiryAndPaymentDetails() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTADriverLicenseInquiryAndPaymentDetails newInstance() {
        return new RTADriverLicenseInquiryAndPaymentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtadriver_license_inquiry_and_payment_details, container, false);

        Bundle bundle = getArguments();
       /* ServiceItems = bundle.getParcelableArrayList(ConfigurationVLDL.ServiceItems);
        ServiceTypeDetails = bundle.getParcelableArrayList(ConfigurationVLDL.ServiceTypeDetails);
        CustomerUniqueNo = bundle.getParcelableArrayList(ConfigurationVLDL.CustomerUniqueNo);
        TrafficFileno = bundle.getString(Constant.TrafficFileNo);
        Title = bundle.getString(ConfigurationVLDL.DriverTitle);*/
        try {
            ServiceItems = (ArrayList<KskServiceItem>) Common.readObject(getContext(), ConfigurationVLDL.ServiceItems);
            ServiceTypeDetails = (ArrayList<KskServiceTypeResponse>) Common.readObject(getContext(), ConfigurationVLDL.ServiceTypeDetails);
            CustomerUniqueNo = (ArrayList<CKeyValuePair>) Common.readObject(getContext(), ConfigurationVLDL.CustomerUniqueNo);
            TrafficFileno = PreferenceConnector.readString(getContext(), Constant.TrafficFileNo, "");
            Title = PreferenceConnector.readString(getContext(), ConfigurationVLDL.DriverTitle, "");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mListView = (ListView) view.findViewById(R.id.vehiclerenewalinquirydetails);

        btn_Fipdetailscontinue = (Button) view.findViewById(R.id.btn_driverdetailscontinue);
        btn_Back = (Button) view.findViewById(R.id.btn_rtadrivingback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtadrivinginfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtadrivinghome);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_driverdetailslicenseNo = (TextView) view.findViewById(R.id.tv_driverdetailslicenseno);
        tv_driverdetailsexpiryDate = (TextView) view.findViewById(R.id.tv_driverdetailsexpirydate);
        tv_driverdetailsPlateSource = (TextView) view.findViewById(R.id.tv_driverdetailsplatesource);
        tv_driverdetailsticketNo = (TextView) view.findViewById(R.id.tv_driverdetailsticketno);
        tv_driverdetailsbirthYear = (TextView) view.findViewById(R.id.tv_driverdetailsbirthyear);
        tv_driverDetails = (TextView) view.findViewById(R.id.tv_driverdetails);
        tv_txtdriverDetails = (TextView) view.findViewById(R.id.tv_txtdriverdetails);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        //To Reset the timer when the dialog is closed
        drivingLicenseDetailsDialogFragment.setMethodCallBack(this);
        builder = new AlertDialog.Builder(getContext());

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        LicenseNo = CustomerUniqueNo.get(1).getValue();
        Expiry_Date = CustomerUniqueNo.get(4).getValue();
        Plate_Source = CustomerUniqueNo.get(0).getValue();
        TicketNo = CustomerUniqueNo.get(3).getValue();
        BirthYear = CustomerUniqueNo.get(2).getValue();

        mAdapter = new DriverDetailsViewAdapter(getContext(), LicenseNo, getProperServiceDate(Expiry_Date), Plate_Source, TicketNo, BirthYear,
                CustomerUniqueNo, mListView);
        mListView.setAdapter(mAdapter);
        mAdapter.setvehicleRenewalDetailsMethodCallBack(this);

        //Get Services Name to set the digital pass
        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Driver_InquiryandPaymentDetails);

        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
        if (FragmentName.equals(ConfigrationRTA.Driver_Renewal)) {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTADriverRenewalByLicenseNo.newInstance(), getFragmentManager());
        } else if (FragmentName.equals(ConfigrationRTA.Driving_Damaged_Lost)) {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTADriverLossDamageByLicenseNo.newInstance(), getFragmentManager());
        }


        btn_Fipdetailscontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cTimer != null)
                        cTimer.cancel();
                    for (int i = 0; i <= getitemfromList().size() - 1; i++) {
                        getSelectedListItems = getitemfromList();
                        MaximumAmount += getSelectedListItems.get(i).getMaximumAmount();//getSelectedListItems.get(i).getMaximumAmount();
                        MinimumAmount += ServiceTypeDetails.get(i).getMinimumAmount();
                        DueAmount += ServiceTypeDetails.get(i).getDueamount();
                        PaidAmount += getSelectedListItems.get(i).getItemPaidAmount();
                        ServiceCharge += ServiceTypeDetails.get(i).getServiceCharge();
                        DiscountRate += getSelectedListItems.get(i).getDiscountRate();
                    }
                    TotalAmount = ServiceCharge + PaidAmount + DiscountRate;
                    KskServiceItem[] items = getSelectedListItems.toArray(new KskServiceItem[getSelectedListItems.size()]);
                    try {
                        writeObject(getContext(), Constant.FinesListSelectedItems, items);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    serviceId = ConfigurationVLDL.SIDCreateTransactionInquiry; //108
                    trfNo = TrafficFileno;
                    //setviceCode = bundle.getString(ConfigurationVLDL.SetviceCode);
                    //ReplacementReason = bundle.getString(ConfigurationVLDL.IsLost);
                    setviceCode = PreferenceConnector.readString(getContext(), ConfigurationVLDL.SetviceCode, "");
                    ReplacementReason = PreferenceConnector.readString(getContext(), ConfigurationVLDL.IsLost, "");
                    chassisNo = ServiceItems.get(0).getField6();
                    isMortgage = ServiceItems.get(0).getField1();

                    //String isLostCode;
                    if (setviceCode.equals(ConfigurationVLDL.SETVICE_ID_VEHICLE_LOSS_DAMAGE) || setviceCode.equals(ConfigurationVLDL.SETVICE_ID_DRIVER_LOSS_DAMAGE)) {
                        if (ReplacementReason.equals(ConfigurationVLDL.ReplacementReasonLost)) {
                            isLostCode = ConfigurationVLDL.lostValue;
                        } else {
                            isLostCode = ConfigurationVLDL.damagedValue;
                        }
                    }

                    ServiceLayerVLDL.CallCreateTransactionInquiry(serviceId, trfNo, setviceCode, chassisNo, isMortgage, isLostCode, MinimumAmount, MaximumAmount, DueAmount,
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
                                            Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        if (cTimer != null)
                                            cTimer.start();
                                        e.printStackTrace();
                                    }

                                    //Do things with object.
                                    resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);
                                    TransactionId = resObjectItem.ServiceAttributesList.CKeyValuePair[0].getValue();


                                    ServiceLayerVLDL.CallRTAAvailableDeliveryMethod(ConfigurationVLDL.SIDRTAAvailableDeliveryMethod, TransactionId, new ServiceCallback() { //123
                                        @Override
                                        public void onSuccess(JSONObject obj) {

                                            Gson gson = new Gson();

                                            String reasonCode = null;
                                            try {
                                                reasonCode = obj.getString("ReasonCode");
                                                if (!(reasonCode.equals("0000"))) {
                                                    Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                    return;
                                                }
                                            } catch (JSONException e) {
                                                if (cTimer != null)
                                                    cTimer.start();
                                                e.printStackTrace();
                                            }

                                            resObjectItem2 = gson.fromJson(obj.toString(), KioskPaymentResponseItem.class);
                                            KioskStatus = resObjectItem2.ServiceAttributesList.CKeyValuePair[0].getValue();

                                            if (KioskStatus.equals("available")) {
                                                ServiceLayerVLDL.CallRTASetAvailableDeliveryInquiry(ConfigurationVLDL.SIDRTAsetAvailableDeliveryInquiry, TransactionId, ConfigurationVLDL.CENTERID, new ServiceCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject obj) {

                                                        Gson gson = new Gson();

                                                        String reasonCode = null;
                                                        try {
                                                            reasonCode = obj.getString("ReasonCode");
                                                            if (!(reasonCode.equals("0000"))) {
                                                                Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                                return;
                                                            }
                                                        } catch (JSONException e) {
                                                            if (cTimer != null)
                                                                cTimer.start();
                                                            e.printStackTrace();
                                                        }


                                                        ServiceLayerVLDL.CallRTAReCertifyTransactionInquiry(ConfigurationVLDL.SIDRTAReCertifyTransactionInquiry, TransactionId, trfNo, ConfigurationVLDL.SETVICE_CODE_SIDRTAReCertifyTransactionInquiry, MinimumAmount, MaximumAmount, DueAmount,
                                                                PaidAmount, ServiceCharge, items, String.valueOf(TotalAmount), new ServiceCallback() {
                                                                    @Override
                                                                    public void onSuccess(JSONObject obj) {

                                                                        Gson gson = new Gson();

                                                                        String reasonCode = null;
                                                                        try {
                                                                            reasonCode = obj.getString("ReasonCode");
                                                                            if (!(reasonCode.equals("0000"))) {
                                                                                Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                                                return;
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            if (cTimer != null)
                                                                                cTimer.start();
                                                                            e.printStackTrace();
                                                                        }
                                                                        //resObject2 = gson.fromJson(obj.toString(), KioskPaymentResponse.class);
                                                                        resObjectItem2 = gson.fromJson(obj.toString(), KioskPaymentResponseItem.class);
                                                                        TotalAmount = Double.valueOf(resObjectItem.ServiceAttributesList.CKeyValuePair[2].getValue());
                                                                        //Transaction is null from RTARecertify response
                                                                        PreferenceConnector.writeString(getContext(), ConfigrationRTA.TransactionDateCurrent, Common.getdateTimeforNull());

                                                                        ServiceLayerVLDL.CallInquiryToServicesCharges(ConfigurationVLDL.SIDInquiryToServicesCharges, TotalAmount, setviceCode, TransactionId, new ServiceCallback() {
                                                                            @Override
                                                                            public void onSuccess(JSONObject obj) {

                                                                                Gson gson = new Gson();

                                                                                String reasonCode = null;
                                                                                try {
                                                                                    reasonCode = obj.getString("ReasonCode");
                                                                                    if (!(reasonCode.equals("0000"))) {
                                                                                        //Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                                                        if (cTimer != null)
                                                                                            cTimer.start();
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
                                                                                    } else {
                                                                                        //Get data in an object
                                                                                        //resObject = gson.fromJson(obj.toString(), KioskPaymentResponse.class);
                                                                                        try { writeObject(getContext(), Constant.FIPCreateTransactionObject, resObjectItem2);
                                                                                        } catch (IOException e) {
                                                                                            if (cTimer != null)
                                                                                                cTimer.start();
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                        //mFragment = FipTotalAmount.newInstance();
                                                                                        //addFragment();

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

                                                                    }

                                                                    @Override
                                                                    public void onFailure(String obj) {
                                                                        if (cTimer != null)
                                                                            cTimer.start();
                                                                        Log.d("Service Response", obj);
                                                                    }
                                                                }, getContext());

                                                    }

                                                    @Override
                                                    public void onFailure(String obj) {
                                                        if (cTimer != null)
                                                            cTimer.start();
                                                        Log.d("Service Response", obj);
                                                    }
                                                }, getContext());
                                            } else {

                                                return;
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

                                @Override
                                public void onFailure(String obj) {
                                    if (cTimer != null)
                                        cTimer.start();
                                    Log.d("Service Response", obj);
                                }
                            }, getContext());

                } catch (Exception ex) {
                    if (cTimer != null)
                        cTimer.start();
                    Log.d(TAG, ex.getMessage());
                }
            }
        });

        return view;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    public ArrayList<KskServiceItem> getitemfromList() {
        ArrayList<KskServiceItem> list = new ArrayList<>();
        for (int i = 0; i < ServiceItems.size(); i++) {
            list.add(ServiceItems.get(i));
        }
        return list;
    }

    @Override
    public void onClick(View view) {
        if (cTimer != null)
            cTimer.cancel();
        switch (view.getId()) {
            case R.id.btn_rtadrivingback:
                mFragment = RTADriverServices.newInstance();
                addFragment();
                break;
            /*case R.id.btn_rtadrivinginfo:
                break;*/
            case R.id.btn_rtadrivinghome:
                mFragment = RTAMainServices.newInstance();
                addFragment();
                break;
        }
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_Fipdetailscontinue.setText(resources.getString(R.string.Continue));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_driverdetailslicenseNo.setText(resources.getString(R.string.LicenseNumber));
        tv_driverdetailsexpiryDate.setText(resources.getString(R.string.ExpiryDate));
        tv_driverdetailsPlateSource.setText(resources.getString(R.string.PlateSource));
        tv_driverdetailsticketNo.setText(resources.getString(R.string.TicketNumber));
        tv_driverdetailsbirthYear.setText(resources.getString(R.string.BirthYear));
        tv_driverDetails.setText(resources.getString(R.string.Details));
        tv_txtdriverDetails.setText(resources.getString(R.string.Details));
        tv_Seconds.setText(resources.getString(R.string.seconds));
        //Setting Title
        if (Title.contains("Lost") || Title.contains("Damage"))
            tvrtaServiceTitle.setText(resources.getString(R.string.DrivingLicenseLossDamage));
        else
            tvrtaServiceTitle.setText(resources.getString(R.string.DrivingLicenseRenewal));
    }

    @Override
    public void VehicleDetailsCallBackMethod(int Position) {
        if (cTimer != null)
            cTimer.cancel();
        Bundle bundlepos = new Bundle();
        bundlepos.putParcelableArrayList(Constant.FIPServiceTypeItemsArrayList, ServiceItems);
        bundlepos.putInt(Constant.FIPDetailsListPosition, Position);
        drivingLicenseDetailsDialogFragment.setArguments(bundlepos);
        drivingLicenseDetailsDialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void ResetTimerCallBackMethod() {
        if (cTimer != null)
            cTimer.start();
    }
}