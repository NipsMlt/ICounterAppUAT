package RTANetworking.RequestAndResponse;

import android.support.design.internal.ParcelableSparseArray;

import java.io.Serializable;

/**
 * Created by raheel on 3/3/2018.
 */

public class CKeyValuePair extends ParcelableSparseArray implements Serializable{

    public CKeyValuePair(String key, String value) {

        this.setKey(key);
        this.setValue(value);
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String Key;
    public String Value;
}
