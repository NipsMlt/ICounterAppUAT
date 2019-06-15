package RTANetworking.RequestAndResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NPGKInquiryResponseES implements Serializable {

    @SerializedName("xmlns")
    @Expose
    private String xmlns;
    @SerializedName("ReasonCode")
    @Expose
    private String reasonCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("CustomerUniqueNo")
    @Expose
    private CustomerUniqueNo customerUniqueNo;
    @SerializedName("ServiceAttributesList")
    @Expose
    private ServiceAttributesList serviceAttributesList;
    @SerializedName("ServiceType")
    @Expose
    private ServiceTypeES serviceType;
    @SerializedName("IsRefundable")
    @Expose
    private Boolean isRefundable;
    @SerializedName("ReplayFor")
    @Expose
    private String replayFor;

    public class CustomerUniqueNo implements Serializable {

        @SerializedName("CKeyValuePair")
        @Expose
        private CKeyValuePairES cKeyValuePair;

        public CKeyValuePairES getCKeyValuePair() {
            return cKeyValuePair;
        }

        public void setCKeyValuePair(CKeyValuePairES cKeyValuePair) {
            this.cKeyValuePair = cKeyValuePair;
        }

    }

    public class ServiceAttributesList implements Serializable{

        @SerializedName("CKeyValuePair")
        @Expose
        private CKeyValuePairES_ cKeyValuePair;

        public CKeyValuePairES_ getCKeyValuePair() {
            return cKeyValuePair;
        }

        public void setCKeyValuePair(CKeyValuePairES_ cKeyValuePair) {
            this.cKeyValuePair = cKeyValuePair;
        }

    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CustomerUniqueNo getCustomerUniqueNo() {
        return customerUniqueNo;
    }

    public void setCustomerUniqueNo(CustomerUniqueNo customerUniqueNo) {
        this.customerUniqueNo = customerUniqueNo;
    }

    public ServiceAttributesList getServiceAttributesList() {
        return serviceAttributesList;
    }

    public void setServiceAttributesList(ServiceAttributesList serviceAttributesList) {
        this.serviceAttributesList = serviceAttributesList;
    }

    public ServiceTypeES getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceTypeES serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getIsRefundable() {
        return isRefundable;
    }

    public void setIsRefundable(Boolean isRefundable) {
        this.isRefundable = isRefundable;
    }

    public String getReplayFor() {
        return replayFor;
    }

    public void setReplayFor(String replayFor) {
        this.replayFor = replayFor;
    }

    public class ServiceTypeES implements Serializable{

        @SerializedName("ServiceTypeId")
        @Expose
        private Integer serviceTypeId;
        @SerializedName("ServiceTitle")
        @Expose
        private String serviceTitle;
        @SerializedName("Comment")
        @Expose
        private String comment;
        @SerializedName("MinimumAmount")
        @Expose
        private Integer minimumAmount;
        @SerializedName("MaximumAmount")
        @Expose
        private Integer maximumAmount;
        @SerializedName("Dueamount")
        @Expose
        private Integer dueamount;
        @SerializedName("PaidAmount")
        @Expose
        private Integer paidAmount;
        @SerializedName("ServiceCharge")
        @Expose
        private Integer serviceCharge;
        @SerializedName("Items")
        @Expose
        private List<KskServiceItem> items = null;

        public Integer getServiceTypeId() {
            return serviceTypeId;
        }

        public void setServiceTypeId(Integer serviceTypeId) {
            this.serviceTypeId = serviceTypeId;
        }

        public String getServiceTitle() {
            return serviceTitle;
        }

        public void setServiceTitle(String serviceTitle) {
            this.serviceTitle = serviceTitle;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Integer getMinimumAmount() {
            return minimumAmount;
        }

        public void setMinimumAmount(Integer minimumAmount) {
            this.minimumAmount = minimumAmount;
        }

        public Integer getMaximumAmount() {
            return maximumAmount;
        }

        public void setMaximumAmount(Integer maximumAmount) {
            this.maximumAmount = maximumAmount;
        }

        public Integer getDueamount() {
            return dueamount;
        }

        public void setDueamount(Integer dueamount) {
            this.dueamount = dueamount;
        }

        public Integer getPaidAmount() {
            return paidAmount;
        }

        public void setPaidAmount(Integer paidAmount) {
            this.paidAmount = paidAmount;
        }

        public Integer getServiceCharge() {
            return serviceCharge;
        }

        public void setServiceCharge(Integer serviceCharge) {
            this.serviceCharge = serviceCharge;
        }

        public List<KskServiceItem> getItems() {
            return items;
        }

        public void setItems(List<KskServiceItem> items) {
            this.items = items;
        }

    }

}
