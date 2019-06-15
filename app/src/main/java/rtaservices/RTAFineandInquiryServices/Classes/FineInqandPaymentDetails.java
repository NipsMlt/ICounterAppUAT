package rtaservices.RTAFineandInquiryServices.Classes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;

import example.dtc.R;
import interfaces.CheckBoxClickListener;
import interfaces.ResetTimerListener;
import rtamain.RTAMain;
import rtaservices.FipContactDetails;
import rtaservices.FipContactDetailsOnlyEmail;
import rtaservices.RTAFineandInquiryServices.Adapters.FineInqandPaymentDetailsAdapter;
import rtaservices.RTAFineandInquiryServices.Adapters.FineInqandPaymentListViewAdapter;
import rtaservices.RTAFineandInquiryServices.Interfaces.DrivingLicenseDetailsClickListener;
import rtaservices.RTAMainServices;
import rtaservices.RTAMainSubServices;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;

public class FineInqandPaymentDetails extends Fragment implements CheckBoxClickListener, DrivingLicenseDetailsClickListener, Serializable,
        View.OnClickListener, ResetTimerListener {

    Fragment mFragment;
    private ListView mListView;
    private FineInqandPaymentListViewAdapter mAdapter;
    private FineInqandPaymentDetailsAdapter adapter_menus;
    private String[] arrTicketno, arrDate;
    private double[] arrFineAmount, arrPenalty, arrKnowledgeFee, arrTotal;
    private TextView tv_fineCount, tv_selectedFine, tv_fineAmount, tv_selectedfineAmount, tv_blackpointsCount;
    public double TotalFineAmount, FineAmount, CachedFineAmount, SelectedFineAmount, TotalSelectedfineAmout, BlackPointsCount;
    private int ListCheckedItemsCount, FineCount, SelectedFine;
    private CheckBox cb_SelectAll;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private ArrayList<KskServiceItem> getSelectedListItems = new ArrayList<>();
    int count = 1, cachedcount;
    private String FA, TrafficFileno, FinesPage;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    boolean cb_selectall;
    private int listPosition, checkedCount, finecount;
    private Button btn_Fipdetailscontinue;
    // Response Final Object
    private static KioskPaymentResponse resObject;
    private static KioskPaymentResponseItem resObjectItem;
    private RecyclerView recyclerView, recyclerViewMenus;
    private Button btn_Back, btn_Info, btn_Home;
    AlertDialog.Builder builder;
    String ClassName;
    private ArrayList<KskServiceItem> createtransactionItemsList = new ArrayList<>();
    private TextView tv_timer, tv_ticketNo, tv_Date, tv_txtfineamountDetails,
            tv_Penalty, tv_knowledgeFee, tv_Total, tv_Details, tv_txtfineCount, tv_txtblackpointsCount,
            tv_txtfineAmount, tv_txtselectedFine, tv_txtselectedfineAmount, tvrtaServiceTitle, tv_Seconds, tv_unpayableTickets, tv_txtDetails;

    //Declare timer
    private static CountDownTimer cTimer = null;
    String ServicesName;
    private String Language;
    FIPDrivingLicenseDetailsDialogFragment fipDrivingLicenseDetailsDialogFragment = new FIPDrivingLicenseDetailsDialogFragment();
    private LinkedHashMap<String, String> hashMap = new LinkedHashMap();
    private MediaPlayer mPlayer;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static FineInqandPaymentDetails newInstance() {
        return new FineInqandPaymentDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.rtaserviesfineinquiryandpaymentdetails, null);
        try {
            //fipdetailsItemsList = bundle.getParcelableArrayList(Constant.FIPServiceTypeItemsArrayList);
            //TrafficFileno = bundle.getString(Constant.TrafficFileNo);
            //FinesPage = bundle.getString(Constant.FinesPage);
            fipdetailsItemsList = (ArrayList<KskServiceItem>) Common.readObject(getContext(), Constant.FIPServiceTypeItemsArrayList);
            TrafficFileno = PreferenceConnector.readString(getContext(), Constant.TrafficFileNo, null);
            FinesPage = PreferenceConnector.readString(getContext(), Constant.FinesPage, null);


        } catch (Exception e) {

        }
        mListView = (ListView) view.findViewById(R.id.rv_fineinquirydetails);
        //recyclerViewMenus = (RecyclerView) view.findViewById(R.id.rv_fineinquirydetails);
        cb_SelectAll = (CheckBox) view.findViewById(R.id.cb_fipselectall);
        tv_fineCount = (TextView) view.findViewById(R.id.tv_finecount);
        tv_fineAmount = (TextView) view.findViewById(R.id.tv_fineamount);
        tv_selectedfineAmount = (TextView) view.findViewById(R.id.tv_selectedfineamount);
        tv_selectedFine = (TextView) view.findViewById(R.id.tv_selectedfine);
        tv_blackpointsCount = (TextView) view.findViewById(R.id.tv_blackpointscount);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        tv_ticketNo = (TextView) view.findViewById(R.id.tv_ticketno);
        tv_Date = (TextView) view.findViewById(R.id.tv_date);
        tv_txtfineamountDetails = (TextView) view.findViewById(R.id.tv_txtfineamountdetails);
        tv_Penalty = (TextView) view.findViewById(R.id.tv_penalty);
        tv_knowledgeFee = (TextView) view.findViewById(R.id.tv_knowledgefee);
        tv_Total = (TextView) view.findViewById(R.id.tv_total);
        tv_Details = (TextView) view.findViewById(R.id.tv_details);
        tv_txtfineCount = (TextView) view.findViewById(R.id.tv_txtfinecount);
        tv_txtblackpointsCount = (TextView) view.findViewById(R.id.tv_txtblackpointscount);
        tv_txtfineAmount = (TextView) view.findViewById(R.id.tv_txtfineamount);
        tv_txtselectedFine = (TextView) view.findViewById(R.id.tv_txtselectedfine);
        tv_unpayableTickets = (TextView) view.findViewById(R.id.tv_unpayabletickets);
        tv_txtDetails = (TextView) view.findViewById(R.id.tv_txtdetails);
        btn_Fipdetailscontinue = (Button) view.findViewById(R.id.btn_fipdetailscontinue);
        tv_txtselectedfineAmount = (TextView) view.findViewById(R.id.tv_txtselectedfineamount);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        btn_Back = (Button) view.findViewById(R.id.btnrtafipback);
        btn_Info = (Button) view.findViewById(R.id.btnrtafipinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtafiphome);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Get Services Name to set the digital pass
        ServicesName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        btn_Back.setOnClickListener(this);
        // btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        builder = new AlertDialog.Builder(getContext());

        try {
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

        }

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, RTAMainSubServices.newInstance(), getFragmentManager());

        //Get total fines amount
        int size = fipdetailsItemsList.size();
        for (int i = 0; i < size; i++) {
            TotalFineAmount += fipdetailsItemsList.get(i).getItemPaidAmount() + fipdetailsItemsList.get(i).getServiceCharge();
            finecount++;
        }
        //Set fine amount
        tv_fineAmount.setText(String.valueOf(TotalFineAmount));
        tv_fineCount.setText(String.valueOf(finecount));

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
                        int size = fipdetailsItemsList.size();
                        for (int i = 0; i < size; i++) {
                            //if fines are paybale
                            if (fipdetailsItemsList.get(i).getField7().equals("2")) {
                                KskServiceItem dto = fipdetailsItemsList.get(i);
                                dto.setChecked(true);
                                //Get total fine amount and black points count
                                //TotalFineAmount += fipdetailsItemsList.get(i).getItemPaidAmount();
                                PreferenceConnector.writeString(getContext(), Constant.TotalFineAmount, String.valueOf(TotalFineAmount));
                                TotalSelectedfineAmout += fipdetailsItemsList.get(i).getItemPaidAmount() + fipdetailsItemsList.get(i).getServiceCharge() +
                                        fipdetailsItemsList.get(i).getDiscountRate();
                                CachedFineAmount = TotalSelectedfineAmout;
                                BlackPointsCount = fipdetailsItemsList.get(i).getPoints();
                                checkedCount++;
                                cachedcount = checkedCount;
                            } else {
                                //.setText(String.valueOf(" 0"));
                                tv_selectedFine.setText(String.valueOf(" 0"));
                            }
                        }

                        //TotalSelectedfineAmout = TotalFineAmount;
                        tv_selectedFine.setText(String.valueOf(checkedCount));
                        tv_selectedfineAmount.setText(String.valueOf(TotalSelectedfineAmout));
                        tv_blackpointsCount.setText(String.valueOf(BlackPointsCount));

                        //Set to notify Dataset is changed
                        mAdapter.notifyDataSetChanged();

                    } else {
                        //Uncheck all checkboxes in a listview
                        int size = fipdetailsItemsList.size();
                        for (int i = 0; i < size; i++) {
                            KskServiceItem dto = fipdetailsItemsList.get(i);
                            dto.setChecked(false);
                            TotalSelectedfineAmout = 0;
                            checkedCount = 0;
                        }

                        mAdapter.notifyDataSetChanged();

                        //TotalFineAmount = 0.0;
                        BlackPointsCount = 0.0;
                        //tv_fineCount.setText("0");
                        tv_selectedFine.setText("0");
                        //tv_fineAmount.setText("0.0");
                        tv_selectedfineAmount.setText("0.0");
                        tv_blackpointsCount.setText("0.0");
                        cb_selectall = false;
                        cachedcount = 0;
                        CachedFineAmount = 0;
                    }
                } catch (Exception e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }
            }
        });


       /* mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), position, Toast.LENGTH_LONG).show();
            }
        });*/

        btn_Fipdetailscontinue.setOnClickListener(new View.OnClickListener() {
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
                        Common.writeObject(getContext(), Constant.FinesListSelectedItems, items);

                        if (cTimer != null)
                            cTimer.cancel();
                        ServiceLayer.callCreateTransactionService(TrafficFileno, MinimumAmount, MaximumAmount, DueAmount,
                                PaidAmount, ServiceCharge, items, String.valueOf(TotalAmount), new ServiceCallbackPayment() {
                                    @Override
                                    public void onSuccess(JSONObject obj) throws JSONException {

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

                                                String data = obj.optJSONObject("OperationType").getJSONObject("ServicesType").getString("Items");
                                                Object json = new JSONTokener(data).nextValue();
                                                if (json instanceof JSONObject) {
                                                    //Get data in an object
                                                    resObject = gson.fromJson(obj.toString(), KioskPaymentResponse.class);
                                                    createtransactionItemsList.add(resObject.OperationType.getServicesType().getItems());
                                                    try {
                                                        Common.writeObject(getContext(), Constant.FIPCreateTransactionObjectList, createtransactionItemsList);
                                                        Common.writeObject(getContext(), Constant.FIPCreateTransactionObject, resObject);
                                                    } catch (IOException e) {
                                                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                    }
                                                } else if (json instanceof JSONArray) {
                                                    resObjectItem = gson.fromJson(obj.toString(), KioskPaymentResponseItem.class);
                                                    for (int i = 0; i < resObjectItem.OperationType.getServicesType().getItems().size(); i++)
                                                        createtransactionItemsList.add(resObjectItem.OperationType.getServicesType().getItems().get(i));
                                                    try {
                                                        Common.writeObject(getContext(), Constant.FIPCreateTransactionObject, resObjectItem);
                                                        Common.writeObject(getContext(), Constant.FIPCreateTransactionObjectList, createtransactionItemsList);
                                                    } catch (IOException e) {
                                                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                    }
                                                }
                                                //Cancel the timer
                                                if (cTimer != null)
                                                    cTimer.cancel();

                                                if (ServicesName.equals(ConfigrationRTA.TrafficFines)) {
                                                    mFragment = FipContactDetailsOnlyEmail.newInstance();
                                                    addFragment();
                                                } else {
                                                    mFragment = FipContactDetails.newInstance();
                                                    addFragment();
                                                }

                                            }
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

                    } catch (Exception e) {
                        if (cTimer != null)
                            cTimer.start();
                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                    }
                } else {
                    builder.setMessage("You did not select any fines")
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
        arrTicketno = new String[fipdetailsItemsList.size()];
        arrDate = new String[fipdetailsItemsList.size()];
        arrFineAmount = new double[fipdetailsItemsList.size()];
        arrPenalty = new double[fipdetailsItemsList.size()];
        arrKnowledgeFee = new double[fipdetailsItemsList.size()];
        arrTotal = new double[fipdetailsItemsList.size()];

        // specify an adapter (see also next example)
        for (int i = 0; i < fipdetailsItemsList.size(); i++) {
            arrTicketno[i] = fipdetailsItemsList.get(i).getItemText();           //Ticket Number=ItemText
            arrDate[i] = getProperServiceDate(fipdetailsItemsList.get(i).getServiceDate());           //Date=ServiceDate
            arrFineAmount[i] = fipdetailsItemsList.get(i).getItemPaidAmount();   //Fine Amount=ItemPaidAmount
            arrPenalty[i] = fipdetailsItemsList.get(i).getDiscountRate();        //Penalty =DiscountRate
            arrKnowledgeFee[i] = fipdetailsItemsList.get(i).getServiceCharge();  //Knowledge Fee =ServiceCharge
            arrTotal[i] = arrFineAmount[i] + arrPenalty[i] + arrKnowledgeFee[i]; //ServiceCharge + DiscountRate + ItemPaidAmount
        }
        mAdapter = new FineInqandPaymentListViewAdapter(getContext(), arrTicketno, arrDate, arrFineAmount, arrPenalty, arrKnowledgeFee,
                arrTotal, cb_SelectAll, fipdetailsItemsList, mListView, cTimer);
        mListView.setAdapter(mAdapter);

        //Setting method callback
        mAdapter.setCheckBoxMethodCallBack(this);
        mAdapter.setDrivingLicenseDetailsMethodCallBack(this);
        fipDrivingLicenseDetailsDialogFragment.setMethodCallBack(this);
        /*recyclerViewMenus.setHasFixedSize(true);
        recyclerViewMenus.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter_menus = new FineInqandPaymentDetailsAdapter(getContext(), arrTicketno, arrDate, arrFineAmount, arrPenalty, arrKnowledgeFee,
                arrTotal, cb_SelectAll, fipdetailsItemsList, recyclerView);
        adapter_menus.setCheckBoxMethodCallBack(this);
        adapter_menus.setDrivingLicenseDetailsMethodCallBack(this);
        recyclerViewMenus.setAdapter(adapter_menus);*/

        return view;
    }


    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    //Check box click
    @Override
    public void CheckBoxClickCallBackMethod(boolean checkboxSelect, int Position) {
        try {
            if (cTimer != null)
                cTimer.start();
            listPosition = Position;
            BlackPointsCount = fipdetailsItemsList.get(Position).getPoints();
            FineAmount = fipdetailsItemsList.get(Position).getItemPaidAmount() + fipdetailsItemsList.get(Position).getDiscountRate() +
                    fipdetailsItemsList.get(Position).getServiceCharge();
            PreferenceConnector.writeString(getContext(), Constant.FineAmount, String.valueOf(FineAmount));
            FA = PreferenceConnector.readString(getContext(), Constant.FineAmount, null);
            FineAmount = Double.parseDouble(FA);
            if (checkboxSelect) {
               /* if (count == 0)
                    count++;*/
                PreferenceConnector.writeInteger(getContext(), "count", count);
                count = PreferenceConnector.readInteger(getContext(), "count", 0);

                cachedcount = cachedcount + count;
                CachedFineAmount = CachedFineAmount + FineAmount;

                //tv_fineAmount.setText(String.valueOf(CachedFineAmount));
                //tv_fineCount.setText(String.valueOf(cachedcount));
                tv_selectedFine.setText(String.valueOf(cachedcount));
                tv_selectedfineAmount.setText(String.valueOf(CachedFineAmount));
                tv_blackpointsCount.setText(String.valueOf(BlackPointsCount));

                ListCheckedItemsCount = cachedcount;

            } else {
               /* if (TotalFineAmount != 0.0) {
                    CachedFineAmount = TotalFineAmount;
                    cachedcount = fipdetailsItemsList.size();
                    count = 1;
                }*/
                CachedFineAmount = CachedFineAmount - FineAmount;
                cachedcount = cachedcount - count;
                //tv_fineAmount.setText(String.valueOf(CachedFineAmount));
                //tv_fineCount.setText(String.valueOf(cachedcount));
                tv_selectedFine.setText(String.valueOf(cachedcount));
                tv_selectedfineAmount.setText(String.valueOf(CachedFineAmount));
                tv_blackpointsCount.setText(String.valueOf(BlackPointsCount));
                TotalFineAmount = 0.0;
            }
        } catch (Exception e) {

            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
    }

    //Details Image click
    @Override
    public void DrivingLicenseDetailsCallBackMethod(int position) {
        if (cTimer != null)
            cTimer.cancel();
        Bundle bundlepos = new Bundle();
        FIPDrivingLicenseDetailsDialogFragment fipDrivingLicenseDetailsDialogFragment = new FIPDrivingLicenseDetailsDialogFragment();
        bundlepos.putParcelableArrayList(Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
        bundlepos.putInt(Constant.FIPDetailsListPosition, position);
        fipDrivingLicenseDetailsDialogFragment.setArguments(bundlepos);
        fipDrivingLicenseDetailsDialogFragment.show(getFragmentManager(), "");
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

        btn_Fipdetailscontinue.setText(resources.getString(R.string.Continue));
        tv_ticketNo.setText(resources.getString(R.string.TicketNumber));
        tv_Date.setText(resources.getString(R.string.Date));
        tv_txtfineamountDetails.setText(resources.getString(R.string.FineAmount));
        tv_Penalty.setText(resources.getString(R.string.Penalty));
        tv_knowledgeFee.setText(resources.getString(R.string.KnowledgeFee));
        tv_Total.setText(resources.getString(R.string.Total));
        tv_Details.setText(resources.getString(R.string.Details));
        tv_txtfineCount.setText(resources.getString(R.string.FineCount));
        tv_txtblackpointsCount.setText(resources.getString(R.string.BlackPointsCount));
        tv_txtfineAmount.setText(resources.getString(R.string.fineAmount));
        tv_txtselectedFine.setText(resources.getString(R.string.SelectedFine));
        tv_txtDetails.setText(resources.getString(R.string.Details));
        tv_unpayableTickets.setText(resources.getString(R.string.UnPayableTickets));
        tv_txtselectedfineAmount.setText(resources.getString(R.string.SelectedFineAmount));
        tvrtaServiceTitle.setText(resources.getString(R.string.FineInquiryandPayment));
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
            case R.id.btnrtafipback:
                if (FinesPage.equals(Constant.TrafficFileNo)) {
                    mFragment = FinesByTrafficFileNo.newInstance();
                    addFragment();
                } else if (FinesPage.equals(Constant.DrivingLicenseNo)) {
                    mFragment = FinesByDrivingLicense.newInstance();
                    addFragment();
                } else if (FinesPage.equals(Constant.PlateNo)) {
                    mFragment = FinesByPlateNo.newInstance();
                    addFragment();
                } else if (FinesPage.equals(Constant.FineNo)) {
                    mFragment = FinesByFineNumber.newInstance();
                    addFragment();
                }
                break;
            case R.id.btnrtafipinfo:
                break;
            case R.id.btnrtafiphome:
                mFragment = RTAMainServices.newInstance();
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