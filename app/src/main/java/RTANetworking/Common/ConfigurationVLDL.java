package RTANetworking.Common;

import android.content.Context;

import RTANetworking.Interfaces.MerchantDetailsCallback;
import rtaservices.RTAMainServices;
import utility.PreferenceConnector;

public class ConfigurationVLDL {


    // Terminal Id
    public static String TERMINAL_ID; //will vary per device
    //****************************************** Center ID ***********************************************//
    public static final String CENTERID = "2618"; // Center id for Deira

    //*******************************Service Code**********************************************//
    public static final String SERVICE_ID_VEHICLE = "106";
    public static final String SERVICE_ID_DRIVER = "107";
    public static final String SERVICE_ID_PERSONAL_INFO = "105";

    //*******************************Setvice Codes*********************************************//
    public static final String SETVICE_ID_VEHICLE_RENEWAL = "204";
    public static final String SETVICE_ID_VEHICLE_LOSS_DAMAGE = "211";
    public static final String SETVICE_ID_DRIVER_RENEWAL = "103";
    public static final String SETVICE_ID_DRIVER_LOSS_DAMAGE = "104";
    public static final String SETVICE_CODE_SIDRTAReCertifyTransactionInquiry = "411";

    //Certificates
    public static final String SETVICE_ID_Clearance_Certificate = "213";
    public static final String SETVICE_ID_Vehicle_Ownership_Certificate = "214";
    public static final String SETVICE_ID_Vehicle_Non_Ownership_Certificate = "215";
    public static final String SETVICE_ID_Insurance_Refund_Certificate = "216";
    public static final String SETVICE_ID_Driving_Exp_Certificate = "105";

    //*****************************************   Service IDs   **************************************//
    public static final String SIDCreateTransactionInquiry = "108";
    public static final String SIDRTAAvailableDeliveryMethod = "123";
    public static final String SIDRTAsetAvailableDeliveryInquiry = "126";
    public static final String SIDRTAReCertifyTransactionInquiry = "125";
    public static final String SIDInquiryToServicesCharges = "039";


    //*****************************************   Service names   **************************************//
    public static final String SNInquiryRTAVehicleDetailByPlateInfo = "InquiryRTAVehicleDetailByPlateInfo";
    public static final String SNCreateTransactionInquiry = "CreateTransactionInquiry";
    public static final String SNRTAAvailableDeliveryMethod = "RTAAvailableDeliveryMethod";
    public static final String SNRTAsetAvailableDeliveryInquiry = "RTAsetAvailableDeliveryInquiry";
    public static final String SNRTAReCertifyTransactionInquiry = "RTAReCertifyTransactionInquiry";
    public static final String SNInquiryToServicesCharges = "InquiryToServicesCharges";

    public static final String SNInquiryRTADRLDetail = "InquiryRTADRLDetail"; //InquiryRTADRLDetail

    //Certificates
    public static final String SNInquiryRTAPersonalInfo = "InquiryRTAPersonalInfo";
    public static final String SNRTA_CertificateLookup = "RTA_CertificateLookup";

    // **************************************** Secret Key ***********************************************//
    public static final String SecretKey = ConfigrationRTA.SECRET_KEY;

    //
    //***************************************** CKeyValuePair Keys Configuration for VL ***********************************//
    public static final String TrfNo = "trafficFileNo";
    public static final String SetviceCode = "setviceCode";
    public static final String IsFine = "isFine";
    public static final String ChasissNo = "chasissNo";
    public static final String ChasissNoLD = "chassisNo";
    public static final String IsMortgaged = "isMortgaged"; //issueType
    public static final String cauCode = "cauCode";
    public static final String issueType = "issueType";
    public static final String TransactionID = "TransactionID";
    public static final String ProcessingCenterID = "processingCenterID";
    public static final String TrsMode = "trsMode";
    public static final String PrefEmail = "prefEmail";
    public static final String PrefMobile = "prefMobile";
    public static final String TrsDeliveryDate = "trsDeliveryDate";
    public static final String PrefContactName = "prefContactName";
    public static final String PrefAddressLine1 = "prefAddressLine1";
    public static final String PrefAddressLine2 = "prefAddressLine2";
    public static final String PrefArea = "prefArea";
    public static final String EmiCode = "emiCode";
    public static final String Amount = "Amount";
    public static final String ServiceCode = "serviceCode";
    public static final String IsLost = "isLost";
    public static final String reprintReason = "reprintReason";
    public static final String licenseNo = "licenseNo";

    //Certificates
    public static final String policyNumber = "policyNumber";
    public static final String insuranceExpiryDate = "insuranceExpiryDate";
    public static final String insuranceCompanyId = "insuranceCompanyId";
    public static final String sddiLoggedInTrafficNo = "sddiLoggedInTrafficNo";
    //

    public static final String CustomerUniqueNo = "CustomerUniqueNo";
    public static final String ServiceTypeDetails = "ServiceTypeDetails";
    public static final String ServiceItems = "ServiceItems";
    public static final String ReplacementReasonLost = "LOST";
    public static final String ReplacementReasonDamaged = "DAMAGED";

    public static final String lostValue = "1";
    public static final String damagedValue = "2";
    //
    // ****************************************   Constant  *******************************************//
    public static final String Version = "1.0";

    // Terminal Id
    public static String TerminalId = ConfigrationRTA.TERMINAL_ID;//"020030101107";

    public static final String CLUTerminalId = "9001"; //This is not used by any

    // Source Application
    public static final String SourceApplication = "KSK";

    // Signature Field
    public static final String SignatureFields = "RequestId,TimeStamp,MerchantId,SourceApplication,BankId";

    // Request Type
    public static final String RequestType = "REQ_KSK_INQ";

    // Request Type
    public static final String RequestTypePMT = "REQ_KSK_PMT";

    // Request Category
    public static final String RequestCategory = "Inquiry";

    // Request Category
    public static final String RequestCategoryPMT = "Payment";

    // Merchant ID
    public static final String MerchantId = ConfigrationRTA.MERCHANT_ID;// <-(this is for UAT) //RTA0000001(for production)

    // Login Id
    public static final String LoginId = ConfigrationRTA.LOGIN_ID;

    // Password
    public static final String Password = ConfigrationRTA.PASSWORD;

    // Description
    public static final String Description = "";

    // Language
    public static final String Language = "EN";

    // Ip
    public static final String IP = "127.0.0.1";

    // Bank Id
    public static final String BankId = ConfigrationRTA.BANK_ID;


    public static final String CTIServiceTitle = "PaymentInquiry";
    public static final String CTICashPaymentType = "Cash";
    public static final String CTICardPaymentType = "Card";

    public static final String RCTIServiceTitle = "PaymentInquiry";
    public static final String RCTICashPaymentType = "Cash";
    public static final String RCTICardPaymentType = "Card";

    //Title
    public static final String VehicleTitle = "VehicleTitle";
    public static final String DriverTitle = "DriverLicenseTitle";
    public static final String VehicleLostDamage = "Vehicle Registration Card Lost / Damaged";
    public static final String VehicleLostRenewal = "Vehicle Renewal";
    public static final String DriverRenewal = "Driving License Renewal";
    public static final String DriverLostDamage = "Driving License Lost / Damaged";


}