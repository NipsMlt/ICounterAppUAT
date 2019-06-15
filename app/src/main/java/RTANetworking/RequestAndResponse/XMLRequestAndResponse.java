package RTANetworking.RequestAndResponse;

import android.util.Log;

/**
 * Created by raheel on 3/6/2018.
 */

public class XMLRequestAndResponse {

    public String createInquiryTrfNoRequest(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryTrfNoCertRequest(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryServiceCharges(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryPlateNumberRequest(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(), request.getCustomerUniqueNo().get(4).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryTicketNoRequest(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryLicenseNumberRequest(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryServiceCharge(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryRTAVehicleDetailByPlateInfo(InquiryRequest request) {
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(), request.getCustomerUniqueNo().get(4).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    /**
     * Create Transaction Request
     *
     * @param request
     * @return
     */
    public String createTransaction(KioskPaymentRequest request) {

        String ItemsObject = "";

        for (KskServiceItem obj : request.getServiceType().Items) {

            try {
                ItemsObject += String.format("<tem:Items ItemId=\"%s\" ItemText=\"%s\" ItemPaidAmount=\"%s\" IsSelected=\"%s\" ServiceDate=\"%s\"" +
                                " DiscountRate=\"%s\" type=\"%s\" location=\"%s\" points=\"%s\" details=\"%s\" Field1=\"%s\" Field2=\"%s\" Field3=\"%s\" Field4=\"%s\" " +
                                "Field5=\"%s\" Field6=\"%s\" Field7=\"%s\">\n" +
                                "               <!--Optional:-->\n" +
                                "               <tem:PaymentFlag>%s</tem:PaymentFlag>\n" +
                                "               <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                                "               <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                                "               <tem:Dueamount>%s</tem:Dueamount>\n" +
                                "               <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                                "            </tem:Items>\n", obj.ItemId, obj.ItemText, obj.ItemPaidAmount,
                        obj.IsSelected, obj.ServiceDate, obj.DiscountRate, obj.type,
                        obj.location, obj.points, obj.details, obj.Field1, obj.Field2, obj.Field3, obj.Field4, obj.Field5,
                        obj.Field6, obj.Field7, obj.PaymentFlag, obj.MinimumAmount, obj.MaximumAmount, obj.Dueamount, obj.ServiceCharge);

            } catch (Exception Ex) {
                Log.d("The Exception is", Ex.getLocalizedMessage());
            }

        }

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:KioskPaymentRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ServiceType ServiceTypeId=\"%s\" ServiceTitle=\"%s\" Comment=\"%s\">\n" +
                            "            <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                            "            <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                            "            <tem:Dueamount>%s</tem:Dueamount>\n" +
                            "            <tem:PaidAmount>%s</tem:PaidAmount>\n" +
                            "            <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            ItemsObject +
                            "         </tem:ServiceType>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:MerchantRefernceNumber>%s</tem:MerchantRefernceNumber>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ActionDate>%s</tem:ActionDate>\n" +
                            "         <tem:PaymentType>%s</tem:PaymentType>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:BankTransactionId>%s</tem:BankTransactionId>\n" +
                            "         <tem:IsRefundable>%s</tem:IsRefundable>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ServiceAttributesList>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:ServiceAttributesList>\n" +
                            "      </tem:KioskPaymentRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().getDescription(), request.getLogin().getLoginId(), request.getLogin().getPassword(),
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),

                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),

                    request.getServiceType().getServiceTypeId(), request.getServiceType().getServiceTitle(), request.getServiceType().getComment(),

                    request.getServiceType().getMinimumAmount(), request.getServiceType().getMaximumAmount(), request.getServiceType().getDueamount(),
                    request.getServiceType().getPaidAmount(), request.getServiceType().getServiceCharge(),

                    request.getMerchantRefernceNumber(), request.getActionDate(), request.getPaymentType(), request.getBankTransactionId(), request.getIsRefundable(),

                    request.getServiceAttributesList().get(0).getKey(), request.getServiceAttributesList().get(0).getValue(),
                    request.getServiceAttributesList().get(1).getKey(), request.getServiceAttributesList().get(1).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    /**
     * Direct Debit Request
     *
     * @param request
     * @return
     */
    public String directDebit(KioskPaymentRequest request) {

        String ItemsObject = "";

        for (KskServiceItem obj : request.getServiceType().Items) {

            try {
                ItemsObject += String.format("<tem:Items ItemId=\"%s\" ItemText=\"%s\" ItemPaidAmount=\"%s\" IsSelected=\"%s\" ServiceDate=\"%s\"" +
                                " DiscountRate=\"%s\" type=\"%s\" location=\"%s\" points=\"%s\" details=\"%s\" Field1=\"%s\" Field2=\"%s\" Field3=\"%s\" Field4=\"%s\" " +
                                "Field5=\"%s\" Field6=\"%s\" Field7=\"%s\">\n" +
                                "               <!--Optional:-->\n" +
                                "               <tem:PaymentFlag>%s</tem:PaymentFlag>\n" +
                                "               <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                                "               <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                                "               <tem:Dueamount>%s</tem:Dueamount>\n" +
                                "               <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                                "            </tem:Items>\n", obj.ItemId, obj.ItemText, obj.ItemPaidAmount,
                        obj.IsSelected, obj.ServiceDate, obj.DiscountRate, obj.type,
                        obj.location, obj.points, obj.details, obj.Field1, obj.Field2, obj.Field3, obj.Field4, obj.Field5,
                        obj.Field6, obj.Field7, obj.PaymentFlag, obj.MinimumAmount, obj.MaximumAmount, obj.Dueamount, obj.ServiceCharge);

            } catch (Exception Ex) {
                Log.d("The Exception is", Ex.getLocalizedMessage());
            }

        }

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:KioskPaymentRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ServiceType ServiceTypeId=\"%s\" ServiceTitle=\"%s\" Comment=\"%s\">\n" +
                            "            <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                            "            <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                            "            <tem:Dueamount>%s</tem:Dueamount>\n" +
                            "            <tem:PaidAmount>%s</tem:PaidAmount>\n" +
                            "            <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            ItemsObject +
                            "         </tem:ServiceType>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:MerchantRefernceNumber>%s</tem:MerchantRefernceNumber>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ActionDate>%s</tem:ActionDate>\n" +
                            "         <tem:PaymentType>%s</tem:PaymentType>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:BankTransactionId>%s</tem:BankTransactionId>\n" +
                            "         <tem:IsRefundable>%s</tem:IsRefundable>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:ServiceAttributesList>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:ServiceAttributesList>\n" +
                            "      </tem:KioskPaymentRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().getDescription(), request.getLogin().getLoginId(), request.getLogin().getPassword(),
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),

                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),

                    request.getServiceType().getServiceTypeId(), request.getServiceType().getServiceTitle(), request.getServiceType().getComment(),

                    request.getServiceType().getMinimumAmount(), request.getServiceType().getMaximumAmount(), request.getServiceType().getDueamount(),
                    request.getServiceType().getPaidAmount(), request.getServiceType().getServiceCharge(),

                    request.getMerchantRefernceNumber(), request.getActionDate(), request.getPaymentType(), request.getBankTransactionId(), request.getIsRefundable(),

                    request.getServiceAttributesList().get(0).getKey(), request.getServiceAttributesList().get(0).getValue(),
                    request.getServiceAttributesList().get(1).getKey(), request.getServiceAttributesList().get(1).getValue(),
                    request.getServiceAttributesList().get(2).getKey(), request.getServiceAttributesList().get(2).getValue(),
                    request.getServiceAttributesList().get(3).getKey(), request.getServiceAttributesList().get(3).getValue(),
                    request.getServiceAttributesList().get(4).getKey(), request.getServiceAttributesList().get(4).getValue(),
                    request.getServiceAttributesList().get(5).getKey(), request.getServiceAttributesList().get(5).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquirySendEmail(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue());


        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryDigitalpass(InquiryRequest request) {
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(), request.getCustomerUniqueNo().get(4).getValue(),
                    request.getCustomerUniqueNo().get(5).getKey(), request.getCustomerUniqueNo().get(5).getValue(),
                    request.getCustomerUniqueNo().get(6).getKey(), request.getCustomerUniqueNo().get(6).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryRTAReceipt(InquiryRequest request) {
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquirySeasonalCardVehicleDetails(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue());


        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryValidateSeasonalCard(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(), request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(), request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(), request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(), request.getCustomerUniqueNo().get(4).getValue(),
                    request.getCustomerUniqueNo().get(5).getKey(), request.getCustomerUniqueNo().get(5).getValue(),
                    request.getCustomerUniqueNo().get(6).getKey(), request.getCustomerUniqueNo().get(6).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryOrderNo(InquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header>\n" +
                            "      <tem:Version>%s</tem:Version>\n" +
                            "      <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "      <tem:Terminalid>%s</tem:Terminalid>\n" +
                            "      <tem:SourceApplication>%s</tem:SourceApplication>\n" +
                            "      <tem:SignatureFields>%s</tem:SignatureFields>\n" +
                            "      <tem:ServiceName>%s</tem:ServiceName>\n" +
                            "      <tem:ServiceId>%s</tem:ServiceId>\n" +
                            "      <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "      <tem:RequestType>%s</tem:RequestType>\n" +
                            "      <tem:RequestId>%s</tem:RequestId>\n" +
                            "      <tem:RequestCategory>%s</tem:RequestCategory>\n" +
                            "      <tem:MerchantId>%s</tem:MerchantId>\n" +
                            "      <tem:Login Description=\"%s\" LoginId=\"%s\" Password=\"%s\"/>\n" +
                            "      <tem:Language>%s</tem:Language>\n" +
                            "      <tem:IpAssigned>%s</tem:IpAssigned>\n" +
                            "      <tem:BankId>%s</tem:BankId>\n" +
                            "   </soapenv:Header>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NPGKskInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:CustomerUniqueNo>\n" +
                            "            <!--Zero or more repetitions:-->\n" +
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "         </tem:CustomerUniqueNo>\n" +
                            "         <!--Optional:-->\n" +
                            "        \n" +
                            "      </tem:NPGKskInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>\n", request.getVersion(), request.getTimeStamp(), request.getTerminalid(),
                    request.getSourceApplication(), request.getSignatureFields(), request.getServiceName(), request.getServiceId(),
                    request.getSecureHash(), request.getRequestType(), request.getRequestId(), request.getRequestCategory(),
                    request.getMerchantId(), request.getLogin().Description, request.getLogin().LoginId, request.getLogin().Password,
                    request.getLanguage(), request.getIpAssigned(), request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(), request.getCustomerUniqueNo().get(0).getValue());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String createInquiryCardPayment(NipsiCounterInquiryRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NipsiCounterInquiryRequest>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:RequestId>%s</tem:RequestId>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:TerminalId>%s</tem:TerminalId>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:SignatureField>%s</tem:SignatureField>\n" +
                            "      </tem:NipsiCounterInquiryRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getRequestId(), request.getTerminalId(), request.getTimeStamp(),
                    request.getSecureHash(), request.getSignatureField());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    //Creating XML for the device serial number and more useful values
    public String createInquiryDeviceSerialNumber(NipsMerchantDetailsRequest request) {

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
                            "   <soapenv:Header/>\n" +
                            "   <soapenv:Body>\n" +
                            "      <tem:NipsMerchantDetailsRequest_MultipleMerchants>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:RequestId>%s</tem:RequestId>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:TimeStamp>%s</tem:TimeStamp>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:SecureHash>%s</tem:SecureHash>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:SignatureField>%s</tem:SignatureField>\n" +
                            "         <!--Optional:-->\n" +
                            "         <tem:DeviceSerialNumber>%s</tem:DeviceSerialNumber>\n" +
                            "      </tem:NipsMerchantDetailsRequest_MultipleMerchants>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>", request.getRequestId(), request.getTimeStamp(), request.getSecureHash(),
                    request.getSignatureField(), request.getDeviceSerialNumber());

        } catch (Exception Ex) {
            Log.d("The Exception is", Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }
}
