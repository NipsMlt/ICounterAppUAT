package services.UserLoginService.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Networking.ServicesUrls;
import RTANetworking.GenericServiceCall.AsyncServiceCall;
import RTANetworking.Interfaces.AsyncResponse;
import interfaces.ServiceCallback;
import services.UserLoginService.RequestandResponse.CKeyValuePair;
import services.UserLoginService.RequestandResponse.UserLoginDetails;
import utility.Constant;


public class UserLoginService {

    UserLoginDetails userLoginDetails;
    Context mContext;

    public UserLoginService(Context context) {
        mContext = context;
    }

    public void CallUserLoginService(String ServiceId, String RequestId, String SourceApplication, String RequestType,
                                     String RequestCategory, String TimeStamp, String LoginId, String Password,
                                     String UserNameAdminLogin, String PasswordAdminLogin, String DeviceIDAdminLogin,
                                     String LoginTimeAdminLogin, ServiceCallback serviceCallback) {


        try {
            userLoginDetails = new UserLoginDetails();
            userLoginDetails.setServiceId(ServiceId);
            userLoginDetails.setRequestId(RequestId);
            userLoginDetails.setSourceApplication(SourceApplication);
            userLoginDetails.setRequestType(RequestType);
            userLoginDetails.setRequestCategory(RequestCategory);
            userLoginDetails.setTimeStamp(TimeStamp);
            UserLoginDetails.Login login = new UserLoginDetails.Login();
            login.setLoginID(LoginId);
            login.setPassword(Password);
            userLoginDetails.setLogin(login);
            List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(Constant.UserNameAdminLogin, UserNameAdminLogin));
                    add(new CKeyValuePair(Constant.PasswordAdminLogin, PasswordAdminLogin));
                    add(new CKeyValuePair(Constant.DeviceIdAdminLogin, DeviceIDAdminLogin));
                    add(new CKeyValuePair(Constant.LoginTimeAdminLogin, LoginTimeAdminLogin));
                }
            };
            userLoginDetails.setCustomerUniqueNo(ServiceKeyValues);

            AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
                @SuppressLint("LongLogTag")
                @Override
                public void processFinish(Object output) {
                    //Log.d("Response From Asynchronous:", (String) output);

                    if (output != null) {
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = XML.toJSONObject((String) output);
                            JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("InquiryResponse");
                            JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                            // Assign the response to result variable
                            serviceCallback.onSuccess(body);

                        } catch (JSONException e) {
                            Log.e("JSON exception", e.getMessage());
                            serviceCallback.onFailure(e.getLocalizedMessage());
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("Response From Asynchronous:", "error");
                        serviceCallback.onFailure("error");
                    }
                }
            }, mContext);

            // Pass the Object Here and Get the XML
            String SOAPRequestXML = createUserLoginService(userLoginDetails);

            // Pass the Object Here For the Service
            asyncCall.execute(SOAPRequestXML, ServicesUrls.URLProd, ServicesUrls.URLSOAPAction);

        } catch (Exception e) {
            e.getMessage();
        }


    }

    public String createUserLoginService(UserLoginDetails request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\" xmlns:nip=\"http://schemas.datacontract.org/2004/07/NipsGateway.Component.DTC\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>sdfadfsd</tem:SignatureFields>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>asd</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>asadffdf</tem:MerchantId>\n" +
                            "      <tem:Login>\n" +
                            "         <!--Optional:-->\n" +
                            "         <nip:Description>?</nip:Description>\n" +
                            "         <!--Optional:-->\n" +
                            "         <nip:LoginID>%s</nip:LoginID>\n" +
                            "         <!--Optional:-->\n" +
                            "         <nip:Password>%s</nip:Password>\n" +
                            "      </tem:Login>\n" +
                            "      <tem:BankId>?</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:InquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "\t\t<nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "\t\t<nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "\t\t<nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ServiceAttributesList>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>?</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>?</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "         </tem:ServiceAttributesList>\n" +
                            "      </tem:InquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getTimeStamp(), request.getSourceApplication(), request.getServiceId(),
                    request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getLogin().LoginID, request.getLogin().Password,
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

}