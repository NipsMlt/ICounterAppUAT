package rtaservices.RTAVehicleServices.Classes;

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
import rtaservices.FipContactDetails;
import rtaservices.RTAMainServices;
import rtaservices.RTAVehicleServices.Adapters.VehicleRenewalDetailsViewAdapter;
import rtaservices.RTAVehicleServices.Interfaces.VehicleDetailsClickListener;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;
import static android.content.ContentValues.TAG;
import static utility.Common.writeObject;

public class RTAVehicleRenewalInquiryAndPaymentDetails extends Fragment implements View.OnClickListener, VehicleDetailsClickListener, ResetTimerListener {

    Fragment mFragment;
    private ListView mListView;
    private VehicleRenewalDetailsViewAdapter mAdapter;
    private String Plate_Number, Plate_Source, Plate_Category, Plate_Type, Expiry_Date, ReplacementReason, isLostCode, PlateCode;
    private String serviceId, trfNo, setviceCode, chassisNo, isMortgage, Title, FragmentName;
    private static KioskPaymentResponse resObject2;
    private static NPGKskInquiryResponseItem resObjectItem;
    private static KioskPaymentResponseItem resObjectItem2;
    private static String TransactionId, KioskStatus;

    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    private ArrayList<KskServiceTypeResponse> ServiceTypeDetails = new ArrayList<>();
    private ArrayList<KskServiceItem> getSelectedListItems = new ArrayList<>();
    private String TrafficFileno;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    private Button btn_Fipdetailscontinue;
    //private RecyclerView recyclerView, recyclerViewMenus;
    RTAVehicleServices_VehicleDetailsDialogFragment rtaVehicleServices_vehicleDetailsDialogFragment = new RTAVehicleServices_VehicleDetailsDialogFragment();
    private Button btn_Back, btn_Info, btn_Home;
    AlertDialog.Builder builder;
    private TextView tv_timer, tv_Seconds, tvrtaServiceTitle, tv_vehiclerenewaldetailsplateNo, tv_vehiclerenewaldetailsplateSource, tv_vehiclerenewaldetailsplateCategory, tv_vehiclerenewaldetailsplateType, tv_vehiclerenewaldetailsexpiryDate, tv_vehiclerenewalDetails, tv_txtvehiclerenewalDetails;
    //Declare timer
    private static CountDownTimer cTimer = null;
    private String Language;

    private LinkedHashMap<String, String> hashMap = new LinkedHashMap();

    public RTAVehicleRenewalInquiryAndPaymentDetails() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RTAVehicleRenewalInquiryAndPaymentDetails newInstance() {
        return new RTAVehicleRenewalInquiryAndPaymentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtavehicle_renewal_inquiry_and_payment_details, container, false);

        Bundle bundle = getArguments();
       /* ServiceItems = bundle.getParcelableArrayList(ConfigurationVLDL.ServiceItems);
        ServiceTypeDetails = bundle.getParcelableArrayList(ConfigurationVLDL.ServiceTypeDetails);
        CustomerUniqueNo = bundle.getParcelableArrayList(ConfigurationVLDL.CustomerUniqueNo);
        TrafficFileno = bundle.getString(Constant.TrafficFileNo);
        Title = bundle.getString("VehicleTitle");*/
        try {
            ServiceItems = (ArrayList<KskServiceItem>) Common.readObject(getContext(), ConfigurationVLDL.ServiceItems);
            ServiceTypeDetails = (ArrayList<KskServiceTypeResponse>) Common.readObject(getContext(), ConfigurationVLDL.ServiceTypeDetails);
            CustomerUniqueNo = (ArrayList<CKeyValuePair>) Common.readObject(getContext(), ConfigurationVLDL.CustomerUniqueNo);
            TrafficFileno = PreferenceConnector.readString(getContext(), Constant.TrafficFileNo, "");
            Title = PreferenceConnector.readString(getContext(), ConfigurationVLDL.VehicleTitle, "");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mListView = (ListView) view.findViewById(R.id.vehiclerenewalinquirydetails);

        btn_Fipdetailscontinue = (Button) view.findViewById(R.id.btn_fipdetailscontinue);
        btn_Back = (Button) view.findViewById(R.id.btn_rtavehicledetailsrenewalback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtavehicledetailsrenewalinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtavehicledetailsrenewalhome);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tv_title_rtavehicleservices);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_vehiclerenewaldetailsplateNo = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetailsplateno);
        tv_vehiclerenewaldetailsplateSource = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetailsplatesource);
        tv_vehiclerenewaldetailsplateCategory = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetailsplatecategory);
        tv_vehiclerenewaldetailsplateType = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetailsplatetype);
        tv_vehiclerenewaldetailsexpiryDate = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetailsexpirydate);
        tv_vehiclerenewalDetails = (TextView) view.findViewById(R.id.tv_vehiclerenewaldetails);
        tv_txtvehiclerenewalDetails = (TextView) view.findViewById(R.id.tv_txtvehiclerenewaldetails);

        btn_Back.setOnClickListener(this);
       // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //To Reset the timer when the dialog is closed
        rtaVehicleServices_vehicleDetailsDialogFragment.setMethodCallBack(this);
        builder = new AlertDialog.Builder(getContext());

        Plate_Number = CustomerUniqueNo.get(0).getValue();
        Plate_Source = CustomerUniqueNo.get(3).getValue();
        Plate_Category = CustomerUniqueNo.get(1).getValue();
        Plate_Type = CustomerUniqueNo.get(2).getValue();
        PlateCode = PreferenceConnector.readString(getContext(), Constant.PlateCode, null);
        try {
            /*SimpleDateFormat expiry_date = new SimpleDateFormat("dd/MM/yyyy");
            String date = ServiceItems.get(0).getIsSelected().toString();
            Expiry_Date = expiry_date.format(expiry_date.parse(date));*/
            Expiry_Date = getProperServiceDate(ServiceItems.get(0).getServiceDate().toString());
        } catch (Exception ex) {
        }

        mAdapter = new VehicleRenewalDetailsViewAdapter(getContext(), Plate_Number, Plate_Source, Plate_Category, Plate_Type, Expiry_Date,
                CustomerUniqueNo, mListView);
        mListView.setAdapter(mAdapter);
        mAdapter.setvehicleRenewalDetailsMethodCallBack(this);

        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Vehicle_InquiryandPaymentDetails);
        if (FragmentName.equals(ConfigrationRTA.Vehicle_Renewal)) {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAVehicleRenewalByPlateNo.newInstance(), getFragmentManager());
        } else if (FragmentName.equals(ConfigrationRTA.Vehicle_Damaged_Lost)) {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAVehicleLossDamageByPlateNo.newInstance(), getFragmentManager());
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
                                            Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_SHORT).show();
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


                                                        ServiceLayerVLDL.CallRTAReCertifyTransactionInquiry(ConfigurationVLDL.SIDRTAReCertifyTransactionInquiry, TransactionId, trfNo, ConfigurationVLDL.SETVICE_CODE_SIDRTAReCertifyTransactionInquiry, MinimumAmount, MaximumAmount, DueAmount,
                                                                PaidAmount, ServiceCharge, items, String.valueOf(TotalAmount), new ServiceCallback() {
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
                                                                                                        if (cTimer != null)
                                                                                                            cTimer.start();
                                                                                                    }
                                                                                                });
                                                                                        AlertDialog alert = builder.create();
                                                                                        alert.show();

                                                                                        return;
                                                                                    } else {
                                                                                        //Get data in an object
                                                                                        //resObject = gson.fromJson(obj.toString(), KioskPaymentResponse.class);
                                                                                        try {
                                                                                            writeObject(getContext(), Constant.FIPCreateTransactionObject, resObjectItem2);
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
    public void onClick(View v) {
        if (cTimer != null)
            cTimer.cancel();
        switch (v.getId()) {
            case R.id.btn_rtavehicledetailsrenewalback:
                mFragment = RTAVehicleServices.newInstance();
                addFragment();
                break;
          /*  case R.id.btn_rtavehicledetailsrenewalinfo:
                break;*/
            case R.id.btn_rtavehicledetailsrenewalhome:
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
        tv_vehiclerenewaldetailsplateNo.setText(resources.getString(R.string.Plateno));
        tv_vehiclerenewaldetailsplateSource.setText(resources.getString(R.string.PlateSource));
        tv_vehiclerenewaldetailsplateCategory.setText(resources.getString(R.string.Platecategory));
        tv_vehiclerenewaldetailsplateType.setText(resources.getString(R.string.Platetype));
        tv_vehiclerenewaldetailsexpiryDate.setText(resources.getString(R.string.Expirydate));
        tv_vehiclerenewalDetails.setText(resources.getString(R.string.Details));
        tv_txtvehiclerenewalDetails.setText(resources.getString(R.string.Details));
        //Setting Title
        if (Title.contains("Lost") || Title.contains("Damage"))
            tvrtaServiceTitle.setText(resources.getString(R.string.VehicleRegistrationLossDamage));
        else
            tvrtaServiceTitle.setText(resources.getString(R.string.VehicleRegistrationRenewal));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    @Override
    public void VehicleDetailsCallBackMethod(int Position) {
        if (cTimer != null)
            cTimer.cancel();
        Bundle bundlepos = new Bundle();
        RTAVehicleServices_VehicleDetailsDialogFragment rtaVehicleServices_vehicleDetailsDialogFragment = new RTAVehicleServices_VehicleDetailsDialogFragment();
        bundlepos.putParcelableArrayList(Constant.FIPServiceTypeItemsArrayList, ServiceItems);
        bundlepos.putInt(Constant.FIPDetailsListPosition, Position);
        rtaVehicleServices_vehicleDetailsDialogFragment.setArguments(bundlepos);
        rtaVehicleServices_vehicleDetailsDialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void ResetTimerCallBackMethod() {
        if (cTimer != null)
            cTimer.start();
    }
}