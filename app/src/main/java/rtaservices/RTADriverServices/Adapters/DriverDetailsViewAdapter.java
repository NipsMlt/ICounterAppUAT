package rtaservices.RTADriverServices.Adapters;

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

public class DriverDetailsViewAdapter extends BaseAdapter {

    private String _licenseNo;
    private String _expiryDate;
    private String _plateSource;
    private String _ticketNo;
    private String _birthYear;
    private ArrayList<CKeyValuePair> _fipdetailsList;
    private Context _mContext;
    private ListView _listView;
    ViewHolder viewHolder;
    static private VehicleDetailsClickListener vehicleDetailsClickListener;


    // View lookup cache
    private static class ViewHolder {
        public TextView tv_licenseNo;
        public TextView tv_expiryDate;
        public TextView tv_plateSource;
        public TextView tv_ticketNo;
        public TextView tv_birthYear;
        public ImageView iv_Details;
    }

    public DriverDetailsViewAdapter(Context context, String licenseNo, String expiryDate, String plateSource,
                                    String ticketNo, String birthYear, ArrayList<CKeyValuePair> fipdetailsList, ListView mlistView) {
        _licenseNo = licenseNo;
        _expiryDate = expiryDate;
        _plateSource = plateSource;
        _ticketNo = ticketNo;
        _birthYear = birthYear;
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
            viewHolder = new DriverDetailsViewAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(_mContext);
            convertView = inflater.inflate(R.layout.rtadriver_customize, parent, false);
            viewHolder.tv_licenseNo = convertView.findViewById(R.id.txtLicenseNo);
            viewHolder.tv_expiryDate = convertView.findViewById(R.id.txtExpiryDate);
            viewHolder.tv_plateSource = convertView.findViewById(R.id.txtPlateSource);
            viewHolder.tv_ticketNo = convertView.findViewById(R.id.txtTicketNo);
            viewHolder.tv_birthYear = convertView.findViewById(R.id.txtBirthYear);
            viewHolder.iv_Details = convertView.findViewById(R.id.iv_details);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.tv_licenseNo.setText(_licenseNo);
        viewHolder.tv_expiryDate.setText(_expiryDate);
        viewHolder.tv_plateSource.setText(_plateSource);
        viewHolder.tv_ticketNo.setText(_ticketNo);
        viewHolder.tv_birthYear.setText(_birthYear);
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
