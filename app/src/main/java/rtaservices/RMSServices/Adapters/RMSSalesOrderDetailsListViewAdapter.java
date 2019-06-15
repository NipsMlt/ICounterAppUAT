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
import utility.PreferenceConnector;

public class RMSSalesOrderDetailsListViewAdapter extends BaseAdapter {

    private String[] ItemID;
    private String[] ItemName;
    private double[] Quantity;
    private double[] Amount;
    private Context mContext;
    private ArrayList<KskServiceItem> detailsItemsList;
    static private DrivingLicenseDetailsClickListener DrivingLicenseDetailsMethodCallBack;
    private String OrderNo;
    boolean[] mCheckedState;
    ViewHolder viewHolder;
    private static CountDownTimer cTimer = null;
    LayoutInflater inflater;

    // View lookup cache
    private static class ViewHolder {
        public TextView tv_salesorderdetailscustomize_ItmeId;
        public TextView tv_alesorderdetailscustomize_ItmeName;
        public TextView tv_alesorderdetailscustomize_Amount;
        public TextView tv_alesorderdetailscustomize_Quantity;
    }

    public RMSSalesOrderDetailsListViewAdapter(Context context,
                                               ArrayList<KskServiceItem> salesorderdetaiList, CountDownTimer ctimer) {
        detailsItemsList = salesorderdetaiList;
        mCheckedState = new boolean[detailsItemsList.size()];
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        cTimer = ctimer;
        //Array Intitializing
        ItemID = new String[detailsItemsList.size()];
        ItemName = new String[detailsItemsList.size()];
        Quantity = new double[detailsItemsList.size()];
        Amount = new double[detailsItemsList.size()];
        for (int i = 0; i < detailsItemsList.size(); i++) {
            ItemID[i] = detailsItemsList.get(i).getItemId();
            ItemName[i] = detailsItemsList.get(i).getItemText();
            Amount[i] = detailsItemsList.get(i).getItemPaidAmount();
            Quantity[i] = detailsItemsList.get(i).getPoints();
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

    public void setDrivingLicenseDetailsMethodCallBack(DrivingLicenseDetailsClickListener CallBack) {
        this.DrivingLicenseDetailsMethodCallBack = CallBack;
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
            convertView = inflater.inflate(R.layout.rtaservicesrmssalesorderpaydetailscustomize, parent, false);
            viewHolder.tv_salesorderdetailscustomize_ItmeId = convertView.findViewById(R.id.tv_salesorderdetailscustomize_itmeid);
            viewHolder.tv_alesorderdetailscustomize_ItmeName = convertView.findViewById(R.id.tv_alesorderdetailscustomize_itmename);
            viewHolder.tv_alesorderdetailscustomize_Amount = convertView.findViewById(R.id.tv_alesorderdetailscustomize_amount);
            viewHolder.tv_alesorderdetailscustomize_Quantity = convertView.findViewById(R.id.tv_alesorderdetailscustomize_quantity);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        OrderNo = PreferenceConnector.readString(mContext, ConfigrationRTA.RMS_SalesOrderID, "");
        viewHolder.tv_salesorderdetailscustomize_ItmeId.setText(ItemID[position]);
        viewHolder.tv_alesorderdetailscustomize_ItmeName.setText(ItemName[position]);
        viewHolder.tv_alesorderdetailscustomize_Amount.setText(String.valueOf(Amount[position]));
        viewHolder.tv_alesorderdetailscustomize_Quantity.setText(String.valueOf(Quantity[position]));


        convertView.setTag(viewHolder);
        // Return the completed view to render on screen
        return convertView;
    }

}
