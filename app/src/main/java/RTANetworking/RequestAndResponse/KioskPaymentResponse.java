package RTANetworking.RequestAndResponse;

import java.io.Serializable;

public class KioskPaymentResponse implements Serializable {

    public String ReasonCode;
    public String Message;
    public String Notes;
    public String Status;

    public KioskPaymentResponse.ServiceAttributesList getServiceAttributesList() {
        return ServiceAttributesList;
    }

    public void setServiceAttributesList(KioskPaymentResponse.ServiceAttributesList serviceAttributesList) {
        ServiceAttributesList = serviceAttributesList;
    }

    public ServiceAttributesList ServiceAttributesList;
    public KskOperationType OperationType;

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

    public KskOperationType getOperationType() {
        return OperationType;
    }

    public void setOperationType(KskOperationType operationType) {
        OperationType = operationType;
    }

    public class ServiceAttributesList implements Serializable {

        public CKeyValuePair[] getCKeyValuePair() {
            return CKeyValuePair;
        }

        public void setCKeyValuePair(CKeyValuePair[] CKeyValuePair) {
            this.CKeyValuePair = CKeyValuePair;
        }

        public CKeyValuePair[] CKeyValuePair;
    }


}
