package rtamain;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import example.dtc.R;
import loginfragments.SettingsLoginFragment;
import rtaservices.RTAMainServices;
import saioapi.service.SystemUIService.SystemUIService;
import utility.Constant;
import utility.PreferenceConnector;

import static utility.Common.getYear;


public class RTAlanguages extends Fragment implements View.OnClickListener {

    private Fragment mFragment;
    private ImageView iv_langenglish, iv_langarabic, iv_langurdu, iv_langmalyalam, iv_langChinese;
    private Button btn_Hidden;
    Locale myLocale;
    TextView tv_Copyright, tv_copyright_Symbol, tv_copyright_Year, tv_copyright_mlt_Allrightsreserved, tv_copyright_Version;
    int count = 0;
    RTAMain rtaMain = new RTAMain();

    public RTAlanguages() {
        // Required empty public constructor
    }

    @NonNull
    public static RTAlanguages newInstance() {
        return new RTAlanguages();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.rtalanguages, container, false);
        Resources resources = getContext().getResources();

        iv_langenglish = (ImageView) view.findViewById(R.id.iv_langenglish);
        iv_langarabic = (ImageView) view.findViewById(R.id.iv_langarabic);
        iv_langurdu = (ImageView) view.findViewById(R.id.iv_langurdu);
        iv_langmalyalam = (ImageView) view.findViewById(R.id.iv_langmalyalam);
        iv_langChinese = (ImageView) view.findViewById(R.id.iv_langchinese);
        btn_Hidden = (Button) view.findViewById(R.id.btn_hidden);
        tv_Copyright = (TextView) view.findViewById(R.id.tv_copyright);
        tv_copyright_Symbol = (TextView) view.findViewById(R.id.tv_copyright_symbol);
        tv_copyright_Year = (TextView) view.findViewById(R.id.tv_copyright_year);
        tv_copyright_mlt_Allrightsreserved = (TextView) view.findViewById(R.id.tv_copyright_mlt_allrightsreserved);
        tv_copyright_Version = (TextView) view.findViewById(R.id.tv_copyright_version);

        iv_langenglish.setOnClickListener(this);
        iv_langarabic.setOnClickListener(this);
        iv_langurdu.setOnClickListener(this);
        iv_langmalyalam.setOnClickListener(this);
        iv_langChinese.setOnClickListener(this);
        btn_Hidden.setOnClickListener(this);

        tv_Copyright.setText(resources.getString(R.string.Copyright)+" ");
        tv_copyright_Symbol.setText(resources.getString(R.string.CopyrightSymbol));
        tv_copyright_Year.setText(String.valueOf(getYear())+" ");
        tv_copyright_mlt_Allrightsreserved.setText(resources.getString(R.string.MLTAllRightsReserved)+ " ");
        tv_copyright_Version.setText(resources.getString(R.string.Version));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_langenglish:
                iv_langenglish.setImageResource(R.drawable.englishbtnactive);
                iv_langarabic.setImageResource(R.drawable.arabicbtnpassive);
                iv_langurdu.setImageResource(R.drawable.pakistanibtnpassive);
                iv_langmalyalam.setImageResource(R.drawable.malayalambtnpassive);
                iv_langChinese.setImageResource(R.drawable.chinesebtnpassive);
                PreferenceConnector.writeString(getContext(), Constant.Language, Constant.LanguageEnglish);
                mFragment = RTAMainServices.newInstance();
                addFragment();
                break;
            case R.id.iv_langarabic:
                iv_langenglish.setImageResource(R.drawable.englishbtnpassive);
                iv_langarabic.setImageResource(R.drawable.arabicbtnactive);
                iv_langurdu.setImageResource(R.drawable.pakistanibtnpassive);
                iv_langmalyalam.setImageResource(R.drawable.malayalambtnpassive);
                iv_langChinese.setImageResource(R.drawable.chinesebtnpassive);
                PreferenceConnector.writeString(getContext(), Constant.Language, Constant.LanguageArabic);
                mFragment = RTAMainServices.newInstance();
                addFragment();
                break;
            case R.id.iv_langurdu:
                iv_langenglish.setImageResource(R.drawable.englishbtnpassive);
                iv_langarabic.setImageResource(R.drawable.arabicbtnpassive);
                iv_langurdu.setImageResource(R.drawable.pakistanibtnactive);
                iv_langmalyalam.setImageResource(R.drawable.malayalambtnpassive);
                iv_langChinese.setImageResource(R.drawable.chinesebtnpassive);
                PreferenceConnector.writeString(getContext(), Constant.Language, Constant.LanguageUrdu);
                mFragment = RTAMainServices.newInstance();
                addFragment();
                //Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_langmalyalam:
                iv_langenglish.setImageResource(R.drawable.englishbtnpassive);
                iv_langarabic.setImageResource(R.drawable.arabicbtnpassive);
                iv_langurdu.setImageResource(R.drawable.pakistanibtnpassive);
                iv_langmalyalam.setImageResource(R.drawable.malayalambtnactive);
                iv_langChinese.setImageResource(R.drawable.chinesebtnpassive);
                PreferenceConnector.writeString(getContext(), Constant.Language, Constant.LanguageMalayalam);
                mFragment = RTAMainServices.newInstance();
                addFragment();
                // Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_langchinese:
                iv_langenglish.setImageResource(R.drawable.englishbtnpassive);
                iv_langarabic.setImageResource(R.drawable.arabicbtnpassive);
                iv_langurdu.setImageResource(R.drawable.pakistanibtnpassive);
                iv_langmalyalam.setImageResource(R.drawable.malayalambtnactive);
                iv_langChinese.setImageResource(R.drawable.chinesebtnactive);
                PreferenceConnector.writeString(getContext(), Constant.Language, Constant.LanguageChinese);
                mFragment = RTAMainServices.newInstance();
                addFragment();
                // Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hidden:
                count++;
                if (count == 20) {
                    mFragment = SettingsLoginFragment.newInstance();
                    addFragment();
                   /* //getActivity().finish();
                    SystemUIService.setNaviButtonVisibility(getContext(), SystemUIService.NAVIBUTTON_NAVIBAR, View.VISIBLE);
                    //It exits to the installer where the apk gets installed
                    Intent intent = new Intent();
                    intent.setClassName("com.xac.util.saioutility", "com.xac.util.saioutility.Main");
                    startActivity(intent);*/
                }
                break;
            default:
                break;
        }
    }

    public void HideNavigationBar() {
        services.SystemUIService.setNaviButtonVisibility(getContext(), services.SystemUIService.NAVIBUTTON_NAVIBAR, View.GONE);
        services.SystemUIService.setStatusBarVisibility(getContext(), View.GONE);
    }

    public void addFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        count = 0;
        HideNavigationBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        count = 0;
        HideNavigationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        count = 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
    }
}
