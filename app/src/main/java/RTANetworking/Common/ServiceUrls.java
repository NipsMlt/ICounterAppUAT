package RTANetworking.Common;

/**
 * Created by raheel on 3/5/2018.
 */

public class ServiceUrls {

    //public static final String RTA_INQUIRY_URL = "https://kpg.networkips.com:8109/NIPSKioskService/NIPSKioskServices.svc"; //prod
    public static final String RTA_INQUIRY_URL = "https://kp.networkips.com:6103/NIPSKioskService/NIPSKioskServices.svc"; //UAT

    //Request URL for Card payment
    //public static final String RTA_INQUIRY_URL_CARD_PAYMENT = "https://nipsgateway.networkips.com/NIPSICounterService/NIPSICounterService.svc";//Prod
    public static final String RTA_INQUIRY_URL_CARD_PAYMENT = "https://testpg.networkips.com/NIPSICounterService/NIPSICounterService.svc";//UAT

    //Request SOAP Action for Card payment
    public static final String RTA_INQUIRY_URL_SOAP_ACTION_CARD_PAYMENT = "http://tempuri.org/INIPSiCounterService/Inquiry";

    //NIPS merchant details URL
    public final static String RTA_INQUIRY_SOAP_ACTION_MERCHANT_INQUIRY = "http://tempuri.org/INIPSiCounterService/MerchantInquiry_MultipleMerchants    ";

    public static final String RTA_INQUIRY_URL_SOAP_ACTION = "http://tempuri.org/INIPSKioskServices/Inquiry";

    public static final String RTA_PAYMENT_URL_SOAP_ACTION = "http://tempuri.org/INIPSKioskServices/Payment";

    public static final String DIGITAL_PASS_INQUIRY_URL = "https://testbusinesscentre.networkips.com:6102/WebServices/TheBookPROAPI.svc";

    public static final String DIGITAL_PASS_INQUIRY_URL_SOAP_ACTION = "http://tempuri.org/ITheBookPROAPI/Inquiry";
}
