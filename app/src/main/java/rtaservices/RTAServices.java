package rtaservices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



import example.dtc.R;

public class RTAServices extends Fragment {

    Context context;
    Fragment mFragment;

    Button btnBack;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static RTAServices newInstance() {
        return new RTAServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rtamainactivity, null);

        return view;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }
}