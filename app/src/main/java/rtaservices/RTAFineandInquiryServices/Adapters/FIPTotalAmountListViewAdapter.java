package rtaservices.RTAFineandInquiryServices.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import RTANetworking.RequestAndResponse.KskServiceItem;
import data.PlayListItem;
import example.dtc.R;

public class FIPTotalAmountListViewAdapter extends ArrayAdapter<PlayListItem> {

    Context mContext;
    private List<KskServiceItem> arr_create_transaction_Items = new ArrayList<>();
    private String FineDescription;
    double Amount;

    // View lookup cache
    private static class ViewHolder {
        TextView tv_FipTotalamount_Fines;
        TextView tv_FipTotalamount_Amount;
    }

    public FIPTotalAmountListViewAdapter(List<KskServiceItem> arr_Create_Transaction_Items, Context context) {
        super(context, R.layout.rowitemslistview_fiptotalamount);
        this.arr_create_transaction_Items = arr_Create_Transaction_Items;
        this.mContext = context;
    }


    public int getCount() {
        return arr_create_transaction_Items.size();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.rowitemslistview_fiptotalamount, parent, false);
            viewHolder.tv_FipTotalamount_Fines = (TextView) convertView.findViewById(R.id.tv_fiptotalamountfines);
            viewHolder.tv_FipTotalamount_Amount = (TextView) convertView.findViewById(R.id.tv_fiptotalamountamount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        try {
            FineDescription = arr_create_transaction_Items.get(position).getField2();
            //FineDescription.charAt(FineDescription.length() - 1);
            Amount = arr_create_transaction_Items.get(position).getItemPaidAmount();

            viewHolder.tv_FipTotalamount_Fines.setText(FineDescription);
            viewHolder.tv_FipTotalamount_Amount.setText(String.valueOf(Amount));

        } catch (Exception e) {

        }
        // Return the completed view to render on screen
        return convertView;
    }


}
