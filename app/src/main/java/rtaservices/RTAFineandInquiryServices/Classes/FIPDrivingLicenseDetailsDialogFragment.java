package rtaservices.RTAFineandInquiryServices.Classes;

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

import static RTANetworking.Common.Utilities.getFinePayableorUnpayable;
import static RTANetworking.Common.Utilities.getProperServiceDate;
import static RTANetworking.Common.Utilities.getProperServiceDateYear;
import static RTANetworking.Common.Utilities.getTimeFine;
import static utility.Common.getFormattedDate;
import static utility.Common.getFormattedYear;

public class FIPDrivingLicenseDetailsDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    Fragment mFragment;
    ImageView iv_Closefipdetails;
    TextView tv_detailsFineno, tv_detailsfineSource, tv_detailsTime, tv_detailsplateCategory, tv_detailsVehiclespeed,
            tv_detailstotalAmount, tv_detailsLocation, tv_detailsViolations, tv_detailsFineyear, tv_detailsDate,
            tv_detailsfineStatus, tv_detailsplateNumber, tv_detailsplateCode, tv_detailsplateSource, tv_txtdetailsfineNo, tv_txtdetailsfineSource, tv_txtdetailsTime, tv_txtdetailsplateCategory, tv_txtdetailsvehicleSpeed, tv_txtdetailstotalAmount, tv_txtdetailsLocation, tv_txtdetailsViolations, tv_txtdetailsfineYear, tv_txtdetailsDate, tv_txtdetailsfineStatus, tv_txtdetailsplateNumber, tv_txtdetailsplateCode, tv_txtdetailsplateSource;
    private ArrayList<KskServiceItem> fipdetailsItemsList = new ArrayList<>();
    private int listPosition;
    private double TotalAmount;
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
        View view = inflater.inflate(R.layout.rtaservicesfipdrivinglicensedetails, null);

        iv_Closefipdetails = (ImageView) view.findViewById(R.id.iv_closefipdetails);
        tv_detailsFineno = (TextView) view.findViewById(R.id.tv_detailsfineno);
        tv_detailsfineSource = (TextView) view.findViewById(R.id.tv_detailsfinesource);
        tv_detailsTime = (TextView) view.findViewById(R.id.tv_detailstime);
        tv_detailsplateCategory = (TextView) view.findViewById(R.id.tv_detailsplatecategory);
        tv_detailsVehiclespeed = (TextView) view.findViewById(R.id.tv_detailsvehiclespeed);
        tv_detailstotalAmount = (TextView) view.findViewById(R.id.tv_detailstotalamount);
        tv_detailsLocation = (TextView) view.findViewById(R.id.tv_detailslocation);
        tv_detailsViolations = (TextView) view.findViewById(R.id.tv_detailsviolations);
        tv_detailsFineyear = (TextView) view.findViewById(R.id.tv_detailsfineyear);
        tv_detailsDate = (TextView) view.findViewById(R.id.tv_detailsdate);
        tv_detailsfineStatus = (TextView) view.findViewById(R.id.tv_detailsfinestatus);
        tv_detailsplateNumber = (TextView) view.findViewById(R.id.tv_detailsplatenumber);
        tv_detailsplateCode = (TextView) view.findViewById(R.id.tv_detailsplatecode);
        tv_detailsplateSource = (TextView) view.findViewById(R.id.tv_detailsplatesource);
        //Text of all fields
        tv_txtdetailsfineNo = (TextView) view.findViewById(R.id.tv_txtdetailsfineno);
        tv_txtdetailsfineSource = (TextView) view.findViewById(R.id.tv_txtdetailsfinesource);
        tv_txtdetailsTime = (TextView) view.findViewById(R.id.tv_txtdetailstime);
        tv_txtdetailsplateCategory = (TextView) view.findViewById(R.id.tv_txtdetailsplatecategory);
        tv_txtdetailsvehicleSpeed = (TextView) view.findViewById(R.id.tv_txtdetailsvehiclespeed);
        tv_txtdetailstotalAmount = (TextView) view.findViewById(R.id.tv_txtdetailstotalamount);
        tv_txtdetailsLocation = (TextView) view.findViewById(R.id.tv_txtdetailslocation);
        tv_txtdetailsViolations = (TextView) view.findViewById(R.id.tv_txtdetailsviolations);
        tv_txtdetailsfineYear = (TextView) view.findViewById(R.id.tv_txtdetailsfineyear);
        tv_txtdetailsDate = (TextView) view.findViewById(R.id.tv_txtdetailsdate);
        tv_txtdetailsfineStatus = (TextView) view.findViewById(R.id.tv_txtdetailsfinestatus);
        tv_txtdetailsplateNumber = (TextView) view.findViewById(R.id.tv_txtdetailsplatenumber);
        tv_txtdetailsplateCode = (TextView) view.findViewById(R.id.tv_txtdetailsplatecode);
        tv_txtdetailsplateSource = (TextView) view.findViewById(R.id.tv_txtdetailsplatesource);

        //Setting view for Custom Dialog
        customDialogMain.setView(view);
        //Show dialog
        dialog = customDialogMain.show();

        TotalAmount = fipdetailsItemsList.get(listPosition).getServiceCharge() + fipdetailsItemsList.get(listPosition).getDiscountRate() +
                fipdetailsItemsList.get(listPosition).getItemPaidAmount();
        tv_detailsFineno.setText(fipdetailsItemsList.get(listPosition).getItemText());
        tv_detailsfineSource.setText(fipdetailsItemsList.get(listPosition).getField3());
        tv_detailsTime.setText(getTimeFine(fipdetailsItemsList.get(listPosition).getServiceDate()));
        tv_detailsplateCategory.setText(fipdetailsItemsList.get(listPosition).getField5());
        tv_detailsVehiclespeed.setText(fipdetailsItemsList.get(listPosition).getField1());
        tv_detailstotalAmount.setText(String.valueOf(TotalAmount));
        tv_detailsLocation.setText(fipdetailsItemsList.get(listPosition).getLocation());
        tv_detailsViolations.setText("");
        tv_detailsFineyear.setText(getProperServiceDateYear(fipdetailsItemsList.get(listPosition).getServiceDate()));
        tv_detailsDate.setText(getProperServiceDate(fipdetailsItemsList.get(listPosition).getServiceDate()));
        tv_detailsfineStatus.setText(getFinePayableorUnpayable(fipdetailsItemsList.get(listPosition).getField7()));
        tv_detailsplateNumber.setText(fipdetailsItemsList.get(listPosition).getItemId());
        tv_detailsplateCode.setText(fipdetailsItemsList.get(listPosition).getField4());
        tv_detailsplateSource.setText(fipdetailsItemsList.get(listPosition).getField6());

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);


        iv_Closefipdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Call the call back method to restart the timer when the dialog is closed
                if (mMethodCallBack != null) {
                    mMethodCallBack.ResetTimerCallBackMethod();
                }
            }
        });

        return dialog;
    }

    //Change the language with the help of language code
    private void ChangeLanguage(String languageCode) {
        Context context = LocaleHelper.setLocale(getContext(), languageCode);
        Resources resources = context.getResources();

        tv_txtdetailsfineNo.setText(resources.getString(R.string.FineNumber));
        tv_txtdetailsfineSource.setText(resources.getString(R.string.FineSource));
        tv_txtdetailsTime.setText(resources.getString(R.string.Time));
        tv_txtdetailsplateCategory.setText(resources.getString(R.string.Platecategory));
        tv_txtdetailsvehicleSpeed.setText(resources.getString(R.string.VehicleSpeed));
        tv_txtdetailstotalAmount.setText(resources.getString(R.string.TotalAmount));
        tv_txtdetailsLocation.setText(resources.getString(R.string.Location));
        tv_txtdetailsViolations.setText(resources.getString(R.string.Violations));
        tv_txtdetailsfineYear.setText(resources.getString(R.string.FineYear));
        tv_txtdetailsDate.setText(resources.getString(R.string.Date));
        tv_txtdetailsfineStatus.setText(resources.getString(R.string.FineStatus));
        tv_txtdetailsplateNumber.setText(resources.getString(R.string.PlateNumber));
        tv_txtdetailsplateCode.setText(resources.getString(R.string.Platecode));
        tv_txtdetailsplateSource.setText(resources.getString(R.string.PlateSource));

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