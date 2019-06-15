package RTANetworking.GenericServiceCall;

import android.annotation.SuppressLint;
import android.util.Log;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

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
import RTANetworking.RequestAndResponse.XMLRequestAndResponse;
import RTANetworking.RequestAndResponse.XMLRequestAndResponseVLDL;

public class ServiceLayerVLDL {

    public static void CallCreateTransactionInquiry(String serviceId, String trfNo, String setviceCode, String chassisNo, String isMortgage,String isLost, final double minimumAmount, final double maximumAmount, final double dueAmount, final double paidAmount, final double serviceCharge, final KskServiceItem items[], final String totalAmount, final ServiceCallbackPayment callback, Context context){

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNCreateTransactionInquiry);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestTypePMT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategoryPMT);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.SetviceCode,setviceCode));
                add(new CKeyValuePair(ConfigurationVLDL.TrfNo,trfNo));
                add(new CKeyValuePair(ConfigurationVLDL.IsFine,"2"));
            }
        };

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigurationVLDL.CTICashPaymentType);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        KioskPaymentResponseItem kioskPaymentResponseItem = new KioskPaymentResponseItem();
        //get the items
        KioskPaymentResponse kioskPaymentResponse = new KioskPaymentResponse();

        // Service Type Id
        service.setServiceTypeId(serviceId);

        // Payment Inquiry
        service.setServiceTitle(ConfigurationVLDL.CTIServiceTitle);

        // Comment
        service.setComment("");

        // Minimum Amount
        service.setMinimumAmount(minimumAmount);

        // Maximum Amount
        service.setMaximumAmount(maximumAmount);

        // Due Amount
        service.setDueamount(dueAmount);

        // Paid Amount
        service.setPaidAmount(paidAmount);

        // Service Charge
        service.setServiceCharge(serviceCharge);

        // Set the Items Array here
        service.setItems(items);

        // Set the service Type Object here
        request.setServiceType(service);

        List<CKeyValuePair> ServiceAttrLst;
        if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_VEHICLE_LOSS_DAMAGE)) //211
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.ChasissNoLD,chassisNo));
                    add(new CKeyValuePair(ConfigurationVLDL.issueType,isMortgage));
                    add(new CKeyValuePair(ConfigurationVLDL.IsLost, isLost));
                }
            };
        }
        else if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_DRIVER_LOSS_DAMAGE)) //104
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.reprintReason, isLost));
                }
            };
        }
        else if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_Vehicle_Ownership_Certificate)) //214
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.ChasissNo,chassisNo));
                    add(new CKeyValuePair(ConfigurationVLDL.cauCode,isLost));
                }
            };
        }
        else if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_Vehicle_Non_Ownership_Certificate)) //215
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.cauCode,isLost));
                }
            };
        }
        else if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_Clearance_Certificate)) //213
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.ChasissNo,chassisNo));
                    add(new CKeyValuePair(ConfigurationVLDL.sddiLoggedInTrafficNo,trfNo));
                }
            };
        }
        else if(setviceCode.equals(ConfigurationVLDL.SETVICE_ID_Driving_Exp_Certificate)) //105
        {
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.licenseNo,chassisNo));
                }
            };
        }
        else
        {
            // Set the Service Attribute List Content Here
            ServiceAttrLst = new ArrayList<CKeyValuePair>() {
                {
                    add(new CKeyValuePair(ConfigurationVLDL.ChasissNo,chassisNo));
                    add(new CKeyValuePair(ConfigurationVLDL.IsMortgaged,isMortgage));
                }
            };
        }


        request.setCustomerUniqueNo(CustomerUniqueNum);
        request.setServiceAttributesList(ServiceAttrLst);

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

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.CreateTransactionInquiry(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    public static void CallRTAAvailableDeliveryMethod(String serviceId, String transactionId, final ServiceCallback callback, Context context){
        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNRTAAvailableDeliveryMethod);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestType);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategory);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.TransactionID,transactionId));
            }
        };

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
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.RTAAvailableDeliveryMethod(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void CallRTASetAvailableDeliveryInquiry(String serviceId, String transactionId, String centerId, final ServiceCallback callback, Context context){
        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNRTAsetAvailableDeliveryInquiry);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestType);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategory);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.TransactionID,transactionId));
                add(new CKeyValuePair(ConfigurationVLDL.ProcessingCenterID,centerId));
                add(new CKeyValuePair(ConfigurationVLDL.TrsMode,"6"));
                add(new CKeyValuePair(ConfigurationVLDL.PrefEmail,""));
                add(new CKeyValuePair(ConfigurationVLDL.PrefMobile,""));
                add(new CKeyValuePair(ConfigurationVLDL.TrsDeliveryDate,""));
                add(new CKeyValuePair(ConfigurationVLDL.PrefContactName,""));
                add(new CKeyValuePair(ConfigurationVLDL.PrefAddressLine1,""));
                add(new CKeyValuePair(ConfigurationVLDL.PrefAddressLine2,""));
                add(new CKeyValuePair(ConfigurationVLDL.PrefArea,""));
                add(new CKeyValuePair(ConfigurationVLDL.EmiCode,""));
            }
        };

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
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.RTASetAvailableDeliveryInquiry(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void CallRTAReCertifyTransactionInquiry(String serviceId, String transactionId, String trfNo, String setviceCode,final double minimumAmount, final double maximumAmount, final double dueAmount, final double paidAmount, final double serviceCharge, final KskServiceItem items[], final String totalAmount, final ServiceCallback callback, Context context){
        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNRTAReCertifyTransactionInquiry);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestTypePMT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategoryPMT);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.SetviceCode,setviceCode));
                add(new CKeyValuePair(ConfigurationVLDL.TrfNo,trfNo));
                add(new CKeyValuePair(ConfigurationVLDL.TransactionID,transactionId));
            }
        };

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigurationVLDL.RCTICashPaymentType);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        // Service Type Id
        service.setServiceTypeId(serviceId);

        // Payment Inquiry
        service.setServiceTitle(ConfigurationVLDL.RCTIServiceTitle);

        // Comment
        service.setComment("");

        // Minimum Amount
        service.setMinimumAmount(minimumAmount);

        // Maximum Amount
        service.setMaximumAmount(maximumAmount);

        // Due Amount
        service.setDueamount(dueAmount);

        // Paid Amount
        service.setPaidAmount(paidAmount);

        // Service Charge
        service.setServiceCharge(serviceCharge);

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

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.RTAReCertifyTransactionInquiry(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

    public static void CallInquiryToServicesCharges(String serviceId,final double amount, String serviceCode, String transactionId, final ServiceCallback callback, Context context){
        // Set the Object for the Inquiry
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNInquiryToServicesCharges);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestType);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategory);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.Amount,String.valueOf(amount)));
                add(new CKeyValuePair(ConfigurationVLDL.ServiceCode,serviceCode));
                add(new CKeyValuePair(ConfigurationVLDL.TransactionID,transactionId));
            }
        };

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
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.InquiryToServicesCharges(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void CallRTACertificatesLookupInsurance(final ServiceCallback callback, Context context) {
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNRTA_CertificateLookup);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId("132");

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestType);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategory);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair("lookupName","ORGANIZATIONS"));
                add(new CKeyValuePair("conditionCode","INSURANCE"));
            }
        };

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
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.RTACertificateLookUp(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void CallRTACertificatesLookupVehicle(final ServiceCallback callback, Context context) {
        InquiryRequest request = new InquiryRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.CLUTerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNRTA_CertificateLookup);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId("132");

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestType);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategory);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        InquiryRequest.Login login = new InquiryRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair("lookupName","TF_CML_AUTHORITIES"));
                add(new CKeyValuePair("conditionCode","VEHICLES"));
            }
        };

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
                        JSONObject body = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Body").getJSONObject("NPGKskInquiryResponse");
                        JSONObject header = jsonObj.getJSONObject("s:Envelope").getJSONObject("s:Header");
                        callback.onSuccess(body);

                    } catch (JSONException e) {
                        Log.e("JSON exception", e.getMessage());
                        callback.onFailure(e.getLocalizedMessage());
                        e.printStackTrace();
                    }

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.RTACertificateLookUp(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_INQUIRY_URL_SOAP_ACTION);
    }

    public static void CallCreateTransactionInquiry(String serviceId, String trfNo, String setviceCode, String chassisNo, String policyNumber,String insuranceExpiryDate, String insuranceCompanyID, final double minimumAmount, final double maximumAmount, final double dueAmount, final double paidAmount, final double serviceCharge, final KskServiceItem items[], final String totalAmount, final ServiceCallbackPayment callback, Context context){

        // Set the Object for the Inquiry
        KioskPaymentRequest request = new KioskPaymentRequest();

        // ********* Object For the Headers ***************//

        // Get the Random Generated UUID
        String RequestId = Utilities.shortUUID();

        //Get the current Datetime
        String currectDateTime = Utilities.currentDateTime();

        // Set the Version Number Here From Constant File
        request.setVersion(ConfigurationVLDL.Version);

        // Take the current date time format
        request.setTimeStamp(currectDateTime);

        // Set the Terminal ID from Configration file
        request.setTerminalid(ConfigurationVLDL.TerminalId);

        // Set the Source Application
        request.setSourceApplication(ConfigurationVLDL.SourceApplication);

        // Set the service Name
        request.setServiceName(ConfigurationVLDL.SNCreateTransactionInquiry);

        // Set the Signature field
        request.setSignatureFields(ConfigurationVLDL.SignatureFields);

        // Set the Service Id
        request.setServiceId(serviceId);

        // Set the secure hash
        request.setSecureHash(EncryptDecrpt.GetSecureHash(RequestId,currectDateTime,ConfigurationVLDL.MerchantId,ConfigurationVLDL.SourceApplication,ConfigurationVLDL.BankId,ConfigurationVLDL.SecretKey));

        // Set the Request Type
        request.setRequestType(ConfigurationVLDL.RequestTypePMT);

        // Set the Request Id
        request.setRequestId(RequestId);

        // Set the Request Category
        request.setRequestCategory(ConfigurationVLDL.RequestCategoryPMT);

        // Set the Merchant Id
        request.setMerchantId(ConfigurationVLDL.MerchantId);

        // Set the Login Object here
        KioskPaymentRequest.Login login = new KioskPaymentRequest.Login();
        login.setDescription(ConfigurationVLDL.Description);
        login.setLoginId(ConfigurationVLDL.LoginId);
        login.setPassword(ConfigurationVLDL.Password);
        request.setLogin(login);

        // Set the Language
        request.setLanguage(ConfigurationVLDL.Language);

        // Set the Ip Assigned
        request.setIpAssigned(ConfigurationVLDL.IP);

        // Set the Bank Id
        request.setBankId(ConfigurationVLDL.BankId);

        // Set the CustomerUniqueNo Content Here
        List<CKeyValuePair> CustomerUniqueNum = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.SetviceCode,setviceCode));
                add(new CKeyValuePair(ConfigurationVLDL.TrfNo,trfNo));
                add(new CKeyValuePair(ConfigurationVLDL.IsFine,"2"));
            }
        };

        // Merchant Reference Number
        request.setMerchantRefernceNumber("");

        // Action Date
        request.setActionDate(currectDateTime);

        //Payment Type
        request.setPaymentType(ConfigurationVLDL.CTICashPaymentType);

        // Bank Transaction Id
        request.setBankTransactionId(RequestId);

        // Is Refundable
        request.setIsRefundable("false");

        // Set the Login
        KioskPaymentRequest.ServiceType service = new KioskPaymentRequest.ServiceType();

        KioskPaymentResponseItem kioskPaymentResponseItem = new KioskPaymentResponseItem();
        //get the items
        KioskPaymentResponse kioskPaymentResponse = new KioskPaymentResponse();

        // Service Type Id
        service.setServiceTypeId(serviceId);

        // Payment Inquiry
        service.setServiceTitle(ConfigurationVLDL.CTIServiceTitle);

        // Comment
        service.setComment("");

        // Minimum Amount
        service.setMinimumAmount(minimumAmount);

        // Maximum Amount
        service.setMaximumAmount(maximumAmount);

        // Due Amount
        service.setDueamount(dueAmount);

        // Paid Amount
        service.setPaidAmount(paidAmount);

        // Service Charge
        service.setServiceCharge(serviceCharge);

        // Set the Items Array here
        service.setItems(items);

        // Set the service Type Object here
        request.setServiceType(service);

        List<CKeyValuePair> ServiceAttrLst;

        ServiceAttrLst = new ArrayList<CKeyValuePair>() {
            {
                add(new CKeyValuePair(ConfigurationVLDL.ChasissNoLD,chassisNo));
                add(new CKeyValuePair(ConfigurationVLDL.policyNumber,policyNumber));
                add(new CKeyValuePair(ConfigurationVLDL.insuranceExpiryDate, insuranceExpiryDate));
                add(new CKeyValuePair(ConfigurationVLDL.insuranceCompanyId,insuranceCompanyID));
            }
        };

        request.setCustomerUniqueNo(CustomerUniqueNum);
        request.setServiceAttributesList(ServiceAttrLst);

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

                }
                else {
                    Log.d("Response From Asynchronous:", "error");
                    callback.onFailure("error");
                }
            }
        },context);

        // Pass the Object Here and Get the XML
        XMLRequestAndResponseVLDL req = new XMLRequestAndResponseVLDL();
        String SOAPRequestXML = req.CreateTransactionInquiry(request);

        // Pass the Object Here For the Service
        asyncCall.execute(SOAPRequestXML, ServiceUrls.RTA_INQUIRY_URL,ServiceUrls.RTA_PAYMENT_URL_SOAP_ACTION);
    }

}