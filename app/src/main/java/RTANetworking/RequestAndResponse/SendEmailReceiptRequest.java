package RTANetworking.RequestAndResponse;

public class SendEmailReceiptRequest {

    // Date
    private String Date;

    // Receipt Number
    private String ReceiptNumber;

    // Kiosk Number
    private String KioskNumber;

    // Kiosk Location
    private String KioskLocation;

    // Service Provider
    private String ServiceProvider;

    // Service Name
    private String ServiceName;

    // Ticket No.
    private String TicketNo;

    // Plate No.
    private String PlateNo;

    // Fine Amount
    private String FineAmount;

    // Payment Type
    private String PaymentType;

    // Total Fine Amount
    private String TotalFineAmount;

    // Total Amount
    private String TotalAmount;

    // Received Amount
    private String ReceivedAmount;


    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getReceiptNumber() {
        return ReceiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        ReceiptNumber = receiptNumber;
    }

    public String getKioskNumber() {
        return KioskNumber;
    }

    public void setKioskNumber(String kioskNumber) {
        KioskNumber = kioskNumber;
    }

    public String getKioskLocation() {
        return KioskLocation;
    }

    public void setKioskLocation(String kioskLocation) {
        KioskLocation = kioskLocation;
    }

    public String getServiceProvider() {
        return ServiceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        ServiceProvider = serviceProvider;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getTicketNo() {
        return TicketNo;
    }

    public void setTicketNo(String ticketNo) {
        TicketNo = ticketNo;
    }

    public String getPlateNo() {
        return PlateNo;
    }

    public void setPlateNo(String plateNo) {
        PlateNo = plateNo;
    }

    public String getFineAmount() {
        return FineAmount;
    }

    public void setFineAmount(String fineAmount) {
        FineAmount = fineAmount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getTotalFineAmount() {
        return TotalFineAmount;
    }

    public void setTotalFineAmount(String totalFineAmount) {
        TotalFineAmount = totalFineAmount;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getReceivedAmount() {
        return ReceivedAmount;
    }

    public void setReceivedAmount(String receivedAmount) {
        ReceivedAmount = receivedAmount;
    }
}
