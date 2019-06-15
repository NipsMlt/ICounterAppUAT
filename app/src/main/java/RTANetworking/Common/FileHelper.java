package RTANetworking.Common;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import utility.Constant;

/**
 * Created by Tan on 2/18/2016.
 */
public class FileHelper {
    final static String fileName = "data.txt";
    //final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/instinctcoder/readwrite/";
    final static String Path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.DirectoryNameICounter;
    final static String TAG = FileHelper.class.getName();

    // public static String Path;
    // private static File file;
    // private static File directory;

    public static void saveTextFile() {


    }

    public static String ReadFile(Context context) {
        String line = null;

        try {
           /* //Define a path
            Path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.DirectoryNameICounter;
            //Create a Directory
            directory = new File(Path);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            //Saved data in this text file
            file = new File(Path + Constant.FileNameICounterPrintingPCIP);*/

            FileInputStream fileInputStream = new FileInputStream(new File(Path + Constant.FileNameICounterPrintingPCIP));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public static boolean saveToFile() {
        try {
            new File(Path).mkdir();
            File file = new File(Path + Constant.FileNameICounterPrintingPCIP);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            //fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        } catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return false;


    }

}