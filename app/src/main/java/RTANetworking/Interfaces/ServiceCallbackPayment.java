package RTANetworking.Interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface ServiceCallbackPayment {

    void onSuccess(JSONObject obj) throws JSONException;
    void onFailure(String obj);
}
