package MerchantDetailsService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantMultipleDetail {

    @SerializedName("MerchantServiceType")
    @Expose
    private String merchantServiceType;
    @SerializedName("ItemDetails")
    @Expose
    private ItemDetails itemDetails;

    public String getMerchantServiceType() {
        return merchantServiceType;
    }

    public void setMerchantServiceType(String merchantServiceType) {
        this.merchantServiceType = merchantServiceType;
    }

    public ItemDetails getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ItemDetails itemDetails) {
        this.itemDetails = itemDetails;
    }

}