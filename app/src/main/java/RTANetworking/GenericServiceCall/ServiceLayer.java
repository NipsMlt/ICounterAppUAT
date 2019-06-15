package RTANetworking.GenericServiceCall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.ConfigurationVLDL;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.Common.ServiceUrls;
import RTANetworking.Common.Utilities;
import RTANetworking.Interfaces.AsyncResponse;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackCyberSource;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.HTMLRequestAndRequest;
import RTANetworking.RequestAndResponse.InquiryRequest;
import RTANetworking.RequestAndResponse.KioskPaymentRequest;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.NipsMerchantDetailsRequest;
import RTANetworking.RequestAndResponse.NipsiCounterInquiryRequest;
import RTANetworking.RequestAndResponse.PaymentRequest;
import RTANetworking.RequestAndResponse.PaymentResponse;
import RTANetworking.RequestAndResponse.XMLRequestAndResponse;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import services.GenericServiceCall.AsyncServiceCallNoDialog;

/**
 * Created by raheel on 3/27/2018.
 */

public class ServiceLayer {

    // Response Final Object
    private static KioskPaymentResponse kioskResponse;

    // Response from 3D Secure Payment
    private static PaymentResponse paymentResponse;

    /**
     * Call Inquiry By TRF Number
     *
     * @param callback  -> Request and Response for the service Callback
     * @param TrfNo     -> String TRF Number from the user Input
     * @param BirthYear -> String Birth Year from the zuser Input
     */
    public static void callInquiryByTrfNo(final String TrfNo, final String BirthYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigrationRTA.VERSION);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_TRF);

        // Set the Signature field
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(ConfigrationRTA.SERVICE_TYPE_ID);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request Type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);

        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>();

        ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRF_NO, TrfNo));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, BirthYear));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryTrfNoRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By TRF Number
     *
     * @param callback  -> Request and Response for the service Callback
     * @param TrfNo     -> String TRF Number from the user Input
     * @param BirthYear -> String Birth Year from the zuser Input
     */
    public static void callInquiryByTrfNo(final String ServiceID, final String ServiceName, final String TrfNo, final String BirthYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigrationRTA.VERSION);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the service Name
        request.setServiceName(ServiceName);

        // Set the Signature field
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(ServiceID);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request Type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);

        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRF_NO, TrfNo));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, BirthYear));
                add(new CKeyValuePair("SearchBy", "2"));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryTrfNoCertRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }


    /**
     * Call Inquiry By License Number
     *
     * @param LicenseFrom
     * @param LicenseNo
     * @param BirthYear
     * @param callback
     */
    public static void callInquiryByLicenseNo(final String LicenseFrom, final String LicenseNo, final String BirthYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the time stamp
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the source application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_LICENSE);

        // Set the signature filed here
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_LICENSE);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant ID
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Request
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language Here
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.LICENSE_FROM, LicenseFrom));
                add(new CKeyValuePair(ConfigrationRTA.LICENSE_NUMBER, LicenseNo));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, BirthYear));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryLicenseNumberRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Inquiry By Plate Number
     *
     * @param PlateNo
     * @param PlateCode
     * @param PlateCategory
     * @param PlateSource
     * @param BirthYear
     * @param callback
     */
    public static void callInquiryByPlateNo(final String ServiceId, final String ServiceName, final String PlateNo, final String PlateCode, final String PlateCategory, final String PlateSource, final String BirthYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ServiceName);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ServiceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.PLATE_NO, PlateNo));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CODE, PlateCode));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CATEGORY, PlateCategory));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_SOURCE, PlateSource));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, BirthYear));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryPlateNumberRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Inquiry By ticket Info
     *
     * @param BenefeciaryCode
     * @param TicketNumber
     * @param TicketYear
     * @param callback
     */
    public static void callInquiryByTicketInfoNo(final String BenefeciaryCode, final String TicketNumber, final String TicketYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_TICKET_INFO);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_TICKET_INFO);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.BENEFICIARY_CODE, BenefeciaryCode));
                add(new CKeyValuePair(ConfigrationRTA.TICKET_NUMBER, TicketNumber));
                add(new CKeyValuePair(ConfigrationRTA.TICKET_YEAR, TicketYear));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryTicketNoRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }


    public static void callInquiryServiceCharges(final String Amount, String TransactionID, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_SERVICE_CHARGES);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_SERVICE_CHARGES);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.SERVICE_CHARGES_AMOUNT, Amount));
                add(new CKeyValuePair(ConfigrationRTA.SERVICE_CHARGES_SERVICE_CODE, ConfigrationRTA.SERVICE_CHARGES_SERVICE_CODE_VALUE));
                add(new CKeyValuePair(ConfigrationRTA.SERVICE_CHARGES_TRANSACTION_ID, TransactionID));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        Gson gson = new Gson();
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryServiceCharges(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void callCreateTransactionService(final String TrafficFileNo, final double MinimumAmount, final double MaximumAmount,
                                                    final double Dueamount, final double PaidAmount, final double ServiceCharge,
                                                    final KskServiceItem Items[], final String TotalAmount,
                                                    final ServiceCallbackPayment callback, Context context) {

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_CREATE_TRANSACTION);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_CREATE_TRANSACTION);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE_PAYMENT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY_PAYMENT);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.SET_VICE_CODE, ConfigrationRTA.SET_VICE_CODE_FINE));
                add(new CKeyValuePair(ConfigrationRTA.TRAFFIC_FILE_NUMBER, TrafficFileNo));
                add(new CKeyValuePair(ConfigrationRTA.IS_FINE, ConfigrationRTA.IS_FINE_VALUE));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigrationRTA.PAYMENT_TYPE);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        // Service Type Id
        service.setServiceTypeId(ConfigrationRTA.SERVICE_TYPE_ID);

        // Payment Inquiry
        service.setServiceTitle(ConfigrationRTA.SERVICE_TITLE);

        // Comment
        service.setComment("");

        // Minimum Amount
        service.setMinimumAmount(MinimumAmount);

        // Maximum Amount
        service.setMaximumAmount(MaximumAmount);

        // Due Amount
        service.setDueamount(Dueamount);

        // Paid Amount
        service.setPaidAmount(PaidAmount);

        // Service Charge
        service.setServiceCharge(ServiceCharge);

        // Set the Items Array here
        service.setItems(Items);

        // Set the service Type Object here
        request.setServiceType(service);

        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValuesAttributeList = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TOTAL_AMOUNT, TotalAmount));
                add(new CKeyValuePair(ConfigrationRTA.TRAFFIC_FILE_NUMBER, TrafficFileNo));
            }
        };

        request.setServiceAttributesList(ServiceKeyValuesAttributeList);


        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("KioskPaymentResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createTransaction(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    public static void callDirectAccountService(final KskServiceItem Items[], final String TransactionID, final String TransactionDate,
                                                final String TransactionAmount, final double MinimumAmount, final double MaximumAmount,
                                                final double DueAmount, final double PaidAmount, final double ServiceCharge,
                                                final ServiceCallbackPayment callback, Context context) {

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_DIRECT_DEBIT);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_DIRECT_DEBIT);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE_PAYMENT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY_PAYMENT);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_ID, TransactionID));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigrationRTA.PAYMENT_TYPE_CREDIT_CARD);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        // Service Type Id
        service.setServiceTypeId(ConfigrationRTA.SERVICE_TYPE_ID);

        // Payment Inquiry
        service.setServiceTitle(ConfigrationRTA.SERVICE_TITLE);

        // Comment
        service.setComment("");

        // Minimum Date
        service.setMinimumAmount(MinimumAmount);

        // Maximum Amount
        service.setMaximumAmount(MaximumAmount);

        // Due Amount
        service.setDueamount(DueAmount);

        // Paid Amount
        service.setPaidAmount(Double.parseDouble(TransactionAmount));

        // Service Charge
        service.setServiceCharge(ServiceCharge);

        // Set the Items Array here
        service.setItems(Items);

        // Set the service Type Object here
        request.setServiceType(service);

        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValuesAttributeList = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_ID_DA, TransactionID));
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_DATE_DA, TransactionDate));
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_AMOUNT_DA, TransactionAmount));
                add(new CKeyValuePair(ConfigrationRTA.CC_NR, ""));
                add(new CKeyValuePair(ConfigrationRTA.CC_HOLDER_NAME, ""));
                add(new CKeyValuePair(ConfigrationRTA.CC_BRAND, ""));
            }
        };

        request.setServiceAttributesList(ServiceKeyValuesAttributeList);


        AsyncServiceCallNoDialog asyncCall = new AsyncServiceCallNoDialog(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("KioskPaymentResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.directDebit(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    /*Direct Account for RMS*/
    public static void callDirectAccountServiceRMS(final String TransactionID, final String TransactionAmount, final String BankTransactionID,
                                                   final String paymentType, final double MinimumAmount, final double MaximumAmount,
                                                   final double DueAmount, final double PaidAmount, final String ServiceCharge,
                                                   final ServiceCallbackPayment callback, Context context) {

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_DIRECT_DEBIT);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_DIRECT_DEBIT);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE_PAYMENT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY_PAYMENT);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_ID, TransactionID));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigrationRTA.PAYMENT_TYPE_CREDIT_CARD);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        // Service Type Id
        service.setServiceTypeId(ConfigrationRTA.SERVICE_TYPE_ID);

        // Payment Inquiry
        service.setServiceTitle(ConfigrationRTA.SERVICE_TITLE);

        // Comment
        service.setComment("");

        // Minimum Date
        service.setMinimumAmount(MinimumAmount);

        // Maximum Amount
        service.setMaximumAmount(MaximumAmount);

        // Due Amount
        service.setDueamount(DueAmount);

        // Paid Amount
        service.setPaidAmount(Double.parseDouble(TransactionAmount));

        // Service Charge
        service.setServiceCharge(Double.parseDouble(ServiceCharge));

        // Set the Items Array here
        //service.setItems(Items);

        // Set the service Type Object here
        request.setServiceType(service);

        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValuesAttributeList = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TRANSACTION_ID_DA_RMS, TransactionID));
                add(new CKeyValuePair(ConfigrationRTA.BANKTRANSACTION_ID_DA_RMS, BankTransactionID));
                add(new CKeyValuePair(ConfigrationRTA.SERVICE_CHARGES_DA_RMS, ServiceCharge));
                add(new CKeyValuePair(ConfigrationRTA.PAYMENT_TYPE_DA_RMS, paymentType));
            }
        };

        request.setServiceAttributesList(ServiceKeyValuesAttributeList);


        AsyncServiceCallNoDialog asyncCall = new AsyncServiceCallNoDialog(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("KioskPaymentResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.directDebit(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    /**
     * 3D secure Service For Card Payment
     * WITH OTP Processing
     *
     * @param CardNumber
     * @param CardExpiry
     * @param Amount
     * @param TransactionReferenceNo
     * @param ReferenceNumber
     * @param CardType
     * @param CVV
     * @param IpAddress
     * @param webView
     * @param FirstName
     * @param LastName
     * @param CustomerAddress
     * @param CustomerCity
     * @param PostalCode
     * @param CustomerState
     * @param CustomerCountry
     * @param CustomerEmail
     * @param CustomerContactNumber
     * @param callback
     */
    public static void threeDSecurePayment(String CardNumber, String CardExpiry, String Amount, String TransactionReferenceNo,
                                           String ReferenceNumber, String CardType, String CVV, String IpAddress,
                                           WebView webView, String FirstName, String LastName, String CustomerAddress,
                                           String CustomerCity, String PostalCode, String CustomerState, String CustomerCountry,
                                           String CustomerEmail, String CustomerContactNumber,
                                           final ServiceCallbackCyberSource callback) {

        // Web View Setting Object here
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                // do your stuff here
                view.evaluateJavascript("(function(){return document.getElementById(\"FullResponse\").innerHTML})();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {
                                try {

                                    if (!TextUtils.isEmpty(html) && !html.equals("null")) {

                                        html = StringEscapeUtils.unescapeJava(html);
                                        Document doc = Jsoup.parse(html);
                                        Map<String, Object> data = new HashMap<String, Object>();

                                        for (Element input : doc.select("input")) {
                                            data.put(input.attr("id"), input.attr("value"));
                                        }

                                        JSONObject json = new JSONObject(data);
                                        Gson gson = new Gson();
                                        paymentResponse = gson.fromJson(json.toString(), PaymentResponse.class);
                                        System.out.println(paymentResponse);

                                        callback.onSuccess(paymentResponse);
                                    }

                                } catch (Exception ex) {

                                    // Assign the response to result variable
                                    callback.onFailure(ex.getLocalizedMessage());
                                }

                            }
                        });
            }
        });

        // Start the Request Object from Here
        PaymentRequest request = new PaymentRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        //String RequestId = "95fea68d-22f3-471a-ab9b-17e9a9433bb2";
        String currectDateTime = Utilities.currentDateTime();

        // Set the action here
        request.setAction(ConfigrationRTA.PAYMENT_ACTION);

        // Set the request ID
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.PAYMENT_REQUEST_CATEGORY);

        // Set the Source Application here
        request.setSourceApplication(ConfigrationRTA.PAYMENT_SOURCE_APPLICATION);

        // Set the Device Finger Print
        request.setDeviceFingerPrint(ConfigrationRTA.PAYMENT_DEVICE_FINGER_PRINT);

        // set the Merchant ID
        request.setMerchantId(ConfigrationRTA.PAYMENT_MERCHANT_ID);

        // Set the User ID
        request.setUserId(ConfigrationRTA.PAYMENT_USER_ID);

        // Set the Password
        request.setPassword(ConfigrationRTA.PAYMENT_PASSWORD);

        // Set the Ip ASSIGNED
        request.setIpAssigned(ConfigrationRTA.PAYMENT_IP_ASSIGNED);

        // Set the Version
        request.setVersion(ConfigrationRTA.PAYMENT_VERSION);

        // Set the Service Id
        request.setServiceId(ConfigrationRTA.PAYMENT_SERVICE_ID);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.PAYMENT_SERVICE_NAME);

        // Set the TimeStamp
        request.setTimeStamp(currectDateTime);
        //request.setTimeStamp("19062017 12:11:34");

        // Set the Transaction Reference Number
        request.setTransactionReferenceNumber(TransactionReferenceNo);

        // Set the Reference Number
        request.setReferenceNumber(ReferenceNumber);

        // Set the Currency
        request.setCurrency(ConfigrationRTA.PAYMENT_CURRENCY);

        // Set the amount to be passed from the service
        request.setAmount(Amount);

        // Set the First Name
        request.setFirstName(FirstName);

        // Set the Second Name
        request.setLastName(LastName);

        // Set the Customer Address
        request.setCustomerAddress(CustomerAddress);

        // Set the Customer City
        request.setCustomerCity(CustomerCity);

        // Set the Customer Postal Code
        request.setCustomerPostalCode(PostalCode);

        // Set the Customer State
        request.setCustomerState(CustomerState);

        // Set the Customer Country
        request.setCustomerCountry(CustomerCountry);

        // Set the Customer Email
        request.setCustomerEmail(CustomerEmail);

        // Set the Customer Contact Number
        request.setCustomerContactNumber(CustomerContactNumber);

        // Set the Callback Url
        request.setCallBackUrl(ConfigrationRTA.PAYMENT_CALL_BACK_URL);

        // Set the Payment Channel
        request.setPaymentChannel(ConfigrationRTA.PAYMENT_CHANNEL);

        // Set the Language
        request.setLanguage(ConfigrationRTA.PAYMENT_LANGUAGE);

        // Set the Sub Merchant Id
        request.setSubMerchantID(ConfigrationRTA.PAYMENT_SUB_MERCHANT_ID);

        // Set the Signature Fields
        request.setSignatureFields(ConfigrationRTA.PAYMENT_SIGNATURE_FIELDS);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash3DSecure(RequestId, currectDateTime, ReferenceNumber, ConfigrationRTA.MERCHANT_ID, Amount, ConfigrationRTA.PAYMENT_SECRET_KEY));

        // Set the Card Number
        request.setCardNumber(CardNumber);

        // Set the Card Type
        request.setCardType(CardType);

        // Set the CVV
        request.setCVV(CVV);

        // Set the Card Expiry
        request.setCardExpiry(CardExpiry);

        // Set the Customer Ip Address
        request.setCustomerIPAddress(IpAddress);

        // Pass the Object Here and Get the XML
        HTMLRequestAndRequest req = new HTMLRequestAndRequest();
        String SOAPRequestXML = req.paymentRequest(request);

        webView.loadData(SOAPRequestXML, "text/html", "UTF-8");
    }

    /**
     * Call Inquiry By License Number
     *
     * @param CustomerEmail
     * @param EmailBody
     * @param Subject
     * @param callback
     */
    public static void callSendEmailRTAReceiptService(final String CustomerEmail, final String EmailBody,
                                                      final String Subject, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the time stamp
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the source application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_LICENSE);

        // Set the signature filed here
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_LICENSE);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant ID
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Request
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language Here
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.LICENSE_FROM, CustomerEmail));
                add(new CKeyValuePair(ConfigrationRTA.LICENSE_NUMBER, EmailBody));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, Subject));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquirySendEmail(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Digital pass service
     *
     * @param CustomerName
     * @param TransactionNumber
     * @param CustomerEmail
     * @param MobileNo
     * @param ServiceCode
     * @param ServiceName
     * @param LanguageCode_DP
     */
    public static void callDigitalPassService(final String CustomerName, final String TransactionNumber, final String CustomerEmail,
                                              final String MobileNo, final String ServiceCode, final String ServiceName,
                                              final String LanguageCode_DP
            , final ServiceCallback callback, Context context) {


        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID_DP);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_DP);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_DP);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.Customer_Name_DP, CustomerName));
                add(new CKeyValuePair(ConfigrationRTA.Transaction_Number_DP, TransactionNumber));
                add(new CKeyValuePair(ConfigrationRTA.Customer_Email_DP, CustomerEmail));
                add(new CKeyValuePair(ConfigrationRTA.MobileNo_DP, MobileNo));
                add(new CKeyValuePair(ConfigrationRTA.Service_Code_DP, ServiceCode));
                add(new CKeyValuePair(ConfigrationRTA.Service_Name_DP, ServiceName));
                add(new CKeyValuePair(ConfigrationRTA.EmailSmsLang_DP, LanguageCode_DP));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCallNoDialog asyncCall = new AsyncServiceCallNoDialog(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryDigitalpass(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Receipt service
     *
     * @param TransactionID
     * @param CustomerEmail
     * @param ServiceName
     */
    public static void callRTAReceiptService(final String TransactionID, final String CustomerEmail, final String ServiceName
            , final ServiceCallback callback, Context context) {


        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID_Receipt);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_Receipt);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ConfigrationRTA.SERVICE_ID_Receipt);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.TransactionID_Receipt, TransactionID));
                add(new CKeyValuePair(ConfigrationRTA.Customer_TOEmail_Receipt, CustomerEmail));
                add(new CKeyValuePair(ConfigrationRTA.Subject_Receipt, ServiceName));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCallNoDialog asyncCall = new AsyncServiceCallNoDialog(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryRTAReceipt(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }


    public static void callDriverInquiryByLicenseNo(final String serviceId, final String LicenseFrom, final String LicenseNo, final String BirthYear, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the time stamp
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the source application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ConfigurationVLDL.SNInquiryRTADRLDetail);

        // Set the signature filed here
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant ID
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Request
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language Here
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair("licenseSource", LicenseFrom));
                add(new CKeyValuePair("licenseNo", LicenseNo));
                add(new CKeyValuePair(ConfigrationRTA.BIRTH_YEAR, BirthYear));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryLicenseNumberRequest(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /*****Parking Services*****/
    /**
     * Inquiry By Plate Number
     *
     * @param PlateNo
     * @param PlateCode
     * @param PlateCategory
     * @param PlateSource
     * @param callback
     */
    public static void callInquirySeasonalCardVehicleDetails(final String ServiceId, final String ServiceName, final String PlateNo, final String PlateCode, final String PlateCategory, final String PlateSource, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ServiceName);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ServiceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.PLATE_NO, PlateNo));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CODE, PlateCode));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CATEGORY, PlateCategory));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_SOURCE, PlateSource));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquirySeasonalCardVehicleDetails(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Inquiry By Plate Number
     *
     * @param PlateNo
     * @param PlateCode
     * @param PlateCategory
     * @param PlateSource
     * @param callback
     */
    public static void callInquiryValidateSeasonalCard(final String ServiceId, final String ServiceName, final String PlateNo, final String PlateCode, final String PlateCategory, final String PlateSource, final String BranchID, final String RegistrationNo, final String OwnerName, final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // ********* Object For the Headers ***************//

        // Set the version number
        request.setVersion(ConfigrationRTA.VERSION);

        // Set the current Date and time
        request.setTimeStamp(currectDateTime);

        // Set the Terminal Id
        request.setTerminalid(ConfigrationRTA.TERMINAL_ID);

        // Set the Source Application
        request.setSourceApplication(ConfigrationRTA.SOURCE_APPLICATION);

        // Set the Service Name
        request.setServiceName(ServiceName);

        // Set the signature fields
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the service Id
        request.setServiceId(ServiceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigrationRTA.DESCRIPTION);
        login.setLoginId(ConfigrationRTA.LOGIN_ID);
        login.setPassword(ConfigrationRTA.PASSWORD);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigrationRTA.LANGUAGE);

        // Set the Ip
        request.setIpAssigned(ConfigrationRTA.IP);

        // Set the Bank Id
        request.setBankId(ConfigrationRTA.BANK_ID);


        // Set the Body Content Here
        List<CKeyValuePair> ServiceKeyValues = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.BRANCH_ID_VSCI, BranchID));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_NO, PlateNo));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CODE, PlateCode));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_CATEGORY, PlateCategory));
                add(new CKeyValuePair(ConfigrationRTA.PLATE_SOURCE, PlateSource));
                add(new CKeyValuePair(ConfigrationRTA.REGISTRATION_NO_VSCI, RegistrationNo));
                add(new CKeyValuePair(ConfigrationRTA.OWNER_NAME_VSCI, OwnerName));
            }
        };

        request.setCustomerUniqueNo(ServiceKeyValues);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                // Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");

                        // Assign the response to result variable
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryValidateSeasonalCard(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    /**
     * Call Inquiry By TRF Number
     *
     * @param callback -> Request and Response for the service Callback
     */
    public static void callInquiryCardPayment(final ServiceCallback callback, Context context) {

        // Set the Object for the Inquiry
        NipsiCounterInquiryRequest request = new NipsiCounterInquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Terminal ID from Configration file
        request.setTerminalId(ConfigrationRTA.TERMINAL_ID);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHashCardPayment(RequestId, currectDateTime, ConfigrationRTA.TERMINAL_ID));

        // Set the Signature field
        request.setSignatureField(ConfigrationRTA.SIGNATURE_FIELDS_CARD_PAYMENT);


        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        jsonObj = XML.toJSONObject((String) output);
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NipsiCounterInquiryResponse");
                        //JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure("error");
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryCardPayment(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL_CARD_PAYMENT, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION_CARD_PAYMENT);

    }

    /**
     * Call the service where Serial number of the device is passed and in return the values for the payment configuration
     * are returned
     *
     * @param callback           -> Request and Response for the service Callback
     * @param DeviceSerialNumber -> This is the serial number of the device
     */
    public static void callServiceByDeviceSerialNumber(final ServiceCallback callback, Context context, final String DeviceSerialNumber) {

        // Set the Object for the Inquiry
        NipsMerchantDetailsRequest request = new NipsMerchantDetailsRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();
        String currectDateTime = Utilities.currentDateTime();

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Terminal ID from Configration file
        request.setDeviceSerialNumber(DeviceSerialNumber);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHashDeviceSerialNumber(RequestId, currectDateTime, DeviceSerialNumber));

        // Set the Signature field
        request.setSignatureField(ConfigrationRTA.SIGNATURE_FIELDS_DEVICE_SERIAL_NUMBER);


        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NipsMerchantDetailsResponse_MultipleMerchants");
                        //JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                } else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        }, context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponse req = new XMLRequestAndResponse();
        String SOAPRequestXML = req.createInquiryDeviceSerialNumber(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL_CARD_PAYMENT, ServiceUrls.RTA_INQUIRY_SOAP_ACTION_MERCHANT_INQUIRY);

    }
}
