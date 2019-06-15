package rtaservices.RMSServices.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.RequestAndResponse.KskServiceItem;
import example.dtc.R;
import interfaces.CheckBoxClickListener;
import rtaservices.RMSServices.Interfaces.RMSSalesInvoiceDetailsClickListener;
import utility.PreferenceConnector;

public class RMSSalesOrderListViewAdapter extends BaseAdapter {

    private String[] orderDate = new String[]{};
    private String[] StatusCode = new String[]{};
    private double[] Details = new double[]{};
    private double[] Amount = new double[]{};
    private CheckBox cb_SelectAll;
    private Context mContext;
    private ArrayList<KskServiceItem> detailsItemsList;
    static private CheckBoxClickListener CheckBoxMethodCallBack;
    static private RMSSalesInvoiceDetailsClickListener rmsSalesInvoiceDetailsClickListener;
    private ListView listView;
    private String OrderNo, InvoiceNo, ServiceName;
    boolean[] mCheckedState;
    ViewHolder viewHolder;
    private static CountDownTimer cTimer = null;
    LayoutInflater inflater;
    private Resources resources;

    // View lookup cache
    private static class ViewHolder {
        public TextView tv_salesorder_orderno_Details;
        public TextView tv_salesorder_orderdate_Details;
        public TextView tv_salesorder_statuscode_Details;
        public TextView tv_salesorder_amount_Details;
        public TextView tv_salesorder_details_Details;
        public TextView tv_fipTotal;
        public CheckBox cb_Select;
        public ImageView iv_details;
        private ImageView iv_UnpayableFine;
    }

    public RMSSalesOrderListViewAdapter(Context context, String[] orderdate, String[] statuscode,
                                        double[] details, double[] amount,
                                        CheckBox cb_selectAll, ArrayList<KskServiceItem> fipdetailsItemsList, ListView mlistView, CountDownTimer ctimer) {
        orderDate = orderdate;
        StatusCode = statuscode;
        Details = details;
        Amount = amount;
        cb_SelectAll = cb_selectAll;
        detailsItemsList = fipdetailsItemsList;
        listView = mlistView;
        mCheckedState = new boolean[detailsItemsList.size()];
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        cTimer = ctimer;
        resources = context.getResources();
        ServiceName = PreferenceConnector.readString(mContext, ConfigrationRTA.Service_Name, "");
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        if (detailsItemsList.get(position).getPoints() == 0) {
            return 0; //payable
        } else if (detailsItemsList.get(position).getPoints() == 1) {
            return 1;//UnPayable
        } else if (detailsItemsList.get(position).getPoints() == 2) {
            return 2; //UnPayable
        } else return 0;
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


    //Call back methods
    public void setCheckBoxMethodCallBack(CheckBoxClickListener CallBack) {
        this.CheckBoxMethodCallBack = CallBack;
    }

    public void setRMSSalesInvoiceDetailsDetailsMethodCallBack(RMSSalesInvoiceDetailsClickListener CallBack) {
        this.rmsSalesInvoiceDetailsClickListener = CallBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (cTimer != null)
            cTimer.start();
        final int Position = position;
        // Check if an existing view is being reused, otherwise inflate the view
        // view lookup cache stored in tag
        //check the type has 0 1 or 2
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (type) {
                case 0: //payable
                    convertView = inflater.inflate(R.layout.rtaservicesrmssalesorderpaydetails_customize_payable, parent, false);
                    viewHolder.tv_salesorder_orderno_Details = convertView.findViewById(R.id.tv_salesorder_orderno_details);
                    viewHolder.tv_salesorder_orderdate_Details = convertView.findViewById(R.id.tv_salesorder_orderdate_details);
                    viewHolder.tv_salesorder_statuscode_Details = convertView.findViewById(R.id.tv_salesorder_statuscode_details);
                    viewHolder.tv_salesorder_amount_Details = convertView.findViewById(R.id.tv_salesorder_amount_details);
                    viewHolder.tv_salesorder_details_Details = convertView.findViewById(R.id.tv_salesorder_details_details);
                    viewHolder.cb_Select = (CheckBox) convertView.findViewById(R.id.cb_salesorder);
                    viewHolder.iv_details = (ImageView) convertView.findViewById(R.id.iv_salesorder_details);
                    viewHolder.tv_salesorder_details_Details.setText(resources.getString(R.string.Payable));
                    break;
                case 1://UnPayable
                    convertView = inflater.inflate(R.layout.rtaservicesrmssalesorderpaydetails_customize_unpayable, parent, false);
                    viewHolder.tv_salesorder_orderno_Details = convertView.findViewById(R.id.tv_salesorder_orderno_details);
                    viewHolder.tv_salesorder_orderdate_Details = convertView.findViewById(R.id.tv_salesorder_orderdate_details);
                    viewHolder.tv_salesorder_statuscode_Details = convertView.findViewById(R.id.tv_salesorder_statuscode_details);
                    viewHolder.tv_salesorder_amount_Details = convertView.findViewById(R.id.tv_salesorder_amount_details);
                    viewHolder.tv_salesorder_details_Details = convertView.findViewById(R.id.tv_salesorder_details_details);
                    viewHolder.iv_details = (ImageView) convertView.findViewById(R.id.iv_salesorder_details);
                    viewHolder.iv_UnpayableFine = (ImageView) convertView.findViewById(R.id.iv_salesorder_unpayablefine);
                    viewHolder.tv_salesorder_details_Details.setText(resources.getString(R.string.UnPayable));
                    break;
                case 2://UnPayable
                    convertView = inflater.inflate(R.layout.rtaservicesrmssalesorderpaydetails_customize_unpayable, parent, false);
                    viewHolder.tv_salesorder_orderno_Details = convertView.findViewById(R.id.tv_salesorder_orderno_details);
                    viewHolder.tv_salesorder_orderdate_Details = convertView.findViewById(R.id.tv_salesorder_orderdate_details);
                    viewHolder.tv_salesorder_statuscode_Details = convertView.findViewById(R.id.tv_salesorder_statuscode_details);
                    viewHolder.tv_salesorder_amount_Details = convertView.findViewById(R.id.tv_salesorder_amount_details);
                    viewHolder.tv_salesorder_details_Details = convertView.findViewById(R.id.tv_salesorder_details_details);
                    viewHolder.iv_details = (ImageView) convertView.findViewById(R.id.iv_salesorder_details);
                    viewHolder.iv_UnpayableFine = (ImageView) convertView.findViewById(R.id.iv_salesorder_unpayablefine);
                    viewHolder.tv_salesorder_details_Details.setText(resources.getString(R.string.UnPayable));
                    break;
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        OrderNo = PreferenceConnector.readString(mContext, ConfigrationRTA.RMS_SalesOrderID, "");
        InvoiceNo = PreferenceConnector.readString(mContext, ConfigrationRTA.RMSInvoiceNo, "");
        if (ServiceName.equals(ConfigrationRTA.RMS_SALES_ORDER))
            viewHolder.tv_salesorder_orderno_Details.setText(OrderNo);
        else if (ServiceName.equals(ConfigrationRTA.RMS_SALES_INVOICE))
            viewHolder.tv_salesorder_orderno_Details.setText(InvoiceNo);
        viewHolder.tv_salesorder_orderdate_Details.setText(orderDate[position]);
        viewHolder.tv_salesorder_statuscode_Details.setText(StatusCode[position]);
        viewHolder.tv_salesorder_amount_Details.setText(String.valueOf(Amount[position]));


        //Check the all checkboxes
        switch (type) {
            case 0:
                viewHolder.cb_Select.setOnCheckedChangeListener(null);
                final KskServiceItem listViewItemDto = detailsItemsList.get(position);
                viewHolder.cb_Select.setChecked(listViewItemDto.isChecked());

                viewHolder.cb_Select.setTag(position); // This line is important.
                //Check boxes individual click
                viewHolder.cb_Select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        detailsItemsList.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.

                        if (isChecked) {
                            if (CheckBoxMethodCallBack != null) {
                                CheckBoxMethodCallBack.CheckBoxClickCallBackMethod(isChecked, Position);
                            }
                        } else {
                            if (CheckBoxMethodCallBack != null) {
                                CheckBoxMethodCallBack.CheckBoxClickCallBackMethod(isChecked, Position);
                            }
                        }
                    }
                });
                break;
        }


        //Opens the details dialog
        viewHolder.iv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rmsSalesInvoiceDetailsClickListener != null) {
                    rmsSalesInvoiceDetailsClickListener.RMSSalesInvoiceDetailsCallBackMethod(Position, listView);
                }
            }
        });


        convertView.setTag(viewHolder);
        // Return the completed view to render on screen
        return convertView;
    }

}
