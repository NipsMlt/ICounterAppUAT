package rtaservices.RMSServices.Classes;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.LocaleHelper;
import RTANetworking.GenericServiceCall.ServiceLayerRMS;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItemRMS;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseRMS;

import example.dtc.R;
import interfaces.ResetTimerListener;
import rtaservices.RMSServices.Adapters.RMSSalesOrderDetailsListViewAdapter;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getCityNameFromStateCodeUpperCase;
import static RTANetworking.Common.Utilities.getDriverLicenseType;
import static RTANetworking.Common.Utilities.getIssueDate;
import static RTANetworking.Common.Utilities.getLicenseClass;
import static RTANetworking.Common.Utilities.getLicenseStatus;
import static RTANetworking.Common.Utilities.getProperServiceDate;
import static utility.Common.readObject;

public class RMSSalesOrderDetailsDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    private String[] ItemID = new String[]{};
    private String[] ItemName = new String[]{};
    private double[] Quantity = new double[]{};
    private double[] Amount = new double[]{};
    Fragment mFragment;
    ImageView iv_Closefipdetails;
    TextView tvrtaServiceTitle, tv_txt_salesorderdetails_ItemId, tv_txt_salesorderdetails_ItemName, tv_txt_salesorderdetails_Amount,
            tv_txt_salesorderdetails_Quantity;
    static private ResetTimerListener mMethodCallBack;
    private String Language, ClassName, SalesOrderID;
    //Declare timer
    CountDownTimer cTimer = null;
    // Response Final Object
    private static NPGKskInquiryResponseRMS resObject;
    private static NPGKskInquiryResponseItemRMS resObjectItem;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    ListView lv_rmssalesorderdeails_List;
    RMSSalesOrderDetailsListViewAdapter mAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder customDialogMain = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rtaservicesrmssalesorderdetails, null);

        iv_Closefipdetails = (ImageView) view.findViewById(R.id.iv_closefipdetails);
        lv_rmssalesorderdeails_List = (ListView) view.findViewById(R.id.lv_rmssalesorderdeails_list);
        tv_txt_salesorderdetails_ItemId = (TextView) view.findViewById(R.id.tv_txt_salesorderdetails_itemid);
        tv_txt_salesorderdetails_ItemName = (TextView) view.findViewById(R.id.tv_txt_salesorderdetails_itemname);
        tv_txt_salesorderdetails_Amount = (TextView) view.findViewById(R.id.tv_txt_salesorderdetails_amount);
        tv_txt_salesorderdetails_Quantity = (TextView) view.findViewById(R.id.tv_txt_salesorderdetails_quantity);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        /*//Getting the sales order ID
        SalesOrderID = PreferenceConnector.readString(getContext(), ConfigrationRTA.RMS_SalesOrderID, "");

        try {
//            if (cTimer != null)
//                cTimer.cancel();
            ServiceLayerRMS.callInquiryBySalesOrderDetails(SalesOrderID, ConfigrationRTA.SERVICE_NAME_RMS_SALESORDERDETAILS, ConfigrationRTA.SERVICE_ID_SALESORDERDETAILS, new ServiceCallback() {
                @Override
                public void onSuccess(JSONObject obj) {
                    Gson gson = new Gson();
                    //Setting view for Custom Dialog
                    customDialogMain.setView(view);
                    //Show dialog
                    dialog = customDialogMain.show();
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
                            resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItemRMS.class);

                        } else {

                            //Do things with array
                            resObject = gson.fromJson(obj.toString(), NPGKskInquiryResponseRMS.class);
                        }

                        // This is Array of Items
                        if (resObject != null) {

                            for (int i = 0; i < resObject.ServiceType.getItems().size(); i++)
                                salesorderdetaiList.add(resObject.getServiceType().getItems().get(i));

                                Log.d("Service Response", resObject.Message);

                            // This is object of Items
                        } else {
                            salesorderdetaiList.add(resObjectItem.getServiceType().getItems());
                        }

                        //Write a list in a cache
                        try {
                            // Common.writeObject(getContext(), Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                        } catch (Exception e) {
                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                        }
                        //Cancel the timer
//                        if (cTimer != null)
//                            cTimer.cancel();

                        //Writing it in the cache to get it anywhere
                        try {
                            // Common.writeObject(getContext(), Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Setting Adapter to the ListView
                        mAdapter = new RMSSalesOrderDetailsListViewAdapter(getContext(), salesorderdetaiList, cTimer);
                        lv_rmssalesorderdeails_List.setAdapter(mAdapter);


                    } catch (JSONException e) {
                        ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                    }

                }

                @Override
                public void onFailure(String obj) {
//                    if (cTimer != null)
//                        cTimer.start();
                }
            }, getContext());

        } catch (Exception e) {
//            if (cTimer != null)
//                cTimer.start();
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }*/

        try {
            fipdetailsItemsList = (ArrayList<KskServiceItem>) readObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mAdapter = new RMSSalesOrderDetailsListViewAdapter(getContext(), fipdetailsItemsList, null);
        lv_rmssalesorderdeails_List.setAdapter(mAdapter);

        iv_Closefipdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMethodCallBack != null) {
                    mMethodCallBack.ResetTimerCallBackMethod();
                }
                dialog.dismiss();
            }
        });

        //Setting view for Custom Dialog
        customDialogMain.setView(view);
        //Show dialog
        dialog = customDialogMain.show();

        return dialog;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        tvrtaServiceTitle.setText(resources.getString(R.string.SalesOrderDetails));
        tv_txt_salesorderdetails_ItemId.setText(resources.getString(R.string.ItemID));
        tv_txt_salesorderdetails_ItemName.setText(resources.getString(R.string.ItemName));
        tv_txt_salesorderdetails_Amount.setText(resources.getString(R.string.Amount));
        tv_txt_salesorderdetails_Quantity.setText(resources.getString(R.string.Quantity));
    }

    //Reset Timer call back method
    public void setMethodCallBack(ResetTimerListener CallBack) {
        this.mMethodCallBack = CallBack;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}