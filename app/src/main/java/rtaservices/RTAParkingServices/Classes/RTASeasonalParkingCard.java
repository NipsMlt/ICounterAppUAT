package rtaservices.RTAParkingServices.Classes;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.Utilities;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.KskServiceTypeResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponse;
import RTANetworking.RequestAndResponse.NPGKskInquiryResponseItem;

import example.dtc.R;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.PreferenceConnector;

import static utility.Constant.TAG;

public class RTASeasonalParkingCard extends Fragment implements View.OnClickListener {

    private static KskServiceTypeResponse ServiceType;
    private ArrayList<KskServiceTypeResponse> ServiceTypes = new ArrayList<>();
    private ArrayList<CKeyValuePair> CustomerUniqueNo = new ArrayList<>();
    private Fragment mFragment;
    private Button btn_Back, btn_Info, btn_Home, btn_Seasonalparkingcardaddvehicle;
    private EditText edt_Applicantname, edt_Mobileno, edt_Activationdate;
    private Spinner sp_Cardtype, sp_Cardperiod, sp_Activationdate;
    private String ApplicantName, Mobileno, ActivationDate, CardPeriod, CardType;
    private static NPGKskInquiryResponse InquiryResponse;
    private static NPGKskInquiryResponseItem InquiryResponseItem;
    private ArrayList<KskServiceItem> ServiceItems = new ArrayList<>();
    private TextView tv_timer;
    final Calendar calendar = Calendar.getInstance();
    boolean calendarisOpened = false;
    //Declare timer
    private static CountDownTimer cTimer = null;
    InputMethodManager imm;

    public RTASeasonalParkingCard() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static RTASeasonalParkingCard newInstance() {
        return new RTASeasonalParkingCard();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utilities utilities = new Utilities();
        View view = inflater.inflate(R.layout.rtaparkingservices_seasonal_parking_card, null);
        edt_Applicantname = (EditText) view.findViewById(R.id.edt_applicantname);
        edt_Mobileno = (EditText) view.findViewById(R.id.edt_mobileno);
        sp_Cardtype = (Spinner) view.findViewById(R.id.sp_cardtype);
        btn_Seasonalparkingcardaddvehicle = (Button) view.findViewById(R.id.btn_seasonalparkingcardaddvehicle);
        sp_Cardperiod = (Spinner) view.findViewById(R.id.sp_cardperiod);
        edt_Activationdate = (EditText) view.findViewById(R.id.edt_activationdate);
        btn_Back = (Button) view.findViewById(R.id.btn_rtaseasonalparkingcardback);
        btn_Info = (Button) view.findViewById(R.id.btn_rtaseasonalparkingcardinfo);
        btn_Home = (Button) view.findViewById(R.id.btn_rtaseasonalparkingcardhome);
        tv_timer = (TextView) view.findViewById(R.id.tv_rtamainservices_timer);

        btn_Back.setOnClickListener(this);
        btn_Info.setOnClickListener(this);
        btn_Home.setOnClickListener(this);

        //Automatic keyboard to be hidden on fragment start
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Get Services Name to set the digital pass
        PreferenceConnector.writeString(getContext(), ConfigrationRTA.Service_Name, ConfigrationRTA.Driving_Damaged_Lost);

        try {
            //Work for timer
            cTimer = Common.SetCountDownTimer(cTimer, 60000, 1000, tv_timer, RTAParkingServices.newInstance(), getFragmentManager());
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }

        //Set the time in the edittext
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edt_Activationdate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                if (calendarisOpened == false) {
                    imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        new DatePickerDialog(getContext(), date, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                        //To prevent the calendar to reopen make this boolean true, there is a glitch the calendar will open two at a time
                        calendarisOpened = true;
                    }
                }
                //Restart the timer
                if (cTimer != null)
                    cTimer.start();
                return true;

            }
        });

        edt_Applicantname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        edt_Mobileno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });

        //Timer is getting reset on text changed
        edt_Applicantname.addTextChangedListener(Common.getTextWatcher(cTimer));
        edt_Mobileno.addTextChangedListener(Common.getTextWatcher(cTimer));
        edt_Activationdate.addTextChangedListener(Common.getTextWatcher(cTimer));

        sp_Cardtype.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });
        sp_Cardperiod.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cTimer != null)
                    cTimer.start();
                return false;
            }
        });


        btn_Seasonalparkingcardaddvehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cTimer != null)
                        cTimer.cancel();
                    ApplicantName = edt_Applicantname.getText().toString();
                    Mobileno = edt_Mobileno.getText().toString();
                    ActivationDate = edt_Activationdate.getText().toString();
                    CardType = sp_Cardtype.getSelectedItem().toString();
                    CardPeriod = sp_Cardperiod.getSelectedItem().toString();

                    if (ApplicantName.equals("") || Mobileno.equals("")) {
                        Toast.makeText(getContext(), "Kindly fill all the fields", Toast.LENGTH_LONG).show();
                    } else {
                        mFragment = RTASeasonalParkingCardAddVehicle.newInstance();
                        addFragment();

                    }


                } catch (Exception ex) {
                    if (cTimer != null)
                        cTimer.start();
                    Log.d(TAG, ex.getMessage());
                }
            }
        });

        return view;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edt_Activationdate.setText(sdf.format(calendar.getTime()));
        //Make this boolean false to reopen the calendar
        calendarisOpened = false;
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
                case R.id.btn_rtaseasonalparkingcardback:
                    mFragment = RTAParkingServices.newInstance();
                    addFragment();
                    break;
                case R.id.btn_rtaseasonalparkingcardinfo:
                    break;
                case R.id.btn_rtaseasonalparkingcardhome:
                    mFragment = RTAMain.newInstance();
                    addFragment();
                    break;
            }
        } catch (Exception e) {
            ExceptionService.ExceptionLogService(getContext(), e.getMessage(), getClass().getCanonicalName(), RTAMain.AndroidSerialNo);
        }
    }

}
