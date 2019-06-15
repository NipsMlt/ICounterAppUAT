package rtaservices.RMSServices.Adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.RequestAndResponse.KskServiceItem;
import example.dtc.R;
import rtaservices.RMSServices.Interfaces.DrivingLicenseDetailsClickListener;
import rtaservices.RMSServices.Interfaces.RMSSalesInvoiceDetailsClickListener;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;

public class RMSInvoiceNoDetailsListViewAdapter extends BaseAdapter {

    private String[] InvoiceID;
    private String[] InvoiceDate;
    private String[] SalesID;
    private String[] ItemID;
    private String[] ItemName;
    private double[] Quantity;
    private double[] Amount;
    private Context mContext;
    private ArrayList<KskServiceItem> detailsItemsList;
    static private RMSSalesInvoiceDetailsClickListener rmsSalesInvoiceDetailsClickListener;
    private String OrderNo;
    boolean[] mCheckedState;
    ViewHolder viewHolder;
    private static CountDownTimer cTimer = null;
    LayoutInflater inflater;

    // View lookup cache
    private static class ViewHolder {
        public TextView tv_invoicenodetailscustomize_InvoiceId;
        public TextView tv_invoicenodetailscustomize_InvoiceDate;
        public TextView tv_invoicenodetailscustomize_SalesId;
        public TextView tv_invoicenodetailscustomize_ItemId;
        public TextView tv_invoicenodetailscustomize_ItemName;
        public TextView tv_invoicenodetailscustomize_Amount;
        public TextView tv_invoicenodetailscustomize_Quantity;
    }

    public RMSInvoiceNoDetailsListViewAdapter(Context context,
                                              ArrayList<KskServiceItem> invoicenodetaiList, CountDownTimer ctimer) {
        detailsItemsList = invoicenodetaiList;
        mCheckedState = new boolean[detailsItemsList.size()];
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        cTimer = ctimer;
        //Array Intitializing
        InvoiceID = new String[detailsItemsList.size()];
        InvoiceDate = new String[detailsItemsList.size()];
        SalesID = new String[detailsItemsList.size()];
        ItemID = new String[detailsItemsList.size()];
        ItemName = new String[detailsItemsList.size()];
        Quantity = new double[detailsItemsList.size()];
        Amount = new double[detailsItemsList.size()];
        for (int i = 0; i < detailsItemsList.size(); i++) {
            InvoiceID[i] = detailsItemsList.get(i).getItemText();
            InvoiceDate[i] = getProperServiceDate(detailsItemsList.get(i).getServiceDate());
            ItemID[i] = detailsItemsList.get(i).getItemId();
            ItemName[i] = detailsItemsList.get(i).getField2();
            Amount[i] = detailsItemsList.get(i).getItemPaidAmount();
            Quantity[i] = detailsItemsList.get(i).getDiscountRate();
            SalesID[i] = detailsItemsList.get(i).getType();
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        return position;
    }

    public int getCount() {
        return detailsItemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public void setRmsSalesInvoiceDetailsClickListener(RMSSalesInvoiceDetailsClickListener CallBack) {
        this.rmsSalesInvoiceDetailsClickListener = CallBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (cTimer != null)
            cTimer.start();
        final int Position = position;
        // Check if an existing view is being reused, otherwise inflate the view
        // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.rtaservicesrmsinvoicenopaydetailscustomize, parent, false);
            viewHolder.tv_invoicenodetailscustomize_InvoiceId = convertView.findViewById(R.id.tv_invoicenodetailscustomize_invoiceid);
            viewHolder.tv_invoicenodetailscustomize_InvoiceDate = convertView.findViewById(R.id.tv_invoicenodetailscustomize_invoicedate);
            viewHolder.tv_invoicenodetailscustomize_ItemId = convertView.findViewById(R.id.tv_invoicenodetailscustomize_itmeid);
            viewHolder.tv_invoicenodetailscustomize_ItemName = convertView.findViewById(R.id.tv_invoicenodetailscustomize_itmename);
            viewHolder.tv_invoicenodetailscustomize_Amount = convertView.findViewById(R.id.tv_invoicenodetailscustomize_amount);
            viewHolder.tv_invoicenodetailscustomize_Quantity = convertView.findViewById(R.id.tv_invoicenodetailscustomize_quantity);
            viewHolder.tv_invoicenodetailscustomize_SalesId = convertView.findViewById(R.id.tv_invoicenodetailscustomize_salesid);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        OrderNo = PreferenceConnector.readString(mContext, ConfigrationRTA.RMS_SalesOrderID, "");
        viewHolder.tv_invoicenodetailscustomize_InvoiceId.setText(InvoiceID[position]);
        viewHolder.tv_invoicenodetailscustomize_InvoiceDate.setText(InvoiceDate[position]);
        viewHolder.tv_invoicenodetailscustomize_SalesId.setText(SalesID[position]);
        viewHolder.tv_invoicenodetailscustomize_ItemId.setText(ItemID[position]);
        viewHolder.tv_invoicenodetailscustomize_ItemName.setText(ItemName[position]);
        viewHolder.tv_invoicenodetailscustomize_Amount.setText(String.valueOf(Amount[position]));
        viewHolder.tv_invoicenodetailscustomize_Quantity.setText(String.valueOf(Quantity[position]));


        convertView.setTag(viewHolder);
        // Return the completed view to render on screen
        return convertView;
    }

}
