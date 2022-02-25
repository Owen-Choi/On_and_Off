package org.techtown.sns_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;

import org.techtown.sns_project.Enterprise.Setting.EnterpriseMemberInfo;
import org.techtown.sns_project.Enterprise.Setting.EnterpriseSettingActivity;
import org.techtown.sns_project.Normal.Setting.NormalMemberInfo;


public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/Owen-Choi/ToyProject_SNS"));
        Preference mypref = (Preference)findPreference("contact_preference");
        Preference memberInfo = (Preference)findPreference("memberInfo");
//        SeekBarPreference sp = findPreference("volume_notifications");
//        sp.getValue();
        mypref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(i);
                return true;
            }
        });

        // 두가지 방법이 있다. 일반 회원인지 기업 회원인지 판단한 후 화면을 다르게 띄워주거나
        // 아예 클래스를 분리해서 기업용 클래스를 하나 더 만들거나.
        memberInfo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                StartActivity(NormalMemberInfo.class);
                return true;
            }
        });
    }

    private void StartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivity(intent);
    }


}