package rtaservices;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAFineandInquiryServices.Adapters.FIPTotalAmountListViewAdapter;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

public class FipTotalAmount extends Fragment implements View.OnClickListener {

    Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Paylater, btn_Paynow;
    private KioskPaymentResponse createTransactionObject;
    private KioskPaymentResponseItem createTransactionObjectItem;
    private ListView mListView;
    private FIPTotalAmountListViewAdapter mAdapter;
    private List<KskServiceItem> arr_create_transaction_Items = new ArrayList<>();
    private KskServiceItem arr_create_transaction_ItemsResponses = new KskServiceItem();
    private String TotalAmount, TransactionID;
    NPGKskInquiryResponse npgKskInquiryResponse = new NPGKskInquiryResponse();
    Gson gson = new Gson();
    private String ServiceChargeonCard;
    String ClassName, FragmentName;
    private TextView tv_timer, tvrtaServiceTitle, tv_Description, tv_Amount, tv_txtfipdesctotalAmount, tv_fipdescTotalAmount, tv_Seconds;
    //Declare timer
    CountDownTimer cTimer = null;
    private String Language, ServicesName;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }


    public static FipTotalAmount newInstance() {
        return new FipTotalAmount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtaserviestotalamount, null);
        //btn_Paylater = (Button) view.findViewById(R.id.btn_paylater);
        btn_Paynow = (Button) view.findViewById(R.id.btn_paynow);
        mListView = (ListView) view.findViewById(R.id.lv_fipdescamount);
        tv_fipdescTotalAmount = (TextView) view.findViewById(R.id.tv_fipdesctotalamount);
        tv_Description = (TextView) view.findViewById(R.id.tv_description);
        tv_Amount = (TextView) view.findViewById(R.id.tv_amount);
        tv_txtfipdesctotalAmount = (TextView) view.findViewById(R.id.tv_txtfipdesctotalamount);
        tv_Seconds = (TextView) view.findViewById(R.id.tv_seconds);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        btn_Back = (Button) view.findViewById(R.id.btnrtafiptotalamountback);
        btn_Info = (Button) view.findViewById(R.id.btnrtafiptotalamountinfo);
        btn_Home = (Button) view.findViewById(R.id.btnrtafiptotalamounthome);

        btn_Back.setOnClickListener(this);
        //btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Get Services Name to set the digital pass
        ServicesName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");
        tvrtaServiceTitle.setText(ServicesName);

        //Work for timer
        if (ServicesName.equals(ConfigrationRTA.TrafficFines)) {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipContactDetailsOnlyEmail.newInstance(), getFragmentManager());
        } else {
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000,
                    tv_timer, FipContactDetails.newInstance(), getFragmentManager());
        }


        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Get the fragment name as per the services
        FragmentName = PreferenceConnector.readString(getContext(), ConfigrationRTA.Service_Name, "");


        try {
            createTransactionObject = (KioskPaymentResponse) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);

        } catch (Exception e) {

        }
        try {
            createTransactionObjectItem = (KioskPaymentResponseItem) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);

        } catch (Exception e) {

        }

        try {
            if (createTransactionObject != null) {
                TransactionID = createTransactionObject.getServiceAttributesList().CKeyValuePair[0].getValue();
                if (createTransactionObject.getServiceAttributesList().CKeyValuePair.length > 2) {
                    TotalAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[2].getValue() + ".00";
                    arr_create_transaction_ItemsResponses = createTransactionObject.OperationType.ServicesType.getItems();
                    arr_create_transaction_Items.add(arr_create_transaction_ItemsResponses);
                } else
                    TotalAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[1].getValue() + ".00";

            } else if (createTransactionObjectItem != null) {
                TransactionID = createTransactionObjectItem.getServiceAttributesList().CKeyValuePair[0].getValue();
                if (createTransactionObjectItem.getServiceAttributesList().CKeyValuePair.length > 2) {
                    TotalAmount = createTransactionObjectItem.getServiceAttributesList().CKeyValuePair[2].getValue() + ".00";
                    arr_create_transaction_Items = createTransactionObjectItem.OperationType.ServicesType.Items;
                } else
                    TotalAmount = createTransactionObject.getServiceAttributesList().CKeyValuePair[1].getValue() + ".00";

            }
            //arr_create_transaction_Items = (ArrayList<KskServiceItem>) Common.readObject(getContext(), Constant.FIPCreateTransactionObject);
            //arr_create_transaction_Items = createTransactionObjectItem.OperationType.ServicesType.Items;
            tv_fipdescTotalAmount.setText(TotalAmount + " AED");

            mAdapter = new FIPTotalAmountListViewAdapter(arr_create_transaction_Items, getContext());
            mListView.setAdapter(mAdapter);

        } catch (Exception e) {
            //ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }


        btn_Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cTimer != null)
                    cTimer.cancel();

                if (FragmentName.equals(ConfigrationRTA.TrafficFines)) {
                    ServiceLayer.callInquiryServiceCharges(TotalAmount, TransactionID, new ServiceCallback() {
                        @Override
                        public void onSuccess(JSONObject obj) throws JSONException {
                            try {
                                npgKskInquiryResponse = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);
                                ServiceChargeonCard = npgKskInquiryResponse.ServiceAttributesList.CKeyValuePair[0].Value;
                                if (cTimer != null)
                                    cTimer.cancel();
                                Bundle bundlepos = new Bundle();
                                bundlepos.putString(Constant.ServiceChargesonCard, ServiceChargeonCard);
                                mFragment = PaymentConfirmation.newInstance();
                                mFragment.setArguments(bundlepos);
                                addFragment();

                            } catch (Exception e) {
                                ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                            }
                        }

                        @Override
                        public void onFailure(String obj) {
                            if (cTimer != null)
                                cTimer.start();
                        }
                    }, getContext());

                } else {
                    mFragment = PaymentConfirmation.newInstance();
                    addFragment();
                }


            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        try {
            if (cTimer != null)
                cTimer.cancel();
            switch (v.getId()) {
                case R.id.btnrtafiptotalamountback:
                    if (FragmentName.equals(ConfigrationRTA.TrafficFines)) {
                        mFragment = FipContactDetailsOnlyEmail.newInstance();
                        addFragment();
                    } else {
                        mFragment = FipContactDetails.newInstance();
                        addFragment();
                    }
                    break;
               /* case R.id.btnrtafiptotalamountinfo:
                    break;*/
                case R.id.btnrtafiptotalamounthome:
                    mFragment = RTAMainServices.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        tv_fipdescTotalAmount.setText(resources.getString(R.string.EnteryourEmailAddress));
        tv_Description.setText(resources.getString(R.string.Description));
        tv_Amount.setText(resources.getString(R.string.Amount));
        tv_txtfipdesctotalAmount.setText(resources.getString(R.string.TotalAmount));
        btn_Paynow.setText(resources.getString(R.string.Paynow));
        btn_Home.setText(resources.getString(R.string.Home));
        btn_Info.setText(resources.getString(R.string.Info));
        btn_Back.setText(resources.getString(R.string.Back));
        tvrtaServiceTitle.setText(resources.getString(R.string.TotalAmount));
        tv_Seconds.setText(resources.getString(R.string.seconds));

    }


    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}