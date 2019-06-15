package com.xac.demo.Interface;

import android.content.Context;

import rtaservices.RTAFineandInquiryServices.Interfaces.CardValuesListener;

/**
 * Created by simon_chen on 2018/2/14.
 */

public interface PresenterInterface {
    void initPresenter(Context context, ViewInterface viewInterface, CardValuesListener cardValuesListener);
    void onDestroy();
}
