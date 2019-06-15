package services.GenericServiceCall;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import RTANetworking.Interfaces.AsyncResponse;

/**
 * Created by talal on 08/06/2018.
 */


public class AsyncServiceCallIncident extends AsyncTask<Object, Object, Object> {

    //Call back interface
    public AsyncResponse delegate = null;
    private ProgressDialog dialog;
    private Context mContext;
    BufferedInputStream inputStream;
    HttpURLConnection urlConnection;
    byte[] outputBytes;
    String query;
    String ClassName, ResponseData;


    // Call back assigning to local delegate
    public AsyncServiceCallIncident(AsyncResponse asyncResponse, Context context) {
        //Assigning call back interface through constructor
        delegate = asyncResponse;
        mContext = context;
        //get Class Name
        ClassName = getClass().getCanonicalName();

        dialog = ProgressDialog.show(context, "Please Wait",
                "Page is Loading...");
        dialog.setCancelable(false);
    }

    @Override
    protected Object doInBackground(Object... params) {

        // Send data
        try {

            String URL = (String) params[0];
            String query = (String) params[1];
            /* forming th java.net.URL object */
            //URL url = new URL("http://10.0.87.130:811/IncidentManagementExternal/Create");
            //URL url = new URL("https://networkips.anghami.com/rest/do/PRElogin");
            URL url = new URL(URL);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* pass post data */
            outputBytes = query.getBytes("UTF-8");

            urlConnection.setRequestMethod("POST");
            urlConnection.connect();
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputBytes);
            os.close();

            /* Get Response and execute WebService request*/
            int statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpsURLConnection.HTTP_OK) {

                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                ResponseData = convertStreamToString(inputStream);

            } else {

                ResponseData = null;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return ResponseData;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        delegate.processFinish(result);
    }

    //Converts Input Stream to String
    public String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
