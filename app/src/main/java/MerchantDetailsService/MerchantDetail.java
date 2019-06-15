package MerchantDetailsService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MerchantDetail {

    @SerializedName("MerchantMultipleDetails")
    @Expose
    private List<MerchantMultipleDetail> merchantMultipleDetails = null;

    public List<MerchantMultipleDetail> getMerchantMultipleDetails() {
        return merchantMultipleDetails;
    }

    public void setMerchantMultipleDetails(List<MerchantMultipleDetail> merchantMultipleDetails) {
        this.merchantMultipleDetails = merchantMultipleDetails;
    }

}