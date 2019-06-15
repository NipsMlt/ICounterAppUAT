package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RMSServices.Classes.PaymentConfirmationRMSEmployeeServices;
import rtaservices.RMSServices.Classes.RMSEmiratesIDListofPendingPaymentDetails;
import rtaservices.RMSServices.Classes.RMSSalesOrderListofPendingPaymentDetails;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class PaymentConfirmation extends Fragment implements View.OnClickListener {

    Fragment mFragment;
    Button btn_Proceedtopayment, btn_Back, btn_Info, btn_Home;
    TextView tvrtaServiceTitle, tv_fippaymentcategorydueAmount, tv_fippaymentcategorytotalAmount,
            tv_fippaymentcategoryServiceChargeonCard, tv_fippaymentcategoryservicechargeText,
            tv_txtfippaymentcategorydueAmount, tv_txtfippaymentcategorytotalAmount;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;
    private double DueAmount;
    private Double TotalAmount;
    private Double ServiceChargeonCard;
    private TextView tv_timer, tv_Seconds;
    CountDownTimer cTimer;
    String ClassName;
    private String Language, FragmentName, ItemQuantity;
    private MediaPlayer mPlayer;
    private Context context;
    private Resources resources;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    public static PaymentConfirmation newInstance() {
        return new PaymentConfirmation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.rtaserviespaymentconfirmation, null);
        tv_fippaymentcategorydueAmount = (TextView) view.findViewById(R.id.tv_fippaymentcategorydueamount);
        tv_fippaymentcategorytotalAmount = (TextView) view.findViewById(R.id.tv_fippaymentcategorytotalamount);
        tv_fippaymentcategoryServiceChargeonCard = (TextView) view.findViewById(R.id.tv_fippaymentcategoryservicecharge);
        tv_fippaymentcategoryservicechargeText = (TextView) view.findViewById(R.id.tv_fippaymentcategoryservicechargetext);
        tv_txtfippaymentcategorydueAmount = (TextView) view.findViewById(R.id.tv_txtfippaymentcategorydueamount);
        tv_txtfippaymentcategorytotalAmount = (TextView) view.findViewById(R.id.tv_txtfippaymentcategorytotalamount);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Proceedtopayment = (Button) view.findViewById(R.id.btn_proceedtopayment);
        btn_Back = (Button) view.findViewById(R.id.btnrtapcback);
        btn_Info = (Button) view.findViewById(R.id.btnrtapcinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtapchome);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);
        btn_Proceedtopayment.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        context = LocaleHelper.setLocale(getContext(), Language);
        resources = context.getResources();
        ChangeLanguage(Language);


        //Fragment Name
        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");

        //Work for timer
        if (FragmentName.equals(ConfigrationRTA.RMS_SALES_ORDER) || FragmentName.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipContactDetailsOnlyEmail.newInstance(), getFragmentManager());
        } else if (FragmentName.equals(ConfigrationRTA.RMS_EMIRATESID) || FragmentName.equals(ConfigrationRTA.RMS_TRADE_LICENSE)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipContactDetailsOnlyEmail.newInstance(), getFragmentManager());
        } else if (FragmentName.equals(ConfigrationRTA.RMS_EMPLOYEE_SERVICES)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipContactDetailsOnlyEmail.newInstance(), getFragmentManager());
        } else {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipTotalAmount.newInstance(), getFragmentManager());
        }
        try {
            ServiceChargeonCard = Double.parseDouble(bundle.getString(Constant.ServiceChargesonCard));
            // cTimer = Common.SetCountDownTimer(cTimer, 50000, 1000, tv_timer, PaymentConfirmation.newInstance(), getFragmentManager());
        } catch (Exception e) {

        }
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

        try {
            createTransactionObject = (KioskPaymentResponse) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);
            if (createTransactionObject.getServiceAttributesList().CKeyValuePair.length > 2) {
                tv_txtfippaymentcategorydueAmount.setText(resources.getString(R.string.DueAmount));
                DueAmount = createTransactionObject.OperationType.Dueamount;
                TotalAmount = Double.parseDouble(createTransactionObject.ServiceAttributesList.CKeyValuePair[2].getValue());
                tv_fippaymentcategorydueAmount.setText(String.valueOf(DueAmount) + " AED");
            } else {
                if (ConfigrationRTA.RMS_SALES_ORDER.equals(FragmentName) || ConfigrationRTA.RMS_SALES_INVOICE.equals(FragmentName) ||
                        ConfigrationRTA.RMS_EMIRATESID.equals(FragmentName) || ConfigrationRTA.RMS_TRADE_LICENSE.equals(FragmentName)) {
                    //Get Item Quantity
                    tv_txtfippaymentcategorydueAmount.setText(resources.getString(R.string.ItemQuantity));
                    ItemQuantity = PreferenceConnector.readString(getContext(), Constant.ITEMQUANTITY, "");
                    // TotalAmount = Double.parseDouble(createTransactionObject.ServiceAttributesList.CKeyValuePair[1].getValue());
                    tv_fippaymentcategorydueAmount.setText(ItemQuantity);
                } else {
                    tv_fippaymentcategorydueAmount.setVisibility(View.GONE);
                    tv_txtfippaymentcategorydueAmount.setVisibility(View.GONE);
                }
                TotalAmount = Double.parseDouble(createTransactionObject.ServiceAttributesList.CKeyValuePair[1].Value);
            }


        } catch (Exception e) {

        }
        try {
            createTransactionObjectItem = (KioskPaymentResponseItem) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);
            if (createTransactionObjectItem.getServiceAttributesList().CKeyValuePair.length > 2) {
                tv_txtfippaymentcategorydueAmount.setText(resources.getString(R.string.DueAmount));
                DueAmount = createTransactionObjectItem.OperationType.Dueamount;
                TotalAmount = Double.parseDouble(createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[2].getValue());
                tv_fippaymentcategorydueAmount.setText(String.valueOf(DueAmount) + " AED");
            } else {
                if (ConfigrationRTA.RMS_SALES_ORDER.equals(FragmentName) || ConfigrationRTA.RMS_SALES_INVOICE.equals(FragmentName) ||
                        ConfigrationRTA.RMS_EMIRATESID.equals(FragmentName) || ConfigrationRTA.RMS_TRADE_LICENSE.equals(FragmentName)) {
                    //Get Item Quantity
                    tv_txtfippaymentcategorydueAmount.setText(resources.getString(R.string.ItemQuantity));
                    ItemQuantity = PreferenceConnector.readString(getContext(), Constant.ITEMQUANTITY, "");
                    //TotalAmount = Double.parseDouble(createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[1].getValue());
                    tv_fippaymentcategorydueAmount.setText(ItemQuantity);
                } else {
                    tv_fippaymentcategorydueAmount.setVisibility(View.GONE);
                    tv_txtfippaymentcategorydueAmount.setVisibility(View.GONE);
                }
                TotalAmount = Double.parseDouble(createTransactionObjectItem.ServiceAttributesList.CKeyValuePair[1].Value);
            }


        } catch (Exception e) {

        }

        try {
            //get service Charge
            if (ServiceChargeonCard != null) {
                if (ServiceChargeonCard > 0) {
                    tv_fippaymentcategoryservicechargeText.setVisibility(View.VISIBLE);
                    tv_fippaymentcategoryServiceChargeonCard.setVisibility(View.VISIBLE);
                    tv_fippaymentcategoryServiceChargeonCard.setText(ServiceChargeonCard.toString());
                    TotalAmount = TotalAmount + ServiceChargeonCard;
                }
            } else {
                tv_fippaymentcategoryservicechargeText.setVisibility(View.GONE);
                tv_fippaymentcategoryServiceChargeonCard.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }
        //Adding service charge in total amount
        tv_fippaymentcategorytotalAmount.setText(String.valueOf(TotalAmount) + "AED");

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
                if (FragmentName.equals(ConfigrationRTA.RMS_EMPLOYEE_SERVICES) ||
                        FragmentName.equals(ConfigrationRTA.RMS_EMIRATESID) ||
                        FragmentName.equals(ConfigrationRTA.RMS_TRADE_LICENSE) ||
                        FragmentName.equals(ConfigrationRTA.RMS_SALES_ORDER) ||
                        FragmentName.equals(ConfigrationRTA.RMS_SALES_INVOICE)) {
                    mFragment = FipContactDetailsOnlyEmail.newInstance();
                    addFragment();
                } else {
                    mFragment = FipTotalAmount.newInstance();
                    addFragment();
                }
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

        btn_Proceedtopayment.setText(resources.getString(R.string.ProceedToPayment));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.PaymentConfirmation));
        tv_fippaymentcategoryservicechargeText.setText(resources.getString(R.string.ServiceCharge));
        //tv_txtfippaymentcategorydueAmount.setText(resources.getString(R.string.DueAmount));
        tv_txtfippaymentcategorytotalAmount.setText(resources.getString(R.string.TotalAmount));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}