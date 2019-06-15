package volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by raheel on 10/3/2016.
 */
public interface ServiceCallback {
        void onSuccess(JSONObject obj) throws JSONException;
        void onFailure(String obj);
    }

