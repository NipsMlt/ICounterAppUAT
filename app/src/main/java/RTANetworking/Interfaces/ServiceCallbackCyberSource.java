package RTANetworking.Interfaces;

import org.json.JSONException;

import RTANetworking.RequestAndResponse.PaymentResponse;

/**
 * Created by raheel on 3/26/2018.
 */

public interface ServiceCallbackCyberSource {
    void onSuccess(PaymentResponse obj) throws JSONException;
    void onFailure(String obj);
}
