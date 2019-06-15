package RTANetworking.RequestAndResponse;

import android.support.design.internal.ParcelableSparseArray;

import java.io.Serializable;

public class KskServiceItem extends ParcelableSparseArray implements Serializable{
    public String ItemId;
    public String ItemText;
    public double ItemPaidAmount;
    public String IsSelected;
    public String PaymentFlag;
    public double MinimumAmount;
    public double MaximumAmount;
    public double Dueamount;
    public double ServiceCharge;
    public String ServiceDate;
    public double DiscountRate;
    public String type;
    public String location;
    public double points;
    public String details;
    public String Field1;
    public String Field2;
    public String Field3;
    public String Field4;
    public String Field5;
    public String Field6;
    public String Field7;
    private boolean setChecked;
    private boolean isChecked;

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemText() {
        return ItemText;
    }

    public void setItemText(String itemText) {
        ItemText = itemText;
    }

    public double getItemPaidAmount() {
        return ItemPaidAmount;
    }

    public void setItemPaidAmount(double itemPaidAmount) {
        ItemPaidAmount = itemPaidAmount;
    }

    public String getIsSelected() {
        return IsSelected;
    }

    public void setIsSelected(String isSelected) {
        IsSelected = isSelected;
    }

    public String getPaymentFlag() {
        return PaymentFlag;
    }

    public void setPaymentFlag(String paymentFlag) {
        PaymentFlag = paymentFlag;
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

    public double getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public String getServiceDate() {
        return ServiceDate;
    }

    public void setServiceDate(String serviceDate) {
        ServiceDate = serviceDate;
    }

    public double getDiscountRate() {
        return DiscountRate;
    }

    public void setDiscountRate(double discountRate) {
        DiscountRate = discountRate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getField1() {
        return Field1;
    }

    public void setField1(String field1) {
        Field1 = field1;
    }

    public String getField2() {
        return Field2;
    }

    public void setField2(String field2) {
        Field2 = field2;
    }

    public String getField3() {
        return Field3;
    }

    public void setField3(String field3) {
        Field3 = field3;
    }

    public String getField4() {
        return Field4;
    }

    public void setField4(String field4) {
        Field4 = field4;
    }

    public String getField5() {
        return Field5;
    }

    public void setField5(String field5) {
        Field5 = field5;
    }

    public String getField6() {
        return Field6;
    }

    public void setField6(String field6) {
        Field6 = field6;
    }

    public String getField7() {
        return Field7;
    }

    public void setField7(String field7) {
        Field7 = field7;
    }

    public boolean isSetChecked() {
        return setChecked;
    }

    public void setSetChecked(boolean setChecked) {
        this.setChecked = setChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}