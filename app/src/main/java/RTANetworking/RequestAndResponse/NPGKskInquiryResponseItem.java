package RTANetworking.RequestAndResponse;

import java.io.Serializable;

/**
 * Created by raheel on 3/25/2018.
 */

public class NPGKskInquiryResponseItem implements Serializable{

    public boolean IsRefundable;
    public String Message;
    public String Notes;
    public String ReasonCode;
    public String ReplayFor;
    public CustomerUniqueNo CustomerUniqueNo;
    public ServiceAttributesList ServiceAttributesList;
    public ServiceType ServiceType;

    public NPGKskInquiryResponseItem.ServiceType getServiceType() {
        return ServiceType;
    }

    public void setServiceType(NPGKskInquiryResponseItem.ServiceType serviceType) {

        ServiceType = serviceType;
    }

    public NPGKskInquiryResponseItem.ServiceAttributesList getServiceAttributesList() {
        return ServiceAttributesList;
    }

    public void setServiceAttributesList(NPGKskInquiryResponseItem.ServiceAttributesList serviceAttributesList) {
        ServiceAttributesList = serviceAttributesList;
    }

    public NPGKskInquiryResponseItem.CustomerUniqueNo getCustomerUniqueNo() {
        return CustomerUniqueNo;
    }

    public void setCustomerUniqueNo(NPGKskInquiryResponseItem.CustomerUniqueNo customerUniqueNo) {
        CustomerUniqueNo = customerUniqueNo;
    }


    public boolean isRefundable() {
        return IsRefundable;
    }

    public void setRefundable(boolean refundable) {
        IsRefundable = refundable;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
    }

    public String getReplayFor() {
        return ReplayFor;
    }

    public void setReplayFor(String replayFor) {
        ReplayFor = replayFor;
    }

    public class CustomerUniqueNo implements Serializable{

        public CKeyValuePair[] getCKeyValuePair() {
            return CKeyValuePair;
        }

        public void setCKeyValuePair(CKeyValuePair[] CKeyValuePair) {
            this.CKeyValuePair = CKeyValuePair;
        }

        public CKeyValuePair[] CKeyValuePair;

    }

    public class ServiceAttributesList implements Serializable{

        public CKeyValuePair[] getCKeyValuePair() {
            return CKeyValuePair;
        }

        public void setCKeyValuePair(CKeyValuePair[] CKeyValuePair) {
            this.CKeyValuePair = CKeyValuePair;
        }

        public CKeyValuePair[] CKeyValuePair;
    }

    public class ServiceType implements Serializable
    {

        public String getServiceTypeId() {
            return ServiceTypeId;
        }

        public void setServiceTypeId(String serviceTypeId) {
            ServiceTypeId = serviceTypeId;
        }

        public String getServiceTitle() {
            return ServiceTitle;
        }

        public void setServiceTitle(String serviceTitle) {
            ServiceTitle = serviceTitle;
        }

        public double getMinimumAmount() {
            return MinimumAmount;
        }

        public void setMinimumAmount(double minimumAmount) {
            MinimumAmount = minimumAmount;
        }

        public double getMaximumAmount() {
            return MaximumAmount;
        }

        public void setMaximumAmount(double maximumAmount) {
            MaximumAmount = maximumAmount;
        }

        public double getDueamount() {
            return Dueamount;
        }

        public void setDueamount(double dueamount) {
            Dueamount = dueamount;
        }

        public double getPaidAmount() {
            return PaidAmount;
        }

        public void setPaidAmount(double paidAmount) {
            PaidAmount = paidAmount;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public double getServiceCharge() {
            return ServiceCharge;
        }

        public void setServiceCharge(double serviceCharge) {
            ServiceCharge = serviceCharge;
        }

        public KskServiceItem getItems() {
            return Items;
        }

        public void setItems(KskServiceItem items) {
            Items = items;
        }

        public String ServiceTypeId;
        public String ServiceTitle;
        public double MinimumAmount;
        public double MaximumAmount;
        public double Dueamount;
        public double PaidAmount;
        public String Comment;
        public double ServiceCharge;
        public KskServiceItem Items;

    }
}
