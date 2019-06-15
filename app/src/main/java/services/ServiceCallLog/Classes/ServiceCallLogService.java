package services.ServiceCallLog.Classes;

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
import RTANetworking.Interfaces.AsyncResponse;

import rtamain.RTAMain;
import services.CommonService.ExceptionLogService.ExceptionService;
import services.GenericServiceCall.AsyncServiceCallNoDialog;
import services.ServiceCallLog.RequestandResponse.CKeyValuePair;
import services.ServiceCallLog.RequestandResponse.ServiceCallLogServiceRequest;
import utility.Common;
import utility.Constant;


public class ServiceCallLogService {

    ServiceCallLogServiceRequest serviceCallLogServiceRequest;
    Context mContext;
    String clicklogXML, clicklogcopyXML = "", clicklogconcatenatedString = "";
    String ClassName;


    public ServiceCallLogService(Context context) {
        mContext = context;
    }

    public void CallServiceCallLogService(String ServiceName, String ServiceDescription) {
        //get Class Name
        ClassName = getClass().getCanonicalName();

        try {
            /****Header****/
            serviceCallLogServiceRequest = new ServiceCallLogServiceRequest();
            serviceCallLogServiceRequest.setServiceId(Constant.ServiceCallLogServiceID);
            serviceCallLogServiceRequest.setRequestId(Common.getUUID());
            serviceCallLogServiceRequest.setSourceApplication(Constant.SourceApplication);
            serviceCallLogServiceRequest.setRequestType(Constant.RequestType);
            serviceCallLogServiceRequest.setRequestCategory(Constant.RequestCategory);
            serviceCallLogServiceRequest.setTimeStamp(Common.getdateTime());
            ServiceCallLogServiceRequest.Login login = new ServiceCallLogServiceRequest.Login();
            login.setLoginID(Constant.LoginID);
            login.setPassword(Constant.Password);
            serviceCallLogServiceRequest.setLogin(login);
            /****Header****/

            /****Body****/
            List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(Constant.Request_Id_SCLS, Common.getUUID()));
                    add(new CKeyValuePair(Constant.Service_Name_SCLS, ServiceName));
                    add(new CKeyValuePair(Constant.Description_SCLS, ServiceDescription));
                    add(new CKeyValuePair(Constant.Device_Id_SCLS, RTAMain.AndroidSerialNo));
                    add(new CKeyValuePair(Constant.Time_Stamp_SCLS, Common.getdateTime()));
                }
            };
            serviceCallLogServiceRequest.setCustomerUniqueNo(ServiceKeyValues);
            /****Body****/

            //Service Call
            AsyncServiceCallNoDialog asyncCall = new AsyncServiceCallNoDialog(new AsyncResponse() {
                @SuppressLint("LongLogTag")
                @Override
                public void processFinish(Object output) {
                    // Log.d("Response From Asynchronous:", (String) output);

                    if (output != null) {
                        JSONObject jsonObj = null;

                        try {
                            jsonObj = XML.toJSONObject((String) output);
                            JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("InquiryResponse");
                            JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                            // Assign the response to result variable
                            //serviceCallback.onSuccess(body);

                        } catch (JSONException e) {
                            Log.e("JSON exception", e.getMessage());
                            //serviceCallback.onFailure(e.getLocalizedMessage());
                            ExceptionService.ExceptionLogService(mContext, e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                        }

                    } else {
                        Log.d("Response From Asynchronous:", "error");
                        //serviceCallback.onFailure("error");
                    }
                }
            }, mContext);

            // Pass the Object Here and Get the XML
            String SOAPRequestXML = createServiceCallLogService(serviceCallLogServiceRequest);

            // Pass the Object Here For the Service
            asyncCall.execute(SOAPRequestXML, ServicesUrls.URL, ServicesUrls.URLSOAPAction);

        } catch (Exception e) {
            ExceptionService.ExceptionLogService(mContext, e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
        }
    }

    public String createServiceCallLogService(ServiceCallLogServiceRequest request) {

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
                            "            <nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "            <nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "            <nip:CKeyValuePair>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Key>%s</nip:Key>\n" +
                            "               <!--Optional:-->\n" +
                            "               <nip:Value>%s</nip:Value>\n" +
                            "            </nip:CKeyValuePair>\n" +
                            "\n" +
                            "            <nip:CKeyValuePair>\n" +
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
                            "         </tem:ServiceAttributesList>\n" +
                            "      </tem:InquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getTimeStamp(), request.getSourceApplication(), request.getServiceId(),
                    request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getLogin().LoginID, request.getLogin().Password,
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(), request.getCustomerUniqueNo().get(4).getValue());

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