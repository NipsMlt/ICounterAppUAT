package loginfragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;
import example.dtc.R;
import interfaces.ServiceCallback;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.UserLoginService.Classes.UserLoginService;
import services.UserLoginService.RequestandResponse.UserLoginDetailsResponse;
import utility.Common;
import utility.Constant;

public class SettingsLoginFragment extends Fragment {

    Fragment mFragment;
    UserLoginDetailsResponse userLoginDetailsResponse;
    Gson gson = new Gson();
    String ServiceCode, ServiceMessage;
    String ClassName;
    Button btn_BackLogin;
    private String email, password;
    private static final String TAG = "LoginActivity";
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;


    public SettingsLoginFragment() {
        // Required empty public constructor
    }

    public static SettingsLoginFragment newInstance() {
        return new SettingsLoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settingslogin, container, false);
        _emailText = (EditText) view.findViewById(R.id.input_email);
        _passwordText = (EditText) view.findViewById(R.id.input_password);
        _loginButton = (Button) view.findViewById(R.id.btn_login);
        btn_BackLogin = (Button) view.findViewById(R.id.btn_backlogin);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Randomly generated no for taxi meter info service request ID
        UUID uuid = UUID.randomUUID();
        String uuidInString = uuid.toString();

        _emailText.setText("");
        _passwordText.setText("");

        btn_BackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment = RTAMain.newInstance();
                addFragment();
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (!validate()) {
                        onLoginFailed();
                        return;
                    }

                    email = _emailText.getText().toString();
                    password = _passwordText.getText().toString();

                    //if (MainActivity.internetCheck) {
                    UserLoginService loginService = new UserLoginService(getContext());
                    loginService.CallUserLoginService(Constant.ServiceIdAdminLogin, uuidInString, Constant.SourceApplication,
                            Constant.RequestType, Constant.RequestCategory, Common.getdateTime(), Constant.LoginID, Constant.Password,
                            email, password, RTAMain.AndroidSerialNo, Common.getdateTime(), new ServiceCallback() {
                                @Override
                                public void onSuccess(JSONObject obj) throws JSONException {
                                    userLoginDetailsResponse = gson.fromJson(obj.toString(), UserLoginDetailsResponse.class);
                                    ServiceCode = userLoginDetailsResponse.getServiceAttributesList().getACKeyValuePair().get(0).getAValue();
                                    ServiceMessage = userLoginDetailsResponse.getServiceAttributesList().getACKeyValuePair().get(2).getAValue();

                                    if (ServiceCode.equals("0000")) {
                                        mFragment = SettingsMenuFragmnent.newInstance();
                                        addFragment();
                                        Toast.makeText(getContext(), ServiceMessage, Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(getContext(), ServiceMessage, Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(String obj) {
                                    Toast.makeText(getContext(), "An error has occured", Toast.LENGTH_LONG).show();

                                }
                            }
                    );

                } catch (Exception e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }
            }
        });

        return view;
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();

                        mFragment = SettingsMenuFragmnent.newInstance();
                        addFragment();

                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        //finish();
        Toast.makeText(getContext(), "Login success", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {/* @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }*/
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            Toast.makeText(getContext(), "enter a valid email address", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Please enter a correct password between 4 and 10 characters");
            Toast.makeText(getContext(), "enter a valid password address", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

}
