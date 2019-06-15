package rtaservices.RTAFineandInquiryServices.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FineInqandPaymentDetailsAdapter extends RecyclerView.Adapter<FineInqandPaymentDetailsAdapter.ViewHolder> {
    private String[] arrTicketno;
    private String[] arrDate = new String[]{};
    private double[] arrFineAmount = new double[]{};
    private double[] arrPenalty = new double[]{};
    private double[] arrKnowledgeFee = new double[]{};
    private double[] arrTotal = new double[]{};
    private CheckBox cb_SelectAll, cb_Select;
    private ArrayList<KskServiceItem> FipdetailsItemsList;
    private RecyclerView mRecyclerView;
    static private CheckBoxClickListener CheckBoxMethodCallBack;
    static private DrivingLicenseDetailsClickListener DrivingLicenseDetailsMethodCallBack;
    private ListView listView;
    boolean[] mCheckedState;
    private ImageView iv_details;
    private ImageView iv_UnpayableFine;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_fipTicketNo;
        public TextView tv_fipDate;
        public TextView tv_fipFineAmount;
        public TextView tv_fipPenalty;
        public TextView tv_fipKnowledgeFee;
        public TextView tv_fipTotal;
        public CheckBox cb_Select;

        public ViewHolder(View v) {
            super(v);
            tv_fipTicketNo = v.findViewById(R.id.tv_fipticketno);
            tv_fipDate = v.findViewById(R.id.tv_fipdate);
            tv_fipFineAmount = v.findViewById(R.id.tv_fipfineamount);
            tv_fipPenalty = v.findViewById(R.id.tv_fippenalty);
            tv_fipKnowledgeFee = v.findViewById(R.id.tv_fipknowledgefee);
            tv_fipTotal = v.findViewById(R.id.tv_fiptotal);
            cb_Select = (CheckBox) v.findViewById(R.id.cb_fipselect);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FineInqandPaymentDetailsAdapter(Context context, String[] arrticketno, String[] arrdate, double[] arrfineamount,
                                           double[] arrpenalty, double[] arrknowledgefee, double[] arrtotal,
                                           CheckBox cb_selectAll, ArrayList<KskServiceItem> fipdetailsItemsList, RecyclerView mrecyclerView) {
        arrTicketno = arrticketno;
        arrDate = arrdate;
        arrFineAmount = arrfineamount;
        arrPenalty = arrpenalty;
        arrKnowledgeFee = arrknowledgefee;
        arrTotal = arrtotal;
        cb_SelectAll = cb_selectAll;
        FipdetailsItemsList = fipdetailsItemsList;
        mCheckedState = new boolean[FipdetailsItemsList.size()];
        mRecyclerView = mrecyclerView;
    }

    public void setCheckBoxMethodCallBack(CheckBoxClickListener CallBack) {
        this.CheckBoxMethodCallBack = CallBack;
    }

    public void setDrivingLicenseDetailsMethodCallBack(DrivingLicenseDetailsClickListener CallBack) {
        this.DrivingLicenseDetailsMethodCallBack = CallBack;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FineInqandPaymentDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rtaservicesfineinqnpaydetails_customize_payable, parent, false);

        iv_details = (ImageView) view.findViewById(R.id.iv_fipfnpdetails);
        iv_UnpayableFine = (ImageView) view.findViewById(R.id.iv_unpayablefine);

        ViewHolder vh = new ViewHolder(view);
        vh.cb_Select.setOnCheckedChangeListener(null);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_fipTicketNo.setText(arrTicketno[position]);
        holder.tv_fipDate.setText(arrDate[position]);
        holder.tv_fipFineAmount.setText(String.valueOf(arrFineAmount[position]));
        holder.tv_fipKnowledgeFee.setText(String.valueOf(arrKnowledgeFee[position]));
        holder.tv_fipPenalty.setText(String.valueOf(arrPenalty[position]));
        holder.tv_fipTotal.setText(String.valueOf(arrTotal[position]));

        //Check the all checkboxes
        final KskServiceItem listViewItemDto = FipdetailsItemsList.get(position);
        holder.cb_Select.setChecked(listViewItemDto.isChecked());

        for(int i=0;i<FipdetailsItemsList.size();i++) {
            //If the fines are not unpaybale
            if (FipdetailsItemsList.get(i).getField7().equals("1")) {
                // 1 Unpayable, 2 Payable
                holder.cb_Select.setVisibility(View.GONE);
                iv_UnpayableFine.setVisibility(View.VISIBLE);

                //If the fines are not Paybale
            } else if (FipdetailsItemsList.get(i).getField7().equals("2")) {
//                    iv_UnpayableFine.setVisibility(View.GONE);
//                    viewHolder.cb_Select.setVisibility(View.VISIBLE);
            }
        }

        iv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DrivingLicenseDetailsMethodCallBack != null) {
                    DrivingLicenseDetailsMethodCallBack.DrivingLicenseDetailsCallBackMethod(position);
                }
            }
        });

        holder.cb_Select.setTag(position); // This line is important.
        //Check boxes individual click
        holder.cb_Select.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                FipdetailsItemsList.get(getPosition).setChecked(buttonView.isChecked()); // Set the value of checkbox to maintain its state.

                if (isChecked) {
                    if (CheckBoxMethodCallBack != null) {
                        CheckBoxMethodCallBack.CheckBoxClickCallBackMethod(isChecked, position);
                    }
                } else {
                    if (CheckBoxMethodCallBack != null) {
                        CheckBoxMethodCallBack.CheckBoxClickCallBackMethod(isChecked, position);
                    }
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return arrTicketno.length;
    }
}