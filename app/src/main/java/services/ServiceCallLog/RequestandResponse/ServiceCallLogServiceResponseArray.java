package services.ServiceCallLog.RequestandResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServiceCallLogServiceResponseArray {
    @SerializedName("xmlns")
    @Expose
    private String xmlns;
    @SerializedName("CustomerUniqueNo")
    @Expose
    private CustomerUniqueNoArray customerUniqueNo;
    @SerializedName("ServiceAttributesList")
    @Expose
    private ServiceAttributesList serviceAttributesList;

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public CustomerUniqueNoArray getCustomerUniqueNo() {
        return customerUniqueNo;
    }

    public void setCustomerUniqueNo(CustomerUniqueNoArray customerUniqueNo) {
        this.customerUniqueNo = customerUniqueNo;
    }

    public ServiceAttributesList getServiceAttributesList() {
        return serviceAttributesList;
    }

    public void setServiceAttributesList(ServiceAttributesList serviceAttributesList) {
        this.serviceAttributesList = serviceAttributesList;
    }

}

