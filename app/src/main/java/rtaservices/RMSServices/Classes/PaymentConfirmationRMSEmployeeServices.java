package rtaservices.RMSServices.Classes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayerRMS;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.CardPaymentSwipe;
import rtaservices.FipContactDetails;
import rtaservices.FipContactDetailsOnlyEmail;
import rtaservices.FipTotalAmount;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.ServiceCallLog.Classes.ServiceCallLogService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class PaymentConfirmationRMSEmployeeServices extends Fragment implements View.OnClickListener {

    Fragment mFragment;
    Button btn_Proceedtopayment, btn_Back, btn_Info, btn_Home;
    TextView tvrtaServiceTitle, tv_txt_rmsemployeeid_ES, tv_txt_rmsservice_ES,
            tv_txt_rmsamounttopay_ES, tv_rmsemployeeid_ES,
            tv_rmsservice_ES, tv_rmsamounttopay_ES;
    private String EmployeeID, AmountToPay, Services;
    private TextView tv_timer, tv_Seconds;
    CountDownTimer cTimer;
    String ClassName, ServiceID;
    double TotalAmount, MaximumAmount, MinimumAmount, DueAmount, PaidAmount, ServiceCharge, DiscountRate;
    private ArrayList<KskServiceItem> createSalesOrderList;
    private static KioskPaymentResponse resObject;
    private static KioskPaymentResponseItem resObjectItem;
    private String Language, FragmentName, ServicesName;
    private MediaPlayer mPlayer;
    AlertDialog.Builder builder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
        builder = new AlertDialog.Builder(context);
    }

    public static PaymentConfirmationRMSEmployeeServices newInstance() {
        return new PaymentConfirmationRMSEmployeeServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.rtaservicespaymentconfirmationrmsemployeeservices, null);
        tv_txt_rmsemployeeid_ES = (TextView) view.findViewById(R.id.tv_txt_rmsemployeeid_es);
        tv_txt_rmsservice_ES = (TextView) view.findViewById(R.id.tv_txt_rmsservice_es);
        tv_txt_rmsamounttopay_ES = (TextView) view.findViewById(R.id.tv_txt_rmsamounttopay_es);
        tv_rmsemployeeid_ES = (TextView) view.findViewById(R.id.tv_rmsemployeeid_es);
        tv_rmsservice_ES = (TextView) view.findViewById(R.id.tv_rmsservice_es);
        tv_rmsamounttopay_ES = (TextView) view.findViewById(R.id.tv_rmsamounttopay_es);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Proceedtopayment = (Button) view.findViewById(R.id.btn_proceedtopayment_es);
        btn_Back = (Button) view.findViewById(R.id.btnrtapcback);
        btn_Info = (Button) view.findViewById(R.id.btnrtapcinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtapchome);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        btn_Proceedtopayment.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Fragment Name
        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        //Get Services Name to set the digital pass
        ServicesName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                tv_timer, RMSEmployeeServices.newInstance(), getFragmentManager());
        //get Class Name
        ClassName = getClass().getCanonicalName();


        if (Language.equals(Constant.LanguageEnglish))
            mPlayer = MediaPlayer.create(getContext(), R.raw.proceedtomakepayment);
        else if (Language.equals(Constant.LanguageArabic))
            mPlayer = MediaPlayer.create(getContext(), R.raw.proceedtomakepaymentarabic);
        else if (Language.equals(Constant.LanguageUrdu))
            mPlayer = MediaPlayer.create(getContext(), R.raw.proceedtomakepaymenturdu);
        else if (Language.equals(Constant.LanguageChinese))
            mPlayer = MediaPlayer.create(getContext(), R.raw.proceedtomakepaymentchinese);
        else if (Language.equals(Constant.LanguageMalayalam))
            mPlayer = MediaPlayer.create(getContext(), R.raw.proceedtomakepaymentmalayalam);
        mPlayer.start();

        //Getting values to be for EmployeeID,Services and Amount to pay
        EmployeeID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SERVICE_EMPLOYEEID_ES, "");
        Services = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SERVICE_SERVICES_ES, "");
        AmountToPay = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SERVICE_AMOUNTTOPAY_ES, "");

        //Service Id for Create Transation for RMS SalesOrder and InvoiceNo
        ServiceID = ConfigrationRTA.SERVICE_ID_RMS;

        //Show the values in the textview
        tv_rmsemployeeid_ES.setText(EmployeeID);
        tv_rmsservice_ES.setText(Services);
        tv_rmsamounttopay_ES.setText(AmountToPay);

        //Initializing the list
        createSalesOrderList = new ArrayList<>();

        //Wr it in the cache to get it anywhere
        try {
            createSalesOrderList = (ArrayList<KskServiceItem>) Common.readObject(getContext(), ConfigrationRTA.RMS_SERVICES_CREATESALESORDERLIST);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        btn_Proceedtopayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cancer the timers
                if (cTimer != null)
                    cTimer.cancel();
                //Calling the service to log this services
                ServiceCallLogService serviceCallLogService = new ServiceCallLogService(getContext());
                serviceCallLogService.CallServiceCallLogService(Constant.nameRTAServices, Constant.name_RTA_CreateTransaction_Service_desc);

                try {
                    for (int i = 0; i <= createSalesOrderList.size() - 1; i++) {
                        MaximumAmount += createSalesOrderList.get(i).getItemPaidAmount();
                        MinimumAmount += createSalesOrderList.get(i).getMinimumAmount();
                        DueAmount += createSalesOrderList.get(i).getDueamount();
                        PaidAmount += createSalesOrderList.get(i).getItemPaidAmount();
                        ServiceCharge += createSalesOrderList.get(i).getServiceCharge();
                        DiscountRate += createSalesOrderList.get(i).getDiscountRate();
                    }
                    TotalAmount = ServiceCharge + PaidAmount + DiscountRate;
                    KskServiceItem[] items = createSalesOrderList.toArray(new KskServiceItem[createSalesOrderList.size()]);

                    ServiceLayerRMS.CallCreateTransactionInquiryRMSEID(ServiceID, EmployeeID, MinimumAmount, MaximumAmount, DueAmount,
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
                                                Common.writeObject(getContext(), Constant.FIPCreateTransactionObject, resObject);
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
                                }
                            }, getContext());

                } catch (Exception e) {
                    if (cTimer != null)
                        cTimer.start();
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }

            }
        });


        return view;
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
            case R.id.btn_proceedtopayment:
                mFragment = CardPaymentSwipe.newInstance();
                addFragment();
                break;
            case R.id.btnrtapcback:
                mFragment = RMSEmployeeServices.newInstance();
                addFragment();
                break;
           /* case R.id.btnrtapcinfo:
                break;*/
            case R.id.btnrtapchome:
                mFragment = RTAMain.newInstance();
                addFragment();
        }

    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();
        tv_txt_rmsemployeeid_ES.setText(resources.getString(R.string.EmployeeID));
        tv_txt_rmsservice_ES.setText(resources.getString(R.string.Service));
        tv_txt_rmsamounttopay_ES.setText(resources.getString(R.string.AmounttoPay));
        btn_Proceedtopayment.setText(resources.getString(R.string.ProceedToPayment));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.PaymentConfirmation));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}