package volley;

/**
 * Created by raheel on 10/3/2016.
 */
public class ServiceUrls {


    //Live URL
   // public  static final  String PREFIX_USER_MODULE_URL = "https://nipserpservices.networkips.com:8106/ServiceModules/UserModule.svc/";

    // UAT URL For User
    public static final String PREFIX_USER_MODULE_URL = "http://10.0.87.130:81/ServiceModules/UserModule.svc/";

    // UAT URL For Inventory
    public static final String PREFIX_INVENTORY_URL = "http://10.0.87.130:81/ServiceModules/InventoryModule.svc/";


    // Login Url
    public static final String LOGIN_URL = PREFIX_USER_MODULE_URL+"authenticateUser";

    // Update Session Url
    public static final String UPDATE_SESSION_URL = PREFIX_USER_MODULE_URL+"UpdateSession";

    // Search Device By Serial Number
    public static final String SEARCH_DEVICE_BY_SERIAL_URL = PREFIX_INVENTORY_URL+"searchDevicesBySerialNumber";

    // Fetch ALL Merchant
    public static final String FETCH_MERCHANT_URL = PREFIX_INVENTORY_URL+"fetchMerchant";

    // Fetch ALL Kiosk
    public static final String FETCH_KIOSK_URL = PREFIX_INVENTORY_URL+"fetchKiosk";

    // Fetch All Merchant Branches
    public static final  String FETCH_MERCHANT_BRANCH_URL = PREFIX_INVENTORY_URL+"fetchmerchantBranchWithMerchantId";

    // Fetch All User
    public static final  String FETCH_ALL_USER_URL = PREFIX_INVENTORY_URL+"fetchUserWithoutCurrentUser";

    // Assign Devices
    public static final  String ASSIGN_DEVICE_URL = PREFIX_INVENTORY_URL+"deviceAssignedSingle";

    // Get Device Assign by Serial Number
    public static final  String GET_DEVICE_ASSIGNED_BY_SERIAL_URL = PREFIX_INVENTORY_URL+"getDeviceAssignedIdBySeriaNumber";

    //  Return Devices
    public static final  String RETURN_DEVICES_URL = PREFIX_INVENTORY_URL+"deviceReturn";

    //  Get Device Information By Serial Number
    public static final  String Get_DEVICES_BY_SERIAL_NUMBER_URL = PREFIX_INVENTORY_URL+"getDeviceInformationBySerialNumber";

    //  Get Device Logs
    public static final  String Get_DEVICE_LOG_URL = PREFIX_INVENTORY_URL+"getDeviceLog";

    //  Fetch User Information
    public static final  String Fetch_USER_URL = PREFIX_INVENTORY_URL+"fetchUser";

}
