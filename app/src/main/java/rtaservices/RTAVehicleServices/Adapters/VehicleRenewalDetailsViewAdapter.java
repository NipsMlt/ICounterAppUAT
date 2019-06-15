package rtaservices.RTAVehicleServices.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import RTANetworking.RequestAndResponse.CKeyValuePair;
import example.dtc.R;
import rtaservices.RTAVehicleServices.Interfaces.VehicleDetailsClickListener;

public class VehicleRenewalDetailsViewAdapter extends BaseAdapter {

    private String _plate_no;
    private String _plate_source;
    private String _plate_category;
    private String _plate_type;
    private String _expiry_date;
    private ArrayList<CKeyValuePair> _fipdetailsList;
    private Context _mContext;
    private ListView _listView;
    ViewHolder viewHolder;
    static private VehicleDetailsClickListener vehicleDetailsClickListener;


    // View lookup cache
    private static class ViewHolder {
        public TextView tv_plate_no;
        public TextView tv_plate_source;
        public TextView tv_plate_category;
        public TextView tv_plate_type;
        public TextView tv_expiry_date;
        public ImageView iv_Details;
    }

    public VehicleRenewalDetailsViewAdapter(Context context, String plate_no, String plate_source, String plate_category,
                                            String plate_type, String expiry_date, ArrayList<CKeyValuePair> fipdetailsList, ListView mlistView) {
        _plate_no = plate_no;
        _plate_source = plate_source;
        _plate_category = plate_category;
        _plate_type = plate_type;
        _expiry_date = expiry_date;
        _fipdetailsList = fipdetailsList;
        _listView = mlistView;
        _mContext = context;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return 1;
    }

    public void setvehicleRenewalDetailsMethodCallBack(VehicleDetailsClickListener CallBack) {
        this.vehicleDetailsClickListener = CallBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new VehicleRenewalDetailsViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(_mContext);
            convertView = inflater.inflate(R.layout.rtavehicle_renewal_customize, parent, false);
            viewHolder.tv_plate_no = convertView.findViewById(R.id.txtPlateNo);
            viewHolder.tv_plate_source = convertView.findViewById(R.id.txtPlateSource);
            viewHolder.tv_plate_type = convertView.findViewById(R.id.txtPlateType);
            viewHolder.tv_expiry_date = convertView.findViewById(R.id.txtExpiryDate);
            viewHolder.tv_plate_category = convertView.findViewById(R.id.txtPlateCategory);
            viewHolder.iv_Details = convertView.findViewById(R.id.iv_details);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.tv_plate_no.setText(_plate_no);
        viewHolder.tv_plate_source.setText(_plate_source);
        viewHolder.tv_plate_type.setText(_plate_type);
        viewHolder.tv_expiry_date.setText(_expiry_date);
        viewHolder.tv_plate_category.setText(_plate_category);
        viewHolder.iv_Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vehicleDetailsClickListener != null) {
                    vehicleDetailsClickListener.VehicleDetailsCallBackMethod(position);
                }
            }
        });

        return convertView;
    }
}
