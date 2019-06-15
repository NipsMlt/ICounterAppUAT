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
import rtaservices.RMSServices.Adapters.RMSInvoiceNoDetailsListViewAdapter;
import rtaservices.RMSServices.Adapters.RMSSalesOrderDetailsListViewAdapter;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Constant;
import utility.PreferenceConnector;

import static utility.Common.readObject;

public class RMSInvoiceDetailsDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    Fragment mFragment;
    ImageView iv_Closefipdetails;
    TextView tvrtaServiceTitle, tv_txt_invoicenodetails_InvoiceId, tv_txt_invoicenodetails_InvoiceDate,
            tv_txt_invoicenodetails_ItemId, tv_txt_invoicenodetails_ItemName,
            tv_txt_invoicenodetails_Amount, tv_txt_invoicenodetails_Quantity,
            tv_txt_invoicenodetails_SalesId;
    static private ResetTimerListener mMethodCallBack;
    private String Language, ClassName;
    ListView lv_rmsinvoicenodeails_List;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private RMSInvoiceNoDetailsListViewAdapter mAdapterRmsInvoiceNoDetailsListViewAdapter;


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
        View view = inflater.inflate(R.layout.rtaservicesrmsinvoicenodetails, null);

        iv_Closefipdetails = (ImageView) view.findViewById(R.id.iv_closefipdetails);
        lv_rmsinvoicenodeails_List = (ListView) view.findViewById(R.id.lv_rmssalesorderdeails_list);
        tv_txt_invoicenodetails_InvoiceId = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_invoiceid);
        tv_txt_invoicenodetails_InvoiceDate = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_invoicedate);
        tv_txt_invoicenodetails_ItemId = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_itemid);
        tv_txt_invoicenodetails_ItemName = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_itemname);
        tv_txt_invoicenodetails_Amount = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_amount);
        tv_txt_invoicenodetails_Quantity = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_quantity);
        tv_txt_invoicenodetails_SalesId = (TextView) view.findViewById(R.id.tv_txt_invoicenodetails_salesid);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        try {
            fipdetailsItemsList = (ArrayList<KskServiceItem>) readObject(getContext(), Constant.RMSSalesOrderInvoicedetailsResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        mAdapterRmsInvoiceNoDetailsListViewAdapter = new RMSInvoiceNoDetailsListViewAdapter(getContext(), fipdetailsItemsList, null);
        lv_rmsinvoicenodeails_List.setAdapter(mAdapterRmsInvoiceNoDetailsListViewAdapter);

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

        tvrtaServiceTitle.setText(resources.getString(R.string.InvoiceDetails));
        tv_txt_invoicenodetails_InvoiceId.setText(resources.getString(R.string.InvoiceID));
        tv_txt_invoicenodetails_InvoiceDate.setText(resources.getString(R.string.InvoiceDate));
        tv_txt_invoicenodetails_ItemId.setText(resources.getString(R.string.ItemID));
        tv_txt_invoicenodetails_ItemName.setText(resources.getString(R.string.ItemName));
        tv_txt_invoicenodetails_Amount.setText(resources.getString(R.string.Amount));
        tv_txt_invoicenodetails_Quantity.setText(resources.getString(R.string.Quantity));
        tv_txt_invoicenodetails_SalesId.setText(resources.getString(R.string.SalesID));
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