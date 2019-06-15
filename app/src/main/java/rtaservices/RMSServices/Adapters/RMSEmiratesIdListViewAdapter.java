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

public class RMSEmiratesIdListViewAdapter extends BaseAdapter {

    private String[] orderInvoiceDate = new String[]{};
    private String[] orderInvoiceNo = new String[]{};
    private String[] StatusCode = new String[]{};
    private double[] Details = new double[]{};
    private double[] Amount = new double[]{};
    private CheckBox cb_SelectAll;
    private Context mContext;
    private ArrayList<KskServiceItem> detailsItemsList;
    static private CheckBoxClickListener CheckBoxMethodCallBack;
    static private RMSSalesInvoiceDetailsClickListener rmsSalesInvoiceDetailsClickListener;
    private ListView listView;
    private String OrderNo;
    boolean[] mCheckedState;
    ViewHolder viewHolder;
    private static CountDownTimer cTimer = null;
    LayoutInflater inflater;
    private Resources resources;

    // View lookup cache
    private static class ViewHolder {
        public TextView tv_emiratesid_orderno_Details;
        public TextView tv_emiratesid_orderdate_Details;
        public TextView tv_salesorder_statuscode_Details;
        public TextView tv_emiratesid_statuscode_Details;
        public TextView tv_emiratesid_amount_Details;
        public TextView tv_emiratesid_details_Details;
        public TextView tv_fipTotal;
        public CheckBox cb_select_emiratesId_Details;
        public ImageView iv_emiratesid_Details;
        private ImageView iv_UnpayableFine;
    }

    public RMSEmiratesIdListViewAdapter(Context context, String[] orderinvoicedate, String[] orderinvoiceno, String[] statuscode,
                                        double[] details, double[] amount,
                                        CheckBox cb_selectAll, ArrayList<KskServiceItem> fipdetailsItemsList, ListView mlistView, CountDownTimer ctimer) {
        orderInvoiceDate = orderinvoicedate;
        orderInvoiceNo = orderinvoiceno;
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

    public void setRMSSalesInvoiceDetailsCallBackMethod(RMSSalesInvoiceDetailsClickListener CallBack) {
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
                    convertView = inflater.inflate(R.layout.rtaservicesrmsemiratesidpaydetails_customize_payable, parent, false);
                    viewHolder.tv_emiratesid_orderno_Details = convertView.findViewById(R.id.tv_emiratesid_orderno_details);
                    viewHolder.tv_emiratesid_orderdate_Details = convertView.findViewById(R.id.tv_emiratesid_orderdate_details);
                    viewHolder.tv_emiratesid_statuscode_Details = convertView.findViewById(R.id.tv_emiratesid_statuscode_details);
                    viewHolder.tv_emiratesid_amount_Details = convertView.findViewById(R.id.tv_emiratesid_amount_details);
                    viewHolder.tv_emiratesid_details_Details = convertView.findViewById(R.id.tv_emiratesid_details_details);
                    viewHolder.cb_select_emiratesId_Details = (CheckBox) convertView.findViewById(R.id.cb_select_emiratesid);
                    viewHolder.iv_emiratesid_Details = (ImageView) convertView.findViewById(R.id.iv_emiratesid_details);
                    viewHolder.tv_emiratesid_details_Details.setText(resources.getString(R.string.Payable));
                    break;
                case 1://UnPayable
                    convertView = inflater.inflate(R.layout.rtaservicesrmsemiratesidpaydetails_customize_unpayable, parent, false);
                    viewHolder.tv_emiratesid_orderno_Details = convertView.findViewById(R.id.tv_emiratesid_orderno_details);
                    viewHolder.tv_emiratesid_orderdate_Details = convertView.findViewById(R.id.tv_emiratesid_orderdate_details);
                    viewHolder.tv_emiratesid_statuscode_Details = convertView.findViewById(R.id.tv_emiratesid_statuscode_details);
                    viewHolder.tv_emiratesid_amount_Details = convertView.findViewById(R.id.tv_emiratesid_amount_details);
                    viewHolder.tv_emiratesid_details_Details = convertView.findViewById(R.id.tv_emiratesid_details_details);
                    viewHolder.iv_emiratesid_Details = (ImageView) convertView.findViewById(R.id.iv_emiratesid_details);
                    viewHolder.iv_UnpayableFine = (ImageView) convertView.findViewById(R.id.iv_emiratesid_unpayablefine);
                    viewHolder.tv_emiratesid_details_Details.setText(resources.getString(R.string.UnPayable));
                    break;
                case 2://UnPayable
                    convertView = inflater.inflate(R.layout.rtaservicesrmsemiratesidpaydetails_customize_unpayable, parent, false);
                    viewHolder.tv_emiratesid_orderno_Details = convertView.findViewById(R.id.tv_emiratesid_orderno_details);
                    viewHolder.tv_emiratesid_orderdate_Details = convertView.findViewById(R.id.tv_emiratesid_orderdate_details);
                    viewHolder.tv_emiratesid_statuscode_Details = convertView.findViewById(R.id.tv_emiratesid_statuscode_details);
                    viewHolder.tv_emiratesid_amount_Details = convertView.findViewById(R.id.tv_emiratesid_amount_details);
                    viewHolder.tv_emiratesid_details_Details = convertView.findViewById(R.id.tv_emiratesid_details_details);
                    viewHolder.iv_emiratesid_Details = (ImageView) convertView.findViewById(R.id.iv_emiratesid_details);
                    viewHolder.iv_UnpayableFine = (ImageView) convertView.findViewById(R.id.iv_emiratesid_unpayablefine);
                    viewHolder.tv_emiratesid_details_Details.setText(resources.getString(R.string.UnPayable));
                    break;
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        OrderNo = PreferenceConnector.readString(mContext, ConfigrationRTA.RMS_SalesOrderID, "");
        viewHolder.tv_emiratesid_orderno_Details.setText(orderInvoiceNo[position]);
        viewHolder.tv_emiratesid_orderdate_Details.setText(orderInvoiceDate[position]);
        viewHolder.tv_emiratesid_statuscode_Details.setText(StatusCode[position]);
        viewHolder.tv_emiratesid_amount_Details.setText(String.valueOf(Amount[position]));
        viewHolder.tv_emiratesid_details_Details.setText(String.valueOf(Details[position]));


        //Check the all checkboxes
        switch (type) {
            case 0:
                viewHolder.cb_select_emiratesId_Details.setOnCheckedChangeListener(null);
                final KskServiceItem listViewItemDto = detailsItemsList.get(position);
                viewHolder.cb_select_emiratesId_Details.setChecked(listViewItemDto.isChecked());

                viewHolder.cb_select_emiratesId_Details.setTag(position); // This line is important.
                //Check boxes individual click
                viewHolder.cb_select_emiratesId_Details.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        viewHolder.iv_emiratesid_Details.setOnClickListener(new View.OnClickListener() {
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
