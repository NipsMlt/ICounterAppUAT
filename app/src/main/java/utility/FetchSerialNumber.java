package utility;

import com.xac.demo.Presenter.BasePresenterViewWrapper;
import com.xac.helper.VNG;

public class FetchSerialNumber extends BasePresenterViewWrapper {

    public String getSerialNumber() {

        initCommPort();

        String serialNo = "";

        commPort.connect();
        VNG rsp = exchangeData(new VNG("R0"));
        if (rsp != null) {
            if (rsp.parseString(2).equals("R0")) {

                String serial = rsp.parseString(48);
                serialNo = serial.substring(serial.indexOf("E"));
            }
        }
        commPort.disconnect();

        return serialNo;
    }
}
