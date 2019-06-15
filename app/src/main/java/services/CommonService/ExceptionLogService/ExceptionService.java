package services.CommonService.ExceptionLogService;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import interfaces.ServiceCallback;
import services.CommonService.ExceptionLogService.Classes.ExceptionLogService;
import services.CommonService.ExceptionLogService.RequestandResponse.ExceptionLogServiceResponseArray;
import services.CommonService.ExceptionLogService.RequestandResponse.ExceptionLogServiceResponseObject;

import static utility.Common.WriteExceptionofExceptionLogInTextFile;
import static utility.Common.getFilePathForException;

public class ExceptionService {

    public static Context mContext;
    public static ExceptionLogServiceResponseArray exceptionLogServiceResponseArray;
    public static ExceptionLogServiceResponseObject exceptionLogServiceResponseObject;
    public static Gson gson = new Gson();
    public static String ServiceCode, ServiceMessage;


    public static void ExceptionLogService(Context context, String ExceptionMessage, String ClassName, String AndroidSerialNo) {
        //Exception Log service
        ExceptionLogService exceptionLogService = new ExceptionLogService(context);

        exceptionLogService.CallExceptionLogService(ExceptionMessage, ClassName, AndroidSerialNo, new ServiceCallback() {
            @Override
            public void onSuccess(JSONObject obj) throws JSONException {
                try {
                    String data = obj.optJSONObject("CustomerUniqueNo").getString("a:CKeyValuePair");
                    Object json = new JSONTokener(data).nextValue();
                    if (json instanceof JSONObject) {
                        exceptionLogServiceResponseObject = gson.fromJson(obj.toString(), ExceptionLogServiceResponseObject.class);
                        ServiceCode = exceptionLogServiceResponseObject.getServiceAttributesList().getACKeyValuePair().get(0).getAValue();
                        ServiceMessage = exceptionLogServiceResponseObject.getServiceAttributesList().getACKeyValuePair().get(2).getAValue();
                    }
                    //you have an object
                    else if (json instanceof JSONArray) {
                        exceptionLogServiceResponseArray = gson.fromJson(obj.toString(), ExceptionLogServiceResponseArray.class);
                        ServiceCode = exceptionLogServiceResponseArray.getServiceAttributesList().getACKeyValuePair().get(0).getAValue();
                        ServiceMessage = exceptionLogServiceResponseArray.getServiceAttributesList().getACKeyValuePair().get(2).getAValue();
                    }
                } catch (Exception e) {
                    WriteExceptionofExceptionLogInTextFile(getFilePathForException(), e.getMessage());
                }
            }

            @Override
            public void onFailure(String obj) {

            }
        });
    }
}
