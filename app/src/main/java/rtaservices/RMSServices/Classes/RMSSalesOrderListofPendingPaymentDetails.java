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
import rtaservices.RMSServices.Adapters.RMSInvoiceNoDetailsListViewAdapter;
import rtaservices.RMSServices.Adapters.RMSSalesOrderDetailsListViewAdapter;
import rtaservices.RMSServices.Adapters.RMSSalesOrderListViewAdapter;
import rtaservices.RMSServices.Interfaces.RMSSalesInvoiceDetailsClickListener;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;
import static utility.Common.writeObject;

public class RMSSalesOrderListofPendingPaymentDetails extends Fragment implements RMSSalesInvoiceDetailsClickListener, Serializable,
        View.OnClickListener, ResetTimerListener, CheckBoxClickListener {

    Fragment mFragment;
    private ListView mListView;
    private RMSSalesOrderListViewAdapter mAdapter;
    private RMSSalesOrderDetailsListViewAdapter mAdapterRmsSalesOrderDetailsListViewAdapter;
    private RMSInvoiceNoDetailsListViewAdapter mAdapterRmsInvoiceNoDetailsListViewAdapter;
    private RMSSalesOrderDetailsDialogFragment rmsSalesOrderDetailsDialogFragment = new RMSSalesOrderDetailsDialogFragment();
    private RMSInvoiceDetailsDialogFragment rmsinvoiceDetailsDialogFragment = new RMSInvoiceDetailsDialogFragment();
    private String[] orderDate, StausCode;
    private double[] Amount, Details;
    private CheckBox cb_SelectAll;
    private int itemQuantity, checkedCount, cachedcount, count = 1;
    private double selectedAmount, SelectedAmountCachedInDouble;

    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private ArrayList<KskServiceItem> salesorderinvoiceDetailsList;
    private ArrayList<KskServiceItem> getSelectedListItems = new ArrayList<>();
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    // Response Final Object
    private static KioskPaymentResponse resObject;
    private static KioskPaymentResponseItem resObjectItem;
    private static NPGKskInquiryResponseRMS resObjectSalesOrderDetails;
    private static NPGKskInquiryResponseItemRMS resObjectItemSalesOrderDetails;
    private Button btn_Back, btn_Info, btn_Home, btn_salesorder_lisofpending_DetailsContinue;
    AlertDialog.Builder builder;
    String ClassName;
    private ArrayList<KskServiceItem> createtransactionItemsList = new ArrayList<>();
    private TextView tv_timer, tv_txt_salesorder_ItemQuantity, tv_salesorder_ItemQuantity, tv_txt_salesorder_Amountbelow,
            tv_salesorder_Amountbelow, tv_txt_salesorder_orderNo, tv_txt_salesorder_orderDate,
            tv_txt_salesorder_statusCode, tv_txt_salesorder_Amount, tv_txt_salesorder_Details, tv_txt_salesorder_Status,
            tvrtaServiceTitle, tv_Seconds, tv_unpayableTickets;

    //Declare timer
    private static CountDownTimer cTimer = null;
    String ServicesName, SalesOrderID, InvoiceNo, SalesOrderIDorInvoiceNo;
    private String Language, ServiceID, Fragment, ServiceName, RMSTransactionID, SelectedAmountCached, ItemQuantity;
    private MediaPlayer mPlayer;
    private PrimeThread primeThread;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
        ServiceName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
    }

    public static RMSSalesOrderListofPendingPaymentDetails newInstance() {
        return new RMSSalesOrderListofPendingPaymentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtaservies_rmssalesorder_listofpendingppaymentdetails, null);
        try {
            fipdetailsItemsList = (ArrayList<KskServiceItem>) Common.readObject(getContext(), Constant.FIPServiceTypeItemsArrayList);
            Fragment = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
            SalesOrderID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SalesOrderID, "");
            InvoiceNo = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMSInvoiceNo, "");

            //Service Id for Create Transation for RMS SalesOrder and InvoiceNo
            ServiceID = ConfigrationRTA.SERVICE_ID_RMS;


        } catch (Exception e) {

        }
        mListView = (ListView) view.findViewById(R.id.lv_rmssalesorder_listofpendingdetails);
        //recyclerViewMenus = (RecyclerView) view.findViewById(R.id.rv_fineinquirydetails);
        cb_SelectAll = (CheckBox) view.findViewById(R.id.cb_salesorder_selectall);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_txt_salesorder_ItemQuantity = (TextView) view.findViewById(R.id.tv_txt_salesorder_itemquantity);
        tv_salesorder_ItemQuantity = (TextView) view.findViewById(R.id.tv_salesorder_itemquantity);
        tv_txt_salesorder_Amountbelow = (TextView) view.findViewById(R.id.tv_txt_salesorder_amountbelow);
        tv_salesorder_Amountbelow = (TextView) view.findViewById(R.id.tv_salesorder_amountbelow);
        tv_txt_salesorder_orderNo = (TextView) view.findViewById(R.id.tv_txt_salesorder_orderno);
        tv_txt_salesorder_orderDate = (TextView) view.findViewById(R.id.tv_txt_salesorder_orderdate);
        tv_txt_salesorder_statusCode = (TextView) view.findViewById(R.id.tv_txt_salesorder_statuscode);
        tv_txt_salesorder_Amount = (TextView) view.findViewById(R.id.tv_txt_salesorder_amount);
        tv_txt_salesorder_Details = (TextView) view.findViewById(R.id.tv_txt_salesorder_details);
        tv_txt_salesorder_Status = (TextView) view.findViewById(R.id.tv_txt_salesorder_status);
        tv_unpayableTickets = (TextView) view.findViewById(R.id.tv_txt_lppunpayablepayments);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        btn_salesorder_lisofpending_DetailsContinue = (Button) view.findViewById(R.id.btn_salesorder_lisofpending_detailscontinue);
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
        builder = new AlertDialog.Builder(getContext());

        primeThread = new PrimeThread(10000, R.raw.selectfines);
        primeThread.start();


     /*   try {
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
        if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER)) {
            SalesOrderIDorInvoiceNo = SalesOrderID;
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSOrderNo.newInstance(), getFragmentManager());
        }
        if (ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {
            SalesOrderIDorInvoiceNo = InvoiceNo;
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, RMSInvoiceNo.newInstance(), getFragmentManager());
        }


        //If the amount is payable and unpayable
        for (int i = 0; i <= fipdetailsItemsList.size() - 1; i++) {
            if (fipdetailsItemsList.get(i).getPoints() == 1 || fipdetailsItemsList.get(i).getPoints() == 2) { //Amount is not payable
                btn_salesorder_lisofpending_DetailsContinue.setEnabled(false);
                btn_salesorder_lisofpending_DetailsContinue.setBackgroundResource(R.drawable.gradientlighgrey);
            } else if (fipdetailsItemsList.get(i).getPoints() == 0) {                                         //Amount is payable
                btn_salesorder_lisofpending_DetailsContinue.setEnabled(true);
                btn_salesorder_lisofpending_DetailsContinue.setBackgroundResource(R.drawable.gradientlighttodarkgrey);
            }
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
                                tv_salesorder_Amountbelow.setText(String.valueOf(" 0"));
                            }
                        }
                        //write the checked items in the list in the cache to show it in the payment confirmation screen(when all items are selected)
                        PreferenceConnector.writeString(getContext(), Constant.ITEMQUANTITY, String.valueOf(checkedCount));
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
                    //Setting the text of selected total amount and check itmes
                    tv_salesorder_ItemQuantity.setText(String.valueOf(checkedCount));
                    tv_salesorder_Amountbelow.setText(String.valueOf(selectedAmount));
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
                            MaximumAmount += getSelectedListItems.get(i).getMaximumAmount();
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
                        ServiceLayerRMS.CallCreateTransactionInquiry(ServiceID, SalesOrderIDorInvoiceNo, MinimumAmount, MaximumAmount, DueAmount,
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


                                                mFragment = FipContactDetailsOnlyEmail.newInstance();
                                                addFragment();

                                            }
                                            //Transaction Date is null from RTA Cretae Transaction Response
                                            PreferenceConnector.writeString(getContext(), ConfigrationRTA.TransactionDateCurrent, Common.getdateTimeforNull());
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
        orderDate = new String[fipdetailsItemsList.size()];
        StausCode = new String[fipdetailsItemsList.size()];
        Amount = new double[fipdetailsItemsList.size()];
        Details = new double[fipdetailsItemsList.size()];

        // specify an adapter (see also next example)
        for (int i = 0; i < fipdetailsItemsList.size(); i++) {
            StausCode[i] = fipdetailsItemsList.get(i).getField3();                              //Status Code
            orderDate[i] = getProperServiceDate(fipdetailsItemsList.get(i).getServiceDate());   //Date = Service Date
            Amount[i] = fipdetailsItemsList.get(i).getItemPaidAmount();                         //Amount=ItemPaidAmount
            Details[i] = fipdetailsItemsList.get(i).getPoints();                                //details =Details
        }

        mAdapter = new RMSSalesOrderListViewAdapter(getContext(), orderDate, StausCode, Details, Amount
                , cb_SelectAll, fipdetailsItemsList, mListView, cTimer);
        mListView.setAdapter(mAdapter);

        //Setting method callback tot show the details
        mAdapter.setRMSSalesInvoiceDetailsDetailsMethodCallBack(this);
        //Setting Callback method to reset timer
        rmsSalesOrderDetailsDialogFragment.setMethodCallBack(this);
        rmsinvoiceDetailsDialogFragment.setMethodCallBack(this);
        mAdapter.setCheckBoxMethodCallBack(this);// calling the call back method to perform on check boxes

        return view;
    }

    @Override
    public void CheckBoxClickCallBackMethod(boolean checkboxClick, int Position) {
        //When an individual check box is clicked
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

                tv_salesorder_ItemQuantity.setText(String.valueOf(cachedcount));
                tv_salesorder_Amountbelow.setText(String.valueOf(SelectedAmountCachedInDouble));

            } else {
                SelectedAmountCachedInDouble = SelectedAmountCachedInDouble - selectedAmount;
                cachedcount = cachedcount - count;

                tv_salesorder_ItemQuantity.setText(String.valueOf(cachedcount));
                tv_salesorder_Amountbelow.setText(String.valueOf(SelectedAmountCachedInDouble));
            }
            //write the checked items in the list in the cache to show it in the payment confirmation screen(when individual itmes are selected)
            PreferenceConnector.writeString(getContext(), Constant.ITEMQUANTITY, String.valueOf(cachedcount));
        } catch (Exception e) {

            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
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

        if (Fragment.equals(ConfigrationRTA.RMS_SALES_ORDER)) {
            //Write the sales order no and Invoice no to show the information in the details
            PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMS_SalesOrderID, fipdetailsItemsList.get(position).getField7());

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
                                resObjectItemSalesOrderDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                            } else {

                                //Do things with array
                                resObjectSalesOrderDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                            }

                            // This is Array of Items
                            if (resObjectSalesOrderDetails != null) {

                                for (int i = 0; i < resObjectSalesOrderDetails.ServiceType.getItems().size(); i++)
                                    salesorderinvoiceDetailsList.add(resObjectSalesOrderDetails.getServiceType().getItems().get(i));

                                Log.d("Service Response", resObjectSalesOrderDetails.Message);

                                // This is object of Items
                            } else {
                                salesorderinvoiceDetailsList.add(resObjectItemSalesOrderDetails.getServiceType().getItems());
                            }

                            //Write a list in a cache
                            try {
                                writeObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse, salesorderinvoiceDetailsList);
                            } catch (Exception e) {
                                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                            }

                            //Setting Adapter to the ListView
                            rmsSalesOrderDetailsDialogFragment.show(getFragmentManager(), "");


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

        } else if (Fragment.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {

            //Write the sales order no and Invoice no to show the information in the details
            PreferenceConnector.writeString(getContext(), ConfigrationRTA.RMSInvoiceNo, fipdetailsItemsList.get(position).getField7());

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
                                resObjectItemSalesOrderDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                            } else {

                                //Do things with array
                                resObjectSalesOrderDetails = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                            }

                            // This is Array of Items
                            if (resObjectSalesOrderDetails != null) {

                                for (int i = 0; i < resObjectSalesOrderDetails.ServiceType.getItems().size(); i++)
                                    salesorderinvoiceDetailsList.add(resObjectSalesOrderDetails.getServiceType().getItems().get(i));

                                Log.d("Service Response", resObjectSalesOrderDetails.Message);

                                // This is object of Items
                            } else {
                                salesorderinvoiceDetailsList.add(resObjectItemSalesOrderDetails.getServiceType().getItems());
                            }

                            //Write a list in a cache
                            try {
                                writeObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse, salesorderinvoiceDetailsList);
                            } catch (Exception e) {
                                if (cTimer != null)
                                    cTimer.start();
                                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                            }
                            //Cancel the timer
                            if (cTimer != null)
                                cTimer.cancel();

                            //Opens the dialog of Invoice Details
                            rmsinvoiceDetailsDialogFragment.show(getFragmentManager(), "");


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
        tv_txt_salesorder_ItemQuantity.setText(resources.getString(R.string.Itemquantity));
        tv_txt_salesorder_Amountbelow.setText(resources.getString(R.string.Amountwithcolon));
        tv_txt_salesorder_orderNo.setText(resources.getString(R.string.OrderNo));
        tv_txt_salesorder_orderDate.setText(resources.getString(R.string.OrderDate));
        tv_txt_salesorder_statusCode.setText(resources.getString(R.string.StatusCode));
        tv_txt_salesorder_Amount.setText(resources.getString(R.string.Amount));
        tv_txt_salesorder_Details.setText(resources.getString(R.string.Details));
        tv_txt_salesorder_Status.setText(resources.getString(R.string.Status));
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
                if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER))
                    mFragment = RMSOrderNo.newInstance();
                else if (ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE))
                    mFragment = RMSInvoiceNo.newInstance();
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