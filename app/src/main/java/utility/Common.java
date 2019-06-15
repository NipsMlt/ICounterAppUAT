package utility;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Vector;

import example.dtc.R;

import static RTANetworking.Common.Utilities.ButtonAnimation;

public class Common {

    static Context lContext;
    public static String[] month = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    public static String date;
    private static final String TAG = "LocationAddress";


    // Add 2 rows
    public static void AddRow(Context context, TableLayout table,
                              String Label1, String Label2) {
        lContext = context;
        boolean n;

        String alternateColor = PreferenceConnector.readString(lContext,
                "AlternateColor", null);
        if (alternateColor != null && alternateColor.equals("T")) {
            n = true;
            PreferenceConnector.writeString(lContext, "AlternateColor", "F");
        } else if (alternateColor != null && alternateColor.equals("F")) {
            n = false;
            PreferenceConnector.writeString(lContext, "AlternateColor", "T");
        } else {
            n = false;
            PreferenceConnector.writeString(lContext, "AlternateColor", "F");
        }

        table.addView(AddRow(Label1, Label2, n));

    }

    // Add 3 rows
    public static void AddRow(Context context, TableLayout table,
                              String Label1, String Label2, String Label3) {
        lContext = context;
        boolean n;


        String alternateColor = PreferenceConnector.readString(lContext,
                "AlternateColor", null);

        if (alternateColor != null && alternateColor.equals("T")) {
            n = true;
            PreferenceConnector.writeString(lContext, "AlternateColor", "F");
        } else if (alternateColor != null && alternateColor.equals("F")) {
            n = false;
            PreferenceConnector.writeString(lContext, "AlternateColor", "T");
        } else {
            n = false;
            PreferenceConnector.writeString(lContext, "AlternateColor", "F");
        }

        table.addView(AddRow(Label1, Label2, Label3, n));

    }
	/*public static void AddRow(Context context, TableLayout table,
			String Label1, String Label2, String Label3) {
		lContext = context;
		boolean n;

		String alternateColor = PreferenceConnector.readString(lContext,
				"AlternateColor", null);
		if (alternateColor != null && alternateColor.equals("T")) {
			n = true;
			PreferenceConnector.writeString(lContext, "AlternateColor", "F");
		} else if (alternateColor != null && alternateColor.equals("F")) {
			n = false;
			PreferenceConnector.writeString(lContext, "AlternateColor", "T");
		} else {
			n = false;
			PreferenceConnector.writeString(lContext, "AlternateColor", "F");
		}

		table.addView(AddRow(Label1, Label2, Label3, n));

	}*/

    public static void AddPaymentRow(Context context, TableLayout table,
                                     String Label1, String Label2) {
        lContext = context;
        table.addView(AddPaymentRow(Label1, Label2));

    }

    public static void AddPaymentRow(Context context, TableLayout table,
                                     String Label1, String Label2, int ColorCode2) {
        lContext = context;
        table.addView(AddPaymentRow(Label1, Label2, ColorCode2));

    }

    public static void AddPaymentRow(Context context, TableLayout table,
                                     String Label1, String Label2, int ColorCode2, int FontSize2) {
        lContext = context;
        table.addView(AddPaymentRow(Label1, Label2, ColorCode2, FontSize2));

    }

    public static void AddPaymentRow(Context context, TableLayout table,
                                     String Label1) {
        lContext = context;
        table.addView(AddPaymentRow(Label1));

    }

    public static boolean ReturnReverse(boolean n) {
        if (n)
            return false;
        else
            return true;
    }

    // Add sepertor line b/w rows
    public static TableRow AddSeperatorRow() {
        TableRow row = new TableRow(lContext);

        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setBackgroundResource(R.drawable.aboutus);
        tv2.setBackgroundResource(R.drawable.aboutus);

        tv1.setHeight(1);
        tv2.setHeight(1);

        row.addView(tv1);
        row.addView(tv2);

        return row;
    }

    // Tabular Receipt (Add Header for 3 rows)
    public static void AddHeaderLast(Context context, TableLayout table,
                                     String Label1, String Label2, String Label3) {

        lContext = context;

        TableRow row = new TableRow(lContext);
        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);
        TextView tv3 = new TextView(lContext);

        tv1.setText(Label1);
        tv2.setText(Label2);
        tv3.setText(Label3);

        tv1.setTypeface(null, Typeface.BOLD);
        tv2.setTypeface(null, Typeface.BOLD);
        tv3.setTypeface(null, Typeface.BOLD);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.CENTER_HORIZONTAL);
        tv3.setGravity(Gravity.RIGHT);

        // tv1.setPadding(left, top, right, bottom)
        tv1.setPadding(30, 0, 0, 0);
        tv2.setPadding(0, 0, 0, 0);
        tv3.setPadding(0, 0, 30, 0);

        tv2.setWidth(100);
        tv3.setWidth(100);

        tv1.setTextSize(11);
        tv2.setTextSize(11);
        tv3.setTextSize(11);

        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);
        tv3.setTextColor(Color.WHITE);

        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);

        table.addView(row);
    }

    // Add Header for 2 rows
    public static void AddHeaderLast(Context context, TableLayout table,
                                     String Label1, String Label2) {

        lContext = context;

        TableRow row = new TableRow(lContext);
        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setText(Label1);
        tv2.setText(Label2);

        tv1.setTypeface(null, Typeface.BOLD);
        tv2.setTypeface(null, Typeface.BOLD);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.RIGHT);

        tv1.setTextSize(11);
        tv2.setTextSize(11);

        tv1.setTextColor(Color.WHITE);
        tv2.setTextColor(Color.WHITE);

        tv1.setBackgroundColor(Color.parseColor("#086CA8"));
        tv2.setBackgroundColor(Color.parseColor("#086CA8"));

        tv1.setPadding(3, 0, 0, 0);

        row.addView(tv1);
        row.addView(tv2);

        table.addView(row);
    }

    // Add 2 rows internally
    private static TableRow AddRow(String Label1, String Label2,
                                   boolean lalternateColor) {

        TableRow row = new TableRow(lContext);

        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.RIGHT);
        // tv1.setTypeface(null, Typeface.BOLD);

        if (lalternateColor) {
            tv1.setBackgroundColor(Color.parseColor("#E4EAEF"));
            tv2.setBackgroundColor(Color.parseColor("#E4EAEF"));
        } else {
            tv1.setBackgroundColor(Color.parseColor("#ffffff"));
            tv2.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        tv1.setPadding(5, 0, 0, 0);
        tv2.setPadding(0, 0, 5, 0);

        tv2.setWidth(160);

        tv1.setTextSize(11);
        tv2.setTextSize(11);

        tv1.setText(Label1);
        tv2.setText(Label2);

        row.addView(tv1);
        row.addView(tv2);

        return row;

    }

    // Add 3 rows internally
    private static TableRow AddRow(String Label1, String Label2, String Label3,
                                   boolean lalternateColor) {

        TableRow row = new TableRow(lContext);
        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);
        TextView tv3 = new TextView(lContext);

        // tv1.setTypeface(null, Typeface.BOLD);
		/*if (lalternateColor) {
			tv1.setBackgroundColor(Color.parseColor("#E4EAEF"));
			tv2.setBackgroundColor(Color.parseColor("#E4EAEF"));
			tv3.setBackgroundColor(Color.parseColor("#E4EAEF"));
		} else {
			tv1.setBackgroundColor(Color.parseColor("#ffffff"));
			tv2.setBackgroundColor(Color.parseColor("#ffffff"));
			tv3.setBackgroundColor(Color.parseColor("#ffffff"));
		}*/

        tv1.setBackgroundColor(Color.parseColor("#ffffff"));
        tv2.setBackgroundColor(Color.parseColor("#ffffff"));
        tv3.setBackgroundColor(Color.parseColor("#ffffff"));

        tv1.setGravity(Gravity.CENTER);
        tv2.setGravity(Gravity.LEFT);
        tv3.setGravity(Gravity.RIGHT);

        tv1.setPadding(4, 0, 0, 0);
        tv2.setPadding(5, 0, 5, 0);
        tv3.setPadding(0, 0, 10, 0);

        tv1.setWidth(50);
        tv2.setWidth(150);
        tv3.setWidth(50);

        tv1.setTextSize(11);
        tv2.setTextSize(11);
        tv3.setTextSize(11);

        // Bad Fix
        Label2 = Label2.replace("\n", "");
        Label2 = Label2.replace(System.getProperty("line.separator"), "");
        Label2 = Label2.replaceAll("\\r|\\n", "");


        tv1.setText(Label1);
        tv2.setText(Label2);
        tv3.setText(Label3);

        // tv3.setGravity(Gravity.RIGHT);
        row.addView(tv1);
        row.addView(tv2);
        row.addView(tv3);

        return row;

    }

    // Tabular Receipt for row elements( Add 3 rows internally)
	/*private static TableRow AddRow1(String Label1, String Label2, String Label3,
			boolean lalternateColor) {

		TableRow row = new TableRow(lContext);
		TextView tv1 = new TextView(lContext);
		TextView tv2 = new TextView(lContext);
		TextView tv3 = new TextView(lContext);

		// tv1.setTypeface(null, Typeface.BOLD);
		if (lalternateColor) {
			tv1.setBackgroundColor(Color.parseColor("#E4EAEF"));
			tv2.setBackgroundColor(Color.parseColor("#E4EAEF"));
			tv3.setBackgroundColor(Color.parseColor("#E4EAEF"));
		} else {
			tv1.setBackgroundColor(Color.parseColor("#ffffff"));
			tv2.setBackgroundColor(Color.parseColor("#ffffff"));
			tv3.setBackgroundColor(Color.parseColor("#ffffff"));
		}

		tv1.setGravity(Gravity.LEFT);
		tv2.setGravity(Gravity.LEFT);
		tv3.setGravity(Gravity.RIGHT);

		// tv1.setPadding(left, top, right, bottom)
		tv1.setPadding(0, 0, 0, 0);
		tv2.setPadding(70, 0, 0, 0);
		tv3.setPadding(0, 0, 0, 0);

		
		 * tv1.setPadding(16, 0, 0, 0); tv2.setPadding(15, 0, 0, 0);
		 * tv3.setPadding(0, 0, 21, 0);
		 

		tv2.setWidth(100);
		tv3.setWidth(100);

		tv1.setTextSize(11);
		tv2.setTextSize(11);
		tv3.setTextSize(11);

		tv1.setText(Label1);
		tv2.setText(Label2);
		tv3.setText(Label3);

		
		 * tv1.setTextColor(Color.WHITE); tv2.setTextColor(Color.WHITE);
		 * tv3.setTextColor(Color.WHITE);
		 

		// tv3.setGravity(Gravity.RIGHT);
		row.addView(tv1);
		row.addView(tv2);
		row.addView(tv3);

		return row;

	}*/

    private static TableRow AddPaymentRow(String Label1, String Label2) {

        TableRow row = new TableRow(lContext);

        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.LEFT);

        tv1.setTextColor(Color.parseColor("#086CA8"));
        tv2.setTextColor(Color.parseColor("#7E7E7E"));

		/*tv1.setPadding(10, 0, 0, 0);
		tv2.setPadding(0, 0, 10, 0);*/

        tv2.setWidth(160);

        tv1.setTextSize(13);
        tv2.setTextSize(13);

        tv1.setText(Label1);
        tv2.setText(Label2);

        row.addView(tv1);
        row.addView(tv2);

        return row;

    }

    private static TableRow AddPaymentRow(String Label1, String Label2,
                                          int ColorCode2) {

        TableRow row = new TableRow(lContext);

        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.LEFT);

        tv1.setTextColor(Color.parseColor("#086CA8"));
        tv2.setTextColor(ColorCode2);

		/*tv1.setPadding(10, 0, 0, 0);
		tv2.setPadding(0, 0, 10, 0);*/

        tv2.setWidth(160);

        tv1.setTextSize(13);
        tv2.setTextSize(13);

        tv1.setText(Label1);
        tv2.setText(Label2);

        row.addView(tv1);
        row.addView(tv2);

        return row;

    }

    // PIN confirmation 'To window' forText(Amount)
    private static TableRow AddPaymentRow(String Label1, String Label2,
                                          int ColorCode2, int FontSize2) {

        TableRow row = new TableRow(lContext);

        TextView tv1 = new TextView(lContext);
        TextView tv2 = new TextView(lContext);

        tv1.setGravity(Gravity.LEFT);
        tv2.setGravity(Gravity.RIGHT);

        tv1.setTextColor(Color.parseColor("#7E7E7E"));
        tv2.setTextColor(ColorCode2);

        // set padding(left,top,right,bottom)
		/*tv1.setPadding(13, 0, 0, 0);
		tv2.setPadding(0, 0, 20, 0);*/


        tv2.setWidth(160);

        tv1.setTextSize(13);
        tv2.setTextSize(FontSize2);

        tv1.setText(Label1);
        tv2.setText(Label2);

        row.addView(tv1);
        row.addView(tv2);

        return row;

    }

    // Thankyou 'To window' forText(Bank Name)
    private static TableRow AddPaymentRow(String Label1) {

        TableRow row = new TableRow(lContext);
        TextView tv1 = new TextView(lContext);
        tv1.setGravity(Gravity.LEFT);
        tv1.setTextColor(Color.parseColor("#7E7E7E"));
        //tv1.setPadding(13, 0, 0, 0);

        tv1.setTextSize(13);
        tv1.setText(Label1);
        row.addView(tv1);
        return row;

    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con = (HttpURLConnection) new URL(URLName)
                    .openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String replace(String _text, String _searchStr,
                                 String _replacementStr) {
        // String buffer to store str
        StringBuffer sb = new StringBuffer();

        // Search for search
        int searchStringPos = _text.indexOf(_searchStr);
        int startPos = 0;
        int searchStringLength = _searchStr.length();

        // Iterate to add string
        while (searchStringPos != -1) {
            sb.append(_text.substring(startPos, searchStringPos)).append(
                    _replacementStr);
            startPos = searchStringPos + searchStringLength;
            searchStringPos = _text.indexOf(_searchStr, startPos);
        }

        // Create string
        sb.append(_text.substring(startPos, _text.length()));

        return sb.toString();
    }


    public static String[] MySplit(String original, String separator) {
        Vector<String> nodes = new Vector<String>();
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);
        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++) {
                result[loop] = (String) nodes.elementAt(loop);
                System.out.println(result[loop]);
            }
        }
        return result;
    }

    public static void Delay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);
    }

    public static String ConvertToCurrencyFormat(double Amount) {
        String Pattern = "###,###,###.###";
        DecimalFormat myCurrencyFormat = new DecimalFormat(Pattern);
        return "PKR. " + myCurrencyFormat.format(Amount) + ".00";
    }

    public static String ConvertToCurrencySimpleFormat(double Amount) {
        String Pattern = "###,###,###.###";
        DecimalFormat myCurrencyFormat = new DecimalFormat(Pattern);
        return myCurrencyFormat.format(Amount) + ".00";
    }

    public static String ConvertToCurrencySimpleFormat(String Amount) {
        String retAmount;
        String Pattern = "###,###,###.###";
        DecimalFormat myCurrencyFormat = new DecimalFormat(Pattern);
        retAmount = myCurrencyFormat.format(Double.parseDouble(Amount));

        if (!retAmount.contains(".")) {
            retAmount = retAmount + ".00";
        }
        return retAmount;
    }

    //+00000001100=1.100
    static public String CustomCurrencyFormat(String Amount) {
        String pattern = "00.00";// "$###,##0.00" or "0.00"; // 100,000,000.000
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String stringFormatOutput = myFormatter.format(Double
                .parseDouble(Amount));
        return stringFormatOutput;
    }

    public static String GetLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress()
                                .toString();
                        Log.e("ip address", "" + ipaddress);
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException ex) {
            return "ERROR Obtaining IP";
        }
        return "No IP Available / Network not Available.";
    }

    public static String GetTxnDateTime() {
        String retValue;
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            retValue = dateFormat.format(date); // Get dynamic date time

            /*
             * DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
             * Calendar cal = Calendar.getInstance();
             * dateFormat.format(cal.getTime()); retValue =
             * dateFormat.format(cal.getTime());
             */

        } catch (Exception ex) {
            retValue = "ERROR Obtaining Date time";
        }
        return retValue;
    }

    public static String GetFormattedDate(String Date) {
        String retValue;
        try {
            //Date = yyMMDD
            // open	//retValue = Date.substring(4, 6) + "-" + Date.substring(2, 4) + "-" + "20" + Date.substring(0, 2);
            retValue = Date;
        } catch (Exception ex) {
            retValue = "ERROR Obtaining Date time";
        }
        return retValue;
    }

    public static String GetFormattedBilingMonth(String Date) {
        String retValue;

        //20140102
        //open
		/* Calendar cal1 = new GregorianCalendar(Integer.parseInt(Date.substring(0,4)), 
				 Integer.parseInt(Date.substring(4,6)), Integer.parseInt(Date.substring(6, 8)));*/
        // constructor could also be empty
        // calendar cal2 = new GregorianCalendar();
        // change the month
        // cal1.set(Calendar.MONTH, Calendar.MAY);

        //open //cal1.set(Calendar.MONTH, (Calendar.MONTH) - 1);

        // format the output with leading zeros for days and month
        SimpleDateFormat date_format = new SimpleDateFormat("dd-MMM");

        //open //retValue = date_format.format(cal1.getTime());
        retValue = Date;


        return retValue;
    }


    //Get Time difference from start to end
    public static String GetTimeDifference(String datestart, String dateend) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");
        String taketime = null;
        try {
            Date date1 = simpleDateFormat.parse(datestart);
            Date date2 = simpleDateFormat.parse(dateend);

            taketime = printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return taketime;
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String completeTime = String.format("%d days, %d hours, %d minutes, %d seconds",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        return completeTime;
    }


    //Get formatted date time
    public static String getdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
       /* android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("yyyy-MM-dd HH:mm:ss", new Date());

        return format;*/
    }

    //Get formatted date time with milli seconds
    public static String getdateTimeInMilli() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:sss", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
        /*android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("yyyy-MM-dd HH:mm:ss:sss", new Date(),Locale.ENGLISH);

        return format;*/
    }

    //Get formatted date
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
       /* android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("dd MM yyyy", new Date());

        return format;*/
    }

    //Get formatted date for RTA
    public static String getFormattedDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
       /* android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("dd/MM/yyyy", new Date());

        return format;*/
    }

    //Get formatted Time
    public static String getFormattedTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:a", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
        /*android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("HH:mm:a", new Date());

        return format;*/
    }

    //Get formatted Time
    public static String getFormattedYear(String year) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
        /*android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("yyyy", new Date());
        return format;*/
    }

    public static String getYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
        /*android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("yyyy", new Date());
        return format;*/
    }

    //Get date time for payment for vehicle and
    public static String getdateTimeforNull() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
      /*  android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("dd-MM-yyyy HH:mm", new Date());

        return format;*/
    }

  /*  //Get formatted Time
    public static String getFormattedExpiryDateRTA(String expiryDate) {
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String currentYear = (String) df.format("yyyy", new Date());
        String Century = currentYear.substring(0,1);
        expiryDate = expiryDate.substring(0, 1) + "-" + Century + expiryDate.substring(2, 3);
        return expiryDate;
    }*/

    //Get formatted Time
    public static String getTime() {
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String format = (String) df.format("HHmmssmss", new Date());

        return format;
    }

    public static String getUUID() {
        //get Randomly generated value
        UUID uuid = UUID.randomUUID();
        String uuidInString = uuid.toString();
        return uuidInString;
    }

    static String result = null;

    public static String getAddressFromLocation(final double latitude, final double longitude,
                                                final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        //result = result; //"Latitude: " + latitude + " Longitude: " + longitude + "\n\nAddress:\n" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Unable to get address for this lat-long."; //"Latitude: " + latitude + " Longitude: " + longitude +"\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };

        thread.start();
        return result;
    }


    static CountDownTimer countDownTimer;
    static long millisInFuture = 10000; //10 seconds
    static long countDownInterval = 1000; //1 second
    static long stopWatchTimeRemaining = 0;
    static boolean isPaused = false;
    static boolean isStopped = false;

    Timer timer;
    long timerInterval = 1000; //1 second
    long timerDelay = 1000; //1 second
    int Count = 0;
    static String tvRemainingTime;

    public void resumeStopWatch(Context context) {
        isPaused = false;
        isStopped = false;
        startStopWatch(context);
    }

    public static void stopStopWatch() {
        isPaused = false;
        isStopped = true;
        tvRemainingTime = "Stopped after " + (((millisInFuture - stopWatchTimeRemaining) / 1000) + 1) + " Time is Reset back to 10 seconds";
        stopWatchTimeRemaining = millisInFuture;
    }

    public void pauseStopWatch() {
        isPaused = true;
    }

    public static void startStopWatch(Context context) {
        isPaused = false;
        isStopped = false;

        countDownTimer = new CountDownTimer(stopWatchTimeRemaining, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (isPaused || isStopped) {
                    //If the user request to cancel or paused the
                    //CountDownTimer we will cancel the current instance
                    cancel();
                } else {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    tvRemainingTime = "Remaining Time : " + (millisUntilFinished / 1000) + " Seconds";

                    //Put count down timer remaining time in a variable
                    stopWatchTimeRemaining = millisUntilFinished;
                }

            }

            @Override
            public void onFinish() {
                Toast.makeText(context, tvRemainingTime, Toast.LENGTH_LONG).show();
                //tvRemainingTime.setText("Your allotted time is finished : "+(millisInFuture/1000)+" Seconds");
            }
        }.start();

    }

    public void stopTimer() {

        //tvElapsedTime.setText("Stopped after " + Count + " s");
        Count = 0;
        pauseTimer();
    }

    public void resumeTimer() {
        startTimer();

    }

    public void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //tvElapsedTime.setText(Count + " s");
                Count++;
            }
        }, timerDelay, timerInterval);
    }

    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    //Button Clicked Event log
    public static void WriteTextInTextFile(File file, String buttonClicked) {

        try {
            FileWriter fw = new FileWriter(file, true); //the true will append the new data
            //if((Helper.isAppRunning(MainActivity.this, "example.dtc")))

            fw.write(Constant.SEP_NEWLINE + Constant.DateAndTime + "|" + getdateTime() + "|" + " " + Constant.ButtonEventClicked + "|" + buttonClicked);//appends the string to the file
            fw.close();
        } catch (IOException ex) {
            Log.e("Tag", ex.getMessage(), ex);
        }
    }


    //Get File path for log
    public static String path;
    public static File file;
    public static File directory;

    public static File getFilePath() {
        //Define a path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.DirectoryName;
        //Create a Directory
        directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //Saved data in this text file
        file = new File(path + Constant.FileName);
        return file;
    }

    //Exception write in a different text
    public static File getFilePathForException() {
        //Define a path
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.DirectoryName;
        //Create a Directory
        directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        //Saved data in this text file
        file = new File(path + Constant.FileNameException);
        return file;
    }

    //Trip Details log
    public static String DriverName;
    public static String DoubleLine;
    public static String SingleLineorEmpty;

    public static void WriteTextInTextFileForTrip(File file, String TripID, String TripStatus, String GivenName, String FinalName) {
        try {
            FileWriter fw = new FileWriter(file, true); //the true will append the new data
            //if((Helper.isAppRunning(MainActivity.this, "example.dtc")))
            if (TripStatus.equals(Constant.TSEventDescriptionValue)) {
                SingleLineorEmpty = Constant.SEP_NEWLINE;
            } else {
                SingleLineorEmpty = "";
            }

            DriverName = GivenName + FinalName;
            fw.write(DoubleLine + Constant.TripDetails + Constant.SEP_NEWLINE + Constant.TextTripID + TripID
                    + Constant.SEP_NEWLINE + Constant.TextTripStatus
                    + TripStatus + Constant.SEP_NEWLINE + Constant.TextDriverName + DriverName + Constant.SEP_NEWLINE + Constant.DateAndTime + getdateTime() + SingleLineorEmpty);//appends the string to the file
            fw.close();
        } catch (IOException ex) {
            Log.e("Tag", ex.getMessage(), ex);
        }
    }

    //Shift Details log
    public static String DoubleLineorTripleLine;

    public static void WriteTextInTextFileForShift(File file, String ShiftId, String ShiftStatus, String GivenName, String FinalName) {
        try {
            FileWriter fw = new FileWriter(file, true); //the true will append the new data
            //if((Helper.isAppRunning(MainActivity.this, "example.dtc")))

            if (ShiftStatus.equals(Constant.SSEventDescriptionKey))
                DoubleLineorTripleLine = Constant.SEP_DOUBLENEWLINE;
            else {
                DoubleLineorTripleLine = Constant.SEP_TRIPLENEWLINE;
            }

            DriverName = GivenName + FinalName;
            fw.write(DoubleLineorTripleLine + Constant.SEP_NEWLINE + Constant.TextShiftStatus
                    + ShiftStatus + Constant.SEP_NEWLINE + Constant.TextShiftID + ShiftId
                    + Constant.SEP_NEWLINE + Constant.TextDriverName + DriverName + Constant.SEP_NEWLINE + Constant.DateAndTime + getdateTime() + Constant.SEP_NEWLINE);//appends the string to the file
            fw.close();
        } catch (IOException ex) {
            Log.e("Tag", ex.getMessage(), ex);
        }
    }

    public static void WriteExceptionofExceptionLogInTextFile(File file, String ExceptionMessage) {

        try {
            FileWriter fw = new FileWriter(file, true); //the true will append the new data

            fw.write(Constant.SEP_NEWLINE + Constant.DateAndTime + "|" + getdateTime() + "|" + " " + Constant.ExceptionMessageofExceptionLogService + "|" + ExceptionMessage);//appends the string to the file
            fw.close();
        } catch (IOException ex) {
            Log.e("Tag", ex.getMessage(), ex);
        }
    }

    public static boolean validateEmail(Context context, EditText edt_Email) {
        boolean valid = true;
        String email = edt_Email.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_Email.setError("enter a valid email address");
            Toast.makeText(context, "enter a valid email address", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            edt_Email.setError(null);
        }
        return valid;
    }

    public static boolean validatePassword(Context context, EditText edt_Password) {
        boolean valid = true;
        String password = edt_Password.getText().toString();

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edt_Password.setError("Please enter a correct password between 4 and 10 characters");
            Toast.makeText(context, "enter a valid password address", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            edt_Password.setError(null);
        }

        return valid;
    }

    public static boolean validatePhoneNo(Context context, EditText edt_Phone_No) {
        boolean valid = true;
        String phone_No = edt_Phone_No.getText().toString();

        if (phone_No.isEmpty() || phone_No.length() < 10 || phone_No.length() > 10) {
            edt_Phone_No.setError("Please enter a correct phone no");
            Toast.makeText(context, "enter a valid phone no", Toast.LENGTH_LONG).show();
            valid = false;
        } else {
            edt_Phone_No.setError(null);
        }

        return valid;
    }

    /**
     * CVV will be hiddenby stars
     */
    public static class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }

            public char charAt(int index) {
                char caracter;
                switch (index) {
                    case 4:
                        caracter = ' ';
                        break;
                    case 9:
                        caracter = ' ';
                        break;
                    case 14:
                        caracter = ' ';
                        break;
                    default:
                        if (index < 15)
                            return '*';
                        else
                            caracter = mSource.charAt(index);
                        break;

                }


                return caracter;
            }

            public int length() {
                return mSource.length();
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }

    public static CountDownTimer SetCountDownTimer(CountDownTimer cTimer, long millisInFuture, long countDownInterval, TextView textView,
                                                   Fragment aClass, FragmentManager fragmentManager) {
        cTimer = new CountDownTimer(millisInFuture, countDownInterval) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                //Set timer in seconds seconds
                textView.setText(String.format("%02d", seconds));

            }

            public void onFinish() {
                fragmentManager.beginTransaction()
                        .replace(R.id.content, aClass)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        }.start();

        return cTimer;
    }

    public static CountDownTimer SetCountDownTimer(CountDownTimer cTimer, long millisInFuture, long countDownInterval,
                                                   Fragment aClass, FragmentManager fragmentManager) {
        cTimer = new CountDownTimer(millisInFuture, countDownInterval) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

            }

            public void onFinish() {
                fragmentManager.beginTransaction()
                        .replace(R.id.content, aClass)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        }.start();

        return cTimer;
    }

    //0-9,A-Z,comma and space
    public static boolean KeyboardKeyClick(int keyCode, KeyEvent keyEvent, CountDownTimer cTimer) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_1:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_2:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_3:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_4:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_5:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_6:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_7:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_8:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_9:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_A:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_B:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_C:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_D:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_E:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_F:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_G:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_H:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_I:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_J:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_K:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_L:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_M:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_N:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_O:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_P:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_Q:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_R:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_S:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_T:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_U:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_V:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_W:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_X:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_Y:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_Z:
                if (cTimer != null)
                    cTimer.start();
            case KeyEvent.KEYCODE_COMMA:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_SPACE:
                if (cTimer != null)
                    cTimer.start();
                return true;
            case KeyEvent.KEYCODE_NUMPAD_DOT:
                if (cTimer != null)
                    cTimer.start();
                return true;
            default:
                return true;
        }
    }

    //When text is changed reset the timer
    public static TextWatcher getTextWatcher(CountDownTimer cTimer) {

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (cTimer != null)
                    cTimer.start();

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
                if (cTimer != null)
                    cTimer.start();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cTimer != null)
                    cTimer.start();
            }
        };
        return tw;
    }

    static AnimatorSet animatorSet;
    private static boolean animationcheck;
    private static int i = 0;

    //When text is changed reset the timer for 2 edittexts
    public static TextWatcher getTextWatcher(CountDownTimer cTimer, EditText editText, EditText editText1, Button btn_Search) {

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (cTimer != null)
                    cTimer.start();
                if (editText.getText().toString().length() <= 0) {
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        i = 0;
                    }
                    return;
                } else if (editText1.getText().toString().length() <= 0) {
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        i = 0;
                    }
                    return;
                } else {
                    if (i == 0) {
                        //Animation(Fade in/Fade out) of the button
                        animatorSet = ButtonAnimation(btn_Search);
                        i = 1;
                    } else {
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
                if (cTimer != null)
                    cTimer.start();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cTimer != null)
                    cTimer.start();
            }
        };
        return tw;
    }

    AnimatorSet mAnimationSet = new AnimatorSet();

    //When text is changed reset the timer for 1 edittext
    public static TextWatcher getTextWatcher(CountDownTimer cTimer, EditText editText, Button btn_Search) {

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (cTimer != null)
                    cTimer.start();
                if (editText.getText().toString().length() <= 0) {
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        animatorSet.end();
                        i = 0;
                    }
                    return;
                } else {
                    if (i == 0) {
                        //Animation(Fade in/Fade out) of the button
                        animatorSet = ButtonAnimation(btn_Search);
                        i = 1;
                    } else {
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
                if (cTimer != null)
                    cTimer.start();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cTimer != null)
                    cTimer.start();
            }
        };
        return tw;
    }

    static int prevL = 0;
    static boolean isFormatting = false;
    static boolean deletingHyphen = false;
    static int hyphenStart = 0;
    static boolean deletingBackward = false;

    /*When text is changed reset the timer for 1 edittext and
    this is specially for Emirates ID to put dashes and delete
    dashes as the code is extended because android keyboard
    doesn'nt do it by itself */
    public static TextWatcher getTextWatcherforRMSEmiratesID(CountDownTimer cTimer, EditText editText, Button btn_Search) {

        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (cTimer != null)
                    cTimer.start();

                if (editText.getText().toString().length() <= 0) {
                    if (animatorSet != null) {
                        animatorSet.cancel();
                        i = 0;
                    }
                    return;
                } else {
                    if (i == 0) {
                        //Animation(Fade in/Fade out) of the button
                        animatorSet = ButtonAnimation(btn_Search);
                        i = 1;
                    } else {
                    }
                }

                if (isFormatting)
                    return;

                isFormatting = true;

                // If deleting hyphen, also delete character before or after it
                if (deletingHyphen && hyphenStart > 0) {
                    if (deletingBackward) {
                        if (hyphenStart - 1 < s.length()) {
                            s.delete(hyphenStart - 1, hyphenStart);
                        }
                    } else if (hyphenStart < s.length()) {
                        s.delete(hyphenStart, hyphenStart + 1);
                    }
                }
                int length = editText.length();
                if ((prevL < length) && (length == 3 || length == 8 || length == 16)) {
                    editText.append("-");
                }
                isFormatting = false;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
                if (cTimer != null)
                    cTimer.start();

                if (isFormatting)
                    return;

                // Make sure user is deleting one char, without a selection
                final int selStart = Selection.getSelectionStart(s);
                final int selEnd = Selection.getSelectionEnd(s);
                if (s.length() > 1 // Can delete another character
                        && count == 1 // Deleting only one character
                        && after == 0 // Deleting
                        && s.charAt(start) == '-' // a hyphen
                        && selStart == selEnd) { // no selection
                    deletingHyphen = true;
                    hyphenStart = start;
                    // Check if the user is deleting forward or backward
                    if (selStart == start + 1) {
                        deletingBackward = true;
                    } else {
                        deletingBackward = false;
                    }
                } else {
                    deletingHyphen = false;
                }
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (cTimer != null)
                    cTimer.start();
            }
        };
        return tw;
    }

    //Write Object to Cache
    public static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    //Read Object from Cache
    public static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    //Checks Internets
    private boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    /*
    //Timer in hours minutes and seconds, copy anywhere and use it
            new CountDownTimer(10000, 1000) { // adjust the milli seconds here

        public void onTick(long millisUntilFinished) {

            int seconds = (int) (millisUntilFinished / 1000);

            int hours = seconds / (60 * 60);
            int tempMint = (seconds - (hours * 60 * 60));
            int minutes = tempMint / 60;
            seconds = tempMint - (minutes * 60);

                 //Hours minutes and seconds
                    tv_timer.setText("TIME : " + String.format("%02d", hours)
                            + ":" + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
        }

        public void onFinish() {
            tv_timer.setText("done!");
        }
    }.start();*/
}

