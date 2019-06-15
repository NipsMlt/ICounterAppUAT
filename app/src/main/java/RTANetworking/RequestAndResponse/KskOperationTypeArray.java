package RTANetworking.RequestAndResponse;

import java.io.Serializable;

public class KskOperationTypeArray implements Serializable {

    public String OperationTypeId;
    public String operationTitle;
    public double MinimumAmount;
    public double MaximumAmount;
    public double Dueamount;
    public double PaidAmount;
    public double ServiceCharge;
    public KskServiceTypeResponseArray ServicesType;

    public String getOperationTypeId() {
        return OperationTypeId;
    }

    public void setOperationTypeId(String operationTypeId) {
        OperationTypeId = operationTypeId;
    }

    public String getOperationTitle() {
        return operationTitle;
    }

    public void setOperationTitle(String operationTitle) {
        this.operationTitle = operationTitle;
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

    public double getServiceCharge() {
        return ServiceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        ServiceCharge = serviceCharge;
    }

    public KskServiceTypeResponseArray getServicesType() {
        return ServicesType;
    }

    public void setServicesType(KskServiceTypeResponseArray servicesType) {
        ServicesType = servicesType;
    }

}
