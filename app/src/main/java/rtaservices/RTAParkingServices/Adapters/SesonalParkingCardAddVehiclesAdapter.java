package rtaservices.RTAParkingServices.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import RTANetworking.Common.Utilities;
import RTANetworking.RequestAndResponse.KskServiceItem;
import example.dtc.R;
import rtaservices.RTAParkingServices.Interfaces.DeleteVehicleClickListener;

public class SesonalParkingCardAddVehiclesAdapter extends BaseAdapter {

    private String _PlateNo;
    private String _PlateCategory;
    private String _PlateSource;
    private String _PlateCode;
    private String _TrafficFileNo;
    private String _CarModel;
    private Context _mContext;
    private ListView _listView;
    ViewHolder viewHolder;
    private ArrayList<KskServiceItem> ps_SPCvdetailsItemsList = new ArrayList<>();
    private DeleteVehicleClickListener deleteVehicleClickListener;


    // View lookup cache
    private static class ViewHolder {
        public TextView tv_rtaps_spc_Plateno;
        public TextView tv_rtaps_spc_Platecode;
        public TextView tv_rtaps_spc_Platecategory;
        public TextView tv_rtaps_spc_Platesource;
        public TextView tv_rtaps_spc_Trfno;
        public TextView tv_rtaps_spc_Carmodel;
        public Button btn_ps_spc_Delete;
    }

    public SesonalParkingCardAddVehiclesAdapter(Context context,ArrayList<KskServiceItem> ps_spcvdetailsItemsList ) {
        _mContext = context;
        ps_SPCvdetailsItemsList=ps_spcvdetailsItemsList;
    }

    @Override
    public Object getItem(int i) {
        return ps_SPCvdetailsItemsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return ps_SPCvdetailsItemsList.size();
    }

    public void setDeleteVehicleClickListener(DeleteVehicleClickListener CallBack) {
        this.deleteVehicleClickListener = CallBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            viewHolder = new SesonalParkingCardAddVehiclesAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(_mContext);
            convertView = inflater.inflate(R.layout.rtaparkingservices_customize, parent, false);
            viewHolder.tv_rtaps_spc_Plateno = convertView.findViewById(R.id.tv_rtaps_spc_plateno);
            viewHolder.tv_rtaps_spc_Platecode = convertView.findViewById(R.id.tv_rtaps_spc_platecode);
            viewHolder.tv_rtaps_spc_Platecategory = convertView.findViewById(R.id.tv_rtaps_spc_platecategory);
            viewHolder.tv_rtaps_spc_Platesource = convertView.findViewById(R.id.tv_rtaps_spc_platesource);
            viewHolder.tv_rtaps_spc_Trfno = convertView.findViewById(R.id.tv_rtaps_spc_trfno);
            viewHolder.tv_rtaps_spc_Carmodel = convertView.findViewById(R.id.tv_rtaps_spc_carmodel);
            viewHolder.btn_ps_spc_Delete = convertView.findViewById(R.id.btn_ps_spc_delete);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        _PlateNo = ps_SPCvdetailsItemsList.get(position).getItemId();
        _PlateCategory = ps_SPCvdetailsItemsList.get(position).getField3();
        _PlateSource = Utilities.getCityNameFromStateCodeUpperCase(ps_SPCvdetailsItemsList.get(position).getField7());
        _PlateCode = ps_SPCvdetailsItemsList.get(position).getField2();
        _TrafficFileNo = ps_SPCvdetailsItemsList.get(position).getItemText();
        _CarModel = ps_SPCvdetailsItemsList.get(position).getField4();

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.tv_rtaps_spc_Plateno.setText(_PlateNo);
        viewHolder.tv_rtaps_spc_Platecode.setText(_PlateCode);
        viewHolder.tv_rtaps_spc_Platecategory.setText(_PlateCategory);
        viewHolder.tv_rtaps_spc_Platesource.setText(_PlateSource);
        viewHolder.tv_rtaps_spc_Trfno.setText(_TrafficFileNo);
        viewHolder.tv_rtaps_spc_Carmodel.setText(_CarModel);

        viewHolder.btn_ps_spc_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ps_SPCvdetailsItemsList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
