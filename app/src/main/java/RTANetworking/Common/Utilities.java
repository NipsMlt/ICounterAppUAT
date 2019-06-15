package RTANetworking.Common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import example.dtc.R;
import utility.Common;
import utility.Constant;
import utility.PreferenceConnector;

/**
 * Created by raheel on 3/26/2018.
 */

public class Utilities {

    private static MediaPlayer mPlayer;

    /**
     * Get the Current date time format
     *
     * @return
     */
    public static String currentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    /**
     * Generate 24 Character Random UUID
     *
     * @return
     */
    public static String shortUUID() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
        StringBuilder sb = new StringBuilder(24);
        Random random = new Random();
        for (int i = 0; i < 24; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output;
    }

    //Get Formatted ExpiryDate RTA     09-2021
    public static String getFormattedExpiryDateRTA(String expiryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentYear = sdf.format(new Date());
        String Century = currentYear.substring(0, 2);
        expiryDate = expiryDate.substring(0, 2) + "-" + Century + expiryDate.substring(2, 4);
        return expiryDate;
    }

    /**
     * Generate 24 Character Random UUID
     *
     * @return
     */
    public static String getProperExpiryDate(String expirydate) {
        String date = expirydate.substring(0, 10); //2019-10-08T00:00:00Z
        return date;
    }

    public static String getProperServiceDate(String expirydate) {
        String date = expirydate.substring(0, 10); //2019-10-08T00:00:00Z
        return date;
    }

    public static String getProperServiceDateYear(String expirydate) {
        String date = expirydate.substring(0, 4); //2019-10-08T00:00:00Z
        return date;
    }

    public String getPlateSourceName(String city) {
        if (city.equals("Dubai"))
            return "DXB";
        else
            return "";
    }

    public static String getCityNameFromStateCodeUpperCase(String city) {
        if (city.equals("DBX") || city.equals("DXB"))
            return "DUBAI";
        else
            return "";
    }

    public static String getCityNameFromStateCodeLowerCase(String city) {
        if (city.equals("DBX"))
            return "Dubai";
        else
            return "";
    }

    public static String getVehicleCarNameOrColor(String name, String setKeyCarNameorColor) {
        String[] Cararr = Common.MySplit(name, Constant.SEP_COMMA);
        String[] CarColorarr = Common.MySplit(Cararr[1], Constant.SEP_DASH);
        String CarColor = CarColorarr[0];
        String[] CarNamearr = Common.MySplit(Cararr[0], Constant.SEP_DASH);
        String CarName = CarNamearr[0];
        if (Constant.CarName.equals(setKeyCarNameorColor))
            return CarName;
        else
            return CarColor;
    }

    public static String getVehicleinsuranceExpiry(String name) {
        String[] InsuranceExpiryarr = Common.MySplit(name, Constant.SEP_SPACE);
        String InsuranceExpiry = InsuranceExpiryarr[0];

        return InsuranceExpiry;
    }

    public static String getDriverLicenseType(String name) {
        String[] LicenseTypearr = Common.MySplit(name, Constant.SEP_COMMA);
        String LicenseType = LicenseTypearr[0];

        return LicenseType;
    }

    public static String getIssueDate(String date) {
        String[] IssueDatearr = Common.MySplit(date, Constant.SEP_SPACE);
        String IssueDate = IssueDatearr[0];

        return IssueDate;
    }

    public static String getLicenseClass(String licenseclass) {
        String[] LicenseClassarr = Common.MySplit(licenseclass, Constant.SEP_COMMA);
        String LicenseClass = LicenseClassarr[0];

        return LicenseClass;
    }

    public static String getLicenseStatus(String licensestatus) {
        String[] LicenseStatusearr = Common.MySplit(licensestatus, Constant.SEP_COMMA);
        String LicenseStatus = LicenseStatusearr[0];

        return LicenseStatus;
    }

    public static String getTimeFine(String time) {
        String TimeFine = time.substring(12, time.length());

        return TimeFine;
    }

    public static String getFinePayableorUnpayable(String finestatus) {
        String Fine;
        if ("2".equals(finestatus)) {
            Fine = "Payable";
        } else
            Fine = "UnPayable";
        return Fine;

    }

    public static AnimatorSet ButtonAnimation(Button button) {
        //ObjectAnimator fadeOut = ObjectAnimator.ofFloat(button, "alpha", .5f, .1f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(button, "alpha", .1f, .9f);
        fadeOut.setDuration(800);
        //ObjectAnimator fadeIn = ObjectAnimator.ofFloat(button, "alpha", .1f, .5f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(button, "alpha", .1f, .9f);
        fadeIn.setDuration(900);

        AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
        return mAnimationSet;
    }

    public static MediaPlayer PlayAudio(Context context, int audio) {

        Handler handler = new Handler();
        int delay = 3000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                mPlayer = MediaPlayer.create(context, audio);
                mPlayer.start();
                //to repeat the audio
                //handler.postDelayed(this, delay);
            }
        }, delay);
        return mPlayer;
    }

    Fragment mFragment;

    public void AlertDialog(Context context, String Message, Fragment fragment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFragment = fragment;
                        addFragment(mFragment);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void addFragment(Fragment fragment) {
        fragment.getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static Map<String, String> map = new HashMap<>();

    public static String setAddressedDeptKeyValue(String addressedDept) {
        map.put("Department of Economic Development", "1");
        map.put("TARKHEES-Ports,Customs & Free Zone Corporation", "3");
        map.put("Ministry of Labour", "16");
        map.put("Dubai Police", "17");
        map.put("Dubai Courts", "18");
        map.put("Department of Immigration", "19");
        map.put("Banks", "20");
        map.put("Car Dealers", "21");
        map.put("Insurance Companies", "22");
        map.put("Dubai municipality", "23");
        map.put("Personal Purpose", "24");
        map.put("Department of Tourism and Commerce Marketing", "25");
        map.put("Dubai Silicon Oasis Authority", "27");
        map.put("Dubai Airport Freezone", "29");
        map.put("National Media Council", "30");
        map.put("Dubai Chamber of Commerce &Industry", "31");
        map.put("Al Meydan Free Zone", "32");
        map.put("Community Development Authority", "33");
        map.put("Dubai Silicon Oasis", "39");
        map.put("Department of Economic Development-Abu-Dhabi", "41");
        map.put("Department of Islamic Affairs", "42");
        map.put("Department of Economic Development-SHARJAH", "43");
        map.put("Department of Economic Development-UM-QUWAIN", "44");
        map.put("Department of Economic Development-FUJAIRAH", "45");
        map.put("Department of Economic Development-AJMAN", "46");
        map.put("Department of Economic Development-RAS-ALKHAIMAH", "47");
        map.put("SAUDIA COMPANY", "48");
        map.put("QATAR COMPANY", "49");
        map.put("OMAN COMPANY", "50");
        map.put("KUWAIT COMPANY", "51");
        return map.get(addressedDept);
    }

    public static String setAddressedDeptKeyValueArabic(String addressedDept) {
        map.put("دائرة التنمية الاقتصادية-دبي", "1");
        map.put("تراخيص مؤسسة الموانئ والجمارك والمنطقة الحرة-دبي", "3");
        map.put("وزارة العمل", "16");
        map.put("شرطة دبي", "17");
        map.put("محاكم دبي", "18");
        map.put("الاداره العامه للاقامه وشؤون الاجانب - دبي", "19");
        map.put("البنوك", "20");
        map.put("وكلاء السيارات", "21");
        map.put("شركات التامين", "22");
        map.put("بلدية دبي", "23");
        map.put("اغراض شخصية", "24");
        map.put("دائرة السياحة والتسويق التجاري", "25");
        map.put("سلطة واحة دبي للسيليكون", "27");
        map.put("المنطقة الحرة بمطار دبي", "29");
        map.put("المجلس الوطني للإعلام", "30");
        map.put("غرفة تجارة وصناعة دبي", "31");
        map.put("المنطقة الحره ميدان", "32");
        map.put("هيئة تنمية المجتمع", "33");
        map.put("واحة دبي للسيليكون", "39");
        map.put("دائرة التنمية الاقتصادية ابوظبي", "41");
        map.put("دائرة الشؤون الاسلامية", "42");
        map.put("دائرة التنمية الاقتصادية الشارقة", "43");
        map.put("دائرة التنمية الاقتصادية ام القيوين", "44");
        map.put("دائرة التنمية الاقتصادية الفجيرة", "45");
        map.put("دائرة التنمية الاقتصادية عجمان", "46");
        map.put("دائرة التنمية الاقتصادية رأس الخيمة", "47");
        map.put("منشأت السعودية", "48");
        map.put("منشات قطر", "49");
        map.put("منشات عمان", "50");
        map.put("منشات الكويت", "51");
        return map.get(addressedDept);

    }


    public static String setInsuranceCompaniesKeyValue(String insuranceCompany) {
        map.put("UNITED FIDELITY INSURANCE COMPANY DUBAI BRANCH", "5432");
        map.put("SHARJAH INSURANCE CO", "5433");
        map.put("AL AIN AHLIA INSURANCE CO (DUBAI BR )", "5441");
        map.put("AXA INSURANCE (GULF) B S C (C) DUBAI BRANCH", "6185");
        map.put("AL ITTIHAD AL WATANI GENERAL INSURANCE SOCIETY FOR NEAR EAST", "6490");
        map.put("UNION INSURANCE", "6491");
        map.put("AL BUHAIRA NATIONAL INSURANCE CO (DUBAI BR )", "7629");
        map.put("RAS AL KHAIMAH NATIONAL INSURANCE CO P S C", "9502");
        map.put("OMAN INSURANCE COMPANY (P S C)", "9504");
        map.put("AL DHAFRA INSURANCE CO (PUBLIC SHAREHOLDING CO ) DUBAI BRANCH", "10156");
        map.put("FUJAIRAH NATIONAL INSURANCE CO (DUBAI BRANCH)", "11072");
        map.put("DUBAI ISLAMIC INSURANCE RE INSURANCE COMANY (AMAN )(P J S C)", "12662");
        map.put("American Life Insurance Company", "12752");
        map.put("DUBAI NATIONAL INSURANCE AND REINSURANCE CO (P S C )", "13122");
        map.put("abu dhabi national takaful", "15258");
        map.put("JORDAN INSURANCE CO LTD", "15410");
        map.put("Abu Dhabi National Insurance Company", "15699");
        map.put("DUBAI INSURANCE COMPANY(P S C )", "16806");
        map.put("NATIONAL GENERAL INSURANCE CO (P J S C)", "29180");
        map.put("ALLIANCE INSURANCE (PSC) (BR)", "30932");
        map.put("TOKIO MARINE AND NICHIDO FIRE INSURANCE COMPANY LIMITED (DUBAI BR)", "40080");
        map.put("CIGNA INSURANCE MIDDLE EAST (S.A.L) DUBAI BRANCH", "43020");
        map.put("ORIENT INSURANCE P J S C", "43115");
        map.put("Lebanese Security", "63750");
        map.put("EMIRATES INSURANCE COMPANY (Br of EMIRATES INSURANCE COMPANY )(Dubai Branch)", "63753");
        map.put("QATAR INSURANCE CO", "63757");
        map.put("al wathba natiuonal insurance", "63758");
        map.put("NORWAITCH UNION INSURANCE GULF.", "63761");
        map.put("QATAR GENERAL INSURANCE AND RE INSURANCE CO (S A Q)", "63763");
        map.put("Gulf Resources to Manage the Insurance Services", "63764");
        map.put("Conteniantal Insurance", "63765");
        map.put("The Middle East Insurance Company (S)", "63766");
        map.put("وكاله كاستريل للتامين", "63768");
        map.put("ADAMJEE INSURANCE COMPANY LTD (DUBAI BRANCH)", "63961");
        map.put("Islamic Arab Insurance Company", "63963");
        map.put("SAUDI ARABIAN INSURANCE CO.BSCC DUBAI BRANCH", "63965");
        map.put("AL DAWLIYAH INSURANCE SERVICES(L L C)", "63966");
        map.put("Arab European Security and reinsurance", "63967");
        map.put("General Accident Fire and Life Insurance", "63972");
        map.put("NORTHERN INSURANCE BROKERS LLC", "63974");
        map.put("THE NEW INDIA ASSURANCE COMPANY LIMITED", "63975");
        map.put("Royal International Insurance", "63976");
        map.put("AL KHAZNA INSURANCE CO (P S C) (DUBAI BRANCH)", "63977");
        map.put("THE ORIENTAL INSURANCE COMPANY LTD (DUBAI BRANCH)", "64070");
        map.put("IRAN INSURANCE CO", "64230");
        map.put("Bronchial for Insurance Services", "64253");
        map.put("THE BALOISE INSURANCE CO LTD (DUBAI BRANCH)", "64259");
        map.put("Norwoch Union Insurance Gulf", "64264");
        map.put("شركة البحرين للتامين", "64265");
        map.put("French General Insurance", "64266");
        map.put("ROYAL AND SUN ALLIANCE INSURANCE MIDDLE EAST BSCC (DUBAI BRANCH)", "64268");
        map.put("AL SAGR NATIONAL INSURANCE CO (PSC)", "64271");
        map.put("National Resources for Insurance Services", "64272");
        map.put("MITSUI SUMITOMO INSURANCE COMPANY LIMITED DUBAI BR", "64274");
        map.put("Arab Resource Insurance", "64387");
        map.put("اسيتاليا لي اسكرازيوني دي ايطاليا", "64395");
        map.put("jordan French Insurance", "64401");
        map.put("ARABIAN SCANDINAVIAN INSURANCE", "64402");
        map.put("arabia insurance", "65019");
        map.put("Tahir Insurance", "65021");
        map.put("Delta for Insurance Services", "65970");
        map.put("ALS", "67294");
        map.put("DAR AL TAKAFUL (P J S C)", "86988");
        map.put("NOOR TAKAFUL GENERAL (PJSC)", "88787");
        map.put("METHAQ TAKAFUL INSURANCE P S C DUBAI BR", "89679");
        map.put("Al Hilal Takaful Company P.S.C", "90461");
        map.put("AMERICAN HOME ASSURANCE COMPANY (DUBAI BR)", "91017");
        map.put("INSURANCE HOUSE P S C", "101400");
        map.put("This vehicle is does not have insurance", "105298");
        map.put("NATIONAL TAKAFUL COMPANY WATANIA P J S C DUBAI BRANCH", "110868");
        map.put("ORIENT U N B TAKAFUL P J S C", "173111");
        return map.get(insuranceCompany);
    }

    public static String setInsuranceCompaniesKeyValueArabic(String insuranceCompany) {
        map.put("شركه الشارقه للتامين واعاده التامين", "5432");
        map.put("شركة العين الاهلية للتأمين (فرع دبي)", "5433");
        map.put("اكسا للتأمين (الخليج) ش م ب (م) فرع دبي", "5441");
        map.put("الاتحاد الوطني شركة الضمان العامة للشرق الادنى", "6185");
        map.put("شركه الاتحاد للتامين فرع دبي", "6490");
        map.put("شركة البحيرة الوطنية للتأمين (فرع دبي)", "6491");
        map.put("شركة راس الخيمة الوطنية للتامين", "7629");
        map.put("شركة عمان للتامين (ش م ع)", "9502");
        map.put("شركة الظفرة للتأمين (شركة مساهمة عامة) فرع دبي", "9504");
        map.put("شركة الفجيرة الوطنية للتأمين (فرع دبي)", "10156");
        map.put("شركة دبي الاسلامية للتامين واعادة التامين (امان )(ش.م.ع )", "11072");
        map.put("امريكان لايف إنشورانس كومباني", "12662");
        map.put("شركة دبي الوطنية للتأمين واعادة التأمين (شركة مساهمة عامة)", "12752");
        map.put("شركة ابوظبي الوطنية للتكافل ش م ع تكافل", "13122");
        map.put("شركة التأمين الاردنية المساهمة المحدودة", "15258");
        map.put("شركة ابوظبي الوطنية للتأمين", "15410");
        map.put("شركة دبي للتأمين (شركة مساهمة عامة)", "15699");
        map.put("الشركة الوطنية للتأمينات العامة (شركة مساهمة عامة)", "16806");
        map.put("اللاينس للتأمين (ش . م. ع) فرع", "29180");
        map.put("طوكيو مارين أند نيتشيدو فاير انشورانس كومباني ليمتد (فرع دبي)", "30932");
        map.put("سيغنا الشرق الاوسط للتأمين (ش . م. ل) فرع دبى", "40080");
        map.put("شركة اورينت للتأمين مساهمة عامة", "43020");
        map.put("الضمان اللبنانية", "43115");
        map.put("شركة الامارات للتامين ( فرع من شركة الامارات للتأمين )(فرع دبي)", "63750");
        map.put("شركة قطر للتأمين", "63753");
        map.put("شركة الوثبة الوطنية للتامين", "63757");
        map.put("نورويتش يونيون إنشورانس جلف دبي", "63758");
        map.put("الشركة القطرية العامة للتأمين واعادة التأمين (ش م ق)", "63761");
        map.put("الموارد الخليجية لإدارة خدمات التأمين", "63763");
        map.put("كونتيننتال للتأمين", "63764");
        map.put("الشرق الأوسط للتأمين", "63765");
        map.put("وكالة كاستريل للتأمين", "63766");
        map.put("ادمجي انشورنس كومباني ليمتد (فرع دبي)", "63768");
        map.put("الاسلامية العربية للتأمين", "63961");
        map.put("شركة التأمين العربية السعودية (ش . م. ب . م) فرع دبي", "63963");
        map.put("الدولية لخدمات التأمين (ش ذ م م)", "63965");
        map.put("العربية الاوروبية للضمان وإعادة الضمان", "63966");
        map.put("جنرال أكسيدنت فاير أند لايف إنشورانس", "63967");
        map.put("نورثون للوساطه التامينيه شركة ذات مسئوليه محدودة", "63972");
        map.put("ذي نيو انديا اشورنس كومباني ليمتد", "63974");
        map.put("رويال إنترناشيونال إنشورانس", "63975");
        map.put("شركة الخزنة للتأمين (شركة مساهمة عامة) فرع دبي", "63976");
        map.put("ذي اورينتال انشورنس كومباني ليمتد (فرع دبي)", "63977");
        map.put("شركة سهامي بيمه ايران", "64070");
        map.put("برونشيال لخدمات التأمين", "64230");
        map.put("ذا بلواز انشورانس كومباني ليمتد (فرع دبي)", "64253");
        map.put("نوريج يونيون للتأمين الخليج", "64259");
        map.put("شركة البحرين للتامين", "64264");
        map.put("التأمين العامة الفرنسية", "64265");
        map.put("رويال اند صن اللاينس للتامين(الشرق الاوسط) المحدودة (فرع دبى)", "64266");
        map.put("شركة الصقر الوطنية للتامين (شركة مساهمة عامة)", "64268");
        map.put("الموارد الوطنية لخدمات التأمين", "64271");
        map.put("ميتسوي سوميتومو انشورنس كومباني ليمتد فرع دبي", "64272");
        map.put("اراب ريسورمز إنشورانس", "64274");
        map.put("اسيتاليا لي اسكرازيوني دي ايطاليا", "64387");
        map.put("الاردنية الفرنسية للتأمين", "64395");
        map.put("الشركة العربية الاسكندنافية للتأمين", "64401");
        map.put("شركة التأمين العربية المحدودة", "64402");
        map.put("الطاهر للتأمين", "65019");
        map.put("دلتا لخدمات التأمين", "65021");
        map.put("الشركه الاسلاميه العربيه للتامين", "65970");
        map.put("دار التكافل (ش م ع)", "67294");
        map.put("نور للتكافل العام (ش م ع)", "86988");
        map.put("شركة ميثاق للتأمين التكافلي ش م ع فرع دبي", "88787");
        map.put("شركة الهلال للتكافل ش.م.ع", "89679");
        map.put("اميريكان هوم اشورانس كومباني (فرع دبي)", "90461");
        map.put("دار التامين ش م ع", "91017");
        map.put("هذه المركبة غير مؤمنة", "101400");
        map.put("الوطنية للتكافل (وطنية) ش م ع فرع دبي", "105298");
        map.put("اورينت يو ان بي تكافل (مساهمة عامة)", "110868");
        map.put("اورينت يو ان بي تكافل (مساهمة عامة)", "173111");
        return map.get(insuranceCompany);
    }

    public static String FinesByFineNo(String Finesbyfineno) {
        map.put("Dubai Police", "DubaiPolice");
        map.put("RTA (Parking Fines)", "RTAParkingFines");
        map.put("Abu Dhabi Traffic", "AbuDhabiTraffic");
        map.put("Sharjah Traffic", "SharjahTraffic");
        map.put("Ajman Traffic", "AjmanTraffic");
        map.put("Um Al Quewain Traffic", "UmAlQuewainTraffic");
        map.put("Ras Al Khaymah Traffic", "RasAlKhaymahTraffic");
        map.put("Al Fujairah Traffic", "AlFujairahTraffic");
        map.put("Salik", "Salik");
        map.put("RTA LIS Inspection", "RTALISInspection");
        map.put("Abu Dhabi Manucipality", "AbuDhabiManucipality");
        map.put("Sharjah Manucipality", "SharjahManucipality");
        map.put("Oman", "Oman");
        map.put("Bahrain", "Bahrain");
        map.put("KSA", "KSA");
        map.put("Kuwait", "Kuwait");
        map.put("Qatar", "Qatar");
        map.put("RTA PTA Inspection ", "RTAPTAInspection");
        map.put("Parking Spaces for Trains", "ParkingSpacesforTrains");
        map.put("Sharjah Transportation", "SharjahTransportation");
        map.put("Vehicle Safety Database (VSD)", "VehicleSafetyDatabase");
        map.put("Abu Dhabi Transportation", "AbuDhabiTransportation");
        return map.get(Finesbyfineno);
    }

    static public boolean isServerReachable(Context context, String url) {
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(url);
                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000); //<- 3Seconds Timeout
                urlConn.connect();
                if (urlConn.getResponseCode() == 200) {
                    return true;
                } else {
                    return false;
                }
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public static boolean check;

    public static boolean checkisReachable(String URL) {
        try {
            /*Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(URL); //<- Try ping -c 1 www.serverURL.com
            int mPingResult = proc.waitFor();*/
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int code = connection.getResponseCode();

            if (code == 200) {
                check = true;
                return check;
            }
         /*   if (mPingResult == 0) {
                check = true;
                return check;
            } */
            else {
                check = false;
                return check;
            }
        } catch (IOException e) {
        } /*catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return check;
    }

    /*Printing Work*/
    public static Socket socket;
    private static final int SERVERPORT = 8001;
    private static String SERVER_IP = ""; //"192.168.0.50";
    protected static String transactionID;
    protected static Context getContext;

    public static boolean ResendString(Context context, String TransactionID) {

        try {
            //new Thread(new Utilities.ClientThread(context, TransactionID, ServiceName)).start();

            while (!Thread.currentThread().isInterrupted()) {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(transactionID);

                new Thread(new Utilities.ReadThread(context, transactionID)).start();

            }

            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static class ReadThread implements Runnable {
        public ReadThread(Context context, String TransactionID) {
            transactionID = TransactionID;
            getContext = context;
        }

        @Override
        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                BufferedReader br = new BufferedReader(isr);
                boolean isDone = false;

                String s = new String();

                while (!isDone && ((s = br.readLine()) != null)) {

                    System.out.println("Server Response: " + s);   // Printing on Console
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                while (!ResendString(getContext, transactionID)) {

                }
            } catch (IOException e) {
                while (!ResendString(getContext, transactionID)) {

                }
            } catch (Exception e) {
                e.printStackTrace();
                while (!ResendString(getContext, transactionID)) {

                }
            }
        }
    }

    public static class ClientThread implements Runnable {
        public ClientThread(Context context, String TransactionID) {
            transactionID = TransactionID;
            getContext = context;
        }

        @Override
        public void run() {
            try {
                PrintWriter out = null;
                SERVER_IP = PreferenceConnector.readString(getContext, Constant.PRINTER_IP, "");
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(transactionID);

                new Thread(new ReadThread(getContext, transactionID)).start();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();

            }
        }
    }

    /*
    class PrimeThread extends Thread {
        long minPrime;
        int Audio;
        PrimeThread primeThread;
        PrimeThread(long minPrime, int audio) {
            this.minPrime = minPrime;
            this.Audio = audio;
        }

        public void run() {
            mPlayer = MediaPlayer.create(getContext(), this.Audio);
            mPlayer.start();
        }
    }*/

    /*public void SetTextFontSize(ArrayList<TextView> textViews, Context context) {
        for (int i = 0; i < textViews.size(); i++) {
            textViews.set(i, textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.fab_margin)));
        }
    }*/

    public static String RemoveEscapeSequence(String text) {
        //Removing the escape sequences from tshe string
        String a = text.replaceAll("\r\n", "");
        String b = a.replaceAll("\r", "");
        String c = b.replaceAll("\n", "");
        String d = c.replaceAll("\n\r", "");
        String e = d.replaceAll("\t", "");
        String f = e.replaceAll("\f", "");
        String g = f.replaceAll("\b", "");
        return g;
    }

}
