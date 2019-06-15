package rtaservices.RMSServices.Classes;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayerRMS;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItemRMS;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseRMS;

import example.dtc.R;
import interfaces.CheckBoxClickListener;
import interfaces.ResetTimerListener;
import rtamain.RTAMain;
import rtaservices.FipContactDetailsOnlyEmail;
import rtaservices.RMSServices.Adapters.RMSEmiratesIdListViewAdapter;
import rtaservices.RMSServices.Interfaces.RMSSalesInvoiceDetailsClickListener;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static utility.Common.writeObject;

public class RMSEmiratesIDListofPendingPaymentDetails extends Fragment implements RMSSalesInvoiceDetailsClickListener, Serializable,
        View.OnClickListener, ResetTimerListener, CheckBoxClickListener {

    Fragment mFragment;
    private ListView mListView;
    private RMSEmiratesIdListViewAdapter mAdapter;
    private String[] orderInvoiceDate, orderInvoiceNo, StausCode;
    private double[] Amount, Details;
    private CheckBox cb_SelectAll;

    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private ArrayList<KskServiceItem> getSelectedListItems = new ArrayList<>();
    RMSSalesOrderDetailsDialogFragment rmsSalesOrderDetailsDialogFragment = new RMSSalesOrderDetailsDialogFragment();
    RMSInvoiceDetailsDialogFragment rmsInvoiceDetailsDialogFragment = new RMSInvoiceDetailsDialogFragment();
    private ArrayList<KskServiceItem> salesorderinvoiceDetailsList;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    // Response Final Object
    private static KioskPaymentResponse resObject;
    private static NPGKskInquiryResponseRMS resObjectemiratesIDDetails;
    private static NPGKskInquiryResponseItemRMS resObjectItememiratesIDDetails;
    private static KioskPaymentResponseItem resObjectItem;
    private Button btn_Back, btn_Info, btn_Home, btn_salesorder_lisofpending_DetailsContinue;
    AlertDialog.Builder builder;
    String ClassName;
    private TextView tv_timer, tv_txt_emiratesid_ItemQuantity, tv_emiratesid_ItemQuantity, tv_txt_emiratesid_AmountBelow,
            tv_emiratesid_AmountBelow, tv_txt_emiratesid_orderinvoiceNo, tv_txt_emiratesid_orderonvoiceDate,
            tv_txt_emiratesid_statusCode, tv_txt_emiratesid_Amount, tv_txt_emiratesid_Status, tv_txt_emiratesid_Details,
            tvrtaServiceTitle, tv_Seconds, tv_unpayableTickets;
    private int itemQuantity, checkedCount, cachedcount, count = 1;
    private double selectedAmount, SelectedAmountCachedInDouble;
    //Declare timer
    private static CountDownTimer cTimer = null;
    String ServicesName, SalesOrderID, InvoiceNo, EmiratesID, TradeLicenseID;
    private String Language, ServiceID, Fragment, SelectedAmountCached, EmiratesIDorTradeLicenseID;
    private MediaPlayer mPlayer;
    private PrimeThread primeThread;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static RMSEmiratesIDListofPendingPaymentDetails newInstance() {
        return new RMSEmiratesIDListofPendingPaymentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtaservies_rmsemiratesid_listofpendingppaymentdetails, null);
        try {
            fipdetailsItemsList = (ArrayList<KskServiceItem>) Common.readObject(getContext(), Constant.FIPServiceTypeItemsArrayList);
            Fragment = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
            EmiratesID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMSEmiratesID, "");
            TradeLicenseID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMSTradeLicenseID, "");
            ServiceID = ConfigrationRTA.SERVICE_ID_RMS;

        } catch (Exception e) {

        }
        mListView = (ListView) view.findViewById(R.id.lv_rmsemiratesid_listofpendingdetails);
        cb_SelectAll = (CheckBox) view.findViewById(R.id.cb_emiratesid_selectall);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_txt_emiratesid_ItemQuantity = (TextView) view.findViewById(R.id.tv_txt_emiratesid_itemquantity);
        tv_emiratesid_ItemQuantity = (TextView) view.findViewById(R.id.tv_emiratesid_itemquantity);
        tv_txt_emiratesid_AmountBelow = (TextView) view.findViewById(R.id.tv_txt_emiratesid_amountbelow);
        tv_emiratesid_AmountBelow = (TextView) view.findViewById(R.id.tv_emiratesid_amountbelow);
        tv_txt_emiratesid_orderinvoiceNo = (TextView) view.findViewById(R.id.tv_txt_emiratesid_orderinvoiceno);
        tv_txt_emiratesid_orderonvoiceDate = (TextView) view.findViewById(R.id.tv_txt_emiratesid_orderonvoicedate);
        tv_txt_emiratesid_statusCode = (TextView) view.findViewById(R.id.tv_txt_emiratesid_statuscode);
        tv_txt_emiratesid_Amount = (TextView) view.findViewById(R.id.tv_txt_emiratesid_amount);
        tv_txt_emiratesid_Status = (TextView) view.findViewById(R.id.tv_txt_emiratesid_status);
        tv_txt_emiratesid_Details = (TextView) view.findViewById(R.id.tv_txt_emiratesid_details);
        tv_unpayableTickets = (TextView) view.findViewById(R.id.tv_txt_lppunpayablepayments);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        btn_salesorder_lisofpending_DetailsContinue = (Button) view.findViewById(R.id.btn_emiratesid_lisofpending_detailscontinue);
        btn_Back = (Button) view.findViewById(R.id.btnrta_rmssalesorder_lppback);
        btn_Info = (Button) view.findViewById(R.id.btnrta_rmssalesorder_lppinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrta_rmssalesorder_lpphome);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Get Services Name to set the digital pass
        ServicesName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        /*Reset Timer Listeners */
        /*Start*/
        rmsSalesOrderDetailsDialogFragment.setMethodCallBack(this);
        rmsInvoiceDetailsDialogFragment.setMethodCallBack(this);
        /*Reset Timer Listeners */
        /*Start*/
        builder = new AlertDialog.Builder(getContext());

        primeThread = new PrimeThread(10000, R.raw.selectfines);
        primeThread.start();


       /* try {
            if (Language.equals(Constant.LanguageEnglish))
                mPlayer = MediaPlayer.create(getContext(), R.raw.selectfines);
            else if (Language.equals(Constant.LanguageArabic))
                mPlayer = MediaPlayer.create(getContext(), R.raw.selectfinesarabic);
            else if (Language.equals(Constant.LanguageUrdu))
                mPlayer = MediaPlayer.create(getContext(), R.raw.selectfinesurdu);
            else if (Language.equals(Constant.LanguageChinese))
                mPlayer = MediaPlayer.create(getContext(), R.raw.selectfineschinese);
            else if (Language.equals(Constant.LanguageMalayalam))
                mPlayer = MediaPlayer.create(getContext(), R.raw.selectfinesmalayalam);
            mPlayer.start();
        } catch (Exception e) {

        }*/


        //Set SalesOrderID or Invoice No
        if (ServicesName.equals(ConfigrationRTA.RMS_EMIRATESID)) {
            EmiratesIDorTradeLicenseID = EmiratesID;
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSEmiratesID.newInstance(), getFragmentManager());
        } else if (ServicesName.equals(ConfigrationRTA.RMS_TRADE_LICENSE)) {
            EmiratesIDorTradeLicenseID = TradeLicenseID;
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSTradeLicense.newInstance(), getFragmentManager());
        }

        // Click this checkbox to select all listview items with checkbox checked.
        cb_SelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (cTimer != null)
                        cTimer.start();
                    if (isChecked) {
                        //check all checkboxes in a listview
                        checkedCount = 0;
                        selectedAmount = 0;
                        int size = fipdetailsItemsList.size();
                        for (int i = 0; i < size; i++) {
                            //if fines are paybale
                            if (fipdetailsItemsList.get(i).getPoints() == 0) {
                                KskServiceItem dto = fipdetailsItemsList.get(i);
                                dto.setChecked(true);
                                //Get total fine amount and black points count
                                //PreferenceConnector.writeString(getContext(), Constant.TotalFineAmount, String.valueOf(TotalFineAmount));
                                selectedAmount += fipdetailsItemsList.get(i).getItemPaidAmount() + fipdetailsItemsList.get(i).getServiceCharge();
                                SelectedAmountCachedInDouble = selectedAmount;
                                checkedCount++;
                                cachedcount = checkedCount;
                            } else {
                                tv_emiratesid_AmountBelow.setText(String.valueOf(" 0"));
                            }
                        }

                        //Set to notify Dataset is changed
                        mAdapter.notifyDataSetChanged();

                    } else {
                        //Uncheck all checkboxes in a listview
                        int size = fipdetailsItemsList.size();
                        for (int i = 0; i < size; i++) {
                            KskServiceItem dto = fipdetailsItemsList.get(i);
                            dto.setChecked(false);
                            selectedAmount = 0;
                            checkedCount = 0;
                            SelectedAmountCachedInDouble = 0;
                        }

                        mAdapter.notifyDataSetChanged();
                        cachedcount = 0;

                    }

                    //write the checked items in the list in the cache to show it in the payment confirmation screen(when all items are selected)
                    PreferenceConnector.writeString(getContext(), Constant.ITEMQUANTITY, String.valueOf(checkedCount));

                    //Setting the text of selected total amount and check itmes
                    tv_emiratesid_ItemQuantity.setText(String.valueOf(checkedCount));
                    tv_emiratesid_AmountBelow.setText(String.valueOf(selectedAmount));
                } catch (Exception e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }
            }
        });

        btn_salesorder_lisofpending_DetailsContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cTimer != null)
                    cTimer.cancel();
                //Calling the service to log this services
                ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());
                serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_CreateTransaction_Service_desc);
                if (getcheckeditemfromList().size() > 0) {
                    try {
                        for (int i = 0; i <= getcheckeditemfromList().size() - 1; i++) {
                            getSelectedListItems = getcheckeditemfromList();
                            MaximumAmount += getSelectedListItems.get(i).getItemPaidAmount();
                            MinimumAmount += getSelectedListItems.get(i).getMinimumAmount();
                            DueAmount += getSelectedListItems.get(i).getDueamount();
                            PaidAmount += getSelectedListItems.get(i).getItemPaidAmount();
                            ServiceCharge += getSelectedListItems.get(i).getServiceCharge();
                            DiscountRate += getSelectedListItems.get(i).getDiscountRate();
                        }
                        TotalAmount = ServiceCharge + PaidAmount + DiscountRate;
                        KskServiceItem[] items = getSelectedListItems.toArray(new KskServiceItem[getSelectedListItems.size()]);
                        writeObject(getContext(), Constant.FinesListSelectedItems, items);

                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayerRMS.CallCreateTransactionInquiry(ServiceID, EmiratesIDorTradeLicenseID, MinimumAmount, MaximumAmount, DueAmount,
                                PaidAmount, ServiceCharge, items, new ServiceCallbackPayment() {
                                    @Override
                                    public void onSuccess(JSONObject obj) throws JSONException {

                                        Gson gson = new Gson();

                                        String reasonCode = null;
                                        try {
                                            reasonCode = obj.getString("ReasonCode");
                                            if (!(reasonCode.equals("0000"))) {
                                                //Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();

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
                                                try {
                                                    //Get data in an object
                                                    resObject = gson.fromJson(obj.toString(), KioskPaymentResponse.class);
                                                    writeObject(getContext(), Constant.FIPCreateTransactionObject, resObject);
                                                } catch (IOException e) {
                                                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                }

                                                //Cancel the timer
                                                if (cTimer != null)
                                                    cTimer.cancel();

                                                mFragment = FipContactDetailsOnlyEmail.newInstance();
                                                addFragment();


                                            }
                                            //Transaction Date is null from RTA Cretae Transaction Response
                                            PreferenceConnector.writeString(getContext(), ConfigrationRTA.TransactionDateCurrent, Common.getdateTimeforNull());
                                        } catch (JSONException e) {
                                            if (cTimer != null)
                                                cTimer.start();
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

                        //Make the values 0
                        MaximumAmount = 0;
                        MinimumAmount = 0;
                        DueAmount = 0;
                        PaidAmount = 0;
                        ServiceCharge = 0;
                        DiscountRate = 0;

                    } catch (Exception e) {
                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                    }

                } else {
                    builder.setMessage("You did not select any item")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (cTimer != null)
                                        cTimer.start();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        //Initialization of arrays
        orderInvoiceDate = new String[fipdetailsItemsList.size()];
        orderInvoiceNo = new String[fipdetailsItemsList.size()];
        StausCode = new String[fipdetailsItemsList.size()];
        Amount = new double[fipdetailsItemsList.size()];
        Details = new double[fipdetailsItemsList.size()];

        // specify an adapter (see also next example)
        for (int i = 0; i < fipdetailsItemsList.size(); i++) {
            orderInvoiceNo[i] = fipdetailsItemsList.get(i).getType();
            StausCode[i] = fipdetailsItemsList.get(i).getField1();
            orderInvoiceDate[i] = fipdetailsItemsList.get(i).getField6(); //Date = Service Date
            Amount[i] = fipdetailsItemsList.get(i).getItemPaidAmount();   //Amount=ItemPaidAmount
            Details[i] = fipdetailsItemsList.get(i).getPoints();        //details =Details

        }

        mAdapter = new RMSEmiratesIdListViewAdapter(getContext(), orderInvoiceDate, orderInvoiceNo, StausCode, Details, Amount
                , cb_SelectAll, fipdetailsItemsList, mListView, cTimer);
        mListView.setAdapter(mAdapter);

        //Setting method callback
        mAdapter.setRMSSalesInvoiceDetailsCallBackMethod(this); // Calling call back method to close the dialog
        mAdapter.setCheckBoxMethodCallBack(this);// calling the call back method to perform on check boxes

        return view;
    }


    //Individual click on the check box
    @Override
    public void CheckBoxClickCallBackMethod(boolean checkboxClick, int Position) {
        try {
            if (cTimer != null)
                cTimer.start();
            selectedAmount = fipdetailsItemsList.get(Position).getItemPaidAmount() + fipdetailsItemsList.get(Position).getDiscountRate() +
                    fipdetailsItemsList.get(Position).getServiceCharge();

            if (checkboxClick) {
                PreferenceConnector.writeInteger(getContext(), "count", count);
                count = PreferenceConnector.readInteger(getContext(), "count", 0);
                PreferenceConnector.writeString(getContext(), Constant.SelectedAmount, String.valueOf(selectedAmount));
                SelectedAmountCached = PreferenceConnector.readString(getContext(), Constant.SelectedAmount, null);
                selectedAmount = Double.parseDouble(SelectedAmountCached);


                cachedcount = cachedcount + count;
                SelectedAmountCachedInDouble = SelectedAmountCachedInDouble + selectedAmount;

                tv_emiratesid_ItemQuantity.setText(String.valueOf(cachedcount));
                tv_emiratesid_AmountBelow.setText(String.valueOf(SelectedAmountCachedInDouble));


            } else {
                SelectedAmountCachedInDouble = SelectedAmountCachedInDouble - selectedAmount;
                cachedcount = cachedcount - count;

                tv_emiratesid_ItemQuantity.setText(String.valueOf(cachedcount));
                tv_emiratesid_AmountBelow.setText(String.valueOf(SelectedAmountCachedInDouble));
            }
        } catch (Exception e) {

            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
        //write the checked items in the list in the cache to show it in the payment confirmation screen(when all items are selected)
        PreferenceConnector.writeString(getContext(), Constant.ITEMQUANTITY, String.valueOf(cachedcount));
    }

    //Timer Work
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

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    //Details Image click
    @Override
    public void RMSSalesInvoiceDetailsCallBackMethod(int position, ListView listView) {

        //Initializing the list
        salesorderinvoiceDetailsList = new ArrayList<>();

        //Sales Order => Back Order
        if (StausCode[position].equals(ConfigrationRTA.RMS_EMIRATESID_BACKORDER)) {
            //Write the sales order no and Invoice no to show the information in the details
            PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMS_SalesOrderID, fipdetailsItemsList.get(position).getType());

            //Getting the sales order ID
            SalesOrderID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SalesOrderID, "");

            try {
                if (cTimer != null)
                    cTimer.cancel();
                ServiceLayerRMS.callInquiryBySalesOrderDetails(SalesOrderID, ConfigrationRTA.SERVICE_NAME_RMS_SALESORDERDETAILS, ConfigrationRTA.SERVICE_ID_SALESORDERDETAILS, new ServiceCallback() {
                    @Override
                    public void onSuccess(JSONObject obj) {
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
                                                if (cTimer != null)
                                                    cTimer.start();
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
                                resObjectItememiratesIDDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                            } else {

                                //Do things with array
                                resObjectemiratesIDDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                            }

                            // This is Array of Items
                            if (resObjectemiratesIDDetails != null) {

                                for (int i = 0; i < resObjectemiratesIDDetails.ServiceType.getItems().size(); i++)
                                    salesorderinvoiceDetailsList.add(resObjectemiratesIDDetails.getServiceType().getItems().get(i));

                                Log.d("Service Response", resObjectemiratesIDDetails.Message);

                                // This is object of Items
                            } else {
                                salesorderinvoiceDetailsList.add(resObjectItememiratesIDDetails.getServiceType().getItems());
                            }

                            //Write a list in a cache
                            try {
                                writeObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse, salesorderinvoiceDetailsList);
                                //Open the dialog on success response
                                rmsSalesOrderDetailsDialogFragment.show(getFragmentManager(), "");
                            } catch (Exception e) {
                                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                            }

                        } catch (JSONException e) {
                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                        }

                    }

                    @Override
                    public void onFailure(String obj) {
                        if (cTimer != null)
                            cTimer.start();
                    }
                }, getContext());

            } catch (Exception e) {
                if (cTimer != null)
                    cTimer.start();
                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
            }
            //Invoice No
        } else if (StausCode[position].equals(ConfigrationRTA.RMS_EMIRATESID_INVOICE)) {
            //Write the sales order no and Invoice no to show the information in the details
            PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMSInvoiceNo, fipdetailsItemsList.get(position).getItemText());

            //Getting the sales order ID
            InvoiceNo = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMSInvoiceNo, "");

            try {
                if (cTimer != null)
                    cTimer.cancel();
                ServiceLayerRMS.callInquiryByInvoiceNoDetails(InvoiceNo, ConfigrationRTA.SERVICE_NAME_RMS_INVOICENODETAILS, ConfigrationRTA.SERVICE_ID_INVOICENODETAILS, new ServiceCallback() {
                    @Override
                    public void onSuccess(JSONObject obj) {
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
                                                if (cTimer != null)
                                                    cTimer.start();
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
                                resObjectItememiratesIDDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                            } else {

                                //Do things with array
                                resObjectemiratesIDDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                            }

                            // This is Array of Items
                            if (resObjectemiratesIDDetails != null) {

                                for (int i = 0; i < resObjectemiratesIDDetails.ServiceType.getItems().size(); i++)
                                    salesorderinvoiceDetailsList.add(resObjectemiratesIDDetails.getServiceType().getItems().get(i));

                                Log.d("Service Response", resObjectemiratesIDDetails.Message);

                                // This is object of Items
                            } else {
                                salesorderinvoiceDetailsList.add(resObjectItememiratesIDDetails.getServiceType().getItems());
                            }

                            //Write a list in a cache
                            try {
                                writeObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse, salesorderinvoiceDetailsList);
                                //Opens a dialog of invoice details by gettinng it's details
                                rmsInvoiceDetailsDialogFragment.show(getFragmentManager(), "");

                            } catch (Exception e) {
                                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                            }

                        } catch (JSONException e) {
                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                        }

                    }

                    @Override
                    public void onFailure(String obj) {
                        if (cTimer != null)
                            cTimer.start();
                    }
                }, getContext());

            } catch (Exception e) {
                if (cTimer != null)
                    cTimer.start();
                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
            }

        }
    }

    //Get the checked items from the list
    public ArrayList<KskServiceItem> getcheckeditemfromList() {
        ArrayList<KskServiceItem> list = new ArrayList<>();
        for (int i = 0; i < fipdetailsItemsList.size(); i++) {
            if (fipdetailsItemsList.get(i).isChecked())
                list.add(fipdetailsItemsList.get(i));
        }
        return list;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        btn_salesorder_lisofpending_DetailsContinue.setText(resources.getString(R.string.Continue));
        tv_txt_emiratesid_ItemQuantity.setText(resources.getString(R.string.Itemquantity));
        tv_txt_emiratesid_AmountBelow.setText(resources.getString(R.string.Amountwithcolon));
        tv_txt_emiratesid_orderinvoiceNo.setText(resources.getString(R.string.OrderInvoiceNo));
        tv_txt_emiratesid_orderonvoiceDate.setText(resources.getString(R.string.OrderInvoiceDate));
        tv_txt_emiratesid_statusCode.setText(resources.getString(R.string.StatusCode));
        tv_txt_emiratesid_Amount.setText(resources.getString(R.string.Amount));
        tv_txt_emiratesid_Status.setText(resources.getString(R.string.Status));
        tv_txt_emiratesid_Details.setText(resources.getString(R.string.Details));
        tv_unpayableTickets.setText(resources.getString(R.string.UnPayableTickets));
        tvrtaServiceTitle.setText(resources.getString(R.string.ListofPendingPayment));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    @Override
    public void onClick(View v) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        if (cTimer != null)
            cTimer.cancel();
        switch (v.getId()) {
            case R.id.btnrta_rmssalesorder_lppback:
                if (ServicesName.equals(ConfigrationRTA.RMS_EMIRATESID))
                    mFragment = RMSEmiratesID.newInstance();
                else if (ServicesName.equals(ConfigrationRTA.RMS_TRADE_LICENSE))
                    mFragment = RMSTradeLicense.newInstance();
                addFragment();
                break;
            case R.id.btnrta_rmssalesorder_lppinfo:
                break;
            case R.id.btnrta_rmssalesorder_lpphome:
                mFragment = RTAMain.newInstance();
                addFragment();
                break;
        }
    }

    //When the dialog is closed the timer should restart
    @Override
    public void ResetTimerCallBackMethod() {
        if (cTimer != null)
            cTimer.start();
    }
}