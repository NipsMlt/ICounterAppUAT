package rtaservices.RTAVehicleServices.Classes;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import RTANetworking.Common.LocaleHelper;
import RTANetworking.RequestAndResponse.KskServiceItem;
import example.dtc.R;
import interfaces.ResetTimerListener;
import utility.Constant;
import utility.PreferenceConnector;

import static RTANetworking.Common.Utilities.getProperServiceDate;
import static RTANetworking.Common.Utilities.getProperServiceDateYear;
import static RTANetworking.Common.Utilities.getVehicleCarNameOrColor;
import static RTANetworking.Common.Utilities.getVehicleinsuranceExpiry;
import static utility.Common.getFormattedDate;

public class RTAVehicleServices_VehicleDetailsDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    Fragment mFragment;
    ImageView iv_Closefipdetails;
    TextView tv_vs_vi_Plateno, tv_vs_vi_Platetype, tv_vs_vi_Chassisno, tv_vs_vi_Doorsno, tv_vs_vi_Regexpiry,
            tv_vs_vi_insuranceCompany, tv_vs_vi_Vehiclebrand, tv_vs_vi_Platecode, tv_vs_vi_Platesource, tv_vs_vi_Carmodel,
            tv_vs_vi_Seatno, tv_vs_vi_Insuranceexpiry, tv_vs_vi_Insurancetype, tv_vs_vi_Vehiclecolor, tvrtaServiceTitle,
            tv_txtvs_vi_plateNo, tv_txtvs_vi_plateType, tv_txtvs_vi_chassisNo, tv_txtvs_vi_doorsNo, tv_txtvs_vi_regExpiry,
            tv_txtvs_vi_insuranceCompany, tv_txtvs_vi_vehicleBrand, tv_txtvs_vi_plateCode, tv_txtvs_vi_plateSource,
            tv_txtvs_vi_carModel, tv_txtvs_vi_seatNo, tv_txtvs_vi_insuranceExpiry, tv_txtvs_vi_insuranceType, tv_txtvs_vi_vehicleColor;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private int listPosition;
    private double TotalAmount;
    private String PlateCode;
    static private ResetTimerListener mMethodCallBack;
    private String Language;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Language = PreferenceConnector.readString(getContext(), Constant.Language, "");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        fipdetailsItemsList = bundle.getParcelableArrayList(Constant.FIPServiceTypeItemsArrayList);
        listPosition = bundle.getInt(Constant.FIPDetailsListPosition);
        AlertDialog.Builder customDialogMain = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
        context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rtaservices_vehicleservices_vehicledetails, null);
        PlateCode = PreferenceConnector.readString(getContext(), Constant.PlateCode, null);

        iv_Closefipdetails = (ImageView) view.findViewById(R.id.iv_closefipdetails);
        tv_vs_vi_Plateno = (TextView) view.findViewById(R.id.tv_vs_vi_plateno);
        tv_vs_vi_Platetype = (TextView) view.findViewById(R.id.tv_vs_vi_platetype);
        tv_vs_vi_Chassisno = (TextView) view.findViewById(R.id.tv_vs_vi_chassisno);
        tv_vs_vi_Doorsno = (TextView) view.findViewById(R.id.tv_vs_vi_doorsno);
        tv_vs_vi_Regexpiry = (TextView) view.findViewById(R.id.tv_vs_vi_regexpiry);
        tv_vs_vi_insuranceCompany = (TextView) view.findViewById(R.id.tv_vs_vi_insurancecompany);
        tv_vs_vi_Vehiclebrand = (TextView) view.findViewById(R.id.tv_vs_vi_vehiclebrand);
        tv_vs_vi_Platecode = (TextView) view.findViewById(R.id.tv_vs_vi_platecode);
        tv_vs_vi_Platesource = (TextView) view.findViewById(R.id.tv_vs_vi_platesource);
        tv_vs_vi_Carmodel = (TextView) view.findViewById(R.id.tv_vs_vi_carmodel);
        tv_vs_vi_Seatno = (TextView) view.findViewById(R.id.tv_vs_vi_seatno);
        tv_vs_vi_Insuranceexpiry = (TextView) view.findViewById(R.id.tv_vs_vi_insuranceexpiry);
        tv_vs_vi_Insurancetype = (TextView) view.findViewById(R.id.tv_vs_vi_insurancetype);
        tv_vs_vi_Vehiclecolor = (TextView) view.findViewById(R.id.tv_vs_vi_vehiclecolor);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);

        //Text of all fields
        tv_txtvs_vi_plateNo = (TextView) view.findViewById(R.id.tv_txtvs_vi_plateno);
        tv_txtvs_vi_plateType = (TextView) view.findViewById(R.id.tv_txtvs_vi_platetype);
        tv_txtvs_vi_chassisNo = (TextView) view.findViewById(R.id.tv_txtvs_vi_chassisno);
        tv_txtvs_vi_doorsNo = (TextView) view.findViewById(R.id.tv_txtvs_vi_doorsno);
        tv_txtvs_vi_regExpiry = (TextView) view.findViewById(R.id.tv_txtvs_vi_regexpiry);
        tv_txtvs_vi_insuranceCompany = (TextView) view.findViewById(R.id.tv_txtvs_vi_insurancecompany);
        tv_txtvs_vi_vehicleBrand = (TextView) view.findViewById(R.id.tv_txtvs_vi_vehiclebrand);
        tv_txtvs_vi_plateCode = (TextView) view.findViewById(R.id.tv_txtvs_vi_platecode);
        tv_txtvs_vi_plateSource = (TextView) view.findViewById(R.id.tv_txtvs_vi_platesource);
        tv_txtvs_vi_carModel = (TextView) view.findViewById(R.id.tv_txtvs_vi_carmodel);
        tv_txtvs_vi_seatNo = (TextView) view.findViewById(R.id.tv_txtvs_vi_seatno);
        tv_txtvs_vi_insuranceExpiry = (TextView) view.findViewById(R.id.tv_txtvs_vi_insuranceexpiry);
        tv_txtvs_vi_insuranceType = (TextView) view.findViewById(R.id.tv_txtvs_vi_insurancetype);
        tv_txtvs_vi_vehicleColor = (TextView) view.findViewById(R.id.tv_txtvs_vi_vehiclecolor);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);


        //Setting view for Custom Dialog
        customDialogMain.setView(view);
        //Show dialog
        dialog = customDialogMain.show();

        tv_vs_vi_Plateno.setText(fipdetailsItemsList.get(listPosition).getItemId());
        tv_vs_vi_Platetype.setText(fipdetailsItemsList.get(listPosition).getField3());
        tv_vs_vi_Chassisno.setText(fipdetailsItemsList.get(listPosition).getField6());
        tv_vs_vi_Doorsno.setText(String.valueOf((int) fipdetailsItemsList.get(listPosition).getMaximumAmount()));
        tv_vs_vi_Regexpiry.setText(getProperServiceDate(fipdetailsItemsList.get(listPosition).getServiceDate()));
        tv_vs_vi_insuranceCompany.setText(fipdetailsItemsList.get(listPosition).getLocation());
        tv_vs_vi_Vehiclebrand.setText(getVehicleCarNameOrColor(fipdetailsItemsList.get(listPosition).getField4(), Constant.CarName));
        tv_vs_vi_Platecode.setText(PlateCode);
        tv_vs_vi_Platesource.setText(fipdetailsItemsList.get(listPosition).getField7());
        tv_vs_vi_Carmodel.setText(fipdetailsItemsList.get(listPosition).getPaymentFlag());
        tv_vs_vi_Seatno.setText(String.valueOf((int) fipdetailsItemsList.get(listPosition).getMinimumAmount()));
        tv_vs_vi_Insuranceexpiry.setText(getVehicleinsuranceExpiry(fipdetailsItemsList.get(listPosition).getIsSelected()));
        tv_vs_vi_Insurancetype.setText(fipdetailsItemsList.get(listPosition).getField5());
        tv_vs_vi_Vehiclecolor.setText(getVehicleCarNameOrColor(fipdetailsItemsList.get(listPosition).getField4(), Constant.CarColor));

        iv_Closefipdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMethodCallBack != null) {
                    mMethodCallBack.ResetTimerCallBackMethod();
                }
                dialog.dismiss();
            }
        });

        return dialog;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        tv_txtvs_vi_plateNo.setText(resources.getString(R.string.PlateNumber));
        tv_txtvs_vi_plateType.setText(resources.getString(R.string.Platetype));
        tv_txtvs_vi_chassisNo.setText(resources.getString(R.string.chassisno));
        tv_txtvs_vi_doorsNo.setText(resources.getString(R.string.doorno));
        tv_txtvs_vi_regExpiry.setText(resources.getString(R.string.registrationexpiry));
        tv_txtvs_vi_insuranceCompany.setText(resources.getString(R.string.insurancecompany));
        tv_txtvs_vi_vehicleBrand.setText(resources.getString(R.string.vehiclebrand));
        tv_txtvs_vi_plateCode.setText(resources.getString(R.string.platecode));
        tv_txtvs_vi_plateSource.setText(resources.getString(R.string.platesource));
        tv_txtvs_vi_carModel.setText(resources.getString(R.string.carmodel));
        tv_txtvs_vi_insuranceExpiry.setText(resources.getString(R.string.insuranceexpiry));
        tv_txtvs_vi_seatNo.setText(resources.getString(R.string.seatsnumber));
        tv_txtvs_vi_insuranceType.setText(resources.getString(R.string.insurancetype));
        tv_txtvs_vi_vehicleColor.setText(resources.getString(R.string.vehiclecolor));
        tvrtaServiceTitle.setText(resources.getString(R.string.vehicleinformation));
    }

    //Reset Timer call back method
    public void setMethodCallBack(ResetTimerListener CallBack) {
        this.mMethodCallBack = CallBack;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}