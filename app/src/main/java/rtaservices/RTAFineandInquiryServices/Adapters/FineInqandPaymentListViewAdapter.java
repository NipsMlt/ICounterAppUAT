package rtaservices.RTAFineandInquiryServices.Adapters;

import android.content.Context;
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

import RTANetworking.RequestAndResponse.KskServiceItem;
import example.dtc.R;
import interfaces.CheckBoxClickListener;
import rtaservices.RTAFineandInquiryServices.Interfaces.DrivingLicenseDetailsClickListener;

public class FineInqandPaymentListViewAdapter extends BaseAdapter {

    private String[] arrTicketno;
    private String[] arrDate = new String[]{};
    private double[] arrFineAmount = new double[]{};
    private double[] arrPenalty = new double[]{};
    private double[] arrKnowledgeFee = new double[]{};
    private double[] arrTotal = new double[]{};
    private CheckBox cb_SelectAll;
    private Context mContext;
    private ArrayList<KskServiceItem> FipdetailsItemsList;
    static private CheckBoxClickListener CheckBoxMethodCallBack;
    static private DrivingLicenseDetailsClickListener DrivingLicenseDetailsMethodCallBack;
    private ListView listView;
    boolean[] mCheckedState;
    ViewHolder viewHolder;
    private static CountDownTimer cTimer = null;
    LayoutInflater inflater;

    // View lookup cache
    private static class ViewHolder {
        public TextView tv_fipTicketNo;
        public TextView tv_fipDate;
        public TextView tv_fipFineAmount;
        public TextView tv_fipPenalty;
        public TextView tv_fipKnowledgeFee;
        public TextView tv_fipTotal;
        public CheckBox cb_Select;
        public ImageView iv_details;
        private ImageView iv_UnpayableFine;
    }

    public FineInqandPaymentListViewAdapter(Context context, String[] arrticketno, String[] arrdate, double[] arrfineamount,
                                            double[] arrpenalty, double[] arrknowledgefee, double[] arrtotal,
                                            CheckBox cb_selectAll, ArrayList<KskServiceItem> fipdetailsItemsList, ListView mlistView, CountDownTimer ctimer) {
        arrTicketno = arrticketno;
        arrDate = arrdate;
        arrFineAmount = arrfineamount;
        arrPenalty = arrpenalty;
        arrKnowledgeFee = arrknowledgefee;
        arrTotal = arrtotal;
        cb_SelectAll = cb_selectAll;
        FipdetailsItemsList = fipdetailsItemsList;
        listView = mlistView;
        mCheckedState = new boolean[FipdetailsItemsList.size()];
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        cTimer = ctimer;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // here your custom logic to choose the view type
        if (FipdetailsItemsList.get(position).getField7().equals("1")) {
            return 0; //Unpayable
        } else if (FipdetailsItemsList.get(position).getField7().equals("2")) {
            return 1; //Payable

        } else return 0;
    }

    public int getCount() {
        return arrFineAmount.length;
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
        //check the type has 0 or 1
        int type = getItemViewType(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            switch (type) {
                case 0: //Unpayable
                    convertView = inflater.inflate(R.layout.rtaservicesfineinqnpaydetails_customize_unpayable, parent, false);
                    viewHolder.tv_fipTicketNo = convertView.findViewById(R.id.tv_fipticketno);
                    viewHolder.tv_fipDate = convertView.findViewById(R.id.tv_fipdate);
                    viewHolder.tv_fipFineAmount = convertView.findViewById(R.id.tv_fipfineamount);
                    viewHolder.tv_fipPenalty = convertView.findViewById(R.id.tv_fippenalty);
                    viewHolder.tv_fipKnowledgeFee = convertView.findViewById(R.id.tv_fipknowledgefee);
                    viewHolder.tv_fipTotal = convertView.findViewById(R.id.tv_fiptotal);
                    viewHolder.iv_details = (ImageView) convertView.findViewById(R.id.iv_fipfnpdetails);
                    viewHolder.iv_UnpayableFine = (ImageView) convertView.findViewById(R.id.iv_unpayablefine);
                    break;
                case 1://Payable
                    convertView = inflater.inflate(R.layout.rtaservicesfineinqnpaydetails_customize_payable, parent, false);
                    viewHolder.tv_fipTicketNo = convertView.findViewById(R.id.tv_fipticketno);
                    viewHolder.tv_fipDate = convertView.findViewById(R.id.tv_fipdate);
                    viewHolder.tv_fipFineAmount = convertView.findViewById(R.id.tv_fipfineamount);
                    viewHolder.tv_fipPenalty = convertView.findViewById(R.id.tv_fippenalty);
                    viewHolder.tv_fipKnowledgeFee = convertView.findViewById(R.id.tv_fipknowledgefee);
                    viewHolder.tv_fipTotal = convertView.findViewById(R.id.tv_fiptotal);
                    viewHolder.cb_Select = (CheckBox) convertView.findViewById(R.id.cb_fipselect);
                    viewHolder.iv_details = (ImageView) convertView.findViewById(R.id.iv_fipfnpdetails);
                    break;
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.tv_fipTicketNo.setText(arrTicketno[position]);
        viewHolder.tv_fipDate.setText(arrDate[position]);
        viewHolder.tv_fipFineAmount.setText(String.valueOf(arrFineAmount[position]));
        viewHolder.tv_fipKnowledgeFee.setText(String.valueOf(arrKnowledgeFee[position]));
        viewHolder.tv_fipPenalty.setText(String.valueOf(arrPenalty[position]));
        viewHolder.tv_fipTotal.setText(String.valueOf(arrTotal[position]));

        //Check the all checkboxes
        switch (type) {
            case 1:
                viewHolder.cb_Select.setOnCheckedChangeListener(null);
                final KskServiceItem listViewItemDto = FipdetailsItemsList.get(position);
                viewHolder.cb_Select.setChecked(listViewItemDto.isChecked());

                viewHolder.cb_Select.setTag(position); // This line is important.
                //Check boxes individual click
                viewHolder.cb_Select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        FipdetailsItemsList.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.

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
                if (DrivingLicenseDetailsMethodCallBack != null) {
                    DrivingLicenseDetailsMethodCallBack.DrivingLicenseDetailsCallBackMethod(Position);
                }
            }
        });


        convertView.setTag(viewHolder);
        // Return the completed view to render on screen
        return convertView;
    }

}
