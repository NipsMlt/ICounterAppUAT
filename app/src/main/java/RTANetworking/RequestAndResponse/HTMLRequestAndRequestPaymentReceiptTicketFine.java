package RTANetworking.RequestAndResponse;

import android.util.Log;

public class HTMLRequestAndRequestPaymentReceiptTicketFine {

    public String PaymentReceiptTicketFine(SendEmailReceiptRequest request) {

        String htmlRequest = null;

        try {
            htmlRequest = String.format("<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "    <meta charset=\"utf-8\" />\n" +
                            "    <title>Receipt</title>\n" +
                            "</head>\n" +
                            "<body style=\"background-image:url(~/Content/Images/nipslogo.png)\">\n" +
                            "    <div id=\"htmlTable\" style=\"font-family: Noto Sans Arabic; font-size:medium\">\n" +
                            "        <div id=\"Grid\" style=\"position:relative\">\n" +
                            "            <table width=\"100%\" cellpadding=\"5px\" id=\"htmlTable2\">\n" +
                            "                <tr style=\"background-color: lightgrey; color:  ;\">\n" +
                            "                    <td align=\"left\" width=\"300px\"><b>Receipt</b></td>\n" +
                            "                    <td align=\"center\" width=\"300px\"></td>\n" +
                            "                    <td align=\"right\" width=\"300px\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>إيصال</b></td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td align=\"left\">Date</td>\n" +
                            "                    <td align=\"center\">(Date)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">التاريخ</td>\n" +
                            "                </tr>\n" +
                            "                <tr style=\"background-color: ghostwhite;\">\n" +
                            "                    <td align=\"left\">Receipt Number</td>\n" +
                            "                    <td align=\"center\">(ReceiptNumber)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رقم الوصل</td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td align=\"left\">Kiosk Number</td>\n" +
                            "                    <td align=\"center\">(KioskNumber)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رقم الكشك</td>\n" +
                            "                </tr>\n" +
                            "                <tr style=\"background-color: ghostwhite;\">\n" +
                            "                    <td align=\"left\">Kiosk Location</td>\n" +
                            "                    <td align=\"center\">(KioskLocation)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">موقع الكشك</td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td align=\"left\">Service Provider</td>\n" +
                            "                    <td align=\"center\">(ServiceProvider)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">مقدم الخدمة</td>\n" +
                            "                </tr>\n" +
                            "                <tr style=\"background-color: ghostwhite;\">\n" +
                            "                    <td align=\"left\">Service Name</td>\n" +
                            "                    <td align=\"center\">(ServiceName)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">اسم الخدمة</td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "                <tbody id=\"ServicesSeasonal\">\n" +
                            "                    <tr><td colspan=\"3\">&nbsp;</td></tr>\n" +
                            "\n" +
                            "                    <tr style=\" background-color: lightgrey; color: black;\">\n" +
                            "                        <td align=\"left\"><b>Service Details</b></td>\n" +
                            "                        <td align=\"center\"></td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>تفاصيل الخدمة</b></td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"DeliveryType\">\n" +
                            "                        <td align=\"left\">Delivery Type</td>\n" +
                            "                        <td align=\"center\">(DeliveryType)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">طريقة التوصيل</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"CardNo\" style=\"background-color:ghostwhite\">\n" +
                            "                        <td align=\"left\">Card Number</td>\n" +
                            "                        <td align=\"center\">(CardNo)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رقم البطاقة</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"CardType\">\n" +
                            "                        <td align=\"left\">Card Type</td>\n" +
                            "                        <td align=\"center\">(CardType)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">نوع البطاقة</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"IssueDate\" style=\"background-color:ghostwhite\">\n" +
                            "                        <td align=\"left\">Issue Date</td>\n" +
                            "                        <td align=\"center\">(IssueDate)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">تاريخ الاصدار</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"ExpiryDate\">\n" +
                            "                        <td align=\"left\">Expiry Date</td>\n" +
                            "                        <td align=\"center\">(ExpiryDate)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">تاريخ الانتهاء</td>\n" +
                            "                    </tr>\n" +
                            "                </tbody>\n" +
                            "\n" +
                            "                <tbody id=\"ServicesDriving\">\n" +
                            "                    <tr><td colspan=\"3\">&nbsp;</td></tr>\n" +
                            "\n" +
                            "                    <tr style=\" background-color: lightgrey; color: black;\">\n" +
                            "                        <td align=\"left\"><b>Service Details</b></td>\n" +
                            "                        <td align=\"center\"></td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>تفاصيل الخدمة</b></td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"DeliveryType\">\n" +
                            "                        <td align=\"left\">Delivery Type</td>\n" +
                            "                        <td align=\"center\">(DeliveryType)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">طريقة التوصيل</td>\n" +
                            "                    </tr>\n" +
                            "\n" +
                            "                    <tr id=\"TrafficFileNo\" style=\"background-color: ghostwhite;\">\n" +
                            "                        <td align=\"left\">Traffic File Number</td>\n" +
                            "                        <td align=\"center\">(TrafficFileNo)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">الرمز المروري</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"DriverLicNo\">\n" +
                            "                        <td align=\"left\">Driver License Number</td>\n" +
                            "                        <td align=\"center\">(DriverLicNo)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رقم رخصة القيادة</td>\n" +
                            "                    </tr>\n" +
                            "                </tbody>\n" +
                            "\n" +
                            "                <tbody id=\"ServicesVehicle\">\n" +
                            "                    <tr><td colspan=\"3\">&nbsp;</td></tr>\n" +
                            "\n" +
                            "                    <tr style=\" background-color: lightgrey; color: black;\">\n" +
                            "                        <td align=\"left\"><b>Service Details</b></td>\n" +
                            "                        <td align=\"center\"></td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>تفاصيل الخدمة</b></td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"DeliveryType\">\n" +
                            "                        <td align=\"left\">Delivery Type</td>\n" +
                            "                        <td align=\"center\">(DeliveryType)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">طريقة التوصيل</td>\n" +
                            "                    </tr>\n" +
                            "\n" +
                            "                    <tr id=\"TrafficFileNo\" style=\"background-color: ghostwhite;\">\n" +
                            "                        <td align=\"left\">Traffic File Number</td>\n" +
                            "                        <td align=\"center\">(TrafficFileNo)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">الرمز المروري</td>\n" +
                            "                    </tr>\n" +
                            "                    <tr id=\"PlateNumber\">\n" +
                            "                        <td align=\"left\">Plate Number</td>\n" +
                            "                        <td align=\"center\">(PlateNumber)</td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رقم اللوحة</td>\n" +
                            "                    </tr>\n" +
                            "                </tbody>\n" +
                            "\n" +
                            "                <tbody id=\"ServicesTrafficFine\">\n" +
                            "                    <tr><td colspan=\"3\">&nbsp;</td></tr>\n" +
                            "\n" +
                            "                    <tr style=\" background-color: lightgrey; color: black;\">\n" +
                            "                        <td align=\"left\"><b>Service Details</b></td>\n" +
                            "                        <td align=\"center\"></td>\n" +
                            "                        <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>تفاصيل الخدمة</b></td>\n" +
                            "                    </tr>\n" +
                            "                    <tr>\n" +
                            "                        <td align=\"center\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic;\"><b>Ticket No. / رقم المخالفة</b></td>\n" +
                            "                        <td align=\"center\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>Plate No. / رقم اللوحة</b></td>\n" +
                            "                        <td align=\"center\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>Fine Amount / قيمة المخالفة</b></td>\n" +
                            "                    </tr>\n" +
                            "                </tbody>\n" +
                            "\n" +
                            "                <tr><td>&nbsp;</td></tr>\n" +
                            "                <tr style=\" background-color: lightgrey;\">\n" +
                            "                    <td align=\"left\"><b>Payment Details</b></td>\n" +
                            "                    <td align=\"center\"></td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>بيانات الدفع</b></td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td align=\"left\">Payment Type</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.PaymentType)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">طريقة الدفع</td>\n" +
                            "                </tr>\n" +
                            "                <tr style=\"background-color: ghostwhite;\">\n" +
                            "                    <td align=\"left\">Transaction Amount</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.TransactionAmount)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">المبلغ الإجمالي</td>\n" +
                            "                </tr>\n" +
                            "                <tr id=\"ServiceCharges\">\n" +
                            "                    <td align=\"left\">Service Charges</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.ServiceCharges)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رسوم الخدمات</td>\n" +
                            "                </tr>\n" +
                            "                <tr (ghostwhite)>\n" +
                            "                    <td align=\"left\">Total Amount</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.TotalAmount)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">المبلغ الإجمالي</td>\n" +
                            "                </tr>\n" +
                            "                <tr (white)>\n" +
                            "                    <td align=\"left\">Received Amount</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.ReceivedAmount)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">المبلغ الذي تسلمه</td>\n" +
                            "                </tr>\n" +
                            "                <tr id=\"AmountToPayBack\" (ghostwhite)>\n" +
                            "                    <td align=\"left\">Amount to Pay Back</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.AmountToPayBack)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">المبلغ لتسديده</td>\n" +
                            "                </tr>\n" +
                            "                <tr id=\"RefundedMoneyByKiosk\" (white)>\n" +
                            "                    <td align=\"left\">Refunded Money by Kiosk</td>\n" +
                            "                    <td align=\"center\">(PaymentDetails.RefundedMoneyByKiosk)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">رد الأموال بواسطة الكشك</td>\n" +
                            "                </tr>\n" +
                            "                <tr id=\"PrintingStatus\" (ghostwhite)>\n" +
                            "                    <td align=\"left\">Print Status</td>\n" +
                            "                    <td align=\"center\">(PrintingInfo)</td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\">حالة الطباعة</td>\n" +
                            "                </tr>\n" +
                            "\n" +
                            "                <tr><td>&nbsp;</td></tr>\n" +
                            "                <tr>\n" +
                            "                    <td colspan=\"2\"><b>Thank You For Using Our Kiosk Network.</b></td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>شكراً لإستخدام شبكة الأكشاك الذكية</b></td>\n" +
                            "                </tr>\n" +
                            "                <tr><td>&nbsp;</td></tr>\n" +
                            "                <tr>\n" +
                            "                    <td colspan=\"2\"><b>For Any Inquiries Please Contact:</b></td>\n" +
                            "                    <td align=\"right\" dir=\"ltr\" style=\"font-family: Noto Sans Arabic\"><b>لأي استفسار يرجى الإتصال</b></td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td></td>\n" +
                            "                    <td align=\"center\">600 54 55 56</td>\n" +
                            "                    <td></td>\n" +
                            "                </tr>\n" +
                            "                <tr>\n" +
                            "                    <td></td>\n" +
                            "                    <td align=\"center\">Email: support@mlt-uae.ae</td>\n" +
                            "                    <td></td>\n" +
                            "                </tr>\n" +
                            "            </table>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>",
                    request.getDate(),
                    request.getReceiptNumber(),
                    request.getKioskNumber(),
                    request.getKioskLocation(),
                    request.getServiceProvider(),
                    request.getServiceName(),
                    request.getPaymentType(),
                    request.getTotalFineAmount(),
                    request.getTotalAmount(),
                    request.getReceivedAmount());

        } catch (Exception ex) {
            Log.d("The Exception is", ex.getLocalizedMessage());
        }
        return htmlRequest;
    }
}
