package services.UserLoginService.RequestandResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACKeyValuePair_ {

@SerializedName("a:Key")
@Expose
private String aKey;
@SerializedName("a:Value")
@Expose
private String aValue;

public String getAKey() {
return aKey;
}

public void setAKey(String aKey) {
this.aKey = aKey;
}

public String getAValue() {
return aValue;
}

public void setAValue(String aValue) {
this.aValue = aValue;
}

}