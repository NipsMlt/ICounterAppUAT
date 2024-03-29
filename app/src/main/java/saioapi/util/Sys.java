//
//	Saio API, sys class
//
package saioapi.util;

import com.xac.sysmanager.UsbAPI;

import saioapi.OnEventListener;

/**
 * This class provides methods to operate system devices.
 */
public class Sys
{
    static
    {
        //
        //  Load the corresponding library
        //
        System.loadLibrary("SaioUtil");
    }
    /** The USB port 1. */
    public static final int USB_PORT_1       = 0x00000000;
    
    /** The USB port 2. */
    public static final int USB_PORT_2       = 0x00000001;
    
    /** The USB port 3. */
    public static final int USB_PORT_3       = 0x00000002;
    
    /** The USB port 4. */
    public static final int USB_PORT_4       = 0x00000003;
    
    /** The USB port 5. */
    public static final int USB_PORT_5       = 0x00000004;
    
    /** The USB port 6. */
    public static final int USB_PORT_6       = 0x00000005;
    
    /** The USB port 7. */
    public static final int USB_PORT_7       = 0x00000006;
    
    /** The USB port 8. */
    public static final int USB_PORT_8       = 0x00000007;
    
    /** The USB port 9. */
    public static final int USB_PORT_9       = 0x00000008;
    
    /** The USB port 10. */
    public static final int USB_PORT_10      = 0x00000009;

    /** The USB port for PPOS. */
    public static final int USB_PORT_PPOS    = USB_PORT_7;
    
    /** The inputted parameters are not valid. */
    public static final int ERR_USB_INVALID_PARAM      = 0x0000E000;
    
    /** The reset USB fail. */
    public static final int ERR_USB_RESET_FAIL      = 0x0000E001;

    /** The CPU family is unknown. */
    public static final int CPU_FAMILY_UNKNOWN    = -1;

    /** The CPU family is IMX6. */
    public static final int CPU_FAMILY_IMX6       = 0;

    /** The CPU family is Qualcomm. */
    public static final int CPU_FAMILY_QCOM       = 1;

    /** The CPU family is Allwinner. */
    public static final int CPU_FAMILY_ALLWINNER       = 2;

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
     * The listener that receives notifications when an CTLS event is triggered.
     */
    private OnEventListener mOnEventListener = null;

    //
    //    Methods
    //

    /**
     * Get the registered Listener that handle the CTLS event.
     * @return The callback to be invoked with a CTLS event is triggered,
     *         or null id no callback has been set.
     */
    public final OnEventListener getOnEventListener()
    {
        return mOnEventListener;
    }

    /**
     * Register a callback to be invoked when a CTLS event is triggered.
     * @param listener The callback that will be invoked.
     */
    public void setOnEventListener(OnEventListener listener)
    {
        mOnEventListener = listener;
    }

    /**
     * The method allow user to reset USB port.
     * <p>
     *     Note: only for T3
     * </p>
     * 
     * <table border=1>
     * <thead><tr><th>USB port layout</th></tr></thead>
     *   <tbody>
     *     <tr><td>USB1</td><td>USB3</td><td>USB5</td></tr>
     *     <tr><td>USB2</td><td>USB4</td><td>USB6</td></tr>
     *   </tbody>
     * </table>
     * @param port The USB port id.
     * @return zero if there is no error else nonzero error code defined in class constants.
     */
    public static int resetUsbPort(int port){
        int deviceNum = UsbMappingTable.getDeviceNum(port);
        int portNum = UsbMappingTable.getPortNum(port);
        return resetUsbPort(deviceNum, portNum);
    }

    /**
     * The method allow user to reset USB port.
     * <p>
     *     Note: only for T3
     * </p>
     *
     * @param device The USB device number.
     * @param port The USB port number.
     * @return zero if there is no error else nonzero error code defined in class constants.
     */
    public static int resetUsbPort(int device, int port){
        return UsbAPI.resetUsbPort(device, port);
    }

    /**
     * The method retrieves the CPU Family information in BSP.
     *
     * @return If successful, the CPU Family information is returned, otherwise a {@link #CPU_FAMILY_UNKNOWN} is returned.
     */
    public static native int getCpuFamily();

    /**
     * System APP use only!!! The method enables or disables the PIN entry mode.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param enable Indicates to enable (true) or disable (false) the PIN entry mode
     * @return Return zero if the function succeeds.
     */
    public static native int setPinEntryModeEnabled(boolean enabled);

    /**
     * System APP use only!!! The method allow user to control the on-board LED on or off.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param led_id The LED id.
     * @param enable Indicates to turn on (true) or off (false) the LED
     * @return Return zero if the function succeeds.
     */
    public static native int setLed(int led_id, boolean enabled);

    /**
     * System APP use only!!! The method allow user to adjust the on-board led brightness.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @param led_id The led id.
     * @param value Indicates LED brightness value and must be an integer between 0 and 255.
     *          [Note]: LED_SCR_SLOT/LED_KEY_PAD/LED_MSR_SLOT_E200CP are NOT able to adjust the brightness. The brightness value of these led_id should be 0 or 1.
     *          If the value greater than or equal to 1,that means to set the led_id ON.
     * @return Return zero if the function succeeds.
     */
    public static int setLedValue(int led_id, int value){
        if (led_id == LED_MSR_SLOT_E200CP){
            return setCpMsrLed(value);
        }else{
            if (value <= 0)
                return setLedBriCurValues(led_id,0,value);
            else
                return setLedBriCurValues(led_id,255,value);
        }
    }

    /**
     * System APP use only!!! The method start polling ctls leds status.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @return Return zero if the function succeeds.
     */
    public int startPollingCtlsLeds(){
        return startCtlsLeds();
    }

     /**
     * System APP use only!!! The method stop polling ctls leds status.
     * <p>
     *     Note: only for A3
     * </p>
     *
     * @return Return zero if the function succeeds.
     */
    public int stopPollingCtlsLeds(){
        return stopCtlsLeds();
    }

    private static native int setLedBriCurValues(int led_id,int brightness,int current);

    private static native int setCpMsrLed(int value);

    private native int startCtlsLeds();

    private native int stopCtlsLeds();

    /**
     * The method get called when the class received a notification event of the
     * given CTLS device and the register method has been enabled.
     * @param handle The service handle identifying the opened CTLS device.
     * @param event Indicates the event defined in class constants.
     */
    public void listener(int handle, int event)
    {
        //
        //  Call your real function to handle event here
        //
        if (null != mOnEventListener)
        {
            mOnEventListener.onEvent(handle, event);
        }
    }
}





