package RTANetworking.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.GenericServiceCall.ServiceLayer;
import RTANetworking.Interfaces.ServiceCallbackCyberSource;
import RTANetworking.RequestAndResponse.PaymentResponse;
import example.dtc.R;

import static utility.Constant.TAG;

public class PaymentGatewayFragment extends Fragment {

    private ProgressBar progressBar;
    private WebView webView;
    private Button btnnewsBack;
    private ProgressDialog dialog;
    private static PaymentResponse resObjectItem;
    private static PaymentResponse resObject;
    private ArrayList<PaymentResponse> paymentResponseItemsList = new ArrayList<>();
    Fragment mFragment;
    String encryptedCardNo,encryptedCardExpiry,encryptedCardCCV,encryptedCardType,decryptedCardNo,decryptedCardExpiry,decryptedCardCCV,decryptedCardType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public static PaymentGatewayFragment newInstance() {
        return new PaymentGatewayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paymentgatewayfragment, container, false);
        //progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        webView = (WebView) view.findViewById(R.id.webview);

        try {
          /*  ServiceLayer.threeDSecurePayment("Tsr8ApkwmxG+Fil0/eynhN0iP4hBm/tz", "M7UVg74c5Jc=", "100.00", "DD190617121134",
                    "DD190617121134", "douDsXHYOHw=", "12uhoT06ZF4=", "::1",
                    webView, "Test", "Buyer", "Dubai", "Dubai", "",
                    "", "AE",
                    "Test.Buyer@newindiaassurance.com", "0504751284", new ServiceCallbackCyberSource() {*/

            encryptedCardNo =EncryptDecrpt.Encrypt("4111111111111111", ConfigrationRTA.PAYMENT_SECRET_KEY);
            encryptedCardExpiry =EncryptDecrpt.Encrypt("1234", ConfigrationRTA.PAYMENT_SECRET_KEY);
            encryptedCardCCV =EncryptDecrpt.Encrypt("321", ConfigrationRTA.PAYMENT_SECRET_KEY);
            encryptedCardType =EncryptDecrpt.Encrypt("001", ConfigrationRTA.PAYMENT_SECRET_KEY);//Visa 001 /MasterCard 002

            decryptedCardNo = EncryptDecrpt.Decrypt(encryptedCardNo, ConfigrationRTA.PAYMENT_SECRET_KEY);
            decryptedCardExpiry =EncryptDecrpt.Decrypt(encryptedCardExpiry, ConfigrationRTA.PAYMENT_SECRET_KEY);
            decryptedCardCCV =EncryptDecrpt.Decrypt(encryptedCardCCV, ConfigrationRTA.PAYMENT_SECRET_KEY);
            decryptedCardType =EncryptDecrpt.Decrypt(encryptedCardType, ConfigrationRTA.PAYMENT_SECRET_KEY);//Visa 001 /MasterCard 002



                ServiceLayer.threeDSecurePayment(encryptedCardNo, encryptedCardExpiry, "100.00", "DD190617121134",
                    "DD190617121134", encryptedCardType, encryptedCardCCV, "127.0.0.1",
                    webView, "Test", "Buyer", "Dubai", "Dubai", "123456",
                    "Dubai", "AE",
                    "Test.Buyer@gmail.com", "12345678", new ServiceCallbackCyberSource() {
                        @Override
                        public void onSuccess(PaymentResponse obj) {

                            Gson gson = new Gson();

                            paymentResponseItemsList.add(obj);

                            Log.d("Service Response", resObject.Message);

                            /*Bundle bundlepos = new Bundle();
                            mFragment = FineInqandPaymentDetails.newInstance();
                            bundlepos.putParcelableArrayList(Constant.FIPServiceTypeItemsArrayList, fipdetailsItemsList);
                            bundlepos.putString(Constant.FinesPage, Constant.PlateNo);
                            mFragment.setArguments(bundlepos);
                            addFragment();*/

                            Log.d("Service Response", obj.toString());

                        }

                        @Override
                        public void onFailure(String obj) {
                            Log.d("Service Response", obj);
                        }
                    });

        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        /*btnnewsBack = (Button) view.findViewById(R.id.btnnewsback);

        final Uri uri = Uri.parse("");
        webView.setVisibility(View.VISIBLE);

        dialog = ProgressDialog.show(getContext(), null,
                "Please Wait...Page is Loading...");
        webView.setWebViewClient(new WebViewClient() {

            // This method will be triggered when the Page Started Loading

            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                dialog.setCancelable(false);
                super.onPageStarted(view, url, favicon);
            }

            // This method will be triggered when the Page loading is completed

            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                super.onPageFinished(view, url);
            }

            // This method will be triggered when error page appear

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                dialog.dismiss();
                // You can redirect to your own page instead getting the default
                // error page
                Toast.makeText(getContext(),
                        "The Requested Page Does Not Exist", Toast.LENGTH_LONG).show();
                webView.loadUrl(String.valueOf(uri));
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webView.loadUrl(String.valueOf(uri));
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        btnnewsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        return view;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

}
