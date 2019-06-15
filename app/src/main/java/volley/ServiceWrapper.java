package volley;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raheel on 8/8/2017.
 */

public class ServiceWrapper {

    /**
     * Generic Function For Calling the POST API
     *
     * @param url
     * @param json
     * @param con
     * @param callback
     */
    public static void serviceCall(String url, JSONObject json, Context con, final ServiceCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(con);

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.Log.d("Volley services", response.toString());
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley services", error.toString());
                callback.onFailure(error.toString());

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                  Log.d("TAG","Time Out Error");
                }
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Show the Progress Bar
     *
     * @param progress
     */
    public static void showProgressBar(ProgressDialog progress) {
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

    }

    /**
     * Hide the Progress Bar
     *
     * @param progress
     */
    public static void hideProgressBar(ProgressDialog progress) {
        progress.dismiss();
    }

    /**
     * @param alertDialogBuilder
     * @param message
     */
    public static void showAlertDialog(AlertDialog alertDialogBuilder, String message) {

        // set title
        alertDialogBuilder.setTitle("Error");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.show();
    }

    /**
     * @param alertDialogBuilder
     * @param message
     */
    public static void showAlertDialogSuccess(AlertDialog alertDialogBuilder, String message) {

        // set title
        alertDialogBuilder.setTitle("Success");
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.show();
    }

}
