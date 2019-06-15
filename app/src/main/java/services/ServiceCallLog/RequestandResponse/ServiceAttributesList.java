package services.ServiceCallLog.RequestandResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import services.ConfigurationService.RequestAndResponse.ACKeyValuePair;

public class ServiceAttributesList {

    @SerializedName("xmlns:a")
    @Expose
    private String xmlnsA;
    @SerializedName("xmlns:i")
    @Expose
    private String xmlnsI;
    @SerializedName("a:CKeyValuePair")
    @Expose
    private List<ACKeyValuePair> aCKeyValuePair = null;

    public String getXmlnsA() {
        return xmlnsA;
    }

    public void setXmlnsA(String xmlnsA) {
        this.xmlnsA = xmlnsA;
    }

    public String getXmlnsI() {
        return xmlnsI;
    }

    public void setXmlnsI(String xmlnsI) {
        this.xmlnsI = xmlnsI;
    }

    public List<ACKeyValuePair> getACKeyValuePair() {
        return aCKeyValuePair;
    }

    public void setACKeyValuePair(List<ACKeyValuePair> aCKeyValuePair) {
        this.aCKeyValuePair = aCKeyValuePair;
    }

}