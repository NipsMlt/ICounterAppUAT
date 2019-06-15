package saioapi.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * Created by dennis_wang on 2015/10/22.
 */
public class SaioService {

    static
    {
        //
        //  Load the corresponding library
        //
        System.loadLibrary("SaioUtil");
    }
    
    /** The inputted parameters are not valid. */
    public static final int ERR_2ND_BRIGHTNESS_INVALID_PARAM      = 0x0000E000;

    /** No such device. */
    public static final int ERR_2ND_BRIGHTNESS_NO_SUCH_DEVICE     = 0x0000E001;
    
    /** No such device. */
    public static final int ERR_2ND_TOUCH_NO_SUCH_DEVICE     = 0x0000E002;

    private static final String SET_2ND_BRIGHTNESS = "BLService.SET_2ND_BRIGHTNESS";
    private static final String GET_2ND_BRIGHTNESS = "BLService.GET_2ND_BRIGHTNESS";
    private static final String RET_2ND_BRIGHTNESS = "BLService.RETURN_2ND_2ND_BRIGHTNESS";
    private static final String BRIGHTNESS_LEVEL = "brightness_level";

    private static final String GET_2ND_TOUCHID = "SaioService.GET_2ND_TOUCHID";
    private static final String RET_2ND_TOUCHID = "SaioService.RETURN_2ND_TOUCHID";
    private static final String SECOND_TOUCHID = "2nd_touch_id";

    private static final String REBOOT_DEVICE = "SaioService.REBOOT_DEVICE";
    private static final String REBOOT_REASON = "reboot_reason";
    
    private static final String ACTION_ACTIVATE_2ND_TOUCH = "SaioService.ACTIVATE_2ND_TOUCH";
    private static final String ACTION_DEACTIVATE_2ND_TOUCH = "SaioService.DEACTIVATE_2ND_TOUCH";

    public static final int ANTENNA_INTERNAL = 1;
    public static final int ANTENNA_EXTERNAL = 0;

    private static final String SET_LED = "SaioService.SET_LED";
    private static final String SET_PINENTRY = "SaioService.SET_PINENTRY";
    private static final String LED_ID = "led_id";
    private static final String LED_VALUE = "led_value";
    private static final String PINENTRY_EN = "pin_enable";

    private static final String START_POLLING_CTLS_LED ="SaioService.START_POLLING_CTLS_LED";
    private static final String STOP_POLLING_CTLS_LED ="SaioService.STOP_POLLING_CTLS_LED";

    public static final String SET_REBOOT_TIME = "SaioService.SET_REBOOT_TIME";
    public static final String REBOOT_HOUR = "SaioService.REBOOT_HOUR";
    public static final String REBOOT_MINUTE = "SaioService.REBOOT_MINUTE";

    private static final String ACTION_ACTIVATE_POWERKEY = "SaioService.ACTIVATE_POWERKEY";
    private static final String ACTION_DEACTIVATE_POWERKEY = "SaioService.DEACTIVATE_POWERKEY";

    private static final String ACTION_LATINIME_ENABLE_SOUND_ON_KEYPRESS = "SaioService.ENABLE_SOUND_ON_KEYPRESS";
    private static final String ACTION_LATINIME_DISABLE_SOUND_ON_KEYPRESS = "SaioService.DISABLE_SOUND_ON_KEYPRESS";

    private Context mContext;
    private OnSaioListener mOnSaioListener;
    private BlLevelReceiver mBlLevelReceiver;

    /** The LED id indicates the status bar led with blue light. */
    public static final int LED_STATUS_BAR_BLUE       = 0x00;

    /** The LED id indicates the status bar led with red light. */
    public static final int LED_STATUS_BAR_RED        = 0x01;

    /** The LED id indicates the status bar led with green light. */
    public static final int LED_STATUS_BAR_GREEN      = 0x02;

    /** The LED id indicates the status bar led with yellow light. */
    public static final int LED_STATUS_BAR_YELLOW     = 0x03;

    /** The LED id indicates the 1st led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT1        = 0x04;

    /** The LED id indicates the 2nd led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT2        = 0x05;

    /** The LED id indicates the 3rd led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT3        = 0x06;

    /** The LED id indicates the 4th led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT4        = 0x07;

    /** The LED id indicates the 5th led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT5        = 0x08;

    /** The LED id indicates the 6th led from the left in MSR slot. */
    public static final int LED_MSR_SLOT_LEFT6        = 0x09;

    /** The LED id indicates the logo led. */
    public static final int LED_LOGO                  = 0x0A;

    /** The LED id indicates the led in SCR slot. */
    public static final int LED_SCR_SLOT              = 0x0B;

    /** The LED id indicates the keypad led. */
    public static final int LED_KEY_PAD               = 0x0C;

    /** The LED id indicates the MSR led of product E200CP. */
    public static final int LED_MSR_SLOT_E200CP       = 0x0D;

    /**
     * SaioService constructor.
     *
     * @param context App context.
     * @param onSaioListener listener to get data from SaioService
     */
    public SaioService(Context context, OnSaioListener onSaioListener){
        mContext = context;
        mOnSaioListener = onSaioListener;

        mBlLevelReceiver = new BlLevelReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(RET_2ND_BRIGHTNESS);
        filter.addAction(RET_2ND_TOUCHID);
        mContext.registerReceiver(mBlLevelReceiver, filter);
    }


    /**
     * The method will unregister OnSaioListener.
     *
     */
    public void release(){
        if(mBlLevelReceiver != null) {
            mContext.unregisterReceiver(mBlLevelReceiver);
            mBlLevelReceiver = null;
            mContext = null;
        }
    }

    /**
     * The method can be used to set the brightness of the 2nd display.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     *
     * @param brightness The brightness of the 2nd display (0~1.0).
     * @return zero if there is no error else nonzero error code defined in class constants.
     */
    public int set2ndDispBrightness(float brightness){
        DisplayManager displayManager = (DisplayManager)mContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] display = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if(display.length == 0){
            return ERR_2ND_BRIGHTNESS_NO_SUCH_DEVICE;
        }
        if((brightness < 0)||(brightness > 1.0))
            return ERR_2ND_BRIGHTNESS_INVALID_PARAM;
        Intent blIntent = new Intent();
        blIntent.setAction(SET_2ND_BRIGHTNESS);
        blIntent.putExtra(BRIGHTNESS_LEVEL, brightness);
        mContext.sendBroadcast(blIntent);
        return 0;
    }

    /**
     * The method send request to get brightness of the 2nd display, will return by OnSaioListener.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     *
     * @return zero if there is no error else nonzero error code defined in class constants.
     */
    public int get2ndDispBrightness(){
        DisplayManager displayManager = (DisplayManager)mContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] display = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if(display.length == 0){
            return ERR_2ND_BRIGHTNESS_NO_SUCH_DEVICE;
        }
        Intent blIntent = new Intent();
        blIntent.setAction(GET_2ND_BRIGHTNESS);
        mContext.sendBroadcast(blIntent);
        return 0;
    }
    
    /**
     * The method will return secondary touch id.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     *
     * @return device id if there is no error else nonzero error code defined in class constants.
     */
    public int get2ndTouchDeviceId(){
        DisplayManager displayManager = (DisplayManager)mContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] display = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        if(display.length == 0){
            return ERR_2ND_TOUCH_NO_SUCH_DEVICE;
        }
        Intent tpIntent = new Intent();
        tpIntent.setAction(GET_2ND_TOUCHID);
        mContext.sendBroadcast(tpIntent);
        return 0;
    }
    
    /**
     * Activate the secondary touch.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     */
    public void activate2ndTouch()
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_ACTIVATE_2ND_TOUCH);
        mContext.sendBroadcast(intent);
    }
    
    /**
     * Deactivate the secondary touch.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     */
    public void deactivate2ndTouch()
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_DEACTIVATE_2ND_TOUCH);
        mContext.sendBroadcast(intent);
    }
    
    /**
     * Activate or deactivate the secondary touch if it's available.
     * <p>
     *     Note: only for E200I, T3
     * </p>
     *
     * @param on set true to activate; false to deactivate
     */
    private static void set2ndTouchActive(boolean on)
    {
        native_set2ndTouchActive((on)?1:0);
    }
    
    /**
     * Call this method to reboot device.
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to request special boot modes, or null.
     * @return device id if there is no error else nonzero error code defined in class constants.
     */
    public int reboot(String reason){
        Intent rebootIntent = new Intent();
        rebootIntent.setAction(REBOOT_DEVICE);
        rebootIntent.putExtra(REBOOT_REASON, reason);
        mContext.sendBroadcast(rebootIntent);
        return 0;
    }
    
    /**
     * Switch antenna between internal(default) or external.
     * <p>
     *     Note: only for T3
     * </p>
     *
     * @param dist {@link #ANTENNA_INTERNAL} or {@link #ANTENNA_EXTERNAL} only
     */
    public static void switchAntenna(int dist){
        if((dist != ANTENNA_INTERNAL)&&(dist != ANTENNA_EXTERNAL))
            return;
        native_switchAntenna(dist);
    }

    /**
     * Call this method to activate/deactivate Epp.
     * <p>
     *     Note: only for 200NP
     * </p>
     * @param enabled activate or deactive Epp.
     */
    public static int setEppEnabled(boolean enabled){
        return native_setEppEnabled(enabled);
    }

    
    /**
     * Power on 3G module manually.
     * <p>
     *     Note: only for T3
     * </p>
     */
    public static void powerOn3Gmodule(){
        native_powerOn3Gmodule();
    }

    /**
     * The method allow user to control the on-board LED on or off.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param led_id The LED id.
     * @param enable Indicates to turn on (true) or off (false) the LED
     */
    public void setLed(int led_id, boolean enabled){
        if(enabled)
            setLedValue(led_id,255);
        else
            setLedValue(led_id,0);
    }

    /**
     * The method allow user to adjust the on-board led brightness.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param led_id The led id.
     * @param value Indicates LED brightness value and must be an integer between 0 and 255.
     *          [Note]: LED_SCR_SLOT/LED_KEY_PAD/LED_MSR_SLOT_E200CP are NOT able to adjust the brightness. The brightness value of these led_id should be 0 or 1.
     *          If the value greater than or equal to 1,that means to set the led_id ON.
     */
    public void setLedValue(int led_id, int value){
        Intent ledIntent = new Intent();
        ledIntent.setAction(SET_LED);
        ledIntent.putExtra(LED_ID, led_id);
        ledIntent.putExtra(LED_VALUE, value);
        mContext.sendBroadcast(ledIntent);
    }

    /**
     * The method enables or disables the PIN entry mode.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param enable Indicates to enable (true) or disable (false) the PIN entry mode
     */
    public void setPinEntryModeEnabled(boolean enabled){
        Intent pinIntent = new Intent();
        pinIntent.setAction(SET_PINENTRY);
        pinIntent.putExtra(PINENTRY_EN, enabled);
        mContext.sendBroadcast(pinIntent);
    }

     /**
     * The method start polling ctls leds status.Use this method,then recevie intent "SaioService.CHANGE_CTLSLed" and extra Int objects with names "ctls_led" to get Ctls Leds status value.The value is Ctls 4 led lights correspond to a 4-bit number.
     * <p>
     *     Note: only for A3
     * </p>
     */
    public void startPollingCtlsLeds(){
        Intent StartIntent = new Intent();
        StartIntent.setAction(START_POLLING_CTLS_LED);
        mContext.sendBroadcast(StartIntent);
    }

     /**
     * The method stop polling ctls leds status.
     * <p>
     *     Note: only for A3
     * </p>
     */
    public void stopPollingCtlsLeds(){
        Intent StopIntent = new Intent();
        StopIntent.setAction(STOP_POLLING_CTLS_LED);
        mContext.sendBroadcast(StopIntent);
    }

    /**
     * Call this method to activate/deactivate power key.
     *
     * @param enabled activate or deactive power key.
     */
    public void setPowerkeyEnabled(boolean enabled){
        Intent intent = new Intent();
        if(enabled)
            intent.setAction(ACTION_ACTIVATE_POWERKEY);
        else
            intent.setAction(ACTION_DEACTIVATE_POWERKEY);
        mContext.sendBroadcast(intent);
    }

    /**
     * Call this method to set enable/disable LatinIME sound on keypress.
     *
     * @param enabled enable or disable LatinIME sound on keypress settings.
     */
    public void setLatinIMESoundOnKeypressEnabled(boolean enabled){
        Intent intent = new Intent();
        if(enabled)
            intent.setAction(ACTION_LATINIME_ENABLE_SOUND_ON_KEYPRESS);
        else
            intent.setAction(ACTION_LATINIME_DISABLE_SOUND_ON_KEYPRESS);
        mContext.sendBroadcast(intent);
    }

    /**
     * The method will return auto reboot status
     *
     * @return device auto reboot enabled or disabled.
     */
    public boolean isRebootEnabled(){
        Method get = null;
        boolean enabled = true;
        String value = "true";
        try {
            if (null == get) {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                get = cls.getDeclaredMethod("get", new Class<?>[]{String.class, String.class});
            }
            value = (String) (get.invoke(null, new Object[]{"persist.sys.reboot.enable", "true"}));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value.equals("true");
    }

    /**
     * The method will return current reboot time
     *
     * @return device reboot time.
     */
    public String getRebootTime(){
        Method get = null;
        String value = "2:0";
        try {
            if (null == get) {
                if (null == get) {
                    Class<?> cls = Class.forName("android.os.SystemProperties");
                    get = cls.getDeclaredMethod("get", new Class<?>[]{String.class, String.class});
                }
            }
            value = (String) (get.invoke(null, new Object[]{"persist.sys.reboot.time", "2:0"}));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * The method will set reboot time
     *
     * @return parameter valid or not.
     */
    public boolean setRebootTime(int hour, int minute){
        if((((hour >= 0)&&(hour <= 23))||(hour == 99))&&(((minute >= 0)&&(minute <= 59))||(minute == 99))) {
            Intent intent = new Intent();
            intent.setAction(SET_REBOOT_TIME);
            intent.putExtra(REBOOT_HOUR, hour);
            intent.putExtra(REBOOT_MINUTE, minute);
            mContext.sendBroadcast(intent);
            return true;
        }else
            return false;
    }

    private class BlLevelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(mOnSaioListener == null){
                return;
            }

            if(intent.getAction().equals(RET_2ND_BRIGHTNESS)) {
                float currLevel = intent.getExtras().getFloat(BRIGHTNESS_LEVEL);
                mOnSaioListener.onBrightness(currLevel);
            }else if(intent.getAction().equals(RET_2ND_TOUCHID)) {
                int touchId = intent.getExtras().getInt(SECOND_TOUCHID);
                mOnSaioListener.onTouchId(touchId);
            }
        }
    }
    
    private static native int native_set2ndTouchActive(int onoff);
    
    private static native int native_switchAntenna(int dist);
    
    private static native int native_powerOn3Gmodule();

    private static native int native_setEppEnabled(boolean enabled);
}
