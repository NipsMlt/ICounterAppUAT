package RTANetworking.Common;

/**
 * Created by raheel on 3/26/2018.
 */

public class ConfigrationDTC {


    // ******************* Secret Key *********************//
    public static final String SECRET_KEY = "nips@2015";

    // ******************* POS Key *********************//
    public static final String POSKey="TY8v+Q9yu9UCHMAnENuGNuwMvNBn9A7wSJJMw5/om/f/rJqbrMthAw==";//UAT  //"TY8v+Q9yu9V14WuyiowJdmi+viHo8PGl8/uJi/dTKto=";

    // ************   Constant ***************//
    // Version Number
    public static final String VERSION = "1.0";

    // Terminal Id
    public static final String TERMINAL_ID = "9001";//"61100007";

    // Source Application
    public static final String SOURCE_APPLICATION = "KSK";

    // Signature Field
    public static final String SIGNATURE_FIELDS = "RequestId,TimeStamp,MerchantId,SourceApplication,BankId";

    // Request Type
    public static final String REQUEST_TYPE = "REQ_KSK_INQ";

    // Request Category
    public static final String REQUEST_CATEGORY = "Inquiry";

    // Merchant ID
    public static final String MERCHANT_ID = "rta01";//<-(this is for UAT) //RTA0000001(for production)

    // Login Id
    public static final String LOGIN_ID = "kskuser1";

    // Password
    public static final String PASSWORD = "CGyp1Sx/ggLrojqhXtlvdw==";

    // Description
    public static final String DESCRIPTION = "";

    // Language
    public static final String LANGUAGE = "EN";

    // Ip
    public static final String IP = "127.0.0.1";

    // Bank Id
    public static final String BANK_ID = "EBI";


    //********* Inquiry By TRF Number ***********//

    // Service Name
    public static final String SERVICE_NAME_TRF = "InquiryRTATrafficFineByTrfNo";

    // Service ID
    public static final String SERVICE_ID_TRF = "101";

    // TRF Number
    public static final String TRF_NO = "TrfNo";

    // Birth Year
    public static final String BIRTH_YEAR = "BirthYear";

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
    public static final String REQUEST_TYPE_PAYMENT = "REQ_KSK_PMT";

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


    //******************** Direct Debit ****************************************//

    // Service Id Create Transaction
    public static final String SERVICE_ID_DIRECT_DEBIT = "116";

    // Service Name
    public static final String SERVICE_NAME_DIRECT_DEBIT = "RTADirectAccountInquiry";

    // Service Name
    public static final String TRANSACTION_ID = "TransactionID";

    // Payment Type
    public static final String PAYMENT_TYPE_CREDIT_CARD = "CreditCard";

    // CCNR
    public static final String CC_NR = "CCNR";

    // CCHOLDERNAME
    public static final String CC_HOLDER_NAME = "CCHOLDERNAME";

    // CCBRAND
    public static final String CC_BRAND = "CCBRAND";

    //******************** Payment Cyber Source Service ****************************************//

    // Payment Action
    public static final String PAYMENT_ACTION = "https://3dsecure.networkips.com/Gateway/PG";

    // Request Category
    public static final String PAYMENT_REQUEST_CATEGORY = "sale";

    // Source Application
    public static final String PAYMENT_SOURCE_APPLICATION = "K002";//"61100007";

    // Finger Print
    public static final String PAYMENT_DEVICE_FINGER_PRINT = "PC999999";

    // Merchant ID
    //public static final String PAYMENT_MERCHANT_ID = "dtc01";

    // User ID
    public static final String PAYMENT_USER_ID = "DtcOcdUser";

    // Password
    public static final String PAYMENT_PASSWORD = "PM8uVaYjwCIZYukXVVWRgg==";

    // IP Assigned
    public static final String PAYMENT_IP_ASSIGNED = "127.0.0.1";

    // Version
    public static final String PAYMENT_VERSION = "1.0";

    // Service Id
    public static final String PAYMENT_SERVICE_ID = "DTCFineandInquiry";

    // Service Name
    public static final String PAYMENT_SERVICE_NAME = "FineandInquiry";

    // Payment Currency
    public static final String PAYMENT_CURRENCY = "AED";

    // Callback URL
    public static final String PAYMENT_CALL_BACK_URL = "https://3dsecure.networkips.com/Home/TestMerchantResponse";

    // Payment Channel
    public static final String PAYMENT_CHANNEL = "ksk";

    // Language
    public static final String PAYMENT_LANGUAGE = "EN";

    // Sub Merchant Id
    public static final String PAYMENT_SUB_MERCHANT_ID = "";

    // Signature Fields
    public static final String PAYMENT_SIGNATURE_FIELDS = "RequestId,TimeStamp,ReferenceNumber,MerchantId,Amount";

    // Signature Fields
    public static final String PAYMENT_CARD_TYPE = "RequestId,TimeStamp,ReferenceNumber,MerchantId,Amount";

    public static final String PAYMENT_SECRET_KEY = "Nips@2018#!@#$";

    //Card Values
    public static final String CardFirstName= "CardFirstName";
    public static final String CardLastName= "CardLastName";
    public static final String CardNumber= "CardNumber";
    public static final String CardExpiry= "CardExpiry";
    public static final String Country= "AE";
    public static final String City= "Dubai";
    public static final String DashDTC= "-dtc";
    public static final String GMAILCOM= "@gmail.com";
    public static final String DOTSEP= ".";
    public static final String PostalCode= "123456";
    public static final String ContactNumber= "12345678910";
    public static final String CardCVV= "12345678910";
}
