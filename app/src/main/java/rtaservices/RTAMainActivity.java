package rtaservices;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


import example.dtc.Manifest;
import example.dtc.R;
import rtamain.RTAMain;
import services.SystemUIService;
import utility.Constant;


public class RTAMainActivity extends AppCompatActivity {

    Fragment mFragment;
    public String path;
    private File file;
    private File directory;
    private static FileInputStream is;
    private static BufferedReader reader;
    private static String PrinterIP;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rtamainactivity);

        mFragment = RTAMain.newInstance();
        addFragment();

        HideNavigationBar();
       // verifyStoragePermissions(activity);
       // makeDirecory();

    }

    public void HideNavigationBar() {
        SystemUIService.setNaviButtonVisibility(this.getApplicationContext(), SystemUIService.NAVIBUTTON_NAVIBAR, View.GONE);
        SystemUIService.setStatusBarVisibility(this.getApplicationContext(), View.GONE);
    }


    public void makeDirecory() {
        try {
            //Define a path
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.DirectoryNameICounter;
            //Create a Directory
            directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //Saved data in this text file
            file = new File(path + Constant.FileNameICounterPrintingPCIP);
            //Writing the text in the file
            WriteTextInTextFile(file);
            //Reading the text from file
            getIPfromtheFile(file);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    //Button Clicked Event log
    public static void WriteTextInTextFile(File file) {

        try {
            FileWriter fw = new FileWriter(file, true);

            fw.write("192.168.0.1");
            fw.close();
        } catch (IOException ex) {
            Log.e("Tag", ex.getMessage(), ex);
        }
    }

    public static String getIPfromtheFile(File file) {

        try {
            if (file.exists()) {

                is = new FileInputStream(file);

                reader = new BufferedReader(new InputStreamReader(is));
                PrinterIP = reader.readLine();
                while (PrinterIP != null) {
                    Log.d("Printer IP", PrinterIP);
                    PrinterIP = reader.readLine();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PrinterIP;
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    public void onStart() {
        super.onStart();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onResume() {
        super.onResume();
    }

}
