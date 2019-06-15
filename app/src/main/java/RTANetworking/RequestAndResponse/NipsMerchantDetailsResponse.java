package RTANetworking.RequestAndResponse;

import java.io.Serializable;


/**
 * Created by talal on 3/6/2018.
 */

/**
 * Inquiry Request Class Models
 */


public class NipsMerchantDetailsResponse implements Serializable {

    //Request Id
    private String RequestId;

    //Terminal ID
    private String TerminalId;

    //Curent TimeStamp
    private String TimeStamp;

    //Secure hash to be created for every request
    private String SecureHash;

    //Signature Fields to create the hash
    private String SignatureField;

    //Reason Code
    private String ReasonCode;

    //Message
    private String Message;

    //DeviceSerialNumber
    private String DeviceSerialNumber;

    //MerchantId
    private String MerchantId;

    //LoginId
    private String LoginId;

    //Password
    private String Password;

    //SecretKey
    private String SecretKey;

    //BankId
    private String BankId;

    //PaymentUserId
    private String PaymentUserId;

    //PaymentPassword
    private String PaymentSecretKey;

    private String PaymentPassword;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getTerminalId() {
        return TerminalId;
    }

    public void setTerminalId(String terminalId) {
        TerminalId = terminalId;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSecureHash() {
        return SecureHash;
    }

    public void setSecureHash(String secureHash) {
        SecureHash = secureHash;
    }

    public String getSignatureField() {
        return SignatureField;
    }

    public void setSignatureField(String signatureField) {
        SignatureField = signatureField;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDeviceSerialNumber() {
        return DeviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        DeviceSerialNumber = deviceSerialNumber;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSecretKey() {
        return SecretKey;
    }

    public void setSecretKey(String secretKey) {
        SecretKey = secretKey;
    }

    public String getBankId() {
        return BankId;
    }

    public void setBankId(String bankId) {
        BankId = bankId;
    }

    public String getPaymentUserId() {
        return PaymentUserId;
    }

    public void setPaymentUserId(String paymentUserId) {
        PaymentUserId = paymentUserId;
    }


    public String getPaymentSecretKey() {
        return PaymentSecretKey;
    }

    public void setPaymentSecretKey(String paymentSecretKey) {
        PaymentSecretKey = paymentSecretKey;
    }

    public String getPaymentPassword() {
        return PaymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        PaymentPassword = paymentPassword;
    }
}
