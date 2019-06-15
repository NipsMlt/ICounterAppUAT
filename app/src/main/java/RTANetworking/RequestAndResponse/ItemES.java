package RTANetworking.RequestAndResponse;

import android.support.design.internal.ParcelableSparseArray;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemES extends ParcelableSparseArray implements Serializable {

    @SerializedName("ItemId")
    @Expose
    private String itemId;
    @SerializedName("ItemText")
    @Expose
    private Integer itemText;
    @SerializedName("ItemPaidAmount")
    @Expose
    private Integer itemPaidAmount;
    @SerializedName("IsSelected")
    @Expose
    private String isSelected;
    @SerializedName("ServiceDate")
    @Expose
    private String serviceDate;
    @SerializedName("DiscountRate")
    @Expose
    private Integer discountRate;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("Field1")
    @Expose
    private String field1;
    @SerializedName("Field2")
    @Expose
    private String field2;
    @SerializedName("Field3")
    @Expose
    private String field3;
    @SerializedName("Field4")
    @Expose
    private String field4;
    @SerializedName("Field5")
    @Expose
    private String field5;
    @SerializedName("Field6")
    @Expose
    private String field6;
    @SerializedName("Field7")
    @Expose
    private String field7;
    @SerializedName("PaymentFlag")
    @Expose
    private String paymentFlag;
    @SerializedName("MinimumAmount")
    @Expose
    private Integer minimumAmount;
    @SerializedName("MaximumAmount")
    @Expose
    private Integer maximumAmount;
    @SerializedName("Dueamount")
    @Expose
    private Integer dueamount;
    @SerializedName("ServiceCharge")
    @Expose
    private Integer serviceCharge;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemText() {
        return itemText;
    }

    public void setItemText(Integer itemText) {
        this.itemText = itemText;
    }

    public Integer getItemPaidAmount() {
        return itemPaidAmount;
    }

    public void setItemPaidAmount(Integer itemPaidAmount) {
        this.itemPaidAmount = itemPaidAmount;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(String paymentFlag) {
        this.paymentFlag = paymentFlag;
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

    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

}
