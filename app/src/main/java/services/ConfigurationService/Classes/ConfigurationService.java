package services.ConfigurationService.Classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import Networking.ServicesUrls;
import RTANetworking.GenericServiceCall.AsyncServiceCall;
import RTANetworking.Interfaces.AsyncResponse;

import interfaces.ServiceCallback;
import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import utility.Common;
import utility.Constant;


public class ConfigurationService {

    ClickLogServiceRequest clickLogDetails;
    Context mContext;
    String ClassName;

    public ConfigurationService(Context context) {
        mContext = context;
    }

    public void CallConfigurationService(ServiceCallback serviceCallback) {
        //get Class Name
        ClassName = getClass().getCanonicalName();
        try {
            clickLogDetails = new ClickLogServiceRequest();
            clickLogDetails.setServiceId(Constant.ConfigurationServiceId);
            clickLogDetails.setRequestId(Common.getUUID());
            clickLogDetails.setSourceApplication(Constant.SourceApplication);
            clickLogDetails.setRequestType(Constant.RequestType);
            clickLogDetails.setRequestCategory(Constant.RequestCategory);
            clickLogDetails.setTimeStamp(Common.getdateTime());
            ClickLogServiceRequest.Login login = new ClickLogServiceRequest.Login();
            login.setLoginID(Constant.LoginID);
            login.setPassword(Constant.Password);
            clickLogDetails.setLogin(login);


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
                            ExceptionService.ExceptionLogService(mContext, e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
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
            String SOAPRequestXML = createConfigurationService(clickLogDetails);

            // Pass the Object Here For the Service
            asyncCall.execute(SOAPRequestXML, ServicesUrls.URL, ServicesUrls.URLSOAPAction);

        } catch (Exception e) {
            ExceptionService.ExceptionLogService(mContext, e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
    }

    public String createConfigurationService(ClickLogServiceRequest request) {

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
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getTimeStamp(), request.getSourceApplication(), request.getServiceId(),
                    request.getRequestType(),request.getRequestId(), request.getRequestCategory(),
                    request.getLogin().LoginID, request.getLogin().Password);

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
            ExceptionService.ExceptionLogService(mContext, Ex.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }

        return SOAPRequestXML;
    }

    public class CustomSoapSerializationEnvelope extends SoapSerializationEnvelope {

        public CustomSoapSerializationEnvelope(int version) {
            super(version);
        }

        @Override
        public void write(XmlSerializer writer) throws IOException {
            writer.setPrefix("i", xsi);
            writer.setPrefix("d", xsd);
            writer.setPrefix("c", enc);
            writer.setPrefix("soap", env); // <-- changed line
            writer.startTag(env, "Envelope");
            writer.startTag(env, "Header");
            writeHeader(writer);
            writer.endTag(env, "Header");
            writer.startTag(env, "Body");
            writeBody(writer);
            writer.endTag(env, "Body");
            writer.endTag(env, "Envelope");
        }
    }
}