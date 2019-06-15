package rtamain;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import example.dtc.R;
import rtaservices.RTAMainServices;

public class RTAandOtherServices extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private ImageView btn_RTA, btn_DubaiEco, btn_EmiratesVGate, btn_LandDept;

    public RTAandOtherServices() {
        // Required empty public constructor
    }

    @NonNull
    public static RTAandOtherServices newInstance() {
        return new RTAandOtherServices();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.rtaandotherservices, container, false);

        btn_RTA = (ImageView) view.findViewById(R.id.btn_rta);
        /*btn_DubaiEco = (ImageView) view.findViewById(R.id.btn_dubaieconomy);
        btn_EmiratesVGate = (ImageView) view.findViewById(R.id.btn_emiratesvehiclegate);
        btn_LandDept = (ImageView) view.findViewById(R.id.btn_landdept);*/

        btn_RTA.setOnClickListener(this);
        /*btn_DubaiEco.setOnClickListener(this);
        btn_EmiratesVGate.setOnClickListener(this);
        btn_LandDept.setOnClickListener(this);*/

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rta:
                mFragment = RTAMainServices.newInstance();
                addFragment();
                break;
           /* case R.id.btn_dubaieconomy:
                break;
            case R.id.btn_emiratesvehiclegate:
                break;
            case R.id.btn_landdept:
                break;*/
            default:
                break;
        }
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}
