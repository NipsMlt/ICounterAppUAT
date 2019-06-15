package rtaservices.RTAParkingServices.Classes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.Utilities;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import rtaservices.RTAParkingServices.Adapters.SesonalParkingCardAddVehiclesAdapter;
import rtaservices.RTAParkingServices.Interfaces.DeleteVehicleClickListener;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

import static utility.Constant.TAG;


public class RTASeasonalParkingCardAddVehicle extends Fragment implements View.OnClickListener, DeleteVehicleClickListener {

    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_ps_spc_Continue, btn_ps_spc_AddVehicle;
    private EditText edt_ps_spc_Plateno;
    private Spinner sp_ps_spc_Platesource, sp_ps_spc_Platecategory, sp_ps_spc_Platecode;
    private String ClassName, Plateno, PlateSource, PlateCode, PlateCategory, ServiceId, ServiceName, BranchID,
            RegistrationNo, OwnerName, TrafficFilenNo, CarModel;
    private ListView lv_spcaddVehicle;
    private SesonalParkingCardAddVehiclesAdapter mAdapter;
    // Response Final Object
    private static NPGKskInquiryResponse resObject;
    private static NPGKskInquiryResponseItem resObjectItem;
    private ArrayList<KskServiceItem> ps_spcvdetailsItemsList = null;
    private ArrayList<KskServiceItem> ps_spcvdetailsItemsListCopy = null;
    private ArrayList<KskServiceItem> ps_vscdetailsItemsList = null;
    CountDownTimer cTimer;
    private TextView tv_timer;
    InputMethodManager imm;
    AlertDialog.Builder builder;
    AlertDialog alert;

    public RTASeasonalParkingCardAddVehicle() {
        // Required empty public constructor
    }

    public static RTASeasonalParkingCardAddVehicle newInstance() {
        return new RTASeasonalParkingCardAddVehicle();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rtaparkingservices_seasonal_parking_card_addvehicle, container, false);

        edt_ps_spc_Plateno = (EditText) view.findViewById(R.id.edt_ps_spc_plateno);
        sp_ps_spc_Platesource = (Spinner) view.findViewById(R.id.sp_ps_spc_platesource);
        sp_ps_spc_Platecategory = (Spinner) view.findViewById(R.id.sp_ps_spc_platecategory);
        sp_ps_spc_Platecode = (Spinner) view.findViewById(R.id.sp_ps_spc_platecode);
        btn_ps_spc_Continue = (Button) view.findViewById(R.id.btn_ps_spc_continue);
        btn_ps_spc_AddVehicle = (Button) view.findViewById(R.id.btn_ps_spc_addvehicle);
        lv_spcaddVehicle = (ListView) view.findViewById(R.id.lv_spc);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);
        btn_Back = (Button) view.findViewById(R.id.btn_rta_ps_spc_back);
        btn_Info = (Button) view.findViewById(R.id.btn_rta_ps_spc_info);
        btn_Home = (Button) view.findViewById(R.id.btn_rta_ps_spc_home);

        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Initialization
        ps_spcvdetailsItemsList = new ArrayList<>();
        ps_vscdetailsItemsList = new ArrayList<>();

        builder = new AlertDialog.Builder(getContext());
        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Vehicle_Renewal);

        edt_ps_spc_Plateno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        //Timer is getting reset on text changed
        edt_ps_spc_Plateno.addTextChangedListener(Common.getTextWatcher(cTimer));

        sp_ps_spc_Platesource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });
        sp_ps_spc_Platecategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });
        sp_ps_spc_Platecode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        btn_ps_spc_AddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                try {
                    if (cTimer != null)
                        cTimer.cancel();
                    Utilities utilities = new Utilities();
                    PlateSource = sp_ps_spc_Platesource.getSelectedItem().toString();
                    PlateCode = sp_ps_spc_Platecode.getSelectedItem().toString();
                    PlateCategory = sp_ps_spc_Platecategory.getSelectedItem().toString();
                    Plateno = edt_ps_spc_Plateno.getText().toString();

                    if (Plateno.equals("")) {
                        if (cTimer != null)
                            cTimer.start();
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                    } else {
                        ServiceId = ConfigrationRTA.SERVICE_ID_SCVDI;
                        ServiceName = ConfigrationRTA.SERVICE_NAME_SCVDI;
                        ServiceLayer.callInquirySeasonalCardVehicleDetails(ServiceId, ServiceName, Plateno, PlateCode, PlateCategory, PlateSource,
                                new ServiceCallback() {
                                    @Override
                                    public void onSuccess(JSONObject obj) throws JSONException {
                                        if (cTimer != null)
                                            cTimer.cancel();
                                        Gson gson = new Gson();
                                        String reasonCode = null;
                                        try {
                                            reasonCode = obj.getString("ReasonCode");
                                            if (!(reasonCode.equals("0000"))) {
                                                //Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                builder.setMessage(obj.getString("Message"))
                                                        .setCancelable(false)
                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                //do things
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                        }

                                        // Check the object or Array Items
                                        JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                        if (dataObject != null) {

                                            //Do things with object.
                                            resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);


                                        } else {

                                            //Do things with array
                                            resObject = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);

                                        }

                                        // This is Array of Items
                                        if (resObject != null) {

                                            for (int i = 0; i < resObject.ServiceType.getItems().size(); i++)
                                                ps_spcvdetailsItemsList.add(resObject.getServiceType().getItems().get(i));

                                            Log.d("Service Response", resObject.Message);

                                            // This is object of Items
                                        } else {
                                            ps_spcvdetailsItemsList.add(resObjectItem.getServiceType().getItems());
                                        }
                                        if (cTimer != null)
                                            cTimer.cancel();
                                       /* //Writing it in the cache to get it anywhere
                                        try {
                                            Common.writeObject(getContext(), Constant.SeasonalCardVehicledetailsItemsList, ps_spcvdetailsItemsList);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/

                                        //Assign values to plates and further to call the Validate Seasonal Card service
                                        for (int i = 0; i < ps_spcvdetailsItemsList.size(); i++) {
                                            Plateno = ps_spcvdetailsItemsList.get(i).getItemId();
                                            PlateCode = ps_spcvdetailsItemsList.get(i).getField2();
                                            PlateCategory = ps_spcvdetailsItemsList.get(i).getField3();
                                            PlateSource = Utilities.getCityNameFromStateCodeUpperCase(ps_spcvdetailsItemsList.get(i).getField7());
                                            RegistrationNo = ps_spcvdetailsItemsList.get(i).getItemText();
                                            OwnerName = ps_spcvdetailsItemsList.get(i).getPaymentFlag();
                                        }
                                        //Check if one of the plate code and plate no is same then show that vehicle is added

                                        BranchID = ConfigrationRTA.BRANCH_ID_VSCI;
                                        ServiceId = ConfigrationRTA.SERVICE_ID_VSCI;
                                        ServiceName = ConfigrationRTA.SERVICE_NAME_VSCI;
                                        ServiceLayer.callInquiryValidateSeasonalCard(ServiceId, ServiceName, Plateno, PlateCode, PlateCategory, PlateSource,
                                                BranchID, RegistrationNo, OwnerName,
                                                new ServiceCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject obj) throws JSONException {
                                                        if (cTimer != null)
                                                            cTimer.cancel();
                                                        Gson gson = new Gson();

                                                        String reasonCode = null;
                                                        try {
                                                            reasonCode = obj.getString("ReasonCode");
                                                            if (!(reasonCode.equals("0000"))) {
                                                                //Toast.makeText(getContext(), String.valueOf(obj.getString("Message")), Toast.LENGTH_LONG).show();
                                                                builder.setMessage(obj.getString("Message"))
                                                                        .setCancelable(false)
                                                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                //do things
                                                                            }
                                                                        });
                                                                alert = builder.create();
                                                                alert.show();
                                                                return;
                                                            }
                                                        } catch (JSONException e) {
                                                            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                                                        }

                                                        // Check the object or Array Items
                                                        JSONObject dataObject = obj.optJSONObject("ServiceType").optJSONObject("Items");

                                                        if (dataObject != null) {

                                                            //Do things with object.
                                                            resObjectItem = gson.fromJson(obj.toString(), NPGKskInquiryResponseItem.class);


                                                        } else {

                                                            //Do things with array
                                                            resObject = gson.fromJson(obj.toString(), NPGKskInquiryResponse.class);

                                                        }

                                                        // This is Array of Items
                                                        if (resObject != null) {

                                                            for (int i = 0; i < resObject.ServiceType.getItems().size(); i++)
                                                                ps_vscdetailsItemsList.add(resObject.getServiceType().getItems().get(i));

                                                            Log.d("Service Response", resObject.Message);

                                                            // This is object of Items
                                                        } else {
                                                            ps_vscdetailsItemsList.add(resObjectItem.getServiceType().getItems());
                                                        }
                                                        if (cTimer != null)
                                                            cTimer.cancel();
                                                        //Writing it in the cache to get it anywhere
                                                        try {
                                                            Common.writeObject(getContext(), Constant.VAlidateSeasonalCarddetailsItemsList, ps_vscdetailsItemsList);
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                       /* for (int i = 0; i < ps_spcvdetailsItemsList.size(); i++) {
                                                            if (PlateCode.equals(ps_spcvdetailsItemsList.get(i).getField2())) {
                                                                if (Plateno.equals(ps_spcvdetailsItemsList.get(i).getItemId())) {
                                                                    builder.setMessage("The Vehicle is already added")
                                                                            .setCancelable(false)
                                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    //do things
                                                                                }
                                                                            });
                                                                    AlertDialog alert = builder.create();
                                                                    alert.show();
                                                                } else {
                                                                }
                                                            }
                                                        }*/
                                                        for (int i = 0; i < ps_vscdetailsItemsList.size(); i++) {
                                                            if (ps_spcvdetailsItemsList.size() > 1) {
                                                                if (PlateCode.equals(ps_spcvdetailsItemsList.get(i).getField2())) {
                                                                    if (Plateno.equals(ps_spcvdetailsItemsList.get(i).getItemId())) {
                                                                        //if (("False").equals(ps_vscdetailsItemsList.get(i).getItemId()) || ("false").equals(ps_vscdetailsItemsList.get(i).getItemId())) {
                                                                        builder.setMessage("Vehicle is already added")
                                                                                .setCancelable(false)
                                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                                    public void onClick(DialogInterface dialog, int id) {
                                                                                        //do things
                                                                                    }
                                                                                });
                                                                        alert = builder.create();
                                                                        alert.show();
                                                                        //} else {

                                                                        //}
                                                                    } else {
                                                                        mAdapter = new SesonalParkingCardAddVehiclesAdapter(getContext(), ps_spcvdetailsItemsList);
                                                                        lv_spcaddVehicle.setAdapter(mAdapter);
                                                                        mAdapter.notifyDataSetChanged();
                                                                        //Set the call back method
                                                                        mAdapter.setDeleteVehicleClickListener(RTASeasonalParkingCardAddVehicle.this);
                                                                    }
                                                                } else {
                                                                  /*  mAdapter = new SesonalParkingCardAddVehiclesAdapter(getContext(), ps_spcvdetailsItemsList);
                                                                    lv_spcaddVehicle.setAdapter(mAdapter);
                                                                    mAdapter.notifyDataSetChanged();
                                                                    //Set the call back method
                                                                    mAdapter.setDeleteVehicleClickListener(RTASeasonalParkingCardAddVehicle.this);*/
                                                                }
                                                            } else {
                                                                mAdapter = new SesonalParkingCardAddVehiclesAdapter(getContext(), ps_spcvdetailsItemsList);
                                                                lv_spcaddVehicle.setAdapter(mAdapter);
                                                                mAdapter.notifyDataSetChanged();
                                                                //Set the call back method
                                                                mAdapter.setDeleteVehicleClickListener(RTASeasonalParkingCardAddVehicle.this);
                                                            }
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(String obj) {

                                                    }
                                                }, getContext());

                                    }

                                    @Override
                                    public void onFailure(String obj) {

                                    }
                                }, getContext());

                    }

                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        });

        btn_ps_spc_Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cTimer != null)
                        cTimer.cancel();

                } catch (Exception ex) {
                    Log.d(TAG, ex.getMessage());
                }
            }
        });

        //Work for timer
        cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTASeasonalParkingCard.newInstance(), getFragmentManager());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        try {
            if (cTimer != null)
                cTimer.cancel();
            switch (v.getId()) {
                case R.id.btn_rta_ps_spc_back:
                    mFragment = RTASeasonalParkingCard.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rta_ps_spc_info:
                    break;
                case R.id.btn_rta_ps_spc_home:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

    }

    @Override
    public void DeleteVehicleCallBackMethod(int Position) {
        lv_spcaddVehicle.removeViewAt(Position);
        mAdapter.notifyDataSetChanged();

    }
}
