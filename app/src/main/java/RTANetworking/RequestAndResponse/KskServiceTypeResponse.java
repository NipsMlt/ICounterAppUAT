package RTANetworking.RequestAndResponse;

import android.support.design.internal.ParcelableSparseArray;

import java.io.Serializable;

public class KskServiceTypeResponse extends ParcelableSparseArray implements Serializable {

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

    public String getReceiptNumber() {
        return ReceiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        ReceiptNumber = receiptNumber;
    }

    public String getMerchantRefernceNumber() {
        return MerchantRefernceNumber;
    }

    public void setMerchantRefernceNumber(String merchantRefernceNumber) {
        MerchantRefernceNumber = merchantRefernceNumber;
    }

    public String getBankTransactionId() {
        return BankTransactionId;
    }

    public void setBankTransactionId(String bankTransactionId) {
        BankTransactionId = bankTransactionId;
    }

    public String getReasonCode() {
        return ReasonCode;
    }

    public void setReasonCode(String reasonCode) {
        ReasonCode = reasonCode;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
    public String ReceiptNumber;
    public String MerchantRefernceNumber;
    public String BankTransactionId;
    public String ReasonCode;
    public String Message;
    public String Notes;
    public String Status;
    private KskServiceItem Items;
}
