package RTANetworking.GenericServiceCall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import RTANetworking.Common.ConfigrationRTA;
import RTANetworking.Common.ConfigurationVLDL;
import RTANetworking.Common.EncryptDecrpt;
import RTANetworking.Common.ServiceUrls;
import RTANetworking.Common.Utilities;
import RTANetworking.Interfaces.AsyncResponse;
import RTANetworking.Interfaces.ServiceCallback;
import RTANetworking.Interfaces.ServiceCallbackPayment;
import RTANetworking.RequestAndResponse.CKeyValuePair;
import RTANetworking.RequestAndResponse.InquiryRequest;
import RTANetworking.RequestAndResponse.KioskPaymentRequest;
import RTANetworking.RequestAndResponse.KioskPaymentResponse;
import RTANetworking.RequestAndResponse.KioskPaymentResponseItem;
import RTANetworking.RequestAndResponse.KskServiceItem;
import RTANetworking.RequestAndResponse.XMLRequestAndResponseRMS;
import RTANetworking.RequestAndResponse.XMLRequestAndResponseVLDL;
import fr.arnaudguyon.xmltojsonlib.JsonToXml;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class ServiceLayerRMS {


    /**
     * Call Inquiry By Sales Order Number
     *
     * @param callback -> Request and Response for the service Callback
     * @param OrderNo  -> String Order Number from the user Input
     */
    public static void callInquiryByOrderNo(final String OrderNo, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.SALES_ORDER_ID_KEY, OrderNo));
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
                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryOrderNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By Sales Order Number
     *
     * @param callback -> Request and Response for the service Callback
     */
    public static void callInquiryBySalesOrderDetails(final String OrderNo, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.SALES_ORDER_ID_KEY, OrderNo));
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By Invoice Number
     *
     * @param callback  -> Request and Response for the service Callback
     * @param InvoiceNo -> String Order Number from the user Input
     */
    public static void callInquiryByInvoiceNo(final String InvoiceNo, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.INVOICE_ID_KEY, InvoiceNo));
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By Sales Order Number
     *
     * @param callback -> Request and Response for the service Callback
     */
    public static void callInquiryByInvoiceNoDetails(final String InvoiceNo, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.INVOICE_ID_KEY, InvoiceNo));
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }


    /**
     * Call Inquiry By Invoice Number
     *
     * @param callback   -> Request and Response for the service Callback
     * @param EmiratesID -> String Emirates ID from the user Input
     */
    public static void callInquiryByEmiratesID(final String EmiratesID, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.Emirates_ID_KEY, EmiratesID));
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
                      /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }


    /**
     * Call Inquiry By Invoice Number
     *
     * @param callback     -> Request and Response for the service Callback
     * @param TradeLicense -> String Trade License from the user Input
     */
    public static void callInquiryByTradeLicense(final String TradeLicense, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.Trade_License_ID_KEY, TradeLicense));
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
                        /*Note*/

                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By Invoice Number
     *
     * @param callback -> Request and Response for the service Callback
     */
    public static void callGetRMSEmployeesServiceFees(final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair("", ""));
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
                        /*Note*/

                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryGetEmployeeFees(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call Inquiry By Invoice Number
     *
     * @param callback             -> Request and Response for the service Callback
     * @param EmployeeID           -> String Emirates ID from the user Input
     * @param RMSServiceFeesID     -> String Emirates ID from the user Input
     * @param RMSServiceFeesAmount -> String Emirates ID from the user Input
     */
    public static void callInquiryByCreateSalesOrder(final String EmployeeID, final String RMSServiceFeesID, String RMSServiceFeesAmount, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.EMIRATES_ID_KEY_ES, ""));
                add(new CKeyValuePair(ConfigrationRTA.TRADE_LICENSE_KEY_ES, ""));
                add(new CKeyValuePair(ConfigrationRTA.EMPLOYEE_ID_KEY_ES, EmployeeID));
                add(new CKeyValuePair(ConfigrationRTA.CUSTOMER_ACCOUNT_ES, ""));
                add(new CKeyValuePair(ConfigrationRTA.RMS_SERVICE_FEES_ID_ES, RMSServiceFeesID));
                add(new CKeyValuePair(ConfigrationRTA.RMS_SERVICE_FEES_AMOUNT_ES, RMSServiceFeesAmount));
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
                        /*Note*/

                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createSalesOrderCreateInquiry(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }


    /**
     * Call Inquiry by employee services required values
     *
     * @param callback     -> Request and Response for the service Callback
     * @param TradeLicense -> String Trade License from the user Input
     */
    public static void callInquirybyCreateSalesOrder(final String TradeLicense, final String ServiceName, final String ServiceID, final ServiceCallback callback, Context context) {

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
                add(new CKeyValuePair(ConfigrationRTA.Trade_License_ID_KEY, TradeLicense));
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
                        /*Note*/

                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createInquiryInvoiceNo(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);

    }

    /**
     * Call CreateTransactionInquiry By Sales Order Number and Invoice No
     *
     * @param serviceId     -> Service Id of  this service
     * @param SaleOrderNo   -> This no could be SalesOrderID, InvoiceNo
     * @param MinimumAmount -> Maximum Amount
     * @param Dueamount     -> Due Amount
     * @param PaidAmount    -> PaidAmount
     * @param ServiceCharge -> ServiceCharge
     * @param items         -> Items(the selected list by user)
     * @param callback      -> Call back function for the getting the response
     * @param context       -> Context of the application
     */
    public static void CallCreateTransactionInquiry(String serviceId, String SaleOrderNo, final double MinimumAmount, final double MaximumAmount,
                                                    final double Dueamount, final double PaidAmount, final double ServiceCharge, final KskServiceItem items[],
                                                    final ServiceCallbackPayment callback, Context context) {

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
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
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_RMS);

        // Set the Signature field
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request Type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE_PAYMENT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY_PAYMENT);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Object here
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
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

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.SEARCH_VALUE, SaleOrderNo));
            }
        };

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
        service.setServiceTypeId(serviceId);

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
        service.setItems(items);

        // Set the service Type Object here
        request.setServiceType(service);
        request.setCustomerUniqueNo(CustomerUniqueNum);

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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createTransaction(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    /**
     * Call CreateTransactionInquiry By Emirates ID, Trade License and Employee Services
     *
     * @param serviceId     -> Service Id of  this service
     * @param EmiratesID    -> This no could be EmiratesID, TradeLicenseID or Employee ID
     * @param MinimumAmount -> Maximum Amount
     * @param Dueamount     -> Due Amount
     * @param PaidAmount    -> PaidAmount
     * @param ServiceCharge -> ServiceCharge
     * @param items         -> Items(the selected list by user)
     * @param callback      -> Call back function for the getting the response
     * @param context       -> Context of the application
     */
    public static void CallCreateTransactionInquiryRMSEID(String serviceId, String EmiratesID, final double MinimumAmount, final double MaximumAmount,
                                                          final double Dueamount, final double PaidAmount, final double ServiceCharge, final KskServiceItem items[],
                                                          final ServiceCallbackPayment callback, Context context) {

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
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
        request.setServiceName(ConfigrationRTA.SERVICE_NAME_RMS);

        // Set the Signature field
        request.setSignatureFields(ConfigrationRTA.SIGNATURE_FIELDS);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId, currectDateTime, ConfigrationRTA.MERCHANT_ID, ConfigrationRTA.SOURCE_APPLICATION, ConfigrationRTA.BANK_ID, ConfigrationRTA.SECRET_KEY));

        // Set the Request Type
        request.setRequestType(ConfigrationRTA.REQUEST_TYPE_PAYMENT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigrationRTA.REQUEST_CATEGORY_PAYMENT);

        // Set the Merchant Id
        request.setMerchantId(ConfigrationRTA.MERCHANT_ID);

        // Set the Login Object here
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
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

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigrationRTA.SEARCH_VALUE, EmiratesID));
            }
        };

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
        service.setServiceTypeId(serviceId);

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
        service.setItems(items);

        // Set the service Type Object here
        request.setServiceType(service);
        request.setCustomerUniqueNo(CustomerUniqueNum);

        AsyncServiceCall asyncCall = new AsyncServiceCall(new AsyncResponse() {
            @SuppressLint("LongLogTag")
            @Override
            public void processFinish(Object output) {
                //Log.d("Response From Asynchronous:", (String) output);

                if (output != null) {
                    JSONObject jsonObj = null;

                    try {
                        /*Note*/

                        /*Used an other library for converting the
                        Object to stringXML and then stringXML to JSON because the response has
                        one value type=02348790 which was truncating the leading 0 but now with
                        the help of this library the leading 0 0 hasn't been truncated  */
                        XmlToJson xmlToJson = new XmlToJson.Builder((String) output).build();
                        jsonObj = xmlToJson.toJson();
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("KioskPaymentResponse");
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
        XMLRequestAndResponseRMS req = new XMLRequestAndResponseRMS();
        String SOAPRequestXML = req.createTransactionInquiryForRMSEID(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL, ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }


}