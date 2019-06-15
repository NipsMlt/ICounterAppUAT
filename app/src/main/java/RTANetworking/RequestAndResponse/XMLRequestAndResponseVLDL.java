package RTANetworking.RequestAndResponse;

import android.util.Log;

import RTANetworking.Common.ConfigurationVLDL;

public class XMLRequestAndResponseVLDL {

    public String CreateTransactionInquiry(KioskPaymentRequest request){
        String ItemsObject = "";

        String SOAPRequestXML = null;

        for (KskServiceItem obj : request.getServiceType().Items ) {

            try {
                ItemsObject += String.format( "<tem:Items ItemId=\"%s\" ItemText=\"%s\" ItemPaidAmount=\"%s\" IsSelected=\"%s\" ServiceDate=\"%s\"" +
                                " DiscountRate=\"%s\" type=\"%s\" location=\"%s\" points=\"%s\" details=\"%s\" Field1=\"%s\" Field2=\"%s\" Field3=\"%s\" Field4=\"%s\" " +
                                "Field5=\"%s\" Field6=\"%s\" Field7=\"%s\">\n" +
                                "               <!--Optional:-->\n" +
                                "               <tem:PaymentFlag>%s</tem:PaymentFlag>\n" +
                                "               <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                                "               <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                                "               <tem:Dueamount>%s</tem:Dueamount>\n" +
                                "               <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                                "            </tem:Items>\n",obj.ItemId,obj.ItemText,obj.ItemPaidAmount,
                        obj.IsSelected,obj.ServiceDate,obj.DiscountRate,obj.type,
                        obj.location,obj.points,obj.details,obj.Field1,obj.Field2,obj.Field3,obj.Field4,obj.Field5,
                        obj.Field6,obj.Field7,obj.PaymentFlag,obj.MinimumAmount,obj.MaximumAmount,obj.Dueamount,obj.ServiceCharge);

            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }


        if(request.getCustomerUniqueNo().get(0).getValue().equals(ConfigurationVLDL.SETVICE_ID_VEHICLE_LOSS_DAMAGE)) //211
        {
            try {
                SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                                "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                                "         </tem:ServiceAttributesList>\n" +
                                "      </tem:KioskPaymentRequest>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                        request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                        request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                        request.getMerchantId(),request.getLogin().getDescription(),request.getLogin().getLoginId(),request.getLogin().getPassword(),
                        request.getLanguage(),request.getIpAssigned(),request.getBankId(),

                        request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                        request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                        request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),

                        request.getServiceType().getServiceTypeId(),request.getServiceType().getServiceTitle(),request.getServiceType().getComment(),

                        request.getServiceType().getMinimumAmount(),request.getServiceType().getMaximumAmount(),request.getServiceType().getDueamount(),
                        request.getServiceType().getPaidAmount(),request.getServiceType().getServiceCharge(),

                        request.getMerchantRefernceNumber(),request.getActionDate(),request.getPaymentType(),request.getBankTransactionId(),request.getIsRefundable(),

                        request.getServiceAttributesList().get(0).getKey(),request.getServiceAttributesList().get(0).getValue(),
                        request.getServiceAttributesList().get(1).getKey(),request.getServiceAttributesList().get(1).getValue(),
                        request.getServiceAttributesList().get(2).getKey(),request.getServiceAttributesList().get(2).getValue());

            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }
        else if(request.getCustomerUniqueNo().get(0).getValue().equals(ConfigurationVLDL.SETVICE_ID_DRIVER_LOSS_DAMAGE) || request.getCustomerUniqueNo().get(0).getValue().equals(ConfigurationVLDL.SETVICE_ID_Vehicle_Non_Ownership_Certificate) || request.getCustomerUniqueNo().get(0).getValue().equals(ConfigurationVLDL.SETVICE_ID_Driving_Exp_Certificate)) //104
        {
            try {
                SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                                "         </tem:ServiceAttributesList>\n" +
                                "      </tem:KioskPaymentRequest>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                        request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                        request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                        request.getMerchantId(),request.getLogin().getDescription(),request.getLogin().getLoginId(),request.getLogin().getPassword(),
                        request.getLanguage(),request.getIpAssigned(),request.getBankId(),

                        request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                        request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                        request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),

                        request.getServiceType().getServiceTypeId(),request.getServiceType().getServiceTitle(),request.getServiceType().getComment(),

                        request.getServiceType().getMinimumAmount(),request.getServiceType().getMaximumAmount(),request.getServiceType().getDueamount(),
                        request.getServiceType().getPaidAmount(),request.getServiceType().getServiceCharge(),

                        request.getMerchantRefernceNumber(),request.getActionDate(),request.getPaymentType(),request.getBankTransactionId(),request.getIsRefundable(),

                        request.getServiceAttributesList().get(0).getKey(),request.getServiceAttributesList().get(0).getValue());


            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }
        else if(request.getCustomerUniqueNo().get(0).getValue().equals(ConfigurationVLDL.SETVICE_ID_Insurance_Refund_Certificate)) //216
        {
            try {
                SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                                "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                                "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                                "         </tem:ServiceAttributesList>\n" +
                                "      </tem:KioskPaymentRequest>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                        request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                        request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                        request.getMerchantId(),request.getLogin().getDescription(),request.getLogin().getLoginId(),request.getLogin().getPassword(),
                        request.getLanguage(),request.getIpAssigned(),request.getBankId(),

                        request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                        request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                        request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),

                        request.getServiceType().getServiceTypeId(),request.getServiceType().getServiceTitle(),request.getServiceType().getComment(),

                        request.getServiceType().getMinimumAmount(),request.getServiceType().getMaximumAmount(),request.getServiceType().getDueamount(),
                        request.getServiceType().getPaidAmount(),request.getServiceType().getServiceCharge(),

                        request.getMerchantRefernceNumber(),request.getActionDate(),request.getPaymentType(),request.getBankTransactionId(),request.getIsRefundable(),

                        request.getServiceAttributesList().get(0).getKey(),request.getServiceAttributesList().get(0).getValue(),
                        request.getServiceAttributesList().get(1).getKey(),request.getServiceAttributesList().get(1).getValue(),
                        request.getServiceAttributesList().get(2).getKey(),request.getServiceAttributesList().get(2).getValue(),
                        request.getServiceAttributesList().get(3).getKey(),request.getServiceAttributesList().get(3).getValue());


            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }
        else{
            try {
                SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                                "</soapenv:Envelope>",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                        request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                        request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                        request.getMerchantId(),request.getLogin().getDescription(),request.getLogin().getLoginId(),request.getLogin().getPassword(),
                        request.getLanguage(),request.getIpAssigned(),request.getBankId(),

                        request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                        request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                        request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),

                        request.getServiceType().getServiceTypeId(),request.getServiceType().getServiceTitle(),request.getServiceType().getComment(),

                        request.getServiceType().getMinimumAmount(),request.getServiceType().getMaximumAmount(),request.getServiceType().getDueamount(),
                        request.getServiceType().getPaidAmount(),request.getServiceType().getServiceCharge(),

                        request.getMerchantRefernceNumber(),request.getActionDate(),request.getPaymentType(),request.getBankTransactionId(),request.getIsRefundable(),

                        request.getServiceAttributesList().get(0).getKey(),request.getServiceAttributesList().get(0).getValue(),
                        request.getServiceAttributesList().get(1).getKey(),request.getServiceAttributesList().get(1).getValue());

            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }


        return SOAPRequestXML;
    }

    public String RTAAvailableDeliveryMethod(InquiryRequest request){
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                            "</soapenv:Envelope>\n",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                    request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                    request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                    request.getMerchantId(),request.getLogin().Description,request.getLogin().LoginId,request.getLogin().Password,
                    request.getLanguage(),request.getIpAssigned(),request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue());

        } catch ( Exception Ex ) {
            Log.d("The Exception is",Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String RTASetAvailableDeliveryInquiry(InquiryRequest request){
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                            "            <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
                            "             <tem:CKeyValuePair Key=\"%s\" Value=\"%s\"/>\n" +
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
                            "</soapenv:Envelope>\n",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                    request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                    request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                    request.getMerchantId(),request.getLogin().Description,request.getLogin().LoginId,request.getLogin().Password,
                    request.getLanguage(),request.getIpAssigned(),request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),
                    request.getCustomerUniqueNo().get(3).getKey(),request.getCustomerUniqueNo().get(3).getValue(),
                    request.getCustomerUniqueNo().get(4).getKey(),request.getCustomerUniqueNo().get(4).getValue(),
                    request.getCustomerUniqueNo().get(5).getKey(),request.getCustomerUniqueNo().get(5).getValue(),
                    request.getCustomerUniqueNo().get(6).getKey(),request.getCustomerUniqueNo().get(6).getValue(),
                    request.getCustomerUniqueNo().get(7).getKey(),request.getCustomerUniqueNo().get(7).getValue(),
                    request.getCustomerUniqueNo().get(8).getKey(),request.getCustomerUniqueNo().get(8).getValue(),
                    request.getCustomerUniqueNo().get(9).getKey(),request.getCustomerUniqueNo().get(9).getValue(),
                    request.getCustomerUniqueNo().get(10).getKey(),request.getCustomerUniqueNo().get(10).getValue());

        } catch ( Exception Ex ) {
            Log.d("The Exception is",Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String RTAReCertifyTransactionInquiry(KioskPaymentRequest request){
        String ItemsObject = "";

        for (KskServiceItem obj : request.getServiceType().Items ) {

            try {
                ItemsObject += String.format( "<tem:Items ItemId=\"%s\" ItemText=\"%s\" ItemPaidAmount=\"%s\" IsSelected=\"%s\" ServiceDate=\"%s\"" +
                                " DiscountRate=\"%s\" type=\"%s\" location=\"%s\" points=\"%s\" details=\"%s\" Field1=\"%s\" Field2=\"%s\" Field3=\"%s\" Field4=\"%s\" " +
                                "Field5=\"%s\" Field6=\"%s\" Field7=\"%s\">\n" +
                                "               <!--Optional:-->\n" +
                                "               <tem:PaymentFlag>%s</tem:PaymentFlag>\n" +
                                "               <tem:MinimumAmount>%s</tem:MinimumAmount>\n" +
                                "               <tem:MaximumAmount>%s</tem:MaximumAmount>\n" +
                                "               <tem:Dueamount>%s</tem:Dueamount>\n" +
                                "               <tem:ServiceCharge>%s</tem:ServiceCharge>\n" +
                                "            </tem:Items>\n",obj.ItemId,obj.ItemText,obj.ItemPaidAmount,
                        obj.IsSelected,obj.ServiceDate,obj.DiscountRate,obj.type,
                        obj.location,obj.points,obj.details,obj.Field1,obj.Field2,obj.Field3,obj.Field4,obj.Field5,
                        obj.Field6,obj.Field7,obj.PaymentFlag,obj.MinimumAmount,obj.MaximumAmount,obj.Dueamount,obj.ServiceCharge);

            } catch ( Exception Ex ) {
                Log.d("The Exception is",Ex.getLocalizedMessage());
            }
        }

        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                            "      </tem:KioskPaymentRequest>\n" +
                            "   </soapenv:Body>\n" +
                            "</soapenv:Envelope>",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                    request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                    request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                    request.getMerchantId(),request.getLogin().getDescription(),request.getLogin().getLoginId(),request.getLogin().getPassword(),
                    request.getLanguage(),request.getIpAssigned(),request.getBankId(),

                    request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue(),

                    request.getServiceType().getServiceTypeId(),request.getServiceType().getServiceTitle(),request.getServiceType().getComment(),

                    request.getServiceType().getMinimumAmount(),request.getServiceType().getMaximumAmount(),request.getServiceType().getDueamount(),
                    request.getServiceType().getPaidAmount(),request.getServiceType().getServiceCharge(),

                    request.getMerchantRefernceNumber(),request.getActionDate(),request.getPaymentType(),request.getBankTransactionId(),request.getIsRefundable());


        } catch ( Exception Ex ) {
            Log.d("The Exception is",Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String InquiryToServicesCharges(InquiryRequest request){
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                            "</soapenv:Envelope>\n",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                    request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                    request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                    request.getMerchantId(),request.getLogin().Description,request.getLogin().LoginId,request.getLogin().Password,
                    request.getLanguage(),request.getIpAssigned(),request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue(),
                    request.getCustomerUniqueNo().get(2).getKey(),request.getCustomerUniqueNo().get(2).getValue());

        } catch ( Exception Ex ) {
            Log.d("The Exception is",Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }

    public String RTACertificateLookUp(InquiryRequest request){
        String SOAPRequestXML = null;

        try {
            SOAPRequestXML = String.format( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\">\n" +
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
                            "</soapenv:Envelope>\n",request.getVersion(),request.getTimeStamp(),request.getTerminalid(),
                    request.getSourceApplication(),request.getSignatureFields(),request.getServiceName(),request.getServiceId(),
                    request.getSecureHash(),request.getRequestType(),request.getRequestId(),request.getRequestCategory(),
                    request.getMerchantId(),request.getLogin().Description,request.getLogin().LoginId,request.getLogin().Password,
                    request.getLanguage(),request.getIpAssigned(),request.getBankId(),
                    request.getCustomerUniqueNo().get(0).getKey(),request.getCustomerUniqueNo().get(0).getValue(),
                    request.getCustomerUniqueNo().get(1).getKey(),request.getCustomerUniqueNo().get(1).getValue());

        } catch ( Exception Ex ) {
            Log.d("The Exception is",Ex.getLocalizedMessage());
        }

        return SOAPRequestXML;
    }
}