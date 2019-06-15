package loginfragments;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;


import example.dtc.R;
import rtamain.RTAMain;
import saioapi.service.SystemUIService.SystemUIService;
import services.CommonService.ExceptionLogService.ExceptionService;

import static android.content.Context.AUDIO_SERVICE;

public class SettingsMenuFragmnent extends Fragment {

    Fragment mFragment;
    Button btnBack;
    ImageView volumnBtn, iv_Setting, iv_XACLauncher;
    SeekBar volumeBar, brightnessBar;
    int volumeLevel;
    AudioManager audioManager;
    private float brightness = -1;
    private WindowManager.LayoutParams lpa;
    String ClassName;

    public SettingsMenuFragmnent() {
        // Required empty public constructor
    }

    public static SettingsMenuFragmnent newInstance() {
        return new SettingsMenuFragmnent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settingsmenu, container, false);
        btnBack = (Button) view.findViewById(R.id.btnback);

        volumeBar = (SeekBar) view.findViewById(R.id.volumeBar);
        brightnessBar = (SeekBar) view.findViewById(R.id.brightnessBar);
        audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumnBtn = (ImageView) view.findViewById(R.id.btn_volume);
        iv_Setting = (ImageView) view.findViewById(R.id.iv_setting);
        iv_XACLauncher = (ImageView) view.findViewById(R.id.launcher);

        //get Class Name
        ClassName = getClass().getCanonicalName();

        //Setting the volume control
        VolumeControl();

        iv_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    SettingsFramgnet don = new SettingsFramgnet();
                    don.show(getFragmentManager(), "");
                } catch (Exception e) {
                    ExceptionService.ExceptionLogService(getContext(), e.getMessage(), ClassName, RTAMain.AndroidSerialNo);
                }

              /*  //Randomly generated no for taxi meter info service request ID
                UUID uuid = UUID.randomUUID();
                String uuidInString = uuid.toString();

                TaxiMeterInfoService taxiMeterInfoService = new TaxiMeterInfoService(getContext());
                taxiMeterInfoService.CallTaxiMeterInfoService(Constant.ServiceId, uuidInString, Constant.SourceApplication,
                        "", "", MainActivity.getdateTime(), Constant.LoginID, Constant.Password,
                        Constant.androidDeviceIdvalue, Constant.androidSerialNovalue, Constant.deviceSerialNovalue,
                        Constant.plateNovalue, Constant.taxiCodevalue, MainActivity.getdateTime());*/
            }
        });
        iv_XACLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUIService.setNaviButtonVisibility(getContext(), SystemUIService.NAVIBUTTON_NAVIBAR, View.VISIBLE);
                //It exits to the installer where the apk gets installed
                Intent intent = new Intent();
                intent.setClassName("com.xac.util.saioutility", "com.xac.util.saioutility.Main");
                startActivity(intent);
            }
        });


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getContext())) {
                // Do stuff here
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }*/


        // Set a SeekBar change listener
        brightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Display the current progress of SeekBar
                //mTextView.setText("Screen Brightness : " + i);

                // Change the screen brightness
                //This is taking a brightness from the system
                //setScreenBrightness(i);

                //This is manually increasing the brightness
                BrightnessControl(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content, SettingsLoginFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;

    }

    private void VolumeControl() {

        if (volumeLevel == 0) {
            volumnBtn.setBackgroundResource(R.drawable.ic_volume_off_black_36dp);
        } else {
            volumnBtn.setBackgroundResource(R.drawable.ic_volume_up_black_36dp);
        }

        volumnBtn.setOnClickListener(view -> {
            volumeLevel = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeBar.setProgress(volumeLevel);
            /*if (volumeBar.getVisibility() == View.GONE) {
                volumeBar.setVisibility(View.VISIBLE);

            } else {
                volumeBar.setVisibility(View.GONE);

            }*/
        });

        volumeBar.setProgress(volumeLevel);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volumeLevel = i;
                if (volumeLevel == 0) {
                    volumnBtn.setBackgroundResource(R.drawable.ic_volume_off_black_36dp);
                } else {
                    volumnBtn.setBackgroundResource(R.drawable.ic_volume_up_black_36dp);
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // volumeBar.setVisibility(View.GONE);


                if (volumeLevel == 0) {
                    volumnBtn.setBackgroundResource(R.drawable.ic_volume_off_black_36dp);
                } else {
                    volumnBtn.setBackgroundResource(R.drawable.ic_volume_up_black_36dp);
                }
            }
        });
    }


    private void BrightnessControl(float percent) {
        if (brightness < 0) {
            brightness = getActivity().getWindow().getAttributes().screenBrightness;
            if (brightness <= 0.00f) {
                brightness = 0.50f;
            } else if (brightness < 0.01f) {
                brightness = 0.01f;
            }
        }
        Log.d(this.getClass().getSimpleName(), "brightness:" + brightness + ",percent:" + percent);
        lpa = getActivity().getWindow().getAttributes();
        lpa.screenBrightness = brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        //tv_video_brightBox.setText(((int) (lpa.screenBrightness * 100)) + "%");
        getActivity().getWindow().setAttributes(lpa);
    }

    // Change the screen brightness
    public void setScreenBrightness(int brightnessValue) {
        /*
            public abstract ContentResolver getContentResolver ()
                Return a ContentResolver instance for your application's package.
        */
        /*
            Settings
                The Settings provider contains global system-level device preferences.

            Settings.System
                System settings, containing miscellaneous system preferences. This table holds
                simple name/value pairs. There are convenience functions for accessing
                individual settings entries.
        */
        /*
            public static final String SCREEN_BRIGHTNESS
                The screen backlight brightness between 0 and 255.
                Constant Value: "screen_brightness"
        */
        /*
            public static boolean putInt (ContentResolver cr, String name, int value)
                Convenience function for updating a single settings value as an integer. This will
                either create a new entry in the table if the given name does not exist, or modify
                the value of the existing row with that name. Note that internally setting values
                are always stored as strings, so this function converts the given value to a
                string before storing it.

            Parameters
                cr : The ContentResolver to access.
                name : The name of the setting to modify.
                value : The new value for the setting.
            Returns
                true : if the value was set, false on database errors
        */

        // Make sure brightness value between 0 to 255
        if (brightnessValue >= 0 && brightnessValue <= 255) {
            Settings.System.putInt(
                    getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
        }
    }

    // Get the screen current brightness
    protected int getScreenBrightness() {
        /*
            public static int getInt (ContentResolver cr, String name, int def)
                Convenience function for retrieving a single system settings value as an integer.
                Note that internally setting values are always stored as strings; this function
                converts the string to an integer for you. The default value will be returned
                if the setting is not defined or not an integer.

            Parameters
                cr : The ContentResolver to access.
                name : The name of the setting to retrieve.
                def : Value to return if the setting is not defined.
            Returns
                The setting's current value, or 'def' if it is not defined or not a valid integer.
        */
        int brightnessValue = Settings.System.getInt(
                getContext().getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                0
        );
        return brightnessValue;
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

}



