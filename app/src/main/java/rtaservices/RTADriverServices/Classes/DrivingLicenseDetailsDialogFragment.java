package rtaservices.RTADriverServices.Classes;

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

import static RTANetworking.Common.Utilities.getCityNameFromStateCodeUpperCase;
import static RTANetworking.Common.Utilities.getDriverLicenseType;
import static RTANetworking.Common.Utilities.getIssueDate;
import static RTANetworking.Common.Utilities.getLicenseClass;
import static RTANetworking.Common.Utilities.getLicenseStatus;
import static RTANetworking.Common.Utilities.getProperExpiryDate;
import static RTANetworking.Common.Utilities.getProperServiceDate;

public class DrivingLicenseDetailsDialogFragment extends DialogFragment {

    private AlertDialog dialog;
    Context context;
    Fragment mFragment;
    ImageView iv_Closefipdetails;
    TextView tvrtaServiceTitle, tv_ds_dli_Licenseno, tv_ds_dli_Licensetype, tv_ds_dli_IssueDate, tv_ds_dli_Name,
            tv_ds_dli_Licensesource, tv_ds_dli_Licenseclass, tv_ds_dli_Expirydate, tv_ds_dli_Licensestatus, tv_txtds_dli_licenseNo,
            tv_txtds_dli_licenseType, tv_txtds_dli_issueDate, tv_txtds_dli_Name, tv_txtds_dli_licenseSource, tv_txtds_dli_licenseClass,
            tv_txtds_dli_expiryDate, tv_txtds_dli_licenseStatus;
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
        View view = inflater.inflate(R.layout.rta_drivingservices_drivinglicensedetails, null);

        iv_Closefipdetails = (ImageView) view.findViewById(R.id.iv_closefipdetails);
        tv_ds_dli_Licenseno = (TextView) view.findViewById(R.id.tv_ds_dli_licenseno);
        tv_ds_dli_Licensetype = (TextView) view.findViewById(R.id.tv_ds_dli_licensetype);
        tv_ds_dli_IssueDate = (TextView) view.findViewById(R.id.tv_ds_dli_issuedate);
        tv_ds_dli_Name = (TextView) view.findViewById(R.id.tv_ds_dli_name);
        tv_ds_dli_Licensesource = (TextView) view.findViewById(R.id.tv_ds_dli_licensesource);
        tv_ds_dli_Licenseclass = (TextView) view.findViewById(R.id.tv_ds_dli_licenseclass);
        tv_ds_dli_Expirydate = (TextView) view.findViewById(R.id.tv_ds_dli_expirydate);
        tv_ds_dli_Licensestatus = (TextView) view.findViewById(R.id.tv_ds_dli_licensestatus);

        //Text for all fields
        tv_txtds_dli_licenseNo = (TextView) view.findViewById(R.id.tv_txtds_dli_licenseno);
        tv_txtds_dli_licenseType = (TextView) view.findViewById(R.id.tv_txtds_dli_licensetype);
        tv_txtds_dli_issueDate = (TextView) view.findViewById(R.id.tv_txtds_dli_issuedate);
        tv_txtds_dli_Name = (TextView) view.findViewById(R.id.tv_txtds_dli_name);
        tv_txtds_dli_licenseSource = (TextView) view.findViewById(R.id.tv_txtds_dli_licensesource);
        tv_txtds_dli_licenseClass = (TextView) view.findViewById(R.id.tv_txtds_dli_licenseclass);
        tv_txtds_dli_expiryDate = (TextView) view.findViewById(R.id.tv_txtds_dli_expirydate);
        tv_txtds_dli_licenseStatus = (TextView) view.findViewById(R.id.tv_txtds_dli_licensestatus);
        tvrtaServiceTitle = (TextView) view.findViewById(R.id.tvrtaservice);

        //Call the function to check which language code is it and change it accordingly
        ChangeLanguage(Language);

        //Setting view for Custom Dialog
        customDialogMain.setView(view);
        //Show dialog
        dialog = customDialogMain.show();

        TotalAmount = fipdetailsItemsList.get(listPosition).getServiceCharge() + fipdetailsItemsList.get(listPosition).getDiscountRate() +
                fipdetailsItemsList.get(listPosition).getItemPaidAmount();
        tv_ds_dli_Licenseno.setText(fipdetailsItemsList.get(listPosition).getItemId());
        tv_ds_dli_Licensetype.setText(getDriverLicenseType(fipdetailsItemsList.get(listPosition).getField7()));
        tv_ds_dli_IssueDate.setText(getIssueDate(fipdetailsItemsList.get(listPosition).getField1()));
        tv_ds_dli_Name.setText(fipdetailsItemsList.get(listPosition).getField4());
        tv_ds_dli_Licensesource.setText(getCityNameFromStateCodeUpperCase(fipdetailsItemsList.get(listPosition).getPaymentFlag()));
        tv_ds_dli_Licenseclass.setText(getLicenseClass(fipdetailsItemsList.get(listPosition).getField3()));
        tv_ds_dli_Expirydate.setText(getProperServiceDate(fipdetailsItemsList.get(listPosition).getServiceDate()));
        tv_ds_dli_Licensestatus.setText(getLicenseStatus(fipdetailsItemsList.get(listPosition).getField6()));

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

        tv_txtds_dli_licenseNo.setText(resources.getString(R.string.LicenseNumber));
        tv_txtds_dli_licenseType.setText(resources.getString(R.string.LicenseType));
        tv_txtds_dli_issueDate.setText(resources.getString(R.string.IssueDate));
        tv_txtds_dli_Name.setText(resources.getString(R.string.Name));
        tv_txtds_dli_licenseSource.setText(resources.getString(R.string.Placeofissue));
        tv_txtds_dli_licenseClass.setText(resources.getString(R.string.LicenseClass));
        tv_txtds_dli_expiryDate.setText(resources.getString(R.string.ExpiryDate));
        tv_txtds_dli_licenseStatus.setText(resources.getString(R.string.LicenseStatus));
        tvrtaServiceTitle.setText(resources.getString(R.string.DrivingLicenseInformation));
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