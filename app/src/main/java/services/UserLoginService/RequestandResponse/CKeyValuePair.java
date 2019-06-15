package services.UserLoginService.RequestandResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Talal on 6/30/2018.
 */

public class CKeyValuePair implements Serializable {

    public CKeyValuePair(String key, String value) {

        this.setKey(key);
        this.setValue(value);
    }

    @SerializedName("Key")
    @Expose
    private String key;
    @SerializedName("Value")
    @Expose
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}