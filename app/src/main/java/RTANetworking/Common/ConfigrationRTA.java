package RTANetworking.Common;

import android.content.Context;

import RTANetworking.Interfaces.MerchantDetailsCallback;
import rtaservices.RTAMainServices;
import utility.PreferenceConnector;

/**
 * Created by raheel on 3/26/2018.
 */

public class ConfigrationRTA implements MerchantDetailsCallback {

    private RTAMainServices rtaMainServices = new RTAMainServices();
    //TO make it generic
    //private MerchantConfiguration merchantConfiguration = new MerchantConfiguration();

    public ConfigrationRTA(Context context) {
        rtaMainServices.setMerchantDetailsCallBackMethod(this);
    }

    /***************These are the values fetched from the merchant detials service**************/
    //Secret Key
    public static String SECRET_KEY;//Secure hash for other than payment

    // Terminal Id
    public static String TERMINAL_ID; //will vary per device

    // Merchant ID
    public static String MERCHANT_ID; //RTA0000001

    // Login Id
    public static String LOGIN_ID;

    // Password
    public static String PASSWORD;

    //DeviceSerialNumber
    public static String DEVICE_SERIAL_NUMBER;

    //Payment Merchant ID
    public static String PAYMENT_MERCHANT_ID;

    //Payment User ID
    public static String PAYMENT_USER_ID;

    //Payment Password
    public static String PAYMENT_PASSWORD;

    //Payment secret key
    public static String PAYMENT_SECRET_KEY; //"#pRoD_ksk_key_RTA!@ELLE"; //"nips@2016%" <- for 3dsecure payment;

    // Bank Id
    public static String BANK_ID; //DIB

    // ******************* Branch Id *********************//
    public static final String BRANCH_ID = "819";

    // Version Number
    public static final String VERSION = "1.0";

    // Source Application
    public static String SOURCE_APPLICATION = ""; //"KSK";

    // Signature Field
    public static final String SIGNATURE_FIELDS = "RequestId,TimeStamp,MerchantId,SourceApplication,BankId";

    // Signature Fields for card payment
    public static final String SIGNATURE_FIELDS_CARD_PAYMENT = "RequestId,TimeStamp,TerminalId";

    // Signature Fields for Device Serial Number
    public static final String SIGNATURE_FIELDS_DEVICE_SERIAL_NUMBER = "RequestId,TimeStamp,DeviceSerialNumber";

    // Request Type
    public static final String REQUEST_TYPE = "REQ_KSK_INQ";

    // Request Category
    public static final String REQUEST_CATEGORY = "Inquiry";

    // Description
    public static final String DESCRIPTION = "";

    // Language
    public static final String LANGUAGE = "EN";

    // Ip
    public static final String IP = "127.0.0.1";


    //********* Inquiry By Send Email ***********//

    // CustomerEmail
    public static final String Customer_Email = "CustomerEmail";

    // EmailBody
    public static final String Email_Body = "EmailBody";

    // Subject
    public static final String Subject = "Subject";

    //********* Digital Pass ***********//

    // Terminal Id
    public static String TERMINAL_ID_DP = "";// TERMINAL_ID;//"61100007";
    // Service Name
    public static final String SERVICE_NAME_DP = "ICOUNTER_DigitalPassInquiry";

    // Service ID
    public static final String SERVICE_ID_DP = "503";
    // CustomerName
    public static final String Customer_Name_DP = "CustomerName";
    // CustomerEmail
    public static final String Customer_Email_DP = "Email";

    // EmailBody
    public static final String Transaction_Number_DP = "TransactionNumber";

    // Subject
    public static final String MobileNo_DP = "MobileNo";
    // Subject
    public static final String Service_Code_DP = "ServiceCode";
    // Subject
    public static final String Service_Name_DP = "ServiceName";
    //EmailSmsLang
    public static final String EmailSmsLang_DP = "EmailSmsLang";


    //********* RTA Receipt ***********//

    // Terminal Id
    public static String TERMINAL_ID_Receipt = "";//TERMINAL_ID;//"61100007";

    // Service Name
    public static final String SERVICE_NAME_Receipt = "DTC_ReceiptInquiry";

    // Service ID
    public static final String SERVICE_ID_Receipt = "501";

    // CustomerEmail
    public static final String Customer_TOEmail_Receipt = "TOEmail";

    // EmailBody
    public static final String TransactionID_Receipt = "TransactionID";

    // Subject
    public static final String Subject_Receipt = "Subject";


    //********* Inquiry By TRF Number ***********//

    // Service Name
    public static final String SERVICE_NAME_TRF = "InquiryRTATrafficFineByTrfNo";

    // Service ID
    public static final String SERVICE_ID_TRF = "101";

    // TRF Number
    public static final String TRF_NO = "TrfNo";

    // Birth Year
    public static final String BIRTH_YEAR = "BirthYear";

    //SearchBy
    public static final String SEARCH_BY = "SearchBy";

    //********* Inquiry By License Number ***********//

    // Service Name
    public static final String SERVICE_NAME_LICENSE = "InquiryRTATrafficFineByLicenseInfo";

    // Service Id
    public static final String SERVICE_ID_LICENSE = "102";

    // LicenseFrom
    public static final String LICENSE_FROM = "LicenseFrom";

    // LicenseNo
    public static final String LICENSE_NUMBER = "LicenseNo";

    //********* Inquiry By Plate Number ***********//

    // Service Name
    public static final String SERVICE_NAME_PLATE = "InquiryRTATrafficFineByPlateInfo";

    // Service Id
    public static final String SERVICE_ID_PLATE = "103";

    // Plate Number
    public static final String PLATE_NO = "PlateNo";

    // Plate Code
    public static final String PLATE_CODE = "PlateCode";

    // Plate Category
    public static final String PLATE_CATEGORY = "PlateCategory";

    // Plate Source
    public static final String PLATE_SOURCE = "PlateSource";

    //********* Inquiry By Ticket Info Number ***********//

    // Service Name
    public static final String SERVICE_NAME_TICKET_INFO = "InquiryRTATrafficFineByTicketInfo";

    // SERVICE ID TICKET INFO
    public static final String SERVICE_ID_TICKET_INFO = "104";

    // BENEFICIARY CODE
    public static final String BENEFICIARY_CODE = "BeneficiaryCode";

    // Ticket No
    public static final String TICKET_NUMBER = "TicketNo";

    // Ticket Year
    public static final String TICKET_YEAR = "TicketYear";

    //********* Inquiry of Service Charges ***********//

    // Service Name
    public static final String SERVICE_NAME_SERVICE_CHARGES = "InquiryToServicesCharges";

    // SERVICE ID
    public static final String SERVICE_ID_SERVICE_CHARGES = "039";

    // Amount
    public static final String SERVICE_CHARGES_AMOUNT = "Amount";

    // Service Code
    public static final String SERVICE_CHARGES_SERVICE_CODE = "serviceCode";

    // Service Code value
    public static final String SERVICE_CHARGES_SERVICE_CODE_VALUE = "301"; //103 is for driving license

    // Transaction ID
    public static final String SERVICE_CHARGES_TRANSACTION_ID = "TransactionID";


    // ******************************* Payment Configration **************************** //

    // Request Category Payment
    public static final String REQUEST_CATEGORY_PAYMENT = "Payment";

    // Request Type
    public static String REQUEST_TYPE_PAYMENT = ""; //"REQ_KSK_PMT";

    //******************** Create Transaction ****************************************//

    // Service Id Create Transaction
    public static final String SERVICE_ID_CREATE_TRANSACTION = "108";

    // Service Name
    public static final String SERVICE_NAME_CREATE_TRANSACTION = "CreateFineTransactionInquiry";

    // Set Vice COde
    public static final String SET_VICE_CODE = "setviceCode";

    // Set Vice Code Value For Fine
    public static final String SET_VICE_CODE_FINE = "301";

    // Traffic File Number
    public static final String TRAFFIC_FILE_NUMBER = "trafficFileNo";

    // Is FIne
    public static final String IS_FINE = "isFine";

    // Is FIne Value
    public static final String IS_FINE_VALUE = "1";

    // Payment Type
    public static final String PAYMENT_TYPE = "Cash";

    // Service Type Id
    public static final String SERVICE_TYPE_ID = "101";

    // Service Title
    public static final String SERVICE_TITLE = "PaymentInquiry";

    // Total Amount
    public static final String TOTAL_AMOUNT = "TotalAmount";

    //Total Amount with service service charge added
    public static final String TOTAL_AMOUNT_WITH_SERVICECHARGE = "TotalAmountwithservicecharge";


    //******************** Direct Debit ****************************************//

    // Service Id Direct Debit
    public static final String SERVICE_ID_DIRECT_DEBIT = "116";

    // Service Name
    public static final String SERVICE_NAME_DIRECT_DEBIT = "RTADirectAccountInquiry";

    // TransactionID services attribute list
    public static final String TRANSACTION_ID_DA = "transactionID";
    //TransactionID CKvalue pair
    public static final String TRANSACTION_ID = "TransactionID";

    // Payment Type
    public static final String PAYMENT_TYPE_CREDIT_CARD = "CreditCard";

    // CCNR
    public static final String CC_NR = "CCNR";

    // CCHOLDERNAME
    public static final String CC_HOLDER_NAME = "CCHOLDERNAME";

    // CCBRAND
    public static final String CC_BRAND = "CCBRAND";

    //Transaction Date
    public static final String TRANSACTION_DATE_DA = "transactionDate";

    //Transaction Amount
    public static final String TRANSACTION_AMOUNT_DA = "transactionAmount";


    /*********Direct Account for RMS*********/
    // TransactionID
    public static final String TRANSACTION_ID_DA_RMS = "transactionID";

    //Bank TransactionID
    public static final String BANKTRANSACTION_ID_DA_RMS = "bankTransactionID";

    //Bank Service Charges
    public static final String SERVICE_CHARGES_DA_RMS = "serviceCharges";

    //Payment Type
    public static final String PAYMENT_TYPE_DA_RMS = "paymentType";

    //Payment Type VALUE=Card
    public static final String PAYMENT_TYPE_DA_RMS_CARD = "Card";

    //********* Inquiry of Parking Services ***********//

    // Service Name Seasonal Card Vehicle Details Inquiry
    public static final String SERVICE_NAME_SCVDI = "SeasonalCardVehicleDetailsInquiry";

    // SERVICE ID
    public static final String SERVICE_ID_SCVDI = "112";

    //ValidateSeasonalCardInquiry
    public static final String SERVICE_NAME_VSCI = "ValidateSeasonalCardInquiry";

    // SERVICE ID
    public static final String SERVICE_ID_VSCI = "113";

    //BRANCH_ID
    public static final String BRANCH_ID_VSCI = "BranchID";

    //REGISTRATION_NO
    public static final String REGISTRATION_NO_VSCI = "RegistrationNo";

    //OWNER_NAME
    public static final String OWNER_NAME_VSCI = "OwnerName";


    //******************** Payment Cyber Source Service ****************************************//

    // Payment Action
    //public static final String PAYMENT_ACTION = "https://3d.networkips.com/Gateway/PG";
    public static String PAYMENT_ACTION = "";//"https://3dsecure.networkips.com/Gateway/PG"; //UAT

    // Request Category
    public static String PAYMENT_REQUEST_CATEGORY = "";//"sale";

    // Source Application
    public static String PAYMENT_SOURCE_APPLICATION = "";//TERMINAL_ID;//The terminal id will be passed as a source application in production

    // Finger Print
    public static String PAYMENT_DEVICE_FINGER_PRINT = ""; //"PC999999";

    // IP Assigned
    public static final String PAYMENT_IP_ASSIGNED = "127.0.0.1";

    // Version
    public static final String PAYMENT_VERSION = "1.0";

    // Service Id
    public static final String PAYMENT_SERVICE_ID = "DTCFineandInquiry";

    // Service Name
    public static final String PAYMENT_SERVICE_NAME = "FineandInquiry";

    // Is ThreeD Secure
    public static String IsThreeDSecure = "";

    // Silent API URL
    public static String SilentAPIURL = "";

    // Payment Currency
    public static String PAYMENT_CURRENCY = "AED";

    // Callback URL
    //public static final String PAYMENT_CALL_BACK_URL = "https://3d.networkips.com/Home/TestMerchantResponse"; //Prod
    public static String PAYMENT_CALL_BACK_URL = ""; //"https://3dsecure.networkips.com/Home/TestMerchantResponse"; //UAT

    // Payment Channel
    public static String PAYMENT_CHANNEL = "ksk";

    // Language
    public static String PAYMENT_LANGUAGE = "EN";

    // Sub Merchant Id
    public static final String PAYMENT_SUB_MERCHANT_ID = "";

    // Signature Fields
    public static final String PAYMENT_SIGNATURE_FIELDS = "RequestId,TimeStamp,ReferenceNumber,MerchantId,Amount";

    // Signature Fields
    public static final String PAYMENT_CARD_TYPE = "RequestId,TimeStamp,ReferenceNumber,MerchantId,Amount";


    //********* Inquiry of RMS Services ***********//

    // Service Name Sales Order ID
    public static final String SERVICE_NAME_RMSORDERNO = "RMS_SalesOrderServiceInquiry";

    // Service Name Invoice No
    public static final String SERVICE_NAME_RMS_INVOICENO = "RMS_SalesInvoiceService";

    // Service Name Emirates ID
    public static final String SERVICE_NAME_RMS_EMIRATESID = "RMS_SalesOrderEID";

    // Service Name TRADE LICENSE
    public static final String SERVICE_NAME_RMS_TRADE_LICENSE = "RMS_SalesOrderTradeLicNum";

    // Service Sales Order Details
    public static final String SERVICE_NAME_RMS_SALESORDERDETAILS = "RMS_SalesOrderDetailsServiceInquiry";

    // Service Invoice No Details
    public static final String SERVICE_NAME_RMS_INVOICENODETAILS = "RMS_SalesInvoiceDetailsService";

    // Service Name GetEmployee Service Fees
    public static final String SERVICE_NAME_RMS_GETEMPLOYEESERVICEFEES = "RMS_GetRMSEmployeesServiceFees";

    // SERVICE_NAME_RMS_EMPLOYEE_SERVICES
    public static final String SERVICE_NAME_RMS_EMPLOYEE_SERVICES = "RMS_SalesOrderCreateInquiry";

    // SERVICE_ID_RMSON
    public static final String SERVICE_ID_RMSON = "147";

    // SERVICE_ID_RMSINVOICENO
    public static final String SERVICE_ID_RMSINVOICENO = "146";

    // SERVICE_ID_RMS Emirates Id
    public static final String SERVICE_ID_RMSEMIRATESID = "150";

    // SERVICE_ID_RMS TRADE LICENSE
    public static final String SERVICE_ID_TRADE_LICENSE = "151";

    // SERVICE_ID_SALESORDERDETAILS
    public static final String SERVICE_ID_SALESORDERDETAILS = "160";

    // SERVICE_ID_INVOICENODETAILS
    public static final String SERVICE_ID_INVOICENODETAILS = "161";

    // SERVICE_ID_GETEMPLOYEESERVICEFEES
    public static final String SERVICE_ID_GETEMPLOYEESERVICEFEES = "164";

    // SERVICE_ID_CREATESALESORDER
    public static final String SERVICE_ID_CREATESALESORDER = "157";

    // Sales Order ID Key in the service
    public static final String SALES_ORDER_ID_KEY = "SalesID";

    //Invoice No Key in the service
    public static final String INVOICE_ID_KEY = "InvoiceID";

    //Emirates ID Key in the service
    public static final String Emirates_ID_KEY = "EmiratesID";

    //Trade License Key in the service
    public static final String Trade_License_ID_KEY = "TradeLicNo";

    //RMS SalesOrderID
    public static final String RMS_SalesOrderID = "SalesOrderID";

    //RMS InvoiceNo
    public static final String RMSInvoiceNo = "InvoiceNo";

    //RMS Emirates ID
    public static final String RMSEmiratesID = "EmiratesId";
    //RMS Emirates ID
    public static final String RMSTradeLicenseID = "TradeLicenseID";

    //RMS Create Transaction Request
    public static final String SERVICE_ID_RMS = "153";

    public static final String SERVICE_NAME_RMS = "RMS_CreateTransactionInquiry";

    public static final String SEARCH_VALUE = "SearchValue";

    //Employee Services
    public static final String EMIRATES_ID_KEY_ES = "EmiratesID";
    public static final String TRADE_LICENSE_KEY_ES = "TradeLicNo";
    public static final String EMPLOYEE_ID_KEY_ES = "EmployeeID";
    public static final String CUSTOMER_ACCOUNT_ES = "CustAccount";
    public static final String RMS_SERVICE_FEES_ID_ES = "rmsServiceFeesID";
    public static final String RMS_SERVICE_FEES_AMOUNT_ES = "rmsServiceFeesAmount";
    public static final String RMS_SERVICE_EMPLOYEEID_ES = "EmployeeID";
    public static final String RMS_SERVICE_SERVICES_ES = "Services";
    public static final String RMS_SERVICE_AMOUNTTOPAY_ES = "Amounttopay";
    public static final String RMS_SERVICES_CREATESALESORDERLIST = "CreateSalesOrderList";


    //Card Values
    public static final String CardFirstName = "CardFirstName";
    public static final String CardLastName = "CardLastName";
    public static final String CardNumber = "CardNumber";
    public static final String CardExpiry = "CardExpiry";
    public static final String CardCVV = "CardCVV";
    public static final String Country = "AE";
    public static final String City = "Dubai";
    public static final String GMAILCOM = "@gmail.com";
    public static final String DOTSEP = ".";
    public static final String PostalCode = "123456";
    public static final String ContactNumber = "1234567890";
    //Transaction Date if null
    public static final String TransactionDateCurrent = "TransactionDateforCurrent";

    //ServiceName -> Class Name
    public static final String Service_Name = "ServiceName"; // for individual name of the service like Traffic file no
    public static final String Services_Name = "ServicesName"; // i-e: RMS Services
    public static final String TrafficFines = "TrafficFines";
    public static final String Vehicle_Renewal = "Vehicle Renewal";
    public static final String Driver_Renewal = "Driver Renewal";
    public static final String Driving_Damaged_Lost = "Driving Lost/Damage";
    public static final String Vehicle_Damaged_Lost = "Vehicle Lost/Damage";
    public static final String Vehicle_InquiryandPaymentDetails = "Vehicle Inquiry and Payment Details";
    public static final String Driver_InquiryandPaymentDetails = "Vehicle Inquiry and Payment Details";
    public static final String Parking_Renew = "Parking Renew";
    public static final String Parking_New = "Parking New";
    public static final String Ownership_Cert = "Ownership Cert";
    public static final String Non_Ownership_Cert = "Non Ownership Cert";
    public static final String Refund_Insurance_Cert = "Refund Insurance Cert";
    public static final String No_Obligation_Cert = "No Obligation Cert";
    public static final String Experience_Cert = "Experience Cert";
    public static final String Refund_Service = "Refund Service ";
    public static final String RMS_Service = "RMS Service";
    public static final String RMS_Services = "RMS Services";
    public static final String RMS_SALES_ORDER = "RMS Sales Order";
    public static final String RMS_SALES_INVOICE = "RMS Sales Invoice";
    public static final String RMS_EMIRATESID = "RMS Emirates Id";
    public static final String RMS_TRADE_LICENSE = "RMS Trade License";
    public static final String RMS_EMPLOYEE_SERVICES = "RMS Employee Services";

    //RMS Emirates ID
    public static final String RMS_EMIRATESID_BACKORDER = "Backorder";
    public static final String RMS_EMIRATESID_INVOICE = "Invoiced";

    //For Printing Certifiates
    public static final String CERTIFICATE_PRINTING_VEHICLE_TID = "VL";
    public static final String CERTIFICATE_PRINTING_DRIVING_TID = "DL";
    public static final String CERTIFICATE_PRINTING_CERTIFICATES_TID = "CE";

    //Device Serial Number key
    public static final String DEVICE_SERIAL_NUMBER_MERCHANT_DETAILS_KEY = "DeviceSerialNumber";
    //BankID key
    public static final String BANK_ID_MERCHANT_DETAILS_KEY = "BankID";
    //Terminal Id key
    public static final String TERMINAL_ID_MERCHANT_DETAILS_KEY = "TerminalId";
    //Merchant Id key
    public static final String MERCHANT_ID_MERCHANT_DETAILS_KEY = "MerchantId";
    //Login Id key
    public static final String LOGIN_ID_MERCHANT_DETAILS_KEY = "LoginId";
    //Password key
    public static final String PASSWORD_MERCHANT_DETAILS_KEY = "Password";
    //Secret Id key
    public static final String SECRETKEY_MERCHANT_DETAILS_KEY = "SecretKey";
    //payment user id key
    public static final String PAYMENT_USER_ID_MERCHANT_DETAILS_KEY = "PaymentUserId";
    //payment secretkey key
    public static final String PAYMENT_SECRETKEY_MERCHANT_DETAILS_KEY = "PaymentSecretKey";
    //payment password key
    public static final String PAYMENT_PASSWORD_MERCHANT_DETAILS_KEY = "PaymentPassword";
    //payment action
    public static final String PAYMENT_ACTION_MERCHANT_DETAILS_KEY = "PaymentAction";
    //Source Application
    public static final String SOURCE_APPLICATION_MERCHANT_DETAILS_KEY = "SourceApplication";
    //Call back URL
    public static final String CALLBACK_URL_MERCHANT_DETAILS_KEY = "CallbackURL";
    //Device Finger Print
    public static final String DEVICE_FINGER_PRINT_MERCHANT_DETAILS_KEY = "DeviceFingerPrint";
    //Currency
    public static final String CURRENCY_MERCHANT_DETAILS_KEY = "Currency";
    //Channel
    public static final String CHANNEL_MERCHANT_DETAILS_KEY = "Channel";
    //Language
    public static final String LANGUAGE_MERCHANT_DETAILS_KEY = "LanguageMerhant";
    //Request category
    public static final String REQUEST_CATEGORY_MERCHANT_DETAILS_KEY = "RequestCategory";
    //Request type payment
    public static final String REQUEST_TYPE_PAYMENT_MERCHANT_DETAILS_KEY = "RequestTypePayment";
    //Is3DSecure
    public static final String IS_THREED_SECURE_MERCHANT_DETAILS_KEY = "Is3DSecure";
    //SilentOrderAPIURL
    public static final String SILENT_ORDER_API_URL_MERCHANT_DETAILS_KEY = "SilentOrderAPIURL";

    //From here the values will be assigned to the required places in the project
    @Override
    public void MerchantDetails(Context context) {
        DEVICE_SERIAL_NUMBER = PreferenceConnector.readString(context, ConfigrationRTA.DEVICE_SERIAL_NUMBER_MERCHANT_DETAILS_KEY, "");
        MERCHANT_ID = PreferenceConnector.readString(context, ConfigrationRTA.MERCHANT_ID_MERCHANT_DETAILS_KEY, "");
        SECRET_KEY = PreferenceConnector.readString(context, ConfigrationRTA.SECRETKEY_MERCHANT_DETAILS_KEY, "");
        PAYMENT_SECRET_KEY = PreferenceConnector.readString(context, ConfigrationRTA.PAYMENT_SECRETKEY_MERCHANT_DETAILS_KEY, "");
        TERMINAL_ID = PreferenceConnector.readString(context, ConfigrationRTA.TERMINAL_ID_MERCHANT_DETAILS_KEY, "");
        BANK_ID = PreferenceConnector.readString(context, ConfigrationRTA.BANK_ID_MERCHANT_DETAILS_KEY, "");
        LOGIN_ID = PreferenceConnector.readString(context, ConfigrationRTA.LOGIN_ID_MERCHANT_DETAILS_KEY, "");
        PASSWORD = PreferenceConnector.readString(context, ConfigrationRTA.PASSWORD_MERCHANT_DETAILS_KEY, "");
        PAYMENT_USER_ID = PreferenceConnector.readString(context, ConfigrationRTA.PAYMENT_USER_ID_MERCHANT_DETAILS_KEY, "");
        PAYMENT_PASSWORD = PreferenceConnector.readString(context, ConfigrationRTA.PAYMENT_PASSWORD_MERCHANT_DETAILS_KEY, "");
        SOURCE_APPLICATION = PreferenceConnector.readString(context, ConfigrationRTA.SOURCE_APPLICATION_MERCHANT_DETAILS_KEY, "");
        PAYMENT_ACTION = PreferenceConnector.readString(context, ConfigrationRTA.PAYMENT_ACTION_MERCHANT_DETAILS_KEY, "");
        PAYMENT_CALL_BACK_URL = PreferenceConnector.readString(context, ConfigrationRTA.CALLBACK_URL_MERCHANT_DETAILS_KEY, "");
        PAYMENT_DEVICE_FINGER_PRINT = PreferenceConnector.readString(context, ConfigrationRTA.DEVICE_FINGER_PRINT_MERCHANT_DETAILS_KEY, "");
        PAYMENT_CURRENCY = PreferenceConnector.readString(context, ConfigrationRTA.CURRENCY_MERCHANT_DETAILS_KEY, "");
        PAYMENT_CHANNEL = PreferenceConnector.readString(context, ConfigrationRTA.CHANNEL_MERCHANT_DETAILS_KEY, "");
        PAYMENT_LANGUAGE = PreferenceConnector.readString(context, ConfigrationRTA.LANGUAGE_MERCHANT_DETAILS_KEY, "");
        PAYMENT_REQUEST_CATEGORY = PreferenceConnector.readString(context, ConfigrationRTA.REQUEST_CATEGORY_MERCHANT_DETAILS_KEY, "");
        REQUEST_TYPE_PAYMENT = PreferenceConnector.readString(context, ConfigrationRTA.REQUEST_TYPE_PAYMENT_MERCHANT_DETAILS_KEY, "");
        IsThreeDSecure = PreferenceConnector.readString(context, ConfigrationRTA.IS_THREED_SECURE_MERCHANT_DETAILS_KEY, "");
        SilentAPIURL = PreferenceConnector.readString(context, ConfigrationRTA.SILENT_ORDER_API_URL_MERCHANT_DETAILS_KEY, "");

        //Setting the terminal ids
        TERMINAL_ID_DP = TERMINAL_ID; //61100009
        TERMINAL_ID_Receipt = TERMINAL_ID;
        PAYMENT_SOURCE_APPLICATION = TERMINAL_ID;
    }

}
